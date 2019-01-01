package venus.portal.gbox.resource.option.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

public class OptionVo extends BaseValueObject {
    
    private String id;

    private String name;

    private String code;
    
    private String value;
    
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
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
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
