package venus.oa.organization.aupartyrelation.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.util.IConstants;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.util.VoHelperTools;
import venus.oa.util.tree.DeepTreeSearch;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 翻页示例
 *  
 */
@Controller
@RequestMapping("/auPartyRelation")
public class AuPartyRelationAction implements IConstants {

    /**
     * 得到BS对象
     * 
     * @return BS对象
     */
    public IAuPartyRelationBs getBS() {
        return (IAuPartyRelationBs) Helper.getBean(BS_KEY);
    }

    /**
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/hasRoot")
    public String hasRoot(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String parentCode = request.getParameter("partyrelationtype_id");
        int nCount = getBS().getCountByParentCode(parentCode);
        if(nCount>0) {//有根节点
            request.setAttribute("parent_code", parentCode); 
//            return request.findForward(FORWARD_TREE_KEY);
            return FORWARD_TREE_KEY;
        }else {//无根节点
            request.setAttribute("partyrelationtype_id", parentCode); 
//            return request.findForward(FORWARD_ADDROOT_KEY);
            return FORWARD_ADDROOT_KEY;
        }
    }


    /**
     * 
     * 功能:初始化根节点 
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/initRoot")
    public String initRoot(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String partyRelationTypeId = request.getParameter("partyRelationTypeId");
        String partyId = request.getParameter("partyId");
        //添加团体关系根节点
        getBS().initRoot(partyId, partyRelationTypeId);
        request.setAttribute("parent_code", partyRelationTypeId);
//        return request.findForward(FORWARD_TREE_KEY);
        return FORWARD_TREE_KEY;
    }


    /**
     * 
     * 功能: 添加团体关系
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/insert")
    public String insert(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String childPartyId = request.getParameter("id");
        String parentRelId = request.getParameter("parentRelId");
        String relTypeId = request.getParameter("relTypeId");
        String returnPage = request.getParameter("returnPage");
        getBS().addPartyRelation(childPartyId, parentRelId, relTypeId);
        if("party_detail".equals(returnPage)) {//有根节点
//            return request.findForward(FORWARD_PARTY_DETAIL_KEY);
            return "forward:/auParty/detailPage";
        }else {
            request.setAttribute("parent_code", relTypeId);
//            return request.findForward(FORWARD_TREE_KEY);
            return FORWARD_TREE_KEY;
        }
    }


    /**
     * 
     * 功能: 添加多个团体关系
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/insertMulti")
    public String insertMulti(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String childPartyIds[] = request.getParameter("ids").split(",");
        String parentRelId = request.getParameter("parentRelId");
        String relTypeId = request.getParameter("relTypeId");
        String returnPage = request.getParameter("returnPage");
        for(int i=0; i<childPartyIds.length; i++) {
            getBS().addPartyRelation(childPartyIds[i], parentRelId, relTypeId);
        }
        if("party_detail".equals(returnPage)) {//有根节点
//            return request.findForward(FORWARD_PARTY_DETAIL_KEY);
            return "forward:/auParty/detailPage";
        }else {
            request.setAttribute("parent_code", relTypeId);
//            return request.findForward(FORWARD_TREE_KEY);
            return FORWARD_TREE_KEY;
        }
    }


    /**
     * 
     * 功能:删除团体关系 
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String relId = request.getParameter("relId");
        String returnPage = request.getParameter("returnPage");
        String parentCode = request.getParameter("partyrelationtype_id");//返回关系树页面使用
        getBS().deletePartyRelation(relId);
        if("party_detail".equals(returnPage)) {//有根节点
//            return request.findForward(FORWARD_PARTY_DETAIL_KEY);
            return "forward:/auParty/detailPage";
        }else {
            request.setAttribute("parent_code", parentCode); 
//            return request.findForward(FORWARD_TREE_KEY);
            return FORWARD_TREE_KEY;
        }
    }


    /**
     * 条件查询
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/simpleQuery")
    public String simpleQuery(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        String n = request.getParameter("name");
        AuPartyRelationVo obj = new AuPartyRelationVo();
        if (!Helper.populate(obj, request)) {
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        IAuPartyRelationBs bs = getBS();
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount(n);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        String orderStr = Helper.findOrderStr(request);
        List list = new ArrayList(); //定义结果集
        list = bs.simpleQuery(pageVo.getCurrentPage(), pageVo.getPageSize(), orderStr, obj);
        Helper.saveOrderStr(orderStr, request);
        Helper.savePageVo(pageVo, request);
        request.setAttribute(PROJECT_VALUE, list);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
    }


    /**
     * 以Ajax的方式获得人员的组织机构
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/getOrgName")
    public String getOrgName(HttpServletRequest request, HttpServletResponse response) throws Exception {
	HttpServletResponse res = response;
	res.setCharacterEncoding("GB2312");
	res.setContentType("text/html; charset=GB2312");   
        PrintWriter writer = res.getWriter();
        String partyId = request.getParameter("partyId");
        if (partyId != null || !"".equals(partyId))
            writer.print(DeepTreeSearch.getOrgNameById(request.getParameter("partyId"), false));
        else
            writer.print(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Not_given_parameters"));
        writer.flush();
        writer.close();
	return null;
    }    
}

