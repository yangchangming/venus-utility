package venus.pub.util;

import venus.frames.mainframe.exception.BaseException;

/**
 * RPC异常类<br>
 * 
 * 主要记录异常信息<br>
 * 
 * 本方法中将 RemoteCallException 传入的 MSG 为类中约定编码
 */
public class UtilException extends BaseException {

	/**
	 * 异常中的错误编码
	 */
	private String m_strCode = null;

		
	public UtilException(String str) {
		super(str);
		this.m_strCode = str;
	}
	
	public UtilException(String str,Throwable t) {
		super(str,t);
		this.m_strCode = str;
	}

	/**
	 * 得到错误编码
	 */
	public String getExceptionCode() {
		return this.m_strCode;
	}

	/**
	 * 设置错误编码
	 */
	public void setExceptionCode(String code) {
		this.m_strCode = code;
	}

}
