//Source file: D:\\venus_view\\Tech_Department\\Platform\\Venus\\4项目开发\\1工作区\\4实现\\venus\\frames\\mainframe\\base\\currentlogin\\IProfile.java

package venus.frames.mainframe.currentlogin;

import java.io.Serializable;
import java.util.Enumeration;


public interface IProfile extends Serializable 
{
   
   /**
    * 设置新操作的 Action 名
    * 
    * @param newActionName - 新操作的 Action 名字
    * @return void 
    * @roseuid 3FAE3D890226
    */
   public void setActionName(String newActionName);
   
   /**
    * 设置新操作的 Path
    * @param newPath - 新操作的 Path名字
    * @roseuid 3FAE3D9C01AC
    */
   public void setPath(String newPath);
   
   /**
    * 得到该用户相应的系统数据中名为key 的属性的值
    * 
    * @return Object - 该用户相应的系统数据中key所对应的属性值
    * @roseuid 3FAE3DF30373
    */
   public Object getSysAttribute(String key);
   
   /**
    * 得到该用户相应的应用数据中名为 key 的属性的值
    * @return Object - 该用户相应的应用数据中key所对应的属性值
    * @roseuid 3FAE3E0C01CB
    */
   public Object getAppAttribute(String key);
   
   /**
    * 向Session 的 Profile 中设置暂存数据
    * 
    * @param name - 向该对象中暂存数据的键值
    * @param value - 向该对象中暂存的数据值
    * @return void 
    * @roseuid 3FAE3E92006F
    */
   public void setAttribute(Object name, Object value);
   
   
   /**
    * 向Session 的 Profile 中清除暂存数据
    * 
    * @param name - 向该对象中暂存数据的键值
    * @param value - 向该对象中暂存的数据值
    * @return void 
    * @roseuid 3FAE3E92006F
    */   
   public void removeAttribute(Object name);
   
   /**
    * 从Session的Profile中读取暂存数据
    * 
    * @return Object - 该对象对应的一个暂存数据对象
    * @roseuid 3FAE3EB8039F
    */
   public Object getAttribute(Object key);
   
   /**
    * 得到该用户此次连接的 sessionid
    * 
    * @return String - 该用户此次连接的 sessionid
    * @roseuid 3FAE409A03A2
    */
   public String getSessionId();
   
   /**
    * 返回该用户的登录名
    * 
    * @return String
    * @roseuid 3FAE4130028F
    */
   public String getLoginName();
   
   /**
    * 得到该连接相应的所有暂存数据的属性值
    * 
    * @return Enumeration - 该对象的属性集合Enumeration对象
    * @roseuid 3FB49D1C0147
    */
   public Enumeration getAttributeNames();
   
   /**
    * 得到该用户相应的所有系统数据的名字列表
    * 
    * @return Enumeration - 该用户相应的所有系统数据的名字列表
    * @roseuid 3FB49DB303E7
    */
   public Enumeration getSysAttributeNames();
   
   /**
    * 得到该用户相应的所有应用数据的名字列表
    * 
    * @return Enumeration - 该用户相应的所有应用数据的名字列表
    * @roseuid 3FB49DC3037A
    */
   public Enumeration getAppAttributeNames();
   
   /**
	* 设置用户资料的有效生存时间
	* 
	* 根据 m_arySessionId 是否为空，SYS_LOGIN_TIME 和当前时间的差值 
	* 
	* 以及 m_nLifeTime的值，判断是否释放本数据堆中的所有句柄;
	* 
	* 当该用户相关的所有session均退出，保存用户数据的最长时间参数 
	* ：-1:一直保留，除非程序强行删除；
	*    0:该用户所有SESSION结束则销毁；
	*   >0:该用户所有SESSION结束后多长时间(单位毫秒)销毁
	*  
	* @param name - 设置该用户资料的有效生存时间
	* @return void
	*/
   public void setLifeTime(long lifeTime);
   
   
   /**
	* 获取用户资料的有效生存时间
	* 
	* 根据 m_arySessionId 是否为空，SYS_LOGIN_TIME 和当前时间的差值 
	* 
	* 以及 m_nLifeTime的值，判断是否释放本数据堆中的所有句柄;
	* 
	* 当该用户相关的所有session均退出，保存用户数据的最长时间参数 
	* ：-1:一直保留，除非程序强行删除；
	*    0:该用户所有SESSION结束则销毁；
	*   >0:该用户所有SESSION结束后多长时间(单位毫秒)销毁
	*  
	* @return long - 返回该用户资料的有效生存时间
	*/
   public long getLifeTime();
}

