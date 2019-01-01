/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.documenttyperelation.dao;

import org.springframework.dao.DataAccessException;
import venus.portal.documenttyperelation.model.DocumentTypeRelation;

import java.io.Serializable;
import java.util.List;

/**
 * 文档类型和文档关系DAO接口，此处声明对该关系的各种行为
 * @author yangchangming
 */
public interface IDocumentTypeRelationDao {

    /**
     * 保存
     * @param obj
     * @return
     */
    public Serializable save(Object obj);
    
    /**
     * 更新
     * @param obj
     */
    public void update(Object obj);
    
    /**
     * 删除
     * @param obj
     */
    public void delete(Object obj);
    
    /**
     * 根据文档类型ID 和文档ID 获取关系数据
     * @param docTypeID
     * @param docId
     * @return
     * @throws DataAccessException
     */
    public DocumentTypeRelation getDocumentTypeRelation(String docTypeID, String docId);
    
    /**
     * 根据文档类型ID获取此文档栏目下关联的所有文章ID
     * @param docTypeId
     * @param isActive此关系是否可用
     * @param getPublished 是否获取已发布的
     * @return
     * @throws DataAccessException
     */
    public List<String>  getDocIdsByDocType(String docTypeId, String isActive, boolean getPublished);
    
    /**
     * 根据文档ID获取此文档关联的所有栏目ID
     * @param docTypeId
     * @param  isActive 此关系是否可用
     * @return
     * @throws DataAccessException
     */
    public List<String>  getDocTypeIdsByDocId(String docId, String isActive);
    
    /**
     * 根据文章ID 删除与此文章有关联的栏目文章关系数据
     * @param docId
     * @throws DataAccessException
     */
    public void deleteDocumentTypeRelation(String docId);
    
    /**
     * 根据文章ID获得所有关系数据
     * @param docId
     * @param  isActive 此关系是否可用
     * @return
     */
    public List<DocumentTypeRelation> getDocTypeRelationsByDocId(String docId, String isActive);
}
