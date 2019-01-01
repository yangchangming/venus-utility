//Source file: D:\\venus_view\\Tech_Department\\Platform\\Venus\\4项目开发\\1工作区\\4实现\\venus\\frames\\mainframe\\oid\\TableInfoEO.java

package venus.frames.mainframe.oid;


/**
 * 本实体对象对应于系统中的数据库表信息表 mf_oid<br>
 * 该表中记录系统所有的数据表的
 * 表名（TableName）,表编码（TableCode）,OID的字段名中（OidName）
 * 且该表以 表编码（TableCode）为主键<br>
 * OID组成为：
 * 系统ID4（1000-9222）_表ID4_序列号11。共用十九位数字表示一个主键。<br>
 * 表编码用于OID中“表ID4”部分
 * 
 * @author 张文韬
 */
public class TableInfoEO{

	/**
	 * 表编码：
	 * table_code<br>
	 * 
	 * OID组成为：
	 * 系统ID4（1000-9222）_表ID4_序列号11。共用十九位数字表示一个主键。<br>
	 * 
	 * 表编码用于OID中“表ID4”部分
	 */
	private String m_strTableCode;

	/**
	 * 表名：
	 * table_name
	 */
	private String m_strTableName;

	/**
	 * 表中OID的字段名：
	 * oid_name
	 */
	private String m_strOidName;
	
	/**
	 * 表所属数据源名
	 * oid_name
	 */
	private String m_strOidDBSrc;

	/**
	 * 构造函数
	 * @roseuid 3F9F8EB00297
	 */
	public TableInfoEO() {
		super();
	}

	/**
	 * 得到表编码
	 * @return String 表编码
	 * @roseuid 3F94B54A0197
	 */
	public String getTableCode() {
		return this.m_strTableCode;
	}

	/**
	 * 设置表编码
	 * @param argname 表编码
	 * @roseuid 3F94B55502B0
	 */
	public void setTableCode(String m_strtablecode) {
		this.m_strTableCode = m_strtablecode;
	}

	/**
	 * 取得表名
	 * @return String 表名
	 * @roseuid 3F94B7820004
	 */
	public String getTableName() {
		return this.m_strTableName;
	}

	/**
	 * 设置表名
	 * @param argname 表名
	 * @roseuid 3F94B7840072
	 */
	public void setTableName(String m_strtablename) {
		this.m_strTableName = m_strtablename;
	}

	/**
	 * 取得表中OID的字段名
	 * @return String OID的字段名
	 * @roseuid 3F94B7860331
	 */
	public String getOidName() {
		return this.m_strOidName;
	}

	/**
	 * 设置表中OID的字段名
	 * @param argname OID的字段名
	 * @roseuid 3F94B7880341
	 */
	public void setOidName(String m_stroidname) {
		this.m_strOidName = m_stroidname;
	}
	
	
	/**
	 * 取得表所属数据源名
	 * @return String 表所属数据源名
	 * @roseuid 3F94B7860331
	 */
	public String getOidDBSrc() {
		return this.m_strOidDBSrc;
	}

	/**
	 * 设置表所属数据源名
	 * @param argname 表所属数据源名
	 * @roseuid 3F94B7880341
	 */
	public void setOidDBSrc(String m_stroiddbsrc) {
		this.m_strOidDBSrc = m_stroiddbsrc;
	}

	
	/**
	 * 取得系统的表编码的主键<br>
	 * 实现接口IEntityObject中的方法
	 * @return Object
	 * @roseuid 3F94B8A20035
	 */
	public Object getPrimaryKey() {
		return this.getTableCode();
	}

	/**
	 * return true;
	 * @return boolean true
	 * @roseuid 3F94B8CE0258
	 */
	public boolean validate() {
		return true;
	}
	
	
	
}
