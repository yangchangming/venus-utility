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
package venus.core.event;

import venus.core.Context;

import java.util.EventObject;

/**
 * <p> Event model for venus </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-21 14:36
 */
public class Event extends EventObject {

    private Context context;

    public Event(Object source, Context context){
        super(source);
        this.context = context;
    }

    public Context context(){
        return context;
    }
}
