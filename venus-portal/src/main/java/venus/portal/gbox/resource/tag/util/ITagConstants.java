package venus.portal.gbox.resource.tag.util;

public interface ITagConstants {

    //BS的规划化名称
    public final static String BS_KEY = "TagBs";

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
    public final static String SQL_INSERT = "INSERT INTO GBOX_TAG ( ID, NAME,CREATE_DATE) VALUES ( ?, ?, ?)";
    
    public final static String SQL_DELETE_BY_ID = "DELETE FROM GBOX_TAG WHERE ID=?";
    
    public final static String SQL_DELETE = "DELETE FROM GBOX_TAG ";

    public final static String SQL_QUERY_ALL = "SELECT ID id, NAME name, CLICKS clicks, creatorName,CREATE_DATE createDate FROM GBOX_TAG";
    
    public final static String SQL_FIND_BY_NAME = SQL_QUERY_ALL + " WHERE NAME=?";

    public final static String SQL_UPDATE_BY_ID = "UPDATE GBOX_TAG SET NAME=?, CLICKS=?  WHERE ID=?";
    
    public final static String SQL_UPDATECLICKS = "UPDATE GBOX_TAG SET  CLICKS=CLICKS+1  WHERE NAME=?";
    
    public final static String SQL_COUNT = "SELECT COUNT(ID) FROM GBOX_TAG";
    
    //表名
    public final static String TABLE_NAME = "GBOX_TAG";
    
    //默认启用的查询条件
    public final static String DEFAULT_QUERY_WHERE_ENABLE = " WHERE 1=1 ";
    
    //默认的子表查询条件
    public final static String[] DEFAULT_CONDITION_KEY_ARRAY = new String[]{"id" };
    
    //信息字符串
    public final static String DEFAULT_MSG_ERROR_STR = "Error in populate vo from request.";    
}
