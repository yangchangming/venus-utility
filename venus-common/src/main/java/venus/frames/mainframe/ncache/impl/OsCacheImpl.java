package venus.frames.mainframe.ncache.impl;

import com.opensymphony.oscache.base.CacheEntry;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import venus.frames.mainframe.ncache.CacheExpireException;

/*
 * 基于OSCACHE的缓存类
 * 
 *@author lixizhi
 *
 */
public class OsCacheImpl extends GeneralCacheAdministrator {

    private int refreshPeriod = CacheEntry.INDEFINITE_EXPIRY;

    // 关键字前缀字符;
    private String keyPrefix = "oscache";

    private static final long serialVersionUID = -4397192926052141162L;

    public OsCacheImpl() {
	this(null);
    }

    public OsCacheImpl(String keyPrefix) {
	super();
	if(keyPrefix!=null)
	    this.keyPrefix=keyPrefix;
    }

    public void put(String key, Object value) {
	this.putInCache(this.keyPrefix + "_" + key, value);
    }

    public void remove(String key) {
	this.removeEntry(this.keyPrefix + "_" + key);
    }

    public void removeAll() {
	this.flushAll();
    }

    public Object get(String key) throws CacheExpireException {
	try {
	    return this.getFromCache(this.keyPrefix + "_" + key, refreshPeriod);
	} catch (NeedsRefreshException e) {
	    this.cancelUpdate(this.keyPrefix + "_" + key);
	    throw new CacheExpireException(e);
	}
    }

    public Object get(String key, int refreshPeriod) throws CacheExpireException {
	try {
	    return this.getFromCache(this.keyPrefix + "_" + key, refreshPeriod);
	} catch (NeedsRefreshException e) {
	    this.cancelUpdate(this.keyPrefix + "_" + key);
	    throw new CacheExpireException(e);
	}
    }

}