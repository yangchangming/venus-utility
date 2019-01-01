package venus.oa.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class FunOrgAuFilter implements Filter {

    public FunOrgAuFilter() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        //设置功能数据权限
        //TODO更好的方案是模拟sessionid的url重写机制在req的wapper类中将_function_id_设置为默认参数并在整个module中传递。
        String _function_id_ = req.getParameter("_function_id_");
        if(null!=_function_id_&&_function_id_.length()!=0){
        	if(!_function_id_.equals(req.getSession(false).getAttribute("_function_id_")))
        		req.getSession().setAttribute("_function_id_",_function_id_);
        }
        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}

