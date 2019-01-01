package venus.frames.web.tag;

import venus.frames.base.IGlobalsKeys;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.web.page.PageTool;

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
public class PageParamStrTag extends TagSupport implements IGlobalsKeys {
	
	
	public int doEndTag() throws JspException
	{
		try {
			
			pageContext.getOut().write(getPageParamStr((HttpServletRequest)pageContext.getRequest()) );
		
		} catch (IOException e) {
			LogMgr.getLogger(this).fatal("fatal in doEndTag()",e);
		}
		
		return EVAL_PAGE;
	}
	

	
	/**
	 * 获取pagevo的工具方法
	 * @param name
	 * @return
	 */
	private String getPageParamStr(HttpServletRequest request)
	{
		return PageTool.getPageParamStr(request);
	}
	
}