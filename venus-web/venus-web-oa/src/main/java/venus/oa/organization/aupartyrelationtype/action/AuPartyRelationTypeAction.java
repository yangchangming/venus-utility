package venus.oa.organization.aupartyrelationtype.action;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.oa.authority.aufunctree.bs.IAuFunctreeBs;
import venus.oa.authority.aufunctree.util.IAuFunctreeConstants;
import venus.oa.authority.aufunctree.vo.AuFunctreeVo;
import venus.oa.organization.aupartyrelationtype.bs.IAuPartyRelationTypeBS;
import venus.oa.organization.aupartyrelationtype.util.IConstants;
import venus.oa.organization.aupartyrelationtype.vo.AuPartyRelationTypeVo;
import venus.oa.organization.aupartytype.bs.IAuPartyTypeBS;
import venus.oa.organization.aupartytype.vo.AuPartyTypeVo;
import venus.oa.util.DateTools;
import venus.oa.util.VoHelperTools;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 团体关系类型ACTION
 *  
 */
@Controller
@RequestMapping("/auPartyRelationType")
public class AuPartyRelationTypeAction implements IConstants {

    @Autowired
    private IAuPartyRelationTypeBS auPartyRelationTypeBS;

    @Autowired
    private IAuFunctreeBs auFunctreeBs;

    @Autowired
    private IAuPartyTypeBS auPartyTypeBS;

    /**
     * 添加
     * @param _request
     * @param response
     * @return
     */
    @RequestMapping("/insert")
    public String insert(HttpServletRequest _request, HttpServletResponse response) {
        IRequest request = (IRequest)new HttpRequest(_request);
        AuPartyRelationTypeVo obj = new AuPartyRelationTypeVo();
        if (!Helper.populate(obj, request)) {
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        obj.setCreate_date(DateTools.getSysTimestamp());
        obj.setModify_date(obj.getCreate_date());
        //OID oid = 
        auPartyRelationTypeBS.insert(obj);
        //增加菜单
        if(StringUtils.isNotEmpty(obj.getRoot_partytype_id())){
            //设置菜单 TODO 如果考虑机构和权限分离，这部分代码耦合就重了
            AuFunctreeVo vo = new AuFunctreeVo();
            vo.setParent_code("101003");
            vo.setType("0");
            vo.setIs_leaf("1");
            vo.setType_is_leaf("1");
            
            vo.setName(obj.getName()+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Management"));
//            vo.setUrl("/RelationAction.do?cmd=showTree&relationtype_id="+obj.getId());
            vo.setUrl("/relation/showTree?relationtype_id="+obj.getId());
            vo.setCreate_date(DateTools.getSysTimestamp());
            auFunctreeBs.insert(vo);
        }
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auPartyRelationType/queryAll";
    }

    /**
     * 查询信息
     * @param _request
     * @param response
     * @return
     */
    @RequestMapping("/queryAll")
    public String queryAll(HttpServletRequest _request, HttpServletResponse response) {
        IRequest request = (IRequest)new HttpRequest(_request);
        IAuPartyRelationTypeBS bs = auPartyRelationTypeBS;
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount();
            pageVo = Helper.createPageVo(request, recordCount);
        }
        String orderStr = Helper.findOrderStr(request);
        List list = new ArrayList(); //定义结果集
        list = bs.queryAll(pageVo.getCurrentPage(), pageVo.getPageSize(), orderStr);
		Helper.saveOrderStr(orderStr, request);
        Helper.savePageVo(pageVo, request);
        request.setAttribute(PROJECT_VALUE, list);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools
                .getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
    }


    @RequestMapping("/queryLeftTree")
    public String queryLeftTree(HttpServletRequest request, HttpServletResponse response) {
        IAuPartyRelationTypeBS bs = auPartyRelationTypeBS;
        List list = bs.queryAllEnable(-1,-1,null);
        request.setAttribute(REQUEST_BEAN_VALUE, list);
//        return request.findForward(FORWARD_LEFT_TREE_KEY);
        return FORWARD_LEFT_TREE_KEY;
    }

   
    /**
     * 删除
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/delete")
    public String delete(HttpServletRequest request, HttpServletResponse response) {
        //int deleteCount = 
        auPartyRelationTypeBS.delete(request.getParameter("ids"));
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auPartyRelationType/queryAll";
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
        AuPartyRelationTypeVo obj = new AuPartyRelationTypeVo();
        if (!Helper.populate(obj, request)) {
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }

        IAuPartyRelationTypeBS bs = auPartyRelationTypeBS;
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount(n);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        String orderStr = Helper.findOrderStr(request);

        List list = new ArrayList(); //定义结果集
        list = bs.simpleQuery(pageVo.getCurrentPage(), pageVo.getPageSize(),
                orderStr, obj);
        Helper.saveOrderStr(orderStr, request);
        Helper.savePageVo(pageVo, request);
        request.setAttribute(PROJECT_VALUE, list);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
    }

    /**
     * 查找
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/find")
    public String find(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ids = request.getParameter("ids");
        AuPartyRelationTypeVo obj = (AuPartyRelationTypeVo) auPartyRelationTypeBS.find(ids); //通过id获取vo
        VoHelperTools.null2Nothing(obj);
        request.setAttribute(REQUEST_BEAN_VALUE, obj); //把vo放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromVo(obj)); //回写表单
//        return request.findForward(FORWARD_UPDATE_KEY);
        return FORWARD_UPDATE_KEY;
    }

    /**
     * 修改
     * @param _request
     * @param response
     * @return
     */
    @RequestMapping("/update")
    public String update(HttpServletRequest _request, HttpServletResponse response) {
        IRequest request = (IRequest)new HttpRequest(_request);
        AuPartyRelationTypeVo obj = new AuPartyRelationTypeVo();
        if (!Helper.populate(obj, request)) {
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        obj.setModify_date(DateTools.getSysTimestamp());
        auPartyRelationTypeBS.update(obj);
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auPartyRelationType/queryAll";
    }

    /**
     * 启用
     * @param formBean
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/enable")
    public String enable(HttpServletRequest request, HttpServletResponse response) {
        auPartyRelationTypeBS.enable(request.getParameter("ids"));
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auPartyRelationType/queryAll";
    }
    
    /**
     * 禁用
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/disable")
    public String disable(HttpServletRequest request, HttpServletResponse response) {
        auPartyRelationTypeBS.disable(request.getParameter("ids"));
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auPartyRelationType/queryAll";
    }


    /**
     * 查看详细页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/detailPage")
    public String detailPage(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        AuPartyRelationTypeVo obj = (AuPartyRelationTypeVo) auPartyRelationTypeBS.find(id); //通过id获取vo
        VoHelperTools.null2Nothing(obj);
        if(StringUtils.isNotEmpty(obj.getRoot_partytype_id())){
            AuPartyTypeVo partyTypeVo = (AuPartyTypeVo)auPartyTypeBS.find(obj.getRoot_partytype_id());
            obj.setRoot_partytype_id(partyTypeVo.getName());
        }
        request.setAttribute(REQUEST_BEAN_VALUE, obj); //把vo放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromVo(obj)); //回写表单
//        return request.findForward(FORWARD_DETAIL_KEY);
        return FORWARD_DETAIL_KEY;
    }
}

