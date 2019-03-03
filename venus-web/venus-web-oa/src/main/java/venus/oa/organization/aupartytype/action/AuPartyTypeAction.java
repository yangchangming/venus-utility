package venus.oa.organization.aupartytype.action;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.oa.organization.aupartytype.bs.IAuPartyTypeBS;
import venus.oa.organization.aupartytype.util.IConstants;
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
 * 团体类型维护ACTION
 *  
 */
@Controller
@RequestMapping("/auPartyType")
public class AuPartyTypeAction implements IConstants {

    /**
     * 得到BS对象
     * 
     * @return BS对象
     */
    public IAuPartyTypeBS getBS() {
        return (IAuPartyTypeBS) Helper.getBean(BS_KEY);
    }

    /**
     * 添加
     * @param _request
     * @param response
     * @return
     */
    @RequestMapping("/insert")
    public String insert(HttpServletRequest _request, HttpServletResponse response) {
        IRequest request = (IRequest)new HttpRequest(_request);
        AuPartyTypeVo obj = new AuPartyTypeVo();
        if (!Helper.populate(obj, request)) {
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        boolean isGenerateCode = false;//true 生产代码 需要重启，false 不生成代码 无需重启
        obj.setTable_name((isGenerateCode?"ORGANIZE_":"COLLECTIVE_")+obj.getTable_name().toUpperCase());
        obj.setCreate_date(DateTools.getSysTimestamp());
        obj.setModify_date(obj.getCreate_date());
        obj.setId(getBS().insert(obj));
        //tony 2011-05-13 dyna party bengin
        String paraValue = request.getParameter("paraValue");
        String paraArray = request.getParameter("paraArray");
        getBS().generatePhysicsCode(paraValue,paraArray,obj);            
        //tony 2011-05-13 dyna party end
        return forWard(_request);

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
        IAuPartyTypeBS bs = getBS();
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
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
    }

   
    /**
     * 删除
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/delete")
    public String delete(HttpServletRequest request, HttpServletResponse response) {
        getBS().delete(request.getParameter("ids"));
        return forWard(request);
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
        AuPartyTypeVo obj = new AuPartyTypeVo();
        if (!Helper.populate(obj, request)) {
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        IAuPartyTypeBS bs = getBS();
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount(obj);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        String orderStr = Helper.findOrderStr(request);

        List list = new ArrayList(); //定义结果集
        list = bs.simpleQuery(pageVo.getCurrentPage(), pageVo.getPageSize(),
                orderStr, obj);
        Helper.saveOrderStr(orderStr, request);
        Helper.savePageVo(pageVo, request);
        request.setAttribute(PROJECT_VALUE, list);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools
                .getMapFromRequest((HttpServletRequest) request)); //回写表单
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
        AuPartyTypeVo obj = (AuPartyTypeVo) getBS().find(ids); //通过id获取vo
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
        AuPartyTypeVo obj = new AuPartyTypeVo();
        if (!Helper.populate(obj, request)) {
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        obj.setModify_date(DateTools.getSysTimestamp());
        getBS().update(obj);
        //tony 2011-05-30 dyna party bengin
        String paraValue = request.getParameter("paraValue");
        if(StringUtils.isNotEmpty(paraValue)){
            String paraArray = request.getParameter("paraArray");
            getBS().appendPhysicsCode(paraValue,paraArray,obj);            
        }
        //tony 2011-05-30 dyna party end
        return forWard(_request);
    }

    /**
     * 启用
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/enable")
    public String enable(HttpServletRequest request, HttpServletResponse response) {
        getBS().enable(request.getParameter("ids"));
        return forWard(request);
    }
    
    /**
     * 禁用
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/disable")
    public String disable(HttpServletRequest request, HttpServletResponse response) {
        getBS().disable(request.getParameter("ids"));
        return forWard(request);
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
        AuPartyTypeVo obj = (AuPartyTypeVo) getBS().find(id); //通过id获取vo
        VoHelperTools.null2Nothing(obj);
        request.setAttribute(REQUEST_BEAN_VALUE, obj); //把vo放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromVo(obj)); //回写表单
//        return request.findForward(FORWARD_DETAIL_KEY);
        return FORWARD_DETAIL_KEY;
    }
    
    private String forWard(HttpServletRequest request){
        String keywordconst = request.getParameter("keywordconst");
        if(StringUtils.isNotEmpty(keywordconst)){
            request.setAttribute("keywordconst", keywordconst);
//            return request.findForward(FORWARD_TO_SEARCH_KEY);
            return FORWARD_DETAIL_KEY;
        } else {
//            return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
            return "redirect:/auPartyType/queryAll";
        }
    }
}

