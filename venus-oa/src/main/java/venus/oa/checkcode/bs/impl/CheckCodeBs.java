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
package venus.oa.checkcode.bs.impl;

import com.octo.captcha.service.CaptchaServiceException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.springframework.stereotype.Service;
import venus.oa.checkcode.CaptchaServiceSingleton;
import venus.oa.checkcode.bs.ICheckCodeBs;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * <p> Check code service implements </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-03-02 17:27
 */
@Service
public class CheckCodeBs implements ICheckCodeBs {

    @Override
    public byte[] buildCheckCode(String captchaId) {

        byte[] captchaChallengeAsJpeg = null;
        // the output stream to render the captcha image as jpeg into
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // get the session id that will identify the generated captcha.
            //the same id must be used to validate the response, the session id is a good candidate!

            // miss a param,  request.getLocale()
            BufferedImage challenge = CaptchaServiceSingleton.getInstance().getImageChallengeForID(captchaId);

            // a jpeg encoder
            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream);
            jpegEncoder.encode(challenge);

        } catch (IllegalArgumentException e) {
            return new byte[0];
        } catch (CaptchaServiceException e) {
            return new byte[0];
        } catch (IOException e){
            return new byte[0];
        }
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        return captchaChallengeAsJpeg;
    }
}
