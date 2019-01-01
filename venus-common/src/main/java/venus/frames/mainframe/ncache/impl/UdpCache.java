package venus.frames.mainframe.ncache.impl;

import com.opensymphony.oscache.base.CacheEntry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import venus.frames.mainframe.ncache.CacheExpireException;
import venus.frames.mainframe.ncache.CacheFactory;
import venus.frames.mainframe.ncache.ICache;

import java.util.Map;

/**
 * 缓存类，实现ICache接口，提供常用的缓存方法， 最终会通过UdpCacheManager的adminCache来进行缓存操作。
 * 
 * 在调用put，get，remove等方法进行缓存操作时， 会根据cacheName属性和方法的key参数拼成真正的key,
 * 即cacheName.key，来进行缓存操作。并且需要同时更新cacheInfo。
 * 
 * 
 * @author lixizhi
 * 
 */
public class UdpCache implements ICache {

	private String cacheName = null;

	public static final String CacheInfoKey = "cacheInfoKey";

	private int refreshPeriod = CacheEntry.INDEFINITE_EXPIRY;

	private static Log log = LogFactory.getLog(UdpCache.class);

	public UdpCache(String cacheName, Map config) {
		this.cacheName = cacheName;
		if ((config != null) && config.get(ICache.REFRESHPERIOD) != null) {
			this.refreshPeriod = ((Integer) config.get(ICache.REFRESHPERIOD))
					.intValue();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see venus.frames.mainframe.newcache.ICache#init(java.util.Map)
	 */
	public void init(Map config) {
		if (config == null) {
			log.warn("init方法：config为空");
			return;
		}
		if (config.get(ICache.REFRESHPERIOD) != null)
			this.refreshPeriod = ((Integer) config.get(ICache.REFRESHPERIOD))
					.intValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see venus.frames.mainframe.newcache.ICache#get(java.lang.String)
	 */
	public Object get(String key) throws CacheExpireException {
		return getRealCache().get(getRealKey(key), refreshPeriod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see venus.frames.mainframe.newcache.ICache#put(java.lang.String,
	 *      java.lang.Object)
	 */
	public void put(String key, Object object) {
		// 保存数据到缓存中
		String realKey = getRealKey(key);
		getRealCache().put(realKey, object);

		// 更新缓存信息
		CacheInfo cacheInfo = null;
		cacheInfo = getCacheInfo();
		cacheInfo.updateCacheInfo("add", realKey);
		getRealCache().put(UdpCache.CacheInfoKey, cacheInfo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see venus.frames.mainframe.newcache.ICache#remove(java.lang.String)
	 */
	public void remove(String key) {
		String realKey = getRealKey(key);
		getRealCache().remove(realKey);
		// 更新缓存信息
		CacheInfo cacheInfo = null;
		cacheInfo = getCacheInfo();
		cacheInfo.updateCacheInfo("remove", realKey);
		getRealCache().put(UdpCache.CacheInfoKey, cacheInfo);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see venus.frames.mainframe.newcache.ICache#removeAll()
	 */
	public void removeAll() {
		CacheInfo cacheInfo = null;
		try {
			cacheInfo = (CacheInfo) getRealCache().get(UdpCache.CacheInfoKey);
		} catch (CacheExpireException e) {
			log.error("removeAll->get cache fail");
			return;
		}
		String str = "";
		Object[] keys = cacheInfo.getKeyList().toArray();
		for (int i = 0; i < keys.length; i++) {
			str = keys[i].toString();
			if (str.indexOf(this.cacheName) >= 0) {
				remove(str.substring(str.indexOf(ICache.seperator) + 1, str
						.length()));
			}
		}
	}

	/**
	 * 获取真正的缓存示例
	 * 
	 * @return
	 */
	protected OsCacheImpl getRealCache() {
		return ((UdpCacheManager) CacheFactory.getCacheManager())
				.getAdminCache();
	}

	/**
	 * 获取逻辑分层主键
	 * 
	 * @param key
	 * @return
	 */
	private String getRealKey(String key) {
		return cacheName + seperator + key;
	}

	/**
	 * 获取缓存信息
	 * 
	 * @return
	 */
	private CacheInfo getCacheInfo() {
		CacheInfo cacheInfo;
		try {
			cacheInfo = (CacheInfo) getRealCache().get(UdpCache.CacheInfoKey);
		} catch (CacheExpireException e) {
			cacheInfo = new CacheInfo();
		}
		return cacheInfo;
	}
}
