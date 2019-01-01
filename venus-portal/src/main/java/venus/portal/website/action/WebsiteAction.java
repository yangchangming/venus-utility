package venus.portal.website.action;

import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.i18n.util.LocaleHolder;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;
import venus.portal.doctype.bs.IDocTypeBS;
import venus.portal.helper.EwpJspHelper;
import venus.portal.util.BooleanConstants;
import venus.portal.util.FileSystemConstants;
import venus.portal.util.MultiLanguageConstants;
import venus.portal.util.SqlHelper;
import venus.portal.website.bs.IWebsiteBs;
import venus.portal.website.model.Website;
import venus.portal.website.util.IWebsiteConstants;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Locale;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static venus.portal.util.CommonFieldConstants.*;
import static venus.portal.util.IEwpToolsConstants.DEFAULT_FORWARD;
import static venus.portal.website.model.Website.*;

/**
 * 模板管理
 *
 * @author zhangrenyang
 * @date 2011-9-28
 */
public class WebsiteAction extends DefaultDispatchAction implements IWebsiteConstants {

    public IWebsiteBs getBs() {
        return (IWebsiteBs) Helper.getBean(BS_KEY);
    }

    public IDocTypeBS getDocTypeBs() {
        return (IDocTypeBS) Helper.getBean(DOCTYPE_BS_KEY);
    }

    /**
     * 从页面表单获取信息注入vo，并插入单条记录
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward  业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward insert(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        Website vo = new Website();
        Helper.populate(vo, request);
        getBs().insert(vo);
        if(vo.getDescription()==null){
            vo.setDescription("");
        }
        if(vo.getKeywords()==null){
            vo.setKeywords("");
        }
        return request.findForward(FORWARD_TO_QUERY_ALL);
    }

    /**
     * 从页面的表单获取多条记录id，并删除多条记录
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward  业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward deleteMulti(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String[] ids = EwpJspHelper.getArrayFromRequest(request, REQUEST_IDS);
        getBs().deleteMulti(ids);
        return request.findForward(FORWARD_TO_QUERY_ALL);
    }

    /**
     * 从页面的表单获取单条记录id，查出这条记录的值，并跳转到修改页面
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward  业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward find(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        Website bean = getBs().find(request.getParameter(REQUEST_ID));
        request.setAttribute(REQUEST_BEAN, bean);
        return request.findForward(FORWARD_UPDATE_PAGE);
    }

    /**
     * 从页面表单获取信息注入VO，并修改单条记录
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward  业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward update(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        Website bean = getBs().find(request.getParameter(REQUEST_ID));
        Helper.populate(bean, request);
        getBs().update(bean);

        String isModifyCode = request.getParameter("isModifyCode");
        if (isModifyCode != null && isModifyCode.equals(MODIFY_CODE)) {
            String olderCode = request.getParameter("older_code");
            String realPath = getServlet().getServletContext().getRealPath(FileSystemConstants.FILE_SYSTEM_SEPARATOR);
            File viewDir = new File(realPath + "/" + FileSystemConstants.VIEW_PATH + olderCode);
            if (viewDir.exists()) {
                File desDir = new File(realPath + "/" + FileSystemConstants.VIEW_PATH + bean.getWebsiteCode());
                FileUtils.moveDirectory(viewDir, desDir);
            }
        }

        return request.findForward(FORWARD_TO_QUERY_ALL);
    }

    /**
     * 查询全部记录，分页显示，支持页面上触发的后台排序
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward  业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward queryAll(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        request.setAttribute(REQUEST_QUERY_CONDITION, "");
        simpleQuery(formBean, request, response);
        return request.findForward(FORWARD_LIST_PAGE);
    }

    /**
     * 查询全部记录，分页显示，支持页面上触发的后台排序
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward  业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward queryReference(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String queryCondition = getQueryCondition(request);
        if (!EwpJspHelper.transctPageVo(request)) {
            EwpJspHelper.transctPageVo(request, 0, getBs().getRecordCount(queryCondition));
        }
        PageVo pageVo = Helper.findPageVo(request);
        String orderStr = Helper.findOrderStr(request);

        List beans = getBs().queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition, orderStr);

        Helper.saveOrderStr(orderStr, request);
        request.setAttribute(REQUEST_BEANS, beans);
        return request.findForward(FORWARD_REFERENCE_PAGE);
    }

    /**
     * 从页面的表单获取单条记录id，并察看这条记录的详细信息
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward  业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward detail(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        Website bean = getBs().find(request.getParameter(REQUEST_ID));
        request.setAttribute(REQUEST_BEAN, bean);
        EwpJspHelper.transctPageVo(request);
        return request.findForward(FORWARD_DETAIL_PAGE);
    }

    /**
     * 简单查询，分页显示，支持表单回写
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward  业务完成后的跳转对象
     * @throws Exception 抛出保存网站中出现的异常
     */
    public IForward simpleQuery(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String back_flag = request.getParameter(BACKFLAG);
        StringBuffer queryCondition = new StringBuffer();
        if (isNotBlank(back_flag) && StringUtils.equals(back_flag, BooleanConstants.TRUE)) {
            request.setAttribute(WEBSITE_NAME, request.getParameter(WEBSITE_NAME));
            request.setAttribute(DESCRIPTION, request.getParameter(DESCRIPTION));
            request.setAttribute(LANGUAGE, request.getParameter(LANGUAGE));
            queryCondition.append(getQueryCondition(request));
        }
        if (!EwpJspHelper.transctPageVo(request)) {
            EwpJspHelper.transctPageVo(request, 0, getBs().getRecordCount(queryCondition.toString()));
        }
        PageVo pageVo = Helper.findPageVo(request);
        String orderStr = Helper.findOrderStr(request);

        List beans = getBs().queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition.toString(), orderStr); // 按条件查询全部,带排序
        Helper.saveOrderStr(orderStr, request);
        request.setAttribute(REQUEST_BEANS, beans);
        return request.findForward(FORWARD_LIST_PAGE);
    }

    /**
     * 设置默认站点，分页显示，支持表单回写
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward  业务完成后的跳转对象
     * @throws Exception 抛出保存网站中出现的异常
     */
    public IForward updateDefaultWebsite(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        getBs().updateDefaultWebsite(request.getParameter(REQUEST_ID));
        simpleQuery(formBean, request, response);
        return request.findForward(FORWARD_LIST_PAGE);
    }

    /**
     * 选择站点或者切换站点时更新会话中的站点信息
     *
     * @param website
     * @param hRequest
     * @throws
     */
    private void updateSiteInSession(Website website, HttpServletRequest hRequest) throws Exception {
        if (website != null) {
            hRequest.getSession().setAttribute(SITE_ID, website.getId());
            hRequest.getSession().setAttribute(SITE_CODE, website.getWebsiteCode());
            hRequest.getSession().setAttribute(SITE_NAME, website.getWebsiteName());
            hRequest.getSession().setAttribute(LocaleHolder.LOCAL_IN_SESSION_KEY, website.getLanguage());
            //当zh_CN格式时需要拆分。
            if (website.getLanguage().trim().length() == 5) {
                LocaleHolder.setLocale(new Locale(website.getLanguage().substring(0, 2), website.getLanguage().substring(3, 5)));
            }
        }
        // 更换session中的语言
    }

    /**
     * 更改当前站点
     *
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward changeCurrentSite(DefaultForm from, IRequest request, IResponse response) throws Exception {
        HttpServletRequest hRequest = (HttpServletRequest) request.getServletRequest();
        String site_id = hRequest.getParameter(SITE_ID);
        String isPass = BooleanConstants.YES;
        try {
            updateSiteInSession(getBs().find(site_id), hRequest);
        } catch (Exception ex) {
            isPass = BooleanConstants.NO;
        } finally {
            response.getServletResponse().setContentType("text/plain; charset=UTF-8");
            final String returnValue = "{isPass:\"" + isPass + "\"}";
            response.getServletResponse().getWriter().print(returnValue);
            response.getServletResponse().getWriter().flush();
            return DEFAULT_FORWARD;
        }
    }

    /**
     * 获取所有站点信息
     *
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward getSiteAll(DefaultForm from, IRequest request, IResponse response) throws Exception {
        HttpServletRequest hRequest = (HttpServletRequest) request.getServletRequest();
        String pType = hRequest.getParameter("pType");
        String docTypeIds = hRequest.getParameter("docTypeIds");
        String websitId = hRequest.getParameter("websitId");

        //取出所有的站点
        IWebsiteBs websiteBs = getBs();
        List<Website> allWebsite = websiteBs.queryAll();
        JSONObject jsonO = new JSONObject();
        for (Website ws : allWebsite) {
            jsonO.put(ws.getId(), ws.getWebsiteName());
        }
        response.getServletResponse().setContentType("text/plain; charset=UTF-8");
        String returnValue = jsonO.toString();
        response.getServletResponse().getWriter().print(returnValue);
        response.getServletResponse().getWriter().flush();

        return DEFAULT_FORWARD;
    }

    /**
     * 从request获得查询条件并拼装成查询的SQL语句
     *
     * @param qca 用来取查询条件的request对象
     * @return 查询的SQL语句
     */
    private String getQueryCondition(final IRequest qca) {
        String[] conditions = new String[]{SqlHelper.pushCondition(qca, WEBSITE_NAME),
                SqlHelper.pushCondition(qca, DESCRIPTION), SqlHelper.pushCondition(qca, LANGUAGE)};
        return SqlHelper.build(conditions);
    }

    public IForward checkDocTypeNameIsUniqueByCurrentWebSite(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String returnMessage = "";
        String isPass = BooleanConstants.YES;
        if (getDocTypeBs().checkDocTypeNameIsUniqueByWebSite(request.getParameter(WEBSITE_ID))) {
            isPass = BooleanConstants.NO;
            returnMessage = LocaleHolder.getMessage(MultiLanguageConstants.UDP_EWP_WEBSITE_NAMEISUNIQUE_NO);
            ;
        } else {
            isPass = BooleanConstants.YES;
            returnMessage = "";
        }
        response.getServletResponse().setContentType("text/plain; charset=UTF-8");
        String returnValue = "{isPass:\"" + isPass + "\",returnMessage:\"" + returnMessage + "\"}";
        response.getServletResponse().getWriter().print(returnValue);
        response.getServletResponse().getWriter().flush();
        return DEFAULT_FORWARD;
    }

    public IForward checkWebsiteCodeIsUnique(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String webSiteCode = request.getParameter(WAITING_FOR_CHECK_CODE);
        String returnMessage = "";
        String isPass = BooleanConstants.YES;
        if (getBs().checkWebsiteCodeIsUnique(webSiteCode)) {
            isPass = BooleanConstants.YES;
            returnMessage = LocaleHolder.getMessage(MultiLanguageConstants.UDP_EWP_WEBSITE_CODE_HAS_NOEXIST);
        } else {
            isPass = BooleanConstants.NO;
            returnMessage = LocaleHolder.getMessage(MultiLanguageConstants.UDP_EWP_WEBSITE_CODE_HAS_EXIST);
        }
        response.getServletResponse().setContentType("text/plain; charset=UTF-8");
        String returnValue = "{isPass:\"" + isPass + "\",returnMessage:\"" + returnMessage + "\"}";
        response.getServletResponse().getWriter().print(returnValue);
        response.getServletResponse().getWriter().flush();
        return DEFAULT_FORWARD;
    }

    public IForward checkOnlyDefaultWebsite(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String id = request.getParameter(ID);
        String returnMessage = null;
        String isPass = BooleanConstants.YES;
        String queryCondition = ISDEFAULT + "='" + BooleanConstants.YES + "' ";
        if (!StringUtils.isBlank(id)) {
            queryCondition += " and " + ID + " != '" + id + "'";
        }
        int count = getBs().getRecordCount(queryCondition);
        if (count >= 1) {
            isPass = BooleanConstants.NO;
            returnMessage = LocaleHolder.getMessage(MultiLanguageConstants.UDP_EWP_WEBSITE_ONLYONEDEFAULT);
            ;
        } else {
            isPass = BooleanConstants.YES;
            returnMessage = "";
        }
        response.getServletResponse().setContentType("text/plain; charset=UTF-8");
        String returnValue = "{isPass:\"" + isPass + "\",returnMessage:\"" + returnMessage + "\"}";
        response.getServletResponse().getWriter().print(returnValue);
        response.getServletResponse().getWriter().flush();
        return DEFAULT_FORWARD;
    }


    public IForward isDefaultWebsite(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String id = request.getParameter(ID);
        String returnMessage = null;
        String isPass = BooleanConstants.YES;
        String queryCondition = ISDEFAULT + "='" + BooleanConstants.YES + "' ";
        if (!StringUtils.isBlank(id)) {
            queryCondition += " and " + ID + " = '" + id + "'";
        }
        int count = getBs().getRecordCount(queryCondition);
        if (count > 0) {
            isPass = BooleanConstants.YES;
        } else {
            isPass = BooleanConstants.NO;
        }
        response.getServletResponse().setContentType("text/plain; charset=UTF-8");
        String returnValue = "{isPass:\"" + isPass + "\"}";
        response.getServletResponse().getWriter().print(returnValue);
        response.getServletResponse().getWriter().flush();
        return DEFAULT_FORWARD;
    }

}
