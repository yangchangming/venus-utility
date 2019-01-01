package venus.portal.website.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessResourceFailureException;
import venus.frames.base.dao.BaseHibernateDao;
import venus.frames.mainframe.util.Helper;
import venus.portal.cache.data.DataCache;
import venus.portal.website.dao.IWebsiteDao;
import venus.portal.website.model.Website;
import venus.portal.website.util.IWebsiteConstants;
import venus.pub.lang.OID;

import java.util.List;

public class WebsiteDao extends BaseHibernateDao implements IWebsiteDao, IWebsiteConstants {

    // 缓存
    private DataCache dataCache;

    /**
     * @param dataCache
     *            the dataCache to set
     */
    public void setDataCache(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public void insert(Website vo) {
        OID oid = Helper.requestOID(TABLE_NAME);
        long id = oid.longValue();
        vo.setId(String.valueOf(id));
        super.save(vo);
        dataCache.refreshWebsites();
    }

    /**
     * 删除单条记录
     *
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public void delete(Website vo) {
         super.delete(vo);
         dataCache.refreshWebsites();
    }

    /**
     * 删除多条记录
     *
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public void delete(Website[] vos) {
        if(null != vos){
            for(Website vo : vos){
                super.delete(vo);
            }
        }
        dataCache.refreshWebsites();
    }


    /**
     * 根据Id进行查询
     *
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public Website findWebsiteById(String id) {
        return (Website)super.get(Website.class, id);
    }

    /**
     * 更新单条记录
     *
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public void update(Website vo) {
        super.update(vo);
        dataCache.loadData();
    }

    /**
     * 查询总记录数，带查询条件
     *
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition) {
      StringBuffer sql = new StringBuffer("select count(*)  from Website ");
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
    public List queryByCondition(final int no,final int size, final String queryCondition,final String orderStr) {
        StringBuffer sql = new StringBuffer();
        sql.append(" from Website ");
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


    /**
     * 删除多个站点
     * @param ids 查询条件
     * @return 总记录数
     */
    public int delete(String[] ids) {
        if(null != ids){
            for(String id : ids){
                super.delete(super.load(Website.class, id));
            }
        }
        dataCache.refreshWebsites();
        return 0;
    }

    public Website getWebsiteByName(String name) {
        StringBuffer queryString =new StringBuffer(" from  Website   ewptemp  where ewptemp.Website_name=?   ");
        Object[] values={name};
        List  temp =  this.find(queryString.toString(), values);
        Website result = null;
        if(temp != null&& temp.size()>0){
            result =(Website)temp.get(0);
        }
        return result;
    }

    public Website getWebsiteByCode(String websiteCode) {
        StringBuffer queryString =new StringBuffer(" from  Website  ewptemp  where ewptemp.websiteCode=?");
        Object[] values={websiteCode};
        List temp = this.find(queryString.toString(), values);
        Website result = null;
        if(temp != null&& temp.size()>0){
            result =(Website)temp.get(0);
        }
        return result;
    }

    public List getWebsiteByIsDefault(String is){
        StringBuffer queryString = new StringBuffer(" from  Website as  ewptemp  where ewptemp.isDefault=? ");
        Object[] values = {is};
        List list = find(queryString.toString(),values);
        return list;
    }

    public List queryAll() {
        try {
            return getSession().createQuery(" from  Website").list();
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

