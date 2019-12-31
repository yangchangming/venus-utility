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
import venus.mvc.MvcContext;
import venus.mvc.annotation.RequestHandlerType;
import venus.mvc.render.ForwardRender;

import javax.servlet.RequestDispatcher;

/**
 * <p> Default handler for http request, switch to web container </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-12-31 00:11
 */
@venus.mvc.annotation.RequestHandler(value = "default", type = RequestHandlerType.COMMON, order = Integer.MAX_VALUE-2)
public class DefaultHandler implements RequestHandler {

    private static String DEFAULT_SERVLET_NAME = "default";

    @Override
    public boolean handle(MvcContext context) throws VenusFrameworkException {
        try {
            context.setRender(new ForwardRender(null));
            RequestDispatcher requestDispatcher = context.getServletContext().getNamedDispatcher(DEFAULT_SERVLET_NAME);
            requestDispatcher.forward(context.getRequest(), context.getResponse());
        }catch (Exception e){
            throw new VenusFrameworkException(e.getMessage());
        }
        return false;
    }
}
