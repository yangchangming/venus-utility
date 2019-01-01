package venus.frames.web.tag;

import venus.frames.base.IGlobalsKeys;
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
public class WebContextPathTag extends TagSupport implements IGlobalsKeys{
	
	public int doEndTag() throws JspException
	{

		try {
			pageContext.getOut().write(getContextPath((HttpServletRequest)pageContext.getRequest()) );
		
		} catch (IOException e) {
			LogMgr.getLogger(this).fatal("fatal in doEndTag()",e);

		}
		
		return EVAL_PAGE;
	}
	
	private String getContextPath(HttpServletRequest request){
		
		if( PathMgr.WEB_CONTEXT_PATH==null || PathMgr.WEB_CONTEXT_PATH.length()<1 ) {
			
			PathMgr.WEB_CONTEXT_PATH = request.getContextPath();
			Helper.WEB_CONTEXT_PATH = PathMgr.WEB_CONTEXT_PATH;
			Helper.getContext().setAttribute(WEB_CONTEXT_PATH_KEY,PathMgr.WEB_CONTEXT_PATH);
		
		}
		
		return PathMgr.WEB_CONTEXT_PATH;
	}
}
