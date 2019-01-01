//Source file: D:\\venus_view\\Tech_Department\\Platform\\Venus\\4项目开发\\1工作区\\4实现\\venus\\frames\\mainframe\\actionevent\\bs\\IActionEventBSPlugIn.java

package venus.frames.mainframe.actionevent.bs;

/**
 * Action事件 App端处理的逻辑插件接口说明
 * 
 * @author 岳国云
 */
public interface IActionEventBSPlugIn {

	/**
	 * Action事件 App端处理的逻辑插件执行方法
	 * @param sessionid
	 * @param loginName
	 * @roseuid 3FBAEF75031C
	 */
	public void service(
			String sessionid,
			String loginName,
			String oldState,
			String newValue,
			String oldValue);
}
