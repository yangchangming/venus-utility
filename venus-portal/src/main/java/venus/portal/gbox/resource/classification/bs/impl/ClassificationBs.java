package venus.portal.gbox.resource.classification.bs.impl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.mainframe.util.Helper;
import venus.portal.gbox.resource.api.ResourceTypeAPI;
import venus.portal.gbox.resource.audiolib.bs.IAudioLibBs;
import venus.portal.gbox.resource.audiolib.util.IAudioLibConstants;
import venus.portal.gbox.resource.classification.bs.IClassificationBs;
import venus.portal.gbox.resource.classification.dao.IClassificationDao;
import venus.portal.gbox.resource.classification.util.IClassificationConstants;
import venus.portal.gbox.resource.classification.vo.ClassificationVo;
import venus.portal.gbox.resource.classificationrelation.bs.IClassificationRelationBs;
import venus.portal.gbox.resource.classificationrelation.util.IClassificationRelationConstants;
import venus.portal.gbox.resource.classificationrelation.vo.ClassificationRelationVo;
import venus.portal.gbox.resource.doclib.bs.IDocLibBs;
import venus.portal.gbox.resource.doclib.util.IDocLibConstants;
import venus.portal.gbox.resource.imagelib.bs.IImageLibBs;
import venus.portal.gbox.resource.imagelib.util.IImageLibConstants;
import venus.portal.gbox.resource.resourceimport.bs.IResourceImportBs;
import venus.portal.gbox.resource.resourceimport.util.IResourceImportConstants;
import venus.portal.gbox.resource.resourceimport.vo.ResourceImportVo;
import venus.portal.gbox.resource.resourceinfo.vo.ResourceVo;
import venus.portal.gbox.resource.videolib.bs.IVideoLibBs;
import venus.portal.gbox.resource.videolib.util.IVideoLibConstants;
import venus.portal.gbox.util.DateTools;
import venus.portal.gbox.util.GlobalConstant;
import venus.pub.lang.OID;

import java.util.List;

public class ClassificationBs extends BaseBusinessService implements IClassificationBs,IClassificationConstants {

    private IClassificationDao dao = null;
    
    /**
     * @return the dao
     */
    public IClassificationDao getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(IClassificationDao dao) {
        this.dao = dao;
    }

    /**
     * 查找分类节点
     * @param vo
     * @return 分类节点vo
     */
    public ClassificationVo find(ClassificationVo vo) {
        return getDao().find(vo);
    }
    
    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insertRoot(ClassificationVo vo){
        vo.setParentCode("0");
        vo.setSelfCode(GlobalConstant.getRootNodeCode());
        vo.setDepth(1);
        vo.setIsLeaf("1");
        vo.setOrderCode(GlobalConstant.getRootNodeCode());
        vo.setCreateDate(DateTools.getSysTimestamp());
        return getDao().insert(vo);
    }    
    
    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(ClassificationVo vo){
        ClassificationVo parentNodeVo = find(vo);
        vo.setParentCode(parentNodeVo.getSelfCode());
        vo.setSelfCode(getChildMaxCode(parentNodeVo.getSelfCode()));
        vo.setDepth(getNextDepth(parentNodeVo.getDepth()));
        vo.setIsLeaf("1");
        vo.setOrderCode(vo.getSelfCode());
        vo.setCreateDate(DateTools.getSysTimestamp());
        
        //更新父节点状态
        parentNodeVo.setIsLeaf("0");
        updateLeaf(parentNodeVo);        
        return getDao().insert(vo);
    }
    
    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */    
    public int update(ClassificationVo vo) {
        return getDao().update(vo);
    }
    
    /**
     * 更新节点叶子状态
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */          
    public int updateLeaf(ClassificationVo vo) {
        return getDao().updateLeaf(vo);
    }    
    
    /**
     * 删除指定id的记录
     * @return 成功删除的记录数
     */      
    public int delete(String id) {
        ClassificationRelationVo relationVo = new ClassificationRelationVo();
        relationVo.setClassificationId(id);
        //删除该目录的关联的资源数据
        IClassificationRelationBs bs = (IClassificationRelationBs) Helper.getBean(IClassificationRelationConstants.BS_KEY);
        bs.delete(relationVo);
        //删除该目录关联的导入路径数据
        IResourceImportBs impBs = (IResourceImportBs) Helper.getBean(IResourceImportConstants.BS_KEY);
        String queryCondition = " AND CLASSIFICATION_ID = '" + id + "'";
        List<ResourceImportVo> list = impBs.queryByCondition(queryCondition);
        for (int i = 0; i < list.size(); i++) {
            ResourceImportVo vo = list.get(i);
            impBs.delete(vo.getId());
        }
        return getDao().delete(id);
    }
    
    /**
     * 删除目录以及与该目录相关联的资源
     * @return 成功删除的记录数
     */    
    public int deleteMulti(String selfCode) {
        int rows = 0;
        String queryCondition = "";
        if (selfCode != null && !"".equals(selfCode))
            queryCondition = " AND SELF_CODE LIKE '%" + selfCode + "%'";
        List<ClassificationVo> list = getDao().queryByCondition(queryCondition);
        for (int i = 0; i < list.size(); i++) {
            ClassificationVo vo = list.get(i);
            String queryResourceCondition = " AND B.CLASSIFICATION_ID= '" + vo.getId() + "'";
            IClassificationRelationBs bs = (IClassificationRelationBs) Helper.getBean(IClassificationRelationConstants.BS_KEY);
          //删除该目录关联的导入路径数据
            IResourceImportBs impBs = (IResourceImportBs) Helper.getBean(IResourceImportConstants.BS_KEY);
            String queryImportListConditon = " AND CLASSIFICATION_ID = '" + vo.getId() + "'";
            List<ResourceImportVo> impList = impBs.queryByCondition(queryImportListConditon);
            for (int j = 0; j < impList.size(); j++) {
                ResourceImportVo impVo = impList.get(j);
                impBs.delete(impVo.getId());
            }            
            List<ResourceVo> resList = bs.queryResourceByCondition(-1, -1, queryResourceCondition, null);
            for (int j = 0; j < resList.size(); j++) {
                ResourceVo resVo = resList.get(j);
                if (ResourceTypeAPI.getImageType().equals(resVo.getType())) {
                    IImageLibBs imgBs = (IImageLibBs) Helper.getBean(IImageLibConstants.BS_KEY);
                    imgBs.delete(resVo.getId());
                } else if (ResourceTypeAPI.getVideoType().equals(resVo.getType())) { 
                    IVideoLibBs vdoBs = (IVideoLibBs) Helper.getBean(IVideoLibConstants.BS_KEY);
                    vdoBs.delete(resVo.getId());
                } else if (ResourceTypeAPI.getDocType().equals(resVo.getType())) {
                    IDocLibBs docBs = (IDocLibBs) Helper.getBean(IDocLibConstants.BS_KEY);
                    docBs.delete(resVo.getId());
                } else if (ResourceTypeAPI.getAudioType().equals(resVo.getType())) {
                    IAudioLibBs adoBs = (IAudioLibBs) Helper.getBean(IAudioLibConstants.BS_KEY);
                    adoBs.delete(resVo.getId());
                } 
            }
            rows += getDao().delete(vo.getId());
        }
        return rows;
    }
    
    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition) {
        return getDao().getRecordCount(queryCondition);
    }    
    
    /**
     * 通过查询条件获得所有的VO对象列表
     * 
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */    
    public List<ClassificationVo> queryByCondition(String queryCondition) {
        return getDao().queryByCondition(queryCondition);
    }
    
    /**
     * 获得下级节点级别
     * @param depth
     * @return
     */
    public int getNextDepth(int depth) {
        int depthInt = depth;
        if (depthInt < 0 ) 
            return 0;     
        return depthInt + 1;
    }
    
    /**
     * 获得节点最大code
     * @param parentCode
     * @return String 获得节点最大code
     */
    public String getChildMaxCode(String parentCode) {
        String maxCode = getDao().getChildMaxCode(parentCode);
        return  (maxCode != null && !"".equals(maxCode)) ? maxCode : parentCode + GlobalConstant.getIncreaseNodeCode();
    }
    
    /**
     * 获得下级节点
     * @param queryCondition
     * @return String 树节点JSON
     */
    public String queryNode(String queryCondition) {
        List<ClassificationVo> list = getDao().queryByCondition(queryCondition);
        JSONArray jsonArg = new JSONArray();
        JSONObject jsonObj = new JSONObject();
        for (int i = 0; i < list.size(); i++) {
            ClassificationVo vo = list.get(i);
            jsonObj.put("id", vo.getSelfCode());
            jsonObj.put("text", vo.getName());
            jsonObj.put("value", vo.getId());
            jsonObj.put("checkstate", 0);
            jsonObj.put("hasChildren", ("1".equals(vo.getIsLeaf())) ? false : true);
            jsonObj.put("name", vo.getName());
            jsonObj.put("selfCode", vo.getSelfCode());
            jsonObj.put("parentCode", vo.getParentCode());
            jsonObj.put("depth", vo.getDepth());
            jsonObj.put("isLeaf", vo.getIsLeaf());
            jsonObj.put("description", vo.getDescription());
            jsonArg.add(jsonObj);
        }
        return jsonArg.toString();
    }
    
}
