/*
 * 创建日期 2008-7-31
 */
package venus.oa.service.sys.util;

/**
 * 2008-7-31
 * 
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
public interface IConstants {
	
	public final static String BS_KEY = "sysparams_bs";
	
	public static final String TABLE_NAME = "AU_SYSPARAS";
	
	public final static String FORWARD_TO_QUERY_ALL = "queryall";
	
	public final static String REQUEST_ID = "id";
	
	public final static String ENABLE = "enable";
	
	public final static String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";
	
	public final static String FORWARD_UPDATE_PAGE = "authority/sys/insert.jsp?isModify=1";
	
	public final static String REQUEST_QUERY_CONDITION = "queryCondition";
	
	public final static String REQUEST_BEANS = "beans";
	
	public final static String FORWARD_LIST_PAGE = "authority/sys/list";

	public final static String SQL_INSERT = "INSERT INTO AU_SYSPARAS(ID,PROPERTYKEY,VALUE,INITTIME,UPDATETIME,CREATORID,CREATORNAME,DESCRIPTION,ENABLE,PROPERTYTYPE,CLOUMN1) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

	public final static String SQL_DELETE_BY_ID = "DELETE FROM AU_SYSPARAS WHERE ID=?";
	
	public final static String SQL_QUERY = "SELECT ID,PROPERTYKEY,VALUE,INITTIME,UPDATETIME,CREATORID,CREATORNAME,DESCRIPTION,ENABLE,PROPERTYTYPE,CLOUMN1 FROM AU_SYSPARAS ";

	public final static String SQL_FIND_BY_ID = SQL_QUERY+" WHERE ID = ? ";
	
   public final static String SQL_UPDATE_BY_ID = "UPDATE AU_SYSPARAS SET VALUE=?,UPDATETIME=?,CREATORID=?,CREATORNAME=?,DESCRIPTION=?,ENABLE=?,CLOUMN1=? WHERE ID=? ";
   
   public final static String DEFAULT_QUERY_WHERE_ENABLE = " WHERE 1=1 ";
   
   public final static String DEFAULT_ORDER_CODE = " INITTIME DESC";

}

