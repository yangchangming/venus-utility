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

/**
 * <p> Model and view render </p>
 * 1. according to the other template engine
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
            throw new VenusFrameworkException("Return type error.");
        }
    }

    @Override
    public void render(MvcContext context) throws Exception {





    }
}
