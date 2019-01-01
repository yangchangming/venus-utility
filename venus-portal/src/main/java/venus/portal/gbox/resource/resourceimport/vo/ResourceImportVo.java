package venus.portal.gbox.resource.resourceimport.vo;

import venus.frames.base.vo.BaseValueObject;

public class ResourceImportVo extends BaseValueObject {
    
    private String id;
    
    private String classificationId;
    
    private String resourcePath;
    
    private String isScan;
    
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
     * @return the classificationId
     */
    public String getClassificationId() {
        return classificationId;
    }
    /**
     * @param classificationId the classificationId to set
     */
    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }
    /**
     * @return the resourcePath
     */
    public String getResourcePath() {
        return resourcePath;
    }
    /**
     * @param resourcePath the resourcePath to set
     */
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }
    /**
     * @return the isScan
     */
    public String getIsScan() {
        return isScan;
    }
    /**
     * @param isScan the isScan to set
     */
    public void setIsScan(String isScan) {
        this.isScan = isScan;
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
