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

import java.lang.reflect.Method;

/**
 * <p> Invoke method handler </p>
 * 1. finally handler for request handler chain
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-03 18:33
 */
@venus.mvc.annotation.RequestHandler(value = "invoke", type = RequestHandlerType.COMMON, order = Integer.MAX_VALUE-1)
public class InvokeHandler implements RequestHandler {

    @Override
    public boolean handle(MvcContext context) throws VenusFrameworkException {
        Object[] methodParamValue = context.getMethodParamValue();
        Method method = context.getTargetMethod();
        Object controller = context.getTargetController();
        Object result;
        if (method!=null && controller!=null && methodParamValue!=null){
            try {
                if (methodParamValue.length==0){
                    result = method.invoke(controller);
                }else {
                    result = method.invoke(controller, methodParamValue);
                }
                context.setResult(result);
            }catch (Exception e){
                throw new VenusFrameworkException("Invoke method failure. " + e.getCause().getMessage());
            }
        }else {
           throw new VenusFrameworkException("Cannot execute method, no condition.");
        }
        return true;
    }
}
