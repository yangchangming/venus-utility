//Source file: D:\\venus_view\\Tech_Department\\Platform\\Venus\\4项目开发\\1工作区\\4实现\\venus\\frames\\mainframe\\cache\\Cacher.java

package venus.frames.mainframe.cache;

import java.io.Serializable;
import java.util.Map;

/**
 * cache所要提供的基本功能接口
 * 
 *@author 岳国云
 */
public interface ICacher extends Map,ISizeGetter, Serializable 
{
   
   /**
    * 根据 Key 得到 Cache 中的相应对象
    * @param key - 要得到Cache 中的相应对象的Key
    * @return Object - 要得到Cache 中的相应对象
    * @roseuid 3F8A11C801B4
    */
   public Object getByKey(Object key);
   
   /**
    * 根据 Key 删除 Cache 中的相应对象
    * @param key - 要从Cache中删除对象的Key
    * @return void
    * @roseuid 3F8A1B650261
    */
   public void removeByKey(Object key);
   
   /**
    * 设置 CAHCE 的最大容量
    * @param maxSize - 所要设置的 CAHCE 的最大容量
    * @return void
    * @roseuid 3F8A1C0D0232
    */
   public void setMaxSize(int maxSize);
   
   /**
    * 获得 CAHCE 的最大容量
    * @return int - 该CAHCE 的最大容量
    * @roseuid 3F8E099E0363
    */
   public int getMaxSize();
   
   /**
    * 向 CACHE 中插入对象（键值对）
    * @param key - 向 CACHE 中插入对象的KEY
    * @param object - 向 CACHE 中插入对象的值
    * @return void
    * @roseuid 3F8E0B820105
    */
   public void insert(Object key, Object object);
   
   /**
    * 清空 cache
    * @return void
    * @roseuid 3F94895D0259
    */
   public void clear();
   
   /**
    * 得到cache的名字
    * @return String - cache的名字
    * @roseuid 3F948BD00328
    */
   public String getCacheName();
   
   /**
    * 设置cache的名字
    * @param cacheName - cache的名字
    * @return void
    * @roseuid 3F948BF903D4
    */
   public void setCacheName(String cacheName);   
   
   /**
    * 获取该cache的实现者（类）的名字
    * @return String - cache的实现者（类）的名字
    * @roseuid 3F94918900D0
    */
   public String getImplName();
   
   public java.util.Enumeration keys();
   
}
