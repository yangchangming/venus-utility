package venus.portal.doctype.vo;

import venus.portal.doctype.util.IConstants;
import venus.portal.tree.vo.TreeViewObjectVo;

/**
 * 文档类型树 VO
 */
public class DocTypeTreeVo extends TreeViewObjectVo implements IConstants {

    private String docTypeID;

    private String hasShow = CONSTANTS_FLASE;

    private String hasOperating = CONSTANTS_FLASE;

    public DocTypeTreeVo() {
    }

    /**
     * Construct
     *
     * @param docTypeID
     * @param id
     * @param name
     * @param parentID
     */
    public DocTypeTreeVo(String docTypeID, String id, String name, String parentID) {
        this.docTypeID = docTypeID;
        super.id = docTypeID;
        super.name = name;
        super.parentID = parentID;
    }

    /**
     * Construct
     *
     * @param vo
     */
    public DocTypeTreeVo(DocTypeVo vo) {
        this.docTypeID = vo.getId();
        this.id = docTypeID;
        this.name = vo.getName();
        this.parentID = vo.getParentID();
        this.setType(vo.getChildren().size() > 0 ? TreeViewObjectVo.HAS_SUB_NODE : TreeViewObjectVo.NO_SUB_NODE);
    }

    public String getDocTypeID() {
        return docTypeID;
    }

    public void setDocTypeID(String docTypeID) {
        this.docTypeID = docTypeID;
    }

    public String getHasShow() {
        return hasShow;
    }

    public void setHasShow(String hasShow) {
        this.hasShow = hasShow;
    }

    public String getHasOperating() {
        return hasOperating;
    }

    public void setHasOperating(String hasOperating) {
        this.hasOperating = hasOperating;
    }
}