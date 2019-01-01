/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.document.dao;

import org.springframework.dao.DataAccessException;
import venus.portal.document.model.Document;
import venus.portal.document.vo.DocumentVo;

import java.sql.Timestamp;
import java.util.List;

/**
 * 文档的持久化操作声明
 *
 * @author yangchangming
 */
public interface IDocumentDao {


    /**
     * 删除
     *
     * @param doc
     * @throws BaseDataAccessException
     */
    public int delete(Document doc);

    /**
     * 更新
     *
     * @param doc
     * @throws BaseDataAccessException
     */
    public void update(Document doc);

    /**
     * 新增
     *
     * @param doc
     * @throws BaseDataAccessException
     */
    public void save(Document doc);

    /**
     * 查询指定网站和文档条件下所有的文档
     *
     * @param orderStr
     * @param webSiteId
     * @return
     */
    public List queryAllDocByWebSiteId(String orderStr, String webSiteId);

    /**
     * 分页，条件 查询指定网站和文档条件下所有的文档
     *
     * @param currentPage
     * @param pageSize
     * @param orderStr
     * @param docVo
     * @param webSiteId
     * @return
     */
    public List queryAllDocByWebSiteId(int currentPage, int pageSize, String orderStr, DocumentVo docVo, String webSiteId);

    /**
     * 查询指定网站和文档条件下所有的文档记录数
     *
     * @param docVo
     * @param webSiteId
     * @return
     */
    public int queryAllDocRecordCountByWebSiteId(DocumentVo docVo, String webSiteId);

    /**
     * 分页，条件 查询指定文档类型下所有的文档
     *
     * @param currentPage
     * @param pageSize
     * @param orderStr
     * @param dtto
     * @return
     * @throws BaseDataAccessException
     */
    public List queryAllDoc(int currentPage, int pageSize, String orderStr, DocumentVo docVo);


    /**
     * 查询指定文档类型下所有文档记录个数
     *
     * @param dtto
     * @return
     * @throws BaseDataAccessException
     */
    public int queryDocRecordCountByCondition(DocumentVo docVo);

    /**
     * 根据文档ID，查找文档对象
     *
     * @param docID
     * @return
     * @throws BaseDataAccessException
     */
    public Object findDocById(String docID);

    /**
     * 根据给出的栏目ID分页查找文章
     *
     * @param currentPage
     * @param pageSize
     * @param orderStr
     * @param docTypeId
     * @param isActive
     * @param getPublished
     * @return
     */
    public List<Document> getDocsByDocType(int currentPage, int pageSize, String orderStr,
                                           String docTypeId, String isActive, boolean getPublished);

    /**
     * 根据文档类型ID 获取此文档类型下关联的所有文章的数目
     *
     * @param docTypeId
     * @param getPublished 是否获取已发布的
     * @return
     * @throws DataAccessException
     */
    public Integer getDocCountByDocType(String docTypeId, String isActive, boolean getPublished);

    /**
     * 根据给出的栏目ID分页查找文章
     *
     * @param currentPage
     * @param pageSize
     * @param orderStr
     * @param docVo
     * @param isActive
     * @param getPublished
     * @return
     */
    public List<Document> getDocsByDocConditionDocType(int currentPage, int pageSize, String orderStr,
                                                       DocumentVo docVo, String isActive, boolean getPublished);
    public List queryAllDocByWebSiteIdAndIsPublish(String orderStr, String webSiteId, boolean getPublished);
    /**
     * 根据文档类型ID 获取此文档类型下关联的所有文章的数目
     *
     * @param docVo
     * @param getPublished 是否获取已发布的
     * @return
     * @throws DataAccessException
     */
    public Integer getDocCountByDocConditionDocType(DocumentVo docVo, String isActive, boolean getPublished);

    /**
     * 根据传入的日期，获取符合发布条件的文档
     * @param date
     * @return
     */
    public List<Document> getReleaseDocByDate(Timestamp date);
}
