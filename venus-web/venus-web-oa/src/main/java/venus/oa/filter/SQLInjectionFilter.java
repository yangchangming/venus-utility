package venus.oa.filter;

import venus.oa.helper.LoginHelper;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.util.exception.ExceptionWrapper;
import venus.frames.web.message.MessageStyle;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * 防止SQL注入
 * 
 */
public class SQLInjectionFilter implements Filter {
    private String regularExpression;

    public SQLInjectionFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        regularExpression = filterConfig.getInitParameter("regularExpression");
    }

    /*
     * 如果需要输入“'”、“;”、“--”这些字符 可以考虑将这些字符转义为html转义字符 “'”转义字符为：&#39; “;”转义字符为：&#59;
     * “--”转义字符为：&#45;&#45;
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String requestUrl = req.getRequestURL().toString();
        String contextPath = req.getContextPath();
        requestUrl = requestUrl.substring(requestUrl.indexOf(contextPath)
                + contextPath.length());// 获取剥离contextPath的url路径

        Map parametersMap = request.getParameterMap();
        Iterator it = parametersMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String[] value = (String[]) entry.getValue();
            for (int i = 0; i < value.length; i++) {
                if (null!=value[i]&&value[i].matches(regularExpression)) {
                    LogMgr.getLogger(this.getClass().getName()).warn(
                            "疑似SQL注入攻击！账号：" + LoginHelper.getLoginId(req)
                                    + ";用户名："
                                    + LoginHelper.getLoginName(req)
                                    +";执行Action："
                                    +requestUrl
                                    +";参数名称："
                                    +entry.getKey().toString()
                                    +";录入信息："
                                    +value[i]);
                    // 操作时有SQL注入嫌疑，退回上一页。
                    if (requestUrl.indexOf("/login") == -1 && requestUrl.indexOf("common_error.jsp") == -1) {
                        request.setAttribute(MessageStyle.StyleKey, MessageStyle.ALERT_AND_BACK);
                        request.setAttribute(ExceptionWrapper.Message, "输入有误，请更正！");
                        RequestDispatcher rd = request.getRequestDispatcher("jsp/common/common_error.jsp");
                        rd.forward(request, response);
                        return;
                    } else {// 登陆时有SQL注入嫌疑，记录登陆日志
                        String message = "请输入正确的数据！";
                        request.setAttribute("Message", message);
                        RequestDispatcher rd = request.getRequestDispatcher("jsp/login/loginlog.jsp");
                        rd.forward(request, response);
                        return;
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }

    public void destroy() {

    }

}