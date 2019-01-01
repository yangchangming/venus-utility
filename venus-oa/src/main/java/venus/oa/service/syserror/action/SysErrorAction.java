package venus.oa.service.syserror.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.oa.service.syserror.bs.ISysErrorBs;
import venus.oa.service.syserror.util.IContants;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author zangjian
 *
 */
@Controller
@RequestMapping("/sysError")
public class SysErrorAction implements IContants {

    public ISysErrorBs getBs() {
        return (ISysErrorBs) Helper.getBean(IContants.BS_KEY);
    }
    
    /**
     * 查询全部记录，分页显示
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryAll")
    public String queryAll(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        String errorType = request.getParameter("errorType");
       String queryCondition = "";
       if (errorType != null && errorType.length() > 0)
           queryCondition += "ERROR_TYPE = '" + errorType + "'";

       PageVo pageVo = Helper.findPageVo(request);
       if (pageVo != null) {
           pageVo = Helper.updatePageVo(pageVo, request);
       } else {
           int recordCount = getBs().getRecordCount(queryCondition);
           pageVo = Helper.createPageVo(request, recordCount);
       }
       List sysErrorList = getBs().queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition);
        Helper.savePageVo(pageVo, request);
        request.setAttribute(PROJECT_VALUE, sysErrorList);
//       return request.findForward(FORWARD_SYSERROR_LIST);
        return FORWARD_SYSERROR_LIST;
    }
    
}

