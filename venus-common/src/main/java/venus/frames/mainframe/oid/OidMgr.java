package venus.frames.mainframe.oid;

import org.apache.log4j.Logger;
import venus.frames.base.IGlobalsKeys;
import venus.frames.mainframe.db.conpool.ConnectionHelper;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.oid.bs.IOidBS;
import venus.frames.mainframe.taskmgr.IService;
import venus.frames.mainframe.util.BeanFactoryHolder;
import venus.frames.mainframe.util.ClassLocator;
import venus.frames.mainframe.util.ConfMgr;
import venus.frames.mainframe.util.IConfReader;
import venus.pub.lang.OID;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * OID管理器，供外部单一入口访问<br>
 * 
 * 
 * 实现了启动任务，在系统启动任务中启动<br>
 * 
 * 采用全局单实例模式，singleton model
 * 
 * @author 张文韬
 */
public class OidMgr implements IService, IGlobalsKeys {

    private static Logger logger = Logger.getLogger(OidMgr.class);


    /**
     * 线程的是否运行停止的开关
     * 
     * shutdown() 修改该开关为 FALSE
     */
    private boolean m_bRunning = false;

    /**
     * singleton model中用到的，用于存储该类的单一实例
     * 
     * used for singleton model
     */
    private static OidMgr m_Singleton = null;

    /**
     * 存储所有表的OID生成器的列表，供管理，提取某表的OID生成器提供服务用
     */
    private Hashtable m_hashOIDGenTable = new Hashtable();

    /**
     * 存储所有表信息的列表
     */
    private Hashtable m_hashTableInfo = new Hashtable();

    /**
     * 存储本系统OID基数表,作为以后OID生成的取数表
     */
    public long[] m_nLongMap = { 1000000000000000000L, 100000000000000000L,
            10000000000000000L, 1000000000000000L, 100000000000000L,
            10000000000000L, 1000000000000L, 100000000000L, 10000000000L,
            1000000000L, 100000000L, 10000000L, 1000000L, 100000L, 10000L,
            1000L, 100L, 10L, 1L, };

    /**
     * 存储本系统OID基数,作为以后OID生成的基数
     */
    private long m_nSYSID = 1000000000000000000L;

    public int m_nSysCodeLen = 4;

    public int m_nTableCodeLen = 4;

    /**
     * 存储本系统表id基数,作为以后OID生成的基数
     */
    private long m_nTableID = 100000000000000L;

    private String m_strOidDMImpl = "venus.frames.mainframe.oid.TableInfoDM";

    public static String OID_DB_SRC = "OID_DB";

    public String m_strSysCode = "1000";

    // private String m_strSrvName = null;

    private static boolean m_bIsAllLocalCall = true;

    private Connection connection = null;

    /**
     * 默认构造函数
     *
     * @roseuid 3F94DE69010F
     */
    public OidMgr() {
        super();
    }

    /**
     * Constructor
     *
     * @param connection
     */
    public OidMgr(final Connection connection){
        if (connection!=null){
            this.connection = connection;
        }
    }


    /**
     * 加载配置数据<br>
     *
     * 主要读取系统OID<br>
     *
     * 由于OID组成为： 系统ID4（1000-9222）_表ID4_序列号11。共用十九位数字表示一个主键。<br>
     *
     * 先取得本系统的OID的前4位的基数 the xml conf is : <venus.frames.mainframe.oid.OidMgr
     * SYSID="1000"/>
     *
     * @roseuid 3F94BE1002DE
     */
    private void loadConf() {
        // 读配置文件
        IConfReader dcr = ConfMgr.getConfReader(this.getClass().getName());
        if (dcr == null)
            return;
        try {
            // 获得配置文件中本系统OID的基数
            m_strSysCode = getSYSID(dcr);
            m_nSysCodeLen = m_strSysCode.length();

            if (m_nSysCodeLen < 1 && m_nSysCodeLen < 18)
                throw new NumberFormatException();

            m_nSYSID = Long.parseLong(m_strSysCode)
                    * this.m_nLongMap[m_nSysCodeLen - 1];

            m_bIsAllLocalCall = !dcr.readBooleanAttribute("IsClient");

            m_nTableCodeLen = dcr.readIntAttribute("TableCodeLen");

            m_nTableID = this.m_nLongMap[m_nTableCodeLen + m_nSysCodeLen - 1];

            String tmp_strOidDMImpl = dcr.readStringAttribute("OidDMImpl");
            if (tmp_strOidDMImpl != null && !tmp_strOidDMImpl.equals("")) {
                m_strOidDMImpl = tmp_strOidDMImpl;
            }

        } catch (NumberFormatException e) {
            this.m_nSYSID = 1000000000000000000L;
            LogMgr.getLogger(this).error("OidMgr中的loadConf方法,读取系统OID基数出错！", e);
        } catch (Exception e) {
            this.m_nSYSID = 1000000000000000000L;
            LogMgr.getLogger(this).error("OidMgr中的loadConf方法,读取系统OID基数出错！", e);
        }
    }

    /**
     * 首先获取集群环境下的SYSID，如返回null，再获取conf.xml中配置的SYSID
     * 
     * @param dcr
     * @return
     */
    private String getSYSID(IConfReader dcr) {
        String SYSID = SYSIDClusterUtil.getSID4Cluster();
        if (null == SYSID)
            SYSID = dcr.readStringAttribute("SYSID");
        return SYSID;
    }

    /**
     * 得到服务名称
     * 
     * @return String "OID Management Service"
     * @roseuid 3F94BE1802BF
     */
    public String getServiceName() {
        return "OID Management Service";
    }

    /**
     * 返回服务类型，后台服务
     * 
     * @return int IService.DAEMON_SERVICE
     * @roseuid 3F94BE8F00BC
     */
    public int getServiceType() {
        // 该服务类型由接口IService定义
        return IService.DAEMON_SERVICE;
    }

    /**
     * 停止服务<br>
     * 
     * 将线程的是否运行停止的开关置为FALSE
     * 
     * @roseuid 3F94BE930271
     */
    public void shutdown() {
        m_bRunning = false;

        // if (m_Singleton.isAlive()){
        //		
        // m_Singleton.interrupt();
        //		
        // }
    }

    /**
     * 启动服务<br>
     * 
     * 该方法由任务调度服务启动时新建Thread来启动
     * 
     * @roseuid 3F94BEA30252
     */
    public void startup() {
        m_Singleton = this;
        loadConf();
        if (m_bIsAllLocalCall) {
            loadAllGenerators();

            logger.info("");
            logger.info("------------------------------------------------------------------------");
            logger.info("OID Generator load success.");

            for (Object key : m_hashOIDGenTable.keySet()) {
                if (key!=null && !"".equals(key)){
                    OidGenerator oidGenerator = (OidGenerator)m_hashOIDGenTable.get(key);
                    logger.info("Max(OID): "+ key.toString() + " = " + oidGenerator.getMaxOID().toString());
                }
            }

            logger.info("------------------------------------------------------------------------");
            logger.info("");
        }
    }

    /**
     * 取得默认数据库连接(MAIN)<br>
     * 
     * @return Connection
     * @roseuid 3F94BEE802FF
     */
    public Connection getConnection() {
        Connection result = null;
        try {
            if (this.connection!=null){
                result = this.connection;
            }else {
                //已经没有效果，数据库连接对象已经被重写，老方法无法获取
                result = ConnectionHelper.requestConnection();
            }
        } catch (SQLException e) {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e1) {
                handleException(e1);
            } finally {
                result = null;
            }
            handleException(e);
        }
        return result;
    }

    /**
     * 根据数据源消费者获取数据库连接
     * 
     * @param strDBUser
     * @return
     */
    public Connection getConnection(String strDBUser) {
        Connection result = null;
        try {
            if (this.connection!=null){
                result = this.connection;
            }else {
                //已经没有效果，数据库连接对象已经被重写，老方法无法获取
                result = ConnectionHelper.getConnectionByDS();
            }
        } catch (SQLException e) {
            try {
                if (result != null)
                    result.close();
            } catch (Exception e1) {
                handleException(e1);
            } finally {
                result = null;

            }
            handleException(e);
        }
        return result;
    }

    public void loadGenerator(String tblName) {
        String tableName = tblName.toLowerCase();
        if (m_hashTableInfo.containsKey(tableName)) {
            TableInfoEO teo = (TableInfoEO) m_hashTableInfo.get(tableName);
            Connection con = null;
            ITableInfoDM dmImpl2;
            Class c;
            try {
                c = ClassLocator.loadClass(m_strOidDMImpl);
                dmImpl2 = (ITableInfoDM) c.newInstance();
            } catch (ClassNotFoundException cnfe) {
                LogMgr.getLogger(this.getClass().getName()).error(
                        "loadGenerator(...): ClassNotFoundException", cnfe);
                return;
            } catch (IllegalAccessException iae) {
                LogMgr.getLogger(this.getClass().getName()).error(
                        "loadGenerator(...): IllegalAccessException", iae);
                return;
            } catch (InstantiationException ie) {
                LogMgr.getLogger(this.getClass().getName()).error(
                        "loadGenerator(...): InstantiationException", ie);
                return;
            }

            try {
                // 取得某个表的最大OID值

                con = getConnection(teo.getOidDBSrc());
                // con = getConnection();
                dmImpl2.setConnection(con);
                OID oidMax = dmImpl2.getMaxOIDOfTable(teo);
                OidGenerator oidGen = new OidGenerator(oidMax);
                if (m_hashOIDGenTable.containsKey(tableName))
                    m_hashOIDGenTable.remove(tableName);

                m_hashOIDGenTable.put(tableName, oidGen);
            } catch (SQLException e) {
                // 对任何表OID装入的错误，不影响其他表OID的装入
                LogMgr.getLogger(this.getClass().getName()).error(
                        "loadGenerator(...): getMaxOIDOfTable in table : "
                                + tableName, e);
                /*
                 * if ( !m_hashOIDGenTable.containsKey(tableName) ){
                 * m_hashOIDGenTable.remove(tableName); }
                 */

            } catch (Exception e) {
                LogMgr.getLogger(this.getClass().getName()).error(
                        "loadGenerator(...): getMaxOIDOfTable in table : "
                                + tableName, e);
                // 对任何表OID装入的错误，不影响其他表OID的装入
                handleException(e);
                /*
                 * if ( !m_hashOIDGenTable.containsKey(tableName) ){
                 * m_hashOIDGenTable.remove(tableName); }
                 */
            } finally {
                try {
                    if (con != null)
                        con.close();
                } catch (SQLException e) {
                    handleException(e);
                }

            }
        }
    }

    /**
     * 1.得到所有表的list TableInfoEO[]，并存在m_hashTableInfo中，以表名为键值<br>
     * 2.根据这个列表构建每张表的OID生成器，并存在m_hashOIDTable中，以表名为键值<br>
     * 
     * @roseuid 3F94BF86007F
     */
    public void loadAllGenerators() {
        Connection conn = null;
        // 取得表的信息，并根据此为每个表构建一个OID生成器
        try {
            if ("venus.frames.mainframe.oid.TableInfoDM".equals(m_strOidDMImpl))
                conn = getConnection(OID_DB_SRC);
            // conn = getConnection();
            Hashtable conTable = new Hashtable();
            try {
                /*
                 * //存放OID生成器的哈希表 if (m_hashOIDGenTable == null) {
                 * m_hashOIDGenTable = new Hashtable(); } //加载所有的表信息以及相对应的OID生成器
                 * if (m_hashTableInfo == null) { m_hashTableInfo = new
                 * Hashtable(); }
                 * 
                 * if (m_hashOidSrvTable == null) { m_hashOidSrvTable = new
                 * Hashtable(); }
                 */

                Class c;
                ITableInfoDM dmImpl;
                try {
                    c = ClassLocator.loadClass(m_strOidDMImpl);
                    dmImpl = (ITableInfoDM) c.newInstance();
                } catch (ClassNotFoundException cnfe) {
                    LogMgr.getLogger(this.getClass().getName()).error(
                            "loadAllGenerators(...): ClassNotFoundException",
                            cnfe);
                    return;
                } catch (IllegalAccessException iae) {
                    LogMgr.getLogger(this.getClass().getName()).error(
                            "loadAllGenerators(...): IllegalAccessException",
                            iae);
                    return;
                } catch (InstantiationException ie) {
                    LogMgr.getLogger(this.getClass().getName()).error(
                            "loadAllGenerators(...): InstantiationException",
                            ie);
                    return;
                }

                dmImpl.setConnection(conn);
                TableInfoEO[] tis = dmImpl.getAllTableInfos();

                int length = tis.length;

                for (int i = 0; i < length; i++) {
                    TableInfoEO teo = tis[i];
                    String aTableName = teo.getTableName().toLowerCase();
                    m_hashTableInfo.put(aTableName, teo);

                    Connection con = null;

                    ITableInfoDM dmImpl2;
                    try {
                        if (conTable.containsKey(teo.getOidDBSrc()))
                            con = (Connection) conTable.get(teo.getOidDBSrc());

                        if (con == null || con.isClosed()) {
                            con = getConnection(teo.getOidDBSrc());

                            if (con == null || con.isClosed()) {
                                LogMgr.getLogger(this).debug("error in getMaxOIDOfTable in table : " + aTableName + " , Pls check oid config");
                                continue;
                            }

                            if (conTable.containsKey(teo.getOidDBSrc()))
                                conTable.remove(teo.getOidDBSrc());

                            conTable.put(teo.getOidDBSrc(), con);
                        }

                        // 取得某个表的最大OID值
                        dmImpl2 = (ITableInfoDM) c.newInstance();

                        // con = getConnection();
                        dmImpl2.setConnection(con);
                        OID oidMax = dmImpl2.getMaxOIDOfTable(teo);
                        OidGenerator oidGen = new OidGenerator(oidMax);
                        if (m_hashOIDGenTable.containsKey(aTableName))
                            m_hashOIDGenTable.remove(aTableName);

                        m_hashOIDGenTable.put(aTableName, oidGen);

//                        LogMgr.getLogger(this).debug("oid :" + aTableName + " :" + oidMax.longValue());

                    } catch (SQLException e) {
                        // 对任何表OID装入的错误，不影响其他表OID的装入
                        LogMgr.getLogger(this.getClass().getName()).error("error in loadAllGenerators(...): getMaxOIDOfTable in table : " + aTableName, e);
                    } catch (Exception e) {
                        LogMgr.getLogger(this.getClass().getName()).error("error in loadAllGenerators(...): getMaxOIDOfTable in table : " + aTableName, e);
                        if (con != null)
                            con.close();
                        conTable.remove(teo.getOidDBSrc());
                        // 对任何表OID装入的错误，不影响其他表OID的装入
                    }

                }

                // }
                // conn.commit();
            } finally {
                if (conn != null)
                    conn.close();

                if (conTable.size() > 0) {

                    Enumeration enu = conTable.elements();
                    Connection n = null;
                    while (enu.hasMoreElements()) {
                        try {
                            n = (Connection) enu.nextElement();
                            if (n != null) {
                                n.close();
                                n = null;
                            }
                        } finally {
                            if (n != null) {
                                n.close();
                                n = null;
                            }
                        }

                    }
                    conTable.clear();
                    conTable = null;

                }
            }

        } catch (SQLException e) {
            handleException(e);
        } catch (Exception e1) {
            handleException(e1);
        }
    }

    /**
     * 暂时将错误处理交给 BaseBusinessExceptionHandler处理
     * 
     * @param e
     *            异常类
     * @roseuid 3F94C07D03CC
     */
    public void handleException(Throwable e) {

        // LogMgr.getLogger(this).error("handleException(Throwable e):"+e);
    }

    /**
     * 先得到该表的生成器然后得到生成oid
     * 
     * @param tableName
     *            要请求获得OID的表名
     * @return venus.pub.lang.OID 该表新的最大的OID对象
     * @roseuid 3F94C0EF0294
     */
    public static OID requestOID(String tableName) {// synchronized
        // 处理参数为空的情况
        if (tableName == null) {
            LogMgr.getLogger("venus.frames.mainframe.oid.OidMgr").error("OidMgr中的requestOID方法,参数空指针异常！");
            return null;
        }
        OID result = null;
        if (m_bIsAllLocalCall) {
            result = getSingleton().getOID(tableName);
        } else {
            IOidBS oidbs = (IOidBS) BeanFactoryHolder.getBeanFactory().getBean(OID_BS_NAME);
            result = oidbs.requestOID(tableName);
        }

        return result;
    }

    public OID getOID(String tableName) {
        OID result = null;
        try {
            result = getSingleton().getOIDGenerator(tableName).requestOID();
        } catch (NullPointerException e) {
            LogMgr.getLogger("venus.frames.mainframe.oid.OidMgr").error(
                    "在requestOID方法中不能取得OID值，请检查表名 " + tableName
                            + " 是否正确并重新启动线程获取OID值！", e);
            handleException(e);
        }
        return result;
    }

    public OID[] getOIDArray(String tableName, int len) {
        OID[] result = null;
        try {
            OidGenerator gen = getSingleton().getOIDGenerator(tableName);

            if (len > 0) {
                result = (OID[]) new OID[len];
            } else {
                return null;
            }

            for (int j = 0; j < len; j++) {
                result[j] = gen.requestOID();
            }

        } catch (NullPointerException e) {
            LogMgr.getLogger("venus.frames.mainframe.oid.OidMgr").error(
                    "在requestOID方法中不能取得OID值，请检查表名 " + tableName
                            + " 是否正确并重新启动线程获取OID值！", e);
            handleException(e);
        }
        return result;
    }

    /**
     * 先得到该表的生成器然后得到生成oid
     * 
     * @param tableName
     *            要请求获得OID的表名
     * @param tableName
     *            要请求获得OID的个数
     * @return venus.pub.lang.OID[] 该表新的最大的OID对象组
     * @roseuid 3F94C0EF0294
     */
    public static OID[] requestOIDArray(String tableName, Integer len) {// synchronized
        // 处理参数为空的情况
        if (tableName == null || len == null) {
            LogMgr.getLogger("venus.frames.mainframe.oid.OidMgr").error(
                    "OidMgr中的requestOID方法,参数空指针异常！");
            return null;
        }

        return requestOIDArray(tableName, len.intValue());
    }

    /**
     * 先得到该表的生成器然后得到生成oid
     * 
     * @param tableName
     *            要请求获得OID的表名
     * @param tableName
     *            要请求获得OID的个数
     * @return venus.pub.lang.OID[] 该表新的最大的OID对象组
     * @roseuid 3F94C0EF0294
     */
    public static OID[] requestOIDArray(String tableName, int len) {// synchronized
        // 处理参数为空的情况
        if (tableName == null) {
            LogMgr.getLogger("venus.frames.mainframe.oid.OidMgr").error(
                    "OidMgr中的requestOID方法,参数空指针异常！");
            return null;
        }
        OID[] result = null;
        if (m_bIsAllLocalCall) {

            result = getSingleton().getOIDArray(tableName, len);
        } else {
            IOidBS oidbs = (IOidBS) BeanFactoryHolder.getBeanFactory().getBean(OID_BS_NAME);
            result = oidbs.requestOIDArray(tableName, len);
        }

        return result;
    }

    /**
     * 维持线程运行状态
     * 
     * @roseuid 3F94C24F02C6
     */
    // public void run() {
    // //每隔一秒监听一次
    // while (m_bRunning) {
    // try {
    // sleep(1000);
    // } catch (InterruptedException e) {
    // handleException(e);
    // }
    // }
    // }
    /**
     * 得到系统OID,作为以后OID生成的基数<br>
     * 
     * 此静态方法为方法getSYSID()的静态代理方法
     * 
     * @return long 系统ID
     * @roseuid 3F94D4E8014E
     */
    public static long getSysPrefix() {
        return getSingleton().getSYSID();
    }

    /**
     * 得到系统Code<br>
     * 
     * 此静态方法为方法getSYSCode()的静态代理方法
     * 
     * @return long 系统ID
     * @roseuid 3F94D4E8014E
     */
    public static String getSYSCode() {
        return getSingleton().getSysCode();
    }

    /**
     * 得到表ID基数,作为以后OID生成的基数<br>
     * 
     * OID组成为： 系统ID4（1000-9222）_表ID4_序列号11。共用十九位数字表示一个主键。
     * 
     * @return long 系统ID
     * @roseuid 3F950EB00284
     */
    public static long getTablePrefix() {
        return getSingleton().getTableID();
    }

    public static boolean checkTableCode(String tableCode) {
        // 简单位数检查
        int tableCodeLen = tableCode.length();

        if (getSingleton().m_nTableCodeLen != tableCodeLen)
            return false;

        return true;
    }

    /**
     * 通过此静态方法获得该类的全局单一实例<br>
     * 
     * 如果该实例未构建则构建初始化并加载配置数据<br>
     * 
     * 强烈建议用户使用该类的时候利用该方法取得实例，避免使用new方法新建一个实例
     * 
     * @return venus.frames.mainframe.oid.OidMgr
     * @roseuid 3F94DCDA02C2
     */
    public static OidMgr getSingleton() {
        if (m_Singleton == null) {
            OidMgr a = new OidMgr();
            a.startup();
            // m_Singleton.loadConf();
            // m_Singleton.start();
        }
        return m_Singleton;
    }

    /**
     * 得到系统OID基数,作为以后OID生成的基数<br>
     * 
     * OID组成为： 系统ID4（1000-9222）_表ID4_序列号11。共用十九位数字表示一个主键。
     * 
     * @return long 系统ID
     * @roseuid 3F950EB00284
     */
    public long getSYSID() {
        return m_nSYSID;
    }

    public String getSysCode() {
        return m_strSysCode;
    }

    /**
     * 得到表ID基数,作为以后OID生成的基数<br>
     * 
     * OID组成为： 系统ID4（1000-9222）_表ID4_序列号11。共用十九位数字表示一个主键。
     * 
     * @return long 系统ID
     * @roseuid 3F950EB00284
     */
    public long getTableID() {
        return m_nTableID;
    }

    /**
     * 根据传入的表名得到该表的OID生成器
     *
     * @param tblName
     * @return OidGenerator 该表对应的OID生成器
     */
    public OidGenerator getOIDGenerator(String tblName) {
        // 处理参数为空的情况
        if (tblName == null) {
            LogMgr.getLogger(this).error("OidMgr中的getOIGenerator方法,参数空指针异常！");
            return null;
        }
        // 取得当前表OID的最大值
        String tableName = tblName.toLowerCase();
        try {
            if (m_hashOIDGenTable.containsKey(tableName))
                return (OidGenerator) (m_hashOIDGenTable.get(tableName));

            loadGenerator(tableName);
            return (OidGenerator) (m_hashOIDGenTable.get(tableName));
        } catch (NullPointerException e) {
            handleException(e);
            LogMgr.getLogger(this).error(
                    "OidMgr中的getOIDGenerator方法,未取得表 " + tableName
                            + " 相应的OID生成器！", e);
            return null;
        }
    }
    
    /**
     * 根据表名判断是否为该表配置了OID。
     * @param tableName
     * @return 已配置，则返回true，反之返回ture
     */
    public static boolean hasOIDTable(String tableName){
    	// 处理参数为空的情况
        if (tableName == null) {
            LogMgr.getLogger("venus.frames.mainframe.oid.OIDMgr").info("OidMgr中的hasOIDTable方法参数为空！");
            return false;
        }
        tableName = tableName.toLowerCase();
        if (getSingleton().m_hashOIDGenTable.containsKey(tableName))
        	return true;
    	return false;
    	
    }

}
