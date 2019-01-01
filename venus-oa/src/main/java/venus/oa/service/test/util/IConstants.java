/*
* 
* 创建日期 2006-12-24
*
*/
package venus.oa.service.test.util;

/**
* @author tony
*
*/
public interface IConstants {
    //  BS定义引用
    public final static String BS = "test_bs";
    //  页面跳转
    public final static String TABLE_NAME = "table";
    public final static String PROJECT_VALUE = "wy";
    public final static String TABLE_VALUE = "ta";
    public final static String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";
    public final static String FORWARD_LIST_PAGE_KEY = "authority/test/list";
    public final static String FORWARD_QUERYALL_PAGE_KEY = "queryPage";
    //  SQL语句
    public final static String GETRECORDCOUNT_SQL = "select count(*) from ";
    public final static String QUERY_SQL = "select * from ";
    public final static String QUERY_TABLE_SQL_ORACLE = "select table_name from user_tables ";
    public final static String QUERY_TABLE_SQL_SQLSERVER = "select name  as table_name from sysobjects where xtype='U' ";
    public final static String QUERY_TABLE_SQL_DB2 = "select   tabname as table_name   from   syscat.tables ";
}

