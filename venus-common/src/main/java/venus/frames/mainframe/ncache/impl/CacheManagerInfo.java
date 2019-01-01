package venus.frames.mainframe.ncache.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 缓存管理类的相关信息
 * 
 * @author lixizhi
 * 
 */
public class CacheManagerInfo implements Serializable {

    private List cacheNames = Collections.synchronizedList(new ArrayList());// 缓存对象名称列表

    /**
     * @return the count
     */
    public int getCount() {
	return cacheNames.size();
    }

    /**
     * @return the cacheNames
     */
    public List getCacheNames() {
	return cacheNames;
    }

    /**
     * 更新缓存管理信息
     * 
     * @param flag
     * @param cacheName
     */
    public synchronized void updateMgrInfo(String flag, String cacheName) {
	if (flag.equals("add")) {
	    if (!cacheNames.contains(cacheName))
		cacheNames.add(cacheName);
	} else if (flag.equals("remove")) {
	    if (cacheNames.contains(cacheName))
		cacheNames.remove(cacheName);
	}
    }

}
