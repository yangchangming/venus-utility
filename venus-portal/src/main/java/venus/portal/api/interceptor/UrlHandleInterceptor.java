package venus.portal.api.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import venus.frames.mainframe.log.LogMgr;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: chengliang
 * Date: 13-10-23
 * Time: 下午1:56
 */
public class UrlHandleInterceptor extends HandlerInterceptorAdapter{

    /**
     * 处理所有带有“.*”后缀的请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String requestUrl = request.getRequestURI();
        if (requestUrl.contains(".")) {
            RequestDispatcher rd = request.getSession().getServletContext().getNamedDispatcher("default");
            rd.forward(request, response);
            LogMgr.getLogger(this).debug("Forward \"" + requestUrl + "\" to default Servlet");
            return false;
        }
        return true;
    }
}
