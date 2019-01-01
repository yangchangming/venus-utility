package venus.portal.gbox.resource.tag.action;

import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.util.Helper;
import venus.portal.gbox.resource.tag.bs.ITagBs;
import venus.portal.gbox.resource.tag.util.ITagConstants;

public class TagAction extends DefaultDispatchAction implements ITagConstants {

    /**
     * 得到BS对象
     * 
     * @return BS对象
     */
    public ITagBs getBs() {
        return (ITagBs) Helper.getBean(BS_KEY);  //得到BS对象,受事务控制
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
        return null;
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
        return null;
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
        return null;
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
        return null;
    }
    
}
