package venus.portal.gbox.resource.classificationrelation.util;

public interface IClassificationRelationConstants {

    //BS的规划化名称
    public final static String BS_KEY = "ClassificationRelationBs";
    
    //struts页面跳转
    public final static String FORWARD_TO_QUERY_ALL = "toQueryAll";
    
    public final static String FORWARD_LIST_PAGE = "listPage";    
    
    public final static String FORWARD_RESOURCE_FRAME = "resourceFrame"; 
    
    public final static String FORWARD_CLASSIFICATION_FRAME = "classificationFrame"; 
    
    public final static String FORWARD_LIST_RELATIVE = "listRelative";
    
    //request处理中的key值
    public final static String REQUEST_ID = "id";
    
    public final static String REQUEST_IDS = "ids";    

    public final static String REQUEST_BEAN = "bean";
    
    public final static String REQUEST_BEANS = "beans";
    
    public final static String REQUEST_QUERY_CONDITION = "queryCondition";
    
    public final static String QUERYCONDITION_CLASSID = "classId";
    
    public final static String QUERYCONDITION_NAME = "name";
    
    public final static String QUERYCONDITION_TYPE = "type";
    
    public final static String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";    
    
    //Sql语句
    public final static String SQL_INSERT = "INSERT INTO GBOX_CLASSIFICATION_RELATION ( ID, CLASSIFICATION_ID, RESOURCE_ID, CREATE_DATE) VALUES ( ?, ?, ?, ?)";
    
    public final static String SQL_DELETE= "DELETE FROM GBOX_CLASSIFICATION_RELATION ";
    
    public final static String SQL_CLASSIFY_COUNT = "SELECT COUNT(A.ID) FROM GBOX_CLASSIFICATION A,GBOX_CLASSIFICATION_RELATION B WHERE A.ID = B.CLASSIFICATION_ID ";
    
    public final static String SQL_CLASSIFY_QUERY = "SELECT A.ID id, A.NAME name, A.SELF_CODE seflCode,A.PARENT_CODE parentCode,A.DEPTH depth,A.IS_LEAF isLeaf,A.CREATE_DATE createDate, A.MODIFY_DATE modifyDate, A.ENABLE_STATUS enableStatus, A.DESCRIPTION description FROM GBOX_CLASSIFICATION A,GBOX_CLASSIFICATION_RELATION B WHERE A.ID = B.CLASSIFICATION_ID";
    
    public final static String SQL_RESOURCE_COUNT = "SELECT COUNT(A.ID) FROM GBOX_RESOURCE A,GBOX_CLASSIFICATION_RELATION B WHERE A.ID = B.RESOURCE_ID ";
    
    public final static String SQL_RESOURCE_QUERY = "SELECT A.ID id, A.NAME name, A.CODE code, A.TAG tag, A.TYPE type, A.IS_PROTECTED isProtected, A.IS_EXTERNAL isExternal,A.FILE_NAME fileName, A.FILE_FORMAT fileFormat, A.CREATE_DATE createDate, A.MODIFY_DATE modifyDate, A.ENABLE_STATUS enableStatus, A.DESCRIPTION description FROM GBOX_RESOURCE A,GBOX_CLASSIFICATION_RELATION B WHERE A.ID = B.RESOURCE_ID ";
    
    public final static String SQL_RELATIONLIST_COUNT = "SELECT COUNT(A.ID) FROM GBOX_RESOURCE A ";
    
    public final static String SQL_RELATIONLIST_QUERY = "SELECT A.ID id, A.NAME name, A.CODE code, A.TAG tag, A.TYPE type, A.IS_PROTECTED isProtected, A.IS_EXTERNAL isExternal,A.FILE_NAME fileName, A.FILE_FORMAT fileFormat, A.CREATE_DATE createDate, A.MODIFY_DATE modifyDate, A.ENABLE_STATUS enableStatus, A.DESCRIPTION description FROM GBOX_RESOURCE A ";
    
    //表名
    public final static String TABLE_NAME = "GBOX_CLASSIFICATION_RELATION";
    
    //默认启用的查询条件
    public final static String DEFAULT_QUERY_WHERE_ENABLE = " WHERE 1=1 ";
}
