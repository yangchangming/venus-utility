package venus.frames.mainframe.oid;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.ConfMgr;
import venus.frames.mainframe.util.IConfReader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * 主要操作系统表信息表mf_oid，该表记录了系统所有表的表名、编号、以及每个表对应的oid字段名等信息 <br>
 * 相关的EO为TableInfoEO，可参见TableInfoEO中说明
 * 
 * @author 张文韬
 */
public class TableInfoDM4Xml extends AbstractTableInfoDM {

    /**
     * 通过SQL语句取得系统表信息表mf_oid表中的所有结果集，并根据TableInfo的SET方法构建出该对象集合数组 <br>
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

        //String strSQL =
        //	"select table_code, table_name, oid_name from mf_oid";
        Vector vec = new Vector();

        try {
            //读配置文件
            IConfReader icr = ConfMgr.getConfReader(this.getClass().getName());
            if (icr == null)
                return null;
            ArrayList usrAryLst = icr.readChildNodesAry("table");
            int n_len = usrAryLst.size();

            //将信息存到数组中
            for (int i = 0; i < n_len; i++) {
                Node tmpNode = (Node) usrAryLst.get(i);
                //取信息
                NamedNodeMap tmpNodeMap = tmpNode.getAttributes();
                Node tblCodeNode = tmpNodeMap.getNamedItem("table_code");
                Node tblNameNode = tmpNodeMap.getNamedItem("table_name");
                Node oidNameNode = tmpNodeMap.getNamedItem("oid_name");
                Node srcNameNode = tmpNodeMap.getNamedItem("src_name");

                String tblCode = tblCodeNode.getNodeValue();
                String tblName = tblNameNode.getNodeValue();
                String oidName = oidNameNode.getNodeValue();
                String srcName = ((srcNameNode == null) ? null : srcNameNode
                        .getNodeValue());

                TableInfoEO aTableInfo = new TableInfoEO();
                aTableInfo.setTableCode(tblCode);
                aTableInfo.setTableName(tblName);
                aTableInfo.setOidName(oidName);
                aTableInfo.setOidDBSrc(srcName);
                vec.addElement(aTableInfo);
            }

            //将查询结果提交给TableInfoEO，每条记录对应一个TableInfoEO
            //然后将所有TableInfoEO实例暂存到Vector变量中

        } catch (Throwable e) {
            handleException(e);
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
     * 通过主键值 查询系统表信息表mf_oid表中的一条结果，并根据TableInfoEO的SET方法构建出该对象并返回
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
        String strSQL = "select table_code,table_name,oid_name from mf_oid "
                + "where table_code = '" + pk + "'";
        PreparedStatement stmt = getConnection().prepareStatement(strSQL);
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery();
            while (rs.next()) {
                try {
                    //将得到的结果交给TableInfoEO
                    aTableInfo.setTableCode(rs.getString(1));
                    aTableInfo.setTableName(rs.getString(2));
                    aTableInfo.setOidName(rs.getString(3));
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
     * @param e1
     */
    private void handleException(Throwable e1) {
    }

    /**
     * 通过主键值将mf_oid表中的一条记录删除
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
        /*
         * String strSQL = "delete from mf_oid where table_code = '" + pk + "'";
         * PreparedStatement stmt = getConnection().prepareStatement(strSQL);
         * try { stmt.executeUpdate(); } finally { if (stmt != null) {
         * stmt.close(); } }
         */
    }

    /**
     * 在mf_oid表中的增加一条记录，该记录的值来自传于的参数TableInfoEO val
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
        /*
         * String strSQL = "insert into mf_oid (table_code,table_name,oid_name)
         * values ("
         * +"'"+val.getPrimaryKey().toString()+"'"+","+(val.getTableName() ==
         * null ? "null" : "'" +val.getTableName()+"'")+","+(val.getOidName() ==
         * null ? "null" : "'" +val.getOidName()+"'")+")"; PreparedStatement
         * stmt = getConnection().prepareStatement(strSQL); try { //执行插入操作
         * stmt.executeUpdate(); } finally { if (stmt != null) stmt.close(); }
         */
    }

    /**
     * 在mf_oid表中的更新一条记录，该记录的值来自传于的参数 TableInfoEO val
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
        /*
         * //构造SQL语句 String strSQL = "update mf_oid set "
         * +"table_name="+(val.getTableName() == null ? "null" : "'" +
         * val.getTableName() + "'" ) +",oid_name="+(val.getOidName() == null ?
         * "null" : "'" + val.getOidName() + "' ") +"where table_code =
         * '"+val.getPrimaryKey()+"'"; PreparedStatement stmt =
         * getConnection().prepareStatement(strSQL); try { stmt.executeUpdate(); }
         * finally { if (stmt != null) stmt.close(); }
         */
    }
}