package venus.frames.util.exception.handler;

import venus.frames.base.action.IForward;
import venus.frames.base.action.IMappingCfg;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;

/**
 * Created with IntelliJ IDEA.
 * User: cj
 * Date: 13-9-29
 * Time: 上午11:23
 */
public class UdpStrutsExceptionHandler extends BaseGlobalExceptionHandler{
    public IForward service(IForward exforward,
                            Exception ex,
                            IMappingCfg mapping,
                            DefaultForm formInstance,
                            IRequest request,
                            IResponse response) {
        preHandleException(ex);

        return sendErrorMessage(request, ex);
    }
}
