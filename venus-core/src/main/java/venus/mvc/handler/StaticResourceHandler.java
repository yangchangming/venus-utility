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
import venus.mvc.render.Render;

import javax.servlet.RequestDispatcher;

/**
 * <p> Static resource handler </p>
 * 1. handle by defaultServlet of tomcat
 * 2. by xxx of other web container, such as jetty ...
 * 3. all static resource must be below of /static/ directory
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-13 10:07
 */
@venus.mvc.annotation.RequestHandler(value = "static", type = RequestHandlerType.COMMON, order = 1)
public class StaticResourceHandler implements RequestHandler {

    @Override
    public boolean handle(MvcContext context) throws VenusFrameworkException {
        if (context.getRequest().getPathInfo().startsWith(Render.ASSET_PATH_PRE)){
            RequestDispatcher dispatcher = context.getServletContext().getNamedDispatcher(Render.DEFAULT_TOMCAT_DISPACTHER);
            try {
                dispatcher.forward(context.getRequest(), context.getResponse());
                return false;
            } catch (Exception e){
                throw new VenusFrameworkException(e.getMessage());
            }
        }
        return true;
    }
}
