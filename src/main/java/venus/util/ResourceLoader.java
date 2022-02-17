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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import venus.exception.VenusFrameworkException;
import venus.lang.Jar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

/**
 * <p> Resource loader over all system </p>
 *
 * 1. 兼容文件url为无前缀, classpath:, file:// 三种方式的Resource读取工具集
 *    e.g: classpath:com/myapp/config.xml, file:///data/config.xml, /data/config.xml
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-11 12:59
 */
public class ResourceLoader {
    private static final Logger logger = LoggerFactory.getLogger(ResourceLoader.class);
    private static final String CLASSPATH_PREFIX = "classpath:";
    private static final String URL_PROTOCOL_FILE = "file";

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

    /**
     * 兼容无前缀, classpath:, file:// 的情况获取文件
     * 如果以classpath: 定义的文件不存在会抛出IllegalArgumentException异常，以file://定义的则不会
     */
    public static File asFile(String generalPath) throws IOException {
        if (StringUtils.startsWith(generalPath, CLASSPATH_PREFIX)) {
            String resourceName = StringUtils.substringAfter(generalPath, CLASSPATH_PREFIX);
            return getFileByURL(Jar.asUrl(resourceName));
        }
        try {
            // try URL
            return getFileByURL(new URL(generalPath));
        } catch (MalformedURLException ex) {
            // no URL -> treat as file path
            return new File(generalPath);
        }
    }

    /**
     * 兼容无前缀, classpath:, file:// 的情况打开文件成Stream
     */
    public static InputStream asStream(String generalPath) throws IOException {
        if (StringUtils.startsWith(generalPath, CLASSPATH_PREFIX)) {
            String resourceName = StringUtils.substringAfter(generalPath, CLASSPATH_PREFIX);
            return Jar.asStream(resourceName);
        }

        try {
            // try URL
            return FileUtil.asInputStream(getFileByURL(new URL(generalPath)));
        } catch (MalformedURLException ex) {
            // no URL -> treat as file path
            return FileUtil.asInputStream(generalPath);
        }
    }

    private static File getFileByURL(URL fileUrl) throws FileNotFoundException {
        Validate.notNull(fileUrl, "Resource URL must not be null");
        if (!URL_PROTOCOL_FILE.equals(fileUrl.getProtocol())) {
            throw new FileNotFoundException("URL cannot be resolved to absolute file path "
                    + "because it does not reside in the file system: " + fileUrl);
        }
        try {
            return new File(toURI(fileUrl.toString()).getSchemeSpecificPart());
        } catch (URISyntaxException ex) { // NOSONAR
            // Fallback for URLs that are not valid URIs (should hardly ever happen).
            return new File(fileUrl.getFile());
        }
    }

    public static URI toURI(String location) throws URISyntaxException {
        return new URI(StringUtils.replace(location, " ", "%20"));
    }
}
