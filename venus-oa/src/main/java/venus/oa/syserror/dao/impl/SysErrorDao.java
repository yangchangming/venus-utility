/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.syserror.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import venus.oa.syserror.dao.ISysErrorDao;
import venus.oa.syserror.util.IContants;
import venus.oa.syserror.vo.SysErrorVo;
import venus.oa.util.VoHelperTools;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.base.exception.BaseDataAccessException;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author zangjian
 *
 */
public class SysErrorDao extends BaseTemplateDao implements IContants, ISysErrorDao {
    
    private LobHandler lobHandler = null;  

    public void setLobHandler(LobHandler lobHandler) {
        this.lobHandler = lobHandler;
    }

    public int getRecordCount() {
        return queryForInt(SQL_QUERY_COUNT);
    }

    public int getRecordCount(String queryCondition) {
        String strsql = SQL_QUERY_COUNT + " WHERE 1=1 ";
        if (queryCondition != null && queryCondition.length() > 0) {
            strsql += " AND " + queryCondition;
        }
        return queryForInt(strsql);
    }

    public List queryByCondition(int no, int size, String queryCondition) {
        String strsql = SQL_QUERY + " WHERE 1=1 ";
        if (queryCondition != null && queryCondition.length() > 0) {
            strsql += " AND " + queryCondition; //where后加上查询条件
        }
        strsql += " ORDER BY OPERATE_DATE DESC";
        return query(strsql, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                SysErrorVo vo = new SysErrorVo();
                vo.setId(rs.getString("id"));
                vo.setOperate_id(rs.getString("operate_id"));
                vo.setOperate_name(rs.getString("operate_name"));
                vo.setOperate_date(rs.getTimestamp("operate_date"));
                vo.setError_type(rs.getString("error_type"));
                vo.setSource_id(rs.getString("source_id"));
                vo.setSource_partyid(rs.getString("source_partyid"));
                vo.setSource_code(rs.getString("source_code"));
                vo.setSource_name(rs.getString("source_name"));
                vo.setSource_orgtree(rs.getString("source_orgtree"));
                vo.setSource_typeid(rs.getString("source_typeid"));
                vo.setCloumn1(rs.getString("cloumn1"));
                vo.setRemark(lobHandler.getClobAsString(rs, 12));
                return vo;
            }
        },(no - 1) * size,size);        
    }

    public OID insert (final SysErrorVo vo) {
        String strSql = SQL_INSERT;
        OID oid = Helper.requestOID(TABLE_NAME);
        vo.setId(oid.toString());
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        try {
            update(strSql,new AbstractLobCreatingPreparedStatementCallback( this.lobHandler){
                protected void setValues(PreparedStatement ps,LobCreator lobCreator) throws SQLException {
                    ps.setString(1,vo.getId());
                    ps.setString(2,vo.getOperate_id());
                    ps.setString(3,vo.getOperate_name());
                    ps.setTimestamp(4,vo.getOperate_date());
                    ps.setString(5,vo.getError_type());
                    ps.setString(6,vo.getSource_id());
                    ps.setString(7,vo.getSource_partyid());
                    ps.setString(8,vo.getSource_code());
                    ps.setString(9,vo.getSource_name());
                    ps.setString(10,vo.getSource_orgtree());
                    ps.setString(11,vo.getSource_typeid());
                    lobCreator.setClobAsString(ps,12,vo.getSource_detail());
                    ps.setString(13, vo.getRemark());
                    ps.setString(14, vo.getCloumn1());
                }
            });
        } catch (BaseDataAccessException e) {
            logger.error(e.getMessage());
            throw new BaseDataAccessException("BaseDataAccessException: " + e);
        }       
        return oid;        
    }

    public int delete (SysErrorVo vo) {
        StringBuffer strsql = new StringBuffer(SQL_DELETE + " WHERE 1=1 ");
        if (vo == null)
            return 0;
        if (vo.getId() != null) {
            strsql.append(" AND ID = '").append(vo.getId()).append("'");
        }
        if (vo.getError_type() != null) {
            strsql.append(" AND ERROR_TYPE = '").append(vo.getError_type()).append("'");
        }
        return update(strsql.toString());
    }

    public int deleteAll () {
        return update(SQL_DELETE);
    }
    
}

