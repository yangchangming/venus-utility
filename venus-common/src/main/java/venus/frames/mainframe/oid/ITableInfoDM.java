//Source file: D:\\venus_view\\Tech_Department\\Platform\\Venus\\4项目开发\\1工作区\\4实现\\venus\\frames\\mainframe\\oid\\TableInfoDM.java

package venus.frames.mainframe.oid;

import venus.pub.lang.OID;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 主要操作系统表信息表mf_oid，该表记录了系统所有表的表名、编号、以及每个表对应的oid字段名等信息<br>
 * 相关的EO为TableInfoEO，
 * 可参见TableInfoEO中说明
 * 
 * @author 张文韬
 */
public interface ITableInfoDM extends Serializable {


	/**
	 * 通过SQL语句取得系统表信息表mf_oid表中的所有结果集，并根据TableInfo的SET方法构建出该对象集合数组<br>
	 * 得到TableInfoEO[]
	 * @return venus.frames.mainframe.oid.TableInfoEO[] 
	 * @throws SQLException
	 * @roseuid 3F94B93C0288
	 */
	public TableInfoEO[] getAllTableInfos() throws SQLException ;

	/**
	 * 通过主键值 
	 * 查询系统表信息表mf_oid表中的一条结果，并根据TableInfoEO的SET方法构建出该对象并返回
	 *
	 * @param pk 系统表编号
	 * @return TableInfoEO 有关该表的EO
	 * @throws SQLException
	 * @roseuid 3F94B95A022A
	 */
	public TableInfoEO findByPrimaryKey(String pk) throws SQLException ;

	/**
	 * 通过主键值将mf_oid表中的一条记录删除
	 * 
	 * @param pk 系统表编号
	 * @throws SQLException
	 * @roseuid 3F94B9A50112
	 */
	public void delete(String pk) throws SQLException ;

	/**
	 * 取得某一表的最大OID值，该表信息来自传入的参数 TableInfoEO ti
	 * 
	 * @param ti 表信息对象
	 * @return venus.pub.lang.OID
	 * @throws SQLException
	 * @roseuid 3F94BA170170
	 */
	public OID getMaxOIDOfTable(TableInfoEO ti) throws SQLException ;

	/**
	 * 在mf_oid表中的增加一条记录，该记录的值来自传于的参数TableInfoEO val
	 * 
	 * @param val 表信息对象
	 * @throws SQLException
	 * @roseuid 3F94BA3D0299
	 */
	public void insert(TableInfoEO val) throws SQLException ;
	
	/**
	 * 在mf_oid表中的更新一条记录，该记录的值来自传于的参数 TableInfoEO  val
	 *
	 * @param val 表信息对象
	 * @throws SQLException
	 * @roseuid 3F94BA4B0161
	 */
	public void update(TableInfoEO val) throws SQLException ;
	/**
	 * Method setConn.
	 * @param conn
	 */
	public void setConnection(Connection con) throws SQLException;

}
