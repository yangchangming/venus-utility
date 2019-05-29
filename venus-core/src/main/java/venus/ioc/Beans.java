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
package venus.ioc;


import org.apache.log4j.Logger;
import venus.aop.Aspect;
import venus.lang.Clazz;
import venus.lang.Scanner;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p> Bean container, must singleton and do not extends and new instance by other class </p>
 * usage: Beans.of()......
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-28 17:14
 */
public final class Beans {

    private static Logger logger = Logger.getLogger(Beans.class);
    private static Map<Class<?>, Object> beanContainer = new ConcurrentHashMap<Class<?>, Object>();
    private static Beans instance = new Beans();
    private static boolean hasLoading = false;
    private static final List<Class<? extends Annotation>> BEAN_ANNOTATION_TYPE = Arrays.asList
            (Component.class, Controller.class, Service.class, Repository.class, Aspect.class);

    /**
     * Constructor
     */
    private Beans(){}

    public static Beans of(){
        if (instance==null){
            Beans.instance = new Beans();
        }
        if (!hasLoading){
            instance.loading();
        }
        return Beans.instance;
    }

    /**
     * loading all bean, that will be find by scanner
     * is not invoke by outer class
     *
     * @throws Exception
     */
    private synchronized static void loading(){
        if (hasLoading){
            logger.warn("all bean has loading.");
            return;
        }
        Set<Class<?>> classes = Clazz.loadClassByPackage(Scanner.classPath());
        classes.stream().filter(clz -> {
          for (Class<? extends Annotation> annotation : BEAN_ANNOTATION_TYPE){
              if (clz.isAnnotationPresent(annotation)) {
                  return true;
              }
          }
          return false;
        }).forEach(clz -> beanContainer.put(clz, Clazz.newInstance(clz)));
        hasLoading = true;
    }

    public Object getBean(Class<?> clazz){
        return beanContainer.get(clazz);
    }

    public Set<Object> getBeans(){
        return new HashSet<>(beanContainer.values());
    }

    public Object addBean(Class<?> clazz, Object object){
        beanContainer.put(clazz, object);
        return getBean(clazz);
    }

    /**
     * erase bean specified by clazz
     *
     * @param clazz
     */
    public void eraseBean(Class<?> clazz){
        beanContainer.remove(clazz);
    }

    public int size(){
        return beanContainer.size();
    }

    /**
     * load all class
     *
     * @return
     */
    public Set<Class<?>> loadClass(){
        return beanContainer.keySet();
    }

    /**
     * load the class by annotation specified by the param annotation
     *
     * @param annotation
     * @return
     */
    public Set<Class<?>> loadClassByAnnotation(Class<? extends Annotation> annotation){
        return loadClass().stream().filter(clz -> clz.isAnnotationPresent(annotation)).collect(Collectors.toSet());
    }

    /**
     * load implements class of the super class or interface specified by interfaceClass param
     *
     * @param interfaceClass
     * @return
     */
    public Set<Class<?>> loadClassesBySuper(Class<?> interfaceClass) {
        return beanContainer.keySet().stream().filter(interfaceClass::isAssignableFrom)
                .filter(clz -> !clz.equals(interfaceClass)).collect(Collectors.toSet());
    }

}
