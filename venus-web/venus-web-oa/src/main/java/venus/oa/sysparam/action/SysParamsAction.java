package venus.oa.sysparam.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.oa.helper.LoginHelper;
import venus.oa.sysparam.bs.ISysParamsBs;
import venus.oa.sysparam.util.IConstants;
import venus.oa.sysparam.vo.SysParamVo;
import venus.oa.util.GlobalConstants;
import venus.oa.util.VoHelperTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 2008-7-31
 * 
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
@Controller
@RequestMapping("/sysParams")
public class SysParamsAction implements IConstants {

	@Autowired
	private ISysParamsBs sysParamsBs;

	/**
	 * insert
	 *
	 * @param _request
	 * @param response
	 * @return
	 * @throws Exception
     */
	@RequestMapping("/insert")
	public String insert(HttpServletRequest _request, HttpServletResponse response) throws Exception {
		SysParamVo vo = new SysParamVo();
		IRequest request = (IRequest)new HttpRequest(_request);
		VoHelperTools.populate(vo, request); //从request中注值进去vo
		vo.setCreatorId(LoginHelper.getPartyId((HttpServletRequest) request.getServletRequest()));
		vo.setCreatorName(LoginHelper.getLoginName((HttpServletRequest) request.getServletRequest()));
		Timestamp time=new Timestamp(new Date().getTime());
		vo.setIniTime(time);
		vo.setUpdateTime(time);
		//由管理员新建的配置为系统默认配置项s
		String propertytype = (LoginHelper.getIsAdmin((HttpServletRequest) request.getServletRequest())) ? "0" : "1";
		vo.setPropertytype(propertytype);
		sysParamsBs.insert(vo); //插入单条记录
		refreshMemorySysParam();
//		return request.findForward(FORWARD_TO_QUERY_ALL);
		return "redirect:/sysParams/queryAll";
	}


	@RequestMapping("/update")
	public String update(HttpServletRequest _request, HttpServletResponse response) throws Exception {
		SysParamVo vo = new SysParamVo();
		IRequest request = (IRequest)new HttpRequest(_request);
		VoHelperTools.populate(vo, request); //从request中注值进去vo
		vo.setCreatorId(LoginHelper.getPartyId((HttpServletRequest) request.getServletRequest()));
		vo.setCreatorName(LoginHelper.getLoginName((HttpServletRequest) request.getServletRequest()));
		Timestamp time=new Timestamp(new Date().getTime());
		vo.setUpdateTime(time);
		sysParamsBs.update(vo); //插入单条记录
		refreshMemorySysParam();
//		return request.findForward(FORWARD_TO_QUERY_ALL);
		return "redirect:/sysParams/queryAll";
	}


	@RequestMapping("/enable")
	public String enable(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysParamVo bean = sysParamsBs.find(request.getParameter(REQUEST_ID));
		bean.setEnable(request.getParameter(ENABLE));
		sysParamsBs.update(bean); //插入单条记录
		refreshMemorySysParam();
//		return request.findForward(FORWARD_TO_QUERY_ALL);
		return "redirect:/sysParams/queryAll";
	}

	@RequestMapping("/delete")
	public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		sysParamsBs.delete(request.getParameter(REQUEST_ID)); //删除单条记录
		refreshMemorySysParam();
//		return request.findForward(FORWARD_TO_QUERY_ALL);
		return "redirect:/sysParams/queryAll";
	}

	@RequestMapping("/find")
	public String find(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysParamVo bean = sysParamsBs.find(request.getParameter(REQUEST_ID)); //通过id获取vo
		request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromVo(bean));  //回写表单
//		return request.findForward(FORWARD_UPDATE_PAGE);
		return FORWARD_UPDATE_PAGE;
	}


	@RequestMapping("/queryAll")
	public String queryAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List beans = sysParamsBs.queryByCondition(null); //按条件查询全部
		request.setAttribute(REQUEST_BEANS, beans); //把结果集放入request
//		return request.findForward(FORWARD_LIST_PAGE);
		return FORWARD_LIST_PAGE;
	}

	/**
	 * 更新缓存中的系统配置项
	 */
	private void refreshMemorySysParam(){
		GlobalConstants.loadSysParas();
	}
}

