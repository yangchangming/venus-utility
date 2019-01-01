package venus.portal.tree.vo;

/**
 * 初始化树的参数VO
 * @author zhangrenyang
 */
public class TreeParamVO {
    /**节点链接*/
    private String tree_href;
    /**单击事件方法名*/
    private String tree_onclick;
    /**链接参数名*/
    private String tree_param;
    /**链接打开的目标窗口*/
    private String tree_target;
    /**排除条件*/
    private String location_query;
    /**选择类型*/
    private String checkType;
    /**默认选中值*/
    private String selectedValues;
    /**排除的值*/
    private String notIncludeValues;
    /**不可编辑*/
    private String disableItemID;
    /**站点ID*/
    private String site_id;
    /**服务器路径*/
    private String webModel;
    /**树所在层的ID*/
    private String treeid;
    /**当前栏目的ID*/
    private String currentid;

    public String getTree_href() {
        return tree_href;
    }
 
    public void setTree_href(String tree_href) {
        this.tree_href = tree_href;
    }

    public String getTree_onclick() {
        return tree_onclick;
    }

    public void setTree_onclick(String tree_onclick) {
        this.tree_onclick = tree_onclick;
    }

    public String getTree_param() {
        return tree_param;
    }

    public void setTree_param(String tree_param) {
        this.tree_param = tree_param;
    }

    public String getTree_target() {
        return tree_target;
    }

    public void setTree_target(String tree_target) {
        this.tree_target = tree_target;
    }
    
    public String getLocation_query() {
        return location_query;
    }

    public void setLocation_query(String location_query) {
        this.location_query = location_query;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getSelectedValues() {
        return selectedValues;
    }

    public void setSelectedValues(String selectedValues) {
        this.selectedValues = selectedValues;
    }

    public String getNotIncludeValues() {
        return notIncludeValues;
    }

    public void setNotIncludeValues(String notIncludeValues) {
        this.notIncludeValues = notIncludeValues;
    }
    
    public String getDisableItemID() {
        return disableItemID;
    }
    
    public void setDisableItemID(String disableItemID) {
        this.disableItemID = disableItemID;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getWebModel() {
        return webModel;
    }

    public void setWebModel(String webModel) {
        this.webModel = webModel;
    }

    /**
     * @return the treeid
     */
    public String getTreeid() {
        return treeid;
    }

    /**
     * @param treeid the treeid to set
     */
    public void setTreeid(String treeid) {
        this.treeid = treeid;
    }

    /**
     * @return the currentid
     */
    public String getCurrentid() {
        return currentid;
    }

    /**
     * @param currentid the currentid to set
     */
    public void setCurrentid(String currentid) {
        this.currentid = currentid;
    }


}
