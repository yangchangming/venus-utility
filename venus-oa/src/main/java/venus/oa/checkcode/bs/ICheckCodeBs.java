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
package venus.oa.checkcode.bs;

/**
 * <p> Check code service interface definition </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-03-02 17:24
 */
public interface ICheckCodeBs {

    /**
     * build different checkcode, and return byte array
     *
     * @param captchaId
     * @return
     */
    byte[] buildCheckCode(String captchaId);

    /**
     * validate response
     *
     * @param captchaId
     * @param j_captcha_response
     * @return
     */
    boolean validateResponse(String captchaId, String j_captcha_response);
}
