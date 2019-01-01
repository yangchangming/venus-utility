package venus.portal.doctype.bs.impl;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.base.exception.BaseApplicationException;
import venus.frames.i18n.util.LocaleHolder;
import venus.frames.mainframe.util.Helper;
import venus.oa.helper.AuHelper;
import venus.oa.helper.LoginHelper;
import venus.portal.au.bs.impl.AuRelationBS;
import venus.portal.cache.data.DataCache;
import venus.portal.doctype.bs.IDocTypeBS;
import venus.portal.doctype.dao.IDocTypeDao;
import venus.portal.doctype.model.DocType;
import venus.portal.doctype.util.IConstants;
import venus.portal.doctype.vo.DocTypeTreeCacheVo;
import venus.portal.doctype.vo.DocTypeTreeVo;
import venus.portal.doctype.vo.DocTypeVo;
import venus.portal.template.dao.ITemplateDao;
import venus.portal.template.model.EwpTemplate;
import venus.portal.template.util.ITemplateConstants;
import venus.portal.util.CommonFieldConstants;
import venus.portal.util.PinYinStringUtil;
import venus.portal.website.dao.IWebsiteDao;
import venus.portal.website.model.Website;
import venus.pub.lang.OID;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class DocTypeBS extends BaseBusinessService implements IDocTypeBS, IConstants {

    private IDocTypeDao dao;
    private ITemplateDao templateDao;
    private IWebsiteDao websiteDao;
    private DataCache dataCache;
    private AuRelationBS auRelationBS;

    public IDocTypeDao getDao() {
        return dao;
    }

    public void setDao(IDocTypeDao dao) {
        this.dao = dao;
    }

    /**
     * @return the templateDao
     */
    public ITemplateDao getTemplateDao() {
        return templateDao;
    }

    /**
     * @param templateDao the templateDao to set
     */
    public void setTemplateDao(ITemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    public void setDataCache(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    public AuRelationBS getAuRelationBS() {
        return auRelationBS;
    }

    public void setAuRelationBS(AuRelationBS auRelationBS) {
        this.auRelationBS = auRelationBS;
    }

    private int increaseLoginIdExt(String code, String siteId, int count) {
        DocType doctype = this.getDocTypeByCode(code + ((0 == count) ? "" : String.valueOf(count)), siteId);
        if (doctype != null) {
            return increaseLoginIdExt(code, siteId, count + 1);
        }
        return count;
    }

    /*
    *@see udp.ewp.doctype.bs.IDocTypeBS#save(udp.ewp.doctype.model.DocType)
     */
    public OID save(DocType docType) throws BaseApplicationException {
        OID oid = Helper.requestOID(DOCUMENT_TYPE_OID);
        String docTypeID = String.valueOf(oid);
        docType.setId(docTypeID);
        String name = docType.getName();
        if (docType.getDocTypeCode() == null || "".equals(docType.getDocTypeCode())) {
            String code = PinYinStringUtil.getAllFirstLetter(name);
            int count = increaseLoginIdExt(code, docType.getSite_id(), 0);
            docType.setDocTypeCode(code + ((0 == count) ? "" : String.valueOf(count)));
        }
        dao.save(docType);
        return oid;
    }

    /*
    *@see udp.ewp.doctype.bs.IDocTypeBS#delete(udp.ewp.doctype.model.DocType)
     */
    public void delete(DocType docType) throws BaseApplicationException {
        dao.delete(docType);
    }

    /*
    *@see udp.ewp.doctype.bs.IDocTypeBS#update(udp.ewp.doctype.model.DocType)
     */
    public boolean update(DocType docType) throws BaseApplicationException {
        try {
            String name = docType.getName();
            if (docType.getDocTypeCode() == null || "".equals(docType.getDocTypeCode())) {
                String code = PinYinStringUtil.getAllFirstLetter(name);
                docType.setDocTypeCode(code);
            }
            dao.update(docType);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkDocTypeNameIsUniqueByWebSite(String webSiteId) {
        int count = dao.getDocTypeNameIsUniqueNumByWebSite(webSiteId);
        if (count > 0) {
            return true;
        }
        return false;
    }

    /*
    *@see udp.ewp.doctype.bs.IDocTypeBS#queryDocRecordCountByCondition(udp.ewp.doctype.vo.DocTypeTreeVo)
     */
    public int queryDocRecordCountByCondition(DocTypeTreeVo docTypeTreeVo) throws BaseApplicationException {
        return dao.queryDocRecordCountByCondition(docTypeTreeVo);
    }

    /*
    *@see udp.ewp.doctype.bs.IDocTypeBS#queryAllDocByCondition(int, int, java.lang.String, udp.ewp.doctype.vo.DocTypeTreeVo)
     */
    public List queryAllDocByCondition(int currentPage, int pageSize, String orderStr, DocTypeTreeVo docTypeTreeVo) throws BaseApplicationException {
        return dao.queryAllDoc(currentPage, pageSize, orderStr, docTypeTreeVo);
    }

    /*
    *@see udp.ewp.doctype.bs.IDocTypeBS#getDocumentCountByDocTypeId(java.lang.String)
     */
    public int getDocumentCountByDocTypeId(String docTypeID) {
        DocType dt = this.findDocTypeById(docTypeID);
        return dt.getDocumentCountByDocTypeId(dao);
    }

    /*
    *@see udp.ewp.doctype.bs.IDocTypeBS#findDocTypeById(java.lang.String)
     */
    public DocType findDocTypeById(String nodeID) throws BaseApplicationException {
        DocType doctype = dao.findDocTypeById(nodeID);
        Hibernate.initialize(doctype.getTemplate());
        Hibernate.initialize(doctype.getDocTemplate());
        if (null != doctype && doctype.getSite() != null) {
            doctype.setSite_id(doctype.getSite().getId());
        }
        return doctype;
    }

    /*
    *@see udp.ewp.doctype.bs.IDocTypeBS#queryDocTypesByCondition(java.lang.String)
     */
    public List queryDocTypesByCondition(final String condition) {
        return dao.queryDocTypesByCondition(condition);
    }

    /*
    *@see udp.ewp.doctype.bs.IDocTypeBS#queryDocTypesById(java.lang.String)
     */
    public DocType queryDocTypesById(final String docTypeId) {
        return dao.queryDocTypesById(docTypeId);
    }


    /*
    *@see udp.ewp.doctype.bs.IDocTypeBS#queryAllNode(java.lang.String, java.lang.String)
     */
    public List queryAllNode(String root, HashSet notIncludeSet, String site_id) throws BaseApplicationException {
        return dao.queryAllNode(root, notIncludeSet, site_id);
    }

    /*
    *@see udp.ewp.doctype.bs.IDocTypeBS#queryAllNode(java.lang.String)
     */
    public List queryAllNode(String root, String siteId) throws BaseApplicationException {
        return this.queryAllNode(root, new HashSet(), siteId);
    }

    public List queryAllAuNode(HashSet notIncludeSet, String site_id, HttpServletRequest request) throws BaseApplicationException {
        return dao.queryAllAuNode(queryUserAutority(site_id, request), notIncludeSet, site_id);
    }

    public List querySubAuNode(String parentId, String siteId, HttpServletRequest request) {
        List subNodes = new ArrayList();

        DocTypeTreeCacheVo treeCacheVo = dataCache.getDocTypeTree();
        DocTypeVo parentNode = dataCache.getDocTypeByIds(parentId, siteId);
        SortedSet<DocTypeVo> childrenNodes = parentNode.getChildren();
        HashSet<String> au = queryUserAutority(siteId, request);

        for (DocTypeVo subNode : childrenNodes) {
            if (treeCacheVo.isShow(subNode.getId(), au) || LoginHelper.getIsAdmin(request)) {
                DocTypeTreeVo treeVo = new DocTypeTreeVo(subNode);
                if (!StringUtils.equals(subNode.getParentID(), parentId)) {
                    treeVo.setName(treeVo.getName() + "(" + LocaleHolder.getMessage("udp.ewp.doctype.hang") + ")");
                }
                subNodes.add(treeVo);
            }
        }

        return subNodes;
    }

    /*
    *@see udp.ewp.doctype.bs.IDocTypeBS#addTreeNode(java.lang.String)
     */
    public DocType addTreeNode(String newDoctType) {
        //保存当前栏目
        JSONObject jsonParam = JSONObject.fromObject(newDoctType);
        DocType docType = (DocType) JSONObject.toBean(jsonParam, DocType.class);
        String sharedIds = docType.getSharedIds();
        String templateId = docType.getTemplateId();
        String docTemplateId = docType.getDocTemplateId();
        String site_id = docType.getSite_id();
        if (StringUtils.isBlank(site_id) || StringUtils.equals(CommonFieldConstants.UNDEFINED, site_id)) {
            throw new BaseApplicationException(LocaleHolder.getMessage("udp.ewp.doctype.siteid_is_null"));
        }
        if (site_id != null && !"".equals(site_id) && !("undefined".equals(site_id))) {
            Website website = websiteDao.findWebsiteById(site_id);
            docType.setSite(website);
        }
        if (templateId != null && !"".equals(templateId)) {
            EwpTemplate template = templateDao.findEwpTemplateById(templateId);
            docType.setTemplate(template);
        }
        if (docTemplateId != null && !"".equals(docTemplateId)) {
            EwpTemplate template = templateDao.findEwpTemplateById(docTemplateId);
            docType.setDocTemplate(template);
        }
        OID id = this.save(docType);
        docType = this.findDocTypeById(docType.getId());
        //保存直属栏目
        String parentId = docType.getParentID();
        DocType parentDocType = this.findDocTypeById(parentId);
        docType.setParent(new HashSet<DocType>());
        docType.getParent().add(parentDocType);
        //保存分享栏目
        if (StringUtils.isNotBlank(sharedIds)) {
            DocType sharedDocType;
            String[] selectedValueArray = sharedIds.split(",");
            for (String str : selectedValueArray) {
                sharedDocType = this.findDocTypeById(str);
                docType.getParent().add(sharedDocType);
            }
        }
        this.update(docType);
        return docType;
    }

    /*
     *@see udp.ewp.doctype.bs.IDocTypeBS#updateTreeNode(udp.ewp.doctype.model.DocType)
     */
    public String updateTreeNode(DocType newDocType) {
        String sharedIds = newDocType.getSharedIds();
        //更新本栏目
        DocType oldDocType = findDocTypeById(newDocType.getId());
        String parentID = oldDocType.getParentID();
        String id = oldDocType.getId();
        oldDocType.setSite_id(newDocType.getSite_id());
        oldDocType.setName(newDocType.getName());
        oldDocType.setSortNum(newDocType.getSortNum());
        oldDocType.setKeywords(newDocType.getKeywords());
        oldDocType.setDescription(newDocType.getDescription());
        oldDocType.setSortNum(newDocType.getSortNum());
        oldDocType.setIsValid(newDocType.getIsValid());
        oldDocType.setImagePath(newDocType.getImagePath());
        oldDocType.setDocTypeCode(newDocType.getDocTypeCode());
        oldDocType.setIsNavigateMenu(newDocType.getIsNavigateMenu());
        String templateId = newDocType.getTemplateId();
        String docTemplateId = newDocType.getDocTemplateId();
        if (templateId != null && !"".equals(templateId)) {
            EwpTemplate template = templateDao.findEwpTemplateById(templateId);
            oldDocType.setTemplate(template);
        } else {
            oldDocType.setTemplate(null);
        }
        if (docTemplateId != null && !"".equals(docTemplateId)) {
            EwpTemplate doctemplate = templateDao.findEwpTemplateById(docTemplateId);
            oldDocType.setDocTemplate(doctemplate);
        } else {
            oldDocType.setDocTemplate(null);
        }
        //更新挂接到的栏目
        Set<DocType> parent = oldDocType.getParent();
        Iterator it = parent.iterator();
        HashSet<DocType> newdocSet = new HashSet<DocType>();
        while (it.hasNext()) {
            DocType docType = (DocType) it.next();
            if (StringUtils.equals(docType.getId(), parentID)) {
                newdocSet.add(docType);
                break;
            }
        }
        oldDocType.setParent(newdocSet);
        if (StringUtils.isNotBlank(sharedIds)) {
            DocType sharedDocType;
            String[] selectedValueArray = sharedIds.split(",");
            for (String str : selectedValueArray) {
                sharedDocType = findDocTypeById(str);
                oldDocType.getParent().add(sharedDocType);
            }
        }
        this.update(oldDocType); //执行更新
        return "{flag:true,id:\"" + id + "\",name:\"" + newDocType.getName() + "\"}";
    }

    /*
     *@see udp.ewp.doctype.bs.IDocTypeBS#moveTreeNode(net.sf.json.JSONObject)
     */
    public String moveTreeNode(JSONObject jsonParam) {
        String id = (String) jsonParam.get("id");
        DocType oldDocType = findDocTypeById(id);
        String oldParentID = oldDocType.getParentID();
        String move_parentID = (String) jsonParam.get("move_parentID");
        oldDocType.setParentID(move_parentID);

        DocType oldParentDocType = findDocTypeById(oldParentID);
        DocType newParentDocType = findDocTypeById(move_parentID);
        oldDocType.getParent().remove(oldParentDocType);
        oldDocType.getParent().add(newParentDocType);
        update(oldDocType);
        return "{flag:true,id:\"" + id + "\"}";
    }

    /**
     * 取得一直到根的父节点IDs
     *
     * @param expandIds
     * @param doctype
     * @param docTypeBs
     */
    private void getParentString(StringBuffer expandIds, DocType doctype, IDocTypeBS docTypeBs) {
        Set<DocType> parents = doctype.getParent();
        if (parents != null && parents.size() > 0) {
            for (DocType parent : parents) {
                expandIds.append(parent.getId() + ",");
                do {
                    if (parent != null) {
                        getParentString(expandIds, parent, docTypeBs);
                    }
                    expandIds.append(parent.getParentID() + ",");
                    parent = docTypeBs.queryDocTypesById(parent.getParentID());
                } while (parent != null && parent.getParentID() != null);
            }
        }
    }

    /**
     * 更新栏目图片
     *
     * @param id
     * @param imagePath
     * @return
     * @throws BaseApplicationException
     */
    public boolean updateImagePath(String id, String imagePath) throws BaseApplicationException {
        try {
            DocType docType = dao.queryDocTypesById(id);
            docType.setImagePath(imagePath);
            dao.update(docType);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public EwpTemplate getTplById(String docTypeId) {
        return dao.findDocTypeById(docTypeId).getTemplate();
    }

    public EwpTemplate getDocTplById(String docTypeId) {
        return dao.findDocTypeById(docTypeId).getDocTemplate();
    }

    /* (non-Javadoc)
     * @see udp.ewp.doctype.bs.IDocTypeBS#getDocTypeByCode(java.lang.String)
     */
    public DocType getDocTypeByCode(String code, String siteId) {
        DocType result = dao.getDocTypeByCode(code, siteId);

        return result;
    }

    public DocType getDocTypeByName(String code, String siteId) {
        DocType result = dao.getDocTypeByName(code, siteId);

        return result;
    }

    public IWebsiteDao getWebsiteDao() {
        return websiteDao;
    }

    public void setWebsiteDao(IWebsiteDao websiteDao) {
        this.websiteDao = websiteDao;
    }

    /**
     * @return
     */
    public HashMap<String, DocTypeVo> queryAllVo() {
        HashMap<String, DocTypeVo> map = dao.queryAllVo();

        // 加载栏目模板和文档模板数据
        for (DocTypeVo vo : map.values()) {
            Hibernate.initialize(vo.getTemplate());
            Hibernate.initialize(vo.getDocTemplate());
        }

        return map;
    }

    /**
     * @param siteId
     * @return
     */
    public List<DocType> queryAllBySiteId(String siteId) {
        return dao.queryAllBySiteId(siteId);
    }

    public String getTemplateName(String siteCode, String docTypeCode) {
        String viewName = "";
        EwpTemplate tpl = dataCache.getTemplateData(siteCode, docTypeCode, ITemplateConstants.DOCTYPE_TEMPLATE);

        if (tpl != null) {
            viewName = tpl.getTemplate_name();
        }

        return viewName;
    }

    public DocTypeVo getDocTypeFromCache(String siteCode, String docTypeCode) {
        return dataCache.getData().get(docTypeCode + siteCode);
    }

    public List<DocType> queryAllRootNode(HttpServletRequest request) {
        List<DocType> rootList = dao.queryAllRootNode();

        if (LoginHelper.getIsAdmin(request)) {
            return rootList;
        }

        List<DocType> resultList = new ArrayList<DocType>();

        for (DocType dt : rootList) {
            DocTypeTreeCacheVo treeCacheVo = dataCache.getDocTypeTree();
            if (treeCacheVo.isShow(dt.getId(), queryUserAutority(dt.getSite().getId(), request))) {
                resultList.add(dt);
            }
        }

        return resultList;
    }

    public DocTypeTreeCacheVo getDocTypeTreeMap(HashMap<String, DocTypeVo> docTypeMap) {
        return new DocTypeTreeCacheVo(docTypeMap);
    }

    public String getParentPath(DocType docType) {
        List<String> pathList = new ArrayList<String>();
        StringBuffer path = new StringBuffer();

        DocTypeVo parent = dataCache.getDocTypeByIds(docType.getParentID(), docType.getSite().getId());
        while (parent != null) {
            pathList.add(parent.getName());
            parent = dataCache.getDocTypeByIds(parent.getParentID(), parent.getSite().getId());
        }

        for (int i = pathList.size() - 1; i >= 0; i--) {
            path.append(pathList.get(i)).append(" > ");
        }
        path.delete(path.length() - 3, path.length());

        return path.toString();
    }

    public Boolean checkAuthority(HttpServletRequest request) {
        List<Website> siteList = websiteDao.queryAll();
        for (Website site : siteList) {
            HashSet au = queryUserAutority(site.getId(), request);
            if (au.size() > 0) {
                return true;
            }
        }

        return false;
    }
    
    public String getDocTypeCodeById(String docTypeId) {
    	return dataCache.getDocTypeCodeById(docTypeId);
    }

    private HashSet queryUserAutority(String site_id, HttpServletRequest request) throws BaseApplicationException {
        if(LoginHelper.getLoginId(request)==null){//未登陆用户显示全部栏目，当功能权限启用后，有功能权限限制用户是否能进入栏目的功能页面
            HashSet rsSet=new HashSet();
            List<DocType> docTypeList = queryAllBySiteId(site_id);
            for (DocType o : docTypeList) {
                rsSet.add(o.getId());
            }
            return rsSet;
        }

        String userPartyId = LoginHelper.getPartyId(request);
        String[] rolePartyIds = AuHelper.getRolePartyIdByRequest(request);
        List userPartyIdArray = new ArrayList();
        if (userPartyId != null) {
            userPartyIdArray = Arrays.asList(userPartyId);
        }
        List rolePartyIdArray = new ArrayList();
        if (rolePartyIds != null) {
            rolePartyIdArray = Arrays.asList(rolePartyIds);
        }

        return auRelationBS.queryDoctypes(userPartyIdArray, rolePartyIdArray, site_id);
    }

}
