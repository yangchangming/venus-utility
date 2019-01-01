package venus.frames.web.tag;

import venus.frames.i18n.util.LocaleHolder;
import venus.frames.util.exception.ExceptionWrapper;
import venus.frames.web.message.*;
import venus.pub.util.StringUtil;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author sundaiyong
 *
 * 用于显示消息的Tag.
 * 如果request.getAttribute(MessageStyle.StyleKey)的值为SELF_BACK,
 * 那么弹出网页,并带回退超链接,如果值为ALERT那么弹出警告窗口.
 * 在ApplicationResources.properties文件中设置以下项:
 * error.caption.code="编码: "
 * error.caption.type="类型: "
 * error.caption.severity="严重度: "
 * error.caption.description="描述: "
 * error.caption.message="异常消息: "
 * error.caption.return="返回"
 * 前提:已经在Request里设置了Code, Type, Severity, Description, Message 等属性
 * 使用:
 */
public class MessageTag extends TagSupport {
	
	public static final String SuccessAlertCaptionKey = "success.caption.alert";
	
	public static final String AlertCaptionKey = "error.caption.alert";
	
	public static final String CodeCaptionKey = "error.caption.code";
	
	public static final String TypeCaptionKey = "error.caption.type";
	
	public static final String SeverityCaptionKey = "error.caption.severity";
	
	public static final String DescriptionCaptionKey = "error.caption.description";
	
	public static final String MessageCaptionKey = "error.caption.message";
	
	public static final String BackCmdCaptionKey = "error.caption.backCmd";
	
	public static final String CLoseCmdCaptionKey = "error.caption.CLoseCmd";

	public static final String MESSAGE_DISPLAY_KEY = "DisplayRealError";

	private int width = 0;
	private int height = 0;
	private int left = 300;
	private int top = 300;
	
	private String style = null;
	private String code = null;
	private String type = null;
	private String severity = null;
	private String description = null;
	private String message = null;
	
	private String alertCaption = null;
	private String codeCaption = null;
	private String typeCaption = null;
	private String severityCaption = null;
	private String descriptionCaption = null;
	private String messageCaption = null;
	private String backCmdText =  null;
	private String closeCmdText =  null;
	private String successAlertCaption =  null;
	
	private Boolean isDisplayError = null;
	
	/********Tag属性： 消息类型********/
	private String kind = null;
	
	
	
    
    /**
     * 设置变量信息
     *
     */
    private void setVars()
    {
		ServletRequest request = pageContext.getRequest();
		style = (String)request.getAttribute(MessageStyle.StyleKey);
		//add by chjq on 2006-11-14
//		isDisplayError = (Boolean)request.getAttribute(BaseGlobalExceptionHandler.MESSAGE_DISPLAY_KEY);
		isDisplayError = (Boolean)request.getAttribute(MessageTag.MESSAGE_DISPLAY_KEY);
		if ( isDisplayError == null )
			isDisplayError = new Boolean(true);
		//end 
		code = (String)request.getAttribute(ExceptionWrapper.Code);
		type = (String)request.getAttribute(ExceptionWrapper.Type);
		severity = (String)request.getAttribute(ExceptionWrapper.Severity);
		description = (String)request.getAttribute(ExceptionWrapper.Description);
		message = (String)request.getAttribute(ExceptionWrapper.Message);
		
		
		codeCaption = LocaleHolder.getMessage(CodeCaptionKey);
		typeCaption = LocaleHolder.getMessage(TypeCaptionKey);
		severityCaption = LocaleHolder.getMessage(SeverityCaptionKey);
		descriptionCaption = LocaleHolder.getMessage(DescriptionCaptionKey);
		messageCaption = LocaleHolder.getMessage(MessageCaptionKey);
		backCmdText = LocaleHolder.getMessage(BackCmdCaptionKey);
		
		closeCmdText = LocaleHolder.getMessage(CLoseCmdCaptionKey);
		alertCaption = LocaleHolder.getMessage(AlertCaptionKey);
		successAlertCaption = LocaleHolder.getMessage(SuccessAlertCaptionKey);
		

    }
    

    /**
     * 设置窗口大小
     * @throws IOException
     */
    /*
    private void setWindow() throws IOException
    {
    	pageContext.getOut().println("<script language=\"JavaScript\" type=\"text/javascript\">");

		if (width != 0 && height != 0) {
			pageContext.getOut().println("\twindow.resizeTo(" + width + "," + height + ");");
		}
		
		pageContext.getOut().println("\twindow.moveTo(" + left + "," + top + ")\n</script>");
    }
    */
    
    
    /**
     * 生成弹出信息的脚本
     * @throws IOException
     */
    private void writeAlertScript( String msg ) throws IOException
    {
    	pageContext.getOut().println("<script language=\"JavaScript\" type=\"text/javascript\">");
			
		pageContext.getOut().print("venus_append_alert('"+StringUtil.javaScriptEscape( msg )+"');");

		pageContext.getOut().println("</script>");
    }
    
    /**
     * 生成显示信息
     * @throws IOException
     */
    private void writeRaw( String msg ) throws IOException
    {
		
		pageContext.getOut().print("<pre>"+msg+"</pre>");

    }

    
    /**
     * 生成弹出信息的脚本
     * @throws IOException
     */
    private void writeStatusShow( String msg ) throws IOException
    {
		
    	pageContext.getOut().println("<script language=\"JavaScript\" type=\"text/javascript\">");
		
	    pageContext.getOut().print("showStatus('"+StringUtil.javaScriptEscape( msg )+"');");

	    pageContext.getOut().println("</script>");

    }
    
    private void writeSelfBack() throws IOException
    {
		
    	pageContext.getOut().println("<script language=\"JavaScript\" type=\"text/javascript\">");
		
	    pageContext.getOut().print("history.go(-1);");

	    pageContext.getOut().println("</script>");

    }
    
    private void popAlertScript() throws IOException
    {
		
    	pageContext.getOut().println("<script language=\"JavaScript\" type=\"text/javascript\">");
		
    	pageContext.getOut().print("popAlertScript();");

	    pageContext.getOut().println("</script>");

    }
    
    /**
     * 生成弹出信息的脚本
     * @throws IOException
     */
    private void writeAlertScript() throws IOException
    {
    	StringBuffer msg = new StringBuffer("");
		
		if(code != null && MessageAgent.MESSAGE_AGENT_SUCCESS_KEY.equals(code) ){
			
			if( (successAlertCaption != null) && !(successAlertCaption.toLowerCase().equals("null")) ) msg.append( successAlertCaption+"\r\n" );
		
		}else{
			
			if( (alertCaption != null) && !(alertCaption.toLowerCase().equals("null")) ) msg.append(  alertCaption+"\r\n" ) ;
		
		}    	
		
		if (code != null) {
			msg.append(   codeCaption + code + "\r\n"  );
		}
		if (type != null) {
			msg.append(   typeCaption + type + "\r\n"  );
		}
		if (description != null) {
			msg.append(   descriptionCaption + description + "\r\n"  );
		}
		if (message != null && isDisplayError.booleanValue()) {
			msg.append(   messageCaption + message + "\r\n"  );
		}
		
		writeAlertScript(msg.toString());

    }
    
    /**
     * 生成弹出信息的脚本
     * @throws IOException
     */
    private void writeAlertScript(ExceptionWrapper ew) throws IOException
    {
		
    	writeAlertScript( getStringFromExceptionWrapper(ew) );

    }
    
    private String getStringFromExceptionWrapper(ExceptionWrapper ew){
    	
		String code = ew.getCode();
		String type = ew.getType() ;		
		String description = ew.getDescription() ;
		String message = ew.getMessage() ;
    	
    	StringBuffer msg = new StringBuffer("");	
		
		msg.append(  alertCaption+"\r\n" ) ;

		if (code != null) {
			msg.append(   codeCaption + code + "\r\n"  );
		}
		if (type != null) {
			msg.append(   typeCaption + type + "\r\n"  );
		}
		if (description != null) {
			msg.append(   descriptionCaption + description + "\r\n"  );
		}
		if (message != null&& isDisplayError.booleanValue()) {
			msg.append(   messageCaption + message + "\r\n"  );
		}
		
		return msg.toString();
    }
    
    /**
     * 生成显示信息
     * @throws IOException
     */
    private void writeRaw( ExceptionWrapper ew ) throws IOException
    {
		
		pageContext.getOut().print("<pre>"+getStringFromExceptionWrapper(ew)+"</pre>");

    }
    
    
    /**
     * 生成弹出信息的脚本
     * @throws IOException
     */
    private void writeStatusShow(ExceptionWrapper ew) throws IOException
    {
		String code = ew.getCode();
		String type = ew.getType() ;		
		String description = ew.getDescription() ;
		String message = ew.getMessage() ;
    	
    	StringBuffer msg = new StringBuffer("");	
		
		msg.append(  alertCaption+"\r\n" ) ;

		if (code != null) {
			msg.append(   codeCaption + code + "\r\n"  );
		}
		if (type != null) {
			msg.append(   typeCaption + type + "\r\n"  );
		}
		if (description != null) {
			msg.append(   descriptionCaption + description + "\r\n"  );
		}
		if (message != null&& isDisplayError.booleanValue()) {
			msg.append(   messageCaption + message + "\r\n"  );
		}
		
		writeStatusShow(msg.toString());

    }
    
    
    /**
     * 生成弹出信息的脚本
     * @throws IOException
     */
    private void writeStatusShow() throws IOException
    {
    	StringBuffer msg = new StringBuffer("");
		
		if(code != null && MessageAgent.MESSAGE_AGENT_SUCCESS_KEY.equals(code) ){
			
			msg.append( successAlertCaption+"\r\n" );
		
		}else{
			
			msg.append(  alertCaption+"\r\n" ) ;
		
		}    	
		
		if (code != null) {
			msg.append(   codeCaption + code + "\r\n"  );
		}
		if (type != null) {
			msg.append(   typeCaption + type + "\r\n"  );
		}
		if (description != null) {
			msg.append(   descriptionCaption + description + "\r\n"  );
		}
		if (message != null&& isDisplayError.booleanValue()) {
			msg.append(   messageCaption + message + "\r\n"  );
		}
		
		writeAlertScript(msg.toString());

    }
    
    
    /**
     * 生成带回退按钮的html
     * @throws IOException
     */
    private void writeHaveBackCmd() throws IOException
    {
    	if (code != null) {
			pageContext.getOut().println(codeCaption + code);
			pageContext.getOut().println("<br>");
		}
		if (type != null) {
			pageContext.getOut().println(typeCaption + type);
			pageContext.getOut().println("<br>");
		}
		if (description != null) {
			pageContext.getOut().println(descriptionCaption + description);
			pageContext.getOut().println("<br>");
		}
		if (message != null&& isDisplayError.booleanValue()) {
			pageContext.getOut().println(messageCaption + message);
			pageContext.getOut().println("<br>");
		}
		pageContext.getOut().println("<a href=\"#\" onClick=\"history.go(-1);\" class='"+ MessageStyle.MsgCmdCssClassKey +"' >" + backCmdText + "</a>");
    }
    
    /**
     * 生成带回退按钮的html
     * @throws IOException
     */
    private void writeUserDefine(String cmdText,String cmdHref) throws IOException
    {
    	
    	if( cmdText == null || cmdText.length()<1 ) cmdText = backCmdText ;
    	if( cmdHref == null || cmdHref.length()<1 ) cmdHref = "javascript:history.go(-1);" ;	
    		
    	if (code != null) {
			pageContext.getOut().println(codeCaption + code);
			pageContext.getOut().println("<br>");
		}
		if (type != null) {
			pageContext.getOut().println(typeCaption + type);
			pageContext.getOut().println("<br>");
		}
		if (description != null) {
			pageContext.getOut().println(descriptionCaption + description);
			pageContext.getOut().println("<br>");
		}
		if (message != null&& isDisplayError.booleanValue()) {
			pageContext.getOut().println(messageCaption + message);
			pageContext.getOut().println("<br>");
		}
		pageContext.getOut().println("<a href=\""+cmdHref+"\" class='"+ MessageStyle.MsgCmdCssClassKey +"' >" + backCmdText + "</a>");
    }
    
    /**
     * 生成带关闭按钮的html
     * @throws IOException
     */
    private void writeHaveCloseCmd() throws IOException
    {
    	if (code != null) {
			pageContext.getOut().println(codeCaption + code);
			pageContext.getOut().println("<br>");
		}
		if (type != null) {
			pageContext.getOut().println(typeCaption + type);
			pageContext.getOut().println("<br>");
		}
		if (description != null) {
			pageContext.getOut().println(descriptionCaption + description);
			pageContext.getOut().println("<br>");
		}
		if (message != null&& isDisplayError.booleanValue()) {
			pageContext.getOut().println(messageCaption + message);
			pageContext.getOut().println("<br>");
		}
		pageContext.getOut().println("<a href=\"#\" onClick=\"window.close();\" class='"+ MessageStyle.MsgCmdCssClassKey +"' >" + closeCmdText + "</a>");
    }
    
    
    
    /**
     * 生成在状态栏显示信息的脚本
     * @throws IOException
     */
    private void writeCloseCmd() throws IOException
    {
		pageContext.getOut().println("<a href=\"#\" onClick=\"this.close()\" class='"+ MessageStyle.MsgCmdCssClassKey +"' >" + closeCmdText + "</a>");
    }
    
    private void writeMessageWrapper(MessageWrapper msg) throws IOException{
    	
    	String style = msg.getStyle();
    	
    	ExceptionWrapper ew = msg.getExceptionWrapper();
    	
    	Messager msger = msg.getMessager();
    	
    	String msgstr = msg.getMsg();
    	
    	if( ew!=null ){
    	
			if (MessageStyle.ALERT.equals(style)) {
				if(kind!=null&&kind.equals("script"))
				{
					writeAlertScript(ew);
					popAlertScript();
				}
				if(kind==null)
				{
					writeAlertScript(ew);
					popAlertScript();
				}
				
			} else if (MessageStyle.ALERT_AND_BACK.equals(style)) {
				
				if(kind!=null&&kind.equals("script"))
				{
					writeAlertScript(ew);
					popAlertScript();
					writeSelfBack();
				}
				if(kind==null)
				{
					writeAlertScript(ew);
					popAlertScript();
					writeSelfBack();
				}				
				
				
				
			} else if (MessageStyle.STATUS_SHOW.equals(style)) {
				
				if(kind!=null&&kind.equals("script"))
				{
					writeStatusShow(ew);
					popAlertScript();
				}
				if(kind==null)
				{
					writeStatusShow(ew);
					popAlertScript();
				}
				
				

			} else if (MessageStyle.STATUS_SHOW_AND_BACK.equals(style)) {
				
				if(kind!=null&&kind.equals("script"))
				{
					writeStatusShow(ew);
					popAlertScript();
					writeSelfBack();
				}
				if(kind==null)
				{
					writeStatusShow(ew);
					popAlertScript();
					writeSelfBack();
				}		
				

				
			} else if (MessageStyle.RAW.equals(style)) {
				
				if(kind!=null&&kind.equals("text"))
				{
					writeRaw(ew);
				}
		
				if(kind==null)
				{
					writeRaw(ew);
				}
				
				
			}
    	}else if( msgstr!=null ){
    		
			if (MessageStyle.ALERT.equals(style)) {
				
				
				
				if(kind!=null&&kind.equals("script"))
				{
					writeAlertScript(msgstr);
					popAlertScript();
				}
		
				if(kind==null)
				{
					writeAlertScript(msgstr);
					popAlertScript();
				}
				
			} else if (MessageStyle.ALERT_AND_BACK.equals(style)) {

				
				if(kind!=null&&kind.equals("script"))
				{
					writeAlertScript(msgstr);
					popAlertScript();
					writeSelfBack();
				}
		
				if(kind==null)
				{
					writeAlertScript(msgstr);
					popAlertScript();
					writeSelfBack();
				}
				
				
			} else if (MessageStyle.STATUS_SHOW.equals(style)) {
				
				
				if(kind!=null&&kind.equals("script"))
				{
					writeStatusShow(msgstr);
					popAlertScript();
				}
		
				if(kind==null)
				{
					writeStatusShow(msgstr);
					popAlertScript();
				}
				
				
			} else if (MessageStyle.STATUS_SHOW_AND_BACK.equals(style)) {
				
				
				if(kind!=null&&kind.equals("script"))
				{
					writeStatusShow(msgstr);
					popAlertScript();
					writeSelfBack();
				}
		
				if(kind==null)
				{
					writeStatusShow(msgstr);
					popAlertScript();
					writeSelfBack();
				}
				
				
			} else if (MessageStyle.RAW.equals(style)) {
				
				
				
				if(kind!=null&&kind.equals("text"))
				{
					writeRaw(msgstr);
				}
		
				if(kind==null)
				{
					writeRaw(msgstr);
				}
				
			}
    	}else if( msger!=null ){
    		
			if (MessageStyle.ALERT.equals(style)) {
				
				//writeAlertScript(msgstr);
				
			} else if (MessageStyle.ALERT_AND_BACK.equals(style)) {
				//writeAlertScript(msgstr);
				//writeSelfBack();
				
			} else if (MessageStyle.STATUS_SHOW.equals(style)) {
				//writeStatusShow(msgstr);
				
			} else if (MessageStyle.STATUS_SHOW_AND_BACK.equals(style)) {
				//writeStatusShow(msgstr);
				//writeSelfBack();
				
			} else if (MessageStyle.RAW.equals(style)) {
				
				if(kind!=null&&kind.equals("script"))
				{
					writeRawMessager(msger);
				}
		
				if(kind==null)
				{
					writeRawMessager(msger);
				}
				
				
			}
    	}
    
    }
   
    
    /**
	 * @param msger
	 */
	private void writeRawMessager(Messager msger) {
		
		String strRawMsg = msger.getJavaScript();
		
		try {
			
			pageContext.getOut().println(strRawMsg);
			
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
		
	}

	/**
     * Tag开始时执行
     */
	public int doStartTag() throws JspException {
		
		if(	pageContext.getRequest().getAttribute(MessageAgent.MESSAGES_KEY) != null ){
			
			Messages msgs = (Messages)pageContext.getRequest().getAttribute(MessageAgent.MESSAGES_KEY);
			
			Iterator iter = msgs.iterator();
			
			while(iter.hasNext()){
				
				MessageWrapper msg = (MessageWrapper) iter.next();
				
				try {
					writeMessageWrapper(msg);
				} catch (IOException e1) {
					throw new JspTagException(e1.getMessage());
				}
			
			}
			
		}
		
		if( pageContext.getRequest().getAttribute(MessageStyle.StyleKey) == null ){
			return Tag.EVAL_BODY_INCLUDE;
		}
		
		setVars();

//		try {
//
//			if (MessageStyle.ALERT.equals(style)) {
//
//				writeAlertScript();
//			} else if (MessageStyle.ALERT_AND_BACK.equals(style)) {
//				writeAlertScript();
//				writeSelfBack();
//			} else if (MessageStyle.HAVE_BACK_CMD.equals(style)) {
//				writeHaveBackCmd();
//			} else if (MessageStyle.HAVE_CLOSE_CMD.equals(style)) {
//				writeHaveCloseCmd();
//			} else if (MessageStyle.STATUS_SHOW.equals(style)) {
//				writeStatusShow();
//			} else if (MessageStyle.STATUS_SHOW_AND_BACK.equals(style)) {
//				writeStatusShow();
//				writeSelfBack();
//			} else if (MessageStyle.USER_DEFINE.equals(style)) {
//				
//				String cmdText = "" ;
//				String cmdHref = "" ;
//				
//				Object obj = pageContext.getRequest().getAttribute(MessageAgent.USER_DEFINE_CMD_NAME_KEY);
//				
//				if( obj != null )  cmdText = (String)obj;
//				
//				obj =  pageContext.getRequest().getAttribute(MessageAgent.USER_DEFINE_CMD_HREF_KEY);
//				
//				if( obj != null )  cmdHref = (String)obj;
//				
//				writeUserDefine(cmdText,cmdHref);
//				
//			}
//			
//
//		} catch (Exception e) {
//			throw new JspTagException(e.getMessage());
//		}

		try {

			
			
			
			if (MessageStyle.ALERT.equals(style))
			{
				if(kind!=null&&kind.equals("script")){
					writeAlertScript();
					popAlertScript();
				}
				if(kind == null){
					writeAlertScript();
					popAlertScript();
				}
				
			} else if (MessageStyle.ALERT_AND_BACK.equals(style))
			{
				if(kind!=null&&kind.equals("script"))
				{
					writeAlertScript();
					popAlertScript();
					writeSelfBack();
				}
				
				if(kind == null)
				{
					writeAlertScript();
					popAlertScript();
					writeSelfBack();
				}
		
			} else if (MessageStyle.HAVE_BACK_CMD.equals(style))
			{
				
				if(kind!=null&&kind.equals("script"))
				{
					writeHaveBackCmd();
				}
				
				if(kind == null)
				{
					writeHaveBackCmd();
				}				
				
				
			} else if (MessageStyle.HAVE_CLOSE_CMD.equals(style))
			{
				if(kind!=null&&kind.equals("script"))
				{
					writeHaveCloseCmd();
				}
				
				if(kind == null)
				{
					writeHaveCloseCmd();
				}									
				
			} else if (MessageStyle.STATUS_SHOW.equals(style))
			{
				if(kind!=null&&kind.equals("script"))
				{
					writeStatusShow();
					popAlertScript();
				}
				
				if(kind == null)
				{
					writeStatusShow();
					popAlertScript();
				}
				
				
			} else if (MessageStyle.STATUS_SHOW_AND_BACK.equals(style))
			{
				
				if(kind!=null&&kind.equals("script"))
				{
					writeStatusShow();			
					popAlertScript();
					writeSelfBack();
				}
				
				if(kind==null)
				{
					writeStatusShow();		
					popAlertScript();
					writeSelfBack();
				}
				
				
				
			} else if (MessageStyle.USER_DEFINE.equals(style))
			{
				
				String cmdText = "" ;
				String cmdHref = "" ;
				
				Object obj = pageContext.getRequest().getAttribute(MessageAgent.USER_DEFINE_CMD_NAME_KEY);
				
				if( obj != null )  cmdText = (String)obj;
				
				obj =  pageContext.getRequest().getAttribute(MessageAgent.USER_DEFINE_CMD_HREF_KEY);
				
				if( obj != null )  cmdHref = (String)obj;
				
				writeUserDefine(cmdText,cmdHref);
				
			}
			

		} catch (Exception e) {
			throw new JspTagException(e.getMessage());
		}		
		
		return Tag.EVAL_BODY_INCLUDE;
	}

	public MessageTag() {
		super();
	}

	/**
	 * @return 返回 height。
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * @param height 要设置的 height。
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	
	/**
	 * @return 返回 left。
	 */
	public int getLeft() {
		return left;
	}
	/**
	 * @param left 要设置的 left。
	 */
	public void setLeft(int left) {
		this.left = left;
	}
	/**
	 * @return 返回 top。
	 */
	public int getTop() {
		return top;
	}
	/**
	 * @param top 要设置的 top。
	 */
	public void setTop(int top) {
		this.top = top;
	}
    public void setWidth(int width) {
    	this.width = width;	
    }
	/**
	 * @return 返回 width。
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @return 返回 kind。
	 */
	public String getKind() {
		return kind;
	}
	/**
	 * @param kind 要设置的 kind。
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}
}
