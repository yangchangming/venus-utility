package venus.frames.mainframe.currentlogin;

import venus.frames.mainframe.log.LogMgr;

import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;

/**
 * 实现IProfilePlugIn接口的插件，由ProfileMgr调用，完成WEB端系统数据堆的存储
 */

public class DefaultSysAppProfile implements IProfilePlugIn {

	/**
	 * 缺省构造器
	 */
	public DefaultSysAppProfile() {

	}

	/**
	 * 创建该用户的 Profile 时运行该插件的该方法
	 * 
	 * 暂时不实现
	 * 
	 * @param sessionid - 当前连接的sessionid
	 * @param loginName - 该用户的登录名
	 * @param ht - 传入用来存储该用户数据
	 * @return Hashtable - 存储该用户数据
	 * @roseuid 3FBA0988001F
	 */
	public Hashtable onCreateProfileForLoginer(
		String sessionid,
		String loginName,
		Hashtable ht,
		HttpServletRequest req) {

		/***************/
		LogMgr.getLogger(this.getClass().getName()).info(
			"defaultAppWebProfile onCreateProfileForLoginer is called");
		/***************/

		if (ht == null) {
			ht = new Hashtable();
		}
		//返回Hashtable
		return ht;
	}

	/**
	 * 删除该连接的SessionProfile 时运行该插件的该方法
	 * 
	 * 暂时不实现
	 * 
	 * @param sessionid - 当前连接的sessionid
	 * @param loginName - 该用户的登录名
	 * @return boolean
	 * @roseuid 3FBA0988002E
	 */
	public boolean onEraseSessionProfile(String sessionid, String loginName) {
		return true;
	}
}
