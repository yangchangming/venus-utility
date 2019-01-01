package venus.portal.gbox.resource.doclib.action;

import org.apache.commons.fileupload.FileItem;
import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.message.MessageAgent;
import venus.frames.web.message.MessageStyle;
import venus.portal.gbox.common.upload.FileUpload;
import venus.portal.gbox.resource.api.ResourceTypeAPI;
import venus.portal.gbox.resource.classification.bs.IClassificationBs;
import venus.portal.gbox.resource.classification.util.IClassificationConstants;
import venus.portal.gbox.resource.classification.vo.ClassificationVo;
import venus.portal.gbox.resource.classificationrelation.bs.IClassificationRelationBs;
import venus.portal.gbox.resource.classificationrelation.util.IClassificationRelationConstants;
import venus.portal.gbox.resource.classificationrelation.vo.ClassificationRelationVo;
import venus.portal.gbox.resource.doclib.bs.IDocLibBs;
import venus.portal.gbox.resource.doclib.util.IDocLibConstants;
import venus.portal.gbox.resource.doclib.vo.DocVo;
import venus.portal.gbox.util.DateTools;
import venus.portal.gbox.util.FileTools;
import venus.pub.lang.OID;
import venus.pub.util.StringUtil;

import java.util.List;

public class DocLibAction extends DefaultDispatchAction implements IDocLibConstants {
    
    /**
     * 得到BS对象
     * 
     * @return BS对象
     */
    public IDocLibBs getBs() {
        return (IDocLibBs) Helper.getBean(BS_KEY);  //得到BS对象,受事务控制
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
        String[] id = StringUtil.split(request.getParameter("ids"), ",");  //从request获取多条记录id
        String deleteType = request.getParameter("deleteType");
        String uploadPath = ResourceTypeAPI.getResourceTypeUploadPath(ResourceTypeAPI.getDocType());
        String returnForward = request.getParameter("returnForward");
        returnForward = (returnForward == null || "".equals(returnForward)) ?  FORWARD_RESOURCE_LISTPAGE : returnForward;                 
        for (int i = 0; i < id.length; i++) {
            DocVo vo = getBs().find(id[i]);
            if (vo == null)
                continue;            
            String fileName = vo.getFileName();
            int rows = getBs().delete(id[i]);
            if (rows > 0 && "2".equals(deleteType)) { //彻底删除时再删除实体文件
                if ("1".equals(vo.getIsExternal())) { //删除由外部导入的资源文件
                    FileUpload.delete(fileName);
                } else  if ("1".equals(vo.getIsProtected())) { //删除受保护的资源文件
                    FileUpload.delete(FileUpload.getUploadPath(uploadPath) + fileName);
                } else { //删除非受保护的资源文件
                    FileUpload.delete(FileUpload.getUploadPath(uploadPath,false) + fileName);
                }
            }
        }
        return request.findForward(returnForward);
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
        detail(formBean, request, response);
        return request.findForward(FORWARD_UPDATE_PAGE);
    }    
    
    /**
     * 从页面的表单获取单条记录id，并察看这条记录的详细信息
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward detail(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        DocVo vo = getBs().find(request.getParameter(REQUEST_ID));  //通过id获取vo
        request.setAttribute(REQUEST_BEAN, vo);  //把vo放入request
        return request.findForward(FORWARD_DETAIL_PAGE);
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
        DocVo vo = new DocVo();
        vo.setModifyDate(DateTools.getSysTimestamp());
        if (!Helper.populate(vo, request)) {
            return MessageAgent.sendErrorMessage(request,
                    DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
        }        
        getBs().update(vo);  //更新单条记录
        return request.findForward(FORWARD_RESOURCE_LISTPAGE);
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
    public IForward updateTag(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        updateTag(request);
        return null;
    }

    public IForward updateTagToQuerry(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        updateTag(request);
        return request.findForward(FORWARD_RESOURCE_LISTPAGE);
    }     

    private void updateTag(IRequest request) {
        String id = request.getParameter("id");
        String tags = request.getParameter("tags");
        if (tags != null)
            tags = tags.replaceAll(",", " ");
        DocVo vo = new DocVo();
        vo.setId(id);
        vo.setTag(tags);
        vo.setModifyDate(DateTools.getSysTimestamp());
        getBs().updateTag(vo);  //更新单条记录
    }     
    
    public IForward upload(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        boolean isProtectedFile = ("1".equals(request.getParameter("isProtectedFile"))) ? true : false;
        String classId = request.getParameter("classId");
        String tag = request.getParameter("tag");
        FileUpload fileupload = new FileUpload(request,response);
        List<FileItem> fileItems = fileupload.getFileItems();
        DocVo vo = new DocVo();
        String docUploadPath = ResourceTypeAPI.getResourceTypeUploadPath(ResourceTypeAPI.getDocType());
        String docRealPath = docUploadPath + FileTools.FILE_SEPARATOR + FileUpload.getDatePath();
        String uploadPath = FileUpload.getUploadPath(docRealPath,isProtectedFile);
        String isExternal = "0"; //是否为从外部导入的资源
        if (classId != null && !"".equals(classId) && !"null".equals(classId)) {
            isExternal = "1";
            IClassificationBs bs = (IClassificationBs) Helper.getBean(IClassificationConstants.BS_KEY);
            ClassificationVo classVo = new ClassificationVo();
            classVo.setId(classId);
            ClassificationVo returnVo = bs.find(classVo);
            uploadPath = (returnVo.getPath().endsWith(FileTools.FILE_SEPARATOR)) ? returnVo.getPath() : returnVo.getPath() + FileTools.FILE_SEPARATOR; //外部导入的资源存储其绝对路径
        }
        for (int i = 0; i < fileItems.size(); i++) {
            FileItem fileItem = (FileItem)fileItems.get(i);
            boolean yn = fileupload.upload(uploadPath,null,fileItem); //根据指定目录上传文件
            if (yn) {
                String queryCondition = "";
                queryCondition += " AND NAME = '" + fileItem.getName() + "'";
                queryCondition += " AND FILE_SIZE = '" + fileItem.getSize() + "'";
                int recordCount = getBs().getRecordCount(queryCondition);
                if (recordCount == 0) { //已存在的文件不再存入数据库
                    vo.setName(fileItem.getName());
                    vo.setTag(tag);
                    vo.setIsOriginal("1");
                    vo.setIsExternal(isExternal);
                    vo.setIsProtected(request.getParameter("isProtectedFile")); //从外部导入的资源不受保护
                    vo.setFileName(("1".equals(isExternal) ? uploadPath + fileItem.getName() : FileUpload.getDatePath() + fileItem.getName()));
                    vo.setFileSize(String.valueOf(fileItem.getSize()));
                    vo.setFileFormat(FileTools.getExtension(fileItem.getName()));
                    vo.setCreateDate(DateTools.getSysTimestamp());
                    OID oid = getBs().insert(vo); //插入图片资源
                    if (classId != null && !"".equals(classId) && !"null".equals(classId)) {
                        IClassificationRelationBs relBs = (IClassificationRelationBs) Helper.getBean(IClassificationRelationConstants.BS_KEY);
                        ClassificationRelationVo relVo = new ClassificationRelationVo();
                        relVo.setResourceId(oid.toString());
                        relVo.setClassificationId(classId);
                        relVo.setCreateDate(DateTools.getSysTimestamp());
                        relBs.insert(relVo); //插入资源与分类的关联
                    }
                }
            }
        }
        return null;
    }
}
