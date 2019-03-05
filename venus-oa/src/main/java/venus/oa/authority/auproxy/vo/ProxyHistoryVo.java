/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.authority.auproxy.vo;

import venus.frames.base.vo.BaseValueObject;

import java.sql.Timestamp;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 */
public class ProxyHistoryVo extends BaseValueObject {
    private String id;
    private String proxy_history_id;
    private String proxy_proxyer_history_id;
    private String proxy_authorize_history_id;
    private String operater_id;
    private String operater_name;
    private String canel_id;
    private String canel_name;
    private Timestamp canel_date;
    private Timestamp operater_date;
    private String login_name;
    private String operater_type;
    private String notice_note;
    private String column1;
    private String sponsor;
    private String sponsor_id;
    private String proxy;
    private String proxy_id;
    private String recipient;
    private String recipient_id;
    /**
     * @return the sponsor
     */
    public String getSponsor() {
        return sponsor;
    }
    /**
     * @param sponsor the sponsor to set
     */
    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }
    /**
     * @return the sponsor_id
     */
    public String getSponsor_id() {
        return sponsor_id;
    }
    /**
     * @param sponsor_id the sponsor_id to set
     */
    public void setSponsor_id(String sponsor_id) {
        this.sponsor_id = sponsor_id;
    }
    /**
     * @return the proxy
     */
    public String getProxy() {
        return proxy;
    }
    /**
     * @param proxy the proxy to set
     */
    public void setProxy(String proxy) {
        this.proxy = proxy;
    }
    /**
     * @return the proxy_id
     */
    public String getProxy_id() {
        return proxy_id;
    }
    /**
     * @param proxy_id the proxy_id to set
     */
    public void setProxy_id(String proxy_id) {
        this.proxy_id = proxy_id;
    }
    /**
     * @return the recipient
     */
    public String getRecipient() {
        return recipient;
    }
    /**
     * @param recipient the recipient to set
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    /**
     * @return the recipient_id
     */
    public String getRecipient_id() {
        return recipient_id;
    }
    /**
     * @param recipient_id the recipient_id to set
     */
    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }
    /**
     * @return the proxy_history_id
     */
    public String getProxy_history_id() {
        return proxy_history_id;
    }
    /**
     * @param proxy_history_id the proxy_history_id to set
     */
    public void setProxy_history_id(String proxy_history_id) {
        this.proxy_history_id = proxy_history_id;
    }
    /**
     * @return the proxy_proxyer_history_id
     */
    public String getProxy_proxyer_history_id() {
        return proxy_proxyer_history_id;
    }
    /**
     * @param proxy_proxyer_history_id the proxy_proxyer_history_id to set
     */
    public void setProxy_proxyer_history_id(String proxy_proxyer_history_id) {
        this.proxy_proxyer_history_id = proxy_proxyer_history_id;
    }
    /**
     * @return the proxy_authorize_history_id
     */
    public String getProxy_authorize_history_id() {
        return proxy_authorize_history_id;
    }
    /**
     * @param proxy_authorize_history_id the proxy_authorize_history_id to set
     */
    public void setProxy_authorize_history_id(String proxy_authorize_history_id) {
        this.proxy_authorize_history_id = proxy_authorize_history_id;
    }
    /**
     * @return the operater_id
     */
    public String getOperater_id() {
        return operater_id;
    }
    /**
     * @param operater_id the operater_id to set
     */
    public void setOperater_id(String operater_id) {
        this.operater_id = operater_id;
    }
    /**
     * @return the operater_name
     */
    public String getOperater_name() {
        return operater_name;
    }
    /**
     * @param operater_name the operater_name to set
     */
    public void setOperater_name(String operater_name) {
        this.operater_name = operater_name;
    }
    /**
     * @return the operater_date
     */
    public Timestamp getOperater_date() {
        return operater_date;
    }
    /**
     * @param operater_date the operater_date to set
     */
    public void setOperater_date(Timestamp operater_date) {
        this.operater_date = operater_date;
    }
    /**
     * @return the login_name
     */
    public String getLogin_name() {
        return login_name;
    }
    /**
     * @param login_name the login_name to set
     */
    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }
    /**
     * @return the operater_type
     */
    public String getOperater_type() {
        return operater_type;
    }
    /**
     * @param operater_type the operater_type to set
     */
    public void setOperater_type(String operater_type) {
        this.operater_type = operater_type;
    }
    /**
     * @return the notice_note
     */
    public String getNotice_note() {
        return notice_note;
    }
    /**
     * @param notice_note the notice_note to set
     */
    public void setNotice_note(String notice_note) {
        this.notice_note = notice_note;
    }
    /**
     * @return the column1
     */
    public String getColumn1() {
        return column1;
    }
    /**
     * @param column1 the column1 to set
     */
    public void setColumn1(String column1) {
        this.column1 = column1;
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
     * @return the canel_id
     */
    public String getCanel_id() {
        return canel_id;
    }
    /**
     * @param canel_id the canel_id to set
     */
    public void setCanel_id(String canel_id) {
        this.canel_id = canel_id;
    }
    /**
     * @return the canel_name
     */
    public String getCanel_name() {
        return canel_name;
    }
    /**
     * @param canel_name the canel_name to set
     */
    public void setCanel_name(String canel_name) {
        this.canel_name = canel_name;
    }
    /**
     * @return the canel_date
     */
    public Timestamp getCanel_date() {
        return canel_date;
    }
    /**
     * @param canel_date the canel_date to set
     */
    public void setCanel_date(Timestamp canel_date) {
        this.canel_date = canel_date;
    }
}

