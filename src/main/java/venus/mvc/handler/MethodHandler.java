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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import venus.exception.VenusFrameworkException;
import venus.mvc.MvcContext;
import venus.mvc.Mvcs;
import venus.mvc.annotation.RequestHandlerType;

import java.lang.reflect.Method;

/**
 * <p> Mapping method for this request </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-03 17:54
 */
@venus.mvc.annotation.RequestHandler(value = "method", type = RequestHandlerType.COMMON, order = 2)
public class MethodHandler implements RequestHandler {
    private Logger logger = LoggerFactory.getLogger(MethodHandler.class);

    @Override
    public boolean handle(MvcContext context) throws VenusFrameworkException {
        Method method = Mvcs.request2Method(context);
        if (method==null){
            logger.warn("No method match for this request.");
            return false;
        }else {
            context.setTargetMethod(method);
            return true;
        }
    }
}
