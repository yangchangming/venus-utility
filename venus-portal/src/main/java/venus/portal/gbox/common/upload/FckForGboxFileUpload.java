/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.gbox.common.upload;

import net.fckeditor.handlers.ResourceType;
import org.apache.commons.fileupload.FileItem;
import venus.portal.gbox.resource.api.ResourceTypeAPI;
import venus.portal.gbox.resource.audiolib.bs.IAudioLibBs;
import venus.portal.gbox.resource.audiolib.util.IAudioLibConstants;
import venus.portal.gbox.resource.audiolib.vo.AudioVo;
import venus.portal.gbox.resource.classification.bs.IClassificationBs;
import venus.portal.gbox.resource.classification.util.IClassificationConstants;
import venus.portal.gbox.resource.classification.vo.ClassificationVo;
import venus.portal.gbox.resource.classificationrelation.bs.IClassificationRelationBs;
import venus.portal.gbox.resource.classificationrelation.util.IClassificationRelationConstants;
import venus.portal.gbox.resource.classificationrelation.vo.ClassificationRelationVo;
import venus.portal.gbox.resource.customizelib.bs.ICustomizeLibBs;
import venus.portal.gbox.resource.customizelib.util.ICustomizeLibConstants;
import venus.portal.gbox.resource.customizelib.vo.CustomizeVo;
import venus.portal.gbox.resource.doclib.bs.IDocLibBs;
import venus.portal.gbox.resource.doclib.util.IDocLibConstants;
import venus.portal.gbox.resource.doclib.vo.DocVo;
import venus.portal.gbox.resource.imagelib.bs.IImageLibBs;
import venus.portal.gbox.resource.imagelib.util.IImageLibConstants;
import venus.portal.gbox.resource.imagelib.vo.ImageVo;
import venus.portal.gbox.resource.videolib.bs.IVideoLibBs;
import venus.portal.gbox.resource.videolib.util.IVideoLibConstants;
import venus.portal.gbox.resource.videolib.vo.VideoVo;
import venus.portal.gbox.util.DateTools;
import venus.portal.gbox.util.FileTools;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * @author zhaoyapeng
 *
 */
public class FckForGboxFileUpload {
    /**
     * 获取图片BS
     * @return
     */
    public static IImageLibBs getImageBs() {
        return (IImageLibBs) Helper.getBean("ImageLibBs");  //得到BS对象,受事务控制
    }
    
   /**
    * 获取视频BS
    * @return
    */
    public static IVideoLibBs getVideoBs() {
        return (IVideoLibBs) Helper.getBean("VideoLibBs");  //得到BS对象,受事务控制
    }    
    /**
     * 获取音频BS
     * @return
     */
    public  static IAudioLibBs getAudioBs() {
        return (IAudioLibBs) Helper.getBean("AudioLibBs");  //得到BS对象,受事务控制
    }
    
    /**
     * 获取文档BS
     * @return
     */
    public static IDocLibBs getBs() {
        return (IDocLibBs) Helper.getBean("DocLibBs");  //得到BS对象,受事务控制
    }
    
    
    public static boolean  fckResourceToGbox(final HttpServletRequest request,FileItem fileItem,String url,ResourceType resourceType){
        String contextPath =request.getContextPath();
        String tempUrl = url.replaceFirst(contextPath, "");
        String realUrl = request.getRealPath(tempUrl);
        return addResouce(fileItem,resourceType,realUrl);
    }
    
    private static boolean addResouce(FileItem fileItemm,ResourceType resourceType,String realUrl){
       File file =new File(realUrl);
       
        String fileType = FileTools.getExtension(file);
        if (fileType == null || "".equals(fileType))
            return false;
        fileType = fileType.toLowerCase();
        OID oid = null;
        if (ResourceTypeAPI.getResourceTypeFormat(ResourceTypeAPI.getImageType()).indexOf(fileType) != -1) {  //导入图片类型的资源文件
            IImageLibBs imageBs = (IImageLibBs) Helper.getBean(IImageLibConstants.BS_KEY);
            String queryCondition = "";
            queryCondition += " AND NAME = '" + file.getName() + "'";
            queryCondition += " AND FILE_SIZE = '" + file.length() + "'";
            int recordCount = imageBs.getRecordCount(queryCondition);     
            if (recordCount == 0) { 
                ImageVo vo = new ImageVo();
                vo.setName(file.getName());
                vo.setIsOriginal("1");
                vo.setIsExternal("1"); //标识是由外部导入的资源，非本地上传资源
                vo.setIsProtected("0");
                vo.setFileName(file.getAbsolutePath());
                vo.setFileSize(String.valueOf(file.length()));
                vo.setFileFormat(fileType);
                vo.setThumbsName(file.getAbsolutePath());
                vo.setCreateDate(DateTools.getSysTimestamp());
                oid = imageBs.insert(vo);
            }
        } else  if (ResourceTypeAPI.getResourceTypeFormat(ResourceTypeAPI.getVideoType()).indexOf(fileType) != -1) { //导入视频类型的资源文件
            IVideoLibBs videoBs = (IVideoLibBs) Helper.getBean(IVideoLibConstants.BS_KEY);
            String queryCondition = "";
            queryCondition += " AND NAME = '" + file.getName() + "'";
            queryCondition += " AND FILE_SIZE = '" + file.length() + "'";
            int recordCount = videoBs.getRecordCount(queryCondition);     
            if (recordCount == 0) { 
                VideoVo vo = new VideoVo();
                vo.setName(file.getName());
                vo.setIsOriginal("1");
                vo.setIsExternal("1"); //标识是由外部导入的资源，非本地上传资源
                vo.setIsHD("0");
                vo.setIsProtected("0");
                vo.setFileName(file.getAbsolutePath());
                vo.setFileSize(String.valueOf(file.length()));
                vo.setFileFormat(fileType);
                vo.setThumbsName(file.getAbsolutePath());
                vo.setCreateDate(DateTools.getSysTimestamp());
                oid = videoBs.insert(vo);
            }
        } else  if (ResourceTypeAPI.getResourceTypeFormat(ResourceTypeAPI.getDocType()).indexOf(fileType) != -1) { //导入文档类型的资源文件
            IDocLibBs docBs = (IDocLibBs) Helper.getBean(IDocLibConstants.BS_KEY);
            String queryCondition = "";
            queryCondition += " AND NAME = '" + file.getName() + "'";
            queryCondition += " AND FILE_SIZE = '" + file.length() + "'";
            int recordCount = docBs.getRecordCount(queryCondition);     
            if (recordCount == 0) { 
                DocVo vo = new DocVo();
                vo.setName(file.getName());
                vo.setIsOriginal("1");
                vo.setIsExternal("1"); //标识是由外部导入的资源，非本地上传资源
                vo.setIsProtected("0");
                vo.setFileName(file.getAbsolutePath());
                vo.setFileSize(String.valueOf(file.length()));
                vo.setFileFormat(fileType);
                vo.setCreateDate(DateTools.getSysTimestamp());
                oid = docBs.insert(vo);
            }
        } else  if (ResourceTypeAPI.getResourceTypeFormat(ResourceTypeAPI.getAudioType()).indexOf(fileType) != -1) { //导入音频类型的资源文件
            IAudioLibBs audioBs = (IAudioLibBs) Helper.getBean(IAudioLibConstants.BS_KEY);
            String queryCondition = "";
            queryCondition += " AND NAME = '" + file.getName() + "'";
            queryCondition += " AND FILE_SIZE = '" + file.length() + "'";
            int recordCount = audioBs.getRecordCount(queryCondition);     
            if (recordCount == 0) { 
                AudioVo vo = new AudioVo();
                vo.setName(file.getName());
                vo.setIsOriginal("1");
                vo.setIsExternal("1"); //标识是由外部导入的资源，非本地上传资源
                vo.setIsProtected("0");
                vo.setFileName(file.getAbsolutePath());
                vo.setFileSize(String.valueOf(file.length()));
                vo.setFileFormat(fileType);
                vo.setCreateDate(DateTools.getSysTimestamp());
                oid = audioBs.insert(vo);
            }
        }  else   { //导入其它类型的资源文件
            ICustomizeLibBs customizeBs = (ICustomizeLibBs) Helper.getBean(ICustomizeLibConstants.BS_KEY);
            String queryCondition = "";
            queryCondition += " AND NAME = '" + file.getName() + "'";
            queryCondition += " AND FILE_SIZE = '" + file.length() + "'";
            int recordCount = customizeBs.getRecordCount(queryCondition);     
            if (recordCount == 0) { 
                CustomizeVo vo = new CustomizeVo();
                vo.setName(file.getName());
                vo.setIsOriginal("1");
                vo.setIsExternal("1"); //标识是由外部导入的资源，非本地上传资源
                vo.setIsProtected("0");
                vo.setFileName(file.getAbsolutePath());
                vo.setFileSize(String.valueOf(file.length()));
                vo.setFileFormat(fileType);
                vo.setCreateDate(DateTools.getSysTimestamp());
                oid = customizeBs.insert(vo);
            }
        }     
        
        //成功导入资源后，关联资源与分类节点        
        if (oid != null && !"".equals(oid)) {
            IClassificationRelationBs relBs = (IClassificationRelationBs) Helper.getBean(IClassificationRelationConstants.BS_KEY);
            IClassificationBs classBs =   (IClassificationBs) Helper.getBean(IClassificationConstants.BS_KEY);
           List<ClassificationVo>  classifications = classBs.queryByCondition(null);
           String classificationId ="1099200100000000001"; //默认采用根节点
           for(ClassificationVo classVo:classifications){ //找到第一个叶子节点，将资源关系挂到此节点
                String isLeaf =  classVo.getIsLeaf();
                if("1".equals(isLeaf)){
                     classificationId = classVo.getId();
                    break;
                }
           }
            String queryCondition = " AND B.CLASSIFICATION_ID = '" + classificationId + "' AND B.RESOURCE_ID = '" + oid.toString() + "'";
            if (relBs.getClassifyRecordCount(queryCondition) == 0) {
                ClassificationRelationVo relVo = new ClassificationRelationVo();
                relVo.setResourceId(oid.toString());
                relVo.setClassificationId(classificationId);
                relVo.setCreateDate(DateTools.getSysTimestamp());
                relBs.insert(relVo);        
            }
        }
        return true;
    
    }

}
