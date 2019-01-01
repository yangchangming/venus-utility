/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.documenttyperelation.dao.impl;

import venus.frames.base.dao.BaseHibernateDao;
import venus.portal.document.util.IConstants;
import venus.portal.documenttyperelation.dao.IDocumentTypeRelationDao;
import venus.portal.documenttyperelation.model.DocumentTypeRelation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yangchangming
 *
 */
public class DocumentTypeRelationDao extends BaseHibernateDao implements
        IDocumentTypeRelationDao {

    /* (non-Javadoc)
     * @see venus.frames.base.dao.BaseHibernateDao#delete(java.lang.Object)
     */
    @Override
    public void delete(Object entity) {
        super.delete(entity);
    }

    /* (non-Javadoc)
     * @see venus.frames.base.dao.BaseHibernateDao#save(java.lang.Object)
     */
    @Override
    public Serializable save(Object entity) {
        return super.save(entity);
    }

    /* (non-Javadoc)
     * @see venus.frames.base.dao.BaseHibernateDao#update(java.lang.Object)
     */
    @Override
    public void update(Object entity) {
        super.update(entity);
    }

    /* (non-Javadoc)
     * @see udp.ewp.documenttyperelation.dao.IDocumentTypeRelationDao#getDocumentTypeRelation(java.lang.String, java.lang.String)
     */
    public DocumentTypeRelation getDocumentTypeRelation(String docTypeID, String docID) {
       DocumentTypeRelation result= null;
        StringBuffer queryString =new StringBuffer("from udp.ewp.documenttyperelation.model.DocumentTypeRelation   docTypeRel where  docTypeRel.docTypeID=? and docTypeRel.documentID=?");
        Object[] values={docTypeID,docID};
        List temp =  this.find(queryString.toString(), values);
        if(temp != null&& temp.size()>0){
            result =(DocumentTypeRelation)temp.get(0);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see udp.ewp.documenttyperelation.dao.IDocumentTypeRelationDao#getDocIdsByDocType(java.lang.String)
     */
    public List<String> getDocIdsByDocType(String docTypeId,String isActive,boolean getPublished) {
        List<String> result= null;
        StringBuffer queryString =null;
        Object[] values = null;
        if(getPublished){
            queryString =new StringBuffer("select docTypeRel.documentID from udp.ewp.documenttyperelation.model.DocumentTypeRelation   docTypeRel where  docTypeRel.isActive= ? and   docTypeRel.docTypeID=? and docTypeRel.isPublish=? ");
            values=new Object[]{isActive,docTypeId, IConstants.DOCTYPE_DOC_PUBLISHED};
        }else{
             queryString =new StringBuffer("select docTypeRel.documentID from udp.ewp.documenttyperelation.model.DocumentTypeRelation   docTypeRel where  docTypeRel.isActive= ? and   docTypeRel.docTypeID=?  ");
             values=new Object[]{isActive,docTypeId};
        }
       
        result =  this.find(queryString.toString(), values);
        if(result== null){
            result = new ArrayList<String>();
        }
        return result;
    }

    /* (non-Javadoc)
     * @see udp.ewp.documenttyperelation.dao.IDocumentTypeRelationDao#getDocTypeIdsByDocId(java.lang.String)
     */
    public List<String> getDocTypeIdsByDocId(String docId,String isActive) {
        List<String> result= null;
        StringBuffer queryString =new StringBuffer("select docTypeRel.docTypeID  from udp.ewp.documenttyperelation.model.DocumentTypeRelation   docTypeRel where docTypeRel.isActive=?  and   docTypeRel.documentID=? ");
        Object[] values={isActive,docId};
        result =  this.find(queryString.toString(), values);
        if(result== null){
            result = new ArrayList<String>();
        }
        return result;
    }

    /* (non-Javadoc)
     * @see udp.ewp.documenttyperelation.dao.IDocumentTypeRelationDao#deleteDocumentTypeRelation(java.lang.String)
     */
    public void deleteDocumentTypeRelation(String docId) {
        StringBuffer deleteString = new StringBuffer("FROM udp.ewp.documenttyperelation.model.DocumentTypeRelation  docTypeRel  where  docTypeRel.documentID='");
        deleteString.append(docId).append("'");
        this.getHibernateTemplate().delete(deleteString.toString());
    }
    
    /* (non-Javadoc)
     * @see udp.ewp.documenttyperelation.dao.IDocumentTypeRelationDao#getDocTypeIdsByDocId(java.lang.String)
     */
    public List<DocumentTypeRelation> getDocTypeRelationsByDocId(String docId,String isActive) {
        List<DocumentTypeRelation> result= null;
        StringBuffer queryString =new StringBuffer(" from udp.ewp.documenttyperelation.model.DocumentTypeRelation   docTypeRel where docTypeRel.isActive=?  and   docTypeRel.documentID=? ");
        Object[] values={isActive,docId};
        result =  this.find(queryString.toString(), values);
        if(result== null){
            result = new ArrayList<DocumentTypeRelation>();
        }
        return result;
    }
}
