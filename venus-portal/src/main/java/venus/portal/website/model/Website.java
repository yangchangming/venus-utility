package venus.portal.website.model;

import venus.frames.base.bo.BaseBusinessObject;
import venus.portal.doctype.model.DocType;

public class Website extends BaseBusinessObject {

    /**
     * 网站名称
     */
    public static final String WEBSITE_NAME = "websiteName";
    /**
     * 网站内容
     */
    public static final String DESCRIPTION = "description";
    /**
     * 语言
     */
    public static final String LANGUAGE = "language";
    /**
     * 是否默认网站
     */
    public static final String ISDEFAULT = "isDefault";

    private String id;
    private String websiteName;
    private String websiteCode;
    private String description;
    private String language;
    private String isDefault;
    private String nameIsUnique;
    private String keywords;
    private String hotWordsSwitcher;
    private String linkTarget;
    private Long version; //版本(乐观锁)
    private DocType rootChannel; //此站点栏目的根栏目

    public Website() {
    }

    public Website(String id, String websiteName, String isDefault, String websiteCode) {
        this.id = id;
        this.websiteName = websiteName;
        this.isDefault = isDefault;
        this.websiteCode = websiteCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getWebsiteCode() {
        return websiteCode;
    }

    public void setWebsiteCode(String websiteCode) {
        this.websiteCode = websiteCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public DocType getRootChannel() {
        return rootChannel;
    }

    public void setRootChannel(DocType rootChannel) {
        this.rootChannel = rootChannel;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getHotWordsSwitcher() {
        return hotWordsSwitcher;
    }

    public void setHotWordsSwitcher(String hotWordsSwitcher) {
        this.hotWordsSwitcher = hotWordsSwitcher;
    }

    public String getLinkTarget() {
        return linkTarget;
    }

    public void setLinkTarget(String linkTarget) {
        this.linkTarget = linkTarget;
    }

    /**
     * @return the nameIsUnique
     */
    public String getNameIsUnique() {
        return nameIsUnique;
    }

    /**
     * @param nameIsUnique the nameIsUnique to set
     */
    public void setNameIsUnique(String nameIsUnique) {
        this.nameIsUnique = nameIsUnique;
    }
}
