package venus.portal.doctype.model;

import org.hibernate.collection.PersistentSortedSet;
import venus.frames.base.bo.BaseBusinessObject;
import venus.portal.doctype.dao.IDocTypeDao;
import venus.portal.doctype.util.IConstants;
import venus.portal.doctype.vo.DocTypeTreeVo;
import venus.portal.template.model.EwpTemplate;
import venus.portal.website.model.Website;

import java.util.Set;

public class DocType extends BaseBusinessObject {

    private String name; //栏目名称
    private String description; //栏目描述
    private String parentID; //上级栏目ID
    private String sharedIds; //共享至栏目ID
    private String imagePath; //栏目图片
    private Long level; //栏目级别,展示树时需要并不进行持久化
    private String isLeaf; //是否叶子结点,展示树时需要并不进行持久化
    private String isValid; //是否启用
    private Long sortNum; //排序号
    private Long version; //版本(乐观锁)
    private Set<DocType> parent; //栏目的父栏目(多对多)
    private PersistentSortedSet children; //栏目的子栏目(多对多)
    private String templateId;// 栏目模板ID，此属性字段只做数据存储在持久化时采用 template 与模板表相关联
    private EwpTemplate template;// 栏目的模板
    private String docTemplateId;// 文档模板ID
    private EwpTemplate docTemplate;// 文档模板
    private String docTypeCode;
    private Website site;
    private String site_id;
    private String whetherLocateToParentNode; //是否定位到父节点
    private String keywords;

    private String isNavigateMenu = "0";//2011-12-19新加是否是页头导航栏目,默认为0


    public boolean isLeafNode() {
        if (IConstants.CONSTANTS_TRUE.equals(this.getIsLeaf())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasSubDocType() {
        return this.children.size() > 0 ? true : false;
    }

    /**
     * 当前栏目下包括的文档数量
     *
     * @param dao 栏目DAO
     * @return int 文档数量
     */
    public int getDocumentCountByDocTypeId(IDocTypeDao dao) {
        DocTypeTreeVo dtto = new DocTypeTreeVo();
        dtto.setDocTypeID(super.getId());
        return dao.queryDocRecordCountByCondition(dtto);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public String getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public Long getSortNum() {
        return sortNum;
    }

    public void setSortNum(Long sortNum) {
        this.sortNum = sortNum;
    }

    public PersistentSortedSet getChildren() {
        return children;
    }

    public void setChildren(PersistentSortedSet children) {
        this.children = children;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Set<DocType> getParent() {
        return parent;
    }

    public void setParent(Set<DocType> parent) {
        this.parent = parent;
    }


    public String getSharedIds() {
        return sharedIds;
    }

    public void setSharedIds(String sharedIds) {
        this.sharedIds = sharedIds;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    /**
     * @return the tempate
     */
    public EwpTemplate getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(EwpTemplate template) {
        this.template = template;
    }

    /**
     * @return the docTypeCode
     */
    public String getDocTypeCode() {
        return docTypeCode;
    }

    /**
     * @param docTypeCode the docTypeCode to set
     */
    public void setDocTypeCode(String docTypeCode) {
        this.docTypeCode = docTypeCode;
    }

    /**
     * @return the templateId
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     * @param templateId the templateId to set
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     * @return the site
     */
    public Website getSite() {
        return site;
    }

    /**
     * @param site the site to set
     */
    public void setSite(Website site) {
        this.site = site;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    /**
     * @return the isNavigateMenu
     */
    public String getIsNavigateMenu() {
        return isNavigateMenu;
    }

    /**
     * @param isNavigateMenu the isNavigateMenu to set
     */
    public void setIsNavigateMenu(String isNavigateMenu) {
        this.isNavigateMenu = isNavigateMenu;
    }

    /**
     * @return the whetherLocateToParentNode
     */
    public String getWhetherLocateToParentNode() {
        return whetherLocateToParentNode;
    }

    /**
     * @param whetherLocateToParentNode the whetherLocateToParentNode to set
     */
    public void setWhetherLocateToParentNode(String whetherLocateToParentNode) {
        this.whetherLocateToParentNode = whetherLocateToParentNode;
    }

    public EwpTemplate getDocTemplate() {
        return docTemplate;
    }

    public void setDocTemplate(EwpTemplate docTemplate) {
        this.docTemplate = docTemplate;
    }

    public String getDocTemplateId() {
        return docTemplateId;
    }

    public void setDocTemplateId(String docTemplateId) {
        this.docTemplateId = docTemplateId;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
