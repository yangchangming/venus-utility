/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.api.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import venus.portal.api.util.IApiConstants;
import venus.portal.cache.data.DataCache;
import venus.portal.doctype.vo.DocTypeVo;

import java.util.*;
import java.util.Map.Entry;

/**
 * 此类是获取页头导航菜单控制类，在文档类型管理中，被设置为是导航菜单的栏目，
 * 能够被显示到页头的导航菜单中
 * <p/>
 * 如果此文档类型数据设置为导航菜单，则其模板挂接需要对应至相应的挂接模板
 *
 * @author zhaoyapeng
 */

@Controller
@RequestMapping(value = "{siteCode}/api/nav")
public class DocTypesAllForMenuController {

    @Autowired
    private DataCache dataCache;

    @RequestMapping(value = "/{viewName}")
    public ModelAndView navigateMenuHandle(@PathVariable String siteCode, @PathVariable String viewName) throws Exception {
        if (!StringUtils.isNotBlank(viewName)) {
            viewName = IApiConstants.VIEW_DOCTYPE_MENU; //如果菜单没有传递参数，则采用默认的菜单
        }
        HashMap<String, DocTypeVo> docTypes = dataCache.getData();
        List<DocTypeVo> result = getMenuDocType(docTypes, siteCode);

        return new ModelAndView(siteCode + "/" + viewName, IApiConstants.UI_KEY, result);
    }

    /**
     * @param docTypes 缓存数据
     * @param siteCode 站点编码
     * @return List<DocTypeVo>
     */
    private List<DocTypeVo> getMenuDocType(HashMap<String, DocTypeVo> docTypes, String siteCode) {
        Set mapEntry = docTypes.entrySet();
        Iterator iterator = mapEntry.iterator();
        List<DocTypeVo> result = new ArrayList<DocTypeVo>();
        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            DocTypeVo docType = (DocTypeVo) entry.getValue();
            String tempSiteCode = docType.getSite().getWebsiteCode();
            String isValid = docType.getIsValid();
            String isNavigate = docType.getIsNavigateMenu();
            if (siteCode.equals(tempSiteCode) && isNavigate.equals(IApiConstants.VIEW_DOCTYPE_IS_NAVIGATE)) {
                //获取本站点可用的，首页栏目
                result.add(docType);
            }
        }

        Collections.sort(result, new Comparator<DocTypeVo>() {
            @Override
            public int compare(DocTypeVo o1, DocTypeVo o2) {
                return o1.getSortNum().intValue() - o2.getSortNum().intValue();
            }
        });


        return result;
    }

}
