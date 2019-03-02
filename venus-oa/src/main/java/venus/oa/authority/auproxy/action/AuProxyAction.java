package venus.oa.authority.auproxy.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.oa.authority.auproxy.bs.IAuProxyBs;
import venus.oa.helper.AuHelper;
import venus.oa.helper.LoginHelper;
import venus.oa.helper.OrgHelper;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.organization.auparty.bs.IAuPartyBs;
import venus.oa.organization.auparty.util.IConstants;
import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.history.bs.IHistoryLogBs;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.oa.util.VoHelperTools;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
@Controller
@RequestMapping("/auProxy")
public class AuProxyAction implements IConstants {
    
    /**
     * 得到BS对象
     * 
     * @return BS对象
     */
    public IAuPartyBs getBs() {
        return (IAuPartyBs) Helper.getBean(BS_KEY);
    }
    
    public IAuProxyBs getProxyBs() {
        return (IAuProxyBs) Helper.getBean(venus.oa.authority.auproxy.util.IConstants.PROXY_BS);
    }
    
    public IAuPartyRelationBs getRelationBs(){
        return (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
    }
            
    
    /**
     * 从页面表单获取信息注入vo，并插入单条记录，同时添加团体关系
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/insert")
    public String insert(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        PartyVo vo = new PartyVo();
        if (!Helper.populate(vo, request)) { //从request中注值进去vo
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        String parentRelId = request.getParameter("parentRelId");
        vo.setCreate_date(DateTools.getSysTimestamp());//打创建时间戳
        vo.setPartytype_id(GlobalConstants.getPartyType_proxy());//团体类型表的主键ID－代理
        vo.setOwner_org(LoginHelper.getPartyId((HttpServletRequest) request));
        //添加新的团体和团体关系
        String relType = GlobalConstants.getRelaType_proxy();//团体关系类型－代理关系
        String partyId = getBs().addPartyAndRelation(vo, parentRelId, relType);
        
        //回显团体关系
        String parentCode = OrgHelper.getRelationCodeByRelationId(parentRelId);
        AuPartyRelationVo relvo = new AuPartyRelationVo();
        relvo.setPartyid(partyId);
        relvo.setParent_code(parentCode);
        AuPartyRelationVo proxyRelVo = (AuPartyRelationVo)getRelationBs().queryAuPartyRelation(relvo).get(0);
        
        //记录新增代理团体的历史日志，该日志最终会用来实现代理与创建者的关联
        IHistoryLogBs historyBs = (IHistoryLogBs) Helper.getBean("proxyLogBs");
        Map map = new HashMap();
        map.put("OPERATERID", LoginHelper.getPartyId((HttpServletRequest) request));
        map.put("OPERATERNAME", LoginHelper.getLoginName((HttpServletRequest) request));
        map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_INSERT);
        map.put("SYSDATE", DateTools.getSysTimestamp());
        vo.setId(partyId);
        map.put("PROXYVO",vo);
        map.put("SOURCECODE",proxyRelVo.getCode());
        map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(proxyRelVo.getCode(), false)); //由于这里保存的是父节点，所以要显示最后一级节点
        historyBs.insert(map);

        request.setAttribute("parent_code", relType);
//        return request.findForward(FORWARD_LIST_PAGE_KEY);//返回组织关系管理页面
        return "authority/au/auproxy/listAuProxy";
    }
    
    /**
     * 
     * 功能: 查询已关联的用户列表
     *
     * @param proxyId
     * @return
     */
    public List queryLinkedUserList(String proxyId) {
        AuPartyRelationVo queryVo = new AuPartyRelationVo();
        queryVo.setParent_partyid(proxyId);
        queryVo.setPartytype_id(GlobalConstants.getPartyType_empl());
        return getRelationBs().queryAuPartyRelation(queryVo);
    }

    /**
     * 
     * 功能: 查询用户列表
     *
     * @param _request
     * @param response
     * @return
     */
    @RequestMapping("/queryUserList")
    public String queryUserList(HttpServletRequest _request, HttpServletResponse response) {
        IAuPartyBs bs = getBs();
        IRequest request = (IRequest)new HttpRequest(_request);
        String name = request.getParameter("name");
        String proxyId = request.getParameter("proxyId");
        String department = request.getParameter("hid_department");
        
        String queryCondition = null;
        if(name != null && department != null) {
            queryCondition = "a.name like '%" + name + "%' and d.parent_code like '" + department + "%'";
        }else if(name != null) {
            queryCondition = "a.name like '%" + name + "%'";
        }else if(department != null) {
            queryCondition = "d.parent_code like '" + department + "%'";
        }
        if (queryCondition != null)
            queryCondition += " AND a.id NOT IN('" + LoginHelper.getPartyId((HttpServletRequest)request) + "')";
        else 
            queryCondition = "a.id NOT IN('" + LoginHelper.getPartyId((HttpServletRequest)request) + "')";
        queryCondition = AuHelper.filterOrgPrivInSQL(queryCondition, "d.code", (HttpServletRequest) request);//控制数据权限

        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCountPerson(queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        String orderStr = Helper.findOrderStr(request);
        List list = bs.simpleQueryPerson(pageVo.getCurrentPage(), pageVo.getPageSize(), orderStr, queryCondition);//查询用户列表

        List linkedUserList = queryLinkedUserList(proxyId);//查询已经关联的用户列表
        if(linkedUserList!=null && linkedUserList.size()>0) {
            HashMap linkedUserMap = new HashMap();
            for(int i=0; i<linkedUserList.size(); i++) {
                AuPartyRelationVo relVo = (AuPartyRelationVo)linkedUserList.get(i);
                linkedUserMap.put(relVo.getPartyid(),null);
            }
            for(int i=0; i<list.size(); i++) {
                PartyVo partyVo = (PartyVo)list.get(i);
                if(linkedUserMap.containsKey(partyVo.getId())) {
                    partyVo.setEnable_status("0");//标识已被关联
                }
            }
        }
        Helper.saveOrderStr(orderStr, request);
        Helper.savePageVo(pageVo, request);
        request.setAttribute("proxyId", proxyId);
        request.setAttribute(PROJECT_VALUE, list);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_USER_LIST_KEY);
        return "authority/au/auproxy/userRef";
    }


    /**
     * 
     * 功能:代理树
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/showTree")
    public String showTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String parentCode = GlobalConstants.getRelaType_proxy();
        int nCount = getRelationBs().getCountByParentCode(parentCode);

        request.setAttribute("parent_code", parentCode);
        if (nCount > 0) {//有根节点
//            return request.findForward(FORWARD_LIST_PAGE_KEY);
            return "authority/au/auproxy/listAuProxy";
        } else {//无根节点
//            return request.findForward("noRoot");
            return "authority/au/auproxy/noRoot";
        }
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
        PartyVo vo = new PartyVo();
        if (!Helper.populate(vo, request)) { //从request中注值进去vo
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        
        //记录修改历史
        IHistoryLogBs historyBs = (IHistoryLogBs) Helper.getBean("proxyLogBs");
        Map map = new HashMap();
        map.put("OPERATERID", LoginHelper.getPartyId((HttpServletRequest) request));
        map.put("OPERATERNAME", LoginHelper.getLoginName((HttpServletRequest) request));
        map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_UPDATE);
        map.put("SYSDATE", DateTools.getSysTimestamp());
        map.put("PROXYVO",(PartyVo) getBs().find(vo.getId()));
        AuPartyRelationVo queryVo = new AuPartyRelationVo();
        queryVo.setPartyid(vo.getId());
        queryVo.setRelationtype_id(GlobalConstants.getRelaType_proxy());
        List rel = getRelationBs().queryAuPartyRelation(queryVo);
        for(int i=0;i<rel.size();i++){
            AuPartyRelationVo relVo = (AuPartyRelationVo)rel.get(i);
            map.put("SOURCECODE",relVo.getCode());
            map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(relVo.getCode(),false));
            historyBs.insert(map);
        }
        vo.setModify_date(DateTools.getSysTimestamp());//打修改时间戳
        getBs().updateParty(vo); //更新单条记录

        request.setAttribute("parent_code", GlobalConstants.getRelaType_proxy());
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return "authority/au/auproxy/listAuProxy";
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
        PartyVo bean = (PartyVo) getBs().find(request.getParameter("id")); //通过id获取vo
        request.setAttribute(REQUEST_BEAN_VALUE, bean); //把vo放入request
//        return request.findForward(FORWARD_UPDATE_KEY);
        return "forward:/jsp/authority/au/auproxy/insertAuProxy.jsp?isModify=1";
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
        String id = request.getParameter("id");
        PartyVo bean = (PartyVo) getBs().find(id); //通过id获取vo
        List list = queryLinkedUserList(id);//查询已经关联的用户列表
        request.setAttribute(REQUEST_BEAN_VALUE, bean); //把vo放入request
        request.setAttribute("linkedUserList", list);
//        return request.findForward(FORWARD_DETAIL_KEY);
        return "authority/au/auproxy/detailAuProxy";
    }


    /**
     * 
     * 功能: 删除已关联的用户
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteMulti")
    public String deleteMulti(HttpServletRequest request, HttpServletResponse response) throws Exception {
	LoginSessionVo vo = LoginHelper.getLoginVo(request);
        String ids[] = request.getParameter("ids").split(",");
        getProxyBs().deleteMulti(ids,vo);
        return detail(request, response);
    }


    /**
     * 删除代理
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String partyId = request.getParameter("id");
        //删除团体和团体关系记录历史
        IHistoryLogBs historyBs = (IHistoryLogBs) Helper.getBean("proxyLogBs");
        PartyVo partyVo = (PartyVo) getBs().find(partyId);  //通过id获取vo
        Map map = new HashMap();
        map.put("OPERATERID", LoginHelper.getPartyId((HttpServletRequest) request));
        map.put("OPERATERNAME", LoginHelper.getLoginName((HttpServletRequest) request));
        map.put("OPERATETYPE", GlobalConstants.HISTORY_LOG_DELETE);
        map.put("SYSDATE", DateTools.getSysTimestamp());
        map.put("PROXYVO",partyVo);
        String code = OrgHelper.getRelationCodeByRelationId(GlobalConstants.getRelaID_proxy());
        map.put("SOURCECODE",code);
        map.put("SOURCEORGTREE", OrgHelper.getOrgNameByCode(code,true));

        getBs().delete(partyId);//删除团体和团体关系

        historyBs.insert(map);
        request.setAttribute("parent_code", GlobalConstants.getRelaType_proxy());
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return "authority/au/auproxy/listAuProxy";
    }
    
    /**
     * 
     * 功能: 添加下级节点（单个角色关联单个用户）
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/addRelation")
    public String addRelation(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String partyId = request.getParameter("partyId");
        String parentRelId = request.getParameter("parentRelId");
        String relType = GlobalConstants.getRelaType_proxy();//团体关系类型－代理关系
        //约束：代理关系中，一个代理团体只能关联一个用户
        if(OrgHelper.getSubRelationListByID(parentRelId).size()>0){
//            return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Agents_can_not_manage_more_than_one_user_"), MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        LoginSessionVo vo = LoginHelper.getLoginVo(request);//权限上下文
        getProxyBs().addRelation(partyId, parentRelId, relType,  vo);
        return detail(request, response);
    }
}

