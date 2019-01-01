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
package venus.datasource.mysql.config;

import org.apache.log4j.Logger;
import venus.config.ConfigFactory;
import venus.config.ConfigRegister;
import venus.config.URL;
import venus.core.SpiMeta;

/**
 * <p> Register specified configuration at current module </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-06-07 10:07
 */
@SpiMeta(name = "mysql")
public class ConfigRegister4Mysql implements ConfigRegister {

    private final String DATASOURCE_CONFIG_NAME = "datasource_mysql.properties";
    private static Logger logger = Logger.getLogger(ConfigRegister4Mysql.class);

    public void register() {
        java.net.URL url = this.getClass().getResource("/" + DATASOURCE_CONFIG_NAME);
        if (url==null){
            logger.warn(DATASOURCE_CONFIG_NAME + "not find.");
        }else {
            ConfigFactory.loadConfig(new URL(url));
        }
    }
}
