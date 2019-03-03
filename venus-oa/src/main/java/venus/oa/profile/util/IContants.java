/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.profile.util;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public interface IContants {
    public static final String TABLE_NAME = "AU_USERPROFILE";
    
    public static final String COMMON_FUNCTION_UTIL = "common_function_util";
    
    public static final String BS_KEY = "profile_bs";
    
    public static final String QUERY_PROFILE_SQL = "SELECT ID,PROPERTYKEY,VALUE,INITTIME,UPDATETIME,PARTYID,PARTYNAME,DESCRIPTION,CLOUMN1,ENABLE,PROPERTYTYPE FROM AU_USERPROFILE ";
    
    public static final String QUERY_PROFILE_COUNT_SQL = "SELECT COUNT(distinct A.ID) FROM AU_HISTORY A LEFT OUTER JOIN AU_PARTYRELATION B ON a.source_partyid=b.PARTYID ";
    
    public static final String QUERY_DEFAULT_CONDITON = " WHERE 1=1 ";
    
    public static final String QUERY_AND_CONDITON = " AND ";
    
    public static final String QUERY_DEFAULT_ORDERBY = " ORDER BY ";
    
    public static final String INSERT_PROFILE_SQL = "INSERT INTO AU_USERPROFILE( ID,PROPERTYKEY,VALUE,INITTIME,UPDATETIME,PARTYID,PARTYNAME,DESCRIPTION,CLOUMN1,ENABLE,PROPERTYTYPE) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    
    public static final String SQL_UPDATE_BY_VO = "UPDATE AU_USERPROFILE SET VALUE=?,UPDATETIME=?,PARTYNAME=?,DESCRIPTION=?,ENABLE=?,CLOUMN1=?,PROPERTYTYPE=? WHERE PARTYID=? and PROPERTYKEY=? ";

    public static final String SQL_DELETE_BY_VO = "DELETE FROM AU_USERPROFILE WHERE PARTYID=? ";
}

