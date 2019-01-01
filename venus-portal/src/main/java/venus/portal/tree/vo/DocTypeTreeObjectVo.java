package venus.portal.tree.vo;

import venus.frames.base.vo.BaseValueObject;

/**
 * 栏目树节点对象
 * @author chengliang
 */
public class DocTypeTreeObjectVo extends BaseValueObject {
    // 以下是zTreeNode所需属性
    private String id;
    private String name;
    private String isParent;
    private String icon;
    private String iconSkin;
    private String url;
    private String target;

    // 以下是自定义属性
    private String siteId;
    private String rootId;
    private String drag;

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

    public String getIsParent() {
        return isParent;
    }

    public void setIsParent(String isParent) {
        this.isParent = isParent;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconSkin() {
        return iconSkin;
    }

    public void setIconSkin(String iconSkin) {
        this.iconSkin = iconSkin;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getDrag() {
        return drag;
    }

    public void setDrag(String drag) {
        this.drag = drag;
    }
}
