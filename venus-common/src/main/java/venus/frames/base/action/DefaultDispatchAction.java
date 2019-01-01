package venus.frames.base.action;

import venus.frames.base.IGlobalsKeys;
import venus.frames.base.action.plugin.IActionsPlugin;
import venus.frames.base.bean.*;
import venus.frames.base.exception.BaseApplicationException;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.ClassLocator;
import venus.frames.mainframe.util.ConfMgr;
import venus.frames.mainframe.util.IConfReader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * 专门分发逻辑处理的 Action
 * 主要根据配置数据分发Action给不同插件，
 * 或者项目组件继承该类重载分发部分的方法满足自己的分发逻辑的需要
 * ----------下面是对分发时的有些说明--------------------------------
 * 最奸根据配置数据分发Action给不同插件。
 * 建议plugin不要返回NULL，由于主调用程序如果发现返回为NULL，便会继续查找可用的PLUG
 * IN运行
 * 查找顺序为：
 * 查找配置文件定义的顺序为：
 * "%Action.cmd%":被 DefaultDispatchAction 注册了的某一请求Action的cmd的某一传参。
 * "%Action%":被 DefaultDispatchAction 注册了的某一请求Action。
 * common：通用使用于所有被 DefaultDispatchAction 注册了的请求Action。
 * 先查[ACTION名].[CMD名]对应的CLASS，再查[ACTION名]对应的CLASS，再查[common]对应的
 * CLASS
 * 如果该配置数据没有找到，主调用程序便查找 
 * getMethod(...)方法得到处理方法，如果没有则返回到 forward 为 "error" 的页面，同时传入 
 * Error对象
 * 该方法依赖方法 iniMethodNameMap()中定义的映射表，如果找到适当的方法则运行之。
 * 特别注意：如果iniMethodNameMap()中定义的映射表，会直接根据传递的命令参数名查询方法并执行之。by wujun(2005-8-10)
 * --------------------struts 配置文件片断------------------------
 * <action name="tstActionForm" type="tststruts.tstDispatchAction" validate="true" 
 * scope="request" path="/tstDispatch" parameter="cmd">
 * <forward name="failure" path="/jsp/login/loginForm.jsp"/>
 * <forward name="success" path="/jsp/login/loginForm.jsp"/>
 * <forward name="form" path="/jsp/login/loginForm.jsp"/>
 * <forward name="hello" path="/hello.html"/>
 * <forward name="common" path="/jsp/dispatch.jsp"/>
 * <forward name="dispatch" path="/jsp/dispatch.jsp"/>
 * <forward name="dispatchHello" path="/jsp/dispatch.jsp"/>
 * </action>
 * 
 * @author 岳国云
 */
public class DefaultDispatchAction extends BaseAction implements ILog,IGlobalsKeys {

	/**
	 * 用于存储传入参数同实际类方法名的对应关系的列表
	 * 例如：传入菜单CMD的参数"abc",对应该类中的方法为"Abcd(.....)"
	 */
	protected Hashtable m_hashMethods = new Hashtable();

	/**
	 * 得到自身句柄供反射时调用
	 */
	protected Class m_clazz = this.getClass();

	/**
	 * 用以标识缺省状态运行的标识
	 *
	 * 通用使用于所有被该 DefaultDispatchAction 注册了的请求Action 的响应动作
	 */
	public static String COMMON_KEY = "common";

	/**
	 * 存储传入参数类中方法的对应关系
	 */
	protected Hashtable m_hashMethodName = iniMethodNameMap();

	/**
	 * 用于暂存所有插件实例的 一个Hashtable
	 *
	 * 该Hashtable主要 COMMON_KEY和不同Action及传参对应的插件对象数组
	 *
	 * 这两个数组在Hashtable的key分别是 COMMON_KEY 和 具体的传参
	 */
	private Hashtable m_hashPlugin = new Hashtable();

	/**
	 * 用于暂存所有插件类名的 一个Hashtable
	 * 该Hashtable主要
	 * 存储两个数组，这两个数组，一个存储init的插件类名数组，一个存储service的插件类名?
	 * 组
	 * 这两个数组在Hashtable的key分别是 INIT_KEY 和 SERVICE_KEY
	 */
	private Hashtable m_hashPluginName = new Hashtable();

	protected Class types[] = { DefaultForm.class, IRequest.class, IResponse.class };

	/**
	 * 缺省构造器
	 *
	 * @roseuid 3F42C63501AD
	 */
	public DefaultDispatchAction() {
		super();
//		loadConf();
	}

	/**
	 * 子类可重载该方法加入自己的逻辑
	 *
	 * 在该方法中完成参数 key 的构建并查询运行该KEY相应的插件
	 *
	 * @param formBean - 传入的表单的对象
	 * @param request - 传入的请求对象
	 * @param response - 供传出的响应对象
	 * @return venus.frames.mainframe.base.action.IForward 页面跳转对象
	 * @throws Exception
	 * @roseuid 3F42CAAA013F
	 */
	protected IForward runPlugin(
		DefaultForm formBean,
		IRequest request,
		IResponse response) throws Exception {

		//从request中得到IMappingCfg实例
		IMappingCfg map = request.getMapping();

		IForward re = null;
		//返回该Action组件对应的路径名
		String path = map.getPath();

		//得到配置中的该Action配置数据中传入的参数
		String parameter = map.getParameter();

		if (parameter == null)
			return null;
		if (path == null)
			return null;
		path = path.trim();
		parameter = parameter.trim();

		//从Request中得到参数所对应的值
		String name = request.getServletRequest().getParameter(parameter);
		String keyName = path + "." + name;

		re = findPlugin(keyName, map, formBean, request, response);

		if (re == null) {
			keyName = path;
			re = findPlugin(keyName, map, formBean, request, response);
		}
		if (re == null) {
			keyName = COMMON_KEY;
			re = findPlugin(keyName, map, formBean, request, response);
		}
		return re;
	}

	/**
	 * 根据传入的方法名找到该类对应的方法并返回
	 *
	 * 子类可重载该方法加入自己的逻辑，返回自己需要的方法
	 *
	 * 具体处理过程：
	 * 1.先查询该列表中是否存在该需要的方法。如果存在则返回
	 * 2.如果不存在则通过自身句柄得到该方法并存入 "方法列表" ，并返回
	 * 注意异常处理
	 *
	 * @param name - 传入方法名
	 * @return Method  Method对象
	 *
	 * @throws NoSuchMethodException
	 * @roseuid 3F42CCE1021A
	 */
	protected Method getMethod(String name) throws NoSuchMethodException {

		Method method = null;

		//根据传入的方法名找到该类对应的方法
		if (m_hashMethods.containsKey(name)) {
			method = (Method) m_hashMethods.get(name);
		}
		if (method == null) {
			if( m_hashMethodName!=null && m_hashMethodName.containsKey( name ) ){

				String methodName = (String) m_hashMethodName.get(name);
				method = m_clazz.getMethod(methodName, types);
				m_hashMethods.put(name, method);

			} else {

				method = m_clazz.getMethod(name, types);
				m_hashMethods.put(name, method);

			}

		}

		//返回该方法
		return method;
	}

	/**
	 * 构造参数值同方法名的对应表,子类可重载该方法加入自己的逻辑
	 *
	 * 中定义的映射表，如果找到适当的方法则运行。
	 *
	 * 所以如果采用子类应用模式，需重载该方法加入自己新定义的方法
	 * @return Hashtable
	 * @roseuid 3F42CD22020A
	 */
	protected Hashtable iniMethodNameMap() {
		Hashtable map = new Hashtable();
		return map;
	}

	/**
	 * 子类可重载改方法获取配置数据
	 *
	 * 该配置数据主要集中在
	 *
	 * COMMON_KEY
	 * Action名
	 * Action名.参数值
	 *
	 * 对应的插件对象数组
	 *
	 * 具体操作过程：
	 *
	 * 先通过类名得到缺省配置解析器然后
	 *
	 * 根据属性名"COMMON_KEY" 得到以 "COMMON_KEY"为KEY的插件类名
	 *
	 * 根据字节点名 "dispatch" 得到 "Action名"或"Action名.参数值"为KEY的插件类名数组
	 *
	 * 数组中每条记录的形式为："Action名:插件类名"或"Action名.参数值:插件类名"
	 *
	 * 根据以上得到的数据存入插件类名的 m_hashPluginName 中，供以后使用
	 *
	 * the xml conf is :
	 * <venus.frames.mainframe.base.action.DefaultDispatchAction
	 * common="venus.frames.mainframe.cache.impl.CacheImpl" >
	 * <dispatch action="Action名:插件类名"/>
	 * <dispatch action="Action名.参数值:插件类名"/>
	 * </venus.frames.mainframe.base.action.DefaultDispatchAction>
	 * @roseuid 3F42D68000D2
	 */
	protected void loadConf() {

		String[] actionPluginName = null;
		try {
			//得到venus.frames.mainframe.base.action.DefaultDispatchAction节点的解析器
			IConfReader dcr =
				ConfMgr.getConfReader(this.getClass().getName());

			//读取本节点的"common"属性值
			String commPluginName = dcr.readStringAttribute("common");

			//如果得到的属性值不为空，则以COMMON_KEY为标识存入m_hashPluginName
			if (commPluginName != null)
				m_hashPluginName.put(COMMON_KEY, commPluginName);

			//一一读取子节点属性值，存入字符串数组
			actionPluginName = dcr.readChildStringAry("dispatch", "action");

		} catch (NullPointerException e) {
			LogMgr.getLogger(this.getClass().getName()).info(
				"loadConf(): read config file error！NullPointerException");
		}

		//以":"为分割符分割从配置文件读取的字符串
		if ( actionPluginName!=null && actionPluginName.length > 0) {
			int size=actionPluginName.length;
			for (int i = 0; i < size; i++) {
				StringTokenizer actionpnSt =
					new StringTokenizer(actionPluginName[i], ":");
				while (actionpnSt.hasMoreTokens()) {
					try {
						m_hashPluginName.put(
							actionpnSt.nextToken(),
							actionpnSt.nextToken());
					} catch (NoSuchElementException nsee) {
						LogMgr.getLogger(this).error(
							"loadConf():read config file error: NoSuchElementException");
					}
				}
			}
		}
	}

	/**
	 * 在该方法中实现分发逻辑
	 *
	 * @param formBean - 传入的表单的对象
	 * @param request - 传入的请求对象
	 * @param response - 供传出的响应对象
	 * @return venus.frames.mainframe.base.action.IForward 页面跳转对象
	 * @throws Throwable
	 * @throws venus.frames.base.action.DefaultServletException
	 * @roseuid 3F83B0AB017E
	 */
	public IForward service(
		DefaultForm formBean,
		IRequest request,
		IResponse response) throws Exception {

		IForward iforward = null;
		String parameter = request.getMapping().getParameter();

		//如果在配置文件的mapping中parameter项为空，则加入出错信息并返回null
		if (parameter == null) {
			saveError(request,"parameter error : can not find parameter config in struts config for action "+this.getClass().getName());
			return request.findForward(ERROR_PAGE_KEY);
		}

		//调用本类runPlugin(...)方法，查找配置文件，按照顺序%Action.cmd%、%Action%、common返回分发对象
		//如果分发对象不为空，则转发。
		iforward = runPlugin(formBean, request, response);
		if (iforward != null) {
			return iforward;
		}

		//如果该配置数据没有找到，主调用程序便按照配置文件mapping中parameter项查找请求参数，
		//以此参数为方法名，查找getMethod(...)方法进行处理，
		//如果没有则返回到 error.jsp 页面
		String name =
			((javax.servlet.http.HttpServletRequest) request).getParameter(
				parameter);
		if (name == null) {
			saveError(request,"parameter:\""+parameter+"\" value error : is null ");			

			//response.sendErrors(new BaseError(null, "parameter value error ")
			//	new Errors(new BaseError(null, "parameter value error ")));
			return request.findForward(ERROR_PAGE_KEY);
		}
		
		Method method = getMethod(name);

			Object args[] = { formBean, request, response };
			
			try{
				
				iforward = (DefaultForward) method.invoke(this, args);
				
			}catch (InvocationTargetException e) {	
				Throwable t = e.getTargetException();
				
				if( t instanceof  Exception) {
					
					throw (Exception)t;
				
				}else{
					
					LogMgr.getLogger(this.getClass().getName()).error(
					"InvocationTargetException in service(). the cause is" ,t);
					
					throw new BaseApplicationException("error in service() of "+this.getClass().getName() ,t );
				
				
				}
				
				
				
				
			}
		return iforward;
	}

	/**
	 * runPlugin 的辅助方法 
	 * 
	 * @param keyName - 用于搜索插件的 关键字
	 * @param map - 该 Action 的配置数据，比如说 forward 等
	 * @param formBean - 传入的表单的对象
	 * @param request - 传入的请求对象
	 * @param response - 供传出的响应对象
	 * @return venus.frames.mainframe.base.action.IForward 页面跳转对象
	 * @throws Exception
	 * @roseuid 3FAB503E03C8
	 */
	private IForward findPlugin(
		String keyName,
		IMappingCfg map,
		DefaultForm formBean,
		IRequest request,
		IResponse response) throws Exception {

		IForward forward = null;
		//如果m_hashPlugin中存在关键字keyName的Plugin实例，
		//则返回此实例的service（..）
		if (m_hashPlugin.containsKey(keyName)) {

			//得到Plugin实例iap
			IActionsPlugin iap = (IActionsPlugin) m_hashPlugin.get(keyName);

				//运行plugin实例的service(...)方法，得到页面跳转对象forward
				forward = iap.service(map, formBean, request, response);

			//如果m_hashPluginName中存在关键字keyName的Plugin类名，则实例化此类
			//返回此实例的service（..）
		} else {

			if (!m_hashPluginName.containsKey(keyName)) {
				return null;
			}
			//得到存储的插件类名字
			String loadClassNames = (String) (m_hashPluginName.get(keyName));
			try {
				//得到插件类实例
				Class c = ClassLocator.loadClass(loadClassNames.trim());
				IActionsPlugin s = (IActionsPlugin) c.newInstance();

				//调用插件类的service(...)方法，得到页面跳转对象forward
				forward = s.service(map, formBean, request, response);
				//存储该实例
				m_hashPlugin.put(keyName, s);
			} catch (ClassNotFoundException cnfe) {
				LogMgr.getLogger(this.getClass().getName()).info(
					"findPlugin(): ClassNotFoundException return null");
				return null;
			} catch (IllegalAccessException iae) {
				LogMgr.getLogger(this.getClass().getName()).info(
					"findPlugin(): IllegalAccessException return null");
				return null;
			} catch (InstantiationException dse) {
				LogMgr.getLogger(this.getClass().getName()).info(
					"findPlugin(): InstantiationException return null");
				return null;
			} catch (DefaultServletException dse) {
				LogMgr.getLogger(this.getClass().getName()).info(
					"findPlugin(): DefaultServletException return null");
				return null;
			}
		}
		//返回页面跳转对象forward
		return forward;
	}
	

}
