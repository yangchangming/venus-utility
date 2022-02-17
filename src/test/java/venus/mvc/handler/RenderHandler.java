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
import venus.mvc.ModelAndView;
import venus.mvc.MvcContext;
import venus.mvc.annotation.RequestHandlerType;
import venus.mvc.annotation.ResponseBody;
import venus.mvc.render.*;

/**
 * <p> Render handler </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-12 15:24
 */
@venus.mvc.annotation.RequestHandler(value = "render", type = RequestHandlerType.COMMON, order = Integer.MAX_VALUE)
public class RenderHandler implements RequestHandler {

    @Override
    public boolean handle(MvcContext context) throws VenusFrameworkException {
        if (context.getResult()==null && !(context.getResult() instanceof String) && !(context.getResult() instanceof ModelAndView)){
            throw new VenusFrameworkException("Return value type error for method [" + context.getTargetMethod().getName() + "].");
        }
        Object result = context.getResult();
        if (context.getTargetMethod().isAnnotationPresent(ResponseBody.class)){
            context.setRender(new JsonRender(context.getResult()));
            //jsp velocity freemarker etc. template engine
        }else if (result instanceof ModelAndView || (result instanceof String && !(((String) result).startsWith("redirect:"))
                && !(((String) result).startsWith("forward:")) )){
            context.setRender(new ViewRender(context.getResult()));
        }else if ((result instanceof String) && ((String) result).startsWith("redirect:")){
            context.setRender(new RedirectRender(context.getResult()));
        }else if ((result instanceof String) && ((String) result).startsWith("forward:")){
            context.setRender(new ForwardRender(context.getResult()));
        } else {
            context.setRender(new InternalErrorRender());
        }
        return true;
    }
}
