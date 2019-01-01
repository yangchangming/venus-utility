package venus.frames.base.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import venus.VenusHelper;
import venus.frames.base.IGlobalsKeys;
import venus.frames.jdbc.core.PageableJdbcTemplate;
import venus.frames.jdbc.datasource.ConfDataSource;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

/**
 * BaseJdbcTemplate是使用springframework的JdbcTemplate类实现常见的数据库操作。
 * 使用时,从BaseJdbcTemplate派生自己的子类,派生子类必须提供带DataSource参数签名的构造函数,并调用基类的构造函数.
 * 
 * 如果要使用query(String sql)和query(String sql, Object[] args)方法, 必须提供有意义的Object
 * mapRow(ResultSet rs, int rowNum)方法实现.
 */
public abstract class BaseTemplateDao implements RowMapper, IGlobalsKeys {

    protected final ILog logger = LogMgr.getLogger(this);
    private PageableJdbcTemplate jdbcTemplate;
    public static String DEFAULT_DATASOURE_BEAN_ID = "dataSource";
    public static int DEFAULT_UNIFORM_TYPE = Types.VARCHAR;

    /**
     * 缺省构造函数,构造对象后,必须设置DataSource
     */
    public BaseTemplateDao() {
    }

    /**
     * @param dbUsr db.xml文件配置的数据源名，即DB_USER元素的src属性值
     *
     */
    public BaseTemplateDao(String dbUsr) {
        DataSource ds = new ConfDataSource(dbUsr);
        setDataSource(ds);
    }

    /**
     * 
     * @param dataSource 使用的数据源
     */
    public BaseTemplateDao(DataSource dataSource) {
        setDataSource(dataSource);
    }

    /**
     * 设置数据源
     * @param dataSource
     */
    public final void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new PageableJdbcTemplate(dataSource);
    }

    /**
     * 返回JdbcTemplate使用的数据源
     */
    public final DataSource getDataSource() {
        if (this.jdbcTemplate != null) {
            if (this.jdbcTemplate.getDataSource() != null) {
                return this.jdbcTemplate.getDataSource();
            } else {
                DataSource ds = (DataSource) VenusHelper.getBean(DEFAULT_DATASOURE_BEAN_ID);
                this.jdbcTemplate.setDataSource(ds);
                return getJdbcTemplate().getDataSource();
            }
        } else {
            DataSource ds = (DataSource) VenusHelper.getBean(DEFAULT_DATASOURE_BEAN_ID);
            setJdbcTemplate(new PageableJdbcTemplate(ds));
            return getJdbcTemplate().getDataSource();
        }
    }

    /**
     * 显式设置JdbcTemplate
     */
    public final void setJdbcTemplate(PageableJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 返回 JdbcTemplate,对于此基类没有实现的操作,可以使用JdbcTemplate来操作.
     * 需要预先使用DataSource初始化或显式设置.
     */
    public final PageableJdbcTemplate getJdbcTemplate() {
        if (this.jdbcTemplate == null) {
            DataSource ds = (DataSource) VenusHelper.getBean(DEFAULT_DATASOURE_BEAN_ID);
            setJdbcTemplate(new PageableJdbcTemplate(ds));
        }
        return this.jdbcTemplate;
    }

    /**
     * 执行单条SQL语句, 典型的是Create table,drop table等DDL语句.
     * 
     * @param sql 执行的静态SQL
     */
    public void execute(final String sql) {
        logStrartSQL(sql, null);
        long time = System.currentTimeMillis();

        getJdbcTemplate().execute(sql);

        logEndSQL(sql, null, time);
    }

    /**
     * 添加：胡捷 时间：2005-8-15
     * 执行单条insert，update语句.该方法主要为LOB型字段insert或update服务，但也可为一般字段类型服务
     * 
     * @param sql
     *            SQL语句
     * @param action PreparedStatementCallback回调接口实例，所有参数(?)都在该回调对象中处理
     * @return a result object returned by the action, or null
     */
    public Object update(final String sql, PreparedStatementCallback action) {
        logStrartSQL(sql, null);
        long time = System.currentTimeMillis();

        Object res = getJdbcTemplate().execute(sql, action);

        logEndSQL(sql, null, time);

        return res;
    }

    /**
     * 执行单条insert，update语句.
     * 
     * @param sql 执行的静态SQL
     * @return 影响的行记录数
     */
    public int update(final String sql) {
        logStrartSQL(sql, null);
        long time = System.currentTimeMillis();

        int res = getJdbcTemplate().update(sql);

        logEndSQL(sql, null, time);

        return res;

    }

    /**
     * 执行带参数的SQL语句
     * 
     * @param sql
     *            含有参数的SQL
     * @param args SQL参数
     * @return 影响的行记录数
     */
    public int update(String sql, Object[] args, int[] argTypes) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();

        int res = getJdbcTemplate().update(sql, args, argTypes);

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

        int res = getJdbcTemplate().update(sql, checkArgs(args));

        logEndSQL(sql, args, time);

        return res;
    }

    private Object[] checkArgs(Object[] args) {
        //return args;

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

    /**
     * 执行给定的SQL，返回长整数.
     * <p>
     * 使用JDBC Statement执行静态查询并返回一个long型数据
     * <p>
     * 本方法用来执行一个静态SQL，此SQL返回单行单列的整数.
     * 
     * @param sql
     *            SQL语句
     * @return 长整型, 当SQL执行结果为NULL时，返回0
     * 
     *             如果失败，抛出DataException异常
     */
    public long queryForLong(String sql) {
        logStrartSQL(sql, null);
        long time = System.currentTimeMillis();

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        
        long res = getJdbcTemplate().queryForLong(sql);



        logEndSQL(sql, null, time);

        return res;
    }

    /**
     * 执行给定的SQL，返回长整数.
     * <p>
     * 使用JDBC PreparedStatement执行动态查询并返回一个long型数据
     * <p>
     * 本方法用来执行一个动态SQL，此SQL返回单行单列的整数.
     * 
     * @param sql
     *            SQL语句
     * @param args
     *            SQL参数
     * @return 长整型, 当SQL执行结果为NULL时，返回0
     * 
     *             如果失败，抛出DataException异常
     * 
     * public long queryForLong(String sql, Object[] args,int[] argTypes) {
     * return getJdbcTemplate().queryForLong(sql, checkArgs(args),argTypes); }
     */

    /**
     * 执行给定的SQL，返回长整数.
     * <p>
     * 使用JDBC PreparedStatement执行动态查询并返回一个long型数据
     * <p>
     * 本方法用来执行一个动态SQL，此SQL返回单行单列的整数.
     * 
     * @param sql
     *            SQL语句
     * @param args
     *            SQL参数
     * @return 长整型, 当SQL执行结果为NULL时，返回0
     * 
     *             如果失败，抛出DataException异常
     */
    public long queryForLong(String sql, Object[] args) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        
        long res = getJdbcTemplate().queryForLong(sql, checkArgs(args));

        logEndSQL(sql, args, time);

        return res;
    }

    /**
     * 执行给定的SQL，返回整数.
     * <p>
     * 使用JDBC Statement执行静态查询并返回一个int型数据。
     * <p>
     * 本方法用来执行一个静态SQL，此SQL返回单行单列的整数.
     * 
     * @param sql
     *            SQL语句
     * @return 整型, 当SQL执行结果为NULL时，返回0
     */
    public int queryForInt(String sql) {
        logStrartSQL(sql, null);
        long time = System.currentTimeMillis();

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        
        int res = getJdbcTemplate().queryForInt(sql);

        logEndSQL(sql, null, time);

        return res;
    }

    /**
     * 执行给定的SQL，返回整数.
     * <p>
     * 使用JDBC PreparedStatement执行动态查询并返回一个int型数据.
     * <p>
     * 本方法用来执行一个动态SQL，此SQL返回单行单列的整数.
     * 
     * @param sql
     *            SQL语句
     * @param args
     *            SQL参数
     * @return 整型, 当SQL执行结果为NULL时，返回0
     * 
     *             如果失败，抛出DataException异常
     */
    public int queryForInt(String sql, Object[] args) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        
        int res = getJdbcTemplate().queryForInt(sql, checkArgs(args));

        logEndSQL(sql, args, time);

        return res;
    }

    /**
     * 执行给定的SQL，返回整数.
     * <p>
     * 使用JDBC PreparedStatement执行动态查询并返回一个int型数据.
     * <p>
     * 本方法用来执行一个动态SQL，此SQL返回单行单列的整数.
     * 
     * @param sql
     *            SQL语句
     * @param args
     *            SQL参数
     * 
     *            SQL参数的类型(java.sql.Types.XXXXX)
     * @return 整型, 当SQL执行结果为NULL时，返回0
     * 
     *             如果失败，抛出DataException异常
     * 
     * public int queryForInt(String sql, Object[] args,int[] argTypes) { return
     * getJdbcTemplate().queryForInt(sql, checkArgs(args),argTypes); }
     */

    /**
     * 执行给定的SQL，返回指定类型的对象.
     * <p>
     * 使用JDBC PreparedStatement执行动态查询并返回一个requiredType类型的对象.
     * <p>
     * 本方法用来执行一个动态SQL，此SQL返回单行记录.
     * 
     * @param sql
     *            SQL语句
     * @param args
     *            SQL参数
     * 
     *            SQL参数的类型(java.sql.Types.XXXXX)
     * @param requiredType
     *            返回对象类型
     * @return Object 返回requiredType指定的类型,当SQL执行结果为NULL时，返回null
     * 
     *             如果失败，抛出DataException异常
     * 
     * public Object queryForObject(String sql, Object[] args,int[] argTypes,
     * Class requiredType) { return getJdbcTemplate().queryForObject(sql,
     * checkArgs(args),argTypes, requiredType); }
     */

    /**
     * 执行给定的SQL，返回整数.
     * <p>
     * 使用JDBC Statement执行静态查询并返回一个requiredType类型的对象.
     * <p>
     * 本方法用来执行一个静态SQL，此SQL返回单行记录.
     * 
     * @param sql
     *            执行的SQL语句
     * @param args
     *            SQL语句参数
     * @param requiredType
     *            返回对象类型
     * @return Object 返回requiredType指定的类型,当SQL执行结果为NULL时，返回null
     * 
     *             如果失败，抛出DataException异常
     * 
     * public Object queryForObject(String sql, Class requiredType) {
     * 
     * return getJdbcTemplate().queryForObject(sql, requiredType); }
     */

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对选中的记录，返回一个Java对象
     * <p>
     * 使用JDBC Statement执行静态查询并返回一个Java对象.
     * 
     * @param sql
     *            执行的SQL语句
     * @param mapper
     *            记录集到对象的映射器
     * @return 对象集合
     *             如果失败，抛出DataException异常
     */
    public Object queryForObject(String sql, RowMapper mapper) {
        List re = query(sql, mapper);
        if (re != null && re.size() > 0) {
            return re.get(0);
        } else {
            return null;
        }
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对选中的记录，返回一个Java对象
     * <p>
     * 使用JDBC Statement, 而不是PreparedStatement. 如果希望使用PreparedStatemen
     * 执行静态查询，请使用重载的queryForObject方法，参数数组赋予null.
     * 
     * @param sql
     *            SQL 执行的SQL
     * @param args
     *            SQL参数
     * @param mapper
     *            记录集到对象的映射器
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
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
     * <p>
     * 使用JDBC Statement, 而不是PreparedStatement. 如果希望使用PreparedStatemen
     * 执行静态查询，请使用重载的queryForObject方法，参数数组赋予null.
     * 
     * @param sql
     *            SQL 执行的SQL
     * @param args
     *            SQL参数
     * 
     *            SQL参数的类型(java.sql.Types.XXXXX)
     * @param mapper
     *            记录集到对象的映射器
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public Object queryForObject(String sql, Object[] args, int[] argTypes,
            RowMapper mapper) {
        List re = query(sql, args, argTypes, mapper);
        if (re != null && re.size() > 0) {
            return re.get(0);
        } else {
            return null;
        }
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对每一行记录，返回一个Java对象, 子类必须覆盖mapRow方法
     * <p>
     * 使用JDBC Statement, 而不是PreparedStatement. 如果希望使用PreparedStatemen
     * 执行静态查询，请使用重载的queryForObject方法，参数数组赋予null.
     * 
     * @param sql
     *            SQL 执行的SQL
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public List query(String sql) {
        logStrartSQL(sql, null);
        long time = System.currentTimeMillis();

        final RowMapper mapper = (RowMapper) this;

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        
        List res = getJdbcTemplate().query(sql, new RowMapper() {

            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                return mapper.mapRow(rs, rowNum);
            }
        });

        logEndSQL(sql, null, time);

        return res;
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对指定范围的记录，返回一个Java对象列表, 子类必须覆盖mapRow方法
     * <p>
     * 使用JDBC Statement执行静态查询，如果只返回一个对象，请使用queryForObject方法.
     * 
     * @param sql
     *            执行的SQL语句
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public List query(String sql, int firstResult, int maxResult) {
        logStrartSQL(sql, null);
        long time = System.currentTimeMillis();

        final RowMapper mapper = (RowMapper) this;

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        
        List res = getJdbcTemplate().query(sql, new RowMapper() {

            public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                return mapper.mapRow(rs, rowNum);
            }
        }, firstResult, maxResult);

        logEndSQL(sql, null, time);

        return res;
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对每一行记录，返回一个Java对象
     * <p>
     * 使用JDBC Statement执行静态查询，如果只返回一个对象，请使用queryForObject方法.
     * 
     * @param sql
     *            执行的SQL语句
     * @param mapper
     *            记录集到对象的映射器
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public List query(String sql, final RowMapper mapper) {
        logStrartSQL(sql, null);
        long time = System.currentTimeMillis();

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        
        List res = getJdbcTemplate().query(sql, mapper);

        logEndSQL(sql, null, time);

        return res;

    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对给定范围的记录，返回一个Java对象列表
     * <p>
     * 使用JDBC Statement执行静态查询，如果只返回一个对象，请使用queryForObject方法.
     * 
     * @param sql
     *            执行的SQL语句
     * @param mapper
     *            记录集到对象的映射器
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public List query(String sql, final RowMapper mapper, int firstResult,
            int maxResult) {
        logStrartSQL(sql, null);
        long time = System.currentTimeMillis();

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }

        List res = getJdbcTemplate().query(sql, mapper, firstResult, maxResult);

        logEndSQL(sql, null, time);

        return res;
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对每一行记录， 返回一个Java对象, 子类必须覆盖mapRow方法
     * <p>
     * 使用JDBC PreparedStatement执行动态查询. 如果只返回一个对象，请使用queryForObject方法.
     * 
     * @param sql
     *            执行的SQL语句
     * @param args
     *            SQL语句参数
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public List query(String sql, Object[] args) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();

        final RowMapper mapper = (RowMapper) this;

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        
        List res = getJdbcTemplate().query(sql, checkArgs(args),
                new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum)
                            throws SQLException {

                        return mapper.mapRow(rs, rowNum);
                    }
                });

        logEndSQL(sql, args, time);

        return res;
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对每一行记录， 返回一个Java对象, 子类必须覆盖mapRow方法
     * <p>
     * 使用JDBC PreparedStatement执行动态查询. 如果只返回一个对象，请使用queryForObject方法.
     * 
     * @param sql
     *            执行的SQL语句
     * @param args
     *            SQL语句参数
     * 
     *            SQL语句参数的类型(java.sql.Types.XXXXX)
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public List query(String sql, Object[] args, int[] argTypes) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();

        final RowMapper mapper = (RowMapper) this;

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        
        List res = getJdbcTemplate().query(sql, args, argTypes,
                new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum)
                            throws SQLException {

                        return mapper.mapRow(rs, rowNum);
                    }
                });

        logEndSQL(sql, args, time);

        return res;
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对给定范围的记录， 返回一个Java对象列表, 子类必须覆盖mapRow方法
     * <p>
     * 使用JDBC PreparedStatement执行动态查询. 如果只返回一个对象，请使用queryForObject方法.
     * 
     * @param sql
     *            执行的SQL语句
     * @param args
     *            SQL语句参数
     * 
     *            SQL语句参数的类型(java.sql.Types.XXXXX)
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public List query(String sql, Object[] args, int[] argTypes,
            int firstResult, int maxResult) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();

        final RowMapper mapper = (RowMapper) this;

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        
        List res = getJdbcTemplate().query(sql, args, argTypes,
                new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum)
                            throws SQLException {

                        return mapper.mapRow(rs, rowNum);
                    }
                }, firstResult, maxResult);

        logEndSQL(sql, args, time);

        return res;
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对给定范围的记录， 返回一个Java对象列表, 子类必须覆盖mapRow方法
     * <p>
     * 使用JDBC PreparedStatement执行动态查询. 如果只返回一个对象，请使用queryForObject方法.
     * 
     * @param sql
     *            执行的SQL语句
     * @param args
     *            SQL语句参数
     * 
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public List query(String sql, Object[] args, int firstResult, int maxResult) {

        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();

        final RowMapper mapper = (RowMapper) this;

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        
        List res = getJdbcTemplate().query(sql, checkArgs(args),
                new RowMapper() {

                    public Object mapRow(ResultSet rs, int rowNum)
                            throws SQLException {

                        return mapper.mapRow(rs, rowNum);
                    }
                }, firstResult, maxResult);

        logEndSQL(sql, args, time);

        return res;
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对每一行记录，返回一个Java对象
     * <p>
     * 使用JDBC PreparedStatement执行动态查询. 如果只返回一个对象，请使用queryForObject方法.
     * 
     * @param sql
     *            执行的SQL语句
     * @param args
     *            SQL语句参数
     * 
     *            SQL语句参数的类型(java.sql.Types.XXXXX)
     * @param mapper
     *            记录集到对象的映射器
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public List query(String sql, Object[] args, int[] argTypes,
            final RowMapper mapper) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        
        List res = getJdbcTemplate().query(sql, args, argTypes, mapper);

        logEndSQL(sql, args, time);

        return res;
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对每一行记录，返回一个Java对象
     * <p>
     * 使用JDBC PreparedStatement执行动态查询. 如果只返回一个对象，请使用queryForObject方法.
     * 
     * @param sql
     *            执行的SQL语句
     * @param args
     *            SQL语句参数
     * @param mapper
     *            记录集到对象的映射器
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public List query(String sql, Object[] args, final RowMapper mapper) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        
        List res = getJdbcTemplate().query(sql, checkArgs(args), mapper);

        logEndSQL(sql, args, time);

        return res;
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对给定范围的记录，返回一个Java对象列表
     * <p>
     * 使用JDBC PreparedStatement执行动态查询. 如果只返回一个对象，请使用queryForObject方法.
     * 
     * @param sql
     *            执行的SQL语句
     * @param args
     *            SQL语句参数
     * 
     *            SQL语句参数的类型(java.sql.Types.XXXXX)
     * @param mapper
     *            记录集到对象的映射器
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public List query(String sql, Object[] args, int[] argTypes,
            final RowMapper mapper, int firstResult, int maxResult) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        
        List res = getJdbcTemplate().query(sql, args, argTypes, mapper,
                firstResult, maxResult);

        logEndSQL(sql, args, time);

        return res;
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对给定范围的记录，返回一个Java对象列表
     * <p>
     * 使用JDBC PreparedStatement执行动态查询. 如果只返回一个对象，请使用queryForObject方法.
     * 
     * @param sql
     *            执行的SQL语句
     * @param args
     *            SQL语句参数
     * @param mapper
     *            记录集到对象的映射器
     * @return 对象集合
     */
    public List query(String sql, Object[] args, final RowMapper mapper,
            int firstResult, int maxResult) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        
        List res = getJdbcTemplate().query(sql, checkArgs(args), mapper,
                firstResult, maxResult);

        logEndSQL(sql, args, time);

        return res;
    }

    /**
     * 添加：胡捷 时间：2005-8-15 执行给定SQL, 通过ResultSetExtractor接口进行数据抽取,以处理字段类型为LOB类型的查询
     * 
     * @param sql
     *            执行的SQL语句
     * @param args
     *            SQL语句参数
     * @param extractor
     *            记录集到对象的抽取器
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public Object query(String sql, Object[] args,
            final ResultSetExtractor extractor) {
        logStrartSQL(sql, args);
        long time = System.currentTimeMillis();

        if (VenusHelper.SQL_FILTER) {
            sql = VenusHelper.doSqlFilter(sql);
        }
        
        Object res = getJdbcTemplate().query(sql, checkArgs(args), extractor);

        logEndSQL(sql, args, time);
        return res;
    }

    /**
     * 使用单个 PreparedStatement 执行多条数据更新操作, 使用 JDBC 2.0 批量更新能力,
     * BatchPreparedStatementSetter用来对PreparedStatement语句进行设置值.
     * BatchPreparedStatementSetter有两个方法,getBatchSize()返回批量执行的语句个数,
     * setValues(PreparedStatement ps, int i)方法被循环调用来设置PreparedStatement 的参数值
     * <p>
     * 如果Jdbc不支持批量更新,将使用PreparedStatement的单语句操作
     * 
     * @param sql
     *            要执行的SQL,所有的批量更新都使用相同的SQL语句
     * @param pss
     *            设置参数值的接口
     * @return 影响的行数的数组
     * 
     *             如果出错将抛DataAccessException异常
     */
    public int[] batchUpdate(String sql, BatchPreparedStatementSetter pss) {
        return getJdbcTemplate().batchUpdate(sql, pss);
    }

    /*
     * RowMapper接口mapRow方法的缺省实现,
     */
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
    }

    /**
     * 添加：胡捷 时间：2005-8-16 转换带参数的预编译语句，将"?"转换为真实值
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
     * 添加：胡捷 时间：2005-8-16 获取执行前SQL语句
     * 
     * @param sql
     * @return 返回执行SQL语句
     */
    //    private String getStartSQL(String sql)
    //    {
    //        return convertPreparedSQL(sql, null);
    //    }
    /**
     * 添加：胡捷 时间：2005-8-16 获取执行前SQL语句
     * 
     * @param sql
     * @return 返回执行SQL语句
     */
    private String getStartSQL(String sql, Object[] args) {
        return convertPreparedSQL(sql, args);
    }

    //    /**
    //     * 添加：胡捷 时间：2005-8-16 获取执行前SQL语句
    //     * @param sql
    //     * @return 返回执行SQL语句，带执行时长信息
    //     */
    //    private String getEndSQL(String sql, long timestampOfBeforeExecute)
    //    {
    //        return getEndSQL(sql, null, timestampOfBeforeExecute);
    //    }

    /**
     * 添加：胡捷 时间：2005-8-16 获取执行前SQL语句
     * 
     * @param sql
     * @return 返回执行SQL语句，带执行时长信息
     */
    private String getEndSQL(String sql, Object[] args,
            long timestampOfBeforeExecute) {
        long currentTimestamp = System.currentTimeMillis();
        long time = currentTimestamp - timestampOfBeforeExecute;
        return "Executing SQL: " + convertPreparedSQL(sql, args)
                + ", SQL execute time: " + time + " milliseconds";
    }

    /**
     * @param sql
     * @param args
     * @param time
     */
    private void logEndSQL(String sql, Object[] args, long time) {
        if (VenusHelper.IS_LOG_SQL_END_ENABLED) {
            VenusHelper.getLogger(this.getClass().getName()).debug(
                    this.getEndSQL(sql, args, time));
        }
    }

    /**
     * @param sql
     * @param args
     */
    private void logStrartSQL(String sql, Object[] args) {
        if (VenusHelper.IS_LOG_SQL_START_ENABLED) {
            VenusHelper.getLogger(this.getClass().getName()).debug(
                    getStartSQL(sql, args));
        }
    }

    /**
     * 执行带参数的SQL语句,将参数全都使用统一的type，缺省使用VARCHAR
     * 
     * @param sql
     *            含有参数的SQL
     * @param args
     *            SQL参数
     * 
     *            SQL参数的类型(java.sql.Types.XXXXX)
     * @return 影响的行记录数
     * 
     *             如果失败，抛出DataException异常
     */
    public int updateWithUniformArgType(String sql, Object[] args) {

        if (args == null) {

            return update(sql, args);

        } else {

            return update(sql, args, getUniformTypeForArgs(args));

        }

    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对每一行记录， 返回一个Java对象, 子类必须覆盖mapRow方法
     * <p>
     * 返回长整数.将参数全都使用统一的type，缺省使用VARCHAR
     * <p>
     * 使用JDBC PreparedStatement执行动态查询. 如果只返回一个对象，请使用queryForObject方法.
     * 
     * @param sql
     *            执行的SQL语句
     * @param args
     *            SQL语句参数
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public List queryWithUniformArgType(String sql, Object[] args) {
        if (args == null) {

            return query(sql, args);

        } else {

            return query(sql, args, getUniformTypeForArgs(args));

        }
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对给定范围的记录， 返回一个Java对象列表, 子类必须覆盖mapRow方法
     * <p>
     * 返回长整数.将参数全都使用统一的type，缺省使用VARCHAR
     * <p>
     * 使用JDBC PreparedStatement执行动态查询. 如果只返回一个对象，请使用queryForObject方法.
     * 
     * @param sql
     *            执行的SQL语句
     * @param args
     *            SQL语句参数
     * 
     *            SQL语句参数的类型(java.sql.Types.XXXXX)
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public List queryWithUniformArgType(String sql, Object[] args,
            int firstResult, int maxResult) {
        if (args == null) {

            return query(sql, args, firstResult, maxResult);

        } else {

            return query(sql, args, getUniformTypeForArgs(args), firstResult,
                    maxResult);

        }
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对选中的记录，返回一个Java对象
     * <p>
     * 返回长整数.将参数全都使用统一的type，缺省使用VARCHAR
     * <p>
     * 使用JDBC Statement, 而不是PreparedStatement. 如果希望使用PreparedStatemen
     * 执行静态查询，请使用重载的queryForObject方法，参数数组赋予null.
     * 
     * @param sql
     *            SQL 执行的SQL
     * @param args
     *            SQL参数
     * @param mapper
     *            记录集到对象的映射器
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public Object queryForObjectWithUniformArgType(String sql, Object[] args,
            RowMapper mapper) {
        if (args == null) {

            return queryForObject(sql, args, mapper);

        } else {

            return queryForObject(sql, args, getUniformTypeForArgs(args),
                    mapper);

        }
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对每一行记录，返回一个Java对象
     * <p>
     * 返回长整数.将参数全都使用统一的type，缺省使用VARCHAR
     * <p>
     * 使用JDBC PreparedStatement执行动态查询. 如果只返回一个对象，请使用queryForObject方法.
     * 
     * @param sql
     *            执行的SQL语句
     * @param args
     *            SQL语句参数
     * @param mapper
     *            记录集到对象的映射器
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public List queryWithUniformArgType(String sql, Object[] args,
            final RowMapper mapper) {
        if (args == null) {

            return query(sql, args, mapper);

        } else {

            return query(sql, args, getUniformTypeForArgs(args), mapper);

        }
    }

    /**
     * 执行给定SQL, 通过RowMapper接口的mapRow方法，对给定范围的记录，返回一个Java对象列表
     * <p>
     * 返回长整数.将参数全都使用统一的type，缺省使用VARCHAR
     * <p>
     * 使用JDBC PreparedStatement执行动态查询. 如果只返回一个对象，请使用queryForObject方法.
     * 
     * @param sql
     *            执行的SQL语句
     * @param args
     *            SQL语句参数
     * @param mapper
     *            记录集到对象的映射器
     * @return 对象集合
     * 
     *             如果失败，抛出DataException异常
     */
    public List queryWithUniformArgType(String sql, Object[] args,
            final RowMapper mapper, int firstResult, int maxResult) {
        if (args == null) {

            return query(sql, args, mapper, firstResult, maxResult);

        } else {

            return query(sql, args, getUniformTypeForArgs(args), mapper,
                    firstResult, maxResult);

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

    //    /**
    //     * @param sql
    //     * @param time
    //     */
    //    private void logEndSQL(String sql, long time)
    //    {
    //        if (Helper.IS_LOG_SQL_END_ENABLED)
    //        {
    //            Helper.getLogger(this.getClass().getName()).debug(
    //                    this.getEndSQL(sql, time));
    //        }
    //    }
    //
    //    /**
    //     * @param sql
    //     */
    //    private void logStartSQL(String sql)
    //    {
    //        if (Helper.IS_LOG_SQL_START_ENABLED)
    //        {
    //            Helper.getLogger(this.getClass().getName()).debug(getStartSQL(sql));
    //        }
    //    }
    //	/**
    //	 * 根据表名获得 oid
    //	 *
    //	 * 该方法为辅助的代理方法
    //	 *
    //	 * 实际实现即调用 OidMgr 的 requestOID()方法征得 oid
    //	 *
    //	 * @param tableName -
    //	 * 要获得 oid 的表名
    //	 * @return venus.pub.lang.OID - 所征得的OID对象
    //	 * @roseuid 3F9389900356
    //	 */
    //	public OID generateOid(String tableName) {
    //		return OidMgr.requestOID(tableName);
    //	}
}