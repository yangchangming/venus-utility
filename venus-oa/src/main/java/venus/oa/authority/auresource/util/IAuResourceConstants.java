/*
 * 系统名称:PlatForm
 * 
 * 文件名称: venus.authority.au.auresource.util --> IAuResourceConstants.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2006-06-09 15:32:17.28 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.authority.auresource.util;


/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */

public interface IAuResourceConstants {

    //BS的别名
    String BS_KEY = "AuResource_bs";


    String MESSAGE_AGENT_ERROR = "common/common_error";
    
    String FORWARD_TO_QUERY_ALL_KEY = "queryAll";

    String FORWARD_INIT_FIELD_UPDATE = "authority/au/auresource/fieldmodify";

    String FORWARD_LIST_PAGE_KEY = "authority/au/auresource/list";
    
    String FORWARD_LIST_PAGE_NO_PAGE_KEY = "listPage_noPage";

    String FORWARD_UPDATE_KEY = "authority/au/auresource/modify";

    String FORWARD_FIELD_INSERT_KEY = "authority/au/auresource/fieldinsert";

    String FORWARD_DETAIL_KEY = "detailPage";
    
    String FORWARD_REFERENCE_KEY = "referencePage";

    String FORWARD_FIELD_LIST_KEY = "authority/au/auresource/fieldlist";

    String FORWARD_RECORD_INSERT_KEY = "authority/au/auresource/insert";

    String FORWARD_TEST_KEY = "authority/au/auresource/testSql";
    
    String FORWARD_FINDDEFAULTACCESS_KEY = "findDefaultAccessPage";

    String FORWARD_TABLE_FIELD_LIST = "authority/au/auresource/tablefieldlist";

    String REQUEST_QUERY_ALL_ATBLE = "fieldinsert";

    String REQUEST_QUERY_ALL_RECORD = "recordinsert";



    //request中key值
    String REQUEST_ID_FLAG = "id";
    
    String REQUEST_MULTI_ID_FLAG = "ids";    

    String REQUEST_BEANS_VALUE = "beans";

    String REQUEST_BEAN_VALUE = "bean";
    
    String REQUEST_NAME_VALUE = "name";
    
    String REQUEST_BEAN_FIELD_VALUE = "fieldbean";
    
    String REQUEST_PLOT_PAGE_VALUE = "VENUS_PAGEVO_KEY";
    
    String REQUEST_QUERY_CONDITION_VALUE = "queryCondition";
    
    String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";
    


    //Sql语句
    String SQL_INSERT = "insert into AU_RESOURCE ( ID, RESOURCE_TYPE, IS_PUBLIC, ACCESS_TYPE, NAME, VALUE, TABLE_NAME, PARTY_TYPE, HELP, FILTER_TYPE,FIELD_CHINESENAME,FIELD_NAME,TABLE_CHINESENAME,ENABLE_STATUS, CREATE_DATE) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?, ? )";
    
    String SQL_DELETE_BY_ID = "delete from AU_RESOURCE where ID=?";
    
    String SQL_DELETE_MULTI_BY_IDS = "delete from AU_RESOURCE ";

    String SQL_FIND_BY_ID = "select ID, RESOURCE_TYPE, IS_PUBLIC, ACCESS_TYPE, NAME, VALUE, TABLE_NAME, PARTY_TYPE, HELP, FILTER_TYPE,FIELD_CHINESENAME,FIELD_NAME,TABLE_CHINESENAME,ENABLE_STATUS, ENABLE_DATE, CREATE_DATE, MODIFY_DATE from AU_RESOURCE where ID=?";

    String SQL_UPDATE_BY_ID = "update AU_RESOURCE set RESOURCE_TYPE=?, IS_PUBLIC=?, ACCESS_TYPE=?, NAME=?, VALUE=?, TABLE_NAME=?, PARTY_TYPE=?, HELP=?,FILTER_TYPE=?,FIELD_CHINESENAME=?,FIELD_NAME=?,TABLE_CHINESENAME=?, ENABLE_STATUS=?, MODIFY_DATE=?  where ID=?";
    
    String SQL_UPDATE_TABLE_CHINESENAME = "update AU_RESOURCE set TABLE_CHINESENAME=?  where RESOURCE_TYPE=? and TABLE_NAME=?";
    
    String SQL_COUNT = "select count(*) from AU_RESOURCE";
    
    String SQL_QUERY_ALL = "select ID, RESOURCE_TYPE, IS_PUBLIC, ACCESS_TYPE, NAME, VALUE, TABLE_NAME, PARTY_TYPE, HELP,FILTER_TYPE,FIELD_CHINESENAME,FIELD_NAME,TABLE_CHINESENAME, ENABLE_STATUS, ENABLE_DATE, CREATE_DATE, MODIFY_DATE from AU_RESOURCE";

    String SQL_QUERY_ALL_TABLE = "select distinct t.table_chinesename,t.table_name from au_resource t ";
    
    String SQL_QUERY_ALL_FIELD_TABLE = "select distinct field_name from au_resource ";
    
    //基本常量
    String TABLE_NAME = "AU_RESOURCE";
    
    String TABLE_NAME_CHINESE = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Controlled_resources");
    
    String TABLE_LOG_TYPE_NAME = TABLE_NAME_CHINESE + venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Management");
    
    String DEFAULT_DESC_QUERY_WHERE_ENABLE = " WHERE 1=1 ";
    
    String DEFAULT_DESC_ORDER_BY_ID = ""; //" ORDER BY ID DESC ";
    
    //信息字符串
    String DEFAULT_MSG_ERROR_STR = "从request中自动注值时错误！";
}

