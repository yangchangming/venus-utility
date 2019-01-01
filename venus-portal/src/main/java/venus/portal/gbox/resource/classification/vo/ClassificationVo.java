package venus.portal.gbox.resource.classification.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

public class ClassificationVo extends BaseValueObject {
    
    private String id;  
    
    private String name;    
    
    private String selfCode;   
    
    private String parentCode; 
    
    private int depth;   
    
    private String  isLeaf; 
    
    private String orderCode;  
    
    private int clicks;
    
    private String path;
    
    private Timestamp createDate; 
    
    private Timestamp modifyDate; 
    
    private String enableStatus;   
    
    private String description;
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the selfCode
     */
    public String getSelfCode() {
        return selfCode;
    }
    /**
     * @param selfCode the selfCode to set
     */
    public void setSelfCode(String selfCode) {
        this.selfCode = selfCode;
    }
    /**
     * @return the parentCode
     */
    public String getParentCode() {
        return parentCode;
    }
    /**
     * @param parentCode the parentCode to set
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
    /**
     * @return the depth
     */
    public int getDepth() {
        return depth;
    }
    /**
     * @param depth the depth to set
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }
    /**
     * @return the isLeaf
     */
    public String getIsLeaf() {
        return isLeaf;
    }
    /**
     * @param isLeaf the isLeaf to set
     */
    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }
    /**
     * @return the orderCode
     */
    public String getOrderCode() {
        return orderCode;
    }
    /**
     * @param orderCode the orderCode to set
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
    /**
     * @return the clicks
     */
    public int getClicks() {
        return clicks;
    }
    /**
     * @param clicks the clicks to set
     */
    public void setClicks(int clicks) {
        this.clicks = clicks;
    }
    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }
    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }
    /**
     * @return the createDate
     */
    public Timestamp getCreateDate() {
        return createDate;
    }
    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
    /**
     * @return the modifyDate
     */
    public Timestamp getModifyDate() {
        return modifyDate;
    }
    /**
     * @param modifyDate the modifyDate to set
     */
    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }
    /**
     * @return the enableStatus
     */
    public String getEnableStatus() {
        return enableStatus;
    }
    /**
     * @param enableStatus the enableStatus to set
     */
    public void setEnableStatus(String enableStatus) {
        this.enableStatus = enableStatus;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
}
