package venus.portal.gbox.resource.resourceimport.util;

public interface IResourceImportConstants {
    
    //BS的规划化名称
    public final static String BS_KEY = "ResourceImportBs";
    
    public final static String BS_KEY_TARGET = "ResourceImportBs_target";

    //struts页面跳转
    public final static String FORWARD_TO_QUERY_ALL = "toQueryAll";
    
    public final static String FORWARD_LIST_PAGE = "listPage";
    
    public final static String FORWARD_IMPORT_FRAME = "importFrame"; 
    
    //request处理中的key值
    public final static String REQUEST_ID = "id";
    
    public final static String REQUEST_IDS = "ids";    

    public final static String REQUEST_BEAN = "bean";
    
    public final static String REQUEST_BEANS = "beans";
    
    public final static String REQUEST_QUERY_CONDITION = "queryCondition";
    
    public final static String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";
    
    //Sql语句
    public final static String SQL_INSERT = "INSERT INTO GBOX_RESOURCEIMPORT ( ID, CLASSIFICATION_ID, RESOURCE_PATH, IS_SCAN, DESCRIPTION) VALUES ( ?, ?, ?, ?, ?)";
    
    public final static String SQL_DELETE = "DELETE FROM GBOX_RESOURCEIMPORT";
    
    public final static String SQL_QUERY_ALL = "SELECT ID id, CLASSIFICATION_ID classificationId, RESOURCE_PATH resourcePath,IS_SCAN isScan,DESCRIPTION description FROM GBOX_RESOURCEIMPORT";
    
    public final static String SQL_UPDATE_BY_ID = "UPDATE GBOX_RESOURCEIMPORT SET RESOURCE_PATH=?, DESCRIPTION=? WHERE ID=?";
    
    public final static String SQL_COUNT = "SELECT COUNT(ID) FROM GBOX_RESOURCEIMPORT";
    
    //表名
    public final static String TABLE_NAME = "GBOX_RESOURCEIMPORT";
    
    //默认启用的查询条件
    public final static String DEFAULT_QUERY_WHERE_ENABLE = " WHERE 1=1 ";
    
    //默认的子表查询条件
    public final static String[] DEFAULT_CONDITION_KEY_ARRAY = new String[]{"id" };
    
    //信息字符串
    public final static String DEFAULT_MSG_ERROR_STR = "Error in populate vo from request.";    
}
