package venus.portal.posts.posts.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessResourceFailureException;
import venus.frames.base.dao.BaseHibernateDao;
import venus.portal.posts.posts.dao.IPostsDao;
import venus.portal.posts.posts.model.EwpPostsEntity;
import venus.portal.posts.posts.util.IPostsConstants;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by qj on 14-2-11.
 */
public class PostsDao extends BaseHibernateDao implements IPostsDao, IPostsConstants {

    public void insert(EwpPostsEntity vo) {
        String id = java.util.UUID.randomUUID().toString();
        vo.setId(String.valueOf(id));
        vo.setPubdate(new Timestamp(System.currentTimeMillis()));
        super.save(vo);
    }

    public void delete(EwpPostsEntity vo) {
        super.delete(vo);
    }

    public EwpPostsEntity findById(String id) {
        return (EwpPostsEntity) super.get(EwpPostsEntity.class, id);
    }

    public void update(EwpPostsEntity vo) {
        super.update(vo);
    }

    public int getRecordCount(String queryCondition) {
        StringBuffer sql = new StringBuffer("select count(*)  from EwpPostsEntity ");
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

    public List<EwpPostsEntity> queryByCondition(int no, int size, String queryCondition, String orderStr) {
        StringBuffer sql = new StringBuffer();
        sql.append(" from EwpPostsEntity ");
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
