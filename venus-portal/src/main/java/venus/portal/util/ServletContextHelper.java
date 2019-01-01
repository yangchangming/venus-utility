package venus.portal.util;

import org.apache.commons.lang.StringUtils;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.util.Helper;
import venus.portal.cache.data.DataCache;
import venus.portal.website.model.Website;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;
import static venus.portal.helper.EwpStringHelper.trimNullAsEmpty;
import static venus.portal.util.CommonFieldConstants.*;


/**
 * 请求与会话相关的工具类
 *
 * @author zhangrenyang
 * @date 2011-11-11
 */
public class ServletContextHelper {

    /**
     * 取得默认的网站ID
     *
     * @return 默认站点id
     */
    public static String getDefaultWebsiteId() {
        Website website = getDefaultWebsite();
        if (website == null) {
            return "";
        }
        return trimNullAsEmpty(website.getId());
    }

    /**
     * 取得默认的网站CODE
     *
     * @return 默认站点CODE
     */
    public static String getDefaultWebsiteCode() {
        Website website = getDefaultWebsite();
        if (website == null) {
            return "";
        }
        return trimNullAsEmpty(website.getWebsiteCode());
    }

    /**
     * 取得默认站点
     *
     * @return 默认站点
     */
    public static Website getDefaultWebsite() {
        DataCache dataCache = (DataCache) Helper.getBean("dataCache");
        List<Website> websites = dataCache.getWebsitesData();
        Website website = null;
        if (websites != null && websites.size() > 0) {
            for (Website site : websites) {
                if (StringUtils.equals(BooleanConstants.YES, site.getIsDefault())) {
                    website = site;
                    break;
                }
            }
            if (website == null) {
                website = websites.get(0);
            }
        }
        return website;
    }

    public static String getWebsiteIdBySiteCode(String siteCode) {
        return getWebsiteBySiteCode(siteCode).getId();
    }

    public static Website getWebsiteBySiteCode(String siteCode) {
        if (siteCode == null || StringUtils.isEmpty(siteCode)) {
            return getDefaultWebsite();
        }

        DataCache dataCache = (DataCache) Helper.getBean("dataCache");
        List<Website> websites = dataCache.getWebsitesData();
        Website website = null;
        if (websites != null && websites.size() > 0) {
            for (Website site : websites) {
                if (StringUtils.equals(siteCode, site.getWebsiteCode())) {
                    website = site;
                    break;
                }
            }
            if (website == null) {
                website = websites.get(0);
            }
        }
        return website;
    }

    public static String getWebsiteCodeBySiteId(String siteId) {
        DataCache dataCache = (DataCache) Helper.getBean("dataCache");
        return dataCache.getWebSiteCodeById(siteId);
    }

    /**
     * 取得当然前请求对应的网站ID
     *
     * @param request 客户端请求
     * @return 对应的站点ID
     */
    public static String getCurrentWebsiteId(HttpServletRequest request) {
        String webSiteId = (String) request.getSession().getAttribute(SITE_ID);
        if (isBlank(webSiteId) || StringUtils.equals(UNDEFINED, webSiteId)) {
            webSiteId = getDefaultWebsiteId();
        }
        return trimNullAsEmpty(webSiteId);
    }

    /**
     * 取得当然前请求对应的网站ID
     *
     * @param request 客户端请求
     * @return 对应的站点ID
     */
    public static String getCurrentWebsiteId(IRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request.getServletRequest();
        String webSiteId = (String) httpServletRequest.getSession().getAttribute(SITE_ID);
        if (isBlank(webSiteId) || StringUtils.equals(UNDEFINED, webSiteId)) {
            webSiteId = getDefaultWebsiteId();
        }
        return trimNullAsEmpty(webSiteId);
    }

    /**
     * 取得当然前请求对应的网站CODE
     *
     * @param request 客户端请求
     * @return 对应的站点CODE
     */
    public static String getCurrentWebsiteCode(IRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request.getServletRequest();
        String webSiteCode = (String) httpServletRequest.getSession().getAttribute(SITE_CODE);
        if (isBlank(webSiteCode) || StringUtils.equals(UNDEFINED, webSiteCode)) {
            webSiteCode = getDefaultWebsiteCode();
        }
        return trimNullAsEmpty(webSiteCode);
    }

    /**
     * 设置当然前请求对应的网站ID
     *
     * @param request 客户端请求
     * @return 对应的站点ID
     */
    public static void setCurrentWebsiteId(IRequest request, String site_id) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request.getServletRequest();
        System.out.println(httpServletRequest.getSession());
        httpServletRequest.getSession().setAttribute(SITE_ID, site_id);
    }

}
