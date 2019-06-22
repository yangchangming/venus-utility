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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

/**
 * <p> Class util </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-29 09:04
 */
public final class Clazz {

    public static String FILE_PROTOCAL = "file";
    public static String JAR_PROTOCAL = "jar";

    public static <T> T newInstance(Class<?> clazz){
        try {
            if (clazz!=null && !clazz.isInterface()){
                return (T) clazz.newInstance();
            }else {
                throw new VenusFrameworkException("Class must not a interface when new instance.");
            }
        } catch (InstantiationException e) {
            throw new VenusFrameworkException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new VenusFrameworkException(e.getMessage());
        }
    }

    public static Class<?> loadClassByPath(Path classPath, Path basePath, String basePackage) {
        String packageName = classPath.toString().replace(basePath.toString(), "");
        String className = (basePackage + packageName).replace("/", ".").replace("\\", ".").replace(".class", "");
        className = className.charAt(0) == '.' ? className.substring(1) : className;
        return loadClass(className);
    }

    public static Class<?> loadClassByJar(JarEntry jarEntry){
        String jarEntryName = jarEntry.getName();
        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
        return loadClass(className);
    }

    public static Class<?> loadClass(String className){
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new VenusFrameworkException(e.getMessage());
        }
    }

    /**
     * load all class set, by specified base package
     * class come from file or jar
     *
     * @param basePackage
     * @return
     */
    public static Set<Class<?>> loadClassByPackage(String  basePackage){
        if (basePackage==null || "".equals(basePackage)){
            basePackage = "";
        }
        URL url = java.lang.Thread.currentThread().getContextClassLoader().getResource(basePackage.replace(".", "/"));
        if (url==null){
            throw new VenusFrameworkException("base backage is null. or classpath is null.");
        }
        try {
            if (url.getProtocol().equalsIgnoreCase(Clazz.FILE_PROTOCAL)){
                File file = new File(url.getFile());
                Path path = file.toPath();
                final String finalBasePackage = basePackage;
                return Files.walk(path).filter(_path -> _path.toFile().getName().endsWith(".class"))
                        .map(_path -> loadClassByPath(_path, path, finalBasePackage))
                        .collect(Collectors.toSet());
            }else if(url.getProtocol().equalsIgnoreCase(Clazz.JAR_PROTOCAL)){
                JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
                return jarURLConnection.getJarFile().stream().filter(jarEntry -> jarEntry.getName().endsWith(".class"))
                        .map(jarEntry -> loadClassByJar(jarEntry))
                        .collect(Collectors.toSet());
            }else {
                return Collections.emptySet();
            }
        }catch (IOException exception){
            throw new VenusFrameworkException(exception.getMessage());
        }
    }


    /**
     * set field value
     *
     * @param field
     * @param target
     * @param value
     */
    public static void setFieldValue(Field field, Object target, Object value) {
        if (!field.isAccessible()){
            field.setAccessible(true);
        }
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new VenusFrameworkException(e.getMessage());
        }
    }

    /**
     * true if class is primitive
     *
     * @param clz
     * @return
     */
    public static boolean isPrimitive(Class<?> clz) {
        return clz == boolean.class
                || clz == Boolean.class
                || clz == double.class
                || clz == Double.class
                || clz == float.class
                || clz == Float.class
                || clz == short.class
                || clz == Short.class
                || clz == int.class
                || clz == Integer.class
                || clz == long.class
                || clz == Long.class
                || clz == String.class
                || clz == byte.class
                || clz == Byte.class
                || clz == char.class
                || clz == Character.class;
    }

    /**
     * true if clz implements interface specified by interfaceClz
     *
     * @param clz
     * @param interfaceClz
     * @return
     */
    public static boolean isImplementsInterface(Class<?> clz, Class<?> interfaceClz){
        if (clz==null || interfaceClz==null || !interfaceClz.isInterface()){
            return false;
        }
        for (Class<?> clzInterface : clz.getInterfaces()) {
            if (interfaceClz==clzInterface) return true;
        }
        return false;
    }

}
