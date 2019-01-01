/*
 * 创建日期 2008-11-6
 */
package venus.oa.service.history.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.oa.authority.auauthorize.bs.IAuAuthorizeBS;
import venus.oa.authority.auauthorize.util.IConstants;
import venus.oa.authority.auauthorize.vo.AuAuthorizeVo;
import venus.oa.authority.auvisitor.bs.IAuVisitorBS;
import venus.oa.authority.auvisitor.vo.AuVisitorVo;
import venus.oa.helper.AuHelper;
import venus.oa.service.history.bs.IHistoryLogBs;
import venus.oa.service.history.util.IContants;
import venus.oa.service.history.vo.HistoryLogVo;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.oa.util.SqlBuilder;
import venus.oa.util.VoHelperTools;
import venus.oa.util.sql.OrgPrivilege;
import venus.oa.util.transform.TransformStrategy;
import venus.oa.util.transform.json.JsonDataTools;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 *  2008-11-6
 * @author changming.Y <changming.yang.ah@gmail.com>
 * 历史日志Action
 */
@Controller
@RequestMapping("/historyLog")
public class HistoryLogAction implements IContants {

	/**
	 * 获得带事务的历史日志BS
	 * @return
	 */
	public IHistoryLogBs getBs(){
		return (IHistoryLogBs) Helper.getBean(BS_KEY);
	}
	
	/**
	 * 日志查询后跳转到显示页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/simpleQuery")
	public String simpleQuery(HttpServletRequest request, HttpServletResponse response) throws Exception {
		baseSimpleQuery(request,response);
//		return request.findForward(FORWARD_LIST_PAGE_KEY);
		return FORWARD_LIST_PAGE_KEY;
	}
	
	/**
	 * 跳转到历史数据授权列表页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/simpleQueryForLogFrame")
	public String simpleQueryForLogFrame(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    baseSimpleQueryForLog(request,response);
		IAuVisitorBS visiBs = (IAuVisitorBS) Helper.getBean(venus.oa.authority.auvisitor.util.IConstants.BS_KEY);
    	AuVisitorVo visiVo = visiBs.queryByRelationId(request.getParameter("relId"), request.getParameter("pType"));
		IAuAuthorizeBS auBs = (IAuAuthorizeBS) Helper.getBean(IConstants.BS_KEY);
		Map map = auBs.queryHistoryAuByVisitorId(visiVo.getId(), GlobalConstants.getResType_orga());
	 	Set keySet = new HashSet();
	    for(Iterator it=map.keySet().iterator(); it.hasNext(); ) {
	    	AuAuthorizeVo auVo = (AuAuthorizeVo)map.get((String)it.next());
	    	keySet.add(auVo.getResource_code());
	    } 	
		request.setAttribute("keySet", keySet);  //把结果集放入request
//		return request.findForward(FORWARD_LIST_FRAME_KEY);
		return FORWARD_LIST_FRAME_KEY;
	}	
	
	/**
	 * 跳转到用查看用户历史数据授权页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/simpleQueryForView")
	public String simpleQueryForView(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    baseSimpleQueryForLog(request,response);
//		return request.findForward(FORWARD_LIST_VIEW_KEY);
		return FORWARD_LIST_VIEW_KEY;
	}		
	
	/**
	 * 条件查询的具体执行方法，使用了sqlbuilder来进行权限过滤和日期区间的查询，这样比较容易的支持多种数据库
	 * @param _request
	 * @param response
	 * @return
	 * @throws Exception
	 */	
	private void baseSimpleQuery (HttpServletRequest _request, HttpServletResponse response) throws Exception {
		IHistoryLogBs bs = getBs();
		IRequest request = (IRequest)new HttpRequest(_request);
		String operator_name = request.getParameter("operator_name");
		String source_name = request.getParameter("source_name");
		String log_start_time = request.getParameter("log_start_time");
		String log_end_time = request.getParameter("log_end_time");
		String optType = request.getParameter("optType");
		SqlBuilder sql=new SqlBuilder();
		if(operator_name!=null&&operator_name.length()>0)
			sql.and(sql.like("OPERATE_NAME",operator_name));
		if(source_name!=null&&source_name.length()>0)
			sql.and(sql.like("SOURCE_NAME",source_name));
		if(log_start_time!=null&&log_start_time.length()>0)
			sql.and(sql.greateOrEqual("OPERATE_DATE", DateTools.getTimestamp(log_start_time+" 00:00:00.0")));
		if(log_end_time!=null&&log_end_time.length()>0)
			sql.and(sql.lessOrEqual("OPERATE_DATE", DateTools.getTimestamp(log_end_time+" 23:59:59.0")));
		if(optType != null && optType.length() > 0)
		    	sql.and(sql.equal("OPERATE_TYPE", optType));
		//控制当前数据权限（调级用）
		OrgPrivilege nowPrivilege = (OrgPrivilege)sql.otherMethod("historyLogOrgPrivilege",new Object[]{});
		nowPrivilege.setFieldName("B.CODE");
		nowPrivilege.setRequest(_request);
		//控制历史数据权限（删除用）
		OrgPrivilege forPrivilege = (OrgPrivilege)sql.otherMethod("historyLogOrgPrivilege",new Object[]{});
		forPrivilege.setFieldName("A.SOURCE_CODE");
		forPrivilege.setRequest(_request);
		//权限关系
		sql.and(sql.or(nowPrivilege,forPrivilege));
		//分页
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount(sql);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        sql.setCountBegin((pageVo.getCurrentPage()-1)*pageVo.getPageSize());//设置分页起始页
		sql.setCountEnd(pageVo.getPageSize());//设置分页结束页
        List beans = null;  //定义结果集
        beans = bs.queryByCondition(sql);  //按条件查询
        request.setAttribute("selectOptType", optType);  //把optType的值放入request
        request.setAttribute(REQUEST_BEANS_VALUE, beans);  //把结果集放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request));  //回写表单
    }
	
	/**
	 * 条件查询的具体执行方法，使用了sqlbuilder来进行权限过滤和日期区间的查询，这样比较容易的支持多种数据库
	 *
	 * @param _request
	 * @param response
	 * @return
	 * @throws Exception
	 */	
	private void baseSimpleQueryForLog(HttpServletRequest _request, HttpServletResponse response) throws Exception {
		IHistoryLogBs bs = getBs();
		IRequest request = (IRequest)new HttpRequest(_request);
		String operator_name = request.getParameter("operator_name");
		String source_name = request.getParameter("source_name");
		String log_start_time = request.getParameter("log_start_time");
		String log_end_time = request.getParameter("log_end_time");
		String optType = request.getParameter("optType");
		SqlBuilder sql=new SqlBuilder();
		sql.and(sql.in("OPERATE_TYPE",new String[]{GlobalConstants.HISTORY_LOG_ADJUST, GlobalConstants.HISTORY_LOG_DELETE})); //只显示调级和删除的历史记录
		if(operator_name!=null&&operator_name.length()>0)
			sql.and(sql.like("OPERATE_NAME",operator_name));
		if(source_name!=null&&source_name.length()>0)
			sql.and(sql.like("SOURCE_NAME",source_name));
		if(log_start_time!=null&&log_start_time.length()>0)
			sql.and(sql.greateOrEqual("OPERATE_DATE", DateTools.getTimestamp(log_start_time+" 00:00:00.0")));
		if(log_end_time!=null&&log_end_time.length()>0)
			sql.and(sql.lessOrEqual("OPERATE_DATE", DateTools.getTimestamp(log_end_time+" 23:59:59.0")));
		if(optType != null && optType.length() > 0)
		    	sql.and(sql.equal("OPERATE_TYPE", optType));
		//控制当前数据权限（调级用）
		OrgPrivilege nowPrivilege = (OrgPrivilege)sql.otherMethod("historyLogOrgPrivilege",new Object[]{});
		nowPrivilege.setFieldName("B.CODE");
		nowPrivilege.setRequest(_request);
		//控制历史数据权限（删除用）
		OrgPrivilege forPrivilege = (OrgPrivilege)sql.otherMethod("historyLogOrgPrivilege",new Object[]{});
		forPrivilege.setFieldName("A.SOURCE_CODE");
		forPrivilege.setRequest(_request);
		//权限关系
		sql.and(sql.or(nowPrivilege,forPrivilege));
		//分页
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount(sql);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        sql.setCountBegin((pageVo.getCurrentPage()-1)*pageVo.getPageSize());//设置分页起始页
		sql.setCountEnd(pageVo.getPageSize());//设置分页结束页
        List beans = null;  //定义结果集
        beans = bs.queryByCondition(sql);  //按条件查询
        request.setAttribute(REQUEST_BEANS_VALUE, beans);  //把结果集放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request));  //回写表单
    }	
	
	/**
	 * 通过机构的partyid查询
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/findByPartyId")
	public String findByPartyId(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String source_id = request.getParameter("source_id");
		String queryCondition = "";  //查询条件
        if (source_id!=null&&source_id.length()>0) {
        	queryCondition = "A.SOURCE_PARTYID = '" + source_id + "' ";
        }
        queryCondition = AuHelper.filterOrgPrivInSQL(queryCondition, "B.CODE", (HttpServletRequest) request);//控制数据权限
		List beans = null;  //定义结果集
        beans = getBs().queryByCondition(queryCondition);  //按条件查询
        request.setAttribute(REQUEST_BEANS_VALUE, beans);  //把结果集放入request
//		return request.findForward(FORWARD_LIST_TREE_KEY);
		return FORWARD_LIST_TREE_KEY;
	}
	
	/**
	 * 历史日志的详细内容查询
	 */
	@RequestMapping("/findDetail")
	public String findDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		TransformStrategy ts = new JsonDataTools();
		HistoryLogVo historyVo = getBs().findDetail(id);
		request.setAttribute(REQUEST_BEANS_VALUE, ts.parse(historyVo.getSource_detail()));
		if (GlobalConstants.getPartyType_comp().equals(historyVo.getSource_typeid())) {
//			return request.findForward(FORWARD_LIST_DETAILCOMPANY);
			return FORWARD_LIST_DETAILCOMPANY;
		}else if (GlobalConstants.getPartyType_dept().equals(historyVo.getSource_typeid())) {
//			return request.findForward(FORWARD_LIST_DETAILDEPARTMENT);
			return FORWARD_LIST_DETAILDEPARTMENT;
		}else if (GlobalConstants.getPartyType_posi().equals(historyVo.getSource_typeid())) {
//			return request.findForward(FORWARD_LIST_DETAILPOSITION);
			return FORWARD_LIST_DETAILPOSITION;
		}else if (GlobalConstants.getPartyType_empl().equals(historyVo.getSource_typeid())) {
//			return request.findForward(FORWARD_LIST_DETAILEMPLOYEE);
			return FORWARD_LIST_DETAILEMPLOYEE;
		}else{
		    	request.setAttribute(REQUEST_BEANS_VALUE, historyVo);
//		    	return request.findForward(FORWARD_LIST_DETAILADJUST);
				return FORWARD_LIST_DETAILADJUST;
			//TODO：扩展版需要考虑party类型的转义
		}
	}
}

