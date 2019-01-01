package venus.portal.document.bs;

import venus.portal.document.model.Document;
import venus.portal.document.vo.DocumentVo;
import venus.portal.vo.PageResults;
import venus.pub.lang.OID;

import java.util.List;

/**
 * 文档操作声明接口
 *
 * @author yangchangming
 */
public interface IDocumentBS {

    /**
     * 新建文档
     *
     * @param doc 文档对象
     * @return
     */
    public OID save(Document doc);

    /**
     * 删除文档
     *
     * @param docID
     * @return
     */
    public int delete(String docID);

    /**
     * 批量删除文档
     *
     * @param docIDs
     * @return 成功删除文档个数
     */
    public int batchDelete(String docIDs);


    /**
     * 预览文档
     *
     * @param docID 文档对象ID
     */
    public void previewDocument(String docID);

    /**
     * 转移文档
     *
     * @param srcDocTypeID  源文档类型
     * @param destDocTypeID 目标文档类型
     */
    public void transferDocument(List<String> docIds, String srcDocTypeID, String destDocTypeID);

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
     * 分页，条件查询指定文档类型下的所有文档
     *
     * @param currentPage
     * @param pageSize
     * @param orderStr
     * @param dtto        文档类型树节点 对象
     * @return
     */
    public List queryAllDocByCondition(int currentPage, int pageSize, String orderStr, DocumentVo docVo);

    /**
     * 查询指定文档类型下的所有文档记录个数
     *
     * @param dtto
     * @return
     */
    public int queryDocRecordCountByCondition(DocumentVo docVo);

    /**
     * 根据文档ID，查找文档对象
     *
     * @param docID 文档ID
     * @return
     */
    public Document findDocById(String docID);

    /**
     * 共享文章至其他文档类型，实质是在文档类型与文档关系表中添加一关系数据
     *
     * @param docTypeIds
     */
    public void shareDocument(List<String> docIds, List<String> docTypeIds);

    /**
     * 根据文档类型ID 获取此文档类型下关联的所有文章的数目
     *
     * @param docVo
     * @return
     */
    public Integer getDocCountByDocConditionDocTypeActiveAndNotPublished(DocumentVo docVo);

    /**
     * 根据给出的栏目ID分页查找未发布文章
     *
     * @param currentPage
     * @param pageSize
     * @param orderStr
     * @param docVo
     * @return
     */
    public List<Document> queryAllDocByDocConditionDocTypeActiveAndNotPublished(int currentPage, int pageSize,
                                                                                String orderStr, DocumentVo docVo);

    /**
     * 根据给出的文档条件分页查找文章
     *
     * @param currentPage
     * @param pageSize
     * @param orderStr
     * @param docVo
     * @param isActive
     * @param getPublished
     * @return
     */
    public List<Document> queryAllDocByDocConditionDocType(int currentPage, int pageSize,
                                                           String orderStr, DocumentVo docVo, String isActive, boolean getPublished);

    /**
     * 根据给出的栏目ID分页查找文章总数
     *
     * @param docTypeId
     * @param isActive
     * @return
     */
    public Integer getDocCountByDocTypeNotPublished(String docTypeId, String isActive);

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
    public List<Document> queryAllDocByDocType(int currentPage, int pageSize,
                                               String orderStr, String docTypeId, String isActive, boolean getPublished);

    /**
     * 新建文档,文档属于多栏目关系
     *
     * @param doc 文档对象
     * @return
     * @
     */
    public OID save(Document doc, List<String> docTypeIds);

    /**
     * 复制文章到其他栏目
     *
     * @param docIds
     * @param docTypeIds
     */
    public void copyDocuments(List<String> docIds, List<String> docTypeIds);

    /**
     * 根据文档ID获取此文档关联的所有栏目ID
     *
     * @param docId
     * @return
     */
    public List<String> getDocTypeIdsByDocId(String docId, String isActive);

    /**
     * 更行文档内容以及关系
     *
     * @param doc
     * @param originalDocTypeIds 原有所属栏目ID
     * @param newDocTypeIds      新的所属栏目ID
     */
    public void update(Document doc, List<String> originalDocTypeIds, List<String> newDocTypeIds);


    /**
     * 根据文档类型ID获得此文档类型下所有的文档数据
     *
     * @param docTypeId   文档类型ID
     * @param currentPage 当前页
     * @param pageSize
     * @param orderStr
     * @return PageResults
     */
    public PageResults<Document> queryAllDocByDocTypeId(String docTypeId, int currentPage, int pageSize, String orderStr);

    /**
     * 编辑文档
     *
     * @param doc 文档对象
     */
    public void updateSubmit(Document doc);

    /**
     * 编辑文档
     *
     * @param doc 文档对象
     */
    public void updatePublish(Document doc, String webSiteId);
    public void insertDocIndexAllByWebSiteIds(String ids);
    /**
     * 取消发布状态
     *
     * @param doc
     */
    public void canclePublish(Document doc, String webSiteId);

    /**
     * 根据站点code和栏目id获取文档模板名称
     *
     * @param siteCode  站点code
     * @param docTypeId 栏目id
     * @return 文档模板名称
     */
    public String getDocTemplateName(String siteCode, String docTypeId);

    /**
     * 扫描全文，将匹配上的热词变成链接的形式，用于在页面上显示
     *
     * @param doc 文档内容
     * @param siteCode 文档所属站点code
     */
    public void renderHotWords(Document doc, String siteCode);

    /**
     * 判断是否要在文档中显示热词
     *
     * @param doc 文档内容
     * @param siteCode 文档所属站点code
     * @return
     */
    public boolean isShowHotWords(Document doc, String siteCode);
}
