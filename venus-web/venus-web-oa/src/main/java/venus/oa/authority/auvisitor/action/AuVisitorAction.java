package venus.oa.authority.auvisitor.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;
import venus.oa.authority.auvisitor.bs.IAuVisitorBS;
import venus.oa.authority.auvisitor.util.IConstants;
import venus.oa.authority.auvisitor.vo.AuVisitorVo;
import venus.oa.helper.AuHelper;
import venus.oa.helper.OrgHelper;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.util.GlobalConstants;
import venus.oa.util.VoHelperTools;
import venus.springsupport.BeanFactoryHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 
 * 团体类型维护ACTION
 *  
 */
@Controller
@RequestMapping("/auVisitor")
public class AuVisitorAction implements IConstants {

    @Autowired
    private IAuVisitorBS auVisitorBS;
    
    /**
     * 查询信息
     * @param _request
     * @param response
     * @return
     */
    @RequestMapping("/queryAllByTypes")
    public String queryAllByTypes(HttpServletRequest _request, HttpServletResponse response) {
        IRequest request = (IRequest)new HttpRequest(_request);
        String partyTypes = request.getParameter("partyTypes");
        IAuVisitorBS bs = auVisitorBS;
        String queryCondition = "";  //查询条件
        if( ! GlobalConstants.getVisiType_role().equals(partyTypes)) {//非角色
        	queryCondition = AuHelper.filterOrgPrivInSQL(queryCondition, "r.CODE", (HttpServletRequest) request);//控制数据权限
        }
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCountByTypes(partyTypes,null,queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        List list = bs.simpleQueryByTypes(pageVo.getCurrentPage(), pageVo.getPageSize(), partyTypes, null, queryCondition);
        	//bs.queryAllByTypes(pageVo.getCurrentPage(), pageVo.getPageSize(), partyTypes);
        Helper.savePageVo(pageVo, request);
        request.setAttribute(PROJECT_VALUE, list);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_PARTY_TYPES_KEY);
        return FORWARD_PARTY_TYPES_KEY;
    }
    

    @RequestMapping("/queryAllUser")
    public String queryAllUser(HttpServletRequest _request, HttpServletResponse response) {
        IRequest request = (IRequest)new HttpRequest(_request);
        String partyTypes = "1";
        IAuVisitorBS bs = auVisitorBS;
        String queryCondition = "";  //查询条件
        queryCondition = AuHelper.filterOrgPrivInSQL(queryCondition, "r.CODE", (HttpServletRequest) request);//控制数据权限
        
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCountByTypes(partyTypes,null,queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        List list = bs.simpleQueryByTypes(pageVo.getCurrentPage(), pageVo.getPageSize(),
                partyTypes, null, queryCondition);
        	//bs.queryAllByTypes(pageVo.getCurrentPage(), pageVo.getPageSize(), partyTypes);

        Helper.savePageVo(pageVo, request);
        request.setAttribute(PROJECT_VALUE, list);
        request.setAttribute("partyTypes", partyTypes);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_PARTY_TYPES_KEY);
        return FORWARD_PARTY_TYPES_KEY;
    }

    /**
     * fetch all role controller
     *
     * @param _request
     * @param response
     * @return
     */
    @RequestMapping("/queryAllRole")
    public String queryAllRole(HttpServletRequest _request, HttpServletResponse response) {
        IRequest request = (IRequest)new HttpRequest(_request);
        String partyTypes = "2";
        IAuVisitorBS bs = auVisitorBS;
        String queryCondition = "";  //查询条件
        queryCondition = AuHelper.filterOrgPrivInSQL(queryCondition, "r.CODE", (HttpServletRequest) request);//控制数据权限
        
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCountByTypes(partyTypes,null,queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        List list = bs.simpleQueryByTypes(pageVo.getCurrentPage(), pageVo.getPageSize(), partyTypes, null, queryCondition);
        	//bs.queryAllByTypes(pageVo.getCurrentPage(), pageVo.getPageSize(), partyTypes);

        Helper.savePageVo(pageVo, request);
        request.setAttribute(PROJECT_VALUE, list);
        request.setAttribute("partyTypes", partyTypes);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_PARTY_TYPES_KEY);
        return FORWARD_PARTY_TYPES_KEY;
    }


    /**
     * fetch all organization controller
     *
     * @param _request
     * @param response
     * @return
     */
    @RequestMapping("/queryAllOrg")
    public String queryAllOrg(HttpServletRequest _request, HttpServletResponse response) {
        IRequest request = (IRequest)new HttpRequest(_request);
        String partyTypes = "3";
        IAuVisitorBS bs = auVisitorBS;
        String queryCondition = "";  //查询条件
        queryCondition = AuHelper.filterOrgPrivInSQL(queryCondition, "r.CODE", (HttpServletRequest) request);//控制数据权限
        
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCountByTypes(partyTypes,null,queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        List list = bs.simpleQueryByTypes(pageVo.getCurrentPage(), pageVo.getPageSize(), partyTypes, null, queryCondition);
        	//bs.queryAllByTypes(pageVo.getCurrentPage(), pageVo.getPageSize(), partyTypes);

        Helper.savePageVo(pageVo, request);
        request.setAttribute(PROJECT_VALUE, list);
        request.setAttribute("partyTypes", partyTypes);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_PARTY_TYPES_KEY);
        return FORWARD_PARTY_TYPES_KEY;
    }
    
    
    /**
     * 条件查询
     *
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/simpleQueryByTypes")
    public String simpleQueryByTypes(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        String partyTypes = request.getParameter("partyTypes");
        //String name = request.getParameter("name");
        AuVisitorVo obj = new AuVisitorVo();
        if (!Helper.populate(obj, request)) {
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        String queryCondition = "";  //查询条件
        //if( ! GlobalConstants.getVisiType_role().equals(partyTypes)) {//非角色  //20091109非角色也需要过滤权限
        	queryCondition = AuHelper.filterOrgPrivInSQL(queryCondition, "r.CODE", (HttpServletRequest) request);//控制数据权限
        //}
        IAuVisitorBS bs = auVisitorBS;
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCountByTypes(partyTypes, obj, queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        List list = bs.simpleQueryByTypes(pageVo.getCurrentPage(), pageVo.getPageSize(), partyTypes, obj, queryCondition);
        Helper.savePageVo(pageVo, request);
        request.setAttribute(PROJECT_VALUE, list);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//       return request.findForward(FORWARD_PARTY_TYPES_KEY);
        return FORWARD_PARTY_TYPES_KEY;
    }


    /**
     * 
     * 功能:单个用户关联多个角色 
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/addMultiRole")
    public String addMultiRole(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String partyId = request.getParameter("partyId");
        String parentRelIds[] = split(request.getParameter("parentRelIds"),",");
        String delRelIds[] = split(request.getParameter("delRelIds"),",");
        String relType = GlobalConstants.getRelaType_role();//团体关系类型－角色关系
        //先删再增策略20100528开始
        IAuPartyRelationBs bs = (IAuPartyRelationBs) BeanFactoryHelper.getBean("auPartyRelationBs");
        for(int i=0;i<delRelIds.length;i++){
            AuPartyRelationVo vo = bs.queryRelationVoByKey(partyId, delRelIds[i], relType);
            if(vo!=null){ //在进行用户角色授权时，可能出现在取消父节点角色时，子节点再次获取其值时出现空
                bs.deletePartyRelation(vo.getId());//删除取消的角色关系
            }
        }
        //先删再增策略20100528结束
        for(int i=0; i<parentRelIds.length; i++) {
            if(null!=parentRelIds[i]&&parentRelIds[i].length()>0){
                AuPartyRelationVo vo = bs.queryRelationVoByKey(partyId, parentRelIds[i], relType);
                if(null==vo)
                    OrgHelper.addPartyRelation(partyId, parentRelIds[i], relType);//调用接口添加团体关系
            }
        }
        return queryAllByTypes(request, response);
    }


    private String[] split(String source,String token){
        if(source.contains(token)||source.length()>0){
            return source.split(token);
        }else{
            return new String[0];
        }
    }
}

