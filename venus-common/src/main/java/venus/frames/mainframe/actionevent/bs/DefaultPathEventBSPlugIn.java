package venus.frames.mainframe.actionevent.bs;


import venus.frames.mainframe.log.LogMgr;

/**
 * 触发PathEvent事件时运行该插件，完成事件的处理
 * 
 * @author 岳国云
 */
public class DefaultPathEventBSPlugIn implements IActionEventBSPlugIn {

	/**
	 * 构造函数
	 * 
	 * @see IActionEventBSPlugIn#service(String, String)
	 */
	public DefaultPathEventBSPlugIn() {
	}

	/**
	 * path操作事件处理，完成参数sessionid对应的SessionProfile
	 * 的setPathName方法，构建ActionChangeEvent并绑定事件监听
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
				//得到APP端sessionid的SessionProfile
				SessionProfile sessp = null;
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
		
				//执行SessionProfile的setPath
				if (sessp != null) {
					//sessp.setPath(newValue);
		
					/*************
		LogMgr.getLogger(this.getClass().getName()).info("--setPath() ok--");
		/*************
		
		}
		* /
		/*************/
		LogMgr.getLogger(this.getClass().getName()).info(
			"该PlugIn被调用....");
		/*************/
	}
}
