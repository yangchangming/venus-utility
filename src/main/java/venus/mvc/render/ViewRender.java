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
package venus.mvc.render;

import venus.exception.VenusFrameworkException;
import venus.mvc.ModelAndView;
import venus.mvc.MvcContext;

import javax.servlet.RequestDispatcher;
import java.util.Map;

/**
 * <p> Model and view render </p>
 * 1. according to the other template engine
 * 2. use jspServlet of tomcat for jsp
 * 3. use freemarker template engine for ftl
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-12 16:24
 */
public class ViewRender implements Render {

    private ModelAndView mv;

    /**
     * Constructor
     *
     * @param mv
     */
    public ViewRender(Object mv){
        if (mv instanceof ModelAndView) {
            this.mv = (ModelAndView) mv;
        } else if (mv instanceof String) {
            this.mv = new ModelAndView().setView((String) mv);
        } else {
            throw new VenusFrameworkException("ModelAndView type error.");
        }
    }

    @Override
    public void render(MvcContext context) throws Exception {
        Object dispathcer = parseViewTemplate(mv.getView(), context);
        if (dispathcer==null){
            throw new VenusFrameworkException("No match template for this view ["+ this.mv.getView() +"]");
        }
        if (dispathcer instanceof RequestDispatcher){
            Map<String , Object> model = mv.getModel();
            model.forEach(context.getRequest()::setAttribute);
            ((RequestDispatcher) dispathcer).forward(context.getRequest(), context.getResponse());
        }
    }

    /**
     * Parse view template for the view
     * 1. just by string prefix "/jsp/" or "jsp/"
     *
     * @param view
     * @param context
     * @return
     */
    private Object parseViewTemplate(String view, MvcContext context){
        if (view==null || "".equals(view)){
            return null;
        }
        if (view.startsWith("/jsp/") || view.startsWith("jsp/")){
            String path = view.startsWith("/") ? view.substring(1) : view;
            String forwardPath = Render.TEMPLATE_PATH_PRE + path;
            return context.getServletContext().getRequestDispatcher(forwardPath);
        }
        if (view.startsWith("/ftl/") || view.startsWith("ftl/")){
            //todo freemarker template engine
        }
        return null;
    }
}
