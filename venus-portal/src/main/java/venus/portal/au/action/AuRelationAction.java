package venus.portal.au.action;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.util.Helper;
import venus.portal.au.bs.IAuRelationBS;
import venus.portal.au.util.IConstants;
import venus.portal.util.IEwpToolsConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: qj
 * Date: 13-10-22
 * Time: 上午10:30
 * To change this template use File | Settings | File Templates.
 */
public class AuRelationAction extends DefaultDispatchAction implements IConstants,IEwpToolsConstants {

    public IAuRelationBS getAuRelationBs() {
        return (IAuRelationBS) Helper.getBean(AU_RELATION_BS);
    }
    /**
     * 保存或更新权限关系
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward saveOrUpdateDoctypeAu(DefaultForm from, IRequest request, IResponse response) throws Exception {
        HttpServletRequest hRequest = (HttpServletRequest) request.getServletRequest();
        String isPass="Y";
        try{
            String pType = hRequest.getParameter("pType");
            String partyId = hRequest.getParameter("partyId");
            String docTypeIds = hRequest.getParameter("docTypeIds");
            String websitId = hRequest.getParameter("websitId");

            String[] docTypeIdStrs = docTypeIds.split(",");
            getAuRelationBs().saveOrUpdateDoctypeRelations(pType,partyId,websitId,docTypeIdStrs);
        }catch (Exception e){
            logger.error(e.getMessage());
            isPass="N";
        }finally {
            JSONObject jsonO=new JSONObject();
            jsonO.put("isPass",isPass);
            response.getServletResponse().setContentType("text/plain; charset=UTF-8");
            String returnValue = jsonO.toString();
            response.getServletResponse().getWriter().print(returnValue);
            response.getServletResponse().getWriter().flush();

            return DEFAULT_FORWARD;
        }
    }

    /**
     * 获取网站的栏目权限数据
     */
    public IForward queryDoctypeAuByPartyAndWebSite(DefaultForm from, IRequest request, IResponse response) throws Exception {
        HttpServletRequest hRequest = (HttpServletRequest) request.getServletRequest();
        String isPass="Y";
        String docTypeIds = "";
        try{
            String pType = hRequest.getParameter("pType");
            String partyId = hRequest.getParameter("partyId");
            String websitId = hRequest.getParameter("websitId");

            List doctype_list = getAuRelationBs().queryDoctypes(pType,partyId,websitId);
            docTypeIds = StringUtils.join(doctype_list,',');
        }catch (Exception e){
            logger.error(e.getMessage());
            isPass="N";
        }finally {
            JSONObject jsonO=new JSONObject();
            jsonO.put("isPass",isPass);
            jsonO.put("docTypeIds",docTypeIds);
            response.getServletResponse().setContentType("text/plain; charset=UTF-8");
            String returnValue = jsonO.toString();
            response.getServletResponse().getWriter().print(returnValue);
            response.getServletResponse().getWriter().flush();

            return DEFAULT_FORWARD;
        }
    }
}
