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
package venus.springsupport;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;

/**
 * <p> Bean Factory utils </p>
 * 1. set and rebuild beanfactory
 * 2. get bean and get root applicationContext
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-24 18:42
 */
public class BeanFactoryHelper {

    private static final Logger logger = Logger.getLogger(BeanFactoryHelper.class);

    private static BeanFactory beanFactory;

    public static BeanFactory getBeanFactory(){
        if (beanFactory==null){
            new BeanInitialization().init();
        }
        return beanFactory;
    }

    public static void setBeanFactory(BeanFactory beanFactory){
        BeanFactoryHelper.beanFactory = beanFactory;
    }

    public static Object getBean(String beanName){
        return getBeanFactory().getBean(beanName);
    }

    /**
     * fetch root applicationContext of spring
     *
     * @return
     */
    public static ApplicationContext fetchRootApplicationContext(){
        return (ApplicationContext) getBeanFactory();
    }


}
