/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import venus.portal.api.util.IApiConstants;
import venus.portal.document.bs.IDocumentBS;
import venus.portal.document.model.Document;
import venus.portal.vo.DocumentListVo;
import venus.portal.vo.PageResults;

import javax.servlet.http.HttpServletRequest;

/**
 * 根据所属栏目（文章类型）ID分页获取其下的文章
 * 前台调用时需要传入的参数为
 * id:所属栏目ID
 * view:跳转到的文章列表模板
 * pageSize:每页显示条数，此参数为可选。默认值为15，在前台需要根据不同需求显示条数不同时，可传递此参数
 * orderStr ：排序字段，此参数可选
 * 调用方法例如
 * <a href="${rc.getContextPath()}/api/documents.page?id=${result.id}&view=/documents&pageSize=15">文章列表</a>
 */
@Controller
public class DocumentsController {

    @Autowired
    private IDocumentBS documentBS;

    @RequestMapping(value = "{siteCode}/api/articles/{docTypeId}")
    public ModelAndView handleRequest(@PathVariable String siteCode, @PathVariable String docTypeId,
                                      HttpServletRequest request) throws Exception {
        return handleRequest(siteCode, docTypeId, "", request);
    }

    @RequestMapping(value = "{siteCode}/api/articles/{docTypeId}/{viewName}", method = RequestMethod.GET)
    public ModelAndView handleRequest(@PathVariable String siteCode, @PathVariable String docTypeId,
                                      @PathVariable String viewName, HttpServletRequest request) throws Exception {
        String isRecommend = request.getParameter(IApiConstants.PARAM_ORDERBY_IS_RECOMMEND);
        String isPublishTime = request.getParameter(IApiConstants.PARAM_ORDERBY_IS_PUBLISHTIME);
        String isSortNum = request.getParameter(IApiConstants.PARAM_ORDERBY_IS_SORTNUM);
        String orderStr = getSequenceSql(isRecommend, isPublishTime, isSortNum);

        String tempCurrentPage = request.getParameter(IApiConstants.PAGE_NO);
        int currentPage = Integer.parseInt(tempCurrentPage == null ? "1" : tempCurrentPage);
        String tempPageSize = request.getParameter(IApiConstants.PAGE_SIZE);
        int pageSize = Integer.parseInt(tempPageSize == null ? "15" : tempPageSize);
        PageResults<Document> results = documentBS.queryAllDocByDocTypeId(docTypeId, currentPage, pageSize, orderStr);
        DocumentListVo docListVo = new DocumentListVo();
        docListVo.setDocTypeId(docTypeId);
        docListVo.setPageResults(results);
        if (viewName == null || "".equals(viewName)) {
            viewName = IApiConstants.VIEW_DOCUMENTS;
            return new ModelAndView(viewName, IApiConstants.UI_KEY, docListVo);
        }

        return new ModelAndView(siteCode + "/" + viewName, IApiConstants.UI_KEY, docListVo);
    }

    /**
     * @param isRecommend
     * @param isPublishTime
     * @param isSortNum
     * @return
     */
    private String getSequenceSql(String isRecommend, String isPublishTime,
                                  String isSortNum) {
        String orderStr = "";
        if (isRecommend != null && IApiConstants.PARAM_ORDERBY_IS_Y.equals(isRecommend)) {
            orderStr = noSpaceStringAddComma(orderStr);
            orderStr += "recommend desc";
        }
        if (isPublishTime != null) {
            if (IApiConstants.PARAM_ORDERBY_IS_Y.equals(isPublishTime)) {
                orderStr = noSpaceStringAddComma(orderStr);
                orderStr += "publishTime asc";
            }
            if (IApiConstants.PARAM_ORDERBY_IS_N.equals(isPublishTime)) {
                orderStr = noSpaceStringAddComma(orderStr);
                orderStr += "publishTime desc";
            }
        }
        if (isSortNum != null) {
            if (IApiConstants.PARAM_ORDERBY_IS_Y.equals(isSortNum)) {
                orderStr = noSpaceStringAddComma(orderStr);
                orderStr += "sortNum asc";
            }
            if (IApiConstants.PARAM_ORDERBY_IS_N.equals(isSortNum)) {
                orderStr = noSpaceStringAddComma(orderStr);
                orderStr += "sortNum desc";
            }
        }
        if (orderStr.equals("")) {
            orderStr = "publishTime desc,sortNum desc";
        }
        return orderStr;
    }

    /**
     * @param orderStr
     * @return
     */
    private String noSpaceStringAddComma(String orderStr) {
        if (!orderStr.equals("")) {
            orderStr += ",";
        }
        return orderStr;
    }
}
