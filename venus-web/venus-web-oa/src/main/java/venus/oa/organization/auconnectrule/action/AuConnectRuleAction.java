package venus.oa.organization.auconnectrule.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.oa.organization.auconnectrule.bs.IAuConnectRuleBS;
import venus.oa.organization.auconnectrule.util.IConstants;
import venus.oa.organization.auconnectrule.vo.AuConnectRuleVo;
import venus.oa.organization.aupartyrelationtype.bs.IAuPartyRelationTypeBS;
import venus.oa.organization.aupartytype.bs.IAuPartyTypeBS;
import venus.oa.util.DateTools;
import venus.oa.util.VoHelperTools;
import venus.commons.xmlenum.EnumRepository;
import venus.commons.xmlenum.EnumValueMap;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 团体连接规则 控制器
 */
@Controller
@RequestMapping("/auConnectRule")
public class AuConnectRuleAction implements IConstants {

    @Autowired
    private IAuConnectRuleBS auConnectRuleBS;

    @Autowired
    private IAuPartyTypeBS auPartyTypeBS;

    @Autowired
    private IAuPartyRelationTypeBS auPartyRelationTypeBS;

    /**
     * insert
     * @param _request
     * @param response
     * @return
     */
    @RequestMapping("/insert")
    public String insert(HttpServletRequest _request, HttpServletResponse response) {
        IRequest request = (IRequest)new HttpRequest(_request);
        IAuConnectRuleBS bs = auConnectRuleBS;
        AuConnectRuleVo obj = new AuConnectRuleVo();
        if (!Helper.populate(obj, request)) {
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        List list = bs.queryByType(obj);
        if (list!=null && list.size()>0) {
            return MESSAGE_AGENT_ERROR;
//            return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.The_same_rules_already_exist_to_connect"), MessageStyle.ALERT_AND_BACK);
        }
        obj.setCreate_date(DateTools.getSysTimestamp());
        obj.setModify_date(obj.getCreate_date());
        bs.insert(obj);
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auConnectRule/queryAll";
    }

    /**
     * 查询信息
     * 
     * @param _request
     * @param response
     * @return
     */
    @RequestMapping("/queryAll")
    public String queryAll(HttpServletRequest _request, HttpServletResponse response) {
        IRequest request = (IRequest)new HttpRequest(_request);
        IAuConnectRuleBS bs = auConnectRuleBS;
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

    /**
     * 删除
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/delete")
    public String delete(HttpServletRequest request, HttpServletResponse response) {
        //int deleteCount = 
        auConnectRuleBS.delete(request.getParameter("ids"));
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auConnectRule/queryAll";
    }

    /**
     * 条件查询
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/simpleQuery")
    public String simpleQuery(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        String n = request.getParameter("name");

        AuConnectRuleVo obj = new AuConnectRuleVo();
        if (!Helper.populate(obj, request)) {
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        IAuConnectRuleBS bs = auConnectRuleBS;
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
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools
                .getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
    }

    /**
     * 新增初始化
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/newPage")
    public String newPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //String ids = request.getParameter("ids");
        Map partyRelationMap = new HashMap();
        Map partyMap = new HashMap();
        EnumRepository er = EnumRepository.getInstance();
        er.loadFromDir();
        EnumValueMap relationTypeMap = er.getEnumValueMap("RelationTypeStatus");
        List partyRelationKeyList = relationTypeMap.getEnumList();
        for (int i = 0; i < partyRelationKeyList.size(); i++) {
            String key = relationTypeMap.getValue(partyRelationKeyList.get(i).toString());
            List partyRelationTypeList = auPartyRelationTypeBS.getPartyAllByKeyWord(key);
            partyRelationMap.put(key, partyRelationTypeList);
        }
        EnumValueMap typeMap = er.getEnumValueMap("TypeStatus");
        List partyKeyList = typeMap.getEnumList();
        for (int i = 0; i < partyKeyList.size(); i++) {
            String key = typeMap.getValue( partyKeyList.get(i).toString());
            List partyTypeList = auPartyTypeBS.getPartyAllByKeyword(key);
            partyMap.put(key, partyTypeList);
        }
        request.setAttribute(REQUEST_RELATION_TYPE, partyRelationMap);
        request.setAttribute(REQUEST_PARENT_PARTYTYPE, partyMap);
        request.setAttribute(REQUEST_CHILD_PARTYTYPE, partyMap);
//        return request.findForward(FORWARD_NEW_KEY);
        return FORWARD_NEW_KEY;
    }

    /**
     * 查找
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/find")
    public String find(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ids = request.getParameter("ids");
        AuConnectRuleVo obj = (AuConnectRuleVo) auConnectRuleBS.find(ids); //通过id获取vo
        VoHelperTools.null2Nothing(obj);
        List partyTypeList = auPartyTypeBS.getPartyAll();
        List partyRelationTypeList = auPartyRelationTypeBS.getPartyAll();
        request.setAttribute(REQUEST_BEAN_VALUE, obj); //把vo放入request
        request.setAttribute(REQUEST_RELATION_TYPE, partyRelationTypeList);
        request.setAttribute(REQUEST_PARENT_PARTYTYPE, partyTypeList);
        request.setAttribute(REQUEST_CHILD_PARTYTYPE, partyTypeList);
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
        AuConnectRuleVo obj = new AuConnectRuleVo();
        if (!Helper.populate(obj, request)) {
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        obj.setModify_date(DateTools.getSysTimestamp());
        auConnectRuleBS.update(obj);
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auConnectRule/queryAll";
    }

    /**
     * 查看详细页面
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/detailPage")
    public String detailPage(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        AuConnectRuleVo obj = (AuConnectRuleVo) auConnectRuleBS.find(id); //通过id获取vo
        VoHelperTools.null2Nothing(obj);
        request.setAttribute(REQUEST_BEAN_VALUE, obj); //把vo放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromVo(obj)); //回写表单
//        return request.findForward(FORWARD_DETAIL_KEY);
        return FORWARD_DETAIL_KEY;
    }

}

