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

/**
 * <p> The handler for http request definition </p>
 * 1. handler is singleton in ioc container
 * 2. must be annotation by @RequestHandler(value, type, order)
 * 3. keep go on handler chain if return true, or break chain if return false
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-31 14:16
 */
public interface RequestHandler {

    boolean handle(MvcContext context) throws VenusFrameworkException;
}
