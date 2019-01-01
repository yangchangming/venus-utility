/*
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */

package venus.portal.link.action;

import org.apache.commons.lang.StringUtils;
import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;
import venus.portal.helper.EwpJspHelper;
import venus.portal.helper.EwpVoHelper;
import venus.portal.link.bs.ILinkBs;
import venus.portal.link.model.Link;
import venus.portal.link.util.ILinkConstants;
import venus.portal.util.BooleanConstants;
import venus.portal.util.EnumTools;
import venus.portal.util.SqlHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.TreeMap;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static venus.portal.util.CommonFieldConstants.BACKFLAG;
import static venus.portal.util.EnumTools.LINKCATEGORY;


/**
 * @author zhangrenyang
 */

public class LinkAction extends DefaultDispatchAction implements ILinkConstants {

    TreeMap<String, String> linkCategoryMap = EnumTools.getSortedEnumMap(LINKCATEGORY);

    /**
     * 得到BS对象
     *
     * @return BS对象
     */
    public ILinkBs getBs() {
        return (ILinkBs) Helper.getBean(BS_KEY);
    }

    /**
     * 从页面表单获取信息注入vo，并插入单条记录
     *
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward insert(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        Link vo = new Link();
        Helper.populate(vo, request);
        getBs().insert(vo);
        return request.findForward(FORWARD_TO_QUERY_ALL);
    }

    /**
     * 从页面的表单获取多条记录id，并删除多条记录
     *
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward deleteMulti(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String[] ids = EwpJspHelper.getArrayFromRequest(request, REQUEST_IDS);
        getBs().deleteMulti(ids);
        return request.findForward(FORWARD_TO_QUERY_ALL);
    }

    /**
     * 从页面的表单获取单条记录id，查出这条记录的值，并跳转到修改页面
     *
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward find(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        Link bean = getBs().findById(request.getParameter(REQUEST_ID));
        request.setAttribute(REQUEST_BEAN, bean);
        return request.findForward(FORWARD_UPDATE_PAGE);
    }

    /**
     * 从页面表单获取信息注入vo，并修改单条记录
     *
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward update(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        Link bean = getBs().findById(request.getParameter(REQUEST_ID));
        Helper.populate(bean, request);
        getBs().update(bean);
        return request.findForward(FORWARD_TO_QUERY_ALL);
    }

    /**
     * 查询全部记录，分页显示，支持页面上触发的后台排序
     *
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward queryAll(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        request.setAttribute(REQUEST_QUERY_CONDITION, "");
        simpleQuery(formBean, request, response);
        return request.findForward(FORWARD_LIST_PAGE);
    }

    /**
     * 从页面的表单获取单条记录id，并察看这条记录的详细信息
     *
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward detail(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        Link bean = getBs().findById(request.getParameter(REQUEST_ID));
        bean.setCategory(linkCategoryMap.get(bean.getCategory()));
        request.setAttribute(REQUEST_BEAN, bean);
        EwpJspHelper.transctPageVo(request);
        return request.findForward(FORWARD_DETAIL_PAGE);
    }

    /**
     * 简单查询，分页显示，支持表单回写
     *
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward simpleQuery(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String back_flag = request.getParameter(BACKFLAG);
        StringBuffer queryCondition = new StringBuffer();
        if (isNotBlank(back_flag) && StringUtils.equals(back_flag, BooleanConstants.TRUE)) {
            request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, EwpVoHelper.getMapFromRequest((HttpServletRequest) request));
            queryCondition.append(getQueryCondition(request));
        }
        if (!EwpJspHelper.transctPageVo(request)) {
            EwpJspHelper.transctPageVo(request, 0, getBs().getRecordCount(queryCondition.toString()));
        }
        PageVo pageVo = Helper.findPageVo(request);
        String orderStr = Helper.findOrderStr(request);

        List<Link> beans = getBs().queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition.toString(), orderStr);
        if (null != beans && beans.size() > 0) {
            for (Link bean : beans) {
                bean.setCategory(linkCategoryMap.get(bean.getCategory()));
            }
        }
        Helper.saveOrderStr(orderStr, request);
        request.setAttribute(REQUEST_BEANS, beans);
        return request.findForward(FORWARD_LIST_PAGE);
    }

    /**
     * 从request获得查询条件并拼装成查询的SQL语句
     *
     * @param qca
     * @return 查询的SQL语句
     */
    private String getQueryCondition(final IRequest qca) {

        String[] conditions = new String[]{SqlHelper.pushCondition(qca, Link.ID),
                SqlHelper.pushCondition(qca, Link.CATEGORY),
                SqlHelper.pushCondition(qca, Link.TITLE),
                SqlHelper.pushCondition(qca, Link.CONTENT),
                SqlHelper.pushCondition(qca, Link.WEBSITE),
                SqlHelper.pushCondition(qca, Link.VERSION),
        };

        return SqlHelper.build(conditions) ;
    }
}
