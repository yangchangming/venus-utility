package venus.frames.i18n.tag;

import org.apache.commons.lang3.StringUtils;
import venus.frames.i18n.util.LocaleHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Locale;

/**
 * 国际化加载指定图片的tag实现。
 * 
 * 该版本实现为将多语言版本的图片放在同一个目录下，文件名规则：【jsp中引用的图片名】-【语言】.【图片扩展名】
 */
public class I18NImageTag extends TagSupport {
	

    private String src;
    private String alt;
    private String name;
    private String id;
    private String width;
    private String height;
    private String style;
    private String clazz;
    private String tabIndex;
    

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

        StringBuilder sb = new StringBuilder();
        sb.append("<img ");
        sb.append(buildProperty("alt", getAlt()));
        sb.append(buildProperty("name", getName()));
        sb.append(buildProperty("id", getId()));
        sb.append(buildProperty("width", getWidth()));
        sb.append(buildProperty("height", getHeight()));
        sb.append(buildProperty("style", getStyle()));
        sb.append(buildProperty("clazz", getClazz()));
        sb.append(buildProperty("tabIndex", getTabIndex()));
        
        
        sb.append("src='");
        sb.append(request.getContextPath());
        sb.append(generateTargetLang(src));
        sb.append("'/>");

        try {
            out.println(sb.toString());
        } catch (IOException e) {
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }

    private String generateTargetLang(String source) {
        int indexDot = source.lastIndexOf(".");
        if(indexDot == -1)
            return source;
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
    
    private String buildProperty(String name,String value){
    	if(StringUtils.isEmpty(value))
    		return "";
    	return name + "='" + value + "' ";
    }
}
