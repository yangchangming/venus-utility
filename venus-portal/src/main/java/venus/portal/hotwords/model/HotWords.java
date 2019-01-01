package venus.portal.hotwords.model;

import venus.frames.base.vo.BaseValueObject;
import venus.portal.helper.EwpVoHelper;

import java.sql.Timestamp;

/**
 * @author chengliang
 */

public class HotWords extends BaseValueObject implements Cloneable {

    //开始ewp_hot_words的属性

    /**
     * id 表示: 主键
     * 数据库中的注释:
     */
    private String id;

    /**
     * name 表示: 热词名
     * 数据库中的注释:
     */
    private String name;

    /**
     * link 表示: 热词链接
     * 数据库中的注释:
     */
    private String link;

    /**
     * enableStatus 表示: 是否可用
     * 数据库中的注释:
     */
    private String enableStatus;

    /**
     * create_date 表示: 创建时间
     * 数据库中的注释:
     */
    private Timestamp createDate;

    /**
     * modify_date 表示: 修改时间
     * 数据库中的注释:
     */
    private Timestamp modifyDate;

    /**
     * enable_date 表示: 可用状态的修改时间
     * 数据库中的注释:
     */
    private Timestamp enableDate;

    /**
     * version 表示: 版本
     * 数据库中的注释:
     */
    private Long version;
    //结束ewp_hot_words的属性


    //开始ewp_hot_words的setter和getter方法

    /**
     * 获得主键
     *
     * @return 主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获得name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置name
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获得link
     *
     * @return link
     */
    public String getLink() {
        return link;
    }

    /**
     * 设置link
     *
     * @param link link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * 获得enableStatus
     *
     * @return enableStatus
     */
    public String getEnableStatus() {
        return enableStatus;
    }

    /**
     * 设置enableStatus
     *
     * @param enableStatus enableStatus
     */
    public void setEnableStatus(String enableStatus) {
        this.enableStatus = enableStatus;
    }

    /**
     * 获得createDate
     *
     * @return createDate
     */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /**
     * 设置createDate
     *
     * @param createDate createDate
     */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /**
     * 获得modifyDate
     *
     * @return modifyDate
     */
    public Timestamp getModifyDate() {
        return modifyDate;
    }

    /**
     * 设置modifyDate
     *
     * @param modifyDate modifyDate
     */
    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * 获得enableDate
     *
     * @return enableDate
     */
    public Timestamp getEnableDate() {
        return enableDate;
    }

    /**
     * 设置enableDate
     *
     * @param enableDate enableDate
     */
    public void setEnableDate(Timestamp enableDate) {
        this.enableDate = enableDate;
    }

    /**
     * 获得version
     *
     * @return version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * 设置version
     *
     * @param version version
     */
    public void setVersion(Long version) {
        this.version = version;
    }
    //结束ewp_hot_words的setter和getter方法

    /**
     * override method 'equals'
     *
     * @param other 与本对象比较的其它对象
     * @return boolean 两个对象的各个属性是否都相等
     */
    public boolean equals(Object other) {
        return EwpVoHelper.voEquals(this, other);
    }

    /**
     * override method 'hashCode'
     *
     * @return int Hash码
     */
    public int hashCode() {
        return EwpVoHelper.voHashCode(this);
    }

    /**
     * override method 'clone'
     *
     * @return Object 克隆后对象
     * @see Object#clone()
     */
    public Object clone() {
        return EwpVoHelper.voClone(this);
    }

    /**
     * override method 'toString'
     *
     * @return String 字符串表示
     */
    public String toString() {
        return super.toString() + ":" + EwpVoHelper.voToString(this);
    }
}
