/*
 * 创建日期 2006-10-24
 *
 */
package venus.oa.organization.auparty.dao.impl;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import venus.oa.organization.auparty.dao.IAuPartyDao;
import venus.oa.organization.auparty.util.IConstants;
import venus.oa.organization.auparty.vo.PartyVo;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.util.DateTools;
import venus.oa.util.VoHelperTools;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.base.exception.BaseApplicationException;
import venus.frames.mainframe.util.Helper;
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
public class AuPartyDao extends BaseTemplateDao implements IAuPartyDao,IConstants {
    /**
     * 新增团体
     * @param vo
     * @return
     */
    public String addParty (PartyVo vo){
        
        
        OID oid = Helper.requestOID(OID);
        String id = String.valueOf(oid);
        vo.setId(id);
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] myobj ={ vo.getId().trim(),
                    vo.getPartytype_id().trim(),
                    vo.getPartytype_keyword().trim(),
                    vo.getIs_inherit().trim(),
                    vo.getIs_real().trim(),
                    vo.getName().trim(),
                    vo.getEmail().trim(),
                    vo.getEnable_status().trim(),
                    vo.getEnable_date(),
                    vo.getCreate_date(),
                    vo.getModify_date(),
                    vo.getOwner_org(),
                    vo.getRemark()
                   };
        updateWithUniformArgType(INSERT_SQL_AU_PARTY, myobj);        
        return id;
    }
    /**
     * 据partytype_id查au_partytype表，如查询结果为空，则报错
     * @param partytype_id
     * @return
     * @throws Exception
     */
    public List queryPartytype(String partytype_id) {
        String strSql=QUERY_SQL_AU_PARTYTYPE_KEYWORD+" where ID='"+partytype_id.trim()+"'";
        
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {    
                return rs.getString("keyword");
            }
        };
        List result = query(strSql, rowMapper);
        if(result.size()==0){
            throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Body_type_does_not_exist_"));
        }
        return result;
    }
    /**
     * 据id查au_party表，如查询结果为空，则报错
     * @param id
     * @return
     */
    public List queryParty(String id){
        String strSql=QUERY_SQL_AU_PARTY+" where id='"+id.trim()+"'";
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                PartyVo obj = new PartyVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(strSql, rowMapper);
        if(result.size()==0){
            throw new BaseApplicationException(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.The_group_does_not_exist_"));
        }
        return result;
    }
    /**
     * 据name查au_party表
     * @param partyname
     * @return
     */
    public List queryPartyByName(PartyVo vo){
        String strSql=QUERY_SQL_AU_PARTY+" where enable_status = '1' and name='"+vo.getName()+"' and partytype_id='"+vo.getPartytype_id()+"'";
        
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                PartyVo obj = new PartyVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(strSql, rowMapper);
        return result;
    }
    
    /**
     * 更新团体表
     * @param vo
     */
    public void updateParty(PartyVo vo){
        Object[] myobj ={
                	vo.getName().trim(),
                	vo.getEmail().trim(),
                    DateTools.getSysTimestamp(),
                    vo.getOwner_org(),
                    vo.getRemark(),
                    vo.getId().trim()
                   };
        updateWithUniformArgType(UPDATE_SQL_AU_PARTY, myobj);        
    }
    /**
     * 查询团体关系表
     * @param party_id
     * @return
     */
    public List queryPartyRelation(String party_id){
        String strSql=QUERY_SQL_AU_PARTYRELATION+" where PARTYID='"+party_id.trim()+"'";
        
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyRelationVo obj = new AuPartyRelationVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(strSql, rowMapper);
        /*if(result.size()==0){
            throw new BaseApplicationException("不存在的团体关系！");
        }
        return result;*/
        if(result==null){
            result=new ArrayList();
        }
        return result;
    }
    /**
     * 更新团体关系表
     * @param vo
     * @return
     */
    public void updatePartyRelation(AuPartyRelationVo vo){
        Object[] myobj ={
            	vo.getName().trim(),
            	vo.getEmail().trim(),
                DateTools.getSysTimestamp(),
                vo.getId().trim()
               };
     update(UPDATE_SQL_AU_PARTYRELATION, myobj);    
    }
    /**
     * 禁用团体
     * @param id
     */
    public void disableParty(String id){
        Object[] myobj ={
            	"0",
                DateTools.getSysTimestamp(),
                id.trim()
               };
     update(DISABLE_SQL_AU_PARTY, myobj);  
    }
    /**
     * 删除团体关系
     * @param code
     */
    public void deletePartyRelation(String code){
        String strSql=DELETE_SQL__AU_PARTYRELATION+" where code like '"+code.trim()+"%'";
        update(strSql);
    }
    /**
     * 根据parent_code查询数据库
     * @param parent_code
     * @return
     */
    public List findPartyRelationForParent_Code(String parent_code){
        String strSql=QUERY_SQL_AU_PARTYRELATION+" where PARENT_CODE='"+parent_code.trim()+"'";
        
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyRelationVo obj = new AuPartyRelationVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };

        return  query(strSql, rowMapper);
    }
    /**
     * 设置父节点为叶子节点
     * @param id
     */
    public void setLeafPartyRelation(String code) {
        Object[] myobj ={
            	"1",
            	"1",
                DateTools.getSysTimestamp(),
                code.trim()
               };
     update(UPDATE_SQL_AU_PARTYRELATION_IS_LEAF, myobj);  
    }   
       
    /**
     * 按条件获得记录数
     * @param queryCondition
     * @return
     */
    public int getRecordCount(String typeId, Object objVo) {
        PartyVo obj = (PartyVo) objVo;
        String strSql = GETRECORDCOUNT_SQL;
        strSql += " where enable_status='1' and partytype_id='" + typeId+"'";
        if (obj.getName() != null && !obj.getName().equals(""))
            strSql += " and name like'%" + obj.getName() + "%'";
        return queryForInt(strSql);
    }
    /**
     * 按条件获得记录数
     * @param queryCondition
     * @return
     */
    public int getRecordCountPerson(Object objVo) {
        PartyVo obj = (PartyVo) objVo;
        String strSql = SQL_QUERY_PERSON_COUNT;
        if (obj.getName() != null && !obj.getName().equals(""))
            strSql += " and a.name like'%" + obj.getName() + "%'";
        return queryForInt(strSql);
    }
    /**
     * 按条件获得记录数
     * @param queryCondition
     * @return
     */
    public int getRecordCountPerson(String queryCondition) {
        String strSql = SQL_QUERY_PERSON_COUNT;
        if (queryCondition != null && queryCondition.length() > 0) {
        	strSql += " and " + queryCondition;
        }    
        return queryForInt(strSql);
    }    
    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param typeId
     * @param objVo
     * @return
     */
    public List simpleQuery(int no, int size, String orderStr, String typeId, Object objVo) {
    	PartyVo obj = (PartyVo) objVo;
        String strSql = QUERY_SQL_ALL;
        strSql += " where enable_status='1' and partytype_id='" + typeId+"'";
        if (obj.getName() != null && !obj.getName().equals(""))
            strSql += " and name like'%" + obj.getName() + "%'";
        if (orderStr != null)
            strSql += " order by " + orderStr;
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                PartyVo obj = new PartyVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(strSql, rowMapper, (no - 1) * size, size);
        return result;
    }
    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param typeId
     * @param objVo
     * @return
     */
    public List simpleQueryPerson(int no, int size, String orderStr, Object objVo) {
    	PartyVo obj = (PartyVo) objVo;
        String strSql = SQL_QUERY_PERSON;
        if (obj.getName() != null && !obj.getName().equals(""))
            strSql += " and a.name like'%" + obj.getName() + "%'";
        if (orderStr != null)
            strSql += " order by " + orderStr;
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                PartyVo obj = new PartyVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(strSql, rowMapper, (no - 1) * size, size);
        return result;
    }
    /**
     * 按条件查询,返回LIST
     * @param no
     * @param size
     * @param orderStr
     * @param typeId
     * @param queryCondition
     * @return
     */
    public List simpleQueryPerson(int no, int size, String orderStr, String queryCondition) {
        String strSql = SQL_QUERY_PERSON;
        if (queryCondition != null && queryCondition.length() > 0) {
        	strSql += " and " + queryCondition;
        }  
        if (orderStr != null) {
            strSql += " order by " + orderStr + ",a.id desc";
        } else {
        	strSql += " order by a.id desc";
        }
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                PartyVo obj = new PartyVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(strSql, rowMapper, (no - 1) * size, size);
        return result;
    }    
    /**
     * 启用
     * @param id
     * @return
     */
    public int enableParty(String id) {
        Object[] myobj ={
            	"1",
                DateTools.getSysTimestamp(),
                id.trim()
               };
        return update(DISABLE_SQL_AU_PARTY, myobj);  

	}
    /**
     * 查找
     * @param id
     * @return
     */
	public Object find(String id) {
		return queryForObject(QUERY_SQL_ALL + " where id=? ", new Object[] { id },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
					    PartyVo rvo = new PartyVo();
						Helper.populate(rvo, rs);
						return rvo;
					}
				});
	}    

	/**
     * 查询团体关系表
     * @param party_id
     * @return
     */
    public List queryAllPartyRelation(String party_id){
        Object[] myobj ={
                party_id
               };
        
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyRelationVo obj = new AuPartyRelationVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(QUERY_SQL_ALL_AU_PARTYRELATION, myobj, rowMapper);        
        return result;
    }

	/**
     * 查询团体关系表
     * @param party_id
     * @return
     */
    public List queryAllPartyRelationDivPage(int no, int size, String party_id, Object objVo){
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyRelationVo obj = new AuPartyRelationVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(QUERY_SQL_ALL_AU_PARTYRELATION_BEGIN+party_id+QUERY_SQL_ALL_AU_PARTYRELATION_END, rowMapper, (no - 1) * size, size);
        return result;
    }
    /**
     * 查询团体关系表
     * @param party_id
     * @return
     */
    public int getRecordCountPartyRelation(String party_id){
        return queryForInt("select count(*) from ("+QUERY_SQL_ALL_AU_PARTYRELATION_BEGIN+party_id+"') sub_query");        
    }
    /**
     * 通过Id列表获得name的Map
     * @param party_id
     * @return
     */
    public Map getNameMapByKey(List lPartyId){
        Map map=new HashMap();
        for(int i=0;i<lPartyId.size();i++){
            map.put(lPartyId.get(i),((PartyVo)find((String)lPartyId.get(i))).getName());
        }
        return map;
    }
    
    /**
     * 
     * 功能: 根据id删除一条数据
     *
     * @param id
     * @return
     */
    public int delete(String id) {
        return update(SQL_DELETE_BY_ID, new Object[] { id });
    }
}

