package venus.portal.timerelease.model;

import venus.portal.gbox.util.DateTools;
import venus.pub.util.DateUtil;

import java.sql.Timestamp;

/**
 * Created by ethan on 13-10-12.
 */
public class EwpDocumentTimeRelease {
    private String id;
    private String docId;
    private String webSiteId;
    private Timestamp docPreReleaseTime;

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

    public String getWebSiteId() {
        return webSiteId;
    }

    public void setWebSiteId(String webSiteId) {
        this.webSiteId = webSiteId;
    }

    public Timestamp getDocPreReleaseTime() {
        return docPreReleaseTime;
    }

    public void setDocPreReleaseTime(Timestamp docPreReleaseTime) {
        this.docPreReleaseTime = docPreReleaseTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EwpDocumentTimeRelease that = (EwpDocumentTimeRelease) o;

        if (docId != null ? !docId.equals(that.docId) : that.docId != null) return false;
        if (docPreReleaseTime != null ? !docPreReleaseTime.equals(that.docPreReleaseTime) : that.docPreReleaseTime != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (docId != null ? docId.hashCode() : 0);
        result = 31 * result + (docPreReleaseTime != null ? docPreReleaseTime.hashCode() : 0);
        return result;
    }

    public EwpDocumentTimeReleaseLog tranToDocTimeReleaseLog() {
        EwpDocumentTimeReleaseLog docTRLog = new EwpDocumentTimeReleaseLog();
        docTRLog.setDocReleasedTime(this.getDocPreReleaseTime());
        docTRLog.setDocId(this.getDocId());
        docTRLog.setDocReleasedRealTime(DateTools.getTimestamp(DateUtil.getCurrentTimeStamp()));
        return docTRLog;
    }
}
