package venus.oa.service.history.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import venus.oa.service.history.dao.IHistoryLogDao;
import venus.oa.service.history.util.IContants;
import venus.oa.service.history.vo.HistoryLogVo;
import venus.oa.util.SqlBuilder;
import venus.oa.util.VoHelperTools;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.base.exception.BaseDataAccessException;
import venus.frames.mainframe.util.Helper;
import venus.pub.lang.OID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class HistoryLogDao extends BaseTemplateDao implements IContants,IHistoryLogDao {
	private LobHandler lobHandler = null;  

	/**
	 * 定义用于处理Oracle大字段的lobHandler(需要先在applicationContext.xml中加入lobHandler的定义)
	 * @param lobHandler
	 */
	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}
	
	/**
	 * 增加历史日志
	 * @param vo
	 * @return
	 */
	public OID insert(final HistoryLogVo vo) {
		String strSql = INSERT_LOG_SQL;
		OID oid = Helper.requestOID(OID);
		vo.setId(oid.toString());
		VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
        try {
	        update(strSql,new AbstractLobCreatingPreparedStatementCallback( this.lobHandler){
	        	protected void setValues(PreparedStatement ps,LobCreator lobCreator) throws SQLException {
	        		ps.setString(1,vo.getId());
	        		ps.setTimestamp(2,vo.getOperate_date());
	        		ps.setString(3,vo.getOperate_id());
	        		ps.setString(4,vo.getOperate_name());
	        		ps.setString(5,vo.getOperate_type());
	        		ps.setString(6,vo.getSource_id());
	        		ps.setString(7,vo.getSource_partyid());
	        		ps.setString(8,vo.getSource_code());
	        		ps.setString(9,vo.getSource_name());
	        		ps.setString(10,vo.getSource_orgtree());
	        		lobCreator.setClobAsString(ps,11,vo.getSource_detail());
	        		ps.setString(12,vo.getSource_typeid());
	        		ps.setString(13, vo.getDest_id());
	        		ps.setString(14, vo.getDest_code());
	        		ps.setString(15, vo.getDest_name());
	        		ps.setString(16, vo.getDest_orgtree());
	        	}
	        });
        } catch (BaseDataAccessException e) {
        	logger.error(e.getMessage());
            throw new BaseDataAccessException("BaseDataAccessException: " + e);
        }		

		return oid;
	}

	/**
	 * 根据条件查询一条历史日志的明细
	 * @param id
	 * @return
	 */
	public HistoryLogVo find(String id) {
		String strSql = QUERY_LOG_SQL;
		strSql += " WHERE a.id = '" + id + "'";
		return (HistoryLogVo) queryForObject(strSql, new RowMapper() {
			public Object mapRow(ResultSet rs, int i) throws SQLException {
				HistoryLogVo vo = new HistoryLogVo();
				VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
				Helper.populate(vo, rs);
				return vo;
			}
		});
	}
	
	/**
	 * 根据条件查询历史资源的详细信息
	 * @param id
	 * @return
	 */
	public HistoryLogVo findDetail(String id) {
		String strSql = QUERY_LOG_DETAIL;
		strSql += " WHERE id = '" + id + "'";
		return (HistoryLogVo)queryForObject(strSql,new RowMapper() {
			public Object mapRow(ResultSet rs, int i) throws SQLException {
				HistoryLogVo vo = new HistoryLogVo();
				VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
				vo.setId(rs.getString("id"));
				vo.setOperate_date(rs.getTimestamp("operate_date"));
				vo.setOperate_name(rs.getString("operate_name"));
				vo.setSource_id(rs.getString("source_id"));
				vo.setSource_name(rs.getString("source_name"));
				vo.setSource_partyid(rs.getString("source_partyid"));
				vo.setSource_typeid(rs.getString("source_typeid"));
				vo.setSource_detail(lobHandler.getClobAsString(rs,8));
				vo.setSource_orgtree(rs.getString("source_orgtree"));
				vo.setDest_orgtree(rs.getString("dest_orgtree"));
				return vo;
			}
		});
	}

	/**
	 * 统计记录条数
	 * @return
	 */
	public int getRecordCount(String queryCondition) {
		String strSql = QUERY_LOG_COUNT_SQL;
		if (queryCondition != null && queryCondition.length() > 0) {
			strSql += QUERY_AND_CONDITON + queryCondition;
		}
		return queryForInt(strSql);
	}

	/**
	 * 根据条件分页查询历史日志列表
	 * @param no
	 * @param size
	 * @param queryCondition
	 * @return
	 */
	public List queryByCondition(int no, int size, String queryCondition,String orderStr) {
		String strSql = QUERY_LOG_SQL + QUERY_DEFAULT_CONDITON;
		if (queryCondition != null && queryCondition.length() > 0) {
			strSql += QUERY_AND_CONDITON + queryCondition;
		}
		if (orderStr != null && orderStr.length() > 0) {
            strSql += QUERY_DEFAULT_ORDERBY + orderStr;
        }
		RowMapper rowMapper = new RowMapper() {
			public Object mapRow(ResultSet rs, int i) throws SQLException {
				HistoryLogVo vo = new HistoryLogVo();
				VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
				Helper.populate(vo, rs);
				return vo;
			}
		};
		List result = -1==no?query(strSql, rowMapper):query(strSql, rowMapper, (no - 1) * size, size);
		return result;
	}

	/**
	 * 根据条件分页查询历史日志列表条数
	 * @param sql
	 * @return
	 */
	public int getRecordCount(SqlBuilder sql) {
		return queryForInt(QUERY_LOG_COUNT_SQL + QUERY_DEFAULT_CONDITON + QUERY_AND_CONDITON + sql.bulidSql(),
				sql.getData().toArray());
	}

	/**
	 * 根据条件分页查询历史日志列表
	 * @param sql
	 * @return
	 */
	public List queryByCondition(SqlBuilder sql) {
		String strSql = QUERY_LOG_SQL+QUERY_DEFAULT_CONDITON + QUERY_AND_CONDITON + sql.bulidSql();
		strSql += QUERY_DEFAULT_ORDERBY + " operate_date DESC ";
		return query(strSql,sql.getData().toArray(),new RowMapper() {
				public Object mapRow(ResultSet rs, int i) throws SQLException {
					HistoryLogVo vo = new HistoryLogVo();
					VoHelperTools.null2Nothing(vo); //把vo中null值替换为""
					Helper.populate(vo, rs);
					return vo;
				}
			},sql.getCountBegin(), sql.getCountEnd()
		);
	}

}

