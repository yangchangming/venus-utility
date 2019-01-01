package venus.frames.web.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import venus.frames.mainframe.util.Helper;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * 资源路径tag实现。
 * 
 */
public class ResourcePathTag extends TagSupport {

	protected Log log = LogFactory.getLog(this.getClass());

	@Override
	public int doStartTag() throws JspException {
		String resourcePath = Helper.getResourcePath();
		try {
			pageContext.getOut().write(resourcePath);

		} catch (IOException e) {
			log.error("fatal in doEndTag()", e);

		}

		return EVAL_PAGE;
	}

}
