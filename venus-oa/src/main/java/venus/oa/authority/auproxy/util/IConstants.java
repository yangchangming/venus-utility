/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.authority.auproxy.util;

/**
 * @author zangjian
 *
 */
public interface IConstants {

    public final static String PROXY_BS = "auproxy_bs";
    
    public final static String PROXY_LOG_BS = "proxyLogBs";
    
    public final static String AUTHORIZE_LOG_BS = "auauthorizeLog_bs";
    
    public final static String PROXY_HISTORY_BS = "proxyHistoryBs";
    
    public final static String REQUEST_BEANS_VALUE = "beans";
    
    public final static String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";
    
    public final static String FORWARD_LIST_PAGE_KEY = "listPage";


    String recipient = "authority/au/proxyhistory/listRecipientProxy";

    String listPage = "authority/au/proxyhistory/listProxyHistory";

    String sponsor = "authority/au/proxyhistory/listSponsorProxy";

    String detailPage = "authority/au/auproxy/detailAuProxy";

    
    public final static String OID = "AU_PROXYHISTORY";
    
    public final static String FILEDS = "ID,PROXY_HISTORY_ID,PROXY_PROXYER_HISTORY_ID,PROXY_AUTHORIZE_HISTORY_ID,OPERATER_ID,OPERATER_NAME,OPERATER_DATE,LOGIN_NAME,OPERATER_TYPE,NOTICE_NOTE,COLUMN1,SPONSOR,SPONSOR_ID,PROXY,PROXY_ID,RECIPIENT,RECIPIENT_ID,CANEL_ID,CANEL_NAME,CANEL_DATE"; 
    
    public final static String QUERY = "SELECT "+FILEDS+" FROM AU_PROXYHISTORY ";
    
    public final static String SQL_COUNT = "SELECT COUNT(1) FROM AU_PROXYHISTORY ";
    
    public final static String QUERY_WHERE_ENABLE = " WHERE 1=1 ";
    
    public final static String ORDER_BY_ID = " ORDER BY ID ";
    
    public final static String SQL_UPDATE = "UPDATE AU_PROXYHISTORY SET CANEL_ID=?,CANEL_NAME=?,CANEL_DATE=?,OPERATER_TYPE=? WHERE ID = ?";
    
    public final static String SQL_INSERT = " INSERT INTO  AU_PROXYHISTORY("+FILEDS+") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
}

