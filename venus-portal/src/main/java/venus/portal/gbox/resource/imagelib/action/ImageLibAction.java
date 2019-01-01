package venus.portal.gbox.resource.imagelib.action;

import org.apache.commons.fileupload.FileItem;
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
import venus.portal.gbox.common.upload.FileUpload;
import venus.portal.gbox.resource.api.ResourceTypeAPI;
import venus.portal.gbox.resource.classification.bs.IClassificationBs;
import venus.portal.gbox.resource.classification.util.IClassificationConstants;
import venus.portal.gbox.resource.classification.vo.ClassificationVo;
import venus.portal.gbox.resource.classificationrelation.bs.IClassificationRelationBs;
import venus.portal.gbox.resource.classificationrelation.util.IClassificationRelationConstants;
import venus.portal.gbox.resource.classificationrelation.vo.ClassificationRelationVo;
import venus.portal.gbox.resource.imagelib.bs.IImageLibBs;
import venus.portal.gbox.resource.imagelib.util.IImageLibConstants;
import venus.portal.gbox.resource.imagelib.util.ImageThead;
import venus.portal.gbox.resource.imagelib.vo.ImageVo;
import venus.portal.gbox.util.DateTools;
import venus.portal.gbox.util.FileTools;
import venus.pub.lang.OID;
import venus.pub.util.StringUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ImageLibAction extends DefaultDispatchAction implements IImageLibConstants {

    /**
     * 得到BS对象
     * 
     * @return BS对象
     */
    public IImageLibBs getBs() {
        return (IImageLibBs) Helper.getBean(BS_KEY);  //得到BS对象,受事务控制
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
        String uploadPath = ResourceTypeAPI.getResourceTypeUploadPath(ResourceTypeAPI.getImageType());
        String returnForward = request.getParameter("returnForward");
        returnForward = (returnForward == null || "".equals(returnForward)) ?  FORWARD_RESOURCE_LISTPAGE : returnForward;         
        for (int i = 0; i < id.length; i++) {
            ImageVo vo = getBs().find(id[i]);
            if (vo == null)
                continue;            
            String fileName = vo.getFileName();
            String thbName = vo.getThumbsName();
            int rows = getBs().delete(id[i]);
            if (rows > 0 && "2".equals(deleteType)) { //彻底删除时再删除实体文件
                if ("1".equals(vo.getIsExternal())) { //删除由外部导入的资源文件
                    FileUpload.delete(thbName);
                    FileUpload.delete(fileName);
                } else  if ("1".equals(vo.getIsProtected())) { //删除受保护的资源文件
                    FileUpload.delete(FileUpload.getUploadPath(uploadPath) + thbName);
                    FileUpload.delete(FileUpload.getUploadPath(uploadPath) + fileName);
                } else { //删除非受保护的资源文件
                    FileUpload.delete(FileUpload.getUploadPath(uploadPath,false) + thbName);
                    FileUpload.delete(FileUpload.getUploadPath(uploadPath,false) + fileName);
                }
            }
        }
        return request.findForward(returnForward);
    }
    
    /**
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward deleteAll(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String uploadPath = ResourceTypeAPI.getResourceTypeUploadPath(ResourceTypeAPI.getImageType());
        if (getBs().delete() > 0) {
            FileUpload.deleteDir(FileUpload.getUploadPath(uploadPath,false), false); //删除文件
            FileUpload.deleteDir(FileUpload.getUploadPath(uploadPath), false); //删除文件
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
    public IForward find(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        detail(formBean, request, response);
        return request.findForward(FORWARD_UPDATE_PAGE);
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
        ImageVo vo = new ImageVo();
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
        ImageVo vo = new ImageVo();
        vo.setId(id);
        vo.setTag(tags);
        vo.setModifyDate(DateTools.getSysTimestamp());
        getBs().updateTag(vo);  //更新单条记录
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
     * 从页面的表单获取单条记录id，并察看这条记录的详细信息
     * 
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward detail(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        ImageVo vo = getBs().find(request.getParameter(REQUEST_ID));  //通过id获取vo
        request.setAttribute(REQUEST_BEAN, vo);  //把vo放入request
        IClassificationRelationBs bs = (IClassificationRelationBs) Helper.getBean(IClassificationRelationConstants.BS_KEY);
        String queryCondition = "";
        if (vo != null && !"".equals(vo))
            queryCondition += " AND B.RESOURCE_ID = '" + vo.getId() + "'";
        request.setAttribute("classifyList", bs.queryClassifyByCondition(-1, -1, queryCondition, null));
        return request.findForward(FORWARD_DETAIL_PAGE);
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
        String name = request.getParameter("name");
        String tag = request.getParameter("tag");
        ImageVo vo = new ImageVo();
        if (!Helper.populate(vo, request)) {
            return MessageAgent.sendErrorMessage(request,
                    DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
        }
        String queryCondition = "";
        if (name != null && !"".equals(name))
            queryCondition += " AND NAME LIKE '%" + name + "%'";        
        if (tag != null && !"".equals(tag))
            queryCondition += " AND TAG LIKE '%" + tag + "%'";         
        PageVo pageVo = Helper.findPageVo(request);  //得到当前翻页信息
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = getBs().getRecordCount(queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }        
        String orderStr = Helper.findOrderStr(request);  //得到排序信息

        List list = new ArrayList();
        list = getBs().queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition, orderStr);  //按条件查询全部,带排序

        Helper.saveOrderStr(orderStr, request);  //保存排序信息
        Helper.savePageVo(pageVo, request);
        request.setAttribute(REQUEST_BEANS, list);  //把结果集放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelper.getMapFromRequest((HttpServletRequest) request)); //回写表单
        return request.findForward(FORWARD_LIST_PAGE);
    }
    
    public IForward upload(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        boolean isProtectedFile = ("1".equals(request.getParameter("isProtectedFile"))) ? true : false;
        String classId = request.getParameter("classId");
        String tag = request.getParameter("tag");
        FileUpload fileupload = new FileUpload(request,response);
        List<FileItem> fileItems = fileupload.getFileItems();
        ImageVo vo = new ImageVo();
        String imgUploadPath = ResourceTypeAPI.getResourceTypeUploadPath(ResourceTypeAPI.getImageType());
        String imgRealPath = imgUploadPath + FileTools.FILE_SEPARATOR + FileUpload.getDatePath();
        String uploadPath = FileUpload.getUploadPath(imgRealPath,isProtectedFile);
        String uploadThumbsPath = FileUpload.getThumbsPath(imgRealPath,isProtectedFile);
        String isExternal = "0"; //是否为从外部导入的资源
        if (classId != null && !"".equals(classId) && !"null".equals(classId)) {
            isExternal = "1";
            IClassificationBs bs = (IClassificationBs) Helper.getBean(IClassificationConstants.BS_KEY);
            ClassificationVo classVo = new ClassificationVo();
            classVo.setId(classId);
            ClassificationVo returnVo = bs.find(classVo);
            uploadPath = (returnVo.getPath().endsWith(FileTools.FILE_SEPARATOR)) ? returnVo.getPath() : returnVo.getPath() + FileTools.FILE_SEPARATOR; //外部导入的资源存储其绝对路径
            uploadThumbsPath = uploadPath + FileUpload.getThumbsPath();
        }
        ExecutorService executor  =  Executors.newFixedThreadPool(10);
        for (int i = 0; i < fileItems.size(); i++) {
            FileItem fileItem = (FileItem)fileItems.get(i);
            boolean yn = fileupload.upload(uploadPath,null,fileItem); //根据指定目录上传文件
            if (yn) { //文件上传成功后再向数据库记录资源信息
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
                    vo.setThumbsName(("1".equals(isExternal) ? uploadPath + FileUpload.getThumbsPath() + fileItem.getName() : FileUpload.getDatePath() + FileUpload.getThumbsPath() + fileItem.getName()));
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
                    executor.execute(new ImageThead(uploadPath+fileItem.getName(), uploadThumbsPath+fileItem.getName()));
                }
            }
        }
        executor.shutdown(); 
        return null;
    }
    
    public IForward view(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String id = request.getParameter("id");
        String isThumbs = request.getParameter("isThumbs");
        boolean isProtectedFile = ("1".equals(request.getParameter("isProtectedFile"))) ? true : false;
        boolean isExternal = ("1".equals(request.getParameter("isExternal"))) ? true : false;
        String imgUploadPath = ResourceTypeAPI.getResourceTypeUploadPath(ResourceTypeAPI.getImageType()); //返回图片类型的上传目录 /image
        String uploadPath = FileUpload.getUploadPath(imgUploadPath,isProtectedFile);
        String uploadThumbsPath = FileUpload.getThumbsPath(imgUploadPath,isProtectedFile);
        String filePath = "";
        ImageVo vo = getBs().find(id);
        String fileName = vo.getFileName();
        if (isExternal) //本地导入的文件使用本地绝对路径
            filePath = fileName;
        else
            filePath = ("1".equals(isThumbs)) ? uploadThumbsPath + fileName : uploadPath + fileName;   
        File file = new File(filePath);
        if (!file.exists())
            file = new File(uploadPath + fileName);
        FileInputStream in = new FileInputStream(file);
        HttpServletResponse res = (HttpServletResponse)response;
        res.setContentType("application/x-octet-stream");
        ServletOutputStream  os = res.getOutputStream();
        int len; 
        byte[] buf = new byte[2048]; 
        while((len = in.read(buf)) != -1) { 
            os.write(buf, 0, len); 
        } 
        os.close(); 
        in.close(); 
        return request.findForward(FORWARD_VIEW_PAGE);
    }

}
