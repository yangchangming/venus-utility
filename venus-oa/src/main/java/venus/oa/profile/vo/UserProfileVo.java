/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.profile.vo;

import java.sql.Timestamp;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 * 
 */
public class UserProfileVo {
    String id;
    String propertykey;
    String value;
    Timestamp inittime;
    Timestamp updatetime;
    String partyid;
    String partyname;
    String description;
    String cloumn1;
    String enable;
    String propertytype;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the propertykey
     */
    public String getPropertykey() {
        return propertykey;
    }

    /**
     * @param propertykey
     *            the propertykey to set
     */
    public void setPropertykey(String propertykey) {
        this.propertykey = propertykey;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the inittime
     */
    public Timestamp getInittime() {
        return inittime;
    }

    /**
     * @param inittime
     *            the inittime to set
     */
    public void setInittime(Timestamp inittime) {
        this.inittime = inittime;
    }

    /**
     * @return the updatetime
     */
    public Timestamp getUpdatetime() {
        return updatetime;
    }

    /**
     * @param updatetime
     *            the updatetime to set
     */
    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * @return the partyid
     */
    public String getPartyid() {
        return partyid;
    }

    /**
     * @param partyid
     *            the partyid to set
     */
    public void setPartyid(String partyid) {
        this.partyid = partyid;
    }

    /**
     * @return the partyname
     */
    public String getPartyname() {
        return partyname;
    }

    /**
     * @param partyname
     *            the partyname to set
     */
    public void setPartyname(String partyname) {
        this.partyname = partyname;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the cloumn1
     */
    public String getCloumn1() {
        return cloumn1;
    }

    /**
     * @param cloumn1
     *            the cloumn1 to set
     */
    public void setCloumn1(String cloumn1) {
        this.cloumn1 = cloumn1;
    }

    /**
     * @return the enable
     */
    public String getEnable() {
        return enable;
    }

    /**
     * @param enable
     *            the enable to set
     */
    public void setEnable(String enable) {
        this.enable = enable;
    }

    /**
     * @return the propertytype
     */
    public String getPropertytype() {
        return propertytype;
    }

    /**
     * @param propertytype
     *            the propertytype to set
     */
    public void setPropertytype(String propertytype) {
        this.propertytype = propertytype;
    }

}

