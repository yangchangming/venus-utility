/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.authority.auauthorizelog.vo;

import venus.oa.authority.auauthorize.vo.AuAuthorizeVo;

import java.sql.Timestamp;


/**
 * @author zangjian
 *
 */
public class AuAuthorizeLogVo extends AuAuthorizeVo {

    private Timestamp operate_date;
    private String operate_id;
    private String operate_name;
    private String operate_type;
    private String visitor_name;
    private String resource_name;
    private String accredit_type;
    private Timestamp modify_date;
    private Timestamp delete_date;
    private String authorize_tag;
    
    /**
     * @return the authorize_tag
     */
    public String getAuthorize_tag() {
        return authorize_tag;
    }
    /**
     * @param authorize_tag the authorize_tag to set
     */
    public void setAuthorize_tag(String authorize_tag) {
        this.authorize_tag = authorize_tag;
    }
    /**
     * @return the operate_date
     */
    public Timestamp getOperate_date() {
        return operate_date;
    }
    /**
     * @param operate_date the operate_date to set
     */
    public void setOperate_date(Timestamp operate_date) {
        this.operate_date = operate_date;
    }
    /**
     * @return the operate_id
     */
    public String getOperate_id() {
        return operate_id;
    }
    /**
     * @param operate_id the operate_id to set
     */
    public void setOperate_id(String operate_id) {
        this.operate_id = operate_id;
    }
    /**
     * @return the operate_name
     */
    public String getOperate_name() {
        return operate_name;
    }
    /**
     * @param operate_name the operate_name to set
     */
    public void setOperate_name(String operate_name) {
        this.operate_name = operate_name;
    }
    /**
     * @return the operate_type
     */
    public String getOperate_type() {
        return operate_type;
    }
    /**
     * @param operate_type the operate_type to set
     */
    public void setOperate_type(String operate_type) {
        this.operate_type = operate_type;
    }
    
    /**
     * @return the visitor_name
     */
    public String getVisitor_name() {
        return visitor_name;
    }
    /**
     * @param visitor_name the visitor_name to set
     */
    public void setVisitor_name(String visitor_name) {
        this.visitor_name = visitor_name;
    }
    

    /**
     * @return the resource_name
     */
    public String getResource_name() {
        return resource_name;
    }
    /**
     * @param resource_name the resource_name to set
     */
    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }

     /**
     * @return the accredit_type
     */
    public String getAccredit_type() {
        return accredit_type;
    }
    /**
     * @param accredit_type the accredit_type to set
     */
    public void setAccredit_type(String accredit_type) {
        this.accredit_type = accredit_type;
    }

    /**
     * @return the modify_date
     */
    public Timestamp getModify_date() {
        return modify_date;
    }
    /**
     * @param modify_date the modify_date to set
     */
    public void setModify_date(Timestamp modify_date) {
        this.modify_date = modify_date;
    }
    /**
     * @return the delete_date
     */
    public Timestamp getDelete_date() {
        return delete_date;
    }
    /**
     * @param delete_date the delete_date to set
     */
    public void setDelete_date(Timestamp delete_date) {
        this.delete_date = delete_date;
    }
    
}

