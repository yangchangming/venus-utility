/*
 * 系统名称:单表模板 --> test
 * 
 * 文件名称: venus.authority.sample.position.util --> IPositionConstants.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2007-01-31 14:20:07.103 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.organization.position.util;


/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */

public interface IPositionConstants {

    //BS的别名
    public final static String BS_KEY = "Position_bs";
    
    public final static String LOG_BS_KEY = "positionLogBs";
    
    public final static String FACADE_BS_KEY = "positionFacadeBs";


    String MESSAGE_AGENT_ERROR = "common/common_error";

    public final static String FORWARD_TO_QUERY_ALL_KEY = "queryAll";
    
    public final static String FORWARD_LIST_PAGE_KEY = "authority/sample/position/listPosition";
    
    public final static String FORWARD_LIST_PAGE_NO_PAGE_KEY = "listPage_noPage";

    public final static String FORWARD_UPDATE_KEY = "authority/sample/position/insertPosition.jsp?isModify=1";
    
    public final static String FORWARD_DETAIL_KEY = "authority/sample/position/detailPosition";
    
    public final static String FORWARD_REFERENCE_KEY = "authority/sample/position/referencePosition";
    
    public final static String FORWARD_QUERY_TREE_KEY = "authority/sample/relation/list";
    
    //request中key值
    public final static String REQUEST_ID_FLAG = "id";
    
    public final static String REQUEST_MULTI_ID_FLAG = "ids";    

    public final static String REQUEST_BEANS_VALUE = "beans";

    public final static String REQUEST_BEAN_VALUE = "bean";
    
    public final static String REQUEST_PLOT_PAGE_VALUE = "VENUS_PAGEVO_KEY";
    
    public final static String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";

    //Sql语句
    public final static String SQL_INSERT = "insert into AU_POSITION ( ID, POSITION_NO, POSITION_NAME, POSITION_FLAG, POSITION_TYPE, POSITION_LEVEL, LEADER_FLAG, LEADER_LEVEL, REMARK, ENABLE_STATUS, ENABLE_DATE, CREATE_DATE, MODIFY_DATE, COLUMN1, COLUMN2, COLUMN3) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

    public final static String SQL_UPDATE = "update AU_POSITION set POSITION_NO=?, POSITION_NAME=?, POSITION_FLAG=?, POSITION_TYPE=?, POSITION_LEVEL=?, LEADER_FLAG=?, LEADER_LEVEL=?, REMARK=?, ENABLE_STATUS=?, ENABLE_DATE=?, CREATE_DATE=?, MODIFY_DATE=?, COLUMN1=?, COLUMN2=?, COLUMN3=?  where ID=?";

    public final static String SQL_QUERY = "select distinct A.ID, A.POSITION_NO, A.POSITION_NAME, A.POSITION_FLAG, A.POSITION_TYPE, A.POSITION_LEVEL, A.LEADER_FLAG, A.LEADER_LEVEL, A.REMARK, A.ENABLE_STATUS, A.ENABLE_DATE, A.CREATE_DATE, A.MODIFY_DATE, A.COLUMN1, A.COLUMN2, A.COLUMN3 from AU_POSITION  A inner join AU_PARTYRELATION B on A.ID=B.PARTYID ";
    
    public final static String SQL_COUNT = "select count(distinct A.ID) from AU_POSITION  A inner join AU_PARTYRELATION B on A.ID=B.PARTYID ";
    
    public final static String SQL_DELETE = "delete from AU_POSITION ";
    
    //基本常量
    public final static String TABLE_NAME = "AU_POSITION";
    
    public final static String TABLE_NAME_CHINESE = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Post");
    
    public final static String TABLE_LOG_TYPE_NAME = TABLE_NAME_CHINESE + venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Management");
    
    public final static String DEFAULT_DESC_QUERY_WHERE_ENABLE = " WHERE 1=1 ";
    
    public final static String DEFAULT_DESC_ORDER_BY_ID = ""; //" ORDER BY ID DESC ";
    
    //信息字符串
    public final static String DEFAULT_MSG_ERROR_STR = "从request中自动注值时错误！";
}

