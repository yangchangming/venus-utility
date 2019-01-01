package venus.portal.gbox.resource.classification.dao;

import venus.portal.gbox.resource.classification.vo.ClassificationVo;
import venus.pub.lang.OID;

import java.util.List;

public interface IClassificationDao {
    
    /**
     * 查找分类节点
     * @param vo
     * @return 分类节点vo
     */
    public ClassificationVo find(ClassificationVo vo);

    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(ClassificationVo vo);
    
    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */    
    public int update(ClassificationVo vo);
    
    /**
     * 更新节点叶子状态
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */          
    public int updateLeaf(ClassificationVo vo);
    
    /**
     * 删除指定id的记录
     * @return 成功删除的记录数
     */    
    public int delete(String id);    
    
    /**
     * 删除全部记录
     * @return 成功删除的记录数
     */    
    public int deleteMulti(String selfCode);
    
    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition);    
    
    /**
     * 通过查询条件获得所有的VO对象列表
     * 
     * @param queryCondition 查询条件
     * @return 查询到的VO列表
     */    
    public List<ClassificationVo> queryByCondition(String queryCondition);
    
    /**
     * 获得节点最大code
     * @param parentCode
     * @return String 获得节点最大code
     */
    public String getChildMaxCode(String parentCode);
    
}
