/*
 * 创建日期 2004-12-23
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package venus.frames.mainframe.oid.bs;

import venus.pub.lang.OID;

/**
 * @author wujun
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public interface IOidBS {
	/**
	 * 取得新的oid数组
	 */
	public OID[] requestOIDArray(String tableName, int len);

	/**
	 * 验取得新的oid
	 */
	public OID requestOID(String tableName);
	
	/**
	 * 取得新的oid数组
	 */
	public String[] requestOIDStringArray(String tableName, int len);
	
	/**
	 * 取得新的oid数组
	 */
	public String[] requestOIDStringArrayOneArg(String tableNameAndLen);
	
	/**
	 * 验取得新的oid
	 */
	public String requestOIDString(String tableName);
	
}