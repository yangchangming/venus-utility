package venus.portal.api.controller.posts;

import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import venus.frames.mainframe.util.Helper;
import venus.portal.api.util.IApiConstants;
import venus.portal.posts.user.bs.ILoginUsrInfoBS;
import venus.portal.posts.user.model.EwpLoginEntity;
import venus.portal.posts.user.model.EwpUsrInfoEntity;
import venus.portal.posts.user.util.ILoginUsrInfoConstants;
import venus.portal.posts.user.vo.EwpUsrInfoVo;
import venus.portal.vo.ConditionPageResults;
import venus.pub.util.Encode;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by qj on 14-2-17.
 */
@Controller
@RequestMapping(value = "apis/ewp/user")
public class UsrsController implements IApiConstants, ILoginUsrInfoConstants {

    private static Logger loger = Logger.getLogger(UsrsController.class);
    private Encode encode = new Encode();

    @Autowired
    private ILoginUsrInfoBS ewpLoginUsrInfoBS;

    @RequestMapping(value = "/login")
    @ResponseBody
    public Map login(@RequestBody Map map, HttpServletRequest request) {

        Object loginName = map.get("usrName");
        Object loginPWD = map.get("usrPWD");
        if (loginName == null || loginPWD == null) {
            return getUserMapByRequest(request);
        }
        String encodePwd = encode.encode((String) loginPWD);
        EwpLoginEntity userInfo = ewpLoginUsrInfoBS.findByName((String) loginName, encodePwd);

        request.getSession().setAttribute(POSTS_USER_LOGIN_SESSION_MARK, userInfo);

        Map rs = getUserMapByRequest(request);

        return rs;
    }

    @RequestMapping(value = "/logout")
    @ResponseBody
    public Map logout(HttpServletRequest request) {

        request.getSession().setAttribute(POSTS_USER_LOGIN_SESSION_MARK, null);

        Map rs = getUserMapByRequest(request);

        return rs;
    }

    @RequestMapping(value = "/userRegister")
    @ResponseBody
    public Map userRegister(@RequestBody Map map, HttpServletRequest request) {

        Object loginName = map.get("regName");
        Object loginPWD = map.get("regPWD");
        Object loginGender = map.get("usrGender");
        Object loginEmail = map.get("usrEmail");
        Object loginIsAdmin = map.get("usrIsAdmin");
        if (loginName == null || loginPWD == null) {
            return getUserMapByRequest(request);
        }
        EwpLoginEntity userLogin = new EwpLoginEntity();

        userLogin.setName((String) loginName);
        userLogin.setPwd(encode.encode((String) loginPWD));
        userLogin.setIsadmin(LOGIC_FALSE);

        if (loginIsAdmin != null) {
            userLogin.setIsadmin((String) loginIsAdmin);
        }

        EwpUsrInfoEntity userInfo = new EwpUsrInfoEntity();
        userInfo.setGender((String)loginGender);
        userInfo.setEmail((String)loginEmail);

        ewpLoginUsrInfoBS.insert(userLogin, userInfo);

        request.getSession().setAttribute(POSTS_USER_LOGIN_SESSION_MARK, userLogin);

        Map rs = getUserMapByRequest(request);

        return rs;
    }

    @RequestMapping(value = "/addUser")
    @ResponseBody
    public Map addUser(@RequestBody Map map, HttpServletRequest request) {

        Object loginName = map.get("usrName");
        Object loginPWD = map.get("usrPWD");
        Object loginIsAdmin = map.get("usrIsAdmin");
        if (loginName == null || loginPWD == null) {
            return new HashMap<String, String>() {{
                put("state", LOGIC_FALSE);
            }};
        }
        EwpLoginEntity userLogin = new EwpLoginEntity();

        userLogin.setName((String) loginName);
        userLogin.setPwd(encode.encode((String) loginPWD));
        userLogin.setIsadmin(LOGIC_FALSE);

        if (loginIsAdmin != null) {
            userLogin.setIsadmin((String) loginIsAdmin);
        }

        ewpLoginUsrInfoBS.insert(userLogin);

        return new HashMap<String, String>() {{
            put("state", LOGIC_TRUE);
        }};
    }

    @RequestMapping(value = "/removeUser")
    @ResponseBody
    public Map removeUser(@RequestBody String[] deleteIds, HttpServletRequest request) {

        try {
            ewpLoginUsrInfoBS.deleteMulti(deleteIds);
        } catch (Exception e) {
            loger.error("remove users error!", e);
            return new HashMap<String, String>() {{
                put("state", LOGIC_FALSE);
            }};
        }

        return new HashMap<String, String>() {{
            put("state", LOGIC_TRUE);
        }};
    }

    @RequestMapping(value = "/resetPassword")
    @ResponseBody
    public Map resetPassword(@RequestBody String[] resetIds, HttpServletRequest request) {
        String defaultPWD = encode.encode("123456");
        try {
            for (String id : resetIds) {
                ewpLoginUsrInfoBS.resetPasswordByID(id, defaultPWD);
            }
        } catch (Exception e) {
            loger.error("remove users error!", e);
            return new HashMap<String, String>() {{
                put("state", LOGIC_FALSE);
            }};
        }

        return new HashMap<String, String>() {{
            put("state", LOGIC_TRUE);
        }};
    }

    @RequestMapping(value = "/get")
    @ResponseBody
    public Map getUserInfo(HttpServletRequest request) {
        HashMap<String, Object> msgMap = new HashMap<String, Object>();

        if (request.getSession().getAttribute(POSTS_USER_LOGIN_SESSION_MARK) == null) {
            msgMap.put("flag", "false");
            msgMap.put("message", "Please login first!");
            return msgMap;
        } else {
            EwpLoginEntity userLoginSession = (EwpLoginEntity) (request.getSession().getAttribute(POSTS_USER_LOGIN_SESSION_MARK));
            EwpUsrInfoEntity userInfo = ewpLoginUsrInfoBS.findUserById(userLoginSession.getId());
            EwpUsrInfoVo userInfoVo = new EwpUsrInfoVo();
            Helper.copyProperties(userInfo, userInfoVo);

            msgMap.put("flag", "true");
            msgMap.put("userInfo", userInfoVo);
        }

        return msgMap;
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public Map updateUserInfo(@RequestBody Map map, HttpServletRequest request) throws Exception {
        String userId = (String) map.get("id");
        EwpUsrInfoEntity userInfo = ewpLoginUsrInfoBS.findUserById(userId);
        String birthday = (String) map.get("birthdayLong");

        userInfo.setRealName((String) map.get("realName"));
        userInfo.setEmail((String) map.get("email"));
        userInfo.setGender((String) map.get("gender"));
        userInfo.setPhone((String) map.get("phone"));
        if (!birthday.isEmpty()) {
            long timeValue = Long.parseLong((String) map.get("birthdayLong"));
            userInfo.setBirthday(new Timestamp(timeValue));
        }

        ewpLoginUsrInfoBS.updateUserInfo(userInfo);

        return null;
    }

    @RequestMapping(value = "/updatePwd")
    @ResponseBody
    public Map updateUserPwd(@RequestBody Map map, HttpServletRequest request) throws Exception {
        HashMap<String, Object> msgMap = new HashMap<String, Object>();

        if (request.getSession().getAttribute(POSTS_USER_LOGIN_SESSION_MARK) == null) {
            msgMap.put("flag", "false");
            msgMap.put("message", "Please login first!");
            return msgMap;
        } else {
            EwpLoginEntity userLoginSession = (EwpLoginEntity) (request.getSession().getAttribute(POSTS_USER_LOGIN_SESSION_MARK));
            String originalPwd = encode.encode((String) map.get("originalPwd"));
            if (!userLoginSession.getPwd().equals(originalPwd)) {
                msgMap.put("flag", "false");
                msgMap.put("message", "The original password is wrong!");
            } else {
                String newPwd = encode.encode((String) map.get("newPwd"));
                userLoginSession.setPwd(newPwd);
                ewpLoginUsrInfoBS.update(userLoginSession);
                msgMap.put("flag", "true");
            }
        }

        return msgMap;
    }

    @RequestMapping(value = "/checkName")
    @ResponseBody
    public String checkUserNameUnique(@RequestBody Map map, HttpServletRequest request) throws Exception {
        String userName = (String) map.get("uName");
        if (ewpLoginUsrInfoBS.isExistUser(userName)) {
            return LOGIC_TRUE;
        } else {
            return LOGIC_FALSE;
        }
    }

    private Map getUserMapByRequest(HttpServletRequest request) {
        Map rs = new HashMap();
        String id = "";
        String name = "";
        String isAdmin = "";
        String loginState = LOGIC_FALSE;
        String loginError = LOGIC_TRUE;
        if (request.getSession().getAttribute(POSTS_USER_LOGIN_SESSION_MARK) != null) {
            EwpLoginEntity userInfoSession = (EwpLoginEntity) (request.getSession().getAttribute(POSTS_USER_LOGIN_SESSION_MARK));
            id = userInfoSession.getId();
            name = userInfoSession.getName();
            isAdmin = userInfoSession.getIsadmin();
            loginState = LOGIC_TRUE;
            loginError = LOGIC_FALSE;
        }
        rs.put("id", id);
        rs.put("name", name);
        rs.put("isAdmin", isAdmin);
        rs.put("loginState", loginState);
        rs.put("loginError", loginError);
        return rs;
    }

    @RequestMapping(value = "/loginInterface")
    public ModelAndView userLoginInterface(HttpServletRequest request) {
        Map parameterMap = new HashMap();
        Enumeration pNames = request.getParameterNames();
        while (pNames.hasMoreElements()) {
            String name = (String) pNames.nextElement();
            String value = request.getParameter(name);
            parameterMap.put(name, request.getParameter(name));
        }
        Map rs = login(parameterMap, request);
        if (rs.get("loginState").equals(LOGIC_TRUE)) {
            return new ModelAndView("/" + "postsManagement", "result", rs);
        }
        Object loginName = request.getParameter("usrName");
        if (loginName == null) {
            rs = new HashMap();
        }
        return new ModelAndView("/" + "postsLogin", "result", rs);
    }

    @RequestMapping(value = "/registerInterface")
    public ModelAndView userRegisterInterface(HttpServletRequest request) {
        Map parameterMap = new HashMap();
        Enumeration pNames = request.getParameterNames();
        while (pNames.hasMoreElements()) {
            String name = (String) pNames.nextElement();
            String value = request.getParameter(name);
            parameterMap.put(name, request.getParameter(name));
        }
        Map rs = userRegister(parameterMap, request);
        if (rs.get("loginState").equals(LOGIC_TRUE)) {
            return new ModelAndView("/" + "postsManagement", "result", rs);
        }
        Object loginName = request.getParameter("regName");
        if (loginName == null) {
            rs = new HashMap();
        }
        return new ModelAndView("/" + "postsLogin", "result", rs);
    }


    @RequestMapping(value = "/getUserInfos")
    @ResponseBody
    public ConditionPageResults getUserInfos(@RequestBody Map map) {
        ConditionPageResults cprs = new ConditionPageResults(map);
        Map<String, String> conditions = cprs.getConditions();
        String queryCondition = "";
        if (!Strings.isNullOrEmpty(conditions.get("REAL_NAME"))) {
            queryCondition += " REAL_NAME like '%" + cprs.getConditions().get("REAL_NAME") + "%' ";
        }

        if (!Strings.isNullOrEmpty(conditions.get("GENDER"))) {
            if (!Strings.isNullOrEmpty(queryCondition)) {
                queryCondition += " And ";
            }
            queryCondition += " GENDER = '" + cprs.getConditions().get("GENDER") + "' ";
        }

        int size = ewpLoginUsrInfoBS.getRecordCountUserInfoByCondition(queryCondition);

        List rsList= new LinkedList();
        try {
            for(Object entity:ewpLoginUsrInfoBS.queryUserInfoByCondition(cprs.getCurrentPage(), cprs.getPageSize(), queryCondition, cprs.getOrderBy())){
                Map entityMap = ObjectToMap(entity);
                entityMap.putAll(ObjectToMap(ewpLoginUsrInfoBS.findById((String)(entityMap.get("id")))));
                rsList.add(entityMap);
            }

        } catch (Exception e) {
            loger.error(e);
            size=0;
            rsList= new LinkedList();
        }

        cprs.setTotalCount(size);
        cprs.setResults(rsList);
        return cprs;
    }

    private Map ObjectToMap(Object entity) {
        Map entityMap=new HashMap();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(entity.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(entity);

                    entityMap.put(key, value);
                }

            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return entityMap;
    }

    @RequestMapping(value = "/getTempleteInfo")
    public ModelAndView getTempleteInfo(HttpServletRequest request) {
        String templeteName = request.getParameter("templeteName");
        Map rs = getUserMapByRequest(request);
        if (templeteName == null || "".equals(templeteName)) {
            return new ModelAndView("/" + VIEW_DOCTYPE_COMMON, "result", rs);
        }
        return new ModelAndView("/" + templeteName, "result", rs);
    }
}
