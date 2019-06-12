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

import venus.core.impl.VContext;
import venus.mvc.render.Render;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * <p> Context for mvc </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-30 11:38
 */
public class MvcContext extends VContext {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;
    private RequestPath requestPath;
    private Object targetController;
    private Method targetMethod;
    private Object result; //return result of execute target method
    private Map<String, String> httpParamMap; //all query params map for this http request
    private Object[] methodParamValue; //all params values of the method for this http request
    private Render render;

    /**
     * Constructor
     *
     * @param request
     * @param response
     */
    public MvcContext(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext){
        this.request = request;
        this.response = response;
        this.servletContext = servletContext;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public RequestPath getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(RequestPath requestPath) {
        this.requestPath = requestPath;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(Method targetMethod) {
        this.targetMethod = targetMethod;
    }

    public Map<String, String> getHttpParamMap() {
        return httpParamMap;
    }

    public void setHttpParamMap(Map<String, String> httpParamMap) {
        this.httpParamMap = httpParamMap;
    }

    public Object[] getMethodParamValue() {
        return methodParamValue;
    }

    public void setMethodParamValue(Object[] methodParamValue) {
        this.methodParamValue = methodParamValue;
    }

    public Object getTargetController() {
        return targetController;
    }

    public void setTargetController(Object targetController) {
        this.targetController = targetController;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Render getRender() {
        return render;
    }

    public void setRender(Render render) {
        this.render = render;
    }
}
