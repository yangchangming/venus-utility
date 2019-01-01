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
package venus.core;


import org.apache.log4j.Logger;
import venus.exception.VenusFrameworkException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p> ExtensionLoader </p>
 * 1. a only tools for all extension-class manage
 * 2. a generic Class for the ExtensionLoader specified by T
 * 3. a only tools for all ExtensionLoader-class manage
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-03-19 16:16
 */
public class ExtensionLoader<T> {

    private static Logger logger = Logger.getLogger(ExtensionLoader.class);

    private static String PREFIX_EXTENSION = "META-INF/service/";

    private static ConcurrentMap<Class<?>, ExtensionLoader<?>> extensionLoaders = new ConcurrentHashMap<Class<?>, ExtensionLoader<?>>();

    private ConcurrentMap<String, Class<T>> extensionClazzes = new ConcurrentHashMap<String, Class<T>>();

    private Class<T> extension;

    private ClassLoader classLoader;

    private boolean initialized = false;

    /**
     * Constructor
    */
    private ExtensionLoader(Class<T> extension, ClassLoader classLoader){
        this.extension = extension;
        this.classLoader = classLoader;
        if (!initialized){
            init();
        }
    }

    /**
     *  fetch extensionloader from only extensionLoaders
     *  null if initial extensionloader failure
     *
     * @param extension
     * @param <T>
     * @return
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> extension){
        if (extension == null){
            return null;
        }
        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>)extensionLoaders.get(extension);
        if (extensionLoader==null){
            extensionLoader = initialExtensionLoader(extension);
        }
        return extensionLoader;
    }

    private static <T> ExtensionLoader<T> initialExtensionLoader(Class<T> extension){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ExtensionLoader instance = new ExtensionLoader(extension, classLoader);
        extensionLoaders.putIfAbsent(extension, instance);
        return (ExtensionLoader<T>)extensionLoaders.get(extension);
    }

    private void checkInit(){
        if (!this.initialized){
            init();
        }
    }

    /**
     * initial extension from configuration
     *
     * @return
     */
    private boolean init(){
        if (this.extension==null){
            logger.error("Init extension failure. The ExtensionLoader not be instance.");
            throw new VenusFrameworkException("Init extension failure. The ExtensionLoader not be instance.");
        }
        if (!initialized){
            try {
                Class<T> clazz;
                this.extensionClazzes.clear();
                List<String> extensionNames = parseConfig(this.extension);
                for (String extensionName : extensionNames) {
                    if (this.classLoader==null){
                        clazz = (Class<T>) Class.forName(extensionName);
                    }else {
                        clazz = (Class<T>) Class.forName(extensionName, true, this.classLoader);
                    }
                    addExtension(clazz);
                }
            }catch (Exception e){
                logger.error("Init extension failure. " + e.getMessage());
                throw new VenusFrameworkException(e.getMessage());
            }
            initialized = true;
        }
        return initialized;
    }

    private List<String> parseConfig(Class<T> extensionType) {
        InputStream in = null;
        BufferedReader bufferedReader = null;
        List<String> extensionNames = new ArrayList<String>();
        String extensionFullName = PREFIX_EXTENSION + extensionType.getName();
        try {
            Enumeration<URL> urls;
            if (this.classLoader==null){
                //todo error?
                urls = ClassLoader.getSystemResources(extensionFullName);
            }else {
                urls = this.classLoader.getResources(extensionFullName);
            }

            while (urls.hasMoreElements()){
                URL url = urls.nextElement();
                in = url.openStream();
                if (in != null){
                    bufferedReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String line;
                    while (bufferedReader!=null && (line=bufferedReader.readLine())!=null){
                        checkExtensionNameSyntax(line);
                        if (!extensionNames.contains(line)){
                            extensionNames.add(line);
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error("Error for parse configuration of extension. " + extensionType.getName());
            throw new VenusFrameworkException("Error for parse configuration of extension. " + extensionType.getName());
        }finally {
            try {
                if (in!=null){
                    in.close();
                    in = null;
                }
                if (bufferedReader!=null){
                    bufferedReader.close();
                    bufferedReader = null;
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return extensionNames;
    }

    /**
     * check extension class name syntax
     *
     * @param extensionName
     */
    private void checkExtensionNameSyntax(String extensionName){
        int ci = extensionName.indexOf('#');
        if (ci >= 0) {
            extensionName = extensionName.substring(0, ci);
        }
        extensionName = extensionName.trim();
        int n = extensionName.length();
        if (n != 0) {
            if ((extensionName.indexOf(' ') >= 0) || (extensionName.indexOf('\t') >= 0)){
                logger.error("Illegal extension configuration-file syntax." + extensionName);
                throw new VenusFrameworkException("Illegal extension configuration-file syntax." + extensionName);
            }
            int cp = extensionName.codePointAt(0);
            if (!Character.isJavaIdentifierStart(cp)){
                logger.error("Illegal extension-class name: " + extensionName);
                throw new VenusFrameworkException("Illegal extension-class name: " + extensionName);
            }
            for (int i= Character.charCount(cp); i < n; i += Character.charCount(cp)) {
                cp = extensionName.codePointAt(i);
                if (!Character.isJavaIdentifierPart(cp) && (cp != '.')){
                    logger.error("Illegal extension-class name: " + extensionName);
                    throw new VenusFrameworkException("Illegal extension-class name: " + extensionName);
                }
            }
        }
    }

    /**
     * get spi annotation name for class specified by clazz
     * return simple name of class if annotation spiMeta is null
     *
     * @param clazz
     * @return
     */
    private String getSpiName(Class<T> clazz){
        SpiMeta spiMeta = clazz.getAnnotation(SpiMeta.class);
        String spiName = (spiMeta !=null && !"".equals(spiMeta.name())) ? spiMeta.name() : clazz.getSimpleName();
        return spiName;
    }

    /**
     * check class, such below:
     * 1. if inherit from the class
     * 2. if public class
     * 3. if public constructor and if no-args constructor
     *
     * @param clazz
     */
    private void checkExtensionClassType(Class<T> clazz){
        if (!this.extension.isAssignableFrom(clazz)){
            logger.error("Illegal inherit from " + this.extension.getName() + " of " + clazz.getName());
            throw new VenusFrameworkException("Illegal inherit from " + this.extension.getName() + " of " + clazz.getName());
        }
        if (!Modifier.isPublic(clazz.getModifiers())){
            logger.error("Is not public for " + clazz.getName());
            throw new VenusFrameworkException("Is not public." + clazz.getName());
        }
        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors==null || constructors.length==0){
            logger.error("Has no constructor for " + clazz.getName());
            throw new VenusFrameworkException("Has no constructor for " + clazz.getName());
        }
        for (Constructor constructor : constructors){
            if (constructor.getParameterTypes().length==0 && Modifier.isPublic(constructor.getModifiers())){
                return;
            }
        }
        logger.error("Has no public args-no constructor for " + clazz.getName());
        throw new VenusFrameworkException("Has no public args-no constructor for " + clazz.getName());
    }

    /**
     * add extension extends-class to container extensionClazzes
     *
     * @param clazz
     */
    public synchronized void addExtension(Class<? extends T> clazz){
        if (clazz==null){
            return;
        }
        Class<T> clz = (Class<T>)clazz;
        checkExtensionClassType(clz);
        String spiName = getSpiName(clz);
        if (extensionClazzes.containsKey(spiName)){
            logger.warn("Extension has already exist for " + spiName);
        }else {
            extensionClazzes.putIfAbsent(spiName, clz);
        }
    }

    /**
     * fetch the extension class by specified spiName
     *
     * @param spiName
     * @return
     */
    public Class<T> loadExtension(String spiName){
        if (spiName==null || "".equals(spiName)){
            return null;
        }
        checkInit();
        return this.extensionClazzes.get(spiName);
    }

    /**
     * fetch all extension class of this object
     *
     * @return
     */
    public ConcurrentMap<String, Class<T>> loadExtensions(){
        checkInit();
        return this.extensionClazzes;
    }

    /**
     * fetch the extension instance by spiName
     *
     * @param spiName
     * @return
     */
    public T loadExtensionInstance(String spiName){
        if (spiName==null || "".equals(spiName)){
            return null;
        }
        checkInit();
        Class<T> clazz = this.extensionClazzes.get(spiName);
        if (clazz==null){
            return null;
        }
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            logger.warn("Extension instance failure. " + clazz.getName());
            return null;
        }
    }

}
