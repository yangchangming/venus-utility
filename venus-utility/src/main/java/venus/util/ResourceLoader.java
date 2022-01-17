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
package venus.util;

import org.apache.log4j.Logger;
import venus.exception.VenusFrameworkException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

/**
 * <p> Resource loader over all system </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-11 12:59
 */
public class ResourceLoader {

    private static final Logger logger = Logger.getLogger(ResourceLoader.class);

    /**
     * Load all Configuration of classpath, current project
     *
     * @return
     */
    public static List<File> defaultLoadConfig(){
        String classPath = VenusPathUtil.getClassPath();
        List<File> configFiles = FileUtil.fetchSubFiles(classPath);
        return configFiles;
    }

    /**
     * Load configuration specified by param fullpath
     *
     * @param fullPath
     * @return
     */
    public static Enumeration<URL> loadSpecifiedConfig(String fullPath){
        Enumeration<URL> urlEnumeration;
        if (fullPath==null || "".equals(fullPath)){
            return null;
        }
        if (fullPath.startsWith("/")){
            fullPath = fullPath.substring(1, fullPath.length()-1);
        }
        try {
            urlEnumeration = Thread.currentThread().getContextClassLoader().getResources(fullPath);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new VenusFrameworkException("load configuration ["+ fullPath +"] failure. [" + e.getMessage() + "]");
        }
        return urlEnumeration;
    }

}
