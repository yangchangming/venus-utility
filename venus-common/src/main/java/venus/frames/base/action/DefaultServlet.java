package venus.frames.base.action;

import gap.commons.digest.DigestLoader;
import gap.license.exception.InvalidLicenseException;
import venus.frames.base.IGlobalsKeys;
import venus.frames.base.action.plugin.IServletPlugin;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.action.HttpResponse;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.ClassLocator;
import venus.frames.mainframe.util.Helper;
import venus.frames.mainframe.util.PathMgr;
import venus.pub.util.ReflectionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * 主 Servlet 集中处理类
 * 
 */
public class DefaultServlet extends HttpServlet implements ILog, IGlobalsKeys {

	/**
	 * 用以标识初始化方法运行的标识
	 * 
	 * 1.由于运行插件的方法集中处理，则需要知道是哪个方法起调的插件运行方法
	 * 2.该标识表示配置参数中初始化的参数名
	 */
	public final static String INIT_KEY = "init-classes";

	/**
	 * 用以标识 service方法运行的标识
	 * 
	 * 1.由于运行插件的方法集中处理(runPlugin(...))，则需要知道
	 * 是哪个方法起调的插件运行方法(runPlugin(...))
	 * 2.该标识表示配置参数中service的参数名
	 */
	public final static String SERVICE_KEY = "service-classes";

	/**
	 * MSG_ON_EXCEPTION:servlet捕获异常后的提示信息
	 * 缺省：“访问以下资源时出现异常，请与系统管理员联系，谢谢合作！”
	 */
	public static String MSG_ON_EXCEPTION = "\u8BBF\u95EE\u4EE5\u4E0B\u8D44\u6E90\u65F6\u51FA\u73B0\u5F02\u5E38\uFF0C\u8BF7\u4E0E\u7CFB\u7EDF\u7BA1\u7406\u5458\u8054\u7CFB\uFF0C\u8C22\u8C22\u5408\u4F5C\uFF01";
	
	//"访问以下资源时出现异常，请与系统管理员联系，谢谢合作！";
	
	/**
	 * 用以标识 MSG_ON_EXCEPTION的标识
	 * 
	 * 该标识表示web.xml配置参数中MSG_ON_EXCEPTION的参数名
	 */
	public final static String MSG_ON_EXCEPTION_KEY = "msg-on-exception";
	
	/**
	 * 用于暂存所有插件实例的 一个Hashtable
	 * 
	 * 该Hashtable主要存储两个数组，这两个数组，
	 * 
	 * 一个存储init的插件对象数组，一个存储service的插件对象数组
	 * 
	 * 这两个数组在Hashtablep的key分别是 INIT_KEY 和 SERVICE_KEY
	 */
	private Hashtable m_hashPlugin = new Hashtable();

	/**
	 * 用于暂存所有插件类名的 一个Hashtable
	 * 
	 * 该Hashtable主要存储两个数组，这两个数组，
	 * 
	 * 一个存储init的插件类名数组，一个存储service的插件类名数组
	 * 
	 * 这两个数组在Hashtable的key分别是 INIT_KEY 和 SERVICE_KEY
	 */
	private Hashtable m_hashPluginName = new Hashtable();

	/**
	 * 用以在WEB.XML中标识分发插件的标识
	 * 
	 * <init-param>      
	 * <param-name>dispatch-classes</param-name>      
	 * <param-value>/tst/*:venus.XXX.XXXPlugIn;</param-value>
	 * </init-param>
	 */
	public String DISPATCH_KEY = "dispatch-classes";

	/**
	 * @roseuid 3FAA2D73001F
	 */
	public DefaultServlet() {
		super();
	}

	/**
	 * 初始化的方法
	 * 
	 * 加载初始化配置数据loadConf()
	 * 
	 * 并执行 runPlugin(INIT_KEY,null,null);
	 * 
	 * 最后运行父类的方法 super.init();
	 * 
	 * @throws ServletException
	 * @roseuid 3F41E15A020F
	 */

	public void init() throws ServletException {

		//加载初始化配置数据
		loadConf();

		//运行初始化插件
		try {
			runPlugin(INIT_KEY, null, null);
		} catch (DefaultServletException dse) {
			if (DefaultServletException.STOP_CODE == dse.getCode()) {
				super.init();
				return;
			}
			error("init() : DefaultServletException", dse);
		} 
		//增加Catch，防止plugin跑出其他异常，造成super.init() 不能执行
		catch( Exception e ){
			super.init();
			return;
		}

		//运行父类的方法 init()
//		super.init();
	}

	/**
	* 原 struts 中的Servlet 的集中执行方法。struts 的方法不提倡使用
	* 
	* 重载该方法用于包装出我们自己的执行方法 service(...);
	* 
	* 1.包装request和response
	* 2.调用service(....)方法
	* 3.转发控制权给相应的插件，如果转发成功则交出控制权返回。如果转发失败则继续
	* 3.调用父类的方法，进行Action的分发控制
	* 
	* @param request - HttpServletRequest请求对象
	* @param response - HttpServletResponse对象
	* @throws ServletException
	* @throws IOException
	* @roseuid 3F41E164023E
	*/

	public void process(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		try {
			if( PathMgr.WEB_CONTEXT_PATH==null || PathMgr.WEB_CONTEXT_PATH.length()<1 ) {
				
				PathMgr.WEB_CONTEXT_PATH = request.getContextPath();
				Helper.WEB_CONTEXT_PATH = PathMgr.WEB_CONTEXT_PATH;
				Helper.getContext().setAttribute(WEB_CONTEXT_PATH_KEY, PathMgr.WEB_CONTEXT_PATH);
			
			}
			
			//包装request和response
			IRequest req = new HttpRequest(request);
			IResponse res = new HttpResponse(response);
			//req.setCurrentLoginProfile(....);
			//调用service(IRequest,IResponse)方法
	
			try {
//				try {
//					DigestLoader loader = DigestLoader.getLoader();
//					if (loader.isValid() && Math.random() > 0.95) {
//						LogMgr.getLogger("udp.use.platform").info(
//								"udp platform: access method ckls!");
//						chkLS(loader);
//					} else if (!loader.isValid()) {
//						chkLS(loader);
//					}
//				} catch (RuntimeException re) {
//					LogMgr.getLogger("udp.use.platform").error( re.getClass().getName()+" " + re.getMessage() );
//					throw new ServletException(re.getClass().getName()+" " + re.getMessage());
//				}
				service(req, res);
			} catch (DefaultServletException dse) {
				if (DefaultServletException.STOP_CODE == dse.getCode()){
					info("process(....): stop in DefaultServletException.STOP_CODE");
					return;			
				}else{
					error("call service(req, res) in process(....) ",dse);
					showExceptionForWeb(dse,response,request);
				}
	
			}
	
			//转发控制权给相应的插件，如果转发成功则交出控制权返回
			if (dispatchByPath(request.getServletPath(), req, res)) {
				return;
			}
	
			//如果转发失败，则调用父类的方法，进行Action的分发控制
//			super.process((HttpServletRequest) req.getServletRequest(), (HttpServletResponse) res.getServletResponse());
		
		} catch (Exception e) {
			//info("exception in process(....) ",e);
			showExceptionForWeb(e,response,request);
		}
	}
	
	private void showExceptionForWeb(Exception e,HttpServletResponse response,HttpServletRequest request) throws  ServletException, IOException {
		if( MSG_ON_EXCEPTION == null || MSG_ON_EXCEPTION.length()<1 )  throw new ServletException(e);
		
		try {
			response.setContentType("text/html;charset=GB2312");			
			//response.setHeader();	
			
		    PrintWriter pw = response.getWriter();
			pw.print("<html><head><meta http-equiv='content-type' content=text/html; charset='GB2312'></head><body><br><br>\r\n");
			pw.print(MSG_ON_EXCEPTION);
			
			pw.print("<br><br>\r\nURL: ");
			pw.print(request.getRequestURL());			
			
			String msg = e.getMessage();
			if (msg != null && msg.indexOf("license")>0){
				pw.print("<br><br><font color='red'><pre>\r\nERROR Message: ");
				pw.print( e.getMessage() );
			}else{
				pw.print("<br><br><font color='white'><pre>\r\n");
				e.printStackTrace(pw);
			}
			pw.print("</pre></font><br>\r\n</body></html>");
			
			pw.flush();	
			pw.close();		
		} catch (Exception newe) {
			error("exception in showExceptionForWeb(....) ",newe);
		}	
	}

	/**
	* 销毁时执行的方法
	* 
	* 很简单：就是在插件实例表中 mapPlugin 获取各实例并调用各实例的 destroy()方法
	* 
	* 并销毁 m_mapPlugin 和 m_mapPluginName。
	* 
	* @roseuid 3F41E16B0366
	*/

	public void destroy() {

		if (m_hashPlugin == null) {
			return;
		}

		//取出各实例
		Iterator iters = m_hashPlugin.values().iterator();
		while (iters.hasNext()) {
			Object obj = iters.next();

			//如果是IServletPlugin数组实例，则一一执行其destroy方法
			if (obj instanceof IServletPlugin[]) {
				IServletPlugin[] tmpary = (IServletPlugin[]) obj;
				for (int i = 0; i < tmpary.length; i++) {
					tmpary[i].destroy(this);
				}
				tmpary = null;

			} else if (obj instanceof IServletPlugin) {
				IServletPlugin tmp = (IServletPlugin) obj;
				tmp.destroy(this);
	}
}

		//销毁 m_mapPlugin 和 m_mapPluginName
		if (m_hashPlugin != null)
			m_hashPlugin.clear();
		m_hashPlugin = null;
		if (m_hashPluginName != null)
			m_hashPluginName.clear();
		m_hashPluginName = null;
	}

	/**
	* 我们自己封装后的方法,
	* 
	* 仅起调执行调用插件方法
	* 
	* 供具体项目组重载。
	* 
	* @param request - IRequest对象
	* @param response - IResponse
	* @throws IOException
	* @throws venus.frames.base.action.DefaultServletException
	* @roseuid 3F83B04A00B3
	*/

	public void service(IRequest request, IResponse response)
		throws IOException, DefaultServletException {

		//运行SERVICE_KEY标识的插件
		runPlugin(SERVICE_KEY, request, response);
	}

	/**
	 * 集中运行插件的方法(包括启动spring环境，非springmvc环境)
	 * 
	 * 抽取该方法主要是简化代码，合并重复代码
	 * 
	 * 主要针对 init 和 service 中运行插件的代码，destroy中运行的代码自行编写。
	 * 
	 * 该方法主要根据起调方法标识 获取相应的插件实例数组 
	 * 并调用runPluginAryService(...)运行这些插件service方法
	 * 
	 * 如果在获取该插件实例数组时发现该实例数组并未构建出来,则获取相应的 
	 * 插件名数组，并实例化这些插件，先init,如果是service中运行的插件
	 * 还要运行插件的service()方法.然后将这些实例装入插件实例数组, 
	 * 并按起调方法标识存入映射表中供以后使用.
	 * 
	 * @param runLoc - 起调方法的标识
	 * @param request - 传入的请求对象，供插件使用
	 * @param response - 传入的回应对象，供插件使用
	 * @roseuid 3FA5B65E00F9
	 */

	protected void runPlugin(String runLoc, IRequest request, IResponse response) throws DefaultServletException {

		if (m_hashPlugin.containsKey(runLoc)) {

			//如果存在相应的标识插件实例，根据起调方法标识获取相应的插件实例数组,
			//调用runPluginAryService(...)运行这些插件service方法
			IServletPlugin[] tmpary =
				(IServletPlugin[]) m_hashPlugin.get(runLoc);
			runPluginAryService(tmpary, request, response);
		} else {
			if (!m_hashPluginName.containsKey(runLoc)) {
				return;
			}
			//获取相应的插件名数组，如果存在，则实例化这些插件,运行service()方法,否则返回。
			String[] classNames = (String[]) m_hashPluginName.get(runLoc);
			if (!(classNames.length > 0)) {
				return;
			}

			boolean isService = runLoc.equals(SERVICE_KEY);
			Vector tmpv = new Vector();
			int size = classNames.length;
			for (int k = 0; k < size; k++) {
				String tmpstr = classNames[k];
				tmpstr= tmpstr.trim();
				IServletPlugin isplugin;
				try {
					Class c = ClassLocator.loadClass(tmpstr);
					isplugin = (IServletPlugin) c.newInstance();

					//调用IServletPlugin实例的init（..）方法
					isplugin.init(this);
					//保存Plugin实例
					tmpv.addElement(isplugin);
					//调用IServletPlugin实例的service（...）方法
					if (isService)
						isplugin.service(this, request, response);
				} catch (ClassNotFoundException cnfe) {
					error("runPlugin(): ClassNotFoundException", cnfe);
					continue;
				} catch (IllegalAccessException iae) {
					error("runPlugin(): IllegalAccessException", iae);
					continue;
				} catch (InstantiationException ie) {
					error("runPlugin(): InstantiationException", ie);
					continue;
				} catch(IllegalStateException ise){
					//重复加载applicationContext时，继续执行
					warn("runPlugin(): IllegalStateException,Cannot initialize context because there is already a root application context present", ise.getCause());
					continue;
				}catch (DefaultServletException e) {
					if (DefaultServletException.STOP_CODE == e.getCode())
						throw e;
					continue;
				}catch( Exception e ){
					error("runPlugin(): Exception", e);
					continue;
				}
			}

			//将存储插件实例的Vector转成插件实例数组，
			//然后按起调方法标识存入映射表中供以后使用。
			if (tmpv.size() > 0) {
				IServletPlugin[] tmpary = new IServletPlugin[tmpv.size()];
				tmpv.copyInto(tmpary);
				m_hashPlugin.put(runLoc, tmpary);
			}
		}
	}

	/**
	 * 该方法是为了简化 runPlugin(..) 中的代码而将其中的代码段抽取出来成为新方法。
	 * 
	 * 依次主要执行的操作为读取数组中的对象，并运行 service(this, request, response); 
	 * 方法
	 * 
	 * 注意异常处理
	 * @param tmpary - 需运行的插件对象列表
	 * @param request - 传入的请求对象，供插件使用
	 * @param response - 传入的回应对象，供插件使用
	 * @roseuid 3FA5B94D029F
	 */

	private void runPluginAryService(IServletPlugin[] tmpary, IRequest request, IResponse response) throws DefaultServletException {

		for (int i = 0; i < tmpary.length; i++) {
			try {
				tmpary[i].service(this, request, response);
			} catch (DefaultServletException e) {
				if (DefaultServletException.STOP_CODE == e.getCode())
					throw e;

				//如果某plugin实例的service(...)方法出现异常，继续下一次循环
				continue;
			}
		}
	}

	/**
	 * 从 WEB.XML 文件中加载相应配置数据
	 * -----------------------------------------------
	 * 配置数据可以配在WEB.XML中servlet数据中：
	 * <init-param>      
	 * <param-name>init-classes</param-name>      
	 * <param-value>venus.frames.mainframe.util.InitPathPlugIn</param-value>
	 * </init-param>
	 * <init-param>      
	 * <param-name>service-classes</param-name>      
	 * <param-value>venus.frames.mainframe.util.InitPathPlugIn</param-value>
	 * </init-param>
	 * <init-param>      
	 * <param-name>dispatch-classes</param-name>      
	 * <param-value>/tst/*:venus.XXX.XXXPlugIn;</param-value>
	 * </init-param>
	 * 
	 * 主要是取得这些配置数据并构建插件 存入 m_hashPluginName (Hashtable)
	 * 
	 * 读取过程基本为：
	 * 读取INIT_KEY标识的Classes列表：
	 * 1.读取init参数中的INIT_KEY标识的Classes列表字符串,以";"分割
	 * String iniClasses = getServletConfig().getInitParameter(INIT_KEY);
	 * 2.根据";"分割符，使用StringTokenizer或者其他工具类解析并构建出该字符串数组，
	 * 3.将该数组按INIT_KEY标识存入 m_hashPluginName (Hashtable) 供以后使用
	 * 
	 * 读取SERVICE_KEY标识的Classes列表：
	 * 1.读取init参数中的SERVICE_KEY标识的Classes列表字符串,以";"分割
	 * String serviceClasses = getServletConfig().getInitParameter(SERVICE_KEY);
	 * 2.根据";"分割符，使用StringTokenizer或者其他工具类解析并构建出该字符串数组，
	 * 3.将该数组按SERVICE_KEY标识存入 m_hashPluginName (Hashtable) 供以后使用
	 * 
	 * 读取DISPATCH_KEY标识的Classes列表：
	 * 1.读取init参数中的DISPATCH_KEY标识的Classes列表字符串,以";"分割，以":"分割key:va
	 * lue对
	 * String dispatchClasses = getServletConfig().getInitParameter(DISPATCH_KEY);
	 * 2.根据";"分割符，使用StringTokenizer或者其他工具类解析找到 key->value对，并存入 
	 * m_hashPluginName (Hashtable) 供以后使用
	 * @roseuid 3FA5BCE80280
	 */
	protected void loadConf() {

		String strexmsg = getServletConfig().getInitParameter(MSG_ON_EXCEPTION_KEY);
		if ( strexmsg != null && strexmsg.length()>0 )
			MSG_ON_EXCEPTION = strexmsg;
		
//		if (strexmsg == null ){			
//			MSG_ON_EXCEPTION = null;			
//		}else{			
//			if ( strexmsg.length()>0 ) MSG_ON_EXCEPTION = strexmsg;		
//		} 		
		
		String[] initClasses = getClassary(INIT_KEY);
		if (initClasses.length > 0) {
			m_hashPluginName.put(INIT_KEY, initClasses);
		}
		//调用本类私有方法getClassary(String),以";"分割并构建出该字符串数组
		//达到从WEB.XML的servlet中读取SERVICE_KEY标识的Classes列表
		String[] serviceClasses = getClassary(SERVICE_KEY);
		if (serviceClasses.length > 0) {
			m_hashPluginName.put(SERVICE_KEY, serviceClasses);
		}

		//首先调用本类私有方法getClassary(String),以";"分割并构建出该字符串数组,
		//然后以":"分割,找到KEY->VALUE对,并存入m_hashPluginName (Hashtable) 供以后使用
		String[] dispatchClasses = getClassary(DISPATCH_KEY);
		if (dispatchClasses.length > 0) {
			for (int i = 0; i < dispatchClasses.length; i++) {
				StringTokenizer st = new StringTokenizer(dispatchClasses[i], ":");
				while (st.hasMoreTokens()) {
					m_hashPluginName.put(st.nextToken(), st.nextToken());
				}
			}
		}
	}

	/**
	 * 根据传入的查找标识从配置文件查找插件字符串并以";"分割,
	 * 存入数组.此方法在loadConf()调用
	 * 
	 * @param keyType - 从WEB.XML文件中找到时的标识
	 * @return String[] - 查找结果以";"分割存入字符串数组
	 */

	private String[] getClassary(String keyType) {

		String strClasses = getServletConfig().getInitParameter(keyType);
		Vector v = null;
		String[] strAry = null;
		if (strClasses != null && strClasses.length() > 0) {
			StringTokenizer st = new StringTokenizer(strClasses, ";");
			v = new Vector();
			while (st.hasMoreTokens()) {
				v.addElement(st.nextToken());
			}
		} else {
			return new String[0];
		}
		if (v.size() > 0) {
			strAry = new String[v.size()];
			v.copyInto(strAry);
		}
		return strAry;
	}

	/**
	 * 转发控制权给相应的插件
	 * 
	 * 具体操作流程：
	 * 1.先调用 geiKeyFromPath( servletPath ) 解析出辨别插件所需的Key值
	 * 
	 * 2.先根据该Key 查找在 m_mapPlugin 中 相应的插件是否存在？
	 *     是：则从 m_mapPlugin 中 取出该插件的实例，并运行service(this, request, 
	 * response)方法，返回 true 退出该方法
	 *     
	 *     否：
	 *         先根据KEY从 m_mapPluginName 中得到类名，
	 *              不能取到类名：返回 false 退出该方法
	 *              若能取到类名：实例化该类，并调用service(this, request, 
	 * response)方法，用时将该类存入m_mapPlugin
	 * 
	 * @param servletPath - 从该 request 中得到的 URL中请求servlet字串值
	 * @param req - 被封装的请求对象
	 * @param res - 被封装的响应对象
	 * @return boolean
	 * @roseuid 3FA89F47034A
	 */

	public boolean dispatchByPath(
		String servletPath,
		IRequest req,
		IResponse res) {

		if (servletPath == null || servletPath == "") {
			return false;
		}
		String key = getKeyFromPath(servletPath);
		//由key查找在 m_mapPlugin 中 相应的插件是否存在,如果存在,
		//则取出该插件的实例，并运行service(this, request, response)方法
		if (m_hashPlugin.containsKey(key)) {
			IServletPlugin tem = (IServletPlugin) m_hashPlugin.get(key);
			try {
				tem.service(this, req, res);
			} catch (DefaultServletException dse) {
				error("dispatchByPath() : DefaultServletException", dse);
				return false;
			}
			return true;
			//如果相应的插件不存在,则先根据KEY从 m_mapPluginName 中得到类名,
			//实例化该类，并调用service(this, request,response)方法，同时将该实例存入m_mapPlugin
			//其中的异常都返回为false，如果没有得到类名,则返回false
		} else {
			String className = (String) m_hashPluginName.get(key);
			if (className != null && !className.equals("")) {
				try {
					Class classFor = ClassLocator.loadClass(className);
					IServletPlugin tem =
						(IServletPlugin) classFor.newInstance();
					tem.service(this, req, res);

					//将实例存入m_mapPlugin
					m_hashPlugin.put(key, tem);

				} catch (DefaultServletException dse) {
					error("dispatchByPath() : DefaultServletException", dse);
					return false;
				} catch (ClassNotFoundException cnfe) {
					error("dispatchByPach(): ClassNotFoundException", cnfe);
					return false;
				} catch (IllegalAccessException ilae) {
					error("dispatchByPach() : IllegalAccessException", ilae);
					return false;
				} catch (InstantiationException ie) {
					error("dispatchByPach(): InstantiationException", ie);
					return false;
				}
				return true;
			}
			return false;
		}
	}

	/**
	 * 从传入 request中的 URL中的 “请求servlet字串值” 中拆解出相对组件的路径值
	 * 
	 * 例如：http://localhost:8080/Venus/tst/tst1.do 这个URL中
	 * 
	 * http://localhost:8080 表示 协议+服务器名+端口
	 * 
	 * /Venus 表示 WAR包部署时映射的URL起始 Path 
	 * 
	 * /tst/tst1.do 即 “请求servlet字串值” 即在WAR内根据这个字串查找请求处理对象
	 * 
	 * /tst/* 
	 * 将是注册进WEB.XML中的配置,找到转发插件名的key，标识组件名，说明如果组件tst下(url
	 * 类似/tst/*)的所有请求由XXX插件接管
	 * 
	 * 可以根据该值在WEB.XML中的配置找到转发插件名；
	 * 
	 * 响应WEB.XML中的配置数据如下：
	 * <init-param>      
	 * <param-name>dispatch-classes</param-name>      
	 * <param-value>/tst/*:venus.XXX.XXXPlugIn;</param-value>
	 * </init-param>
	 * 
	 * @param servletPath - 从该 request 中得到的 URL中请求servlet字串值
	 * @return String - 相对组件的路径值
	 * @roseuid 3FA89FE803B8
	 */

	private String getKeyFromPath(String servletPath) {
		if (servletPath == null
			|| servletPath.equals("")
			|| servletPath.indexOf("/") == -1) {
			return null;
		} else {
			//判断该字符串中有几个"/"
			int count = 0;
			int size = servletPath.length();
			for (int i = 0; i < size; i++) {
				if (servletPath.charAt(i) == '/')
					count++;
			}
			//判断是否在字符串首位,是则返回servletPath+"/*"，否则返回空
			if (count == 1) {
				if (servletPath.startsWith("/"))
					return servletPath + "/*";
			}

			//该字符串中有多个"/",
			//则返回从servletPath字符串首位到最后一个"/"之间的字符串加上"/*"
			int index = servletPath.lastIndexOf("/");
			return servletPath.substring(0, index) + "/*";
		}
	}

	/**
	 * 调用记录日志方法
	 * @param message - 日志信息
	 * @roseuid 3FAA2D730119
	 */
	public void debug(Object message) {
		getIlog().debug(message);
	}

	/**
	 * 调用记录日志方法
	 * @param message - 日志信息
	 * @roseuid 3FAA2D730157
	 */
	public void info(Object message) {
		getIlog().info(message);
	}

	/**
	 * 调用记录日志方法
	 * @param message - 日志信息
	 * @roseuid 3FAA2D730186
	 */
	public void warn(Object message) {
		getIlog().warn(message);
	}

	/**
	 * 调用记录日志方法
	 * @param message - 日志信息
	 * @roseuid 3FAA2D7301C5
	 */
	public void error(Object message) {
		getIlog().error(message);
	}

	/**
	 * 调用记录日志方法
	 * @param message - 日志信息
	 * @roseuid 3FAA2D730203
	 */
	public void fatal(Object message) {
		getIlog().fatal(message);
	}

	/**
	 * 调用记录日志方法
	 * @param nLevel - 日志级别
	 * @param message - 日志信息
	 * @roseuid 3FAA2D730232
	 */
	public void log(int nLevel, Object message) {
		getIlog().log(nLevel, message);
	}

	/**
	 * 调用记录日志方法
	 * @param message - 日志信息
	 * @param t - 异常诱因
	 * @roseuid 3FAA2D730290
	 */
	public void debug(Object message, Throwable t) {
		getIlog().debug(message, t);
	}

	/**
	 * 调用记录日志方法
	 * @param message - 日志信息
	 * @param t - 异常诱因
	 * @roseuid 3FAA2D7302FD
	 */
	public void info(Object message, Throwable t) {
		getIlog().info(message, t);
	}

	/**
	 * 调用记录日志方法
	 * @param message - 日志信息
	 * @param t - 异常诱因
	 * @roseuid 3FAA2D73035B
	 */
	public void warn(Object message, Throwable t) {
		getIlog().warn(message, t);
	}

	/**
	 * 调用记录日志方法
	 * @param message - 日志信息
	 * @param t - 异常诱因
	 * @roseuid 3FAA2D7303B9
	 */
	public void error(Object message, Throwable t) {
		getIlog().error(message, t);
	}

	/**
	 * 调用记录日志方法
	 * @param message - 日志信息
	 * @param t - 异常诱因
	 * @roseuid 3FAA2D74003E
	 */
	public void fatal(Object message, Throwable t) {
		getIlog().fatal(message, t);
	}

	/**
	 * 调用记录日志方法
	 * @param nLevel - 日志级别
	 * @param message - 日志信息
	 * @param t - 异常诱因
	 * @roseuid 3FAA2D74009C
	 */
	public void log(int nLevel, Object message, Throwable t) {
		getIlog().log(nLevel, message, t);
	}

	/**
	 * 
	 * 返回记录日志类，ILog的实例
	 * 
	 * @return ILog 
	 */
	private ILog getIlog() {
		return LogMgr.getLogger(this.getClass().getName());
		//return this.logger;
	}
	
	public boolean isDebugEnabled() {
		return getIlog().isDebugEnabled();
	}
	
	public boolean isInfoEnabled() {
		return getIlog().isInfoEnabled();
	}
	
	/**
	 * @param loader
	 */
	private void chkLS(DigestLoader loader) {

		boolean valid = true;
		try {
			Class cls = loader.findClass();
			Method m = ReflectionUtils.findMethod(cls, "checkLicense",
					new Class[] {});
			valid = new Boolean(ReflectionUtils.invokeMethod(m, null,
					new Object[] {}).toString()).booleanValue();
		} catch (RuntimeException e) {
			loader.setValid(false);
			throw e;
		}
		if (!valid) {
			loader.setValid(false);
			throw new InvalidLicenseException();
		} else {
			loader.setValid(true);
			LogMgr.getLogger("udp.use.platform").info( "udp platform: check ls successfully!" );
		}
	}
	
}
