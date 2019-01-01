package venus.frames.mainframe.currentlogin;

import venus.frames.mainframe.log.LogMgr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Hashtable;

/**
 * 实现IProfilePlugIn接口的插件，由ProfileMgr调用，完成WEB端系统数据堆的存储
 */

public class DefaultSysWebProfile implements IProfilePlugIn {

	/**
	 * @roseuid 3FBA0E5B033C
	 */
	public DefaultSysWebProfile() {

	}

	/**
	 * 取得该用户的
	 * 
	 * SimpleProfile.SYS_LOGIN_NAME
	 * SimpleProfile.SYS_LOGIN_PASSWARD
	 * SimpleProfile.SYS_LOGOUT_TIME
	 * 
	 * 并以这些为Key 存入Hashtable
	 * @param sessionid - 当前连接的sessionid
	 * @param loginName - 该用户的登录名
	 * @param ht - 传入用来存储该用户数据
	 * @return Hashtable - 存储该用户数据
	 * @roseuid 3FBA080201B5
	 */
	public Hashtable onCreateProfileForLoginer(String sessionid, String loginName, Hashtable ht, HttpServletRequest req) {

		/***************/
		LogMgr.getLogger(this.getClass().getName()).info(
			"defaultSysWebProfile onCreateProfileForLoginer is called");
		/***************/

		Hashtable hts = null;
		if (ht != null)
			hts = ht;
		else
			hts = new Hashtable();

		if (req != null) {
			HttpServletRequest request = req;
			HttpSession session = request.getSession(true);

			//把当前req的用户信息放入Hashtable
			if (session.getAttribute(SimpleProfile.SYS_LOGIN_NAME) != null)
				hts.put(
					SimpleProfile.SYS_LOGIN_NAME,
					session.getAttribute(SimpleProfile.SYS_LOGIN_NAME));
		}

		//返回Hashtable
		return hts;
	}

	/**
	 * 删除该连接的 SessionProfile 时运行该插件的该方法
	 * 
	 * 暂时不实现
	 * 
	 * @param sessionid - 当前连接的sessionid
	 * @param loginName - 该用户的登录名
	 * @return boolean
	 * @roseuid 3FBA080B004E
	 */
	public boolean onEraseSessionProfile(String sessionid, String loginName) {
		return true;
	}
}
