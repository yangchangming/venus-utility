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
import venus.mvc.annotation.RequestChain;
import venus.mvc.annotation.RequestHandlerType;
import venus.mvc.bean.RequestHandlerWrapper;

import java.lang.reflect.Method;
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





}
