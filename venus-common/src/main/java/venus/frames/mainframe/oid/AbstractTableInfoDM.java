package venus.frames.mainframe.oid;

import venus.frames.mainframe.log.LogMgr;
import venus.pub.lang.OID;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author huqi
 * @version 1.0.0
 * @see
 * @since 1.0.0
 */
public abstract class AbstractTableInfoDM implements ITableInfoDM {
    /**
     * 本数据操作类相应的DB连接
     */
    private Connection m_connect = null;
    private final static int DB2 = 1;
    private final static int MYSQL = 2;

    /**
     * 特殊数据库类型支持
     * <p>
     */
    private static int DB_TYPE = 0;

    public void setConnection(Connection con) throws SQLException {
        m_connect = con;
    }

    /**
     * for this: 29198739(com.ibm.db2.jcc.c.b@934c3b)
     * 
     * @return
     */
    protected Connection getConnection() {
        if (m_connect == null)
            return null;
        String connStr = m_connect.toString().toLowerCase();
        System.out.println( "连接池字符串：" + connStr );
        if( connStr.indexOf("ibm.db2") != -1 )
            DB_TYPE = DB2;
        else if ( connStr.indexOf("mysql") != -1 )
            DB_TYPE = MYSQL;
        return m_connect;
    }

    /**
     * 取得某一表的最大OID值，该表信息来自传入的参数 <TableInfoEO>
     * 
     * @param ti
     *            表信息对象
     * @return <OID>
     * @throws SQLException
     * @author huqi
     */
    public OID getMaxOIDOfTable(TableInfoEO ti) throws SQLException {
        long maxOID = 0;
        OID result = null;
        //处理参数为空的情况
        if (ti == null) {
            LogMgr.getLogger(this).error("参数<TableInfoEO>为空");
            return null;
        }
        //构造SQL语句
        String oidName = ti.getOidName();
        String tableCode = ti.getTableCode();
        String tableName = ti.getTableName();
        if (!OidMgr.checkTableCode(tableCode)) {
            String err = "Table: "
                    + tableName
                    + ", TableCode: "
                    + tableCode
                    + ". Length is above sys tableCode length setting. Check it.";
            LogMgr.getLogger(this).error(err);
            return null;
        }
        Statement stmt = getConnection().createStatement();
        ResultSet rs = null;
        //取数据
        try {
            String strSQL = null;
            if (DB_TYPE == DB2) {
                strSQL = "select max(" + oidName + ") as a from " + tableName + " where CHAR(" + oidName + ") like '" + OidMgr.getSYSCode()
                        + tableCode + "%'";
            } else if (DB_TYPE == MYSQL) {
                strSQL = "select max(" + oidName + ") as a from " + tableName + " where cast( " + oidName + " as char(19) ) > '" + OidMgr.getSYSCode()
                + tableCode + "00000000000' and cast( " + oidName + " as char(19) ) <= '" + OidMgr.getSYSCode() + tableCode + "99999999999'";
            } else {
                strSQL = "select max(" + oidName + ") as a from " + tableName + " where " + oidName + " > '" + OidMgr.getSYSCode()
                        + tableCode + "00000000000' and " + oidName + " <= '" + OidMgr.getSYSCode() + tableCode + "99999999999'";
            }
                
            LogMgr.getLogger(this).debug("SQL: " + strSQL);
            rs = stmt.executeQuery(strSQL);
            if (rs == null) {
                LogMgr.getLogger(this).error(
                        tableName + " Error in SQL: " + strSQL + ", Check it.");
                return null;
            }
            try {
                if (rs.next()) {
                    String str = rs.getString("a");
                    if (str != null) {
                        maxOID = Long.parseLong(str);
                    }
                    //如果表中没有数据，需要根据系统编号和表编号初始化一个OID的最大值
                    else {
                        maxOID = OidMgr.getSysPrefix()
                                + Long.parseLong(ti.getTableCode())
                                * OidMgr.getTablePrefix();
                    }
                }
            } catch (Exception e) {
                LogMgr.getLogger(this).error("Error in " + tableName, e);
                return null;
            }
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
        try {
            result = new OID(maxOID);
        } catch (Exception e) {
            LogMgr.getLogger(this).error("Error in " + tableName, e);
            return null;
        }
        return result;
    }
}
