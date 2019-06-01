/*
 *  Copyright 2015-2018 DataVens, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package venus.mvc;

import org.apache.log4j.Logger;
import venus.core.Context;
import venus.ioc.Beans;
import venus.mvc.annotation.RequestChain;
import venus.mvc.annotation.RequestHandlerType;
import venus.mvc.annotation.RequestMapping;
import venus.mvc.annotation.RequestMethod;
import venus.mvc.bean.RequestHandlerWrapper;
import venus.mvc.handler.RequestHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p> MVC util </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-31 12:24
 */
public class Mvcs {

    private static Logger logger = Logger.getLogger(Mvcs.class);

    /**
     * build RequestPath instance by http request
     * 1. how to splice base path and method path?
     *
     * @param context
     * @return
     */
    public static RequestPath buildPath(Context context){
        if (context!=null && context instanceof MvcContext){
            String path = ((MvcContext)context).getRequest().getPathInfo();
            if (path!=null && path.endsWith("/")){
                path = path.substring(0, path.length()-1);
            }
            String httpMethod = ((MvcContext)context).getRequest().getMethod();
            RequestPath requestPath = new RequestPath(httpMethod, path);

            // TODO: 19/6/1 splice base path and method path

            if (requestPath!=null){
                ((MvcContext)context).setRequestPath(requestPath);
            }
            return requestPath;
        }else {
            return null;
        }
    }

    /**
     * build the handlers by the method
     * 1. common handler and the handler by RequestChain annotation
     * 2. handler order by annotation field specified by order
     *
     * @param method
     * @return
     */
    public static List<Object> loadHandlers(List<RequestHandlerWrapper> allHandlers , Method method){
        if (method==null) {
            return null;
        }
        List<Object> handlers = allHandlers.stream().filter(wrapper -> _filterHandler(wrapper, method))
                .sorted(Comparator.comparing(wrapper -> wrapper.getOrder())).collect(Collectors.toList());
        return handlers;
    }

    private static boolean _filterHandler(RequestHandlerWrapper wrapper, Method method){
        String[] chainValue = method.getAnnotation(RequestChain.class).value();
        for (String handlerName : chainValue) {
            if (wrapper.getValue().equalsIgnoreCase(handlerName) || wrapper.getType().equals(String.valueOf(RequestHandlerType.COMMON))) {
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    public static List<RequestHandlerWrapper> loadAllHandler(){
        List<RequestHandlerWrapper> allHandlers = new ArrayList<>();
        Beans.of().loadClassByAnnotation(venus.mvc.annotation.RequestHandler.class).stream().
                forEach(clz -> {
                    Object obj = Beans.of().getBean(clz);
                    String handlerName = clz.getAnnotation(venus.mvc.annotation.RequestHandler.class).value();
                    RequestHandlerType handlerType = clz.getAnnotation(venus.mvc.annotation.RequestHandler.class).type();

                    if (obj!=null && obj instanceof RequestHandler && !"".equals(handlerName)){
                        final RequestHandlerWrapper requestHandlerWrapper = new RequestHandlerWrapper();
                        requestHandlerWrapper.setValue(handlerName);
                        requestHandlerWrapper.setType(String.valueOf(handlerType));
                        requestHandlerWrapper.setOrder(clz.getAnnotation(venus.mvc.annotation.RequestHandler.class).order());
                        requestHandlerWrapper.setRequestHandler(obj);
                        allHandlers.add(requestHandlerWrapper);
                    }
                });
        return allHandlers;
    }

    /**
     * fetch method by request, strictly follow by:
     * 1. request path
     * 2. http method
     *
     * @param context
     * @return
     */
    public static Method request2Method(Context context){
        RequestPath requestPath = buildPath(context);
        List<Method> methods = new ArrayList<>();
        Beans.of().loadClassByAnnotation(RequestMapping.class).stream().forEach(clz -> {
            String basePath = clz.getAnnotation(RequestMapping.class).value();
            if (basePath != null && !basePath.startsWith("/")){
                basePath = "/" + basePath;
            }
            for (Method method : clz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    String methodPath = method.getAnnotation(RequestMapping.class).value();
                    if (methodPath != null && !methodPath.startsWith("/")) {
                        methodPath = "/" + methodPath;
                    }
                    if (methodPath != null && methodPath.endsWith("/")) {
                        methodPath = methodPath.substring(0, methodPath.length() - 1);
                    }
                    String path = basePath + methodPath;
                    RequestMethod httpMethod = method.getAnnotation(RequestMapping.class).method();
                    if (path.equals(requestPath.getPath()) && requestPath.getHttpMethod().equals(String.valueOf(httpMethod)) ){
                        methods.add(method);
                    }
                }
            }
        });

        if (methods!=null && methods.size()>1){
            logger.warn("Multi method corresponding to a http request.");
            return methods.get(0);
        }
        if (methods !=null && methods.size()==1 ){
            return methods.get(0);
        }
        return null;
    }


}
