package venus.oa.organization.aupartyrelationtype.util;

public interface IConstants {
    //OID定义引用
    public final static String OID = "AU_PARTYRELATIONTYPE";

    //BS的别名
    public final static String BS_KEY = "aupartyrelationtype_bs";

    public final static String MESSAGE_AGENT_ERROR = "common/common_error";

    public final static String FORWARD_LIST_PAGE_KEY = "authority/org/aupartyrelationtype/list";
    
    public final static String FORWARD_LEFT_TREE_KEY = "authority/org/aupartyrelation/leftTree";
    
    public final static String FORWARD_TO_QUERY_ALL_KEY = "queryAll";

    public final static String FORWARD_UPDATE_KEY = "authority/org/aupartyrelationtype/insert.jsp?isModify=1";
    
    public final static String FORWARD_DETAIL_KEY = "authority/org/aupartyrelationtype/detail";

    public final static String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";

    //查询语句
    public final static String FIELDS = "id,name,keyword,enable_status,enable_date,create_date,modify_date,remark,root_partytype_id";

    public final static String QUERY_SQL = "select " + FIELDS + " from au_partyrelationtype";

    public final static String GETRECORDCOUNT_SQL = "select count(id) from au_partyrelationtype";
    
    public final static String DELETEMULTI_SQL = "update au_partyrelationtype set enable_status=?, enable_date=?  where id=?";

    public final static String INSERT_SQL = "insert into au_partyrelationtype(" + FIELDS + ") values(?,?,?,?,?,?,?,?,?)";

    public final static String SQL_UPDATE_BY_ID = "update au_partyrelationtype set name=?, keyword=?, modify_date=?,remark=? where id=?";

    public final static String PROJECT_VALUE = "wy";

    public final static String QUERY_NAME_SQL = "select id label,name value from au_partyrelationtype where enable_status='1' ";

    public final static String QUERY_BYCONDITION1_SQL = "select " + FIELDS + " from au_partyrelationtype";

    public final static String REQUEST_BEAN_VALUE = "bean";

    public final static String RETURN_VALUE = "Template";

    public final static String RETURN_SUCCESS_FLAG = "success";


    //信息字符串
    public final static String DEFAULT_MSG_ERROR_STR = "Error in populate vo from request.";

}

