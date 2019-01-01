package venus.portal.doctype.bs;

import net.sf.json.JSONObject;
import venus.frames.base.exception.BaseApplicationException;
import venus.portal.doctype.model.DocType;
import venus.portal.doctype.vo.DocTypeTreeCacheVo;
import venus.portal.doctype.vo.DocTypeTreeVo;
import venus.portal.doctype.vo.DocTypeVo;
import venus.portal.template.model.EwpTemplate;
import venus.pub.lang.OID;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 栏目管理各种操作的声明
 *
 * @author yangchangming
 */
public interface IDocTypeBS {

    /**
     * 展现root下的所有节点树，不包括排除的节点条件
     *
     * @param root   根节点ID
     * @param siteId 此栏目所属站点
     * @return
     * @throws BaseApplicationException
     */
    public List queryAllNode(String root, String siteId) throws BaseApplicationException;

    /**
     * 展现root下的所有节点树，包括排除的节点条件
     *
     * @param root
     * @param notInclude
     * @param siteId     所属的站点ID
     * @return List
     * @throws BaseApplicationException
     */
    public List queryAllNode(String root, HashSet notIncludeSet, String siteId) throws BaseApplicationException;

    /**
     * 展现root下的所有节点树(有可见和可操作的权限信息)，包括排除的节点条件
     *
     * @param root
     * @param notIncludeSet
     * @param site_id
     * @param partyIdUsers
     * @param partyIdRoles
     * @return
     * @throws BaseApplicationException
     */
    public List queryAllAuNode(HashSet notIncludeSet, String site_id, HttpServletRequest request) throws BaseApplicationException;

    /**
     * 获取栏目对象
     *
     * @param nodeID 栏目ID,必定是一个存在的对象,否则出错
     * @return 栏目对象
     * @throws BaseApplicationException
     */
    public DocType findDocTypeById(String nodeID) throws BaseApplicationException;

    /**
     * 查询parentid下的所有有权限的子节点
     *
     * @param parentId
     * @param request
     * @return
     */
    public List querySubAuNode(String parentId, String siteId, HttpServletRequest request);

    /**
     * 分页，条件查询指定栏目下的所有文档
     *
     * @param currentPage
     * @param pageSize
     * @param orderStr
     * @param dtto        栏目栏目 对象
     * @return
     * @throws BaseApplicationException
     */
    public List queryAllDocByCondition(int currentPage, int pageSize, String orderStr, DocTypeTreeVo dtto) throws BaseApplicationException;

    /**
     * 查询指定栏目下的所有文档记录个数
     *
     * @param dtto
     * @return
     * @throws BaseApplicationException
     */
    public int queryDocRecordCountByCondition(DocTypeTreeVo dtto) throws BaseApplicationException;

    /**
     * 更新栏目(只更新基础字段信息,不修栏目之间的关联记录)
     *
     * @param docType
     * @return
     * @throws BaseApplicationException
     */
    public boolean update(DocType docType) throws BaseApplicationException;

    /**
     * 保存
     *
     * @param docType
     * @return
     * @throws BaseApplicationException
     */
    public OID save(DocType docType) throws BaseApplicationException;

    /**
     * 删除
     *
     * @param docTypeID
     * @return
     * @throws BaseApplicationException
     */
    public void delete(DocType docType) throws BaseApplicationException;

    /**
     * 检测某一网站下是否有名称相同的栏目。
     *
     * @param webSiteId
     * @return
     */
    public boolean checkDocTypeNameIsUniqueByWebSite(String webSiteId);

    /**
     * 栏目下的文档数量
     *
     * @param dao
     * @return
     */
    public int getDocumentCountByDocTypeId(String docTypeID);

    /**
     * 增加栏目
     *
     */
    public DocType addTreeNode(String newDoctType);

    /**
     * 更新栏目对象
     *
     * @param newDocType 节点对象
     * @return 是否成功
     */
    public String updateTreeNode(DocType newDocType);

    /**
     * 移动栏目对象
     *
     * @param data   节点对象属性map
     * @param beanID
     * @return 是否成功
     */
    public String moveTreeNode(JSONObject jsonParam);

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
     * 根据栏目主键查询栏目,查不到返回NULL
     *
     * @param
     * @return void
     * @throws
     * @author zhangrenyang
     * @date 2011-9-16上午06:46:43
     */
    public DocType queryDocTypesById(final String docTypeId);

    /**
     * 更新栏目图片
     *
     * @param docType
     * @return
     * @throws BaseApplicationException
     */
    public boolean updateImagePath(String id, String imagePath) throws BaseApplicationException;

    /**
     * 根据栏目code来获取栏目信息
     *
     * @param code
     * @return
     */
    public DocType getDocTypeByCode(String code, String siteId);

    /**
     * 根据栏目名称来获取栏目信息
     *
     * @param code
     * @return
     */
    public DocType getDocTypeByName(String name, String siteId);

    /**
     * 根据栏目ID获取该栏目挂接的栏目模板
     *
     * @param docTypeId 栏目ID
     * @return 栏目模板model
     */
    public EwpTemplate getTplById(String docTypeId);

    /**
     * 根据栏目ID获取该栏目挂接的文档模板
     *
     * @param docTypeId 栏目ID
     * @return 文档模板model
     */
    public EwpTemplate getDocTplById(String docTypeId);

    /**
     * 查询所有栏目，用于缓存
     *
     * @return 栏目Map, key为“栏目code+站点code”
     */
    public HashMap<String, DocTypeVo> queryAllVo();

    /**
     * 根据站点code和栏目code获取栏目模板名称
     *
     * @param siteCode    站点code
     * @param docTypeCode 栏目code
     * @return 栏目模板名称
     */
    public String getTemplateName(String siteCode, String docTypeCode);

    /**
     * 根据站点code和栏目code获取栏目Vo
     *
     * @param siteCode    站点code
     * @param docTypeCode 栏目code
     * @return 栏目Vo
     */
    public DocTypeVo getDocTypeFromCache(String siteCode, String docTypeCode);

    /**
     * 查询所有栏目的根节点
     *
     * @return
     */
    public List<DocType> queryAllRootNode(HttpServletRequest request);

    /**
     * 按照站点id，将栏目树存入map中
     *
     * @return
     */
    public DocTypeTreeCacheVo getDocTypeTreeMap(HashMap<String, DocTypeVo> docTypeMap);

    /**
     * 获取当前栏目的父节点路径
     *
     * @param docType
     * @return
     */
    public String getParentPath(DocType docType);

    /**
     * 检查用户是否有栏目权限
     *
     * @param request
     * @return
     */
    public Boolean checkAuthority(HttpServletRequest request);
    
    public String getDocTypeCodeById(String docTypeId);
}
