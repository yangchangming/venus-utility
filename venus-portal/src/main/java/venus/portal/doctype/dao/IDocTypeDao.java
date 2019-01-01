package venus.portal.doctype.dao;

import venus.frames.base.exception.BaseDataAccessException;
import venus.portal.doctype.model.DocType;
import venus.portal.doctype.vo.DocTypeTreeVo;
import venus.portal.doctype.vo.DocTypeVo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 文档类型dao层各种操作声明
 *
 * @author yangchangming
 */
public interface IDocTypeDao {

    /**
     * 查询root下所有子节点，包括root节点
     *
     * @param root
     * @param notInclude 排除的节点
     * @return List <DocTypeTreeVo>
     * @throws BaseDataAccessException
     */
    public List queryAllNode(final String root, final HashSet notIncludeSet, String site_id) throws BaseDataAccessException;

    /**
     * 查询所有节点（包含用户权限信息）
     *
     * @param root
     * @param au
     * @param notIncludeSet
     * @param site_id
     * @return
     * @throws BaseDataAccessException
     */
    public List queryAllAuNode(final HashSet<String> au, final HashSet notIncludeSet, final String site_id) throws BaseDataAccessException;

    /**
     * 分页，条件 查询指定文档类型下所有的文档
     *
     * @param currentPage
     * @param pageSize
     * @param orderStr
     * @param docTypeTreeVo
     * @return
     * @throws BaseDataAccessException
     */
    public List queryAllDoc(int currentPage, int pageSize, String orderStr, DocTypeTreeVo docTypeTreeVo) throws BaseDataAccessException;

    /**
     * 查询指定文档类型下所有文档记录个数
     *
     * @param docTypeTreeVo
     * @return
     * @throws BaseDataAccessException
     */
    public int queryDocRecordCountByCondition(DocTypeTreeVo docTypeTreeVo) throws BaseDataAccessException;

    /**
     * 根据id查询文档类型
     *
     * @param docTypeID
     * @return
     * @throws BaseDataAccessException
     */
    public DocType findDocTypeById(String docTypeID) throws BaseDataAccessException;

    /**
     * 更新
     *
     * @param docType
     * @return
     * @throws BaseDataAccessException
     */
    public void update(DocType docType) throws BaseDataAccessException;

    /**
     * 新增
     *
     * @param docType
     * @throws BaseDataAccessException
     */
    public void save(DocType docType) throws BaseDataAccessException;

    /**
     * 保存或者更新VO
     *
     * @param docType 栏目VO
     * @throws BaseDataAccessException
     */
    public void saveOrUpdate(DocType docType) throws BaseDataAccessException;

    /**
     * 删除
     *
     * @param docType
     * @throws BaseDataAccessException
     */
    public void delete(DocType docType) throws BaseDataAccessException;

    /**
     * 获得重复名称的记录条数。
     *
     * @param webSiteId
     * @return
     */
    public int getDocTypeNameIsUniqueNumByWebSite(String webSiteId);

    /**
     * 根据查询条件查询栏目列表
     *
     * @param
     * @return void
     * @throws
     * @author zhangrenyang
     * @date 2011-9-16上午06:46:43
     */
    public List queryDocTypesByCondition(final String condition);

    /**
     * 根据查询条件查询栏目列表
     *
     * @param
     * @return void
     * @throws
     * @author zhangrenyang
     * @date 2011-9-16上午06:46:43
     */
    public DocType queryDocTypesById(final String docTypeId);


    /**
     * 根据栏目code来获取栏目信息
     *
     * @param code
     * @return
     */
    public DocType getDocTypeByCode(String code, String siteId);

    /**
     * 根据栏目name来获取栏目信息
     *
     * @param name
     * @return
     */
    public DocType getDocTypeByName(String name, String siteId);

    /**
     * @return
     */
    public HashMap<String, DocTypeVo> queryAllVo();

    /**
     * @param siteId
     * @return
     */
    public List<DocType> queryAllBySiteId(String siteId);

    public List<DocType> queryAllRootNode();
}
