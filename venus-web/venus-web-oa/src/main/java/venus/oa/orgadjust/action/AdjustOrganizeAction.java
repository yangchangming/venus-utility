package venus.oa.orgadjust.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.frames.base.exception.BaseApplicationException;
import venus.oa.helper.LoginHelper;
import venus.oa.orgadjust.bs.IAdjustOrganizeBs;
import venus.oa.orgadjust.util.IContants;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.util.ProjTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/adjustOrganize")
public class AdjustOrganizeAction implements IContants {

	@Autowired
	private IAdjustOrganizeBs adjustOrganizeBs;

	@Autowired
	private IAuPartyRelationBs auPartyRelationBs;

    /**
     * 调级功能
     */
	@RequestMapping("/updateRelation")
    public String updateRelation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//获得调级功能参数
    	String orgId = LoginHelper.getPartyId(request);
    	String srcrelationid = request.getParameter("srcrelationid");
    	AuPartyRelationVo relationVo = auPartyRelationBs.find(srcrelationid);
    	String oldNodeCode = relationVo.getCode();
    	String newNodeCode = request.getParameter("newNodeCode");
    	if(orgId == null || oldNodeCode == null || newNodeCode == null) { 
//            return MessageAgent.sendErrorMessage(request, "参数不能为空值", MessageStyle.ALERT_AND_BACK);
			return MESSAGE_AGENT_ERROR;
        }
    	if(oldNodeCode.equals(newNodeCode) || oldNodeCode.substring(0,oldNodeCode.length()-5).equals(newNodeCode)){ 
    		request.setAttribute("adjustStatus", "0"); //调级状态,0为调级业务失败
    		request.setAttribute("failCause","原节点与目标节点位置相同！");
//    		return request.findForward(FORWARD_TO_ORGTREE);
			return FORWARD_TO_ORGTREE;
        }
    	try {
    		adjustOrganizeBs.updateRelation(orgId,oldNodeCode,newNodeCode);
    		//
    		String oldKeyWorld = relationVo.getRelationtype_keyword();
    		String relaType= relationVo.getRelationtype_id();
    		  String strRelationSql = "update au_partyrelation set relationtype_keyword='"+oldKeyWorld
               +"'   where RELATIONTYPE_ID='"+relaType+"' and CODE='"+newNodeCode+"' " ;//(select code from au_partyrelation where id='"+parentRelationId+"')";
               ProjTools.getCommonBsInstance().doUpdate(strRelationSql);
    		//
    		request.setAttribute("adjustStatus", "1"); //调级状态,1为调级成功
    	} catch (BaseApplicationException e) {
    		e.printStackTrace();
    		request.setAttribute("adjustStatus", "0"); //调级状态,0为调级业务失败
    		request.setAttribute("failCause",e.getMessage());
    	} catch (Exception e) {
    		e.printStackTrace();
    		request.setAttribute("adjustStatus", "-1"); //调级状态,-1为调级程序失败
    	}
//    	return request.findForward(FORWARD_TO_ORGTREE);
		return FORWARD_TO_ORGTREE;
    }
	
}

