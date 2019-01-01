package venus.frames.mainframe.action;

import venus.frames.base.IGlobalsKeys;
import venus.frames.base.action.DefaultForward;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IMappingCfg;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.context.ContextMgr;
import venus.frames.mainframe.context.IContext;
import venus.frames.mainframe.currentlogin.IProfile;
import venus.frames.mainframe.currentlogin.ProfileException;
import venus.frames.mainframe.currentlogin.ProfileMgr;
import venus.frames.mainframe.currentlogin.SessionProfile;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.VariableTool;
import venus.frames.web.message.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 对 javax.servlet.HttpServletRequest 的一个实例的一个包装类
 * 
 * 继承于HttpServletRequestWrapper
 * 
 * 该类的实例同 HttpServletRequest的一个实例一一对应
 * 
 * 同时该类实现 Request 接口
 * 
 * @author 岳国云
 */
public class HttpRequest extends HttpServletRequestWrapper implements IRequest {

	public static final String CONTINUE_ATTRIBUTE_MAP_KEY = "VENUS_CONTINUE_ATTRIBUTE_MAP_KEY";
	public static final String CONTINUE_ATTRIBUTE_PARAM_KEY = "VENUS_CONTINUE_ATTRIBUTE_PARAM_KEY";
	public static boolean IS_ATTRIBUTE_CONTINUE = false;
	public boolean m_bIsAttributeContinue = IS_ATTRIBUTE_CONTINUE;
	
	
	/**
	 * 保存该 Action(业务操作)对应的配置数据对象
	 */
	private IMappingCfg m_mapcfg = null;

	/**
	 * 保存该 Action(业务操作)对应的 Action(业务名)
	 */
	private String m_strActionName = null;

	/**
	 * 保存该连接的唯一 ID号
	 */
	private String m_strSessionId = null;
	
	/**
	 * 内置暂存m_request
	 */	
	//private HttpServletRequest m_request = null;
	
	
	/**
	 * 保存该 Action(业务操作)对应的配置数据对象
	 */
	private Map m_mapAttributes = null;
	
	private void findAttributefFromSession(){
		
		if( getSession().getAttribute( CONTINUE_ATTRIBUTE_MAP_KEY )!=null ){
			
			Map tmpMap = (Map)getSession().getAttribute( CONTINUE_ATTRIBUTE_MAP_KEY );
			
			Iterator iter = tmpMap.keySet().iterator();
			
			while( iter.hasNext() ) {
				
				String key = (String)iter.next();
			
				this.setAttribute(key , tmpMap.get( key ) );
			
			}

			getSession().removeAttribute( CONTINUE_ATTRIBUTE_MAP_KEY );
		
		}
		
	
	};

	/**
	 * 构造器
	 * 
	 * @param request - 传入已有的请求对象
	 * @roseuid 3FA5D754002E
	 */
	public HttpRequest(HttpServletRequest request) {
		super(request);
		//if( m_request == null ) m_request = request;
		if( request.getParameter( CONTINUE_ATTRIBUTE_PARAM_KEY )!=null ){
			findAttributefFromSession();
		}
		if (request != null) {
			this.m_strSessionId = request.getSession().getId();
		}
	}
	
	public void removeAttribute( String name ){
		//if( m_request !=null ) {
			
		//	m_request.removeAttribute( name );
		
		//} else { 
			
			super.removeAttribute( name );
			
		//}

		m_mapAttributes.remove( name );
	
	}
//	
//	public Object getAttribute( String name ){
//		if( m_request !=null ) {
//			
//			return m_request.getAttribute( name );
//		
//		} else { 
//			
//			return super.getAttribute( name );
//			
//		}
//
//	}
//	
	public void setAttribute(String name,Object value){
	
		if( m_mapAttributes == null ) m_mapAttributes = new HashMap();
		
		if( CONTINUE_ATTRIBUTE_PARAM_KEY.equalsIgnoreCase( name ) && value instanceof Boolean ) {
			
			m_bIsAttributeContinue = ((Boolean)value).booleanValue();
		
		}
		
//		if( m_request !=null ) {
//			
//			m_request.setAttribute( name, value );
//		
//		} else { 
			
			super.setAttribute( name, value );
			
//		}
		
		m_mapAttributes.put( name,value );
		
	}

	/**
	 * 设置请求对象对应的配置数据对象
	 * 设置Path操作事件
	 * 
	 * @param mapping - 请求对象对应的配置数据对象
	 * @roseuid 3F83C49D0270
	 */
	public void setMapping(IMappingCfg mapping) {

		if (mapping != null) {
			this.m_mapcfg = mapping;
			String pathName = mapping.getPath();

			if (pathName == null) {
				LogMgr.getLogger(this.getClass().getName()).warn(
					"setMapping(): Mapping Path 为空!");
				return;
			}
			this.m_strActionName = pathName;
		
			if ( ProfileMgr.isCreateProfilePerRequest() ) {	
	
				//得到该请求对象对应的个性数据堆对象
				SessionProfile pf = (SessionProfile) getCurrentLoginProfile();
	
				//设置Path操作事件
				if (pf != null) {
					pf.setActionName(pathName);
				}
			}else{
				IProfile pf = null;

				//查找是否有当前SessionId的SessionProfile实例
				try {
					pf =
						ProfileMgr.getSessionProfile(
							IGlobalsKeys.WEB_CONTEXT_KEY,
							this.m_strSessionId);
				} catch (ProfileException pe) {
					/****/
					LogMgr.getLogger(this).info(
						"getCurrentLoginProfile() : ProfileException 返回空");
					/****/
				}
				//设置Path操作事件
				if (pf != null) {
					pf.setActionName(pathName);
				}
			
			}
		}
	}

	/**
	 * 得到该请求对象对应的 Action的名字
	 * 
	 * @return String - 该请求对象对应的 Action的名字
	 * @roseuid 3FAD1E980069
	 */
	public String getActionPath() {
		if (this.m_mapcfg == null) {
			return null;
		}
		return this.m_mapcfg.getPath();
	}

	/**
	 * 返回该请求对象对应的配置数据对象
	 * 
	 * @return venus.frames.mainframe.base.action.IMappingCfg - 该请求对象对应的配置数据对象
	 * @roseuid 3FAD1E9800CD
	 */
	public IMappingCfg getMapping() {
		return this.m_mapcfg;
	}

	/**
	 * 返回该请求对象所对应的传入参数的列表
	 * 
	 * 
	 * @return Map - 该请求对象所对应的传入参数的列表
	 * @roseuid 3FAD1E980132
	 */
	public Map getParmsMap() {

//		if( m_request !=null ) {
//
//			return m_request.getParameterMap();
//		
//		} else {
			
			return super.getParameterMap();
			
//		}
		
	}

	/**
	 * 
	 * 返回该请求对象对应的个性数据堆对象
	 
	 * @return PersistentProfile - 该请求对象对应的个性数据堆对象
	 * @roseuid 3FAD1E980150
	 */

	public IProfile getCurrentLoginProfile() {

		IProfile pf = null;


		//查找是否有当前SessionId的SessionProfile实例
		try {
			pf =
				ProfileMgr.getSessionProfile(
					IGlobalsKeys.WEB_CONTEXT_KEY,
					this.m_strSessionId);
		} catch (ProfileException pe) {
			/****/
			LogMgr.getLogger(this).info(
				"getCurrentLoginProfile() : ProfileException 返回空");
			/****/
			return null;
		}
		//如果当前SessionId的SessionProfile实例不存在,则新构建
		if (pf == null) {
			LogMgr.getLogger(this).info("通过ProfileMgr.createSessionProfile(...)构建...");
			pf = ProfileMgr.createSessionProfile(this, null, null);
		}


		//返回当前SessionId的SessionProfile实例
		return pf;
	}

	/**
	 * 返回该请求对象所包装的ServletRequest
	 * 
	 * @return ServletRequest - 该请求对象所包装的ServletRequest
	 * @roseuid 3FAD1E980178
	 */
	public ServletRequest getServletRequest() {
		/**
		 * Author:wj
		 * Date:20050919
		 * Reason:由 return this -> super.getRequest();为了供继承类调用super时不至出现死循环
		 * Date:20050919 back to this
		 */
		
//		if( m_request !=null ) {
//
//			return m_request;
//		
//		} else {
//			
			return this;
			
//		}
		
		
	}

	/**
	 * 返回该请求对象所对应的上下文对象
	 * 
	 * @return venus.frames.mainframe.context.IContext
	 * @roseuid 3FAD1E9801A0
	 */
	public IContext getContext() {
		return ContextMgr.getContext(WEB_CONTEXT_KEY);
	}

	private String parserForwardStringFromRequest(String forwardkey){
		
		return VariableTool.formateSourceString(forwardkey,this);
		
	}
	
   public static String checkPath(String path , boolean isAttributeContinue ){
   	
    if( isAttributeContinue ){
    	
    	if( ! (path.indexOf( HttpRequest.CONTINUE_ATTRIBUTE_PARAM_KEY ) > 0) ){
    		
        	if( path.indexOf("?") > 0 ){

        		return path + "&"+ HttpRequest.CONTINUE_ATTRIBUTE_PARAM_KEY+"=1" ;

        	}else{
	
        		return  path + "?"+ HttpRequest.CONTINUE_ATTRIBUTE_PARAM_KEY+"=1";
    			
    		}
    	
    	}

    
    }else{
    	
    	if( (path.indexOf( HttpRequest.CONTINUE_ATTRIBUTE_PARAM_KEY ) > 0) ){
    		
    		return path.substring( 0, path.indexOf( HttpRequest.CONTINUE_ATTRIBUTE_PARAM_KEY ) ) +  path.substring( path.indexOf( HttpRequest.CONTINUE_ATTRIBUTE_PARAM_KEY )+ HttpRequest.CONTINUE_ATTRIBUTE_PARAM_KEY.length()+2 );

    	}
    
    }
    
    return path;
   
   }
   
   private IForward checkForward(IForward forward){

   		return forward;
   
   }
   
	/**
	 * 返回一个指定名称的IForward页面跳转对象
	 * 
	 * @param pathname 页面跳转逻辑名
	 * @return IForward 页面跳转对象
	 */
	public IForward findForward(String pathname) {
		
		if (this.m_mapcfg == null) {
			return null;
		}
		
		IForward forward = null;
		
		try {
			
			forward = this.m_mapcfg.findForward(pathname);
			
			if(  forward.getPath()!=null && forward.getPath().indexOf("${") > -1 ) {
				
				String newPath = parserForwardStringFromRequest(forward.getPath());
				
				if( newPath != null && newPath.length() > 0  ){
						
					forward = new DefaultForward(forward.getName(),newPath,forward.getRedirect());
					
					if( m_bIsAttributeContinue != IS_ATTRIBUTE_CONTINUE ) {//个性同全局不同需要处理
						
						forward.setPath( checkPath( forward.getPath() , m_bIsAttributeContinue ) ) ;
					
					
					}

				
				}

			
			}else{
				
				if( forward.getRedirect() ){
					
					if( m_bIsAttributeContinue != IS_ATTRIBUTE_CONTINUE ) {//个性同全局不同需要处理
					
						forward = new DefaultForward(forward.getName(),forward.getPath(),forward.getRedirect());//此处采用 new 是为了防止线程安全问题
						
						forward.setPath( checkPath( forward.getPath() , m_bIsAttributeContinue ) ) ;
					}
				}
			}
			if( m_bIsAttributeContinue ){
				getSession().setAttribute(CONTINUE_ATTRIBUTE_MAP_KEY,m_mapAttributes);
			}
		} catch (NullPointerException npe) {
			LogMgr.getLogger(this).warn(
					"NullPointerException in action "+getActionPath()+": can not find forward \""+ pathname + "\" in struts-config file", npe);
			return MessageAgent.sendErrorMessage(this,"NullPointerException in action "+getActionPath()+": can not find forward \""+ pathname + "\" in struits-config file", MessageStyle.ALERT_AND_BACK);
		}

		if( OperationHintUtil.isPushOperationHint() ){
			
			Messages msgs = MessageAgent.getMessages(this);
			
			Iterator iter = msgs.iterator();
			
			while(iter.hasNext()){
				
				Object obj = iter.next();
				
				if( obj instanceof MessageWrapper) {
					
					MessageWrapper mw = (MessageWrapper)obj;

					Messager msgr = mw.getMessager();
					
					if( msgr!=null && msgr.getOperationHint() != null) {
						
						return forward;
						
					}
					
				}
				
			}
			
			String msg = OperationHintUtil.getOperationHintFromForward(pathname);
			
			if( msg != null) MessageAgent.pushOperationHint(this,msg);

		}

		return forward;
	}


}
