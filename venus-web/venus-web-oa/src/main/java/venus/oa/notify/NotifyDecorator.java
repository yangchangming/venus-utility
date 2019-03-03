/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.notify;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public interface NotifyDecorator {
    /**
     * 通知功能
     * @param message
     * @return
     */
    public Object notify(Object message);
}

