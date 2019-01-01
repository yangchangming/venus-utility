/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import venus.portal.api.util.IApiConstants;
import venus.portal.cache.data.DataCache;
import venus.portal.doctype.vo.DocTypeVo;
import venus.portal.document.bs.IDocumentBS;
import venus.portal.document.model.Document;
import venus.portal.document.util.IConstants;
import venus.portal.website.model.Website;

import java.util.HashMap;
import java.util.List;


/**
 * @author zhaoyapeng
 *         获取文章的控制器，通过传入的文章ID 参数来获取相应文章的信息
 *         文章的展示模板跳转也是由前台页面来控制的，前台需要传入2个参数
 *         id :文章的id
 *         view:连接需要跳转到的文章模板
 *         例如  <a href="${rc.getContextPath()}/api/document.page?id=${doc.id}&view=/document"  target ="new"></a>
 */
@Controller
public class DocumentController {

    @Autowired
    private IDocumentBS documentBS;

    @Autowired
    private DataCache dataCache;

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */

    @RequestMapping(value = "/{siteCode}/article/{docId}", method = RequestMethod.GET)
    public ModelAndView handleRequest(@PathVariable String siteCode, @PathVariable String docId) {
        Document document = null;
        String viewName = null;
        try {
            document = documentBS.findDocById(docId);

            if (documentBS.isShowHotWords(document, siteCode)) {
                documentBS.renderHotWords(document, siteCode);
            }

            String status = document.getStatus();
            if (!IConstants.DOCTYPE_DOC_PUBLISHED.equals(status)) { //如果是未发布状态的，则设置为null
                document = null;
            }

            if (document != null) {
                viewName = documentBS.getDocTemplateName(siteCode, document.getDocTypeID());
            }
        } catch (Exception e) {
            document = null;
        }
        if (viewName == null || "".equals(viewName)) {
            viewName = IApiConstants.VIEW_DOCUMENT;
        }

        if (document == null) {
            return new ModelAndView(siteCode + "/" + IApiConstants.VIEW_DOCTYPE_COMMON);
        }

        HashMap<String, String> docMetaMap = new HashMap<String, String>();
        docMetaMap.put("keywords", document.getSeoKeyWord());
        docMetaMap.put("description", document.getTitelAbstract());

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(IApiConstants.UI_KEY, document);
        map.put("documentInfo", docMetaMap);
        setSiteAndDocTypeSEOInfo(siteCode, document.getDocTypeID(), map);

        return new ModelAndView(siteCode + "/" + viewName, map);
    }

    private void setSiteAndDocTypeSEOInfo(String siteCode, String docTypeId, HashMap<String, Object> map) {
        Website webSite = null;
        List<Website> websites = dataCache.getWebsitesData();
        if (websites != null && websites.size() > 0) {
            for (Website site : websites) {
                if (site.getWebsiteCode().equals(siteCode)) {
                    webSite = site;
                    break;
                }
            }
            if (webSite == null) {
                return;
            }
        } else {
            return;
        }

        DocTypeVo docType = dataCache.getDocTypeByIds(docTypeId, webSite.getId());
        HashMap<String, String> docTypeMap = new HashMap<String, String>();
        docTypeMap.put("keywords", docType.getKeywords());
        docTypeMap.put("description", docType.getDescription());
        map.put("docTypeInfo", docTypeMap);
    }
}
