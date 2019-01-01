package venus.portal.requests.action;

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
import venus.portal.requests.bs.IRequestsBs;
import venus.portal.requests.model.Requests;
import venus.portal.requests.util.IRequestsConstants;
import venus.portal.util.BooleanConstants;
import venus.portal.util.CommonFieldConstants;
import venus.portal.util.EnumTools;
import venus.portal.util.SqlHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.TreeMap;

import static org.apache.commons.lang.StringUtils.isNotBlank;

public class RequestsAction extends DefaultDispatchAction implements IRequestsConstants {
    public IRequestsBs getBs() {
        return (IRequestsBs) Helper.getBean(BS_KEY);
    }

    public IForward insert(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        Requests vo = new Requests();
        Helper.populate(vo, request);
        getBs().insert(vo);
        return request.findForward(FORWARD_TO_QUERY_ALL);
    }

    public IForward deleteMulti(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String[] ids = EwpJspHelper.getArrayFromRequest(request, REQUEST_IDS);
        getBs().deleteMulti(ids);
        return request.findForward(FORWARD_TO_QUERY_ALL);
    }

    public IForward find(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        Requests bean = getBs().findById(request.getParameter(REQUEST_ID));
        request.setAttribute(REQUEST_BEAN, bean);
        return request.findForward(FORWARD_UPDATE_PAGE);
    }

    public IForward update(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        Requests bean = getBs().findById(request.getParameter(REQUEST_ID));
        Helper.populate(bean, request);
        getBs().update(bean);
        return request.findForward(FORWARD_TO_QUERY_ALL);
    }

    public IForward queryAll(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        request.setAttribute(REQUEST_QUERY_CONDITION, "");
        simpleQuery(formBean, request, response);
        return request.findForward(FORWARD_LIST_PAGE);
    }

    public IForward detail(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        Requests bean = getBs().findById(request.getParameter(REQUEST_ID));
        TreeMap<String,String> wheretoKnowMap = EnumTools.getSortedEnumMap(EnumTools.WHERETOKNOW);
        bean.setReferer(wheretoKnowMap.get(bean.getReferer()));
        request.setAttribute(REQUEST_BEAN, bean);
        EwpJspHelper.transctPageVo(request);
        return request.findForward(FORWARD_DETAIL_PAGE);
    }

    public IForward simpleQuery(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String back_flag = request.getParameter(CommonFieldConstants.BACKFLAG);
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
        List<Requests> beans = getBs().queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition.toString(), orderStr);
        if (null != beans && beans.size() > 0) {
            TreeMap<String,String> wheretoKnowMap = EnumTools.getSortedEnumMap(EnumTools.WHERETOKNOW);
            for (Requests bean : beans) {
                bean.setReferer(wheretoKnowMap.get(bean.getReferer()));
            }
        }
        Helper.saveOrderStr(orderStr, request);
        request.setAttribute(REQUEST_BEANS, beans);
        return request.findForward(FORWARD_LIST_PAGE);
    }

    private String getQueryCondition(final IRequest qca) {

        String[] conditions = new String[] { SqlHelper.pushCondition(qca, Requests.FIRST_NAME), SqlHelper.pushCondition(qca, Requests.LAST_NAME),
                SqlHelper.pushCondition(qca, Requests.TITLE), SqlHelper.pushCondition(qca, Requests.COMPANY), SqlHelper.pushCondition(qca, Requests.EMAIL),
                SqlHelper.pushCondition(qca, Requests.PHONE), SqlHelper.pushCondition(qca, Requests.COUNTRY), SqlHelper.pushCondition(qca, Requests.WEBSITE),
                SqlHelper.pushCondition(qca, Requests.REFERER), SqlHelper.pushCondition(qca, Requests.COMMENTS) };

        return SqlHelper.build(conditions) ;
    }
}
