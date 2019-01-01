package venus.frames.mainframe.db.conpool;

import net.sourceforge.jtds.jdbcx.JtdsDataSource;
import venus.frames.mainframe.util.IConfReader;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

/**
 * @author wujun
 *
 * 更改所生成类型注释的模板为
 */
public class DefaultConnection {


		static Connection requestConnectionDefault() throws SQLException {
				try {
					Class.forName("net.sourceforge.jtds.jdbc.Driver");
				} catch (ClassNotFoundException ex) {
					ex.printStackTrace();
				}
				JtdsDataSource ds = new JtdsDataSource();
				ds.setServerName("ufre");
				ds.setPortNumber(4336);
				ds.setUser("venus");
				ds.setPassword("venus");
				ds.setDatabaseName("venus");
				return ds.getConnection();
		}
	
	
		static Connection requestConnectionByMs(IConfReader icr) throws SQLException {
    
				String drvName = "net.sourceforge.jtds.jdbc.Driver";
				String srvName = "ufre";
				String port = "1433";
				String usrName = "venus";
				String pwd = "venus";
				String databaseName = "venus";
		    
				Hashtable m_hashConnInfo;
		    
			    Hashtable m_hashConnInfos = ConnectionHelper.getConnInfos();
		    
				if (m_hashConnInfos.containsKey(icr)){
		    
					m_hashConnInfo = (Hashtable)m_hashConnInfos.get(icr);
		    
					if ( m_hashConnInfo.containsKey("drvName") ){
						drvName = (String)m_hashConnInfo.get("drvName");
						srvName = (String)m_hashConnInfo.get("srvName");
						port = (String)m_hashConnInfo.get("port");
						usrName = (String)m_hashConnInfo.get("usrName");
						pwd = (String)m_hashConnInfo.get("pwd");
						databaseName = (String)m_hashConnInfo.get("databaseName");
						    
					}else {
						drvName = icr.readStringAttribute("drvName");
						srvName = icr.readStringAttribute("srvName");
						port = icr.readStringAttribute("port");
						usrName = icr.readStringAttribute("usrName");
						pwd = icr.readStringAttribute("pwd");
						databaseName = icr.readStringAttribute("databaseName");
						
						m_hashConnInfo = new Hashtable(7);	
						
						m_hashConnInfo.put("drvName",drvName);
						m_hashConnInfo.put("srvName",srvName);
						m_hashConnInfo.put("port",port);
						m_hashConnInfo.put("usrName",usrName);
						m_hashConnInfo.put("pwd",pwd);
						m_hashConnInfo.put("databaseName",databaseName);
					
						//m_hashConnInfos.put(icr,m_hashConnInfo);
						ConnectionHelper.addConnInfo(icr,m_hashConnInfo);
					   
					}
				}else{		    

					drvName = icr.readStringAttribute("drvName");
					srvName = icr.readStringAttribute("srvName");
					port = icr.readStringAttribute("port");
					usrName = icr.readStringAttribute("usrName");
					pwd = icr.readStringAttribute("pwd");
					databaseName = icr.readStringAttribute("databaseName");
							
					m_hashConnInfo = new Hashtable(7);	
					m_hashConnInfo.put("drvName",drvName);
					m_hashConnInfo.put("srvName",srvName);
					m_hashConnInfo.put("port",port);
					m_hashConnInfo.put("usrName",usrName);
					m_hashConnInfo.put("pwd",pwd);
					m_hashConnInfo.put("databaseName",databaseName);
				
					m_hashConnInfos.put(icr,m_hashConnInfo);
		    
				}
		    

			
				try {
					Class.forName(drvName);
				} catch (ClassNotFoundException ex) {
					ex.printStackTrace();
				}
				JtdsDataSource ds = new JtdsDataSource();
				ds.setServerName(srvName);
				ds.setPortNumber(Integer.parseInt(port));
				ds.setUser(usrName);
				ds.setPassword(pwd);
				ds.setDatabaseName(databaseName);
				return ds.getConnection();
		}
}
