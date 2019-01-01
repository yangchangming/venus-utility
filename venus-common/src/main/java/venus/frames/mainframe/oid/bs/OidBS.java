package venus.frames.mainframe.oid.bs;

import venus.frames.base.bs.BaseBusinessService;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.oid.OidMgr;
import venus.pub.lang.OID;

/**
 * @author zhangwentao
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class OidBS extends BaseBusinessService implements IOidBS {
	/**
	 * 构造函数
	 */
	public OidBS() {
		super();
	}


	/**
	 * 取得新的oid数组
	 */
/*	
	public OID[] requestOIDArray(String tableName,Integer len) {

		OID[] oids = null;
		int i = len.intValue();		
		
		if ( i>0 ){
			oids = new OID[i];
		}else{
			return null;
		}		
		
		for (int j = 0 ;j<i;i++){
			
			oids[j] = OidMgr.requestOID(tableName);
		
		}		

		return oids;
	}
*/
	
	/**
	 * 取得新的oid数组
	 */
	public OID[] requestOIDArray(String tableName,int len) {

		return OidMgr.requestOIDArray(tableName,new Integer(len));
	}
	
	
	/**
	 * 验取得新的oid
	 */
	public OID requestOID(String tableName) {
		return OidMgr.requestOID(tableName);
	}


	/**
	 * 取得新的oid数组
	 */
	public String[] requestOIDStringArray(String tableName, int len) {
		String[] re = null;
		OID[] oids = requestOIDArray(tableName,len);
		if( oids!=null && oids.length>0 ){
			
			for(int i = 0; i<oids.length;i++){
			
				re[i] = oids[i].toString();
			
			}
		
		}
		return re;
	}


	/**
	 * 验取得新的oid
	 */
	public String requestOIDString(String tableName) {
		return requestOID(tableName).toString();
	}


	/* （非 Javadoc）
	 * @see venus.frames.mainframe.oid.bs.IOidBS#requestOIDStringArrayOneArg(java.lang.String)
	 */
	public String[] requestOIDStringArrayOneArg(String tableNameAndLen) {
		String tableName = null;
		int len = 0;
		
		int pos = tableNameAndLen.indexOf("@@");
		
		if( pos > -1){
			
			tableName = tableNameAndLen.substring(0,pos);
			try{
				len = Integer.parseInt( tableNameAndLen.substring(pos+2) );
			}catch(Exception e){
				LogMgr.getLogger(this).error("parseInt len form tableNameAndLen string:"+tableNameAndLen,e);
			}		


		}
		
		return requestOIDStringArray(tableName, len);
	}
}
