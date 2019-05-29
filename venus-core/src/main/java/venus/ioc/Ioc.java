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

import venus.core.Context;
import venus.core.impl.VContext;
import venus.exception.VenusFrameworkException;
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

    private Context context;

    /**
     * Constructor
     */
    public Ioc(Context context){
        this.context = context==null ? new VContext() : context;
        if (context!=null && this.context.ioc()==null){
            if (this.context instanceof VContext){
                ((VContext)this.context).setIoc(this);
            }
        }
    }

    /**
     * injection all bean
     * just depends on interface
     */
    public void doInjection(){
        for (Class<?> clazz : context.beans().loadClass()){
            Object target = context.beans().getBean(clazz);
            for(Field field : clazz.getDeclaredFields()){
                if (field.isAnnotationPresent(Autowired.class)){
                    final Class<?> fieldClass = field.getType();
                    Object fieldValue = loadImplementsBySuper(fieldClass);
                    if (fieldValue!=null){
                        Clazz.setFieldValue(field, target, fieldValue);
                    }else {
                        throw new VenusFrameworkException("No such bean for injection! Target field is " + field);
                    }
                }
            }
        }
    }

    /**
     * load implements of super class
     *
     * @param superClass
     * @return
     */
    protected Object loadImplementsBySuper(final Class<?> superClass){
        return Optional.ofNullable(context.beans().getBean(superClass))
                .orElseGet(() -> {
                    Class<?> implementsClass = loadClassesBySuper(superClass);
                    if (null != implementsClass) {
                        return context.beans().getBean(implementsClass);
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
    protected Class<?> loadClassesBySuper(final Class<?> superClass){
        return context.beans().loadClassesBySuper(superClass).stream().findFirst().orElse(null);
    }

}
