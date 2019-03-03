package venus.oa.taglib;

import venus.oa.helper.LoginHelper;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 功能按钮tag
 * 
 * @author 吴明强
 *  
 */
public class AuthorityBtnTag extends TagSupport {
    /**
     * 资源code，即功能按钮code
     */
    private String code;

    /**
     * 控制类型type 0:不能控制 1:不能显示
     */
    private String type;

    /**
     * @return 返回 username。
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            要设置的 code。
     */
    public void setCode(String code) {
        this.code = code;
    }

    public int doStartTag() throws JspException {
        return EVAL_PAGE;

    }

    public int doEndTag() throws JspException {
        try {
            //没有code不控制
            if (getCode() != null && !"".equals(getCode())) {
                HttpSession session = pageContext.getSession();
                //没有数据即代表没有权限
                if (! LoginHelper.hasButnAu(session,code)) {
                    if ("1".equals(getType())) {
                        //不能显示
                        //pageContext.getOut().print(" style = \"display:'none';\"");
                        pageContext.getOut().print(" style = \"display:none;\""); 

                    } else {
                        //不能控制
                        pageContext.getOut().print(" disabled ");
                    }
                }
            }

        } catch (Exception e) {
            throw new JspException(e.getMessage());
        }
        return EVAL_PAGE;

    }

    /**
     * @return 返回 type。
     */
    public String getType() {
        if (type == null || "".equals(type)) {
            type = "0";
        }
        return type;
    }

    /**
     * @param type
     *            要设置的 type。
     */
    public void setType(String type) {
        this.type = type;
    }
}

