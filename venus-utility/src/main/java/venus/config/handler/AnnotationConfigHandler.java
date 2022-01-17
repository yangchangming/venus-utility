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
package venus.config.handler;

import venus.config.Config;
import venus.config.ConfigHandler;
import venus.config.URL;
import venus.core.SpiMeta;


/**
 * <p> The handler for annotation configuration </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-03 11:25
 */
@SpiMeta(name = "annotation")
public class AnnotationConfigHandler implements ConfigHandler {

    public Config loadConfig(URL url) {
        return null;
    }
}
