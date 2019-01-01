package venus.portal.template.util;

/**
 * @author zhangrenyang
 */

public interface ITemplateConstants {

    //BS的规划化名称
    public final static String BS_KEY = "ITemplateBs";

    //struts页面跳转
    public final static String FORWARD_TO_QUERY_ALL = "toQueryAll";

    public final static String FORWARD_LIST_PAGE = "listPage";

    public final static String FORWARD_INSERT_PAGE = "insertPage";

    public final static String FORWARD_UPDATE_PAGE = "updatePage";

    public final static String FORWARD_DETAIL_PAGE = "detailPage";

    public final static String FORWARD_REFERENCE_PAGE = "referencePage";

    //request处理中的key值
    public final static String REQUEST_ID = "id";

    public final static String REQUEST_IDS = "ids";

    public final static String REQUEST_CURRENT_SITE = "currentSite";

    public final static String REQUEST_BEAN = "bean";

    public final static String REQUEST_BEANS = "beans";

    public final static String DOCUMENT_BEANS = "docBeans";

    public final static String REQUEST_QUERY_CONDITION = "queryCondition";

    public final static String REQUEST_WRITE_BACK_FORM_VALUES = "writeBackFormValues";

    public final static String REQUEST_WEBSITES = "webSites";
    //表名
    public final static String TABLE_NAME = "ewp_template";

    public final static String DOCTYPE_TEMPLATE = "hang";

    public final static String DOCUMENT_TEMPLATE = "doc";

    /**
     * 会话可能已过期
     */
    public final static String UDP_EWP_DOCTYPE_SESSION_CLOSE = "udp.ewp.doctype_session_close";
    /**
     * 模板视图编码已经存在
     */
    public final static String UDP_EWP_TEMPLATE_VIEWCODE_EXISTS = "udp.ewp.template_viewcode_exists";
    /**
     * 模板视图编码唯一,可以使用
     */
    public final static String UDP_EWP_TEMPLATE_VIEWCODE_DOES_NOT_EXIST = "udp.ewp.template_viewcode_doesnot_exist";
    /**
     * 模板文件夹已存在
     */
    public final static String UDP_EWP_TEMPLATE_FOLDER_IS_EXIST = "udp.ewp.template_folder_is_exist";

    /**
     * 模板已与文档类型进行了挂接，不进行模板的删除，返回消息
     */
    public final static String UDP_EWP_TEMPLATE_TEMPLATE_HAS_BEAN_MOUNT_DOCTYPE = "udp.ewp.template.template_has_bean_mount_doctype";
}
