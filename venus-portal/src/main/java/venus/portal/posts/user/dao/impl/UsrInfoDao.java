package venus.portal.posts.user.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessResourceFailureException;
import venus.frames.base.dao.BaseHibernateDao;
import venus.portal.posts.user.dao.IUsrInfoDao;
import venus.portal.posts.user.model.EwpUsrInfoEntity;
import venus.portal.posts.user.util.ILoginUsrInfoConstants;

import java.util.List;

/**
 * Created by qj on 14-2-12.
 */
public class UsrInfoDao extends BaseHibernateDao implements IUsrInfoDao, ILoginUsrInfoConstants {

    public void insert(EwpUsrInfoEntity vo) {
        super.save(vo);
    }

    public void delete(EwpUsrInfoEntity vo) {
        super.delete(vo);
    }

    public EwpUsrInfoEntity findById(String id) {
        return (EwpUsrInfoEntity) super.get(EwpUsrInfoEntity.class, id);
    }

    public void update(EwpUsrInfoEntity vo) {
        super.update(vo);
    }

    public int getRecordCount(String queryCondition) {
        StringBuffer sql = new StringBuffer("select count(*)  from EwpUsrInfoEntity ");
        if (StringUtils.isNotBlank(queryCondition)) {
            sql.append(" where  " + queryCondition);
        }
        try {
            Object result = getSession().createQuery(sql.toString()).uniqueResult();
            if (result != null) {
                return ((Long) result).intValue();
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

    public List queryByCondition(int no, int size, String queryCondition, String orderStr) {
        StringBuffer sql = new StringBuffer();
        sql.append(" from EwpUsrInfoEntity ");
        if (StringUtils.isNotBlank(queryCondition)) {
            sql.append(" where  " + queryCondition);
        }
        if (StringUtils.isNotBlank(orderStr)) {
            sql.append(" order by   " + orderStr);
        }
        try {
            int start_page = ((no - 1) < 0) ? 0 : (no - 1) * size;
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
