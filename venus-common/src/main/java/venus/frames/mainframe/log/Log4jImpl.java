//Source file: D:\\venus_view\\Tech_Department\\Platform\\Venus\\4项目开发\\1工作区\\4实现\\venus\\frames\\mainframe\\log\\DefaultLogImpl.java

package venus.frames.mainframe.log;

import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * 封装了LOG4J的LOG驱动实现类
 * 
 * 1.全局单实例singleton模式调用,先静态调用 
 * LogMgr.getLogger()方法获取该类实例，然后可以通过该实例进行LOG
 * 
 * 2.类 DefaultLogImpl仅为缺省实现的LOG类（封装了LOG4J），如果不满意该实现，
 * 可以重写LOG类（遵循ILogger规定的方法）
 * 
 * @author 岳国云
 */
public class Log4jImpl implements ILogger, Serializable {

	/**
	 * 封装的 LOG4j 的实例
	 */
	private Logger m_log = null;

	/**
	 * 构造器
	 * 
	 * @roseuid 3F4489D10399
	 */
	public Log4jImpl() {
		super();
	}

	public boolean isDebugEnabled() {
		return m_log.isDebugEnabled();
	}
	
	public boolean isInfoEnabled() {
		return m_log.isInfoEnabled();
	}
	
	/**
	 * 记录调试信息
	 * 
	 * @param message - 传入的日志消息
	 * @return void
	 * @roseuid 3F448BF60379
	 */
	public void debug(Object message) {
		if (m_log != null) {
			if (m_log.isDebugEnabled())
				m_log.debug(message);
		}
	}

	/**
	 * 记录一般信息
	 * 
	 * @param message - 传入的日志消息
	 * @return void
	 * @roseuid 3F448BF60389
	 */
	public void info(Object message) {
		if (m_log != null) {
			if (m_log.isInfoEnabled())
				m_log.info(message);
		}
	}

	/**
	 * 记录警告信息
	 * 
	 * @param message - 传入的日志消息
	 * @return void
	 * @roseuid 3F448BF6038B
	 */
	public void warn(Object message) {
		if (m_log == null) {
			getLogger(this).warn("warn（）：日志实现类实例为空");
			return;
		}
		m_log.warn(message);
	}

	/**
	 * 记录错误信息
	 * 
	 * @param message - 传入的日志消息
	 * @return void
	 * @roseuid 3F448BF60399
	 */
	public void error(Object message) {

		if (m_log == null) {
			getLogger(this).warn("error（）：日志实现类实例为空");
			return;
		}
		m_log.error(message);
	}

	/**
	 * 记录严重错误信息
	 * 
	 * @param message - 传入的日志消息
	 * @return void
	 * @roseuid 3F448BF603A8
	 */
	public void fatal(Object message) {

		if (m_log == null) {
			getLogger(this).warn("fatal（）：日志实现类实例为空");
			return;
		}

		m_log.fatal(message);
	}

	/**
	 * 按 nLevel 定义的级别（该级别即 DEBUG_INT定义的）
	 * 
	 * 按级别记录系统日志信息
	 * 
	 * 日志的级别在ILog中定义为常量，分为：
	 * 调试信息 - DEBUG_INT = 1
	 * 一般信息 - INFO_INT = 2
	 * 警告信息 - WARN_INT = 3
	 * 错误信息 - ERROR_INT = 4
	 * 严重错误信息 - FATAL_INT = 5
	 * 
	 * @param nLevel - 日志的级别
	 * @param message - 传入的日志消息
	 * @return void
	 * @roseuid 3F448BF603B8
	 */
	public void log(int nLevel, Object message) {

		if (nLevel == ILogger.DEBUG_INT
			|| nLevel == ILogger.INFO_INT
			|| nLevel == ILogger.WARN_INT
			|| nLevel == ILogger.ERROR_INT
			|| nLevel == ILogger.FATAL_INT) {
			switch (nLevel) {
				//记录调试日志信息
				case ILogger.DEBUG_INT :
					debug(message);
					break;
					//记录一般信息
				case ILogger.INFO_INT :
					info(message);
					break;
					//记录警告信息
				case ILogger.WARN_INT :
					warn(message);
					break;
					//记录错误信息
				case ILogger.ERROR_INT :
					error(message);
					break;
					//记录严重错误信息
				case ILogger.FATAL_INT :
					fatal(message);
					break;
			}
		} else {
			LogMgr.getLogger(this).warn("log(...):输入的级别不符合规定");
		}
	}

	/**
	 * 设置记录日志类的类名
	 * 
	 * @param clsname - 设置记录日志的调用者的名字
	 * @return void 
	 * @roseuid 3F7257890186
	 */
	public void setClassName(String clsname) {

		if (clsname == null || clsname == "") {
			getLogger(this.getClass().getName()).error("调用者名字不可以为空");
			return;
		}
		m_log = Logger.getLogger(clsname);
	}

	/**
	 * 为便于用户使用提供的一个简单代理接口
	 * 
	 * 得到 LOG 驱动实例
	 * 
	 * @param obj - 记录日志的调用者自身句柄
	 * @return venus.frames.mainframe.log.ILog - 本对象实例
	 * @roseuid 3F725BFC00EA
	 */
	public ILog getLogger(Object obj) {
		return LogMgr.getLogger(obj);
	}

	/**
	 * 记录调试信息和抛出的异常诱因
	 * 
	 * @param message - 传入的日志消息
	 * @param t - 传入的异常诱因
	 * @return void
	 * @roseuid 3F8E75250131
	 */
	public void debug(Object message, Throwable t) {
		if (m_log != null) {
			if (m_log.isDebugEnabled())
				m_log.debug(message, t);
		}
	}

	/**
	 * 记录一般信息和抛出的异常诱因
	 * 
	 * @param message  - 传入的日志消息
	 * @param t  - 传入的异常诱因
	 * @return void
	 * @roseuid 3F8E75320298
	 */
	public void info(Object message, Throwable t) {
		if (m_log != null) {
			if (m_log.isInfoEnabled())
				m_log.info(message, t);
		}
	}

	/**
	 * 记录警告信息和抛出的异常诱因
	 * 
	 * @param message - 传入的日志消息
	 * @param t - 传入的异常诱因
	 * @return void
	 * @roseuid 3F8E753501AE
	 */
	public void warn(Object message, Throwable t) {
		if (m_log == null) {
			getLogger(this).warn("warn（）：日志实现类实例为空");
			return;
		}
		m_log.warn(message, t);
	}

	/**
	 * 记录错误信息和抛出的异常诱因
	 * 
	 * @param message - 传入的日志消息
	 * @param t - 传入的异常诱因
	 * @return void
	 * @roseuid 3F8E753903A2
	 */
	public void error(Object message, Throwable t) {
		if (m_log == null) {
			getLogger(this).warn("error（）：日志实现类实例为空");
			return;
		}
		m_log.error(message, t);
	}

	/**
	 * 按 nLevel 定义的级别（该级别即 DEBUG_INT定义的）
	 * 
	 * 按级记录系统日志信息和抛出的异常诱因
	 * 
	 * 日志的级别在ILog中定义为常量，分为：
	 * 调试信息 - DEBUG_INT = 1
	 * 一般信息 - INFO_INT = 2
	 * 警告信息 - WARN_INT = 3
	 * 错误信息 - ERROR_INT = 4
	 * 严重错误信息 - FATAL_INT = 5
	 * 
	 * @param nLevel - 日志的级别
	 * @param message - 传入的日志消息
	 * @param t - 传入的异常诱因
	 * @return void
	 * @roseuid 3F8E753D0288
	 */
	public void log(int nLevel, Object message, Throwable t) {

		//按级记录系统日志信息和抛出的异常诱因
		if (nLevel == ILogger.DEBUG_INT
			|| nLevel == ILogger.INFO_INT
			|| nLevel == ILogger.WARN_INT
			|| nLevel == ILogger.ERROR_INT
			|| nLevel == ILogger.FATAL_INT) {
			switch (nLevel) {
				case ILogger.DEBUG_INT :
					debug(message, t);
					break;

				case ILogger.INFO_INT :
					info(message, t);
					break;

				case ILogger.WARN_INT :
					warn(message, t);
					break;

				case ILogger.ERROR_INT :
					error(message, t);
					break;

				case ILogger.FATAL_INT :
					fatal(message, t);
					break;
			}
		} else {
			LogMgr.getLogger(this).warn("log(...): 输入的级别不符合规定");
		}
	}

	/**
	 * 记录严重错误信息和抛出的异常诱因
	 * 
	 * @param message - 传入的日志消息
	 * @param t  - 传入的异常诱因
	 * @return void
	 * @roseuid 3F8E75410344
	 */
	public void fatal(Object message, Throwable t) {

		if (m_log == null) {
			getLogger(this).warn("fatal():日志实现类实例为空");
			return;
		}
		m_log.fatal(message, t);
	}

	/**
	 * 得到LOG驱动实现类的名字
	 * 
	 * @return String LOG驱动实现类的名字
	 * @roseuid 3F94938E00F2
	 */
	public String getImplName() {
		return this.getClass().getName();
	}

}
