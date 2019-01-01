/*
 * 系统名称:PlatForm
 * 
 * 文件名称: venus.authority.au.auuser.util --> IAuUserConstants.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2006-06-09 15:32:04.608 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.authority.auuser.util;


/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */

public interface IAuUserConstants {

    //BS的别名
    String BS_KEY = "AuUser_bs";


    String MESSAGE_AGENT_ERROR = "common/common_error";

    String FORWARD_TO_QUERY_ALL_KEY = "queryAll";
    
    String FORWARD_LIST_PAGE_KEY = "authority/au/auuser/listAuUser";
    
    String FORWARD_LIST_PAGE_NO_PAGE_KEY = "listPage_noPage";

    String FORWARD_UPDATE_KEY = "authority/au/auuser/insertAuUser.jsp?isModify=1";

    String FORWARD_MODIFY_PWD_FRAME = "authority/au/auuser/modifyPasswordFrame";

    String FORWARD_FORCE_MODIFY_PWD_FRAME  = "authority/au/auuser/forceModifyPasswordFrame";

    String FORWARD_DETAIL_KEY = "authority/au/auuser/detailAuUser";
    
    String FORWARD_REFERENCE_KEY = "referencePage";

    String FORWARD_TO_REGISTER_KEY = "login/registerSucess";
    
    String FORWARD_VALIDATE_KEY = "login/validateResult";

    //request中key值
    String REQUEST_ID_FLAG = "id";
    
    String REQUEST_MULTI_ID_FLAG = "ids";    

    String REQUEST_BEANS_VALUE = "beans";

    String REQUEST_BEAN_VALUE = "bean";
    
    String REQUEST_PLOT_PAGE_VALUE = "VENUS_PAGEVO_KEY";
    
    String REQUEST_QUERY_CONDITION_VALUE = "queryCondition";
    
    String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";
    
    //Sql语句
    String SQL_INSERT = "insert into AU_USER ( ID, PARTY_ID, LOGIN_ID, PASSWORD, NAME, IS_ADMIN, AGENT_STATUS, ENABLE_STATUS, SYSTEM_CODE, CREATE_DATE,RETIRE_DATE) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)";
    
    String SQL_INSERT_WITHOUTRETIREDATE = "insert into AU_USER ( ID, PARTY_ID, LOGIN_ID, PASSWORD, NAME, IS_ADMIN, AGENT_STATUS, ENABLE_STATUS, SYSTEM_CODE, CREATE_DATE) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    String SQL_DELETE_BY_ID = "delete from AU_USER where ID=?";
    
    String SQL_DELETE_MULTI_BY_IDS = "delete from AU_USER ";

    String SQL_FIND_BY_ID = "select AU_USER.ID, AU_USER.PARTY_ID, AU_USER.LOGIN_ID, AU_USER.PASSWORD, AU_USER.NAME, AU_USER.IS_ADMIN, AU_USER.AGENT_STATUS, AU_USER.SYSTEM_CODE, AU_USER.CREATE_DATE, AU_USER.MODIFY_DATE, AU_USER.ENABLE_STATUS,AU_USER.FAILED_TIMES,AU_USER.RETIRE_DATE from AU_USER where AU_USER.ID=?";

    String SQL_FIND_BY_LOGIN_ID = "select AU_USER.ID, AU_USER.PARTY_ID, AU_USER.LOGIN_ID, AU_USER.PASSWORD, AU_USER.NAME, AU_USER.IS_ADMIN, AU_USER.AGENT_STATUS, AU_USER.SYSTEM_CODE, AU_USER.CREATE_DATE, AU_USER.MODIFY_DATE, AU_USER.ENABLE_STATUS,AU_USER.FAILED_TIMES,AU_USER.RETIRE_DATE from AU_USER where AU_USER.LOGIN_ID=?";

    String SQL_UPDATE_BY_ID = "update AU_USER set PARTY_ID=?, LOGIN_ID=?, PASSWORD=?, NAME=?, IS_ADMIN=?, AGENT_STATUS=?, ENABLE_STATUS=?, SYSTEM_CODE=?, MODIFY_DATE=?,FAILED_TIMES=?,RETIRE_DATE=?  where ID=?";
    
    String SQL_UPDATE_BY_ID_WITHOUTRETIRYDATE = "update AU_USER set PARTY_ID=?, LOGIN_ID=?, PASSWORD=?, NAME=?, IS_ADMIN=?, AGENT_STATUS=?, ENABLE_STATUS=?, SYSTEM_CODE=?, MODIFY_DATE=?,FAILED_TIMES=? where ID=?";
    
    String SQL_COUNT = "select count(*) from AU_USER";
    
    String SQL_COUNT_LIMIT = "select count(distinct A.ID) from AU_USER A inner join AU_PARTYRELATION B on A.PARTY_ID=B.PARTYID ";
    
    String SQL_QUERY_ALL = "select AU_USER.ID, AU_USER.PARTY_ID, AU_USER.LOGIN_ID, AU_USER.PASSWORD, AU_USER.NAME, AU_USER.IS_ADMIN, AU_USER.AGENT_STATUS, AU_USER.SYSTEM_CODE, AU_USER.CREATE_DATE, AU_USER.MODIFY_DATE, AU_USER.ENABLE_STATUS,AU_USER.FAILED_TIMES,AU_USER.RETIRE_DATE from AU_USER";
    
    String SQL_QUERY_ALL_LIMIT = "select distinct A.ID, A.PARTY_ID, A.LOGIN_ID, A.PASSWORD, A.NAME, A.IS_ADMIN, A.AGENT_STATUS, A.SYSTEM_CODE, A.CREATE_DATE, A.MODIFY_DATE, A.ENABLE_STATUS,A.FAILED_TIMES,A.RETIRE_DATE from AU_USER A inner join AU_PARTYRELATION B on A.PARTY_ID=B.PARTYID ";

    String SQL_COUNT_ORG = "select count(distinct b.PARTYID) from AU_USER a, AU_PARTYRELATION b where a.PARTY_ID=b.PARTYID";
    
    String SQL_QUERY_ALL_ORG = "select distinct b.PARTYID, a.ID, a.PARTY_ID, a.LOGIN_ID, a.PASSWORD, a.NAME, a.IS_ADMIN, a.AGENT_STATUS, a.SYSTEM_CODE, a.CREATE_DATE, a.MODIFY_DATE, a.ENABLE_STATUS,A.FAILED_TIMES,A.RETIRE_DATE from AU_USER a, AU_PARTYRELATION b where a.PARTY_ID=b.PARTYID";
    
    String SQL_QUERY_NOACCOUNTUSER = "SELECT DISTINCT partyid party_id,name FROM au_partyrelation ";
    
    String SQL_FIND_PARTYID = "SELECT party_id FROM  au_user";

    //基本常量
    String TABLE_NAME = "AU_USER";
    
    String TABLE_NAME_CHINESE = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Login");
    
    String TABLE_LOG_TYPE_NAME = TABLE_NAME_CHINESE + venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Management");
    
    String DEFAULT_DESC_QUERY_WHERE_ENABLE = " WHERE 1=1 ";
    
    String DEFAULT_DESC_ORDER_BY_ID = ""; //" ORDER BY ID DESC ";
    
    //信息字符串
    String DEFAULT_MSG_ERROR_STR = "从request中自动注值时错误！";
}

