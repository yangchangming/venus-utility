package venus.frames.mainframe.action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import venus.frames.base.action.*;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.log.LogMgr;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wujun
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class BaseExceptionHandler extends ExceptionHandler
{

    
    /**
     * Handle the exception.
     * Return the <code>ActionForward</code> instance (if any) returned by
     * the called <code>ExceptionHandler</code>.
     *
     * @param ex The exception to handle
     * @param ae The ExceptionConfig corresponding to the exception
     * @param mapping The ActionMapping we are processing
     * @param formInstance The ActionForm we are processing
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception ServletException if a servlet exception occurs
     *
     * @since Struts 1.1
     */
    public ActionForward execute(Exception ex,
                                 ExceptionConfig ae,
                                 ActionMapping mapping,
                                 ActionForm formInstance,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws ServletException {

		ActionForward af = super.execute(ex,ae,mapping,formInstance,request,response);
		
		IForward f = null;
		HttpRequest req = null;
		HttpResponse res = null;
		DefaultMapping dm = null;
		DefaultForm formBean = null;
		
		dm = new DefaultMapping(mapping);

		if (af!=null){
			f = new DefaultForward(af);
	
		}else{
			f = null;		
		}

		
		if (formInstance instanceof DefaultForm) {
			formBean = (DefaultForm) formInstance;
		} else {
			LogMgr.getLogger(this.getClass().getName()).debug(
					"form is not instanceof venus.frames.mainframe.base.bean.DefaultForm  in execute(...) so send null to service(...) !");
		}


		//对request转型
		if (request instanceof HttpRequest) {
			req = (HttpRequest) request;
		} else {
			req = new HttpRequest(request);
			LogMgr.getLogger(this.getClass().getName()).debug(
					"request is not instanceof HttpRequest in execute(...) so new a HttpRequest(request) !");
		
			//return mapping.findForward("error");
		}
		
		if (response instanceof HttpResponse) {
			res = (HttpResponse) response;
		} else {
			res = new HttpResponse(response);
			LogMgr.getLogger(this.getClass().getName()).debug(
					"response is not instanceof HttpResponse in execute(...) so new a HttpResponse(response) !");
		
			//return mapping.findForward("error");
		}
		
		//设置请求对象对应的配置数据对象，放入HttpRequest

		req.setMapping((IMappingCfg) dm);
		
		IForward re = service(f,ex,dm,formBean,req,res);
		
		storeException(req, ex, f, ae.getScope());
		
		return (ActionForward)re;


    }

    public IForward service(IForward exforward,
							Exception ex,
							IMappingCfg mapping,
							DefaultForm formInstance,
							IRequest request,
							IResponse response)
        {

		return exforward;
    }

    /**
     * Default implementation for handling an <b>ActionError</b> generated
     * from an Exception during <b>Action</b> delegation.  The default
     * implementation is to set an attribute of the request or session, as
     * defined by the scope provided (the scope from the exception mapping).  An
     * <b>ActionErrors</b> instance is created, the error is added to the collection
     * and the collection is set under the Globals.ERROR_KEY.
     *
     * @param request - The request we are handling
     * @param property  - The property name to use for this error
     * @param error - The error generated from the exception mapping
     * @param forward - The forward generated from the input path (from the form or exception mapping)
     * @param scope - The scope of the exception mapping.
     */

    protected void storeException(IRequest request,
    					Exception ex,
                        IForward forward,
                        String scope) {
                            

    }

    protected void storeException(IRequest request,
			String key,
			BaseActionError actionError,
            IForward forward,
            String scope) {
                
    	storeException(getHttpServletRequest(request),key,actionError, (ActionForward) forward, scope);
    }

    protected HttpServletRequest getHttpServletRequest(IRequest request) {

        return (HttpServletRequest)(request.getServletRequest());

    }



}
