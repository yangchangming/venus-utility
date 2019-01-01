package venus.portal.requests.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessResourceFailureException;
import venus.frames.base.dao.BaseHibernateDao;
import venus.frames.mainframe.util.Helper;
import venus.portal.requests.dao.IRequestsDao;
import venus.portal.requests.model.Requests;
import venus.portal.requests.util.IRequestsConstants;
import venus.pub.lang.OID;

import java.util.List;

public class RequestsDao extends BaseHibernateDao implements IRequestsDao, IRequestsConstants {

    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * 
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public void insert(Requests vo) {
        OID oid = Helper.requestOID(TABLE_NAME); //获得oid
        long id = oid.longValue();
        vo.setId(String.valueOf(id));
        super.save(vo);
    }

    /**
     * 删除单条记录VO
     * 
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public void delete(Requests vo) {
       super.delete(vo);
    }

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public Requests findById(String id) {
         return (Requests)super.get(Requests.class, id);
    }

    /**
     * 更新单条记录
     * 
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public void update(Requests vo) {
         super.update(vo);
    }

    /**
     * 查询总记录数，带查询条件
     * 
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition) {
      StringBuffer sql = new StringBuffer("select count(*)  from Requests ");
      if(StringUtils.isNotBlank(queryCondition)){
          sql.append(" where  "+queryCondition);
      }
      try {
          Object result =  getSession().createQuery(sql.toString()).uniqueResult();
          if(result != null ){
                  return ((Long)result).intValue();
          }
      } catch (DataAccessResourceFailureException e) {
          e.printStackTrace();
      } catch (HibernateException e) {
          e.printStackTrace();
      } catch (IllegalStateException e) {
          e.printStackTrace();
      }
      return 0;
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
        StringBuffer sql = new StringBuffer();
        sql.append(" from Requests ");
        if(StringUtils.isNotBlank(queryCondition)){
            sql.append(" where  "+queryCondition);
        }
        if(StringUtils.isNotBlank(orderStr)){
            sql.append(" order by   "+orderStr);
        }
        try {
            int start_page=((no-1)<0)?0:(no-1)*size;
            return getSession().createQuery(sql.toString()).setFirstResult(start_page).setMaxResults(size).list();
        } catch (DataAccessResourceFailureException e) {
            e.printStackTrace();
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
       return null;
    }
    
}
	
