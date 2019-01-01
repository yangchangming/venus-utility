package venus.frames.web.message;

import venus.frames.base.IGlobalsKeys;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.util.exception.ExceptionWrapper;
import venus.frames.util.exception.ExceptionWrapperFactory;

/**
 * @author zhangzehang
 */
public class MessageAgent implements IGlobalsKeys
{
	public static final String MESSAGE_AGENT_ERROR_PAGE_KEY = "MessageAgentError";
	
	public static final String USER_DEFINE_CMD_NAME_KEY = "USER_DEFINE_CMD_NAME_KEY";
	
	public static final String USER_DEFINE_CMD_HREF_KEY = "USER_DEFINE_CMD_NAME_KEY";
	
	public static final String MESSAGES_KEY = "MESSAGES_KEY";
	
	public static final String MESSAGE_AGENT_SUCCESS_KEY = "-1";

	public static final String MESSAGE_DISPLAY_KEY = "DisplayRealError";
	
	public static void setErrorInfo(IRequest request, ExceptionWrapper wrapper)
	{
		request.setAttribute(ExceptionWrapper.Code, wrapper.getCode());
		request.setAttribute(ExceptionWrapper.Type, wrapper.getType());
		request.setAttribute(ExceptionWrapper.Severity, String.valueOf(wrapper.getSeverity()));
		request.setAttribute(ExceptionWrapper.Description, wrapper.getDescription());
		request.setAttribute(ExceptionWrapper.Message, wrapper.getMessage());
	}
	
	/**
	 * add new method sendErrorMessage(IRequest request,Exception e,String style,boolean isDisplayError)
	 * to deal with the condition not to display the real message but only the customized and friendly information.
	 * modifid by changming.y on 2006-11-15
	 */
	public static IForward sendErrorMessage(IRequest request, Exception e, String style, boolean isDisplayError)
	{
		request.setAttribute(MessageStyle.StyleKey, style);
//		request.setAttribute(BaseGlobalExceptionHandler.MESSAGE_DISPLAY_KEY,new Boolean(isDisplayError));
		request.setAttribute(MessageAgent.MESSAGE_DISPLAY_KEY, new Boolean(isDisplayError));
		ExceptionWrapper wrapper = ExceptionWrapperFactory.getExceptionWrapper(e);
		setErrorInfo( request,  wrapper);
		return request.findForward(MESSAGE_AGENT_ERROR_PAGE_KEY);		
	}
	
	public static IForward sendErrorMessageUserDefine(IRequest request, Exception e, String cmdName, String cmdHref, String forword)
	{
		request.setAttribute(MessageStyle.StyleKey, MessageStyle.USER_DEFINE);
		ExceptionWrapper wrapper = ExceptionWrapperFactory.getExceptionWrapper(e);
		setErrorInfo( request,  wrapper);
		
		request.setAttribute(USER_DEFINE_CMD_NAME_KEY, cmdName);
		request.setAttribute(USER_DEFINE_CMD_HREF_KEY, cmdHref);		
		return request.findForward(forword);		
	}
	
	public static IForward sendErrorMessageUserDefineSimple(IRequest request, Exception e, IForward cmdForward, String forword)
	{
		
		String cmdHref = Helper.WEB_CONTEXT_PATH+cmdForward.getPath();
		return sendErrorMessageUserDefine(request,e,"",cmdHref,forword)	;	
	}
	
	public static IForward sendErrorMessage(IRequest request, Exception e, String style)
	{
		request.setAttribute(MessageStyle.StyleKey, style);
		ExceptionWrapper wrapper = ExceptionWrapperFactory.getExceptionWrapper(e);
		setErrorInfo( request,  wrapper);
		return request.findForward(MESSAGE_AGENT_ERROR_PAGE_KEY);		
	}	
	
	public static IForward sendErrorMessage(IRequest request, String message, String style)
	{
		request.setAttribute(MessageStyle.StyleKey, style);
		request.setAttribute(ExceptionWrapper.Message, message);
		return request.findForward(MESSAGE_AGENT_ERROR_PAGE_KEY);		
	}	
	
	public static IForward sendErrorMessageUserDefine(IRequest request, String message, String cmdName, String cmdHref, String forword)
	{
		request.setAttribute(MessageStyle.StyleKey, MessageStyle.USER_DEFINE);
		request.setAttribute(ExceptionWrapper.Message, message);
		
		request.setAttribute(USER_DEFINE_CMD_NAME_KEY, cmdName);
		request.setAttribute(USER_DEFINE_CMD_HREF_KEY, cmdHref);	
		
		return request.findForward(forword);		
	}
	
	public static IForward sendErrorMessageUserDefineSimple(IRequest request, String message, IForward cmdForward, String forword)
	{
		
		String cmdHref = Helper.WEB_CONTEXT_PATH+cmdForward.getPath();
		return sendErrorMessageUserDefine(request,message,"",cmdHref,forword)	;	
	}
	
	public static IForward sendErrorMessage(IRequest request, String message, String style, String forword)
	{
		request.setAttribute(MessageStyle.StyleKey, style);
		request.setAttribute(ExceptionWrapper.Message, message);
		return request.findForward(forword);		
	}
	
	public static IForward sendSucceedMessage(IRequest request, String message, String style, String forword)
	{
		request.setAttribute(MessageStyle.StyleKey, style);
		request.setAttribute(ExceptionWrapper.Code,MESSAGE_AGENT_SUCCESS_KEY);
		request.setAttribute(ExceptionWrapper.Message, message);
		return request.findForward(forword);
	}	
	
	public static void pushMessage(IRequest request, String message, String style)
	{
		
		MessageWrapper msgwraper = new MessageWrapper(message);
		
		msgwraper.setStyle(style);
		
		Messages msgs ;
		if( request.getAttribute(MESSAGES_KEY)!=null ) {
			
			msgs = (Messages)request.getAttribute(MESSAGES_KEY);
			
		}else{
			
			msgs = new Messages();

		}
		
		msgs.pushMessage(msgwraper);
		
		request.setAttribute(MESSAGES_KEY, msgs);


	}	
	
	public static void pushMessage(IRequest request, Exception e, String style)
	{
		request.setAttribute(MessageStyle.StyleKey, style);
		
		ExceptionWrapper wrapper = ExceptionWrapperFactory.getExceptionWrapper(e);
		
		MessageWrapper msgwraper = new MessageWrapper(wrapper);
		
		msgwraper.setStyle(style);
		
		Messages msgs ;
		if( request.getAttribute(MESSAGES_KEY)!=null ) {
			
			msgs = (Messages)request.getAttribute(MESSAGES_KEY);
			
		}else{
			
			msgs = new Messages();

		}
		
		msgs.pushMessage(msgwraper);
		
		request.setAttribute(MESSAGES_KEY, msgs);

	}
	
	public static void pushMessage(IRequest request, MessageWrapper msg)
	{
		
		Messages msgs ;
		if( request.getAttribute(MESSAGES_KEY)!=null ) {
			
			msgs = (Messages)request.getAttribute(MESSAGES_KEY);
			
		}else{
			
			msgs = new Messages();

		}
		
		msgs.pushMessage(msg);
		
		request.setAttribute(MESSAGES_KEY, msgs);


	}
	
	public static void pushMessage(IRequest request, Messager messager)
	{
		
		Messages msgs ;
		MessageWrapper msg = new MessageWrapper(messager);
		if( request.getAttribute(MESSAGES_KEY)!=null ) {
			
			msgs = (Messages)request.getAttribute(MESSAGES_KEY);
			
		}else{
			
			msgs = new Messages();

		}
		
		msgs.pushMessage(msg);
		
		request.setAttribute(MESSAGES_KEY, msgs);


	}
	
	public static Messages getMessages(IRequest request)
	{
		
		if( request.getAttribute(MESSAGES_KEY)!=null ) {
			
			return (Messages)request.getAttribute(MESSAGES_KEY);
			
		}else{
			
			Messages msgs = new Messages();
			request.setAttribute(MESSAGES_KEY, msgs);
			 return msgs;

		}

	}

	public static void pushOperationHint(IRequest request, String msg)
	{
		
		Messager messager = new Messager();
		
		messager.setOperationHint(msg);	
			
		pushMessage(request,messager);

	}

	public static void pushAlert(IRequest request, String alert)
	{
		
		pushMessage(request,alert, MessageStyle.ALERT);

	}

}
