package venus.frames.mainframe.currentlogin;

import org.w3c.dom.Node;
import venus.frames.base.IGlobalsKeys;
import venus.frames.mainframe.cache.CacheFactory;
import venus.frames.mainframe.context.ContextMgr;
import venus.frames.mainframe.context.IContext;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.ClassLocator;
import venus.frames.mainframe.util.ConfMgr;
import venus.frames.mainframe.util.DefaultConfReader;
import venus.frames.mainframe.util.IConfReader;
import venus.pub.util.Convertor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * 
 * 
 * 配置参数：
 * 
 * 保存用户数据的最长时间参数 
 * ：-1:一直保留，除非程序强行删除；
 *    0:该用户所有SESSION结束则销毁；
 *   >0:该用户所有SESSION结束后多长时间(单位毫秒)销毁
 */
public class ProfileMgr implements IGlobalsKeys {

	/**
	 * singleton model中用到的，用于存储工厂类的单一实例
	 */
	private static ProfileMgr m_Singleton = null;
	
	public final static String SESSIONPROFILE_REGISTER_KEY = "SessionProfile";
	
	public final static String SIMPLEPROFILE_REGISTER_KEY = "SimpleProfile";
	
		
	/**
	 * 用于暂存 sessionProfile的Hashtable的Hashtable
	 * 
	 * 按loc为KEY区分这些Hashtable
	 * 
	 * loc表示当前的位置：web，还是App端
	 * 
	 * 由于web和app端所得到的profile是不一样的
	 */
	private Hashtable m_hashSessionProfileTableByLoc = new Hashtable();

	/**
	 * 用于暂存 simpleProfile 的 Hashtable 的 Hashtable
	 * 
	 * 按 loc 为 KEY 区分这些 Hashtable
	 * 
	 * loc 表示当前的位置：web，还是App端
	 * 
	 * 由于 web 和 app 端所得到的 profile 是不一样的
	 */
	private Hashtable m_hashSimpleProfileTableByLoc = new Hashtable();

	/**
	 * 用于暂存应用数据插件实例的列表 IProfilePlugIn[] 的Hashtable
	 * 
	 * 按 loc 为KEY 区分这些实例列表IProfilePlugIn[]
	 * 
	 * loc 表示当前的位置：web，还是 App 端
	 * 
	 * 由于 web 和 app 端所得到的插件是不一样的
	 */
	private Hashtable m_hashAppPlugInsByLoc = new Hashtable();

	/**
	 * 用于暂存系统数据插件实例的列表IProfilePlugIn[] 的Hashtable
	 * 
	 * 按 loc 为KEY 区分这些实例列表IProfilePlugIn[]
	 * 
	 * loc 表示当前的位置：web，还是 App 端
	 * 
	 * 由于 web 和 app 端所得到的插件是不一样的
	 */
	private Hashtable m_hashSysPlugInsByLoc = new Hashtable();

	/**
	 * 用于暂存 Listener 类名的列表 String[] 的Hashtable
	 * 
	 * 按 loc 为KEY 区分这些类名列表String[]
	 * 
	 * loc 表示当前的位置：web，还是 App 端
	 * 
	 * 由于 web 和 app 端所得到的插件是不一样的
	 */
	private Hashtable m_hashListenerNames = new Hashtable();

	/**
	 * 用于暂存应用数据插件类名的列表 String[] 的Hashtable
	 * 
	 * 按 loc 为KEY 区分这些类名列表 String[]
	 * 
	 * loc 表示当前的位置：web，还是 App 端
	 * 
	 * 由于 web 和 app 端所得到的插件名是不一样的
	 */
	private Hashtable m_hashAppPlugInNames = new Hashtable();

	/**
	 * 用于暂存系统数据插件类名的列表 String[] 的Hashtable
	 * 
	 * 按 loc 为KEY 区分这些类名列表 String[]
	 * 
	 * loc 表示当前的位置：web，还是 App 端
	 * 
	 * 由于 web 和 app 端所得到的插件名是不一样的
	 */
	private Hashtable m_hashSysPlugInNames = new Hashtable();

	/**
	 * 用户数据的最长时间参数 ：-1:一直保留，除非程序强行删除
	 */
	public static long LIVE_ALWAYS = -1L;

	/**
	 * 0:该用户所有SESSION结束则销毁所有暂存的用户数据
	 */
	public static long LIVE_BY_SESSION = 0L;

	/**
	 * 该用户所有SESSION结束后多长时间(单位毫秒)销毁
	 * 
	 * 保存用户数据的最长时间参数 :
	 * 
	 * -1:一直保留，除非程序强行删除；
	 *  0:该用户所有SESSION结束则销毁；
	 * >0:该用户所有SESSION结束后多长时间(单位毫秒) 销毁
	 * 
	 */
	private long m_nDefaultLifeTime = LIVE_BY_SESSION;

	/**
	 * 标识新建 profile 时
	 */
	private static int CREATE_PROFILE_KEY = 1;

	/**
	 * 标识删除 profile 时
	 */
	private static int ERASE_PROFILE_KEY = 0;

	/**
	 * 标识系统构建数据堆
	 */
	private static int SYS_KEY = 0;

	/**
	 * 标识应用构建数据堆
	 */
	private static int APP_KEY = 1;

	/**
	 * 缺省：-1 没有限制
	 *128*1024 :128K
	 */
	private int m_nDefaultProfileSizeForCache = -1;

	/**
	 * Path事件开关："0"为FALSE；"1"为TRUE
	 */
	public int setPath = 1;

	/**
	 * Action事件开关:"0"为FALSE；"1"为TRUE
	 */
	public int setAction = 1;


	/**
	 * 是否没个Request都创建profile
	 */
	private boolean m_bCreateProfilePerRequest = false;


	/**
	 * profile是否放入session，同session绑定
	 */
	private boolean m_bIsSessionListener = false;
	


	/**
	 * 构造器
	 * 
	 * 读取配置文件，初始化数据
	 * 
	 * @roseuid 3FB99FC1024A
	 */
	public ProfileMgr() {
		loadConf();
	}

	/**
	 * 得到 sessionPROFILE的大小限制
	 * 缺省：-1 没有限制
	 * @return int - sessionPROFILE的大小限制
	 */

	public int getDefaultProfileSizeForCache() {
		return this.m_nDefaultProfileSizeForCache;
	}

	/**
	 * 根据 sessionID 取得暂存数据PROFILE
	 * 
	 * 静态方法 ：为 getProfile(...) 的静态代理方法
	 * 
	 * 具体操作流程:
	 * 
	 * 1.得到 ProfileMgr 的全局单一实例 pm
	 * 
	 * 2.根据 getProfile
	 * 
	 * @param loc - 标识数据堆位置：WEB还是APP
	 * @param sessionid - 当前连接的sessionid号
	 * @return venus.frames.mainframe.base.currentlogin.IProfile - SessionProfile数据堆对象
	 * @roseuid 3FB4899D01B4
	 */
	public static IProfile getSessionProfile(String loc, String sessionid)
		throws ProfileException {

		if (loc == null || sessionid == null) {
			LogMgr.getLogger(
				"venus.frames.mainframe.base.currentlogin.ProfileMgr").error(
				"getSessionProfile(loc,sessionid) : 参数为空");
			return null;
		}
		SessionProfile sesspro = null;

		//获取全局单一实例
		ProfileMgr pm = getSingleton();

		//调用方法getProfile得到根据loc和sessionid查询到的SessionProfile实例
		//并返回此实例
		sesspro = (SessionProfile) pm.getProfile(loc, sessionid);
		return sesspro;
	}
	
	/**
	 * 添加：胡捷
	 * 时间：2005-7-28
	 * 根据 loc  在 m_hashSimpleProfileTableByLoc 查询所有的SessionProfile
	 * 
	 * @param loc - 标识数据堆位置：WEB还是APP
	 * @return venus.frames.mainframe.base.currentlogin.SimpleProfile - 该用户的SessionProfile对象
	 */
	public List getAllSessionProfiles(String loc) 
	{
		if (loc == null) 
		{
			LogMgr.getLogger(
				"venus.frames.mainframe.base.currentlogin.ProfileMgr").error(
				"方法getAllSessionProfiles(loc): 参数loc 不可为空");
			return null;
		}

		//如果m_hashSimpleProfileTableByLoc中存在由loc和loginName查询该用户对应的数据堆
		//则返回此数据堆
		//Hashtable ht = getProfileRegister(SIMPLEPROFILE_REGISTER_KEY);
		
		Object o =  m_hashSessionProfileTableByLoc.get(loc);
		
		if( o==null ) return new ArrayList();
		
		IContext htsessionp =(IContext)o;
		//IContext hashsimpl = (IContext) m_hashSimpleProfileTableByLoc.get(loc);
		
		List sessionProfileList=new Vector();
		
	     for (Enumeration e = htsessionp.getAttributeNames() ; e.hasMoreElements() ;)
	     {
	        String sessionId=(String)e.nextElement();
	 		if (containSessionProfile(loc,sessionId )) 
	 		{
	 		   
		        Object p = htsessionp.getAttribute(sessionId);
		        
		        if( p!=null && p instanceof SessionProfile){
		        	
		        	sessionProfileList.add(p);
		     		
		        }
	 		   
			}
	     }
	     
	    return sessionProfileList;
	}

	/**
	 * 构建该session 对应 PROFILE
	 * 
	 * 1.首先根据 containProfile(loc, loginName ) 查询用户数据堆是否存在，如果存在 
	 * 构建对象SessionProfile(null,null,sessionid,loginName,loc,getDefaultLifeTime())
	 * 
	 * 如不存在则开始调用插件初始化数据堆
	 * 
	 * 2.首先如果传入的hts为null 构建一个空的 Hashtable() hts 否则直接使用hts
	 * 然后运行 runPlugInCreate(loc,SYS_KEY,hts,) 得到系统构建的数据堆
	 * 
	 * 3.再如果传入的hta为null 构建一个空的 Hashtable() hta 否则直接使用hta
	 *  
	 * 根据 loc 在 m_hashListenerNames 中查得 Listener的名字列表，并实例化存入 hta
	 * 然后运行 runPlugInCreate(loc,APP_KEY,hta) 得到应用构建的数据堆
	 * 
	 * 4.根据这两个 Hashtable 构建 
	 * SessionProfile(hts,hta,sessionid,null,null,loginName,loc,getDefaultLifeTime )
	 * 
	 * @param sessionid - 当前连接的sessionid号
	 * @param loc - 标识数据堆位置：WEB还是APP
	 * @param lifeTime - 所创建的数据堆对象生存时间
	 * @param loginName - 该用户的登录名
	 * @param req - 当前请求对象
	 * @param hts - 用来存放系统数据堆属性
	 * @param hta - 用来存放应用数据堆属性
	 * @return venus.frames.mainframe.base.currentlogin.IProfile - SessionProfile数据堆对象
	 * @roseuid 3FB48A01007C
	 */
	public IProfile createProfile(
		String sessionid,
		String loc,
		long lifeTime,
		String loginName,
		HttpServletRequest req,
		Hashtable hts,
		Hashtable hta) {

		/******/
		LogMgr.getLogger(this.getClass().getName()).info(
			"createProfile(...).... loginName=" + loginName);
		/*****/

		if (sessionid == null || loc == null) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"方法createProfile() ： 参数loc,sessionid不可以为空");
			return null;
		}
		
		if (containSessionProfile(loc, sessionid)) {
			
			//Hashtable ht = getProfileRegister(SESSIONPROFILE_REGISTER_KEY);
			
			
			
			IProfile ip = (IProfile)(((IContext)m_hashSessionProfileTableByLoc.get(loc)).getAttribute(sessionid));
			
			String tmploginname = (String)ip.getSysAttribute(SimpleProfile.SYS_LOGIN_NAME);
			
			if (loginName != null){
				
				if (loginName.equals(tmploginname)) return ip;				
			
			} 
			
		}

		IProfile sepro = null;
		Hashtable hts1 = new Hashtable();
		Hashtable hta1 = new Hashtable();
		Hashtable htListener = new Hashtable();

		//如果不存在则调用插件初始化数据堆
		if (hts == null)
			hts = new Hashtable();
		if (hta == null)
			hta = new Hashtable();
		

		//查询m_hashListenerNames中以loc为KEY的事件监听名字列表，
		//取出列表，实例化其中每项，同时存入哈稀表hta和htListener
		if (m_hashListenerNames.containsKey(loc)) {

			String[] listary = (String[]) m_hashListenerNames.get(loc);
			int size = listary.length;
			for (int i = 0; i < size; i++) {
				String listname = listary[i];
				try {
					//实例化列表中每一项
					PropertyChangeListener prolist =
						(PropertyChangeListener) ClassLocator
							.loadClass(listname)
							.newInstance();
					//存入htal
					hta.put("listname" + i, prolist);
					htListener.put("listname" + i, prolist);
				} catch (IllegalAccessException iae) {
					LogMgr.getLogger(this.getClass().getName()).error(
						"createProfile IllegalAccessException",
						iae);
					continue;
				} catch (ClassNotFoundException cnfe) {
					LogMgr.getLogger(this.getClass().getName()).error(
						"createProfile ClassNotFoundException",
						cnfe);
					continue;
				} catch (InstantiationException ie) {
					LogMgr.getLogger(this.getClass().getName()).error(
						"createProfile InstantiationException",
						ie);
					continue;
				}
			}
		}
		//查询用户数据堆是否存在,如果存在，则构建 
		// SessionProfile(loc,sessionid,loginName,htListener,getDefaultLifeTime )
		if (loginName != null) {
			if (containProfile(loc, loginName)) {

				sepro =
					new SessionProfile(
						loc,
						loginName,
						sessionid,
						htListener,
						lifeTime);

			}else{
			

				//如果该用户数据堆不存在，用户名不为空，
				//则运行runPlugInCreate（...）提取用户数据信息。
				hts1 =
					runPlugInCreate(loc, SYS_KEY, sessionid, loginName, hts, req);
				hta1 =
					runPlugInCreate(loc, APP_KEY, sessionid, loginName, hta, req);
					

				//构建SessionProfile，并返回此SessionProfile实例
				sepro =
					new SessionProfile(hts1, hta1, sessionid, loginName, loc, lifeTime);

			
			}


		}else{
//			构建SessionProfile，并返回此SessionProfile实例
			sepro =
				new SessionProfile(hts1, hta1, sessionid, loginName, loc, lifeTime);
					
		
		}
		
		//Hashtable ht = getProfileRegister(SESSIONPROFILE_REGISTER_KEY);
		

		//将新构建的SessionProfile实例存储
		if (m_hashSessionProfileTableByLoc.containsKey(loc)) {
			IContext htsessionp =
				(IContext) m_hashSessionProfileTableByLoc.get(loc);
			
			htsessionp.setAttribute(sessionid, sepro);
			
		} else {
			
			//Hashtable htsessionp = new Hashtable();
			
			IContext htsessionp = ContextMgr.getContext(SESSIONPROFILE_REGISTER_KEY+ CacheFactory.CACHE_NAME_SEPARATOR+loc);
			
			htsessionp.setAttribute(sessionid, sepro);
			m_hashSessionProfileTableByLoc.put(loc, htsessionp);	
					
		}
		
		//saveRegister(SESSIONPROFILE_REGISTER_KEY,ht);	
			
		if(this.isSessionListener()) {
			SessionBindingListener sbl = new SessionBindingListener(sessionid,loc);
			if( req.getSession().getAttribute(IGlobalsKeys.PROFILE_LISTENER_KEY)==null ){
				req.getSession().setAttribute(IGlobalsKeys.PROFILE_LISTENER_KEY,sbl);
			}
		}
		
		return sepro;
	}

	/**
	 * 根据 loc 和 loginName 在 m_hashSimpleProfileTableByLoc 查询该用户对应的数据堆
	 * 
	 * @param loc - 标识数据堆位置：WEB还是APP
	 * @param loginName - 该用户的登录名
	 * @return venus.frames.mainframe.base.currentlogin.SimpleProfile - 该用户的SimpleProfile对象
	 * @roseuid 3FB4909A0166
	 */
	public SimpleProfile getSimpleProfile(String loc, String loginName) {

		if (loc == null || loginName == null) {
			LogMgr.getLogger(
				"venus.frames.mainframe.base.currentlogin.ProfileMgr").error(
				"方法getSimpleProfile(loc,loginName): 参数loc,loginName 不可为空");
			return null;
		}

		//如果m_hashSimpleProfileTableByLoc中存在由loc和loginName查询该用户对应的数据堆
		//则返回此数据堆
		//Hashtable ht = getProfileRegister(SIMPLEPROFILE_REGISTER_KEY);
		
		//IContext htsessionp =
		//						(IContext) m_hashSessionProfileTableByLoc.get(loc);
		
		
		if (containProfile(loc, loginName)) {
			
			IContext hashsimpl = (IContext) m_hashSimpleProfileTableByLoc.get(loc);
				
				
			return (SimpleProfile) hashsimpl.getAttribute(loginName);
		}

		//否则返回空
		return null;
	}

	
	/**
	 * 添加：胡捷
	 * 时间：2005-6-30
	 * 根据 loc  在 m_hashSimpleProfileTableByLoc 查询所有用户对应的数据堆
	 * 
	 * @param loc - 标识数据堆位置：WEB还是APP
	 * @return venus.frames.mainframe.base.currentlogin.SimpleProfile - 该用户的SimpleProfile对象
	 */
	public List getAllSimpleProfiles(String loc) 
	{
		if (loc == null) 
		{
			LogMgr.getLogger(
				"venus.frames.mainframe.base.currentlogin.ProfileMgr").error(
				"方法getAllSimpleProfiles(loc): 参数loc 不可为空");
			return null;
		}

		//如果m_hashSimpleProfileTableByLoc中存在由loc和loginName查询该用户对应的数据堆
		//则返回此数据堆
		//Hashtable ht = getProfileRegister(SIMPLEPROFILE_REGISTER_KEY);
		
		//IContext htsessionp =(IContext) m_hashSessionProfileTableByLoc.get(loc);
		
		
		Object t =  m_hashSimpleProfileTableByLoc.get(loc);
		
		if( t==null ) return new ArrayList();
		
		IContext hashsimpl = (IContext)t ;
		
		List simpleProfileList=new Vector();
		
	     for (Enumeration e = hashsimpl.getAttributeNames() ; e.hasMoreElements() ;)
	     {
	        String loginName=(String)e.nextElement();
	        
	        Object o = hashsimpl.getAttribute(loginName);
	        
	        if( o!=null && o instanceof SimpleProfile){
	        	
	     		simpleProfileList.add(o);
	     		
	        }
	     }
	     
	    return simpleProfileList;
	}
	
	/**
	 * 得到 ProfileMgr 的全局单实例对象
	 * 
	 * 如果该实例m_Singleton为 null 则新建ProfileMgr 的实例
	 * 
	 * 并赋给 m_Singleton
	 * 
	 * 若该实例m_Singleton 不为 null 则返回该实例
	 * 
	 * @return venus.frames.mainframe.base.currentlogin.ProfileMgr - 该类全局单实例
	 * @roseuid 3FB490D0009B
	 */
	public static ProfileMgr getSingleton() {

		//如果ProfileMgr的全局单实例对象为null，则新建一个实例对象并返回该实例
		if (m_Singleton == null) {
			m_Singleton = new ProfileMgr();
			return m_Singleton;
		}

		//若该实例m_Singleton不为 null 则返回该实例
		return m_Singleton;
	}

	/**
	 * 根据 loc 和 session 在 m_hashSessionProfileTableByLoc中 得到 
	 * 
	 * 该连接的SessionProfile
	 * 
	 * 查询 loc 相应的暂存列表中是否存在该 sessionid 的 profile 如果存在则返回  
	 * (getProfile ), 如果不存在则抛出异常 ProfileException 说明该暂存数据不存在
	 * 
	 * @param loc - 标识数据堆位置：WEB还是APP
	 * @param sessionid - 当前连接的sessionid号
	 * 
	 * @return venus.frames.mainframe.base.currentlogin.SessionProfile - SessionProfile数据堆对象
	 */
	public IProfile getProfile(String loc, String sessionid)
		throws ProfileException {

		if (loc == null || sessionid == null) {
			LogMgr.getLogger(this).warn(
				"方法getProfile(loc,sessionid):参数loc, sessionid 不可以为空");
			return null;
		}

		IContext ht = null;
		//如果m_hashSessionProfileTableByLoc以loc为标识的SessionProfile的Hashtable
		//则取出此Hashtable
		//Hashtable hts = getProfileRegister(SESSIONPROFILE_REGISTER_KEY);
		
		if ( m_hashSessionProfileTableByLoc.containsKey(loc) ) {
			
			ht = (IContext) m_hashSessionProfileTableByLoc.get(loc);

			//如果所得到的Hashtable中存在以sessionid为标识的SessionProfile实例对象，
			//则取出并返回此实例
			if ( ht.getAttribute(sessionid) != null ) {
				
				IProfile iprofile = (IProfile) ht.getAttribute(sessionid);
				return iprofile;

				//如果不存在则抛出异常 ProfileException 说明该暂存数据不存在
			} else {
//				LogMgr.getLogger(this.getClass().getName()).warn(
//					"getProfile(...) :ProfileException 该暂存数据(IProfile)不存在");
				return null;
			}
		} else {
//			LogMgr.getLogger(this.getClass().getName()).warn(
//				"getProfile(...) : ProfileException  该暂存数据(IProfile)不存在");
			return null;
		}
	}

	/**
	 * 根据 loc 和 loginName 在 m_hashSimpleProfileTableByLoc 
	 * 查询该用户对应的数据堆是否存在，存在则返回TRUE，否则返回FALSE
	 * 
	 * @param loc - 标识数据堆位置：WEB还是APP
	 * @param loginName - 该用户的登录名
	 * @return boolean
	 * @roseuid 3FB991340360
	 */
	public boolean containProfile(String loc, String loginName) {
		IContext hashsimp = null;

		if (loc == null || loginName == null) {
			LogMgr.getLogger(
				"venus.frames.mainframe.base.currentlogin.ProfileMgr").error(
				"方法containProfile(loc,loginName): 参数loc,loginName不可以为空");
			return false;
		}
		
		//查找m_hashSimpleProfileTableByLoc是否存在以loc为标识的保存simpleProfile的Hashtable
		//如果存在则取出并在此Hashtable中查找是否存在以loginName为标识的项，如果存在则返回True
		//否则返回FALSE
		//Hashtable ht = getProfileRegister(SIMPLEPROFILE_REGISTER_KEY);
		
		if (m_hashSimpleProfileTableByLoc.containsKey(loc)) {
			hashsimp = (IContext) m_hashSimpleProfileTableByLoc.get(loc);
			if ( hashsimp.getAttribute(loginName) != null)			
				return true;
		}
		return false;
	}
	
	public boolean containSessionProfile(String loc, String sessionId) {
		IContext hashsimp = null;

		if (loc == null || sessionId == null) {
			LogMgr.getLogger(
				"venus.frames.mainframe.base.currentlogin.ProfileMgr").error(
				"方法containSessionProfile(loc,sessionId): 参数loc,loginName不可以为空");
			return false;
		}
		
		//查找m_hashSessionProfileTableByLoc是否存在以loc为标识的保存sessionProfile的Hashtable
		//如果存在则取出并在此Hashtable中查找是否存在以sessionId为标识的项，如果存在则返回True
		//否则返回FALSE
		
		//Hashtable ht = getProfileRegister(SESSIONPROFILE_REGISTER_KEY);
		
		if ( m_hashSessionProfileTableByLoc.containsKey(loc)) {
			hashsimp = (IContext) m_hashSessionProfileTableByLoc.get(loc);
			if (hashsimp.getAttribute(sessionId) != null)
				return true;
		}
		return false;
	}

	/**
	 * 将 SimpleProfile 加入列表供管理保存
	 * 
	 * @param sf - 传入的SimpleProfile对象
	 * @param loc - 标识数据堆位置：WEB还是APP
	 * @return void
	 * @roseuid 3FB9961F0028
	 */
	public void addSimpleProfile(SimpleProfile sf, String loc) {
		IContext htsimpl;

		if (loc == null || sf == null) {
			LogMgr.getLogger(this).error(
				"方法addSimpleProfile(): 参数loc,SimpleProfile不可以为空");
			return;
		}
		String name = sf.getLoginName();
		
		//Hashtable ht = getProfileRegister(SIMPLEPROFILE_REGISTER_KEY);
		
		
		
		if (name != null) {
			if (containProfile(loc, name)) {
				return;
			}
			//如果在m_hashSimpleProfileTableByLoc中不存在以loc为KEY的Hashtable
			//则构建一个Hashtable实例并以sf.getLoginName()为KEY保存sf
			//再把构建的Hashtable实例以loc为KEY存入m_hashSimpleProfileTableByLoc
			
			if (!m_hashSimpleProfileTableByLoc.containsKey(loc)) {
				
				
				//htsimpl = new Hashtable();
				
				htsimpl = ContextMgr.getContext(SIMPLEPROFILE_REGISTER_KEY+"/"+loc);
				
				htsimpl.setAttribute(sf.getLoginName(), sf);
				
				m_hashSimpleProfileTableByLoc.put(loc, htsimpl);
				
				//saveRegister(SIMPLEPROFILE_REGISTER_KEY,ht);
				return;
			} else {			

				//如果存在，则提取并把sf以sf.getLoginName()为KEY存入
				htsimpl = (IContext) m_hashSimpleProfileTableByLoc.get(loc);
		
				htsimpl.setAttribute(sf.getLoginName(), sf);
			
			}
		}

		
		//saveRegister(SIMPLEPROFILE_REGISTER_KEY,ht);
		
	}
	
	/**
	 * 将 SimpleProfile 加入列表供管理保存
	 * 
	 * @param sf - 传入的SimpleProfile对象
	 * @param loc - 标识数据堆位置：WEB还是APP
	 * @return void
	 * @roseuid 3FB9961F0028
	 */
	public void updateSimpleProfile(SimpleProfile sf, String loc) {
		IContext htsimpl;

		if (loc == null || sf == null) {
			LogMgr.getLogger(this).error(
				"方法addSimpleProfile(): 参数loc,SimpleProfile不可以为空");
			return;
		}
		String name = sf.getLoginName();
		
		//Hashtable ht = getProfileRegister(SIMPLEPROFILE_REGISTER_KEY);
		
		
		
		if (name != null) {

			//如果在m_hashSimpleProfileTableByLoc中不存在以loc为KEY的Hashtable
			//则构建一个Hashtable实例并以sf.getLoginName()为KEY保存sf
			//再把构建的Hashtable实例以loc为KEY存入m_hashSimpleProfileTableByLoc
			
			if (!m_hashSimpleProfileTableByLoc.containsKey(loc)) {

				//htsimpl = new Hashtable();
				
				htsimpl = ContextMgr.getContext(SIMPLEPROFILE_REGISTER_KEY+"/"+loc);
				
				htsimpl.setAttribute(sf.getLoginName(), sf);
				
				m_hashSimpleProfileTableByLoc.put(loc, htsimpl);
				
				//saveRegister(SIMPLEPROFILE_REGISTER_KEY,ht);
				return;
			} else {			

				//如果存在，则提取并把sf以sf.getLoginName()为KEY存入
				htsimpl = (IContext) m_hashSimpleProfileTableByLoc.get(loc);
			
				htsimpl.setAttribute(sf.getLoginName(), sf);
			
			}
		}

		
	}

	/**
	* 加载配置数据
	* 
	* 1.配置数据有m_nDefaultLifeTime 
	* 
	* 对应于配置数据中的配置数据属性名为 "DefaultProfileLifeTime"
	* 
	* m_nDefaultProfileSizeForCache
	* 
	* 对应于配置数据中的配置数据属性名为 "DefaultProfileSizeForCache"
	* 
	* 2.加载数据堆处理插件名的列表
	* 
	* 有 4 组插件列表 ： 
	* WEB端系统数据堆插件，WEB端应用数据堆插件，APP端系统数据堆插件，APP端应用数据堆插件
	* 
	* 3.加载时间监听器配置数据
	* 
	* 在配置文件中样例为：
	* 
	* <venus.frames.mainframe.base.currentlogin.ProfileMgr 
	* DefaultProfileLifeTime="-1" 
	* DefaultProfileSizeForCache="10485760"><!--10*1024*1024=10M-->
	* <plugins>
	* <plugin name="venus.frames.mainframe.base.currentlogin.DefaultSysWebProfile" 
	* loc="web" who="sys"/>
	* <plugin name="venus.frames.mainframe.base.currentlogin.DefaultSysAppProfile" 
	* loc="app" who="sys"/>
	* </plugins>
	* <listeners> 
	* <listener name="venus.frames.mainframe.base.currentlogin.DefaultActionListener" 
	* loc="web"/>
	* <listener name="venus.frames.mainframe.base.currentlogin.DefaultActionBSListener" 
	* loc="app"/>
	* </listeners>
	* </venus.frames.mainframe.taskmgr.ServiceKeeper>
	* 
	* @return void
	* @roseuid 3FB9A1B20289
	*/

	protected void loadConf() {

		//声明暂存配置文件信息的Vector
		Vector vws = new Vector();
		Vector vwa = new Vector();
		Vector vas = new Vector();
		Vector vaa = new Vector();

		Vector vsw = new Vector();
		Vector vsa = new Vector();

		//读取配置文件，
		//首先读取venus.frames.mainframe.base.currentlogin.ProfileMgr（第一层）节点
		IConfReader dcr =
			ConfMgr.getConfReader(this.getClass().getName());

		//提取本节点DefaultProfileLifeTime属性值

		this.m_nDefaultLifeTime =
			dcr.readIntAttribute("DefaultProfileLifeTime");

		//提取本节点DefaultProfileSizeForCache属性值
		this.m_nDefaultProfileSizeForCache =
			dcr.readIntAttribute("DefaultProfileSizeForCache");

		this.setPath = dcr.readIntAttribute("setPath");

		this.setAction = dcr.readIntAttribute("setAction");
		
		this.m_bCreateProfilePerRequest = dcr.readBooleanAttribute("CreateProfilePerRequest");
		
		this.m_bIsSessionListener = dcr.readBooleanAttribute("isSessionListener");
		

		//读取该节点下plugins子节点列表
		ArrayList aryplugins = dcr.readChildNodesAry("plugins");

		if (aryplugins == null) {
			return;
		}
		//读取节点列表"plugins"的"plugin"子节点
		//并取得其"name"、"loc"、"who"属性，
		//并按四种方式进行判断，存入四组Vector
		int sizeplugin = aryplugins.size();
		for (int i = 0; i < sizeplugin; i++) {
			DefaultConfReader dcrplus =
				new DefaultConfReader((Node) aryplugins.get(i));
			ArrayList aryplu = dcrplus.readChildNodesAry("plugin");
			int sizearyplu=aryplu.size();
			for (int j = 0; j < sizearyplu; j++) {
				DefaultConfReader dcrp =
					new DefaultConfReader((Node) aryplu.get(j));
				String namep = dcrp.readStringAttribute("name");
				String locp = (String) dcrp.readStringAttribute("loc");
				int whop = dcrp.readIntAttribute("who");

				//对取得的属性值进行判断,并放放相应的Vector
				try {
					if (locp.equals(IGlobalsKeys.WEB_CONTEXT_KEY)
						&& whop == SYS_KEY) {
						vws.addElement(namep);
					}
					if (locp.equals(IGlobalsKeys.WEB_CONTEXT_KEY)
						&& whop == APP_KEY) {
						vwa.addElement(namep);
					}
					if (locp.equals(IGlobalsKeys.APP_CONTEXT_KEY)
						&& whop == SYS_KEY) {
						vas.addElement(namep);
					}
					if (locp.equals(IGlobalsKeys.APP_CONTEXT_KEY)
						&& whop == APP_KEY) {
						vaa.addElement(namep);
					}
				} catch (NullPointerException npe) {
					LogMgr.getLogger(this).error(
						"loadConf():配置文件Plugin节点有错误",
						npe);
				}
			}
		}

		//调用方法savePlugin(),把暂存到四组vector的插件名放入相应的插件名列表，
		//并把插件名列表以APP_KEY和SYS_KEY分成两组,以loc为标识
		//分别存入m_hashAppPlugInNames和m_hashSYSPlugInNames
		savePlugin(vws, vwa, vas, vaa);

		//读取"listeners"子节点列表
		ArrayList arylisteners = dcr.readChildNodesAry("listeners");

		//读取节点列表"listeners"的"listener"子节点列表
		//取得其"name"、"loc"属性，
		//并按两种方式进行判断，存入相应的字符串列表
		int sizelisten = arylisteners.size();
		for (int i = 0; i < sizelisten; i++) {
			DefaultConfReader dcr1 =
				new DefaultConfReader((Node) arylisteners.get(i));
			ArrayList arylis = dcr1.readChildNodesAry("listener");
			int sizearylis=arylis.size();
			for (int j = 0; j < sizearylis; j++) {
				DefaultConfReader dcrs =
					new DefaultConfReader((Node) arylis.get(j));
				String names = dcrs.readStringAttribute("name");
				String locs = dcrs.readStringAttribute("loc");

				//对取得的属性值进行判断
				try {
					if (locs.equals(IGlobalsKeys.WEB_CONTEXT_KEY))
						vsw.addElement(names);
					if (locs.equals(IGlobalsKeys.APP_CONTEXT_KEY))
						vsa.addElement(names);
				} catch (NullPointerException npe) {
					LogMgr.getLogger(this).error(
						"loadConf():配置文件Listener节点有错误",
						npe);
				}
			}
		}

		//调用方法saveListener(vector,vector),
		//把暂存到两组vector的listener放入相应的列表，
		//并以loc为标识存入m_hashListenerNames
		saveListener(vsw, vsa);

	}

	/**
	 * 把暂存到四组vector的插件名放入相应的插件名列表，
	 * 并把插件名列表以APP_KEY和SYS_KEY分成两组,以loc为标识
	 * 分别存入m_hashAppPlugInNames和m_hashSYSPlugInNames
	 * 
	 * @param v0 - 存放WEB端系统数据堆插件
	 * @param v1 - 存放WEB端应用数据堆插件
	 * @param v2 - 存放APP端系统数据堆插件
	 * @param v3 - 存放APP端应用数据堆插件
	 * @return void
	 */
	private void savePlugin(Vector vwebsys, Vector vwebapp, Vector vappsys, Vector vappapp) {

		String[] websysary = null;
		String[] webappary = null;
		String[] appsysary = null;
		String[] appappary = null;

		//把取得的name列表存入相应的Hashtable
		if (vwebsys.size() > 0) {
			//websysary = new String[vwebsys.size()];
			//vwebsys.copyInto(websysary);
			
			websysary =  Convertor.copyVectorToStringArray(vwebsys);
			
			
			m_hashSysPlugInNames.put(IGlobalsKeys.WEB_CONTEXT_KEY, websysary);
		}
		if (vwebapp.size() > 0) {
			//webappary = new String[vwebapp.size()];
			//vwebapp.copyInto(webappary);
			
			webappary =  Convertor.copyVectorToStringArray(vwebapp);
			
			
			m_hashAppPlugInNames.put(IGlobalsKeys.WEB_CONTEXT_KEY, webappary);
		}
		if (vappsys.size() > 0) {
			//appsysary = new String[vappsys.size()];
			//vappsys.copyInto(appsysary);
			
			
			appsysary =  Convertor.copyVectorToStringArray(vappsys);
			
			m_hashSysPlugInNames.put(IGlobalsKeys.APP_CONTEXT_KEY, appsysary);
		}
		if (vappapp.size() > 0) {
			//appappary = new String[vappapp.size()];
			//vappapp.copyInto(appappary);
			
			appappary =  Convertor.copyVectorToStringArray(vappapp);
			
			m_hashAppPlugInNames.put(IGlobalsKeys.APP_CONTEXT_KEY, appappary);
		}
	}

	/**
	 * 把暂存到两组vector的listener名字放入相应的列表，
	 * 并以loc为标识存入m_hashListenerNames
	 * @param vsw - 存放WEB端Listener 类名的列表
	 * @param vsa - 存放APP端Listener 类名的列表
	 * @return void
	 */
	private void saveListener(Vector vsw, Vector vsa) {

		String[] listwebary = null;
		String[] listappary = null;

		//把取得的name列表存入相应的Hashtable
		if (vsw.size() > 0) {
			//listwebary = new String[vsw.size()];
			//vsw.copyInto(listwebary);
			
			listwebary =  Convertor.copyVectorToStringArray(vsw);
			
			m_hashListenerNames.put(IGlobalsKeys.WEB_CONTEXT_KEY, listwebary);
		}
		if (vsa.size() > 0) {
			//listappary = new String[vsa.size()];			
			//vsa.copyInto(listappary);
			
			listappary =  Convertor.copyVectorToStringArray(vsa);
			
			m_hashListenerNames.put(IGlobalsKeys.APP_CONTEXT_KEY, listappary);
		}
	}

	/**
	 * 得到系统配置的缺省临时数据生命时长
	 * 
	 * 返回 m_nDefaultLifeTime,单位为毫秒
	 * 
	 * 暂时配置文件中配置为 LIVE_ALWAYS
	 * 
	 * @return long - 系统配置的缺省临时数据生命时长
	 * @roseuid 3FB9A2A90252
	 */

	public long getDefaultLifeTime() {
		return this.m_nDefaultLifeTime;
	}

	/** 
	* 
	* 根据loc(WEB_CONTEXT_KEY | APP_CONTEXT_KEY)
	* 
	* who : SYS_KEY | APP_KEY
	* 
	* 运行插件实例
	* 
	* 循环调用插件的 onCreateProfileForLoginer(....) 方法
	* 
	* 将上一插件返回的 Hashtable 当作参数传给下一插件运行
	* 
	* @ param loc - 标识数据堆位置：WEB还是APP
	* @ param who - 构建标识：系统数据端还是应用数据端
	* @ return Hashtable - 得到数据堆属性
	* @ roseuid 3F B9C2DD036B
	*/

	private Hashtable runPlugInCreate(String loc, int who, String sessionid, String loginName, Hashtable ht, HttpServletRequest req) {

		LogMgr.getLogger(this).info("runPlugInCreate(loc:"+loc+",who:"+who+",sessiunid:"+sessionid+",loginName"+loginName+") ...");

		//如果为应用数据插件
		if (who == APP_KEY) {

			// 调用runAppPluginCreate(...)方法,运行当who == APP_KEY时的运行插件
			// 循环调用插件的 onCreateProfileForLoginer(....) 方法
			// 将上一插件返回的 Hashtable 当作参数传给下一插件运行
			runAppPluginCreate(loc, sessionid, loginName, ht, req);

		}
		//如果为系统数据插件实例
		if (who == SYS_KEY) {

			//调用runSysPluginCreate(...)方法,运行当who == SYS_KEY时的运行插件
			//循环调用插件的 onCreateProfileForLoginer(....) 方法
			//将上一插件返回的 Hashtable 当作参数传给下一插件运行
			runSysPluginCreate(loc, sessionid, loginName, ht, req);

		}
		return ht;
	}

	/**
	 * 运行当who == APP_KEY时的运行插件实例
	 * 循环调用插件的 onCreateProfileForLoginer(....) 方法,
	 * 将上一插件返回的 Hashtable 当作参数传给下一插件运行
	 * 
	 * @param loc - 标识数据堆位置：WEB还是APP
	 * @param sessionid - 当前连接的sessionid
	 * @param loginName - 该用户的登录名
	 * @param ht - 传入用来存放应用数据属性值
	 * @param req - 当前请求对象
	 * @return Hashtable - 得到的应用数据属性值
	 */
	private Hashtable runAppPluginCreate(
		String loc,
		String sessionid,
		String loginName, Hashtable ht, HttpServletRequest req) {

		//查找m_hashAppPlugInsByLoc中是否存在以loc为标识应用数据插件实例列表
		if (m_hashAppPlugInsByLoc.containsKey(loc)) {

			//得到插件实例列表
			IProfilePlugIn[] ipluginAppary =
				(IProfilePlugIn[]) m_hashAppPlugInsByLoc.get(loc);

			//运行各插件的onCreateProfileForLoginer(....) 方法,
			//将上一插件返回的 Hashtable 当作参数传给下一插件
			int sizeAppary = ipluginAppary.length;
			for (int i = 0; i < sizeAppary; i++) {
				Hashtable hti = ht;
				try {
					ht =
						ipluginAppary[i].onCreateProfileForLoginer(
							sessionid,
							loginName,
							hti,
							req);
				} catch (Exception e) {
					LogMgr.getLogger(this.getClass().getName()).warn(
						"runAppPluginCreate(...): who == APP_KEY m_hashAppPlugInNames  Exception",
						e);
					continue;
				}
				
			}
			return ht;
		} else {
			//如果为以loc为标识应用数据插件名字，则取出插件名字列表，
			//进行实例化，调用插件实例的onCreateProfileForLoginer(....) 方法
			//把实例存入m_hashAppPlugInsByLoc

			if (!m_hashAppPlugInNames.containsKey(loc)) {
				return ht;
			}

			Vector v = new Vector();
			IProfilePlugIn[] pary = null;

			//以loc为标识取出插件名字列表
			String[] ipluginNameAppary =
				(String[]) m_hashAppPlugInNames.get(loc);
			int size = ipluginNameAppary.length;

			//把各插件从列表中取出并实例化,调用其onCreateProfileForLoginer(....) 方法
			for (int j = 0; j < size; j++) {
				
				try {
					Hashtable hti = ht;
					IProfilePlugIn iplugin = null;
					try {
						Class classFor =
							ClassLocator.loadClass(ipluginNameAppary[j]);
						iplugin = (IProfilePlugIn) classFor.newInstance();
	
					} catch (ClassNotFoundException cnfe) {
						LogMgr.getLogger(this.getClass().getName()).warn(
							"runAppPluginCreate(...): who == APP_KEY m_hashAppPlugInNames  ClassNotFoundException",
							cnfe);
						continue;
					} catch (IllegalAccessException iae) {
						LogMgr.getLogger(this.getClass().getName()).warn(
							"runAppPluginCreate(...): who == APP_KEY m_hashAppPlugInNames  IllegalAccessException",
							iae);
						continue;
					} catch (InstantiationException ie) {
						LogMgr.getLogger(this.getClass().getName()).warn(
							"runAppPluginCreate(...): who == APP_KEY m_hashAppPlugInNames  InstantiationException",
							ie);
						continue;
					}
					//调用插件实例的onCreateProfileForLoginer(....) 方法
					if (iplugin != null) {
						v.addElement(iplugin);
							ht = iplugin.onCreateProfileForLoginer(sessionid, loginName, hti, req);
					}
				
				} catch (Exception e) {
					LogMgr.getLogger(this.getClass().getName()).warn(
						"runAppPluginCreate(...): who == APP_KEY m_hashAppPlugInNames  Exception",
						e);
					continue;
				}
			
			}

			//将实例化的plugin存入m_hashAppPlugInsByLoc
			if (v.size() > 0) {
				//pary = new IProfilePlugIn[v.size()];
				//v.copyInto(pary);
				pary =  copyVectorToIProfilePlugInArray(v);
				m_hashAppPlugInsByLoc.put(loc, pary);
				/**************/
				LogMgr.getLogger(this.getClass().getName()).info(
					"runPlugInCreate app_key 已经运行，实例已存入");
				/**************/
			}
			return ht;
		}
	}

	
	private IProfilePlugIn[] copyVectorToIProfilePlugInArray(Vector v){
		
		int len = v.size();
		
		if ( len < 1 ) return new IProfilePlugIn[0];
		
		IProfilePlugIn[] re = new IProfilePlugIn[len];
		
		Iterator iter = v.iterator();
		
		int i = 0;
		while(iter.hasNext()){
			
			re[i] = (IProfilePlugIn)iter.next();
			i++;		
		}
				
		return re;
		
	}
	
	
	/**
	 * 运行当who == SYS_KEY时的运行插件实例
	 * 循环调用插件的 onCreateProfileForLoginer(....) 方法
	 * 将上一插件返回的 Hashtable 当作参数传给下一插件运行
	 * 
	 * @param loc - 标识数据堆位置：WEB还是APP
	 * @param sessionid - 当前连接的sessionid
	 * @param loginName - 该用户的登录名
	 * @param ht - 传入用来存放系统数据属性值
	 * @param req - 当前请求对象
	 * @return Hashtabel - 得到的系统数据属性值
	 */
	private Hashtable runSysPluginCreate(
		String loc,
		String sessionid,
		String loginName,
		Hashtable ht,
		HttpServletRequest req) {

		//如果为以loc为标识系统数据插件实例，则取出插件实例列表，
		//调用插件的onCreateProfileForLoginer(....) 方法
		if (m_hashSysPlugInsByLoc.containsKey(loc)) {

			//得到插件实例列表
			IProfilePlugIn[] ipluginSysary =
				(IProfilePlugIn[]) m_hashSysPlugInsByLoc.get(loc);

			//运行各插件的onCreateProfileForLoginer(....) 方法,
			//并将上一插件返回的 Hashtable 当作参数传给下一插件
			for (int i = 0; i < ipluginSysary.length; i++) {
				Hashtable hti = ht;

				try {

					//调用ipluginSysary[i]插件的onCreateProfileForLoginer(....) 方法
					ht =
						ipluginSysary[i].onCreateProfileForLoginer(sessionid, loginName, hti, req);
					
				} catch (Exception e) {
					LogMgr.getLogger(this.getClass().getName()).warn(
						"runAppPluginCreate(...): who == APP_KEY m_hashAppPlugInNames  Exception",
						e);
					continue;
				}
				
				
			}
			return ht;
		} else {
			//如果不存在以为标识的插件名字列表，则返回空
			if (!m_hashSysPlugInNames.containsKey(loc)) {
				return ht;
			}
			//如果存在以loc为标识的插件名字列表，则取出该列表，
			//进行实例化列表中每一项，调用该实例的onCreateProfileForLoginer(....) 方法
			//把实例存入m_hashAppPlugInsByLoc

			String[] ipluginNameSysary =
				(String[]) m_hashSysPlugInNames.get(loc);

			Vector v = new Vector();
			IProfilePlugIn[] pluary;
			for (int j = 0; j < ipluginNameSysary.length; j++) {
				
				try {	
					
					IProfilePlugIn iplugin = null;
					Hashtable hti = ht;
					try {
	
						Class classFor =
							ClassLocator.loadClass(ipluginNameSysary[j]);
						iplugin = (IProfilePlugIn) classFor.newInstance();
	
					} catch (ClassNotFoundException cnfe) {
						LogMgr.getLogger(this.getClass().getName()).warn(
							"runSysPluginCreate: who == SYS_KEY m_hashSysPlugInNames  ClassNotFoundException",
							cnfe);
						continue;
					} catch (IllegalAccessException iae) {
						LogMgr.getLogger(this.getClass().getName()).warn(
							"runSysPluginCreate: who == SYS_KEY m_hashSysPlugInNames  IllegalAccessException",
							iae);
						continue;
					} catch (InstantiationException ie) {
						LogMgr.getLogger(this.getClass().getName()).warn(
							"runSysPluginCreate: who == SYS_KEY m_hashSysPlugInNames  InstantiationException",
							ie);
						continue;
					}
					
					if (iplugin != null) {
											
						//将实例化后的插件实例存入Vector
						v.addElement(iplugin);
						
						ht =
							iplugin.onCreateProfileForLoginer(
							sessionid,
							loginName,
							hti,
							req);

					}
				} catch (Exception e) {
					LogMgr.getLogger(this.getClass().getName()).warn(
						"runSysPluginCreate: who == SYS_KEY m_hashSysPlugInNames  InstantiationException",
						e);
					continue;
				}
				
			}

			//将实例化的plugin存入m_hashSysPlugInsByLoc
			if (v.size() > 0) {
				//pluary = new IProfilePlugIn[v.size()];
				//v.copyInto(pluary);
				pluary =  copyVectorToIProfilePlugInArray(v);
				m_hashSysPlugInsByLoc.put(loc, pluary);
				/************/
				LogMgr.getLogger(this.getClass().getName()).info(
					"runPlugInCreate SYS_key 已经运行，实例已存入");
				/************/
			}
			return ht;
		}
	}

	/**
	 * 根据 sessionID 构建暂存数据PROFILE
	 * 
	 * 此方法为 createProfile(....) 方法的静态代理方法
	 * 
	 * 具体操作流程:
	 * 1. 得到 ProfileMgr 的全局单一实例 pm
	 * 
	 * 2. 调用 pm.createProfile(....) 构建并返回
	 * @ param sessionid - 当前连接的sessionid
	 * @ param loc - 标识数据堆位置：WEB还是APP
	 * @ param lifeTime - 该数据堆对象生存时间
	 * @ param loginName - 该用户的登录名
	 * @ return venus.frames.mainframe.base.currentlogin.IProfile - SessionProfile数据堆对象
	 * @ roseuid 3F B9DACC002E
	 */
	public static IProfile createSessionProfile(
		String sessionid,
		String loc,
		long lifeTime,
		String loginName,
		Hashtable hts,
		Hashtable hta) {

		//得到 ProfileMgr 的全局单一实例 pm
		ProfileMgr pm = getSingleton();

		//接受所传参数调用pm.createProfile(....)构建IProfile并返回
		return pm.createProfile(
			sessionid,
			loc,
			lifeTime,
			loginName,
			null,
			hts,
			hta);
	}

	/** 
	* 根据 sessionID 构建暂存数据PROFILE
	* 
	* 此方法为 createProfile(....) 方法的静态代理方法
	* 
	* 具体操作流程:
	* 
	* 1. 得到 ProfileMgr 的全局单一实例 pm
	* 
	* 2. 调用pm.createProfile(....) ，
	* 传入生命时长为系统缺省临时数据生命时长getDefaultLifeTime()并返回
	* 
	* @ param sessionid - 当前连接的sessionid
	* @ param loc - 标识数据堆位置：WEB还是APP
	* @ param loginName - 该用户的登录名 
	* @param hts - 存储系统数据的Hashtable对象
	* @param hta - 存储应用数据的Hashtable对象
	* @ return venus.frames.mainframe.base.currentlogin.IProfile - SessionProfile数据堆对象
	* @ roseuid 3F B9DD2100AB
	*/

	public static IProfile createSessionProfile(
		String sessionid,
		String loc,
		String loginName,
		Hashtable hts,
		Hashtable hta) {

		//得到 ProfileMgr 的全局单一实例 pm
		ProfileMgr pm = getSingleton();

		//得到系统缺省临时数据生命时长
		long lifeTime = pm.getDefaultLifeTime();

		//调用createProfile(....)方法，返回IProfile实例
		//传入生命时长为系统缺省临时数据生命时长getDefaultLifeTime()
		return pm.createProfile(
			sessionid,
			loc,
			lifeTime,
			loginName,
			null,
			hts,
			hta);
	}

	/**
	 * 根据 sessionID 构建暂存数据PROFILE
	* 
	* 此方法为 createProfile(....) 方法的静态代理方法
	* 
	* 具体操作流程 :
	* 
	* 1. 得到ProfileMgr的全局单一实例 pm
	* 
	* 2. 传入的 req提取 sessionid，oginName,(这种方式调用的loc为web)
	* 
	* 3. 调用pm.createProfile(....) ，并返回
	* 
	* @param lifeTime - 当前用户数据堆生存时间
	* @param req - 当前请求对象
	* @param hts - 存储系统数据的Hashtable对象
	* @param hta - 存储应用数据的Hashtable对象
	* @return venus.frames.mainframe.base.currentlogin.IProfile - SessionProfile数据堆对象
	*/

	public static IProfile createSessionProfile(
		long lifeTime,
		HttpServletRequest req,
		Hashtable hts,
		Hashtable hta) {

		if (req == null) {
			LogMgr.getLogger(
				"venus.frames.mainframe.base.currentlogin.ProfileMgr").error(
				"静态方法createSessionProfile(lifeTime,req,hts,hta)方法中参数req不可以为空");
			return null;
		}
		//从传入的req提取sessionid和loginName

		HttpServletRequest request =req;
		String sessionid = request.getSession().getId();
		HttpSession session = request.getSession(true);
		String loginName =
			(String) session.getAttribute(SimpleProfile.SYS_LOGIN_NAME);

		//得到ProfileMgr的全局单一实例
		ProfileMgr pm = getSingleton();

		pm.m_nDefaultLifeTime = lifeTime;
		//调用pm.createProfile(....)，传入生命时长为系统缺省临时数据生命时长getDefaultLifeTime()
		//构建并返回SessionProfile实例
		return pm.createProfile(
			sessionid,
			IGlobalsKeys.WEB_CONTEXT_KEY,
			lifeTime,
			loginName,
			req,
			hts,
			hta);
	}

	/**
	* 根据 sessionID 构建 暂存数据PROFILE
	* 此方法为 createProfile(....) 方法的静态代理方法
	* 具体操作流程
	* 1. 得到 ProfileMgr 的全局单一实例 pm
	* 2. 根据传入的 req 提取 sessionid ， loginName ， (这种方式调用的 loc 为 web)
	* 3. 调用 pm.createProfile(....) 构建并返回
	* 
	* @param req - 当前请求对象
	* @param hts - 存储系统数据的Hashtable对象
	* @param hta - 存储应用数据的Hashtable对象
	* @return venus.frames.mainframe.base.currentlogin.IProfile - SessionProfile数据堆对象
	*/

	public static IProfile createSessionProfile(
		HttpServletRequest req,
		Hashtable hts,
		Hashtable hta) {

		if (req == null) {
			LogMgr.getLogger(
				"venus.frames.mainframe.base.currentlogin.ProfileMgr").error(
				"静态方法createSessionProfile(req,hts,hta)方法中参数req不可以为空");
			return null;
		}

		///从传入的req提取sessionid和loginName
		HttpServletRequest request = req;
		String sessionid = request.getSession().getId();
	
		HttpSession session = request.getSession(true);
		String loginName =
			(String) session.getAttribute(SimpleProfile.SYS_LOGIN_NAME);

		/******/
		LogMgr.getLogger(
			"venus.frames.mainframe.base.currentlogin.ProfileMgr").info(
			"createSessionProfile(HttpServletRequest req,Hashtable hts,Hashtable hta) : loginName="
				+ loginName);
		/******/

		
		//得到ProfileMgr的全局单一实例
		ProfileMgr pm = getSingleton();

		long lifeTime = pm.getDefaultLifeTime();

		//调用pm.createProfile(....)，传入生命时长为系统缺省临时数据生命时长getDefaultLifeTime()
		//构建并返回SessionProfile实例
		return pm.createProfile(
			sessionid,
			IGlobalsKeys.WEB_CONTEXT_KEY,
			lifeTime,
			loginName,
			req,
			hts,
			hta);
	}

	/**
	 * 1.查到该对象
	 * 2.根据该对象得到 sessionid，loginName，并运行销毁插件：runPlugInErase(....)
	 * 2.调用该对象的销毁方法 clear
	 * 3.在 m_hashSessionProfileTableByLoc 中的 Hashtable中清除该数据堆
	 * 
	 * 根据 loc 和 sessionID 在 m_hashSessionProfileTableByLoc 删除该用户对应的数据堆
	 * 
	 * @param loc - 标识数据堆位置：WEB还是APP
	 * @param sessionid - 当前连接的sessionid
	 * @return void
	 * @roseuid 3FB9DFF10148
	 */
	public void eraseSessionProfile(String loc, String sessionid) {

		/***********/
		LogMgr.getLogger(this.getClass().getName()).info(
			"eraseSessionProfile(....)...");
		/*********/

		if (loc == null || sessionid == null) {
			LogMgr.getLogger(this.getClass().getName()).warn(
				"eraseSessionProfile(...) : loc OR sessionid is null");
			return;
		}

		//如果在m_hashSessionProfileTableByLoc中存在以loc为KEY的保存SessionProfile的Hashtable
		//则取出并清除以sessionid为KEY的SessionProfile值
		//Hashtable hts = getProfileRegister(SESSIONPROFILE_REGISTER_KEY);

						
		if (m_hashSessionProfileTableByLoc.containsKey(loc)) {
			
			IContext ht = (IContext) m_hashSessionProfileTableByLoc.get(loc);
			
			
			if (ht.getAttribute(sessionid) != null) {

				//查到该对象
				SessionProfile sp = (SessionProfile)ht.getAttribute(sessionid);
				String loginName = sp.getLoginName();

				//并运行销毁插件：runPlugInErase(....)
				runPlugInErase(loc, SYS_KEY, sessionid, loginName);
				runPlugInErase(loc, APP_KEY, sessionid, loginName);

				//调用该对象的销毁方法 clear
				sp.clear();

				//从Hashtable中清除该数据堆
				ht.removeAttribute(sessionid);
				/****/
				LogMgr.getLogger(this.getClass().getName()).info(
					"eraseSessionProfile(...) : ht.remove(sessionid) ok");
				/****/
			}
		}
		
		//saveRegister(SESSIONPROFILE_REGISTER_KEY,hts);
		return;
	}

	/**
	* 根据 loc 和 loginName 在 m_hashSimpleProfileTableByLoc
	* 
	* 查到该用户的 SimpleProfile
	* 
	* 调用该SimpleProfile的clear 返回结果如果为TRUE ，销毁该句柄 ，若返回FALSE退出
	* 
	* @param loc - 标识数据堆位置：WEB还是APP
	* @param loginName - 该用户的登录名 
	* @return void
	* @ roseuid 3F B9F43A0213
	*/

	public void eraseSimpleProfile(String loc, String loginName) {

		/*****/
		LogMgr.getLogger(this.getClass().getName()).info(
			"eraseSimpleProfile(...)...");
		/******/

		if (loc == null || loginName == null) {
			LogMgr.getLogger(this.getClass().getName()).warn(
				"eraseSimpleProfile(...) : 参数不可以为空");
			return;
		}

		//如果在m_hashSimpleProfileTableByLocc中存在以loc为KEY的保存SimpleProfile的Hashtable
		//则取出并清除以loginName为KEY的SimpleProfile值
		//Hashtable hts = getProfileRegister(SIMPLEPROFILE_REGISTER_KEY);
		
		if (m_hashSimpleProfileTableByLoc.containsKey(loc)) {

			//得到以log为标识的存储SimpleProfile实例的Hashtable
			IContext ht = (IContext) m_hashSimpleProfileTableByLoc.get(loc);

			//从Hashtable中得到该loginName对应的SimpleProfile实例
			if (ht.getAttribute(loginName) !=null ) {
				SimpleProfile simplpro = (SimpleProfile) ht.getAttribute(loginName);

				//调用该SimpleProfile的clear,
				//如返回TRUE,则该SimpleProfile信息删除成功
				//从m_hashSimpleProfileTableByLoc中删除该SimpleProfile
				boolean cle = simplpro.clear();
				
				if (cle == true) {
					
					ht.removeAttribute(loginName);
					
					//saveRegister(SIMPLEPROFILE_REGISTER_KEY,ht);
					LogMgr.getLogger(this.getClass().getName()).info(
						"eraseSimpleProfile(...) : ht.remove(loginName) 清除SimpleProfile成功");

				} else{
					
					return;
				}
					
			}
		}
		return;
	}

	/**
	* 根据 sessionID 删除暂存数据PROFILE
	* 
	* 静态方法 ： 为 eraseSessionProfile(....) 的静态代理方法
	* 
	* 具体操作流程
	* 
	* 1. 得到 ProfileMgr 的全局单一实例 pm
	* 
	* 2. 根据该pm方法eraseSessionProfile（...）删除暂存数据PROFILE
	* 
	* @param loc - 标识数据堆位置：WEB还是APP
	* @param sessionid - 当前连接的sessionid
	* @return void
	* @roseuid 3F B9F457009C
	*/

	public static void eraseProfile(String loc, String sessionid) {

		if (loc == null || sessionid == null) {
			LogMgr.getLogger(
				"venus.frames.mainframe.base.currentlogin.ProfileMgr").error(
				"eraseProfile(loc,sessionid)方法中参数不可以为空");
			return;
		}
		//得到 ProfileMgr 的全局单一实例
		ProfileMgr pm = getSingleton();

		//调用内部方法删除暂存数据PROFILE
		pm.eraseSessionProfile(loc, sessionid);
	}

	/**
	* 根据loc(WEB_CONTEXT_KEY | APP_CONTEXT_KEY)
	* 
	* who : SYS_KEY | APP_KEY
	* 
	* 运行插件实例 onEraseSessionProfile(..) 方法
	* 
	* @param loc - 标识数据堆位置：WEB还是APP
	* @param who - 构建标识：系统数据端还是应用数据端
	* @param sessionid - 当前连接的sessionid
	* @param loginName - 该用户的登录名
	* @ roseuid 3F BA061500FA
	*/

	private void runPlugInErase(
		String loc,
		int who,
		String sessionid,
		String loginName) {

		/*****/
		LogMgr.getLogger(this).info("runPlugInErase(loc:"+loc+",who:"+who+",sessiunid:"+sessionid+",loginName"+loginName+") ...");
		/*****/

		//如果为应用数据插件,调用runAppPlugInErase(...)
		if (who == APP_KEY) {

			//调用runAppPlugInErase(...)方法
			runAppPlugInErase(loc, sessionid, loginName);

		}
		//如果为系统数据插件，调用runSysPlugInErase(...)
		if (who == SYS_KEY) {

			//调用runSysPlugInErase(...)方法
			runSysPlugInErase(loc, sessionid, loginName);
		}
	}

	/**
	 * 由runPlugInErase（...）方法调用，当who == APP_KEY时，运行应用数据插件
	 * 
	 * @param loc - 标识数据堆位置：WEB还是APP
	 * @param sessionid - 当前连接的sessionid
	 * @param loginName - 该用户的登录名
	 */
	private void runAppPlugInErase(
		String loc,
		String sessionid,
		String loginName) {

		//如果m_hashAppPlugInsByLoc中存在以loc为标识应用数据插件实例列表，
		//则取出插件实例列表，调用各插件的onEraseSessionProfile(....) 方法
		if (m_hashAppPlugInsByLoc.containsKey(loc)) {

			//得到以loc为标识应用数据插件实例列表
			IProfilePlugIn[] ipluginary =
				(IProfilePlugIn[]) m_hashAppPlugInsByLoc.get(loc);

			//取出各插件实例,调用其onEraseSessionProfile(....) 方法
			for (int i = 0; i < ipluginary.length; i++) {
				
				try {
					
					ipluginary[i].onEraseSessionProfile(sessionid, loginName);
					
				} catch (Exception e) {
					LogMgr.getLogger(this.getClass().getName()).warn(
						"runAppPlugInErase(...): who == APP_KEY m_hashAppPlugInNames Exception",
						e);
					continue;
				}
				
			}
		} else {

			if (!m_hashAppPlugInNames.containsKey(loc)) {
				return;
			}
			//如果存在以loc为标识应用数据插件名字列表，则取出此字列表，
			//一一进行实例化，调用插件实例的onEraseSessionProfile(....) 方法
			//然后把实例存入m_hashAppPlugInsByLoc

			//得到以loc为标识应用数据插件名字列表
			String[] ipluginNameary = (String[]) m_hashAppPlugInNames.get(loc);
			Vector v = new Vector();
			IProfilePlugIn[] pluary;

			//取出各插件,实例化并调用插件实例的onEraseSessionProfile(....) 方法
			for (int j = 0; j < ipluginNameary.length; j++) {
				try {
					
					IProfilePlugIn iplugin = null;
					try {
	
						//实例化插件类
						Class classFor = ClassLocator.loadClass(ipluginNameary[j]);
						iplugin = (IProfilePlugIn) classFor.newInstance();
					} catch (ClassNotFoundException cnfe) {
						LogMgr.getLogger(this.getClass().getName()).warn(
							"runAppPlugInErase(...): who == APP_KEY m_hashAppPlugInNames  ClassNotFoundException",
							cnfe);
						continue;
					} catch (IllegalAccessException iae) {
						LogMgr.getLogger(this.getClass().getName()).warn(
							"runAppPlugInErase(...): who == APP_KEY m_hashAppPlugInNames  IllegalAccessException",
							iae);
						continue;
					} catch (InstantiationException ie) {
						LogMgr.getLogger(this.getClass().getName()).warn(
							"runAppPlugInErase(...): who == APP_KEY m_hashAppPlugInNames  InstantiationException",
							ie);
						continue;
					}
	
					//调用插件实例的onEraseSessionProfile(....) 方法
					if (iplugin != null) {	
						
						v.addElement(iplugin);
						
						iplugin.onEraseSessionProfile(sessionid, loginName);						
						
					}
					
				} catch (Exception e) {
					LogMgr.getLogger(this.getClass().getName()).warn(
						"runAppPlugInErase(...): who == APP_KEY m_hashAppPlugInNames  Exception",
						e);
					continue;
				}

				
				
			}

			//将实例化的plugin存入m_hashAppPlugInsByLoc
			if (v.size() > 0) {
				pluary =  copyVectorToIProfilePlugInArray(v);
				
				//pluary = new String[v.size()];
				//v.copyInto(pluary);
				m_hashAppPlugInsByLoc.put(loc, pluary);
			}
		}
	}

	/**
	 * 由runPlugInErase（...）方法调用，运行系统数据插件。
	 * 
	 * @param loc - 标识数据堆位置：WEB还是APP
	 * @param sessionid - 当前连接的sessionid
	 * @param loginName - 该用户的登录名
	 * @return void
	 */
	private void runSysPlugInErase(
		String loc,
		String sessionid,
		String loginName) {

		//如果m_hashSysPlugInsByLoc中存在以loc为标识系统数据插件实例列表，则取出此列表，
		//调用各插件的onEraseSessionProfile(....) 方法方法
		if (m_hashSysPlugInsByLoc.containsKey(loc)) {

			//得到以loc为标识系统数据插件实例列表
			IProfilePlugIn[] ipluginary =
				(IProfilePlugIn[]) m_hashSysPlugInsByLoc.get(loc);

			//取出各插件实例,调用其onEraseSessionProfile(....) 方法
			for (int i = 0; i < ipluginary.length; i++) {

				try {
					ipluginary[i].onEraseSessionProfile(sessionid, loginName);
				
				} catch (Exception e) {
					LogMgr.getLogger(this.getClass().getName()).warn(
						"runSysPlugInErase(...): who == SYS_KEY m_hashSysPlugInNames Exception",
						e);
					continue;
				}

			}
		} else {

			if (!m_hashSysPlugInNames.containsKey(loc)) {
				return;
			}
			
			
			
			//如果为以loc为标识的系统数据插件名字，则取出插件名字列表，
			//进行实例化，调用插件实例的onEraseSessionProfile(....) 方法
			//把实例存入m_hashAppPlugInsByLoc

			//得到以loc为标识的系统数据插件名字列表
			String[] ipluginNameary = (String[]) m_hashSysPlugInNames.get(loc);
			Vector v = new Vector();
			IProfilePlugIn[] pluary;

			//取出各插件,实例化并调用插件实例的onEraseSessionProfile(....) 方法
			for (int j = 0; j < ipluginNameary.length; j++) {
				
				try {
					
					IProfilePlugIn iplugin = null;
					try {
	
						//实例化插件
						Class classFor = ClassLocator.loadClass(ipluginNameary[j]);
						iplugin = (IProfilePlugIn) classFor.newInstance();
					} catch (ClassNotFoundException cnfe) {
						LogMgr.getLogger(this.getClass().getName()).warn(
							"runSysPlugInErase(...): who == SYS_KEY m_hashSysPlugInNames  ClassNotFoundException",
							cnfe);
						continue;
					} catch (IllegalAccessException iae) {
						LogMgr.getLogger(this.getClass().getName()).warn(
							"runSysPlugInErase(...): who == SYS_KEY m_hashSysPlugInNames  IllegalAccessException",
							iae);
						continue;
					} catch (InstantiationException ie) {
						LogMgr.getLogger(this.getClass().getName()).warn(
							"runSysPlugInErase(...): who == SYS_KEY m_hashSysPlugInNames  InstantiationException",
							ie);
						continue;
					}
	
					//执行插件的onEraseSessionProfile(....) 方法
					if (iplugin != null) {
						v.addElement(iplugin);
						
						iplugin.onEraseSessionProfile(sessionid, loginName);
						
					}
				
				} catch (Exception e) {
					LogMgr.getLogger(this.getClass().getName()).warn(
						"runSysPlugInErase(...): who == SYS_KEY m_hashSysPlugInNames Exception",
						e);
					continue;
				}
				
			}

			//将实例化的plugin存入m_hashSysPlugInsByLoc
			
			if (v.size() > 0) {
				pluary =  copyVectorToIProfilePlugInArray(v);
				m_hashSysPlugInsByLoc.put(loc, pluary);
			}
		}
	}

	
	/**
	 * 返回Path操作事件开关:"0"或"1"
	 * @return int - Path操作事件开关
	 */
	public int isSetPath() {
		return this.setPath;
	}

	/**
	 * 返回Action操作事件开关:"0"或"1"
	 * @return int - Action操作事件开关
	 */
	public int isSetActionName() {
		return this.setAction;
	}
	
	/**
	 * 返回Action操作事件开关:"0"或"1"
	 * @return int - Action操作事件开关
	 */
	public static boolean isCreateProfilePerRequest() {
		return getSingleton().m_bCreateProfilePerRequest;
	}
	
	/**
	 * 返回Action操作事件开关:"0"或"1"
	 * @return int - Action操作事件开关
	 */
	public boolean isSessionListener() {
		return this.m_bIsSessionListener;
	}

	
}
