package venus.frames.mainframe.db.conpool.provider;

import venus.frames.mainframe.db.conpool.IConProvider;
import venus.frames.mainframe.util.IConfReader;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author huchangcheng
 *
 */
public class JBossLocalJNDIProvider implements IConProvider
{
   
   public Connection requestConnection(IConfReader icr) throws SQLException{

	   long start = System.currentTimeMillis();
	   
	   String datasource_jndiname = icr.readStringAttribute("JndiName");
	   Connection conn = null;
	   try{
		  Context initCtx = new InitialContext();
	     DataSource ds = (DataSource) initCtx.lookup ("java:/"+datasource_jndiname);
	     conn = ds.getConnection();
	   } catch (Exception e) {
	        e.printStackTrace();
	   }
	   long end = System.currentTimeMillis();
   	   System.out.println( "[JBossLocalJNDIProvider INFO] "+(formatDate())+" getConnection use time "+(end-start) );

		return conn;

   }
   
	private static String formatDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	/* （非 Javadoc）
	 * @see venus.frames.mainframe.db.conpool.IConProvider#reset()
	 */
	public void reset() {
		// TODO 自动生成方法存根
		
	}
}