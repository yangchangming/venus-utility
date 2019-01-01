package venus.frames.i18n.tag;

import venus.frames.i18n.util.LocaleHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Locale;

/**
 * 国际化加载指定javascript的tag实现。
 * 
 * 该版本实现为将多语言版本的javascript放在同一个目录下，文件名规则：【jsp中引用的js名】-【语言】.js
 *
 */
public class I18NJavascriptTag extends TagSupport {

    private String src;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

        StringBuilder sb = new StringBuilder();
        sb.append("<script type='text/javascript' src='");
        sb.append(request.getContextPath());
        sb.append(generateTargetLang(src));
        sb.append("'></script>");

        try {
            out.println(sb.toString());
        } catch (IOException e) {
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }

    private String generateTargetLang(String source) {
        int indexDot = source.lastIndexOf(".");
        String strBegin = source.substring(0, indexDot);
        String strEnd = source.substring(indexDot);

        return strBegin + "_" + retrieveUserLocale() + strEnd;
    }

    private String retrieveUserLocale() {
        Locale userLocale = null;
        HttpSession session = pageContext.getSession();
 
        if (session != null) {
            userLocale = LocaleHolder.getLocale();
        }

        if (userLocale == null) {
            userLocale = pageContext.getRequest().getLocale();
        }

        return userLocale.getLanguage();
    }
}
