package venus.oa.organization.auconnectrule.util;

public interface IConstants {
    //OID定义引用
    public final static String OID = "AU_CONNECTRULE";

    //BS的别名
    public final static String BS_KEY = "au_connectrule_bs";

    public static final String MESSAGE_AGENT_ERROR = "common/common_error";

    public final static String FORWARD_LIST_PAGE_KEY = "authority/org/auconnectrule/list";

    public final static String FORWARD_DETAIL_KEY = "authority/org/auconnectrule/detail";
    
    public final static String FORWARD_TO_QUERY_ALL_KEY = "queryAll";

    public final static String FORWARD_UPDATE_KEY = "authority/org/auconnectrule/insert?isModify=1";
    
    public final static String FORWARD_NEW_KEY = "authority/org/auconnectrule/insert";

    public final static String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";

    public final static String SUB_QUERY_SQL = "(select a.*,b.name relation_type,c.name parent_partytype,"
    	+ " d.name child_partytype from au_connectrule a,au_partyrelationtype b,au_partytype c,au_partytype d "
		+ " where a.relation_type_id=b.id and a.parent_partytype_id=c.id and a.child_partytype_id=d.id) sub_query";
    	
    public final static String QUERY_SQL = "select * from " + SUB_QUERY_SQL;

    public final static String GETRECORDCOUNT_SQL = "select count(id) from "+ SUB_QUERY_SQL;

    public final static String DELETEMULTI_SQL = "delete from au_connectrule";

    public final static String INSERT_SQL = "insert into au_connectrule(id,relation_type_id,parent_partytype_id,child_partytype_id,name,create_date,modify_date,remark) values(?,?,?,?,?,?,?,?)";

    public final static String SQL_UPDATE_BY_ID = "update au_connectrule set relation_type_id=?, parent_partytype_id=?, child_partytype_id=?, name=?, modify_date=?,remark=? where id=?";

    public final static String PROJECT_VALUE = "wy";

    public final static String QUERY_BYCONDITION1_SQL = "select * from "+SUB_QUERY_SQL+" where 1=1 ";

    public final static String REQUEST_BEAN_VALUE = "bean";

    public final static String RETURN_VALUE = "Template";

    public final static String RETURN_SUCCESS_FLAG = "success";

    public final static String BEAN_KEY = "templateBS";
    
    public final static String PARTYTYPEBS_KEY = "aupartytype_bs";
    
    public final static String PARTYRELATIONTYPEBS_KEY = "aupartyrelationtype_bs";
    
    public final static String REQUEST_RELATION_TYPE = "beanRELATION_TYPE";
    
    public final static String REQUEST_PARENT_PARTYTYPE = "beanPARENT_PARTYTYPE_KEY";
    
    public final static String REQUEST_CHILD_PARTYTYPE = "beanCHILD_PARTYTYPE_KEY";

    //信息字符串
    public final static String DEFAULT_MSG_ERROR_STR = "Error in populate vo from request.";

}

