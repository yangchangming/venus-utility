/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.cache.data.impl;

//import venus.portal.gbox.resource.option.bs.IOptionBs;
//import venus.portal.gbox.resource.option.vo.OptionVo;
import venus.portal.cache.BaseCache;
import venus.portal.cache.data.DataCache;
import venus.portal.cache.util.IConstants;
import venus.portal.doctype.bs.IDocTypeBS;
import venus.portal.doctype.vo.DocTypeTreeCacheVo;
import venus.portal.doctype.vo.DocTypeVo;
import venus.portal.gbox.resource.option.bs.IOptionBs;
import venus.portal.gbox.resource.option.vo.OptionVo;
import venus.portal.hotwords.bs.IHotWordsBs;
import venus.portal.hotwords.model.HotWords;
import venus.portal.template.bs.ITemplateBs;
import venus.portal.template.model.EwpTemplate;
import venus.portal.template.util.ITemplateConstants;
import venus.portal.util.BooleanConstants;
import venus.portal.website.bs.IWebsiteBs;
import venus.portal.website.model.Website;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 数据缓存接口实现。
 */
public class DataCacheImpl implements DataCache {
    private BaseCache baseCache;

    public void setBaseCache(BaseCache baseCache) {
        this.baseCache = baseCache;
    }

    private IDocTypeBS docTypeBS;
    private IWebsiteBs websiteBs;
    private ITemplateBs templateBs;
    private IHotWordsBs hotWordsBs;
    private IOptionBs optionBs;

    public void setDocTypeBS(IDocTypeBS docTypeBS) {
        this.docTypeBS = docTypeBS;
    }

    public void setWebsiteBs(IWebsiteBs websiteBs) {
        this.websiteBs = websiteBs;
    }

    public void setTemplateBs(ITemplateBs templateBs) {
        this.templateBs = templateBs;
    }

    public void setHotWordsBs(IHotWordsBs hotWordsBs) {
        this.hotWordsBs = hotWordsBs;
    }

    public void setOptionBs(IOptionBs optionBs) {
        this.optionBs = optionBs;
    }

    /* (non-Javadoc)
     * @see udp.ewp.cache.data.DataCache#loadData()
     */
    public void loadData() {
        // 加载栏目缓存
        refreshData();
        // 加载站点缓存
        refreshWebsites();
        // 加载热词缓存
        refreshHotWords();
        // 加载系统配置项
        refreshSysOption();
    }

    /**
     * 刷新指定站点的数据。
     */
    public void refreshData() {
        HashMap<String,DocTypeVo> result = docTypeBS.queryAllVo();
        baseCache.put(IConstants.BASE_DATA_KEY, result);
        baseCache.put(IConstants.DOCTYPECODES,getDoctypeCodeMap(result));
        baseCache.put(IConstants.DOCTYPETREE, docTypeBS.getDocTypeTreeMap(result));
    }
    
    public void refreshWebsites() {
        List<Website> websiteList = websiteBs.queryAll();
        baseCache.put(IConstants.WEBSITES, websiteList);    //放置站点缓存
        baseCache.put(IConstants.WEBSITECODES, getWebsiteCodeMap(websiteList));
        baseCache.put(IConstants.DEFAULTTEMPLATES, getDefaultTemplateMap(websiteList));
    }

    public void refreshHotWords() {
        List<HotWords> hotWordsList = hotWordsBs.queryAll();
        baseCache.put(IConstants.HOTWORDS, generHotWordsMap(hotWordsList));
        baseCache.put(IConstants.HOTWORDS_PATTERN, generHotWordsPattern(hotWordsList));
    }

    public void refreshSysOption() {
        List<OptionVo> optionList = optionBs.queryAll();
        HashMap<String, OptionVo> optionMap = new HashMap<String, OptionVo>();
        for (OptionVo vo : optionList) {
            optionMap.put(vo.getCode(), vo);
        }
        baseCache.put(IConstants.SYSTEM_OPTION, optionMap);
    }

    /* (non-Javadoc)
     * @see udp.ewp.cache.data.DataCache#removeData()
     */
    public void removeData() {
        baseCache.remove(IConstants.BASE_DATA_KEY);
        baseCache.remove(IConstants.WEBSITES);
        baseCache.remove(IConstants.DOCTYPECODES);
        baseCache.remove(IConstants.WEBSITECODES);
        baseCache.remove(IConstants.DEFAULTTEMPLATES);
        baseCache.remove(IConstants.DOCTYPETREE);
        baseCache.remove(IConstants.HOTWORDS);
        baseCache.remove(IConstants.HOTWORDS_PATTERN);
        baseCache.remove(IConstants.SYSTEM_OPTION);
    }

    /* (non-Javadoc)
     * @see udp.ewp.cache.data.DataCache#getData()
     */
    public HashMap<String,DocTypeVo> getData() {
        return (HashMap<String,DocTypeVo>) baseCache.get(IConstants.BASE_DATA_KEY);
    }
    
    public List<Website> getWebsitesData() {
        return (List<Website>) baseCache.get(IConstants.WEBSITES);
    }

    public String getDocTypeCodeById(String docTypeId) {
        HashMap<String, String> map = (HashMap<String, String>) baseCache.get(IConstants.DOCTYPECODES);
        return map.get(docTypeId);
    }

    public String getWebSiteCodeById(String webSiteId) {
        HashMap<String, String> map = (HashMap<String, String>) baseCache.get(IConstants.WEBSITECODES);
        return map.get(webSiteId);
    }

    public DocTypeVo getDocTypeByIds(String docTypeId, String webSiteId) {
        String docTypeCode = getDocTypeCodeById(docTypeId);
        String webSiteCode = getWebSiteCodeById(webSiteId);
        return getData().get(docTypeCode + webSiteCode);
    }

    public EwpTemplate getTemplateData(String siteCode, String docTypeCode, String templateType) {
        EwpTemplate tpl = null;
        boolean isDocTemple = templateType.equals(ITemplateConstants.DOCUMENT_TEMPLATE) ? true : false;
        HashMap<String, DocTypeVo> docTypes = getData();
        DocTypeVo dt= docTypes.get(docTypeCode + siteCode);

        if (dt != null) {
            if (!isDocTemple && dt.getTemplate() != null) {
                tpl = dt.getTemplate();
            } else if(isDocTemple && dt.getDocTemplate() != null) {
                tpl = dt.getDocTemplate();
            }

            // 未找到，递归查找父栏目
            while (tpl == null) {
                if (dt.getParentID() == null || dt.getParentID().isEmpty()) {
                    break;
                }

                String docTypeKey = getDocTypeCodeById(dt.getParentID()) + siteCode;
                dt = docTypes.get(docTypeKey);
                if (!isDocTemple) {
                    tpl = dt.getTemplate();
                } else {
                    tpl = dt.getDocTemplate();
                }
            }

            if(tpl == null) {
                // 查找当前站点所挂接的默认文档模板
                HashMap<String,EwpTemplate> defTemplate = getDefaultTemplateData();
                tpl = defTemplate.get(siteCode + templateType);
            }
        }

        return tpl;
    }

    public DocTypeTreeCacheVo getDocTypeTree() {
        return (DocTypeTreeCacheVo) baseCache.get(IConstants.DOCTYPETREE);
    }

    public List<Pattern> getHotWordsPattern() {
        return (List<Pattern>) baseCache.get(IConstants.HOTWORDS_PATTERN);
    }

    public HashMap<String, String> getHotWords() {
        return (HashMap<String, String>) baseCache.get(IConstants.HOTWORDS);
    }

    public HashMap<String, OptionVo> getSystemOption() {
        return (HashMap<String, OptionVo>) baseCache.get(IConstants.SYSTEM_OPTION);
    }

    private HashMap<String, EwpTemplate> getDefaultTemplateData() {
        return (HashMap<String, EwpTemplate>) baseCache.get(IConstants.DEFAULTTEMPLATES);
    }

    private HashMap<String,String> getDoctypeCodeMap(HashMap<String,DocTypeVo> docTypeMap) {
        HashMap<String,String>  codeMap = new HashMap<String, String>();

        Set<String> keys = docTypeMap.keySet();
        for (String k : keys) {
            codeMap.put(docTypeMap.get(k).getId(), docTypeMap.get(k).getDocTypeCode());
        }

        return codeMap;
    }

    private HashMap<String, String> getWebsiteCodeMap(List<Website> siteList) {
        HashMap<String, String> codeMap = new HashMap<String, String>();

        for (Website site : siteList) {
            codeMap.put(site.getId(), site.getWebsiteCode());
        }

        return codeMap;
    }

    private HashMap<String,EwpTemplate> getDefaultTemplateMap(List<Website> websiteList) {
        HashMap<String,EwpTemplate>  tplMap = new HashMap<String, EwpTemplate>();

        for (Website site: websiteList) {
            List<EwpTemplate> tplList = templateBs.getTemplateByIsDefault(BooleanConstants.YES, site.getId());
            for (EwpTemplate tpl : tplList) {
                tplMap.put(site.getWebsiteCode() + tpl.getTemplate_type(), tpl);
            }
        }

        return tplMap;
    }

    private HashMap<String, String> generHotWordsMap(List<HotWords> hotWordsList) {
        HashMap<String, String> hotwords = new HashMap<String, String>();

        for (HotWords hw : hotWordsList) {
            hotwords.put(hw.getName(), hw.getLink());
        }

        return hotwords;
    }

    private List<Pattern> generHotWordsPattern(List<HotWords> hotWordsList) {
        List<Pattern> patterns = new ArrayList<Pattern>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hotWordsList.size(); i++) {
            HotWords hw = hotWordsList.get(i);
            sb.append(hw.getName() + "|");

            // 每500个热词生成一个Pattern
            if (i % 500 == 0 || i == hotWordsList.size() - 1) {
                sb.delete(sb.length() - 1, sb.length());
                Pattern pt = Pattern.compile(sb.toString());
                patterns.add(pt);
                sb.delete(0, sb.length());
            }
        }
        return patterns;
    }
}
