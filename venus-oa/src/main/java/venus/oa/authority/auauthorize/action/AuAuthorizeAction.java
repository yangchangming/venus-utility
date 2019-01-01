package venus.oa.authority.auauthorize.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.oa.authority.auauthorize.bs.IAuAuthorizeBS;
import venus.oa.authority.auauthorize.util.IConstants;
import venus.oa.authority.auauthorize.vo.AuAuthorizeVo;
import venus.oa.authority.aufunctree.bs.IAuFunctreeBs;
import venus.oa.authority.aufunctree.util.IAuFunctreeConstants;
import venus.oa.authority.aufunctree.vo.AuFunctreeVo;
import venus.oa.authority.auresource.bs.IAuResourceBs;
import venus.oa.authority.auresource.util.IAuResourceConstants;
import venus.oa.authority.auresource.vo.AuResourceVo;
import venus.oa.authority.auvisitor.bs.IAuVisitorBS;
import venus.oa.authority.auvisitor.vo.AuVisitorVo;
import venus.oa.helper.LoginHelper;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.util.DateTools;
import venus.oa.util.GlobalConstants;
import venus.frames.mainframe.util.Helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 团体类型维护ACTION
 */
@Controller
@RequestMapping("/auAuthorize")
public class AuAuthorizeAction implements IConstants {

    /**
     * 得到BS对象
     * 
     * @return BS对象
     */
    public IAuAuthorizeBS getBs() {
        return (IAuAuthorizeBS) Helper.getBean(BS_KEY);
    }


	/**
	 * save user
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
     */
	@RequestMapping("/saveAuByVisitor")
    public String saveAuByVisitor(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sIds = request.getParameter("ids");
        String sCodes = request.getParameter("codes");
        String sTypes = request.getParameter("types");
        String sStatus = request.getParameter("status");
        String sAccess = request.getParameter("access");
        String vId = request.getParameter("vId");
        String vCode = request.getParameter("vCode");
        String vType = request.getParameter("vType");
        String auType = request.getParameter("auType");
        if(sIds==null || sCodes==null || sTypes==null || sStatus==null || sAccess==null || vId==null || vCode==null || vType==null || auType==null){ 
//            return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Missing_parameter"), MessageStyle.ALERT_AND_BACK);
        	return MESSAGE_AGENT_ERROR;
		}
        //分析表单值
        String[] ids = sIds.length()>0 ? sIds.split(",") : new String[0];
        String[] codes = sCodes.length()>0 ? sCodes.split(",") : new String[0];
        String[] types = sTypes.length()>0 ? sTypes.split(",") : new String[0];
        String[] status = sStatus.length()>0 ? sStatus.split(",") : new String[0];
        String[] access = sAccess.length()>0 ? sAccess.split(",") : new String[0];
        if(ids.length!=codes.length || codes.length!=types.length || types.length!=status.length || status.length!=access.length){ 
//            return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.The_parameter_is_incorrect"), MessageStyle.ALERT_AND_BACK);
        	return MESSAGE_AGENT_ERROR;
		}
        
        List voList = new ArrayList();
        for(int i=0; i<ids.length; i++ ) { 
            AuAuthorizeVo vo = new AuAuthorizeVo();
            vo.setAccess_type(access[i]);
            vo.setIs_append("0");//没有附加数据
            vo.setVisitor_code(vCode);
            vo.setVisitor_id(vId);
            vo.setVisitor_type(vType);
	        vo.setAuthorize_status(status[i]);
	        vo.setResource_code(codes[i]);
	        vo.setResource_id(ids[i]);
	        vo.setResource_type(types[i]);
	        vo.setCreate_date(DateTools.getSysTimestamp());  //打创建时间,IP戳
	        voList.add(vo);
        }
        LoginSessionVo sessonVo = LoginHelper.getLoginVo(request);//权限上下文
        getBs().saveAu(vId, voList, auType,sessonVo);//保存授权结果
        request.setAttribute("retMessage", venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Save_successful_"));
//        return request.findForward(FORWARD_TO_AU_FRAME_KEY);
		return FORWARD_TO_AU_FRAME_KEY;
    }


	/**
	 * save organization controller
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
     */
	@RequestMapping("/saveOrgAuByRelId")
    public String saveOrgAuByRelId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获得request传来的参数
    	String addCodes = request.getParameter("add_codes");
    	String delCodes = request.getParameter("del_codes");//所有勾掉的id（包括后来又勾上的）
    	String types = request.getParameter("types");
    	String names = request.getParameter("names");
    	String relId = request.getParameter("relId");
    	String pType = request.getParameter("pType");
    	if(addCodes==null || delCodes==null || relId==null || types==null || names==null || pType==null) { 
//            return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Missing_parameter"), MessageStyle.ALERT_AND_BACK);
        	return MESSAGE_AGENT_ERROR;
		}
        //分析参数，首先取到所有要添加的编号列表
		String[] addCodeArray = addCodes.length()>0 ? addCodes.split(",") : new String[0];
		String[] sTypes = types.length()>0 ? types.split(",") : new String[0];
        String[] sNames = names.length()>0 ? names.split(",") : new String[0];
        if(addCodeArray.length!=sTypes.length || sTypes.length!=sNames.length){ 
//            return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.The_parameter_is_incorrect"), MessageStyle.ALERT_AND_BACK);
        	return MESSAGE_AGENT_ERROR;
		}
		//接着取到所有真正取消打勾的节点的编号（即不包括后来又勾上的）
    	ArrayList delCodeList = new ArrayList();
    	if(delCodes.length()>0) {
    		String allDelCodeArray[] = delCodes.split(",");
    		for(int i=0; i<allDelCodeArray.length; i++) {
    			boolean flag = true;
    			for(int j=0; j<addCodeArray.length; j++) {
    				if(allDelCodeArray[i].equals(addCodeArray[j])) {
    					flag = false;
    					break;
    				}
    			}
    			if(flag) {
    			    delCodeList.add(allDelCodeArray[i]);
    			}
    		}
		}
    	String delCodeArray[] = (String[])delCodeList.toArray(new String[0]);
    	//将团体关系id转化成访问者vo
    	IAuVisitorBS visiBs = (IAuVisitorBS) Helper.getBean(venus.oa.authority.auvisitor.util.IConstants.BS_KEY);
    	AuVisitorVo visiVo = visiBs.queryByRelationId(relId, pType);
    	LoginSessionVo sessonVo = LoginHelper.getLoginVo(request);//权限上下文

    	//分析并保存针对组织机构的授权结果
    	getBs().saveOrgAu(visiVo.getId(), visiVo.getCode(), visiVo.getVisitor_type(), addCodeArray, delCodeArray, sNames, sTypes,sessonVo);
    	request.setAttribute("retMessage", venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Save_successful_"));
//        return request.findForward(FORWARD_TO_AU_FRAME_KEY);
		return FORWARD_TO_AU_FRAME_KEY;
    }


	@RequestMapping("/getFuncAu")
    public String getFuncAu(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String relId = request.getParameter("relId");
        String pType = request.getParameter("pType");
    	if(relId==null || pType==null){ 
//            return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Missing_parameter"), MessageStyle.ALERT_AND_BACK);
        	return MESSAGE_AGENT_ERROR;
		}
    	doFuncAu(request,response);
//        return request.findForward(FORWARD_TO_AU_FUNC_KEY);
		return FORWARD_TO_AU_FUNC_KEY;
    }
    
    private void doFuncAu(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String relId = request.getParameter("relId");
        String pType = request.getParameter("pType");
        
    	String fType = GlobalConstants.getResType_menu();//功能菜单
    	String bType = GlobalConstants.getResType_butn();//功能按钮

        //将团体关系id转化成访问者vo
    	IAuVisitorBS visiBs = (IAuVisitorBS) Helper.getBean(venus.oa.authority.auvisitor.util.IConstants.BS_KEY);
    	AuVisitorVo visiVo = visiBs.queryByRelationId(relId, pType);
    	//获取该访问者自身拥有权限的节点
    	Map selMap = getBs().getAuByVisitorId(visiVo.getId(),null);
    	//获取该访问者继承权限的节点
    	Map extMap = getBs().getExtendAuByVisitorCode(visiVo.getCode(), null);
 
    	//获取全部功能节点
    	String strSql = "TOTAL_CODE like'101%' AND (TYPE='"+fType+"' OR TYPE='"+bType+"')";
    	IAuFunctreeBs funcBs = (IAuFunctreeBs) Helper.getBean(IAuFunctreeConstants.BS_KEY);
    	List lFunctree = funcBs.queryByCondition(strSql, "tree_level, order_code");

    	//过滤权限，实现分级授权
    	if( ! LoginHelper.getIsAdmin(request)) {
			Map auMap = LoginHelper.getOwnerFunc4Admin(request);
			Iterator itLTree = lFunctree.iterator();
			if(itLTree.hasNext()) {
				itLTree.next();//根节点不过滤
			}
			while( itLTree.hasNext() ) {
				AuFunctreeVo vo = (AuFunctreeVo) itLTree.next();
			    if(auMap==null || ! auMap.keySet().contains(vo.getTotal_code())) {
					itLTree.remove();
				}
			}
		}
    	
    	//获取公开访问的节点
    	IAuResourceBs resBs = (IAuResourceBs) Helper.getBean(IAuResourceConstants.BS_KEY);
    	List lResource = resBs.queryByCondition("IS_PUBLIC ='1' and ENABLE_STATUS='1' and (RESOURCE_TYPE='"+fType+"' or RESOURCE_TYPE='"+bType+"')");
    	Map resMap = new HashMap();
    	for(Iterator itLResource = lResource.iterator(); itLResource.hasNext(); ) {
    		AuResourceVo resVo = (AuResourceVo) itLResource.next();
            resMap.put(resVo.getId(), "");
        }
    	request.setAttribute("EXT_AU_MAP", extMap);
    	request.setAttribute("SEL_AU_MAP", selMap);
    	request.setAttribute("FUNC_LIST", lFunctree);
    	request.setAttribute("PUB_RES_MAP", resMap);
    	
    	request.setAttribute("vId", visiVo.getId());
    	request.setAttribute("vCode", visiVo.getCode());
    	request.setAttribute("vType", visiVo.getVisitor_type());
    }


	@RequestMapping("/getDataAu")
    public String getDataAu(HttpServletRequest request, HttpServletResponse response) throws Exception {
       
        String relId = request.getParameter("relId");
        String pType = request.getParameter("pType");
        String rType = request.getParameter("rType");
    	if(relId==null || pType==null  || rType==null){ 
//            return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Missing_parameter"), MessageStyle.ALERT_AND_BACK);
        	return MESSAGE_AGENT_ERROR;
		}
        //将团体关系id转化成访问者vo
    	IAuVisitorBS visiBs = (IAuVisitorBS) Helper.getBean(venus.oa.authority.auvisitor.util.IConstants.BS_KEY);
    	AuVisitorVo visiVo = visiBs.queryByRelationId(relId, pType);
    	//获取该访问者自身拥有权限的节点
    	Map selMap = getBs().getAuByVisitorId(visiVo.getId(),rType);
    	//获取该访问者继承权限的节点
    	Map extMap = getBs().getExtendAuByVisitorCode(visiVo.getCode(),rType);

    	//获取全部记录资源
    	IAuResourceBs resBs = (IAuResourceBs) Helper.getBean(IAuResourceConstants.BS_KEY);
    	List lResource = resBs.queryByCondition("RESOURCE_TYPE='"+rType+"' and ENABLE_STATUS='1'");
    
    	//过滤权限，实现分级授权
    	if( ! LoginHelper.getIsAdmin((HttpServletRequest)request)) {
    	    Map auMap = null;
    	    if(GlobalConstants.getResType_fild().equals(rType)) {
    	        auMap = LoginHelper.getOwnerFild4Admin((HttpServletRequest)request);
    	    }else if(GlobalConstants.getResType_recd().equals(rType)) {
    	        auMap = LoginHelper.getOwnerRecd4Admin((HttpServletRequest)request);
    	    }
			for(Iterator it = lResource.iterator(); it.hasNext(); ) {
				AuResourceVo vo = (AuResourceVo) it.next();
			    if(auMap==null ||  ! auMap.keySet().contains(vo.getId())) {
					it.remove();
				}
			}
		}
    	
    	//获取公开访问的记录资源
    	List lPubRes = resBs.queryByCondition("IS_PUBLIC ='1' and ENABLE_STATUS='1' and RESOURCE_TYPE='"+rType+"'");
    	Map resMap = new HashMap();
    	for(Iterator it = lPubRes.iterator(); it.hasNext(); ) {
    		AuResourceVo resVo = (AuResourceVo) it.next();
            resMap.put(resVo.getId(), null);
        }
    	request.setAttribute("EXT_AU_MAP", extMap);
    	request.setAttribute("SEL_AU_MAP", selMap);
    	request.setAttribute("RES_LIST", lResource);
    	request.setAttribute("PUB_RES_MAP", resMap);
    	
    	request.setAttribute("vId", visiVo.getId());
    	request.setAttribute("vCode", visiVo.getCode());
    	request.setAttribute("vType", visiVo.getVisitor_type());
    	
    	if(GlobalConstants.getResType_fild().equals(rType)) {
//    	    return request.findForward(FORWARD_TO_AU_FILD_KEY);
			return FORWARD_TO_AU_FILD_KEY;
    	}else {
//    	    return request.findForward(FORWARD_TO_AU_RECD_KEY);
			return FORWARD_TO_AU_RECD_KEY;
    	}
    }


    @RequestMapping("/viewFuncAu")
    public String viewFuncAu(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	doViewFuncOrgAu(request, response);
//        return request.findForward(FORWARD_TO_VIEW_FUNC_KEY);
		return FORWARD_TO_VIEW_FUNC_KEY;
    }
    
    private void doViewFuncOrgAu(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String vCode = request.getParameter("vCode");
        String isUser = request.getParameter("isUser");
        String partyId = request.getParameter("partyId");
    	String fType = GlobalConstants.getResType_menu();//功能菜单
    	String bType = GlobalConstants.getResType_butn();//功能按钮
    	
    	//获取访问者拥有的权限
    	Map allMap = null;
    	if ("1".equals(isUser)){
    	    allMap = getBs().getAuByPartyId(partyId,null);
    	}else{
    	    allMap = getBs().getAuByVisitorCode(vCode,null);
    	}
    	    
   
    	//获取全部功能节点
    	String strSql = "TOTAL_CODE like'101%' AND (TYPE='"+fType+"' OR TYPE='"+bType+"')";
    	IAuFunctreeBs funcBs = (IAuFunctreeBs) Helper.getBean(IAuFunctreeConstants.BS_KEY);
    	List lFunctree = funcBs.queryByCondition(strSql, "tree_level, order_code");

    	//获取公开访问的节点
    	IAuResourceBs resBs = (IAuResourceBs) Helper.getBean(IAuResourceConstants.BS_KEY);
    	List lResource = resBs.queryByCondition("IS_PUBLIC ='1' and ENABLE_STATUS='1' and (RESOURCE_TYPE='"+fType+"' or RESOURCE_TYPE='"+bType+"')");
    	Map resMap = new HashMap();
    	for(Iterator itLResource = lResource.iterator(); itLResource.hasNext(); ) {
    		AuResourceVo resVo = (AuResourceVo) itLResource.next();
            resMap.put(resVo.getId(), "");
        }
    	request.setAttribute("ALL_AU_MAP", allMap);
    	request.setAttribute("FUNC_LIST", lFunctree);
    	request.setAttribute("PUB_RES_MAP", resMap);
    }


	@RequestMapping("/viewDataAu")
    public String viewDataAu(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String vCode = request.getParameter("vCode");
        String isUser = request.getParameter("isUser");
        String partyId = request.getParameter("partyId");
    	String rType = request.getParameter("rType");
    	
    	//获取该访问者自身拥有权限的节点
    	Map allMap = null;
    	if ("1".equals(isUser)){
    	    allMap = getBs().getAuByPartyId(partyId, rType);
    	}else{
    	    allMap = getBs().getAuByVisitorCode(vCode,rType);
    	}
    	//获取全部记录资源
    	IAuResourceBs resBs = (IAuResourceBs) Helper.getBean(IAuResourceConstants.BS_KEY);
    	List lResource = resBs.queryByCondition("RESOURCE_TYPE='"+rType+"' and ENABLE_STATUS='1'");
    	
    	//获取公开访问的记录资源
    	List lPubRes = resBs.queryByCondition("IS_PUBLIC ='1' and ENABLE_STATUS='1' and RESOURCE_TYPE='"+rType+"'");
    	Map resMap = new HashMap();
    	for(Iterator it = lPubRes.iterator(); it.hasNext(); ) {
    		AuResourceVo resVo = (AuResourceVo) it.next();
            resMap.put(resVo.getId(), "");
        }
    	request.setAttribute("ALL_AU_MAP", allMap);
    	request.setAttribute("RES_LIST", lResource);
    	request.setAttribute("PUB_RES_MAP", resMap);
    	if(GlobalConstants.getResType_fild().equals(rType)) {
//    	    return request.findForward(FORWARD_TO_VIEW_FILD_KEY);
			return FORWARD_TO_VIEW_FILD_KEY;
    	}else {
//    	    return request.findForward(FORWARD_TO_VIEW_RECD_KEY);
			return FORWARD_TO_VIEW_RECD_KEY;
    	}
    }
    
}

