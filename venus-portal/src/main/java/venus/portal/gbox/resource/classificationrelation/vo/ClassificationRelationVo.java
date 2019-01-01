package venus.portal.gbox.resource.classificationrelation.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

public class ClassificationRelationVo extends BaseValueObject {
    
    private String id;
    
    private String classificationId;
    
    private String resourceId;
    
    private Timestamp createDate;

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
     * @return the resourceId
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * @param resourceId the resourceId to set
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
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
    
}
