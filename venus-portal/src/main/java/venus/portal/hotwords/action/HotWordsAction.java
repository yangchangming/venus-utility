package venus.portal.hotwords.action;

import org.apache.commons.lang.StringUtils;
import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.util.Helper;
import venus.frames.mainframe.util.QueryBuilder;
import venus.frames.web.page.PageVo;
import venus.portal.helper.EwpJspHelper;
import venus.portal.helper.EwpVoHelper;
import venus.portal.hotwords.bs.IHotWordsBs;
import venus.portal.hotwords.model.HotWords;
import venus.portal.hotwords.util.IHotWordsConstants;
import venus.portal.util.BooleanConstants;
import venus.portal.util.CommonFieldConstants;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * @author chengliang
 */

public class HotWordsAction extends DefaultDispatchAction implements IHotWordsConstants {

    /**
     * 得到BS对象
     *
     * @return BS对象
     */
    public IHotWordsBs getBs() {
        return (IHotWordsBs) Helper.getBean(BS_KEY);  //得到BS对象,受事务控制
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
        HotWords vo = new HotWords();
        Helper.populate(vo, request);
        vo.setCreateDate(new Timestamp(System.currentTimeMillis()));
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
        HotWords bean = getBs().findById(request.getParameter(REQUEST_ID));
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
        HotWords bean = getBs().findById(request.getParameter(REQUEST_ID));
        Helper.populate(bean, request);
        bean.setModifyDate(new Timestamp(System.currentTimeMillis()));
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
        HotWords bean = getBs().findById(request.getParameter(REQUEST_ID));
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
        String backFlag = request.getParameter(CommonFieldConstants.BACKFLAG);
        StringBuffer queryCondition = new StringBuffer();
        if (isNotBlank(backFlag) && StringUtils.equals(backFlag, BooleanConstants.TRUE)) {
            request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, EwpVoHelper.getMapFromRequest((HttpServletRequest) request));
            queryCondition.append(getQueryCondition(request));
        }
        if (!EwpJspHelper.transctPageVo(request)) {
            EwpJspHelper.transctPageVo(request, 0, getBs().getRecordCount(queryCondition.toString()));
        }
        PageVo pageVo = Helper.findPageVo(request);
        String orderStr = Helper.findOrderStr(request);

        List<HotWords> beans = getBs().queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition.toString(), orderStr); // 按条件查询全部,带排序
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
        return new QueryBuilder() {
            public void andCondition() {
                conditions = new String[]{pushCondition(qca, "name")};
            }
        }.build();
    }
}
