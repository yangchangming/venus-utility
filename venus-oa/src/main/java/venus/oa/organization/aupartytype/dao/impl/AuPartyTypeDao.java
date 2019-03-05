package venus.oa.organization.aupartytype.dao.impl;

import org.apache.struts.util.LabelValueBean;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import venus.oa.organization.aupartytype.dao.IAuPartyTypeDao;
import venus.oa.organization.aupartytype.util.IConstants;
import venus.oa.organization.aupartytype.vo.AuPartyTypeVo;
import venus.oa.util.VoHelperTools;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.base.exception.BaseDataAccessException;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;
import venus.pub.util.DateUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

/**
 * 团体类型DAO
 * @author wumingqiang
 *
 */
@Repository
public class AuPartyTypeDao extends BaseTemplateDao implements IAuPartyTypeDao, IConstants {

  
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
            strSql = QUERY_SQL;
        else
            strSql = QUERY_SQL + " order by " + orderStr;
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyTypeVo obj = new AuPartyTypeVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(strSql, rowMapper, (no - 1) * size, size);
        return result;
    }

    /**
     * 
     * 功能: 查询所有启用状态的团体类型
     * 当参数no和size小于或等于0时，不翻页
     * 当参数orderStr为null时，不进行排序
     * @param no
     * @param size
     * @param orderStr
     * @return
     */
    public List queryAllEnable(int no, int size, String orderStr) {
        String strSql = null;
        if (orderStr == null)
            strSql = QUERY_SQL + " where enable_status='1' ";
        else
            strSql = QUERY_SQL + " where enable_status='1' order by " + orderStr;
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyTypeVo obj = new AuPartyTypeVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        if(no <= 0 || size <= 0) {
            return query(strSql, rowMapper);
        } else {
            return query(strSql, rowMapper, (no - 1) * size, size);
        }
    }
	/**
     * 获得记录数
     * @return
     */
    public int getRecordCount() {
        String strSql = GETRECORDCOUNT_SQL;
        return queryForInt(strSql);
    }

    
    /**
     * 按条件获得记录数
     * @param queryCondition
     * @return
     */
    public int getRecordCount(Object objVo) {
        String strSql = GETRECORDCOUNT_SQL+" where 1=1 ";
        AuPartyTypeVo obj = (AuPartyTypeVo) objVo;
        if (obj.getName() != null && !obj.getName().equals(""))
            strSql += " and name like'%" + obj.getName() + "%'";
        if (obj.getKeyword() != null && !obj.getKeyword().equals(""))
            strSql += " and keyword ='" + obj.getKeyword() + "'"; 
        return queryForInt(strSql);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public int delete(String id) {
    	//设计上不用实现
		return 0;		
    }

    /**
     *  添加
     * @param rvo
     * @return
     */
    public String insert(Object objVo) {
	    AuPartyTypeVo obj = (AuPartyTypeVo) objVo;
	    OID oid = Helper.requestOID(OID);
        String soid = String.valueOf(oid);
        obj.setId(soid);		
        VoHelperTools.null2Nothing(obj);
        Object[] myobj ={ obj.getId(),
                    obj.getName(),
                    obj.getKeyword(),
					obj.getTable_name(),
					obj.getCode_prefix(),
					"1",
					obj.getCreate_date(),
                    obj.getCreate_date(),
					obj.getModify_date(),
					obj.getRemark()
                   };
         update(INSERT_SQL, myobj);
               
        return obj.getId();
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public Object find(String id) {
        return  queryForObject(QUERY_SQL + " where id=? ",
                new Object[] { id }, new RowMapper() {
                    public Object mapRow(ResultSet rs, int i)
                            throws SQLException {
                        AuPartyTypeVo rvo = new AuPartyTypeVo();
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
    	AuPartyTypeVo obj = (AuPartyTypeVo) objVo;
    	VoHelperTools.null2Nothing(obj);
        Object myobj[] = { 
                    obj.getName(),
                    obj.getKeyword(),
                    obj.getTable_name(),
					obj.getCode_prefix(),
					obj.getModify_date(),
					obj.getRemark(),
					obj.getId()
                     };
         return update(SQL_UPDATE_BY_ID, myobj);        
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
    	AuPartyTypeVo obj = (AuPartyTypeVo) objVo;
        String strSql = QUERY_BYCONDITION1_SQL+" where 1=1 ";
        
        if (obj.getName() != null && !obj.getName().equals(""))
            strSql += " and name like'%" + obj.getName() + "%'";
        if (obj.getKeyword() != null && !obj.getKeyword().equals(""))
            strSql += " and keyword ='" + obj.getKeyword() + "'"; 
        if (orderStr != null)
            strSql += " order by " + orderStr;
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyTypeVo obj = new AuPartyTypeVo();
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
     * @param objVo
     * @return
     */
    public List queryByName(Object objVo) {
    	AuPartyTypeVo obj = (AuPartyTypeVo) objVo;
        String strSql = QUERY_BYCONDITION1_SQL;
        
        if (obj.getName() != null && !obj.getName().equals(""))
            strSql += " where name='" + obj.getName() + "'";
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyTypeVo obj = new AuPartyTypeVo();
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(strSql, rowMapper);
        return result;
    }
    
    /**
     * List 中是LabelValueBean
     * @return
     */
    public List getPartyAll(){
    	RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                LabelValueBean obj = new LabelValueBean("","");
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(QUERY_NAME_SQL, rowMapper);
        return result;
    }
    /**
     * List 中是LabelValueBean
     * @return
     */
    public List getPartyAllByKeyword(String keyword){
    	RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
            	LabelValueBean obj = new LabelValueBean("","");
                Helper.populate(obj, rs);
                return obj;
            }
        };
        List result = query(QUERY_NAME_SQL+ " and keyword='"+keyword+"'", rowMapper);
        return result;
    }
    /**
     * 启用
     * @param id
     * @return
     */
    public int enable(String id) {
    	try {
            Object myobj[] = { 
            		"1",
            		DateUtil.getGBDateFrmString(
            				new Timestamp(System.currentTimeMillis()).toString().substring(0,19)),
					id
                     };
            return update(DELETEMULTI_SQL, myobj);
        } catch (ParseException e) {
            throw new BaseDataAccessException("Exception: " + e);
        }

	}
    /**
     * 禁用
     * @param id
     * @return
     */
	public int disable(String id) {
		try {
            Object myobj[] = { 
            		"0",
            		DateUtil.getGBDateFrmString(
            				new Timestamp(System.currentTimeMillis()).toString().substring(0,19)),
					id
                     };
            return update(DELETEMULTI_SQL, myobj);
        } catch (ParseException e) {
            throw new BaseDataAccessException("Exception: " + e);
        }
	}
	
	/**
     * 根据KeyWord获得ID
     * @param keyword
     * @return
     */
	public String getIdByKeyWord(String keyword){
		AuPartyTypeVo auPartyTypeVo = (AuPartyTypeVo) queryForObject(QUERY_SQL + " where keyword=? ",
                new Object[] { keyword }, new RowMapper() {
                    public Object mapRow(ResultSet rs, int i)
                            throws SQLException {
                        AuPartyTypeVo rvo = new AuPartyTypeVo();
                        Helper.populate(rvo, rs);
                        return rvo;
                    }
                });
		String id = "";
		if (auPartyTypeVo != null) {
			if (auPartyTypeVo.getId() != null) {
				id = auPartyTypeVo.getId();
			}
		}
        return id;
    }
	/**
     * 根据KeyWord获得所有该类型的party列表
     * @return
     */
    public List getPartysByKeyWord(String keyword){
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuPartyTypeVo rvo = new AuPartyTypeVo();
                Helper.populate(rvo, rs);
                return rvo;
            }
        };
        List result = query("select a.id,a.name,a.keyword,a.table_name,a.code_prefix,a.enable_status,a.enable_date,a.create_date,a.modify_date,a.remark from au_partytype a,au_partyrelationtype b where a.enable_status='1' and b.id = '"+keyword+"' and b.keyword=a.keyword", rowMapper);
        return result;
    }
}

