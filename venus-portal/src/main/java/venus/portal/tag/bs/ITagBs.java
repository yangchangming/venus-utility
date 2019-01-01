/*
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */

package venus.portal.tag.bs;

import venus.portal.tag.dao.ITagDao;
import venus.portal.tag.model.Tag;

import java.util.List;

/**
 * @author zhangrenyang
 * 
 */

public interface ITagBs {

    /**
     * 设置数据访问接口
     * 
     * @return
     */
    public ITagDao getDao();

    /**
     * 获取数据访问接口
     * 
     * @param dao
     */
    public void setDao(ITagDao dao);

    /**
     * 插入单条记录
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public void insert(Tag vo);

    /**
     * 删除多条记录
     * 
     * @param ids 用于删除的记录的ids
     * @return 成功删除的记录数
     */
    public int deleteMulti(String ids[]) throws Exception;
    
     /**
     * 删除单条记录
     * 
     * @param vo 用于删除的记录的vo
     * @return 成功删除的记录数
     */
    public void delete(Tag vo);

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public Tag findById(String id);

    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public void update(Tag vo);

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
    public List queryByCondition(int no, int size, String queryCondition, String orderStr);

}
