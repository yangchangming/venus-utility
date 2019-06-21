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
package venus.mvc.chain;

import venus.core.Context;
import venus.exception.VenusFrameworkException;
import venus.ioc.Beans;
import venus.mvc.Mvcs;
import venus.mvc.RequestPath;
import venus.mvc.annotation.RequestMapping;
import venus.mvc.annotation.RequestMethod;
import venus.mvc.bean.RequestHandlerWrapper;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> Request chain factory </p>
 * 1. build handler chain for a request
 * 2. diff request corresponding to diff chain
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-30 16:26
 */
public class RequestHandlerChainFactory {

    private static List<RequestHandlerWrapper> allHandlers = Mvcs.loadAllHandler();
    private static Map<RequestPath, RequestHandlerChain> chains = new ConcurrentHashMap<>();

    public static void initialChain(){
        Set<Class<?>> mappingClz = Beans.of().loadClassByAnnotation(RequestMapping.class);
        mappingClz.stream().forEach(clz -> buildPathChainMapping(clz));
    }

    /**
     * fetch request handler chain
     * 1. after fetch chain, set context to chain
     *
     * @param context
     * @return
     */
    public static RequestHandlerChain chain(Context context){
        RequestPath path = Mvcs.buildPath(context);
        if (chains.containsKey(path)){ //todo bug 没有办法比较requestpath
            RequestHandlerChain chain = chains.get(path);
            if (chain.getContext()==null){
                chain.setContext(context);
            }
            return chain;
        }else {
            chains.putIfAbsent(path, RequestHandlerChainFactory.buildChain(context));
        }
        return chains.get(path);
    }

    /**
     * build chain for every diff http request
     *
     * @param context
     * @return
     */
    protected static RequestHandlerChain buildChain(Context context){
        RequestHandlerChain chain = new RequestHandlerChain(context);
        chain.setHandlerWrapperList(Mvcs.loadHandlers(allHandlers, Mvcs.request2Method(context)));
        return chain;
    }

    /**
     * build url to chain mapping
     *
     * @param clz
     */
    protected static void buildPathChainMapping(Class<?> clz){
        String basePath = clz.getAnnotation(RequestMapping.class).value();
        if (basePath != null && !basePath.startsWith("/")){
            basePath = "/" + basePath;
        }
        for (Method method : clz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)){
                String methodPath = method.getAnnotation(RequestMapping.class).value();
                if (methodPath!=null && !methodPath.startsWith("/")){
                    methodPath = "/" + methodPath;
                }
                if (methodPath!=null && methodPath.endsWith("/")){
                    methodPath = methodPath.substring(0, methodPath.length()-1);
                }
                String path = basePath + methodPath;
                RequestMethod httpMethod = method.getAnnotation(RequestMapping.class).method();
                String _httpMethod = String.valueOf(httpMethod);
                RequestPath requestPath = new RequestPath(_httpMethod, path);
                requestPath.setBasePath(basePath);
                requestPath.setMethodPath(methodPath);

                List<Object> handlers = Mvcs.loadHandlers(allHandlers, method);
                if (handlers==null || handlers.size()<=0){
                    throw new VenusFrameworkException("Request handler chain is null.");
                }
                RequestHandlerChain requestHandlerChain = new RequestHandlerChain(handlers);
                chains.putIfAbsent(requestPath, requestHandlerChain);
            }
        }
    }




}