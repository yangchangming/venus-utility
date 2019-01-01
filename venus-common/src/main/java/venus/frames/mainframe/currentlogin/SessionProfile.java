package venus.frames.mainframe.currentlogin;

import venus.frames.mainframe.action.ActionChangeEvent;
import venus.frames.mainframe.cache.CacheFactory;
import venus.frames.mainframe.cache.ICacher;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.*;

/**
 * 
 * @author 岳国云
 */

public class SessionProfile implements IProfile,Serializable  {

	/**
	 * 处理 path 变更事件的代理器
	 *  
	 * m_support.addPropertyChangeListener( listener );
	 * 
	 * 的方式绑定事件监听器
	 */
	private PropertyChangeSupport m_support =
		new PropertyChangeSupport(this);

//	/**
//	 * 用于存储历史操作的 Action 的名字
//	 */
//	private String m_strOldActionName = null;
//
//	/**
//	 * 用于存储历史操作的 Path 名
//	 */
//	private String m_strOldPathName = null;
//
//	/**
//	 * 用于存储该操作的 Action 名
//	 */
//	private String m_strActionName = null;
//
//	/**
//	 * 用于存储该操作的 Path 名
//	 */
//	private String m_strPathName = null;
//
//	/**
//	 * 用于存储历史操作的 Action 的名字 的KEY
//	 */
//	public static String OLD_ACTION_NAME_KEY = "VENUS_OLD_ACTION_NAME_KEY";
//	
//	/**
//	 * 用于存储历史操作的 Path 名 的KEY
//	 */
//	public static String OLD_PATH_NAME_KEY = "VENUS_OLD_PATH_NAME_KEY";
	
	/**
	 * 用于存储该操作的 Action 名 的KEY
	 */	
	public static String ACTION_NAME_KEY = "VENUS_ACTION_NAME_KEY";
	
	/**
	 * 用于存储该操作的 Path 名 的KEY
	 */
	public static String PATH_NAME_KEY = "VENUS_PATH_NAME_KEY";	
	
	
	
	/**
	 * 用于存储该用户临时数据的 Hashtable
	 */
	private Map m_hashAttribute = null;

	/**
	 * 存储此次连接的 sessionid
	 */
	private String m_strSessionId = null;

	/**
	 * 存储该用户的登录名
	 */
	private String m_strLoginName = null;

	/**
	 * 存储该连接的登录时间
	 */
	private long m_nLoginTime = 0;

	/**
	 * 用于存储该 PROFILE 所处的位置 WEB 还是 app 
	 */
	private String m_strLoc = null;

	
	/**
	 * 构造器
	 * 
	 * 注意构造时抛出构建事件
	 * 
	 * 1.将传入的数据存入相应的变量。
	 * 
	 * 2.根据系统当前时间存入 m_nLoginTime
	 * 
	 * 3.绑定 SimpleProfile.
	 * 
	 * 注意先判断 sysAttribute 中是否存在 
	 * SimpleProfile.SYS_LOGIN_NAME，SimpleProfile.SYS_LOGIN_TIME 的值,
	 * 如果不存在则填入相应的值新建 SimpleProfile 并将新建的实例赋给m_SimpleProfile，
	 * 并将该实例存入 ProfileMgr addProfile(...)
	 * 
	 * 4.检索sysAttribute 和 appAttribute 中的对象，查看是否存在 
	 * PropertyChangeListener 对象，如果有则使用 m_support.addPropertyChangeListener( 
	 * listener );的方式绑定该事件监听器
	 * 
	 * 5.如果用户名不为空，则抛出构建事件
	 * m_support.firePropertyChange(new ActionChangeEvent(this,ActionChangeEvent.BUILD_KEY,
	 * ActionChangeEvent.BUILD_KEY,null,sessionid,loginName)
	 * 
	 * 6.初始化自身数据堆：
	 *从 ProfileMgr getDefaultProfileSizeForCache() 读取 PROFILE的大小限制size
	 * 
	 *如果-1说明没有限制，则 m_hashAttribute = new Hashtable();
	 * 
	 *如果>0 说明有限制，使用cache m_cacheAttribute =  CacheFactory.createCache(size,"sessionprofile"+sessionid);
	 * 
	 * @param sysAttribute - 系统数据属性
	 * @param appAttribute - 应用数据属性
	 * @param sessionid - 当前连接的sessionid
	 * @param loginName  - 该用户的登录名
	 * @param loc -  该 PROFILE 所处的位置WEB或APP
	 * @param lifeTime - 该数据堆的生存时间
	 * @roseuid 3FB4A44E01A5
	 */
	public SessionProfile(
		Hashtable sysAttribute,
		Hashtable appAttribute,
		String sessionid,
		String loginName,
		String loc,
		long lifeTime) {

		/***********/
		getIlog().info(
			"create SessionProfile(...)....."
				+ " loc="
				+ loc
				+ "  loginName="
				+ loginName+" sessionid="+sessionid);
		/***********/

		if (loc == null
			|| sessionid == null) {
			getIlog().error("create SessionProfile(...) : parameters are null！ ");
			return;
		}
		//存储传入的变量
		this.m_strSessionId = sessionid;
		this.m_strLoginName = loginName;
		this.m_strLoc = loc;

		//得到当前时间
		Date date = new Date();
		m_nLoginTime = date.getTime();
		ProfileMgr pm = ProfileMgr.getSingleton();

		//初始化自身数据堆
		int cacheSize = pm.getDefaultProfileSizeForCache();
		if (cacheSize == -1) {
			m_hashAttribute = new Hashtable();
		}
		if (cacheSize > 0) {
			
			m_hashAttribute = CacheFactory.createCache(cacheSize, m_strLoc+".S." + sessionid);
			//m_cacheAttribute = CacheFactory.createCache(cacheSize, m_strLoc+".S." + sessionid);
		}

		//构建SimpleProfile新实例
		if (loginName != null) {
			if (!sysAttribute.containsKey(SimpleProfile.SYS_LOGIN_NAME)) {
				sysAttribute.put(SimpleProfile.SYS_LOGIN_NAME, loginName);
			}
		}
		if (!sysAttribute.containsKey(SimpleProfile.SYS_LOGIN_TIME)) {
			sysAttribute.put(
				SimpleProfile.SYS_LOGIN_TIME,
				String.valueOf(m_nLoginTime));
		}

		//新建SimpleProfile实例,赋给m_SimpleProfile，并将该实例存储 
		if (loginName != null) {
			SimpleProfile sp =
				new SimpleProfile(
					sysAttribute,
					appAttribute,
					sessionid,
					loc,
					lifeTime);

			//存储SimpleProfile实例
			if (sp != null) {
				pm.addSimpleProfile(sp, loc);
				//this.m_SimpleProfile = sp;
			}
		}

		//检索sysAttribute 中的对象，查看是否存在PropertyChangeListener对象，
		//如果有则绑定该事件监听器
		Collection sysCol = sysAttribute.values();
		Iterator sysi = sysCol.iterator();
		while (sysi.hasNext()) {
			Object value = sysi.next();
			if (value instanceof PropertyChangeListener)
				m_support.addPropertyChangeListener((PropertyChangeListener) value);
		}

		//检索appAttribute 中的对象，查看是否存在PropertyChangeListener对象，
		//如果有则绑定该事件监听器
		Collection appCol = appAttribute.values();
		Iterator appi = appCol.iterator();
		while (appi.hasNext()) {
			Object value = appi.next();
			if (value instanceof PropertyChangeListener)
				m_support.addPropertyChangeListener((PropertyChangeListener) value);
		}

		//抛出构建事件
		if (loginName != null) {
			m_support.firePropertyChange(new ActionChangeEvent(this, ActionChangeEvent.BUILD_KEY, ActionChangeEvent.BUILD_KEY,
					null,
					sessionid,
					loginName));
		}
	}

	/**
	 * 构造器
	 * 
	 * 注意构造时抛出构建事件
	 * 
	 * 1.将传入的数据存入相应的变量。
	 * 
	 * 2.根据系统当前时间存入 m_nLoginTime
	 * 
	 * 3.如果loginName不为空，则调用pm.getSimpleProfile(...)方法
	 *   得到SimpleProfile实例赋值给m_SimpleProfile
	 * 
	 * 4.初始化自身数据堆：
	 * 从 ProfileMgr getDefaultProfileSizeForCache() 读取 PROFILE的大小限制size
	 * 如果-1说明没有限制，则 m_hashAttribute = new Hashtable();
	 * 如果>0 说明有限制，使用cache m_cacheAttribute =  CacheFactory.createCache(size,"sessionprofile"+sessionid);
	 * 
	 * 5.检索htListener，从中取出PropertyChangeListener项，实例化并使用 m_support.addPropertyChangeListener( 
	 * listener );的方式绑定该事件监听器
	 * 
	 * @param loc - 该 PROFILE 所处的位置WEB或APP
	 * @param loginName - 当前登录用户名
	 * @param sessionid - 当前连接的sessionid
	 * @param htListener - 存储PropertyChangeListener项
	 */
	public SessionProfile(
		String loc,
		String loginName,
		String sessionid,
		Hashtable htListener,
		long lifeTime) {

		if (loc == null || sessionid == null) {
			getIlog().error("create SessionProfile(...) : parameters are null！ ");
			return;
		}

		//存储传入的变量
		this.m_strSessionId = sessionid;
		this.m_strLoginName = loginName;
		this.m_strLoc = loc;

		//得到当前时间
		Date date = new Date();
		m_nLoginTime = date.getTime();
		ProfileMgr pm = ProfileMgr.getSingleton();

		//如果存在当前loginName的SimpleProfile实例，
		//则根据 loginName取出并赋给m_SimpleProfile
		if (loginName != null) {
			SimpleProfile simplepro = getSimpleProfile();
			
			if (simplepro != null) {
				simplepro.addSessionId(sessionid);
getIlog().info(simplepro+" .addSessionId("+sessionid+")");
				pm.updateSimpleProfile(simplepro,loc);
				//this.m_SimpleProfile = simplepro;
				
			}
		}

		//初始化自身数据堆
		int cacheSize = pm.getDefaultProfileSizeForCache();
		if (cacheSize == -1) {
			m_hashAttribute = new Hashtable();
		}
		if (cacheSize > 0) {
			
			m_hashAttribute = CacheFactory.createCache(cacheSize, m_strLoc+".S." + sessionid);

			//m_cacheAttribute = CacheFactory.createCache(cacheSize, m_strLoc+".S." + sessionid);
		}

		//检索存储传入的存储PropertyChangeListener的Hashtable，
		//查看是否存在PropertyChangeListener对象，如果有则绑定该事件监听器
		if (htListener != null) {
			Collection collListerner = htListener.values();
			Iterator listener = collListerner.iterator();
			while (listener.hasNext()) {
				Object value = listener.next();
				if (value instanceof PropertyChangeListener)
					m_support.addPropertyChangeListener(
						(PropertyChangeListener) value);
			}
		}
	}

	/**
	 * 设置新操作的 Action 名
	 * 
	 * @param newActionName 新的Action操作的名字
	 * @return void
	 * @roseuid 3FAE3F6C027B
	 */
	public void setActionName(String newActionName) {

		if (newActionName == null) {
			getIlog().error("setActionName(...) :parameter 'newActionName' is null");
			return;
		}
		
		String m_strActionName = this.getM_strActionName();
		
		ProfileMgr pm = ProfileMgr.getSingleton();
		//如果newActionName与历史操作的 Action 的名字不同
		//则构建ActionChangeEvent并绑定事件监听
		if (!newActionName.equals(m_strActionName)) {
			if (pm.isSetActionName() == 1) {
				m_support.firePropertyChange(
					new ActionChangeEvent(this, ActionChangeEvent.ACTION_CHG_KEY, m_strActionName, newActionName, this.getSessionId(), ""));
			}
			//配置Action操作
			/**			
			 * 修改：胡捷 时间:2005-12-19
			 m_strOldActionName = m_strActionName;
			 m_strActionName = newActionName;
			*/
			setM_strActionName(newActionName);
			
			
		}

	}

	/**
	 * 得到新操作的 Action 名
	 *
	 * @return m_strActionName 新操作的 Action名
	 */
	public String getActionName() {
		return this.getM_strActionName();
	}

	/**
	 * 设置新操作的 Path ，如果事件开关为TRUE，则绑定此Path事件监听
	 * 
	 * @param newPath - 新操作的 Path名
	 * @return void
	 * @roseuid 3FAE3F6C0325
	 */
	public void setPath(String newPath) {

		if (newPath == null) {
			getIlog().error("setPath(...) :newPath is null");
			return;
		}
		
		String m_strPathName = this.getM_strPathName();
		
		ProfileMgr pm = ProfileMgr.getSingleton();
		//如果newPath与历史操作的 Path 的名字不同,
		//则构建ActionChangeEvent并绑定事件监听

		if (!newPath.equals(m_strPathName)) {
			if (pm.isSetPath() == 1) {
				m_support.firePropertyChange(
					new ActionChangeEvent(
						this,
						ActionChangeEvent.PATH_CHG_KEY,
						m_strPathName,
						newPath,
						getSessionId(),
						""));
			}
			
			//把新操作Path存入当前操作变量
			setM_strPathName(newPath);
			
		}
		
	}

	/**
	 * 得到新操作的Path 
	 * 
	 * @param - 新操作的Path 名
	 */
	public String getPath() {
		return this.getM_strPathName();
	}

	/**
	 * 得到该用户相应的系统数据中名为 key 的属性的值
	 * 
	 * @param key  - 读取属性的关键字
	 * @return Object 返回的属性对象
	 * @roseuid 3FAE3F6C03B1
	 */
	public Object getSysAttribute(String key) {
		
		SimpleProfile simplepro = getSimpleProfile();

		if (simplepro == null || key == null) {
			getIlog().error("getSysAttribute(...) :参数不可以为空");
			return null;
		}
		return simplepro.getSysAttribute(key);
	}

	/**向 SessionProfile 中设置暂存数据
	
	 *1.存入 m_hashAttribute(如果m_hashAttribute空，m_cacheAttribute非空则存入 
	 *m_cacheAttribute ) 中
	 *
	 *2.多作一步判断，判断该对象是否为 PropertyChangeListener
	 *
	 *如果是则使用 m_support.addPropertyChangeListener( listener );
	 *
	 *的方式绑定该事件监听器
	 * 
	 * @param name - 暂存数据的KEY
	 * @param value - 要暂存的数据
	 * @return void 
	 * @roseuid 3FAE3F6D011E
	 */
	public void setAttribute(Object name, Object value) {

		if (name == null || value == null) {
			getIlog().error("setAttribute(...) :name OR value is null");
			return;
		}

		//如果m_hashAttribute空，m_cacheAttribute非空则把数据存入m_cacheAttribute中
//		if (m_hashAttribute == null && m_cacheAttribute != null) {
//			Object oldvalue = m_cacheAttribute.getByKey(name) ;
//			
//			if( oldvalue!=null ) m_cacheAttribute.removeByKey(name);
//			
//			m_cacheAttribute.insert(name, value);
//		} else {
			
			//否则存入m_hashAttribute
			if(  m_hashAttribute.containsKey(name) ) m_hashAttribute.remove(name);
			
			
			m_hashAttribute.put(name, value);
//		}

		//如果value对象为PropertyChangeListener，则绑定该事件监听器
		if (value instanceof PropertyChangeListener) {
			m_support.addPropertyChangeListener((PropertyChangeListener) value);
		}
		
	}

	public void removeAttribute(Object name) {

		if (name == null) {
			getIlog().error("reooveAttribute(...) :name is null");
			return;
		}

		//如果m_hashAttribute空，m_cacheAttribute非空则把数据存入m_cacheAttribute中
//		if (m_hashAttribute == null && m_cacheAttribute != null) {
//			//Object oldvalue = m_cacheAttribute.getByKey(name) ;
//			
//			//if( oldvalue!=null ) m_cacheAttribute.removeByKey(name);
//			
//			m_cacheAttribute.removeByKey(name);
//		} else {
			
			if(  m_hashAttribute.containsKey(name) ) m_hashAttribute.remove(name);

//		}

	}
	
	/**
	 * 从 SessionProfile 中读取暂存数据
	 * 
	 * 从 m_hashAttribute(如果m_hashAttribute空，m_cacheAttribute非空则从 m_cacheAttribute )中读取
	 * @param key - 要读取数据的KEY
	 * @return Object - 要读取的数据
	 * @roseuid 3FAE3F6D02CD
	 */
	public Object getAttribute(Object key) {
		if (key == null) {
			getIlog().error("getAttribute(...) :key is null,return null");
			return null;
		}

		//如果m_hashAttribute空，m_cacheAttribute非空则从 m_cacheAttribute 中读取
//		if (m_hashAttribute == null && m_cacheAttribute != null) {
//			if (m_cacheAttribute.getByKey(key) != null)
//				return m_cacheAttribute.getByKey(key);
//		}
		//从m_hashAttribute中读取
		if (m_hashAttribute != null) {
			if (m_hashAttribute.containsKey(key)) {
				return m_hashAttribute.get(key);
			}
		}
		return null;
	}

	/**
	 * 得到该用户此次连接的 sessionid
	 * 
	 * @return String - 此次连接的 sessionid
	 * @roseuid 3FAE40570161
	 */
	public String getSessionId() {
		return this.m_strSessionId;
	}

	/**
	 * 返回该用户的登录名
	 * 
	 * @return String - 用户的登录名
	 * @roseuid 3FAE412001A6
	 */
	public String getLoginName() {
		return this.m_strLoginName;
	}

	/**
	 * 得到该用户相应的应用数据中名为 key 的属性的值
	 * 
	 * @param key - 要得到的数据的KEY
	 * @return Object - 要得到的应用数据的值对象
	 * @roseuid 3FAE425B0036
	 */
	public Object getAppAttribute(String key) {

		SimpleProfile simplepro = getSimpleProfile();

		if (simplepro== null || key == null) {
			getIlog().error(
				"getAppAttribute(...) :parameters is null");
			return null;
		}

		return simplepro.getAppAttribute(key);
	}

	/**
	 * 得到该用户相应的所有系统数据的名字列表
	 * 
	 * @return Enumeration - 该用户相应的所有系统数据的关键字列表
	 * @roseuid 3FB491650389
	 */
	public Enumeration getSysAttributeNames() {
		
		SimpleProfile simplepro = getSimpleProfile();

		if (simplepro == null) {
			getIlog().error("getSysAttributeNames(...) :SimpleProfile is null，return nulll");
			return null;
		}
		return simplepro.getSysAttributeNames();
	}

	/**
	 * 
	 * 得到该连接相应的所有暂存数据
	 * 
	 * 从 m_hashAttribute 中读取
	 * 
	 * @return Collection - 该连接相应的所有暂存数据的Collectioncf对象
	 * @roseuid 3FB49CD502ED
	 */

	public Enumeration getAttributeNames() {

		//如果m_hashAttribute空，m_cacheAttribute非空则从 m_cacheAttribute 中读取
//		if (m_hashAttribute == null && m_cacheAttribute != null) {
//			return m_cacheAttribute.keys();
//		}

		//从m_hashAttribute中读取
		if (m_hashAttribute != null) {
			
			if( m_hashAttribute instanceof Hashtable){
				
				return ((Hashtable)m_hashAttribute).keys();
				
				
			}else{
				
				return ((ICacher)m_hashAttribute).keys();
				
			}

		}
		return null;
	}

	/**
	 * 得到该用户相应的所有应用数据的名字列表
	 * 
	 * @return Enumeration - 该用户相应的所有应用数据的关键字列表
	 * @roseuid 3FB49CEC0109
	 */
	public Enumeration getAppAttributeNames() {
		
		SimpleProfile simplepro = getSimpleProfile();

		if (simplepro == null) {
			getIlog().error("getAppAttributeNames(...) :SimpleProfile实例为空，返回空");
			return null;
		}

		return simplepro.getAppAttributeNames();
	}


	/**
	 * 调用调用ProfileMgr实例的 eraseSimpleProfile(...)销毁该用户数据堆
	 * 
	 * 释放本数据堆所有数据句柄
	 * 
	 * @return void 
	 * @roseuid 3FB9F72E0128
	 */
	public void clear() {
		
		SimpleProfile simpleProfile = getSimpleProfile();
		
		if (simpleProfile == null) {
			getIlog().error("clear(...) :m_SimpleProfile is null,return");
			return;
		}
		
		m_support.firePropertyChange(new ActionChangeEvent(this, ActionChangeEvent.ERASE_KEY, getM_strPathName(), null, getSessionId(), ""));
		getIlog().info("clear(...) ...");


		ProfileMgr pm = ProfileMgr.getSingleton();
		//调用SimpleProfile的方法,把该sessionid从列表中删除
		simpleProfile.removeSessionId(m_strSessionId);

		
		//如果SimpleProfile中清除信息成功,
		//则调用从ProfileMgr清除此sessionProfile实例
		//并置m_SimpleProfile为空
		//m_SimpleProfile = null;
		
		pm.eraseSimpleProfile(m_strLoc, m_strLoginName);


		if (m_hashAttribute != null) {
			m_hashAttribute.clear();
		}
		m_hashAttribute = null;
		m_strLoc = null;
		m_strLoginName = null;
		m_strSessionId = null;

	}


	/***
	 * 得到日志记录实例
	 * 
	 * @return ILog
	 */
	public ILog getIlog() {

		return LogMgr.getLogger(this.getClass().getName());
	}
	
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
	public void setLifeTime(long lifeTime){
	
		getSimpleProfile().setLifeTime(lifeTime);
	
	}
	
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
	public long getLifeTime(){
	
		return getSimpleProfile().getLifeTime() ;
	
	}
	
	/**
	 * 获取Session连接时间戳
	 * 添加：胡捷
	 * 时间：2005-8-1
	 * @retur 返回该Session连接的登录时间
	 */
	public long getLoginTime()
	{
	    return this.m_nLoginTime;
	}
	
	private SimpleProfile getSimpleProfile(){
		
		ProfileMgr pm = ProfileMgr.getSingleton();
		
		return pm.getSimpleProfile(this.m_strLoc,this.m_strLoginName);
		
	}
	
	
//	/**
//	 * @return 返回 m_strOldActionName。
//	 */
//	private String getM_strOldActionName() {
//		if( m_hashAttribute.containsKey( OLD_ACTION_NAME_KEY ) ){
//			
//			return (String)m_hashAttribute.get(OLD_ACTION_NAME_KEY);
//		
//		}
//		
//		return null;
//	}
//	/**
//	 * @param oldActionName 要设置的 m_strOldActionName。
//	 */
//	private void setM_strOldActionName(String oldActionName) {
//		if( oldActionName!=null ){
//			
//			 m_hashAttribute.put(OLD_ACTION_NAME_KEY, oldActionName);
//		
//		}else{
//			
//			 m_hashAttribute.remove(OLD_ACTION_NAME_KEY);
//			 
//		}
//	}
//	/**
//	 * @return 返回 m_strOldPathName。
//	 */
//	private String getM_strOldPathName() {
//		if( m_hashAttribute.containsKey( OLD_PATH_NAME_KEY ) ){
//			
//			return (String)m_hashAttribute.get(OLD_PATH_NAME_KEY);
//		
//		}
//		
//		return null;
//	}
//	/**
//	 * @param oldPathName 要设置的 m_strOldPathName。
//	 */
//	private void setM_strOldPathName(String oldPathName) {
//		if( oldPathName!=null ){
//			
//			 m_hashAttribute.put(OLD_PATH_NAME_KEY, oldPathName);
//		
//		}else{
//			
//			 m_hashAttribute.remove(OLD_PATH_NAME_KEY);
//			 
//		}
//	}
	/**
	 * @return 返回 m_strActionName。
	 */
	private String getM_strActionName() {
		if( m_hashAttribute.containsKey( ACTION_NAME_KEY ) ){
			
			return (String)m_hashAttribute.get(ACTION_NAME_KEY);
		
		}
		
		return null;
	}
	/**
	 * @param actionName 要设置的 m_strActionName。
	 */
	private void setM_strActionName(String actionName) {
		if( actionName!=null ){
			
			 m_hashAttribute.put(ACTION_NAME_KEY, actionName);
		
		}else{
			
			 m_hashAttribute.remove(ACTION_NAME_KEY);
			 
		}

	}
	/**
	 * @return 返回 m_strPathName。
	 */
	private String getM_strPathName() {
		if( m_hashAttribute.containsKey( PATH_NAME_KEY ) ){
			
			return (String)m_hashAttribute.get(PATH_NAME_KEY);
		
		}
		
		return null;
	}
	/**
	 * @param pathName 要设置的 m_strPathName。
	 */
	private void setM_strPathName(String pathName) {
		if( pathName!=null ){
			
			 m_hashAttribute.put(PATH_NAME_KEY, pathName);
		
		}else{
			
			 m_hashAttribute.remove(PATH_NAME_KEY);
			 
		}
	}
}
