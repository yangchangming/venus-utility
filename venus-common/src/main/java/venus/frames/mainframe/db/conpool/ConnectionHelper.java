package venus.frames.mainframe.db.conpool;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import venus.VenusHelper;
import venus.frames.jdbc.datasource.ConfDataSource;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.ClassLocator;
import venus.frames.mainframe.util.DefaultConfReader;
import venus.frames.mainframe.util.IConfReader;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * DB连接的辅助类，帮助程序员以最简单的方式获取DB连接<br>
 */
public class ConnectionHelper {

    //private static Hashtable m_hashConnInfo = null;
    
	private static IConfReader m_oConfRead = null;
	
	private static IConfReader m_oDefaultConfRead = null;
    
	//private static boolean m_bUseProvider = false;
    
	private static boolean m_bLoadConfed = false;
	
	private static boolean m_bUseDefault = false;
	
	//add by chjq for multi-datasource
	private static boolean m_bResetConnection = false;
	//end 
	
	//private static String m_strConProvider = null;
	
	private static IConProvider m_oDefaultConProvider = null;
	
	private static Hashtable m_hashConnSrc = null;
	
	private static Hashtable m_hashDBUser = null;
	
	private static Hashtable m_hashDBList = null;
	
	private static boolean m_bUseMain = false;
	
	private static IConProvider[] m_arySrc;
	
	private static IConfReader[] m_aryIcr;
	
	public static final String MAIN_DB_SRC = "MAIN";
	
	public final static String DATASOURCE = "dataSource";
	
	private static Hashtable m_hashConnInfos = new Hashtable();
	
	/**
	 * 此方法为辅助方法, 获取默认的数据源链接  
	 * 即db.xml中<DB_SRC name="MAIN" .../>指定的数据源的连接
	 *
	 * @return java.sql.Connection - 数据库连接
	 * @throws  SQLException Sql异常
	 */
	public static Connection requestConnection() throws SQLException {

		if ( m_oDefaultConProvider!=null){
			if ( m_bResetConnection ){
				m_oDefaultConProvider.reset();
				m_bResetConnection = false;
			}
			return m_oDefaultConProvider.requestConnection(m_oDefaultConfRead);
		}

		if ( !m_bLoadConfed ) {
			loadConf();
		}

		if ( m_bUseDefault ) {

			return DefaultConnection.requestConnectionDefault();
		}

		if ( m_oDefaultConProvider == null ){

			return DefaultConnection.requestConnectionByMs(m_oDefaultConfRead);

		}else{

			return m_oDefaultConProvider.requestConnection(m_oDefaultConfRead);

		}


	}

	/**
	 *
	 * 通过该方法获取指定名称（消费字）的数据库连接, 为ConfDataSource服务
	 * 该消费字在db.xml中定义<DB_USER name="TOOLS" src="TOOLS_DB_SRC"/>中的name
	 *
	 * @param m_strDBUsr
	 * @return java.sql.Connection - 数据库连接
	 * @throws  SQLException Sql异常
	 */
	public static Connection requestConnection(String m_strDBUsr) throws SQLException {

		if(m_strDBUsr==null){
			if (m_bUseMain){
				return requestConnection();
			}else{
				return null;
			}
		}else if( ConfDataSource.getCurrentUsr() != null ){
			m_strDBUsr = ConfDataSource.getCurrentUsr();
		}
		
		if(m_strDBUsr.equals(MAIN_DB_SRC)){
			return requestConnection();
			
		}else{
			if ( !m_bLoadConfed ) {
				loadConf();
			} 
				
			if (m_hashDBList.containsKey(m_strDBUsr)){
				int n = ((Integer)(m_hashDBList.get(m_strDBUsr))).intValue();
				IConProvider icp = m_arySrc[n];
				if(icp==null){
					return DefaultConnection.requestConnectionByMs(m_aryIcr[n]);
				}else{
					if ( m_bResetConnection ){
						icp.reset();
						m_bResetConnection = false;
					}
					return icp.requestConnection(m_aryIcr[n]);
				}		
			
			}else{
			
				if (m_bUseMain){
					return requestConnection();
				}else{
					return null;
				}
			
			}
		
		}	

	}
	
	/**
	 * 对于需要多数据源切换的应用系统，
	 * 第三方组件(quartz,report..)必须使用该方法获取数据源.
	 * 否则无法实现特切换
	 * @return Connection
	 * @throws SQLException
	 */
	public static Connection getConnectionByDS() throws SQLException{
		ConfDataSource ds = (ConfDataSource) VenusHelper.getBean(DATASOURCE);
		return ds.getConnection();
	}
	

		
	private static void loadConf() { 
		m_oConfRead = venus.frames.mainframe.util.ConfMgr.getConfReader(ConnectionHelper.class.getName());
		m_bLoadConfed = true;
		m_bUseMain = m_oConfRead.readBooleanAttribute("useMain");
		loadDBSrcFrmConf(m_oConfRead);
	}
	
	private static boolean useDefault(IConfReader icr,int num){
	
		if ( icr == null){
			m_arySrc[num] = null;
			return true;
		} 
		boolean useProvider = icr.readBooleanAttribute("useProvider");
		if (useProvider){
			String strConProvider = icr.readStringAttribute("conProvider");
			if ( strConProvider == null ){
					m_arySrc[num] = null;					
					return true;			
			}else{
					IConProvider conProvider;
					try {
						Class c = ClassLocator.loadClass(strConProvider);
						conProvider = (IConProvider)c.newInstance();
					} catch (ClassNotFoundException cnfe) {
						LogMgr.getLogger(ConnectionHelper.class.getName()).error("useDefault(...): ClassNotFoundException", cnfe);
						m_arySrc[num] = null;
						return true;	
					} catch (IllegalAccessException iae) {
						LogMgr.getLogger(ConnectionHelper.class.getName()).error("useDefault(...): IllegalAccessException", iae);
						m_arySrc[num] = null;
						return true;	
					} catch (InstantiationException ie) {
						LogMgr.getLogger(ConnectionHelper.class.getName()).error("useDefault(...): InstantiationException", ie);
						m_arySrc[num] = null;
						return true;	
					}	
					m_arySrc[num] = conProvider;
					return false;		
			}	
		}else{
			m_arySrc[num] = null;
			return true;
		}
		
	}
	
	private static void loadDBSrcFrmConf(IConfReader icr){
		
		ArrayList srcAryLst = icr.readChildNodesAry("DB_SRC");
		int len = srcAryLst.size();
		
		m_arySrc = new IConProvider[len];
		m_aryIcr = new IConfReader[len];
		m_hashConnSrc = new Hashtable(len);	
		
		//将DB信息存到数组中
		for (int i = 0; i < len; i++) {
			Node tmpNode = (Node)srcAryLst.get(i);
			//取信息
			NamedNodeMap tmpNodeMap = tmpNode.getAttributes();
			Node nameNode = tmpNodeMap.getNamedItem("name");
			String name = nameNode.getNodeValue();
			m_aryIcr[i] = new DefaultConfReader(tmpNode);


			boolean m_bUseDefault = useDefault(m_aryIcr[i],i) ;
			
			if( name.equals(MAIN_DB_SRC) ){
				m_oDefaultConfRead = m_aryIcr[i];
				m_oDefaultConProvider = m_arySrc[i];
			}		
			
			//将信息存入m_hashConnSrc
			m_hashConnSrc.put(name,new Integer(i));
			
		}
		
	    ArrayList usrAryLst = icr.readChildNodesAry("DB_USER");
	    int n_len = usrAryLst.size();
		
		m_hashDBUser = new Hashtable(n_len);
		m_hashDBList = new Hashtable(n_len); 
		
		//将信息存到数组中
		for (int i = 0; i < n_len; i++) {
			Node tmpNode = (Node)usrAryLst.get(i);
			//取信息
			NamedNodeMap tmpNodeMap = tmpNode.getAttributes();
			Node nameNode = tmpNodeMap.getNamedItem("name");
			Node srcNode = tmpNodeMap.getNamedItem("src");
			String dbUsr = nameNode.getNodeValue();
			String dbSrc = srcNode.getNodeValue();
			
			//将信息存入m_hashConnUser
			m_hashDBUser.put(dbUsr,dbSrc);
			
			
			if(m_hashConnSrc.containsKey(dbSrc)){
				m_hashDBList.put(dbUsr,m_hashConnSrc.get(dbSrc));			
			}else{
				m_hashDBList.put(dbUsr,m_hashConnSrc.get(MAIN_DB_SRC));			
			}						
		}	
	}
	
	/**
	 * It means that connection should be resetted 
	 * before invoking requestConnection operation.
	 * 
	 */
	public static void resetConnection(){
		m_bResetConnection = true;
	}
		
		
//	/**
//	 * 通过weblogic方式获得数据库连接
//	 * @return Connection 数据库连接
//	 */
//	private static Connection getConnection() throws SQLException {
//		try {
//			ClassLocator.loadClass("weblogic.jdbc.mssqlserver4.Driver");
//		} catch (ClassNotFoundException e) {
//			throw new SQLException("db_connect:class not fount exception");
//		}
//		return DriverManager.getConnection(
//			"jdbc:weblogic:mssqlserver4:10.64.1.109:1433?db=venus;weblogic.codeset=GBK",
//			"sa",
//			"chjq");
//	}
	
	static Hashtable getConnInfos() {
	
			return m_hashConnInfos;
	
	}
	
	static void addConnInfo(IConfReader icr,Hashtable m_hashConnInfo) {
	
			m_hashConnInfos.put(icr,m_hashConnInfo);
	
	}
}
