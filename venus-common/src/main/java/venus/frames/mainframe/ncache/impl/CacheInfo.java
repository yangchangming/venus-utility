/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.frames.mainframe.ncache.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 缓存类的相关信息
 * 
 * @author lixizhi
 * 
 */
public class CacheInfo implements Serializable {

    private List keyList = Collections.synchronizedList(new ArrayList());

    /**
     * @return the keyList
     */
    public List getKeyList() {
	return keyList;
    }

    /**
     * 更新缓存信息
     * 
     * @param flag
     * @param key
     */
    public synchronized void updateCacheInfo(String flag, String key) {
	if (flag.equals("add")) {
	    if (!keyList.contains(key))
		keyList.add(key);
	} else if (flag.equals("remove")) {
	    if (keyList.contains(key))
		keyList.remove(key);
	}
    }

}
