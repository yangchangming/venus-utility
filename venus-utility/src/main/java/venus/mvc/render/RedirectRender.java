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

import venus.mvc.MvcContext;

/**
 * <p> Redirect render </p>
 * 1. redirect path must be full path
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-12 17:17
 */
public class RedirectRender implements Render {

    private String path;
    private final String REDIRECT_PRE_LABEL = "redirect:";

    public RedirectRender(Object path){
        if (path!=null && path instanceof String){
            this.path = (String)path;
        }
    }

    @Override
    public void render(MvcContext context) throws Exception {
        String redirectPath = "";
        if (this.path==null || "".equals(this.path)){
            return;
        }
        redirectPath = this.path.replaceFirst(REDIRECT_PRE_LABEL, "");
        if (!redirectPath.startsWith("/")){
            redirectPath = "/" + redirectPath;
        }
        context.getResponse().sendRedirect(context.getRequest().getContextPath() + redirectPath);
    }
}
