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
package venus.mvc.handler;

import org.springframework.web.bind.annotation.RequestBody;
import venus.exception.VenusFrameworkException;
import venus.lang.Clazz;
import venus.mvc.Castor;
import venus.mvc.MvcContext;
import venus.mvc.annotation.RequestHandlerType;
import venus.mvc.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <p> Http request data binder handler </p>
 * 1. get param handle, for query param or form data
 * 2. post method handle, for http body data
 * 3. method param value object assemble
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-05 09:55
 */
@venus.mvc.annotation.RequestHandler(value = "dataBind", type = RequestHandlerType.COMMON, order = 2)
public class DataBindHandler implements RequestHandler {

    @Override
    public boolean handle(MvcContext context) throws VenusFrameworkException {
        if (context.getTargetMethod()!=null){
            Method targetMethod = context.getTargetMethod();
            List<Object> methodParams = new ArrayList<>();

            for (Class<?> parameterType : targetMethod.getParameterTypes()) {

                if (parameterType.isAnnotationPresent(RequestParam.class)){
                    String methodParamName = parameterType.getAnnotation(RequestParam.class).value();
                    if (methodParamName==null || "".equals(methodParamName)){
                        throw new VenusFrameworkException("The value of RequestParam Annotation is not null.");
                    }

                    Map<String, String> httpParamMap = parseRequestParam(context.getRequest());
                    context.setHttpParamMap(httpParamMap);

                    if (httpParamMap.containsKey(methodParamName) && Clazz.isPrimitive(parameterType)){
                        Object o = Castor.stringToClzInstance(httpParamMap.get(methodParamName), parameterType);
                        methodParams.add(o);

                    } else if (Array.class.equals(parameterType) || List.class.equals(parameterType) || Set.class.equals(parameterType)){
                        Object o =bindNonPrimitiveSet(httpParamMap, methodParamName, parameterType);

                    } else if (!Clazz.isPrimitive(parameterType)){
                        Object o = bindNonPrimitive(httpParamMap, methodParamName, parameterType);
                    }



                }else if (parameterType.isAnnotationPresent(RequestBody.class)){




                }else {



                }
            }


            //todo method param array build
            return true;

        }else {
            throw new VenusFrameworkException("Target method is not found!");
        }
    }

    protected Map<String, String> parseRequestParam(HttpServletRequest request){
        Map<String, String> paramMap = new HashMap<>();
        for (Object paramName : request.getParameterMap().keySet()) {
            paramMap.put(String.valueOf(paramName), request.getParameter(String.valueOf(paramName)));
        }
        // TODO: Body、Path、Header 请求参数获取
        return paramMap;
    }


    /**
     * bind http param to non-primitive object
     * user.name="" user.age=11
     *
     * @param httpParamMap
     * @param methodParamName
     * @param parameterType
     * @return
     */
    protected Object bindNonPrimitive(Map<String, String> httpParamMap, final String methodParamName, Class<?> parameterType){
        Map<String, String> targetMap = new HashMap<>(); //{(age->18),(name->"中本聪"),...}
        httpParamMap.keySet().stream().forEach(key -> {
            if (key.startsWith(methodParamName + ".")){
                String[] keys  = key.split(".");
                String postKey = keys[keys.length-1];
                if (postKey!=null && !"".equals(postKey)){
                    for (Field declaredField : parameterType.getDeclaredFields()) {
                        if (declaredField.getName().equals(postKey)){
                            targetMap.put(postKey, httpParamMap.get(key));
                        }
                    }
                }
            }
        });
        return Castor.stringToNonPrimitive(targetMap, parameterType);
    }

    /**
     * bind http param to set of non-primitive
     *
     * @param httpParamMap
     * @param methodParamName
     * @param parameterType
     * @return
     */
    protected Object bindNonPrimitiveSet(Map<String, String> httpParamMap, final String methodParamName, Class<?> parameterType){
        //{(user[0]->((name->"中本聪"),(age->33),...)),(user[1]->((age->22),(name->"中本聪"),...)),...}
        Map<String, Map<String, String>> targetSetMap = new HashMap<>();

        Class<?> componentClz = null;
        if (parameterType.isArray()){
            componentClz = parameterType.getComponentType();
        }else {
            //todo ??
            componentClz = parameterType.getGenericSuperclass().getClass();
        }


        httpParamMap.keySet().stream().forEach(key -> {
            if (key.startsWith(methodParamName + "[")){
                //user[0].name
                String[] keys  = key.split(".");
                String preKey = keys[0];
                String postKey = keys[keys.length-1];

                if (postKey!=null && !"".equals(postKey)){

                    for (Field field : parameterType.getDeclaredFields()) {

                        if (field.getName().equals(postKey)){
                            if (targetSetMap.containsKey(preKey)){
                                targetSetMap.get(preKey).put(postKey, httpParamMap.get(key));
                            }else {
                                Map<String, String> fieldMap = new HashMap<>();
                                fieldMap.put(postKey, httpParamMap.get(key));
                                targetSetMap.put(preKey, fieldMap);
                            }
                        }
                    }
                }

            }
        });
        return Castor.stringSetToNonPrimitive(targetSetMap, componentClz);
    }

}
