/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.vo;

import java.util.List;
import java.util.Map;

/**
 * 文档列表查询时的VO对象
 *
 * @author zhaoyapeng
 */
public class ConditionPageResults<T> {

    private Map conditionPageMap = null;
    //conditions; //条件集合
    //results //结果列表
    //totalCount = 0;
    //pageSize = 15; //默认分页条数
    //currentPage = 1;  //当前页
    //orderBy; //排序

    public ConditionPageResults(Map conditionPageMap) {
        this.conditionPageMap = conditionPageMap;
    }

    public Map<String, String> getConditions() {
        return (Map<String, String>) (this.conditionPageMap.get("conditions"));
    }

    public void setConditions(Map<String, String> conditions) {
        this.conditionPageMap.put("conditions", conditions);
    }

    public List<T> getResults() {
        return (List<T>) (this.conditionPageMap.get("results"));
    }

    public void setResults(List<T> results) {
        this.conditionPageMap.put("results", results);
    }

    public int getPageSize() {
        return Integer.parseInt(this.conditionPageMap.get("pageSize").toString());
    }

    public void setPageSize(int pageSize) {
        this.conditionPageMap.put("pageSize", pageSize);
    }

    public void setPageSize(String pageSize) {
        this.conditionPageMap.put("pageSize", pageSize);
    }

    public int getCurrentPage() {
        return (Integer) (this.conditionPageMap.get("currentPage"));
    }

    public void setCurrentPage(int currentPage) {
        if (currentPage <= 0) currentPage = 1;
        this.conditionPageMap.put("currentPage", currentPage);
    }

    public int getTotalCount() {
        return (Integer) (this.conditionPageMap.get("totalCount"));
    }

    public void setTotalCount(int totalCount) {
        if (totalCount < 0) totalCount = 1;
        this.conditionPageMap.put("totalCount", totalCount);
    }

    public String getOrderBy() {
        return (String) (this.conditionPageMap.get("orderBy"));
    }

    public void setOrderBy(String orderBy) {
        this.conditionPageMap.put("orderBy", orderBy);
    }


}
