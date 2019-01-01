package venus.frames.mainframe.actionevent.bs;

/**
 * @author wujun
 */
public interface IActionEventBS {
	/**
	 * 构建事件类型方法，创建当前用户数据堆，调用创建类型插件
	 * @param sessionid - 当前用户的sessionid号
	 * @param liginName - 当前用户名
	 * @return void
	 * @roseuid 3FBAE2CF033B
	 */
	public abstract void create(String sessionid, String loginName);

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
	public abstract void changeAction(String sessionid, String oldState,
									  String oldValue, String newValue);

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
	public abstract void changePath(String sessionid, String oldState,
									String oldValue, String newValue);

	/**
	 * 调用销毁插件，调用销毁类型插件
	 * 
	 * @param sessionid - 要销毁的用户的sessionid号
	 * @param oldState  - 要销毁的用户名
	 * @return viod
	 * @roseuid 3FBAE4C801B4
	 */
	public abstract void erase(String sessionid, String oldState);
}