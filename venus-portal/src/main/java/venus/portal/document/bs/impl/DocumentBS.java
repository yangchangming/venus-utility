package venus.portal.document.bs.impl;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import venus.frames.base.bs.BaseBusinessService;
import venus.frames.base.exception.BaseApplicationException;
import venus.frames.mainframe.util.Helper;
import venus.portal.cache.data.DataCache;
import venus.portal.document.bs.IDocumentBS;
import venus.portal.document.dao.IDocumentDao;
import venus.portal.document.model.Document;
import venus.portal.document.util.IConstants;
import venus.portal.document.vo.DocumentVo;
import venus.portal.documenttyperelation.dao.IDocumentTypeRelationDao;
import venus.portal.documenttyperelation.model.DocumentTypeRelation;
import venus.portal.searchengine.lucene.Operation.impl.OperationImpl;
import venus.portal.searchengine.lucene.operationvo.index.DocVo;
import venus.portal.template.model.EwpTemplate;
import venus.portal.template.util.ITemplateConstants;
import venus.portal.timerelease.dao.IEwpDocTimeReleaseDao;
import venus.portal.timerelease.model.EwpDocumentTimeRelease;
import venus.portal.util.HTMLTagConstants;
import venus.portal.vo.PageResults;
import venus.portal.website.model.Website;
import venus.pub.lang.OID;
import venus.pub.util.StringUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yangchangming
 */
public class DocumentBS extends BaseBusinessService implements IDocumentBS, IConstants {

    private IDocumentDao dao;
    private IDocumentTypeRelationDao docTypeRelationdao;
    private IEwpDocTimeReleaseDao ewpDocTimeReleaseDao;
    private DataCache dataCache;
    private OperationImpl operation;

    public OperationImpl getOperation() {
        return operation;
    }

    public void setOperation(OperationImpl operation) {
        this.operation = operation;
    }

    public void setDataCache(DataCache dataCache) {
        this.dataCache = dataCache;
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#findDocById(java.lang.String)
     */
    public Document findDocById(String docId) {
        Object docObj = dao.findDocById(docId);
        if (docObj instanceof Document) {
            return (Document) docObj;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#queryAllDocByWebSitId(
     *      java.lang.String,  java.lang.String)
     */
    public List queryAllDocByWebSiteId(String orderStr, String webSiteId) {
        return dao.queryAllDocByWebSiteId(orderStr, webSiteId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#queryAllDocByWebSitId(int, int,
     *      java.lang.String, udp.ewp.document.vo.DocumentVo, java.lang.String)
     */
    public List queryAllDocByWebSiteId(int currentPage, int pageSize,
                                       String orderStr, DocumentVo docVo, String webSiteId) {
        return dao.queryAllDocByWebSiteId(currentPage, pageSize, orderStr, docVo, webSiteId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#queryAllDocRecordCountByWebSiteId(udp.ewp.document.vo.DocumentVo, java.lang.String)
     */
    public int queryAllDocRecordCountByWebSiteId(DocumentVo docVo, String webSiteId) {
        return dao.queryAllDocRecordCountByWebSiteId(docVo, webSiteId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#queryAllDocByCondition(int, int,
     *      java.lang.String, udp.ewp.document.vo.DocumentVo)
     */
    public List queryAllDocByCondition(int currentPage, int pageSize,
                                       String orderStr, DocumentVo docVo) {
        return dao.queryAllDoc(currentPage, pageSize, orderStr, docVo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#queryDocRecordCountByCondition(udp.ewp.document.vo.DocumentVo)
     */
    public int queryDocRecordCountByCondition(DocumentVo docVo) {
        return dao.queryDocRecordCountByCondition(docVo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#batchDeletDoc(java.lang.String[])
     */
    public int batchDelete(String docIDs) {
        String values = "";
        int deleteCount = 0;

        if (StringUtils.isNotBlank(docIDs)) {
            values = StringUtil.trimString(docIDs);
            values = values.substring(0, values.length() - 1);
        }

        String[] docIDS = (values).split(",");
        for (int i = 0; i < docIDS.length; i++) {
            Document doc = (Document) dao.findDocById(docIDS[i]);
            String status = doc.getStatus();
            if (!DOC_STATUS_PUBLISHED.equals(status)) { //如果不是发布状态的则可以删除，已发布状态的文档不允许删除
                if (!LOGIC_FALSE.equals(doc.getIsValid())) {
                    // 假删除，置为无效
                    doc.setIsValid(LOGIC_FALSE);
                    // 文档置为 归档 状态
                    doc.setStatus(DOC_STATUS_ARCHIVE);
                }

                // 删除ewp_document_type_relation表中的关联数据
                List<DocumentTypeRelation> temps = docTypeRelationdao
                        .getDocTypeRelationsByDocId(docIDS[i], DOCTYPE_DOC_ACTIVE);// 查找可用的
                for (DocumentTypeRelation relation : temps) {
                    relation.setIsActive(DOCTYPE_DOC_NOT_ACTIVE);// 0为非活跃
                    docTypeRelationdao.delete(relation);
                }

                // 删除ewp_document_time_release表中的关联数据
                List<EwpDocumentTimeRelease> releaseList = ewpDocTimeReleaseDao.queryByDocId(docIDS[i]);
                if (releaseList != null && releaseList.size() > 0) {
                    ewpDocTimeReleaseDao.delete(releaseList.get(0));
                }

                //物理删除
                dao.delete(doc);
                deleteCount++;
            }
        }
        return deleteCount;
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#createDocument(udp.ewp.document.model.Document)
     */
    public OID save(Document doc) {
        DocumentTypeRelation dtr = new DocumentTypeRelation();
        OID oid = Helper.requestOID(DOCUMENT_OID);
        String docID = String.valueOf(oid);
        doc.setId(docID);
        dao.save(doc);
        // 先保存文档，才能增加文档和文档类型关联关系记录；否则外键关联会出错
        dtr.addRelation(doc.getId(), doc.getDocTypeID(), docTypeRelationdao, doc.getStatus());
        return oid;
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#deleteDocument(java.lang.String)
     */
    public int delete(String docID) {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#editDocument(java.lang.String)
     */
    private void update(Document doc) {
        dao.update(doc);
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#previewDocument(java.lang.String)
     */
    public void previewDocument(String docID) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#transferDocument(java.lang.String)
     */
    private void transferDocument(String docID, String srcDocTypeID,
                                  String destDocTypeID) {
        if ("".equals(srcDocTypeID) || srcDocTypeID == null
                || "".equals(destDocTypeID) || destDocTypeID == null) {
            return;
        }
        DocumentTypeRelation tempSrc = docTypeRelationdao.getDocumentTypeRelation(
                srcDocTypeID, docID);
        // 转移文章，先把原来的关系删除，再创建新的关系
        if (tempSrc == null) {
            return;
        }
        docTypeRelationdao.delete(tempSrc);

        DocumentTypeRelation tempDest = docTypeRelationdao.getDocumentTypeRelation(
                destDocTypeID, docID);
        // 当原来的此栏目已有此文档，不需创建新的关系
        if (tempDest != null) {
            return;
        }

        DocumentTypeRelation dtr = new DocumentTypeRelation();
        Document doc = this.findDocById(docID);
        // 更新文档中docTypeId的值
        doc.setDocTypeID(destDocTypeID);
        getDao().update(doc);
        // 添加新关系
        dtr.addRelation(docID, destDocTypeID, docTypeRelationdao, doc.getStatus());
    }

    /**
     * @return the dao
     */
    public IDocumentDao getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(IDocumentDao dao) {
        this.dao = dao;
    }

    /**
     * @return the docTypeRelationdao
     */
    public IDocumentTypeRelationDao getDocTypeRelationdao() {
        return docTypeRelationdao;
    }

    /**
     * @param docTypeRelationdao the docTypeRelationdao to set
     */
    public void setDocTypeRelationdao(
            IDocumentTypeRelationDao docTypeRelationdao) {
        this.docTypeRelationdao = docTypeRelationdao;
    }

    /**
     * @param ewpDocTimeReleaseDao the ewpDocTimeReleaseDao to set
     */
    public void setEwpDocTimeReleaseDao(
            IEwpDocTimeReleaseDao ewpDocTimeReleaseDao) {
        this.ewpDocTimeReleaseDao = ewpDocTimeReleaseDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#shareDocument(java.lang.String,
     *      java.lang.String)
     */
    private void shareDocument(String docID, String docTypeId) {
        DocumentTypeRelation dtr = new DocumentTypeRelation();
        // 先保存文档，才能增加文档和文档类型关联关系记录；否则外键关联会出错
        DocumentTypeRelation temp = docTypeRelationdao.getDocumentTypeRelation(
                docTypeId, docID);
        if (temp == null) {
            Document doc = this.findDocById(docID);
            dtr.addRelation(docID, docTypeId, docTypeRelationdao, doc.getStatus());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#getDocCountByDocType(java.lang.String)
     */
    public Integer getDocCountByDocConditionDocTypeActiveAndNotPublished(DocumentVo docVo) {
        Integer temp = dao.getDocCountByDocConditionDocType(docVo,
                DOCTYPE_DOC_ACTIVE, false);
        return temp;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#queryAllDocByDocConditionDocTypeActiveAndNotPublished(int, int,
            String, DocumentVo,String,boolean)
     */

    public List<Document> queryAllDocByDocConditionDocTypeActiveAndNotPublished(int currentPage, int pageSize,
                                                                                String orderStr, DocumentVo docVo) {
        List<Document> result = dao.getDocsByDocConditionDocType(currentPage,
                pageSize, orderStr, docVo, DOCTYPE_DOC_ACTIVE, false);
        return result;
    }

    public List<Document> queryAllDocByDocConditionDocTypeActiveAndPublished(int currentPage, int pageSize,
                                                                             String orderStr, DocumentVo docVo) {
        List<Document> result = dao.getDocsByDocConditionDocType(currentPage,
                pageSize, orderStr, docVo, DOCTYPE_DOC_ACTIVE, true);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#queryAllDocByDocConditionDocType(int, int,
            String, DocumentVo,String,boolean)
     */

    public List<Document> queryAllDocByDocConditionDocType(int currentPage, int pageSize, String orderStr,
                                                           DocumentVo docVo, String isActive, boolean getPublished) {
        List<Document> result = dao.getDocsByDocConditionDocType(currentPage,
                pageSize, orderStr, docVo, isActive, getPublished);
        return result;
    }
    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#getDocCountByDocTypeNotPublished(String,String,boolean)
     */

    public Integer getDocCountByDocTypeNotPublished(String docTypeId, String isActive) {
        return dao.getDocCountByDocType(docTypeId, isActive, false);
    }
    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#queryAllDocByDocType(int, int,
            String, String,String,boolean)
     */

    public List<Document> queryAllDocByDocType(int currentPage, int pageSize,
                                               String orderStr, String docTypeId, String isActive, boolean getPublished) {
        List<Document> result = dao.getDocsByDocType(currentPage,
                pageSize, orderStr, docTypeId, isActive, getPublished);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#shareDocument(java.util.List,
     *      java.util.List)
     */
    public void shareDocument(List<String> docIds, List<String> docTypeIds) {
        for (String docID : docIds) {
            for (String docTypeId : docTypeIds) {
                this.shareDocument(docID, docTypeId);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#transferDocument(java.util.List,
     *      java.lang.String, java.lang.String)
     */
    public void transferDocument(List<String> docIds, String srcDocTypeID,
                                 String destDocTypeID) {
        for (String docId : docIds) {
            this.transferDocument(docId, srcDocTypeID, destDocTypeID);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#save(udp.ewp.document.model.Document,
     *      java.util.List)
     */
    public OID save(Document doc, List<String> docTypeIds) {
        OID oid = Helper.requestOID(DOCUMENT_OID);
        String docID = String.valueOf(oid);
        doc.setId(docID);
        dao.save(doc);
        for (String docTypeID : docTypeIds) {
            DocumentTypeRelation dtr = new DocumentTypeRelation();
            // 先保存文档，才能增加文档和文档类型关联关系记录；否则外键关联会出错
            dtr.addRelation(doc.getId(), docTypeID, docTypeRelationdao, doc.getStatus());
        }
        return oid;
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#copyDocuments(java.util.List,
     *      java.util.List)
     */
    public void copyDocuments(List<String> docIds, List<String> docTypeIds) {
        for (String docTypeID : docTypeIds) {
            for (String docId : docIds) {
                Document doc = (Document) dao.findDocById(docId);
                Document newDoc = new Document();
                try {
                    PropertyUtils.copyProperties(newDoc, doc);
                } catch (Exception e) {
                    throw new BaseApplicationException(
                            "Document propertyCopy error!!", e);
                }
                OID oid = Helper.requestOID(DOCUMENT_OID);
                String docID = String.valueOf(oid);
                newDoc.setId(docID);
                newDoc.setStatus(IConstants.DOC_STATUS_DRAFT);
                newDoc.setPublishTime(null);//设置发布时间为null
                Timestamp editTime = new Timestamp((new Date()).getTime());
                newDoc.setEditTime(editTime);
                dao.save(newDoc);
                DocumentTypeRelation dtr = new DocumentTypeRelation();
                // 先保存文档，才能增加文档和文档类型关联关系记录；否则外键关联会出错
                dtr.addRelation(newDoc.getId(), docTypeID, docTypeRelationdao, newDoc.getStatus());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#update(udp.ewp.document.model.Document,
     *      java.util.List, java.util.List)
     */
    public void update(Document doc, List<String> originalDocTypeIds,
                       List<String> newDocTypeIds) {
        this.update(doc);
        String docId = doc.getId();
        List<String> deleteRels = new ArrayList<String>();
        List<String> addRels = new ArrayList<String>();
        if (!newDocTypeIds.isEmpty()) {
            for (String tempId : originalDocTypeIds) { // 获得需要删除的docTypeId
                if (!newDocTypeIds.contains(tempId)
                        && !tempId.equals(doc.getDocTypeID())) {// 不能在更新时将原始docTypeId删除
                    deleteRels.add(tempId);
                }
            }

            for (String tempId : newDocTypeIds) {// 获得需要添加的docTypeId
                if (!originalDocTypeIds.contains(tempId)) {
                    addRels.add(tempId);
                }
            }
        }
        for (String docTypeID : deleteRels) {
            DocumentTypeRelation relation = docTypeRelationdao
                    .getDocumentTypeRelation(docTypeID, docId);
            docTypeRelationdao.delete(relation);
        }
        for (String docTypeID : addRels) {
            DocumentTypeRelation dtr = new DocumentTypeRelation();
            // 先保存文档，才能增加文档和文档类型关联关系记录；否则外键关联会出错
            dtr.addRelation(doc.getId(), docTypeID, docTypeRelationdao, doc.getStatus());
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#getDocTypeIdsByDocId(java.lang.String)
     */
    public List<String> getDocTypeIdsByDocId(String docId, String isActive) {
        List<String> temp = docTypeRelationdao.getDocTypeIdsByDocId(docId,
                isActive);
        return temp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#update(udp.ewp.document.model.Document,
     *      java.lang.Boolean)
     */
    private void update(Document doc, Boolean isPublish) {
        if (isPublish) {
            List<DocumentTypeRelation> relations = docTypeRelationdao
                    .getDocTypeRelationsByDocId(doc.getId(), DOCTYPE_DOC_ACTIVE);
            for (DocumentTypeRelation relation : relations) {
                relation.setIsPublish(IConstants.DOCTYPE_DOC_PUBLISHED);
                docTypeRelationdao.update(relation);
            }
        } else {
            List<DocumentTypeRelation> relations = docTypeRelationdao
                    .getDocTypeRelationsByDocId(doc.getId(), DOCTYPE_DOC_ACTIVE);
            for (DocumentTypeRelation relation : relations) {
                relation.setIsPublish(IConstants.DOCTYPE_DOC_NOT_PUBLISHED);
                docTypeRelationdao.update(relation);
            }
        }
        this.update(doc);
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.bs.IDocumentBS#queryAllDocByDocTypeId(java.lang.String,
     *      int, int, java.lang.String)
     */
    public PageResults<Document> queryAllDocByDocTypeId(String docTypeId, int currentPage, int pageSize, String orderStr) {
        int docCount = dao.getDocCountByDocType(docTypeId, IConstants.DOCTYPE_DOC_ACTIVE, true);
        List<Document> docs = this.queryAllDocByDocType(currentPage, pageSize, orderStr, docTypeId, IConstants.DOCTYPE_DOC_ACTIVE, true);
        PageResults results = new PageResults();
        results.setCurrentPage(currentPage);
        results.setOrderBy(orderStr);
        results.setPageSize(pageSize);
        results.setTotalCount(docCount);
        results.setResults(docs);
        return results;
    }

    /* (non-Javadoc)
     * @see udp.ewp.document.bs.IDocumentBS#updatePublish(udp.ewp.document.model.Document)
     */
    public void updatePublish(Document doc, String webSiteId) {
        update(doc, true);

        //为文档建立查询索引
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        DocVo docIndex = new DocVo(doc.getId(), doc.getTitle(), HTMLTagConstants.getText(doc.getContent()), webSiteId, doc.getId(),format.format(doc.getPublishTime()));
        try {
            if(!(operation.isExistIndex(docIndex))){
                operation.insertIndex(docIndex);
            }
        } catch (IOException e) {
            logger.error("document index create error!", e);
        }

    }

    public void insertDocIndexAllByWebSiteIds(String ids){
        String[] websiteIds = ids.split(",");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        for(String websiteId:websiteIds){
            List<Document> docList = dao.queryAllDocByWebSiteIdAndIsPublish("",websiteId,true);
            logger.info("website:"+websiteId+" document index create start!");
            for(Document doc:docList){
                //为文档建立查询索引
                DocVo docIndex = new DocVo(doc.getId(), doc.getTitle(), HTMLTagConstants.getText(doc.getContent()), websiteId, doc.getId(),format.format(doc.getPublishTime()));
                try {
                    if(!(operation.isExistIndex(docIndex))){
                        operation.insertIndex(docIndex);
                    }
                } catch (IOException e) {
                    logger.error("document index create error!", e);
                    logger.error("document index create error doc id:"+doc.getId());
                    logger.error("document index create error doc title:"+doc.getTitle());
                }

            }
            logger.info("website:"+websiteId+" document index create end!");
        }
    }

    /* (non-Javadoc)
     * @see udp.ewp.document.bs.IDocumentBS#updateSubmit(udp.ewp.document.model.Document)
     */
    public void updateSubmit(Document doc) {
        update(doc, false);
    }

    /* (non-Javadoc)
     * @see udp.ewp.document.bs.IDocumentBS#canclePublish(udp.ewp.document.model.Document)
     */
    public void canclePublish(Document doc, String webSiteId) {
        List<DocumentTypeRelation> relations = docTypeRelationdao.getDocTypeRelationsByDocId(doc.getId(), DOCTYPE_DOC_ACTIVE);
        for (DocumentTypeRelation relation : relations) {
            relation.setIsPublish(IConstants.DOCTYPE_DOC_NOT_PUBLISHED);
            docTypeRelationdao.update(relation);
        }
        this.update(doc);

        //删除文档查询索引
        try {
            operation.deleteIndex(doc.getId());
        } catch (IOException e) {
            logger.error("document index create error!", e);
        } catch (Exception e) {
            logger.error("document index not exists!", e);
        }

    }

    public String getDocTemplateName(String siteCode, String docTypeId) {
        String viewName = "";
        String docTypeCode = dataCache.getDocTypeCodeById(docTypeId);
        EwpTemplate docTpl = dataCache.getTemplateData(siteCode, docTypeCode, ITemplateConstants.DOCUMENT_TEMPLATE);

        if (docTpl != null) {
            viewName = docTpl.getTemplate_name();
        }
        return viewName;
    }

    public boolean isShowHotWords(Document doc, String siteCode) {
        List<Website> sites = dataCache.getWebsitesData();
        for (Website site : sites) {
            if (site.getWebsiteCode().equals(siteCode)) {
                if (site.getHotWordsSwitcher().equals(IConstants.LOGIC_TRUE) ||
                    doc.getIsShowHotWords().equals(IConstants.LOGIC_TRUE)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void renderHotWords(Document doc, String siteCode) {
        HashMap<String, String> hotMap = dataCache.getHotWords();
        List<Pattern> patternList = dataCache.getHotWordsPattern();
        List<Website> sites = dataCache.getWebsitesData();
        String target = "";
        for (Website site : sites) {
            if (site.getWebsiteCode().equals(siteCode)) {
                target = site.getLinkTarget();
            }
        }

        Matcher matcher;
        String newContent = doc.getContent();
        for (Pattern pattern : patternList) {
            StringBuffer sb = new StringBuffer();
            matcher = pattern.matcher(newContent);
            while (matcher.find()) {
                String word = matcher.group();
                String hotWordsLink = "<a href='" + hotMap.get(word) + "' target='" + target + "' class='hot-link'>" + word + "</a>";
                matcher.appendReplacement(sb, hotWordsLink);
            }
            matcher.appendTail(sb);
            newContent = sb.toString();
        }
        doc.setContent(newContent);
    }
}
