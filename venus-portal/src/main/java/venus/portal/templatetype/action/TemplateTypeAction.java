package venus.portal.templatetype.action;

import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.util.Helper;
import venus.portal.helper.EwpJspHelper;
import venus.portal.helper.EwpVoHelper;
import venus.portal.templatetype.bs.ITemplateTypeBs;
import venus.portal.templatetype.model.TemplateType;
import venus.portal.templatetype.util.ITemplateTypeConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author zhaoyapeng
 *
 */
public class TemplateTypeAction extends DefaultDispatchAction implements ITemplateTypeConstants {

    private ITemplateTypeBs getTemplateTypeBs(){
        return (ITemplateTypeBs) Helper.getBean(BS_KEY);
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
    public IForward queryAll(DefaultForm formBean, IRequest request,
            IResponse response) throws Exception {
        List<TemplateType> beans = this.getTemplateTypeBs().findAllTemplateType();
        request.setAttribute(REQUEST_BEANS, beans); // 把结果集放入request
        if ("true".equals(request.getParameter("backFlag"))) {
            request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, EwpVoHelper
                    .getMapFromRequest((HttpServletRequest) request)); // 回写表单
        }
        return request.findForward(FORWARD_LIST_PAGE);
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
    public IForward insert(DefaultForm formBean, IRequest request,
            IResponse response) throws Exception {
       TemplateType vo = new TemplateType();
        Helper.populate(vo, request);
        vo.setCreateTime(new Date());
        EwpVoHelper.markCreateStamp(request, vo); // 打创建时间,IP戳
        getTemplateTypeBs().saveTemplateType(vo);
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
    public IForward find(DefaultForm formBean, IRequest request,
            IResponse response) throws Exception {
        String templateTypeId =request.getParameter(REQUEST_ID);
        TemplateType bean = getTemplateTypeBs().findTemplateType(templateTypeId); // 通过id获取vo
        request.setAttribute(REQUEST_BEAN, bean); // 把vo放入request
        return request.findForward(FORWARD_UPDATE_PAGE);
    }
    
    /**
     * 从页面表单获取信息注入VO，并修改单条记录
     * 
     * @param formBean
     *            前台封装的 formBean
     * @param request
     *            请求
     * @param response
     *            响应
     * @return IForward 跳转对象
     * @throws Exception
     */
    public IForward update(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String templateTypeId = request.getParameter(REQUEST_ID);
        TemplateType bean = getTemplateTypeBs().findTemplateType(templateTypeId); // 通过id获取vo
        Helper.populate(bean, request); // 从request中注值进去vo
        getTemplateTypeBs().updateTemplateType(bean); // 更新单条记录
        return request.findForward(FORWARD_TO_QUERY_ALL);
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
    public IForward detail(DefaultForm formBean, IRequest request,
            IResponse response) throws Exception {
        String templateTypeId = request.getParameter(REQUEST_ID);
        TemplateType bean = getTemplateTypeBs().findTemplateType(templateTypeId); // 通过id获取vo
        request.setAttribute(REQUEST_BEAN, bean); // 把vo放入request
        return request.findForward(FORWARD_DETAIL_PAGE);
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
    public IForward deleteTemplateTypes(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String[] ids = EwpJspHelper.getArrayFromRequest(request, REQUEST_IDS);
        if (ids != null && ids.length != 0) {
            TemplateType templateType=null;
            for(String id : ids){
                getTemplateTypeBs().deleteTemplateType(id);
            }
        }
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
    public IForward queryReference(DefaultForm formBean, IRequest request,
            IResponse response) throws Exception {
        List beans = getTemplateTypeBs().findAllTemplateType();
        request.setAttribute(REQUEST_BEANS, beans); // 把结果集放入request
        if ("true".equals(request.getParameter("backFlag"))) {
            request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, EwpVoHelper
                    .getMapFromRequest((HttpServletRequest) request)); // 回写表单
        }
        return request.findForward(FORWARD_REFERENCE_PAGE);
    }

    /**
     * 从旧环境中导入数据
     */
    public IForward importData(DefaultForm from, IRequest request, IResponse response) throws Exception {
        getTemplateTypeBs().insertData();
        return null;
    }
    
}
