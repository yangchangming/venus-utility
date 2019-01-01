package venus.frames.mainframe.currentlogin;

import venus.frames.mainframe.cache.CacheFactory;
import venus.frames.mainframe.cache.ICacher;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

import java.io.Serializable;
import java.util.*;

/**
 * 用户数据对象：用于暂存同用户对应的基本数据
 */
public class SimpleProfile implements Serializable {
	
	public static boolean IS_USE_CACHE = true;
	
	/**
	 * 用以存储该用户系统的PROFILE存储空间
	 */
	private Map m_hashSys = null;

	/**
	 * 用以存储该用户应用程序的PROFILE存储空间
	 */
	private Map m_hashApp = null;

	/**
	 * 用以标识该用户系统登录名的读取字
	 */
	public static String SYS_LOGIN_NAME = "SYS_LOGIN_NAME";

	/**
	 * 用以标识该用户系统密码的读取字
	 */
	public static String SYS_LOGIN_PASSWORD = "SYS_LOGIN_PASSWORD";
	
	/**
	 * 用以标识该用户SESSION_IDS
	 */
	public static String SESSION_IDS_KEY = "SESSION_IDS_KEY";
	
	/**
	 * 用以标识该用户m_nSessionLogoutTime
	 */
	public static String SESSION_LOGOUT_TIME_KEY = "SESSION_LOGOUT_TIME_KEY";

	/**
	 * 用以标识该用户登录系统时间的读取字
	 */
	public static String SYS_LOGIN_TIME = "SYS_LOGIN_TIME";

	/**
	 * 用于存储该PROFILE所处的位置WEB还是app 
	 */
	private String m_strLoc = null;
	
	/**
	 * 用于存储该PROFILE的生存时间
	 */
	private long m_nLifeTime = ProfileMgr.LIVE_BY_SESSION;
	

	/**
	 * 构造器
	 * 
	 * 分别将 sysAttribute ，appAttribute 赋给 m_hashSys 和 m_hashApp
	 * 
	 * 然后将 sessionid 加入m_arySessionId
	 * 
	 * 将 loc 赋给 m_strLoc
	 * 
	 * 将 lifeTime 赋给 m_nLifeTime
	 * @param sysAttribute - 
	 * @param appAttribute - 
	 * @param sessionid - 该用户的sessionid
	 * @param loc - 
	 * @param lifeTime - 该用户的
	 * @roseuid 3FB0854203DE
	 */
	public SimpleProfile(
		Hashtable sysAttribute,
		Hashtable appAttribute,
		String sessionid,
		String loc,
		long lifeTime) {

		if (loc == null || sessionid == null) {
			LogMgr.getLogger(this).warn("SimpleProfile():parameters are null");
		}
		m_strLoc = loc;
		String loginName = null;
		if( sysAttribute.containsKey( SYS_LOGIN_NAME ) ){
			
			loginName = (String) sysAttribute.get(SYS_LOGIN_NAME);
			
		}
		

		//初始化传入的参数
		if (appAttribute != null) {
			setM_hashApp(appAttribute,loginName);
		}
		if (sysAttribute != null) {
			setM_hashSys(sysAttribute,loginName);
		}
		if (sessionid != null){
			
			addSessionId(sessionid);
				
		
		}

		setM_nSessionLogoutTime(lifeTime);		
		
	}

	/**
	 * 得到该用户系统登录名
	 * 
	 * @return String - 该用户系统登录名
	 * @roseuid 3FAE41110172
	 */
	public String getLoginName() {

		if (m_hashSys.containsKey(SYS_LOGIN_NAME)){
			
			return (String) m_hashSys.get(SYS_LOGIN_NAME);
		
		}
			
		return null;
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
	
		this.m_nLifeTime = lifeTime;
	
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
	
		return this.m_nLifeTime ;
	
	}

	/**
	 * 得到该用户相应的系统数据中名为 key 的属性的值
	 * @param key - 要得到的属性值的关键字
	 * @return Object - 要得到的系统数据属性值
	 * @roseuid 3FAE417103E7
	 */
	public Object getSysAttribute(String key) {

		if (key == null) {
			LogMgr.getLogger(this).warn("getSysAttribute():parameter 'key' is null");
			return null;
		}


		if (m_hashSys.containsKey(key)) {
			return m_hashSys.get(key);
		}
		return null;

	}

	/**
	 * 得到该用户相应的应用程序数据中名为 key 的属性的值
	 * 
	 * @param key - 要得到的属性值的关键字
	 * @return Object - 要得到的应用程序数据属性值
	 * @roseuid 3FAE41D603A6
	 */
	public Object getAppAttribute(String key) {

		if (key == null) {
			LogMgr.getLogger(this).warn("getAppAttribute():parameter 'key' is null");
			return null;
		}
	
			if (m_hashApp.containsKey(key)) {
				return m_hashApp.get(key);
			} else {
				return null;
			}


	}
	
	public void setAppAttribute(String key,Object value) {

		if ( key == null ) {
			LogMgr.getLogger(this).warn("setAppAttribute(.....):parameter 'key' is null");
			return ;
		}

		if ( value != null ) m_hashApp.put( key,value );
			

		
		
	}
	
	public void setSysAttribute(String key,Object value) {

		if (key == null ) {
			LogMgr.getLogger(this).warn("setSysAttribute(......):parameter 'key' is null");
			return ;
		} 

		if ( value != null ) m_hashSys.put( key,value );

		
		
	}
	
	public void removeAppAttribute(String key) {

		if ( key == null ) {
			LogMgr.getLogger(this).warn("removeAppAttribute(.....):parameter 'key' is null");
			return ;
		}

		if (m_hashApp.containsKey(key)) {
			m_hashApp.remove(key);
		}

		

		
	}
	
	public void removeSysAttribute(String key) {

		if (key == null ) {
			LogMgr.getLogger(this).warn("removeSysAttribute(......):parameter 'key' is null");
			return ;
		} 

		if (m_hashSys.containsKey(key)) {
			m_hashSys.remove(key);
		}

		
	}
	
	
	

	/**
	 * 得到该用户相应的所有系统数据的名字列表
	 * 
	 * @return Enumeration - 返回该用户相应的所有系统数据的关键字的枚举
	 * @roseuid 3FB086780390
	 */
	public Enumeration getSysAttributeNames() {

		if (m_hashSys != null) {
			
			if( !IS_USE_CACHE ){
				
				return ((Hashtable)m_hashSys).keys();
				
				
			}else{
				
				return ((ICacher)m_hashSys).keys();
				
			}

		}
		
		return null;

		
	}

	/**
	 * 得到该用户相应的所有应用数据的名字列表
	 * 
	 * @return Enumeration - 返回该用户相应的所有应用数据的关键字的枚举
	 * @roseuid 3FB086A500F0
	 */
	public Enumeration getAppAttributeNames() {

		if (m_hashApp != null) {
			
			if( !IS_USE_CACHE ){
				
				return ((Hashtable)m_hashApp).keys();
				
				
			}else{
				
				return ((ICacher)m_hashApp).keys();
				
			}

		}
		
		return null;

	}

	/**
	 * 加入该用户对应的某一sessionid
	 * @param sessionid - 该用户的sessionid
	 * @roseuid 3FB85D5700CA
	 */
	public void addSessionId(String sessionid) {

		if (sessionid == null) {
			LogMgr.getLogger(this).warn("addSessionId():parameter 'sessionid' is null");
			return;
		}
		List arySessionId = getM_arySessionId();
		arySessionId.add(sessionid);
		setM_arySessionId(arySessionId);

	}

	/**
	 * 返回该用户是否存在对应的 Session
	 * 
	 * @return boolean
	 * @roseuid 3FB85D6B002E
	 */
	public boolean haveSession() {
		
		List arySessionId = getM_arySessionId();
		
		if (arySessionId.isEmpty()){
			
			return false;
		
		}else{
			
			return true;
		
		}		
		
		
	}

	/**
	 * 得到该用户对应的所有 sessionid 的数组
	 * 
	 * @return String[] - 该用户对应的所有 sessionid 的数组
	 * @roseuid 3FB85DBE01D4
	 */
	public String[] getSessionIds() {

		List arySessionId = getM_arySessionId();
		
		int size = arySessionId.size();
		String[] s = null;

		//将m_arySessionId中每一项取出转型后存入数组
		if (size > 0) {
			s = new String[size];
			for (int i = 0; i < size; i++) {
				s[i] = (String) arySessionId.get(i);
			}
		}
		//返回数组
		if (s != null) {
			return s;
		} else
			//返回一个空的数组
			return new String[0];
	}

	/**
	 * 删除该用户对应的某一 sessionid
	 * 
	 * @param sessionid - 该用户对应的sessionid
	 * @return void
	 * @roseuid 3FB85E96006D
	 */
	public void removeSessionId(String sessionid) {
		
		List arySessionId = getM_arySessionId();
		
		
		if (sessionid != null){
			arySessionId.remove(sessionid);
		    if (arySessionId.size() == 0){
		    	
		    	 this.setM_nSessionLogoutTime( System.currentTimeMillis() );		    
		    
		    }
		}
		
		setM_arySessionId(arySessionId);
		return;
	}

	/**
	 * 调用clearByLifeTime()释放本数据堆中的所有句柄
	 * 
	 * @return boolean
	 * @roseuid 3FB9F7AF0271
	 */
	public boolean clear() {
		return clearByLifeTime();
	}

	/**
	 * 1.根据 m_arySessionId 是否为空，SYS_LOGIN_TIME 和当前时间的差值 
	 * 
	 * 以及 m_nLifeTime的值，判断是否释放本数据堆中的所有句柄;
	 * 
	 * 保存用户数据的最长时间参数 
	 *   -2:立即删除(胡捷添加，2005-7-15)
	 * ：-1:一直保留，除非程序强行删除；
	 *    0:该用户所有SESSION结束则销毁；
	 *   >0:该用户所有SESSION结束后多长时间(单位毫秒)销毁
	 * 
	 * 1.如果销毁,返回 true，否则返回false；
	 * @return boolean
	 * @roseuid 3FB9F7E9029F
	 */
	protected boolean clearByLifeTime() {

		List arySessionId = getM_arySessionId();
		
		//一直保留，除非程序强行删除,返回FALSE 
		if (m_nLifeTime == -1L) {
			return false;
		}
		
		/**--------------------------------------------------------------
		 * 添加：胡捷
		 * 时间：2005-7-15
		 * 目的：立即销毁
		 * 注释：2005-7-28
		 */
//		if (this.m_nLifeTime == LIFE_TIME_DELETE_IMMEDIATELY) 
//		{
//			if (m_hashApp != null)
//				m_hashApp.clear();
//			m_hashApp = null;
//			if (m_hashSys != null)
//				m_hashSys.clear();
//			m_hashSys = null;
//			m_strLoc = null;
//
//			/****/
//			LogMgr.getLogger(this.getClass().getName()).info(
//				"clearByLifeTime():-->return true");
//			/****/
//
//			return true;
//		}
		//----------------------------------------------------------------
		
		
		//如果m_arySessionId 空则销毁
		if (arySessionId.isEmpty()) {

			if (m_nLifeTime == 0L) {

				if (m_hashApp != null){					
					m_hashApp.clear();				
				}					
				m_hashApp = null;
				if (m_hashSys != null){					
					m_hashSys.clear();
				}					
				m_hashSys = null;
				m_strLoc = null;

				/****/
				LogMgr.getLogger(this.getClass().getName()).info(
					"clearByLifeTime():m_arySessionId.isEmpty()-->return true");
				/****/

				return true;

			} else if (m_nLifeTime > 0L) {

				//得到当前时间，并计算登录时间与当前时间差值
				long haveTime = 0L;
				try {
					haveTime = System.currentTimeMillis() - this.getM_nSessionLogoutTime();
				} catch (NumberFormatException nfe) {
					LogMgr.getLogger(this.getClass().getName()).error(
						"clearByLifeTime(): loginTime NumberFormatException",
						nfe);
				}

				if (haveTime > m_nLifeTime) {
					if (m_hashApp != null){
						m_hashApp.clear();
						m_hashApp = null;
					}						
					
					if (m_hashSys != null){
						m_hashSys.clear();
						m_hashSys = null;
					}	
					m_strLoc = null;
					if (arySessionId != null){						
						arySessionId.clear();
						setM_arySessionId(arySessionId);
						arySessionId = null;
					}						

					/****/
					LogMgr.getLogger(this.getClass().getName()).info(
						"clearByLifeTime():m_nLifeTime > 0L :haveTime > m_nLifeTime : -->return true");
					/****/
					return true;
				} else {

					/****/
					LogMgr.getLogger(this.getClass().getName()).info(
						"clearByLifeTime(): return false");
					/****/

					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 */
	private ILog getIlog() {
		return LogMgr.getLogger(this);
	}
	
	/**
	 * @param app 要设置的 m_hashApp。
	 * @param loginName
	 */
	private void setM_hashApp(Hashtable app, String loginName) {
		
		if(!IS_USE_CACHE){
			
			m_hashApp = app;
			
			if( m_hashApp==null ) m_hashApp= new Hashtable();
		
		}else{
			
			if( m_hashApp==null ) {
				
				m_hashApp = CacheFactory.getCache(m_strLoc+".App." + loginName);
			
			}
			
			if( app!=null ) m_hashApp.putAll(app);
			
			
		}
		;
	}
	/**
	 * @param sys 要设置的 m_hashSys。
	 * @param loginName
	 */
	private void setM_hashSys(Hashtable sys, String loginName) {
		
		if(!IS_USE_CACHE){
			
			m_hashSys = sys;
			if( m_hashSys==null ) m_hashSys= new Hashtable();
		
		}else{
			if( m_hashSys==null ) {
				m_hashSys = CacheFactory.getCache(m_strLoc+".Sys." + loginName);
			}
			if( sys!=null ) m_hashSys.putAll(sys);
		}
		
	}
	
	private List getM_arySessionId(){
		
		Object o = m_hashSys.get(SESSION_IDS_KEY);

		if( o != null ){
			
			return (List)o;
		
		}else{
			
			return new ArrayList();
		
		}
	}
	
	private void setM_arySessionId(List arySessionId){

		m_hashSys.put(SESSION_IDS_KEY,arySessionId);


	}
	
	/**
	 * @return 返回 m_nSessionLogoutTime。
	 */
	private long getM_nSessionLogoutTime() {

		return ((Long)m_hashSys.get(SESSION_LOGOUT_TIME_KEY)).longValue();


	}
	/**
	 * @param sessionLogoutTime 要设置的 m_nSessionLogoutTime。
	 */
	private void setM_nSessionLogoutTime(long sessionLogoutTime) {
			
		m_hashSys.put(SESSION_LOGOUT_TIME_KEY,new Long(sessionLogoutTime));

	}
}
