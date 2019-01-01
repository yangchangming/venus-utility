/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.api.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import venus.portal.cache.data.DataCache;
import venus.portal.util.BooleanConstants;
import venus.portal.website.model.Website;

import java.util.HashMap;
import java.util.List;

@Controller
public class WelcomeController {

    @Autowired
    private DataCache dataCache;

    @RequestMapping(value = "/{siteCode}", method = RequestMethod.GET)
    public ModelAndView welcome(@PathVariable String siteCode) throws Exception {
        Website webSite = null;
        HashMap<String, Object> map = new HashMap<String, Object>();
        List<Website> websites = dataCache.getWebsitesData();
        if (websites != null && websites.size() > 0) {
            for (Website site : websites) {
                if (!siteCode.isEmpty()) {
                    if (site.getWebsiteCode().equals(siteCode)) {
                        webSite = site;
                        break;
                    }
                } else if (StringUtils.equals(BooleanConstants.YES, site.getIsDefault())) {
                    webSite = site;
                    break;
                }
            }
            if (webSite == null) {
                return new ModelAndView(siteCode + "/welcome", map);
            }
            map.put("siteCode", "/" + webSite.getWebsiteCode());
        }

        return new ModelAndView(webSite.getWebsiteCode() + "/welcome", map);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView defaultHandle() throws Exception {
        return welcome("");
    }

}
