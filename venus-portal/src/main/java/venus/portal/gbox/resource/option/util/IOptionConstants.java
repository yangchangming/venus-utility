package venus.portal.gbox.resource.option.util;

public interface IOptionConstants {

    //BS的规划化名称
    public final static String BS_KEY = "OptionBs";

    public final static String DATACACHE_KEY = "dataCache";

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

    public final static String OPTION_DATA = "optionData";

    //Sql语句
    public final static String SQL_QUERY_ALL = "SELECT ID id, NAME name, CODE code, VALUE value,  MODIFY_DATE modifyDate, ENABLE_STATUS enableStatus, DESCRIPTION description FROM GBOX_OPTION";

    public final static String SQL_FIND_BY_ID = SQL_QUERY_ALL + " WHERE ID=?";

    public final static String SQL_UPDATE_BY_ID = "UPDATE GBOX_OPTION SET VALUE=?, MODIFIER_NAME=?, MODIFY_DATE=? WHERE ID=?";

    //表名
    public final static String TABLE_NAME = "GBOX_OPTION";

    //默认启用的查询条件
    public final static String DEFAULT_QUERY_WHERE_ENABLE = " WHERE 1=1 ";

    //默认的子表查询条件
    public final static String[] DEFAULT_CONDITION_KEY_ARRAY = new String[]{"id"};

    //信息字符串
    public final static String DEFAULT_MSG_ERROR_STR = "Error in populate vo from request.";

    public final static String ACCESSIBLE_UPLOADPATH = "ACCESSIBLE_UPLOADPATH";
    public final static String INVALID_FILETYPE = "INVALID_FILETYPE";
    public final static String RESOURCE_SERVERSPATH = "RESOURCE_SERVERSPATH";

}
