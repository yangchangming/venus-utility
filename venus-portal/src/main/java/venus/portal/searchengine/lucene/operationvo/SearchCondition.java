package venus.portal.searchengine.lucene.operationvo;

import venus.portal.searchengine.lucene.operationvo.index.DocVo;
import venus.portal.vo.PageResults;

/**
 * Created by qj on 13-12-24.
 */
public class SearchCondition {

    public static final String CURRENT_PAGE="currentPage";
    public static final String CONDITION_KEY="condition";
    public static final String CONDITION_WEBSITE="website";

    private String condition="";
    private String website="";

    private PageResults<DocVo> pageResults = new PageResults<DocVo>();

    public SearchCondition(String condition, String website,int currentPage, int recordSize) {
        this.setCondition(condition);
        this.setWebsite(website);
        getPageResults().setTotalCount(recordSize);
        getPageResults().setCurrentPage(currentPage);
    }
    public SearchCondition(String condition, String website,int currentPage, int recordSize, int recordNumPerPage) {
        this.setCondition(condition);
        this.setWebsite(website);
        getPageResults().setTotalCount(recordSize);
        getPageResults().setPageSize(recordSize);
        getPageResults().setCurrentPage(currentPage);
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public PageResults<DocVo> getPageResults() {
        return pageResults;
    }

    public void setPageResults(PageResults<DocVo> pageResults) {
        this.pageResults = pageResults;
    }
}
