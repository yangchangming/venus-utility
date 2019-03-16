package venus.oa.organization.auparty.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;
import venus.oa.authority.auuser.bs.IAuUserBs;
import venus.oa.authority.auuser.vo.AuUserVo;
import venus.oa.organization.auparty.bs.IAuPartyBs;
import venus.oa.organization.auparty.util.IConstants;
import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.organization.aupartytype.bs.IAuPartyTypeBS;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.oa.util.VoHelperTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 团体类型维护ACTION
 *  
 */
@Controller
@RequestMapping("/auParty")
public class AuPartyAction implements IConstants {

    @Autowired
    private IAuPartyBs auPartyBs;

    @Autowired
    private IAuPartyTypeBS auPartyTypeBS;

    @Autowired
    private IAuUserBs auUserBs;

    /**
     * 添加
     * 
     * @param _request
     * @param response
     * @return
     */
    @RequestMapping("/insert")
    public String insert(HttpServletRequest _request, HttpServletResponse response) {
        IRequest request = (IRequest)new HttpRequest(_request);
        PartyVo obj = new PartyVo();
        String typeId = request.getParameter(TYPE_VALUE);
        if (!Helper.populate(obj, request)) {
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        obj.setPartytype_id(typeId);
        obj.setCreate_date(DateTools.getSysTimestamp());  //打创建时间戳
        auPartyBs.addParty(obj);
        request.setAttribute(TYPE_VALUE, typeId);
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auParty/queryAll?typeId=" + typeId;
    }

    /**
     * 查询信息
     * 
     * @param _request
     * @param response
     * @return
     */
    @RequestMapping("queryAll")
    public String queryAll(HttpServletRequest _request, HttpServletResponse response) {
        IRequest request = (IRequest)new HttpRequest(_request);
        String name = request.getParameter("name");
        String typeId = request.getParameter(TYPE_VALUE);
        PartyVo obj = new PartyVo();
        obj.setName(name);
        obj.setPartytype_id(typeId);
        IAuPartyBs bs = auPartyBs;
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount(typeId, obj);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        String orderStr = Helper.findOrderStr(request);
        List list = new ArrayList(); //定义结果集
        list = bs.simpleQuery(pageVo.getCurrentPage(), pageVo.getPageSize(), orderStr, typeId, obj);
        Helper.saveOrderStr(orderStr, request);
        Helper.savePageVo(pageVo, request);
        request.setAttribute(TYPE_VALUE, typeId);
        request.setAttribute(PROJECT_VALUE, list);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
    }

    /**
     * 查询信息
     * 
     * @param _request
     * @param response
     * @return
     */
    @RequestMapping("/queryByOrgType")
    public String queryByOrgType(HttpServletRequest _request, HttpServletResponse response) {
        IRequest request = (IRequest)new HttpRequest(_request);
        String name = request.getParameter("name");
        String typeId = request.getParameter(TYPE_VALUE);
        PartyVo obj = new PartyVo();
        obj.setName(name);
        obj.setPartytype_id(typeId);
        IAuPartyBs bs = auPartyBs;
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCountPerson(obj);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        String orderStr = Helper.findOrderStr(request);
        List list = new ArrayList(); //定义结果集
        list = bs.simpleQueryPerson(pageVo.getCurrentPage(), pageVo.getPageSize(), orderStr, obj);
        Helper.saveOrderStr(orderStr, request);
        Helper.savePageVo(pageVo, request);
        request.setAttribute(TYPE_VALUE, typeId);
        request.setAttribute(PROJECT_VALUE, list);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_EMP_KEY);
        return FORWARD_EMP_KEY;
    }

    /**
     * 查找
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/find")
    public String find(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ids = request.getParameter("id");
        String typeId = request.getParameter(TYPE_VALUE);
        PartyVo obj = (PartyVo) auPartyBs.find(ids); //通过id获取vo
        VoHelperTools.null2Nothing(obj);
        request.setAttribute(TYPE_VALUE, typeId);
        request.setAttribute(REQUEST_BEAN_VALUE, obj); //把vo放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromVo(obj)); //回写表单
//        return request.findForward(FORWARD_UPDATE_KEY);
        return FORWARD_UPDATE_KEY;
    }

    /**
     * 修改
     * 
     * @param _request
     * @param response
     * @return
     */
    @RequestMapping("/update")
    public String update(HttpServletRequest _request, HttpServletResponse response) {
        IRequest request = (IRequest)new HttpRequest(_request);
        PartyVo obj = new PartyVo();
        String typeId = request.getParameter(TYPE_VALUE);
        if (!Helper.populate(obj, request)) {
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        obj.setModify_date(DateTools.getSysTimestamp());
        auPartyBs.updateParty(obj);
        request.setAttribute(TYPE_VALUE, typeId);
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auParty/queryAll?typeId=" + typeId;
    }

    /**
     * 启用
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/enable")
    public String enable(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String typeId = request.getParameter(TYPE_VALUE);
        auPartyBs.enableParty(id);
        request.setAttribute(TYPE_VALUE, typeId);
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auParty/queryAll?typeId=" + typeId;
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
        String id = request.getParameter("id");
        String typeId = request.getParameter(TYPE_VALUE);
        auPartyBs.disableParty(id);
        request.setAttribute(TYPE_VALUE, typeId);
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auParty/queryAll?typeId=" + typeId;
    }

    /**
     * 初始化管理页面
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/initPage")
    public String initPage(HttpServletRequest request, HttpServletResponse response) {
        String partyRelationTypeId=request.getParameter("partyrelationtypeId");
        List partyTypeList=new ArrayList();
        if(partyRelationTypeId==null || "".equals(partyRelationTypeId)){
            partyTypeList = auPartyTypeBS.queryAllEnable(-1,-1,null);
        }else{
            partyTypeList = auPartyTypeBS.getPartysByKeyWord(partyRelationTypeId);
            request.setAttribute("IS_ROOT", "1");
        }
        request.setAttribute(REQUEST_BEAN_VALUE, partyTypeList);
//        return request.findForward(FORWARD_LEFT_KEY);
        return FORWARD_LEFT_KEY;
    }

    /**
     * 初始化管理页面
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/detailPage")
    public String detailPage(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String pageFlag = request.getParameter("pageFlag");
        PartyVo obj = (PartyVo) auPartyBs.find(id); //通过id获取vo
        List list = auPartyBs.queryAllPartyRelation(id);
        VoHelperTools.null2Nothing(obj);
        request.setAttribute(REQUEST_BEAN_VALUE, obj); //把vo放入request
        request.setAttribute(REQUEST_LIST_VALUE, list); //把vo放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromVo(obj)); //回写表单
        if (pageFlag == null) {
            return FORWARD_DETAIL_KEY;
        } else {
            if(pageFlag.equals(GlobalConstants.getAuPartyTypePeop())) {
                List userList = auUserBs.queryByCondition("PARTY_ID='" + id + "'");
                if(userList!=null && userList.size()>0) {
                    AuUserVo userVo = (AuUserVo)userList.get(0);
                    request.setAttribute("UserVo", userVo);
                }
            }
            request.setAttribute("pageFlag", pageFlag);
            return FORWARD_DETAIL_FORPARTY_KEY;
        }

    }
    /**
     * 初始化管理页面
     * 
     * @param _request
     * @param response
     * @return
     */
    @RequestMapping("/detailList")
    public String detailList(HttpServletRequest _request, HttpServletResponse response) {
        IRequest request = (IRequest)new HttpRequest(_request);
        String id = request.getParameter("id");
        String pageFlag = request.getParameter("pageFlag");
        PartyVo obj = (PartyVo) auPartyBs.find(id); //通过id获取vo
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount =  auPartyBs.getRecordCountPartyRelation(id);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        String orderStr = Helper.findOrderStr(request);

        List list = new ArrayList(); //定义结果集
        list = auPartyBs.queryAllPartyRelationDivPage(pageVo.getCurrentPage(), pageVo.getPageSize(), id, obj);
        Helper.saveOrderStr(orderStr, request);
        Helper.savePageVo(pageVo, request);
        VoHelperTools.null2Nothing(obj);
        request.setAttribute(REQUEST_BEAN_VALUE, obj); //把vo放入request
        request.setAttribute(REQUEST_LIST_VALUE, list); //把vo放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools
                .getMapFromRequest((HttpServletRequest) request)); //回写表单
        if (pageFlag == null) {
//            return request.findForward(FORWARD_DETAIL_KEY);
            return FORWARD_DETAIL_KEY;
        } else {
            if(pageFlag.equals(GlobalConstants.getAuPartyTypePeop())) {
                List userList = auUserBs.queryByCondition("PARTY_ID='" + id + "'");
                if(userList!=null && userList.size()>0) {
                    AuUserVo userVo = (AuUserVo)userList.get(0);
                    request.setAttribute("UserVo", userVo); //把vo放入request
                }
            }
            request.setAttribute("pageFlag", pageFlag); //回写pageFlag
//            return request.findForward(FORWARD_DETAIL_FORPARTY_KEY);
            return FORWARD_DETAIL_FORPARTY_KEY;
        }
    }


    /**
     * 打开添加公司功能
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/addPage")
    public String addPage(HttpServletRequest request, HttpServletResponse response) {
        String typeId = request.getParameter(TYPE_VALUE);
        PartyVo obj = new PartyVo();
        request.setAttribute(TYPE_VALUE, typeId);
        request.setAttribute(REQUEST_BEAN_VALUE, obj); //把vo放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromVo(obj)); //回写表单
//        return request.findForward(FORWARD_ADD_KEY);
        return FORWARD_ADD_KEY;

    }

}

