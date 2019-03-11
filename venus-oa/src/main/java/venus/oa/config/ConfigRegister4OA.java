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
package venus.oa.config;

import org.apache.log4j.Logger;
import venus.common.VenusConstants;
import venus.config.ConfigFactory;
import venus.config.ConfigRegister;
import venus.core.SpiMeta;
import venus.util.ResourceLoader;

import java.net.URL;
import java.util.Enumeration;

/**
 * <p> Config register for oa module </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-03-08 01:20
 */
@SpiMeta(name = "oa")
public class ConfigRegister4OA implements ConfigRegister {

    private final String SPRING_CONFIG_NAME = "applicationContext.xml";
    private final String MOTAN_CONFIG_NAME = "applicationContext-motan.xml";
    private static Logger logger = Logger.getLogger(ConfigRegister4OA.class);

    @Override
    public void register() {
        Enumeration<java.net.URL> springConfigs = ResourceLoader.loadSpecifiedConfig("spring/" + SPRING_CONFIG_NAME);
        Enumeration<java.net.URL> motanConfigs = ResourceLoader.loadSpecifiedConfig("spring/" + MOTAN_CONFIG_NAME);
        _register(springConfigs);
        _register(motanConfigs);
    }

    private void _register(Enumeration<java.net.URL> configs){
        if (configs!=null){
            while (configs.hasMoreElements()){
                URL url = configs.nextElement();
                if (VenusConstants.URL_PROTOCOL_JAR.equals(url.getProtocol())){
                    ConfigFactory.loadConfig(new venus.config.URL(url));
                }
            }
        }
    }
}
