package venus.oa.login.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import venus.frames.base.action.DefaultServletException;
import venus.frames.mainframe.util.Helper;
import venus.oa.authority.appenddata.bs.IAppendDataBs;
import venus.oa.authority.appenddata.util.IConstantsimplements;
import venus.oa.authority.auauthorize.bs.IAuAuthorizeBS;
import venus.oa.authority.auauthorize.vo.AuAuthorizeVo;
import venus.oa.authority.aufunctree.bs.IAuFunctreeBs;
import venus.oa.authority.aufunctree.util.IAuFunctreeConstants;
import venus.oa.authority.aufunctree.vo.AuFunctreeVo;
import venus.oa.authority.auuser.bs.IAuUserBs;
import venus.oa.authority.auuser.util.IAuUserConstants;
import venus.oa.authority.auuser.vo.AuUserVo;
import venus.oa.checkcode.bs.ICheckCodeBs;
import venus.oa.helper.LoginHelper;
import venus.oa.login.tools.OnLineUser;
import venus.oa.login.tools.OnlineUserVo;
import venus.oa.login.vo.LoginSessionVo;
import venus.oa.organization.aupartyrelation.bs.IAuPartyRelationBs;
import venus.oa.organization.aupartyrelation.util.IConstants;
import venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo;
import venus.oa.sysparam.vo.SysParamVo;
import venus.oa.util.DateTools;
import venus.oa.util.Encode;
import venus.oa.util.GlobalConstants;
import venus.oa.util.ProjTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/")
public class LoginAction implements IAuUserConstants {

    private static final String LOGIN_LOG = "login/loginlog";
    private static final String SUCCESS = "main";
    private static final String NOTICE = "login/pwdnofity";
    private static final String MESSAGE_AGENT_ERROR = "common/common_error";

    @Autowired
    private ICheckCodeBs checkCodeBs;


    @RequestMapping("/login")
    public String service(HttpServletRequest request, HttpServletResponse response) throws DefaultServletException {

        IAuUserBs bs = (IAuUserBs) Helper.getBean(BS_KEY);
        String message = null;

//        HttpServletRequest req = (HttpServletRequest) request.getServletRequest();
        HttpSession session = (HttpSession) request.getSession(false);

        //验证码
        SysParamVo checkCodeVo = GlobalConstants.getSysParam("CHECKCODE");
        String checkCode = null==checkCodeVo?"":checkCodeVo.getValue();
        //先获取一下LoginSessionVo看用户是否是登陆过的，如果是登陆过的则不进行checkCode的校验，直接进行关系转换
        LoginSessionVo tempLoginSessionVo =(LoginSessionVo)session.getAttribute("LOGIN_SESSION_VO");

        if("true".equals(checkCode) && tempLoginSessionVo==null){
            String captchaId = session.getId();
            String j_captcha_response = request.getParameter("j_captcha_response");
            Boolean isResponseCorrect = checkCodeBs.validateResponse(captchaId, j_captcha_response);
            if(!isResponseCorrect.booleanValue()){
                message = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Checksum_error_please_re_fill_");
                request.setAttribute("Message", message);
//                return request.findForward("login");
                return LOGIN_LOG;
            }
        }

        String login_id = request.getParameter("login_id");
        String password = request.getParameter("password");
        String relationtype_id = request.getParameter("relationtype_id");
        String relation_id = request.getParameter("relation_id");
        password = null!=password? Encode.encode(password):password;
        
        //启用登录系统
        SysParamVo loginStrategy = GlobalConstants.getSysParam(GlobalConstants.LOGINSTRATEGY);
        if (loginStrategy != null && "1".equals(loginStrategy.getValue())) {
            String login_mac = request.getParameter("login_mac");
                //限制同一用户只能登录系统一次
                if (login_id != null) {
                    List list = OnLineUser.queryOnlineUserList();
                    for (int i = 0; i < list.size();i++) {
                    OnlineUserVo onlineUserVo = (OnlineUserVo)list.get(i);
                    if(login_id.equals(onlineUserVo.getLogin_id()) && (!login_mac.equals(onlineUserVo.getLogin_mac()))) {
                        request.setAttribute("Message", venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.User") +
                                login_id +venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Has") + onlineUserVo.getLogin_ip() +
                                venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Console_log") +"!");
//                        return request.findForward("login");
                        return LOGIN_LOG;
                    }
                }
            }        
            session.setAttribute("login_mac",login_mac);            
        }

        // 变换团体关系类型
        if(relationtype_id != null || relation_id != null) {
            LoginSessionVo oldLoginVo = LoginHelper.getLoginVo(session);
            if(oldLoginVo==null) {
                request.setAttribute("Message", venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Login_has_timed_out") +
                        venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority._1") +
                        venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Please_login_") +
                        "<script>if(confirm('"+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Login_has_timed_out") +
                        venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority._1") +
                        venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Please_login_")+"')){window.close()}</script>");
//                return request.findForward("MessageAgentError");
                return MESSAGE_AGENT_ERROR;
            }

            if("-1".equals(relationtype_id)) {
                relationtype_id = null;
            }
            login_id = oldLoginVo.getLogin_id()==null?"":oldLoginVo.getLogin_id();
            password = oldLoginVo.getPassword()==null?"":oldLoginVo.getPassword();
        }

        // 是不是从portal单点登陆过来的
        boolean isPortalLogin = false;
        // 从portal单点登陆过来
        if("1".equals(request.getParameter("isPortalLogin"))) {
            isPortalLogin = true;

            /*UserMappingEntry userMap = bs.getPortalUserMap(login_id);
            if(userMap==null) {
                request.setAttribute("Message", "获取portal帐户信息时发生异常！<script>if(confirm('获取portal帐户信息时发生异常！')){window.close()}</script>");
                return request.findForward("MessageAgentError");
            }
            login_id = userMap.getUsername()==null?"":userMap.getUsername();
            password = userMap.getPassword()==null?"":userMap.getPassword();*/
        }
        
        List lResult = bs.queryByCondition("login_id='" + login_id + "'");
        
        //用户身份校验
        if (lResult != null && lResult.size() > 0) {
            AuUserVo userVo = (AuUserVo)lResult.get(0);
            //密码过期
            SysParamVo pwdLifeParam = GlobalConstants.getSysParam(GlobalConstants.PWDLIFECYCLE);
            int pwdLifeCycle = -1;
            if(null!=pwdLifeParam){
                pwdLifeCycle = Integer.parseInt(pwdLifeParam.getValue());
            }
            
            //管理员密码是否过期
            SysParamVo admin_pwd_expiredVo = GlobalConstants.getSysParam(GlobalConstants.ADMIN_PWD_EXPIRED);
            String admin_pwd_expired = null==admin_pwd_expiredVo?"":admin_pwd_expiredVo.getValue();
            if((-1!=pwdLifeCycle)&&(null!=userVo.getRetire_date())&&(userVo.getRetire_date().before(new Date()))&&
                    (("1".equals(userVo.getIs_admin())&&"true".equals(admin_pwd_expired))||!"1".equals(userVo.getIs_admin()))){
                userVo.setEnable_status("0");//禁用该账户
                bs.update(userVo);//设置禁用
                if(isPortalLogin) {
                    message = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Password_expiration_account_has_been_disabled_")+"<script>if(confirm('"+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Password_expiration_account_has_been_disabled_")+"')){window.close()}</script>";
                    request.setAttribute("Message", message);
//                    return request.findForward("MessageAgentError");
                    return MESSAGE_AGENT_ERROR;
                }else {
                    message = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Password_expiration_account_has_been_disabled_");
                    request.setAttribute("Message", message);
//                    return request.findForward("login");
                    return LOGIN_LOG;
                }
            }
            if ("1".equals(userVo.getEnable_status())) {
                if (isPortalLogin||password.equals(userVo.getPassword())) {
                    //初始化密码尝试次数为0
                    userVo.setFailed_times(new Integer(0));
                    if(null==userVo.getRetire_date())
                        if("1".equals(userVo.getIs_admin())&&"true".equals(admin_pwd_expired)||!"1".equals(userVo.getIs_admin()))
                            userVo.setRetire_date(DateTools.getRetireDate());
                    bs.update(userVo);
                    LoginSessionVo loginVo = new LoginSessionVo();
                    //查询AuPartyRelation表
                    IAuPartyRelationBs relBs = (IAuPartyRelationBs) Helper.getBean(IConstants.BS_KEY);
                    List relList = null;
                    SysParamVo chooseAuRel = GlobalConstants.getSysParam(GlobalConstants.CHOOSEAUREL);
                    if(!"1".equals(userVo.getIs_admin())&&relation_id == null&&chooseAuRel!=null&&"true".equals(chooseAuRel.getValue())){
                        AuPartyRelationVo queryVo = new AuPartyRelationVo();
                        queryVo.setPartyid(userVo.getParty_id());
                        List currentRelList = relBs.queryAuPartyRelation(queryVo);//已经order by code了,自然order by relationType
                        relation_id=((AuPartyRelationVo)currentRelList.get(0)).getId();
                    }
                    if(relation_id != null){
                        relList = new ArrayList();
                        AuPartyRelationVo currentRelVo = relBs.find(relation_id);
                        relList.add(currentRelVo);
                        //设置当前客户的code
                        loginVo.setCurrent_code(currentRelVo.getCode());
                        if(currentRelVo.getRelationtype_id().equals(GlobalConstants.getRelaType_comp())){
                            //设置账户的角色关系
                            AuPartyRelationVo searchRelVo = new AuPartyRelationVo();
                            searchRelVo.setPartyid(userVo.getParty_id());
                            searchRelVo.setRelationtype_id(GlobalConstants.getRelaType_role());
                            List roleRelList = relBs.queryAuPartyRelation(searchRelVo);
                            relList.addAll(roleRelList);
                        }
                    }else{
                        AuPartyRelationVo queryVo = new AuPartyRelationVo();
                        queryVo.setPartyid(userVo.getParty_id());
                        relList = relBs.queryAuPartyRelation(queryVo);//员工信息
                    }   
                    if(relList != null) {
                        for(int i=0; i<relList.size(); i++) {
                            AuPartyRelationVo relVo = (AuPartyRelationVo)relList.get(i);
                            //查询当前节点的所有上级节点，一直到根节点
                            List parList = relBs.queryParentRelation(relVo.getParent_code());
                            relVo.setAll_parent_vo(parList);
                        }
                    }
                    //获取全部权限
                    IAuAuthorizeBS auBs = (IAuAuthorizeBS) Helper.getBean(venus.oa.authority.auauthorize.util.IConstants.BS_KEY);
                    Map auMap = null;
                    if(relationtype_id != null)
                        auMap = auBs.getAuByPartyId(userVo.getParty_id(), null, relationtype_id);
                    else
                        auMap = auBs.getAuByRelList(relList, null);
                    //获取全部菜单、按钮
                    String fType = GlobalConstants.getResType_menu();//功能菜单
                    String bType = GlobalConstants.getResType_butn();//功能按钮
                    IAuFunctreeBs funcBs = (IAuFunctreeBs) Helper.getBean(IAuFunctreeConstants.BS_KEY);
                    List lFunc = funcBs.queryByCondition("TYPE='"+fType+"' OR TYPE='"+bType+"'");
                    Map m_all_func = new HashMap();
                    for(Iterator it = lFunc.iterator(); it.hasNext(); ) {
                        AuFunctreeVo vo = (AuFunctreeVo) it.next();
                        m_all_func.put(vo.getId(),vo);
                    }
                    Map m_owner_butn = new HashMap();//按钮权限
                    Map m_owner_menu = new HashMap();//菜单权限
                    Map m_owner_fild = new HashMap();//字段权限
                    Map m_owner_recd = new HashMap();//记录权限
                    List l_owner_orga = new ArrayList();//组织权限
                    Map m_all_url = new HashMap();//全部功能菜单url
                    Map m_owner_url = new HashMap();//有权限的功能菜单url
                    Map m_owner_butn_admin = new HashMap();//可授权按钮权限
                    Map m_owner_menu_admin = new HashMap();//可授权菜单权限
                    Map m_owner_fild_admin = new HashMap();//可授权字段权限
                    Map m_owner_recd_admin = new HashMap();//可授权记录权限
                    //List l_owner_orga_admin = new ArrayList();//可授权组织权限
                    Map l_owner_fun_orga = new HashMap();//功能数据权限
                    
                    //将权限拆分：
                    String AU_PERMIT = GlobalConstants.getAuTypePermit();//权限类型——允许
                    int AU_AUTHORIZE = Integer.parseInt(GlobalConstants.getAuTypeAuthorize());//权限类型——可授权
                    
                    for(Iterator it=auMap.keySet().iterator(); it.hasNext(); ) {
                        AuAuthorizeVo auVo = (AuAuthorizeVo)auMap.get((String)it.next());
                        String resType = auVo.getResource_type();//资源类型
                        String resId = auVo.getResource_id();//资源id
                        String auStatus = auVo.getAuthorize_status();//授权情况
                        String is_append = auVo.getIs_append();//是否有附加数据
                        int auAccess = Integer.parseInt(auVo.getAccess_type());//权限类型
                        
                        if(GlobalConstants.getResType_butn().equals(resType)) {//按钮
                            AuFunctreeVo fvo = (AuFunctreeVo) m_all_func.get(resId);
                            if(AU_PERMIT.equals(auStatus)) { //只取允许的
                                m_owner_butn.put(fvo.getTotal_code(),fvo);
                            }
                            if("1".equals(is_append)){//有附加数据
                                //获取附加数据                                
                                l_owner_fun_orga.putAll(appendAuthority(relation_id,relList,auVo,userVo));
                            }
                            if(ProjTools.judgeNum(auAccess, AU_AUTHORIZE)) {//可授权权限
                                m_owner_butn_admin.put(fvo.getTotal_code(),fvo);
                            }
                        }else if(GlobalConstants.getResType_menu().equals(resType)) {//菜单
                            AuFunctreeVo fvo = (AuFunctreeVo) m_all_func.get(resId);
                            if(AU_PERMIT.equals(auStatus)) { //只取允许的
                                m_owner_menu.put(fvo.getTotal_code(),fvo);
                                if("1".equals(is_append)){//有附加数据
                                    //获取附加数据                                    
                                    l_owner_fun_orga.putAll(appendAuthority(relation_id,relList,auVo,userVo));
                                }
                            }
                            if(ProjTools.judgeNum(auAccess,AU_AUTHORIZE)) {//可授权权限
                                m_owner_menu_admin.put(fvo.getTotal_code(),fvo);
                            }                           
                        }else if(GlobalConstants.getResType_fild().equals(resType)) {//字段
                            if(AU_PERMIT.equals(auStatus)) { //只取允许的
                                m_owner_fild.put(resId,auVo);
                            }
                            if(ProjTools.judgeNum(auAccess,AU_AUTHORIZE)) {//可授权权限
                                m_owner_fild_admin.put(resId,auVo);
                            }
                        }else if(GlobalConstants.getResType_recd().equals(resType)) {//记录
                            if(AU_PERMIT.equals(auStatus)) { //只取允许的
                                m_owner_recd.put(resId,auVo);
                            }
                            if(ProjTools.judgeNum(auAccess,AU_AUTHORIZE)) {//可授权权限
                                m_owner_recd_admin.put(resId,auVo);
                            }
                        }else if(GlobalConstants.getResType_orga().equals(resType)) {//组织
                            l_owner_orga.add(auVo.getResource_code());//组织
                            //l_owner_orga_admin.add(auVo.getResource_code());//可授权权限
                        }
                    }
                    for(Iterator it = lFunc.iterator(); it.hasNext(); ) {
                        AuFunctreeVo vo = (AuFunctreeVo) it.next();
                        String url = vo.getUrl();
                        if(url!=null && url.length()>0) {
                            m_all_url.put(url, ""); //全部功能菜单url
                            if((auMap.containsKey(vo.getId()))&&(((AuAuthorizeVo)auMap.get(vo.getId())).getAuthorize_status().equals(GlobalConstants.getAuTypePermit()))) {
                                m_owner_url.put(url,"");//有权限的功能菜单url
                            }
                        }
                    }
                    //设置AuUser表中的属性
                    loginVo.setAgent_status(userVo.getAgent_status());
                    loginVo.setIs_admin(userVo.getIs_admin());
                    loginVo.setLogin_id(userVo.getLogin_id());
                    loginVo.setName(userVo.getName());
                    loginVo.setParty_id(userVo.getParty_id());
                    loginVo.setPassword(userVo.getPassword());
                    loginVo.setRelationtype_id(relationtype_id);
                    //设置所属组织机构属性
                    loginVo.setRelation_vo_list(relList);
                    //设置所拥有的权限属性
                    loginVo.setOwner_butn_map(m_owner_butn);
                    loginVo.setOwner_fild_map(m_owner_fild);
                    loginVo.setOwner_recd_map(m_owner_recd);
                    loginVo.setOwner_menu_map(m_owner_menu);
                    loginVo.setOwner_menu_url_map(m_owner_url);
                    loginVo.setAll_menu_url_map(m_all_url);
                    //设置功能数据权限
                    loginVo.setOwner_fun_orga(l_owner_fun_orga);
                    //拥有权限的组织机构
                    if("1".equals(loginVo.getIs_admin())) {//超级管理员
                        loginVo.setOwner_org_arr(new String[]{""});
                    }else {
                        loginVo.setOwner_org_arr((String[])l_owner_orga.toArray(new String[0]));
                    }
                    //设置拥有的授权权限
                    loginVo.setOwner_butn_map_admin(m_owner_butn_admin);
                    loginVo.setOwner_fild_map_admin(m_owner_fild_admin);
                    loginVo.setOwner_recd_map_admin(m_owner_recd_admin);
                    loginVo.setOwner_menu_map_admin(m_owner_menu_admin);
                    
                    //拥有权限的组织机构
                    /*作用与效果重复
                    if("1".equals(loginVo.getIs_admin())) {//超级管理员
                        loginVo.setOwner_org_arr_admin(new String[]{""});
                    }else {
                        loginVo.setOwner_org_arr_admin((String[])l_owner_orga_admin.toArray(new String[0]));
                    }
                    */
                    
                    //放入session
                    session.setAttribute("LOGIN_SESSION_VO",loginVo);
                    //密码过期提醒
                    SysParamVo pwdNofityParam = GlobalConstants.getSysParam(GlobalConstants.PWDNOFITY);
                    int pwdNotify = -1;
                    if(null!=pwdNofityParam){
                        pwdNotify = Integer.parseInt(pwdNofityParam.getValue());
                    }
                    //判断是否需要过期提示（首先过期日期不能为null）
                    if(null!=userVo.getRetire_date()){
                        Calendar notifyDate=Calendar.getInstance();
                        notifyDate.setTime(userVo.getRetire_date());
                        notifyDate.add(Calendar.DATE,0-pwdNotify);
                        Date nowDate=new Date();
                        //判断是否满足密码即将过期提示条件
                        if((-1!=pwdLifeCycle)&&(-1!=pwdNotify)&&(nowDate.after(notifyDate.getTime()))){
                            message = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Password_are")+ DateTools.getDateBetween(userVo.getRetire_date(),nowDate)+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Days_expired_please_change_your_password_");
                            request.setAttribute("Message", message);
//                            return request.findForward("pwdnotify");//跳转到密码将要过期提示页面
                            return NOTICE;
                        }
                    }
//                    return request.findForward("success");//正常跳转
                    return SUCCESS;

                } else {
                    int failedTimes = null==userVo.getFailed_times()?0:userVo.getFailed_times().intValue();//失败次数
                    userVo.setFailed_times(new Integer(failedTimes+1));//失败次数增加一次
                    SysParamVo sysParamVo = GlobalConstants.getSysParam(GlobalConstants.RETRYTIMES);
                    int retryMaxTimes = -1;
                    if(null != sysParamVo){
                        retryMaxTimes=Integer.parseInt(sysParamVo.getValue());
                    }
                    if(-1!=retryMaxTimes&&userVo.getFailed_times().intValue()>=retryMaxTimes){
                        userVo.setEnable_status("0");//禁用该账户
                    }
                    if(-1!=retryMaxTimes)
                        bs.update(userVo);//保存失败次数
                    if(isPortalLogin) {
                        message = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.The_password_is_incorrect_enter_the_correct_password_")+"<script>if(confirm('"+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.The_password_is_incorrect_enter_the_correct_password_")+"')){window.close()}</script>";
                        request.setAttribute("Message", message);
//                        return request.findForward("MessageAgentError");
                        return MESSAGE_AGENT_ERROR;
                    }else {
                        message = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.The_password_is_incorrect_enter_the_correct_password_");
                        request.setAttribute("Message", message);
//                        return request.findForward("login");
                        return LOGIN_LOG;
                    }
                }
            } else {
                if(isPortalLogin) {
                    message = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Account_has_been_disabled_please_use_the_other_account_login_")+"<script>if(confirm('"+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Account_has_been_disabled_please_use_the_other_account_login_")+"')){window.close()}</script>";
                    request.setAttribute("Message", message);
//                    return request.findForward("MessageAgentError");
                    return MESSAGE_AGENT_ERROR;
                }else {
                    message = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Account_has_been_disabled_please_use_the_other_account_login_");
                    request.setAttribute("Message", message);
//                    return request.findForward("login");
                    return LOGIN_LOG;
                }
            }
        } else {
            if(isPortalLogin) {
                message = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Account_does_not_exist_please_enter_the_correct_account_number_")+"<script>if(confirm('"+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Account_does_not_exist_please_enter_the_correct_account_number_")+"')){window.close()}</script>";
                request.setAttribute("Message", message);
//                return request.findForward("MessageAgentError");
                return MESSAGE_AGENT_ERROR;
            }else {
                message = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Account_does_not_exist_please_enter_the_correct_account_number_");
                request.setAttribute("Message", message);
//                return request.findForward("login");
                return LOGIN_LOG;
            }
        }
    }
    
    /**
     * 获取附加数据
     * @param relation_id
     * @param relList
     * @param auVo
     * @param userVo
     * @return
     */
    private Map appendAuthority(String relation_id, List relList, AuAuthorizeVo auVo, AuUserVo userVo){
        //获取附加数据
        IAppendDataBs appendBs = (IAppendDataBs) Helper.getBean(IConstantsimplements.BS_KEY);
        Map appendData = null;
        if(relation_id != null){
            String relCode[]=new String[relList.size()];
            for(int i=0;i<relList.size();i++){
                relCode[i]=((AuPartyRelationVo)relList.get(i)).getCode();
            }
            appendData = appendBs.getExtendAppendAuByVisitorCode(relCode,auVo.getResource_id(),null);//根据code查询
        }else{
            appendData = appendBs.getExtendAppendAuByPartyId(userVo.getParty_id(),auVo.getResource_id());//根据partyid查询
        }        
        return appendData;
    }
}