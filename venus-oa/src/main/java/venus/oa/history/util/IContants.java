package venus.oa.history.util;

public interface IContants {

	String OID = "AU_HISTORY";
	
	String BS_KEY = "adjustOrgLogBs";
	
    String FORWARD_LIST_PAGE_KEY = "authority/history/list";
    
    String FORWARD_LIST_TREE_KEY = "authority/history/listTree";
    
    String FORWARD_LIST_FRAME_KEY = "authority/history/authorizeLogList";
    
    String FORWARD_LIST_VIEW_KEY = "authority/au/auauthorize/viewHisOrg";
    
    String REQUEST_BEANS_VALUE = "beans";
    
    String FORWARD_LIST_DETAILCOMPANY = "authority/history/detailCompany";
    
    String FORWARD_LIST_DETAILDEPARTMENT = "authority/history/detailDepartment";
    
    String FORWARD_LIST_DETAILPOSITION = "authority/history/detailPosition";
    
    String FORWARD_LIST_DETAILEMPLOYEE= "authority/history/detailEmployee";

    String FORWARD_LIST_DETAILADJUST= "authority/history/detailAdjust";
    
    String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";
	
	String QUERY_LOG_SQL = "SELECT distinct A.ID, A.operate_date, A.operate_id, A.operate_name, A.source_id, A.source_partyid, A.source_code, A.source_name, A.source_orgtree, A.tag_id, A.tag_userid, A.tag_date, A.cloumn1, A.cloumn2, A.cloumn3, A.operate_type,A.source_typeid FROM AU_HISTORY A  LEFT OUTER JOIN AU_PARTYRELATION B ON a.operate_id=b.PARTYID ";
	
	String QUERY_LOG_DETAIL = "SELECT id,operate_date,operate_name,source_id,source_name,source_partyid,source_typeid,source_detail,source_orgtree,dest_orgtree FROM AU_HISTORY";
	
	String QUERY_LOG_COUNT_SQL = "SELECT COUNT(distinct A.ID) FROM AU_HISTORY A LEFT OUTER JOIN AU_PARTYRELATION B ON a.operate_id=b.PARTYID ";
	
	String QUERY_DEFAULT_CONDITON = " WHERE 1=1 ";
	
	String QUERY_AND_CONDITON = " AND ";
	
	String QUERY_DEFAULT_ORDERBY = " ORDER BY ";
	
	String INSERT_LOG_SQL = "INSERT INTO AU_HISTORY (id,operate_date,operate_id,operate_name,operate_type,source_id,source_partyid,source_code,source_name,source_orgtree,source_detail,source_typeid,dest_id,dest_code,dest_name,dest_orgtree) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
}

