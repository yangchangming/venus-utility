/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.document.model;

import venus.frames.base.bo.BaseBusinessObject;

import java.sql.Timestamp;

/**
 * @author yangchangming
 */
public class Document extends BaseBusinessObject {

    private String id; //标识
    private String title;
    private String shortTitle;
    private String docTypeID;
    private String picture;
    private String titelAbstract;
    private String content;
    private String status;
    private String url;
    private String seoKeyWord;
    private String source;
    private String thumbnail;
    private String createBy;
    private Timestamp createTime;
    private String editBy;
    private Timestamp editTime;
    private String tag;
    private String permissions;
    private String isComment;
    private String isShowHotWords;
    private Long visitCount;
    private String recommend;
    private Timestamp publishTime;
    private Long sortNum;
    private String isValid;
    private String spare1;
    private String spare2;
    // private Set docTypeRelationsList = new HashSet();
    // private Set<DocType> docTypes= new HashSet<DocType>();

    /**
     * construct
     */
    public Document() {
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the shortTitle
     */
    public String getShortTitle() {
        return shortTitle;
    }

    /**
     * @param shortTitle the shortTitle to set
     */
    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    /**
     * @return the docTypeID
     */
    public String getDocTypeID() {
        return docTypeID;
    }

    /**
     * @param docTypeID the docTypeID to set
     */
    public void setDocTypeID(String docTypeID) {
        this.docTypeID = docTypeID;
    }

    /**
     * @return the picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * @param picture the picture to set
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * @return the titelAbstract
     */
    public String getTitelAbstract() {
        return titelAbstract;
    }

    /**
     * @param titelAbstract the titelAbstract to set
     */
    public void setTitelAbstract(String titelAbstract) {
        this.titelAbstract = titelAbstract;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the seoKeyWord
     */
    public String getSeoKeyWord() {
        return seoKeyWord;
    }

    /**
     * @param seoKeyWord the seoKeyWord to set
     */
    public void setSeoKeyWord(String seoKeyWord) {
        this.seoKeyWord = seoKeyWord;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the thumbnail
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * @param thumbnail the thumbnail to set
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * @return the createBy
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * @param createBy the createBy to set
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * @return the createTime
     */
    public Timestamp getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the editBy
     */
    public String getEditBy() {
        return editBy;
    }

    /**
     * @param editBy the editBy to set
     */
    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }

    /**
     * @return the editTime
     */
    public Timestamp getEditTime() {
        return editTime;
    }

    /**
     * @param editTime the editTime to set
     */
    public void setEditTime(Timestamp editTime) {
        this.editTime = editTime;
    }

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return the permissions
     */
    public String getPermissions() {
        return permissions;
    }

    /**
     * @param permissions the permissions to set
     */
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    /**
     * @return the isComment
     */
    public String getIsComment() {
        return isComment;
    }

    /**
     * @param isComment the isComment to set
     */
    public void setIsComment(String isComment) {
        this.isComment = isComment;
    }

    public String getIsShowHotWords() {
        return isShowHotWords;
    }

    public void setIsShowHotWords(String isShowHotWords) {
        this.isShowHotWords = isShowHotWords;
    }

    /**
     * @return the visitCount
     */
    public Long getVisitCount() {
        return visitCount;
    }

    /**
     * @param visitCount the visitCount to set
     */
    public void setVisitCount(Long visitCount) {
        this.visitCount = visitCount;
    }

    /**
     * @return the recommend
     */
    public String getRecommend() {
        return recommend;
    }

    /**
     * @param recommend the recommend to set
     */
    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    /**
     * @return the publishTime
     */
    public Timestamp getPublishTime() {
        return publishTime;
    }

    /**
     * @param publishTime the publishTime to set
     */
    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
    }

    /**
     * @return the sortNum
     */
    public Long getSortNum() {
        return sortNum;
    }

    /**
     * @param sortNum the sortNum to set
     */
    public void setSortNum(Long sortNum) {
        this.sortNum = sortNum;
    }

    /**
     * @return the isValid
     */
    public String getIsValid() {
        return isValid;
    }

    /**
     * @param isValid the isValid to set
     */
    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    /**
     * @return the spare1
     */
    public String getSpare1() {
        return spare1;
    }

    /**
     * @param spare1 the spare1 to set
     */
    public void setSpare1(String spare1) {
        this.spare1 = spare1;
    }

    /**
     * @return the spare2
     */
    public String getSpare2() {
        return spare2;
    }

    /**
     * @param spare2 the spare2 to set
     */
    public void setSpare2(String spare2) {
        this.spare2 = spare2;
    }

    /**
     * @return the docTypeRelationsList
     */
//    public Set getDocTypeRelationsList() {
//        return docTypeRelationsList;
//    }

    /**
     * @param docTypeRelationsList the docTypeRelationsList to set
     */
//    public void setDocTypeRelationsList(Set docTypeRelationsList) {
//        this.docTypeRelationsList = docTypeRelationsList;
//    }


}
