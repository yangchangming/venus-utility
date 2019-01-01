package venus.frames.mainframe.db.conpool.provider;

import venus.frames.mainframe.db.conpool.IConProvider;
import venus.frames.mainframe.util.ClassLocator;
import venus.frames.mainframe.util.IConfReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBCPProviderWithJOCL implements IConProvider
{
   private String m_strConURI ;
   private String m_strDrvName;
   public static String DBCP_POOL_DRIVER_STR = "org.apache.commons.dbcp.PoolingDriver";
   
   public Connection requestConnection(IConfReader icr) throws SQLException{
	
        if ( m_strConURI == null ){
		m_strConURI = icr.readStringAttribute("conUrl");
		m_strDrvName = icr.readStringAttribute("drvName");
		//System.setProperty("org.xml.sax.driver","org.apache.xerces.parsers.SAXParser");
		try {
			ClassLocator.loadClass(DBCP_POOL_DRIVER_STR);
			ClassLocator.loadClass(m_strDrvName);
			//System.setProperty("jdbc.drivers",m_strDrvName+":"+DBCP_POOL_DRIVER_STR);
			//venus.frames.mainframe.log.LogMgr.getLogger(this).debug("jdbc.drivers="+System.getProperty("jdbc.drivers"));//jdbc.drivers//=oracle.jdbc.driver.OracleDriver:org.apache.commons.dbcp.PoolingDriver
		} catch (ClassNotFoundException e) {
			throw new SQLException("db_drv:class not fount exception "+m_strDrvName);
		}	    
	}	
	
	return DriverManager.getConnection(m_strConURI);
	
   }

/* （非 Javadoc）
 * @see venus.frames.mainframe.db.conpool.IConProvider#reset()
 */
public void reset() {
	// TODO 自动生成方法存根
	
}
   
}