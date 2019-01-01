package venus.frames.web.tag;

import venus.frames.mainframe.log.LogMgr;
import venus.frames.web.page.PageVo;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * @author 周利臣
 * @
 */
public class SequenceTag extends TagSupport {

    
	public int doEndTag() throws JspException
	{

	    Integer wy4 = (Integer) pageContext.getAttribute("orderNumber");
	    
		PageVo pageVo = new PageVo();
		if(pageContext.getRequest().getAttribute("VENUS_PAGEVO_KEY") != null) {
			pageVo = (PageVo) pageContext.getRequest().getAttribute("VENUS_PAGEVO_KEY");
		}
		
		try {
			pageContext.getOut().print( (pageVo.getCurrentPage()-1)*pageVo.getPageSize() + wy4.intValue()+1 );
		
		} catch (IOException e) {
			LogMgr.getLogger(this).fatal("fatal in doEndTag()",e);

		}
		
		return EVAL_PAGE;
	}
    
}

