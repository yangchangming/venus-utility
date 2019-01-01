package venus.frames.web.tag;

import venus.frames.base.IGlobalsKeys;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.Helper;
import venus.frames.mainframe.util.PathMgr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * @author wujun
 *
 * TODO 要更改此生成的类型注释的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class SortTag extends TagSupport implements IGlobalsKeys {
	
	/********属性：排序关键字********/
	private String orderBy = null;
	
	/********属性：Action关键字********/
	private String actionStr = null;
	
	/********属性：cmd关键字********/
	private String cmdStr = null;
	

	public int doEndTag() throws JspException
	{
		try {
			
			pageContext.getOut().write(getOrderTag(orderBy,(HttpServletRequest)pageContext.getRequest()) );
		
		} catch (IOException e) {
			LogMgr.getLogger(this).fatal("fatal in doEndTag()",e);

		}

		
		return EVAL_PAGE;
	}
	
	
	private StringBuffer changeUrlForAction(HttpServletRequest req){
		
		StringBuffer url ;

//		目前这种改法只支持单列表。20050819 update BY wj
		
		if(  actionStr != null ){
			 
			url  = new StringBuffer( req.getContextPath() +"/"+ actionStr);
				 			
	    } else {

			if( req instanceof IRequest) {

				url  = new StringBuffer( req.getContextPath() + ((IRequest)req).getActionPath() + ".do" );

			
			}else{
				
				url = req.getRequestURL();
			
			}
	    	
	    
	    }

		return url;
	
	}
	
	private StringBuffer changeUrlForCmd(HttpServletRequest req){
		
		StringBuffer url = new StringBuffer( req.getQueryString() );

		
//		目前这种改法只支持单列表。20050819 update BY wj

		
		if( cmdStr != null ){

	 			
				int pos_cmd = url.indexOf("cmd=");

				if( pos_cmd > -1 ) {  			
		 			
					
					String requestPathStr_end = url.substring( pos_cmd + 4 ); 
		 			
		 			String requestPathStr_start = url.substring(0, pos_cmd + 4 ); 			
		 			
		 			int pos_cmd_1 = requestPathStr_end.indexOf('&');
					
					
					
					if( pos_cmd_1 > -1  ){
						
						url =  new StringBuffer(  requestPathStr_start + cmdStr  + requestPathStr_end.substring(pos_cmd_1) ) ;

						
					}else{
					
						url =  new StringBuffer(  requestPathStr_start + cmdStr );
					
					}

	 			
	 			} else {
	
	 				
	 				if( url.indexOf("?")>-1 ){
	 					
	 					url.append("&cmd="+cmdStr);
	 				}else{
	 					
	 					url.append( "?cmd="+cmdStr);

	 				}

	 				
	 			}

	 			
	 	}
		
		
		return url ;
	
	}
	
	
	private String getOrderTag(String orderStrName,HttpServletRequest req)
	{
		
		StringBuffer url = changeUrlForAction(req);
		
		
		StringBuffer sb = changeUrlForCmd(req) ;
		
		
		
		String OrderStr = findOrderStr(req);
		
		if( OrderStr!=null && OrderStr.length()>0){
			
			//String srtSortsymbol = null;
			int descpos = orderStrName.indexOf(Helper.SORT_SYMBOL_DESC) ;
			int ascpos = orderStrName.indexOf(Helper.SORT_SYMBOL_ASC) ;
			
			if( descpos >0 ) {
				
				orderStrName = orderStrName.substring(0,descpos).trim();
				//srtSortsymbol = ""+Helper.SORT_SYMBOL_DESC;
				
			}
			
			if( ascpos >0 ) {
				
				orderStrName = orderStrName.substring(0,ascpos).trim();
				//srtSortsymbol = ""+Helper.SORT_SYMBOL_ASC;
				
			}
			
			if( OrderStr.indexOf(orderStrName) >=0 ){
		
				if( OrderStr.indexOf(Helper.SORT_SYMBOL_DESC) >0 ){
					
					sb = repleaceOrderParam(sb,orderStrName," "+ Helper.SORT_SYMBOL_ASC);
	
					return "<a href=\""+url+"?"+sb+"\" class=\"orderStr\"><img src=\""+ PathMgr.WEB_CONTEXT_PATH+"/images/"+ Helper.SORT_SYMBOL_DESC+"_selected.gif\" border=\"0\"/></a>";
					
				}
				
				//if( OrderStr.indexOf(Helper.SORT_SYMBOL_ASC) >0  ){
					
					sb = repleaceOrderParam(sb,orderStrName," "+ Helper.SORT_SYMBOL_DESC);
					
					return "<a href=\""+url+"?"+sb+"\" class=\"orderStr\"><img src=\""+ PathMgr.WEB_CONTEXT_PATH+"/images/"+ Helper.SORT_SYMBOL_ASC+"_selected.gif\" border=\"0\"/></a>";
					
				//}
				
	
				//sb = repleaceOrderParam(sb,orderStrName,"");
				
				//return "<a href=\""+url+"?"+sb+"\" class=\"orderStr\"><img src=\"images/"+Helper.SORT_SYMBOL_ASC+"_selected.gif\" border=\"0\"/></a>";
			
			}
				
			sb = repleaceOrderParam(sb,orderStrName,"");
			return "<a href=\""+url+"?"+sb+"\" class=\"orderStr\"><img src=\""+ PathMgr.WEB_CONTEXT_PATH+"/images/ASC.gif\" border=\"0\"/></a>";
			
		}

		
		 sb = sb.append("&"+ Helper.ORDER_KEY+"="+orderStrName);
		
		 return "<a href=\""+url+"?"+sb+"\" class=\"orderStr\"><img src=\""+ PathMgr.WEB_CONTEXT_PATH+"/images/ASC.gif\" border=\"0\"/></a>";
			

		
	}
	
	private StringBuffer repleaceOrderParam(StringBuffer sb,String orderStrName,String sortSymbol)
	{
		String sbstr = sb.toString();
		
		if ( sbstr.indexOf(Helper.ORDER_KEY) >=0 ){
			int start = sbstr.indexOf("&"+ Helper.ORDER_KEY+"=");
			int stop = sbstr.indexOf("&",start+1);
			
			if ( stop == -1 ) stop = sb.length();
			
			sb = sb.delete(start,stop);
			sb = sb.insert(start,"&"+ Helper.ORDER_KEY+"="+orderStrName+sortSymbol);
			
		}else{
			sb = sb.append("&"+ Helper.ORDER_KEY+"="+orderStrName+sortSymbol);
		}
		return sb;
	}
	
	private String findOrderStr(HttpServletRequest request)
	{
		String re = "";;
		String orstr = request.getParameter(ORDER_KEY);
		
		if( orstr!=null && orstr.length()>0 ){
			
			re = orstr ;	
			
		} else {	

			Object o = request.getAttribute(ORDER_KEY);
			if( o!=null && o instanceof String ){
				
				re = (String)o ;	
				
			}
		}
		return re;
		

	}

	/**
	 * @return 返回 orderBy。
	 */
	public String getOrderBy() {
		return orderBy;
	}
	/**
	 * @param orderBy 要设置的 orderBy。
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return 返回 cmdStr。
	 */
	public String getCmdStr() {
		return cmdStr;
	}
	/**
	 * @param cmdStr 要设置的 cmdStr。
	 */
	public void setCmdStr(String cmdStr) {
		this.cmdStr = cmdStr;
	}
	/**
	 * @return 返回 actionStr。
	 */
	public String getActionStr() {
		return actionStr;
	}
	/**
	 * @param actionStr 要设置的 actionStr。
	 */
	public void setActionStr(String actionStr) {
		this.actionStr = actionStr;
	}
}
