package venus.frames.mainframe.util;

import org.apache.commons.lang3.StringUtils;
import venus.frames.base.IGlobalsKeys;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.context.ContextMgr;
import venus.frames.mainframe.context.IContext;
import venus.frames.mainframe.currentlogin.IProfile;
import venus.frames.mainframe.currentlogin.ProfileException;
import venus.frames.mainframe.currentlogin.ProfileMgr;
import venus.frames.mainframe.currentlogin.SimpleProfile;
import venus.frames.mainframe.db.conpool.ConnectionHelper;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.ncache.CacheFactory;
import venus.frames.mainframe.ncache.ICache;
import venus.frames.mainframe.oid.OidMgr;
import venus.frames.web.page.PageTool;
import venus.pub.lang.OID;
import venus.pub.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Venus中所有工具的辅助代理方法，Venus系列组件使用的快速入口
 *
 * @author wujun
 */
public class Helper extends PageTool implements IGlobalsKeys {

    /**
     * WEB WAR ContextPath
     */
    public static String WEB_CONTEXT_PATH;

    /**
     * DEFAULT_PAGE_NO
     */
    public static int DEFAULT_PAGE_NO = 1;

    /**
     * DEFAULT_PAGE_SIZEh
     */
    public static int DEFAULT_PAGE_SIZE = 15;

    /**
     * DEFAULT_UPLOAD_PATH = "/WEB-INF/upload";
     */
    public static String DEFAULT_UPLOAD_PATH = "/WEB-INF/upload/";

    /**
     * RESOURCE_PATH 资源路径
     */
    public static String RESOURCE_PATH = "";

    /**
     * 样式主题
     */
    public static String THEME = "default";

    /**
     * VALIDATE_AT_POPULATE
     */
    public static boolean VALIDATE_AT_POPULATE = false;

    /**
     * VALIDATE_AT_POPULATE_FROM_RESULTSET
     */
    public static boolean VALIDATE_AT_POPULATE_FROM_RESULTSET = false;

    /**
     * 是否过滤where 1=1这样的sql语句，以满足客户的安全要求
     */
    public static boolean SQL_FILTER = false;

    /**
     * 时间：2005-8-16 是否记录SQL语句调试语句
     */
    public static boolean IS_LOG_SQL_START_ENABLED = false;

    public static boolean IS_LOG_SQL_END_ENABLED = false;

    /**
     * 时间：2005-8-16 是否页面调试模式，调试模式下页面不作权限校验
     */
    public static boolean IS_DEBUG = false;

    /**
     * 提供统一的文件上传目录，可以通过两种方式配置 1、配置相对路径，以WEB-INF为根，例如/WEB-INF/upload/ 2、配置绝对路径
     * 返回值均为绝对路径
     *
     * @return
     */
    public static String getUploadPath() {
        if (DEFAULT_UPLOAD_PATH.indexOf("WEB-INF") > 0)
            return PathMgr.getRealRootPath() + "/" + DEFAULT_UPLOAD_PATH;
        return DEFAULT_UPLOAD_PATH;
    }

    /**
     * 返回当前系统配置的资源路径
     *
     * @return
     */
    public static String getResourcePath() {
        String resourcePath = RESOURCE_PATH;
        if (StringUtils.isEmpty(RESOURCE_PATH)) {
            resourcePath = PathMgr.getRealRootPath();
        } else if (!resourcePath.startsWith("http")) {
            resourcePath = PathMgr.getRealRootPath() + resourcePath;
        }
        return resourcePath;
    }

    /**
     * 返回当前系统配置的主题
     *
     * @return
     */
    public static String getTheme() {
        String theme = THEME;
        if (StringUtils.isEmpty(theme)){
            theme = "default";
        }
        return theme;
    }

    /**
     * 过滤SQL语句 过滤的SQL语句格式如下： 1.数字 (<>=!) 数字 2.(数字 (<>=!) 数字) 3.NOT 数字 (<>=!)
     * 数字
     *
     * @param sql String 需要过滤的SQL语句
     * @return String 过滤后的SQL语句
     */
    public static String doSqlFilter(String sql) {

        boolean ExsitParentheses = true;
        boolean ExsitNOT = true;

        String regEx1 = "[+-]*\\s*\\d+[!=<>\\s]+[+-]*\\s*\\d+";
        String regEx2 = "\\(\\s*###\\s*\\)";
        String regEx3 = "(?i)not\\s+###";
        String regEx4 = "###\\s+((?i)or|(?i)and)+\\s";
        String regEx5 = "\\s((?i)or|(?i)and|(?i)where(?i)|having)+\\s+###";

        Matcher matcher = Pattern.compile(regEx1).matcher(sql);

        if (matcher.find()) {
            sql = matcher.replaceAll(" ### ");
        }

        while (ExsitNOT || ExsitParentheses) {
            ExsitNOT = true;
            ExsitParentheses = true;

            matcher = Pattern.compile(regEx2).matcher(sql);
            if (matcher.find()) {
                sql = matcher.replaceAll(" ### ");
            } else {
                ExsitParentheses = false;
            }

            matcher = Pattern.compile(regEx3).matcher(sql);
            if (matcher.find()) {
                sql = matcher.replaceAll(" ### ");
            } else {
                ExsitNOT = false;
            }

            matcher = Pattern.compile(regEx4).matcher(sql);
            if (matcher.find()) {
                sql = matcher.replaceAll(" ");
            }

            matcher = Pattern.compile(regEx5).matcher(sql);
            if (matcher.find()) {
                sql = matcher.replaceAll(" ");
            }
        }

        return sql;
    }

    /**
     * 此方法为辅助方法，代理 ProfileMgr.getSessionProfile(WEB_CONTEXT_KEY, sessionid);
     * <p/>
     * 根据 sessionID 取得暂存数据PROFILE
     * <p/>
     * 标识数据堆位置：WEB（IGlobalsKeys.WEB_CONTEXT_KEY）
     *
     * @param sessionid -
     *                  当前连接的sessionid号
     * @return venus.frames.mainframe.base.currentlogin.IProfile -
     *         SessionProfile数据堆对象
     * @throws venus.frames.mainframe.base.currentlogin.ProfileException
     *
     */
    public static IProfile getSessionProfile(String sessionid)
            throws ProfileException {

        return ProfileMgr.getSessionProfile(WEB_CONTEXT_KEY, sessionid);

    }

    /**
     * 此方法为辅助方法，代理 ProfileMgr.getSessionProfile(WEB_CONTEXT_KEY, sessionid);
     * <p/>
     * 根据 sessionID 取得暂存数据PROFILE
     * <p/>
     * 标识数据堆位置：WEB（IGlobalsKeys.WEB_CONTEXT_KEY）
     *
     * @param IProfile profile - 当前请求
     * @return venus.frames.mainframe.base.currentlogin.IProfile -
     *         SessionProfile数据堆对象
     * @throws venus.frames.mainframe.base.currentlogin.ProfileException
     *
     */
    public static IProfile getSessionProfile(HttpServletRequest request)
            throws ProfileException {

        return ProfileMgr.getSessionProfile(WEB_CONTEXT_KEY, request
                .getSession().getId());

    }

    /**
     * 此方法为辅助方法，代理 ProfileMgr.getSessionProfile(WEB_CONTEXT_KEY, sessionid);
     * <p/>
     * 根据 sessionID 取得暂存数据PROFILE
     * <p/>
     * 标识数据堆位置：WEB（IGlobalsKeys.WEB_CONTEXT_KEY）
     *
     * @param IProfile profile - 当前请求
     * @return Object - 暂存对象
     * @throws venus.frames.mainframe.base.currentlogin.ProfileException
     *
     */
    public static Object getAppAttributeFromProfile(HttpServletRequest request,
                                                    String key) throws ProfileException {

        IProfile p = ProfileMgr.getSessionProfile(WEB_CONTEXT_KEY, request
                .getSession().getId());

        return p.getAppAttribute(key);

    }

    /**
     * 此方法为辅助方法，代理 ProfileMgr.getSessionProfile(WEB_CONTEXT_KEY, sessionid);
     * <p/>
     * 根据 sessionID 取得暂存数据PROFILE
     * <p/>
     * 标识数据堆位置：WEB（IGlobalsKeys.WEB_CONTEXT_KEY）
     *
     * @param IProfile profile - 当前请求
     * @return Object - 暂存对象
     * @throws venus.frames.mainframe.base.currentlogin.ProfileException
     *
     */
    public static Object getAttributeFromProfile(HttpServletRequest request,
                                                 String key) throws ProfileException {

        IProfile p = ProfileMgr.getSessionProfile(WEB_CONTEXT_KEY, request
                .getSession().getId());

        return p.getAttribute(key);

    }

    public static void setAttributeIntoProfile(HttpServletRequest request,
                                               String key, Object obj) throws ProfileException {

        IProfile p = ProfileMgr.getSessionProfile(WEB_CONTEXT_KEY, request
                .getSession().getId());

        p.setAttribute(key, obj);

    }

    public static void setAttributeIntoProfile(IRequest request, String key,
                                               Object obj) throws ProfileException {

        IProfile p = request.getCurrentLoginProfile();

        p.setAttribute(key, obj);

    }

    /**
     * 此方法为辅助方法，代理 ProfileMgr.getSessionProfile(WEB_CONTEXT_KEY, sessionid);
     * <p/>
     * 根据 sessionID 取得暂存数据PROFILE
     * <p/>
     * 标识数据堆位置：WEB（IGlobalsKeys.WEB_CONTEXT_KEY）
     *
     * @param IProfile profile - 当前请求
     * @return Object - 暂存对象
     * @throws venus.frames.mainframe.base.currentlogin.ProfileException
     *
     */
    public static Object getSysAttributeFromProfile(HttpServletRequest request,
                                                    String key) throws ProfileException {

        IProfile p = ProfileMgr.getSessionProfile(WEB_CONTEXT_KEY, request
                .getSession().getId());

        return p.getSysAttribute(key);

    }

    /**
     * 此方法为辅助方法，代理 ProfileMgr.getSessionProfile(WEB_CONTEXT_KEY, sessionid);
     * <p/>
     * 根据 sessionID 取得暂存数据PROFILE
     * <p/>
     * 标识数据堆位置：WEB（IGlobalsKeys.WEB_CONTEXT_KEY）
     *
     * @param IProfile profile - 当前请求
     * @return Object - 暂存对象
     * @throws venus.frames.mainframe.base.currentlogin.ProfileException
     *
     */
    public static String getLoginName(HttpServletRequest request)
            throws ProfileException {

        IProfile p = ProfileMgr.getSessionProfile(WEB_CONTEXT_KEY, request
                .getSession().getId());

        return (String) p.getSysAttribute(SimpleProfile.SYS_LOGIN_NAME);

    }

    public static Object getLoginName(IRequest request) throws ProfileException {

        IProfile p = request.getCurrentLoginProfile();

        return (String) p.getSysAttribute(SimpleProfile.SYS_LOGIN_NAME);

    }

    public static Object getSysAttributeFromProfile(IRequest request, String key)
            throws ProfileException {

        IProfile p = request.getCurrentLoginProfile();

        return p.getSysAttribute(key);

    }

    public static Object getAppAttributeFromProfile(IRequest request, String key)
            throws ProfileException {

        IProfile p = request.getCurrentLoginProfile();

        return p.getAppAttribute(key);

    }

    public static Object getAttributeFromProfile(IRequest request, String key)
            throws ProfileException {

        IProfile p = request.getCurrentLoginProfile();

        return p.getAttribute(key);

    }

    /**
     * 此方法为辅助方法，代理 ProfileMgr.getSessionProfile(WEB_CONTEXT_KEY, sessionid);
     * <p/>
     * 根据 sessionID 取得暂存数据PROFILE
     * <p/>
     * 标识数据堆位置：WEB（IGlobalsKeys.WEB_CONTEXT_KEY）
     *
     * @param IRequest request - 当前请求
     * @return venus.frames.mainframe.base.currentlogin.IProfile -
     *         SessionProfile数据堆对象
     * @throws venus.frames.mainframe.base.currentlogin.ProfileException
     *
     */
    public static IProfile getSessionProfile(IRequest request)
            throws ProfileException {

        return request.getCurrentLoginProfile();

    }

    /**
     * 此方法为辅助方法，代理 ProfileMgr.eraseProfile(WEB_CONTEXT_KEY, sessionid);
     * <p/>
     * 根据 sessionID 删除暂存数据PROFILE
     * <p/>
     * 静态方法 ： 为 eraseSessionProfile(....) 的静态代理方法
     * <p/>
     * 具体操作流程
     * <p/>
     * 1. 得到 ProfileMgr 的全局单一实例 pm
     * <p/>
     * 2. 根据该pm方法eraseSessionProfile（...）删除暂存数据PROFILE
     *
     * @param sessionid -
     *                  当前连接的sessionid
     * @return void
     */

    public static void eraseProfile(String sessionid) {

        ProfileMgr.eraseProfile(WEB_CONTEXT_KEY, sessionid);
    }

    /**
     * 此方法为辅助方法，代理 CacheFactory.removeCache(cacheName)
     * <p/>
     * 根据 sessionID 取得暂存数据PROFILE
     * <p/>
     * 静态方法 ：为 getProfile(...) 的静态代理方法
     * <p/>
     * 具体操作流程:
     * <p/>
     * 1.得到 ProfileMgr 的全局单一实例 pm
     * <p/>
     * 2.根据 getProfile
     *
     * @param loc       -
     *                  标识数据堆位置：WEB（IGlobalsKeys.WEB_CONTEXT_KEY）还是APP（IGlobalsKeys.APP_CONTEXT_KEY）
     * @param sessionid -
     *                  当前连接的sessionid号
     * @return venus.frames.mainframe.base.currentlogin.IProfile -
     *         SessionProfile数据堆对象
     * @throws venus.frames.mainframe.base.currentlogin.ProfileException
     *
     */
    public static IProfile getSessionProfile(String loc, String sessionid)
            throws ProfileException {

        return ProfileMgr.getSessionProfile(loc, sessionid);

    }

    /**
     * 根据 sessionID 删除暂存数据PROFILE
     * <p/>
     * 静态方法 ： 为 eraseSessionProfile(....) 的静态代理方法
     * <p/>
     * 具体操作流程
     * <p/>
     * 1. 得到 ProfileMgr 的全局单一实例 pm
     * <p/>
     * 2. 根据该pm方法eraseSessionProfile（...）删除暂存数据PROFILE
     *
     * @param loc       -
     *                  标识数据堆位置：WEB（IGlobalsKeys.WEB_CONTEXT_KEY）还是APP（IGlobalsKeys.APP_CONTEXT_KEY）
     * @param sessionid -
     *                  当前连接的sessionid
     * @return void
     */

    public static void eraseProfile(String loc, String sessionid) {

        ProfileMgr.eraseProfile(loc, sessionid);
    }

    /**
     * 在CACHE 列表中销毁缓存实例
     *
     * @param cacheName -
     *                  缓存名称
     * @return void
     */
    public static void removeCache(String cacheName) {

        CacheFactory.getCacheManager().clearCache(cacheName);
    }

    /**
     * 获取缓存实例
     * <p/>
     * 该方法同 createCache 所不同的是先查找缓存列表中是否存在cache， 有则直接返回，否则再构建一个永不过期的缓存实例
     *
     * @param cacheName -
     *                  缓存名称
     * @return Cache缓存实例
     */
    public static ICache getCache(String cacheName) {

        return CacheFactory.getCacheManager().getCache(cacheName);
    }

    /**
     * 创建缓存实例,如果已经存在，直接返回缓存实例，否则根据传入参数创建新的缓存示例
     *
     * @param cacheName     缓存名称
     * @param refreshPeriod 缓存过期时间，单位秒；传入-1表示永不过期
     * @return 缓存实例
     */
    public static ICache createCache(String cacheName, int refreshPeriod) {

        return CacheFactory.getCacheManager().createCache(cacheName,
                refreshPeriod);
    }

    /**
     * 此方法为辅助方法，代理 ContextMgr.getContext();
     * <p/>
     * 根据传入标识获取上下文对象<br>
     * <p/>
     * 标识为： IGlobalsKeys.WEB_CONTEXT_KEY
     *
     * @param key 上下文对象在系统中的标识
     * @return venus.frames.mainframe.context.IContext
     */
    public static IContext getContext() {
        return ContextMgr.getContext();
    }

    /**
     * 此方法为辅助方法，代理 ConnectionHelper.requestConnection(m_strDBUsr)
     * <p/>
     * 通过该方法获取指定名称（消费字）的数据库连接 该消费字在db.xml中定义<DB_USER name="TOOLS"
     * src="TOOLS_DB_SRC"/>中的name
     *
     * @param caller -
     *               记录日志的调用者,不可以为空
     * @return java.sql.Connection - 数据库连接
     * @throws SQLException Sql异常
     */
    public static Connection requestConnection(String m_strDBUsr)
            throws SQLException {

        return ConnectionHelper.requestConnection(m_strDBUsr);

    }

    /**
     * 此方法为辅助方法，代理 ConnectionHelper.requestConnection()
     * <p/>
     * 通过该方法获取缺省数据库连接
     * <p/>
     * 即db.xml中<DB_SRC name="MAIN" .../>指定的数据源的连接
     *
     * @return java.sql.Connection - 数据库连接
     * @throws SQLException Sql异常
     */
    public static Connection requestConnection() throws SQLException {

        return ConnectionHelper.requestConnection();

    }

    /**
     * 此方法为辅助方法，代理 LogMgr.getLogger(caller)
     * <p/>
     * 静态方法，对应于静态方法 getLogger(String)的简单包装
     * <p/>
     * 传入参数：记录日志的调用者自身句柄，参数不可以为空，否则会抛出空指针异常
     * <p/>
     * 该方法通过调用者句柄自动获取调用者的类名
     *
     * @param caller -
     *               记录日志的调用者,不可以为空
     * @return venus.frames.mainframe.log.Ilogger - LOG驱动实例
     */
    @Deprecated
    public static ILog getLogger(Object caller) {

        return LogMgr.getLogger(caller);

    }

    /**
     * 此方法为辅助方法，代理 LogMgr.getLogger(caller)
     * <p/>
     * 静态方法对应于 getLogger(...) 的静态代理方法
     * <p/>
     * 传入参数：记录日志的调用者名字，参数不可以为空，否则会抛出空指针异常
     * <p/>
     * 得到LogMgr 的实例并根据得到的实现者名得到 LOG驱动实例
     *
     * @param caller 记录日志的调用者名字
     * @return venus.frames.mainframe.log.Ilogger - LOG驱动实例
     */
    @Deprecated
    public static ILog getLogger(String caller) {

        return LogMgr.getLogger(caller);
    }

    /**
     * 此方法为辅助方法，代理 OidMgr.requestOIDArray(tableName,len); 先得到该表的生成器然后得到生成oid对象组
     *
     * @param tableName 要请求获得OID的表名
     * @param tableName 要请求获得OID的个数
     * @return venus.pub.lang.OID[] 该表新的最大的OID对象组
     */
    public static OID[] requestOIDArray(String tableName, Integer len) {

        return OidMgr.requestOIDArray(tableName, len);
    }

    /**
     * 此方法为辅助方法，代理 OidMgr.requestOID(tableName); 先得到该表的生成器然后得到生成oid
     *
     * @param tableName 要请求获得OID的表名
     * @return venus.pub.lang.OID 该表新的最大的OID对象
     */
    public static OID requestOID(String tableName) {

        return OidMgr.requestOID(tableName);
    }

    /**
     * 此方法为辅助方法，代理 OidMgr.hasOIDTable(tableName);
     *
     * @param tableName
     * @return boolean
     */
    public static boolean hasOIDTable(String tableName) {
        return OidMgr.hasOIDTable(tableName);
    }

    /**
     * 此方法为辅助方法，代理 ConfMgr.getConfReader(tag); 根据给定的TAG名，返回配置解析器
     * 此方法为辅助方法，帮助项目组件更便捷的获取配置数据<br>
     * 建议用户使用该方法取得解析xml节点的类实例。
     *
     * @param tag 待解析的节点名称
     * @return xml文档节点解析器
     */
    public static IConfReader getConfReader(String tag) {

        return ConfMgr.getConfReader(tag);

    }

    /**
     * 此方法为辅助方法，代理 ClassLocator.loadClass(...);
     *
     * @param className 待加载的类名
     * @return 已经加载的Class对象
     * @throws ClassNotFoundException
     *          如果不能找到相应的类
     */
    public static Class loadClass(String className)
            throws ClassNotFoundException {
        return ClassLocator.loadClass(className);
    }

    /**
     * 此方法为辅助方法，代理 PathMgr.getResourceAsStream(...) <br>
     * 获取给定路径文件的IO流，
     *
     * @param path 文件的虚拟路径（相对路径）
     * @return 文件IO流：InputStream
     * @see PathMgr.getResourceAsStream
     */
    public static InputStream getResourceAsStream(String path) {
        return PathMgr.getResourceAsStream(path);
    }

    /**
     * 调用日志记录方法
     *
     * @param message 日志信息
     * @return void
     * @roseuid 3FAA2C0F037A
     */
    public static void debug(Object message) {
        getIlog().debug(message);
    }

    /**
     * 调用日志记录方法
     *
     * @param message 日志信息
     * @return void
     * @roseuid 3FAA2C0F0399
     */
    @Deprecated
    public static void info(Object message) {
        getIlog().info(message);
    }

    /**
     * 调用日志记录方法
     *
     * @param message 日志信息
     * @return void
     * @roseuid 3FAA2C0F03A9
     */
    @Deprecated
    public static void warn(Object message) {
        getIlog().warn(message);
    }

    /**
     * 调用日志记录方法
     *
     * @param message 日志信息
     * @return void
     * @roseuid 3FAA2C0F03B9
     */
    @Deprecated
    public static void error(Object message) {
        getIlog().error(message);
    }

    /**
     * 调用日志记录方法
     *
     * @param message 日志信息
     * @roseuid 3FAA2C0F03D8
     */
    @Deprecated
    public static void fatal(Object message) {
        getIlog().fatal(message);
    }

    /**
     * 调用日志记录方法
     *
     * @param nLevel  日志级别
     * @param message 日志信息
     * @return void
     * @roseuid 3FAA2C0F03E7
     */
    @Deprecated
    public static void log(int nLevel, Object message) {
        getIlog().log(nLevel, message);
    }

    /**
     * 调用日志记录方法
     *
     * @param message 日志信息
     * @param t       异常诱因
     * @return void
     * @roseuid 3FAA2C10001F
     */
    @Deprecated
    public static void debug(Object message, Throwable t) {
        getIlog().debug(message, t);
    }

    /**
     * 调用日志记录方法
     *
     * @param message 日志信息
     * @param t       异常诱因
     * @return void
     * @roseuid 3FAA2C10003E
     */
    @Deprecated
    public static void info(Object message, Throwable t) {
        getIlog().info(message, t);
    }

    /**
     * 调用日志记录方法
     *
     * @param message 日志信息
     * @param t       异常诱因
     * @return void
     * @roseuid 3FAA2C10006D
     */
    @Deprecated
    public static void warn(Object message, Throwable t) {
        getIlog().warn(message, t);
    }

    /**
     * 调用日志记录方法
     *
     * @param message 日志信息
     * @param t       异常诱因
     * @return void
     * @roseuid 3FAA2C10008C
     */
    @Deprecated
    public static void error(Object message, Throwable t) {
        getIlog().error(message, t);
    }

    /**
     * 调用日志记录方法
     *
     * @param message 日志信息
     * @param t       异常诱因
     * @return void
     * @roseuid 3FAA2C1000AB
     */
    @Deprecated
    public static void fatal(Object message, Throwable t) {
        getIlog().fatal(message, t);
    }

    /**
     * 调用日志记录方法
     *
     * @param nLevel  日志级别
     * @param message 日志信息
     * @param t       异常诱因
     * @return void
     * @roseuid 3FAA2C1000DA
     */
    @Deprecated
    public static void log(int nLevel, Object message, Throwable t) {
        getIlog().log(nLevel, message, t);
    }

    /**
     * 得到日志记录驱动实例
     *
     * @return ILog LOG驱动实例
     */
    private static ILog getIlog() {
        return LogMgr.getLogger(Helper.class);
    }

    /**
     * 得到Bean
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return BeanFactoryHolder.getBean(name);
    }

    public static String getContextPath(HttpServletRequest request) {

        if (PathMgr.WEB_CONTEXT_PATH == null
                || PathMgr.WEB_CONTEXT_PATH.length() < 1) {

            PathMgr.WEB_CONTEXT_PATH = request.getContextPath();
            Helper.WEB_CONTEXT_PATH = PathMgr.WEB_CONTEXT_PATH;
            Helper.getContext().setAttribute(WEB_CONTEXT_PATH_KEY,
                    PathMgr.WEB_CONTEXT_PATH);

        }

        return PathMgr.WEB_CONTEXT_PATH;
    }

	/* populate */

    /**
     * Bean注值工具方法
     *
     * @param obj     target bean
     * @param request source request
     * @return
     */
    public static boolean populate(Object obj, IRequest request) {
        return PopulateUtil.populate(obj, request);
    }

    /**
     * Bean复制工具方法
     *
     * @param obj              target bean
     * @param request          source request
     * @param ignoreProperties ignore target bean's property name
     * @return
     */
    public static boolean populate(Object obj, IRequest request,
                                   String[] ignoreProperties) {

        return PopulateUtil.populate(obj, request, ignoreProperties);

    }

    /**
     * Bean复制工具方法
     *
     * @param obj              target bean
     * @param request          source request
     * @param map              rename property map: key: srcName, value:targetName
     * @param ignoreProperties ignore target bean's property name
     * @return
     */
    public static boolean populate(Object obj, IRequest request, Map map) {

        return PopulateUtil.populate(obj, request, map);

    }

    /**
     * Bean复制工具方法
     *
     * @param obj              target bean
     * @param request          source request
     * @param map              rename property map: key: srcName, value:targetName
     * @param ignoreProperties ignore target bean's property name
     * @return
     */
    public static boolean populate(Object obj, IRequest request, Map map,
                                   String[] ignoreProperties) {

        return PopulateUtil.populate(obj, request, map, ignoreProperties);

    }

    /*
     * Bean注值工具方法 @param obj target bean @param rs source ResultSet @return
     * @throws SQLException @throws BeansException
     */
    public static boolean populate(Object obj, java.sql.ResultSet rs)
            throws SQLException {

        return PopulateUtil.populate(obj, rs);

    }

    /*
     * Bean注值工具方法 @param obj target bean @param rs source ResultSet @param map
     * rename property map: key: srcName, value:targetName @return @throws
     * SQLException @throws BeansException
     */
    public static boolean populate(Object obj, java.sql.ResultSet rs, Map map)
            throws SQLException {

        return PopulateUtil.populate(obj, rs, map);

    }

    /*
     * Bean注值工具方法 @param obj target bean @param rs source ResultSet @param
     * ignoreProperties ignore target bean's property name @return @throws
     * SQLException
     */
    public static boolean populate(Object obj, java.sql.ResultSet rs, String[] ignoreProperties) throws SQLException {
        return PopulateUtil.populate(obj, rs, ignoreProperties);
    }

    /**
     * Bean注值工具方法
     *
     * @param obj              target bean
     * @param rs               source ResultSet
     * @param map              rename property map: key: srcName, value:targetName
     * @param ignoreProperties ignore target bean's property name
     * @return
     * @throws SQLException
     */
    public static boolean populate(Object obj, java.sql.ResultSet rs, Map map,
                                   String[] ignoreProperties) throws SQLException {

        return PopulateUtil.populate(obj, rs, map, ignoreProperties);

    }

    /**
     * 根据request变量中的参数值向Bean中注值
     *
     * @param obj                 被注值的Bean
     * @param request
     * @param prefix              request变量中参数的前缀
     * @param postfix             request变量中参数的后缀
     * @param effectiveProperties 参与注值的有效属性
     * @author lihong@use.com.cn
     */
    public static boolean populateFromRequest(Object obj, IRequest request,
                                              String prefix, String postfix, String[] effectiveProperties) {

        return PopulateUtil.populateFromRequest(obj, request, prefix, postfix,
                effectiveProperties);

    }

    /**
     * 根据request变量中的参数值向Bean中注值
     *
     * @param obj                 被注值的Bean
     * @param request
     * @param prefix              request变量中参数的前缀
     * @param postfix             request变量中参数的后缀
     * @param map                 rename property map: key: srcName, value:targetName
     * @param effectiveProperties 参与注值的有效属性
     * @author lihong@use.com.cn
     */
    public static boolean populateFromRequest(Object obj, IRequest request,
                                              String prefix, String suffix, Map map, String[] effectiveProperties) {

        return PopulateUtil.populateFromRequest(obj, request, prefix, suffix,
                map, effectiveProperties);

    }

    /**
     * Bean复制工具方法
     *
     * @param source           source bean
     * @param target           target bean
     * @param ignoreProperties ignore target bean's property name
     * @return
     */
    public static boolean copyProperties(Object source, Object target,
                                         String[] ignoreProperties) {

        return PopulateUtil.copyProperties(source, target, ignoreProperties);
    }

    /**
     * Bean复制工具方法
     *
     * @param source source bean
     * @param target target bean
     * @return
     */
    public static boolean copyProperties(Object source, Object target) {

        return PopulateUtil.copyProperties(source, target);

    }

    /**
     * Bean复制工具方法
     *
     * @param srcObj    source bean
     * @param targetObj target bean
     * @param map       rename property map: key: srcName, value:targetName
     * @return
     */

    public static boolean copyProperties(Object source, Object target, Map map) {

        return PopulateUtil.copyProperties(source, target, map);

    }

    /**
     * Bean复制工具方法 缺省将同名的属性均注值
     *
     * @param source           source bean
     * @param target           target bean
     * @param map              rename property map: key: srcName, value:targetName
     * @param ignoreProperties ignore target bean's property name
     * @return
     */

    public static boolean copyProperties(Object source, Object target, Map map,
                                         String ignoreProperties[]) {

        return PopulateUtil.copyProperties(source, target, map,
                ignoreProperties);

    }

    /**
     * Bean复制工具方法
     *
     * @param source source bean must be a instance of target
     * @param target target bean
     * @return
     */
    public static boolean copyPropertiesFromRelative(Object source,
                                                     Object target) {

        return PopulateUtil.copyPropertiesFromRelative(source, target);

    }

    /**
     * Bean复制工具方法
     *
     * @param source           source bean must be a instance of target
     * @param target           target bean
     * @param ignoreProperties array of property names to ignore
     * @return
     */
    public static boolean copyPropertiesFromRelative(Object source,
                                                     Object target, String[] ignoreProperties) {

        return PopulateUtil.copyPropertiesFromRelative(source, target,
                ignoreProperties);

    }

    /**
     * Bean复制工具方法
     *
     * @param srcObj    source bean must be a instance of target
     * @param targetObj target bean
     * @param map       key: targetName, value:srcName
     * @return
     */

    public static boolean copyPropertiesFromRelative(Object srcObj,
                                                     Object targetObj, Map map) {

        return PopulateUtil.copyPropertiesFromRelativeAll(srcObj, targetObj,
                map);

    }

    /**
     * Bean复制工具方法 缺省将同名的属性均注值
     *
     * @param srcObj    source bean must be a instance of target
     * @param targetObj target bean
     * @param map       key: targetName, value:srcName
     * @return
     */

    public static boolean copyPropertiesFromRelativeAll(Object srcObj,
                                                        Object targetObj, Map map) {

        return PopulateUtil.copyPropertiesFromRelativeAll(srcObj, targetObj,
                map);
    }

    public static Date getGBDateFrmString(String dateValue)
            throws ParseException {
        return DateUtil.getGBDateFrmString(dateValue);
    }

    public static String getStringFrmGBDate(Date dateValue) {
        return DateUtil.getStringFrmGBDate(dateValue);
    }

}
