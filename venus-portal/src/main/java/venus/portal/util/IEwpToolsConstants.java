package venus.portal.util;

import venus.frames.base.action.DefaultForward;

public interface IEwpToolsConstants {
    
   public final static String DICTIONARY_EWP_ENABLE_STATUS = "EWP_ENABLE_STATUS";
    
    public final static String DICTIONARY_EWP_HAS_OR_NOT = "EWP_HAS_OR_NOT";
    
    public final static String DICTIONARY_EWP_YES_OR_NOT = "EWP_YES_OR_NOT";
    
    public final static String DICTIONARY_EWP_ACCESSORY_TYPE = "EWP_ACCESSORY_TYPE";
    
    public final static int HR_DEFAULT_BATCH_PAGE_NUMBER = 10000; //默认批处理的记录数

    public final static int HR_DEFAULT_BATCH_SQL_IN_NUMBER = 200; //默认批处理in的个数

    public final static int HR_DEFAULT_CURRENT_PAGE = 1; //默认批处理的开始页数

    public final static String HR_BREAK_PK_LETTER = "^";  //  默认分隔符

    public final static int MAX_RECURSIVE_COUNT = 50; //最大递归数
    
    public final static String DESC_CREATE_DATE = "create_date";  //描述创建时间
    
    public final static String DESC_CREATE_IP = "create_ip";  //描述创建IP
    
    public final static String DESC_CREATE_USER_ID = "create_user_id";  //描述创建用户ID
    
    public final static String DESC_MODIFY_DATE = "modify_date";  //描述修改时间
    
    public final static String DESC_MODIFY_IP = "modify_ip";  //描述修改IP
    
    public final static String DESC_MODIFY_USER_ID = "modify_user_id";  //描述修改用户ID
    
    public final static String DESC_ENABLE_DATE = "enable_date";  //描述启用/禁用时间
    
    public final static String DESC_ENABLE_IP = "enable_ip";  //描述启用/禁用IP
    
    public final static String DESC_DELETE_DATE = "delete_date";  //描述删除时间
    
    public final static String DESC_DELETE_IP = "delete_ip";  //描述删除IP
    
    public final static String DESC_USABLE_STATUS = "usable_status";
    
    public final static String DESC_ORDER_CODE = "ID"; //默认排序字段

    public final static String STATUS_ENABLE = "1";  //启用标志
    
    public final static String STATUS_DISABLE = "0";  //禁用标志
    
    public final static String EWP_YES = "1";  //true的定义
    
    public final static String EWP_NO = "0";  //否的定义
    
    public final static String DESC_USABLE_STATUS_EVALUATE_ENABLE = DESC_USABLE_STATUS + "='" + STATUS_ENABLE + "'";
    
    public final static String DESC_USABLE_STATUS_EVALUATE_DISABLE = DESC_USABLE_STATUS + "='" + STATUS_DISABLE + "'";
    
    public final static String AUTHORIZE_CALL_CODE_FUNCTREE = "100";
    
    public final static String AUTHORIZE_CALL_CODE_INFOSTYLE = "101101";
    
    public final static String AUTHORIZE_CALL_CODE_CHANNEL = "101102";
    
    public final static String AUTHORIZE_CALL_CODE_INFO = "101103";
    
    public final static String AUTHORIZE_CALL_CODE_COMPANYSITE = "101104";
    
    public final static String AUTHORIZE_CALL_CODE_CONTROLINFO = "101105";
    
    public final static String AUTHORIZE_CALL_CODE_DATA = "102";
    
    public final static String AUTHORIZE_CALL_CODE_SUBSET = "103";
    
    public final static String REQUEST_WRITE_BACK_WORK_FLOW = "request_write_back_work_flow";
    
    public final static String REQUEST_FORM_ELEMENTS_AUTHORIZE = "request_form_elements_authorize";
    
    public final static String REQUEST_IS_READ_ONLY = "request_is_read_only";

    public final static String REQUEST_TRANSFER_XML_OBJECT = "request_transfer_xml_object";
    
    public final static String REQUEST_STATISTIC_HANDLER = "statisticHandler";
    
    public final static String REQUEST_PLOT_PAGE = "VENUS_PAGEVO_KEY";
    
    /**空白字符串*/
    public final static String  EMPTY_LINE = "";
    
    /**默认的跳转对象*/
    public final static DefaultForward DEFAULT_FORWARD = new DefaultForward();
}
