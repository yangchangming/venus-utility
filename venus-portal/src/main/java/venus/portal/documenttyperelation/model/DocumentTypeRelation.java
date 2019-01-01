/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.documenttyperelation.model;

import venus.frames.mainframe.util.Helper;
import venus.portal.documenttyperelation.dao.IDocumentTypeRelationDao;
import venus.pub.lang.OID;

/**
 * 文档与文档类型关系(领域模型对象)
 * @author yangchangming
 */
public class DocumentTypeRelation {

    private String id;
    private String documentID;
    private String docTypeID;
    private String  isActive="1"; //此文档与栏目的关系是否可用
    private String isPublish = "0" ;//次文档与栏目的关系，标识文档状态是否为已发布，默认为0
    
    /**
     * 新增关系记录
     * @param documentID
     * @param docTypeID
     * @param dao
     */
    public void addRelation(String documentID, String docTypeID, IDocumentTypeRelationDao dao, String isPublish){
        OID oid = Helper.requestOID("ewp_document_type_relation");
        this.setId(String.valueOf(oid));
        this.setDocumentID(documentID);
        this.setDocTypeID(docTypeID);
        this.setIsPublish(isPublish);
        dao.save(this);
    }
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the documentID
     */
    public String getDocumentID() {
        return documentID;
    }

    /**
     * @param documentID the documentID to set
     */
    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    /**
     * @return the docTypeID
     */
    public String getDocTypeID() {
        return docTypeID;
    }

    /**
     * @param docTypeID the docTypeID to set
     */
    public void setDocTypeID(String docTypeID) {
        this.docTypeID = docTypeID;
    }


    /**
     * @return the isActive
     */
    public String getIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the isPublish
     */
    public String getIsPublish() {
        return isPublish;
    }

    /**
     * @param isPublish the isPublish to set
     */
    public void setIsPublish(String isPublish) {
        this.isPublish = isPublish;
    }
}
