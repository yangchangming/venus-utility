package venus.portal.gbox.resource.option.action;

import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.util.Helper;
import venus.portal.cache.data.DataCache;
import venus.portal.gbox.resource.option.bs.IOptionBs;
import venus.portal.gbox.resource.option.util.IOptionConstants;
import venus.portal.gbox.resource.option.vo.OptionVo;
import venus.portal.gbox.util.DateTools;

public class OptionAction extends DefaultDispatchAction implements IOptionConstants {

    /**
     * 得到BS对象
     *
     * @return BS对象
     */
    public IOptionBs getBs() {
        return (IOptionBs) Helper.getBean(BS_KEY);  //得到BS对象,受事务控制
    }

    /**
     * 进入列表页面
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward  业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward queryAll(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        DataCache dataCache = (DataCache) Helper.getBean(DATACACHE_KEY);
        request.setAttribute(OPTION_DATA, dataCache.getSystemOption());

        return request.findForward(FORWARD_LIST_PAGE);
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
     * 从页面的表单获取单条记录id，并察看这条记录的详细信息
     *
     * @param formBean
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward detail(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        OptionVo vo = getBs().find(request.getParameter(REQUEST_ID));  //通过id获取vo
        request.setAttribute(REQUEST_BEAN, vo);  //把vo放入request
        return request.findForward(FORWARD_DETAIL_PAGE);
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
        String[] id = request.getParameterValues("id");
        String[] values = request.getParameterValues("values");
        OptionVo vo = new OptionVo();
        vo.setModifyDate(DateTools.getSysTimestamp());
        int sum = 0;
        for (int i = 0; i < id.length; i++) {
            vo.setValue(values[i]);
            vo.setId(id[i]);
            sum += getBs().update(vo);  //更新单条记录*/
        }
        request.setAttribute("UPDATE_COUNT", sum);
        return queryAll(formBean, request, response);
    }

}
