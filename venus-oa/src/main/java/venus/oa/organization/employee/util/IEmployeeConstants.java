/*
 * 系统名称:单表模板 --> test
 * 
 * 文件名称: venus.authority.sample.employee.util --> IEmployeeConstants.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2007-01-31 14:20:08.494 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.organization.employee.util;

import venus.oa.util.GlobalConstants;


/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */

public interface IEmployeeConstants{

    //BS的别名
    String BS_KEY = "Employee_bs";
    
    String LOG_BS_KEY = "employeeLogBs";
    
    String FACADE_BS_KEY = "employeeFacadeBs";

    String MESSAGE_AGENT_ERROR = "common/common_error";
    
    String FORWARD_TO_QUERY_ALL_KEY = "queryAll";
    
    String FORWARD_LIST_PAGE_KEY = "authority/sample/employee/listEmployee";
    
    String FORWARD_LIST_PAGE_NO_PAGE_KEY = "listPage_noPage";

    String FORWARD_UPDATE_KEY = "authority/sample/employee/insertEmployee.jsp?isModify=1";
    
    String FORWARD_DETAIL_KEY = "authority/sample/employee/detailEmployee";
    
    String FORWARD_REFERENCE_KEY = "authority/sample/employee/referenceEmployee";
    
    String FORWARD_QUERY_TREE_KEY = "authority/sample/relation/list";
    
    //request中key值
    String REQUEST_ID_FLAG = "id";
    
    String REQUEST_MULTI_ID_FLAG = "ids";    

    String REQUEST_BEANS_VALUE = "beans";

    String REQUEST_BEAN_VALUE = "bean";
    
    String REQUEST_PLOT_PAGE_VALUE = "VENUS_PAGEVO_KEY";
    
    String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";

    //Sql语句
    String SQL_INSERT = "insert into AU_EMPLOYEE ( ID, PERSON_NO, PERSON_NAME, ENGLISH_NAME, PERSON_TYPE, SEX, MOBILE, TEL, EMAIL, ADDRESS, POSTALCODE, REMARK, ENABLE_STATUS, ENABLE_DATE, CREATE_DATE, MODIFY_DATE, COLUMN1, COLUMN2, COLUMN3) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
    
    String SQL_DELETE = "delete from AU_EMPLOYEE";

    String SQL_UPDATE = "update AU_EMPLOYEE set PERSON_NO=?, PERSON_NAME=?, ENGLISH_NAME=?, PERSON_TYPE=?, SEX=?, MOBILE=?, TEL=?, EMAIL=?, ADDRESS=?, POSTALCODE=?, REMARK=?, ENABLE_STATUS=?, ENABLE_DATE=?, CREATE_DATE=?, MODIFY_DATE=?, COLUMN1=?, COLUMN2=?, COLUMN3=?  where ID=? ";
    
    String SQL_COUNT = "select count(distinct A.ID) from AU_EMPLOYEE  A inner join AU_PARTYRELATION B on A.ID=B.PARTYID ";
    
    String SQL_QUERY = "select distinct A.ID, A.PERSON_NO, A.PERSON_NAME, A.ENGLISH_NAME, A.PERSON_TYPE, A.SEX, A.MOBILE, A.TEL, A.EMAIL, A.ADDRESS, A.POSTALCODE, A.REMARK, A.ENABLE_STATUS, A.ENABLE_DATE, A.CREATE_DATE, A.MODIFY_DATE, A.COLUMN1, A.COLUMN2, A.COLUMN3 from AU_EMPLOYEE  A inner join AU_PARTYRELATION B on A.ID=B.PARTYID ";
    
    String SQL_COMPANY_TYPE = " and B.RELATIONTYPE_ID='"+ GlobalConstants.getRelaType_comp()+"' ";

    //基本常量
    String TABLE_NAME = "AU_EMPLOYEE";
    
    String TABLE_NAME_CHINESE = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Staff0");
    
    String TABLE_LOG_TYPE_NAME = TABLE_NAME_CHINESE + venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Management");
    
    String DEFAULT_DESC_QUERY_WHERE_ENABLE = " WHERE 1=1 ";
    
    String DEFAULT_DESC_ORDER_BY_ID = ""; //" ORDER BY ID DESC ";
    
    //信息字符串
    String DEFAULT_MSG_ERROR_STR = "从request中自动注值时错误！";
}

