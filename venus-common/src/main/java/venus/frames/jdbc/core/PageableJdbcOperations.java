package venus.frames.jdbc.core;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**
 * <p>
 * 支持分页操作的JDBC操作,它的查询方法都有参数firstResult和maxResult
 * firstResult指定了记录集的范围
 * </p>
 * 
 * <p>迁移到spring2.0.6版本，修改时间2007-09-06.</p>
 * <br/>
 * 删除内容：<br/>
 * 1) List query(String sql, RowCallbackHandler rch, int firstResult, int maxResult) throws DataAccessException;<br/>
 * 2) List query(String sql, final PreparedStatementSetter pss, final RowCallbackHandler rch, int firstResult, int maxResult) throws DataAccessException;<br/>
 * 3) List query(String sql, final Object[] args, RowCallbackHandler rch, int firstResult, int maxResult) throws DataAccessException;<br/>
 * <br/>
 * 
 * @author sundaiyong
 * @author xiajinxin
 */
public interface PageableJdbcOperations {

	/**
	 * Execute a query given static SQL, mapping each row within the appointed
	 * scope to a Java object via a RowMapper.
	 * <p>Uses a JDBC Statement, not a PreparedStatement. If you want to execute
	 * a static query with a PreparedStatement, use the overloaded query method
	 * with null as PreparedStatementSetter argument.
	 * @param sql SQL query to execute
	 * @param rowMapper object that will map one object per row
	 * @param firstResult the start position of the ResultSet
	 * @param maxResult the max number of records.
	 * @return the result List in case of a ResultReader, or null else
	 * @throws DataAccessException if there is any problem executing the query
	 * @see #query(String, PreparedStatementSetter, RowCallbackHandler)
	 */
	List query(String sql, RowMapper rowMapper, int firstResult, int maxResult) throws DataAccessException;
	
	/**
	 * Query given SQL to create a prepared statement from SQL and a list of
	 * arguments to bind to the query, mapping each row within the appointed
	 * scope to a Java object
	 * via a RowMapper.
	 * @param sql SQL to execute
	 * @param args arguments to bind to the query
	 * (leaving it to the PreparedStatement to guess the respective SQL type)
	 * @param rowMapper object that will map one object per row
	 * @param firstResult the start position of the ResultSet
	 * @param maxResult the max number of records.
	 * @return the result List in case of a ResultReader, or null else
	 * @throws DataAccessException if the query fails
	 */
	List query(String sql, Object[] args, RowMapper rowMapper, int firstResult, int maxResult)
			throws DataAccessException;

}
