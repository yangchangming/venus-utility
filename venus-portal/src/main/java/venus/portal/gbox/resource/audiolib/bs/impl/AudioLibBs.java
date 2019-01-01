package venus.portal.gbox.resource.audiolib.bs.impl;

import venus.VenusHelper;
import venus.frames.base.bs.BaseBusinessService;
import venus.portal.gbox.common.upload.FileUpload;
import venus.portal.gbox.resource.api.ResourceTypeAPI;
import venus.portal.gbox.resource.audiolib.bs.IAudioLibBs;
import venus.portal.gbox.resource.audiolib.dao.IAudioLibDao;
import venus.portal.gbox.resource.audiolib.util.IAudioLibConstants;
import venus.portal.gbox.resource.audiolib.vo.AudioVo;
import venus.portal.gbox.resource.classificationrelation.dao.IClassificationRelationDao;
import venus.portal.gbox.resource.classificationrelation.vo.ClassificationRelationVo;
import venus.portal.gbox.resource.resourceinfo.bs.IResourceBs;
import venus.portal.gbox.resource.resourceinfo.util.IResourceConstants;
import venus.portal.gbox.resource.resourceinfo.vo.ResourceVo;
import venus.pub.lang.OID;

import java.util.List;

public class AudioLibBs extends BaseBusinessService implements IAudioLibBs, IAudioLibConstants {

    
    /**
     * dao 表示: 数据访问层的实例
     */
    private IAudioLibDao dao = null;
    
    private IClassificationRelationDao relationDao = null;

    /**
     * 设置数据访问接口
     * 
     * @return
     */
    public IAudioLibDao getDao() {
        return dao;
    }

    /**
     * 获取数据访问接口
     * 
     * @param dao
     */
    public void setDao(IAudioLibDao dao) {
        this.dao = dao;
    }

    /**
     * @return the relationDao
     */
    public IClassificationRelationDao getRelationDao() {
        return relationDao;
    }

    /**
     * @param relationDao the relationDao to set
     */
    public void setRelationDao(IClassificationRelationDao relationDao) {
        this.relationDao = relationDao;
    }

    /**
     * 插入单条记录
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(AudioVo vo) {
        IResourceBs resBs = (IResourceBs) VenusHelper.getBean(IResourceConstants.BS_KEY);
        ResourceVo resVo = new ResourceVo();
        resVo.setName(vo.getName());
        resVo.setCode(vo.getCode());
        resVo.setTag(vo.getTag());
        resVo.setType(ResourceTypeAPI.getAudioType());
        resVo.setIsProtected(vo.getIsProtected());
        resVo.setIsExternal(vo.getIsExternal());
        resVo.setFileName(vo.getFileName());
        resVo.setFileSize(vo.getFileSize());
        resVo.setFileFormat(vo.getFileFormat());
        resVo.setCreatorName(vo.getCreatorName());
        resVo.setCreateDate(vo.getCreateDate());
        resVo.setEnableStatus(vo.getEnableStatus());
        resVo.setDescription(vo.getDescription());
        OID oid = resBs.insert(resVo);
        vo.setId(String.valueOf(oid.longValue()));
        return getDao().insert(vo);
    }

    /**
     * 删除全部记录
     * @return 成功删除的记录数
     */
    public int delete() {
        List<AudioVo> list = getDao().queryByCondition("");
        ClassificationRelationVo relationVo = new ClassificationRelationVo();
        AudioVo vo = new AudioVo();
        for(int i = 0; i <  list.size(); i++) {
            vo = list.get(i);
            relationVo.setResourceId(vo.getId());
            getRelationDao().delete(relationVo);                    
        }
        getDao().delete();
        IResourceBs resBs = (IResourceBs) VenusHelper.getBean(IResourceConstants.BS_KEY);
        ResourceVo resVo = new ResourceVo();
        resVo.setType(ResourceTypeAPI.getAudioType());
        int rows = resBs.delete(resVo);
        return rows;
    }    
    
    /**
     * 删除单条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id) {
      //删除资源与目录的关联
        ClassificationRelationVo relationVo = new ClassificationRelationVo();
        relationVo.setResourceId(id);
        getRelationDao().delete(relationVo);        
      //删除音频
        getDao().delete(id);
      //删除资源
        IResourceBs resBs = (IResourceBs) VenusHelper.getBean(IResourceConstants.BS_KEY);
        ResourceVo resVo = new ResourceVo();
        resVo.setId(id);
        resVo.setType(ResourceTypeAPI.getAudioType());        
        int rows = resBs.delete(resVo);
        return rows;
    }    
    
    /**
     * 删除多条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id[]) {
        ClassificationRelationVo relationVo = new ClassificationRelationVo();
        for (int i = 0; i < id.length; i++) {
            relationVo.setResourceId(id[i]);
            getRelationDao().delete(relationVo);
        }
        getDao().delete(id);
        IResourceBs resBs = (IResourceBs) VenusHelper.getBean(IResourceConstants.BS_KEY);
        int rows = resBs.delete(id);
        return rows;
    }
    
    /**
     * 删除多条记录
     * @param id 用于删除的记录的id
     * @param deleteType 删除方式
     * @return 成功删除的记录数
     */
    public int delete(String id,String deleteType) {
        String uploadPath = ResourceTypeAPI.getResourceTypeUploadPath(ResourceTypeAPI.getImageType());
        AudioVo vo = find(id);
        if (vo == null)
            return 0;
        String fileName = vo.getFileName();
        int rows = delete(id);
        if (rows > 0 && "2".equals(deleteType)) { //彻底删除时再删除实体文件
            if ("1".equals(vo.getIsExternal())) { //删除由外部导入的资源文件
                FileUpload.delete(fileName);
            } else  if ("1".equals(vo.getIsProtected())) { //删除受保护的资源文件
                FileUpload.delete(FileUpload.getUploadPath(uploadPath) + fileName);
            } else { //删除非受保护的资源文件
                FileUpload.delete(FileUpload.getUploadPath(uploadPath,false) + fileName);
            }
        }        
        return rows;
    }    
    
    /**
     * 删除全部记录
     * 
     * @return 成功删除的记录数
     */
    public int deleteAll() {
        return getDao().deleteAll();
    }    

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public AudioVo find(String id) {
        AudioVo vo = getDao().find(id);
        return vo;
    }

    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(AudioVo vo) {
        IResourceBs resBs = (IResourceBs) VenusHelper.getBean(IResourceConstants.BS_KEY);
        ResourceVo resVo = new ResourceVo();
        resVo.setName(vo.getName());
        resVo.setCode(vo.getCode());
        resVo.setTag(vo.getTag());
        resVo.setModifierName(vo.getModifierName());
        resVo.setModifyDate(vo.getModifyDate());
        resVo.setEnableStatus(vo.getEnableStatus());
        resVo.setDescription(vo.getDescription());        
        resVo.setId(vo.getId());
        resBs.update(resVo);
        int rows = getDao().update(vo);
        return rows;
    }
    
    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int updateTag(AudioVo vo) {
        IResourceBs resBs = (IResourceBs) VenusHelper.getBean(IResourceConstants.BS_KEY);
        ResourceVo resVo = new ResourceVo();
        resVo.setTag(vo.getTag());
        resVo.setModifierName(vo.getModifierName());
        resVo.setModifyDate(vo.getModifyDate());      
        resVo.setId(vo.getId());
        resBs.updateTag(resVo);
        int rows = getDao().updateTag(vo);
        return rows;
    }     

    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition) {
        int rows = getDao().getRecordCount(queryCondition);
        return rows;
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List<AudioVo> queryByCondition(int no, int size, String queryCondition, String orderStr) {
        List<AudioVo> lResult = getDao().queryByCondition(no, size, queryCondition, orderStr);
        return lResult;
    }
}
