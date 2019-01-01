package venus.frames.jdbc.datasource;

import org.springframework.jdbc.datasource.AbstractDataSource;
import venus.frames.jdbc.core.IPageSqlProvider;
import venus.frames.mainframe.db.conpool.ConnectionHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Stack;

/**
 * @author 孙代勇
 *
 * Venus平台数据源，根据db.xml文件配置的数据库连接信息，
 * 使用主框架提供的Helper类获取连接
 * 使用ConfDataSource（String dbSrc）构造
 */
public class ConfDataSource extends AbstractDataSource
{
	private String dbUsr = null;
	
	//Connection有效性验证sql
	private String testSql = null;

	//计数器
	private static int testCount = 0;

	//记录当前的数据源用户
	private static String dbCurrentUsr = null;

	//数据源队列
	private static Stack stack = null;

	//数据库连接有效标识，针对多数据源切换的并发访问
	private static boolean isNeedNewConn = true;

	//多线程的currentUserHolder，用来输出正确的日志信息
	private  static ThreadLocal currentUserHolder = new ThreadLocal();
	
	private String nullEscapeStr = null;
	
	private IPageSqlProvider pageSqlProvider = null;
	
	/**
	 * Constructor
	 */
	public ConfDataSource() {
	}
	
	/**
	 * 使用db.xml文件中配置的数据连结名，构造数据源
	 */

	/**
	 * Constructor using dbUser
	 * @param dbUsr
     */
	public ConfDataSource(String dbUsr) {
		setDbSrc(dbUsr);
	}
	
	/**
	 * 使用Helper类获取连接
	 */
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		if (dbUsr == null) {
			conn = ConnectionHelper.requestConnection();
			return conn;
		}
		trackDbUser();
		try{
//			logger.debug("Create new JDBC connection ('" +dbCurrentUsr +  "') using ConnectionHelper");
			currentUserHolder.set( dbCurrentUsr );
			conn = ConnectionHelper.requestConnection(dbCurrentUsr);
			if (testSql != null && testSql.length() != 0) {
				boolean isValid = validate(conn);
				if (!isValid) {
					if (testCount > stack.size()) {
						testCount = 0;
						return conn;
					}
					testCount++;
					conn = getNewConnection(conn, stack);
				}
			}
		}catch( SQLException e ){
			e.printStackTrace();
			//由于超过数据库最大连接数限制而无法获取的conn，直接返回，不进行多数据源切换
			if ( e.getMessage().toLowerCase().indexOf("maximum connection") > 0 )
				throw e;
			logger.warn("Get connection('" +currentUserHolder.get() +  "') failed, cause: "+e.getMessage());
			if (testCount > stack.size()){
				testCount = 0;
				return conn;
			}
			testCount++;
			conn = getNewConnection(conn, stack);
		}
		testCount = 0;
		return conn;
	}
	
    
    /**
     * 当dbCurrentUsr为空时，构造一个队列来存储dbUser，同时为dbCurrentUsr赋值
     * 当获取connection出现异常或者connection无效时，
	 * 顺序遍历数据源，当所有数据源无效时，抛出SQLException异常
	 */
	private void trackDbUser() {
		if (dbCurrentUsr == null) {
			String[] users = dbUsr.split( "," );
			
			stack = new Stack();
			for ( int i = users.length-1; i >= 0; i-- ){
				stack.push(users[i].trim());
			}
			if (users.length >= 1) {
				dbCurrentUsr = (String)stack.pop();
				stack.insertElementAt(dbCurrentUsr, 0);
			}
		}
	}

	/**
     * Get new connection by dbusers when current connection is invalid.
	 * @param conn
	 * @param users
	 * @return
	 */
	private synchronized Connection getNewConnection(Connection conn, Stack stack) {

		//并发访问时如果数据库连接已经有效，则直接返回getConnection()
		if ( !isNeedNewConn )
			try {
				isNeedNewConn = true;
				return getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		
		dbCurrentUsr = (String) stack.pop();
		stack.insertElementAt(dbCurrentUsr, 0);
		// logger.debug("Create new JDBC connection ('" +dbCurrentUsr + "')
		// using ConnectionHelper");
		try {
			ConnectionHelper.resetConnection();
			conn = ConnectionHelper.requestConnection(dbCurrentUsr);
			isNeedNewConn = false;
		} catch (SQLException ex) {
			ex.printStackTrace();
			logger.warn("Get new connection('" + dbCurrentUsr
					+ "') failed, cause: " + ex.getMessage());
		}

		return conn;
	}

	/**
	 * Validate the connection.
	 * @param conn
	 * @return boolean.
	 */
    public boolean validate( Connection conn ) {
        // make sure a test SQL is defined
        if (testSql == null || (testSql.length() == 0)) {
            logger.warn("Connection validation requested but testSql not defined");
            return true;
        }        
        
        // execute the test statement
        java.sql.Statement st = null;
        try {
            st = conn.createStatement();
            st.execute(testSql);

            return true;
        } 
        catch (Throwable t) {
            // got an exception while executing the test statement
            // log the problem and return false
            logger.debug("The connection failed the validation test with error: "+t);
            return false;
        } 
        finally {
            if (st != null) {
                try {
                    st.close();
                } catch (Throwable t) {
                    // Ignore
                    return false;
                }
            }
        }
    }

	
	public String getDbUsr() {
		return dbUsr;
	}

	public void setDbUsr(String dbUsr) {
		this.dbUsr = dbUsr;
		//通过spring注值后调用trackDbUser方法处理dbUser的相关逻辑
		trackDbUser();
	}

	/**
	 * 功能和getConnection()相同，username,password不起作用
	 */
	public Connection getConnection(String username, String password) throws SQLException {
		return getConnection();
	}

	public String getDbSrc() {
		return dbUsr;
	}

	public void setDbSrc(String string) {
		dbUsr = string;
	}

	/**
	 * @return 返回 nullEscapeStr。
	 */
	public String getNullEscapeStr() {
		return nullEscapeStr;
	}

	/**
	 * @param nullEscapeStr 要设置的 nullEscapeStr。
	 */
	public void setNullEscapeStr(String nullEscapeStr) {
		this.nullEscapeStr = nullEscapeStr;
	}

	/**
	 * @return 返回 pageSqlProvider。
	 */
	public IPageSqlProvider getPageSqlProvider() {
		return pageSqlProvider;
	}

	/**
	 * @param pageSqlProvider 要设置的 pageSqlProvider。
	 */
	public void setPageSqlProvider(IPageSqlProvider pageSqlProvider) {
		this.pageSqlProvider = pageSqlProvider;
	}

	/**
	 * @return 返回 dbCurrentUsr。
	 */
	public static String getCurrentUsr() {
		return dbCurrentUsr;
	}

	/**
	 * @return 返回 testSql。
	 */
	public String getTestSql() {
		return testSql;
	}

	/**
	 * @param testSql 要设置的 testSql。
	 */
	public void setTestSql(String testSql) {
		this.testSql = testSql;
	}
}
