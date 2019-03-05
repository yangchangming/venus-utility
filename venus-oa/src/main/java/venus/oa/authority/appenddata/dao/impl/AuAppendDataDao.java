package venus.oa.authority.appenddata.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import venus.oa.authority.appenddata.dao.IAuAppendDataDao;
import venus.oa.authority.appenddata.util.IConstantsimplements;
import venus.oa.authority.appenddata.vo.AuAppendVo;
import venus.oa.util.StringHelperTools;
import venus.oa.util.VoHelperTools;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *  2008-9-26
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
@Repository
public class AuAppendDataDao extends BaseTemplateDao implements IConstantsimplements, IAuAppendDataDao {

	/* （非 Javadoc）
	 * @see venus.authority.au.appenddata.dao.IAuAppendDataDao#delete(java.lang.String)
	 */
	public int delete(String id) {
		Object[] obj = { id };
        String sql = DELETE_FOR_APPENDDATA_SQL + " where ID=? ";
        //先删除附加数据
        return update(sql, obj);
    }

	/* （非 Javadoc）
	 * @see venus.authority.au.appenddata.dao.IAuAppendDataDao#insert(venus.authority.au.appenddata.vo.AuAppendVo)
	 */
	public String insert(AuAppendVo vo) {
		OID oid = Helper.requestOID(OID);
        String id = String.valueOf(oid);
        vo.setId(id);
        VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        Object[] myobj = { vo.getId(),vo.getVisitor_id(),vo.getVisitor_code(),vo.getResource_id(),vo.getAuthorize_id(),vo.getAppend_value(),vo.getCreate_date() };
        update(INSERT_SQL, myobj);
       return vo.getId();
	}

	/* （非 Javadoc）
	 * @see venus.authority.au.appenddata.dao.IAuAppendDataDao#queryAppendByVisitorCode(java.lang.String[], java.lang.String, java.lang.String)
	 */
	public List queryAppendByVisitorCode(String[] visiCodes, String resourceId,String appendValue) {
        String strSql = QUERYALL+" WHERE VISITOR_CODE in (" + StringHelperTools.parseToSQLStringComma(visiCodes) + ")";
        if (appendValue != null && appendValue.length()>0) {
        	strSql += " and APPEND_VALUE='" + appendValue + "'";
        }
        if (resourceId != null && resourceId.length()>0) {
        	strSql += " and RESOURCE_ID='" + resourceId + "'";
        }
        RowMapper rowMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int i) throws SQLException {
            	AuAppendVo vo = new AuAppendVo();
                Helper.populate(vo, rs);
                return vo;
            }
        };
        List result = query(strSql, rowMapper);
        return result;
	}

	/* （非 Javadoc）
	 * @see venus.authority.au.appenddata.dao.IAuAppendDataDao#queryAppendByAuthorizeId(java.lang.String)
	 */
	public List queryAppendByAuthorizeId(String authorizeId) {
		return query(QUERYALL+" WHERE AUTHORIZE_ID=? ",new Object[]{authorizeId},new RowMapper(){
			public Object mapRow(ResultSet rs, int i) throws SQLException {
            	AuAppendVo vo = new AuAppendVo();
                Helper.populate(vo, rs);
                return vo;
            }
		});
	}

	/* （非 Javadoc）
	 * @see venus.authority.au.appenddata.dao.IAuAppendDataDao#deleteByAuthorizeId(java.lang.String)
	 */
	public void deleteByAuthorizeId(String id) {
		Object[] obj = { id };
        String sql = DELETE_FOR_APPENDDATA_SQL + " where AUTHORIZE_ID=? ";
        //先删除附加数据
        update(sql, obj);
	}
}

