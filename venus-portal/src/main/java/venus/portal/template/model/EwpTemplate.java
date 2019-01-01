package venus.portal.template.model;

import venus.frames.base.bo.BaseBusinessObject;
import venus.portal.website.model.Website;

import java.sql.Timestamp;


public class EwpTemplate extends BaseBusinessObject {

    /**
     * 模板名称
     */
    public static final String TEMPLATE_NAME = "template_name";
    /**
     * 模板内容
     */
    public static final String TEMPLATE_CONTENT = "template_content";
    /**
     * 模板类型
     */
    public static final String TEMPLATE_TYPE = "template_type";
    /**
     * 是否默认模板
     */
    public static final String ISDEFAULT = "isdefault";
    /**
     * 所属网站ID
     */
    public static final String WEBSITEID = "website_id";

    private String id;
    private String template_name;
    private String template_viewname;
    private String filepath;
    private String directory;
    private String oldfilepath;
    private String template_content;
    private String order_code;
    private String remark;
    private String usable_status;
    private Timestamp create_date;
    private String create_ip;
    private String create_user_id;
    private Timestamp modify_date;
    private String modify_ip;
    private String modify_user_id;
    private String template_type;
    private Long version; //版本(乐观锁)
    private String isSystem; //是否系统预置模板
    private String isDefault; // 是否为站点默认

    private String webSiteId;//模板所属站点Id
    private Website webSite;//多站点时，区分模板所属站点

    public EwpTemplate() {
    }

    public EwpTemplate(String templateName, Website webSite, Timestamp createDate) {
        this.template_name = templateName;
        this.template_viewname = templateName;
        this.webSite = webSite;
        this.create_date = createDate;
        this.template_type = "common";
        this.isDefault = "N";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplate_name() {
        return template_name;
    }

    public void setTemplate_name(String template_name) {
        this.template_name = template_name;
    }

    public String getTemplate_content() {
        return template_content;
    }

    public void setTemplate_content(String template_content) {
        this.template_content = template_content;
    }

    /**
     * 获得获得过滤后的template_content(在textarea中显示时遇到</textarea>时会提前闭合标签，故替换为&lt;。textarea会显示<,提交到后台也是<,不会提交&lt;)
     *
     * @return String
     */
    public String getTemplate_content_of_filter() {
        return template_content != null ? template_content.replaceAll("</textarea", "&lt;/textarea") : template_content;
    }

    /**
     * 设置过滤后的template_content
     *
     * @param template_content_of_filter
     */
    public void setTemplate_content_of_filter(String template_content_of_filter) {
        this.template_content = template_content_of_filter != null ? template_content_of_filter.replaceAll("&lt;/textarea", "</textarea") : template_content_of_filter;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUsable_status() {
        return usable_status;
    }

    public void setUsable_status(String usable_status) {
        this.usable_status = usable_status;
    }

    public Timestamp getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Timestamp create_date) {
        this.create_date = create_date;
    }

    public String getCreate_ip() {
        return create_ip;
    }

    public void setCreate_ip(String create_ip) {
        this.create_ip = create_ip;
    }

    public String getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(String create_user_id) {
        this.create_user_id = create_user_id;
    }

    public Timestamp getModify_date() {
        return modify_date;
    }

    public void setModify_date(Timestamp modify_date) {
        this.modify_date = modify_date;
    }

    public String getModify_ip() {
        return modify_ip;
    }

    public void setModify_ip(String modify_ip) {
        this.modify_ip = modify_ip;
    }

    public String getModify_user_id() {
        return modify_user_id;
    }

    public void setModify_user_id(String modify_user_id) {
        this.modify_user_id = modify_user_id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getTemplate_viewname() {
        return template_viewname;
    }

    public void setTemplate_viewname(String template_viewname) {
        this.template_viewname = template_viewname;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getOldfilepath() {
        return oldfilepath;
    }

    public void setOldfilepath(String oldfilepath) {
        this.oldfilepath = oldfilepath;
    }

    public Website getWebSite() {
        return webSite;
    }

    public void setWebSite(Website webSite) {
        this.webSite = webSite;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    public String getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getWebSiteId() {
        return webSiteId;
    }

    public void setWebSiteId(String webSiteId) {
        this.webSiteId = webSiteId;
    }
}
