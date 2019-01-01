package venus.frames.web.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import venus.VenusHelper;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * 多主题tag实现。
 * 
 */
public class ThemeTag extends TagSupport {

	protected Log log = LogFactory.getLog(this.getClass());

	@Override
	public int doStartTag() throws JspException {
//		String theme = Helper.getTheme();
		String theme = VenusHelper.getTheme();
		try {
			pageContext.getOut().write(theme);
		} catch (IOException e) {
			log.error("fatal in doEndTag()", e);
		}

		return EVAL_PAGE;
	}
}
