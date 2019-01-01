package venus.frames.mainframe.ncache;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 缓存管理接口类，提供缓存服务
 * 
 * @author lixizhi
 * 
 */
public interface ICacheManager<T> extends Serializable {

    /**
     * 创建缓存对象
     * @param cacheName
     * @param config
     * @return
     */
    public ICache<T> createCache(String cacheName, Map config);

    /**
     * 创建缓存对象
     * @param cacheName
     * @param refreshPeriod
     * @return
     */
    public ICache<T> createCache(String cacheName, int refreshPeriod);

    /**
     * 根据名称创建缓存命名空间。
     * @param cacheName
     * @return
     */
    public ICache<T> createCache(String cacheName);

    /**
     * 获取缓存对象
     * @param cacheName
     * @return
     */
    public ICache<T> getCache(String cacheName);

    /**
     * 删除缓存对象
     * 
     * @param cacheName     
     */
    public void clearCache(String cacheName);
    
    /**
     * 查找缓存对象
     * @param cacheName
     * @return
     */
    public ICache<T> findCache(String cacheName);

    /**
     * 获取缓存对象数
     * @return
     */
    public int getCacheCount();

    /**
     * 获取指定缓存对象内部存储的对象数量
     * @return
     */
    public int getCacheEntryCount(String cacheName);
    
    /**
     * 获取缓存大小
     * @param cacheName
     * @return
     */
    public int getCacheSize(String cacheName); 

    /**
     * 获取缓存大小
     * @return
     */
    public int getCacheSize(); 

    /**
     * 获取缓存大小
     * @return
     */
    public List getCacheNames(); 
    
    /**
     * 向缓存中增加对象
     * 
     * @param cacheName  
     * @param key
     * @param value
     */
    public void put(String cacheName, String key, T value);

    /**
     * 从缓存中获取对象，如果缓存已经过期，则抛出异常
     * 
     * @param cacheName
     * @param key
     * @return
     */
    public T get(String cacheName, String key) throws CacheExpireException;

    /**
     * 从缓存对象中移除数据
     * 
     * @param cacheName
     * @param key
     */
    public void remove(String cacheName, String key);

    
}
