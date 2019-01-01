package venus.frames.base;

import java.io.Serializable;

/**
 * 定义常量接口
 *
 * @author 岳国云
 */

public interface IGlobalsKeys extends Serializable {

    /**
     * WEB 端上下文对象捡取的标识
     */
    String WEB_CONTEXT_KEY = "WEB_CONTEXT_KEY";

    /**
     * 系统错误页标识名
     */
    String ERROR_PAGE_KEY = "error";

    /**
     * WEB WAR Path
     */
    //String WEB_CONTEXT_PATH = null;

    /**
     * WEB 端捡取上下文对象路径的标记
     */
    String WEB_CONTEXT_PATH_KEY = "WEB_CONTEXT_PATH_KEY";

    /**
     * APP 端上下文对象捡取的标识
     */
    String APP_CONTEXT_KEY = "APP_CONTEXT_KEY";

    /**
     * session 中暂存的错误信息的捡取标识
     */
    String WEB_ERRORS_IN_SESSION = "WEB_ERRORS_IN_SESSION";

    /**
     * WEB 全局的错误信息的捡取标识
     */
    String WEB_GLOBAL_ERROR = "WEB_GLOBAL_ERROR";

    /**
     * 标识 sessionlistener 存入 SESSION的 Key
     */
    String SESSION_LISTENER_KEY = "SESSION_LISTENER_KEY";


    /**
     * 标识 tree 菜单存入 session 的 Key
     */
    String PROFILE_LISTENER_KEY = "PROFILE_LISTENER_KEY";

    /**
     * 标识全局统一 tree 菜单存入 IContext 的 Key
     */
    String WEB_CONTEXT_TREE_KEY = "WEB_CONTEXT_TREE_KEY";

    /**
     * 标识 tree 菜单存入 session 的 Key
     */
    String PROFILE_TREE_KEY = "PROFILE_TREE_KEY";

    /**
     * 缺省本地cache的名字
     */
    String DEFAULT_LOCAL_CACHE_NAME = "VENUS_DEFAULT_LOCAL_CACHE_NAME";


    /**
     * 缺省Action Event Service的名字
     */
    String ACTION_EVENT_BS_NAME = "VENUS_ACTION_EVENT_BS_NAME";

    /**
     * 缺省Login Service的名字
     */
    String LOGIN_BS_NAME = "VENUS_LOGIN_BS_NAME";


    /**
     * 缺省Oid Service的名字
     */
    String OID_BS_NAME = "VENUS_OID_BS_NAME";


    /**
     * 缺省 PageVO 的 KEY
     */
    String PAGEVO_KEY = "VENUS_PAGEVO_KEY";


    /**
     * 缺省 PageVO NO 的 KEY
     */
    String PAGE_NO_KEY = "VENUS_PAGE_NO_KEY";


    /**
     * 缺省 PageVO PAGE SIZE的 KEY
     */
    String PAGE_SIZE_KEY = "VENUS_PAGE_SIZE_KEY";


    /**
     * 缺省 PageVO 的 PAGE_ALLCOUNT_KEY
     */
    String PAGE_ALLCOUNT_KEY = "VENUS_PAGE_ALLCOUNT_KEY";

    /**
     * 缺省 OrderVO 的 KEY
     */
    String ORDER_KEY = "VENUS_ORDER_KEY";

    String SORT_SYMBOL_ASC = "ASC";
    String SORT_SYMBOL_DESC = "DESC";
    String ORDER_BY_SYMBOL = " ORDER BY ";

    String MESSAGES_KEY = "org.apache.struts.action.MESSAGE";
    

}
