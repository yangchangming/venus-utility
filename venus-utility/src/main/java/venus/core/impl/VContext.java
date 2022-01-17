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
package venus.core.impl;

import venus.core.Context;
import venus.core.Venus;
import venus.core.event.EventEngine;
import venus.ioc.Beans;
import venus.ioc.Ioc;

/**
 * <p> Default context implements </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-20 19:06
 */
public class VContext implements Context {

    private Ioc ioc;
    private EventEngine eventEngine;
    private Venus venus;

    /**
     * Constructor
     */
    public VContext(){}

    /**
     * Constructor
     *
     * @param ioc
     * @param venus
     */
    public VContext(Ioc ioc, Venus venus){
        this.ioc = ioc;
        this.venus = venus;
        this.eventEngine = new EventEngine();
    }

    @Override
    public Ioc ioc() {
        return ioc;
    }

    @Override
    public Venus venus() {
        return venus;
    }

    @Override
    public Beans beans() {
        return Beans.of();
    }

    @Override
    public EventEngine eventEngine() {
        if (this.eventEngine==null){
            this.eventEngine = new EventEngine();
        }
        return this.eventEngine;
    }

    public void setVenus(Venus venus) {
        this.venus = venus;
    }

    public void setIoc(Ioc ioc) {
        this.ioc = ioc;
    }
}
