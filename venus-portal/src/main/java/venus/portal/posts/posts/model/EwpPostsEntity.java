package venus.portal.posts.posts.model;

import venus.frames.base.vo.BaseValueObject;
import venus.portal.helper.EwpStringHelper;

import java.sql.Timestamp;

/**
 * Created by qj on 14-1-27.
 */
public class EwpPostsEntity extends BaseValueObject implements Cloneable {
    private String id;
    private String websiteID;
    private String websiteName;
    private String websiteCode;
    private String docId;
    private String usrId;
    private String usrName;
    private Timestamp pubdate;
    private String content;
    private String replyto;
    private String isreply;

    private String replytoUserName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public String getUsrName() {
        return usrName;
    }

    public void setUsrName(String usrName) {
        this.usrName = usrName;
    }

    public Timestamp getPubdate() {
        return pubdate;
    }

    public void setPubdate(Timestamp pubdate) {
        this.pubdate = pubdate;
    }

    public String getContent() {
        return content;
    }

    public String getHtmlContent() {
        return EwpStringHelper.replaceStringToHtml(content);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWebsiteID() {
        return websiteID;
    }

    public void setWebsiteID(String websiteID) {
        this.websiteID = websiteID;
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

    public String getIsreply() {
        return isreply;
    }

    public void setIsreply(String isreply) {
        this.isreply = isreply;
    }

    public String getReplyto() {
        return replyto;
    }

    public void setReplyto(String replyto) {
        this.replyto = replyto;
    }

    public String getReplytoUserName() {
        return replytoUserName;
    }

    public void setReplytoUserName(String replytoUserName) {
        this.replytoUserName = replytoUserName;
    }
}
