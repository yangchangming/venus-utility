package venus.portal.cache;

import venus.frames.base.exception.BaseApplicationException;
import venus.frames.mainframe.ncache.CacheExpireException;
import venus.frames.mainframe.ncache.CacheFactory;
import venus.frames.mainframe.ncache.ICache;

/**
 * 对UDP cache api的简单封装。
 */
public class BaseCache {

    private ICache cache = CacheFactory.getCacheManager().createCache("ewp_data", null);

    // 添加被缓存的对象;
    public void put(String key, Object value) {
        cache.put(key, value);
    }

    // 删除被缓存的对象;
    public void remove(String key) {
        cache.remove(key);
    }

    public void removeAll() {
        cache.removeAll();
    }

    // 获取被缓存的对象;
    public Object get(String key) {
        try {
            return cache.get(key);
        } catch (CacheExpireException e) {
            throw new BaseApplicationException(e.getMessage());
        }
    }
}
