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

import venus.exception.VenusFrameworkException;
import venus.ioc.annotation.Autowired;
import venus.lang.Clazz;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * <p> Ioc </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-20 18:45
 */
public final class Ioc {
    private static boolean hasInjection = false;
    private static Ioc instance = null;

    /**
     * Constructor
     */
    private Ioc(){}

    /**
     * Ioc util
     * 1. not recommend injection class by invoke this function for outer
     *
     * @param beans
     * @return
     */
    public static Ioc of(Beans beans){
        if (instance==null){
            instance = new Ioc();
        }
        if (!hasInjection){
            instance.doInjection(beans);
        }
        return instance;
    }

    /**
     * injection all bean, just depends on interface
     * 1. not open for other class, just inner invoke
     * 2. thread safe
     * 3. must be invoke after beans.of()...
     */
    private synchronized void doInjection(Beans beans){
        if (hasInjection){
            return;
        }
        for (Class<?> clazz : beans.loadClass()){
            Object target = beans.getBean(clazz);
            for(Field field : clazz.getDeclaredFields()){
                if (field.isAnnotationPresent(Autowired.class)){
                    final Class<?> fieldClass = field.getType();
                    Object fieldValue = loadImplementsBySuper(fieldClass, beans);
                    if (fieldValue!=null){
                        Clazz.setFieldValue(field, target, fieldValue);
                    }else {
                        throw new VenusFrameworkException("No such bean for injection! Target field is " + field);
                    }
                }
            }
        }
        hasInjection = true;
    }

    /**
     * load implements of super class
     *
     * @param superClass
     * @return
     */
    private Object loadImplementsBySuper(final Class<?> superClass, Beans beans){
        return Optional.ofNullable(beans.getBean(superClass))
                .orElseGet(() -> {
                    Class<?> implementsClass = loadClassesBySuper(superClass, beans);
                    if (null != implementsClass) {
                        return beans.getBean(implementsClass);
                    }else {
                        return null;
                    }
                });
    }

    /**
     * load implements class from bean container, just first when multi implements?
     *
     * @param superClass
     * @return
     */
    private Class<?> loadClassesBySuper(final Class<?> superClass, Beans beans){
        return beans.loadClassesBySuper(superClass).stream().findFirst().orElse(null);
    }

}
