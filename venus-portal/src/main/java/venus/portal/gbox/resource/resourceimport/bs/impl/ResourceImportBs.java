package venus.portal.gbox.resource.resourceimport.bs.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.mainframe.util.Helper;
import venus.portal.gbox.resource.api.OptionAPI;
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
import venus.portal.gbox.resource.imagelib.bs.IImageLibBs;
import venus.portal.gbox.resource.imagelib.util.IImageLibConstants;
import venus.portal.gbox.resource.imagelib.vo.ImageVo;
import venus.portal.gbox.resource.resourceimport.bs.IResourceImportBs;
import venus.portal.gbox.resource.resourceimport.dao.IResourceImportDao;
import venus.portal.gbox.resource.resourceimport.util.IResourceImportConstants;
import venus.portal.gbox.resource.resourceimport.vo.ResourceImportVo;
import venus.portal.gbox.resource.videolib.bs.IVideoLibBs;
import venus.portal.gbox.resource.videolib.util.IVideoLibConstants;
import venus.portal.gbox.resource.videolib.vo.VideoVo;
import venus.portal.gbox.util.DateTools;
import venus.portal.gbox.util.FileTools;
import venus.pub.lang.OID;
import venus.portal.gbox.resource.doclib.vo.DocVo;

import java.io.File;
import java.util.List;

public class ResourceImportBs extends BaseBusinessService implements IResourceImportConstants, IResourceImportBs {
    
    /**
     * dao 表示: 数据访问层的实例
     */
    private IResourceImportDao dao = null;
    
    private static String pCode = null;
    
    /**
     * 设置数据访问接口
     * 
     * @return
     */
    public IResourceImportDao getDao() {
        return dao;
    }

    /**
     * 获取数据访问接口
     * 
     * @param dao
     */
    public void setDao(IResourceImportDao dao) {
        this.dao = dao;
    }

    public int delete(String id) {
        return getDao().delete(id);
    }

    public OID insert(ResourceImportVo vo) {
        return getDao().insert(vo);
    }

    public int update(ResourceImportVo vo) {
        return getDao().update(vo);
    }

    public List<ResourceImportVo> queryVoByIDs(String ids){
        String queryCondition="";
        if (ids != null&& ids.length()>0) {
            String[] idArray = ids.split(",");
            for (int i = 0; i < idArray.length; i++) {
                if (idArray[i] != null && !"".equals(idArray[i])){
                    queryCondition+=" AND ID = '" + idArray[i] + "'";
                }
            }
        }
        return getDao().queryByCondition(queryCondition);
    }
    
    public int getRecordCount(String queryCondition) {
        return getDao().getRecordCount(queryCondition);
    }
    
    public List<ResourceImportVo> queryByCondition(String queryCondition) {
        return getDao().queryByCondition(queryCondition);
    }
    
    public String queryResourcePath(String queryCondition) {
        List<ResourceImportVo> list = getDao().queryByCondition(queryCondition);
        JSONArray jsonArg = new JSONArray();
        JSONObject jsonObj = new JSONObject();
        for (int i = 0; i < list.size(); i++) {
            ResourceImportVo vo = list.get(i);
            jsonObj.put("id", vo.getId());
            jsonObj.put("resourcePath", vo.getResourcePath());
            jsonObj.put("description", vo.getDescription());
            jsonArg.add(jsonObj);
        }
        return jsonArg.toString();        
    }
    
    public boolean importClassification(ClassificationVo classVo, ResourceImportVo resVo) {
        if (resVo == null)
            return false;
        pCode = classVo.getParentCode();
        String[] folderNames = FileTools.splitFolderName(resVo.getResourcePath()); //如果是多级目录，将目录按层级分割
        if (folderNames == null || "".equals(folderNames))
            return false;
        ClassificationVo vo = new ClassificationVo ();
        IClassificationBs bs = (IClassificationBs) Helper.getBean(IClassificationConstants.BS_KEY);
        for (int i = 0; i < folderNames.length; i++) { //根据资源文件路径增加分类节点
            String folderFiles = FileTools.splitAbsolutePath(resVo.getResourcePath(), folderNames[i]);//取得每个资源文件夹的绝对路径
            vo.setParentCode(pCode);
            vo.setName(folderNames[i]);
            vo.setPath(FileTools.splitAbsolutePath(resVo.getResourcePath(), folderNames[i]));
            String queryCondition = " AND PARENT_CODE = '" + vo.getParentCode()  + "' AND NAME = '" + vo.getName() + "'";
            List<ClassificationVo> list = bs.queryByCondition(queryCondition);
            String id = null;
            if (list.size() == 0) { //如果没有同名目录则插入
                id = bs.insert(vo).toString();
                pCode = vo.getSelfCode();
            } else { //出现同名目录判断是否追加新文件
                pCode = list.get(0).getSelfCode();
                id = list.get(0).getId();
            } 
            if (folderFiles.equals(resVo.getResourcePath()))  //只有当目录与外部导入路径相同时才导入文件
                importResource(id,resVo.getResourcePath()); //添加目录后，向其导入资源文件
        }        
        return true;
    }

    /**
     * 将当前路径下的文件导入到指定目录中，如果当前路径里是文件，与将文件目录建立关联；如果是文件夹，只建立目录结构
     * @param classificationId 分类节点ID
     * @param filePath 要导入的文件路径
     * @return boolean 文件导入是否成功
     */
    private boolean importResource(String classificationId,String filePath) {
        if (filePath == null || "".equals(filePath))
            return false;
        File file = new File(filePath);
        if (!file.exists())
            return false;
        for (int i = 0; i < file.listFiles().length; i++) {
            File resourceFile = file.listFiles()[i];
            if (resourceFile.isFile()) { //导入资源且与分类建立关联
                if (OptionAPI.isValidResourceType(resourceFile.getName()) && !resourceFile.isHidden()) {
                        importData(classificationId,resourceFile);
                }
            } else { //将文件目录导入为分类目录
                //2012-05-18 加入在添加分类目录时首先查询一下有无此目录。有则不导入
                IClassificationBs bs = (IClassificationBs) Helper.getBean(IClassificationConstants.BS_KEY);
                String queryCondition =" and PARENT_CODE='"+pCode+"' and NAME='"+resourceFile.getName()+"' ";
                List<ClassificationVo> result =    bs.queryByCondition(queryCondition);
                if(result ==null||result.size()==0){
                ClassificationVo vo = new ClassificationVo ();
                vo.setParentCode(pCode);
                vo.setName(resourceFile.getName());
                vo.setPath(resourceFile.getAbsolutePath());
                OID oid = bs.insert(vo);
                importResource(oid.toString(),resourceFile.getAbsolutePath());
            }
            }
        }
        return true;
    }
    
    /**
     * 将资源信息导入到数据库，同时与分类建立关联
     * @param classificationId 分类id
     * @param file 资源文件
     */
    private boolean importData(String classificationId,File file) {
        String fileType = FileTools.getExtension(file);
        if (fileType == null || "".equals(fileType))
            return false;
        if(ResourceTypeAPI.getAllResourceTypeFormat().indexOf(fileType)==-1){//资源类型未包含此种后缀，无法区分类型
            return false;
        }
        fileType = fileType.toLowerCase();
        OID oid = null;
        if (ResourceTypeAPI.getResourceTypeFormat(ResourceTypeAPI.getImageType()).indexOf(fileType) != -1) {  //导入图片类型的资源文件
            IImageLibBs imageBs = (IImageLibBs) Helper.getBean(IImageLibConstants.BS_KEY);
            String queryCondition = "";
            queryCondition += " AND FILE_NAME = '" + file.getAbsolutePath() + "'";
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
