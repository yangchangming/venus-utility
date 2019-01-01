package venus.portal.gbox.resource.resourceimport.action;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.util.Helper;
import venus.frames.mainframe.util.VoHelper;
import venus.portal.gbox.resource.classification.vo.ClassificationVo;
import venus.portal.gbox.resource.resourceimport.bs.IResourceImportBs;
import venus.portal.gbox.resource.resourceimport.util.IResourceImportConstants;
import venus.portal.gbox.resource.resourceimport.vo.ResourceImportVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class ResourceImportAction extends DefaultDispatchAction implements IResourceImportConstants {
    
    /**
     * 得到BS对象
     * 
     * @return BS对象
     */
    public IResourceImportBs getBs() {
        return (IResourceImportBs) Helper.getBean(BS_KEY_TARGET);  //得到BS对象,不受事务控制
    }
    
    /**
     * 将本地资源导入到资源库
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */    
    public IForward importResource(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String classId = request.getParameter("classId");
        String selectedIds = request.getParameter("selectedIds");
        String parentCode = request.getParameter("parentCode");
        String selectedIdsArry[] = selectedIds.split(",");
        String queryCondition = " AND CLASSIFICATION_ID = '" + classId + "'";
        if(selectedIdsArry!=null&&selectedIdsArry.length>0){
            queryCondition+="AND (";
            int i=0;
            for(String selecteItem:selectedIdsArry){
                i++;
                if(selecteItem!=null&&!("".equals(selecteItem))){
                    queryCondition+=" ID = '"+selecteItem+"'";
                }
                if(selectedIdsArry.length>i){
                    queryCondition+=" OR ";
                }
            }            
            queryCondition+=")";
        }
        insert(formBean, request, response); //导入前先保存当前目录关联的外部路径
        List<ResourceImportVo> list = getBs().queryByCondition(queryCondition);  //按条件查询全部,带排序
        ClassificationVo classVo = new ClassificationVo ();
        classVo.setId(classId);
        classVo.setParentCode(parentCode);
        for (int i = 0; i < list.size(); i++) { //根据定义的外部路径，为当前目录导入外部资源
            ResourceImportVo resVo = list.get(i);
            getBs().importClassification(classVo, resVo);
        }
        request.setAttribute("returnClassId", classId);
        return request.findForward(FORWARD_IMPORT_FRAME);
    }
    
    /**
     * 从路径中导入资源
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward insert(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String classId = request.getParameter("classId");                   //父文件ID
        String parentCode = request.getParameter("parentCode");     //父编码
        String[] hiddenId = request.getParameterValues("hiddenId");  //隐藏ID
        String[] filePath = request.getParameterValues("filePath");      //要导入的文件路径
        String deletedIds = request.getParameter("deletedIds");         //选中的ID
        if (deletedIds != null&& deletedIds.length()>0) {
            String[] ids = deletedIds.split(",");
            for (int i = 0; i < ids.length; i++) {
                if (ids[i] != null && !"".equals(ids[i]))
                    getBs().delete(ids[i]);
            }
        }
        if (filePath != null && !"".equals(filePath)) {
            ResourceImportVo vo = new ResourceImportVo();
            for(int i = 0; i < filePath.length; i++) {
                vo.setClassificationId(classId);
                vo.setResourcePath(filePath[i]);
                vo.setIsScan("0");
                if (hiddenId[i] == null || "".equals(hiddenId[i]))
                    getBs().insert(vo);  
                else {
                    vo.setId(hiddenId[i]);
                    getBs().update(vo);
                }
            }       
        }
        request.setAttribute("classId", classId);
        request.setAttribute("parentCode", parentCode);
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
    public IForward delete(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String[] deletedIds = request.getParameterValues("deletedIds");
        for (int i = 0; i < deletedIds.length; i++) {
            getBs().delete(deletedIds[i]);  //插入单条记录
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
     * 简单查询，分页显示，支持表单回写
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward simpleQuery(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String classId = request.getParameter("classId");
        String queryCondition = " AND CLASSIFICATION_ID = '" + classId + "'";
        List list = getBs().queryByCondition(queryCondition);  //按条件查询全部,带排序
        request.setAttribute(REQUEST_BEANS, list);  //把结果集放入request
        request.setAttribute("classId", classId);  //把结果集放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelper.getMapFromRequest((HttpServletRequest) request)); //回写表单
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
    public IForward queryResourcePath(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        HttpServletResponse res = (HttpServletResponse)response;
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = res.getWriter();
        String classId = request.getParameter("classId");
        String queryCondition = " AND CLASSIFICATION_ID = '" + classId + "'";
        writer.write(getBs().queryResourcePath(queryCondition));
        writer.flush();
        return null;
    }
    
    /**
     * 验证相应导入目录是否存在。
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward isExistForDirectory(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        HttpServletResponse res = (HttpServletResponse)response;
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = res.getWriter();
        String resourceIds = request.getParameter("resourceIds");
        JSONArray jsonArg = new JSONArray();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("result_yn", "Y");
        
        List<ResourceImportVo> list = getBs().queryVoByIDs(resourceIds);
        
        for(ResourceImportVo tempVo:list){    
            String tempPath = tempVo.getResourcePath();
            if(tempPath==null||"".equals(tempPath)){
                jsonObj.put("result_yn", "N");
                break;
            }
            File tempF=new File(tempPath);
            if(!tempF.exists()){
                jsonObj.put("result_yn", "N");
                break;
            }
        }
        
        writer.write(jsonObj.toString());
        writer.flush();
        return null;
    }
}
