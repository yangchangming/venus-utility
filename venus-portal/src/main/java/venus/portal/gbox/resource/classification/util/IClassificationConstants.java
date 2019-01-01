package venus.portal.gbox.resource.classification.util;

public interface IClassificationConstants {
    
    //BS的规划化名称
    public final static String BS_KEY = "ClassificationBs";

    //struts页面跳转
    public final static String FORWARD_TO_QUERY_ALL = "toQueryAll";
    
    public final static String FORWARD_LIST_PAGE = "listPage";

    public final static String FORWARD_UPDATE_PAGE = "updatePage";
    
    public final static String FORWARD_DETAIL_PAGE = "detailPage";
    
    public final static String FORWARD_MGT_PAGE = "mgtPage";
    
    public final static String FORWARD_REFERENCE_PAGE = "referencePage";
    
    //request处理中的key值
    public final static String REQUEST_ID = "id";
    
    public final static String REQUEST_IDS = "ids";    

    public final static String REQUEST_BEAN = "bean";
    
    public final static String REQUEST_BEANS = "beans";
    
    public final static String REQUEST_QUERY_CONDITION = "queryCondition";
    
    public final static String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";
    
    //Sql语句
    public final static String SQL_INSERT = "INSERT INTO GBOX_CLASSIFICATION ( ID, NAME, SELF_CODE, PARENT_CODE, DEPTH,IS_LEAF,ORDER_CODE,PATH,CREATE_DATE, DESCRIPTION) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    public final static String SQL_DELETE_BY_ID = "DELETE FROM GBOX_CLASSIFICATION WHERE ID=?";
    
    public final static String SQL_DELETE_MULTI = "DELETE FROM GBOX_CLASSIFICATION ";

    public final static String SQL_QUERY_ALL = "SELECT ID id, NAME name, SELF_CODE selfCode,PARENT_CODE parentCode,DEPTH depth, IS_LEAF isLeaf,ORDER_CODE orderCode,CLICKS clicks,PATH path,CREATE_DATE createDate,MODIFY_DATE modifyDate, ENABLE_STATUS enableStatus, DESCRIPTION description FROM GBOX_CLASSIFICATION";
    
    public final static String SQL_FIND_BY_ID = SQL_QUERY_ALL + " WHERE ID=?";
    
    public final static String SQL_MAXCODE = "SELECT MAX(SELF_CODE) + 1 maxcode FROM GBOX_CLASSIFICATION ";

    public final static String SQL_UPDATE_BY_ID = "UPDATE GBOX_CLASSIFICATION SET NAME=?, MODIFY_DATE=?, DESCRIPTION=?  WHERE ID=?";
    
    public final static String SQL_UPDATE_LEAF = "UPDATE GBOX_CLASSIFICATION SET IS_LEAF=?  WHERE ID=?";
    
    public final static String SQL_COUNT = "SELECT COUNT(ID) FROM GBOX_CLASSIFICATION";
    
    //表名
    public final static String TABLE_NAME = "GBOX_CLASSIFICATION";
    
    //默认启用的查询条件
    public final static String DEFAULT_QUERY_WHERE_ENABLE = " WHERE 1=1 ";
    
    //默认的子表查询条件
    public final static String[] DEFAULT_CONDITION_KEY_ARRAY = new String[]{"id" };
    
    //信息字符串
    public final static String DEFAULT_MSG_ERROR_STR = "Error in populate vo from request.";
    
}
