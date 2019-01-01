package venus.portal.gbox.resource.resourcetype.action;

import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.util.Helper;
import venus.frames.mainframe.util.VoHelper;
import venus.frames.web.message.MessageAgent;
import venus.frames.web.message.MessageStyle;
import venus.frames.web.page.PageVo;
import venus.portal.gbox.resource.resourcetype.bs.IResourceTypeBs;
import venus.portal.gbox.resource.resourcetype.util.IResourceTypeConstants;
import venus.portal.gbox.resource.resourcetype.vo.ResourceTypeVo;
import venus.portal.gbox.util.DateTools;
import venus.portal.gbox.util.NumericalTools;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


public class ResourceTypeAction extends DefaultDispatchAction implements IResourceTypeConstants {

    /**
     * 得到BS对象
     * 
     * @return BS对象
     */
    public IResourceTypeBs getBs() {
        return (IResourceTypeBs) Helper.getBean(BS_KEY);  //得到BS对象,受事务控制
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
        ResourceTypeVo vo = new ResourceTypeVo();
        if (!Helper.populate(vo, request)) {
            return MessageAgent.sendErrorMessage(request,
                    DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
        }         
        vo.setSingleMaximum(String.valueOf(NumericalTools.MBToLong(vo.getSingleMaximum())));
        vo.setTotalMaximum((String.valueOf(NumericalTools.MBToLong(vo.getTotalMaximum()))));
        vo.setCreateDate(DateTools.getSysTimestamp());
        getBs().insert(vo);  //插入单条记录
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
        String[] id = request.getParameterValues("ids");  //从request获取多条记录id
        if (id != null && id.length != 0) {
            getBs().delete(id);  //删除多条记录
        }
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
        detail(formBean, request, response);
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
        ResourceTypeVo vo = new ResourceTypeVo();
        if (!Helper.populate(vo, request)) {
            return MessageAgent.sendErrorMessage(request,
                    DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
        }        
        vo.setSingleMaximum(String.valueOf(NumericalTools.MBToLong(vo.getSingleMaximum())));
        vo.setTotalMaximum((String.valueOf(NumericalTools.MBToLong(vo.getTotalMaximum()))));
        vo.setModifyDate(DateTools.getSysTimestamp());
        getBs().update(vo);  //更新单条记录
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
        ResourceTypeVo vo = getBs().find(request.getParameter(REQUEST_ID));  //通过id获取vo
        request.setAttribute(REQUEST_BEAN, vo);  //把vo放入request
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
        String name = request.getParameter("typeName");
        String relevanceFormat = request.getParameter("relFormat");
        ResourceTypeVo vo = new ResourceTypeVo();
        if (!Helper.populate(vo, request)) {
            return MessageAgent.sendErrorMessage(request,
                    DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
        }
        String queryCondition = "";
        if (name != null && !"".equals(name))
            queryCondition += " AND NAME LIKE '%" + name + "%'";        
        if (relevanceFormat != null && !"".equals(relevanceFormat))
            queryCondition += " AND RELEVANCE_FORMAT LIKE '%" + relevanceFormat + "%'";         
        PageVo pageVo = Helper.findPageVo(request);  //得到当前翻页信息
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = getBs().getRecordCount(queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }        
        String orderStr = Helper.findOrderStr(request);  //得到排序信息

        List beans = new ArrayList();
        beans = getBs().queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition, orderStr);  //按条件查询全部,带排序

        Helper.saveOrderStr(orderStr, request);  //保存排序信息
        Helper.savePageVo(pageVo, request);
        request.setAttribute(REQUEST_BEANS, beans);  //把结果集放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelper.getMapFromRequest((HttpServletRequest) request)); //回写表单
        return request.findForward(FORWARD_LIST_PAGE);
    }

}
