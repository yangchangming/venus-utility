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
package venus.init;

import org.apache.log4j.Logger;
import venus.core.ExtensionLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * <p> Initialization Factory </p>
 * Init all extensions that implements Interface Initialization
 * All init extensions must be assemble by ExtensionLoader
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-05-24 10:33
 */
public class InitializationFactory {

    private static final Logger logger = Logger.getLogger(InitializationFactory.class);

    private static List<Initialization> initializations = new ArrayList<Initialization>();

    public static List<Initialization> fetchInitializations(){
        List<Initialization> initializations = new ArrayList<Initialization>();
        ConcurrentMap<String, Class<Initialization>> _initializations = ExtensionLoader.getExtensionLoader(Initialization.class).loadExtensions();
        if (_initializations==null || _initializations.size()==0){
            return Collections.emptyList();
        }
        // TODO: 18/5/25 initializations must order?
        for (Class<Initialization> initializationClass : _initializations.values()) {
            try {
                Initialization initialization = initializationClass.newInstance();
                if (initialization instanceof Initialization){
                    initializations.add(initialization);
                }
            } catch (InstantiationException e) {
                logger.warn("Initial " + initializationClass.toString() + " failure. [" + e.getMessage() + "]");
            } catch (IllegalAccessException e) {
                logger.warn("Initial " + initializationClass.toString() + " failure. [" + e.getMessage() + "]");
            }
        }
        return initializations;
    }

    public static void init(){
        if (initializations==null || initializations.size()==0){
            initializations = fetchInitializations();
        }
        for (Initialization initialization : initializations) {
            initialization.init();
        }
    }
}
