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

import venus.core.event.EventEngine;
import venus.ioc.Beans;
import venus.ioc.Ioc;

/**
 * <p> Application context, and super context </p>
 * 1. a context must be new in proper location, such as system initial or http request
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-20 18:36
 */
public interface Context {

    /**
     * fetch ioc
     *
     * @return
     */
    Ioc ioc();

    /**
     * fetch venus, about app info such as.
     *
     * @return
     */
    Venus venus();

    /**
     * fetch beans
     *
     * @return
     */
    Beans beans();

    /**
     * fetch event engine
     *
     * @return
     */
    EventEngine eventEngine();
}
