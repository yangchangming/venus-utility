package venus.frames.mainframe.db.conpool.provider;

import venus.frames.mainframe.db.conpool.IConProvider;
import venus.frames.mainframe.util.IConfReader;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;

/**
 * 针对WebLogic连接池的处理.
 * 
 * @author huchangcheng
 * 
 */
public class WeblogicPoolInsideProvider implements IConProvider {
	Hashtable m_hashConnInfo = new Hashtable();

	public Connection requestConnection(IConfReader icr) throws SQLException {
		String jndiFactoryName = "";//"weblogic.jndi.WLInitialContextFactory";
		String providerUrl = "";//"t3://127.0.0.1:7001";
		String poolJndiName = "";//"venusPool";
		

		if (m_hashConnInfo.containsKey("drvName")) {
			jndiFactoryName = (String) m_hashConnInfo.get("jndiFactoryName");
			providerUrl = (String) m_hashConnInfo.get("providerUrl");
			poolJndiName = (String) m_hashConnInfo.get("poolJndiName");
			

		} else {
			jndiFactoryName = icr.readStringAttribute("jndiFactoryName");
			providerUrl = icr.readStringAttribute("providerUrl");
			poolJndiName = icr.readStringAttribute("poolJndiName");
			

			m_hashConnInfo.put("jndiFactoryName", jndiFactoryName);
			m_hashConnInfo.put("providerUrl", providerUrl);
			m_hashConnInfo.put("poolJndiName", poolJndiName);
			

		}

		Context ctx = null;
		Connection con = null;
		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, jndiFactoryName);
		env.put(Context.PROVIDER_URL, providerUrl);
		
		try {
			ctx = new InitialContext(env);
			DataSource ds = (DataSource) ctx.lookup(poolJndiName);
			con = ds.getConnection();
			return con;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/* （非 Javadoc）
	 * @see venus.frames.mainframe.db.conpool.IConProvider#reset()
	 */
	public void reset() {
		// TODO 自动生成方法存根
		
	}
}