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

import com.google.common.io.Resources;
import venus.base.Charsets;
import venus.exception.VenusFrameworkException;
import venus.reflect.ClassLoaderUtil;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;

/**
 * <p> Jar util </p>
 * 1. ClassLoader
 *    不指定contextClass时，优先使用Thread.getContextClassLoader()， 如果ContextClassLoader未设置则使用Guava Resources类的ClassLoader,
 *    指定contextClass时，则直接使用该contextClass的ClassLoader
 * 2. 路径
 *    不指定contextClass时，按URLClassLoader的实现, 从jar file中查找resourceName，所以resourceName无需以"/"打头即表示jar file中的根目录，带了"/" 反而
 *    导致JarFile.getEntry(resourceName)时没有返回.指定contextClass时，class.getResource()会先对name进行处理再交给classLoader，打头的"/"的会被去除，
 *    不以"/"打头则表示与该contextClass package的相对路径,会先转为绝对路径.
 * 3. 同名资源
 *    如果有多个同名资源，除非调用getResources()获取全部资源，否则在URLClassLoader中按ClassPath顺序打开第一个命中的Jar文件
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-10-09 16:33
 */
public class Jar {

    /**
     * load all class, from jar file of local file system
     *
     * @param jar
     * @return
     */
    public static Set<Class<?>> loadClassOfJarFile(File jar){
        if (!jar.exists()){
            return null;
        }
        try {
            JarFile jarFile = new JarFile(jar);
            return jarFile.stream().filter(jarEntry -> jarEntry.getName().endsWith(".class"))
                    .map(jarEntry -> loadClassByJarEntry(jarEntry)).collect(Collectors.toSet());
        } catch (IOException e) {
            throw new VenusFrameworkException(e.getCause().getMessage());
        }
    }

    /**
     * load the class, from jarEntity in the jar
     *
     * @param jarEntry
     * @return
     */
    public static Class<?> loadClassByJarEntry(JarEntry jarEntry){
        String jarEntryName = jarEntry.getName();
        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
        return Clazz.loadClass(className);
    }

    /**
     * todo complete
     *
     * @param jar
     * @param sourcePath
     * @throws IOException
     */
    public void buildJar(File jar, File sourcePath) throws IOException {
        JarOutputStream jos = null;
        try {
            jos = new JarOutputStream(new FileOutputStream(jar));
            List<File> fileList = getFiles(sourcePath);
            System.out.println("Jaring " + fileList.size() + " files");
            List<String> lstPaths = new ArrayList<String>(25);
            for (File file : fileList) {
                String path = file.getParent().replace("\\", "/");
                String name = file.getName();

                path = path.substring(sourcePath.getPath().length());
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }
                if (path.length() > 0) {
                    path += "/";
                    if (!lstPaths.contains(path)) {
                        JarEntry entry = new JarEntry(path);
                        jos.putNextEntry(entry);
                        jos.closeEntry();
                        lstPaths.add(path);
                    }
                }
                System.out.println("Adding " + path + name);
                JarEntry entry = new JarEntry(path + name);
                jos.putNextEntry(entry);

                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    byte[] byteBuffer = new byte[1024];
                    int bytesRead = -1;
                    while ((bytesRead = fis.read(byteBuffer)) != -1) {
                        jos.write(byteBuffer, 0, bytesRead);
                    }
                    jos.flush();
                } finally {
                    try {
                        fis.close();
                    } catch (Exception e) {
                    }
                }
                jos.closeEntry();
            }
            jos.flush();
        } finally {
            try {
                jos.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * todo complete
     *
     * @param jar
     * @param outputPath
     * @throws IOException
     */
    public void releaseJar(File jar, File outputPath) throws IOException {
        JarFile jarFile = null;
        try {
            if (outputPath.exists() || outputPath.mkdirs()) {
                jarFile = new JarFile(jar);
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    File path = new File(outputPath + File.separator + entry.getName());
                    if (entry.isDirectory()) {
                        if (!path.exists() && !path.mkdirs()) {
                            throw new IOException("Failed to create output path " + path);
                        }
                    } else {
                        System.out.println("Extracting " + path);

                        InputStream is = null;
                        OutputStream os = null;
                        try {
                            is = jarFile.getInputStream(entry);
                            os = new FileOutputStream(path);

                            byte[] byteBuffer = new byte[1024];
                            int bytesRead = -1;
                            while ((bytesRead = is.read(byteBuffer)) != -1) {
                                os.write(byteBuffer, 0, bytesRead);
                            }
                            os.flush();
                        } finally {
                            try {
                                os.close();
                            } catch (Exception e) {
                            }
                            try {
                                is.close();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            } else {
                throw new IOException("Output path does not exist/could not be created");
            }
        } finally {
            try {
                jarFile.close();
            } catch (Exception e) {
            }
        }
    }

    public List<File> getFiles(File sourcePath){
        return null;
    }

    /**
     * 读取规则见本类注释.
     */
    public static URL asUrl(String resourceName) {
        return Resources.getResource(resourceName);
    }

    /**
     * 读取规则见本类注释.
     */
    public static URL asUrl(Class<?> contextClass, String resourceName) {
        return Resources.getResource(contextClass, resourceName);
    }

    /**
     * 读取规则见本类注释.
     */
    public static InputStream asStream(String resourceName) throws IOException {
        return Resources.getResource(resourceName).openStream();
    }

    /**
     * 读取文件的每一行，读取规则见本类注释.
     */
    public static InputStream asStream(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.getResource(contextClass, resourceName).openStream();
    }

    ////// 读取单个文件内容／／／／／

    /**
     * 读取文件的每一行，读取规则见本类注释.
     */
    public static String toString(String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(resourceName), Charsets.UTF_8);
    }

    /**
     * 读取文件的每一行，读取规则见本类注释.
     */
    public static String toString(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(contextClass, resourceName), Charsets.UTF_8);
    }

    /**
     * 读取文件的每一行，读取规则见本类注释.
     */
    public static List<String> toLines(String resourceName) throws IOException {
        return Resources.readLines(Resources.getResource(resourceName), Charsets.UTF_8);
    }

    /**
     * 读取文件的每一行，读取规则见本类注释.
     */
    public static List<String> toLines(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.readLines(Resources.getResource(contextClass, resourceName), Charsets.UTF_8);
    }

    ///////////// 打开所有同名文件///////

    public static List<URL> getResourcesQuietly(String resourceName) {
        return getResourcesQuietly(resourceName, ClassLoaderUtil.getDefaultClassLoader());
    }

    public static List<URL> getResourcesQuietly(String resourceName, ClassLoader contextClassLoader) {
        try {
            Enumeration<URL> urls = contextClassLoader.getResources(resourceName);
            List<URL> list = new ArrayList<URL>(10);
            while (urls.hasMoreElements()) {
                list.add(urls.nextElement());
            }
            return list;
        } catch (IOException e) {// NOSONAR
            return Collections.emptyList();
        }
    }


}
