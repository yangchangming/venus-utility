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

import venus.exception.VenusFrameworkException;
import venus.lang.Clazz;
import venus.mvc.Castor;
import venus.mvc.MIMEType;
import venus.mvc.MvcContext;
import venus.mvc.Mvcs;
import venus.mvc.annotation.RequestBody;
import venus.mvc.annotation.RequestHandlerType;
import venus.mvc.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Http request data binder handler </p>
 * 1. get param handle, for query param or form data
 * 2. post method handle, for http body data
 * 3. method param value object assemble
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-05 09:55
 */
@venus.mvc.annotation.RequestHandler(value = "dataBind", type = RequestHandlerType.COMMON, order = 4)
public class DataBindHandler implements RequestHandler {

    @Override
    public boolean handle(MvcContext context) throws VenusFrameworkException {
        if (context.getTargetMethod()!=null){
            Method targetMethod = context.getTargetMethod();
            List<Object> methodParams = _bind(targetMethod, context);
            if (methodParams==null || methodParams.size()!=targetMethod.getParameterTypes().length){
                throw new VenusFrameworkException("Data bind to method params failure.");
            }
            context.setMethodParamValue(methodParams.toArray());
            return true;
        }else {
            throw new VenusFrameworkException("Target method is not found!");
        }
    }

    /**
     * Http请求数据注入调用方法参数
     * 1. 支持自定义类型注入，自定义类型属性必须是原生类型
     * 2. 支持List 和 array集合注入，集合中的对象必须是自定义类型，自定义类型属性必须是原生类型
     * 3. 支持原生类型注入
     * 4. 支持注解RequestParam和RequestBody，RequestBody从http的body获取数据，只获取键值对，不支持json
     * 5. 没有注解的参数，原生类型默认注入默认值，非原生类型默认注入null
     *
     * @param targetMethod
     * @param context
     * @return
     */
    protected List<Object> _bind(Method targetMethod, MvcContext context){
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

                } else if (parameterType.isArray()) {
                    Object o = bindNonPrimitiveSet(httpParamMap, methodParamName, parameterType);
                    methodParams.add(o);

                // set null if parameterType is List
                }else if (Clazz.isImplementsInterface(parameterType, List.class)){
//                    Object o = bindNonPrimitiveSet(httpParamMap, methodParamName, parameterType);
                    methodParams.add(null);

                } else if (!Clazz.isPrimitive(parameterType)){
                    Object o = bindNonPrimitive(httpParamMap, methodParamName, parameterType);
                    methodParams.add(o);
                } else {
                    methodParams.add(null);
                }

                //name="xxx"&age=22&sex=2&birthday="2018-11-19"
            }else if (parameterType.isAnnotationPresent(RequestBody.class)){
                String bodyContent = Mvcs.requestBody2Str(context.getRequest());
                if (MIMEType.MIME_TYPE_JSON.equals(context.getRequest().getContentType())){
                    //todo json to object
                }else {
                    Map<String, String> httpBodyMap = new HashMap<>();
                    if (bodyContent!=null && !"".equals(bodyContent)
                            && bodyContent.indexOf("=")>-1){
                        String[] bodies = bodyContent.split("&");
                        for (String body : bodies) {
                            String[] keyValue = body.split("=");
                            httpBodyMap.put(keyValue[0], keyValue[1]==null?"":keyValue[1]);
                        }
                    }
                    Object o = Castor.stringToNonPrimitive(httpBodyMap, parameterType);
                    methodParams.add(o);
                }

            }else {
                Object o;
                if (parameterType==HttpServletRequest.class){
                    o = context.getRequest();
                }else if (parameterType== HttpServletResponse.class){
                    o = context.getResponse();
                }else {
                    o = Castor.stringToClzInstance(null, parameterType);
                }
                methodParams.add(o);
            }
        }
        return methodParams;
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
        //{(age->18),(name->"中本聪"),...}
        Map<String, String> targetMap = new HashMap<>();
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
     * bind http param to list or array of non-primitive
     *
     * @param httpParamMap
     * @param methodParamName
     * @param parameterType
     * @return
     */
    protected Object bindNonPrimitiveSet(Map<String, String> httpParamMap, final String methodParamName, Class<?> parameterType){
        //{(user[0]->((name->"中本聪"),(age->33),...)),(user[1]->((age->22),(name->"中本聪"),...)),...}
        Map<String, Map<String, String>> targetSetMap = new HashMap<>();
        httpParamMap.keySet().stream().forEach(key -> {
            if (key.startsWith(methodParamName + "[")){
                //key: user[0].name
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
        return Castor.stringToNonPrimitiveSet(targetSetMap, parameterType);
    }

}
