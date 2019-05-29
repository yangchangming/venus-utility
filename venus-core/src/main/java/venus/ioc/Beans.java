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
import venus.lang.Scanner;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p> Bean container, must singleton and do not extends and new instance by other class </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-28 17:14
 */
public final class Beans {

    private static Logger logger = Logger.getLogger(Beans.class);
    private static Map<Class<?>, Object> beanContainer = new ConcurrentHashMap<Class<?>, Object>();
    private static Beans instance = new Beans();
    private boolean hasLoading = false;

    /**
     * Constructor
     */
    private Beans(){}

    public static Beans of(){
        if (instance==null){
            Beans.instance = new Beans();
        }
        return Beans.instance;
    }

    /**
     * loading all bean, that will be find by scanner
     *
     * @throws Exception
     */
    public void loading(){
        String classPath = Scanner.classPath();
        if (hasLoading){
            logger.warn("all bean has loading.");
            return;
        }



    }


}
