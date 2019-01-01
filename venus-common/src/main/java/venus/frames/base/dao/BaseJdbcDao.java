package venus.frames.base.dao;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcAccessor;
import venus.frames.base.IGlobalsKeys;
import venus.frames.jdbc.datasource.ConfDataSource;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * 传统的JDBC操作基类,使用DataSource作为构造参数,不支持申明式事务,
 * 也不支持使用TransactionTemplate来管理事务.
 * 数据库操作中的SQLException异常要使用translateException函数对异常进行转换.
 * 再抛出RuntimeException.
 */
public class BaseJdbcDao extends JdbcAccessor implements InitializingBean , IGlobalsKeys{
	//日志
	protected final ILog logger = LogMgr.getLogger(this);
	
	/**
	 * 无参数的构造函数,供BeanFactory构造使用
	 * 在使用构造的实例之前,必须设置数据源
	 */
	public BaseJdbcDao() {
	}
	
	/**
	 * 使用db.xml文件中的配置的数据源
	 * @param dbUsr
	 */
	public BaseJdbcDao(String dbUsr)
	{
		DataSource ds = new ConfDataSource(dbUsr);
		setDataSource(ds);
		afterPropertiesSet();
	}
	
	/**
	 * 基类,使用DataSource获取数据库连接
	 * @param dataSource
	 */
	public BaseJdbcDao(DataSource dataSource)
	{
		setDataSource(dataSource);
		afterPropertiesSet();
	}
	
	/**
	 * 获取数据库连接,子类必须调用此方法来获取数据库连接,
	 * 不能使用DataSource直接获取连接
	 * @return Connection 数据库连接
	 */
	protected Connection getConnection()
	{
		return DataSourceUtils.getConnection(getDataSource());
	}
	
	/**
	 * 获取数据库连接,子类必须调用此方法来获取数据库连接,
	 * 不能使用DataSource直接获取连接
	 * @param autoCommit 是否自动提交
	 * @return Connection 数据库连接
	 */
	protected Connection getConnection(boolean autoCommit)
	{
		Connection conn = DataSourceUtils.getConnection(getDataSource());
		if (conn != null) {
				try {
					conn.setAutoCommit(autoCommit);
				} catch (SQLException e) {
					throw translateException("executing setAutoCommit [ " + autoCommit + " ]", "setAutoCommit", e);
				}
		}
		return conn;
	}
	
	/**
	 * 关闭数据库连接,必须调用此方法来关闭数据库连接,
	 * 不能直接使用Connection的Close方法
	 * @param conn 数据库连接
	 */
	protected void closeConnection(Connection conn)
	{
		DataSourceUtils.releaseConnection(conn, getDataSource());
	}
	
	/**
	 * 设置语句超时限制,使用DataSource的设置
	 * @param stmt
	 */
	protected void setStatementTimeout(Statement stmt)
	{
		try {
			DataSourceUtils.applyTransactionTimeout(stmt, getDataSource());
		} catch (SQLException e) {
			throw getExceptionTranslator().translate("executing setStatementTimeout", null, e);
		}
	}
	
	/**
	 * 关闭语句
	 * @param stmt
	 */
	protected void closeStatement(Statement stmt)
	{
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException ex) {
				logger.warn("Could not close JDBC Statement", ex);
			}
		}
	}
	
	/**
	 * 关闭结果集
	 * @param rs
	 */
	protected  void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException ex) {
				logger.warn("Could not close JDBC ResultSet", ex);
			}
		}
	}
	
	/**
	 * 提交事务
	 * @param conn
	 */
	protected void commit(Connection conn) {
		if (conn == null)
			return;
		try {
			conn.commit();
		} catch (SQLException e) {
			throw translateException("executing commit","can't commit",e);
		}
	}
	
	/**
	 * 回滚事务
	 * @param conn
	 */
	protected void rollback(Connection conn) {
		if (conn == null)
			return;
		try {
			conn.rollback();
		} catch (SQLException e) {
			throw translateException("executing rollback","can't rollback",e);
		}
	}
	
	/**
	 * 
	 * @param task 执行的异常
	 * @param sql 执行的SQL,可以为空
	 * @param sqlex SQLException
	 * @return DataAccessException 数据存取异常
	 */
	protected DataAccessException translateException(String task, String sql, SQLException sqlex)
	{
		return getExceptionTranslator().translate(task,sql,sqlex);	
	}
	
	
//	/**
//	 * 根据表名征得 oid
//	 * 
//	 * 该方法为辅助的代理方法
//	 * 
//	 * 实际实现即调用 OidMgr 的 requestOID()方法征得 oid
//	 * 
//	 * @param tableName - 要征得 oid 的表名
//	 * @return venus.pub.lang.OID - 所征得的OID对象
//	 * @roseuid 3F9389900356
//	 */
//	public OID generateOid(String tableName) {
//		return OidMgr.requestOID(tableName);
//	}
}
