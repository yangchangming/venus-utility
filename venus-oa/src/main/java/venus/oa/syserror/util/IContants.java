/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.syserror.util;

/**
 * @author zangjian
 *
 */
public interface IContants {

    public final static String BS_KEY = "syserror_bs";
    
    public final static String PROJECT_VALUE = "sysErrorList";
    
    public final static String FORWARD_SYSERROR_LIST = "authority/xmport/errorDataList";
    
    public final static String TABLE_NAME = "AU_SYSERROR";
    
    public final static String SQL_QUERY_COUNT = "SELECT COUNT(*) FROM AU_SYSERROR ";
    
    public final static String SQL_QUERY = "SELECT ID,OPERATE_ID,OPERATE_NAME,OPERATE_DATE,ERROR_TYPE,SOURCE_ID,SOURCE_PARTYID,SOURCE_CODE,SOURCE_NAME,SOURCE_ORGTREE,SOURCE_TYPEID,REMARK,CLOUMN1 FROM AU_SYSERROR ";
    
    public final static String SQL_INSERT = "INSERT INTO AU_SYSERROR (ID,OPERATE_ID,OPERATE_NAME,OPERATE_DATE,ERROR_TYPE,SOURCE_ID,SOURCE_PARTYID,SOURCE_CODE,SOURCE_NAME,SOURCE_ORGTREE,SOURCE_TYPEID,SOURCE_DETAIL,REMARK,CLOUMN1) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    public final static String SQL_DELETE = "DELETE FROM AU_SYSERROR ";
    
}

