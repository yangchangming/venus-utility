package venus.frames.mainframe.log;

import venus.frames.mainframe.util.ClassLocator;
import venus.frames.mainframe.util.ConfMgr;
import venus.frames.mainframe.util.IConfReader;
import venus.frames.mainframe.util.PathMgr;

import java.util.Hashtable;

/**
 * 单实例模式的工厂类
 * 为需要使用log的用户提供log.
 * 作为工厂类将log的提供者同使用者分离
 *
 * 使用者面向接口使用，不关心log的提供者是谁
 *
 * log的提供者实现接口
 *
 * 由工厂类 中的配置数据决定 制造出的 log 是由哪个提供者提供的？
 *
 * 本项目中使用的LOG的提供者是LOG4J
 *
 * 现在实现输出日志到控制台和D：/venus.log
 *
 *
 * @author 岳国云
 */
public class LogMgr {

	/**
	 * 用于存储 log 提供（实现类）者的名字，
	 * 目的：工厂通过这个名字找到提供者，实例化并接受提供者的服务
	 */
	private static String m_strLoggerImplName;

	/**
	 * LogMgr类中用于存储logger驱动的表，目的是为了所有logger驱动的集中管理，可以通过此
	 * 手段优化每个类的LOG个数。
	 *
	 *
	 * 类似：工厂中用于存放成品的货架
	 */
	private static Hashtable m_hashLoggerTable = new Hashtable();

	/**
	 * singleton model中用到的，用于存储工厂类的单一实例
	 *
	 * used for singleton model
	 */
	private static LogMgr m_Singleton = null;

	public static String DEFAULT_LOG_IMPL = "venus.frames.mainframe.log.DefaultLogImpl";

	private static String LOG4J_IMPL = "venus.frames.mainframe.log.Log4jImpl";

	private static String LOG4J_CHK_CLASS = "org.apache.log4j.Logger";

	private static Class m_classImpl = venus.frames.mainframe.log.DefaultLogImpl.class.getClass();
	
	private static boolean m_bIsLoadConf = false;
	
	private static boolean m_bIsRunLoadConf = false;

	/**
	 * default Construction
	 *
	 * @roseuid 3F94DE8C0006
	 */
	public LogMgr() {

		//判断日志配置是否成功，如不成功返回信息
		if (!PathMgr.hasLog4jConf())
			PathMgr.configLog4j(null);
	}

	/**
	 * 得到 logger 驱动提供者的名字
	 *
	 * 该方法可以被重载，以修改提供者的名字
	 *
	 * @return String - 日志驱动提供者的名字
	 * @roseuid 3F72486D01D4
	 */
	protected String getLoggerImplName() {

		//如果Ilogger实现者名字为空，则从配置文件读取
		if (m_strLoggerImplName == null) {
			loadConf();

				
		}
		return m_strLoggerImplName;
	}

	/**
	 * 构建logger 驱动的真实的构造方法
	 *
	 * 传入参数：调用者的名字，日志驱动的名字
	 *
	 * @param callerName - 记录日志的调用者的名字
	 * @param loggerName - 日志驱动提供者的名字
	 * @return venus.frames.mainframe.log.Ilogger - LOG驱动实例
	 * @roseuid 3F724BB700EA
	 */
	private ILogger createLogger(String callerName, String loggerName) {
		Class classFor = null;
		//如果Ilogger驱动提供者名字为空，则由本类方法getLoggerImplName()来提供
		if ((loggerName == null) || loggerName.equals("")) {
			loggerName = getLoggerImplName();
			classFor = m_classImpl ;	
		}
		ILogger ilogger = null;
		//得到驱动者类对象并实例化
		try {
			if (classFor == null ) classFor = ClassLocator.loadClass(loggerName);
			ilogger = (ILogger) classFor.newInstance();
		} catch (ClassNotFoundException cnfe) {
			m_strLoggerImplName = DEFAULT_LOG_IMPL;
			cnfe.printStackTrace();

		} catch (IllegalAccessException iae) {
            m_strLoggerImplName = DEFAULT_LOG_IMPL;
            iae.printStackTrace();
            ilogger = createLogger(callerName, DEFAULT_LOG_IMPL);
			//LogMgr.getLogger(this.getClass().getName()).warn(
			//	"createLogger(...):   IllegalAccessException",
			//	iae);
		} catch (InstantiationException ie) {
            m_strLoggerImplName = DEFAULT_LOG_IMPL;
            ie.printStackTrace();
            ilogger = createLogger(callerName, DEFAULT_LOG_IMPL);
			//LogMgr.getLogger(this.getClass().getName()).warn(
			//	"createLogger(...):  InstantiationException",
			//	ie);
		}

		//设置记录日志的调用者的名字
		ilogger.setClassName(callerName);

		//返回logger 驱动
		return ilogger;
	}

	/**
	 * 静态方法，对应于静态方法 getLogger(String)的简单包装
	 *
	 * 传入参数：记录日志的调用者自身句柄，参数不可以为空，否则会抛出空指针异常
	 *
	 * 该方法通过调用者句柄自动获取调用者的类名
	 *
	 * @param caller - 记录日志的调用者,不可以为空
	 * @return venus.frames.mainframe.log.Ilogger - LOG驱动实例
	 */
	public static ILog getLogger(Object caller) {

		if (caller != null) {
			if  ( caller instanceof Class ){
				return LogMgr.getLogger(((Class)caller).getName());
			}
			return LogMgr.getLogger(caller.getClass().getName());
		}
		return null;
		}

		/**
		 * 静态方法对应于 getLogger(...) 的静态代理方法
		 *
		 * 传入参数：记录日志的调用者名字，参数不可以为空，否则会抛出空指针异常
		 *
		 * 得到LogMgr 的实例并根据得到的实现者名得到 LOG驱动实例
		 *
		 * @param caller  记录日志的调用者名字
		 * @return venus.frames.mainframe.log.Ilogger - LOG驱动实例
		 */
		public static ILog getLogger(String caller) {
			
			if ( m_bIsRunLoadConf ){

				if ( (ConfMgr.class.getName().equals(caller) || PathMgr.class.getName().equals(caller)) && m_strLoggerImplName==null ){
					
					m_strLoggerImplName = DEFAULT_LOG_IMPL;
				}
				
			}
			
			//得到LogMgr单实例
			LogMgr lm = getSingleton();

			//返回LOG驱动实例
			return lm.getLogger(caller, lm.getLoggerImplName());
		}

		/**
		 * 从XML文件中提取配置数据
		 *
		 * 配置项名“ImplName”
		 *
		 * 并使用缺省配置解析器帮助获取数据
		 *
		 * the xml conf is :
		 * <venus.frames.mainframe.log.LogMgr
		 * ImplName="venus.frames.mainframe.log.DefaultLogImpl" />
		 *
		 * @return void
		 */

		private void loadConf() {

			m_bIsRunLoadConf = true;
			
			//读配置文件提取配置数据
			try {
				IConfReader dcr =					
						ConfMgr.getConfReader(this.getClass().getName());
				m_strLoggerImplName = dcr.readStringAttribute("ImplName");

				//如果读取配置文件出现异常，则m_strLoggerImplName为空，
				//由getLoggerImplName（）设置固定名字，此异常不做其它处理
			} catch (NullPointerException e) {
				m_strLoggerImplName = DEFAULT_LOG_IMPL;
			}
			
			
			//如果读取配置文件为空，则使用默认驱动
			if (m_strLoggerImplName == null || m_strLoggerImplName.equals("") 
			|| m_strLoggerImplName.indexOf(46)<1 ){			
				m_strLoggerImplName = DEFAULT_LOG_IMPL;			
			}else if(m_strLoggerImplName.equals(LOG4J_IMPL) ){
						
				try {
					ClassLocator.loadClass(LOG4J_CHK_CLASS);
					Class tmpclassImpl = ClassLocator.loadClass(m_strLoggerImplName);
					m_classImpl = tmpclassImpl ;				
				}catch (ClassNotFoundException cnfe) {
					m_strLoggerImplName = DEFAULT_LOG_IMPL;
					cnfe.printStackTrace();
				}				
				
			}
			
			m_bIsLoadConf = true;
			
		}

		/**
		 * 工厂实例中真正获取 log驱动 的方法
		 *
		 * 先查找列表中是否存在log驱动，然后再调用createLogger(caller,loggerName) 构建
		 *
		 * 本方法中参数caller不可以为空，否则会抛出空指针异常
		 * @param caller - 记录日志的调用者名字
		 * @param loggerName - 日志实现类的名字
		 * @return venus.frames.mainframe.log.Ilogger - LOG驱动实例
		 */
		public ILogger getLogger(String caller, String loggerName) {

			if (caller == null) {
				LogMgr.getLogger(this.getClass().getName()).warn("getLogger(): 调用者名字不可以为空");
				return null;
			}
			ILogger ilogger = null;
			//如果记录日志调用者存在，则从Hashtable中提取并返回实现接口
			if (m_hashLoggerTable.containsKey(caller))
				ilogger = (ILogger) m_hashLoggerTable.get(caller);

			//如果没有调用者名字，则新建一个驱动实例，保存调用者名字
			else {
				ilogger = createLogger(caller, loggerName);
				m_hashLoggerTable.put(caller, ilogger);
			}
			return ilogger;
		}

		/**
		 * 通过此静态方法获得该工厂类的全局单一实例
		 *
		 * 如果该实例未构建则构建初始化并加载配置数据
		 *
		 * @return venus.frames.mainframe.log.LogMgr - 该工厂类的全局单一实例
		 */
		public static LogMgr getSingleton() {

			//如果实例不存在,则新创建一个实例
			if (m_Singleton == null) {
				m_Singleton = new LogMgr();
				
				if ( m_bIsRunLoadConf ){
					LogMgr.m_strLoggerImplName = DEFAULT_LOG_IMPL;
					return m_Singleton;
				
				}
				
				m_Singleton.loadConf(); 
				
				
			}
			//返回实例
			return m_Singleton;
		}
	}
