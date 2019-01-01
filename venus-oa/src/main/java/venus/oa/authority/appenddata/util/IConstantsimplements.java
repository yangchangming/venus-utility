package venus.oa.authority.appenddata.util;

/**
 * 2008-9-26
 * 
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
public interface IConstantsimplements {
	//OID定义引用
	String OID = "AU_APPENDDATA";
	
	//BS定义引用
	String BS_KEY = "AuAppendData_bs";


	String MESSAGE_AGENT_ERROR = "common/common_error";
	
    String FORWARD_TO_AU_ORG_FRAME_KEY = "authority/au/auauthorize/auOrgSuccess";
    
    String FORWARD_TO_AU_FUNC_ORG_KEY = "authority/au/auauthorize/functionOrg";
    
    String FORWARD_TO_VIEW_FUNC_ORG_KEY = "authority/au/auauthorize/viewFunctionOrg";

	//SQL语句
	public static final String FIELDS = " ID,VISITOR_ID,VISITOR_CODE,RESOURCE_ID,AUTHORIZE_ID,APPEND_VALUE,CREATE_DATE ";
	
	public static final String INSERT_SQL = "INSERT INTO AU_APPENDDATA(" + FIELDS + ") VALUES(?,?,?,?,?,?,?)";

	public static final String QUERYALL = "SELECT " + FIELDS + " FROM AU_APPENDDATA ";

	String DELETE_FOR_APPENDDATA_SQL = "delete from AU_APPENDDATA ";
}

