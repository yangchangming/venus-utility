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

import venus.core.Context;
import venus.mvc.chain.RequestHandlerChainFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p> The core servlet for handle http request </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-29 15:39
 */
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        RequestHandlerChainFactory.initialChain();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new MvcContext(req, resp, getServletContext());
        RequestHandlerChainFactory.chain(context).doNext();
    }
}
