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
package venus.dao.provider;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import venus.VenusHelper;
import venus.core.SpiMeta;
import venus.dao.BaseDao;
import venus.datasource.GenericDataSource;
import venus.exception.VenusFrameworkException;
import venus.frames.jdbc.core.PageableJdbcTemplate;
import venus.frames.jdbc.datasource.ConfDataSource;
import venus.springsupport.BeanFactoryHelper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * <p> Base Template dao provider, based spring jdbc </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2019-03-06 13:26
 */
@SpiMeta(name = "template")
public class BaseTemplateDao extends PageableJdbcTemplate implements BaseDao {

    private static int DEFAULT_UNIFORM_TYPE = Types.VARCHAR;

    private static DataSource dataSource;

    private static Logger logger = Logger.getLogger(BaseTemplateDao.class);

    static {
        Object _dataSource = BeanFactoryHelper.getBean("dataSource");
        if (_dataSource!=null && _dataSource instanceof GenericDataSource){
            dataSource = (GenericDataSource)_dataSource;
        }else {
            logger.error("Datasource not match!");
            throw new VenusFrameworkException("Datasource not match!");
        }
    }

    /**
     * Constructor
     */
    public BaseTemplateDao(){
        super(dataSource);
    }

    public Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new VenusFrameworkException("Fetch datasource connection failure!");
        }
    }


    /**
     * 执行单条SQL语句
     *
     * @param sql 执行的静态SQL
     */
    public void execute(final String sql) {
        logStrartSQL(sql, null);
        long time = System.currentTimeMillis();
        super.execute(sql);
        logEndSQL(sql, null, time);
    }

    /**
     * 执行单条insert，update语句.该方法主要为LOB型字段insert或update服务，但也可为一般字段类型服务
     *
     * @param sql
     * @param action PreparedStatementCallback回调接口实例，所有参数(?)都在该回调对象中处理
     * @return a result object returned by the action, or null
     */
    public Object update(final String sql, PreparedStatementCallback action) {
        logStrartSQL(sql, null);
        long time = System.currentTimeMillis();
        Object res = execute(sql, action);
        logEndSQL(sql, null, time);
        return res;
    }

    /**
     * 执行单条insert，update语句
     *
     * @param sql
     * @return 影响的行记录数
     */
    public int update(final String sql) {
        logStrartSQL(sql, null);
        long time = System.currentTimeMillis();
        int res = super.update(sql);
        logEndSQL(sql, null, time);
        return res;
    }

    /**
     * 执行带参数的SQL语句
     *
     * @param sql 含有参数的SQL
     * @param args SQL参数
     * @return 影响的行记录数
     */
    public int update(String sql, Object[] args, int[] argTypes) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();
        int res = super.update(sql, args, argTypes);
        logEndSQL(sql, args, time);
        return res;
    }

    /**
     * 执行带参数的SQL语句
     *
     * @param sql 含有参数的SQL
     * @param args SQL参数
     * @return 影响的行记录数
     */
    public int update(String sql, Object[] args) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();
        int res = super.update(sql, checkArgs(args));
        logEndSQL(sql, args, time);
        return res;
    }

    /**
     * 执行给定的SQL，返回长整数.
     * 使用JDBC Statement执行静态查询并返回一个long型数据
     * 本方法用来执行一个静态SQL，此SQL返回单行单列的整数.
     *
     * @param sql
     * @return 长整型, 当SQL执行结果为NULL时，返回0, 如果失败，抛出DataException异常
     */
    public long queryForLong(String sql) {
        logStrartSQL(sql, null);
        long time = System.currentTimeMillis();
        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        long res = super.queryForLong(sql);
        logEndSQL(sql, null, time);
        return res;
    }

    /**
     * 执行给定的SQL，返回长整数.
     * 使用JDBC PreparedStatement执行动态查询并返回一个long型数据
     * 本方法用来执行一个动态SQL，此SQL返回单行单列的整数.
     *
     * @param sql
     * @param args
     * @return 长整型, 当SQL执行结果为NULL时，返回0, 如果失败，抛出DataException异常
     */
    public long queryForLong(String sql, Object[] args) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();
        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        long res = super.queryForLong(sql, checkArgs(args));
        logEndSQL(sql, args, time);
        return res;
    }

    /**
     * 执行给定的SQL，返回整数.
     * 使用JDBC Statement执行静态查询并返回一个int型数据。
     * 本方法用来执行一个静态SQL，此SQL返回单行单列的整数.
     *
     * @param sql
     * @return 整型, 当SQL执行结果为NULL时，返回0
     */
    public int queryForInt(String sql) {
        logStrartSQL(sql, null);
        long time = System.currentTimeMillis();
        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        int res = super.queryForInt(sql);
        logEndSQL(sql, null, time);
        return res;
    }

    /**
     * 执行给定的SQL，返回整数.
     * 使用JDBC PreparedStatement执行动态查询并返回一个int型数据.
     * 本方法用来执行一个动态SQL，此SQL返回单行单列的整数.
     *
     * @param sql
     * @param args
     * @return 整型, 当SQL执行结果为NULL时，返回0 如果失败，抛出DataException异常
     */
    public int queryForInt(String sql, Object[] args) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();
        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        int res = super.queryForInt(sql, checkArgs(args));
        logEndSQL(sql, args, time);
        return res;
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对选中的记录，返回一个Java对象
     * 使用JDBC Statement执行静态查询并返回一个Java对象.
     *
     * @param sql
     * @param mapper
     * @return 对象集合 如果失败，抛出DataException异常
     */
    public Object queryForObject(String sql, RowMapper mapper) {
        List result = query(sql, mapper);
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对选中的记录，返回一个Java对象
     * 使用JDBC Statement, 而不是PreparedStatement. 如果希望使用PreparedStatemen
     * 执行静态查询，请使用重载的queryForObject方法，参数数组赋予null.
     *
     * @param sql
     * @param args
     * @param mapper
     * @return 对象集合 如果失败，抛出DataException异常
     */
    public Object queryForObject(String sql, Object[] args, RowMapper mapper) {
        List re = query(sql, checkArgs(args), mapper);
        if (re != null && re.size() > 0) {
            return re.get(0);
        } else {
            return null;
        }
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对选中的记录，返回一个Java对象
     * 使用JDBC Statement, 而不是PreparedStatement. 如果希望使用PreparedStatemen
     * 执行静态查询，请使用重载的queryForObject方法，参数数组赋予null.
     *
     * @param sql
     * @param args SQL参数的类型(java.sql.Types.XXXXX)
     * @param mapper
     * @return 对象集合 如果失败，抛出DataException异常
     */
    public Object queryForObject(String sql, Object[] args, int[] argTypes, RowMapper mapper) {
        List re = query(sql, args, argTypes, mapper);
        if (re != null && re.size() > 0) {
            return re.get(0);
        } else {
            return null;
        }
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对每一行记录，返回一个Java对象
     * 使用JDBC Statement执行静态查询，如果只返回一个对象，请使用queryForObject方法.
     *
     * @param sql
     * @param mapper
     * @return 对象集合, 如果失败，抛出DataException异常
     */
    public List query(String sql, final RowMapper mapper) {
        logStrartSQL(sql, null);
        long time = System.currentTimeMillis();
        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        List res = super.query(sql, mapper);
        logEndSQL(sql, null, time);
        return res;
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对给定范围的记录，返回一个Java对象列表
     * 使用JDBC Statement执行静态查询，如果只返回一个对象，请使用queryForObject方法.
     *
     * @param sql
     * @param mapper
     * @return 对象集合, 如果失败，抛出DataException异常
     */
    public List query(String sql, final RowMapper mapper, int firstResult, int maxResult) {
        logStrartSQL(sql, null);
        long time = System.currentTimeMillis();
        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        List res = super.query(sql, mapper, firstResult, maxResult);
        logEndSQL(sql, null, time);
        return res;
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对每一行记录，返回一个Java对象
     * 使用JDBC PreparedStatement执行动态查询. 如果只返回一个对象，请使用queryForObject方法.
     *
     * @param sql
     * @param args
     *            SQL语句参数的类型(java.sql.Types.XXXXX)
     * @param mapper
     * @return 对象集合,如果失败，抛出DataException异常
     */
    public List query(String sql, Object[] args, int[] argTypes, final RowMapper mapper) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();
        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        List res = super.query(sql, args, argTypes, mapper);
        logEndSQL(sql, args, time);
        return res;
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对每一行记录，返回一个Java对象
     * 使用JDBC PreparedStatement执行动态查询. 如果只返回一个对象，请使用queryForObject方法.
     *
     * @param sql
     * @param args
     * @param mapper
     * @return 对象集合
     */
    public List query(String sql, Object[] args, final RowMapper mapper) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();
        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        List res = super.query(sql, checkArgs(args), mapper);
        logEndSQL(sql, args, time);
        return res;
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对给定范围的记录，返回一个Java对象列表
     * 使用JDBC PreparedStatement执行动态查询. 如果只返回一个对象，请使用queryForObject方法.
     *
     * @param sql
     * @param args
     *            SQL语句参数的类型(java.sql.Types.XXXXX)
     * @param mapper
     * @return 对象集合
     */
    public List query(String sql, Object[] args, int[] argTypes,
                      final RowMapper mapper, int firstResult, int maxResult) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        List res = super.query(sql, args, argTypes, mapper, firstResult, maxResult);
        logEndSQL(sql, args, time);
        return res;
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
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();
        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        List res = super.query(sql, checkArgs(args), mapper, firstResult, maxResult);
        logEndSQL(sql, args, time);
        return res;
    }

    /**
     * 执行给定SQL, 通过ResultSetExtractor接口进行数据抽取,以处理字段类型为LOB类型的查询
     *
     * @param sql
     * @param args
     * @param extractor
     * @return 对象集合
     */
    public Object query(String sql, Object[] args, final ResultSetExtractor extractor) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();
        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        Object res = super.query(sql, checkArgs(args), extractor);
        logEndSQL(sql, args, time);
        return res;
    }

    /**
     * 使用单个 PreparedStatement 执行多条数据更新操作, 使用 JDBC 2.0 批量更新能力,
     * BatchPreparedStatementSetter用来对PreparedStatement语句进行设置值.
     * BatchPreparedStatementSetter有两个方法,getBatchSize()返回批量执行的语句个数,
     * setValues(PreparedStatement ps, int i)方法被循环调用来设置PreparedStatement 的参数值
     * 如果Jdbc不支持批量更新,将使用PreparedStatement的单语句操作
     *
     * @param sql 要执行的SQL,所有的批量更新都使用相同的SQL语句
     * @param batchPreparedStatementSetter
     * @return 影响的行数的数组
     */
    public int[] batchUpdate(String sql, BatchPreparedStatementSetter batchPreparedStatementSetter) {
        return super.batchUpdate(sql, batchPreparedStatementSetter);
    }

    /**
     * 转换带参数的预编译语句，将"?"转换为真实值
     */
    private String convertPreparedSQL(String sql, Object[] args) {
        if (args == null) {
            return sql;
        } else {
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    sql = sql.replaceFirst("\\?", args[i].toString());
                } else {
                    sql = sql.replaceFirst("\\?", "null");
                }
            }
            return sql;
        }
    }

    /**
     * 获取执行前SQL语句
     *
     * @param sql
     * @return 返回执行SQL语句
     */
    private String getStartSQL(String sql, Object[] args) {
        return convertPreparedSQL(sql, args);
    }

    /**
     * 获取执行前SQL语句
     *
     * @param sql
     * @return 返回执行SQL语句，带执行时长信息
     */
    private String getEndSQL(String sql, Object[] args, long timestampOfBeforeExecute) {
        long currentTimestamp = System.currentTimeMillis();
        long time = currentTimestamp - timestampOfBeforeExecute;
        return "Executing SQL: " + convertPreparedSQL(sql, args) + ", SQL execute time: " + time + " milliseconds";
    }

    private void logEndSQL(String sql, Object[] args, long time) {
        if (VenusHelper.IS_LOG_SQL_END_ENABLED) {
            VenusHelper.getLogger(this.getClass().getName()).debug(this.getEndSQL(sql, args, time));
        }
    }

    private void logStrartSQL(String sql, Object[] args) {
        if (VenusHelper.IS_LOG_SQL_START_ENABLED) {
            VenusHelper.getLogger(this.getClass().getName()).debug(getStartSQL(sql, args));
        }
    }

    /**
     * 执行带参数的SQL语句,将参数全都使用统一的type，缺省使用VARCHAR
     *
     * @param sql
     * @param args SQL参数的类型(java.sql.Types.XXXXX)
     * @return 影响的行记录数
     */
    public int updateWithUniformArgType(String sql, Object[] args) {
        if (args == null) {
            return update(sql, args);
        } else {
            return update(sql, args, getUniformTypeForArgs(args));
        }
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对选中的记录，返回一个Java对象
     * 返回长整数.将参数全都使用统一的type，缺省使用VARCHAR
     * 使用JDBC Statement, 而不是PreparedStatement. 如果希望使用PreparedStatemen
     * 执行静态查询，请使用重载的queryForObject方法，参数数组赋予null.
     *
     * @param sql
     * @param args
     * @param mapper
     * @return 对象集合
     */
    public Object queryForObjectWithUniformArgType(String sql, Object[] args, RowMapper mapper) {
        if (args == null) {
            return queryForObject(sql, args, mapper);
        } else {
            return queryForObject(sql, args, getUniformTypeForArgs(args), mapper);
        }
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对每一行记录，返回一个Java对象
     * 返回长整数.将参数全都使用统一的type，缺省使用VARCHAR
     * 使用JDBC PreparedStatement执行动态查询. 如果只返回一个对象，请使用queryForObject方法.
     *
     * @param sql
     * @param args
     * @param mapper
     * @return 对象集合
     */
    public List queryWithUniformArgType(String sql, Object[] args, final RowMapper mapper) {
        if (args == null) {
            return query(sql, args, mapper);
        } else {
            return query(sql, args, getUniformTypeForArgs(args), mapper);
        }
    }

    private int[] getUniformTypeForArgs(int len) {
        int[] types = new int[len];
        for (int i = 0; i < len; i++) {
            types[i] = DEFAULT_UNIFORM_TYPE;
        }
        return types;
    }


    private int[] getUniformTypeForArgs(Object[] args) {
        int types[] = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null && args[i] instanceof Timestamp) {
                types[i] = Types.TIMESTAMP;
            } else {
                types[i] = DEFAULT_UNIFORM_TYPE;
            }
        }
        return types;
    }

    private Object[] checkArgs(Object[] args) {
        Object ds = this.getDataSource();
        if ((ds == null) || !(ds instanceof ConfDataSource)) {
            return args;
        } else {
            String replaceStr = ((ConfDataSource) ds).getNullEscapeStr();
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
