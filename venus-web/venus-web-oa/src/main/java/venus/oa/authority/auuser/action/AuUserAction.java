package venus.oa.authority.auuser.action;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.oa.authority.auuser.bs.IAuUserBs;
import venus.oa.authority.auuser.util.IAuUserConstants;
import venus.oa.authority.auuser.vo.AuUserVo;
import venus.oa.helper.AuHelper;
import venus.oa.helper.LoginHelper;
import venus.oa.profile.model.UserProfileModel;
import venus.oa.util.DateTools;
import venus.oa.util.Encode;
import venus.oa.util.VoHelperTools;
import venus.commons.xmlenum.EnumRepository;
import venus.commons.xmlenum.EnumValueMap;
import venus.frames.base.action.IRequest;
import venus.frames.mainframe.action.HttpRequest;
import venus.frames.mainframe.util.Helper;
import venus.frames.web.page.PageVo;
import venus.pub.lang.OID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/auUser")
public class AuUserAction implements IAuUserConstants {

    /**
     * 得到BS对象
     * 
     * @return BS对象
     */
    public IAuUserBs getBs() {
        return (IAuUserBs) Helper.getBean(BS_KEY); //得到BS对象,受事务控制
    }

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
        IAuUserBs bs = getBs();
        this.writeBackParam(_request);
        AuUserVo vo = new AuUserVo();
        if (!Helper.populate(vo, request)) { //从request中注值进去vo
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        int c1 = bs.getRecordCount("PARTY_ID='" + vo.getParty_id() + "'");
        if (c1 > 0) {
//            return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.The_user_already_exists"), MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        int c2 = bs.getRecordCount("LOGIN_ID='" + vo.getLogin_id() + "'");
        if (c2 > 0) {
//            return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.This_account_has_been_occupied"), MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        vo.setAgent_status("0");//默认没代理
        vo.setCreate_date(DateTools.getSysTimestamp());//打创建时间戳
        bs.insert(vo); //插入单条记录
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auUser/queryAll";
    }
    
    /**
     * 从页面表单获取信息注入vo，并插入多条记录
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/insertMulti")
    public String insertMulti(HttpServletRequest request, HttpServletResponse response) throws Exception {
	String codes[] = request.getParameter("code").split(",");
        if (codes == null || codes.length < 1)
//            return MessageAgent.sendErrorMessage(request,venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Incoming_parameter_is_incorrect_"), MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        OID[] oid = getBs().insertMulti(codes);
        HttpServletRequest req = (HttpServletRequest)request;
        HttpSession session = req.getSession(false);
        session.setAttribute("insertCount", oid.length);
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auUser/queryAll";
    }
        
    /**
     * 从页面表单获取信息注入vo，并插入单条记录
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/insert4party")
    public String insert4party(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        IAuUserBs bs = getBs();
        this.writeBackParam(_request);
        AuUserVo vo = new AuUserVo();
        if (!Helper.populate(vo, request)) { //从request中注值进去vo
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        int c = bs.getRecordCount("LOGIN_ID='" + vo.getLogin_id() + "'");
        if (c > 0) {
//            return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.This_account_has_been_occupied"), MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        vo.setAgent_status("0");//默认没代理
        vo.setCreate_date(DateTools.getSysTimestamp());//打创建时间戳
        bs.insert4party(vo); //插入单条记录
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auUser/queryAll";
    }
    
    /**
     * 从页面表单获取信息注入vo，并插入单条记录
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/register")
    public String register(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        IAuUserBs bs = getBs();
        HttpServletRequest req = _request;
        HttpSession session = (HttpSession) req.getSession(false);
        String inputVerify = request.getParameter("verify");
        String verifyCode = (String) session.getAttribute("verifyCode");
        if (verifyCode != null & verifyCode.equals(inputVerify)) {
            AuUserVo vo = new AuUserVo();
            if (!Helper.populate(vo, request)) { //从request中注值进去vo
//                return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
                return MESSAGE_AGENT_ERROR;
            }
            int c1 = bs.getRecordCount("PARTY_ID='" + vo.getParty_id() + "'");
            if (c1 > 0) {
//                return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.The_user_already_exists"), MessageStyle.ALERT_AND_BACK);
                return MESSAGE_AGENT_ERROR;
            }
            int c2 = bs.getRecordCount("LOGIN_ID='" + vo.getLogin_id() + "'");
            if (c2 > 0) {
//                return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.This_account_has_been_occupied"), MessageStyle.ALERT_AND_BACK);
                return MESSAGE_AGENT_ERROR;
            }
            vo.setAgent_status("0");//默认没代理
            vo.setCreate_date(DateTools.getSysTimestamp());//打创建时间戳
            bs.insert(vo); //插入单条记录
            vo.setPassword("");
            request.setAttribute(REQUEST_BEANS_VALUE, vo);
//            return request.findForward(FORWARD_TO_REGISTER_KEY);
            return FORWARD_TO_REGISTER_KEY;
        } else {
//            return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Verification_code_input_error"), MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
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
        this.writeBackParam(request);
        getBs().delete(request.getParameter(REQUEST_ID_FLAG)); //删除单条记录
        //RmJspHelper.transctPageVo(request,-deleteCount); //翻页偏移
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auUser/queryAll";
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
        this.writeBackParam(request);
        String[] id = request.getParameterValues(REQUEST_MULTI_ID_FLAG); //从request获取多条记录id
        if (id != null && id.length != 0) {
            getBs().delete(id); //删除多条记录
        }
        //RmJspHelper.transctPageVo(request, -deleteCount); //翻页偏移
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auUser/queryAll";
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
        this.writeBackParam(request);
        AuUserVo bean = getBs().find(request.getParameter(REQUEST_ID_FLAG)); //通过id获取vo
        request.setAttribute(REQUEST_BEAN_VALUE, bean); //把vo放入request
        //RmJspHelper.transctPageVo(request); //翻页重载
//        return request.findForward(FORWARD_UPDATE_KEY);
        return FORWARD_UPDATE_KEY;
    }

    /**
     * 从页面的表单获取单条记录login_id，查出这条记录的值
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/validate")
    public String validate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List beans = getBs().queryByCondition(" login_id='" + request.getParameter("login_id") + "'"); //通过loginid获取vo
        boolean hasLogin_id = false;
        if (beans != null & beans.size() > 0) {
            hasLogin_id = true;
        }
        request.setAttribute(REQUEST_BEAN_VALUE, String.valueOf(hasLogin_id)); //把vo放入request
        //RmJspHelper.transctPageVo(request); //翻页重载
//        return request.findForward(FORWARD_VALIDATE_KEY);
        return FORWARD_VALIDATE_KEY;
    }

    /**
     * validate login id
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/validateLoginId")
    public String validateLoginId(HttpServletRequest request, HttpServletResponse response) throws Exception {
        IAuUserBs bs = getBs();
        PrintWriter writer = response.getWriter();
        writer.print(bs.getRecordCount("LOGIN_ID='" + request.getParameter("loginId") + "'"));
        writer.close();
	    return null;
    }


    /**
     * reset password
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/resetPassword")
    public String resetPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.writeBackParam(request);
        getBs().resetPassword(request.getParameter(REQUEST_ID_FLAG)); //通过id获取vo
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auUser/queryAll";
    }

    /**
     * enable
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/enable")
    public String enable(HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.writeBackParam(request);
        getBs().enable(request.getParameter(REQUEST_ID_FLAG)); //通过id获取vo
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auUser/queryAll";
    }

    /**
     * disable
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/disable")
    public String disable(HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.writeBackParam(request);
        getBs().disable(request.getParameter(REQUEST_ID_FLAG)); //通过id获取vo
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auUser/queryAll";
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
        IRequest request = (IRequest)new HttpRequest(_request);
        IAuUserBs bs = getBs();
        this.writeBackParam(_request);
        AuUserVo vo = new AuUserVo();
        if (!Helper.populate(vo, request)) { //从request中注值进去vo
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        int c2 = bs.getRecordCount("LOGIN_ID='" + vo.getLogin_id() + "' and ID<>'" + vo.getId() + "'");
        if (c2 > 0) {
//            return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.This_account_has_been_occupied"), MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        vo.setModify_date(DateTools.getSysTimestamp()); //打修改时间戳
        vo.setRetire_date(DateTools.getRetireDate());//设置过期时间
        if(!request.getParameter("password").equals(request.getParameter("password1")))
            vo.setPassword(Encode.encode(vo.getPassword()));
        bs.update(vo); //更新单条记录
        //RmJspHelper.transctPageVo(request); //翻页重载
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auUser/queryAll";
    }


    /**
     * 从页面表单获取信息注入vo，并修改单条记录及相关的party表记录
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/update4party")
    public String update4party(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        IAuUserBs bs = getBs();
        this.writeBackParam(_request);
        AuUserVo vo = new AuUserVo();
        if (!Helper.populate(vo, request)) { //从request中注值进去vo
//            return MessageAgent.sendErrorMessage(request, DEFAULT_MSG_ERROR_STR, MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        int c2 = bs.getRecordCount("LOGIN_ID='" + vo.getLogin_id() + "' and ID<>'" + vo.getId() + "'");
        if (c2 > 0) {
//            return MessageAgent.sendErrorMessage(request, venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.This_account_has_been_occupied"), MessageStyle.ALERT_AND_BACK);
            return MESSAGE_AGENT_ERROR;
        }
        vo.setModify_date(DateTools.getSysTimestamp()); //打修改时间戳
        bs.update4party(vo); //更新单条记录
        //RmJspHelper.transctPageVo(request); //翻页重载
//        return request.findForward(FORWARD_TO_QUERY_ALL_KEY);
        return "redirect:/auUser/queryAll";
    }
    
    /**
     * 
     * 功能: 修改密码
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/modifyPassword")
    public String modifyPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        IAuUserBs bs = getBs();
        String loginId = request.getParameter("login_id");
        String oldPwd = request.getParameter("old_password");
        String newPwd = request.getParameter("new_password");
        
        List list = bs.queryByCondition("LOGIN_ID='" + loginId + "'");
        if (list==null || list.size()==0) {
            request.setAttribute("success_flag","-1");//用户名不正确
//            return request.findForward("modifyPasswordFrame");
            return FORWARD_MODIFY_PWD_FRAME;
        }else {
            AuUserVo vo = (AuUserVo)list.get(0);
            if( ! Encode.encode(oldPwd).equals(vo.getPassword())) {
                request.setAttribute("success_flag","0");//旧密码不正确
//                return request.findForward("modifyPasswordFrame");
                return FORWARD_MODIFY_PWD_FRAME;
            }else {
                vo.setPassword(Encode.encode(newPwd));
                vo.setModify_date(DateTools.getSysTimestamp()); //打修改时间戳
                vo.setRetire_date(DateTools.getRetireDate());//设置过期时间
                bs.update(vo); //更新单条记录
                request.setAttribute("success_flag","1");//修改成功
//                return request.findForward("modifyPasswordFrame");
                return FORWARD_MODIFY_PWD_FRAME;
            }
        }
    }
    
    /**
     * 
     * 功能: 修改密码
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/forceModifyPassword")
    public String forceModifyPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        IAuUserBs bs = getBs();
        String loginId = request.getParameter("login_id");
        String force = request.getParameter("force");
        String seeagain = request.getParameter("seeagain");
        String newPwd = request.getParameter("new_password");
        //设置用户概要配置，是否再次显示修改密码页面
        EnumRepository er = EnumRepository.getInstance();
        er.loadFromDir();
        EnumValueMap seechangepwdagain = er.getEnumValueMap("Au_UserProfile");
        UserProfileModel profile = new UserProfileModel(LoginHelper.getPartyId((HttpServletRequest) request));
        profile.updateProfile(seechangepwdagain.getValue("SEECHANGEPWDAGAIN"),StringUtils.isBlank(seeagain)?"1":seeagain);

        List list = bs.queryByCondition("LOGIN_ID='" + loginId + "'");
        if (list==null || list.size()==0) {
            request.setAttribute("success_flag","-1");//用户名不正确
//            return request.findForward("forceModifyPasswordFrame");
            return FORWARD_FORCE_MODIFY_PWD_FRAME;
        }else {
            AuUserVo vo = (AuUserVo)list.get(0);
            if( (Encode.encode(newPwd).equals(vo.getPassword()))&&("2".equals(force))) {//2表示强制密码不能相同
                request.setAttribute("success_flag","2");//旧密码与新密码相同
//                return request.findForward("forceModifyPasswordFrame");
                return FORWARD_FORCE_MODIFY_PWD_FRAME;
            }else {
                vo.setPassword(Encode.encode(newPwd));
                vo.setModify_date(DateTools.getSysTimestamp()); //打修改时间戳
                vo.setRetire_date(DateTools.getRetireDate());//设置过期时间
                bs.update(vo); //更新单条记录
                request.setAttribute("success_flag","1");//修改成功
//                return request.findForward("forceModifyPasswordFrame");
                return FORWARD_FORCE_MODIFY_PWD_FRAME;
            }
        }
    }


    /**
     * 从页面的表单获取单条记录id，并查看这条记录的详细信息
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/detail")
    public String detail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.writeBackParam(request);
        AuUserVo bean = getBs().find(request.getParameter(REQUEST_ID_FLAG)); //通过id获取vo
        request.setAttribute(REQUEST_BEAN_VALUE, bean); //把vo放入request
        //RmJspHelper.transctPageVo(request); //翻页重载
//        return request.findForward(FORWARD_DETAIL_KEY);
        return FORWARD_DETAIL_KEY;
    }


    /**
     * 查询全部记录，分页显示，支持页面上触发的后台排序
     * 
     * @param _request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryAll")
    public String queryAll(HttpServletRequest _request, HttpServletResponse response) throws Exception {
        IRequest request = (IRequest)new HttpRequest(_request);
        String system_id = request.getParameter("system_id");
        String func_code = request.getParameter("func_code");
        request.setAttribute("system_id", system_id);
        request.setAttribute("func_code", func_code);
        IAuUserBs bs = getBs();
        
        //定义默认用户状态条件
    	EnumRepository er = EnumRepository.getInstance();
        er.loadFromDir();
    	EnumValueMap enableStatusMap = er.getEnumValueMap("EnableStatus");
    	String enableStatus = "";
    	if (enableStatusMap != null) {
    	    List enableStatusList = enableStatusMap.getEnumList();
    	    enableStatus = enableStatusMap.getValue(enableStatusList.get(0).toString());
    	}
    	
        String queryCondition = "";  //查询条件
        if (!"".equals(enableStatus)) {
        	queryCondition = "A.ENABLE_STATUS = '" + enableStatus + "'";
        }
        queryCondition = AuHelper.filterOrgPrivInSQL(queryCondition, "B.CODE", (HttpServletRequest) request);//控制数据权限
       
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount4Limit(queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        List list = new ArrayList(); //定义结果集
        //list = bs.queryAll(pageVo.getCurrentPage(), pageVo.getPageSize(), orderStr);
        list = bs.queryByCondition4Limit(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition);  //按条件查询
        Helper.savePageVo(pageVo, request);
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest(request)); //回写表单
        request.setAttribute(REQUEST_BEANS_VALUE, list); //把结果集放入request/
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
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
        IRequest request = (IRequest)new HttpRequest(_request);
        String system_id = request.getParameter("system_id");
        String func_code = request.getParameter("func_code");
        
        String login_id = request.getParameter("login_id"); 
        String name = request.getParameter("name");
        String department = request.getParameter("hid_department");
        String enableStatus = request.getParameter("enableStatus");
        
        request.setAttribute("system_id", system_id);
        request.setAttribute("func_code", func_code);
        IAuUserBs bs = getBs();
        
        String queryCondition = null;
		if(login_id != null && name != null) {
			queryCondition = "A.login_id like'%" + login_id + "%' and A.name like'%" + name + "%'";
		}else if(login_id != null) {
			queryCondition = "A.login_id like'%" + login_id + "%'";
		}else if(name != null) {
			queryCondition = "A.name like'%" +name + "%'";
		}
		if(enableStatus != null && !"".equals(enableStatus)) {
			if ((login_id != null) || (name != null)) {
				queryCondition += " and A.enable_status = '" + enableStatus + "'";
			} else {
				queryCondition = "A.enable_status = '" + enableStatus + "'";
			}
		}
		if(department != null) {
			if ((login_id != null) || (name != null) || (enableStatus != null)) {
				queryCondition += " and B.parent_code like '" + department + "%'";
			} else {
				queryCondition = "B.parent_code like '" + department + "%'";
			}
		}
        queryCondition = AuHelper.filterOrgPrivInSQL(queryCondition, "B.CODE", (HttpServletRequest) request);//控制数据权限
        PageVo pageVo = Helper.findPageVo(request); //得到当前翻页信息
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = bs.getRecordCount4Limit(queryCondition);
            pageVo = Helper.createPageVo(request, recordCount);
        }
        List beans = bs.queryByCondition4Limit(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition); //按条件查询
        request.setAttribute(REQUEST_BEANS_VALUE, beans); //把结果集放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
//        return request.findForward(FORWARD_LIST_PAGE_KEY);
        return FORWARD_LIST_PAGE_KEY;
    }


    /**
     * 参照信息查询，带简单查询，分页显示，支持表单回写
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryReference")
    public String queryReference(HttpServletRequest request, HttpServletResponse response) throws Exception {
        IAuUserBs bs = getBs();
        String queryCondition = request.getParameter(REQUEST_QUERY_CONDITION_VALUE); //从request获得查询条件

        PageVo pageVo = Helper.findPageVo(request); //得到当前翻页信息
        List beans = bs.queryByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), queryCondition); //按条件查询
        request.setAttribute(REQUEST_BEANS_VALUE, beans); //把结果集放入request
        request.setAttribute(REQUEST_WRITE_BACK_FORM_VALUES, VoHelperTools.getMapFromRequest((HttpServletRequest) request)); //回写表单
        request.setAttribute("inputType", request.getParameter("inputType")); //传送输入方式,checkbox或radio
//        return request.findForward(FORWARD_REFERENCE_KEY);
        // no usages found
        return FORWARD_REFERENCE_KEY;
    }


    /**
     * 回写参数
     * 
     * @param request
     */
    private void writeBackParam(HttpServletRequest request) {
        String system_id = request.getParameter("system_id");
        String func_code = request.getParameter("func_code");
        request.setAttribute("system_id", system_id);
        request.setAttribute("func_code", func_code);
    }
}

