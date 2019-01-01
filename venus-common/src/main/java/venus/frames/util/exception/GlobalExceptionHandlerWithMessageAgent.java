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
package venus.frames.util.exception;

import venus.frames.base.action.IForward;
import venus.frames.base.action.IMappingCfg;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.util.exception.handler.UdpStrutsExceptionHandler;

/**
 * <p>  </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-06-13 16:17
 */
public class GlobalExceptionHandlerWithMessageAgent extends UdpStrutsExceptionHandler {

    public IForward service(IForward exforward,
                            Exception ex,
                            IMappingCfg mapping,
                            DefaultForm formInstance,
                            IRequest request,
                            IResponse response) {

        return super.service(exforward, ex, mapping, formInstance, request, response);
    }
}
