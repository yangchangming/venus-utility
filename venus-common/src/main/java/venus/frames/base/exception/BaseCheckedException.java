/*
 * 创建日期 2005-1-4
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package venus.frames.base.exception;

import org.springframework.core.NestedCheckedException;
import venus.frames.base.IGlobalsKeys;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

/**
 * @author sundaiyong
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class BaseCheckedException extends NestedCheckedException implements IGlobalsKeys {
	protected final ILog logger = LogMgr.getLogger(this);

	/**
	 * @param msg
	 */
	public BaseCheckedException(String msg) {
		super(msg);
		logger.warn(msg);
	}

	/**
	 * @param msg
	 * @param ex
	 */
	public BaseCheckedException(String msg, Throwable ex) {
		super(msg, ex);
		logger.warn(msg, ex);
	}

}
