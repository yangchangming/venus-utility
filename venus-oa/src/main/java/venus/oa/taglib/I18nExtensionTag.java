/*
 * 创建日期 2006-11-14
 *
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package venus.oa.taglib;

import venus.frames.i18n.util.LocaleHolder;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Locale;

public class I18nExtensionTag extends TagSupport {
    
    public int doStartTag() throws JspException {
        return EVAL_PAGE;

    }

    public int doEndTag() throws JspException {
        try {
            Locale userLocale = null;
            HttpSession session = pageContext.getSession();
     
            if (session != null) {
                userLocale = LocaleHolder.getLocale();
            }

            if (userLocale == null) {
                userLocale = pageContext.getRequest().getLocale();
            }

            String language = userLocale.getLanguage();
            if(null != language)
                pageContext.getOut().print("_"+language);            
        } catch (Exception e) {
            e.printStackTrace();
            throw new JspException(e.getMessage());
        }
        return EVAL_PAGE;

    }
    
}

