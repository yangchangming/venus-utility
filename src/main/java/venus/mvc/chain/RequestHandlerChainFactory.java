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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import venus.core.Context;
import venus.exception.VenusFrameworkException;
import venus.ioc.Beans;
import venus.mvc.Mvcs;
import venus.mvc.RequestPath;
import venus.mvc.annotation.RequestHandlerType;
import venus.mvc.annotation.RequestMapping;
import venus.mvc.bean.RequestHandlerWrapper;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p> Request chain factory </p>
 * 1. build handler chain for a request
 * 2. diff request corresponding to diff chain
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-30 16:26
 */
public class RequestHandlerChainFactory {

    private static Logger logger = LoggerFactory.getLogger(RequestHandlerChainFactory.class);
    private static List<RequestHandlerWrapper> allHandlers = Mvcs.loadAllHandler();
    private static Map<RequestPath, RequestHandlerChain> chains = new ConcurrentHashMap<>();

    public static void initialChain(){
        Set<Class<?>> mappingClz = Beans.of().loadClassByAnnotation(RequestMapping.class);
        mappingClz.stream().forEach(clz -> buildPathChainMapping(clz));
    }

    /**
     * fetch request handler chain
     * 1. after fetch chain, update context in chain
     * 2. every request responding to a request handler chain in chains
     * 3. two RequestPath object is equals when http-method and http-path is equals
     *
     * @param context
     * @return
     */
    public static RequestHandlerChain chain(Context context){
        RequestPath path = Mvcs.buildPath(context);
        for (RequestPath requestPath : chains.keySet()) {
            if (requestPath.getHttpMethod().equals(path.getHttpMethod()) && requestPath.getPath().equals(path.getPath())){
                RequestHandlerChain chain = chains.get(requestPath);
                chain.setContext(context);
                return chain;
            }
        }
        if ("".equals(path.getPath()) || "/".equals(path.getPath())){
            chains.putIfAbsent(path, buildIndexChain(context));
            return chains.get(path);
        }
        chains.putIfAbsent(path, RequestHandlerChainFactory.buildChain(context));
        return chains.get(path);
    }

    /**
     * build chain for welcome page http request
     *
     * @param context
     * @return
     */
    protected static RequestHandlerChain buildIndexChain(Context context){
        RequestHandlerChain chain = new RequestHandlerChain(context);
        List<Object> handlers = allHandlers.stream().filter(wrapper -> {
            if (String.valueOf(RequestHandlerType.CUSTOMIZE).equals(wrapper.getType()) &&
                    ("default".equals(wrapper.getValue()))) {
                return true;
            }
            return false;
        }).sorted(Comparator.comparing(wrapper -> wrapper.getOrder())).collect(Collectors.toList());
        chain.setHandlerWrapperList(handlers);
        return chain;
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
                RequestPath requestPath =Mvcs.buildRequestPathByMethod(method, basePath);
                if (requestPath==null){
                    logger.warn("Can not build RequestPath for the method. [" + method.getName() + "]");
                    break;
                }
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