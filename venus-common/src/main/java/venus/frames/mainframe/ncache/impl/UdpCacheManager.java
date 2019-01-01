package venus.frames.mainframe.ncache.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import venus.frames.mainframe.ncache.CacheExpireException;
import venus.frames.mainframe.ncache.ICache;
import venus.frames.mainframe.ncache.ICacheManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存管理实现类
 * 
 * 使用一个基于oscache的缓存类（adminCache）来保存数据。可以保存的数据逻辑上分为三种：
 * 1、管理信息，key为CACHEMANAGEINFOKEY 2、缓存对象（UdpCache对象）， key为缓存对象名，即cacheName
 * 3、数据对象。key为cacheName.key
 * 
 * @author lixizhi
 * 
 */
public class UdpCacheManager implements ICacheManager {

    // 基于oscache的缓存类，用于保存缓存数据
    private static OsCacheImpl adminCache = new OsCacheImpl("udp");

    private final String CACHEMANAGEINFOKEY = "cacheManageInfo";

    private static Log log = LogFactory.getLog(UdpCacheManager.class);

    /*
     * (non-Javadoc)
     * 
     * @see venus.frames.mainframe.ncache.ICacheManager#createCache(java.lang.String,
     *      int)
     */
    public ICache createCache(String cacheName, int refreshPeriod) {
	Map config = new HashMap();
	config.put(ICache.REFRESHPERIOD, new Integer(refreshPeriod));
	return createCache(cacheName, config);
    }

    public ICache createCache(String cacheName) {
        return createCache(cacheName, -1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see venus.frames.mainframe.newcache.ICacheManager#createCache(java.lang.String,
     *      java.util.Map)
     */
    public synchronized ICache createCache(String cacheName, Map config) {
	if (cacheName == null)
	    return null;

	ICache ic = findCache(cacheName);// 若已经存在，就返回已有对象实例
	if (ic != null) {
	    return ic;
	}

	log.info("创建新的缓存示例：" + cacheName);
	ic = new UdpCache(cacheName, config);

	// 处理cacheManager的信息
	CacheManagerInfo cacheMgrInfo = getCacheMgrInfo();
	cacheMgrInfo.updateMgrInfo("add", cacheName);

	adminCache.put(CACHEMANAGEINFOKEY, cacheMgrInfo);

	adminCache.put(cacheName, ic);
	return ic;
    }

    /**
     * 获取缓存管理信息
     * 
     * @return
     */
    private CacheManagerInfo getCacheMgrInfo() {
	CacheManagerInfo cacheInfo;
	try {
	    cacheInfo = (CacheManagerInfo) adminCache.get(CACHEMANAGEINFOKEY);
	} catch (CacheExpireException e) {
	    cacheInfo = new CacheManagerInfo();
	}
	return cacheInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see venus.frames.mainframe.newcache.ICacheManager#get(java.lang.String)
     */
    public Object get(String cacheName, String key) throws CacheExpireException {
	ICache ic = getCache(cacheName);
	return ic.get(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see venus.frames.mainframe.newcache.ICacheManager#put(java.lang.String,
     *      java.lang.Object)
     */
    public void put(String cacheName, String key, Object object) {
	ICache ic = getCache(cacheName);
	ic.put(key, object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see venus.frames.mainframe.newcache.ICacheManager#remove(java.lang.String)
     */
    public void remove(String cacheName, String key) {
	ICache ic = getCache(cacheName);
	ic.remove(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see venus.frames.mainframe.newcache.ICacheManager#removeAll()
     */
    public void remove(String cacheName) {
	ICache ic = findCache(cacheName);
	if (ic == null) {
	    log.warn("remove:缓存" + cacheName + "不存在");
	    return;
	}
	ic.removeAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see venus.frames.mainframe.newcache.ICacheManager#getCache(java.lang.String)
     */
    public ICache getCache(String cacheName) {
	return createCache(cacheName, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see venus.frames.mainframe.newcache.ICacheManager#findCache(java.lang.String)
     */
    public ICache findCache(String cacheName) {
	ICache cache = null;
	try {
	    cache = (ICache) adminCache.get(cacheName);
	} catch (CacheExpireException e) {
	}
	return cache;
    }

    /**
     * @return the adminCache
     */
    OsCacheImpl getAdminCache() {
	return UdpCacheManager.adminCache;
    }

    /*
     * (non-Javadoc)
     * 
     * @see venus.frames.mainframe.newcache.ICacheManager#getCacheSize(java.lang.String)
     */
    public int getCacheSize(String cacheName) {
	CacheInfo cacheInfo = null;
	int cacheSize = 0;
	try {
	    cacheInfo = (CacheInfo) adminCache.get(UdpCache.CacheInfoKey);
	} catch (CacheExpireException e) {
	    log.warn("getCacheSize->获取缓存信息失败！", e);
	    return 0;
	}
	String str = null;
	for (int i = 0; i < cacheInfo.getKeyList().size(); i++) {
	    str = cacheInfo.getKeyList().get(i).toString();
	    if (str.indexOf(cacheName) >= 0) {
		Object obj = null;
		try {
		    obj = getCache(cacheName).get(
			    str.substring(str.indexOf(ICache.seperator) + 1,
				    str.length()));
		} catch (CacheExpireException e) {
		    log.error("getCacheSize->获取" + cacheName + "缓存失败！", e);
		}
		cacheSize = cacheSize + ObjectSizeUtil.sizeof(obj);
	    }
	}
	return cacheSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see venus.frames.mainframe.newcache.ICacheManager#getCacheSize()
     */
    public int getCacheSize() {
	CacheManagerInfo cacheInfo = getCacheMgrInfo();
	List list = cacheInfo.getCacheNames();
	int totalSize = 0;
	for (int i = 0; i < list.size(); i++) {
	    totalSize = totalSize + getCacheSize(list.get(i).toString());
	}
	return totalSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see venus.frames.mainframe.newcache.ICacheManager#getCacheCount()
     */
    public int getCacheCount() {
	CacheManagerInfo cacheInfo = getCacheMgrInfo();
	return cacheInfo.getCount();
    }

    /*
     * (non-Javadoc)
     * 
     * @see venus.frames.mainframe.newcache.ICacheManager#clearCache(java.lang.String)
     */
    public synchronized void clearCache(String cacheName) {
	if (findCache(cacheName) == null)
	    return;
	// 清除cacheName缓存对象里面的内容
	remove(cacheName);
	// 清除cacheName缓存对象本身
	adminCache.remove(cacheName);
	// 从缓存对象列表中移除cacheName
	CacheManagerInfo cacheMgrInfo = getCacheMgrInfo();
	cacheMgrInfo.updateMgrInfo("remove", cacheName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see venus.frames.mainframe.newcache.ICacheManager#getCacheNames()
     */
    public List getCacheNames() {
	CacheManagerInfo cacheInfo = getCacheMgrInfo();
	return cacheInfo.getCacheNames();
    }

    /*
     * (non-Javadoc)
     * 
     * @see venus.frames.mainframe.newcache.ICacheManager#getCacheEntryCount()
     */
    public int getCacheEntryCount(String cacheName) {
	CacheInfo cacheInfo = null;
	int cacheEntryCount = 0;
	try {
	    cacheInfo = (CacheInfo) adminCache.get(UdpCache.CacheInfoKey);
	} catch (CacheExpireException e) {
	    log.warn("getCacheSize->获取缓存信息失败！", e);
	    return 0;
	}
	String str = null;
	for (int i = 0; i < cacheInfo.getKeyList().size(); i++) {
	    str = cacheInfo.getKeyList().get(i).toString();
	    if (str.indexOf(cacheName) >= 0) {
		cacheEntryCount++;
	    }
	}
	return cacheEntryCount;
    }

}
