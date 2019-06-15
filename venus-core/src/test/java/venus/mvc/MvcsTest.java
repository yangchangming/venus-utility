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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import venus.mvc.bean.RequestHandlerWrapper;

import java.lang.reflect.Method;
import java.util.List;

/**
 * <p> Mvcs test case </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-14 13:44
 */
public class MvcsTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockServletContext servletContext;
    List<RequestHandlerWrapper> allHandlers;

    @Before
    public void setUp(){
        request = new MockHttpServletRequest();
        request.setPathInfo("/user/list");
        request.setMethod("GET");
        request.setCharacterEncoding("UTF-8");
        response = new MockHttpServletResponse();
        servletContext = new MockServletContext();
    }

    @Test
    public void buildRequestPathTest(){
        MvcContext context = new MvcContext(request, response, servletContext);
        RequestPath requestPath = Mvcs.buildPath(context);
        Assert.assertEquals(context.getRequestPath().getPath(), requestPath.getPath());
    }

    @Test
    public void loadAllRequestHandlerTest(){
        Assert.assertEquals(7, Mvcs.loadAllHandler().size());
    }

    @Test
    public void request2MethodTest(){
        MvcContext context = new MvcContext(request, response, servletContext);
        Method method = Mvcs.request2Method(context);
        Assert.assertEquals("list", method.getName());
    }

    @Test
    public void loadHandlerByMethodTest(){
        MvcContext context = new MvcContext(request, response, servletContext);
        Method method = Mvcs.request2Method(context);
        allHandlers = Mvcs.loadAllHandler();
        Assert.assertEquals(7, Mvcs.loadHandlers(allHandlers, method).size());
    }

    @Test
    public void requestBody2StrTest(){
        request.setMethod("POST");
        request.setContentType("text/plain");
        request.setContent(String.valueOf("woaizhongguo").getBytes());
        Assert.assertEquals("woaizhongguo" ,Mvcs.requestBody2Str(request));
    }
}