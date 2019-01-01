/*
 * 工具类
 * 
 * 创建日期 2005-7-24
 *
 */
package venus.pub.util;

import org.springframework.beans.BeanWrapper;
import venus.frames.base.vo.BaseValueObject;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Set;

/**
 * 
 * comment for Tools 1、对象属性拷贝到HashMap中
 * 
 * @author zhengyan
 *  
 */
public class Tools {



    /**
     * 得到属性数组的值
     * 
     * @param vo
     * @param equalFieldsName
     * @return
     */
    public static Object[] getPropertyValues(Object vo_bean,
            String equalFieldsName[]) {

        BeanWrapper bw = new org.springframework.beans.BeanWrapperImpl(vo_bean);
        Object[] values = new Object[equalFieldsName.length];
        for (int i = 0; i < equalFieldsName.length; i++) {
            values[i] = bw.getPropertyValue(equalFieldsName[i].toLowerCase());
        }
        return values;

    }

    /**
     * 拷贝对象的属性值到HashMap 中
     * 
     * @param vo
     * @param names
     *            需要拷贝的属性数组
     * @return 为了避开大小写敏感问题，属性值只支持小写
     */
    public static HashMap copyPropertiesToHashMap(Object bean, String names[]) {
        BeanWrapper bw = new org.springframework.beans.BeanWrapperImpl(bean);
        HashMap hm = new HashMap(names.length);

        for (int i = 0; i < names.length; i++) {
            String name = names[i].toLowerCase().trim();
            hm.put(name, bw.getPropertyValue(name));
        }
        return hm;

    }

    /**
     * 拷贝VO对象的属性值到HashMap 中
     * 
     * @param vo
     * @param names
     * @return 为了避开大小写敏感问题，属性值只支持小写
     */
    public static HashMap copyPropertiesToHashMap(BaseValueObject vo) {
        BeanWrapper bw = new org.springframework.beans.BeanWrapperImpl(vo);

        PropertyDescriptor pds[] = bw.getPropertyDescriptors();
        HashMap hm = new HashMap(pds.length - 1);

        for (int i = 0; i < pds.length; i++) {
            String name = pds[i].getName();
            if (name.equals("class"))
                continue;
            hm.put(name, bw.getPropertyValue(name));
        }
        return hm;
    }

    /**
     * 得到HashMap的键数组
     * 
     * @param hm
     * @return
     */
    public static Object[] getHashMapKeys(HashMap hm) {
        Set keys = hm.keySet();
        return keys.toArray();

    }

    /**
     * 得到HashMap的值数组
     * 
     * @param hm
     * @return
     */
    public static Object[] getHashMapValues(HashMap hm) {
        if (hm == null)
            hm = new HashMap();
        Object[] keys = getHashMapKeys(hm);
        Object[] values = new Object[keys.length];
        for (int i = 0; i < keys.length; i++) {
            values[i] = hm.get(keys[i]);
        }

        return values;
    }

    
    /**
	 * 得到日志记录驱动实例
	 * @author lihong@use.com.cn
	 * 
	 * @return ILog LOG驱动实例
	 */
	private static ILog getLogger() {
		return LogMgr.getLogger("venus.pub.util.Tools");
	}
}

