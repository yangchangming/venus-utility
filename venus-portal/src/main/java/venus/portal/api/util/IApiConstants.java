/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.api.util;

/**
 * EWP常量定义接口。
 */
public interface IApiConstants {

    /**
     * 用户评论系统用户登录标识
     */
    public static final String POSTS_USER_LOGIN_SESSION_MARK = "UDP_EWP_POSTS_USER_LOGIN_SESSION_MARK";

    /**
     * URL请求参数Key，栏目或者文章的标识。
     */
    public static final String PARAM_KEY = "id";

    /**
     * URL请求参数code,栏目编码
     */
    public static final String PARAM_CODE = "code";

    /**
     * URL请求参数view，模板。
     */
    public static final String VIEW_KEY = "view";

    /**
     * URL请求参数isRecommend，是否按推荐排序，此参数可选（可选值为Y和N）。
     */
    public static final String PARAM_ORDERBY_IS_RECOMMEND = "isRecommend";

    /**
     * URL请求参数isPublishTime，是否按发布时间排序顺序，此参数可选（可选值为Y和N）。
     */
    public static final String PARAM_ORDERBY_IS_PUBLISHTIME = "isPublishTime";

    /**
     * URL请求参数isSortNum，是否按序号顺序排序，此参数可选（可选值为Y和N）。
     */
    public static final String PARAM_ORDERBY_IS_SORTNUM = "isSortNum";

    public static final String PARAM_ORDERBY_IS_Y = "Y";
    public static final String PARAM_ORDERBY_IS_N = "N";

    /**
     * 存储在通用Map中的Key，以便UI取值使用。
     */
    public static final String UI_KEY = "result";

    /**
     * Action成功后的forward。
     */
    public static final String FORWARD_SUCCESS = "success";

    /**
     * Action失败后的forward。
     */
    public static final String FORWARD_FAILURE = "failure";

    /**
     * 前台需要根据客户自定义排序时，设置此变量
     */
    public static final String DOC_ORDER_STR = " sortNum desc";

    /**
     * 分页显示时，页码
     */
    public static final String PAGE_NO = "pageNo";

    /**
     * 前台展示时每页显示条数
     */
    public static final String PAGE_SIZE = "pageSize";


    //文档类型导航菜单视图公共跳转模板
    public final static String VIEW_DOCTYPE_COMMON = "common_notexist";
    //文档类型导航菜单视图菜单跳转模板
    public final static String VIEW_DOCTYPE_MENU = "menu";

    //文档标题列表默认跳转模板
    public final static String VIEW_DOCUMENTS = "paging";
    //文档展示页默认跳转模板
    public final static String VIEW_DOCUMENT = "document";
    //搜索默认跳转模板
    public final static String VIEW_SEARCHRESULT = "searchResult";

    public final static String SITE_ID = "siteId";

    public final static String VIEW_DOCTYPE_IS_VALID = "1";

    public final static String VIEW_DOCTYPE_IS_NAVIGATE = "1";
}
