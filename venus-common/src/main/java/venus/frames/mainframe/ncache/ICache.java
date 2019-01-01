package venus.frames.mainframe.ncache;

import java.io.Serializable;

/**
 * 缓存接口类，提供缓存服务
 * 根据声明时的泛型，返回该类型的对象
 * @author lixizhi
 * 
 */
public interface ICache<T> extends Serializable {
    
    public static final String seperator = ".";    

    public final static String REFRESHPERIOD = "refreshPeriod";// 刷新时间

    /**
     * 向缓存中增加对象
     * 
     * @param key
     * @param object 使用泛型
     */
    public void put(String key, T object);

    /**
     * 从缓存中获取对象，如果缓存已经过期，则抛出异常
     * 
     * @param key
     * @return
     */
    public T get(String key) throws CacheExpireException;

    /**
     * 从缓存移除对象
     * 
     * @param key
     */
    public void remove(String key);

    /**
     * 移除所有缓存对象
     */
    public void removeAll();

}
