/*
 * 系统名称:
 * 
 * 文件名称: venus.authority.sample.position.action --> PositionAction.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2007-01-31 14:20:06.9 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.organization.position.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.oa.helper.AuHelper;
import venus.oa.helper.LoginHelper;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.organization.position.bs.IPositionBs;
import venus.oa.organization.position.bs.IPositionFacadeBs;
import venus.oa.organization.position.util.IPositionConstants;
import venus.oa.organization.position.vo.PositionVo;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.oa.util.QueryBuilder;
import venus.oa.util.VoHelperTools;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @since 1.0.0
 */
@Controller
@RequestMapping("/position")
public class PositionAction implements IPositionConstants {

    /**
     * 得到BS对象
     * 
     * @return BS对象
     */
    public IPositionBs getBs() {
        return (IPositionBs) Helper.getBean(BS_KEY);  //得到BS对象,受事务控制
    }
    
    /**
     * 得到Facade BS对象
     * 
     * @return BS对象
     */
    public IPositionFacadeBs getFacadeBs() {
    	return (IPositionFacadeBs) Helper.getBean(FACADE_BS_KEY);//得到BS对象,受事务控制
    }    


    /**
     * 从页面表单获取信息注入vo，并插入单条记录，同时添加团体、团体关系（如果parentRelId为空则不添加团体关系）
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/insert")
    public String insert(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        LoginSessionVo AuthorizedContext= LoginHelper.getLoginVo((HttpServletRequest) request.getServletRequest());//权限上下文
        PositionVo vo = new PositionVo();
        if (!Helper.populate(vo, request)) {  //从request中注值进去vo
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        vo.setCreate_date(DateTools.getSysTimestamp());//打创建时间戳
        String parentRelId = request.getParameter("parentRelId");
        if(parentRelId!=null && !"".equals(parentRelId) && !"null".equals(parentRelId)) {
            getFacadeBs().insert(vo, parentRelId, AuthorizedContext);
            request.setAttribute("parent_code", GlobalConstants.getRelaType_comp());
//            return request.findForward(FORWARD_QUERY_TREE_KEY);
            return FORWARD_QUERY_TREE_KEY;
        }else {
            String owner_party_id = request.getParameter("owner_party_id");
            getFacadeBs().insert(vo, owner_party_id, AuthorizedContext);
//            return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
            return "redirect:/position/queryAll";
        }
    }


    /**
     * 从页面的表单获取团体关系id，并删除团体关系及相关的权限记录
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	LoginSessionVo AuthorizedContext= LoginHelper.getLoginVo(request);//权限上下文
        getFacadeBs().delete(request.getParameter("relationId"),AuthorizedContext); //只删除团体关系及相关的权限记录
        request.setAttribute("parent_code", GlobalConstants.getRelaType_comp()); 
//        return request.findForward(FORWARD_QUERY_TREE_KEY);
        return FORWARD_QUERY_TREE_KEY;
    }


    /**
     * 从页面的表单获取多条记录id，并删除多条记录
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteMulti")
    public String deleteMulti(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	LoginSessionVo AuthorizedContext= LoginHelper.getLoginVo(request);
        String ids = request.getParameter(REQUEST_MULTI_ID_FLAG);  //从request获取多条记录id
        String id[] = ids.split(",");
        if (id != null && id.length != 0) {
        	getFacadeBs().delete(id,AuthorizedContext);  //删除多条记录
        }
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/position/queryAll";
    }


    /**
     * 从页面的表单获取单条记录id，查出这条记录的值，并跳转到修改页面
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/find")
    public String find(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PositionVo bean = getBs().find(request.getParameter(REQUEST_ID_FLAG));  //通过id获取vo
        request.setAttribute(REQUEST_BEAN_VALUE, bean);  //把vo放入request
//        return request.findForward(FORWARD_UPDATE_KEY);
        return FORWARD_UPDATE_KEY;
    }


    /**
     * 从页面表单获取信息注入vo，并修改单条记录，同时调用接口更新相应的团体、团体关系记录
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/update")
    public String update(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
	    LoginSessionVo AuthorizedContext= LoginHelper.getLoginVo(_request);//权限上下文
        PositionVo vo = new PositionVo();
        if (!Helper.populate(vo, request)) {
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        vo.setModify_date(DateTools.getSysTimestamp());//打修改时间戳
        getFacadeBs().update(vo, AuthorizedContext);
        String parentRelId = request.getParameter("parentRelId");
        if(parentRelId!=null && !"".equals(parentRelId) && !"null".equals(parentRelId)) {
            request.setAttribute("parent_code", GlobalConstants.getRelaType_comp()); 
//            return request.findForward(FORWARD_QUERY_TREE_KEY);
            return FORWARD_QUERY_TREE_KEY;
        }else {
//            return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
            return "redirect:/position/queryAll";
        }
    }


    /**
     * 查询全部记录，分页显示，支持页面上触发的后台排序
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryAll")
    public String queryAll(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        IPositionBs bs = getBs();
        String queryCondition = "";  //查询条件
        queryCondition = AuHelper.filterOrgPrivInSQL(queryCondition, "B.CODE", (HttpServletRequest) request.getServletRequest());//控制数据权限
        PageVo pageVo = Helper.findPageVo(request);  //得到当前翻页信息
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount(queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        List beans = null;  //定义结果集     
        //beans = bs.queryAll(pageVo.getCurrentPage(), pageVo.getPageSize());  //查询全部
        beans = bs.queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition);  //按条件查询
        request.setAttribute(REQUEST_BEANS_VALUE, beans);  //把结果集放入request
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
    }


    /**
     * 从页面的表单获取单条记录id，并查看这条记录的详细信息
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/detail")
    public String detail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PositionVo bean = getBs().find(request.getParameter(REQUEST_ID_FLAG));  //通过id获取vo
        request.setAttribute(REQUEST_BEAN_VALUE, bean);  //把vo放入request
//        return request.findForward(FORWARD_DETAIL_KEY);
        return FORWARD_DETAIL_KEY;
    }


    /**
     * 简单查询，分页显示，支持表单回写
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/simpleQuery")
    public String simpleQuery(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        IPositionBs bs = getBs();
        String queryCondition = queryCondition(request);  //从request获得查询条件
        queryCondition = AuHelper.filterOrgPrivInSQL(queryCondition, "B.CODE", (HttpServletRequest) request.getServletRequest());//控制数据权限
        PageVo pageVo = Helper.findPageVo(request);  //得到当前翻页信息
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount(queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        List beans = bs.queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition);  //按条件查询
        request.setAttribute(REQUEST_BEANS_VALUE, beans);  //把结果集放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request));  //回写表单
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
    }


    /**
     * 参照信息查询，带简单查询，分页显示，支持表单回写
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryReference")
    public String queryReference(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IPositionBs bs = getBs();
        IRequest request = (IRequest)new HttpRequest(_request);
        String queryCondition = queryCondition(request);  //从request获得查询条件
        PageVo pageVo = Helper.findPageVo(request);  //得到当前翻页信息
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount(queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        List beans = bs.queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition);  //按条件查询
        request.setAttribute(REQUEST_BEANS_VALUE, beans);  //把结果集放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request));  //回写表单
        request.setAttribute("inputType", request.getParameter("inputType"));  //传送输入方式,checkbox或radio
//        return request.findForward(FORWARD_REFERENCE_KEY);
        return FORWARD_REFERENCE_KEY;
    }


    /**
     * 从request获得查询条件并拼装成查询的SQL语句
     * @param qca
     * @return 查询的SQL语句
     */
    private String queryCondition(final IRequest qca){
        return new QueryBuilder(){
            @Override
            public void andCondition() {
                conditions = new String[]{
                        pushCondition(qca,"position_no"),
                        pushCondition(qca,"position_name"),
                        pushCondition(qca,"position_flag", "='", "'"),
                        pushCondition(qca,"leader_flag", "='", "'"),
                        /*
                        pushCondition(qca,"position_type", "='", "'"),
                        pushCondition(qca,"position_level", "='", "'"),
                        pushCondition(qca,"leader_level"),
                        pushCondition(qca,"remark"),
                        pushCondition(qca,"enable_status", "='", "'"),
                        pushCondition(qca,"enable_date_from", ">=to_date('" ,"','YYYY-MM-DD')", "enable_date"),
                        pushCondition(qca,"enable_date_to", "<=to_date('", "','YYYY-MM-DD')", "enable_date"),
                        pushCondition(qca,"create_date_from", ">=to_date('" ,"','YYYY-MM-DD')", "create_date"),
                        pushCondition(qca,"create_date_to", "<=to_date('", "','YYYY-MM-DD')", "create_date"),
                        pushCondition(qca,"modify_date_from", ">=to_date('" ,"','YYYY-MM-DD')", "modify_date"),
                        pushCondition(qca,"modify_date_to", "<=to_date('", "','YYYY-MM-DD')", "modify_date"),
                        pushCondition(qca,"column1"),
                        pushCondition(qca,"column2"),
                        pushCondition(qca,"column3"),
                        */
                };
            }            
        }.build();    	
    }
}

