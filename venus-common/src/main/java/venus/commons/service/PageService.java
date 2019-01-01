package venus.commons.service;


import venus.frames.web.page.PageVo;

import javax.servlet.ServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: ethan
 * Date: 13-9-21
 * Time: 上午11:59
 * To change this template use File | Settings | File Templates.
 */
public interface PageService {

    /**
     *
     * @param request
     * @return
     */
    PageVo findPageVo(ServletRequest request);

    PageVo createPageVo(ServletRequest request, int recordCount);

    PageVo updatePageVo(PageVo pageVo, ServletRequest request);

    void saveOrderStr(String orderStr, ServletRequest request);

    void savePageVo(PageVo pageVo, ServletRequest request);

    String findOrderStr(ServletRequest request);
}
