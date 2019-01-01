package venus.oa.authority.auauthorize.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import venus.oa.authority.auauthorize.dao.IAuAuthorizeDao;
import venus.oa.authority.auauthorize.util.IConstants;
import venus.oa.authority.auauthorize.vo.AuAuthorizeVo;
import venus.oa.authority.auauthorizelog.vo.AuAuthorizeLogVo;
import venus.oa.authority.auvisitor.vo.AuVisitorVo;
import venus.oa.util.GlobalConstants;
import venus.oa.util.StringHelperTools;
import venus.oa.util.VoHelperTools;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 权限DAO
 * 
 * @author ganshuo
 *  
 */
public class AuAuthorizeDao extends BaseTemplateDao implements IAuAuthorizeDao, IConstants {

    /**
     * 根据id删除授权情况（包括附加数据）
     * 
     * @param id
     * @return
     */
    public int delete(String id) {
        Object[] obj = { id };
        String sql = DELETE_FOR_APPENDDATA_SQL + " where AUTHORIZE_ID=? ";
        //先删除附加数据
        update(sql, obj);
        //删除权限记录
        return update(DELETE_BY_ID_SQL, obj);
    }

    /**
     * 根据访问者ID删除授权情况（包括附加数据）
     * 
     * @param id
     * @return
     */
    public int deleteByVisitorId(String visitorId) {
        Object[] obj = { visitorId };
        String sql = DELETE_FOR_APPENDDATA_SQL + " where visitor_id=? ";
        //先删除附加数据
        update(sql, obj);
        sql = DELETE_ALL_SQL + " where visitor_id=? ";
        //删除权限记录
        return update(sql, obj);
    }

    /**
     * 根据资源ID删除授权情况（包括附加数据）
     * 
     * @param id
     * @return
     */
    public int deleteByResourceId(String resourceId) {
        Object[] obj = { resourceId };
        String sql = DELETE_FOR_APPENDDATA_SQL + " where resource_id=? ";
        //先删除附加数据
        update(sql, obj);
        sql = DELETE_ALL_SQL + " where resource_id=? ";
        //删除权限记录
        return update(sql, obj);
    }

    /**
     * 添加记录到权限表
     * 
     * @param vo
     * @return
     */
    public String insert(AuAuthorizeVo vo) {
        OID oid = Helper.requestOID(OID);
        String id = String.valueOf(oid);
        vo.setId(id);
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] myobj = { vo.getId(), vo.getVisitor_id(), vo.getVisitor_code(), vo.getVisitor_type(),
                vo.getResource_id(), vo.getResource_code(), vo.getResource_type(), vo.getAuthorize_status(),
                vo.getAccess_type(), vo.getIs_append(), vo.getCreate_date() , vo.getSystem_id() };
        update(INSERT_SQL, myobj);

        return vo.getId();
    }

    /**
     * 根据ID更新权限表
     * 
     * @param vo
     * @return
     */
    public int update(AuAuthorizeVo vo) {
        Object myobj[] = { vo.getVisitor_id(), vo.getVisitor_code(), vo.getVisitor_type(), vo.getResource_id(),
                vo.getResource_code(), vo.getResource_type(), vo.getAuthorize_status(), vo.getAccess_type(),
                vo.getIs_append(), vo.getId() };
        return update(UPDATE_BY_ID_SQL, myobj);
    }

    /**
     * 
     * 功能: 根据访问者ID和资源类型查询该访问者自身拥有的权限
     * 		如果资源类型为null，则查询全部资源类型的权限
     *
     * @param visitorId 访问者ID
     * @param resType 资源类型
     * @return
     */
    public List queryByVisitorId(String visitorId, String resType) {

        String strSql = QUERY_ALL_SQL + " where VISITOR_ID='" + visitorId + "'";
        if (resType != null && resType.length()>0) {
            strSql += " and RESOURCE_TYPE='" + resType + "'";
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
    
    /**
     * 功能: 根据访问者ID和资源类型查询该访问者自身拥有的权限
     * @param visitorId
     * @return
     */
    public List queryByVisitorId(String visitorId) {
	Object[] obj = { visitorId };
	String strSql = QUERY_AUTHORITY_LIST;
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuAuthorizeLogVo vo = new AuAuthorizeLogVo();
                Helper.populate(vo, rs);
                return vo;
            }
        };
        return query(strSql, obj,rowMapper);
    }
    
    /**
     * 
     * 功能: 根据访问者ID和资源类型查询该访问者自身拥有的历史权限
     * 		如果资源类型为null，则查询全部资源类型的权限
     *
     * @param visitorId 访问者ID
     * @param resType 资源类型
     * @return
     */    
    public List queryHistoryAuByVisitorId(String visitorId, String resType) {
	
	StringBuffer strSql = new StringBuffer();
	strSql.append("SELECT a.ID,a.VISITOR_ID,a.VISITOR_CODE,a.VISITOR_TYPE,a.RESOURCE_ID,a.RESOURCE_CODE,a.RESOURCE_TYPE,a.AUTHORIZE_STATUS,a.ACCESS_TYPE,a.IS_APPEND,a.CREATE_DATE FROM AU_AUTHORIZE a,au_history b ")
	          .append(" WHERE a.VISITOR_ID='").append(visitorId).append( "' AND  a.resource_code = b.source_code AND b.operate_type IN ('")
	  	  .append(GlobalConstants.HISTORY_LOG_ADJUST)
	  	  .append("','").append(GlobalConstants.HISTORY_LOG_DELETE)
	  	  .append("')");
        if (resType != null && resType.length()>0) {
            strSql.append(" AND a.RESOURCE_TYPE='").append(resType).append("'");
        }
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuAuthorizeVo vo = new AuAuthorizeVo();
                Helper.populate(vo, rs);
                return vo;
            }
        };
        List result = query(strSql.toString(), rowMapper);
        return result;	
    }
    
    /**
     * 
     * 功能: 根据访问者Code数组和资源类型查询该访问者自身拥有的权限+在同一团体关系类型内它所继承的权限
     *		如果资源类型为null，则查询全部资源类型的权限
     *
     * @param visiCodes 访问者编号数组
     * @param resType 资源类型
     * @return
     */
    public List queryByVisitorCode(String[] visiCodes, String resType) {

        String strSql = QUERY_ALL_SQL + " where VISITOR_CODE in (" + StringHelperTools.parseToSQLStringComma(visiCodes) + ")";
        if (resType != null && resType.length()>0) {
            strSql += " and RESOURCE_TYPE='" + resType + "'";
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

    /**
     * 
     * 功能: 根据partyId和relationTypeId获取相应的访问者列表，
     * relationTypeId为可选参数，当为null或""时不起作用
     *
     * @param partyId
     * @param relationTypeId （可选）
     * @return
     */
    public List parsePartyIdToVisitor(String partyId, String relationTypeId) {
        String strSql = "select a.* from AU_VISITOR a, AU_PARTYRELATION b where a.ORIGINAL_ID=b.ID and b.PARTYID='" + partyId + "'";
        if (relationTypeId!=null && relationTypeId.length()>0) {
            strSql += " and b.RELATIONTYPE_ID='" + relationTypeId + "'";
        }
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                AuVisitorVo vo = new AuVisitorVo();
                Helper.populate(vo, rs);
                return vo;
            }
        };
        List result = query(strSql, rowMapper);
        return result;
    }
    
    /**
     * 
     * 功能: 根据资源Code数组和访问者类型查询拥有资源权限的访问者
     *	如果访问者类型为null，则查询全部类型的访问者
     *
     * @param resCodes 资源编号数组
     * @param visiType 访问者类型
     * @return
     */
    public List queryByResourceCode(String[] resCodes, String visiType) {

        String strSql = QUERY_ALL_SQL + " where RESOURCE_CODE in (" + StringHelperTools.parseToSQLStringComma(resCodes) + ")";
        if (visiType != null && visiType.length()>0) {
            strSql += " and VISITOR_TYPE='" + visiType + "'";
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
    
    /**
     * 功能: 根据访问者编号数组和团体类型获取相关的（自身及下属）partyid列表；
     * partyTypeId为可选参数，当为null或""时不起作用，将查询全部类型
     * @param visitorCode 访问者编号数组
     * @param partyTypeId 团体类型
     * @return
     */
    public List parseVisitorToPartyId(String visitorCode[], String partyTypeId) {
    	if(visitorCode==null || visitorCode.length==0)
    		return null;
        String strSql = "select distinct PARTYID from AU_PARTYRELATION where (code like'"+visitorCode[0]+"%'";
        for(int i=1; i<visitorCode.length; i++) {
        	strSql += " or code like'"+visitorCode[i]+"%'";
        }
        strSql += ")";
        if (partyTypeId!=null && partyTypeId.length()>0) {
            strSql += " and PARTYTYPE_ID='" + partyTypeId + "'";
        }
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString("PARTYID");
            }
        };
        List result = query(strSql, rowMapper);
        return result;
    }
    /**
     * 功能: 根据访问者编号数组获取相关的（仅自身）partyid列表
     * @param visitorCode 访问者编号数组
     * @return
     */
    public List parseVisitorToPartyId(String visitorCode[]) {
    	if(visitorCode==null || visitorCode.length==0)
    		return null;
        String strSql = "select distinct PARTYID from AU_PARTYRELATION where (code='"+visitorCode[0]+"'";
        for(int i=1; i<visitorCode.length; i++) {
        	strSql += " or code='"+visitorCode[i]+"'";
        }
        strSql += ")";
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString("PARTYID");
            }
        };
        List result = query(strSql, rowMapper);
        return result;
    }
    /**
     * 功能: 根据访问者编号数组和团体类型获取相应的partyrelation表自身及所有下级的code；
     * partyTypeId为可选参数，当为null或""时不起作用，将查询全部类型
     * @param visitorCode 访问者编号数组
     * @param partyTypeId 团体类型
     * @return
     */
    public List parseVisitorToRelCode(String visitorCode[], String partyTypeId) {
    	if(visitorCode==null || visitorCode.length==0)
    		return null;
        String strSql = "select distinct CODE from AU_PARTYRELATION where (code like'"+visitorCode[0]+"%'";
        for(int i=1; i<visitorCode.length; i++) {
        	strSql += " or code like'"+visitorCode[i]+"%'";
        }
        strSql += ")";
        if (partyTypeId!=null && partyTypeId.length()>0) {
            strSql += " and PARTYTYPE_ID='" + partyTypeId + "'";
        }
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString("CODE");
            }
        };
        List result = query(strSql, rowMapper);
        return result;
    }
    /**
     * 功能: 根据访问者编号数组获取相应的partyrelation表自身的code
     * @param visitorCode 访问者编号数组
     * @return
     */
    public List parseVisitorToRelCode(String visitorCode[]) {
    	if(visitorCode==null || visitorCode.length==0)
    		return null;
        String strSql = "select distinct CODE from AU_PARTYRELATION where (code='"+visitorCode[0]+"'";
        for(int i=1; i<visitorCode.length; i++) {
        	strSql += " or code ='"+visitorCode[i]+"'";
        }
        strSql += ")";
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
                return rs.getString("CODE");
            }
        };
        List result = query(strSql, rowMapper);
        return result;
    }

	/* （非 Javadoc）
	 * @see venus.authority.au.auauthorize.dao.IAuAuthorizeDao#find(java.lang.String)
	 */
	public AuAuthorizeVo find(String authorizeId) {
		return (AuAuthorizeVo) queryForObject(QUERY_ALL_SQL + " where id=? ", new Object[] { authorizeId },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
						AuAuthorizeVo vo = new AuAuthorizeVo();
		                Helper.populate(vo, rs);
						return vo;
					}
				});
	}

    /* (non-Javadoc)
     * @see venus.authority.au.auauthorize.dao.IAuAuthorizeDao#queryByVisitorCodeWithOutHistory(java.lang.String[])
     */
    public List queryByVisitorCodeWithOutHistory(String[] visiCodeArray,String resType) {
        String strSql = QUERY_ALL_SQL + ",AU_PARTYRELATION r where AU_AUTHORIZE.RESOURCE_CODE=r.code and VISITOR_CODE in (" + StringHelperTools.parseToSQLStringComma(visiCodeArray) + ") and RESOURCE_TYPE='5' ";
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

