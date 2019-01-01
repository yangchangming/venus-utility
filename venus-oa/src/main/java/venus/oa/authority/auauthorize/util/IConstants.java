package venus.oa.authority.auauthorize.util;

public interface IConstants {
	//OID定义引用
    String OID = "AU_AUTHORIZE";
    
    String TAGID = "AU_AUTHORIZE_LOGTAG";

    //BS的别名
    String BS_KEY = "auauthorize_bs";

    String MESSAGE_AGENT_ERROR = "common/common_error";
    
    String FORWARD_TO_AU_FRAME_KEY = "authority/au/auauthorize/auFrame";
    
    String FORWARD_TO_RETMESSAGE_KEY = "authority/au/auauthorize/retMessage";

    String FORWARD_TO_AU_FUNC_KEY = "authority/au/auauthorize/function";
    
    String FORWARD_TO_AU_FILD_KEY = "authority/au/auauthorize/field";
    
    String FORWARD_TO_AU_RECD_KEY = "authority/au/auauthorize/record";
    
    String FORWARD_TO_AU_ORGA_KEY = "org";
    
    String FORWARD_TO_VIEW_FUNC_KEY = "authority/au/auauthorize/viewFunction";
    
    String FORWARD_TO_VIEW_FILD_KEY = "authority/au/auauthorize/viewField";
    
    String FORWARD_TO_VIEW_RECD_KEY = "authority/au/auauthorize/viewRecord";
    
    String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";

    //查询语句

    String FIELDS = "AU_AUTHORIZE.ID,AU_AUTHORIZE.VISITOR_ID,AU_AUTHORIZE.VISITOR_CODE,AU_AUTHORIZE.VISITOR_TYPE,AU_AUTHORIZE.RESOURCE_ID,AU_AUTHORIZE.RESOURCE_CODE,AU_AUTHORIZE.RESOURCE_TYPE,AU_AUTHORIZE.AUTHORIZE_STATUS,AU_AUTHORIZE.ACCESS_TYPE,AU_AUTHORIZE.IS_APPEND,AU_AUTHORIZE.CREATE_DATE,AU_AUTHORIZE.SYSTEM_ID";
    
    String QUERY_ALL_SQL = "select " + FIELDS + " from AU_AUTHORIZE";
    
    String QUERY_AUTHORITY_LIST = "SELECT A.*, B.NAME VISITOR_NAME, C.NAME RESOURCE_NAME FROM AU_AUTHORIZE A, AU_VISITOR B, AU_RESOURCE C  WHERE A.VISITOR_ID = B.ID AND A.RESOURCE_ID = C.ID AND A.VISITOR_ID = ?";

    String GET_RECORD_COUNT_SQL = "select count(*) from AU_AUTHORIZE";
    
    String DELETE_BY_ID_SQL = "delete from AU_AUTHORIZE where id=?";
	
	String DELETE_ALL_SQL = "delete from AU_AUTHORIZE ";
	
	String DELETE_FOR_APPENDDATA_SQL = "delete from AU_APPENDDATA ";

	String INSERT_SQL = "insert into AU_AUTHORIZE(" + FIELDS + ") values(?,?,?,?,?,?,?,?,?,?,?,?)";

    String UPDATE_BY_ID_SQL = "update AU_AUTHORIZE set VISITOR_ID=?,VISITOR_CODE=?,VISITOR_TYPE=?,RESOURCE_ID=?,RESOURCE_CODE=?,RESOURCE_TYPE=?,AUTHORIZE_STATUS=?,ACCESS_TYPE=?,IS_APPEND=? where ID=?";

    //变量名称
    String PROJECT_VALUE = "PF_AU";

    String REQUEST_BEAN_VALUE = "bean";

    String RETURN_VALUE = "Template";

    String RETURN_SUCCESS_FLAG = "success";

    //信息字符串
    String DEFAULT_MSG_ERROR_STR = "Error in populate vo from request.";

}

