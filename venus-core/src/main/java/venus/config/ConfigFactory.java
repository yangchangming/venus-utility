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

import org.apache.log4j.Logger;
import venus.common.VenusConstants;
import venus.core.ExtensionLoader;
import venus.exception.VenusFrameworkException;
import venus.util.ResourceLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p> System Configuration Factory </p>
 *
 *  Configurations Service for all over of system
 *  1. annotation config
 *  2. xml config
 *  3. property config
 *  4. zookeeper config
 *  5. yaml config
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-03 11:08
 */
public class ConfigFactory {

    private static final Logger logger = Logger.getLogger(ConfigFactory.class);

    public static final String DEFAULT_CONFIG_PREFIX_PATH = "WEB-INF/conf/";

    /**
     * Configurations that be assemble all config from all over the system
     */
    private static ConcurrentMap<URL, Config> configs = new ConcurrentHashMap<URL, Config>();

    private static boolean initialized = false;

    /**
     * Initial all configuration at default position
     */
    public synchronized static void init(){
        if (initialized){
            return;
        }
        registerConfig();
        loadCurrentConfig();
        initialized = true;
    }

    private static void checkInit(){
        if (!ConfigFactory.initialized){
            init();
        }
    }

    /**
     * Load current application all configuration at classpath
     */
    private static void loadCurrentConfig(){
        List<File> configFiles = ResourceLoader.defaultLoadConfig();
        for (File configFile : configFiles) {
            try {
                loadConfig(new URL(configFile.toURL()));
            } catch (MalformedURLException e) {
                logger.error(e.getMessage());
                throw new VenusFrameworkException(e.getMessage());
            }
        }
    }

    /**
     * Register all config for all of modules
     */
    private static void registerConfig(){
        ConcurrentMap<String, Class<ConfigRegister>> configRegisters = ExtensionLoader.getExtensionLoader(ConfigRegister.class).loadExtensions();
        for (Class<ConfigRegister> configRegisterClass : configRegisters.values()) {
            try {
                ConfigRegister configRegister = configRegisterClass.newInstance();
                if (configRegister instanceof ConfigRegister){
                    configRegister.register();
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Load configuration specified by url, and assemble to ConfigFactory.configs
     * the type of configuration is just spi name
     *
     * @param url config url
     */
    public static void loadConfig(URL url){

        Map<URL, Config> _map = new HashMap<URL, Config>();
        String spiName = parseURL(url);
        if (spiName==null || "".equals(spiName)){
            return;
        }
        ConfigHandler configHandler = ExtensionLoader.getExtensionLoader(ConfigHandler.class).loadExtensionInstance(spiName);
        Config config = configHandler.loadConfig(url);
        if (config==null){
            logger.warn("Failure for getting configuration by url [" + url + "]");
            return;
        }
        _map.put(url, config);
        addConfig(_map);
    }

    private static String parseURL(URL url){
        if (url==null){
            throw new VenusFrameworkException("Configuration url is Null.");
        }
        if (!url.isValid()){
            logger.warn("Unsupported configuration file type. [" + url.getType() + "]");
            return "";
        }
        return url.getType()==null ? "" : url.getType();
    }

    /**
     * fetch configs by keywords specified by key, keywords same as key
     *
     * @param key
     * @return
     */
    public static List<Config> fetchConfigByKeywords(String key){
        if (key == null || "".equals(key)){
            return null;
        }
        List<Config> _configs = new ArrayList<Config>();
        Set<URL> keys = configs.keySet();
        for (URL url : keys) {
            String path = url.getUrl().getPath();
            if (path!=null && path.toLowerCase().indexOf(key.toLowerCase())>-1 ){
                _configs.add(configs.get(url));
            }
        }
        return _configs;
    }


    /**
     * Fetch all spring configuration that start with "spring"
     *
     * @return
     */
    public static List<String> fetchSpringConfigLocations(){
        List<String> configLocations = new ArrayList<String>();
        checkInit();
        Set<URL> keys = configs.keySet();
        for (URL url : keys) {
            String path = url.getUrl().getPath();
            if (path!=null && (path.toLowerCase().indexOf(VenusConstants.CONFIG_SPRING_PREFIX.toLowerCase())>-1 || path.indexOf("applicationContext")>-1) ){
                // excluding spring mvc configuration
                if (!path.endsWith("mvc.xml") && !path.endsWith("MVC.xml")){
                    configLocations.add(path);
                }
            }
        }
        return configLocations;
    }

    public static List<String> fetchSpringConfigNames(){
        List<String> configNames = new ArrayList<String>();
        List<String> configLocations = fetchSpringConfigLocations();
        if (configLocations!=null && configLocations.size()>0){
            for (String configLocation : configLocations) {
                String[] temp = configLocation.split("/");
                for (String path : temp) {
                    if (path!=null && !"".equals(path) && path.trim().startsWith(VenusConstants.CONFIG_SPRING_PREFIX) &&
                            path.trim().indexOf(VenusConstants.CONFIG_EXTENSION_SEPARATOR)>-1 &&
                            path.trim().endsWith(VenusConstants.CONFIG_EXTENSION_XML)){
                        configNames.add(path.trim());
                    }
                }
            }
        }
        return configNames;
    }



    public static ConcurrentMap<URL, Config> fetchAllConfig(){
        return ConfigFactory.configs;
    }

    private static void addConfig(Map<URL, Config> configs) {
        if (configs != null){
            Set<URL> urls = configs.keySet();
            for (URL url : urls) {
                if (configs.get(url) != null){
                    ConfigFactory.configs.putIfAbsent(url, configs.get(url));
                }
            }
        }else {
            logger.warn("Configuration is null.");
            return;
        }
    }
}
