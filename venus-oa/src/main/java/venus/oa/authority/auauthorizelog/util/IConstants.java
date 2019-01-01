/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.authority.auauthorizelog.util;

/**
 * @author zangjian
 *
 */
public interface IConstants {
    
    //页面跳转
    public final static String FORWARD_TO_QUERYALL = "authority/au/auauthorizelog/listAuthorizeLog";
    
    public final static String FORWARD_TO_VIEW_FUNC_KEY = "authority/au/auauthorize/viewFunction";
    
    //request中key值
    public final static String REQUEST_BEANS_VALUE = "beans";
    
    public final static String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";
    
    //OID定义引用
    public final static String OID = "AU_AUTHORIZE_LOG";

    //BS的别名
    public final static String BS_KEY = "auauthorizeLog_bs";
    
    //SQL
    public final static String FILEDS = "AU_AUTHORIZE_LOG.id,AU_AUTHORIZE_LOG.operate_date,AU_AUTHORIZE_LOG.operate_id,AU_AUTHORIZE_LOG.operate_name,AU_AUTHORIZE_LOG.visitor_id,AU_AUTHORIZE_LOG.visitor_name,AU_AUTHORIZE_LOG.visitor_code,AU_AUTHORIZE_LOG.visitor_type,AU_AUTHORIZE_LOG.resource_id,AU_AUTHORIZE_LOG.resource_name,AU_AUTHORIZE_LOG.resource_code,AU_AUTHORIZE_LOG.resource_type,AU_AUTHORIZE_LOG.authorize_status,AU_AUTHORIZE_LOG.authorize_tag,AU_AUTHORIZE_LOG.accredit_type,AU_AUTHORIZE_LOG.create_date";
    
    public final static String RECORD_COUNT_SQL = " SELECT COUNT(*) FROM AU_AUTHORIZE_LOG ";
    
    public final static String QUERY_ALL_SQL = " SELECT "+FILEDS+" FROM AU_AUTHORIZE_LOG ";
    
    public final static String INSERT_SQL  = " INSERT INTO AU_AUTHORIZE_LOG ("+FILEDS+") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
}

