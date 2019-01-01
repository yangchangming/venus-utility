package venus.frames.mainframe.context;

import venus.frames.base.IGlobalsKeys;
import venus.frames.mainframe.cache.CacheFactory;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.ClassLocator;
import venus.frames.mainframe.util.ConfMgr;
import venus.frames.mainframe.util.IConfReader;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 单实例模式的工厂类，
 * 为需要使用 IContext 的用户提供Context <br>
 * 作为工厂类将 Context的提供者同使用者分离，
 * 使用者面向接口使用，不关心 Context的提供者是谁<br>
 * Context的提供者实现接口<br>
 * 
 * 由工厂类中的配置数据决定制造出的Context是由哪个提供者提供的<br>
 * 
 * 本项目中缺省使用的提供者是使用的Hashtable完成Context，并加以包装<br>
 * 
 * 该类目前简单处理，不设置配置数据加载，将来根据系统重构情况考虑加入
 * 
 * @author 张文韬
 */
public class ContextMgr implements IGlobalsKeys {

	/**
	 * ContextMgr中用于存储Context实例的哈希表，目的是为了所有Context的集中管理。<br>
	 * 
	 * 
	 * 类似：工厂中用于存放成品的货架
	 */
	private Hashtable m_hashContextTable = null;

	/**
	 * singleton model中用到的，用于存储该类的单一实例
	 */
	private static ContextMgr m_Singleton = null;

	/**
	 * 用于存储 IContext  提供（实现类）者的名字，<br>
	 * 目的：工厂通过这个名字找到提供者，实例化并接受提供者的服务<br>
	 * 
	 * 目前简单处理，不设置配置数据加载，将来根据系统重构情况考虑加入，所以目前写死
	 */
	private String m_strContextImplName =
		"venus.frames.mainframe.context.DefaultConextImpl";
		
	final public static String CONTEXT_NODE_KEY =  CacheFactory.CACHE_NAME_ROOT+"Venus"+ CacheFactory.CACHE_NAME_SEPARATOR+"Context"+ CacheFactory.CACHE_NAME_SEPARATOR;


	/**
	 * 构造函数
	 * @roseuid 3FA0D9350108
	 */
	public ContextMgr() {
		super();
		loadConf();
		//初始化m_hashContextTable
		m_hashContextTable = new Hashtable();
	}

	/**
	 * 根据传入标识获取上下文对象<br>
	 * 
	 * 此方法为静态方法为方法 requestContext(...) 的静态代理
	 * 
	 * 
	 * @param key 上下文对象在系统中的标识
	 * @return venus.frames.mainframe.context.IContext
	 * @roseuid 3F8A1109036A
	 */
	public static IContext getContext(String key) {
		//调用静态方法进行处理
		
		//初始工厂类并加载配置数据
		ContextMgr cf = getSingleton();

		//如果列表中保存有cacheName的Cacher实例，则返回此实例
		IContext ic = cf.findContext(key);
		
		if ( ic != null) {
			return ic;
		} else {

			//新构建成一个IContext实例
			return cf.requestConext(
			key,
			cf.getContextImplName());
		}
		
	}
	
	/**
	 * 在Context 列表中获取Context 实例
	 * 
	 * @param cacheName - Context的名字
	 * @return venus.frames.mainframe.cache.Cacher - Cacher驱动实例
	 * @roseuid 3F948CD2029D
	 */
	public IContext findContext(String key) {

		if (key == null) {
			LogMgr.getLogger(this).warn("findContext(...):参数不可以为空");
			return null;
		}

		if (m_hashContextTable.containsKey(key)) {
			return (IContext) m_hashContextTable.get(key);
		}
		return null;
	}
	
	
	/**
	 * 根据传入标识获取上下文对象,标识为：WEB_CONTEXT_KEY<br>
	 * 
	 * 此方法为静态方法为方法 requestContext(...) 的静态代理
	 * 
	 * 
	 * @param key 上下文对象在系统中的标识
	 * @return venus.frames.mainframe.context.IContext
	 * @roseuid 3F8A1109036A
	 */
	public static IContext getContext() {
		//调用静态方法进行处理
		
		return getContext(WEB_CONTEXT_KEY);
	}

	/**
	 * 根据传入标识清除上下文对象<br>
	 * 
	 * 此方法为静态方法为方法clearContext(...) 的静态代理,
	 * 使得用户在销毁Context实例时不需要重新创建一个新的对象，
	 * 直接调用此静态方法即可；
	 * 
	 * @param key 上下文对象在系统中的标识
	 * @roseuid 3F94A7A601FE
	 */
	public static void removeContext(String key) {
		//通过静态方法进行调用
		getSingleton().clearContext(key);
	}

	/**
	 * 工厂实例中真正制造 IContext 的方法<br>
	 * 
	 * 根据标识先查找列表中是否存在Context，然后再构建
	 * 
	 * @param key 上下文对象在系统中的标识
	 * @param strImplClassName Context实现类的名字
	 * @return venus.frames.mainframe.context.IContext
	 * @roseuid 3FA0C1E30389
	 */
	public IContext requestConext(String key, String strImplClassName) {
		//如果m_hashContextTable为空，则初始化该属性
		if (m_hashContextTable == null) {
			m_hashContextTable = new Hashtable();
		}
		//如果key为空，记录错误，返回空
		if (key == null) {
			LogMgr.getLogger(this).error("ContextMgr的requestContext方法中，参数空指针错误!");
			return null;
		}
		//如果m_hashContextTable中存在所要的实例，直接返回该实例;
		//否则创建一个实例返回，并将该实例存到哈希表中
		Object o = m_hashContextTable.get(key);
		if ( o!= null) {
			return (IContext) o;
		} else {
			IContext cont = null;
			try {
				cont =
					(IContext) ClassLocator
						.loadClass(strImplClassName)
						.newInstance();
				cont.setContextName(CONTEXT_NODE_KEY+key);
			} catch (Exception e) {
				LogMgr.getLogger(this).error(e.getMessage(),e);
				return null;
			}
			try {
				m_hashContextTable.put(key, cont);
			} catch (NullPointerException ee) {
				LogMgr.getLogger(this).error(ee.getMessage(),ee);
				ee.printStackTrace();
			}
			return cont;
		}
	}

	/**
	 * 在Context列表中销毁Context实例<br>
	 * 
	 * @param key 上下文对象在系统中的标识
	 * @roseuid 3FA0C7D30202
	 */
	public void clearContext(String key) {
		//如果字符串为空抛出异常
		if (key == null) {
			LogMgr.getLogger(this).error("ContextMgr的clearContext方法中，输入的键值为空!");
			return;
		}
		//根据键值查找对象并删除
		Object o = m_hashContextTable.get(key);
		if ( o == null) {
			return;
		} else {
			//先将实例本身销毁，然后再清除哈希表中的内容
			 ((IContext) (o)).clear();
			m_hashContextTable.remove(key);
		}
	}
	
	/**
	 * 销毁所有Context实例<br>
	 * 
	 */
	public void clearAllContext() {
		
		Enumeration enu = m_hashContextTable.keys();
		
		while( enu.hasMoreElements() ){
			
			Object key = enu.nextElement();
			
			//根据键值查找对象并删除
			Object o = m_hashContextTable.get(key);
			if (o == null) {
				
				m_hashContextTable.remove(key);
				
			} else {
				
				//先将实例本身销毁，然后再清除哈希表中的内容
				 ((IContext) (o)).clear();
				m_hashContextTable.remove(key);
				
			}
		}
	}

	/**
	 * 得到Context提供者的名字<br>
	 * 
	 * 该方法可以被重载，以修改提供者的名字<br> 
	 * 
	 * 目前简单处理，不设置配置数据加载，将来根据系统重构情况考虑加入<br>
	 * 
	 * 
	 * @return String Context提供者的名字
	 * @roseuid 3FA0C7F20176
	 */
	protected String getContextImplName() {
		return this.m_strContextImplName;
	}

	/**
	 * 通过此静态方法获得该类的全局单一实例<br>
	 * 强烈建议用户使用该方法取得该类的实例，而不要使用new来创建实例
	 * @return venus.frames.mainframe.context.ContextMgr
	 * @roseuid 3FA0CB0F00BA
	 */
	public static ContextMgr getSingleton() {
		//如果该实例未被创建则先创建再返回实例
		if (m_Singleton == null) {
			m_Singleton = new ContextMgr();
		}
		return m_Singleton;
	}
	
	private void loadConf() {

		//以本类名为标识从XML文件中提取cache实现类的名字，
		//配置项名“ImplName”.
		try {
			IConfReader dcr = ConfMgr.getConfReader(this.getClass().getName());
			this.m_strContextImplName = dcr.readStringAttribute("ImplName");
		} catch (NullPointerException be) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"loadConf(...) : NullPointerException");
			this.m_strContextImplName = "venus.frames.mainframe.context.DefaultConextImpl";;
		}
	}
	
	
}
