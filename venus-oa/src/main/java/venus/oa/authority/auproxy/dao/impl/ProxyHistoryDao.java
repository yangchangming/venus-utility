/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.authority.auproxy.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import venus.oa.authority.auproxy.dao.IProxyHistoryDao;
import venus.oa.authority.auproxy.util.IConstants;
import venus.oa.authority.auproxy.vo.ProxyHistoryVo;
import venus.oa.util.VoHelperTools;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
@Repository
public class ProxyHistoryDao extends BaseTemplateDao implements IProxyHistoryDao,IConstants {

    /* (non-Javadoc)
     * @see venus.authority.au.auproxy.dao.IProxyHistoryDao#find(java.lang.String)
     */
    public ProxyHistoryVo find(String id) {
        return (ProxyHistoryVo) queryForObject(QUERY+" where ID=?", new Object[] { id }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                ProxyHistoryVo vo = new ProxyHistoryVo();
                Helper.populate(vo, rs);
                return vo;
            }
        });
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auproxy.dao.IProxyHistoryDao#getRecordCount(java.lang.String)
     */
    public int getRecordCount(String queryCondition) {
        String strsql = SQL_COUNT + QUERY_WHERE_ENABLE;
        if (queryCondition != null && queryCondition.length() > 0) {
            strsql += " AND " + queryCondition; //where后加上查询条件
        }
        return queryForInt(strsql);
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auproxy.dao.IProxyHistoryDao#insert(venus.authority.au.auproxy.vo.ProxyHistoryVo)
     */
    public String insert(ProxyHistoryVo vo) {
        OID oid = Helper.requestOID(OID);
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        vo.setId(oid.toString());
        Object[] obj = { vo.getId(), vo.getProxy_history_id(),vo.getProxy_proxyer_history_id(),vo.getProxy_authorize_history_id(),vo.getOperater_id(),vo.getOperater_name(),vo.getOperater_date(),vo.getLogin_name(),vo.getOperater_type(),vo.getNotice_note(),vo.getColumn1(),vo.getSponsor(),vo.getSponsor_id(),vo.getProxy(),vo.getProxy_id(),vo.getRecipient(),vo.getRecipient_id(),vo.getCanel_id(),vo.getCanel_name(),vo.getCanel_date()};
        updateWithUniformArgType(SQL_INSERT, obj);
        return oid.toString();
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auproxy.dao.IProxyHistoryDao#queryByCondition(int, int, java.lang.String, java.lang.String)
     */
    public List queryByCondition(int no, int size, String queryCondition,
            String orderStr) {
        String strsql = QUERY + QUERY_WHERE_ENABLE;
        if (queryCondition != null && queryCondition.length() > 0) {
            strsql += " AND " + queryCondition; //where后加上查询条件
        }
        if(orderStr == null ) {
            strsql += ORDER_BY_ID;
        } else {
            strsql += ORDER_BY_SYMBOL + orderStr;
        }
        if(no <= 0 || size <= 0) {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    ProxyHistoryVo vo = new ProxyHistoryVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            });
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    ProxyHistoryVo vo = new ProxyHistoryVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            }, (no - 1) * size, size); 
        }
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auproxy.dao.IProxyHistoryDao#update(venus.authority.au.auproxy.vo.ProxyHistoryVo)
     */
    public void update(ProxyHistoryVo vo) {
        Object[] obj = { vo.getCanel_id(), vo.getCanel_name(),vo.getCanel_date(),vo.getOperater_type(),vo.getId()};
        updateWithUniformArgType(SQL_UPDATE, obj);
    }

}

