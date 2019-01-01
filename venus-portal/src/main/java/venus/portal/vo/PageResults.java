/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 前台展示页面用到的VO对象，提供freemarker 展示模板有翻页功能的数据展示
 *
 * @author zhaoyapeng
 */
public class PageResults<T> {
    private List<T> results = new ArrayList();
    private int totalCount = 0;
    private int pageSize = 15; //默认分页条数
    private int currentPage = 1;  //当前页
    private String orderBy;

    public List<T> getResults() {
        return this.results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return ((this.totalCount + this.pageSize - 1) / this.pageSize);
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        if (currentPage <= 0) currentPage = 1;
        this.currentPage = currentPage;
    }

    public int getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getResultsFrom() {
        return ((this.currentPage - 1) * this.pageSize + 1);
    }

    public int getResultsEnd() {
        return (this.pageSize * this.currentPage);
    }
}
