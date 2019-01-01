package venus.portal.posts.user.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessResourceFailureException;
import venus.frames.base.dao.BaseHibernateDao;
import venus.frames.base.exception.BaseDataAccessException;
import venus.portal.posts.user.dao.ILoginDao;
import venus.portal.posts.user.model.EwpLoginEntity;
import venus.portal.posts.user.util.ILoginUsrInfoConstants;

import java.util.List;

/**
 * Created by qj on 14-2-12.
 */
public class LoginDao extends BaseHibernateDao implements ILoginDao, ILoginUsrInfoConstants {

    public void insert(EwpLoginEntity vo) {
        String id = java.util.UUID.randomUUID().toString();
        vo.setId(id);
        super.save(vo);
    }

    public void delete(EwpLoginEntity vo) {
        super.delete(vo);
    }

    public EwpLoginEntity findById(String id) {
        return (EwpLoginEntity) super.get(EwpLoginEntity.class, id);
    }

    public EwpLoginEntity findByName(String name, String pwd, String enable) {

        List list = queryByCondition(0, 1, "name = '" + name + "' and pwd = '" + pwd + "' ", "");
        if (list != null && !list.isEmpty()) {
            return (EwpLoginEntity) (list.get(0));
        }
        return null;
    }

    public EwpLoginEntity findByName(String name) {
        List list = queryByCondition(0, 1, "name = '" + name + "' ", "");
        if (list != null && !list.isEmpty()) {
            return (EwpLoginEntity) (list.get(0));
        }
        return null;
    }

    public void update(EwpLoginEntity vo) {
        super.update(vo);
    }

    public int getRecordCount(String queryCondition) {
        StringBuilder sql = new StringBuilder("select count(*)  from EwpLoginEntity ");
        if (StringUtils.isNotBlank(queryCondition)) {
            sql.append(" where  " + queryCondition);
        }
        try {
            Object result = getSession().createQuery(sql.toString()).uniqueResult();
            if (result != null) {
                return ((Long) result).intValue();
            }
            return 0;
        } catch (DataAccessResourceFailureException e) {
            throw new BaseDataAccessException("DataAccessResourceFailureException occurs when invoke getRecordCount method", e);
        } catch (HibernateException e) {
            throw new BaseDataAccessException("HibernateException occurs when invoke getRecordCount method", e);
        } catch (IllegalStateException e) {
            throw new BaseDataAccessException("IllegalStateException occurs when invoke getRecordCount method", e);
        }

    }

    public List queryByCondition(int no, int size, String queryCondition, String orderStr) {
        StringBuilder sql = new StringBuilder();
        sql.append(" from EwpLoginEntity ");
        if (StringUtils.isNotBlank(queryCondition)) {
            sql.append(" where " + queryCondition);
        }
        if (StringUtils.isNotBlank(orderStr)) {
            sql.append(" order by   " + orderStr);
        }
        try {
            int start_page = ((no - 1) < 0) ? 0 : (no - 1) * size;
            return getSession().createQuery(sql.toString()).setFirstResult(start_page).setMaxResults(size).list();
        } catch (DataAccessResourceFailureException e) {
            throw new BaseDataAccessException("DataAccessResourceFailureException occurs when invoke queryByCondition method", e);
        } catch (HibernateException e) {
            throw new BaseDataAccessException("HibernateException occurs when invoke queryByCondition method", e);
        } catch (IllegalStateException e) {
            throw new BaseDataAccessException("IllegalStateException occurs when invoke queryByCondition method", e);
        }
    }

    public boolean checkUserNameUnique(String userName) {
        StringBuilder sql = new StringBuilder("from EwpLoginEntity el");
        if (userName != null && !userName.equals("")) {
            sql.append(" where el.name = '" + userName + "'");
            List rs = getSession().createQuery(sql.toString()).list();
            if (rs.size() > 0) {
                return false;
            } else {
                return true;
            }
        }

        return false;
    }

}