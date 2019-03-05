/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.authority.auauthorizelog.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import venus.oa.authority.auauthorize.vo.AuAuthorizeVo;
import venus.oa.authority.auauthorizelog.dao.IAuAuthorizeLogDao;
import venus.oa.authority.auauthorizelog.util.IConstants;
import venus.oa.authority.auauthorizelog.vo.AuAuthorizeLogVo;
import venus.oa.util.SqlBuilder;
import venus.oa.util.StringHelperTools;
import venus.oa.util.VoHelperTools;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author zangjian
 *
 */
@Repository
public class AuAuthorizeLogDao extends BaseTemplateDao implements IAuAuthorizeLogDao, IConstants {

    /**
     * 查询总记录数，带查询条件
     * @param sql
     * @return
     */
    public int getRecordCount(SqlBuilder sql) {
        String strsql = RECORD_COUNT_SQL;
        if (sql != null) {
            strsql += " WHERE " + sql.bulidSql(); //where后加上查询条件
        }
        return queryForInt(strsql,sql.getData().toArray());
    }
    
    /**
     * 通过查询条件获得所有的VO对象列表
     * @param sql
     * @return 查询到的VO列表
     */
    public List queryByCondition(SqlBuilder sql) {
	String strsql = QUERY_ALL_SQL;
        if (sql != null ) {
            strsql += " WHERE " + sql.bulidSql(); //where后加上查询条件
        }
        strsql += " ORDER BY AUTHORIZE_TAG DESC ";
        return query(strsql,sql.getData().toArray(), new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    AuAuthorizeLogVo vo = new AuAuthorizeLogVo();
                    VoHelperTools.null2Nothing(vo);
                    Helper.populate(vo, rs);
                    return vo;
                }
        },sql.getCountBegin(), sql.getCountEnd());    
    }    
    
    /**
     * 添加记录到权限日志表
     * @param vo
     * @return
     */
    public OID insert(AuAuthorizeLogVo vo) {
        OID oid = Helper.requestOID(OID);
        String id = String.valueOf(oid);
        vo.setId(id);
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] obj = { vo.getId(), vo.getOperate_date(),vo.getOperate_id(),vo.getOperate_name(),
        	vo.getVisitor_id(), vo.getVisitor_name(),vo.getVisitor_code(), vo.getVisitor_type(),
                vo.getResource_id(), vo.getResource_name(),vo.getResource_code(), vo.getResource_type(), 
                vo.getAuthorize_status(),vo.getAuthorize_tag(),vo.getAccredit_type(),vo.getCreate_date()};
        update(INSERT_SQL, obj);
        return oid;        
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auauthorizelog.dao.IAuAuthorizeLogDao#getRecordCount(java.lang.String)
     */
    public int getRecordCount(String queryCondition) {
        String strsql = RECORD_COUNT_SQL + " WHERE 1=1 ";
        if (queryCondition != null && queryCondition.length() > 0) {
            strsql += " AND " + queryCondition; //where后加上查询条件
        }
        return queryForInt(strsql);
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auauthorizelog.dao.IAuAuthorizeLogDao#queryByCondition(int, int, java.lang.String, java.lang.String)
     */
    public List queryByCondition(int no, int size, String queryCondition,
            String orderStr) {
        String strsql = QUERY_ALL_SQL + " WHERE 1=1 ";
        if (queryCondition != null && queryCondition.length() > 0) {
            strsql += " AND " + queryCondition; //where后加上查询条件
        }
        if(orderStr == null ) {
            strsql += " ORDER BY ID ";
        } else {
            strsql += ORDER_BY_SYMBOL + orderStr;
        }
        if(no <= 0 || size <= 0) {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    AuAuthorizeLogVo vo = new AuAuthorizeLogVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            });
        } else {
            return query(strsql, new RowMapper() {
                public Object mapRow(ResultSet rs, int i) throws SQLException {
                    AuAuthorizeLogVo vo = new AuAuthorizeLogVo();
                    Helper.populate(vo, rs);
                    return vo;
                }
            }, (no - 1) * size, size); 
        }
    }

    /**
     * 
     * 功能: 根据访问者Code数组和资源类型查询该访问者自身拥有的权限+在同一团体关系类型内它所继承的权限
     *      如果资源类型为null，则查询全部资源类型的权限
     *
     * @param visiCodes 访问者编号数组
     * @param resType 资源类型
     * @return
     */
    public List queryByVisitorCode(String[] visiCodes, String resType, String auHisTag) {

        String strSql = QUERY_ALL_SQL + " where VISITOR_CODE in (" + StringHelperTools.parseToSQLStringComma(visiCodes) + ")";
        if (resType != null && resType.length()>0) {
            strSql += " and RESOURCE_TYPE='" + resType + "'";
        }
        if (auHisTag != null && auHisTag.length()>0) {
            strSql += " and AUTHORIZE_TAG='" + auHisTag + "'";
        }
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuAuthorizeVo vo = new AuAuthorizeVo();
                Helper.populate(vo, rs);
                return vo;
            }
        };
        List result = query(strSql, rowMapper);
        return result;
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auauthorizelog.dao.IAuAuthorizeLogDao#queryByVisitorCodeWithOutHistory(java.lang.String[], java.lang.String)
     */
    public List queryByVisitorCodeWithOutHistory(String[] visiCodeArray,String resType,
            String auHisTag) {
        String strSql = QUERY_ALL_SQL + ",AU_PARTYRELATION r  where AU_AUTHORIZE_LOG.RESOURCE_CODE=r.code and VISITOR_CODE in (" + StringHelperTools.parseToSQLStringComma(visiCodeArray) + ") and RESOURCE_TYPE='5' ";
        if (auHisTag != null && auHisTag.length()>0) {
            strSql += " and AU_AUTHORIZE_LOG.AUTHORIZE_TAG='" + auHisTag + "'";
        }
        if (resType!=null && resType.length()>0) {
            strSql += " and r.RELATIONTYPE_ID='" + resType + "'";
        }
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuAuthorizeVo vo = new AuAuthorizeVo();
                Helper.populate(vo, rs);
                return vo;
            }
        };
        List result = query(strSql, rowMapper);
        return result;
    }
    
}

