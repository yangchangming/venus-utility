package venus.frames.mainframe.db.conpool;

import venus.frames.mainframe.util.IConfReader;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * a interface define of connection pool for multi-datasource
 */
public interface IConProvider 
{
	/**
	 * Get the datasource connection for application system.
	 * @param icr
	 * @return
	 * @throws SQLException
	 */
	Connection requestConnection(IConfReader icr) throws SQLException ;
   
	/**
	 * reset the connection when connection is invalid.
	 */
	void reset();
   
}
