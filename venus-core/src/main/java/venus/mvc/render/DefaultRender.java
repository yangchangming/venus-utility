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

import venus.mvc.HttpStatus;
import venus.mvc.MvcContext;

/**
 * <p> Default Render, 200 </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-03 16:40
 */
public class DefaultRender implements Render {

    @Override
    public void render(MvcContext context) throws Exception {
        context.getResponse().setStatus(HttpStatus.SC_OK);
    }
}