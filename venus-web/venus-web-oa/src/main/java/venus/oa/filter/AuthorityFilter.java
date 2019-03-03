package venus.oa.filter;

import venus.oa.helper.LoginHelper;
import venus.oa.login.tools.OnlineUserVo;
import venus.frames.mainframe.log.LogMgr;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AuthorityFilter implements Filter {

    public AuthorityFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        String requestUrl = req.getRequestURL().toString();
        String queryString = req.getQueryString();
        if (queryString != null && queryString.length() > 0) {
            requestUrl += "?" + queryString;
        }
        String contextPath = req.getContextPath();
        requestUrl = requestUrl.substring(requestUrl.indexOf(contextPath) + contextPath.length());// 获取剥离contextPath的url路径
        if(requestUrl.contains("&_function_id_")){
            requestUrl = requestUrl.substring(0,requestUrl.indexOf("&_function_id_"));
        }else if(requestUrl.contains("_function_id_")){
            requestUrl = requestUrl.substring(0,requestUrl.indexOf("_function_id_"));
        }
        if (!requestUrl.equals("") && !requestUrl.equals("/") && requestUrl.indexOf("/login") == -1
        		&& requestUrl.indexOf("checkcode") == -1
                && requestUrl.indexOf("login.jsp") == -1 && requestUrl.indexOf("gap-css.jsp") == -1
                && requestUrl.indexOf("tabs.css.jsp") == -1 && requestUrl.indexOf("loginlog.jsp") == -1
                && requestUrl.indexOf("download.jsp") == -1 && requestUrl.indexOf("authority_error.jsp") == -1) {

            Map m_au = LoginHelper.getOwnerMenuUrl(req);
            Map m_all = LoginHelper.getAllMenuUrl(req);
            List m_public = LoginHelper.getPublicFuncTree();
            if (m_au == null || m_all == null) {
            	LogMgr.getLogger(this.getClass().getName()).warn(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.You_are_logged_in_the_system_or_login_has_timed_out_please_re_login_system_"));
            	HttpServletResponse httpRes = (HttpServletResponse) response;
				httpRes.sendRedirect(req.getContextPath() +  "/jsp/login/login.jsp");
				return;
            }
            if (!LoginHelper.getIsAdmin(req) && m_all.containsKey(requestUrl) && !m_au.containsKey(requestUrl)&&!m_public.contains(requestUrl)) {// 如果没有权限，拒绝请求
            	LogMgr.getLogger(this.getClass().getName()).warn(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Attempt_to_access_unauthorized_resources_has_been_denied_by_the_system_"));
            	HttpServletResponse httpRes = (HttpServletResponse) response;
				httpRes.sendRedirect(req.getContextPath() +  "/jsp/common/authority_error.jsp");
				return;
            }
            //防止Session劫持
            OnlineUserVo vo = (OnlineUserVo)req.getSession().getAttribute("OnlineUserVo");
            //TODO 扩展getRemoteAddr功能，添加其他获取元素
            if(null!=vo&&!request.getRemoteAddr().equals(vo.getLogin_ip())){//根据客户端ip来确认客户端是否变化，进一步可以考虑根据客户端的os或者ie类型来确认。
            	LogMgr.getLogger(this.getClass().getName()).warn(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Login_to_change_your_environment_please_re_login_system_"));
            	HttpServletResponse httpRes = (HttpServletResponse) response;
				httpRes.sendRedirect(req.getContextPath() +  "/jsp/login/login.jsp?errorType=1");
				return;
            }
        }
        chain.doFilter(request, response);
    }

    public void destroy() {

    }

}

