package venus.portal.gbox.resource.tag.bs;

import venus.portal.gbox.resource.tag.vo.TagVo;
import venus.pub.lang.OID;

import java.util.List;

public interface ITagBs {
    
    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(TagVo vo);
    
    /**
     * 删除单条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id);    

    /**
     * 删除多条记录
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public int delete(String id[]);

    /**
     * 根据name进行查询
     * 
     * @param name 用于查找的name
     * @return 查询到的VO对象
     */
    public TagVo find(String name);

    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int update(TagVo vo);
    
    /**
     * 更新tag点击数
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public int updateClicks(TagVo vo);

    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition);
    
    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @param orderStr 排序字符 
     * @return 查询到的VO列表
     */
    public List<TagVo> queryByCondition(String queryCondition);
}
