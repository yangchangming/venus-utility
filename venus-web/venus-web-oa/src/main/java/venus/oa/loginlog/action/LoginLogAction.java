/*
 * 系统名称:单表模板 --> sample
 * 
 * 文件名称: venus.authority.login.loginlog.action --> LoginLogAction.java
 * 
 * 功能描述:
 * 
 * 版本历史: 2007-10-16 10:29:59.005 创建1.0.0版 (甘硕)
 *  
 */

package venus.oa.loginlog.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;
import venus.oa.loginlog.bs.ILoginLogBs;
import venus.oa.loginlog.util.ILoginLogConstants;
import venus.oa.loginlog.vo.LoginLogVo;
import venus.oa.util.DateTools;
import venus.oa.util.QueryBuilder;
import venus.oa.util.SqlBuilder;
import venus.oa.util.VoHelperTools;
import venus.oa.util.sql.OrgPrivilege;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 功能、用途、现存BUG:
 * 
 * @author 甘硕
 * @version 1.0.0
 * @since 1.0.0
 */

@Controller
@RequestMapping("/loginLog")
public class LoginLogAction implements ILoginLogConstants {

    @Autowired
    private ILoginLogBs loginLogBs;

    /**
     * 从页面表单获取信息注入vo，并插入单条记录
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/insert")
    public String insert(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        LoginLogVo vo = new LoginLogVo();
        VoHelperTools.populate(vo, request);  //从request中注值进去vo
        loginLogBs.insert(vo);  //插入单条记录
//        return request.findForward(FORWARD_TO_QUERY_ALL);
        return "redirect:/loginLog/queryAll";
    }

    /**
     * 从页面的表单获取单条记录id，并删除单条记录
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        loginLogBs.delete(request.getParameter(REQUEST_ID));  //删除单条记录
//        return request.findForward(FORWARD_TO_QUERY_ALL);
        return "redirect:/loginLog/queryAll";
    }

    /**
     * 从页面的表单获取多条记录id，并删除多条记录
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteMulti")
    public String deleteMulti(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String[] id = request.getParameter(REQUEST_IDS).split(","); //从request获取多条记录id
        //int deleteCount = 0;  //定义成功删除的记录数
        if (id != null && id.length != 0) {
            //deleteCount = 
            loginLogBs.delete(id);  //删除多条记录
        }
//        return request.findForward(FORWARD_TO_QUERY_ALL);
        return "redirect:/loginLog/queryAll";
    }

    /**
     * 删除列表中全部记录
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/deleteAll")
    public String deleteAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //int deleteCount = 
        loginLogBs.deleteAll();
//        return request.findForward(FORWARD_TO_QUERY_ALL);
        return "redirect:/loginLog/queryAll";
    }    
    
    /**
     * 从页面的表单获取单条记录id，查出这条记录的值，并跳转到修改页面
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/find")
    public String find(HttpServletRequest request, HttpServletResponse response) throws Exception {
        detail(request, response);
//        return request.findForward(FORWARD_UPDATE_PAGE);
        return FORWARD_UPDATE_PAGE;
    }

    /**
     * 从页面表单获取信息注入vo，并修改单条记录
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/update")
    public String update(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        LoginLogVo vo = new LoginLogVo();
        IRequest request = (IRequest)new HttpRequest(_request);
        VoHelperTools.populate(vo, request);  //从request中注值进去vo
        loginLogBs.update(vo);  //更新单条记录
//        return request.findForward(FORWARD_TO_QUERY_ALL);
        return "redirect:/loginLog/queryAll";
    }

    /**
     * 查询全部记录，分页显示，支持页面上触发的后台排序
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryAll")
    public String queryAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //request.setAttribute(REQUEST_QUERY_CONDITION, "");
        simpleQuery(request, response);
//        return request.findForward(FORWARD_LIST_PAGE);
        return FORWARD_LIST_PAGE;
    }

    /**
     * 从页面的表单获取单条记录id，并察看这条记录的详细信息
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/detail")
    public String detail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginLogVo bean = loginLogBs.find(request.getParameter(REQUEST_ID));  //通过id获取vo
        request.setAttribute(REQUEST_BEAN, bean);  //把vo放入request
//        return request.findForward(FORWARD_DETAIL_PAGE);
        return FORWARD_DETAIL_PAGE;
    }

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
        ILoginLogBs bs = loginLogBs;
        IRequest request = (IRequest)new HttpRequest(_request);
        String login_id = request.getParameter("login_id");
        String name = request.getParameter("name");
        String login_ip = request.getParameter("login_ip");
        String login_time_from = request.getParameter("login_time_from");
        String login_time_to = request.getParameter("login_time_to");
        SqlBuilder sql=new SqlBuilder();
        if(login_id!=null&&login_id.length()>0)
			sql.and(sql.like("A.LOGIN_ID",login_id));
		if(name!=null&&name.length()>0)
			sql.and(sql.like("A.NAME",name));
		if(login_ip!=null&&login_ip.length()>0)
			sql.and(sql.like("A.LOGIN_IP",login_ip));
		if(login_time_from!=null&&login_time_from.length()>0)
			sql.and(sql.greateOrEqual("A.LOGIN_TIME", DateTools.getTimestamp(login_time_from+" 00:00:00.0")));
		if(login_time_to!=null&&login_time_to.length()>0)
			sql.and(sql.lessOrEqual("A.LOGIN_TIME", DateTools.getTimestamp(login_time_to+" 23:59:59.999")));
		//数据权限
		OrgPrivilege nowPrivilege = (OrgPrivilege)sql.otherMethod("historyLogOrgPrivilege",new Object[]{});
		nowPrivilege.setFieldName("B.CODE");
		nowPrivilege.setRequest((HttpServletRequest) request.getServletRequest());
		sql.and(nowPrivilege);
        PageVo pageVo = Helper.findPageVo(request); //得到当前翻页信息
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount(sql);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        sql.setCountBegin((pageVo.getCurrentPage()-1)*pageVo.getPageSize());//设置分页起始页
		sql.setCountEnd(pageVo.getPageSize());//设置分页结束页
        List beans = bs.queryByCondition(sql);  //按条件查询全部
         
        request.setAttribute(REQUEST_BEANS, beans);  //把结果集放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request));  //回写表单
//        return request.findForward(FORWARD_LIST_PAGE);
        return FORWARD_LIST_PAGE;
    }
    
    /**
     * （虽然废弃，但保留历史供再次开发时参考。）
     * 从request获得查询条件并拼装成查询的SQL语句
     * @param qca
     * @return 查询的SQL语句
     */
    protected String queryCondition(final IRequest qca){
        return new QueryBuilder(){
            @Override
            public void andCondition() {
                conditions = new String[]{
                        pushCondition(qca,"login_id"),
                        pushCondition(qca,"name"),
                        pushCondition(qca,"login_ip"),
                        //如果是Oracle,用下边的语句
                        pushCondition(qca,"login_time_from", ">=to_date('" ,"','YYYY-MM-DD')", "login_time"),
                        pushCondition(qca,"login_time_to", "<=to_date('", "','YYYY-MM-DD')", "login_time"),
                        //本写法适用于Microsoft SQL Server, IBM DB2或MySQL等
                        /*
                        pushCondition(qca,"login_time_from", ">='" ," 00:00:00'", "login_time"),
                        pushCondition(qca,"login_time_to", "<='", " 23:59:59'", "login_time"),
                        */
                };
            }            
        }.build();    	
    }
}

