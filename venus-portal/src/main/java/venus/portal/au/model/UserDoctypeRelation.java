package venus.portal.au.model;

import venus.frames.base.bo.BaseBusinessObject;

/**
 * Created with IntelliJ IDEA.
 * User: qj
 * Date: 13-10-9
 * Time: 上午10:40
 * To change this template use File | Settings | File Templates.
 */
public class UserDoctypeRelation extends BaseBusinessObject {
    private String userID="";
    private String docTypeID="";
    private String websiteID="";

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDocTypeID() {
        return docTypeID;
    }

    public void setDocTypeID(String docTypeID) {
        this.docTypeID = docTypeID;
    }

    public String getWebsiteID() {
        return websiteID;
    }

    public void setWebsiteID(String websiteID) {
        this.websiteID = websiteID;
    }
}
