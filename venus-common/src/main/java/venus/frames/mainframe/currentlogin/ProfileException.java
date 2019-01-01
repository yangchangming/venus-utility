//Source file: D:\\venus_view\\Tech_Department\\Platform\\Venus\\4项目开发\\1工作区\\4实现\\venus\\frames\\mainframe\\base\\currentlogin\\ProfileException.java

package venus.frames.mainframe.currentlogin;

import venus.frames.mainframe.exception.BaseException;

public class ProfileException extends BaseException {

	/**
	 * 为空错误标识，用于得到用户数据堆时的空异常
	 */
	public static String NULL_CODE = "NULL_CODE";

	/**
	 * 构造函数
	 * @roseuid 3FBA0D750128
	 */
	public ProfileException() {
		super();
	}

	/**
	 * 构造函数，根据传入消息编码对象构造异常对象
	 * @param code - 传入的消息编码
	 */
	public ProfileException(String code) {
		super(code);
	}

	/**
	 * 根据传入诱因对象构造异常对象
	 * 
	 * @param cause - 传入的诱因对象
	 */
	public ProfileException(Throwable cause) {
		super(cause);
	}

	/**
	 * 构造函数，根据传入消息编码和诱因对象构造异常对象
	 * 
	 * @param code - 传入的消息编码
	 * @param cause  - 传入的诱因对象
	 */
	public ProfileException(String code, Throwable cause) {
		super(code, cause);
	}

	/**
	 * 构造函数，根据传入诱因对象和消息对象构造异常对象
	 * 
	 * @param code - 传入的消息编码
	 * @param msg - 传入的消息对象
	 * @param cause  - 传入的诱因对象
	 */
	public ProfileException(String code, String msg, Throwable cause) {
		super(code, msg, cause);
	}

}
