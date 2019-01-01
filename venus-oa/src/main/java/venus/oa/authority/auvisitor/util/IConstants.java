package venus.oa.authority.auvisitor.util;

public interface IConstants {
	 //OID定义引用
    String OID = "AU_VISITOR";

    //BS的别名
    String BS_KEY = "auvisitor_bs";

    String MESSAGE_AGENT_ERROR = "common/common_error";

    String FORWARD_LIST_PAGE_KEY = "listPage";

    String FORWARD_PARTY_TYPES_KEY = "authority/au/auvisitor/auList";

    String FORWARD_TO_QUERY_ALL_KEY = "queryAll";

    String FORWARD_UPDATE_KEY = "updatePage";
    
    String FORWARD_DETAIL_KEY = "detailPage";

    String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";

    //查询语句    

    String FIELDS = "id,original_id,partyrelationtype_id,partytype_id,visitor_type,name,code,create_date,modify_date";
    
    String QUERY_BY_ORGID = "select " + FIELDS + " from au_visitor where original_id=?";
    
    String QUERY_BY_ID = "select " + FIELDS + " from au_visitor where id=?";
    
    String QUERY_BY_ORGID_TYPE = "select " + FIELDS + " from au_visitor where original_id=? and visitor_type=?";
    
    String SUBQUERY = "select a.*,b.name partyrelationtype,d.name||'-->'||c.name parentName from au_visitor a,au_partyrelationtype b,au_partyrelation c, au_partyrelation d where a.partyrelationtype_id=b.id(+) and substr(a.code,1,length(a.code)-5)=c.code(+) and substr(a.code,1,24)=d.code(+)";
    
    String QUERY_SQL = "select * from (" + SUBQUERY + ") sub_query";

    String GETRECORDCOUNT_SQL = "select count(id) from au_visitor";
    
    String DELETE_VISITOR_SQL = "delete from au_visitor where id=?";

    String INSERT_SQL = "insert into au_visitor(id,original_id,partyrelationtype_id,partytype_id,visitor_type,name,code,create_date) values(?,?,?,?,?,?,?,?)";

    String SQL_UPDATE_BY_ID = "update au_visitor set original_id=?, partyrelationtype_id=?, partytype_id=?, visitor_type=?, name=?, code=?, modify_date=? where id=?";
    
    String SQL_PARTY_TYPE_TYPES="select distinct a.id, a.name, a.enable_status, a.remark,r.code from au_party a,au_partytype b, au_partyrelation r where a.partytype_id=b.id and a.id=r.partyid and a.enable_status='1' and b.enable_status='1'";
    
    String COUNT_PARTY_TYPE_TYPES="select count(distinct a.id) from au_party a,au_partytype b, au_partyrelation r where a.partytype_id=b.id and a.id=r.partyid and a.enable_status='1' and b.enable_status='1'";
    
    String SQL_PA_TY_UES_TYPES="select distinct a.id, a.name, a.enable_status, a.remark from au_party a,au_partytype b,au_user c, au_partyrelation r where a.partytype_id=b.id and c.party_id=a.id and a.id=r.partyid and a.enable_status='1' and b.enable_status='1' and c.enable_status='1' ";

    String COUNT_PA_TY_UES_TYPES="select count(distinct a.id) from au_party a,au_partytype b,au_user c,  au_partyrelation r where a.partytype_id=b.id and c.party_id=a.id and a.id=r.partyid and a.enable_status='1' and b.enable_status='1' and c.enable_status='1' ";
    
    String PROJECT_VALUE = "wy";

    String QUERY_BYCONDITION1_SQL = "select * from (" + SUBQUERY + ") sub_query";

    String REQUEST_BEAN_VALUE = "bean";

    String RETURN_VALUE = "Template";

    String RETURN_SUCCESS_FLAG = "success";
    
    String RETURN_RELATIONROLE = "relationrole";
    
    String RETURN_RELATIONORG = "relationorg";
    
    String RETURN_PARTYROLE = "partyrole";
    
    String RETURN_PARTYEMPLOYEE = "partyemployee";
    
    String RETURN_PARTYDEPT = "partydept";
    
    String RETURN_PARTYCOMP = "partycomp";
    
    String RETURN_LIST = "list";

    //信息字符串
    String DEFAULT_MSG_ERROR_STR = "Error in populate vo from request.";


}

