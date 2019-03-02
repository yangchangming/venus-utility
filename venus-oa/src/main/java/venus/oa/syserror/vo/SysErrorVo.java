/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.syserror.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

/**
 * @author zangjian
 *
 */
public class SysErrorVo extends BaseValueObject {
    
    private String  id;             
    private String operate_id;         
    private String operate_name;       
    private Timestamp operate_date;  
    private String error_type;
    private String source_id ;
    private String source_partyid ;
    private String source_code;
    private String source_name;
    private String source_orgtree;
    private String source_typeid;
    private String source_detail;
    private String remark;
    private String cloumn1;
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
     * @return the error_type
     */
    public String getError_type() {
        return error_type;
    }
    /**
     * @param error_type the error_type to set
     */
    public void setError_type(String error_type) {
        this.error_type = error_type;
    }
    /**
     * @return the source_id
     */
    public String getSource_id() {
        return source_id;
    }
    /**
     * @param source_id the source_id to set
     */
    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }
    /**
     * @return the source_partyid
     */
    public String getSource_partyid() {
        return source_partyid;
    }
    /**
     * @param source_partyid the source_partyid to set
     */
    public void setSource_partyid(String source_partyid) {
        this.source_partyid = source_partyid;
    }
    /**
     * @return the source_code
     */
    public String getSource_code() {
        return source_code;
    }
    /**
     * @param source_code the source_code to set
     */
    public void setSource_code(String source_code) {
        this.source_code = source_code;
    }
    /**
     * @return the source_name
     */
    public String getSource_name() {
        return source_name;
    }
    /**
     * @param source_name the source_name to set
     */
    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }
    /**
     * @return the source_orgtree
     */
    public String getSource_orgtree() {
        return source_orgtree;
    }
    /**
     * @param source_orgtree the source_orgtree to set
     */
    public void setSource_orgtree(String source_orgtree) {
        this.source_orgtree = source_orgtree;
    }
    /**
     * @return the source_typeid
     */
    public String getSource_typeid() {
        return source_typeid;
    }
    /**
     * @param source_typeid the source_typeid to set
     */
    public void setSource_typeid(String source_typeid) {
        this.source_typeid = source_typeid;
    }
    /**
     * @return the source_detail
     */
    public String getSource_detail() {
        return source_detail;
    }
    /**
     * @param source_detail the source_detail to set
     */
    public void setSource_detail(String source_detail) {
        this.source_detail = source_detail;
    }
    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }
    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /**
     * @return the cloumn1
     */
    public String getCloumn1() {
        return cloumn1;
    }
    /**
     * @param cloumn1 the cloumn1 to set
     */
    public void setCloumn1(String cloumn1) {
        this.cloumn1 = cloumn1;
    }
    
}

