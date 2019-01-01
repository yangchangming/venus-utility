/*
 * 系统名称:VENUS 组织权限系统
 * 
 * 文件名称: venus.authority.login.loginlog.util --> ILoginLogConstants.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2007-10-16 10:30:00.333 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.login.loginlog.util;


/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */

public interface ILoginLogConstants {

    //BS的规划化名称
    String BS_KEY = "ILoginLogBs";

    //struts页面跳转
    String FORWARD_TO_QUERY_ALL = "toQueryAll";
    
    String FORWARD_LIST_PAGE = "authority/au/loginlog/listLoginLog";

    String FORWARD_UPDATE_PAGE = "authority/au/loginlog/insertLoginLog.jsp?isModify=1";
    
    String FORWARD_DETAIL_PAGE = "authority/au/loginlog/detailLoginLog";
    
    String FORWARD_REFERENCE_PAGE = "referencePage";
    
    String FORWARD_STATISTIC_PAGE = "statisticPage";
    
    String FORWARD_TO_SORT_PAGE = "toSortPage";
    
    String FORWARD_SORT_PAGE = "sortPage";
    
    //request处理中的key值
    String REQUEST_ID = "id";
    
    String REQUEST_IDS = "ids";    

    String REQUEST_BEAN = "bean";
    
    String REQUEST_BEANS = "beans";
    
    //String REQUEST_QUERY_CONDITION = "queryCondition";
    
    String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";
    
    String REQUEST_REFERENCE_INPUT_TYPE = "referenceInputType";
    
    String REQUEST_SORT_MAP = "sortMap";
    
    String REQUEST_SORT_SELECT_MULTIPLE = "sortSelectMultiple";

    //Sql语句
    String SQL_INSERT = "insert into AU_LOGIN_LOG ( ID, LOGIN_ID, NAME, LOGIN_IP, PARTY_ID, IE, OS, HOST, LOGOUT_TYPE, LOGIN_TIME, LOGOUT_TIME, LOGIN_STATE,LOGIN_MAC) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    String SQL_DELETE_BY_ID = "delete from AU_LOGIN_LOG where ID=?";
    
    String SQL_DELETE_MULTI_BY_IDS = "delete from AU_LOGIN_LOG ";

    String SQL_FIND_BY_ID = "select AU_LOGIN_LOG.ID, AU_LOGIN_LOG.LOGIN_ID, AU_LOGIN_LOG.NAME, AU_LOGIN_LOG.LOGIN_IP, AU_LOGIN_LOG.PARTY_ID, AU_LOGIN_LOG.IE, AU_LOGIN_LOG.OS, AU_LOGIN_LOG.HOST, AU_LOGIN_LOG.LOGOUT_TYPE, AU_LOGIN_LOG.LOGIN_TIME, AU_LOGIN_LOG.LOGOUT_TIME, AU_LOGIN_LOG.LOGIN_STATE, AU_LOGIN_LOG.LOCK_TIME, AU_LOGIN_LOG.LOGIN_MAC from AU_LOGIN_LOG where AU_LOGIN_LOG.ID=?";

    String SQL_UPDATE_BY_ID = "update AU_LOGIN_LOG set LOGIN_ID=?, NAME=?, LOGIN_IP=?,PARTY_ID=?, IE=?, OS=?, HOST=?, LOGOUT_TYPE=?, LOGOUT_TIME=?, LOCK_TIME=?,LOGIN_MAC=?  where ID=?";
    
    String SQL_COUNT = "SELECT COUNT(DISTINCT A.ID) FROM AU_LOGIN_LOG A LEFT JOIN AU_PARTYRELATION B ON A.PARTY_ID=B.PARTYID WHERE 1=1 ";
    
    String SQL_QUERY_ALL = "SELECT DISTINCT A.ID,A.LOGIN_ID,A.NAME,A.LOGIN_IP,A.PARTY_ID,A.IE,A.OS,A.HOST,A.LOGOUT_TYPE,A.LOGIN_TIME,A.LOGOUT_TIME,A.LOGIN_STATE,A.LOCK_TIME,A.LOGIN_MAC FROM AU_LOGIN_LOG A LEFT JOIN AU_PARTYRELATION B ON A.PARTY_ID=B.PARTYID WHERE 1=1 ";

    //消息定义
    String MESSAGE_NO_CONDITION_KEY = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Not_query_keywords_");
    
    //表名
    String TABLE_NAME = "AU_LOGIN_LOG";
    
    //表名汉化
    String TABLE_NAME_CHINESE = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Log_log");
    
    //日志类型名称
    String TABLE_LOG_TYPE_NAME = TABLE_NAME_CHINESE + venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Management");
    
    //默认启用的查询条件
    String DEFAULT_QUERY_WHERE_ENABLE = " WHERE 1=1 ";
    
    //默认的排序字段
    String DEFAULT_ORDER_CODE = "LOGIN_TIME DESC";
    
    //默认的子表查询条件
    String[] DEFAULT_CONDITION_KEY_ARRAY = new String[]{"id" };
}

