package venus.portal.gbox.resource.resourceinfo.bs;

import venus.portal.gbox.resource.resourceinfo.vo.ResourceVo;
import venus.pub.lang.OID;

import java.util.List;

public interface IResourceBs {
    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(ResourceVo vo);
    
    /**
     * 删除单条记录
     * 
     * @param vo 用于删除的VO对象
     * @return 成功删除的记录数
     */
    public int delete(ResourceVo vo);        

    /**
     * 删除多条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id[]);
    
    /**
     * 删除多条记录
     * @param id 用于删除的记录的id
     * @param deleteType 删除方式
     * @return 成功删除的记录数
     */
    public int deleteMulti(String[] id, String deleteType);
    
    /**
     * 删除全部记录
     * 
     * @return 成功删除的记录数
     */
    public int deleteAll();

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public ResourceVo find(String id);
    
    /**
     * 根据Id进行查询，该方法可获得完整文件路径
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public ResourceVo findAll(String id);
    
    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(ResourceVo vo);
    
    /**
     * 更新tag
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int updateTag(ResourceVo vo);    
    
    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(ResourceVo vo);
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param vo 查询条件
     * @param orderStr 排序字符 
     * @return 查询到的VO列表
     */
    public List<ResourceVo> queryByCondition(int no, int size, ResourceVo vo, String orderStr);

}
