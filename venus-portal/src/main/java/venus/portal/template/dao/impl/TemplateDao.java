package venus.portal.template.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessResourceFailureException;
import venus.portal.template.dao.ITemplateDao;
import venus.portal.template.model.EwpTemplate;
import venus.portal.template.util.ITemplateConstants;
import venus.frames.base.dao.BaseHibernateDao;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.util.List;


public class TemplateDao extends BaseHibernateDao implements ITemplateDao, ITemplateConstants {

    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     *
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public OID insert(EwpTemplate vo) {
        OID oid = Helper.requestOID(TABLE_NAME); //获得oid
        long id = oid.longValue();
        vo.setId(String.valueOf(id));
        super.save(vo);
        return oid;
    }

    /**
     * 删除单条记录
     *
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public void delete(EwpTemplate vo) {
        super.delete(vo);
    }

    /**
     * 删除站点id下的所有模板
     *
     * @param websiteId 用于删除的记录的站点ID
     * @return 成功删除的记录数
     */
    public void deleteByWebSiteId(String websiteId) {
        String hql = "delete from EwpTemplate tpl where tpl.webSite = '" + websiteId + "'";
        getSession().createQuery(hql).executeUpdate();
    }

    /**
     * 删除多条记录
     *
     * @param id 用于删除的记录的id
     * @return 成功删除的记录数
     */
    public void delete(EwpTemplate[] vos) {
        if (null != vos) {
            for (EwpTemplate vo : vos) {
                super.delete(vo);
            }
        }
    }


    /**
     * 根据Id进行查询
     *
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public EwpTemplate findEwpTemplateById(String id) {
        EwpTemplate result = (EwpTemplate) super.get(EwpTemplate.class, id);
        Hibernate.initialize(result.getWebSite());
        result.setWebSiteId(result.getWebSite().getId());
        return result;
    }

    /**
     * 更新单条记录
     *
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public void update(EwpTemplate vo) {
        super.update(vo);
    }

    /**
     * 查询总记录数，带查询条件
     *
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition) {
        StringBuffer sql = new StringBuffer("select count(*)  from EwpTemplate  template");
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

    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     *
     * @param no             当前页数
     * @param size           每页记录数
     * @param queryCondition 查询条件
     * @param orderStr       排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(final int no, final int size, final String queryCondition, final String orderStr) {
        List resultList = null;
        StringBuffer sql = new StringBuffer();
        sql.append(" from EwpTemplate  template");
        if (StringUtils.isNotBlank(queryCondition)) {
            sql.append(" where  " + queryCondition);
        }
        if (StringUtils.isNotBlank(orderStr)) {
            sql.append(" order by   " + orderStr);
        }
        try {
            int start_page = ((no - 1) < 0) ? 0 : (no - 1) * size;
            if (size == -1) {
                resultList = getSession().createQuery(sql.toString()).setFirstResult(start_page).list();
            } else {
                resultList = getSession().createQuery(sql.toString()).setFirstResult(start_page).setMaxResults(size).list();
            }
        } catch (DataAccessResourceFailureException e) {
            e.printStackTrace();
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        for (Object temp : resultList) {
            Hibernate.initialize(((EwpTemplate) temp).getWebSite());
        }
        return resultList;
    }

    /**
     * 通过查询条件获得所有的VO对象
     *
     * @param siteId 站点ID
     * @return 查询到的VO列表
     */
    public List queryAll(String siteId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" from EwpTemplate  template where template.webSite.id = '" + siteId + "'");
        try {
            return getSession().createQuery(sql.toString()).list();
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
     * 删除多个模板
     *
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int delete(String[] ids) {
        if (null != ids) {
            for (String id : ids) {
                super.delete(super.load(EwpTemplate.class, id));
            }
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see udp.ewp.template.dao.ITemplateDao#getTemplateByName(java.lang.String)
     */
    public EwpTemplate getTemplateByName(String name, String siteId) {
        StringBuffer queryString = new StringBuffer(" from  udp.ewp.template.model.EwpTemplate   ewptemp  where ewptemp.template_name=?  and ewptemp.webSite.id=? ");
        Object[] values = {name, siteId};
        List temp = this.find(queryString.toString(), values);
        EwpTemplate result = null;
        if (temp != null && temp.size() > 0) {
            result = (EwpTemplate) temp.get(0);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see udp.ewp.template.dao.ITemplateDao#getTemplateByViewCodeName(java.lang.String)
     */
    public EwpTemplate getTemplateByViewCodeName(String viewCode, String siteId) {
        StringBuffer queryString = new StringBuffer(" from  udp.ewp.template.model.EwpTemplate   ewptemp  where ewptemp.template_viewname=?  and ewptemp.webSite.id=?  ");
        Object[] values = {viewCode, siteId};
        List temp = this.find(queryString.toString(), values);
        EwpTemplate result = null;
        if (temp != null && temp.size() > 0) {
            result = (EwpTemplate) temp.get(0);
        }
        return result;
    }

    public List getTemplateByIsDefault(String is, String siteId) {
        StringBuffer queryString = new StringBuffer(" from  udp.ewp.template.model.EwpTemplate   ewptemp  where ewptemp.isDefault=? and ewptemp.webSite.id=?");
        Object[] values = {is, siteId};
        List list = find(queryString.toString(), values);
        return list;
    }
}

