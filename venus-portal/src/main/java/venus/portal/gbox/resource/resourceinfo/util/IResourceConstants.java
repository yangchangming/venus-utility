package venus.portal.gbox.resource.resourceinfo.util;

public interface IResourceConstants {
    
    //BS的规划化名称
    public final static String BS_KEY = "ResourceBs";

    //struts页面跳转
    public final static String FORWARD_TO_QUERY_ALL = "toQueryAll";
    
    public final static String TO_RELATIONEDLISTPAGE = "toRelationedListPage";
    
    public final static String FORWARD_LIST_PAGE = "listPage";
    
    public final static String FORWARD_UPDATE_PAGE = "updatePage";
    
    public final static String FORWARD_DETAIL_PAGE = "detailPage";
    
    public final static String FORWARD_TOIMAGEUPLOAD_ACTION = "toImageUploadAction";
    
    public final static String FORWARD_TOIMAGEFIND_ACTION = "toImageFindAction";
    
    public final static String FORWARD_TOIMAGEUPDATE_ACTION = "toImageUpdateAction";
    
    public final static String FORWARD_TOIMAGEUPDATETAG_ACTION = "toImageUpdateTagAction";
    
    public final static String FORWARD_TOIMAGEUPDATETAGTOQUERRY_ACTION = "toImageUpdateTagToQuerryAction";
    
    public final static String FORWARD_TOIMAGEDELETE_ACTION = "toImageDeleteAction";
    
    public final static String FORWARD_TOVIEDOUPLOAD_ACTION = "toVideoUploadAction";
    
    public final static String FORWARD_TOVIEDOFIND_ACTION = "toVideoFindAction";
    
    public final static String FORWARD_TOVIEDOUPDATE_ACTION = "toVideoUpdateAction";
    
    public final static String FORWARD_TOVIEDOUPDATETAG_ACTION = "toVideoUpdateTagAction"
        ;
    public final static String FORWARD_TOVIEDOUPDATETAGTOQUERRY_ACTION = "toVideoUpdateTagToQuerryAction";
    
    public final static String FORWARD_TOVIDEODELETE_ACTION = "toVideoDeleteAction";
    
    public final static String FORWARD_TODOCUPLOAD_ACTION = "toDocUploadAction";
    
    public final static String FORWARD_TODOCFIND_ACTION = "toDocFindAction";
    
    public final static String FORWARD_TODOCUPDATE_ACTION = "toDocUpdateAction";
    
    public final static String FORWARD_TODOCUPDATETAG_ACTION = "toDocUpdateTagAction";
    
    public final static String FORWARD_TODOCUPDATETAGTOQUERRY_ACTION = "toDocUpdateTagToQuerryAction";
    
    public final static String FORWARD_TODOCDELETE_ACTION = "toDocDeleteAction";
    
    public final static String FORWARD_TOAUDIOUPLOAD_ACTION = "toAudioUploadAction";
    
    public final static String FORWARD_TOAUDIOFIND_ACTION = "toAudioFindAction";
    
    public final static String FORWARD_TOAUDIOUPDATE_ACTION = "toAudioUpdateAction";
    
    public final static String FORWARD_TOAUDIOUPDATETAG_ACTION = "toAudioUpdateTagAction";
    
    public final static String FORWARD_TOAUDIOUPDATETAGTOQUERRY_ACTION = "toAudioUpdateTagToQuerryAction";
    
    public final static String FORWARD_TOAUDIODELETE_ACTION = "toAudioDeleteAction";
    
    public final static String FORWARD_RESOURCEFEFERENCE = "resourceFeference";
    
    public final static String FORWARD_TAGREFERENCE = "tagReference";
    
    //request处理中的key值
    public final static String REQUEST_ID = "id";
    
    public final static String REQUEST_IDS = "ids";    

    public final static String REQUEST_BEAN = "bean";
    
    public final static String REQUEST_BEANS = "beans";
    
    public final static String REQUEST_QUERY_CONDITION = "queryCondition";
    
    public final static String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";
    
    //Sql语句
    public final static String SQL_INSERT = "INSERT INTO GBOX_RESOURCE ( ID, NAME, CODE, TAG,  TYPE, IS_PROTECTED,IS_EXTERNAL,FILE_NAME,FILE_SIZE,FILE_FORMAT,CREATOR_NAME, CREATE_DATE, DESCRIPTION) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ?)";
    
    public final static String SQL_DELETE_BY_ID = "DELETE FROM GBOX_RESOURCE WHERE ID=?";
    
    public final static String SQL_DELETE = "DELETE FROM GBOX_RESOURCE ";
    
    public final static String SQL_QUERY_ALL = "SELECT ID id, NAME name, CODE code, TAG tag, TYPE type, IS_PROTECTED isProtected, IS_EXTERNAL isExternal,FILE_NAME fileName, FILE_SIZE fileSize, FILE_FORMAT fileFormat, CREATE_DATE createDate, MODIFY_DATE modifyDate, ENABLE_STATUS enableStatus, DESCRIPTION description FROM GBOX_RESOURCE";    
    
    public final static String SQL_FIND_BY_ID = SQL_QUERY_ALL +  " WHERE ID=?";
    
    public final static String SQL_FINDALL_BY_ID = "SELECT a.ID id, a.NAME name, a.CODE code, a.TAG tag, a.TYPE type, a.IS_PROTECTED isProtected, a.IS_EXTERNAL isExternal,b.UPLOAD_PATH || '/' || a.FILE_NAME fileName, a.FILE_SIZE fileSize, a.FILE_FORMAT fileFormat, a.CREATE_DATE createDate, a.MODIFY_DATE modifyDate, a.ENABLE_STATUS enableStatus, a.DESCRIPTION description FROM GBOX_RESOURCE a,GBOX_RESOURCETYPE b WHERE a.TYPE = b.ID AND a.id = ?";

    public final static String SQL_UPDATE_BY_ID = "UPDATE GBOX_RESOURCE SET NAME=?, CODE=?, MODIFIER_NAME=?, MODIFY_DATE=?, ENABLE_STATUS=?, DESCRIPTION=?  WHERE ID=?";
    
    public final static String SQL_UPDATETAG_BY_ID = "UPDATE GBOX_RESOURCE SET TAG=?, MODIFIER_NAME=?, MODIFY_DATE=? WHERE ID=?";
    
    public final static String SQL_COUNT = "SELECT COUNT(ID) FROM GBOX_RESOURCE";
    
    //表名
    public final static String TABLE_NAME = "GBOX_RESOURCE";
    
    //默认启用的查询条件
    public final static String DEFAULT_QUERY_WHERE_ENABLE = " WHERE 1=1 ";
    
    //默认的子表查询条件
    public final static String[] DEFAULT_CONDITION_KEY_ARRAY = new String[]{"id" };
    
    //信息字符串
    public final static String DEFAULT_MSG_ERROR_STR = "Error in populate vo from request.";    
}
