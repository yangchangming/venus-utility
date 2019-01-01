package venus.oa.organization.aupartytype.util;

public interface IConstants {
	 //OID定义引用
    public final static String OID = "AU_PARTYTYPE";

    //BS的别名
    public final static String BS_KEY = "aupartytype_bs";


    public final static String MESSAGE_AGENT_ERROR = "common/common_error";

    public final static String FORWARD_LIST_PAGE_KEY = "authority/org/aupartytype/list";

    public final static String FORWARD_DETAIL_KEY = "authority/org/aupartytype/detail";
    
    public final static String FORWARD_TO_QUERY_ALL_KEY = "queryAll";
    
    public final static String FORWARD_TO_SEARCH_KEY = "search";

    public final static String FORWARD_UPDATE_KEY = "authority/org/aupartytype/insert?isModify=1";

    public final static String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";


    public final static String FIELDS = "id,name,keyword,table_name,code_prefix,enable_status,enable_date,create_date,modify_date,remark";
    
    public final static String QUERY_SQL = "select " + FIELDS + " from au_partytype";

    public final static String GETRECORDCOUNT_SQL = "select count(id) from au_partytype";
    
    public final static String DELETEMULTI_SQL = "update au_partytype set enable_status=?, enable_date=? where id=?";

    public final static String INSERT_SQL = "insert into au_partytype(" + FIELDS + ") values(?,?,?,?,?,?,?,?,?,?)";

    public final static String SQL_UPDATE_BY_ID = "update au_partytype set name=?, keyword=?, table_name=?, code_prefix=?, modify_date=?,remark=? where id=?";

    public final static String PROJECT_VALUE = "wy";

    public final static String QUERY_NAME_SQL = "select id label,name value from au_partytype where enable_status='1'";

    public final static String QUERY_BYCONDITION1_SQL = "select " + FIELDS + "  from au_partytype";

    public final static String REQUEST_BEAN_VALUE = "bean";

    public final static String RETURN_VALUE = "Template";

    public final static String RETURN_SUCCESS_FLAG = "success";


    //信息字符串
    public final static String DEFAULT_MSG_ERROR_STR = "Error in populate vo from request.";


}

