/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.document.util;

/**
 * @author yangchangming
 */
public interface IConstants {

    public final static String DOCUMENT_OID                     = "ewp_document";
    public final static String EWP_DOCUMENT_TIME_RELEASE_ID     = "EWP_DOCUMENT_TIME_RELEASE";
    public final static String EWP_DOCUMENT_TIME_RELEASE_LOG_ID = "EWP_DOCUMENT_TIME_RELEASE_LOG";

    public final static String DOCUMENT_BS             = "documentBS";
    public final static String EWP_DOC_TIME_RELEASE_BS = "ewpDocTimeReleaseBs";

    public final static String DOCUMENT_TYPE_BS = "docTypeBS";

    public final static String DOCUMENT_TYPE_TEMPLATE_BS = "ITemplateBs";

    //逻辑 -- 是
    public final static String LOGIC_TRUE  = "1";
    //逻辑 -- 否
    public final static String LOGIC_FALSE = "0";

    //文档的操作类型
    public final static String EDITOR_DOCUMENT_OPERATION_KEY = "editorDocument";
    public final static String ADD_DOCUMENT_OPERATION_KEY    = "addDocument";
    public final static String VIEW_DOCUMENT_OPERATION_KEY   = "viewDocument";

    //文档状态编码
    public final static String DOC_STATUS_DRAFT       = "0";        // 草稿 （默认）
    public final static String DOC_STATUS_UNPUBLISHED = "1";  // 待发布
    public final static String DOC_STATUS_PUBLISHED   = "2";    //已发布
    public final static String DOC_STATUS_ARCHIVE     = "3";      //归  档


    //注入错误提示消息
    public final static String POPULATE_ERROR_MSG_STR     = "VO POPULATE ERROR!";
    //数据错误提示消息
    public final static String DATA_ERROR_MSG_STR         = "DATA ERROR!";
    //回写表单KEY
    public final static String WRITE_BACK_FORM_VALUES_KEY = "writeBackFormValues";

    //struts跳转KEY
    public final static String DOCUMENT_MANAGER_KEY = "documentManager";
    public final static String DOCUMENT_LIST_KEY    = "documentList";
    public final static String DOCUMENT_QUERY_LIST  = "queryDocuments";
    public final static String PRE_EDITOR_KEY       = "preEditor";
    public final static String INIT_DOCUMENT_PAGE   = "initPage";
    public final static String FORWARD_LIST_PAGE="toQueryAll";
    public final static String FORWARD_NOPOWER_PAGE="toNoPowerPage";

    //文档类型与文档关系数据 ----可用
    public final static String DOCTYPE_DOC_ACTIVE     = "1";
    ///文档类型与文档关系数据 ----不可用
    public final static String DOCTYPE_DOC_NOT_ACTIVE = "0";

    //文档类型与文档关系数据 ----文档已发布
    public final static String DOCTYPE_DOC_PUBLISHED     = "2";
    ///文档类型与文档关系数据 ----文档未发布
    public final static String DOCTYPE_DOC_NOT_PUBLISHED = "0";

    //文档查询范围
    //栏目
    public final static String DOCSCOPE_DOC_TYPE = "1";
    //站点
    public final static String DOCSCOPE_WEB_SITE = "0";
}
