/*
 * 系统名称:PlatForm
 * 
 * 文件名称: venus.authority.au.aufunctree.util --> IAuFunctreeConstants.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2006-06-09 15:32:55.043 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.authority.aufunctree.util;


/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */

public interface IAuFunctreeConstants {

    String BS_KEY = "AuFunctree_bs";

    String MESSAGE_AGENT_ERROR = "common/common_error";
    
    String FORWARD_TO_QUERY_ALL_KEY = "queryAll";
    
    String FORWARD_LIST_PAGE_KEY = "authority/au/aufunctree/listAuFunctree";
    
    String FORWARD_LIST_PAGE_NO_PAGE_KEY = "listPage_noPage";

    String FORWARD_UPDATE_KEY = "authority/au/aufunctree/insertAuFunctree.jsp?isModify=1";
    
    String FORWARD_DETAIL_KEY = "authority/au/aufunctree/detailAuFunctree";
    
    String FORWARD_REFERENCE_KEY = "referencePage";
    
    //request中key值
    String REQUEST_ID_FLAG = "id";
    
    String REQUEST_MULTI_ID_FLAG = "ids";    

    String REQUEST_BEANS_VALUE = "beans";

    String REQUEST_BEAN_VALUE = "bean";
    
    String REQUEST_PLOT_PAGE_VALUE = "VENUS_PAGEVO_KEY";
    
    String REQUEST_QUERY_CONDITION_VALUE = "queryCondition";
    
    String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";
    
    //20101201由于虚拟主机技术实现国际化菜单而关闭sql功能 start
    /*//Sql语句
    String SQL_INSERT = "insert into AU_FUNCTREE ( ID, KEYWORD, TYPE, CODE, PARENT_CODE, TOTAL_CODE, NAME, HOT_KEY, HELP, URL, IS_LEAF, TYPE_IS_LEAF, ORDER_CODE, SYSTEM_ID, CREATE_DATE, TREE_LEVEL,IS_SSL,IS_PUBLIC,TREE_ID) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    String SQL_DELETE_BY_ID = "delete from AU_FUNCTREE where ID=?";
    
    String SQL_DELETE_MULTI_BY_IDS = "delete from AU_FUNCTREE ";

    String SQL_FIND_BY_ID = "select AU_FUNCTREE.ID, AU_FUNCTREE.KEYWORD, AU_FUNCTREE.TYPE, AU_FUNCTREE.CODE, AU_FUNCTREE.PARENT_CODE, AU_FUNCTREE.TOTAL_CODE, AU_FUNCTREE.NAME, AU_FUNCTREE.HOT_KEY, AU_FUNCTREE.HELP, AU_FUNCTREE.URL, AU_FUNCTREE.IS_LEAF, AU_FUNCTREE.TYPE_IS_LEAF, AU_FUNCTREE.ORDER_CODE, AU_FUNCTREE.SYSTEM_ID, AU_FUNCTREE.CREATE_DATE, AU_FUNCTREE.MODIFY_DATE, AU_FUNCTREE.TREE_LEVEL,AU_FUNCTREE.IS_SSL,AU_FUNCTREE.IS_PUBLIC from AU_FUNCTREE where AU_FUNCTREE.ID=?";

    String SQL_UPDATE_BY_ID = "update AU_FUNCTREE set KEYWORD=?, TYPE=?, CODE=?, PARENT_CODE=?, TOTAL_CODE=?, NAME=?, HOT_KEY=?, HELP=?, URL=?, IS_LEAF=?, TYPE_IS_LEAF=?, ORDER_CODE=?, SYSTEM_ID=?, MODIFY_DATE=?,IS_SSL=?,IS_PUBLIC=? where ID=?";
    
    String SQL_COUNT = "select count(*) from AU_FUNCTREE";
    
    String SQL_QUERY_ALL = "select AU_FUNCTREE.ID, AU_FUNCTREE.KEYWORD, AU_FUNCTREE.TYPE, AU_FUNCTREE.CODE, AU_FUNCTREE.PARENT_CODE, AU_FUNCTREE.TOTAL_CODE, AU_FUNCTREE.NAME, AU_FUNCTREE.HOT_KEY, AU_FUNCTREE.HELP, AU_FUNCTREE.URL, AU_FUNCTREE.IS_LEAF, AU_FUNCTREE.TYPE_IS_LEAF, AU_FUNCTREE.ORDER_CODE, AU_FUNCTREE.SYSTEM_ID, AU_FUNCTREE.CREATE_DATE, AU_FUNCTREE.MODIFY_DATE, AU_FUNCTREE.TREE_LEVEL,AU_FUNCTREE.IS_SSL,AU_FUNCTREE.IS_PUBLIC from AU_FUNCTREE";

    //基本常量
    String TABLE_NAME = "AU_FUNCTREE";
    
    String TABLE_NAME_CHINESE = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Function_menu");
    
    String TABLE_LOG_TYPE_NAME = TABLE_NAME_CHINESE + venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Management");
    
    String DEFAULT_DESC_QUERY_WHERE_ENABLE = " WHERE 1=1 ";
    
    String DEFAULT_DESC_ORDER_BY_ID = ""; //" ORDER BY ID DESC ";
     */    
    //20101201由于虚拟主机技术实现国际化菜单而关闭sql功能 end
    
    //信息字符串
    String DEFAULT_MSG_ERROR_STR = "从request中自动注值时错误！";
}

