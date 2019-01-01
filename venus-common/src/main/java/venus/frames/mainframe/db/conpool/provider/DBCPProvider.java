package venus.frames.mainframe.db.conpool.provider;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import venus.frames.mainframe.db.conpool.IConProvider;
import venus.frames.mainframe.util.ClassLocator;
import venus.frames.mainframe.util.IConfReader;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

/**
 * default config：
 * The default "when exhausted action" for the pool：block(wait) <br/>
 * The default maximum amount of time (in millis) method should block before throwing<br/>
 * an exception when the pool is exhausted and the ：DEFAULT_MAX_WAIT = -1L;<br/>
 * The default "test on borrow" value：false<br/>
 * The default "test on return" value: false<br/>
 * The default "test while idle" value: false<br/>
 * The default "time between eviction runs" value: DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = -1L;<br/>
 * The default number of objects to examine per run in the
 * idle object evictor:DEFAULT_NUM_TESTS_PER_EVICTION_RUN = 3;
 * The default value for {@link #getMinEvictableIdleTimeMillis}:DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS = 1000L * 60L * 30L;
 */
public class DBCPProvider implements IConProvider
{

  private static Hashtable m_hashPool = null;

  private String m_strConnectURI;
  private String m_strUName;
  private String m_strPasswd;
  private int m_nMaxActive;
  private String m_strDrvName;


  public Connection requestConnection(IConfReader icr) throws SQLException{

      if ( m_strDrvName== null ){
           m_strDrvName = icr.readStringAttribute("drvName");
           m_strConnectURI = icr.readStringAttribute("conUrl");
           m_strUName = icr.readStringAttribute("usrName");
           m_strPasswd = icr.readStringAttribute("pwd");
           m_nMaxActive = icr.readIntAttribute("maxActive");
       try {
           ClassLocator.loadClass(m_strDrvName);
       } catch (ClassNotFoundException e) {
           throw new SQLException("DBCPProvider:driver class not fount exception:"+m_strDrvName);
       }

      }



   PoolingDataSource pds = getDataSource(m_strConnectURI, m_strUName,m_strPasswd,m_nMaxActive);

   if ( pds==null ) return null;

   return pds.getConnection();


   //getDataSource(m_hashConnInfo).getConnection();


}

/*
   public static PoolingDataSource getDataSource(Hashtable ht){
           if ( m_hashPool == null ) m_hashPool = new Hashtable();
           String connectURI = (String)ht.get("conUrl");
           String uname = (String)ht.get("usrName");
           String passwd = (String)ht.get("pwd");
           int maxActive = ((Integer)ht.get("maxActive")).intValue();

           if ( m_hashPool.containsKey(connectURI) ) {
               return (PoolingDataSource)m_hashPool.get(connectURI);

           }else{
               PoolingDataSource datasource = setupDataSource(connectURI,uname, passwd,maxActive);

               m_hashPool.put(connectURI,datasource);

               return datasource;

           }

   }
*/

   public static PoolingDataSource getDataSource(String connectURI, String uname, String passwd,int maxActive){
           if ( m_hashPool == null ) m_hashPool = new Hashtable();

           String key = connectURI+";usr="+uname+";pwd="+passwd ;

           if ( m_hashPool.containsKey(key) ) {
               return (PoolingDataSource)m_hashPool.get(key);

           }else{
               PoolingDataSource datasource = setupDataSource(connectURI,uname, passwd,maxActive);

               m_hashPool.put(key,datasource);

               return datasource;

           }

   }


   private static PoolingDataSource setupDataSource(String connectURI, String uname, String passwd,int maxActive) {
       //
       // First, we'll need a ObjectPool that serves as the
       // actual pool of connections.
       //
       // We'll use a GenericObjectPool instance, although
       // any ObjectPool implementation will suffice.
       //
       ObjectPool connectionPool = new GenericObjectPool(null,maxActive);

       //
       // Next, we'll create a ConnectionFactory that the
       // pool will use to create Connections.
       // We'll use the DriverManagerConnectionFactory,
       // using the connect string passed in the command line
       // arguments.
       //
       ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, uname, passwd);

       //
       // Now we'll create the PoolableConnectionFactory, which wraps
       // the "real" Connections created by the ConnectionFactory with
       // the classes that implement the pooling functionality.
       //
       PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);

       //
       // Finally, we create the PoolingDriver itself,
       // passing in the object pool we created.
       //
       PoolingDataSource dataSource = new PoolingDataSource(connectionPool);

       return dataSource;
   }

   /* （非 Javadoc）
    * @see venus.frames.mainframe.db.conpool.IConProvider#reset()
    */
   public void reset() {
       // TODO 自动生成方法存根

   }


}