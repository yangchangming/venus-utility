/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.vo;

/**
 * 文档列表查询时的VO对象
 * @author zhaoyapeng
 *
 */
public class DocumentListVo {

    private PageResults pageResults;
    private String docTypeId;
 
    /**
     * @return the pageResults
     */
    public PageResults getPageResults() {
        return pageResults;
    }
    /**
     * @param pageResults the pageResults to set
     */
    public void setPageResults(PageResults pageResults) {
        this.pageResults = pageResults;
    }
    /**
     * @return the docTypeId
     */
    public String getDocTypeId() {
        return docTypeId;
    }
    /**
     * @param docTypeId the docTypeId to set
     */
    public void setDocTypeId(String docTypeId) {
        this.docTypeId = docTypeId;
    }
    
    
}
