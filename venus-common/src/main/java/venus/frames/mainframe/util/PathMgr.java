package venus.frames.mainframe.util;

import venus.frames.mainframe.log.LogMgr;

import javax.servlet.ServletContext;
import java.io.*;

/**
 * 系统路径资源管理器.<br>
 *
 * 注：该类中所用到的路径只有getRealPath()方法中得到的是真实路径，<br>
 * 其他方法涉及的均为虚拟路径(相对路径)；
 *
 * @author 张文韬
 *
 */
public class PathMgr {


	/**
	 * 该系统的存放路径,为缺省绝对路径
	 */
	private static String DEFAULT_REAL_PATH = "/";

	public static String WEB_CONTEXT_PATH;

	/**
	 * 均为虚路径,非真实的文件系统路径
	 * 对应于 System.getProperty(USE_VENUS_ROOT)
	 */
	private String m_strRootPath = "/";

	/**
	 * 均为虚路径,非真实的文件系统路径
	 * 对应于 System.getProperty(USE_VENUS_TMP)
	 */
	private String m_strTmpPath = "/tmp/";

	/**
	 * 为了支持多Module同时在一个jvm中运行，
	 * 
	 * 不同的PathMgr初始化获取根真实路径时不至于征用同一个"USE_HOME"
	 * 
	 * 故PathMgr初始化获取Module的根时先看m_strModuleName是否为null，
	 * 
	 * 若为null：直接 System.getProperty(USE_HOME)获取根真实路径
	 * 
	 * 若不为空：先查询System.getProperty(USE_HOME+"."+m_strModuleName)
	 * 
	 * 若未查询到结果即使用System.getProperty(USE_HOME)
	 * 
	 * 对应于 System.getProperty(USE_VENUS_TMP)
	 */
	private String m_strModuleName = null;

	/**
	 * 真实的文件系统起始路径 --->"/"， 对应系统中该路径在系统中的真实路径
	 * 对应于 System.getProperty(USE_HOME)
	 */
	private String m_strRealPath = DEFAULT_REAL_PATH;


	/**
	 * 如果是以WAR方式部署，该变量寄存 Servlet的上下文环境ServletContext，
	 * Servlet初始化时传入PathMgr
	 */
	private ServletContext m_ServletContext = null;

	/**
	 * 配置文件所在目录的路径
	 */
	private static String CONF_PATH = "conf";

	/**
	 * 安全路径<br>
	 * 保证放在此路径中的文件不可被公开浏览，此主要针对WEB项目中使用
	 */
	private static String SAFE_PATH = "WEB-INF";

	/**
	 * 配置文件所在的路径
	 */
	private static String CONF_FILE = "conf.xml";
	
	/**
	 * 表示系统部署时的状态，是以WAR方式部署还是以其他方式部署
	 */
	private boolean m_bIsInWar = false;

	/**
	 * 记录是否配置了log4j
	 */
	private static boolean m_bHasLog4jConf = false;

	/**
	 * 记录 整个路径信息是否 init 了
	 */
	private static boolean m_bHasInit = false;

	/**
	 * 记录 整个路径信息是否正在执行 init 方法，
	 * 若正在执行初始化，则构造器构造不执行缺省初始化，否则构造器构造时执行初始化
	 */
	private static boolean m_bIsInit = false;
	
	/**
	 * log4j的配置文件名
	 */
	private static String LOG_CONF_FILE = "log4j.properties";

	/**
	 * singleton model中用到的，用于存储该类的单一实例
	 */
	private static PathMgr m_Singleton = null;

	/**
	 * 在系统中的 “USE_HOME”的键名
	 */
	public static String USE_HOME = "USE_HOME";


	/**
	 * 在系统中的 “WAR_NAME”的键名
	 */
	public static String WAR_NAME = "WAR_NAME";

	/**
	 * 文件系统中的路径分割符
	 */
	public static char SEPARATOR = File.separatorChar;

	/**
	 * web系统中的路径分割符："/"
	 */
	public static char WEB_SEPARATOR = 47;

	/**
	 * 在系统中的 “USE_VENUS_ROOT”的键名
	 */
	public static String USE_VENUS_ROOT = "USE_VENUS_ROOT";

	/**
	 * 在系统中的 “USE_VENUS_TMP”的键名
	 */
	public static String USE_VENUS_TMP = "USE_VENUS_TMP";

	/**
	 * 空构造函数，继承父类
	 */
	public PathMgr() {
		super();
		
		/* 若正在执行初始化，则构造器构造不执行缺省初始化，否则构造器构造时执行初始化*/
		if (!m_bIsInit){
			//设置路径
			String realpath = (System.getProperty(USE_HOME) == null ? DEFAULT_REAL_PATH : System.getProperty(USE_HOME));
			try {
				setRealPath(realpath);
				inWar(false);
		
				if ( System.getProperty(USE_HOME) == null ) {
					System.setProperty(USE_HOME,realpath);
					configLog4j(null);
				}		
			} catch (PathException e) {
					e.printStackTrace();
			}
		}
		
	}

	/**
	 * 获取指定的虚拟路径（相对路径）“virtualPath”对应于文件系统的真实路径
	 * 虚拟路径的分隔符为右向分隔符；
	 * @param virtualPath 相对路径
	 * @return 真实路径
	 * @throws venus.frames.mainframe.util.PathException
	 * @roseuid 3F6821BB00CA
	 */
	public String getRealPath(String virtualPath) throws PathException {
		//如果不是以WAR方式部署，则需要对文件分隔符进行判断
		//考虑如果IN WAR 真实路径也是要转PATH的分隔符号
		if (SEPARATOR != 47) {
			char c = 47;
			String newPath = virtualPath.replace(c, SEPARATOR);
			return m_strRealPath + newPath;
		}
		//其他情况直接返回结果
		return m_strRealPath + virtualPath;
	}

    public ServletContext getServletContext(){
		return this.m_ServletContext;

    }

	public void setServletContext(ServletContext sc){
		this.m_ServletContext = sc;

	}

	/**
	 * 设置虚拟根路径
	 * @param rootpath 虚拟根路径
	 * @roseuid 3F68232002FD
	 */
	public void setRootPath(String rootpath) {
		this.m_strRootPath = rootpath;
	}

	/**
	 * 获得系统根路径，均为虚拟路径
	 * @return 系统的虚拟根路径（相对路径）
	 * @roseuid 3F68246B01D4
	 */
	public String getRootPath() {
		return m_strRootPath;
	}

	/**
	 * 设置虚拟系统临时文件区根路径
	 * @roseuid 3F6824860212
	 */
	public void setTmpPath(String tmppath) {
		this.m_strTmpPath = tmppath;
	}

	/**
	 * 获得系统临时文件区根路径，均为虚拟路径
	 * @return 系统临时文件区根路径（虚拟路径）
	 * @roseuid 3F68249603E7
	 */
	public String getTmpPath() {
		return m_strTmpPath;
	}

	/**
	 * 暂时不实现
	 * @roseuid 3F6825B7002E
	 */
	public void mkDir() {

	}

	/**
	 * 暂时不实现
	 * @roseuid 3F6825C7029F
	 */
	public void delDir() {

	}

	/**
	 * 暂时不实现
	 * @roseuid 3F6825E2035A
	 */
	public void createFile() {

	}

	/**
	 * 暂时不实现
	 * @roseuid 3F6825F103C8
	 */
	public void delFile() {
	}

	/**
	 * 获取给定路径文件的IO流，<br>
	 * 当以非WAR方式部署时，取得的路径为真实路径。
	 * @param path 文件的虚拟路径（相对路径）
	 * @return 文件IO流：InputStream
	 * @roseuid 3F9E230C033C
	 */
	public static InputStream getResourceAsStream(String path) {
		//如果参数为空
		if (path == null) {
			LogMgr.getLogger("venus.frames.mainframe.util.PathMgr").error("PathMgr类中的getResourceAsStream方法参数空指针异常!");
			return null;
		}
		//当以war方式部署时
		if (getSingleton().m_bIsInWar) {
			try{
				return getSingleton().getServletContext().getResourceAsStream(path);
			}catch (NullPointerException e){
			    LogMgr.getLogger("venus.frames.mainframe.util.PathMgr").error("读取文件出错:PathMgr.getResourceAsStream(" + path + ")!",e);
			    return null;
			}

		}
		//非WAR方式部署
		else {
			try {
				return new FileInputStream(getSingleton().getRealPath(path));
			} catch (PathException e) {
				LogMgr.getLogger("venus.frames.mainframe.util.PathMgr").error("读取文件出错:PathMgr.getResourceAsStream(" + path + ")!",e);
				return null;
			} catch(FileNotFoundException e) {

                // get conf path for maven env
                try {
                    return new FileInputStream(getMavenAbsoluteConfPath(path));
                } catch (FileNotFoundException e1) {
                    LogMgr.getLogger("venus.frames.mainframe.util.PathMgr").error("读取文件出错:PathMgr.getResourceAsStream(" + path + ")!", e);
                    return null;
                }
            }
        }
	}

	/**
	 * 获取集中配置文件的 IO 流
	 * @return 配置文件的IO流（InputStream）
	 * @roseuid 3F9E23540196
	 */
	public static InputStream getConfAsStream() {
		return getResourceAsStream(getConfPath() + CONF_FILE);
	}

	/**
	 * 获取集中配置文件路径
	 * @return 配置文件的虚拟路径（相对路径）
	 * @roseuid 3F9E26FE00DA
	 */
	public static String getConfPath() {
		//全部取虚拟路径，分隔符为右向分隔符"/"
		return getSingleton().getSafePath() +  CONF_PATH + WEB_SEPARATOR;
	}

	/**
	 * 设置LOG4J 的配置文件的路径。<br>
	 * 如果指定了路径则系统按照指定路径进行设置；如果没有指定则按照我们的
	 * 默认路径进行配置。
	 * @param path Log4j配置文件的真实路径
	 * @roseuid 3F9E35AD0232
	 */
	public static void configLog4j(String path) {
		//表明Log4j已经被初始化
		m_bHasLog4jConf = true;

		try {
				ClassLocator.loadClass("org.apache.log4j.Logger");
		}catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
                return;
        }
		//如果指定路径则按照指定路径设置
		if (path != null) {
			File f = new File(path);
			if (f.isFile() && f.canRead()){

				try {
					org.apache.log4j.PropertyConfigurator.configure(path);

				}catch (Exception ex) {
					org.apache.log4j.BasicConfigurator.configure();
	                ex.printStackTrace();
	            }
			}else{
				org.apache.log4j.BasicConfigurator.configure();

			}
		}
		//若不指定则按照默认路径进行设置
		else {
			try {
				if ( LOG_CONF_FILE.endsWith(".properties") && getSingleton().isInWar() ) {
                      java.util.Properties p = new java.util.Properties();
                      try {
                        p.load(getResourceAsStream(getConfPath() +
                            LOG_CONF_FILE));
                      } catch (IOException ex) {
                      	org.apache.log4j.BasicConfigurator.configure();
                        ex.printStackTrace();
                        return;
                      }

                      org.apache.log4j.PropertyConfigurator.configure(p);

                    }else if( !getSingleton().isInWar() ){

                    		String fname = getSingleton().getRealPath(getConfPath()) + LOG_CONF_FILE;
                          	File f = new File(fname);
							if (f.isFile() && f.canRead()){

								try {
									org.apache.log4j.PropertyConfigurator.configure(fname);
								}catch (Exception ex) {
									org.apache.log4j.BasicConfigurator.configure();
					                ex.printStackTrace();
					            }
							}else{
								org.apache.log4j.BasicConfigurator.configure();
							}
                    }

			} catch (PathException e) {
	          	org.apache.log4j.BasicConfigurator.configure();
	            e.printStackTrace();
            } catch (Exception ex) {
				org.apache.log4j.BasicConfigurator.configure();
				ex.printStackTrace();
			}
		}
	}

	public static void init(String realRootPath,String ModuleName) throws PathException {
		//设置状态标志说明正在初始化系统路径数据
		m_bIsInit = true;
		//取得单一实例
		PathMgr pm = getSingleton();
		//表明整个路径信息已经被初始化完毕
		m_bHasInit = true;

		pm.setModuleName(ModuleName);

		if ( realRootPath!=null ){
			if(ModuleName!=null ){
				System.setProperty(USE_HOME+"."+ModuleName,realRootPath);
			}else{
				System.setProperty(USE_HOME,realRootPath);
			}
		}
		initRealPathNotInWar(pm);
	}

	public void setModuleName(String ModuleName){
		this.m_strModuleName = ModuleName;
	}

	public static String getModuleName(){
		PathMgr pm = getSingleton();
		return pm.m_strModuleName;
	}

	public static String getRealRootPath(){
		PathMgr pm = getSingleton();
		return pm.m_strRealPath;
	}

	private static void initRealPathNotInWar(PathMgr pm) throws PathException {
		String realpath = null;
		//设置路径
		if ( pm.m_strModuleName == null ) {
			realpath = System.getProperty(USE_HOME);
			if ( realpath == null ) {
				System.setProperty(USE_HOME,DEFAULT_REAL_PATH);
				configLog4j(null);
			}
		}else{
			realpath = (System.getProperty(USE_HOME+"."+pm.m_strModuleName) == null) ? System.getProperty(USE_HOME) : System.getProperty(USE_HOME+"."+pm.m_strModuleName);
			System.setProperty(USE_HOME+"."+pm.m_strModuleName,realpath);
			if ( realpath == null ) {
				System.setProperty(USE_HOME+"."+pm.m_strModuleName,DEFAULT_REAL_PATH);

				configLog4j(null);
			}
		}

		if ( realpath == null ) realpath = DEFAULT_REAL_PATH;
		pm.setRealPath(realpath);
		pm.inWar(false);
	}


	/**
	 * 系统初始化时获取环境变量中的配置路径参数
	 * @param sc Servlet上下文环境
	 * @throws venus.frames.mainframe.util.PathException
	 * @roseuid 3F9E379F02FD
	 */
	public static void init(ServletContext sc,String strModuleName) throws PathException {

		//设置状态标志说明正在初始化系统路径数据
		m_bIsInit = true;
		//取得单一实例
		PathMgr pm = getSingleton();
		//表明整个路径信息已经被初始化完毕
		m_bHasInit = true;

		//通过判断设定是否以WAR方式部署
		if (sc == null) {			
			initRealPathNotInWar(pm);

		} else {
			//设置路径
			String realpath = sc.getRealPath("/");
			pm.setServletContext(sc);

			//String strModuleName =
			// moduleName;//sc.getInitParameter(PathMgr.WAR_NAME);
			pm.setModuleName(strModuleName);
			pm.inWar(true);
			if (realpath != null) {
				LogMgr.getLogger("venus.frames.mainframe.util.PathMgr").info("");
				LogMgr.getLogger("venus.frames.mainframe.util.PathMgr").info("---------------------------------------------------------------------------------------");
			    LogMgr.getLogger("venus.frames.mainframe.util.PathMgr").info("Context Path: " + realpath);
				LogMgr.getLogger("venus.frames.mainframe.util.PathMgr").info("---------------------------------------------------------------------------------------");
				LogMgr.getLogger("venus.frames.mainframe.util.PathMgr").info("");
				pm.setRealPath(realpath);
				
				//由于USE_HOME在WAR包部署和非WAR包部署的情况下都会使用，
				//所以只在System.getProperty(USE_HOME) == null的条件下进行赋值
				if (strModuleName == null) {
					if (System.getProperty(USE_HOME) == null) {
						configLog4j(null);
						System.setProperty(USE_HOME, realpath);
					}					
				}
				
				//由于Jboss等服务器在重复部署的情况下会生成不同的realpath，所以需要每次部署是重新设置
				//USE_HOME + "." + pm.m_strModuleName的系统属性值.	
				if (System.getProperty(USE_HOME + "." + pm.m_strModuleName) == null) 
					configLog4j(null);				
				System.setProperty(USE_HOME + "." + pm.m_strModuleName, realpath);
			} else {
			    LogMgr.getLogger("venus.frames.mainframe.util.PathMgr").info("------------PathMgr.init(...)-------------------can not get realpath from servletContext of app server : <NULL>");
			}
		}
		if ( pm.m_strModuleName !=null ){

			if (System.getProperty(USE_VENUS_ROOT+"."+pm.m_strModuleName) != null)
				pm.setRootPath(System.getProperty(USE_VENUS_ROOT+"."+pm.m_strModuleName));
			if (System.getProperty(USE_VENUS_TMP+"."+pm.m_strModuleName) != null)
				pm.setTmpPath(System.getProperty(USE_VENUS_TMP+"."+pm.m_strModuleName));
		}else{
			if (System.getProperty(USE_VENUS_ROOT) != null)
				pm.setRootPath(System.getProperty(USE_VENUS_ROOT));
			if (System.getProperty(USE_VENUS_TMP) != null)
				pm.setTmpPath(System.getProperty(USE_VENUS_TMP));
		
		}
	}

	/**
	 * 获取安全路径
	 * @return 安全路径（为相对路径），使用右向分隔符"/"
	 * @roseuid 3F9E42A402CE
	 */
	public String getSafePath() {
		//全部取虚拟路径，分隔符为右向分隔符"/"
		return getRootPath() + SAFE_PATH + WEB_SEPARATOR;
	}

	/**
	 * 获取PathMgr的全局单实例<br>
	 * 强烈建议用户利用该方法取得该类的实例，避免使用new方法新建一个实例
	 * @return venus.frames.mainframe.util.PathMgr
	 * @roseuid 3F9E4343006D
	 */
	public static PathMgr getSingleton() {
		//如果没有实例化，则首先实例化该类
		if (m_Singleton == null) {
			m_Singleton = new PathMgr();
		}
		return m_Singleton;
	}

	/**
	 * 设置真实的文件系统起始路径 --->"/"， 对应系统中该路径在系统中的真实路径，
	 * 其他的虚拟路径都是相对于该路经而言的。
	 * @param realpath 真实文件系统的起始路经
	 * @roseuid 3F9E513701B1
	 */
	public void setRealPath(String realpath) throws PathException {
		try {
//		    LogMgr.getLogger(this).info( "excute setRealPath method in setRealPath(), realpath=" + realpath );
			File tmpFile = new File(realpath);
			if (!tmpFile.exists()) {
				throw new PathException("输入的路径不存在或不可访问！");
			}
		} catch (Exception e) {
			LogMgr.getLogger(this).error("PathMgr类中的setRealPath方法，输入的路径参数错误！",e);
			//throw new PathException("输入的路径错误！");
		}
		this.m_strRealPath = realpath;
	}

	/**
	 * 设置是否在WAR中部署
	 * @param inwar true-以WAR方式部署；false-以非WAR方式部署
	 * @roseuid 3F9E53310188
	 */
	public void inWar(boolean inwar) {
		this.m_bIsInWar = inwar;
	}

	/**
	 * 得到是否在WAR中部署
	 * @return true-以WAR方式部署；false-以非WAR方式部署
	 */
	public boolean isInWar() {
		return this.m_bIsInWar;
	}

	/**
	 * 查看整个路径是否已经被初始化完毕
	 * @return true-已经初始化完毕；false-还没有被初始化
	 */
	public static boolean hasInit() {
		return m_bHasInit;
	}

	/**
	 * 查看Log4j路径是否已经被初始化
	 * @return true-已经初始化完毕；false-还没有被初始化
	 */
	public static boolean hasLog4jConf() {
		return PathMgr.m_bHasLog4jConf;
	}

    public static String getMavenAbsoluteConfPath(String path) {
        return PathMgr.class.getResource("/").getPath() + path.substring(path.lastIndexOf("/") + 1);
    }

}
