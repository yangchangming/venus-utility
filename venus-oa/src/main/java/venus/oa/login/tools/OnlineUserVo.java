package venus.oa.login.tools;

import venus.oa.loginlog.vo.LoginLogVo;

import javax.servlet.http.HttpSession;


/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */

public class OnlineUserVo extends LoginLogVo {
    
	private String session_id;
	private transient HttpSession userSession;
	private boolean isAdmin;

	/**
	 * @return 返回 session_id。
	 */
	public String getSession_id() {
		return session_id;
	}
	/**
	 * @param session_id 要设置的 session_id。
	 */
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	
	/**
	 * @return 返回 userSession。
	 */
	public HttpSession getUserSession() {
		return userSession;
	}
	/**
	 * @param userSession 要设置的 userSession。
	 */
	public void setUserSession(HttpSession userSession) {
		this.userSession = userSession;
	}
	
	
	/**
	 * @return 返回 isAdmin。
	 */
	public boolean getIsAdmin() {
		return isAdmin;
	}
	/**
	 * @param isAdmin 要设置的 isAdmin。
	 */
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}	

