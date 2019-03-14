package venus.oa.authority.auproxy.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;
import venus.oa.authority.auproxy.bs.IProxyHistoryBs;
import venus.oa.authority.auproxy.util.IConstants;
import venus.oa.helper.LoginHelper;
import venus.oa.util.QueryBuilder;
import venus.oa.util.VoHelperTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
@Controller
@RequestMapping("/auProxyHistory")
public class AuProxyHistoryAction implements IConstants {
    
    @Autowired
    private IProxyHistoryBs proxyHistoryBs;
    

    /**
     * 简单查询，分页显示，支持表单回写
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/simpleQuery")
    public String simpleQuery(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        IProxyHistoryBs bs = proxyHistoryBs;
        String queryCondition = queryCondition(request);
        //queryCondition = AuHelper.filterOrgPrivInSQL(queryCondition, "B.CODE", (HttpServletRequest) request.getServletRequest());//控制数据权限
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount(queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        List beans = bs.queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition," OPERATER_DATE DESC");  //按条件查询
        request.setAttribute(REQUEST_BEANS_VALUE, beans);  //把结果集放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request));  //回写表单
//        return request.findForward(request.getParameter("forwardPage"));
        String forward = request.getParameter("forwardPage");

        if (forward!=null && !"".equals(forward)){
            if ("listPage".equals(forward)){
                return listPage;
            }else if ("recipient".equals(forward)){
                return recipient;
            }else if ("sponsor".equals(forward)){
                return sponsor;
            }else if ("detailPage".equals(forward)) {
                return detailPage;
            }else {
                return listPage;
            }
        }else {
            return listPage;
        }
    }


    /**
     * 简单查询，分页显示，支持表单回写
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryAll")
    public String queryAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return simpleQuery(request,response);
    }

    
    /**
     * 从request获得查询条件并拼装成查询的SQL语句
     * @param request
     * @return 查询的SQL语句
     */
    private String queryCondition(final IRequest request){
        final String forwardPage = request.getParameter("forwardPage");
        return new QueryBuilder(){
            @Override
            public void andCondition() {
                conditions = new String[]{
                        "sponsor".equals(forwardPage)?"sponsor_id ='"+ LoginHelper.getPartyId((HttpServletRequest) request)+"'":pushCondition(request,"sponsor"),
                                "recipient".equals(forwardPage)?"recipient_id ='"+ LoginHelper.getPartyId((HttpServletRequest) request)+"'":pushCondition(request,"recipient"),
                                pushCondition(request,"proxy"),
                                pushCondition(request,"operater_type", "='", "'"),
                };
            }            
        }.build();    
    }
}

