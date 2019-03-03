package venus.oa.organization.relation.action;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.oa.helper.LoginHelper;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.organization.auconnectrule.bs.IAuConnectRuleBS;
import venus.oa.organization.auconnectrule.vo.AuConnectRuleVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.util.IConstants;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.organization.aupartytype.bs.IAuPartyTypeBS;
import venus.oa.organization.aupartytype.vo.AuPartyTypeVo;
import venus.oa.organization.relation.bs.IRelationBs;
import venus.oa.organization.relation.util.IRelationConstants;
import venus.oa.util.GlobalConstants;
import venus.oa.util.VoHelperTools;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * relation controller
 */
@Controller
@RequestMapping("/relation")
public class RelationAction implements IConstants, IRelationConstants {

    /**
     * 得到BS对象
     * 
     * @return BS对象
     */
    public IAuPartyRelationBs getBS() {
        return (IAuPartyRelationBs) Helper.getBean(BS_KEY);
    }

    /**
     * 
     * 功能: 初始化组织机构树
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/showTree")
    public String showTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String relationtype_id = request.getParameter("relationtype_id");
        String parentCode = null;
        if(StringUtils.isNotEmpty(relationtype_id))
            parentCode = relationtype_id;
        else
            parentCode = GlobalConstants.getRelaType_comp();
        
        int nCount = getBS().getCountByParentCode(parentCode);
        
        request.setAttribute("parent_code", parentCode); 
        if(nCount>0) {//有根节点
//            return request.findForward("list");
            return FORWARD_LIST_KEY;
        }else {//无根节点
//            return request.findForward("noRoot");
            return FORWARD_NOROOT_KEY;
        }
    }
    
    /**
     * 
     * 功能: 组织机构树关系业务
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/showRelationTree")
    public String showRelationTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rootCode = request.getParameter("rootCode");
        String parentCode = null;
        if(StringUtils.isNotEmpty(rootCode))
            parentCode = rootCode;
        else
            parentCode = GlobalConstants.getRelaType_comp();
        request.setAttribute("parent_code", parentCode);
//        return request.findForward("relationList");
        return FORWARD_RELATION_LIST_KEY;
    }
    
    /**
     * 
     * 功能: 添加组织机构树的根节点
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/initRoot")
    public String initRoot(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String relTypeId = request.getParameter("relTypeId");
        String partyRelationTypeId = StringUtils.isNotEmpty(relTypeId)?relTypeId: GlobalConstants.getRelaType_comp();
        String partyId = request.getParameter("partyId");
        //添加团体关系根节点
        getBS().initRoot(partyId, partyRelationTypeId);
        request.setAttribute("parent_code", partyRelationTypeId);
//        return request.findForward("list");
        return FORWARD_LIST_KEY;
    }


    /**
     * 
     * 功能:查看团体详细信息
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/detail")
    public String detail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        String partyTypeId = request.getParameter("type");
        
        if(GlobalConstants.getPartyType_comp().equals(partyTypeId)) {
//            return request.findForward("queryCompDetail");
            return "forward:/company/detail";

        }else if(GlobalConstants.getPartyType_dept().equals(partyTypeId)) {
//            return request.findForward("queryDeptDetail");
            return "forward:/department/detail";

        }else if(GlobalConstants.getPartyType_posi().equals(partyTypeId)) {
//            return request.findForward("queryPosiDetail");
            return "forward:/position/detail";

        }else if(GlobalConstants.getPartyType_empl().equals(partyTypeId)) {
//            return request.findForward("queryEmplDetail");
            return "forward:/employee/detail";

        }else if(StringUtils.isNotEmpty(partyTypeId)){
            AuPartyTypeVo obj = (AuPartyTypeVo) ((IAuPartyTypeBS) Helper.getBean(venus.oa.organization.aupartytype.util.IConstants.BS_KEY)).find(partyTypeId); //通过id获取vo
            String tableName = obj.getTable_name();
            boolean isGenerateCode = tableName.contains("ORGANIZE_");
            String catalogue = isGenerateCode?tableName.substring("ORGANIZE_".length()):tableName.substring("COLLECTIVE_".length());
            request.setAttribute("actionName", String.valueOf(catalogue.charAt(0))+catalogue.substring(1).toLowerCase()+(isGenerateCode?"Organize":"Collective"));
//            return request.findForward("queryCommonDetail");

            String actionName = String.valueOf(catalogue.charAt(0))+catalogue.substring(1).toLowerCase()+(isGenerateCode?"Organize":"Collective");
            return "forward:/" + actionName + "/detail";
        }else {
//            return request.findForward("default");
            return FORWARD_DEFAULT_KEY;
        }
    }


    /**
     * 
     * 功能: 获取连接规则，并跳转到添加下级节点的页面
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/go2Add")
    public String go2Add(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String partyType = request.getParameter("partyType");
        String relationId = request.getParameter("relationId");
        String relationTypeId = getRelationTypeIdByRelationId(relationId);
        IAuConnectRuleBS ruleBs = (IAuConnectRuleBS) Helper.getBean(venus.oa.organization.auconnectrule.util.IConstants.BS_KEY);
        AuConnectRuleVo vo = new AuConnectRuleVo();
        vo.setRelation_type_id(relationTypeId);
        vo.setParent_partytype_id(partyType);
        List list = ruleBs.queryByType(vo);
        HashMap ruleMap = new HashMap();
        if(list!=null) {
            for(int i=0; i<list.size(); i++) {
                AuConnectRuleVo ruleVo = (AuConnectRuleVo)list.get(i);
                if(ruleVo.getChild_partytype_id().equals(GlobalConstants.getPartyType_comp())) {
                    ruleMap.put("comp",null);
                }else if(ruleVo.getChild_partytype_id().equals(GlobalConstants.getPartyType_dept())){
                    ruleMap.put("dept",null);
                }else if(ruleVo.getChild_partytype_id().equals(GlobalConstants.getPartyType_posi())){
                    ruleMap.put("posi",null);
                }else if(ruleVo.getChild_partytype_id().equals(GlobalConstants.getPartyType_empl())){
                    ruleMap.put("empl",null);
                }else{
                    if(!ruleMap.containsKey("common"))
                        ruleMap.put("common", new ArrayList());
                    AuPartyTypeVo obj = (AuPartyTypeVo) ((IAuPartyTypeBS) Helper.getBean(venus.oa.organization.aupartytype.util.IConstants.BS_KEY)).find(ruleVo.getChild_partytype_id()); //通过id获取vo
                    String tableName = obj.getTable_name();
                    boolean isGenerateCode = tableName.contains("ORGANIZE_");
                    String catalogue = isGenerateCode?tableName.substring("ORGANIZE_".length()):tableName.substring("COLLECTIVE_".length());
                    ((List)ruleMap.get("common")).add(String.valueOf(catalogue.charAt(0))+catalogue.substring(1).toLowerCase());
                }
            }
        }
        request.setAttribute("relationId", request.getParameter("relationId")); 
        request.setAttribute("id", request.getParameter("id")); 
        request.setAttribute("partyType", request.getParameter("partyType")); 
        request.setAttribute("ruleMap", ruleMap); 
//        return request.findForward("add");
        return FORWARD_ADD_KEY;
    }


    /**
     * 
     * 功能: 添加下级节点
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/addRelation")
    public String addRelation(HttpServletRequest request,HttpServletResponse response) throws Exception {
        LoginSessionVo vo = LoginHelper.getLoginVo(request);//权限上下文
        String partyIds[] = request.getParameter("partyIds").split(",");
        String parentRelId = request.getParameter("parentRelId");
        String relType = getRelationTypeIdByRelationId(parentRelId);//团体关系类型
        IRelationBs bs = (IRelationBs) Helper.getBean("Relation_bs");
        bs.addRelation(partyIds, parentRelId, relType,vo);
        request.setAttribute("parent_code", relType); 
//        return request.findForward("list");
        return FORWARD_LIST_KEY;
    }
    
    /**
     * 
     * 功能: 组织机构排序
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/sort")
    public String sort(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String parent_code = request.getParameter("parent_code");
        String[] newIds = request.getParameterValues("sortList");
        
        AuPartyRelationVo queryVo = new AuPartyRelationVo();
        queryVo.setParent_code(parent_code);
    	List lOld = getBS().queryAuPartyRelation(queryVo);//查询旧的列表
    	
    	List lChange = new ArrayList();
    	if (lOld.size() != newIds.length) {  //判断排序前后记录的数量是否一致，以免出错
//            return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sorting_operation_failed_please_try_again_return_to_higher_level_page"), MessageStyle.ALERT_AND_BACK);
            return IRelationConstants.MESSAGE_AGENT_ERROR;
        }

    	for(int i=0; i<lOld.size(); i++) {
    	    AuPartyRelationVo vo = (AuPartyRelationVo) lOld.get(i);
    		if ( ! newIds[i].equals(vo.getId()) ) { //如果新旧id不等，则记录下来进行更新
    		    String[] param = new String[2];
    		    param[0] = vo.getOrder_code();
    		    param[1] = newIds[i];
				lChange.add(param);
			}
    	}
    	
    	if ( lChange.size() > 0 ) {
    	    getBS().sort(lChange);//更新多条记录
		}
    	queryVo = new AuPartyRelationVo();
    	queryVo.setCode(parent_code);
    	List queryRelation = getBS().queryAuPartyRelation(queryVo);
    	String relType = getRelationTypeIdByRelationId(((AuPartyRelationVo)queryRelation.get(0)).getId());//团体关系类型
    	request.setAttribute("parent_code", relType); 
//        return request.findForward("list");
        return FORWARD_LIST_KEY;
    }
    
    /**
     * 
     * 功能:删除团体关系 (废弃)
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
            return IRelationConstants.FORWARD_TREE_KEY;
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
            return IRelationConstants.MESSAGE_AGENT_ERROR;
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
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest(request)); //回写表单
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return IRelationConstants.FORWARD_LIST_PAGE_KEY;
    }


    private String getRelationTypeIdByRelationId(String relationId){
        return getBS().find(relationId).getRelationtype_id();
    }
    
    /**
     * 验证在公司新加和更新时，公司名称与当前关系下，其他公司的名称是否相同，确保公司名称的唯一性
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/checkCompany")
    public String checkCompany(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String parentRelId= request.getParameter("parentRelId");
        String companyName= request.getParameter("companyName");
        String operation = request.getParameter("operation");
        String partyId = request.getParameter("partyId");
        
        AuPartyRelationVo queryVo = new AuPartyRelationVo();
        if(parentRelId!=null && !parentRelId.equals("")){//组织机构管理页面跳转过来
            queryVo.setId(parentRelId);
        }else if("add".equals(operation)){//从公司管理档案跳转过来的,新增页面
            queryVo.setId(partyId);
        }else{//从公司管理档案跳转过来的,修改页面
            queryVo.setPartyid(partyId);
        }
        
        IAuPartyRelationBs bs = getBS();
        List<AuPartyRelationVo> auRelations= bs.queryAuPartyRelation(queryVo);
        AuPartyRelationVo relationVo= auRelations.get(0);
        String result = "1";
        if(relationVo!=null&&companyName!=null&&!"".equals(companyName)){
            String parentCode = null;
            if("update".equals(operation)){
                parentCode=  relationVo.getParent_code();
            }else{
                parentCode =relationVo.getCode();
            }
            if(parentRelId.equals("")){//从公司管理档案跳转过来的,parentRelld为当前关系的ID
                parentRelId=relationVo.getId();
            }
            queryVo =new AuPartyRelationVo();
            queryVo.setParent_code(parentCode);
            queryVo.setPartytype_id(GlobalConstants.getPartyType_comp());
          List<AuPartyRelationVo>  childAuRelations=  bs.queryAuPartyRelation(queryVo);
            for(AuPartyRelationVo relation:childAuRelations){
                String  tempName= relation.getName();
                if(parentRelId.equals(relation.getId())){//在是公司更新时，parentRelld为当前关系的ID，确保当前关系ID不与本身做名字校验
                    continue;
                }
               if(companyName.equals(tempName)){//如果名字与已有的团体名字相同，则返回0.不用进行其他团体关系名字的校验了
                   result="0";
                   break;
               }
            }
        }
        response.setContentType("text/plain; charset=UTF-8");
        response.getWriter().print(result);
        response.getWriter().close();
        return null;
    }


    /**
     *  * 验证在部门新加和更新时，部门名称与当前关系下，其他部门的名称是否相同，确保部门名称的唯一性
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/checkDeptName")
    public String checkDeptName(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String parentRelId= request.getParameter("parentRelId");
        String deptName= request.getParameter("dept_name");
        String operation = request.getParameter("operation");
        String partyId = request.getParameter("partyId");
        
        AuPartyRelationVo queryVo = new AuPartyRelationVo();
        if(parentRelId!=null && !parentRelId.equals("")){//组织机构管理页面跳转过来
            queryVo.setId(parentRelId);
        }else if("add".equals(operation)){//从部门管理档案跳转过来的,新增页面
            queryVo.setId(partyId);
        }else{//从部门管理档案跳转过来的,修改页面
            queryVo.setPartyid(partyId);
        }
        
        IAuPartyRelationBs bs = getBS();
        List<AuPartyRelationVo> auRelations= bs.queryAuPartyRelation(queryVo);
        AuPartyRelationVo relationVo= auRelations.get(0);
        String result = "1";
        if(relationVo!=null&&deptName!=null&&!"".equals(deptName)){
            String parentCode = null;
            if("update".equals(operation)){
                parentCode=  relationVo.getParent_code();
            }else{
                parentCode =relationVo.getCode();
            }
            if(parentRelId.equals("")){//从部门管理档案跳转过来的,parentRelld为当前关系的ID
                parentRelId=relationVo.getId();
            }
        
            queryVo =new AuPartyRelationVo();
            queryVo.setParent_code(parentCode);
            queryVo.setPartytype_id(GlobalConstants.getPartyType_dept());
          List<AuPartyRelationVo>  childAuRelations=  bs.queryAuPartyRelation(queryVo);
            for(AuPartyRelationVo relation:childAuRelations){
                String  tempName= relation.getName();
                if(parentRelId.equals(relation.getId())){//在是公司更新时，parentRelld为当前关系的ID，确保当前关系ID不与本身做名字校验
                    continue;
                }
               if(deptName.equals(tempName)){//如果名字与已有的团体名字相同，则返回0.不用进行其他团体关系名字的校验了
                   result="0";
                   break;
               }
            }
         
        }
        response.setContentType("text/plain; charset=UTF-8");
        response.getWriter().print(result);
        response.getWriter().close();
        return null;
    }
    
    /**
     * 验证角色名，在同一个父角色关系下，验证是否有同名的角色
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/checkRoleName")
    public String checkRoleName(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String parentRelId= request.getParameter("parentRelId");
        String deptName= request.getParameter("role_name");
        String operation = request.getParameter("operation");
        AuPartyRelationVo queryVo = new AuPartyRelationVo();
        queryVo.setId(parentRelId);
        IAuPartyRelationBs bs = getBS();
        List<AuPartyRelationVo> auRelations= bs.queryAuPartyRelation(queryVo);
        AuPartyRelationVo relationVo= auRelations.get(0);
        String result = "1";
        if(relationVo!=null&&deptName!=null&&!"".equals(deptName)){
            String parentCode = null;
            if("update".equals(operation)){
                parentCode=  relationVo.getParent_code();
            }else{
                parentCode =relationVo.getCode();
            }
        
            queryVo =new AuPartyRelationVo();
            queryVo.setParent_code(parentCode);
            queryVo.setPartytype_id(GlobalConstants.getPartyType_role());
          List<AuPartyRelationVo>  childAuRelations=  bs.queryAuPartyRelation(queryVo);
            for(AuPartyRelationVo relation:childAuRelations){
                String  tempName= relation.getName();
                if(parentRelId.equals(relation.getId())){//在是公司更新时，parentRelld为当前关系的ID，确保当前关系ID不与本身做名字校验
                    continue;
                }
               if(deptName.equals(tempName)){//如果名字与已有的团体名字相同，则返回0.不用进行其他团体关系名字的校验了
                   result="0";
                   break;
               }
            }
         
        }
        response.setContentType("text/plain; charset=UTF-8");
        response.getWriter().print(result);
        response.getWriter().close();
        return null;
    }
    
    /**
     * 验证同一关系下，岗位名称的唯一性
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/checkPositionName")
    public String checkPositionName(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String parentRelId= request.getParameter("parentRelId");
        String deptName= request.getParameter("position_name");
        String operation = request.getParameter("operation");
        String partyId = request.getParameter("partyId");
        AuPartyRelationVo queryVo = new AuPartyRelationVo();
        
        if(parentRelId!=null && !parentRelId.equals("")){//组织机构管理页面跳转过来
            queryVo.setId(parentRelId);
        }else if("add".equals(operation)){//从岗位管理档案跳转过来的,新增页面
            queryVo.setId(partyId);
        }else{//从岗位管理档案跳转过来的,修改页面
            queryVo.setPartyid(partyId);
        }
        
        IAuPartyRelationBs bs = getBS();
        List<AuPartyRelationVo> auRelations= bs.queryAuPartyRelation(queryVo);
        AuPartyRelationVo relationVo= auRelations.get(0);
        String result = "1";
        if(relationVo!=null&&deptName!=null&&!"".equals(deptName)){
            String parentCode = null;
            if("update".equals(operation)){
                parentCode=  relationVo.getParent_code();
            }else{
                parentCode =relationVo.getCode();
            }
            if(parentRelId.equals("")){//从岗位管理档案跳转过来的,parentRelld为当前关系的ID
                parentRelId=relationVo.getId();
            }
            queryVo =new AuPartyRelationVo();
            queryVo.setParent_code(parentCode);
            queryVo.setPartytype_id(GlobalConstants.getPartyType_posi());
          List<AuPartyRelationVo>  childAuRelations=  bs.queryAuPartyRelation(queryVo);
            for(AuPartyRelationVo relation:childAuRelations){
                String  tempName= relation.getName();
                if(parentRelId.equals(relation.getId())){//在是公司更新时，parentRelld为当前关系的ID，确保当前关系ID不与本身做名字校验
                    continue;
                }
               if(deptName.equals(tempName)){//如果名字与已有的团体名字相同，则返回0.不用进行其他团体关系名字的校验了
                   result="0";
                   break;
               }
            }
        }
        response.setContentType("text/plain; charset=UTF-8");
        response.getWriter().print(result);
        response.getWriter().close();
        return null;
    }
}