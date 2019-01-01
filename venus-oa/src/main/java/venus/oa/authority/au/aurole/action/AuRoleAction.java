package venus.oa.authority.au.aurole.action;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.oa.helper.AuHelper;
import venus.oa.helper.LoginHelper;
import venus.oa.helper.OrgHelper;
import venus.oa.organization.auparty.bs.IAuPartyBs;
import venus.oa.organization.auparty.util.IConstants;
import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.oa.util.ProjTools;
import venus.oa.util.VoHelperTools;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @since 1.0.0
 */
@Controller
@RequestMapping("/auRole")
public class AuRoleAction implements IConstants {

    /**
     * 得到BS对象
     * 
     * @return BS对象
     */
    public IAuPartyBs getBs() {
        return (IAuPartyBs) Helper.getBean(BS_KEY);
    }

    /**
     * 
     * 功能:
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/showTree")
    public String showTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String parentCode = GlobalConstants.getRelaType_role();
        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
        int nCount = relBs.getCountByParentCode(parentCode);

        request.setAttribute("parent_code", parentCode);
        if (nCount > 0) {//有根节点
//            return request.findForward(FORWARD_LIST_PAGE_KEY);
            return FORWARD_LIST_PAGE_KEY;

        } else {//无根节点
//            return request.findForward("noRoot");
            return "authority/au/aurole/noRoot";
        }
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
        vo.setPartytype_id(GlobalConstants.getPartyType_role());//团体类型表的主键ID－角色

        //添加新的团体和团体关系
        String relType = GlobalConstants.getRelaType_role();//团体关系类型－角色关系
        getBs().addPartyAndRelation(vo, parentRelId, relType);

        request.setAttribute("parent_code", relType);
//        return request.findForward(FORWARD_LIST_PAGE_KEY);//返回组织关系管理页面
        return FORWARD_LIST_PAGE_KEY;
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
    public String addRelation(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String partyId = request.getParameter("partyId");
        String parentRelId = request.getParameter("parentRelId");
        String relType = GlobalConstants.getRelaType_role();//团体关系类型－行政关系
        OrgHelper.addPartyRelation(partyId, parentRelId, relType);//调用接口添加团体关系
        request.setAttribute("parent_code", relType); 
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
    }
    /**
     * 
     * 功能: 添加多个下级节点（单个角色关联多个用户）
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/addMultiRelation")
    public String addMultiRelation(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String partyIds[] = request.getParameter("partyIds").split(",");
        String parentRelId = request.getParameter("parentRelId");
        String relType = GlobalConstants.getRelaType_role();//团体关系类型－行政关系
        for(int i=0; i<partyIds.length; i++) {
            OrgHelper.addPartyRelation(partyIds[i], parentRelId, relType);//调用接口添加团体关系
        }
        return detail(request, response);
    }
    /**
     * 删除角色
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        getBs().delete(request.getParameter("id"));//删除团体和团体关系
        request.setAttribute("parent_code", GlobalConstants.getRelaType_role());
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
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
        String ids[] = request.getParameter("ids").split(",");
        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
        for(int i=0; i<ids.length; i++){
            relBs.deletePartyRelation(ids[i]);//删除团体关系
        }
        return detail(request, response);
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
        PartyVo vo = new PartyVo();
        if (!Helper.populate(vo, request)) { //从request中注值进去vo
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        vo.setModify_date(DateTools.getSysTimestamp());//打修改时间戳
        getBs().updateParty(vo); //更新单条记录

        request.setAttribute("parent_code", GlobalConstants.getRelaType_role());
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
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        PartyVo bean = (PartyVo) getBs().find(id); //通过id获取vo
        AuPartyRelationVo vo =  new AuPartyRelationVo();
        vo.setName(name);
        vo.setParent_partyid(id);
        List list = queryLinkedUserList(vo);//查询已经关联的用户列表
        request.setAttribute(REQUEST_BEAN_VALUE, bean); //把vo放入request
        request.setAttribute("linkedUserList", list);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_DETAIL_KEY);
        return FORWARD_DETAIL_KEY;
    }

    /**
     * 从页面的表单获取单条记录id，并查看这条记录的详细信息
     * 该方法过滤了数据权限，但由于行政权限和角色权限在过滤时有冲突，需要客户定制开发，一下程序供参考
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/detailWithPrivilage")
    public String detailWithPrivilage(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        String id = request.getParameter("id");
        PartyVo bean = (PartyVo) getBs().find(id); //通过id获取vo
        
        String queryCondition = queryCondition(request);
        String privilageCondition = ""; 
        privilageCondition = AuHelper.filterOrgPrivInSQL(privilageCondition, "b.code", _request);//控制数据权限
        if(null!=privilageCondition&&privilageCondition.length()>0)
            privilageCondition = " and partyid in (select partyid from au_partyrelation b where "+privilageCondition+")";
        List list = queryLinkedUserListWithAuthorize(queryCondition + privilageCondition);//查询已经关联的用户列表
        
        request.setAttribute(REQUEST_BEAN_VALUE, bean); //把vo放入request
        request.setAttribute("linkedUserList", list);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_DETAIL_KEY);
        return FORWARD_DETAIL_KEY;
    }


    private String queryCondition(IRequest request){
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        StringBuffer queryConditon = new StringBuffer();
        if (!(name == null || name.trim().equals(""))) {
            queryConditon.append(" and NAME LIKE '%" + name + "%' ");
        }
        if (!(id == null || id.trim().equals(""))) {
            queryConditon.append(" and PARENT_PARTYID='" + id + "' ");
        }
        queryConditon.append(" and PARTYTYPE_ID='" + GlobalConstants.getPartyType_empl()+ "' ");
        return queryConditon.toString();
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

        IRequest request = (IRequest)new HttpRequest(_request);
        IAuPartyBs bs = getBs();
        String name = request.getParameter("name");
        String roleId = request.getParameter("roleId");
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
		    queryCondition += " AND a.id NOT IN('" + LoginHelper.getPartyId((HttpServletRequest) request) + "')";
		else 
		    queryCondition = "a.id NOT IN('" + LoginHelper.getPartyId((HttpServletRequest)request) + "')";
		queryCondition = AuHelper.filterOrgPrivInSQL(queryCondition, "d.code", (HttpServletRequest) request);//控制数据权限

	    if(queryCondition!=null){
            queryCondition+=" and a.id IN ( select party_id from au_user) ";
        }else{
            queryCondition= " a.id IN  ( select party_id from au_user) " ;
        }
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCountPerson(queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        String orderStr = Helper.findOrderStr(request);
        
    
        List list = bs.simpleQueryPerson(pageVo.getCurrentPage(), pageVo.getPageSize(), orderStr, queryCondition);//查询用户列表

        AuPartyRelationVo vo =  new AuPartyRelationVo();
        vo.setParent_partyid(roleId);
        List linkedUserList = queryLinkedUserList(vo);//查询已经关联的用户列表
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
        request.setAttribute("roleId", roleId);
        request.setAttribute(PROJECT_VALUE, list);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_USER_LIST_KEY);
        return "authority/au/aurole/userRef";
    }

    /**
     * 
     * 功能: 查询已关联的用户列表
     *
     * @param vo
     * @return
     */
    public List queryLinkedUserList(AuPartyRelationVo vo) {
        IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(venus.oa.organization.aupartyrelation.util.IConstants.BS_KEY);
        vo.setPartytype_id(GlobalConstants.getPartyType_empl());
        return relBs.queryAuPartyRelation(vo);
    }
    /**
     * 
     * 功能: 查询已关联的用户列表，控制权限
     *
     * @param queryCondition
     * @return
     */
    public List queryLinkedUserListWithAuthorize(String queryCondition) {
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyRelationVo vo = new AuPartyRelationVo();
                Helper.populate(vo, rs);
                return vo;
            }
        };
        String strsql = venus.oa.organization.aupartyrelation.util.IConstants.QUERY_AU_PARTYRELATION_SQL+" where 1=1 ";
        if(null!=queryCondition)
            strsql+= queryCondition;
        return ProjTools.getCommonBsInstance().doQuery(strsql, rowMapper);
    }
}

