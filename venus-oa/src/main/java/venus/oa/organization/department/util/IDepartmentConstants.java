/*
 * 系统名称:单表模板 --> test
 * 
 * 文件名称: venus.authority.sample.department.util --> IDepartmentConstants.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2007-01-31 14:20:10.041 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.organization.department.util;


/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */

public interface IDepartmentConstants {

    //BS的别名
    String BS_KEY = "Department_bs";
    
    String LOG_BS_KEY = "departmentLogBs";
    
    String FACADE_BS_KEY = "departmentFacadeBs";

    String MESSAGE_AGENT_ERROR = "common/common_error";
    
    String FORWARD_TO_QUERY_ALL_KEY = "queryAll";
    
    String FORWARD_LIST_PAGE_KEY = "authority/sample/department/listDepartment";
    
    String FORWARD_LIST_PAGE_NO_PAGE_KEY = "listPage_noPage";

    String FORWARD_UPDATE_KEY = "authority/sample/department/insertDepartment.jsp?isModify=1";
    
    String FORWARD_DETAIL_KEY = "authority/sample/department/detailDepartment";
    
    String FORWARD_REFERENCE_KEY = "authority/sample/department/referenceDepartment";
    
    String FORWARD_QUERY_TREE_KEY = "authority/sample/relation/list";
    
    //request中key值
    String REQUEST_ID_FLAG = "id";
    
    String REQUEST_MULTI_ID_FLAG = "ids";    

    String REQUEST_BEANS_VALUE = "beans";

    String REQUEST_BEAN_VALUE = "bean";
    
    String REQUEST_PLOT_PAGE_VALUE = "VENUS_PAGEVO_KEY";
    
    //String REQUEST_QUERY_CONDITION_VALUE = "queryCondition";
    
    String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";

    //Sql语句
    String SQL_INSERT = "insert into AU_DEPARTMENT ( ID, DEPT_NO, DEPT_NAME, DEPT_FLAG, DEPT_TYPE, DEPT_LEVEL, DEPT_LEADER, REMARK, ENABLE_STATUS, ENABLE_DATE, CREATE_DATE, MODIFY_DATE, COLUMN1, COLUMN2, COLUMN3) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
    
    String SQL_DELETE = "delete from AU_DEPARTMENT ";

    String SQL_UPDATE = "update AU_DEPARTMENT set DEPT_NO=?, DEPT_NAME=?, DEPT_FLAG=?, DEPT_TYPE=?, DEPT_LEVEL=?, DEPT_LEADER=?, REMARK=?, ENABLE_STATUS=?, ENABLE_DATE=?, CREATE_DATE=?, MODIFY_DATE=?, COLUMN1=?, COLUMN2=?, COLUMN3=?  where ID=?";
    
    String SQL_COUNT = "select count(distinct A.ID) from AU_DEPARTMENT  A inner join AU_PARTYRELATION B on A.ID=B.PARTYID ";
    
    String SQL_QUERY = "select distinct A.ID, A.DEPT_NO, A.DEPT_NAME, A.DEPT_FLAG, A.DEPT_TYPE, A.DEPT_LEVEL, A.DEPT_LEADER, A.REMARK, A.ENABLE_STATUS, A.ENABLE_DATE, A.CREATE_DATE, A.MODIFY_DATE, A.COLUMN1, A.COLUMN2, A.COLUMN3 from AU_DEPARTMENT A inner join AU_PARTYRELATION B on A.ID=B.PARTYID ";

    //基本常量
    String TABLE_NAME = "AU_DEPARTMENT";
    
    String TABLE_NAME_CHINESE = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Department0");
    
    String TABLE_LOG_TYPE_NAME = TABLE_NAME_CHINESE + venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Management");
    
    String DEFAULT_DESC_QUERY_WHERE_ENABLE = " WHERE 1=1 ";
    
    String DEFAULT_DESC_ORDER_BY_ID = ""; //" ORDER BY ID DESC ";
    
    //信息字符串
    String DEFAULT_MSG_ERROR_STR = "从request中自动注值时错误！";
}

