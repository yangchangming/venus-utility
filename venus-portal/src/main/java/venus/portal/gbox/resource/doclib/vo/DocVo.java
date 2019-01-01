package venus.portal.gbox.resource.doclib.vo;

import venus.frames.base.vo.BaseValueObject;

import java.io.File;
import java.sql.Timestamp;

public class DocVo extends BaseValueObject {
    
    private String id;

    private String name;
    
    private String code;
    
    private String tag;
    
    private String author;
    
    private String isOriginal;
    
    private String isExternal;
    
    private String isProtected;
    
    private String origin;
    
    private int clicks;
    
    private int goodCount;
    
    private int badCount;
    
    private int recoCount;
    
    private String fileName;
    
    private String fileSize;
    
    private String fileFormat;
    
    private String fileStatus;
    
    private File fileObject;    

    private String creatorName;
    
    private Timestamp createDate;
    
    private String modifierName;

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
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return the isOriginal
     */
    public String getIsOriginal() {
        return isOriginal;
    }

    /**
     * @param isOriginal the isOriginal to set
     */
    public void setIsOriginal(String isOriginal) {
        this.isOriginal = isOriginal;
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
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(String origin) {
        this.origin = origin;
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
     * @return the goodCount
     */
    public int getGoodCount() {
        return goodCount;
    }

    /**
     * @param goodCount the goodCount to set
     */
    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }

    /**
     * @return the badCount
     */
    public int getBadCount() {
        return badCount;
    }

    /**
     * @param badCount the badCount to set
     */
    public void setBadCount(int badCount) {
        this.badCount = badCount;
    }

    /**
     * @return the recoCount
     */
    public int getRecoCount() {
        return recoCount;
    }

    /**
     * @param recoCount the recoCount to set
     */
    public void setRecoCount(int recoCount) {
        this.recoCount = recoCount;
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
     * @return the fileStatus
     */
    public String getFileStatus() {
        return fileStatus;
    }

    /**
     * @param fileStatus the fileStatus to set
     */
    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    /**
     * @return the fileObject
     */
    public File getFileObject() {
        return fileObject;
    }

    /**
     * @param fileObject the fileObject to set
     */
    public void setFileObject(File fileObject) {
        this.fileObject = fileObject;
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
