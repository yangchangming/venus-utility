package venus.oa.authority.auvisitor.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import venus.oa.authority.auvisitor.dao.IAuVisitorDao;
import venus.oa.authority.auvisitor.util.IConstants;
import venus.oa.authority.auvisitor.vo.AuVisitorVo;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 团体类型DAO
 * 
 * @author wumingqiang
 *  
 */
public class AuVisitorDao extends BaseTemplateDao implements IAuVisitorDao, IConstants {

    /**
     * 获得记录数
     * 
     * @return
     */
    public int getRecordCount() {
        String strSql = GETRECORDCOUNT_SQL;
        return queryForInt(strSql);
    }

    /**
     * 按条件获得记录数
     * 
     * @param queryCondition
     * @return
     */
    public int getRecordCount(Object objVo) {
        AuVisitorVo obj = (AuVisitorVo) objVo;
        String strSql = GETRECORDCOUNT_SQL + " where 1=1";
        if (obj.getName() != null && !obj.getName().equals(""))
            strSql += " and name like'%" + obj.getName() + "%'";
        if (obj.getVisitor_type() != null && !obj.getVisitor_type().equals(""))
            strSql += " and VISITOR_TYPE='" + obj.getVisitor_type() + "'";
        return queryForInt(strSql);
    }
    /**
     * 按条件获得记录数
     * 
     * @param queryCondition
     * @return
     */
    public int getRecordCountByTypes(String partyTypes){
        String strSql = null;
        if ("1".equals(partyTypes)) {
            strSql = COUNT_PA_TY_UES_TYPES + " and  b.keyword='" + partyTypes + "'";
                    
        } else {
            strSql = COUNT_PARTY_TYPE_TYPES + " and  b.keyword='" + partyTypes + "'";
        }
        return queryForInt(strSql);
    }
    /**
     * 删除
     * 
     * @param id
     * @return
     */
    public int delete(String id) {
        Object[] myobj = { id };
        return update(DELETE_VISITOR_SQL, myobj);
    }

    /**
     * 添加
     * 
     * @param rvo
     * @return
     */
    public String insert(Object objVo) {
        AuVisitorVo obj = (AuVisitorVo) objVo;
        OID oid = Helper.requestOID(OID);
        String id = String.valueOf(oid);
        obj.setId(id);
        Object[] myobj = { obj.getId(), obj.getOriginal_id(), obj.getPartyrelationtype_id(), obj.getPartytype_id(),
                obj.getVisitor_type(), obj.getName(), obj.getCode(), obj.getCreate_date() };
        update(INSERT_SQL, myobj);

        return obj.getId();
    }

    /**
     * 查找
     * 
     * @param id
     * @return
     */
    public Object findByOrgId(String id) {
        return queryForObject(QUERY_BY_ORGID, new Object[] { id }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuVisitorVo rvo = new AuVisitorVo();
                Helper.populate(rvo, rs);
                return rvo;
            }
        });
    }

    public Object findByOrgId(String id, String type) {
        return queryForObject(QUERY_BY_ORGID_TYPE, new Object[] { id, type }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuVisitorVo rvo = new AuVisitorVo();
                Helper.populate(rvo, rs);
                return rvo;
            }
        });
    }

    /**
     * 更新
     * 
     * @param objVo
     * @return
     */
    public int update(Object objVo) {
        AuVisitorVo obj = (AuVisitorVo) objVo;
        Object myobj[] = { obj.getOriginal_id(), obj.getPartyrelationtype_id(), obj.getPartytype_id(),
                obj.getVisitor_type(), obj.getName(), obj.getCode(), obj.getModify_date(), obj.getId() };
        return update(SQL_UPDATE_BY_ID, myobj);
    }

    /**
     * 按条件查询,返回LIST
     * 
     * @param no
     * @param size
     * @param orderStr
     * @param objVo
     * @return
     */
    public List simpleQuery(int no, int size, String orderStr, Object objVo) {
        AuVisitorVo obj = (AuVisitorVo) objVo;
        String strSql = QUERY_BYCONDITION1_SQL + " where 1=1 ";

        if (obj.getName() != null && !obj.getName().equals(""))
            strSql += " and name like'%" + obj.getName() + "%'";
        if (obj.getVisitor_type() != null && !obj.getVisitor_type().equals(""))
            strSql += " and VISITOR_TYPE='" + obj.getVisitor_type() + "'";
        if (orderStr != null)
            strSql += " order by " + orderStr;
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuVisitorVo obj = new AuVisitorVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(strSql, rowMapper, (no - 1) * size, size);
        return result;
    }

    /**
     * 通过团体类型分类查询所有
     * 
     * @param no
     * @param size
     * @param orderStr
     * @return
     */
    public List queryAllByTypes(int no, int size, String orderStr) {
        String strSql = null;
        if ("1".equals(orderStr)) {
            strSql = SQL_PA_TY_UES_TYPES + " and  b.keyword='" + orderStr + "'";
                    
        } else {
            strSql = SQL_PARTY_TYPE_TYPES + " and  b.keyword='" + orderStr + "'";
        }
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuVisitorVo obj = new AuVisitorVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(strSql, rowMapper, (no - 1) * size, size);
        return result;
    }

    /**
     * 通过团体类型分类按条件查询
     * 
     * @param no
     * @param size
     * @param orderStr
     * @param objVo
     * @return
     */
    public List simpleQueryByTypes(int no, int size, String orderStr, Object objVo, String condition) {
        AuVisitorVo obj = (AuVisitorVo) objVo;
        String strSql = null;
        if ("1".equals(orderStr)) {
            strSql = SQL_PA_TY_UES_TYPES + " and  b.keyword='" + orderStr + "'";
            if(obj != null) {
             	strSql += " and a.name like'%" + obj.getName()+ "%'";
             }
            if( ! "".equals(condition)) {
            	strSql += " and "+condition;
            }
        } else {
            strSql = SQL_PARTY_TYPE_TYPES + " and  b.keyword='" + orderStr + "'";
            if(obj != null) {
             	strSql += " and a.name like'%" + obj.getName()+ "%'";
             }
            if( ! "".equals(condition)) {
            	strSql += " and "+condition;
            }
        }
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuVisitorVo obj = new AuVisitorVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(strSql, rowMapper, (no - 1) * size, size);
        return result;
    }
    /**
     * 按条件获得记录数
     * 
     * @param queryCondition
     * @return
     */
    public int getRecordCountByTypes(String partyTypes, Object objVo, String condition){
        AuVisitorVo obj = (AuVisitorVo) objVo;
        String strSql = null;
        if ("1".equals(partyTypes)) {
            strSql = COUNT_PA_TY_UES_TYPES + " and  b.keyword='" + partyTypes +"'";
             if(obj != null) {
             	strSql += " and a.name like'%" + obj.getName()+ "%'";
             }
             if( ! "".equals(condition)) {
            	strSql += " and "+condition;
            }
        } else {
            strSql = COUNT_PARTY_TYPE_TYPES + " and  b.keyword='" + partyTypes + "'";
            if(obj != null) {
             	strSql += " and a.name like'%" + obj.getName()+ "%'";
             }
            if( ! "".equals(condition)) {
            	strSql += " and "+condition;
            }
        }
        return queryForInt(strSql);
    }

    /* (non-Javadoc)
     * @see venus.authority.au.auvisitor.dao.IAuVisitorDao#find(java.lang.String)
     */
    public AuVisitorVo find(String visitorId) {
        return (AuVisitorVo) queryForObject(QUERY_BY_ID, new Object[] { visitorId }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuVisitorVo rvo = new AuVisitorVo();
                Helper.populate(rvo, rs);
                return rvo;
            }
        });
    }
}

