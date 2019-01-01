/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.service.notify.impl;

import venus.oa.service.notify.NotifyDecorator;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public abstract class AbstractNotifyDecorator implements NotifyDecorator {
    protected NotifyDecorator decorator;
    
    public AbstractNotifyDecorator(){};
    
    public AbstractNotifyDecorator(NotifyDecorator decorator){
        this.decorator = decorator;
    }
    
    /**
     * 设置被修饰的对象
     * @param decorator the decorator to set
     */
    public void setDecorator(NotifyDecorator decorator) {
        this.decorator = decorator;
    }

    /* (non-Javadoc)
     * @see venus.authority.service.notify.NotifyDecorator#notify(java.lang.Object)
     * note : decorator.notify(message); must be use!
     */
    public abstract Object notify(Object message) ;
    }

