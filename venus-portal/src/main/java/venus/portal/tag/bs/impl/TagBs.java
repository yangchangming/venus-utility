package venus.portal.tag.bs.impl;

import venus.frames.base.bs.BaseBusinessService;
import venus.portal.tag.bs.ITagBs;
import venus.portal.tag.dao.ITagDao;
import venus.portal.tag.model.Tag;
import venus.portal.tag.util.ITagConstants;

import java.util.List;

/**
 * @author zhangrenyang
 * 
 */

public class TagBs extends BaseBusinessService implements ITagBs, ITagConstants {
    
    /**
     * dao 表示: 数据访问层的实例
     */
    private ITagDao dao = null;

    /**
     * 设置数据访问接口
     * 
     * @return
     */
    public ITagDao getDao() {
        return dao;
    }

    /**
     * 获取数据访问接口
     * 
     * @param dao
     */
    public void setDao(ITagDao dao) {
        this.dao = dao;
    }


    /**
     * 插入单条记录
     * 
     * @param vo 用于添加的VO对象
     */
    public void insert(Tag vo) {
	 getDao().insert(vo);
    }

   /**
     * 删除多条记录
     * 
     * @param id  用于删除的记录的id
     * @return 成功删除的记录数
     * @throws Exception 
     */
    public int deleteMulti(String ids[]) throws Exception {
        for(String id : ids){
            delete(findById(id));
        }
        return 0;
    }
    
    /**
     * 删除单条记录
     * 
     * @param vo 用于删除的VO对象
     */
    public void delete(Tag vo) {
	   getDao().delete(vo);
    }

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public Tag findById(String id) {
		return getDao().findById(id);
    }

    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public void update(Tag vo) {
		 getDao().update(vo);
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
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     * 
     * @param no 当前页数
     * @param size 每页记录数
     * @param queryCondition 查询条件
     * @param orderStr 排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition, String orderStr) {
		return getDao().queryByCondition(no, size, queryCondition, orderStr);
    }

}
