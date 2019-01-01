package venus.portal.requests.bs.impl;

import venus.frames.base.bs.BaseBusinessService;
import venus.portal.requests.bs.IRequestsBs;
import venus.portal.requests.dao.IRequestsDao;
import venus.portal.requests.model.Requests;
import venus.portal.requests.util.IRequestsConstants;

import java.util.List;


public class RequestsBs extends BaseBusinessService implements IRequestsBs, IRequestsConstants {
    
    private IRequestsDao dao = null;

    public IRequestsDao getDao() {
        return dao;
    }

    public void setDao(IRequestsDao dao) {
        this.dao = dao;
    }

    public void insert(Requests vo) {
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
    public void delete(Requests vo) {
	   getDao().delete(vo);
    }

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public Requests findById(String id) {
		return getDao().findById(id);
    }

    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public void update(Requests vo) {
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
