/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.cache.data.impl;

import venus.portal.cache.data.DataCache;
import venus.portal.cache.data.DataLoader;

/**
 * 缓存数据加载实现。
 */
public class DataLoaderImpl implements DataLoader {
    private DataCache dataCache;

    public void setDataCache(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    /* (non-Javadoc)
     * @see udp.ewp.cache.data.DataLoader#init()
     */
    public void init() {
        // 从数据库中读取要缓存的数据
        dataCache.loadData();
    }

    /* (non-Javadoc)
     * @see udp.ewp.cache.data.DataLoader#destory()
     */
    public void destory() {
        // 清空缓存
        dataCache.removeData();
    }
}
