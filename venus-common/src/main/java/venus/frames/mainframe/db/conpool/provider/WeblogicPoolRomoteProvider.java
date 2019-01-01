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
public class WeblogicPoolRomoteProvider implements IConProvider {
	Hashtable m_hashConnInfo = new Hashtable();

	public Connection requestConnection(IConfReader icr) throws SQLException {
		String jndiFactoryName = "";//"weblogic.jndi.WLInitialContextFactory";
		String providerUrl = "";//"t3://127.0.0.1:7001";
		String poolJndiName = "";//"venusPool";
		String security_user = "";
		String security_pwd = "";

		if (m_hashConnInfo.containsKey("drvName")) {
			jndiFactoryName = (String) m_hashConnInfo.get("jndiFactoryName");
			providerUrl = (String) m_hashConnInfo.get("providerUrl");
			poolJndiName = (String) m_hashConnInfo.get("poolJndiName");
			security_user = (String) m_hashConnInfo.get("securityPrincipal"); //访问服务器的用户名
			security_pwd = (String) m_hashConnInfo.get("securityCredentials");//访问服务器的密码

		} else {
			jndiFactoryName = icr.readStringAttribute("jndiFactoryName");
			providerUrl = icr.readStringAttribute("providerUrl");
			poolJndiName = icr.readStringAttribute("poolJndiName");
			security_user = icr.readStringAttribute("securityPrincipal");
			security_pwd = icr.readStringAttribute("securityCredentials");

			m_hashConnInfo.put("jndiFactoryName", jndiFactoryName);
			m_hashConnInfo.put("providerUrl", providerUrl);
			m_hashConnInfo.put("poolJndiName", poolJndiName);
			m_hashConnInfo.put("security_user", security_user);
			m_hashConnInfo.put("security_pwd", security_pwd);

		}

		Context ctx = null;
		Connection con = null;
		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, jndiFactoryName);
		env.put(Context.PROVIDER_URL, providerUrl);
		env.put("java.naming.security.principal" , security_user);
		env.put("java.naming.security.credentials" ,security_pwd);
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