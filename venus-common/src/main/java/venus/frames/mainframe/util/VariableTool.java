package venus.frames.mainframe.util;

import venus.frames.base.IGlobalsKeys;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.currentlogin.ProfileException;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.web.page.PageTool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * @author wujun
 */
public class VariableTool implements IGlobalsKeys {

    public final static String VENUS_BASE_KEY = "base";

    public final static String VENUS_PAGE_PARAM_KEY = "pageParamStr";


    public static String formateSourceString(String srcStr, IRequest request) {

        int startPos = srcStr.indexOf("${");
        int endPos = srcStr.indexOf("}");

        if (endPos > startPos && startPos > -1) {

            StringBuffer sb = new StringBuffer(srcStr);
            return formateSourceStringBuffer(sb, request, startPos, endPos).toString();

        } else {

            return srcStr;

        }

    }

    private static StringBuffer formateSourceStringBuffer(StringBuffer srcSb, IRequest request, int startPos, int endPos) {
        String key = srcSb.substring(startPos + 2, endPos);
        Object value = null;
        if (VENUS_BASE_KEY.equals(key)) {
            value = Helper.WEB_CONTEXT_PATH;
        } else if (VENUS_PAGE_PARAM_KEY.equals(key)) {
            value = PageTool.getPageParamStr((HttpServletRequest) request);
        } else {
            if (request.getAttribute(key) != null) {
                value = request.getAttribute(key);
            } else if (request.getParameter(key) != null) {
                value = request.getParameter(key);
            } else if (((HttpServletRequest) request).getSession().getAttribute(key) != null) {
                value = ((HttpServletRequest) request).getSession().getAttribute(key);
            }
            if (value == null) {
                try {
                    value = Helper.getAppAttributeFromProfile(request, key);
                    if (value == null) value = "";
                } catch (ProfileException e1) {
                    LogMgr.getLogger(VariableTool.class).error("error in formateSourceStringBuffer(...,request,...) get var form profile", e1);
                }
            }
        }
        srcSb = srcSb.replace(startPos, endPos + 1, value.toString());
        int startPos1 = srcSb.indexOf("${");
        int endPos1 = srcSb.indexOf("}");
        if (endPos1 > startPos1 && startPos1 > -1) {
            return formateSourceStringBuffer(srcSb, request, startPos1, endPos1);
        } else {
            return srcSb;
        }
    }


    public static String formateSourceString(String srcStr, PageContext pageContext) {

        int startPos = srcStr.indexOf("${");
        int endPos = srcStr.indexOf("}");

        if (endPos > startPos && startPos > -1) {

            StringBuffer sb = new StringBuffer(srcStr);
            return formateSourceStringBuffer(sb, pageContext, startPos, endPos).toString();

        } else {

            return srcStr;

        }

    }

    private static StringBuffer formateSourceStringBuffer(StringBuffer srcSb, PageContext pageContext, int startPos, int endPos) {

        String key = srcSb.substring(startPos + 2, endPos);

        Object value = null;

        if (!VENUS_BASE_KEY.equals(key)) {


            if (pageContext.getAttribute(key) != null) {

                value = pageContext.getAttribute(key);

            } else if (pageContext.getRequest().getAttribute(key) != null) {

                value = pageContext.getRequest().getAttribute(key);

            } else if (pageContext.getRequest().getParameter(key) != null) {

                value = pageContext.getRequest().getParameter(key);

            } else if (pageContext.getSession().getAttribute(key) != null) {

                value = pageContext.getSession().getAttribute(key);

            } else if (pageContext.getServletContext().getAttribute(key) != null) {

                value = pageContext.getServletContext().getAttribute(key);

            }

            if (value == null) {

                try {

                    value = Helper.getAppAttributeFromProfile((HttpServletRequest) pageContext.getRequest(), key);
                    if (value == null) value = "";

                } catch (ProfileException e1) {

                    LogMgr.getLogger(VariableTool.class).error("error in formateSourceStringBuffer(...,pagecontext,...) get var form profile", e1);

                }

            }

        } else {

            value = Helper.WEB_CONTEXT_PATH;

        }


        srcSb = srcSb.replace(startPos, endPos + 1, value.toString());


        int startPos1 = srcSb.indexOf("${");

        int endPos1 = srcSb.indexOf("}");

        if (endPos1 > startPos1 && startPos1 > -1) {

            return formateSourceStringBuffer(srcSb, pageContext, startPos1, endPos1);

        } else {

            return srcSb;

        }


    }


}
