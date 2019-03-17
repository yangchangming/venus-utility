/*
 *  Copyright 2015-2018 DataVens, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package venus.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import venus.VenusHelper;
import venus.core.ExtensionLoader;
import venus.datasource.GenericDataSource;
import venus.datasource.PaginationProvider;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>  </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-03-11 23:04
 */
public class PaginationTemplateDao extends JdbcDaoSupport {

    /**
     * spring3*版本，JdbcTemplate中存在queryForInt方法，而spring4*中不存在，所以自定义
     *
     * @param sql
     * @param args
     * @return
     */
    public int queryForInt(String sql, Object[] args) {
//        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();
        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        Number number = getJdbcTemplate().queryForObject(sql, checkArgs(args), Integer.class);
        return (number != null ? number.intValue() : 0);
//        logEndSQL(sql, args, time);
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对给定范围的记录，返回一个Java对象列表
     * 使用JDBC PreparedStatement执行动态查询. 如果只返回一个对象，请使用queryForObject方法.
     *
     * @param sql
     * @param args
     * @param mapper
     * @return 对象集合
     */
    public List query(String sql, Object[] args, final RowMapper mapper, int firstResult, int maxResult) {
//        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();
        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        List result = query(sql, (PreparedStatementSetter) (new ArgPreparedStatementSetter(args,null)), mapper, firstResult, maxResult);
//        logEndSQL(sql, args, time);
        return result;
    }


    public List query(String sql, final PreparedStatementSetter pss, final RowMapper rch, int firstResult, int maxResult) throws DataAccessException {
        DataSource tmpds = this.getDataSource();
        PaginationProvider paginationProvider = ExtensionLoader.getExtensionLoader(PaginationProvider.class).loadUniqueExtensionInstance();
        if (paginationProvider!=null){
            return (List) pageQuery(paginationProvider.getSql(sql,firstResult, maxResult), pss, new RowMapperResultSetExtractor(rch));
        }
        return (List) pageQuery(sql, pss, new PageableRowCallbackHandlerResultSetExtractor(rch, firstResult, maxResult));
    }


    public Object pageQuery(final String sql, final PreparedStatementSetter pss, final ResultSetExtractor rse) throws DataAccessException {
        if (sql == null) {
            throw new InvalidDataAccessApiUsageException("SQL may not be null");
        }
        return pageQuery(new PagePreparedStatementCreator(sql), pss, rse);
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
    protected Object pageQuery(
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
                    if (getJdbcTemplate().getFetchSize() > 0 ) {
                        ps.setFetchSize(getJdbcTemplate().getFetchSize());
                    }
                    rs = ps.executeQuery();
                    ResultSet rsToUse = rs;
                    if (getJdbcTemplate().getNativeJdbcExtractor() != null) {
                        rsToUse = getJdbcTemplate().getNativeJdbcExtractor().getNativeResultSet(rs);
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
            if (getJdbcTemplate().getNativeJdbcExtractor() != null && getJdbcTemplate().getNativeJdbcExtractor().isNativeConnectionNecessaryForNativePreparedStatements()) {
                conToUse = getJdbcTemplate().getNativeJdbcExtractor().getNativeConnection(con);
            }
            ps = psc.createPreparedStatement(conToUse);
            DataSourceUtils.applyTransactionTimeout(ps, getDataSource());
            PreparedStatement psToUse = ps;
            if (getJdbcTemplate().getNativeJdbcExtractor() != null) {
                psToUse = getJdbcTemplate().getNativeJdbcExtractor().getNativePreparedStatement(ps);
            }
            Object result = action.doInPreparedStatement(psToUse);
            SQLWarning warning = ps.getWarnings();
            throwExceptionOnWarningIfNotIgnoringWarnings(warning);
            return result;
        }
        catch (SQLException ex) {
            throw getExceptionTranslator().translate("executing PreparedStatementCallback [" + psc + "]", getSql(psc), ex);
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
            if (getJdbcTemplate().isIgnoreWarnings()) {
                logger.warn("SQLWarning ignored: " + warning);
            }
            else {
                throw new SQLWarningException("Warning not ignored", warning);
            }
        }
    }


    /**
     * check args of sql params
     *
     * @param args
     * @return
     */
    protected Object[] checkArgs(Object[] args) {
        Object ds = this.getDataSource();
        if ((ds == null) || !(ds instanceof GenericDataSource)) {
            return args;
        } else {
//            String replaceStr = ((ConfDataSource) ds).getNullEscapeStr();
            String replaceStr = null;
            if (replaceStr != null) {
                if (args == null || args.length < 1) {
                    Object[] re = {};
                    return re;
                } else {
                    Object[] re = new Object[args.length];
                    for (int i = 0; i < args.length; i++) {
                        re[i] = (args[i] == null) ? replaceStr : args[i];
                    }
                    return re;
                }
            } else {
                return args;
            }
        }
    }

}

/**
 * Simple adapter for PreparedStatementSetter that applies
 * a given array of arguments.
 */
class ArgPreparedStatementSetter implements PreparedStatementSetter, ParameterDisposer {
    private final Object[] args;
    private final String replaceStr;

    public ArgPreparedStatementSetter(Object[] args,String nullEscapeStr) {
        this.args = args;
        this.replaceStr = nullEscapeStr;
    }

    public void setValues(PreparedStatement ps) throws SQLException {
        if (this.args != null) {
            for (int i = 0; i < this.args.length; i++) {
                if (this.args[i] != null) {
                    StatementCreatorUtils.setParameterValue(ps, i + 1, SqlTypeValue.TYPE_UNKNOWN, null, this.args[i]);
                }else{
                    if (replaceStr != null) {
                        StatementCreatorUtils.setParameterValue(ps, i + 1, SqlTypeValue.TYPE_UNKNOWN, null, replaceStr);
                    }
                    else {
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
 * Adapter to enable use of a RowCallbackHandler inside a ResultSetExtractor,
 * the ResultSetExtractor extracts result in a scope.
 * <p>Uses a regular ResultSet, so we have to be careful when using it:
 * We don't use it for navigating since this could lead to unpredictable consequences.
 */
class PageableRowCallbackHandlerResultSetExtractor implements ResultSetExtractor {
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

/**
 * Page adapter for PreparedStatementCreator, allowing to use a plain SQL statement.
 */
class PagePreparedStatementCreator implements PreparedStatementCreator, SqlProvider {

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