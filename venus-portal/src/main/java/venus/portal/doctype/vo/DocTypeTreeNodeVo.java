package venus.portal.doctype.vo;

import venus.frames.base.vo.BaseValueObject;

import java.util.HashSet;

/**
 * Created by ethan on 14-1-3.
 */
public class DocTypeTreeNodeVo extends BaseValueObject{

    private String id;
    private String parentId;

    private HashSet<String> parent;

    private int level;

    public DocTypeTreeNodeVo() {
    }

    public DocTypeTreeNodeVo(String id, String parentId) {
        this.id = id;
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public HashSet<String> getParent() {
        return parent;
    }

    public void setParent(HashSet<String> parent) {
        this.parent = parent;
    }
}
