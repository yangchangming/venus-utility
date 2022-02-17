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

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p> Default thread executor </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-21 15:43
 */
public class VThreadExecutor extends ThreadPoolExecutor {

    public static final int DEFAULT_MIN_THREADS = 20;
    public static final int DEFAULT_MAX_THREADS = 200;

    /**
     * 1 minutes
     */
    public static final int DEFAULT_MAX_IDLE_TIME = 60 * 1000;

    protected AtomicInteger currentTaskCount;
    private int maxTaskCount;

    /**
     * Constructor
     *
     * @param threadFactory
     */
    public VThreadExecutor(ThreadFactory threadFactory){
        this(DEFAULT_MIN_THREADS, DEFAULT_MAX_THREADS, threadFactory);
    }

    /**
     * Constructor
     *
     * @param minThreads
     * @param maxThreads
     * @param threadFactory
     */
    public VThreadExecutor(int minThreads, int maxThreads, ThreadFactory threadFactory){
        super(minThreads, maxThreads, DEFAULT_MAX_IDLE_TIME, TimeUnit.MILLISECONDS, new ExecutorQueue(), threadFactory, new AbortPolicy());
        currentTaskCount = new AtomicInteger(0);
        maxTaskCount = maxThreads + maxThreads;
    }

    @Override
    public void execute(Runnable command) {
        int count = currentTaskCount.incrementAndGet();
        if (count > maxTaskCount){
            currentTaskCount.decrementAndGet();
            getRejectedExecutionHandler().rejectedExecution(command, this);
        }
        try{
            super.execute(command);
        } catch (RejectedExecutionException e){
            // there could have been contention around the queue
            if (!((ExecutorQueue) getQueue()).force(command)) {
                currentTaskCount.decrementAndGet();
                getRejectedExecutionHandler().rejectedExecution(command, this);
            }
        }
    }

    public int currentTaskCount() {
        return this.currentTaskCount.get();
    }

    public int maxTaskCount() {
        return maxTaskCount;
    }

    protected void afterExecute(Runnable r, Throwable t) {
        currentTaskCount.decrementAndGet();
    }
}

class ExecutorQueue extends LinkedTransferQueue<Runnable> {

    private static final long serialVersionUID = -265236426751004839L;
    private VThreadExecutor threadExecutor;

    public ExecutorQueue(){
        super();
    }

    /**
     * Constructor
     *
     * @param threadExecutor
     */
    public ExecutorQueue(VThreadExecutor threadExecutor){
        this.threadExecutor = threadExecutor;
    }

    public boolean force(Runnable o) {
        if (threadExecutor.isShutdown()) {
            throw new RejectedExecutionException("Executor not running, can't force a command into the queue");
        }
        // forces the item onto the queue, to be used if the task is rejected
        return super.offer(o);
    }

    // 注：tomcat的代码进行一些小变更
    public boolean offer(Runnable o) {
        int poolSize = threadExecutor.getPoolSize();
        // we are maxed out on threads, simply queue the object
        if (poolSize == threadExecutor.getMaximumPoolSize()) {
            return super.offer(o);
        }
        // we have idle threads, just add it to the queue
        // note that we don't use getActiveCount(), see BZ 49730
        if (threadExecutor.currentTaskCount() <= poolSize) {
            return super.offer(o);
        }
        // if we have less threads than maximum force creation of a new thread
        if (poolSize < threadExecutor.getMaximumPoolSize()) {
            return false;
        }
        // if we reached here, we need to add it to the queue
        return super.offer(o);
    }

}
