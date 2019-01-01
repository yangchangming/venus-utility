package venus.portal.gbox.resource.resourcetype.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

public class ResourceTypeVo extends BaseValueObject {

	private String id;

	private String name;

	private String relevanceFormat;

	private String uploadPath;
	
	private String singleMaximum;
	
	private String totalMaximum;
	
	private String isDefaultType;

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
     * @return the relevanceFormat
     */
    public String getRelevanceFormat() {
        return relevanceFormat;
    }

    /**
     * @param relevanceFormat the relevanceFormat to set
     */
    public void setRelevanceFormat(String relevanceFormat) {
        this.relevanceFormat = relevanceFormat;
    }

    /**
     * @return the uploadPath
     */
    public String getUploadPath() {
        return uploadPath;
    }

    /**
     * @param uploadPath the uploadPath to set
     */
    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    /**
     * @return the singleMaximum
     */
    public String getSingleMaximum() {
        return singleMaximum;
    }

    /**
     * @param singleMaximum the singleMaximum to set
     */
    public void setSingleMaximum(String singleMaximum) {
        this.singleMaximum = singleMaximum;
    }

    /**
     * @return the totalMaximum
     */
    public String getTotalMaximum() {
        return totalMaximum;
    }

    /**
     * @param totalMaximum the totalMaximum to set
     */
    public void setTotalMaximum(String totalMaximum) {
        this.totalMaximum = totalMaximum;
    }

    /**
     * @return the isDefaultType
     */
    public String getIsDefaultType() {
        return isDefaultType;
    }

    /**
     * @param isDefaultType the isDefaultType to set
     */
    public void setIsDefaultType(String isDefaultType) {
        this.isDefaultType = isDefaultType;
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
