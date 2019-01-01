/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.document.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import venus.frames.base.dao.BaseHibernateDao;
import venus.portal.document.dao.IDocumentDao;
import venus.portal.document.model.Document;
import venus.portal.document.util.IConstants;
import venus.portal.document.vo.DocumentVo;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author yangchangming
 * 
 */
public class DocumentDao extends BaseHibernateDao implements IDocumentDao, IConstants {

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.dao.IDocumentDao#findDocById(java.lang.String)
     */
    public Object findDocById(String docID) {
        return super.get(Document.class, docID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.dao.IDocumentDao#queryAllDoc(int, int,
     *      java.lang.String, udp.ewp.doctype.vo.DocTypeTreeVo)
     */
    public List queryAllDoc(int currentPage, int pageSize, String orderStr,
            DocumentVo docVo) {
        List documentList =  null;
        StringBuffer hql = new StringBuffer("from doc in " + Document.class
                + " where doc.isValid='" + LOGIC_TRUE + "'");

        if (docVo != null) {
            // 文档标题
            if (docVo.getTitle() != null && !"".equals(docVo.getTitle())) {
                hql.append(" and doc.title like '%" + docVo.getTitle() + "%'");
            }

            // 文档作者
            if (docVo.getCreateBy() != null && !"".equals(docVo.getCreateBy())) {
                hql.append(" and doc.createBy like '%" + docVo.getCreateBy()
                        + "%'");
            }

            // 文档状态
            if (docVo.getStatus() != null && !"".equals(docVo.getStatus())) {
                hql.append(" and doc.status = '" + docVo.getStatus() + "'");
            }

            // 文档标签
            if (docVo.getTag() != null && !"".equals(docVo.getTag())) {
                hql.append(" and doc.tag like '%" + docVo.getTag() + "%'");
            }
            if (docVo.getSeoKeyWord() != null
                    && !"".equals(docVo.getSeoKeyWord())) {
                hql.append(" and doc.seoKeyWord like '%"
                        + docVo.getSeoKeyWord() + "%' ");
            }
            if (docVo.getDocTypeID() != null && !"".equals(docVo.getDocTypeID())) {
                hql.append(" and doc.docTypeID = '" + docVo.getDocTypeID() + "'");
            }
        }
        if (orderStr != null && !"".equals(orderStr)) {
            hql.append(" order by " + orderStr);
        }
        documentList = super.find(hql.toString(), (currentPage - 1) * pageSize,
                pageSize);

        return documentList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.dao.IDocumentDao#queryAllDocByWebSiteId(
     *      java.lang.String, java.lang.String)
     */
    public List queryAllDocByWebSiteId(String orderStr,String webSiteId) {
        List documentList =  null;

        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT  {doc.*} FROM ewp_document doc LEFT JOIN ewp_document_type_relation dtr ON doc.ID=dtr.DOCUMENTID LEFT JOIN ewp_doctype dt ON dtr.DOCTYPEID=dt.ID LEFT JOIN ewp_website ws ON dt.siteid=ws.ID");
        sql.append(" where doc.isValid='" + LOGIC_TRUE + "'");
        //网站
        if (webSiteId != null && !"".equals(webSiteId)) {
            sql.append(" and ws.id = '" + webSiteId + "' ");
        }

        if (orderStr != null && !"".equals(orderStr)) {
            sql.append(" order by " + orderStr);
        }

        //this.getSession().clear();
        Query query = this.getSession().createSQLQuery(sql.toString()).addEntity("doc",Document.class);
        try {
            documentList = query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return documentList;
    }


    public List queryAllDocByWebSiteIdAndIsPublish(String orderStr,String webSiteId,boolean getPublished) {
        List documentList =  null;

        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT  {doc.*} FROM ewp_document doc LEFT JOIN ewp_document_type_relation dtr ON doc.ID=dtr.DOCUMENTID LEFT JOIN ewp_doctype dt ON dtr.DOCTYPEID=dt.ID LEFT JOIN ewp_website ws ON dt.siteid=ws.ID");
        sql.append(" where doc.isValid='" + LOGIC_TRUE + "'");
        //网站
        if (webSiteId != null && !"".equals(webSiteId)) {
            sql.append(" and ws.id = '" + webSiteId + "' ");
        }

        if(getPublished){
            sql.append("and doc.STATUS='"+ IConstants.DOCTYPE_DOC_PUBLISHED+"' ");
        }

        if (orderStr != null && !"".equals(orderStr)) {
            sql.append(" order by " + orderStr);
        }

        //this.getSession().clear();
        Query query = this.getSession().createSQLQuery(sql.toString()).addEntity("doc",Document.class);
        try {
            documentList = query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return documentList;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.dao.IDocumentDao#queryAllDocByWebSiteId(int, int,
     *      java.lang.String, udp.ewp.doctype.vo.DocTypeTreeVo, java.lang.String)
     */
    public List queryAllDocByWebSiteId(int currentPage, int pageSize, String orderStr,
                                       DocumentVo docVo, String webSiteId) {
        List documentList =  null;

        StringBuffer sql=new StringBuffer("SELECT  {doc.*} ");
        String sqlrs = sqlWebSiteByDocVoAndWebSiteIdAndOrderStr(docVo,webSiteId,orderStr,sql).toString();

        this.getSession().clear();  
        Query query = this.getSession().createSQLQuery(sqlrs).addEntity("doc", Document.class);
        int start=((currentPage-1)<0)?0:(currentPage-1)*pageSize;
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        try {
            documentList = query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        
        return documentList;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.dao.IDocumentDao#queryAllDocRecordCountByWebSiteId(
     *       udp.ewp.doctype.vo.DocTypeTreeVo, java.lang.String)
     */
    public int queryAllDocRecordCountByWebSiteId(DocumentVo docVo, String webSiteId) {
        int recordCount = 0;
        List rsList=null;

        StringBuffer sql=new StringBuffer("SELECT  count(doc.id)");
        String sqlrs = sqlWebSiteByDocVoAndWebSiteIdAndOrderStr(docVo,webSiteId,null,sql).toString();

        this.getSession().clear();
        Query query;
        query = this.getSession().createSQLQuery(sqlrs);

        try {
            rsList = query.list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        if (rsList!=null&&rsList.size()>0){
            Object temp = rsList.get(0);
            if(temp!=null){
                if (temp instanceof java.math.BigInteger) { // for mysql
                    recordCount=((java.math.BigInteger)temp).intValue();
                } else if (temp instanceof java.math.BigDecimal) {  // for oracle
                    recordCount=((java.math.BigDecimal)temp).intValue();
                }else{
                    recordCount=Integer.parseInt(temp.toString());
                }
            }
        }

        return recordCount;
    }

    /**
     * @param docVo
     * @param webSiteId
     * @param orderStr
     */        

    private StringBuffer sqlWebSiteByDocVoAndWebSiteIdAndOrderStr(DocumentVo docVo, String webSiteId, String orderStr, StringBuffer sql) {
        String alias="doc";
        sql.append(" FROM ewp_document doc LEFT JOIN ewp_document_type_relation dtr ON doc.ID=dtr.DOCUMENTID LEFT JOIN ewp_doctype dt ON dtr.DOCTYPEID=dt.ID LEFT JOIN ewp_website ws ON dt.siteid=ws.ID");
        sql.append(" where doc.isValid='" + LOGIC_TRUE + "'");

        sql.append(sqlWebSiteBydocVo(docVo));
        //网站
        if (webSiteId != null && !"".equals(webSiteId)) {
            sql.append(" and ws.id = '" + webSiteId + "' ");
        }

        //排序
        if (orderStr != null && !"".equals(orderStr)) {
            sql.append(" order by " + orderStr);
        }
        return sql;
    }


    /**利用条件docVo组成条件语句
     * @param docVo
     */

    private StringBuffer sqlWebSiteBydocVo(DocumentVo docVo) {
        String alias="doc";
        StringBuffer condition = new  StringBuffer();
        if (docVo != null) {
            // 文档标题
            if (docVo.getTitle() != null && !"".equals(docVo.getTitle())) {
                condition.append(" and "+alias+".title like '%" + docVo.getTitle() + "%'");
            }

            // 文档作者
            if (docVo.getCreateBy() != null && !"".equals(docVo.getCreateBy())) {
                condition.append(" and "+alias+".createBy like '%" + docVo.getCreateBy()
                        + "%'");
            }

            // 文档状态
            if (docVo.getStatus() != null && !"".equals(docVo.getStatus())) {
                condition.append(" and "+alias+".status = '" + docVo.getStatus() + "'");
            }

            // 文档标签
            if (docVo.getTag() != null && !"".equals(docVo.getTag())) {
                condition.append(" and "+alias+".tag like '%" + docVo.getTag() + "%'");
            }
            if (docVo.getSeoKeyWord() != null
                    && !"".equals(docVo.getSeoKeyWord())) {
                condition.append(" and "+alias+".seoKeyWord like '%"
                        + docVo.getSeoKeyWord() + "%' ");
            }

        }

        return condition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.dao.IDocumentDao#queryDocRecordCountByCondition(udp.ewp.doctype.vo.DocTypeTreeVo)
     */
    public int queryDocRecordCountByCondition(DocumentVo docVo) {
        int recordCount = 0;
        StringBuffer hql = new StringBuffer("select count(doc.id) from doc in "
                + Document.class + " where doc.isValid='" + LOGIC_TRUE + "'");
        if (docVo != null) {
            // 文档标题
            if (docVo.getTitle() != null && !"".equals(docVo.getTitle())) {
                hql.append(" and doc.title like '%" + docVo.getTitle() + "%'");
            }

            // 文档作者
            if (docVo.getCreateBy() != null && !"".equals(docVo.getCreateBy())) {
                hql.append(" and doc.createBy like '%" + docVo.getCreateBy()
                        + "%'");
            }

            // 文档状态
            if (docVo.getStatus() != null && !"".equals(docVo.getStatus())) {
                hql.append(" and doc.status = '" + docVo.getStatus() + "'");
            }

            // 文档标签
            if (docVo.getTag() != null && !"".equals(docVo.getTag())) {
                hql.append(" and doc.tag like '%" + docVo.getTag() + "%'");
            }
            if (docVo.getSeoKeyWord() != null
                    && !"".equals(docVo.getSeoKeyWord())) {
                hql.append(" and doc.seoKeyWord like '%"
                        + docVo.getSeoKeyWord() + "%' ");
            }
            if (docVo.getDocTypeID() != null && !"".equals(docVo.getDocTypeID())) {
                hql.append(" and doc.docTypeID = '" + docVo.getDocTypeID() + "'");
            }
        }
        recordCount = ((Long) (super.find(hql.toString()).get(0))).intValue();
        return recordCount;
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.dao.IDocumentDao#saveDocument(udp.ewp.document.model.Document)
     */
    public void save(Document doc) {
        super.save(doc);
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.dao.IDocumentDao#delete(udp.ewp.document.model.Document)
     */
    public int delete(Document doc) {
        super.delete(doc);
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see udp.ewp.document.dao.IDocumentDao#update(udp.ewp.document.model.Document)
     */
    public void update(Document doc) {
        super.update(doc);
    }

    /* (non-Javadoc)
     * @see udp.ewp.documenttyperelation.dao.IDocumentTypeRelationDao#getDocIdsByDocType(java.lang.String)
     */
    public List<Document> getDocsByDocType(int currentPage, int pageSize, String orderStr,
                                           String docTypeId, String isActive, boolean getPublished) {
        List<Document> documentList =  null;
        StringBuffer queryString = new StringBuffer(
                "from udp.ewp.document.model.Document doc  where doc.isValid='"
                        + LOGIC_TRUE + "'");
        queryString.append(" and exists ( ");
        if(getPublished){
            queryString.append("select docTypeRel.documentID from udp.ewp.documenttyperelation.model.DocumentTypeRelation   docTypeRel " );
            queryString.append("where  docTypeRel.isActive='"+isActive+"' and   docTypeRel.docTypeID='"+docTypeId+"' and docTypeRel.isPublish='"+IConstants.DOCTYPE_DOC_PUBLISHED+"' ");
        }else{
            queryString.append("select docTypeRel.documentID from udp.ewp.documenttyperelation.model.DocumentTypeRelation   docTypeRel ");
            queryString.append("where  docTypeRel.isActive= '"+isActive+"' and   docTypeRel.docTypeID='"+docTypeId+"'  ");
        }
        queryString.append(" and docTypeRel.documentID=doc.id ");
        queryString.append(" ) ");
       
        if (orderStr != null && !"".equals(orderStr)) {
            queryString.append(" order by " + orderStr);
        }else{ //如果没有排序字段，则默认按创建时间排序
            queryString.append(" order by doc.createTime desc");
        }
        documentList = super.find(queryString.toString(), (currentPage - 1)
                * pageSize, pageSize);
        return documentList;
    }


    /* (non-Javadoc)
     * @see udp.ewp.documenttyperelation.dao.IDocumentTypeRelationDao#getDocCountByDocType(java.lang.String)
     */
    public Integer getDocCountByDocType(String docTypeId,String isActive,boolean getPublished) {
        Integer result = 0;
        StringBuffer queryString =null;
        Object[] values = null;
        if(getPublished){
            queryString = new StringBuffer("select count(docTypeRel.documentID) from udp.ewp.documenttyperelation.model.DocumentTypeRelation   docTypeRel where  docTypeRel.isActive=?  and docTypeRel.docTypeID=? and docTypeRel.isPublish=?");
            values=new Object[]{isActive,docTypeId,IConstants.DOCTYPE_DOC_PUBLISHED};
        }else{
           queryString = new StringBuffer("select count(docTypeRel.documentID) from udp.ewp.documenttyperelation.model.DocumentTypeRelation   docTypeRel where  docTypeRel.isActive=?  and docTypeRel.docTypeID=? ");
            values=new Object[]{isActive,docTypeId};
        }
        List temp =this.find(queryString.toString(), values);
        if(temp != null&& temp.size()>0){
          result =((Long)  temp.get(0)).intValue();
        }
        return result;
    }
    
    public List<Document> getDocsByDocConditionDocType(int currentPage, int pageSize, String orderStr,
                                                       DocumentVo docVo, String isActive, boolean getPublished) {
        List<Document> documentList =  null;
        String docTypeId=docVo.getDocTypeID();
        StringBuffer queryString = new StringBuffer(
                "from udp.ewp.document.model.Document doc  where doc.isValid='"
                        + LOGIC_TRUE + "'");
        queryString.append(" and exists ( ");
        if(getPublished){
            queryString.append("select docTypeRel.documentID from udp.ewp.documenttyperelation.model.DocumentTypeRelation   docTypeRel " );
            queryString.append("where  docTypeRel.isActive='"+isActive+"' and   docTypeRel.docTypeID='"+docTypeId+"' and docTypeRel.isPublish='"+IConstants.DOCTYPE_DOC_PUBLISHED+"' ");
        }else{
            queryString.append("select docTypeRel.documentID from udp.ewp.documenttyperelation.model.DocumentTypeRelation   docTypeRel ");
            queryString.append("where  docTypeRel.isActive= '"+isActive+"' and   docTypeRel.docTypeID='"+docTypeId+"'  ");
        }
        queryString.append(" and docTypeRel.documentID=doc.id ");
        queryString.append(" ) ");
        queryString.append(sqlWebSiteBydocVo(docVo));
       
        if (orderStr != null && !"".equals(orderStr)) {
            queryString.append(" order by " + orderStr);
        }else{ //如果没有排序字段，则默认按创建时间排序
            queryString.append(" order by doc.createTime desc");
        }
        documentList = super.find(queryString.toString(), (currentPage - 1)
                * pageSize, pageSize);
        return documentList;
    }
    
    public Integer getDocCountByDocConditionDocType(DocumentVo docVo, String isActive, boolean getPublished){
        Integer result = 0;
        String docTypeId=docVo.getDocTypeID();
        StringBuffer queryString = new StringBuffer(
                "select count(doc.id) from udp.ewp.document.model.Document doc  where doc.isValid='"
                        + LOGIC_TRUE + "'");
        queryString.append(" and exists ( ");
        if(getPublished){
            queryString.append("select docTypeRel.documentID from udp.ewp.documenttyperelation.model.DocumentTypeRelation   docTypeRel " );
            queryString.append("where  docTypeRel.isActive='"+isActive+"' and   docTypeRel.docTypeID='"+docTypeId+"' and docTypeRel.isPublish='"+IConstants.DOCTYPE_DOC_PUBLISHED+"' ");
        }else{
            queryString.append("select docTypeRel.documentID from udp.ewp.documenttyperelation.model.DocumentTypeRelation   docTypeRel ");
            queryString.append("where  docTypeRel.isActive= '"+isActive+"' and   docTypeRel.docTypeID='"+docTypeId+"'  ");
        }
        queryString.append(" and docTypeRel.documentID=doc.id ");
        queryString.append(" ) ");
        queryString.append(sqlWebSiteBydocVo(docVo));
        
        List temp =this.find(queryString.toString());
        if(temp != null&& temp.size()>0){
          result =((Long)  temp.get(0)).intValue();
        }
        return result;
    }

    public List<Document> getReleaseDocByDate(Timestamp date) {
        String hql = "from udp.ewp.document.model.Document doc " +
                "where exists(select tr.docId from EwpDocumentTimeRelease tr " +
                "where tr.docId=doc.id and tr.docPreReleaseTime<?)";
        return super.find(hql, date);
    }
    
}
