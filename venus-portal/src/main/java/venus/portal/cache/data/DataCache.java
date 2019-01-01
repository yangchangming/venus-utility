/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.cache.data;


import venus.portal.doctype.vo.DocTypeTreeCacheVo;
import venus.portal.doctype.vo.DocTypeVo;
import venus.portal.gbox.resource.option.vo.OptionVo;
import venus.portal.template.model.EwpTemplate;
import venus.portal.website.model.Website;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 数据缓存接口。
 */
public interface DataCache {

    /**
     * 将数据放入缓存。
     */
    public void loadData();

    /**
     * 将数据从缓存移除。
     */
    public void removeData();

    /**
     * 刷新特定站点的栏目数据。
     */
    public void refreshData();

    /**
     * 刷新站点数据。
     */
    public void refreshWebsites();

    /**
     * 刷新系统配置项
     */
    public void refreshSysOption();

    /**
     * 获取所有缓存数据。
     */
    public HashMap<String, DocTypeVo> getData();

    /**
     * 获取站点数据
     *
     * @return
     */
    public List<Website> getWebsitesData();

    /**
     * 获取栏目id对应的栏目code
     *
     * @param docTypeId 栏目id
     * @return
     */
    public String getDocTypeCodeById(String docTypeId);

    public String getWebSiteCodeById(String webSiteId);

    public DocTypeVo getDocTypeByIds(String docTypeId, String webSiteId);

    public DocTypeTreeCacheVo getDocTypeTree();

    /**
     * 根据站点code、栏目code获取指定类型的模板数据
     *
     * @param siteCode     站点code
     * @param docTypeCode  栏目code
     * @param templateType 模板类型
     * @return
     */
    public EwpTemplate getTemplateData(String siteCode, String docTypeCode, String templateType);

    /**
     * 获取热词的Pattern，用于进行热词匹配
     *
     * @return
     */
    public List<Pattern> getHotWordsPattern();

    /**
     * 刷新热词缓存
     */
    public void refreshHotWords();

    /**
     * 获取热词数据
     *
     * @return
     */
    public HashMap<String, String> getHotWords();

    /**
     * 获取系统配置项数据
     *
     * @return
     */
    public HashMap<String, OptionVo> getSystemOption();
}
