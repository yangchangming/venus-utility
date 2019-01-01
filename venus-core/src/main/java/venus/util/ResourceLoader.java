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

import java.io.File;
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
     * Load all Configuration of classpath
     *
     * @return
     */
    public static List<File> defaultLoadConfig(){
        String classPath = VenusPathUtil.getClassPath();
        List<File> configFiles = FileUtil.fetchSubFiles(classPath);
        return configFiles;
    }
}
