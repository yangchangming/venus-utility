package venus.portal.gbox.resource.classification.action;

import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.util.Helper;
import venus.frames.mainframe.util.VoHelper;
import venus.frames.web.message.MessageAgent;
import venus.frames.web.message.MessageStyle;
import venus.portal.gbox.resource.classification.bs.IClassificationBs;
import venus.portal.gbox.resource.classification.util.IClassificationConstants;
import venus.portal.gbox.resource.classification.vo.ClassificationVo;
import venus.portal.gbox.util.DateTools;
import venus.portal.gbox.util.GlobalConstant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

public class ClassificationAction extends DefaultDispatchAction implements IClassificationConstants {

    public IClassificationBs getBs() {
        return (IClassificationBs) Helper.getBean(BS_KEY);
    }
    
    /**
     * 查找下级节点
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward queryNode(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        HttpServletResponse res = (HttpServletResponse)response;
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = res.getWriter();
        String parentCode = request.getParameter("id"); //在展开节点时，wdtree向后台传入id
        String queryCondition;
        if (parentCode == null || "".equals(parentCode)) 
            queryCondition = " AND PARENT_CODE = '0' ";
        else
            queryCondition = " AND PARENT_CODE = '" + parentCode + "'";
        writer.write(getBs().queryNode(queryCondition));
        writer.flush();
        return null;
    }       
    
    /**
     * 从页面的表单获取单条记录id，查出这条记录的值，并跳转到修改页面
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward find(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        ClassificationVo vo = new ClassificationVo();
        vo.setParentCode(request.getParameter("selfCode"));
        request.setAttribute(REQUEST_BEAN, getBs().find(vo));  //把vo放入request
        return request.findForward(FORWARD_UPDATE_PAGE);
    }    
    
    /**
     * 从页面表单获取信息注入vo，并插入单条记录
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward insertRoot(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        ClassificationVo vo = new ClassificationVo();
        if (!Helper.populate(vo, request)) {
            return MessageAgent.sendErrorMessage(request,
                    DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
        }         
        getBs().insertRoot(vo);  //插入单条记录
        return request.findForward(FORWARD_TO_QUERY_ALL);
    }    
    
    /**
     * 从页面表单获取信息注入vo，并插入单条记录
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward insert(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        ClassificationVo vo = new ClassificationVo();    
        if (!Helper.populate(vo, request)) {
            return MessageAgent.sendErrorMessage(request,
                    DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
        }         
        vo.setSelfCode(request.getParameter("parentCode"));
        getBs().insert(vo);
        return request.findForward(FORWARD_TO_QUERY_ALL);
    }    
    
    /**
     * 从页面表单获取信息注入vo，并修改单条记录
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward update(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        ClassificationVo vo = new ClassificationVo();
        vo.setModifyDate(DateTools.getSysTimestamp());
        if (!Helper.populate(vo, request)) {
            return MessageAgent.sendErrorMessage(request,
                    DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
        }        
        getBs().update(vo);  //更新单条记录
        return request.findForward(FORWARD_TO_QUERY_ALL);
    }    
    
    /**
     * 从页面的表单获取多条记录，并删除多条记录
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward delete(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String selfCode = request.getParameter("selfCode");  
        String parentCode = request.getParameter("parentCode");
        if (selfCode != null) 
            getBs().deleteMulti(selfCode);
        String queryCondition = " AND PARENT_CODE = '" + parentCode + "'";
        List list = getBs().queryByCondition(queryCondition);  //按条件查询全部,带排序
        if (list.size() == 0 && !GlobalConstant.getRootNodeCode().equals(selfCode)) {
            ClassificationVo vo = new ClassificationVo();
            vo.setParentCode(parentCode);
            ClassificationVo parentNodeVo = getBs().find(vo);
            parentNodeVo.setIsLeaf("1");
            getBs().updateLeaf(parentNodeVo);
        }
        return request.findForward(FORWARD_TO_QUERY_ALL);
    }    
    
    /**
     * 查询全部记录，分页显示，支持页面上触发的后台排序
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward queryAll(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        request.setAttribute(REQUEST_QUERY_CONDITION, "");
        simpleQuery(formBean, request, response);
        return request.findForward(FORWARD_MGT_PAGE);
    }
    
    /**
     * 简单查询，分页显示，支持表单回写
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward simpleQuery(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String queryCondition = "";
        List list = getBs().queryByCondition(queryCondition);  //按条件查询全部,带排序
        request.setAttribute(REQUEST_BEANS, list);  //把结果集放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelper.getMapFromRequest((HttpServletRequest) request)); //回写表单
        return request.findForward(FORWARD_MGT_PAGE);
    }     
    
    /**
     * 简单查询，分页显示，支持表单回写
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward queryByCondition(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String name = request.getParameter("name");
        String queryCondition = " AND IS_LEAF = '1' AND ENABLE_STATUS = '1' ";
        if (name != null && !"".equals(name))
            queryCondition += " AND NAME LIKE '%" + name + "%'";
        List list = getBs().queryByCondition(queryCondition);  //按条件查询全部,带排序
        request.setAttribute(REQUEST_BEANS, list);  //把结果集放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelper.getMapFromRequest((HttpServletRequest) request)); //回写表单
        return request.findForward(FORWARD_REFERENCE_PAGE);
    }    
    
    /**
     * 简单查询，分页显示，支持表单回写
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward queryReference(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        queryByCondition(formBean,request,response);
        return request.findForward(FORWARD_REFERENCE_PAGE);
    }
    
}
