package venus.portal.gbox.resource.customizelib.util;

public interface ICustomizeLibConstants {
    
    //BS的规划化名称
    public final static String BS_KEY = "CustomizeLibBs";

    //struts页面跳转
    public final static String FORWARD_TO_QUERY_ALL = "toQueryAll";
    
    public final static String FORWARD_LIST_PAGE = "listPage";

    public final static String FORWARD_UPDATE_PAGE = "updatePage";
    
    public final static String FORWARD_DETAIL_PAGE = "detailPage";
    
    public final static String FORWARD_VIEW_PAGE = "viewPage";
    
    //request处理中的key值
    public final static String REQUEST_ID = "id";
    
    public final static String REQUEST_IDS = "ids";    

    public final static String REQUEST_BEAN = "bean";
    
    public final static String REQUEST_BEANS = "beans";
    
    public final static String REQUEST_QUERY_CONDITION = "queryCondition";
    
    public final static String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";
    
    //Sql语句
    public final static String SQL_INSERT = "INSERT INTO GBOX_CUSTOMIZE ( ID, NAME, CODE, TAG,  IS_ORIGINAL,IS_EXTERNAL,IS_PROTECTED,ORIGIN,FILE_NAME,FILE_SIZE,FILE_FORMAT,FILE_STATUS,FILE_OBJECT,CREATOR_NAME,CREATE_DATE, DESCRIPTION) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    public final static String SQL_DELETE_BY_ID = "DELETE FROM GBOX_CUSTOMIZE WHERE ID=?";
    
    public final static String SQL_DELETE_MULTI_BY_IDS = "DELETE FROM GBOX_CUSTOMIZE ";

    public final static String SQL_QUERY_ALL = "SELECT ID id, NAME name, CODE code,TAG tag, IS_ORIGINAL isOriginal, IS_EXTERNAL isExternal,IS_PROTECTED isProtected,ORIGIN origin,CLICKS clicks,GOOD_COUNT good_count,BAD_COUNT bad_count,RECO_COUNT reco_count,FILE_NAME fileName,FILE_SIZE fileSize,FILE_FORMAT fileFormat,FILE_STATUS fileStatus, FILE_OBJECT fileObject,CREATOR_NAME creatorName,CREATE_DATE createDate, MODIFIER_NAME modifierName,MODIFY_DATE modifyDate, ENABLE_STATUS enableStatus, DESCRIPTION description FROM GBOX_CUSTOMIZE";
    
    public final static String SQL_FIND_BY_ID = SQL_QUERY_ALL + " WHERE ID=?";

    public final static String SQL_UPDATE_BY_ID = "UPDATE GBOX_CUSTOMIZE SET NAME=?, CODE=?, TAG=?, MODIFIER_NAME=?,MODIFY_DATE=?, ENABLE_STATUS=?, DESCRIPTION=?  WHERE ID=?";
    
    public final static String SQL_COUNT = "SELECT COUNT(ID) FROM GBOX_CUSTOMIZE";
    
    //表名
    public final static String TABLE_NAME = "GBOX_CUSTOMIZE";
    
    //默认启用的查询条件
    public final static String DEFAULT_QUERY_WHERE_ENABLE = " WHERE 1=1 ";
    
    //默认的子表查询条件
    public final static String[] DEFAULT_CONDITION_KEY_ARRAY = new String[]{"id" };
    
    //信息字符串
    public final static String DEFAULT_MSG_ERROR_STR = "Error in populate vo from request.";
    
}
