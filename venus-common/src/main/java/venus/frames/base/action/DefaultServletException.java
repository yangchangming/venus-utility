package venus.frames.base.action;

import venus.frames.mainframe.exception.BaseException;

/**
 * 缺省 Servlet 层抛出的异常类
 * 
 * @author 岳国云
 */
public class DefaultServletException extends BaseException {
	
	/**
	 * 错误消息编码
	 */
	public static String STOP_CODE = "STOP_CODE";

	/**
	 * 缺省构造器
	 * @roseuid 3FAA2D94029F
	 */
	public DefaultServletException() {
		super();
	}

	/**
	 * 根据传入消息编码对象构造异常对象
	 * 
	 * @param code - 传入的消息编码
	 */
	public DefaultServletException(String code) {
		super(code);
	}

	/**
	 * 根据传入诱因对象和消息对象构造异常对象
	 * 
	 * @param code - 传入的消息编码
	 * @param msg - 传入的消息对象
	 * @param cause  - 传入的诱因对象
	 */
	public DefaultServletException(String code, String msg, Throwable cause) {

		super(code, msg, cause);
	}

	/**
	 * 根据传入消息编码和诱因对象构造异常对象
	 * @param code - 传入的消息编码
	 * @param cause  - 传入的诱因对象
	 */
	public DefaultServletException(String code, Throwable cause) {
		super(code, cause);
	}

	/**
	 * 根据传入诱因对象构造异常对象
	 * 
	 * @param cause - 传入的诱因对象
	 * 
	 */
	public DefaultServletException(Throwable cause) {
		super(cause);
	}

}
