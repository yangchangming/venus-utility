package venus.portal.gbox.resource.classificationrelation.bs.impl;

import venus.frames.base.bs.BaseBusinessService;
import venus.portal.gbox.resource.classification.vo.ClassificationVo;
import venus.portal.gbox.resource.classificationrelation.bs.IClassificationRelationBs;
import venus.portal.gbox.resource.classificationrelation.dao.IClassificationRelationDao;
import venus.portal.gbox.resource.classificationrelation.util.IClassificationRelationConstants;
import venus.portal.gbox.resource.classificationrelation.vo.ClassificationRelationVo;
import venus.portal.gbox.resource.resourceinfo.vo.ResourceVo;
import venus.pub.lang.OID;

import java.util.List;
import java.util.Map;

public class ClassificationRelationBs extends BaseBusinessService implements IClassificationRelationConstants, IClassificationRelationBs {
    
    /**
     * dao 表示: 数据访问层的实例
     */
    private IClassificationRelationDao dao = null;

    /**
     * 设置数据访问接口
     * 
     * @return
     */
    public IClassificationRelationDao getDao() {
        return dao;
    }

    /**
     * 获取数据访问接口
     * 
     * @param dao
     */
    public void setDao(IClassificationRelationDao dao) {
        this.dao = dao;
    }
    
    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(ClassificationRelationVo vo) {
        return getDao().insert(vo);
    }    
    
    /**
     * 根据条件删除多条记录
     * 
     * @param vo 用于删除的记录的vo
     * @return 成功删除的记录数
     */
    public int delete(ClassificationRelationVo vo) {
        return getDao().delete(vo);
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
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getClassifyRecordCount(String queryCondition) {
        return getDao().getClassifyRecordCount(queryCondition);
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
    public List<ClassificationVo> queryClassifyByCondition(int no, int size, String queryCondition, String orderStr){
        return getDao().queryClassifyByCondition(no, size, queryCondition, orderStr);
    }   
    
    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getResourceRecordCount(String queryCondition) {
        return getDao().getResourceRecordCount(queryCondition);
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
    public List<ResourceVo> queryResourceByCondition(int no, int size, String queryCondition, String orderStr) {
        return getDao().queryResourceByCondition(no, size, queryCondition, orderStr);
    }
    
    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件 map参数为：{classId,name,type}
     * @return 总记录数
     */
    public int getRelationRecordCount(Map queryCondition) {
        return getDao().getRelationRecordCount(queryCondition); 
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件 map参数为：{classId,name,type}
     * @param orderStr 排序字符 
     * @return 查询到的VO列表
     */
    public List<ResourceVo> queryRelationByCondition(int no, int size, Map queryCondition, String orderStr) {
        return getDao().queryRelationByCondition(no, size, queryCondition, orderStr);
    }    

}
