package venus.portal.api.controller.posts;

import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import venus.portal.api.util.IApiConstants;
import venus.portal.posts.posts.bs.IPostsBS;
import venus.portal.posts.posts.model.EwpPostsEntity;
import venus.portal.posts.user.model.EwpLoginEntity;
import venus.portal.posts.user.util.ILoginUsrInfoConstants;
import venus.portal.util.ServletContextHelper;
import venus.portal.util.SqlHelper;
import venus.portal.vo.ConditionPageResults;
import venus.portal.website.model.Website;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qj on 14-2-17.
 */
@Controller
@RequestMapping(value = "apis/ewp/posts")
public class PostsController implements IApiConstants, ILoginUsrInfoConstants {

    private static Logger loger = Logger.getLogger(UsrsController.class);

    @Autowired
    private IPostsBS ewpPostsBS;

    @RequestMapping(value = "/gettests")
    @ResponseBody
    public Map getMap(
            @RequestBody Map cprs) {
        Map tempc = cprs;
        Map temp = new HashMap();
        temp.put("sq", 2);
        temp.put("sa", 3);
        return temp;

    }

    @RequestMapping(value = "/getPosts")
    @ResponseBody
    public ConditionPageResults getPosts(@RequestBody Map map) {
        ConditionPageResults cprs = new ConditionPageResults(map);
        String queryCondition = SqlHelper.build(cprs.getConditions());
        if (!queryCondition.isEmpty()) {
            int size = ewpPostsBS.getRecordCount(queryCondition);
            cprs.setTotalCount(size);
            cprs.setResults(ewpPostsBS.queryByCondition(cprs.getCurrentPage(), cprs.getPageSize(), queryCondition, cprs.getOrderBy()));
        }

        return cprs;

    }

    @RequestMapping(value = "/getPostsByUser")
    @ResponseBody
    public ConditionPageResults getPostsByUser(@RequestBody Map map, HttpServletRequest request) {
        Map<String, String> userMap = getUserInfoByRequest(request);
        ConditionPageResults cprs = new ConditionPageResults(map);
        Map<String, String> conditions = cprs.getConditions();
        String queryCondition = "";
        if (!Strings.isNullOrEmpty(userMap.get("id"))) {
            queryCondition += " USR_ID = '" + userMap.get("id") + "' ";
        }

        if (!Strings.isNullOrEmpty(conditions.get("CONTENT"))) {
            if (!Strings.isNullOrEmpty(queryCondition)) {
                queryCondition += " And ";
            }
            queryCondition += " CONTENT like '%" + cprs.getConditions().get("CONTENT") + "%' ";
        }

        if (!Strings.isNullOrEmpty(conditions.get("WEB_SITE_ID"))) {
            if (!Strings.isNullOrEmpty(queryCondition)) {
                queryCondition += " And ";
            }
            queryCondition += " WEBSITE_ID = '" + cprs.getConditions().get("WEB_SITE_ID") + "' ";
        }

        if (!Strings.isNullOrEmpty(queryCondition)) {
            int size = ewpPostsBS.getRecordCount(queryCondition);
            cprs.setTotalCount(size);
            cprs.setResults(ewpPostsBS.queryByCondition(cprs.getCurrentPage(), cprs.getPageSize(), queryCondition, cprs.getOrderBy()));
        }
        return cprs;
    }

    @RequestMapping(value = "/getPostsByAdmin")
    @ResponseBody
    public ConditionPageResults getPostsByAdmin(@RequestBody Map map, HttpServletRequest request) {
        Map<String, String> userMap = getUserInfoByRequest(request);
        ConditionPageResults cprs = new ConditionPageResults(map);
        Map<String, String> conditions = cprs.getConditions();
        String queryCondition = "";
        if (Strings.isNullOrEmpty(userMap.get("isadmin")) ||
                Strings.isNullOrEmpty(userMap.get("id"))) {
            return cprs;
        }

        if (!Strings.isNullOrEmpty(conditions.get("usrName"))) {
            queryCondition += " usrName like '%" + conditions.get("usrName") + "%' ";
        }

        if (!Strings.isNullOrEmpty(conditions.get("CONTENT"))) {
            if (!Strings.isNullOrEmpty(queryCondition)) {
                queryCondition += " And ";
            }
            queryCondition += " CONTENT like '%" + cprs.getConditions().get("CONTENT") + "%' ";
        }

        if (!Strings.isNullOrEmpty(conditions.get("WEB_SITE_ID"))) {
            if (!Strings.isNullOrEmpty(queryCondition)) {
                queryCondition += " And ";
            }
            queryCondition += " WEBSITE_ID = '" + cprs.getConditions().get("WEB_SITE_ID") + "' ";
        }

        int size = ewpPostsBS.getRecordCount(queryCondition);
        cprs.setTotalCount(size);
        cprs.setResults(ewpPostsBS.queryByCondition(cprs.getCurrentPage(), cprs.getPageSize(), queryCondition, cprs.getOrderBy()));
        return cprs;

    }

    @RequestMapping(value = "/deletePosts")
    @ResponseBody
    public Map deletePosts(@RequestBody String[] deleteIds, HttpServletRequest request) {
        try {
            ewpPostsBS.deleteMulti(deleteIds);
        } catch (Exception e) {
            loger.error("remove posts error!", e);
            return new HashMap<String, String>() {{
                put("state", LOGIC_FALSE);
            }};
        }

        return new HashMap<String, String>() {{
            put("state", LOGIC_TRUE);
        }};

    }

    @RequestMapping(value = "/getAllPosts/{docid}")
    @ResponseBody
    public List<EwpPostsEntity> getAllPosts(
            @PathVariable String docid) {
        return ewpPostsBS.queryByDocID(docid);
    }

    @RequestMapping(value = "/addPost/{siteCode}/{docid}")
    @ResponseBody
    public EwpPostsEntity addPost(@PathVariable String siteCode,
                                  @PathVariable String docid,
                                  @RequestBody String content,
                                  String replyTo,
                                  String replyToUser,
                                  HttpServletRequest request) {

        Map<String, String> userInfo = getUserInfoByRequest(request);
        if (LOGIC_FALSE.equals(userInfo.get("loginState"))) {
            return new EwpPostsEntity();
        }
        String uID = userInfo.get("id");
        String uName = userInfo.get("name");

        if (uID == null || uID.equals("")) {
            return null;
        }

        Website currentWebSite = ServletContextHelper.getWebsiteBySiteCode(siteCode);
        String siteID = currentWebSite.getId();
        String siteName = currentWebSite.getWebsiteName();

        EwpPostsEntity epeVo = new EwpPostsEntity();
        epeVo.setWebsiteCode(siteCode);
        epeVo.setWebsiteID(siteID);
        epeVo.setWebsiteName(siteName);
        epeVo.setDocId(docid);
        epeVo.setUsrId(uID);
        epeVo.setUsrName(uName);
        epeVo.setContent(content);
        if (replyTo != null && !replyTo.isEmpty()) {
            epeVo.setReplyto(replyTo);
            epeVo.setReplytoUserName(replyToUser);
            epeVo.setIsreply("1");
        }
        ewpPostsBS.insert(epeVo);

        return epeVo;
    }

    @RequestMapping(value = "/replyPost/{replyTo}")
    @ResponseBody
    public EwpPostsEntity replyPost(@PathVariable String replyTo,
                                    @RequestBody Map map,
                                    HttpServletRequest request) {
        String siteCode = (String) map.get("siteCode");
        siteCode = siteCode.substring(1); //去掉斜杠
        String docId = (String) map.get("docId");
        String content = (String) map.get("content");
        String replyToUser = (String) map.get("toUser");
        return addPost(siteCode, docId, content, replyTo, replyToUser,request);
    }

    private Map<String, String> getUserInfoByRequest(HttpServletRequest request) {
        Map<String, String> rs = new HashMap<String, String>();
        String id = "";
        String name = "";
        String isadmin = "";
        String loginState = LOGIC_FALSE;
        if (request.getSession().getAttribute(POSTS_USER_LOGIN_SESSION_MARK) != null) {
            EwpLoginEntity userInfoSession = (EwpLoginEntity) (request.getSession().getAttribute(POSTS_USER_LOGIN_SESSION_MARK));
            id = userInfoSession.getId();
            name = userInfoSession.getName();
            isadmin = userInfoSession.getIsadmin();
            loginState = LOGIC_TRUE;
        }
        rs.put("id", id);
        rs.put("name", name);
        rs.put("isadmin", isadmin);
        rs.put("loginState", loginState);
        return rs;
    }

}
