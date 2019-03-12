package venus.oa.authority.auauthorizelog.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;
import venus.oa.authority.auauthorizelog.bs.IAuAuthorizeLogBS;
import venus.oa.authority.auauthorizelog.util.IConstants;
import venus.oa.authority.aufunctree.bs.IAuFunctreeBs;
import venus.oa.authority.auresource.bs.IAuResourceBs;
import venus.oa.authority.auresource.vo.AuResourceVo;
import venus.oa.helper.AuHelper;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.oa.util.SqlBuilder;
import venus.oa.util.VoHelperTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author zangjian
 *
 */
@Controller
@RequestMapping("/auAuthorizeLog")
public class AuAuthorizeLogAction implements IConstants {
    
    @Autowired
    private IAuAuthorizeLogBS auAuthorizeLogBS;

    @Autowired
    private IAuFunctreeBs auFunctreeBs;

    @Autowired
    private IAuResourceBs auResourceBs;

    /**
     * 查询授权日志全部记录
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryAll")
    public String queryAll(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        IAuAuthorizeLogBS bs = auAuthorizeLogBS;
        String operator_name = request.getParameter("operator_name");
        String visitor_name = request.getParameter("visitor_name");
        String resource_name = request.getParameter("resource_name");
        String start_time = request.getParameter("start_time");
        String end_time = request.getParameter("end_time");
        String accredit_type = request.getParameter("accredit_type");
        String queryCondition = "";
        SqlBuilder sql=new SqlBuilder();

        if (operator_name != null && operator_name.length() > 0)
            sql.and(sql.like("operate_name", operator_name));

        if (visitor_name != null && visitor_name.length() > 0)
            sql.and(sql.like("visitor_name", visitor_name));

        if (resource_name != null && resource_name.length() > 0)
            sql.and(sql.like("resource_name", resource_name));

        if (start_time != null && start_time.length() > 0)
            sql.and(sql.greateOrEqual("operate_date", DateTools.getTimestamp(start_time+" 00:00:00.0")));

        if (end_time != null &&end_time.length() > 0)
            sql.and(sql.lessOrEqual("operate_date", DateTools.getTimestamp(end_time+" 23:59:59.0")));

        if (accredit_type != null && accredit_type.length() > 0)
            sql.and(sql.equal("accredit_type", accredit_type));
        queryCondition = AuHelper.filterOrgPrivInSQL(queryCondition, "B.CODE", (HttpServletRequest) request);//控制数据权限
	
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount(sql);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        sql.setCountBegin((pageVo.getCurrentPage()-1)*pageVo.getPageSize());//设置分页起始页
        sql.setCountEnd(pageVo.getPageSize());//设置分页结束页
        List list = new ArrayList(); //定义结果集
        list = bs.queryByCondition(sql);
        Helper.savePageVo(pageVo, request);
        request.setAttribute(REQUEST_BEANS_VALUE, list);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//	    return request.findForward(FORWARD_TO_QUERYALL);
        return FORWARD_TO_QUERYALL;
    }


    @RequestMapping("/viewFuncAu")
    public String viewFuncAu(HttpServletRequest request, HttpServletResponse response) throws Exception {
        doViewFuncOrgAu(request, response);
//        return request.findForward(FORWARD_TO_VIEW_FUNC_KEY);
        return FORWARD_TO_VIEW_FUNC_KEY;
    }


    private void doViewFuncOrgAu(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String auHisTag = request.getParameter("auHisTag");
        String vCode = request.getParameter("vCode");
        String fType = GlobalConstants.getResType_menu();//功能菜单
        String bType = GlobalConstants.getResType_butn();//功能按钮
        
        //获取访问者拥有的权限
        Map allMap = null;
        allMap = auAuthorizeLogBS.getAuByVisitorCode(vCode,null,auHisTag);
        
        //获取全部功能节点
        String strSql = "TOTAL_CODE like'101%' AND (TYPE='"+fType+"' OR TYPE='"+bType+"')";
        List lFunctree = auFunctreeBs.queryByCondition(strSql, "tree_level, order_code");

        //获取公开访问的节点
        List lResource = auResourceBs.queryByCondition("IS_PUBLIC ='1' and ENABLE_STATUS='1' and (RESOURCE_TYPE='"+fType+"' or RESOURCE_TYPE='"+bType+"')");
        Map resMap = new HashMap();
        for(Iterator itLResource = lResource.iterator(); itLResource.hasNext(); ) {
            AuResourceVo resVo = (AuResourceVo) itLResource.next();
            resMap.put(resVo.getId(), "");
        }
        request.setAttribute("ALL_AU_MAP", allMap);
        request.setAttribute("FUNC_LIST", lFunctree);
        request.setAttribute("PUB_RES_MAP", resMap);
    }
}

