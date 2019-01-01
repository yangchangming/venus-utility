package venus.portal.gbox.resource.resourcetype.util;

public interface IResourceTypeConstants {

    //BS的规划化名称
    public final static String BS_KEY = "ResourceTypeBs";

    //struts页面跳转
    public final static String FORWARD_TO_QUERY_ALL = "toQueryAll";
    
    public final static String FORWARD_LIST_PAGE = "listPage";

    public final static String FORWARD_UPDATE_PAGE = "updatePage";
    
    public final static String FORWARD_DETAIL_PAGE = "detailPage";
    
    //request处理中的key值
    public final static String REQUEST_ID = "id";
    
    public final static String REQUEST_IDS = "ids";    

    public final static String REQUEST_BEAN = "bean";
    
    public final static String REQUEST_BEANS = "beans";
    
    public final static String REQUEST_QUERY_CONDITION = "queryCondition";
    
    public final static String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";
    
    //Sql语句
    public final static String SQL_INSERT = "INSERT INTO GBOX_RESOURCETYPE ( ID, NAME, RELEVANCE_FORMAT, UPLOAD_PATH, SINGLE_MAXIMUM,TOTAL_MAXIMUM,CREATE_DATE, ENABLE_STATUS, DESCRIPTION) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";
    
    public final static String SQL_DELETE_BY_ID = "DELETE FROM GBOX_RESOURCETYPE WHERE ID=?";
    
    public final static String SQL_DELETE_MULTI_BY_IDS = "DELETE FROM GBOX_RESOURCETYPE WHERE IS_DEFAULTTYPE = 0 ";

    public final static String SQL_QUERY_ALL = "SELECT ID id, NAME name, RELEVANCE_FORMAT relevanceFormat, UPLOAD_PATH uploadPath, SINGLE_MAXIMUM singleMaximum, TOTAL_MAXIMUM totalMaximum, IS_DEFAULTTYPE isDefaultType, CREATE_DATE createDate, MODIFY_DATE modifyDate, ENABLE_STATUS enableStatus, DESCRIPTION description FROM GBOX_RESOURCETYPE";
    
    public final static String SQL_FIND_BY_ID = SQL_QUERY_ALL+ " WHERE ID=?";

    public final static String SQL_UPDATE_BY_ID = "UPDATE GBOX_RESOURCETYPE SET NAME=?, RELEVANCE_FORMAT=?, UPLOAD_PATH=?, SINGLE_MAXIMUM=?, TOTAL_MAXIMUM=?, MODIFY_DATE=?, ENABLE_STATUS=?, DESCRIPTION=?  WHERE ID=?";
    
    public final static String SQL_COUNT = "SELECT COUNT(ID) FROM GBOX_RESOURCETYPE";
    
    //表名
    public final static String TABLE_NAME = "GBOX_RESOURCETYPE";
    
    //默认启用的查询条件
    public final static String DEFAULT_QUERY_WHERE_ENABLE = " WHERE 1=1 ";
    
    //默认的子表查询条件
    public final static String[] DEFAULT_CONDITION_KEY_ARRAY = new String[]{"id" };
    
    //信息字符串
    public final static String DEFAULT_MSG_ERROR_STR = "Error in populate vo from request.";
}
