package venus.portal.tree.vo;

import venus.frames.base.vo.BaseValueObject;

/**
 * 树型节点对象，作为基类提供给具体树型业务类VO继承
 * @author yangchangming
 */
public class TreeViewObjectVo extends BaseValueObject {
    
    public final static int ROOT = 0;
    public final static int HAS_SUB_NODE = 1;
    public final static int NO_SUB_NODE = 2;
    public final static long INIT_LEVEL = 0;
    
    /**树节点唯一标识*/
    protected String id;
    
    /** 树节点显示名称*/
    protected String name;
    
    /*** 树节点类型，暂时用作 【有无子节点*/
    protected int type;
    
    /** 树节点的显示层次，决定该节点在树中的缩进级别*/
    protected Long level;
    
    /**父节点ID**/
    protected String parentID;
    
    public TreeViewObjectVo(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }
    
    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }
}
