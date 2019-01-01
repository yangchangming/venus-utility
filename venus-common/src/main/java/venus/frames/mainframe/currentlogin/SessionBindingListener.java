//Source file: D:\\venus_view\\Tech_Department\\Platform\\Venus\\4项目开发\\1工作区\\4实现\\venus\\frames\\mainframe\\base\\currentlogin\\SessionProfile.java

package venus.frames.mainframe.currentlogin;

import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.io.Serializable;

/**
 * HttpSession Listener for profile
 * @author wujun
 */

public class SessionBindingListener implements HttpSessionBindingListener,Serializable  {


	/**
	 * 存储此次连接的 sessionid
	 */
	private String m_strSessionId = null;

	/**
	 * 用于存储该 PROFILE 所处的位置 WEB 还是 app 
	 */
	private String m_strLoc = null;

	public SessionBindingListener(String sessionid,String loc){
		
		this.m_strLoc = loc;
		this.m_strSessionId = sessionid;
		
	}


	/**
	 * Session事件监听器的方法，传入Session的是一个监听器对象，
	 * 则在传入的时候会调用此方法
	 * 
	 * @param event - HttpSessionBindingEvent对象
	 * @roseuid 3FB9F65401A5
	 */
	public void valueUnbound(HttpSessionBindingEvent event) {

		/*******/
		getIlog().info("valueUnbound(...).........");
		/*******/

		if (m_strLoc != null
			&& m_strSessionId != null){			//&& ( (event.getSession() == null) || (event.getSession().getId() != null && m_strSessionId != event.getSession().getId() )) ) {

			getIlog().info("valueUnbound() : ProfileMgr.eraseProfile(...) ...");

			ProfileMgr.eraseProfile(m_strLoc, m_strSessionId);

		}
	}

	/**
	 * Session监听器的方法，不需要实现
	 * @param httpsessionbindingevent - 
	 * @roseuid 3FBA0C900186
	 */
	public void valueBound(HttpSessionBindingEvent httpsessionbindingevent) {

		getIlog().info("valueBound(...).........");

	}

	/***
	 * 得到日志记录实例
	 * 
	 * @return ILog
	 */
	public ILog getIlog() {

		return LogMgr.getLogger(this.getClass().getName());
	}
	
}
