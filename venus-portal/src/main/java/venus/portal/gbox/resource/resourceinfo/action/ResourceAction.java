package venus.portal.gbox.resource.resourceinfo.action;

import org.apache.commons.lang.StringUtils;
import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.util.Helper;
import venus.frames.mainframe.util.VoHelper;
import venus.frames.web.message.MessageAgent;
import venus.frames.web.message.MessageStyle;
import venus.frames.web.page.PageVo;
import venus.portal.gbox.resource.api.ResourceTypeAPI;
import venus.portal.gbox.resource.resourceinfo.bs.IResourceBs;
import venus.portal.gbox.resource.resourceinfo.util.IResourceConstants;
import venus.portal.gbox.resource.resourceinfo.vo.ResourceVo;
import venus.pub.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResourceAction extends DefaultDispatchAction implements IResourceConstants {
    
    /**
     * 得到BS对象
     * 
     * @return BS对象
     */
    public IResourceBs getBs() {
        return (IResourceBs) Helper.getBean(BS_KEY);  //得到BS对象,受事务控制
    }
    
    /**
     * 从页面的表单获取多条记录id，并删除多条记录
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward deleteMulti(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String[] id = StringUtil.split(request.getParameter("ids"), ",");
        String deleteType = request.getParameter("deleteType");
        String currentViewModel = request.getParameter("currentViewModel");
        String currentNode = request.getParameter("currentNode");
        request.setAttribute("currentViewModel", currentViewModel);
        request.setAttribute("classId", currentNode);
        String returnForward = FORWARD_TO_QUERY_ALL;
        if(StringUtils.isNotBlank(currentViewModel)){
            returnForward = "toRelationedListPage";
        }
        getBs().deleteMulti(id, deleteType);
        return request.findForward(returnForward);
    }
    
    /**
     * 从页面的表单获取多条记录id，并删除多条记录
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward deleteAll(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        getBs().deleteAll();
        return request.findForward(FORWARD_TO_QUERY_ALL);
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
        String id = request.getParameter("id");
        ResourceVo vo = getBs().find(id);
        if (ResourceTypeAPI.getImageType().equals(vo.getType())) { //跳转到ImageAction
            return request.findForward(FORWARD_TOIMAGEFIND_ACTION);
        } else if (ResourceTypeAPI.getVideoType().equals(vo.getType())) { //跳转到videoAction
            return request.findForward(FORWARD_TOVIEDOFIND_ACTION);
        } else if (ResourceTypeAPI.getDocType().equals(vo.getType())) { //跳转到docAction
            return request.findForward(FORWARD_TODOCFIND_ACTION);
        } else if (ResourceTypeAPI.getAudioType().equals(vo.getType())) { //跳转到audioAction
            return request.findForward(FORWARD_TOAUDIOFIND_ACTION);
        }        
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
    public IForward findVo(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String id = request.getParameter("id");
        ResourceVo vo = getBs().find(id);
        request.setAttribute("resourceVo", vo);
        return request.findForward(FORWARD_TAGREFERENCE);
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
    public IForward update(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String id = request.getParameter("id");
        ResourceVo vo = getBs().find(id);
        if (ResourceTypeAPI.getImageType().equals(vo.getType())) { //跳转到ImageAction
            return request.findForward(FORWARD_TOIMAGEUPDATE_ACTION);
        } else if (ResourceTypeAPI.getVideoType().equals(vo.getType())) { //跳转到videoAction
            return request.findForward(FORWARD_TOVIEDOUPDATE_ACTION);
        } else if (ResourceTypeAPI.getDocType().equals(vo.getType())) { //跳转到docAction
            return request.findForward(FORWARD_TODOCUPDATE_ACTION);
        } else if (ResourceTypeAPI.getAudioType().equals(vo.getType())) { //跳转到audioAction
            return request.findForward(FORWARD_TOAUDIOUPDATE_ACTION);
        }  
        simpleQuery(formBean, request, response);
        return request.findForward(FORWARD_LIST_PAGE);
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
    public IForward updateTag(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String id = request.getParameter("id");
        ResourceVo vo = getBs().find(id);
        if (ResourceTypeAPI.getImageType().equals(vo.getType())) { //跳转到ImageAction
            return request.findForward(FORWARD_TOIMAGEUPDATETAG_ACTION);
        } else if (ResourceTypeAPI.getVideoType().equals(vo.getType())) { //跳转到videoAction
            return request.findForward(FORWARD_TOVIEDOUPDATETAG_ACTION);
        } else if (ResourceTypeAPI.getDocType().equals(vo.getType())) { //跳转到docAction
            return request.findForward(FORWARD_TODOCUPDATETAG_ACTION);
        } else if (ResourceTypeAPI.getAudioType().equals(vo.getType())) { //跳转到audioAction
            return request.findForward(FORWARD_TOAUDIOUPDATETAG_ACTION);
        }
        return request.findForward(FORWARD_TO_QUERY_ALL);
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
    public IForward updateTagToQuerry(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String id = request.getParameter("id");
        ResourceVo vo = getBs().find(id);
        if (ResourceTypeAPI.getImageType().equals(vo.getType())) { //跳转到ImageAction
            return request.findForward(FORWARD_TOIMAGEUPDATETAGTOQUERRY_ACTION);
        } else if (ResourceTypeAPI.getVideoType().equals(vo.getType())) { //跳转到videoAction
            return request.findForward(FORWARD_TOVIEDOUPDATETAGTOQUERRY_ACTION);
        } else if (ResourceTypeAPI.getDocType().equals(vo.getType())) { //跳转到docAction
            return request.findForward(FORWARD_TODOCUPDATETAGTOQUERRY_ACTION);
        } else if (ResourceTypeAPI.getAudioType().equals(vo.getType())) { //跳转到audioAction
            return request.findForward(FORWARD_TOAUDIOUPDATETAGTOQUERRY_ACTION);
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
        return request.findForward(FORWARD_LIST_PAGE);
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
    public IForward queryReference(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        request.setAttribute(REQUEST_QUERY_CONDITION, "");
        simpleQuery(formBean, request, response);
        return request.findForward(FORWARD_RESOURCEFEFERENCE);
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
        String name = request.getParameter("name");//名称
        String type = request.getParameter("type");//类型
        String tag = request.getParameter("tag");//标签
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");     
        String sDate = request.getParameter("log_date_from");
        String eDate = request.getParameter("log_date_to");
        Date startDate =  (sDate != null && !"".equals(sDate)) ? format.parse(sDate) : null;
        Date endDate =  (eDate !=null  && !"".equals(eDate)) ? format.parse(eDate) : null;
        ResourceVo vo = new ResourceVo();
        if (!Helper.populate(vo, request)) {
            return MessageAgent.sendErrorMessage(request,
                    DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
        }
        if (name != null && !"".equals(name)) {
            name = StringUtil.trimString(name);
            vo.setName("%" + name + "%");
        }
        if (type != null && !"".equals(type)) {
            vo.setType(type);
        }
        if (tag != null && !"".equals(tag)) {
            tag = StringUtil.trimString(tag);
            vo.setTag("%" + tag + "%");
        }        
        if (startDate != null) {
            vo.setStardDate(startDate);
        }
        if (endDate != null) {
            vo.setEndDate(endDate);
        }
        PageVo pageVo = Helper.findPageVo(request);  //得到当前翻页信息
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = getBs().getRecordCount(vo);
            pageVo = Helper.createPageVo(request, recordCount);
        }        
        String orderStr = Helper.findOrderStr(request);  //得到排序信息
        List beans = new ArrayList();
        beans = getBs().queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), vo, orderStr);  //按条件查询全部,带排序
        Helper.saveOrderStr(orderStr, request);  //保存排序信息
        Helper.savePageVo(pageVo, request);
        request.setAttribute(REQUEST_BEANS, beans);  //把结果集放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelper.getMapFromRequest((HttpServletRequest) request)); //回写表单
        return request.findForward(FORWARD_LIST_PAGE);
    }
    
    /**
     * 资源上传
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */    
    public IForward upload(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String resourceType = request.getParameter("resourceType");
        if (ResourceTypeAPI.getImageType().equals(resourceType)) { //跳转到ImageAction
            return request.findForward(FORWARD_TOIMAGEUPLOAD_ACTION);
        } else if (ResourceTypeAPI.getVideoType().equals(resourceType)) { //跳转到videoAction
            return request.findForward(FORWARD_TOVIEDOUPLOAD_ACTION);
        } else if (ResourceTypeAPI.getDocType().equals(resourceType)) { //跳转到docAction
            return request.findForward(FORWARD_TODOCUPLOAD_ACTION);
        } else if (ResourceTypeAPI.getAudioType().equals(resourceType)) { //跳转到audioAction
            return request.findForward(FORWARD_TOAUDIOUPLOAD_ACTION);
        }
        return null;
    }
    
    public IForward queryResource(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String resourceType = request.getParameter("resourceType");
        String gboxSourceName = request.getParameter("gboxSourceName");      
        String txtUrl = request.getParameter("txtUrl");//回写资源url的引用页面的id
        String txtGboxLnkUrl = request.getParameter("txtGboxLnkUrl");//回写资源url的fck图片插件引用页面的id
        ResourceVo vo = new ResourceVo();
        if(resourceType!=null&&!"".equals(resourceType)){
            vo.setType(resourceType);
        }
   
        PageVo pageVo = Helper.findPageVo(request);  //得到当前翻页信息
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = getBs().getRecordCount(vo);
            pageVo = Helper.createPageVo(request, recordCount);
        }        
        List beans = new ArrayList();
        beans = getBs().queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), vo, null);  //按条件查询全部,带排序
    
        Helper.savePageVo(pageVo, request);
        request.setAttribute(REQUEST_BEANS, beans);  //把结果集放入request
        request.setAttribute("resourceType", resourceType);
        request.setAttribute("gboxSourceName", gboxSourceName);
        request.setAttribute("txtUrl", txtUrl);
        request.setAttribute("txtGboxLnkUrl", txtGboxLnkUrl);
        return request.findForward("listResource");
    }
    
    public IForward queryResourcePath(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
       StringBuffer result =new StringBuffer();
       String resourceId = request.getParameter("resourceId");
       String actionServlet = request.getParameter("actionServlet");
       String requestType = request.getParameter("requestType");
       HttpServletRequest tempRequest=(HttpServletRequest) request;
       if(requestType!=null&&"image".equals(requestType)){
           result.append(tempRequest.getContextPath());
           result.append("/");
           result.append(actionServlet);
           result.append("?id=");
           result.append(resourceId);
         
       }else if(requestType!=null&&"link".equals(requestType)){
          String temp = tempRequest.getServerName();
          result.append(temp);
          result.append(":");
          result.append( tempRequest.getServerPort());
          String basePath=  tempRequest.getContextPath();
          result.append(basePath);
          result.append("/");
          result.append(actionServlet);
          result.append("&id=");
          result.append(resourceId);
       }
       response.getServletResponse().setContentType("text/plain; charset=UTF-8");
       response.getServletResponse().getWriter().print(result.toString());  
       response.getServletResponse().getWriter().flush();
        return null;
    }
    
    
}
