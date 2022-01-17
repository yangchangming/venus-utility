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

import org.apache.log4j.Logger;
import venus.config.Config;
import venus.config.ConfigHandler;
import venus.config.URL;
import venus.config.factor.DefaultPropertyConfig;
import venus.core.SpiMeta;
import venus.exception.VenusFrameworkException;
import venus.util.VenusConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


/**
 * <p> The handler for property file configuration </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-03 11:28
 */
@SpiMeta(name = "property")
public class PropertyConfigHandler implements ConfigHandler {

    private static final Logger logger = Logger.getLogger(PropertyConfigHandler.class);

    public Config loadConfig(URL url) {
        if (url==null){
            return null;
        }
        if (url.getType()==null || "".equals(url.getType())){
            url.setType(VenusConstants.CONFIG_TYPE_PROPERTY);
        }
        Config config = new DefaultPropertyConfig();
        config.setType(url.getType());
        try {
            InputStream in = url.getUrl().openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String line = "";
            while (bufferedReader!=null && (line=bufferedReader.readLine())!=null){
                Map<String, String> _map = checkPropertySyntax(line);
                if (_map!=null && _map.size()>0){
                    config.refreshData(_map);
                }
            }
        } catch (IOException e) {
            logger.error("Failure for handle configuration file. [" + url.getUrl().getPath() + "]");
            throw new VenusFrameworkException(e.getMessage());
        }
        return config;
    }


    private Map<String, String> checkPropertySyntax(String propertyPair){
        Map<String, String> _map = new HashMap<String, String>();
        if (propertyPair==null || "".equals(propertyPair)){
            return _map;
        }
        if (!propertyPair.startsWith("#") && !propertyPair.startsWith("=") && propertyPair.indexOf("=")>0){
            int index = propertyPair.indexOf("=");
            String key = propertyPair.substring(0, index);

            String value = propertyPair.substring(index+1, propertyPair.length());

            if (key!=null && !"".equals(key.trim())){
                _map.put(key.trim(), value.replaceAll("\"","").trim());
            }
        }else {
            logger.warn("Property configuration Syntax error. [" + propertyPair + "]" );
        }
        return _map;
    }

}
