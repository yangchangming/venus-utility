package venus.portal.gbox.resource.resourceinfo.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;
import java.util.Date;

public class ResourceVo extends BaseValueObject {
    
    private String id;

    private String name;

    private String code;

    private String tag;
    
    private String type;
    
    private String isProtected;
    
    private String isExternal;
    
    private String fileName;
    
    private String fileSize;
    
    private String fileFormat;
    
    private String creatorName;

    private Timestamp createDate;
    
    private Date stardDate;
    
    private Date endDate;

    private String modifierName;
    
    private Timestamp modifyDate;

    private String enableStatus;

    private String description;

    /**
     * @return the stardDate
     */
    public Date getStardDate() {
        return stardDate;
    }

    /**
     * @param stardDate the stardDate to set
     */
    public void setStardDate(Date stardDate) {
        this.stardDate = stardDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

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
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the isProtected
     */
    public String getIsProtected() {
        return isProtected;
    }

    /**
     * @param isProtected the isProtected to set
     */
    public void setIsProtected(String isProtected) {
        this.isProtected = isProtected;
    }

    /**
     * @return the isExternal
     */
    public String getIsExternal() {
        return isExternal;
    }

    /**
     * @param isExternal the isExternal to set
     */
    public void setIsExternal(String isExternal) {
        this.isExternal = isExternal;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the fileSize
     */
    public String getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return the fileFormat
     */
    public String getFileFormat() {
        return fileFormat;
    }

    /**
     * @param fileFormat the fileFormat to set
     */
    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    /**
     * @return the creatorName
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * @param creatorName the creatorName to set
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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
     * @return the modifierName
     */
    public String getModifierName() {
        return modifierName;
    }

    /**
     * @param modifierName the modifierName to set
     */
    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
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
