package venus.frames.mainframe.cache;

import venus.frames.base.IGlobalsKeys;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.ClassLocator;
import venus.frames.mainframe.util.ConfMgr;
import venus.frames.mainframe.util.DefaultConfReader;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 单实例模式的工厂类,为需要使用CACHE的用户提供cache.
 * 作为工厂类将Cache的提供者同使用者分离,使用者面向接口使用，
 * 不关心Cache的提供者是谁,Cache的提供者实现接口
 * 由工厂类中的配置数据决定制造出的 cache 是由哪个提供者提供的
 * 
 * 本项目中缺省使用的提供者是JIVE1.1.3中的cache，并加以包装
 * 
 *  @author 岳国云
 * 
 */
public class CacheFactory implements IGlobalsKeys {
	
	public static String CACHE_NAME_ROOT = "/" ;
	public static String CACHE_NAME_SEPARATOR = "-" ;
	
	/**
	 * CACHE工厂类中用于存储cache的表，目的是为了所有CACHE的集中管理和监控状态，
	 * 可以通过此手段控制内存大小等。
	 * 类似：工厂中用于存放成品的货架
	 */
	private Hashtable m_hashCacheTable = new Hashtable();

	/**
	 * 用于存储 CACHE提供（实现类）者的名字，
	 * 目的：工厂通过这个名字找到提供者，实例化并接受提供者的服务.
	 */
	private String m_strCacherImplName = null;

	/**
	 * singleton model中用到的，用于存储工厂类的单一实例.
	 */
	private static CacheFactory m_Singleton = null;
	
	private ICacher m_objLocalCache = null;
	
	public static String CACHE_NODE_KEY = CACHE_NAME_ROOT+"Venus"+CACHE_NAME_SEPARATOR+"Cache"+CACHE_NAME_SEPARATOR;
		
	public final static String DEFAULT_LOCAL_CACHE_IMPL_NAME = "venus.frames.mainframe.cache.impl.CacheSimpleImpl";	

	/**
	 * 缺省构造器
	 * @roseuid 3F94DEF7018D
	 */
	public CacheFactory() {
	}

	/**
	 * 工厂实例中真正制造 CACHE的方法
	 * 
	 * 由于该方法的起调者是getCache，由于在那个方法中已经查找的Cache是否在列表中 
	 * 所以此处不用查找，而其他构建方法中需要先查找列表中是否存在cache，然后再构建
	 *
	 * @param strImplClassName - cache 提供者的类名，实例化用
	 * @param cacheName - cache的名字
	 * @return venus.frames.mainframe.cache.Cacher - Cacher驱动实例
	 * @roseuid 3F8A4CB100F9
	 */
	public ICacher buildCache(String strImplClassName, String cacheName) {
		ICacher cacher = null;

		if (strImplClassName == null || cacheName == null) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"buildCache(...)：参数为空");
			return null;
		}

		//得到CacheImpl的CLASS对象并初始化
		try {
			Class classFor = ClassLocator.loadClass(strImplClassName);
			cacher = (ICacher) classFor.newInstance();
		} catch (ClassNotFoundException cnfe) {
			LogMgr.getLogger(this.getClass().getName()).warn(
				"buildCache(...):  ClassNotFoundException",
				cnfe);
			return null;
		} catch (IllegalAccessException iae) {
			LogMgr.getLogger(this.getClass().getName()).warn(
				"buildCache(...):   IllegalAccessException",
				iae);
			return null;
		} catch (InstantiationException ie) {
			LogMgr.getLogger(this.getClass().getName()).warn(
				"buildCache(...):  InstantiationException",
				ie);
			return null;
		}

		//设置CACHE的名字
		cacher.setCacheName(CACHE_NODE_KEY+cacheName);

		//将cache放入列表用于将来管理
		m_hashCacheTable.put(cacheName, cacher);
		return cacher;
	}

	/**
	 * 工厂实例中真正制造 CACHE的方法
	 * 
	 * 先查找列表中是否存在cache，然后再构建。
	 * 
	 * @param strImpiClassName - cache 提供者的类名，实例化用
	 * @param maxSize - cache的最大容量
	 * 
	 * 例如: 128 K：128*1024
	 * @param cacheName - cache的名字
	 * @return venus.frames.mainframe.cache.Cacher - Cacher驱动实例
	 * @roseuid 3F8E0CB501A2
	 */
	public ICacher buildCache(
		String strImpiClassName,
		int maxSize,
		String cacheName) {

		if (strImpiClassName == null || maxSize < 0 || cacheName == null) {
			LogMgr.getLogger(this).warn("buildCache():参数错误");
			return null;
		}

		if (findCache(cacheName) != null) {
			return (ICacher) findCache(cacheName);
		} else {
			ICacher cacher = null;
			try {
				Class classFor = ClassLocator.loadClass(strImpiClassName);
				cacher = (ICacher) classFor.newInstance();
			} catch (ClassNotFoundException cnfe) {
				LogMgr.getLogger(this.getClass().getName()).warn(
					"buildCache(...):  ClassNotFoundException",
					cnfe);
				return null;
			} catch (IllegalAccessException iae) {
				LogMgr.getLogger(this.getClass().getName()).warn(
					"buildCache(...):   IllegalAccessException",
					iae);
				return null;
			} catch (InstantiationException ie) {
				LogMgr.getLogger(this.getClass().getName()).warn(
					"buildCache(...):  InstantiationException",
					ie);
				return null;
			}
			cacher.setMaxSize(maxSize);
			cacher.setCacheName(CACHE_NODE_KEY+cacheName);
			m_hashCacheTable.put(cacheName, cacher);
			return cacher;
		}
	}

	/**
	 * 在CACHE 列表中获取CACHE 实例
	 * 
	 * @param cacheName - cache的名字
	 * @return venus.frames.mainframe.cache.Cacher - Cacher驱动实例
	 * @roseuid 3F948CD2029D
	 */
	public ICacher findCache(String cacheName) {

		if (cacheName == null) {
			LogMgr.getLogger(this).warn("findCache(...):参数不可以为空");
			return null;
		}

		if (m_hashCacheTable.containsKey(cacheName)) {
			return (ICacher) m_hashCacheTable.get(cacheName);
		}
		return null;
	}

	/**
	 * 静态方法对应于 clearCache(...) 的静态代理方法
	 * 
	 * 在CACHE 列表中销毁 CACHE 实例
	 * 
	 * @param cacheName - cache的名字
	 * @return void
	 * @roseuid 3F948D0C03D6
	 */
	public static void removeCache(String cacheName) {

		if (cacheName == null) {
			LogMgr.getLogger("venus.frames.mainframe.cache.CacheFactory").warn(
				"removeCache(...):参数不可以为空");
			return;
		}
		//得到本工厂单实例
		CacheFactory cf = getSingleton();

		//调用清除方法
		cf.clearCache(cacheName);
	}

	/**
	 * 从XML文件中提取配置数据
	 * 
	 * 配置项名“ImplName”,并使用缺省配置解析器帮助获取数据
	 * 
	 * the xml conf is :
	 * <venus.frames.mainframe.cache.CacheFactory 
	 * ImplName="venus.frames.mainframe.cache.impl.CacheImpl" />
	 * 
	 * @return void
	 * @roseuid 3F94956F000B
	 */
	private void loadConf() {

		//以本类名为标识从XML文件中提取cache实现类的名字，
		//配置项名“ImplName”.
		try {
			DefaultConfReader dcr =
				new DefaultConfReader(
					ConfMgr.getNode(this.getClass().getName()));
			this.m_strCacherImplName = dcr.readStringAttribute("ImplName");
		} catch (NullPointerException be) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"loadConf(...) : NullPointerException");
			this.m_strCacherImplName = null;
		}
	}

	/**
	 * 静态方法对应于 buildCache(...) 的静态代理方法
	 * 
	 * 传入参数：cache的最大容量，cache的名字
	 * ;
	 * @param maxSize - cache的最大容量
	 * 
	 * 例如: 128 K：128*1024
	 * @param cacheName - cache的名字
	 * @return venus.frames.mainframe.cache.Cacher - Cacher驱动实例
	 * @roseuid 3F9495880192
	 */
	public static ICacher createCache(int maxSize, String cacheName) {

		if (maxSize < 0 || cacheName == null) {
			return null;
		}
		//初始工厂类并加载配置数据
		CacheFactory cf = getSingleton();

		//如果列表中保存有cacheName的Cacher实例，则返回此实例
		ICacher ic = cf.findCache(cacheName);
		if (ic != null) {
			return ic;
		}

		//如果列表中没有cacheName的Cacher实例，则构建一个实例
		return cf.buildCache(cf.getCacherImplName(), maxSize, cacheName);
	}

	/**
	 * 得到 cache 提供者的名字
	 * 
	 * 该方法可以被重载，以修改提供者的名字
	 * 
	 * 该方法依赖于 loadConf(); 的执行，从XML文件中提取配置数据
	 * 
	 * 但是如果未提取到数据 则使用缺省数据"venus.frames.mainframe.cache.impl.CacheImpl"
	 * 
	 * @return String  cache - 提供者的名字
	 * @roseuid 3F9497FC039A
	 */
	protected String getCacherImplName() {

		if (this.m_strCacherImplName == null) {

			//从配置文件提取数据
			loadConf();

			//如果未提取到数据 则使用缺省数据
			if (this.m_strCacherImplName == null
				|| this.m_strCacherImplName.equals("")){
				
				this.m_strCacherImplName = DEFAULT_LOCAL_CACHE_IMPL_NAME;
			
			}
				
		}
		return this.m_strCacherImplName;
	}

	/**
	 * 静态方法对应于 buildCache(...) 的静态代理方法
	 * 
	 * 传入参数：cache的名字
	 * 
	 * 该方法同 createCache 所不同的是先先查找列表中是否存在cache，然后再构建
	 * 
	 * @param cacheName - cache的名字
	 * @return venus.frames.mainframe.cache.Cacher - Cacher驱动实例
	 * @roseuid 3F94999E00FC
	 */
	public static ICacher getCache(String cacheName) {

		//初始工厂类并加载配置数据
		CacheFactory cf = getSingleton();

		//如果列表中保存有cacheName的Cacher实例，则返回此实例
		ICacher re = cf.findCache(cacheName);
		
		if (re != null) {
			return re;
		} else {
			
			//新构建成一个Cacher实例
			return cf.buildCache(cf.getCacherImplName(), cacheName);
			
		}
	}

	/**
	 * 通过此静态方法获得该工厂类的全局单一实例
	 * 
	 * 如果该实例未构建则构建初始化并加载配置数据
	 * 
	 * @return venus.frames.mainframe.cache.CacheFactory - 该工厂类的全局单一实例
	 * @roseuid 3F94DD03033F
	 */
	public static CacheFactory getSingleton() {

		if (m_Singleton == null) {
			m_Singleton = new CacheFactory();

//			加载配置数据
			try {
				m_Singleton.loadConf();
			} catch (Exception e) {
				LogMgr
						.getLogger("venus.frames.mainframe.cache.CacheFactory")
						.error(
								"Can't get the implement class name of cacher, so you have to provide the impl class when cache data");
			}
		}
		return m_Singleton;
	}

	/**
	 * 在CACHE 列表中销毁 CACHE 实例
	 * 
	 * 先查找列表中是否存在cache，然后再销毁
	 * 
	 * @param cacheName - cache的名字
	 * @return void
	 * @roseuid 3F9F267A039A
	 */
	public void clearCache(String cacheName) {

		if (findCache(cacheName) == null) {
			return;
		}
		
		if ( IGlobalsKeys.DEFAULT_LOCAL_CACHE_NAME.equals(cacheName) ){
			m_objLocalCache.clear();
			m_objLocalCache = null;		
		}

		//从实例中清除CACHE。
		findCache(cacheName).clear();

		//从列表中销毁CACHE实例
		m_hashCacheTable.remove(cacheName);
	}
	
	/**
	 * 销毁所有Cache实例<br>
	 * 
	 */
	public void clearAllCache() {
		
		Enumeration enu = m_hashCacheTable.keys();
		
		while( enu.hasMoreElements() ){
			
			Object key = enu.nextElement();
			Object o = m_hashCacheTable.get(key);
			//根据键值查找对象并删除
			if ( o == null) {
				
				m_hashCacheTable.remove(key);
				
			} else {
				
				//先将实例本身销毁，然后再清除哈希表中的内容
				 ((ICacher) (o)).clear();
				 m_hashCacheTable.remove(key);
				
			}
		} 

	}
		
	/**
	 * 销毁所有ImplName实现的Cache实例<br>
	 * 
	 */
	public void clearAllCacheByImplName(String ImplName) {
		
		Enumeration enu = m_hashCacheTable.keys();
		
		while( enu.hasMoreElements() ){
			
			Object key = enu.nextElement();
			Object o = m_hashCacheTable.get(key);
			//根据键值查找对象并删除
			if ( o == null ) {
				
				m_hashCacheTable.remove(key);
				
			} else {
				
				//先将实例本身销毁，然后再清除哈希表中的内容
				ICacher ic = (ICacher) (o);
				 
				if( ImplName == null || ImplName.equalsIgnoreCase( ic.getImplName() ) ){
				 	
					ic.clear();
					m_hashCacheTable.remove(key);
				 
				}
				
			}
		} 

	}
	
	
	
	/**
	 * 得到缺省local cache
	 * 
	 * 该方法同 createCache 所不同的是先先查找列表中是否存在cache，然后再构建
	 * 
	 * @return venus.frames.mainframe.cache.Cacher - Cacher驱动实例
	 */
	public static ICacher getLocalCache() {
		
		CacheFactory cf = CacheFactory.getSingleton();
		
		if ( cf.m_objLocalCache == null ) cf.m_objLocalCache = cf.buildCache(IGlobalsKeys.DEFAULT_LOCAL_CACHE_NAME,DEFAULT_LOCAL_CACHE_IMPL_NAME);
		
		return cf.m_objLocalCache;

		
	}

}
