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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p> Default thread factory </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-21 15:22
 */
public class VThreadFactory implements ThreadFactory {

    private AtomicInteger threadNo = new AtomicInteger(1);
    private String threadName = "";
    private boolean isDaemon = false;
    private int priority = Thread.NORM_PRIORITY;

    /**
     * Constructor
     *
     * @param threadName
     * @param isDaemon
     * @param priority
     */
    public VThreadFactory(String threadName, boolean isDaemon, int priority) {
        this.threadName = "[" + Venus.frameworkName() + "-" + threadName + "-" + this.threadNo.getAndIncrement() + "]";
        this.isDaemon = isDaemon;
        if (priority>0){
            this.priority = priority;
        }
    }

    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, this.threadName);
        thread.setDaemon(this.isDaemon);
        thread.setPriority(this.priority);
        return thread;
    }
}
