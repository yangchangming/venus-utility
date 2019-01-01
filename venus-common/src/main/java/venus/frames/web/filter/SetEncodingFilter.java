package venus.frames.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class SetEncodingFilter implements Filter {

 public SetEncodingFilter() {
     encoding = null;
     filterConfig = null;
     ignore = true;
 }

 public void destroy() {
     encoding = null;
     filterConfig = null;
 }

 public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
     throws IOException, ServletException {
     if(ignore || request.getCharacterEncoding() == null)
         request.setCharacterEncoding(selectEncoding(request));
     chain.doFilter(request, response);
 }

 public void init(FilterConfig filterConfig) throws ServletException {
     this.filterConfig = filterConfig;
     encoding = filterConfig.getInitParameter("encoding");
     String value = filterConfig.getInitParameter("ignore");
     if(value == null)
         ignore = true;
     else
     if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes"))
         ignore = true;
     else
         ignore = false;
 }

 protected String selectEncoding(ServletRequest request)
 {
     return encoding;
 }

 public FilterConfig getFilterConfig()
 {
     return filterConfig;
 }

 public void setFilterConfig(FilterConfig filterConfig)
 {
     this.filterConfig = filterConfig;
 }

 protected String encoding;
 protected FilterConfig filterConfig;
 protected boolean ignore;
}