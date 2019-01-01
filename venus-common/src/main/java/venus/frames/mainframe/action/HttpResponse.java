package venus.frames.mainframe.action;

import venus.frames.base.action.IForward;
import venus.frames.base.action.IResponse;
import venus.frames.mainframe.log.LogMgr;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 对 javax.servlet.HttpServletResponse 的一个实例的一个包装类
 * 
 * 继承于ServletResponseWrapper
 * 
 * 该类的实例同 HttpServletResponse的一个实例 一一对应
 * 
 * 同时该类实现 Response 接口
 * 
 * @author 岳国云
 */
public class HttpResponse extends HttpServletResponseWrapper implements IResponse {

	/**
	 * 用于存储 响应对象中的目标页面
	 */
	private IForward m_forward = null;

	/**
	 * 存储响应对象中的需返回的错误对象列表
	 */
	private Errors m_errs = null;

	/**
	 * 根据传入的响应对象来构造本对象
	 * @param response - 传入已有的响应对象
	 * @roseuid 3FA5DCE103A9
	 */
	public HttpResponse(HttpServletResponse response) {
		super(response);
	}

	/**
	 * 返回该响应对象转向的目标页面
	 * 
	 * @return venus.frames.mainframe.base.action.IForward - 该响应对象转向的目标页面
	 * @roseuid 3FAD1E6F02E1
	 */
	public IForward getForward() {
		return this.m_forward;
	}

	/**
	 * 返回该响应对象所包装的 ServletResponse
	 * 
	 * @return ServletResponse - 该响应对象所包装的 ServletResponse
	 * @roseuid 3FAD1E6F0313
	 */
	public ServletResponse getServletResponse() {
		return this;
	}

	/**
	 * 向响应对象中设置需返回的错误对象列表
	 * 
	 * @param errs - 返回的错误信息
	 * @roseuid 3FAD1E6F0359
	 */
	public void sendErrors(Errors errs) {
		
		if (errs == null) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"sendErrors(...):参数为空!");
		} else {
			this.m_errs = errs;
			return;
		}
	}

	/**
	 * 得到返回的错误对象列表
	 * 
	 * @return venus.frames.mainframe.base.action.Errors - 错误对象列表
	 * @roseuid 3FAE26900360
	 */
	public Errors getErrors() {
		return this.m_errs;
	}

	/**
	 * 向响应对象中设置需返回的目标页面
	 * 
	 * @param forward - 要设置的响应对象中的目标页面
	 * @roseuid 3FAE277902ED
	 */
	public void setForward(IForward forward) {
		
		if (forward == null) {
			LogMgr.getLogger(this.getClass().getName()).error(
				"setForward() : the parameter is null!");
		} else {
			this.m_forward = forward;
			return;
		}
	}
}
