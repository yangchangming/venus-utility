/*
 * 创建日期 2007-3-19
 * CreateBy zhangbaoyu
 */
package venus.cron.action;

import venus.frames.base.action.BaseAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.i18n.util.LocaleHolder;
import venus.frames.mainframe.log.ILog;
import venus.frames.mainframe.log.LogMgr;

/**
 * @author zhangbaoyu
 *  
 */
public class CheckJobDefineAction extends BaseAction {
    private static ILog log = LogMgr.getLogger(CheckJobDefineAction.class);

    public IForward service(DefaultForm form, IRequest request,
                            IResponse response) throws Exception {

        String className = request.getParameter("className");
        String errorInfo = "";
        //验证类是否存在；
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            errorInfo= LocaleHolder.getMessage("udp.quartz.Class_Not_Found", new Object[]{className});
            log.info(errorInfo);
        }catch(Exception e){
            errorInfo= LocaleHolder.getMessage("udp.quartz.Class_Not_Found", new Object[]{className});
            log.info(errorInfo);
        }
        request.setAttribute("errorInfo", errorInfo);
        return request.findForward("errorInfo");
    }
}