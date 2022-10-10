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

import org.apache.commons.lang3.StringUtils;
import venus.base.Platforms;
import venus.exception.VenusFrameworkException;
import venus.util.FileUtil;

import java.io.File;

/**
 * <p> Path util of web context or jar context </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-10-09 09:35
 */
public class Path {

    /**
     * fetch real classpath in file system
     * bug： 在其他工程中，引入了venus-core，此方法会导致获取到的classpath不是真实工程的，而是venus-core的classpath
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

    /**
     * Fetch absolute path for web content root path
     *
     * @return
     */
    public static String fetchAppBase4Web(){
        String classPath = Path.fetchRealClassPath();
        if (classPath.endsWith("!")){
            classPath = classPath.substring(0, classPath.length()-1);
        }
        File webContentFolder = new File(classPath);
        if (!FileUtil.isDirExists(webContentFolder)) {
            webContentFolder.mkdir();
        }
        return webContentFolder.getAbsolutePath();
    }

    /**
     * 获得参数clazz所在的Jar文件的绝对路径
     */
    public static String getJarPath(Class<?> clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
    }

    public static String getParentPath(String path) {
        String parentPath = path;
        if (Platforms.FILE_PATH_SEPARATOR.equals(parentPath)) {
            return parentPath;
        }
        parentPath = Strings.removeEnd(parentPath, Platforms.FILE_PATH_SEPARATOR_CHAR);
        int idx = parentPath.lastIndexOf(Platforms.FILE_PATH_SEPARATOR_CHAR);
        if (idx >= 0) {
            parentPath = parentPath.substring(0, idx + 1);
        } else {
            parentPath = Platforms.FILE_PATH_SEPARATOR;
        }
        return parentPath;
    }

    /**
     * 在Windows环境里，兼容Windows上的路径分割符，将 '/' 转回 '\'
     */
    public static String normalizePath(String path) {
        if (Platforms.FILE_PATH_SEPARATOR_CHAR == Platforms.WINDOWS_FILE_PATH_SEPARATOR_CHAR
                && StringUtils.indexOf(path, Platforms.LINUX_FILE_PATH_SEPARATOR_CHAR) != -1) {
            return StringUtils.replaceChars(path, Platforms.LINUX_FILE_PATH_SEPARATOR_CHAR,
                    Platforms.WINDOWS_FILE_PATH_SEPARATOR_CHAR);
        }
        return path;
    }

    /**
     * 以拼接路径名
     */
    public static String concat(String baseName, String... appendName) {
        if (appendName.length == 0) {
            return baseName;
        }
        StringBuilder concatName = new StringBuilder();
        if (Strings.endWith(baseName, Platforms.FILE_PATH_SEPARATOR_CHAR)) {
            concatName.append(baseName).append(appendName[0]);
        } else {
            concatName.append(baseName).append(Platforms.FILE_PATH_SEPARATOR_CHAR).append(appendName[0]);
        }
        if (appendName.length > 1) {
            for (int i = 1; i < appendName.length; i++) {
                concatName.append(Platforms.FILE_PATH_SEPARATOR_CHAR).append(appendName[i]);
            }
        }
        return concatName.toString();
    }

}
