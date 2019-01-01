package venus.frames.mainframe.db.conpool.provider;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang.StringUtils;
import venus.frames.mainframe.db.conpool.IConProvider;
import venus.frames.mainframe.util.IConfReader;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: ethan
 * Date: 13-8-23
 * Time: 下午11:36
 * To change this template use File | Settings | File Templates.
 */
public class DruidProvider implements IConProvider {

    private DruidDataSource druidDataSource;

    private String conUrl;
    private String username;
    private String drvName;
    private String filters;
    private String password;

    private long maxWait;
    private int  maxActive;
    private int  minActive;
    private int  retryCount;
    private int  initialSize;
    private int  timeBetweenEvictionRunsMillis;
    private int  maxPoolPreparedStatementPerConnectionSize;

    private boolean removeAbandoned;
    private boolean logAbandoned;
    private boolean poolPreparedStatements;
    private boolean testOnReturn;
    private boolean testOnBorrow;
    private boolean testWhileIdle;

    /**
     * Get the datasource connection for application system.
     *
     * @param icr
     * @return
     * @throws SQLException
     */
    public Connection requestConnection(IConfReader icr) throws SQLException {

        if (druidDataSource == null) {
            druidDataSource = new DruidDataSource();
            drvName = icr.readStringAttribute("drvName");
            username = icr.readStringAttribute("usrName");
            password = icr.readStringAttribute("pwd");
            conUrl = icr.readStringAttribute("conUrl");
            filters = icr.readStringAttribute("filters");

            maxActive = icr.readIntAttribute("maxActive");
            maxWait = icr.readIntAttribute("maxWait");
            minActive = icr.readIntAttribute("minActive");
            retryCount = icr.readIntAttribute("retryCount");
            initialSize = icr.readIntAttribute("initialSize");
            timeBetweenEvictionRunsMillis = icr.readIntAttribute("timeBetweenEvictionRunsMillis");
            maxPoolPreparedStatementPerConnectionSize = icr.readIntAttribute("maxPoolPreparedStatementPerConnectionSize");

            removeAbandoned = icr.readBooleanAttribute("removeAbandoned");
            logAbandoned = icr.readBooleanAttribute("logAbandoned");
            poolPreparedStatements = icr.readBooleanAttribute("poolPreparedStatements");
            testOnReturn = icr.readBooleanAttribute("testOnReturn");
            testOnBorrow = icr.readBooleanAttribute("testOnBorrow");
            testWhileIdle = icr.readBooleanAttribute("testWhileIdle");

            druidDataSource.setUsername(username);
            druidDataSource.setDriverClassName(drvName);
            druidDataSource.setPassword(password);
            druidDataSource.setUrl(conUrl);
            if (maxWait > 0) {
                druidDataSource.setMaxWait(maxWait);
            }
            if (maxActive > 0) {
                druidDataSource.setMaxActive(maxActive);
            }
            if (initialSize > 0) {
                druidDataSource.setInitialSize(initialSize);
            }
            if (minActive > 0) {
                druidDataSource.setMinIdle(minActive);
            }
            if (retryCount > 0) {
                druidDataSource.setConnectionErrorRetryAttempts(retryCount);
            }
            if (timeBetweenEvictionRunsMillis > 0) {
                druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            }
            if (maxPoolPreparedStatementPerConnectionSize > 0) {
                druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
            }
            if(StringUtils.isNotEmpty(filters)) {
                druidDataSource.setFilters(filters);
            }
            druidDataSource.setRemoveAbandoned(removeAbandoned);
            druidDataSource.setLogAbandoned(logAbandoned);
            druidDataSource.setPoolPreparedStatements(poolPreparedStatements);
            druidDataSource.setTestOnBorrow(testOnBorrow);
            druidDataSource.setTestOnReturn(testOnReturn);
            druidDataSource.setTestWhileIdle(testWhileIdle);
        }
        return druidDataSource.getConnection();
    }

    /**
     * reset the connection when connection is invalid.
     */
    public void reset() {
    }
}
