package venus.portal.hotwords.dao;

import venus.portal.hotwords.model.HotWords;

import java.util.List;


/**
 * @author chengliang
 */

public interface IHotWordsDao {

    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     *
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public void insert(HotWords vo);

    /**
     * 删除单条记录
     *
     * @param vo 用于删除的记录的vo
     * @return 成功删除的记录数
     */
    public void delete(HotWords vo);

    /**
     * 根据Id进行查询
     *
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public HotWords findById(String id);

    /**
     * 更新单条记录
     *
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public void update(HotWords vo);

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
     * @param no             当前页数
     * @param size           每页记录数
     * @param queryCondition 查询条件
     * @param orderStr       排序字符
     * @return 查询到的VO列表
     */
    public List<HotWords> queryByCondition(int no, int size, String queryCondition, String orderStr);

    /**
     * 查询所有的热词，用于缓存
     *
     * @return 查询到的VO列表
     */
    public List<HotWords> queryAll();
}
