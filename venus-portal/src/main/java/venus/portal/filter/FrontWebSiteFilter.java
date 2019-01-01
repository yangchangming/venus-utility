package venus.portal.filter;

import org.apache.commons.lang.StringUtils;
import venus.frames.mainframe.util.Helper;
import venus.portal.api.util.IApiConstants;
import venus.portal.cache.data.DataCache;
import venus.portal.util.BooleanConstants;
import venus.portal.website.model.Website;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author zhaoyapeng
 * 拦截所有前台 *.page的页面请求，查看session中是否有webSite对象
 * 如果没有则默认获取中文站点，设置session
 * 因为前台页面展示是跟各webSite 相关联的，页面信息不仅仅需要栏目code，还要有其所属站点
 * 站点信息保存在session中
 *
 */
public class FrontWebSiteFilter implements Filter {

    public void destroy() {
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String siteId =request.getParameter(IApiConstants.SITE_ID);
        Website website =(Website) req.getSession().getAttribute("webSite");//获取前台视图 的siteId
        DataCache dataCache = (DataCache) Helper.getBean("dataCache");
        List<Website> websites = dataCache.getWebsitesData();
        if(siteId!=null&&!"".equals(siteId)){
            for(Website site : websites){
                if(siteId.equals(site.getId())){
                   website = site;
                    break;
                }
            }
        }
        if(website==null){
            if(websites != null && websites.size()>0){
                for(Website site : websites){
                    if(StringUtils.equals(BooleanConstants.YES,site.getIsDefault())){
                        website = site;
                        break;
                    }
                }
                if(website == null){
                    website = websites.get(0);
                }
            }
        }
        req.getSession().setAttribute("webSite", website);
        chain.doFilter(request, response);
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(FilterConfig arg0) throws ServletException {
    

    }

}
