package venus.portal.gbox.resource.resourceinfo.bs.impl;

import venus.portal.gbox.resource.api.ResourceTypeAPI;
import venus.portal.gbox.resource.audiolib.bs.IAudioLibBs;
import venus.portal.gbox.resource.audiolib.util.IAudioLibConstants;
import venus.portal.gbox.resource.classificationrelation.bs.IClassificationRelationBs;
import venus.portal.gbox.resource.classificationrelation.util.IClassificationRelationConstants;
import venus.portal.gbox.resource.doclib.bs.IDocLibBs;
import venus.portal.gbox.resource.doclib.util.IDocLibConstants;
import venus.portal.gbox.resource.imagelib.bs.IImageLibBs;
import venus.portal.gbox.resource.imagelib.util.IImageLibConstants;
import venus.portal.gbox.resource.resourceinfo.bs.IResourceBs;
import venus.portal.gbox.resource.resourceinfo.dao.IResourceDao;
import venus.portal.gbox.resource.resourceinfo.util.IResourceConstants;
import venus.portal.gbox.resource.resourceinfo.vo.ResourceVo;
import venus.portal.gbox.resource.tag.bs.ITagBs;
import venus.portal.gbox.resource.tag.util.ITagConstants;
import venus.portal.gbox.resource.tag.vo.TagVo;
import venus.portal.gbox.resource.videolib.bs.IVideoLibBs;
import venus.portal.gbox.resource.videolib.util.IVideoLibConstants;
import venus.portal.gbox.util.DateTools;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.util.List;

public class ResourceBs extends BaseBusinessService implements IResourceConstants, IResourceBs {
   
    /**
     * dao 表示: 数据访问层的实例
     */
    private IResourceDao dao = null;

    /**
     * 设置数据访问接口
     * 
     * @return
     */
    public IResourceDao getDao() {
        return dao;
    }

    /**
     * 获取数据访问接口
     * 
     * @param dao
     */
    public void setDao(IResourceDao dao) {
        this.dao = dao;
    }
    
    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(ResourceVo vo) {
        if (vo == null)
            return null;
        String tag = vo.getTag();
        if (tag != null && !"".equals(tag)) { //tag不为空时才插入tag
            String[] tags = tag.split(" ");
            ITagBs tagBs = (ITagBs) Helper.getBean(ITagConstants.BS_KEY);
            for (int i = 0; i < tags.length; i++) {
                if (tags[i] != null && !"".equals(tags[i])) {
                    String queryCondition = " AND NAME = '" + tags[i] + "'";
                    if (tagBs.getRecordCount(queryCondition) == 0) { //数据库中不存在该标签则创建
                        TagVo tagVo = new TagVo();
                        tagVo.setName(tags[i]);
                        tagVo.setCreateDate(DateTools.getSysTimestamp());
                        tagBs.insert(tagVo);
                    }
                }
            }
        }
        return getDao().insert(vo);
    }
    
    /**
     * 删除单条记录
     * 
     * @param vo 用于删除的VO对象
     * @return 成功删除的记录数
     */
    public int delete(ResourceVo vo) {
        return getDao().delete(vo);
    }

    /**
     * 删除多条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id[]) {
        return getDao().delete(id);
    }
    
    /**
     * 删除多条记录
     * @param id 用于删除的记录的id
     * @param deleteType 删除方式
     * @return 成功删除的记录数
     */
    public int deleteMulti(String[] id,String deleteType) {
        int rows = 0;
        if (id == null || deleteType == null || "".equals(deleteType))
            return 0;
        for (int i = 0; i < id.length; i++) {
            ResourceVo vo = find(id[i]); 
            if (vo == null)
                return 0;
            //删除图片
            if (ResourceTypeAPI.getImageType().equals(vo.getType())) {
                IImageLibBs imgBs = (IImageLibBs) Helper.getBean(IImageLibConstants.BS_KEY);
                rows += imgBs.delete(id[i],deleteType);
            }
            //删除视频
            if (ResourceTypeAPI.getVideoType().equals(vo.getType())) {
                IVideoLibBs vdoBs = (IVideoLibBs) Helper.getBean(IVideoLibConstants.BS_KEY);
                rows += vdoBs.delete(id[i], deleteType);             
            }   
            //删除文档
            if (ResourceTypeAPI.getDocType().equals(vo.getType())) {
                IDocLibBs docBs = (IDocLibBs) Helper.getBean(IDocLibConstants.BS_KEY);
                rows += docBs.delete(id[i],deleteType);
            }
            //删除音频
            if (ResourceTypeAPI.getAudioType().equals(vo.getType())) { 
                IAudioLibBs adoBs = (IAudioLibBs) Helper.getBean(IAudioLibConstants.BS_KEY);
                rows += adoBs.delete(id[i],deleteType);
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
        //删除目录与资源的关联
        IClassificationRelationBs relationBs = (IClassificationRelationBs) Helper.getBean(IClassificationRelationConstants.BS_KEY);
        relationBs.deleteAll();
        //删除图片
        IImageLibBs imgBs = (IImageLibBs) Helper.getBean(IImageLibConstants.BS_KEY);
        imgBs.deleteAll();
        //删除视频
        IVideoLibBs vdoBs = (IVideoLibBs) Helper.getBean(IVideoLibConstants.BS_KEY);
        vdoBs.deleteAll();
        //删除文档
        IDocLibBs docBs = (IDocLibBs) Helper.getBean(IDocLibConstants.BS_KEY);
        docBs.deleteAll();
        //删除音频
        IAudioLibBs adoBs = (IAudioLibBs) Helper.getBean(IAudioLibConstants.BS_KEY);
        adoBs.deleteAll();
        //删除资源
        return getDao().deleteAll();
    }
    
    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public ResourceVo find(String id) {
        return getDao().find(id);
    }
    
    /**
     * 根据Id进行查询，该方法可获得完整文件路径
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public ResourceVo findAll(String id) {
        return getDao().findAll(id);
    }

    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(ResourceVo vo) {
        return getDao().update(vo);
    }
    
    /**
     * 更新tag
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int updateTag(ResourceVo vo) {
        return getDao().updateTag(vo);
    }    

    public int getRecordCount(ResourceVo vo){
        return getDao().getRecordCount(vo);
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param vo 查询条件
     * @param orderStr 排序字符 
     * @return 查询到的VO列表
     */
    public List<ResourceVo> queryByCondition(int no, int size, ResourceVo vo, String orderStr) {
        return getDao().queryByCondition(no, size, vo, orderStr);
    }

}
