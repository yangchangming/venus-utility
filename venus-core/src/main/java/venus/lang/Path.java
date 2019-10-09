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
package venus.lang;

import venus.exception.VenusFrameworkException;

/**
 * <p> Path util of web context or jar context </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-10-09 09:35
 */
public class Path {

    /**
     * fetch real classpath in file system
     *
     * @return
     */
    public static String fetchRealClassPath(){
        Class clazz = Path.class;
        String clsName = clazz.getName() + ".class";
        Package pack = clazz.getPackage();
        String path = "";
        if (pack != null) {
            String packName = pack.getName();
            clsName = clsName.substring(packName.length() + 1);
            if (packName.indexOf(".") < 0)
                path = packName + "/";
            else {
                int start = 0, end = 0;
                end = packName.indexOf(".");
                while (end != -1) {
                    path = path + packName.substring(start, end) + "/";
                    start = end + 1;
                    end = packName.indexOf(".", start);
                }
                path = path + packName.substring(start) + "/";
            }
        }
        java.net.URL url = clazz.getClassLoader().getResource(path + clsName);
        if (url==null) {
            return null;
        }
        String realPath = url.getPath();
        int pos = realPath.indexOf(path + clsName);
        String realClassPath = realPath.substring(0, pos - 1);
        try {
            realClassPath = java.net.URLDecoder.decode(realClassPath, "utf-8");
        } catch (Exception e) {
            throw new VenusFrameworkException(e.getCause().getMessage());
        }
        return realClassPath;
    }


}
