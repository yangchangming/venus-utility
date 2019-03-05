package venus.oa.organization.auconnectrule.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import venus.oa.organization.auconnectrule.dao.IAuConnectRuleDao;
import venus.oa.organization.auconnectrule.util.IConstants;
import venus.oa.organization.auconnectrule.vo.AuConnectRuleVo;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 团体规则DAO
 * @author wumingqiang
 */
@Repository
public class AuConnectRuleDao extends BaseTemplateDao implements IAuConnectRuleDao,
        IConstants {

    
    /**
     * 查询所有
     * @param no
     * @param size
     * @param orderStr
     * @return
     */
    public List queryAll(int no, int size, String orderStr) {
        String strSql = null;
        if (orderStr == null)
            strSql = IConstants.QUERY_SQL;
        else
            strSql = IConstants.QUERY_SQL + " order by " + orderStr;
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuConnectRuleVo obj = new AuConnectRuleVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        
        List result = query(strSql, rowMapper, (no - 1) * size, size);
        
        return result;
    }

    
    /**
     * 获得记录数
     * @return
     */
    public int getRecordCount() {
        String strSql = IConstants.GETRECORDCOUNT_SQL;
        return queryForInt(strSql);
    }

    
    /**
     * 按条件获得记录数
     * @param queryCondition
     * @return
     */
    public int getRecordCount(String queryCondition) {
        String strSql = IConstants.GETRECORDCOUNT_SQL;
        if (queryCondition != null && queryCondition.length() > 0)
            strSql += " where name like'%" + queryCondition + "%'"; 
        return queryForInt(strSql);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public int delete(String id) {
    	String strSql = DELETEMULTI_SQL;
        if (id != null && id.length() > 0)
            strSql += " where id in (" + id + ")";
        return update(strSql);
    }

    /**
     *  添加
     * @param rvo
     * @return
     */
    public OID insert(Object objVo) {
	    AuConnectRuleVo obj = (AuConnectRuleVo) objVo;

        OID oid = Helper.requestOID(OID);
        String roomOid = String.valueOf(oid);
        obj.setId(roomOid);       
		
        Object[] myobj ={ obj.getId(),
                    obj.getRelation_type_id(),
                    obj.getParent_partytype_id(),
                    obj.getChild_partytype_id(),
                    obj.getName(),
                    obj.getCreate_date(),
					obj.getModify_date(),
					obj.getRemark()
                   };
         update(IConstants.INSERT_SQL, myobj);
         
        return oid;
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public Object find(String id) {
        return  queryForObject(IConstants.QUERY_SQL + " where id=? ",
                new Object[] { id }, new RowMapper() {
                    public Object mapRow(ResultSet rs, int i)
                            throws SQLException {
                        AuConnectRuleVo rvo = new AuConnectRuleVo();
                        Helper.populate(rvo, rs);
                        return rvo;
                    }
                });
    }

  

    /**
     * 更新
     * @param objVo
     * @return
     */
    public int update(Object objVo) {
    	AuConnectRuleVo obj = (AuConnectRuleVo) objVo;
        Object myobj[] = { obj.getRelation_type_id(),
                    obj.getParent_partytype_id(),
                    obj.getChild_partytype_id(),
                    obj.getName(),
					obj.getModify_date(),
					obj.getRemark(),
					obj.getId()
                     };
         return update(IConstants.SQL_UPDATE_BY_ID, myobj);
        
    }


    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param objVo
     * @return
     */
    public List simpleQuery(int no, int size, String orderStr, Object objVo) {
    	AuConnectRuleVo obj = (AuConnectRuleVo) objVo;
        String strSql = IConstants.QUERY_BYCONDITION1_SQL;
        if (obj.getName() != null && !obj.getName().equals(""))
            strSql += " AND name like'%" + obj.getName() + "%'";
        if (obj.getChild_partytype_id() != null && !obj.getChild_partytype_id().equals(""))
            strSql += " AND child_partytype_id ='" + obj.getName() + "'";
        if (obj.getParent_partytype_id() != null && !obj.getParent_partytype_id().equals(""))
            strSql += " AND parent_partytype_id ='" + obj.getName() + "'";
        if (orderStr != null)
            strSql += " order by " + orderStr;
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuConnectRuleVo obj = new AuConnectRuleVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(strSql, rowMapper, (no - 1) * size, size);
        return result;
    }
    
    /**
     * 按条件查询,返回LIST
     * @param objVo
     * @return
     */
    public List queryByType(Object objVo) {
    	AuConnectRuleVo obj = (AuConnectRuleVo) objVo;
        String strSql = IConstants.QUERY_BYCONDITION1_SQL;
        if (obj.getRelation_type_id() != null && !obj.getRelation_type_id().equals(""))
            strSql += " AND relation_type_id='" + obj.getRelation_type_id() + "'";
        if (obj.getParent_partytype_id() != null && !obj.getParent_partytype_id().equals(""))
            strSql += " AND parent_partytype_id='" + obj.getParent_partytype_id() + "'";
        if (obj.getChild_partytype_id() != null && !obj.getChild_partytype_id().equals(""))
            strSql += " AND child_partytype_id='" + obj.getChild_partytype_id() + "'";
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuConnectRuleVo obj = new AuConnectRuleVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(strSql, rowMapper);
        return result;
    }

    /**
     *按条件查询获取记录数
     * @param m
     * @param n
     * @return
     */
    public int getRecordCount(String m, String n) {
        StringBuffer sb = new StringBuffer();
        sb.append(IConstants.GETRECORDCOUNT_SQL);

        sb.append(" where 1=1 ");

        if (!StringUtils.isEmpty(m)) {
            sb.append(" AND Id like '%");
            sb.append(StringUtils.trim(m));
            sb.append("%' ");
        }

        if (!StringUtils.isEmpty(n)) {
            sb.append(" AND name like '%");
            sb.append(StringUtils.trim(n));
            sb.append("%' ");
        }

        return queryForInt(sb.toString());
    }
    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param objVo
     * @return
     */
    public List queryByName(Object objVo) {
        AuConnectRuleVo obj = (AuConnectRuleVo) objVo;
        String strSql = QUERY_BYCONDITION1_SQL;
        
        if (obj.getName() != null && !obj.getName().equals(""))
            strSql += " and name='" + obj.getName() + "'";
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuConnectRuleVo obj = new AuConnectRuleVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(strSql, rowMapper);
        return result;
    }
}

