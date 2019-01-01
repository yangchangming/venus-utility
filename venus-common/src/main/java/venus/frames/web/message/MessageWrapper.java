package venus.frames.web.message;

import org.apache.struts.util.MessageResources;
import venus.frames.base.IGlobalsKeys;
import venus.frames.mainframe.util.PathMgr;
import venus.frames.util.exception.ExceptionWrapper;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author wujun
 */
public class MessageWrapper implements Serializable {

	private ExceptionWrapper exceptionwrapper = null;
	private Messager messager = null;
	private String msg = null;
	private String style = MessageStyle.RAW;

	public MessageWrapper( Messager messager){
		this.messager = messager;
	}
	
	public MessageWrapper( ExceptionWrapper e){
		this.exceptionwrapper = e;
	}
	
	public MessageWrapper(String key, String arg0,String arg1,String arg2,String arg3 ){
		if( getResources()==null ){
			this.msg = key+" "+arg0+" "+arg1+" "+arg2+" "+arg3 ;
		}
		MessageResources resources = (MessageResources)getResources();
		this.msg = resources.getMessage(key,arg0,arg1,arg2, arg3 );
	}
	
	public MessageWrapper(Locale loc,String key, String arg0,String arg1,String arg2,String arg3 ){
		if( getResources()==null ){
			this.msg = key+" "+arg0+" "+arg1+" "+arg2+" "+arg3 ;
		}
		MessageResources resources = (MessageResources)getResources();
		this.msg = resources.getMessage(loc,key, arg0,arg1,arg2,arg3 );
	}

	public MessageWrapper( String message ){
		this.msg = message;
	}
	
    private MessageResources getResources() {
        if ( PathMgr.getSingleton().getServletContext() == null) return null;
    	return ((MessageResources) PathMgr.getSingleton().getServletContext().getAttribute(IGlobalsKeys.MESSAGES_KEY));
    }

	/**
	 * @return 返回 msg。
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg 要设置的 msg。
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return 返回 wrapper。
	 */
	public ExceptionWrapper getExceptionWrapper() {
		return exceptionwrapper;
	}

	/**
	 * @param wrapper 要设置的 wrapper。
	 */
	public void setExceptionWrapper(ExceptionWrapper wrapper) {
		this.exceptionwrapper = wrapper;
	}

	/**
	 * @return 返回 style。
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style 要设置的 style。
	 */
	public void setStyle(String style) {
		this.style = style;
	}
	
	/**
	 * @return 返回 messager。
	 */
	public Messager getMessager() {
		return messager;
	}

	/**
	 * @param messager 要设置的 messager。
	 */
	public void setMessager(Messager messager) {
		this.messager = messager;
	}
}
