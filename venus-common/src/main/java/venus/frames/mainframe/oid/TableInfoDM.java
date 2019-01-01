package venus.frames.mainframe.oid;

import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.ConfMgr;
import venus.frames.mainframe.util.IConfReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * 主要操作系统表信息表m_strTableName，该表记录了系统所有表的表名、编号、以及每个表对应的oid字段名等信息 <br>
 * 相关的EO为TableInfoEO， 可参见TableInfoEO中说明
 * 
 * @author 张文韬
 */
public class TableInfoDM extends AbstractTableInfoDM {

    private String m_strTableName = "mf_oid";

    private String m_strCodeColName = "table_code";

    private String m_strNameColName = "table_name";

    private String m_strOidColName = "oid_name";

    private String m_strOidDBSrcName = "src_name";

    /**
     * 该DM的构造器 <br>
     * 
     * 继承于 BaseDataManager
     * 
     * @param con
     *            数据库连接
     * @roseuid 3F94B9C20075
     */
    public TableInfoDM(Connection con) throws SQLException {
        this.setConnection(con);
        loadConf();
    }

    public TableInfoDM() {
        loadConf();
    }

    private void loadConf() {
        //读配置文件
        IConfReader dcr = ConfMgr.getConfReader(this.getClass().getName());
        if (dcr == null)
            return;
        try {
            //获得配置文件中本系统OID的基数
            String tmp_strTableName = dcr.readStringAttribute("OidTableName");
            String tmp_strCodeColName = dcr.readStringAttribute("CodeCol");
            String tmp_strNameColName = dcr.readStringAttribute("NameCol");
            String tmp_strOidColName = dcr.readStringAttribute("OidCol");
            String tmp_strOidDBSrcName = dcr.readStringAttribute("DBSrcCol");
            if (tmp_strTableName != null && !tmp_strTableName.equals("")
                    && tmp_strCodeColName != null
                    && !tmp_strCodeColName.equals("")
                    && tmp_strNameColName != null
                    && !tmp_strNameColName.equals("")
                    && tmp_strOidColName != null
                    && !tmp_strOidColName.equals("")
                    && tmp_strOidDBSrcName != null
                    && !tmp_strOidDBSrcName.equals("")) {
                m_strTableName = tmp_strTableName;
                m_strCodeColName = tmp_strCodeColName;
                m_strNameColName = tmp_strNameColName;
                m_strOidColName = tmp_strOidColName;
                m_strOidDBSrcName = tmp_strOidDBSrcName;
            }
        } catch (NumberFormatException e) {
            LogMgr.getLogger(this).error(
                    "TableInfoDM中的loadConf方法,读取系统OID表配置信息出错！", e);
        }
    }

    /**
     * 通过SQL语句取得系统表信息表m_strTableName表中的所有结果集，并根据TableInfo的SET方法构建出该对象集合数组 <br>
     * 得到TableInfoEO[]
     * 
     * @return venus.frames.mainframe.oid.TableInfoEO[]
     * @throws SQLException
     * @roseuid 3F94B93C0288
     */
    public TableInfoEO[] getAllTableInfos() throws SQLException {
        //一条数据对应一个TableInfoEO
        TableInfoEO[] result = null;
        //从系统表信息表中读取表编码、表名以及OID字段名
        String strSQL = "select " + m_strCodeColName + ", " + m_strNameColName
                + ", " + m_strOidColName + ", " + m_strOidDBSrcName + " from "
                + m_strTableName;
        Vector vec = new Vector();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(strSQL);
            rs = stmt.executeQuery();
            //将查询结果提交给TableInfoEO，每条记录对应一个TableInfoEO
            //然后将所有TableInfoEO实例暂存到Vector变量中
            while (rs.next()) {
                TableInfoEO aTableInfo = new TableInfoEO();
                aTableInfo.setTableCode(rs.getString(m_strCodeColName));
                aTableInfo.setTableName(rs.getString(m_strNameColName));
                aTableInfo.setOidName(rs.getString(m_strOidColName));
                aTableInfo.setOidDBSrc(rs.getString(m_strOidDBSrcName));
                vec.addElement(aTableInfo);
            }
        } catch (Throwable e) {
            handleException(e);
        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
        //将Vector中的结果转交给数组返回,如果Vector为空则返回一个空的数组
        if (vec.size() > 0) {
            result = new TableInfoEO[vec.size()];
            vec.copyInto(result);
            return result;
        } else {
            result = new TableInfoEO[0];
            return result;
        }
    }

    /**
     * @param e
     */
    private void handleException(Throwable e) {
    }

    /**
     * 通过主键值 查询系统表信息表m_strTableName表中的一条结果，并根据TableInfoEO的SET方法构建出该对象并返回
     * 
     * @param pk
     *            系统表编号
     * @return TableInfoEO 有关该表的EO
     * @throws SQLException
     * @roseuid 3F94B95A022A
     */
    public TableInfoEO findByPrimaryKey(String pk) throws SQLException {
        //处理参数空指针异常
        if (pk == null) {
            LogMgr.getLogger(this).error(
                    "TableInfoDM中的findByPrimaryKey方法,参数空指针异常！");
            return null;
        }
        TableInfoEO aTableInfo = new TableInfoEO();
        //定义查询语句
        String strSQL = "select " + m_strCodeColName + "," + m_strNameColName
                + "," + m_strOidColName + "," + m_strOidDBSrcName + " from "
                + m_strTableName + " where " + m_strCodeColName + " = '" + pk
                + "'";
        PreparedStatement stmt = getConnection().prepareStatement(strSQL);
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery();
            while (rs.next()) {
                try {
                    //将得到的结果交给TableInfoEO
                    aTableInfo.setTableCode(rs.getString(m_strCodeColName));
                    aTableInfo.setTableName(rs.getString(m_strNameColName));
                    aTableInfo.setOidName(rs.getString(m_strOidColName));
                    aTableInfo.setOidDBSrc(rs.getString(m_strOidDBSrcName));
                } catch (Throwable e1) {
                    handleException(e1);
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return aTableInfo;
    }

    /**
     * 通过主键值将m_strTableName表中的一条记录删除
     * 
     * @param pk
     *            系统表编号
     * @throws SQLException
     * @roseuid 3F94B9A50112
     */
    public void delete(String pk) throws SQLException {
        //处理参数空指针异常
        if (pk == null) {
            LogMgr.getLogger(this).error("TableInfoDM中的delete方法,参数空指针异常！");
            return;
        }
        //构造执行语句
        String strSQL = "delete from " + m_strTableName + " where "
                + m_strCodeColName + " = '" + pk + "'";
        PreparedStatement stmt = getConnection().prepareStatement(strSQL);
        try {
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    /**
     * 在m_strTableName表中的增加一条记录，该记录的值来自传于的参数TableInfoEO val
     * 
     * @param val
     *            表信息对象
     * @throws SQLException
     * @roseuid 3F94BA3D0299
     */
    public void insert(TableInfoEO val) throws SQLException {
        //处理参数为空的情况
        if (val == null) {
            LogMgr.getLogger(this).error("TableInfoDM中的insert方法,参数空指针异常！");
            return;
        }
        //构造SQL语句
        //		String tableCode = (val.getTableCode() == null ? null :
        // val.getTableCode());
        //		String tableName = (val.getTableName() == null ? null :
        // val.getTableName());
        //		String oidName = (val.getOidName() == null ? null :
        // val.getOidName());
        String strSQL = "insert into "
                + m_strTableName
                + " ("
                + m_strCodeColName
                + ","
                + m_strNameColName
                + ","
                + m_strOidColName
                + ","
                + m_strOidDBSrcName
                + ") values ("
                + "'"
                + val.getPrimaryKey().toString()
                + "'"
                + ","
                + (val.getTableName() == null ? "null" : "'"
                        + val.getTableName() + "'")
                + ","
                + (val.getOidName() == null ? "null" : "'" + val.getOidName()
                        + "'")
                + ","
                + (val.getOidDBSrc() == null ? "null" : "'" + val.getOidDBSrc()
                        + "'") + ")";
        PreparedStatement stmt = getConnection().prepareStatement(strSQL);
        try {
            //执行插入操作
            stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

    /**
     * 在m_strTableName表中的更新一条记录，该记录的值来自传于的参数 TableInfoEO val
     * 
     * @param val
     *            表信息对象
     * @throws SQLException
     * @roseuid 3F94BA4B0161
     */
    public void update(TableInfoEO val) throws SQLException {
        //处理参数为空的情况
        if (val == null) {
            LogMgr.getLogger(this).error("TableInfoDM中的update方法,参数空指针异常！");
            return;
        }
        //构造SQL语句
        String strSQL = "update "
                + m_strTableName
                + " set "
                + m_strNameColName
                + "="
                + (val.getTableName() == null ? "null" : "'"
                        + val.getTableName() + "'")
                + ","
                + m_strOidColName
                + "="
                + (val.getOidName() == null ? "null" : "'" + val.getOidName()
                        + "' ")
                + ","
                + m_strOidDBSrcName
                + "="
                + (val.getOidDBSrc() == null ? "null" : "'" + val.getOidDBSrc()
                        + "' ") + " where " + m_strCodeColName + " = '"
                + val.getPrimaryKey() + "'";

        PreparedStatement stmt = getConnection().prepareStatement(strSQL);
        try {
            stmt.executeUpdate();
        } finally {
            if (stmt != null)
                stmt.close();
        }
    }

}