/*
 * Created on 2004-11-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package venus.frames.base.exception;

import org.springframework.remoting.RemoteAccessException;
import venus.frames.base.IGlobalsKeys;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

/**
 * @author 孙代勇
 * 
 * 远程调用异常基类类,所有的远程调用异常类都从此类派生
 */
public class BaseRemoteAccessException extends RemoteAccessException  implements IGlobalsKeys {
	
	protected final ILog logger = LogMgr.getLogger(this);
	/**
	 * @param msg
	 *            消息
	 * @param ex
	 *            异常,通常是导致异常的原因
	 */
	public BaseRemoteAccessException(String msg, Throwable ex) {
		super(msg, ex);
		logger.warn(msg, ex);
	}

	/**
	 * @param msg
	 *            异常消息
	 */
	public BaseRemoteAccessException(String msg) {
		super(msg, null);
		logger.warn(msg);
	}


	
}