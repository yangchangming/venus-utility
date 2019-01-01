package venus.frames.base.bean;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import venus.frames.base.action.IMappingCfg;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.DefaultMapping;
import venus.frames.mainframe.action.Errors;
import venus.frames.mainframe.action.HttpRequest;

import javax.servlet.http.HttpServletRequest;

public class DefaultForm extends ActionForm implements java.io.Serializable{

	/**
	 * 继承于 struts 的方法
	 * 
	 * 转调自己的方法 validate( new DefaultMapping( actionMapping ),request )
	 * @param mapping - 该对象对应的配置数据对象
	 * @param request - HttpServletRequest请求对象
	 * @return ActionErrors - 该验证返回的错误对象列表
	 * @roseuid 3F6817AE006C
	 */
	public ActionErrors validate(
		ActionMapping mapping,
		HttpServletRequest request) {
		
		if ( request instanceof IRequest){
			return validate(new DefaultMapping(mapping), (IRequest)request);
		}else{
			return validate(new DefaultMapping(mapping), new HttpRequest(request));
		}
		
	}

	/**
	 * 继承于 struts 的方法
	 * 
	 * 转调自己的方法 reset( new DefaultMapping( actionMapping ),(IRequest)request )
	 * @param mapping - 该对象对应的配置数据对象
	 * @param request - HttpServletRequest请求对象
	 * @return void
	 * @roseuid 3FAF083A03A8
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		
		if ( request instanceof IRequest){
			reset(new DefaultMapping(mapping), (IRequest)request);
		}else{
			reset(new DefaultMapping(mapping), new HttpRequest(request));
		}

	}
	/**
	 * 自有方法，尽量不使用 struts 的方法
	 * 
	 * 表单数据自校验
	 * @param map - 该对象对应的配置数据对象
	 * @param request - 该对象对应的请求对象
	 * @return venus.frames.mainframe.base.action.Errors - Errors对象列表，
	 * @roseuid 3FAF09DC0250
	 */
	public Errors validate(IMappingCfg map, IRequest request) {
		return null;
	}
	
	/**
	 * 自有方法，尽量不使用 struts 的方法
	 * 
	 * 表单数据对象自复位（配置数据自复位）
	 * @param map - 该对象对应的配置数据对象
	 * @param request - 该对象对应的请求对象
	 * @return void 
	 * @roseuid 3FAF0AA402CD
	 */
	public void reset(IMappingCfg map, IRequest request) {

	}
	
}
