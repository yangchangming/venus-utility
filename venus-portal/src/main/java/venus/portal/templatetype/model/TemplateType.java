/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.templatetype.model;

import venus.frames.base.bo.BaseBusinessObject;

import java.util.Date;

/**
 * 模板类型
 * @author zhaoyapeng
 *
 */
public class TemplateType extends BaseBusinessObject {

    private String id;
    private String typeName;//模板类型名称
    private String description;//模板类型描述
    private Date createTime;//创建时间
    private Long version; //版本(乐观锁)
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
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }
    /**
     * @param typeName the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return createTime;
    }
    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    /**
     * @return the version
     */
    public Long getVersion() {
        return version;
    }
    /**
     * @param version the version to set
     */
    public void setVersion(Long version) {
        this.version = version;
    }
    
    
}
