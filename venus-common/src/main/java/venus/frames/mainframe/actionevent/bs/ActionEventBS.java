//Source file: D:\\venus_view\\Tech_Department\\Platform\\Venus\\4项目开发\\1工作区\\4实现\\venus\\frames\\mainframe\\actionevent\\bs\\ActionEventBS.java

package venus.frames.mainframe.actionevent.bs;

import org.w3c.dom.Node;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.mainframe.currentlogin.ProfileMgr;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.ClassLocator;
import venus.frames.mainframe.util.ConfMgr;
import venus.frames.mainframe.util.DefaultConfReader;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Action事件 App端处理的逻辑主程序
 * 
 * 由他起调相应插件运行插件
 * 
 * @author 岳国云
 */
public class ActionEventBS extends BaseBusinessService implements IActionEventBS {

	/**
	 * 说明响应事件的类型为构建
	 */
	public static int CREATE_KEY = 1;

	/**
	 * 说明响应事件的类型为 Action变更
	 */
	public static int ACTION_CHG_KEY = 2;

	/**
	 * 说明响应事件的类型为销毁
	 */
	public static int ERASE_KEY = 0;

	/**
	 * 说明响应事件的类型为 path变更
	 */
	public static int PATH_CHG_KEY = 3;

	/**
	 * 用于存储插件名列表
	 * 
	 * 按不同key 存储
	 */
	private static Hashtable m_hashPlugInNames = new Hashtable();

	/**
	 * 用于存储插件实例列表
	 * 
	 * 按不同key 存储
	 */
	private static Hashtable m_hashPlugIns = new Hashtable();

	/**
	 * 缺省构造器
	 * 
	 * super();
	 * 
	 * loadConf();
	 * @roseuid 3FBAEDDB028F
	 */
	public ActionEventBS() {
		super();
		//读取配置文件，初始化数据
		loadConf();
	}

	/**
	 * 构建事件类型方法，创建当前用户数据堆，调用创建类型插件
	 * @param sessionid - 当前用户的sessionid号
	 * @param liginName - 当前用户名
	 * @return void
	 * @roseuid 3FBAE2CF033B
	 */
	public void create(String sessionid, String loginName) {

		/***************/
		LogMgr.getLogger(this.getClass().getName()).info(
			"create(...........)sessionid="
				+ sessionid
				+ ": loginName="
				+ loginName);
		/***************/

		if (sessionid == null || loginName == null) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"create(): sessionid , 参数不可以为空");
			return;
		}

		//构建一个SessionProfile
		ProfileMgr.createSessionProfile(sessionid, venus.frames.base.IGlobalsKeys.APP_CONTEXT_KEY,
			loginName, null, null);

		//运行构建类型插件
		runPlugIn(CREATE_KEY, sessionid, loginName, null, null);
	}

	/**
	 * Action事件类型方法，调用Action变更类型插件
	 * 
	 * @param sessionid - 当前用户的sessionid号
	 * @param oldState  - 历史操作的状态
	 * @param newValue  - 当前Action事件操作
	 * @param oldValue  -历史Action事件操作
	 * @return void
	 * @roseuid 3FBAE2E50251
	 */
	public void changeAction(
		String sessionid,
		String oldState,
		String oldValue,
		String newValue) {

		if (sessionid == null || newValue == null) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"changeAction(...) : 参数不可以为空");
			return;
		}

		//运行 Action变更类型插件
		runPlugIn(ACTION_CHG_KEY, sessionid, oldState, oldValue, newValue);
	}

	/**
	 * Path事件类型方法，调用Path变更类型插件
	 * 
	 * @param sessionid 当前用户的sessionid号
	 * @param oldState  历史操作的状态
	 * @param newValue  当前Path事件操作
	 * @param oldValue  历史Path事件操作
	 * @return void
	 * 
	 * @roseuid 3FBAE2FD01B4
	 */
	public void changePath(
		String sessionid,
		String oldState,
		String oldValue,
		String newValue) {

		if (sessionid == null
			|| oldState == null
			|| newValue == null
			|| oldValue == null) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"changePath(...): 参数不可以为空");
			return;
		}
		//运行path变更类型插件
		runPlugIn(PATH_CHG_KEY, sessionid, oldState, oldValue, newValue);
	}

	/**
	 * 调用销毁插件，调用销毁类型插件
	 * 
	 * @param sessionid - 要销毁的用户的sessionid号
	 * @param oldState  - 要销毁的用户名
	 * @return viod
	 * @roseuid 3FBAE4C801B4
	 */
	public void erase(String sessionid, String oldState) {

		if (sessionid == null || oldState == null) {
			LogMgr.getLogger(this.getClass().getName()).error("erase(...): ");
			return;
		}

		//运行销毁类型插件
		runPlugIn(ERASE_KEY, sessionid, oldState, null, null);
		
		//销毁一个SessionProfile
		ProfileMgr.eraseProfile(venus.frames.base.IGlobalsKeys.APP_CONTEXT_KEY,sessionid);
	}

	/**
	 * 加载配置数据:
	 * 
	 * 主要配置数据为不同事件类型响应下运行的插件名列表
	 * 
	 * 存入 m_hashPlugInNames
	 * 
	 * 配置文件如下：
	 * 
	 * <venus.frames.mainframe.actionevent.bs.ActionEventBS>
	 * <plugin name="XXX.XXX.XXXPlugin" type="0"/>
	 * <plugin name="XXX.XXX.XXXPluginr" type="1"/>
	 * </venus.frames.mainframe.actionevent.bs.ActionEventBS>
	 * 
	 * @return void
	 * @roseuid 3FBAEE9501D4
	 */
	protected void loadConf() {

		//四个Vector，分别用来暂存对应四种事件类型的Plugin名字
		Vector vErase = new Vector();
		Vector vCreate = new Vector();
		Vector vAction = new Vector();
		Vector vPath = new Vector();

		try {

			//得到venus.frames.mainframe.actionevent.bs.ActionEventBS节点的解析器
			DefaultConfReader dcr =
				new DefaultConfReader(ConfMgr.getNode(this.getClass().getName()));

			//读取venus.frames.mainframe.actionevent.bs.ActionEventBS的所有子节点
			ArrayList ary = dcr.readChildNodesAry("plugin");

			//一一读取各子节点的属性
			for (int i = 0; i < ary.size(); i++) {
				DefaultConfReader tempdcr = new DefaultConfReader((Node) ary.get(i));
				//读取第"i"个节点的"name"属性
				String name = tempdcr.readStringAttribute("name");

				//读取第"i"个节点的"type"属性值
				int type = tempdcr.readIntAttribute("type");

				//根据type的不同把从配置文件里读取的"name"属性存入相应的vector
				//当type为0时存入v0
				if (type == 0) {
					vErase.addElement(name);
				}
				//当type为1时存入v1
				if (type == 1) {
					vCreate.addElement(name);
				}
				//当type为2时存入v2
				if (type == 2) {
					vAction.addElement(name);
				}
				//当type为3时存入v3
				if (type == 3) {
					vPath.addElement(name);
				}
			}

			//把读入vector里的数据复制到相应的数组，
			//并以读取的type为KEY把各列表存入m_hashPlugInNames
			savePlugin(vErase, vCreate, vAction, vPath);

		} catch (Exception e) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"loadConf 读取配置文件错误！",
				e);
		}
	}

	/**
	 * 把读入vector里的数据复制到相应的数组，
	 * 并以读取的type为KEY把各列表存入m_hashPlugInNames
	 * 
	 * @param v0 存储type为erase操作的插件名
	 * @param v1 存储type为create操作的插件名
	 * @param v2 存储type为action操作的插件名
	 * @param v3 存储type为path操作的插件名
	 * @return void
	 */
	private void savePlugin(Vector v0, Vector v1, Vector v2, Vector v3) {

		//四个字符串数组，分别用来存储从
		//配置文件读取的对应四种事件类型的Plugin名字
		String[] ary0 = null;
		String[] ary1 = null;
		String[] ary2 = null;
		String[] ary3 = null;

		//把读入vector里的数据复制到相应的数组，
		//并以读取的type为KEY把各列表存入m_hashPlugInNames

		if (v0.size() > 0) {
			ary0 = new String[v0.size()];
			v0.copyInto(ary0);
			m_hashPlugInNames.put(String.valueOf(ActionEventBS.ERASE_KEY), ary0);
		}

		if (v1.size() > 0) {
			ary1 = new String[v1.size()];
			v1.copyInto(ary1);
			m_hashPlugInNames.put(String.valueOf(ActionEventBS.CREATE_KEY), ary1);
		}

		if (v2.size() > 0) {
			ary2 = new String[v2.size()];
			v2.copyInto(ary2);
			m_hashPlugInNames.put(
				String.valueOf(ActionEventBS.ACTION_CHG_KEY),
				ary2);
		}

		if (v3.size() > 0) {
			ary3 = new String[v3.size()];
			v3.copyInto(ary3);
			m_hashPlugInNames.put(String.valueOf(ActionEventBS.PATH_CHG_KEY), ary3);
		}
	}

	/**
	 * 根据 loc 查找不同的 plugin 运行:
	 * 
	 * 当create(..)事件调用此方法时，oldState为当前用户名
	 * 当erase(..)事件调用此方法时，oldState为要销毁的用户名
	 * 
	 * @param loc - 事件类型：ERASE_KEY = 0；CREATE_KEY = 1；ACTION_CHG_KEY = 2；PATH_CHG_KEY = 3。
	 * @param sessionid - 当前用户的sessionid号
	 * @return void
	 * @roseuid 3FBAEEF50157
	 */
	private void runPlugIn(
		int loc,
		String sessionid,
		String oldState,
		String oldValue,
		String newValue) {

		String loginName = null;
		if (loc == ActionEventBS.CREATE_KEY) {
			loginName = oldState;
			oldState = null;
		}

		String locstr = String.valueOf(loc);

		//如果m_hashPlugIns中存在以loc为标识的IActionEventBSPlugIn实例，
		//则一一运行实例的service(...)方法
		if (m_hashPlugIns.containsKey(locstr)) {
			IActionEventBSPlugIn[] iabsp =
				(IActionEventBSPlugIn[]) m_hashPlugIns.get(locstr);
			for (int i = 0; i < iabsp.length; i++) {
				iabsp[i].service(sessionid, loginName, oldState, oldValue, newValue);
			}

			//否则如果m_hashPlugInNames存在以loc为标识的PlugIn类名列表，
			//则一一实例化类名并运行各实例的service(...)方法
		} else if (m_hashPlugInNames.containsKey(locstr)) {

			//得到PlugIn类名列表
			String[] classnames = (String[]) m_hashPlugInNames.get(locstr);
			Vector v = new Vector();
			IActionEventBSPlugIn[] sary = null;
			for (int j = 0; j < classnames.length; j++) {
				try {

					//调用ClassLocator的方法loadClass（）得到类对象
					Class classFor = ClassLocator.loadClass(classnames[j]);

					//实例化得到的类对象
					IActionEventBSPlugIn iactionbsplu =
						(IActionEventBSPlugIn) classFor.newInstance();

					//运行IActionEventBSPlugIn实例的service(...)方法
					iactionbsplu.service(
						sessionid,
						loginName,
						oldState,
						oldValue,
						newValue);

					//将IActionEventBSPlugIn实例暂存于vector
					v.addElement(iactionbsplu);

				} catch (ClassNotFoundException cnfe) {
					LogMgr.getLogger(this.getClass().getName()).warn(
						"runPlugIn(): ClassLocator.loadClass--> ClassNotFoundException",
						cnfe);
					continue;
				} catch (InstantiationException ie) {
					LogMgr.getLogger(this.getClass().getName()).warn(
						"runPlugIn(): classFor.newInstance--> ClassNotFoundException",
						ie);
					continue;
				} catch (IllegalAccessException iae) {
					LogMgr.getLogger(this.getClass().getName()).warn(
						"runPlugIn(): classFor.newInstance--> ClassNotFoundException",
						iae);
					continue;
				}
			}

			//将IActionEventBSPlugIn实例列表存入m_hashPlugIns
			int size = v.size();
			if (size > 0) {
				sary = new IActionEventBSPlugIn[size];
				v.copyInto(sary);
				m_hashPlugIns.put(locstr, sary);
			}
		}
		return;
	}
}
