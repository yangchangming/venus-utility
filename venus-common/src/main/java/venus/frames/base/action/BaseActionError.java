package venus.frames.base.action;

import org.apache.struts.action.ActionError;

import java.io.Serializable;

/**
 * 错误基类，与 Exception 不同的是错误主要是为提示用户而设置的类
 * 她主要存储错误编码，错误信息数据等。
 */
public class BaseActionError extends ActionError implements Serializable {

	/**
	 * 用于存储该错误的错误编码
	 */
	private String m_strCode = "";

	/**
	 * 用于存储该错误的显示消息
	 */
	private String m_strMsg = "";

	/**
	 * 用于存储该错误的错误诱因
	 */
	private Throwable m_thrwCause = null;

	/**
	 * 缺省错误编码
	 */
	public static String BASE_ERR_CODE = "BASE_ERR_CODE";

	/**
	 * 构造器
	 * 
	 * 根据传入的 错误编码 和 信息参数 构建错误对象
	 * 
	 * -------------code sample---------------------------
	 * if (key == null || key.length() == 0) key = BASE_ERR_CODE;
	 * this.m_strCode = key;
	 * super(key,value0,value1);
	 * @param key
	 * @param value0
	 * @param value1
	 * @roseuid 3FAE49B700DF
	 */
	public BaseActionError(String key, Object value0, Object value1) {
		//调用父类方法，如果key为空，则使用缺省错误编码为参数
		super(
			(key == null || key.length() == 0) ? BASE_ERR_CODE : key,
			value0,
			value1);
		//存储该错误的错误编码，如果key为空，则存储缺省错误编码
		if (key == null || key.length() == 0)
			key = BASE_ERR_CODE;
		this.m_strCode = key;
	}

	/**
	 * 构造器
	 * 
	 * 根据传入的错误编码和信息参数构建错误对象
	 * 
	 * -------------code sample---------------------------
	 * if (key == null || key.length() == 0) key = BASE_ERR_CODE;
	 * this.m_strCode = key;
	 * super(key,value0,value1,value2);
	 * @param key
	 * @param value0
	 * @param value1
	 * @param value2
	 * @roseuid 3FAE49520225
	 */
	public BaseActionError(String key, Object value0, Object value1, Object value2) {
		//调用父类方法，如果key为空，则使用缺省错误编码为参数
		super(
			(key == null || key.length() == 0) ? BASE_ERR_CODE : key,
			value0,
			value1,
			value2);
		//存储该错误的错误编码，如果key为空，则存储缺省错误编码
		if (key == null || key.length() == 0)
			key = BASE_ERR_CODE;
		this.m_strCode = key;
	}

	/**
	 * 构造器
	 * 
	 * 根据传入的 错误编码 和 信息参数 构建错误对象
	 * 
	 * -------------code sample---------------------------
	 * if (key == null || key.length() == 0) key = BASE_ERR_CODE;
	 * this.m_strCode = key;
	 * super(key,value0,value1,value2,value3);
	 * @param key
	 * @param value0
	 * @param value1
	 * @param value2
	 * @param value3
	 * @roseuid 3FAE485901B8
	 */
	public BaseActionError(
		String key,
		Object value0,
		Object value1,
		Object value2,
		Object value3) {
		//调用父类方法，如果key为空，则使用缺省错误编码为参数
		super(
			(key == null || key.length() == 0) ? BASE_ERR_CODE : key,
			value0,
			value1,
			value2,
			value3);
		//存储该错误的错误编码，如果key为空，则存储缺省错误编码
		if (key == null || key.length() == 0)
			key = BASE_ERR_CODE;
		this.m_strCode = key;
	}

	/**
	 * 构造器
	 * 
	 * 根据传入的 错误编码 构建错误对象
	 * 
	 * -------------code sample---------------------------
	 * if (key == null || key.length() == 0) key = BASE_ERR_CODE;
	 * this.m_strCode = key;
	 * super(key);
	 * @param key
	 * @roseuid 3FAE46D00019
	 */
	public BaseActionError(String key) {

		//调用父类方法，如果key为空，则使用缺省错误编码为参数
		super((key == null || key.length() == 0) ? BASE_ERR_CODE : key);

		//存储该错误的错误编码，如果key为空，则存储缺省错误编码
		if (key == null || key.length() == 0)
			key = BASE_ERR_CODE;
		this.m_strCode = key;
	}

	/**
	 * 构造器
	 * 
	 * 根据传入的 错误编码，错误消息，构建错误对象
	 * 
	 * -------------code sample---------------------------
	 * if (code == null || code.length() == 0) code = BASE_ERR_CODE;
	 * this.m_strCode = code;
	 * this.m_strMsg = msg.toString();
	 * super(code,msg);
	 * @param code - 错误编码
	 * @param msg - 错误提示的消息
	 * @roseuid 3F93BDDA0385
	 */
	public BaseActionError(String code, Object msg) {
		
		//调用父类方法，如果key为空，则使用缺省错误编码为参数
		super((code == null || code.length() == 0) ? BASE_ERR_CODE : code, msg);
		
		if ( msg == null ){
			return;	    
	    }
		
		//存储该错误的错误编码，如果key为空，则存储缺省错误编码
		if (code == null || code.length() == 0)
			code = BASE_ERR_CODE;
		this.m_strCode = code;
		this.m_strMsg = msg.toString();

	}

	/**
	 * 构造器
	 * 
	 * 根据传入的错误诱因，构建错误对象
	 * 
	 * -------------code sample---------------------------
	 * this.m_strCode = BASE_ERR_CODE;
	 * this.m_strMsg = cause.toString();
	 * this.m_thrwCause = cause;
	 * super(m_strCode,m_strMsg,cause);
	 * @param cause - 错误的引起者
	 * @roseuid 3F93BDD70029
	 */

	public BaseActionError(Throwable cause) {
	    	    
		super(BASE_ERR_CODE,(cause == null)? null : cause.toString(), cause);
				
	    if ( cause == null ){
			this.m_strCode = BASE_ERR_CODE;
			return;	    
	    }
	    
		//存储错误诱因
		this.m_thrwCause = cause;
		
		this.m_strCode = BASE_ERR_CODE;
		this.m_strMsg = cause.toString();
	}

	/**
	 * 构造器
	 * 
	 * 根据传入的 错误编码，错误消息，错误诱因，构建错误对象
	 * 
	 * -------------code sample---------------------------
	 * if (code == null || code.length() == 0) 
	 *     code = BASE_ERR_CODE;
	 * this.m_strCode = code;
	 * this.m_strMsg = msg;
	 * this.m_thrwCause = cause;
	 * super(code,msg,cause);
	 * @param cause - 错误的引起者
	 * @param code - 错误编码
	 * @param msg - 错误提示的消息
	 * @roseuid 3F93BDCC0123
	 */

	public BaseActionError(Throwable cause, String key, String msg) {
		//调用父类方法，如果key为空，则使用缺省错误编码为参数
		super((key == null || key.length() == 0) ? BASE_ERR_CODE : key, msg, cause);
		//存储该错误的错误编码，如果key为空，则存储缺省错误编码
		if (key == null || key.length() == 0)
			key = BASE_ERR_CODE;
		this.m_strCode = key;
		
		this.m_strMsg = msg;
		this.m_thrwCause = cause;
	}

	/**
	 * 返回错误对象的显示消息
	 * 
	 * --------------------code sample-------------
	 * //如果在错误资源文件中未找到m_strCode对应的错误信息则返回：
	 * return this.m_strMsg;
	 * @return String
	 * @roseuid 3FA70DE200E9
	 */
	public String getMessage() {
		return this.m_strMsg;
	}

	/**
	 * 该方法主要是支持多语言，目前转向给 getMessage();
	 * 
	 * 以后实现时根据不同的系统语言给予不同提示信息
	 * 
	 * --------------------code sample---------------------------
	 * 
	 * return getMessage();
	 * @return String
	 * @roseuid 3FA70DEB006C
	 */
	public String getLocalMessage() {
		return getMessage();
	}

	/**
	 * 返回存储该错误的错误编码
	 * 
	 * --------------------code sample-------------
	 * return this.m_strCode;
	 * @return String
	 * @roseuid 3FA70DF502BE
	 */
	public String getCode() {
		return this.m_strCode;		
	}
	
	
	
}
