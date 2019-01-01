package venus.portal.api.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import venus.portal.api.util.IApiConstants;
import venus.portal.cache.data.DataCache;
import venus.portal.searchengine.lucene.Operation.Operation;
import venus.portal.searchengine.lucene.operationvo.SearchCondition;
import venus.portal.searchengine.lucene.operationvo.index.DocVo;
import venus.portal.searchengine.lucene.operationvo.index.util.DocSearchCol;
import venus.portal.util.ServletContextHelper;
import venus.portal.vo.PageResults;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cj
 * Date: 13-10-15
 * Time: 下午3:32
 */

@Controller
public class SearchEngineController {
    private static Logger loger = Logger.getLogger(SearchEngineController.class);
    @Autowired
    @Qualifier("ewpSearchEngineOperation")
    private Operation ewpSearchEngineOperation;

    @Autowired
    private DataCache dataCache;

    /**
     * 搜索关键字
     *
     * @param request
     * @return
     * @throws
     */
    @RequestMapping(value = "{siteCode}/search")
    public ModelAndView onSearch(@PathVariable String siteCode, HttpServletRequest request) throws Exception {
        String condition = request.getParameter(SearchCondition.CONDITION_KEY);
        String website = ServletContextHelper.getWebsiteIdBySiteCode(siteCode);
        String currentPage = request.getParameter(SearchCondition.CURRENT_PAGE) == null ? "1" : request.getParameter(SearchCondition.CURRENT_PAGE);

        if (condition == null || condition.isEmpty()) {
            return new ModelAndView("/" + siteCode + "/" + IApiConstants.VIEW_SEARCHRESULT, IApiConstants.UI_KEY, new SearchCondition("",website,0,0));
        } else {

            Map<String, String> conditionMap = new HashMap<String, String>();
            conditionMap.put(SearchCondition.CONDITION_KEY, condition);
            conditionMap.put(SearchCondition.CONDITION_WEBSITE, website);
            conditionMap.put(SearchCondition.CURRENT_PAGE, currentPage);

            SearchCondition searchCondition = null;
            try {
                searchCondition = ewpSearchEngineOperation.searchIndex(conditionMap);
            } catch (Exception e) {
                loger.error("search parse or io error!", e);
            }

            if (searchCondition != null) {
                initPath(siteCode, searchCondition.getPageResults());
            }

            //未查询出结果时显示空白结果集。
            if(searchCondition == null){
                searchCondition = new SearchCondition(condition,website,1,0);
            }

            return new ModelAndView("/" + siteCode + "/" + IApiConstants.VIEW_SEARCHRESULT, IApiConstants.UI_KEY, searchCondition);
        }
    }

    private void initPath(String siteCode, PageResults<DocVo> pageResults) {
        for (DocVo doc : pageResults.getResults()) {
            String temp = doc.getFields().get(DocSearchCol.PATH.getColName()).getValue();
            doc.getFields().get(DocSearchCol.PATH.getColName()).setValue(siteCode + "/article/" + temp);
        }
    }
}
