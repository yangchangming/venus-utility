/*
 * 创建日期 2008-7-31
 */
package venus.oa.sysparam.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import venus.oa.sysparam.dao.ISysParamDao;
import venus.oa.sysparam.util.IConstants;
import venus.oa.sysparam.vo.SysParamVo;
import venus.oa.util.VoHelperTools;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 2008-7-31
 * 
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
public class SysParamDao extends BaseTemplateDao implements ISysParamDao, IConstants {

	/*
	 * （非 Javadoc）
	 * 
	 * @see venus.authority.sys.dao.ISysParamDao#addPara(venus.authority.sys.vo.SysParamVo)
	 */
	public OID addPara(SysParamVo vo) {
		OID oid = Helper.requestOID(TABLE_NAME); //获得oid
		long id = oid.longValue();
		vo.setId(String.valueOf(id));
		VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
		Object[] obj = { vo.getId(), vo.getPropertykey(), vo.getValue(),
				vo.getIniTime(), vo.getUpdateTime(), vo.getCreatorId(),
				vo.getCreatorName(), vo.getDescription(),vo.getEnable(), vo.getPropertytype(),vo.getColumn1() };
		updateWithUniformArgType(SQL_INSERT, obj);
		return oid;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see venus.authority.sys.dao.ISysParamDao#delete(java.lang.String)
	 */
	public int delete(String id) {
		return update(SQL_DELETE_BY_ID, new Object[] { id });
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see venus.authority.sys.dao.ISysParamDao#find(java.lang.String)
	 */
	public SysParamVo find(String id) {
		return (SysParamVo) queryForObject(SQL_FIND_BY_ID, new Object[] { id },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
						SysParamVo vo = new SysParamVo();
						Helper.populate(vo, rs);
						return vo;
					}
				});
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see venus.authority.sys.dao.ISysParamDao#update(venus.authority.sys.vo.SysParamVo)
	 */
	public int update(SysParamVo vo) {
		VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
		Object[] obj = { vo.getValue(), 
				vo.getUpdateTime(), vo.getCreatorId(), vo.getCreatorName(),
				vo.getDescription(),vo.getEnable(), vo.getColumn1(), vo.getId() };
		return updateWithUniformArgType(SQL_UPDATE_BY_ID, obj);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see venus.authority.sys.dao.ISysParamDao#queryByCondition(java.lang.String)
	 */
	public List queryByCondition(String queryCondition) {
		return queryByCondition(queryCondition, null);
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see venus.authority.sys.dao.ISysParamDao#queryByCondition(java.lang.String,
	 *           java.lang.String)
	 */
	public List queryByCondition(String queryCondition, String orderStr) {
		String strsql = SQL_QUERY + DEFAULT_QUERY_WHERE_ENABLE;
		if (queryCondition != null && queryCondition.length() > 0
				&& queryCondition.trim().length() > 0) {
			strsql += " AND " + queryCondition; //where后加上查询条件
		}
		if (orderStr == null) {
			strsql += ORDER_BY_SYMBOL + DEFAULT_ORDER_CODE;
		} else {
			strsql += ORDER_BY_SYMBOL + orderStr;
		}
		return query(strsql, new RowMapper() {
			public Object mapRow(ResultSet rs, int i) throws SQLException {
				SysParamVo vo = new SysParamVo();
				Helper.populate(vo, rs);
				return vo;
			}
		});
	}

}

