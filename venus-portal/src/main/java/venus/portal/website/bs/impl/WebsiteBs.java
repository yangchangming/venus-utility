package venus.portal.website.bs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.i18n.util.LocaleHolder;
import venus.portal.cache.data.DataCache;
import venus.portal.doctype.bs.IDocTypeBS;
import venus.portal.doctype.model.DocType;
import venus.portal.doctype.util.IConstants;
import venus.portal.helper.EwpVoHelper;
import venus.portal.template.bs.ITemplateBs;
import venus.portal.util.BooleanConstants;
import venus.portal.util.CommonFieldConstants;
import venus.portal.util.MultiLanguageConstants;
import venus.portal.website.bs.IWebsiteBs;
import venus.portal.website.dao.IWebsiteDao;
import venus.portal.website.model.Website;
import venus.portal.website.util.IWebsiteConstants;

import java.util.List;
import java.util.Set;

public class WebsiteBs extends BaseBusinessService implements IWebsiteBs, IWebsiteConstants {

    private IWebsiteDao dao = null;
    private IDocTypeBS docTypeBS = null;

    @Autowired
    @Qualifier("ITemplateBs_target")
    private ITemplateBs templateBs;

    // 缓存
    private DataCache dataCache;

    public IWebsiteDao getDao() {
        return dao;
    }

    public void setDao(IWebsiteDao dao) {
        this.dao = dao;
    }

    public void setDataCache(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    /**
     * 插入单条记录
     *
     * @param vo 用于添加的VO对象
     */
    public void insert(Website vo) {
        List list = getDao().queryAll();
        if (list == null || list.size() <= 0) {
            vo.setIsDefault(BooleanConstants.YES);
        }

        // 保存网站到数据库
        getDao().insert(vo);

        // 同时保存一条栏目数据
        DocType docType = new DocType();
        docType.setDescription((vo.getDescription()==null?"":vo.getDescription()));
        docType.setKeywords((vo.getKeywords()==null?"":vo.getKeywords()));
        docType.setSite_id(vo.getId());
        docType.setSite(vo);
        docType.setDocTypeCode(CommonFieldConstants.ROOT);
        docType.setIsValid(IConstants.CONSTANTS_TRUE);
        docType.setSortNum(Long.valueOf(0));
        docType.setName(vo.getWebsiteName());
        docType.setSite(vo);// 设置栏目所属站点
        getDocTypeBS().save(docType);
        vo.setRootChannel(docType);// 设置站点的根栏目
        getDao().update(vo);

        templateBs.createNewWebSiteBaseTemplate(vo);
    }

    /**
     * 删除多条记录
     *
     * @param ids 用于删除的记录的id
     * @return 成功删除的记录数
     * @throws Exception
     */
    public int deleteMulti(String ids[]) throws Exception {
        for (String id : ids) {
            delete(find(id));
        }
        return 0;
    }

    /**
     * 根据Id进行查询
     *
     * @param id 用于查找的id
     * @return 查询到的VO对象
     */
    public Website find(String id) {
        Website site = getDao().findWebsiteById(id);
        EwpVoHelper.null2Nothing(site);
        return site;
    }

    /**
     * 更新单条记录
     *
     * @param vo 用于更新的VO对象
     * @return 成功更新的记录数
     */
    public void update(Website vo) {
        getDao().update(vo);
    }

    /**
     * 查询总记录数，带查询条件
     *
     * @param queryCondition 查询条件
     * @return 总记录数
     */
    public int getRecordCount(String queryCondition) {
        return getDao().getRecordCount(queryCondition);
    }

    /**
     * 通过查询条件获得所有的VO对象列表，带翻页，带排序字符
     *
     * @param no             当前页数
     * @param size           每页记录数
     * @param queryCondition 查询条件
     * @param orderStr       排序字符
     * @return 查询到的VO列表
     */
    public List queryByCondition(int no, int size, String queryCondition, String orderStr) {
        return getDao().queryByCondition(no, size, queryCondition, orderStr);
    }

    /**
     * 删除单条记录
     *
     * @param website 用于删除的记录的VO
     * @return 成功删除的记录数
     */
    public void delete(Website website) throws Exception {
        DocType docType = getDocTypeBS().getDocTypeByCode(CommonFieldConstants.ROOT, website.getId());
        //必须有一个网站（一个网站都没有时，没有默认网站及修改网站相应信息页面出错）。
        if (dataCache.getWebsitesData().size() > 1) {
            if (docType != null) {
                Set<DocType> docTypeSet = docType.getChildren();
                if (docTypeSet != null && docTypeSet.size() > 0) {
                    throw new Exception(LocaleHolder.getMessage(MultiLanguageConstants.UDP_EWP_WEBSITE_DELETE_HAVE_DOC_TYPE_NO));
                } else {
                    getDao().delete(docType.getSite());
                    getDocTypeBS().delete(docType);
                    templateBs.deleteByWebSiteId(website.getId());
                }
            }
        } else {
            throw new Exception(LocaleHolder.getMessage(MultiLanguageConstants.UDP_EWP_WEBSITE_DELETE_LAST_NO));
        }
    }

    /**
     * 通过网站名称找网站实体
     *
     * @param name 网站名称
     * @return 网站和实体对象
     */
    public Website getWebsiteByName(String name) {
        Website result = getDao().getWebsiteByName(name);
        return result;
    }

    public void updateDefaultWebsite(String id) {
        List list = getDao().getWebsiteByIsDefault(BooleanConstants.YES);
        if (list.size() > 0) {
            Website beanNo = (Website) list.get(0);
            if (id.equals(beanNo.getId())) {
                return;
            }
            if (!beanNo.getIsDefault().equals(BooleanConstants.NO)) {
                beanNo.setIsDefault(BooleanConstants.NO);
                update(beanNo);
            }
        }

        Website beanYes = getDao().findWebsiteById(id);
        beanYes.setIsDefault(BooleanConstants.YES);
        beanYes.setVersion(null);
        update(beanYes);
    }

    public List queryAll() {
        return getDao().queryAll();
    }

    public IDocTypeBS getDocTypeBS() {
        return docTypeBS;
    }

    public void setDocTypeBS(IDocTypeBS docTypeBS) {
        this.docTypeBS = docTypeBS;
    }

    public boolean checkWebsiteCodeIsUnique(String websiteCode) {
        Website ws = dao.getWebsiteByCode(websiteCode);

        if (ws != null) {
            return false;
        } else {
            return true;
        }
    }


}
