package venus.portal.gbox.resource.classificationrelation.action;

import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.util.Helper;
import venus.frames.mainframe.util.VoHelper;
import venus.frames.web.page.PageVo;
import venus.portal.gbox.resource.classificationrelation.bs.IClassificationRelationBs;
import venus.portal.gbox.resource.classificationrelation.util.IClassificationRelationConstants;
import venus.portal.gbox.resource.classificationrelation.vo.ClassificationRelationVo;
import venus.portal.gbox.util.DateTools;
import venus.portal.gbox.util.GlobalConstant;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClassificationRelationAction extends DefaultDispatchAction implements IClassificationRelationConstants {
    
    /**
     * 得到BS对象
     * 
     * @return BS对象
     */
    public IClassificationRelationBs getBs() {
        return (IClassificationRelationBs) Helper.getBean(BS_KEY);  //得到BS对象,受事务控制
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
    public IForward relativeResource(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String classId = request.getParameter("classId");
        String resourceIds = request.getParameter("resourceIds");
        String[] resourceIdArr = (resourceIds != null && !"".equals(resourceIds)) ? resourceIds.split(",") : new String[0];
        ClassificationRelationVo vo = new ClassificationRelationVo();
        vo.setCreateDate(DateTools.getSysTimestamp());
        vo.setClassificationId(classId);
        for (int i = 0; i < resourceIdArr.length; i++) {
            vo.setResourceId(resourceIdArr[i]);
            getBs().insert(vo);
        }
        request.setAttribute("returnClassId", classId);
        return request.findForward(FORWARD_RESOURCE_FRAME);
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
        String classId = request.getParameter("classId");
        String resourceIds = request.getParameter("resourceIds");
        String[] resourceIdArr = (resourceIds != null && !"".equals(resourceIds)) ? resourceIds.split(",") : new String[0];
        ClassificationRelationVo vo = new ClassificationRelationVo();
        vo.setClassificationId(classId);
        for (int i = 0; i < resourceIdArr.length; i++) {
            vo.setResourceId(resourceIdArr[i]);
            getBs().delete(vo);
        }        
        return queryByCondition(formBean,request,response);
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
    public IForward relativeClassification(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String classIds = request.getParameter("classIds");
        String resourceIds = request.getParameter("resourceIds");
        String[] classIdArr = (classIds != null && !"".equals(classIds)) ? classIds.split(",") : new String[0];
        String[] resourceIdArr = (resourceIds != null && !"".equals(resourceIds)) ? resourceIds.split(",") : new String[0];
        ClassificationRelationVo vo = new ClassificationRelationVo();
        vo.setCreateDate(DateTools.getSysTimestamp());
        for (int i = 0; i < classIdArr.length; i++) {
            for (int j = 0; j < resourceIdArr.length; j++) {
                String queryCondition = " AND B.CLASSIFICATION_ID = '" + classIdArr[i] + "' AND B.RESOURCE_ID = '" + resourceIdArr[j] + "'";
                if (getBs().getResourceRecordCount(queryCondition) == 0) {
                    vo.setClassificationId(classIdArr[i]);
                    vo.setResourceId(resourceIdArr[j]);
                    getBs().insert(vo);
                }
            }
        }
        request.setAttribute("returnStatus", GlobalConstant.RETURNSTATUS_SUCCESS);
        return request.findForward(FORWARD_CLASSIFICATION_FRAME);
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
        String classId = request.getParameter("classId");  //当前选中的目录
        String name = request.getParameter("name");      //名称
        String type = request.getParameter("type");         //资源类型
        request.setAttribute("currentViewModel", request.getParameter("currentViewModel"));
        String queryCondition = "";
        
        if (classId == null || "".equals(classId) || "undefined".equals(classId)){
            classId = GlobalConstant.getRootNodeCode();
        }
            queryCondition += " AND B.CLASSIFICATION_ID = '" + classId + "'";
        if (name != null && !"".equals(name))
            queryCondition += " AND A.NAME LIKE '%" + name + "%'";       
        if (type != null && !"".equals(type))
            queryCondition += " AND A.TYPE = '" + type + "'";         
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = getBs().getResourceRecordCount(queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }        
        String orderStr = Helper.findOrderStr(request);
        List list = getBs().queryResourceByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition, orderStr);
        Helper.saveOrderStr(orderStr, request);
        Helper.savePageVo(pageVo, request);
        request.setAttribute("classId", classId);
        request.setAttribute("viewMode", request.getParameter("viewMode"));
        request.setAttribute(REQUEST_BEANS, list); 
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelper.getMapFromRequest((HttpServletRequest) request));
        return request.findForward(FORWARD_LIST_PAGE);
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
    public IForward queryRelationByCondition(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String classId = request.getParameter("classId");
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        Map queryCondition = new HashMap();
        queryCondition.put(QUERYCONDITION_CLASSID, classId);
        queryCondition.put(QUERYCONDITION_NAME, name);
        queryCondition.put(QUERYCONDITION_TYPE, type);
        PageVo pageVo = Helper.findPageVo(request);  //得到当前翻页信息
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = getBs().getRelationRecordCount(queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }        
        String orderStr = Helper.findOrderStr(request);  //得到排序信息
        List list = getBs().queryRelationByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition, orderStr);
        Helper.saveOrderStr(orderStr, request);  //保存排序信息
        Helper.savePageVo(pageVo, request);
        request.setAttribute(REQUEST_BEANS, list);  //把结果集放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelper.getMapFromRequest((HttpServletRequest) request)); //回写表单
        return request.findForward(FORWARD_LIST_RELATIVE);
    }     
}
