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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import venus.core.VThreadExecutor;
import venus.core.VThreadFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * <p> Event engine for venus </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-05-21 14:48
 */
public class EventEngine {

    private static final Logger logger = LoggerFactory.getLogger(EventEngine.class);
    private ExecutorService executorService = new VThreadExecutor(new VThreadFactory("event.engine", false, Thread.NORM_PRIORITY));
    private Set<Listener> eventListenerSet = new HashSet<Listener>();
    private Map<Class<? extends Event>,ListenerRegistry> cachedEventListeners = new HashMap<Class<? extends Event>, ListenerRegistry>();

    /**
     * 注册监听器
     *
     * @param listener
     */
    public void addFOPListener(Listener listener){
        eventListenerSet.add(listener);
    }

    /**
     * 删除监听器
     *
     * @param listener
     */
    public void removeFOPListener(Listener listener){
        eventListenerSet.remove(listener);
    }

    /**
     * 清除所有注册的监听器
     */
    public void removeAllFOPListeners(){
        eventListenerSet.clear();
    }

    /**
     * 组播事件给相应的注册监听器 - 异步执行
     *
     * @param event
     */
    public void multicastEvent(final Event event){
        try {
            List<Listener> eventListeners = getEventListener(event);
            for (final Listener listener : eventListeners) {
                if(executorService!=null){
                    executorService.execute(new Runnable() {
                        public void run() {
                            if (logger.isInfoEnabled()){
                                logger.info("开始异步处理事件[" + event.getClass().getName() + "]......");
                            }
                            listener.onEvent(event);
                        }
                    });
                }else {
                    if (logger.isInfoEnabled()){
                        logger.info("开始同步处理事件[" + event.getClass().getName() + "]......");
                    }
                    listener.onEvent(event);
                }
            }
        }catch (Exception e){
            logger.error("处理事件[" + event.getClass().getName() + "]发生错误!");
        }
    }


    /**
     * 获取相应的事件监听器
     *
     * @param event
     * @return
     */
    protected List<Listener> getEventListener(Event event){
        Class<? extends Event> eventClazz = event.getClass();
        if (!cachedEventListeners.containsKey(eventClazz)){
            LinkedList<Listener> listeners = new LinkedList<Listener>();
            if (eventListenerSet!=null && eventListenerSet.size()>0){
                for (Listener listener : eventListenerSet) {
                    if (supportedEvent(listener,eventClazz)) {
                        listeners.add(listener);
                    }
                }
                sortFOPEventListener(listeners);
            }
            ListenerRegistry listenerRegistry = new ListenerRegistry(listeners);
            cachedEventListeners.put(eventClazz,listenerRegistry);
        }
        return cachedEventListeners.get(eventClazz).getFopEventListenerList();
    }

    /**
     * 判断监听器类型和事件类型是否匹配
     *
     * @param listener
     * @param eventClazz
     * @return
     */
    protected boolean supportedEvent(Listener listener, Class<? extends Event> eventClazz) {
        //todo 去掉spring依赖
//        Class typeArg = GenericTypeResolver.resolveTypeArgument(listener.getClass(), Listener.class);
//        if (typeArg == null || typeArg.equals(Event.class)) {
//            Class targetClass = AopUtils.getTargetClass(listener);
//            if (targetClass != listener.getClass()) {
//                typeArg = GenericTypeResolver.resolveTypeArgument(targetClass, Listener.class);
//            }
//        }
        Class typeArg = null;
        return (typeArg == null || typeArg.isAssignableFrom(eventClazz));
    }

    protected void sortFOPEventListener(LinkedList<Listener> fopEventListeners){
        Collections.sort(fopEventListeners, new Comparator<Listener>() {
            public int compare(Listener listener1, Listener listener2) {
                if (listener1.getOrder() > listener2.getOrder()) {
                    return 1;
                } else if (listener1.getOrder() < listener2.getOrder()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

    /**
     * inner Class
     */
    private class ListenerRegistry {
        public List<Listener> fopEventListenerList;

        private ListenerRegistry(List<Listener> fopEventListenerList) {
            this.fopEventListenerList = fopEventListenerList;
        }
        public List<Listener> getFopEventListenerList() {
            return fopEventListenerList;
        }
    }

}
