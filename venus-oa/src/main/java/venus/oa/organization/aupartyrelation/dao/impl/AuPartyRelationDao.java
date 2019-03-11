/*
 * 创建日期 2006-10-24
 *  
 */
package venus.oa.organization.aupartyrelation.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import venus.dao.provider.BaseTemplateDao;
import venus.frames.base.exception.BaseApplicationException;
import venus.frames.mainframe.util.Helper;
import venus.oa.organization.auconnectrule.vo.AuConnectRuleVo;
import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.organization.aupartyrelation.dao.IAuPartyRelationDao;
import venus.oa.organization.aupartyrelation.util.IConstants;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.util.DateTools;
import venus.oa.util.ProjTools;
import venus.oa.util.StringHelperTools;
import venus.oa.util.VoHelperTools;
import venus.pub.lang.OID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author maxiao
 *  
 */
@Repository
public class AuPartyRelationDao extends BaseTemplateDao implements IAuPartyRelationDao, IConstants {

    /**
     * 对团体关系表进行查询
     * 
     * @param vo
     * @return
     */
    public List queryAuPartyRelation(AuPartyRelationVo vo) {
        StringBuffer strSql = new StringBuffer(QUERY_AU_PARTYRELATION_SQL);
        strSql.append(" where 1=1 ");
        if (!(vo.getId() == null || vo.getId().trim().equals(""))) {
            strSql.append(" and ID='" + vo.getId() + "' ");
        }
        if (!(vo.getName() == null || vo.getName().trim().equals(""))) {
            strSql.append(" and NAME LIKE '%" + vo.getName() + "%' ");
        }        
        if (!(vo.getRelationtype_id() == null || vo.getRelationtype_id().trim().equals(""))) {
            strSql.append(" and RELATIONTYPE_ID='" + vo.getRelationtype_id() + "' ");
        }
        if (!(vo.getParent_partyid() == null || vo.getParent_partyid().trim().equals(""))) {
            strSql.append(" and PARENT_PARTYID='" + vo.getParent_partyid() + "' ");
        }
        if (!(vo.getPartyid() == null || vo.getPartyid().trim().equals(""))) {
            strSql.append(" and PARTYID='" + vo.getPartyid() + "' ");
        }
        if (!(vo.getParent_code() == null || vo.getParent_code().trim().equals(""))) {
            strSql.append(" and PARENT_CODE='" + vo.getParent_code() + "' ");
        }
        if (!(vo.getCode() == null || vo.getCode().trim().equals(""))) {
            strSql.append(" and CODE='" + vo.getCode() + "' ");
        }
        if (!(vo.getPartytype_id() == null || vo.getPartytype_id().trim().equals(""))) {
            strSql.append(" and PARTYTYPE_ID='" + vo.getPartytype_id() + "' ");
        }
        strSql.append(" order by ORDER_CODE");
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyRelationVo vo = new AuPartyRelationVo();
                Helper.populate(vo, rs);
                return vo;
            }
        };
        return query(strSql.toString(), rowMapper);

    }
    /**
     * 
     * 功能:查询所有下级组织 
     *
     * @param parentCode
     * @return
     */
    public List queryAllByCode(String code) {
        String strSql = QUERY_AU_PARTYRELATION_SQL + " where PARENT_CODE like'" + code + "%' ";
        
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyRelationVo vo = new AuPartyRelationVo();
                Helper.populate(vo, rs);
                return vo;
            }
        };
        return query(strSql, rowMapper);

    }
    /**
     * 查询团体关系类型表，如查询结果为空或已禁用，则报错
     * 
     * @param vo
     * @return
     */
    public List queryAuPartyRelationType(AuPartyRelationVo vo) {
        String strSql = QUERY_AU_PARTYRELATIONTYPE_SQL + " where ID='" + vo.getRelationtype_id().trim() + "'";
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                if (rs.getString("enable_status").trim().equals("0")) {
                    throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Body_type_has_been_disabled_0"));
                }

                return rs.getString("keyword");
            }
        };
        List result = query(strSql, rowMapper);
        if (result.size() == 0) {
            throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.There_is_no_relationship_between_body_type_"));
        }

        return result;
    }

    /**
     * 查询团体表
     * 
     * @param vo
     * @return
     */
    public List queryAuParty(PartyVo vo) {
        String strSql = QUERY_AU_PARTY_SQL + " where ID='" + vo.getId().trim() + "'";
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                PartyVo obj = new PartyVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        return query(strSql, rowMapper);
    }

    /**
     * 查询连接规则表
     */
    public List queryAuConnectrule(AuConnectRuleVo vo) {
        String strSql = QUERY_AU_CONNECTRULE + " where RELATION_TYPE_ID='" + vo.getRelation_type_id().trim()
                + "' and PARENT_PARTYTYPE_ID='" + vo.getParent_partytype_id().trim() + "' and CHILD_PARTYTYPE_ID='"
                + vo.getChild_partytype_id().trim() + "'";
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuConnectRuleVo obj = new AuConnectRuleVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        return query(strSql, rowMapper);
    }

    /**
     * 新增一个团体关系
     * 
     * @param vo
     */
    public OID addAuPartyRelation(AuPartyRelationVo vo) {
        OID oid = Helper.requestOID(OID);
        String id = String.valueOf(oid);
        vo.setId(id);
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] myobj = { vo.getId().trim(), vo.getRelationtype_id().trim(), vo.getRelationtype_keyword().trim(),
                vo.getParent_partyid().trim(), vo.getParent_code().trim(), vo.getPartyid().trim(),
                vo.getCode().trim(), vo.getName().trim(), vo.getPartytype_id().trim(),
                vo.getType_level().trim(), vo.getOrder_code().trim(), vo.getIs_leaf(), vo.getType_is_leaf().trim(),
                vo.getIs_inherit().trim(), vo.getIs_chief().trim(), vo.getEmail().trim(),
                DateTools.getSysTimestamp(), DateTools.getSysTimestamp() };
        update(INSERT_AU_PARTYRELATION_SQL, myobj);
        return oid;
    }

    /**
     * 修改团体关系
     * 
     * @param vo
     */
    public void updateLeaf(AuPartyRelationVo vo) {
        String strSql = UPDATE_LEAF_SQL;
        Object[] myobj = { vo.getIs_leaf(), vo.getType_is_leaf(), DateTools.getSysTimestamp(), vo.getId(), };
        update(strSql, myobj);
    }

    /**
     * 删除团体关系
     * 
     * @param code
     */
    public void deleteAuPartyRelation(String id) {
        String strSql = DELETE_AU_PARTYRELATION_SQL + " where id ='" + id + "'";
        update(strSql);
    }

    /**
     * 修改团体关系
     * 
     * @param vo
     */
    public int update(AuPartyRelationVo vo) {
        String strSql = UPDATE_AU_PARTYRELATION_SQL;
        Object[] myobj = { vo.getRelationtype_id(), vo.getRelationtype_keyword(), vo.getParent_partyid(), vo.getParent_code(), vo.getPartyid(), vo.getCode(), vo.getName(), vo.getPartytype_id(), vo.getType_level(), vo.getOrder_code(), vo.getIs_leaf(), vo.getType_is_leaf(), vo.getIs_inherit(), vo.getIs_chief(), vo.getEmail(), vo.getCreate_date(), vo.getModify_date(), vo.getId() };
        return updateWithUniformArgType(strSql, myobj);
    }

    /**
     * 
     * 功能:查询父编号为parentCode的节点的个数
     * 
     * @see venus.oa.organization.aupartyrelation.dao.IAuPartyRelationDao#getCountByParentCode(String)
     * @param parentCode
     * @return
     */
    public int getCountByParentCode(String parentCode) {
        String strsql = SQL_COUNT + " where parent_code='" + parentCode + "'";
        return queryForInt(strsql);
    }

    /**
     * 根据Id进行查询
     * 
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public AuPartyRelationVo find(String id) {
        return (AuPartyRelationVo) queryForObject(SQL_FIND_BY_ID, new Object[] { id }, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyRelationVo vo = new AuPartyRelationVo();
                Helper.populate(vo, rs);
                vo.setCode(rs.getString("CODE"));
                return vo;
            }
        });
    }

    /**
     * 查询当前parentcode的父节点中是childpartyid的团体
     * 
     * @param parentcode
     * @param childPartyId
     * @return
     */
    public List queryAuParentRelation(String parentcode, String childPartyId) {
        String currcode[] = ProjTools.splitTreeCode(parentcode);
        StringBuffer strSql = new StringBuffer(QUERY_AU_PARTYRELATION_SQL);
        strSql.append(" where CODE in(" + StringHelperTools.parseToSQLStringComma(currcode) + ")");
        strSql.append(" and PARTYID='" + childPartyId + "'");

        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyRelationVo obj = new AuPartyRelationVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        return query(strSql.toString(), rowMapper);

    }

    /**
     * 
     * 功能: 查询当前节点的所有上级节点，一直到根节点
     * 
     * @param code 当前节点的父节点编号
     * @return
     */
    public List queryParentRelation(String parentCode) {
        String currcode[] = ProjTools.splitTreeCode(parentCode);
        StringBuffer strSql = new StringBuffer(QUERY_AU_PARTYRELATION_SQL);
        strSql.append(" where CODE in(" + StringHelperTools.parseToSQLStringComma(currcode) + ") order by CODE");

        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyRelationVo vo = new AuPartyRelationVo();
                Helper.populate(vo, rs);
                return vo;
            }
        };
        return query(strSql.toString(), rowMapper);
    }

    /**
     * 功能: 查询当前节点的所有上级节点，一直到根节点
     * @param code
     * @return
     */
    public List getParentRelation(String code) {
    	if (code == null || code.length() == 0)
    		return new ArrayList();
        String codes[] = ProjTools.splitTreeCode(code);
        StringBuffer strSql = new StringBuffer(SQL_FIND_PARTYRELATION);
        strSql.append(" where CODE in (" + StringHelperTools.parseToSQLStringComma(codes) + ") order by CODE DESC");

        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyRelationVo vo = new AuPartyRelationVo();
                Helper.populate(vo, rs);
                return vo;
            }
        };
        return query(strSql.toString(), rowMapper);    	
    }
    
    /**
     * 根据code列表获得name的Map
     * 
     * @param lCode code的列表
     * @return 查询到的VO对象
     */
    public Map getNameMapByCode(ArrayList lCode) {
        Map map = new HashMap();
        for (int i = 0; i < lCode.size(); i++) {
            AuPartyRelationVo vo = (AuPartyRelationVo) queryForObject(SQL_FIND_BY_CODE, new Object[] { lCode.get(i) },
                    new RowMapper() {
                        public Object mapRow(ResultSet rs, int i) throws SQLException {
                            AuPartyRelationVo vo = new AuPartyRelationVo();
                            Helper.populate(vo, rs);
                            vo.setCode(rs.getString("NAME"));
                            return vo;
                        }
                    });
            if(null != vo)
            map.put(lCode.get(i), vo.getName());
        }
        return map;
    }

    /**
     * 按条件获得记录数
     * 
     * @param queryCondition
     * @return
     */
    public int getRecordCount(String queryCondition) {
        String strSql = SQL_COUNT;
        if (queryCondition != null && queryCondition.length() > 0)
            strSql += " where name like'%" + queryCondition + "%'";
        return queryForInt(strSql);
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
        AuPartyRelationVo obj = (AuPartyRelationVo) objVo;
        String strSql = SQL_SIMPLEQUERY_SQL;

        if (obj.getName() != null && !obj.getName().equals(""))
            strSql += "  and a.name like'%" + obj.getName() + "%'";
        if (orderStr != null)
            strSql += " order by " + orderStr;
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyRelationVo obj = new AuPartyRelationVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(strSql, rowMapper, (no - 1) * size, size);
        return result;
    }
    /* (non-Javadoc)
     * @see venus.authority.org.aupartyrelation.dao.IAuPartyRelationDao#queryRelationVoByKey(java.lang.String, java.lang.String, java.lang.String)
     */
    public AuPartyRelationVo queryRelationVoByKey(String childPartyId,
                                                  String parentRelId, String relTypeId) {
        return (AuPartyRelationVo) queryForObject(SQL_FIND_BY_KEY, new Object[] { childPartyId, parentRelId, relTypeId}, new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyRelationVo vo = new AuPartyRelationVo();
                Helper.populate(vo, rs);
                vo.setCode(rs.getString("CODE"));
                return vo;
            }
        });
    }
}

