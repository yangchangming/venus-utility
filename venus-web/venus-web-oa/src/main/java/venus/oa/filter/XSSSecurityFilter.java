package venus.oa.filter;

import org.apache.log4j.Logger;
import venus.oa.xss.XSSHttpRequestWrapper;
import venus.oa.xss.XSSSecurityConfig;
import venus.oa.xss.XSSSecurityConstants;
import venus.oa.xss.XSSSecurityManager;
import venus.frames.i18n.util.LocaleHolder;
import venus.frames.util.exception.ExceptionWrapper;
import venus.frames.web.message.MessageStyle;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * xss攻击脚本过滤器
 */
public class XSSSecurityFilter implements Filter {

    private static Logger logger = Logger.getLogger(XSSSecurityFilter.class);

    /**
     * 销毁操作
     */
    public void destroy() {

        logger.info("XSSSecurityFilter destroy() begin");
        XSSSecurityManager.destroy();
        logger.info("XSSSecurityFilter destroy() end");
    }

    /**
     * 安全审核
     * 读取配置信息
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 判断是否使用HTTP
        checkRequestResponse(request, response);

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // http信息封装类
        XSSHttpRequestWrapper xssRequest = new XSSHttpRequestWrapper(httpRequest);

        // 对request信息进行封装并进行校验工作，若校验失败（含非法字符），根据配置信息进行日志记录和请求中断处理
        if (xssRequest.validateParameter(httpResponse)) {
            if (XSSSecurityConfig.IS_LOG) {
                logger.warn("XSS Security Filter RequestURL:" + httpRequest.getRequestURL().toString());
                logger.warn("XSS Security Filter RequestParameter:" + httpRequest.getParameterMap().toString());
            }

            if (XSSSecurityConfig.IS_CHAIN) {
                request.setAttribute(MessageStyle.StyleKey, MessageStyle.ALERT_AND_BACK);
                request.setAttribute(ExceptionWrapper.Message, LocaleHolder.getMessage("venus.authority.illegal_characters"));
                RequestDispatcher rd = request.getRequestDispatcher(XSSSecurityConstants.FILTER_ERROR_PAGE);
                rd.forward(request, response);
                return;
            }

        }
        chain.doFilter(request, response);
    }

    /**
     * 初始化操作
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        XSSSecurityManager.init(filterConfig);
    }

    /**
     * 判断Request ,Response 类型
     *
     * @param request  ServletRequest
     * @param response ServletResponse
     * @throws ServletException
     */
    private void checkRequestResponse(ServletRequest request,
                                      ServletResponse response) throws ServletException {
        if (!(request instanceof HttpServletRequest)) {
            throw new ServletException("Can only process HttpServletRequest");

        }
        if (!(response instanceof HttpServletResponse)) {
            throw new ServletException("Can only process HttpServletResponse");
        }
    }
}
