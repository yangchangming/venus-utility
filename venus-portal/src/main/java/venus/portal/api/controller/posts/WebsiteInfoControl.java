package venus.portal.api.controller.posts;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import venus.portal.cache.data.DataCache;
import venus.portal.website.model.Website;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qj on 14-4-3.
 */
@Controller
@RequestMapping(value = "apis/ewp/website")
public class WebsiteInfoControl {

    private static Logger loger = Logger.getLogger(WebsiteInfoControl.class);
    @Autowired
    private DataCache dataCache;

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    public List getAll(HttpServletRequest request) {
        List<Website> websites = dataCache.getWebsitesData();
        List<Map> websiteMaps = Lists.transform(websites, new Function<Website, Map>() {
            @Override
            public Map apply(Website website) {
                Map websiteMap = new HashMap();
                websiteMap.put("id", website.getId());
                websiteMap.put("websiteName", website.getWebsiteName());
                return websiteMap;
            }
        });
        loger.info(websiteMaps.toArray());
        return websiteMaps;
    }
}
