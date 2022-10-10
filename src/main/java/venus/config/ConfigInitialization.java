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
package venus.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import venus.core.SpiMeta;
import venus.init.Initialization;

/**
 * <p> Configuration Initialization that all over system </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-24 16:33
 */
@SpiMeta(name = "configuration")
public class ConfigInitialization implements Initialization {
    private static final Logger logger = LoggerFactory.getLogger(ConfigInitialization.class);
    private static boolean finish = false;

    public void init() {
        if (isFinish()){
            logger.info("Configuration Initialization has finished!");
            return;
        }
        venus.log.Logger.keyInfo(logger, "Configuration Initial Success(TO DO Nothing)!");
    }

    public boolean isFinish() {
        return finish;
    }
}
