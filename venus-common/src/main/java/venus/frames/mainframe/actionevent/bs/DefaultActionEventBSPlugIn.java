package venus.frames.mainframe.actionevent.bs;


import venus.frames.mainframe.log.LogMgr;

/**
 * 触发ActionEvent事件时调用该插件，完成事件的处理
 * 
 * @author 岳国云
 */
public class DefaultActionEventBSPlugIn implements IActionEventBSPlugIn {

	/**
	 * @see IActionEventBSPlugIn#service(String, String)
	 */
	public DefaultActionEventBSPlugIn() {
	}

	/**
	 * action操作事件处理，完成参数sessionid对应的SessionProfile
	 * 的setActionName方法，构建ActionChangeEvent并绑定事件监听
	 * 
	 * @param sessionid - 当前用户的sessionid号
	 * @param loginName - 该用户登录名
	 * @param oldState  - 历史操作的状态
	 * @param newValue  - 当前Action事件操作
	 * @param oldValue  -历史Action事件操作
	 * @return void 
	 */
	public void service(
		String sessionid,
		String loginName,
		String oldState,
		String newValue,
		String oldValue) {
		/*
				SessionProfile sessp = null;
		
				//得到sessionid对应的SessionProfile
				try {
					sessp =
						(SessionProfile) ProfileMgr.getSessionProfile(
							IGlobalsKeys.APP_CONTEXT_KEY,
							sessionid);
				} catch (ProfileException pe) {
					LogMgr.getLogger(this.getClass().getName()).error(
						"service ProfileException",
						pe);
				}
		
				//构建ActionChangeEvent并绑定事件监
				if (sessp != null) {
					sessp.setActionName(newValue);
					LogMgr.getLogger(this.getClass().getName()).info("--setActionName ok--");
				}
				*/
		LogMgr.getLogger(this.getClass().getName()).info(
			"--DefaultActionEventBSPlugIn.service(): is called,setActionName not ok--");
	}
}
