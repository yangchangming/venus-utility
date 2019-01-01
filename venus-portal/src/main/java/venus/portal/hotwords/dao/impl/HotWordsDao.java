/*
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */

package venus.portal.hotwords.dao.impl;

import org.apache.commons.lang.StringUtils;
import venus.frames.base.dao.BaseHibernateDao;
import venus.frames.mainframe.util.Helper;
import venus.portal.hotwords.dao.IHotWordsDao;
import venus.portal.hotwords.model.HotWords;
import venus.portal.hotwords.util.IHotWordsConstants;
import venus.pub.lang.OID;

import java.util.List;

/**
 * @author chengliang
 */

public class HotWordsDao extends BaseHibernateDao implements IHotWordsDao, IHotWordsConstants {

    /**
     * 插入单条记录，用Oid作主键，把null全替换为""
     *
     * @param vo 用于添加的VO对象
     * @return 若添加成功，返回新生成的Oid
     */
    public void insert(HotWords vo) {
        OID oid = Helper.requestOID(TABLE_NAME); //获得oid
        long id = oid.longValue();
        vo.setId(String.valueOf(id));
        super.save(vo);
    }

    /**
     * 删除单条记录VO
     *
     * @param vo 用于删除的记录的vo
     * @return 成功删除的记录数
     */
    public void delete(HotWords vo) {
        super.delete(vo);
    }

    /**
     * 根据Id进行查询
     *
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public HotWords findById(String id) {
        return (HotWords) super.get(HotWords.class, id);
    }

    /**
     * 更新单条记录
     *
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public void update(HotWords vo) {
        super.update(vo);
    }

    /**
     * 查询总记录数，带查询条件
     *
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition) {
        StringBuffer sql = new StringBuffer("select count(*)  from HotWords ");
        if (StringUtils.isNotBlank(queryCondition)) {
            sql.append(" where  " + queryCondition);
        }

        Object result = getSession().createQuery(sql.toString()).uniqueResult();
        if (result != null) {
            return ((Long) result).intValue();
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
    public List<HotWords> queryByCondition(int no, int size, String queryCondition, String orderStr) {
        StringBuffer sql = new StringBuffer();
        sql.append(" from HotWords ");
        if (StringUtils.isNotBlank(queryCondition)) {
            sql.append(" where  " + queryCondition);
        }
        if (StringUtils.isNotBlank(orderStr)) {
            sql.append(" order by   " + orderStr);
        }

        int start_page = ((no - 1) < 0) ? 0 : (no - 1) * size;
        return getSession().createQuery(sql.toString()).setFirstResult(start_page).setMaxResults(size).list();
    }

    public List<HotWords> queryAll() {
        return getSession().createQuery(" from  HotWords hw where hw.enableStatus = '1'").list();
    }

}

