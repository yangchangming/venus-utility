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

/**
 * @author zhangbaoyu
 */
public class QuartzLogAction extends BaseAction {
//    private static ILog log = LogMgr.getLogger(QuartzLogAction.class);

    public IForward service(DefaultForm form, IRequest request,
                            IResponse response) throws Exception {
        return request.findForward("quartzLog");
    }
}
