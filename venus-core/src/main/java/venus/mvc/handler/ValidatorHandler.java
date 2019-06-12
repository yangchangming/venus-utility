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
import venus.mvc.annotation.RequestMapping;
import venus.mvc.annotation.RequestParam;

import java.lang.reflect.Method;

/**
 * <p> Validator handler for target method params </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-06-12 09:46
 */
@venus.mvc.annotation.RequestHandler(value = "validator", type = RequestHandlerType.COMMON, order = 2)
public class ValidatorHandler implements RequestHandler {

    @Override
    public boolean handle(MvcContext context) throws VenusFrameworkException {
        if (context.getTargetMethod()!=null){
           Method method = context.getTargetMethod();
           String value = method.getAnnotation(RequestMapping.class).value();
           if (value==null || "".equals(value)){
               throw new VenusFrameworkException("The RequestMapping annotation of method ["+ method.getName() +"] is null.");
           }
           if (!(method.getReturnType()==String.class) || !(method.getReturnType()== ModelAndView.class)){
               throw new VenusFrameworkException("Return type of method [" + method.getName() + "] is error.");
           }
           for (Class<?> paramClz : method.getParameterTypes()) {
               if (paramClz.isAnnotationPresent(RequestParam.class)){
                   if (paramClz.getAnnotation(RequestParam.class).required() && "".equals(paramClz.getAnnotation(RequestParam.class).value())){
                       throw new VenusFrameworkException("The RequestParam annotation of method [" + method.getName() + "] is null.");
                   }
               }
           }
        }else {
            throw new VenusFrameworkException("The target method is null.");
        }
        return true;
    }
}
