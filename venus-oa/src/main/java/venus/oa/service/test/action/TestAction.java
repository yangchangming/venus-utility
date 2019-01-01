package venus.oa.service.test.action;

/**
 * 功能、用途、现存BUG:
 * 
 * @author 刘国华
 * @version 1.0.0
 * @see 需要参见的其它类
 * @since 1.0.0
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.oa.helper.AuHelper;
import venus.oa.helper.LoginHelper;
import venus.oa.service.test.bs.ITestBs;
import venus.oa.service.test.util.IConstants;
import venus.oa.service.test.vo.TestVo;
import venus.oa.util.VoHelperTools;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 团体类型维护ACTION
 *  
 */
@Controller
@RequestMapping("/test")
public class TestAction implements IConstants {

    /**
     * 得到BS对象
     * 
     * @return BS对象
     */
    public ITestBs getBS() {
        return (ITestBs) Helper.getBean(BS);
    }

    /**
     * 查询全部列
     * 
     * @param _request
     * @param response
     * @return
     */
    @RequestMapping("/queryAll")
    public String queryAll(HttpServletRequest _request, HttpServletResponse response) {
        IRequest request = (IRequest)new HttpRequest(_request);
        ITestBs bs = getBS();
        List tableList = new ArrayList(); //定义表名结果集
        String orderStr = Helper.findOrderStr(request);

        // select table_name from user_tables, user_tables 不存在, why?
        tableList = bs.queryAllTable(orderStr);

        String tablename = request.getParameter("tablename");
        if(tablename==null||"".equals(tablename)){
            tablename=((TestVo)tableList.get(0)).getName();
        }
        String strSql=QUERY_SQL+tablename;//+" order by id";//DAO查询的SQL
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount(AuHelper.filterRecordPrivInSQL(strSql, LoginHelper.getLoginVo((HttpServletRequest) request.getServletRequest())));
            pageVo = Helper.createPageVo(request, recordCount);
        }
        List list = new ArrayList(); //定义表名结果集
        List resultList = new ArrayList();//定义结果集
        list = bs.queryAll(pageVo.getCurrentPage(), pageVo.getPageSize(), AuHelper.filterRecordPrivInSQL(strSql, LoginHelper.getLoginVo((HttpServletRequest) request.getServletRequest())), tablename);
        //权限判断
        for (int i = 0; i < list.size(); i++) {
            Object o=list.get(i);
            AuHelper.filterFieldPrivInVo(o, tablename, (HttpServletRequest) request);
            resultList.add(o);
        }
        Helper.saveOrderStr(orderStr, request);
        Helper.savePageVo(pageVo, request);
        request.setAttribute(TABLE_NAME, tablename);
        request.setAttribute(PROJECT_VALUE, resultList);
        request.setAttribute(TABLE_VALUE, tableList);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
    }


    /**
     * 查询全部表
     * 
     * @param _request
     * @param response
     * @return
     */
    @RequestMapping("/queryAllTable")
    public String queryAllTable(HttpServletRequest _request, HttpServletResponse response) {
        ITestBs bs = getBS();
        IRequest request = (IRequest)new HttpRequest(_request);
        String orderStr = Helper.findOrderStr(request);

        List list = new ArrayList(); //定义结果集
        list = bs.queryAllTable(orderStr);
        Helper.saveOrderStr(orderStr, request);
        request.setAttribute(PROJECT_VALUE, list);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_QUERYALL_PAGE_KEY);
        // no usage found
        return FORWARD_QUERYALL_PAGE_KEY;
    }
}


