package venus.frames.mainframe.exception;

import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.ClassLocator;

/**
 * 异常基类
 * 
 * 执行记录系统日志等操作
 */
public class BaseException extends Exception implements java.io.Serializable {

	/**
	 * 是否存在诱因标识
	 */
	private boolean m_bHaveCause = false;

	/**
	 * 存储错误消息
	 */
	private String m_strCode = null;

	/**
	 * 异常诱因
	 */
	private Throwable cause = null;

	/**
	 * 缺省构造器
	 * 
	 * 并执行handler()处理方法，可以是记录系统日志，也可以是被子类重载的内容
	 * 
	 * @param msg - 
	 * @roseuid 3F8E598D0296
	 */
	public BaseException() {
		super();
		this.handler();
	}

	/**
	 * 根据传入消息编码对象构造异常对象
	 * 
	 * 然后执行handler()处理方法，可以是记录系统日志，也可以是被子类重载的内容
	 * @param code - 传入的消息编码
	 * @roseuid 3F8E595101BC
	 */
	public BaseException(String code) {
		super();
		this.m_strCode = code;
		this.handler();
	}

	/**
	 * 根据传入诱因对象构造异常对象
	 * 
	 * 如果传入的诱因对象非空 置位 "是否存在诱因的标识"
	 * 
	 * 并执行handler()处理方法，可以是记录系统日志，也可以是被子类重载的内容
	 * 
	 * @param cause - 传入的诱因对象
	 * @roseuid 3F8E4D8A00F5
	 */
	public BaseException(Throwable cause) {

		this.cause = cause;
		//如果传入的诱因对象非空 置位 "是否存在诱因的标识"
		if (cause != null)
			this.m_bHaveCause = true;
		this.handler();
	}

	/**
	 * 构造器
	 * 
	 * 根据传入诱因对象和消息对象构造异常对象
	 * 
	 * 如果传入的诱因对象非空置位"是否存在诱因的标识"
	 * 
	 * 并执行handler()处理方法，可以是记录系统日志，也可以是被子类重载的内容
	 * 
	 * @param code - 传入的异常编码
	 * @param msg - 传入的消息对象
	 * @param cause - 传入的诱因对象
	 * @roseuid 3F8E4D59024D
	 */
	public BaseException(String code, String msg, Throwable cause) {

		super(msg);
		this.m_strCode = code;
		this.cause = cause;

		//如果传入的诱因对象非空置位"是否存在诱因的标识"
		if (cause != null)
			this.m_bHaveCause = true;
		this.handler();
	}

	/**
		 * 构造器
		 * 
		 * 根据传入诱因对象和消息对象构造异常对象
		 * 
		 * 如果传入的诱因对象非空置位"是否存在诱因的标识"
		 * 
		 * 并执行handler()处理方法，可以是记录系统日志，也可以是被子类重载的内容
		 * 
		 * @param code - 传入的异常编码
		 * @param cause - 传入的诱因对象
		 * @roseuid 3F8E4D59024D
		 */
	public BaseException(String code, Throwable cause) {

		super();
		this.m_strCode = code;

		//如果传入的诱因对象非空 置位 "是否存在诱因的标识"
		this.cause = cause;
		if (cause != null)
			this.m_bHaveCause = true;
		this.handler();
	}

	/**
	 * 得到错误消息
	 * @return String - 错误消息
	 */
	public String getMessage() {

		return m_strCode + ":" + super.getMessage();
	}
	/**
	 * in this method you can write your code to handle
	 * this exception
	 * 
	 * in this method, we only do one thing :just todo log.and log level is warn 
	 * 
	 * "log( Ilog.WARN_INT )"
	 * 
	 * if you want log more or do other more thing,you can override it. 
	 * 
	 * @return void 
	 * @roseuid 3F8E5C300179
	 */
	protected void handler() {
		log(ILog.WARN_INT);
	}

	/**
	 * in this method just log, so simple:
	 * 
	 * only to see if "m_bHaveCause" is true
	 * 
	 * 该类需要做简单判断LOG类是否存在，如果不存在不记日志
	 * 
	 * @param logLevel - 日志的级别
	 * @return void 
	 * @roseuid 3F8E5CAB0233
	 */
	private void log(int logLevel) {

		//标识是否记录日志，为true则记录，否则不记
		boolean isLog = true;

		//判断日志工厂类venus.frames.mainframe.log.LogMgr是否存在，
		//不存在则置之不理isLog为false,此try{}catch()块只做一个开关处理
		try {
			ClassLocator.loadClass("venus.frames.mainframe.log.LogMgr");
		} catch (ClassNotFoundException cfe) {
			isLog = false;
		}

		if (isLog) {
			//得到ILog实例
			ILog logger = LogMgr.getLogger(this.getClass().getName());

			//如果存在诱因，则一同记录诱因信息
			if (m_bHaveCause) {
				logger.log(logLevel, this.getLocalizedMessage(), this.getCause());
				//记录日志信息
			} else {
				logger.log(logLevel, this.getLocalizedMessage());
			}
		}
		return;
	}

	/**
	 * 重载父类方法，返回异常消息
	 * @return String - 异常消息
	 */

	public String getLocalizedMessage() {
		return this.getMessage();
	}

	/**
	 * Method getCause.
	 * @return Throwable - 异常诱因对象
	 */
	public Throwable getCause() {
		return this.cause;
	}

	/**
	 * 得到异常编码
	 * @return String - 异常编码
	 */
	public String getCode() {
		return this.m_strCode;

	}

}
