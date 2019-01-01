package venus.portal.timerelease.model;

import java.sql.Timestamp;

/**
 * Created by ethan on 13-10-12.
 */
public class EwpDocumentTimeReleaseLog {
    private String    id;
    private String    docId;
    private Timestamp docReleasedTime;
    private Timestamp docReleasedRealTime;

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

    public Timestamp getDocReleasedTime() {
        return docReleasedTime;
    }

    public void setDocReleasedTime(Timestamp docReleaseTime) {
        this.docReleasedTime = docReleaseTime;
    }

    public Timestamp getDocReleasedRealTime() {
        return docReleasedRealTime;
    }

    public void setDocReleasedRealTime(Timestamp docReleasedRealTime) {
        this.docReleasedRealTime = docReleasedRealTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EwpDocumentTimeReleaseLog that = (EwpDocumentTimeReleaseLog) o;

        if (docId != null ? !docId.equals(that.docId) : that.docId != null) return false;
        if (docReleasedTime != null ? !docReleasedTime.equals(that.docReleasedTime) : that.docReleasedTime != null)
            return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (docId != null ? docId.hashCode() : 0);
        result = 31 * result + (docReleasedTime != null ? docReleasedTime.hashCode() : 0);
        return result;
    }
}
