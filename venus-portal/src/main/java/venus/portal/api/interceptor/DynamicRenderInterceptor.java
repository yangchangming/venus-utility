package venus.portal.api.interceptor;

import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.IncludePage;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import venus.portal.cache.data.DataCache;
import venus.portal.util.ServletContextHelper;
import venus.portal.website.model.Website;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;


public class DynamicRenderInterceptor extends HandlerInterceptorAdapter {
    private boolean showSiteCode = true;
    private DataCache dataCache;

    public void setShowSiteCode(boolean showSiteCode) {
        this.showSiteCode = showSiteCode;
    }

    public void setDataCache(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            String viewName = modelAndView.getViewName();
            String siteCode = "";
            Website website = null;
            if (viewName != null && !viewName.startsWith("redirect:")) {
                modelAndView.addObject(FreemarkerServlet.KEY_INCLUDE, new IncludePage(request, response));
            }

            if (showSiteCode && viewName != null) {
                String path = request.getServletPath();
                String sub[] = path.split("/");
                if (sub.length > 0) {
                    if (siteCodeExist(sub[1])) {
                        website = ServletContextHelper.getWebsiteBySiteCode(sub[1]);
                    }
                } else {// 访问根路径，返回默认站点code
                    website = ServletContextHelper.getDefaultWebsite();
                }
                
                if(website != null) {
                	siteCode = "/" + website.getWebsiteCode();
                }
            }
            if (website != null) {
                HashMap<String, String> siteInfo = new HashMap<String, String>();
                siteInfo.put("keywords", website.getKeywords());
                siteInfo.put("description", website.getDescription());
                modelAndView.addObject("siteInfo", siteInfo);
                modelAndView.addObject("siteCode", siteCode);
            }
        }
    }

    private boolean siteCodeExist(String siteCode) {
        List<Website> siteList = dataCache.getWebsitesData();
        for (Website site : siteList) {
            if (site.getWebsiteCode().equals(siteCode)) {
                return true;
            }
        }
        return false;
    }
}