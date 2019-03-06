package venus.frames.jdbc.core;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import venus.frames.jdbc.datasource.ConfDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 派生于JdbcTemplate类,它的查询方法都有参数firstResult和maxResult
 * firstResult指定了记录集的起始位置,maxResult指定了最大记录数
 * 对查询到结果集,使用ResultSet的absolute(int firstResult)方法定位到
 * 起始位置,然后循环取maxResult个记录.
 * </p>
 *
 * <p>迁移到spring2.0.6版本，修改时间2007-09-06.</p>
 * <br/>
 * 删除内容：<br/>
 * 1) 内部类RowCallbackHandlerResultSetExtractor; <br/>
 * 2) List query(String sql, RowCallbackHandler rch, int firstResult, int maxResult) throws DataAccessException;<br/>
 * 3) List query(String sql, final PreparedStatementSetter pss, final RowCallbackHandler rch, int firstResult, int maxResult) throws DataAccessException;<br/>
 * 4) List query(String sql, final Object[] args, RowCallbackHandler rch, int firstResult, int maxResult) throws DataAccessException;<br/>
 * 
 * 修改的内容：<br/>
 * 1) 内部类PageableRowCallbackHandlerResultSetExtractor的变量RowCallbackHandler修改为RowMapper，并调整extractData方法实现; <br/>
 * 2) DataSourceUtils.closeConnectionIfNecessary(conn, getDataSource()); 修改为 DataSourceUtils.releaseConnection(conn, getDataSource());
 * 3) JdbcUtils.countParameterPlaceholders 修改为 UdpJdbcUtils.countParameterPlaceholders <br/>
 * <br/>
 *
 * @author Sundaiyong
 * @author xiajinxin
 */
public class PageableJdbcTemplate extends JdbcTemplate implements PageableJdbcOperations {

	private String nullEscapeStr = null;
	
	/**
	 * Construct a new PageableJdbcTemplate for bean usage.
	 * Note: The DataSource has to be set before using the instance.
	 * This constructor can be used to prepare a PageableJdbcTemplate via a BeanFactory,
	 * typically setting the DataSource via setDataSource.
	 * @see #setDataSource
	 */
	public PageableJdbcTemplate() {
	}

	/**
	 * Construct a new JdbcTemplate, given a DataSource to obtain connections from.
	 * Note: This will trigger eager initialization of the exception translator.
	 *
	 * @param dataSource JDBC DataSource to obtain connections from
	 */
	public PageableJdbcTemplate(DataSource dataSource) {
		super(dataSource);
//		if ((dataSource != null) && (dataSource instanceof ConfDataSource)) {
//			String nullEscapeStr = ((ConfDataSource) dataSource).getNullEscapeStr();
//		}
	}


	public void setDataSource(DataSource dataSource) {		
//		if ((dataSource != null) && (dataSource instanceof ConfDataSource)) {
//            String nullEscapeStr = ((ConfDataSource) dataSource).getNullEscapeStr();
//        }
		super.setDataSource(dataSource);
	}
	
	/**
	 * Not invoke getNextWarning method
	 * modify by chjq
	 * 
	 * Throw an SQLWarningException if we're not ignoring warnings,
	 * else log the warnings (at debug level).
	 * @param warning the warnings object from the current statement.
	 * May be <code>null</code>, in which case this method does nothing.
	 * @throws SQLWarningException if not ignoring warnings
	 * @see SQLWarningException
	 */
	protected void handleWarnings(SQLWarning warning) throws SQLWarningException {
		if (warning != null) {
			if (isIgnoreWarnings()) {
				if (logger.isDebugEnabled()) {
					logger.debug("SQLWarning ignored: SQL state '" + warning.getSQLState() + "', error code '" +
							warning.getErrorCode() + "', message [" + warning.getMessage() + "]");
					
//					SQLWarning warningToLog = warning;
//					while (warningToLog != null) {
//						logger.debug("SQLWarning ignored: SQL state '" + warningToLog.getSQLState() + "', error code '" +
//								warningToLog.getErrorCode() + "', message [" + warningToLog.getMessage() + "]");
//						warningToLog = warningToLog.getNextWarning();
//					}
				}
			}
			else {
				throw new SQLWarningException("Warning not ignored", warning);
			}
		}
	}

	public Object pagequery(final String sql, final ResultSetExtractor rse) throws DataAccessException {
		if (sql == null) {
			throw new InvalidDataAccessApiUsageException("SQL must not be null");
		}
		if (UdpJdbcUtils.countParameterPlaceholders(sql, '?', "'\"") > 0) {
			throw new InvalidDataAccessApiUsageException(
					"Cannot execute [" + sql + "] as a static query: it contains bind variables");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Executing SQL query [" + sql + "]");
		}
		class QueryStatementCallback implements StatementCallback, SqlProvider {
			public Object doInStatement(Statement stmt) throws SQLException {
				ResultSet rs = null;
				try {
					if (getFetchSize() > 0)
						stmt.setFetchSize(getFetchSize());
					rs = stmt.executeQuery(sql);
					ResultSet rsToUse = rs;
					if (getNativeJdbcExtractor() != null) {
						rsToUse = getNativeJdbcExtractor().getNativeResultSet(rs);
					}
					return rse.extractData(rsToUse);
				}
				finally {
					JdbcUtils.closeResultSet(rs);
				}
			}
			public String getSql() {
				return sql;
			}
		}
		return pageexecute(new QueryStatementCallback());
	}
	
	public Object pageexecute(final StatementCallback action) {
		Connection con = DataSourceUtils.getConnection(getDataSource());
		Statement stmt = null;
		try {
			Connection conToUse = con;
			if (getNativeJdbcExtractor() != null &&
					getNativeJdbcExtractor().isNativeConnectionNecessaryForNativeStatements()) {
				conToUse = getNativeJdbcExtractor().getNativeConnection(con);
			}
			stmt = conToUse.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
	          		ResultSet.CONCUR_READ_ONLY);
			DataSourceUtils.applyTransactionTimeout(stmt, getDataSource());
			Statement stmtToUse = stmt;
			if (getNativeJdbcExtractor() != null) {
				stmtToUse = getNativeJdbcExtractor().getNativeStatement(stmt);
			}
			Object result = action.doInStatement(stmtToUse);
			SQLWarning warning = stmt.getWarnings();
			throwExceptionOnWarningIfNotIgnoringWarnings(warning);
			return result;
		}
		catch (SQLException ex) {
			throw getExceptionTranslator().translate("executing StatementCallback", getSql(action), ex);
		}
		finally {
			JdbcUtils.closeStatement(stmt);
			DataSourceUtils.releaseConnection(con, getDataSource());
		}
	}
	
	/**
	 * Determine SQL from potential provider object.
	 * @param sqlProvider object that's potentially a SqlProvider
	 * @return the SQL string, or null
	 * @see SqlProvider
	 */
	private String getSql(Object sqlProvider) {
		if (sqlProvider instanceof SqlProvider) {
			return ((SqlProvider) sqlProvider).getSql();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Throw an SQLWarningException if we're not ignoring warnings.
	 * @param warning warning from current statement. May be null,
	 * in which case this method does nothing.
	 */
	private void throwExceptionOnWarningIfNotIgnoringWarnings(SQLWarning warning) throws SQLWarningException {
		if (warning != null) {
			if (isIgnoreWarnings()) {
				logger.warn("SQLWarning ignored: " + warning);
			}
			else {
				throw new SQLWarningException("Warning not ignored", warning);
			}
		}
	}
	
	public Object pagequery(final String sql, final PreparedStatementSetter pss,
			final ResultSetExtractor rse) throws DataAccessException {
		if (sql == null) {
			throw new InvalidDataAccessApiUsageException("SQL may not be null");
		}
		return pagequery(new PagePreparedStatementCreator(sql), pss, rse);
	}
	
	/**
	 * Query using a prepared statement, allowing for a PreparedStatementCreator
	 * and a PreparedStatementSetter. Most other query methods use this method,
	 * but application code will always work with either a creator or a setter.
	 * @param psc Callback handler that can create a PreparedStatement given a
	 * Connection
	 * @param pss object that knows how to set values on the prepared statement.
	 * If this is null, the SQL will be assumed to contain no bind parameters.
	 * @param rse object that will extract results.
	 * @return an arbitrary result object, as returned by the ResultSetExtractor
	 * @throws DataAccessException if there is any problem
	 */
	protected Object pagequery(
			PreparedStatementCreator psc, final PreparedStatementSetter pss, final ResultSetExtractor rse)
			throws DataAccessException {
		if (logger.isDebugEnabled()) {
			String sql = getSql(psc);
			logger.debug("Executing SQL query" + (sql != null ? " [" + sql  + "]" : ""));
		}
		return pageexecute(psc, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException {
				ResultSet rs = null;
				try {
					if (pss != null) {
						pss.setValues(ps);
					}
					if (getFetchSize() > 0) {
						ps.setFetchSize(getFetchSize());
					}
					rs = ps.executeQuery();
					ResultSet rsToUse = rs;
					if (getNativeJdbcExtractor() != null) {
						rsToUse = getNativeJdbcExtractor().getNativeResultSet(rs);
					}
					return rse.extractData(rsToUse);
				}
				finally {
					JdbcUtils.closeResultSet(rs);
					if (pss instanceof ParameterDisposer) {
						((ParameterDisposer) pss).cleanupParameters();
					}
				}
			}
		});
	}
	
	//-------------------------------------------------------------------------
	// Methods dealing with prepared statements
	//-------------------------------------------------------------------------

	public Object pageexecute(PreparedStatementCreator psc, PreparedStatementCallback action) {
		Connection con = DataSourceUtils.getConnection(getDataSource());
		PreparedStatement ps = null;
		try {
			Connection conToUse = con;
			if (getNativeJdbcExtractor() != null &&
					getNativeJdbcExtractor().isNativeConnectionNecessaryForNativePreparedStatements()) {
				conToUse = getNativeJdbcExtractor().getNativeConnection(con);
			}
			ps = psc.createPreparedStatement(conToUse);
			DataSourceUtils.applyTransactionTimeout(ps, getDataSource());
			PreparedStatement psToUse = ps;
			if (getNativeJdbcExtractor() != null) {
				psToUse = getNativeJdbcExtractor().getNativePreparedStatement(ps);
			}
			Object result = action.doInPreparedStatement(psToUse);
			SQLWarning warning = ps.getWarnings();
			throwExceptionOnWarningIfNotIgnoringWarnings(warning);
			return result;
		}
		catch (SQLException ex) {
			throw getExceptionTranslator().translate("executing PreparedStatementCallback [" + psc + "]",
																							 getSql(psc), ex);
		}
		finally {
			if (psc instanceof ParameterDisposer) {
				((ParameterDisposer) psc).cleanupParameters();
			}
			JdbcUtils.closeStatement(ps);
			DataSourceUtils.releaseConnection(con, getDataSource());
		}
	}
	
	/**
	 * Page adapter for PreparedStatementCreator, allowing to use a plain SQL statement.
	 */
	private static class PagePreparedStatementCreator implements PreparedStatementCreator, SqlProvider {

		private final String sql;

		public PagePreparedStatementCreator(String sql) {
			this.sql = sql;
		}

		public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
			return con.prepareStatement(this.sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		}

		public String getSql() {
			return sql;
		}
	}
	
	public List query(String sql, RowMapper rch, int firstResult, int maxResult) throws DataAccessException {
		DataSource tmpds = this.getDataSource();
		if( (tmpds!=null) && (tmpds instanceof ConfDataSource) ){
			IPageSqlProvider psp = ((ConfDataSource)tmpds).getPageSqlProvider();
			if( psp!=null ){
				return (List) pagequery(psp.getSql(sql,firstResult, maxResult), new RowMapperResultSetExtractor(rch));
			}
		}
		return (List) pagequery(sql, new PageableRowCallbackHandlerResultSetExtractor(rch, firstResult, maxResult));
	}

	public List query(String sql, final PreparedStatementSetter pss, final RowMapper rch, int firstResult, int maxResult)
			throws DataAccessException {
		DataSource tmpds = this.getDataSource();
		if( (tmpds!=null) && (tmpds instanceof ConfDataSource) ){
			IPageSqlProvider psp = ((ConfDataSource)tmpds).getPageSqlProvider();
			if( psp!=null ){
				return (List) pagequery(psp.getSql(sql,firstResult, maxResult), pss, new RowMapperResultSetExtractor(rch));			
			}
		}
		return (List) pagequery(sql, pss, new PageableRowCallbackHandlerResultSetExtractor(rch, firstResult, maxResult));
	}

	public List query(String sql, Object[] args, RowMapper rowMapper, int firstResult, int maxResult)
			throws DataAccessException {
		return query(sql, (PreparedStatementSetter) (new ArgPreparedStatementSetter(args,this.nullEscapeStr)), rowMapper, firstResult, maxResult);
	}

	public List query(String sql, Object[] args,int[] argTypes, RowMapper rowMapper, int firstResult, int maxResult)
			throws DataAccessException {
		return query(sql, ((PreparedStatementSetter) (new ArgTypePreparedStatementSetter(args, argTypes))), rowMapper, firstResult, maxResult);
	}


	/**
	 * spring3*版本，JdbcTemplate中存在queryForLong方法，而spring4*中不存在，所以自定义
	 *
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
     */
	public long queryForLong(String sql, Object... args) throws DataAccessException {
		Number number = queryForObject(sql, args, Long.class);
		return (number != null ? number.longValue() : 0);
	}

	/**
	 * spring3*版本，JdbcTemplate中存在queryForInt方法，而spring4*中不存在，所以自定义
	 *
	 * @param sql
	 * @param args
	 * @return
	 * @throws DataAccessException
     */
	public int queryForInt(String sql, Object... args) throws DataAccessException {
		Number number = queryForObject(sql, args, Integer.class);
		return (number != null ? number.intValue() : 0);
	}





	/**
	 * Simple adapter for PreparedStatementSetter that applies
	 * a given array of arguments.
	 */
	private static class ArgPreparedStatementSetter implements PreparedStatementSetter, ParameterDisposer {

		private final Object[] args;
		
		private final String replaceStr;

		public ArgPreparedStatementSetter(Object[] args,String nullEscapeStr) {
			this.args = args;
			this.replaceStr = nullEscapeStr;
		}

		public void setValues(PreparedStatement ps) throws SQLException {
			if (this.args != null) {
				for (int i = 0; i < this.args.length; i++) {
					if (this.args[i] != null)
		            {
						StatementCreatorUtils.setParameterValue(ps, i + 1, SqlTypeValue.TYPE_UNKNOWN, null, this.args[i]);
		            }else{
		            	
		            	if (replaceStr != null)
			            {

	                        StatementCreatorUtils.setParameterValue(ps, i + 1, SqlTypeValue.TYPE_UNKNOWN, null, replaceStr);

			            }
			            else
			            {
			            	StatementCreatorUtils.setParameterValue(ps, i + 1, SqlTypeValue.TYPE_UNKNOWN, null, this.args[i]);
			            }
		            
		            }
				}
			}
		}

		public void cleanupParameters() {
			StatementCreatorUtils.cleanupParameters(this.args);
		}
	}

	/**
	 * Simple adapter for PreparedStatementSetter that applies
	 * given arrays of arguments and JDBC argument types.
	 */
	private static class ArgTypePreparedStatementSetter implements PreparedStatementSetter, ParameterDisposer {

		private final Object[] args;

		private final int[] argTypes;

		public ArgTypePreparedStatementSetter(Object[] args, int[] argTypes) {
			if ((args != null && argTypes == null) || (args == null && argTypes != null) ||
					(args != null && args.length != argTypes.length)) {
				throw new InvalidDataAccessApiUsageException("args and argTypes parameters must match");
			}
			this.args = args;
			this.argTypes = argTypes;
		}

		public void setValues(PreparedStatement ps) throws SQLException {
			if (this.args != null) {
				for (int i = 0; i < this.args.length; i++) {
					StatementCreatorUtils.setParameterValue(ps, i + 1, this.argTypes[i], null, this.args[i]);
				}
			}
		}

		public void cleanupParameters() {
			StatementCreatorUtils.cleanupParameters(this.args);
		}
	}
	
	/**
	 * Adapter to enable use of a RowCallbackHandler inside a ResultSetExtractor,
	 * the ResultSetExtractor extracts result in a scope.
	 * <p>Uses a regular ResultSet, so we have to be careful when using it:
	 * We don't use it for navigating since this could lead to unpredictable consequences.
	 */
	private static class PageableRowCallbackHandlerResultSetExtractor implements ResultSetExtractor {

		private final RowMapper rch;
		
		private final int firstResult;
		
		private final int maxResult;

		public PageableRowCallbackHandlerResultSetExtractor(RowMapper rch, int firstResult, int maxResult) {
			this.rch = rch;

			this.firstResult = firstResult + 1;
			this.maxResult = maxResult;
		}

		public Object extractData(ResultSet rs) throws SQLException {
			boolean inScope = rs.absolute(firstResult);
			rs.setFetchSize(maxResult);

			List results = new ArrayList();
			int resultProcessed = 0;			
			while (inScope && (resultProcessed < maxResult)) { //第一条记录位置未超过结果集范围
				results.add(rch.mapRow(rs, resultProcessed));
				resultProcessed++;
				inScope = rs.next();
			}
			
			return results;
		}
	}
}
