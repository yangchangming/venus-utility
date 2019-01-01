package venus.commons.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import venus.frames.base.IGlobalsKeys;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;

import javax.servlet.ServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: ethan
 * Date: 13-9-21
 * Time: 上午11:59
 * To change this template use File | Settings | File Templates.
 */
@Service
public class StandardPageService implements PageService, IGlobalsKeys {

    private static ILog log = LogMgr.getLogger(StandardPageService.class);

    public PageVo findPageVo(ServletRequest request) {

        String pageAllCountKey = request.getParameter(PAGE_ALLCOUNT_KEY);
        if (StringUtils.isNotBlank(pageAllCountKey)) {
            int recodeCount = Integer.parseInt(pageAllCountKey);
            return createPageVo(request, recodeCount);
        }

        Object o = request.getAttribute(PAGEVO_KEY);

        if (o != null) {
            return (PageVo)o;
        }

        return null;
    }

    public PageVo createPageVo(ServletRequest request, int recordCount) {

        PageVo pageVo = new PageVo();

        int pageNo = Helper.DEFAULT_PAGE_NO;
        int pageSize = Helper.DEFAULT_PAGE_SIZE;

        if (StringUtils.isNotBlank(request.getParameter(PAGE_NO_KEY))) {
            try {
                String s = request.getParameter(PAGE_NO_KEY);
                pageNo = Integer.parseInt(s);
                if (recordCount != 0 && pageNo <= 0) {
                    pageNo = Helper.DEFAULT_PAGE_NO;
                }
                else if (recordCount == 0) {
                    pageNo = 0;
                }
            } catch (Exception e) {
                log.error("error in createPageVo: ", e);
            }
        }
        if (StringUtils.isNotBlank(request.getParameter(PAGE_SIZE_KEY))) {
            try {
                String s = request.getParameter(PAGE_SIZE_KEY);
                pageSize = Integer.parseInt(s);
            } catch (Exception e) {
                log.error("error in createPageVo: ", e);
            }
        }

        //Fix bug: 先排序后翻页，排序条件丢失
        String orderKey = request.getParameter(ORDER_KEY);
        pageVo.setOrderKey(orderKey);

        pageVo.setRecordCount(recordCount);
        pageVo.setPageSize(pageSize);
        pageVo.setCurrentPage(pageNo);
        pageVo.rePageCount();

        savePageVo(pageVo, request);

        return pageVo;
    }

    public PageVo updatePageVo(PageVo pageVo, ServletRequest request) {
        int pageNo = Helper.DEFAULT_PAGE_NO;
        int pageSize = Helper.DEFAULT_PAGE_SIZE;

        if (request.getParameter(PAGE_NO_KEY) != null) {
            try {
                String s = request.getParameter(PAGE_NO_KEY);
                pageNo = Integer.parseInt(s);
            } catch (Exception e) {
                log.error("error in updatePageVo: ", e);
            }
            pageVo.setCurrentPage(pageNo);
        }

        if (request.getParameter(PAGE_SIZE_KEY) != null) {
            try {
                String s = request.getParameter(PAGE_SIZE_KEY);
                pageSize = Integer.parseInt(s);
            } catch (Exception e) {
                log.error("error in updatePageVo: ", e);
            }
            pageVo.setPageSize(pageSize);
            pageVo.rePageCount();

        }

        if (pageVo != null) savePageVo(pageVo, request);

        return pageVo;
    }

    public void saveOrderStr(String orderStr, ServletRequest request) {
        request.setAttribute(ORDER_KEY, orderStr);
    }

    public void savePageVo(PageVo pageVo, ServletRequest request) {
        request.setAttribute(PAGEVO_KEY, pageVo);
    }

    public String findOrderStr(ServletRequest request) {
        return request.getParameter(ORDER_KEY);
    }


}
