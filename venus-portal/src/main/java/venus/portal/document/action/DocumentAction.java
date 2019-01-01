/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.portal.document.action;

import org.apache.commons.lang.StringUtils;
import venus.frames.base.action.DefaultDispatchAction;
import venus.frames.base.action.IForward;
import venus.frames.base.action.IRequest;
import venus.frames.base.action.IResponse;
import venus.frames.base.bean.DefaultForm;
import venus.frames.mainframe.util.Helper;
import venus.frames.mainframe.util.VoHelper;
import venus.frames.web.message.MessageAgent;
import venus.frames.web.message.MessageStyle;
import venus.frames.web.page.PageVo;
import venus.oa.helper.LoginHelper;
import venus.portal.doctype.bs.IDocTypeBS;
import venus.portal.doctype.model.DocType;
import venus.portal.document.bs.IDocumentBS;
import venus.portal.document.model.Document;
import venus.portal.document.util.IConstants;
import venus.portal.document.vo.DocumentVo;
import venus.portal.timerelease.bs.IEwpDocTimeReleaseBs;
import venus.portal.vo.PageResults;
import venus.pub.util.StringUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import venus.frames.web.page.PageVo;
//import venus.frames.web.message.messageAgent;
//import venus.frames.web.message.MessageStyle;
//import venus.frames.mainframe.util.VoHelper;

/**
 * 文章处理Action。
 *
 * @author yangchangming
 */
public class DocumentAction extends DefaultDispatchAction implements IConstants {

    private IDocTypeBS getDocTypeBs() {
        IDocTypeBS docTypeBs = (IDocTypeBS) Helper.getBean(venus.portal.doctype.util.IConstants.DOCTYPE_BS);
        return docTypeBs;
    }

    /**
     * 进入欢迎页面
     *
     * @param formBean 前台封闭的数据对象
     * @param request  请求
     * @param response 响应
     * @return IForward  业务完成后的跳转对象
     * @throws Exception 抛出保存模板中出现的异常
     */
    public IForward queryAll(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        boolean isAdmin = LoginHelper.getIsAdmin(getHttpServletRequest(request));
        if (isAdmin) {
            return request.findForward(FORWARD_LIST_PAGE);
        }

        if (getDocTypeBs().checkAuthority(getHttpServletRequest(request))) {
            return request.findForward(FORWARD_LIST_PAGE);
        } else {
            return request.findForward(FORWARD_NOPOWER_PAGE);
        }
    }

    /**
     * 新建文档
     *
     * @param form
     * @param request
     * @param response
     * @return
     */
    public IForward createDocument(DefaultForm form, IRequest request,
                                   IResponse response) throws Exception {
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        String siteId = request.getParameter("siteId");
        IEwpDocTimeReleaseBs ewpDocTimeReleaseBs = (IEwpDocTimeReleaseBs) Helper.getBean(EWP_DOC_TIME_RELEASE_BS);
        Document doc = new Document();
        if (!Helper.populate(doc, request)) {
            return MessageAgent.sendErrorMessage(request, POPULATE_ERROR_MSG_STR, MessageStyle.ALERT_AND_BACK);
        }
        String destDocTypeIds = request.getParameter("destDocTypeIds");
        List<String> tempDocTypeIds = this.getListString(destDocTypeIds);
        doc.setStatus(DOC_STATUS_DRAFT);
        //默认权限 --- 预留字段(暂未使用)
        doc.setPermissions("22");
        //是否允许评论
        if (doc.getIsComment() == null || !LOGIC_TRUE.equals(doc.getIsComment())) {
            doc.setIsComment(LOGIC_FALSE);
        } else {
            doc.setIsComment(LOGIC_TRUE);
        }
        //是否推荐文章
        if (doc.getRecommend() == null || !LOGIC_TRUE.equals(doc.getRecommend())) {
            doc.setRecommend(LOGIC_FALSE);
        } else {
            doc.setRecommend(LOGIC_TRUE);
        }
        //是否显示热词
        if (doc.getIsShowHotWords() == null || !LOGIC_TRUE.equals(doc.getIsShowHotWords())) {
            doc.setIsShowHotWords(LOGIC_FALSE);
        } else {
            doc.setIsShowHotWords(LOGIC_TRUE);
        }
        //默认有效
        doc.setIsValid(LOGIC_TRUE);
        //默认创建时间为当前系统时间
        Timestamp ts = new Timestamp((new Date()).getTime());
        doc.setCreateTime(ts);
        doc.setVisitCount(new Long(0));

        String docTypeId = doc.getDocTypeID();
        if (!tempDocTypeIds.contains(docTypeId)) {//如果没有默认的栏目ID 则添加
            tempDocTypeIds.add(docTypeId);
        }
        docmentBs.save(doc, tempDocTypeIds);// 创建文章，文章隶属于多栏目
        //文档类型判断标识
        request.setAttribute("docTypeID", doc.getDocTypeID());
        String docScope = request.getParameter("docScope");
        request.setAttribute("docScope", docScope);
        request.setAttribute("siteId", siteId);
        return request.findForward(DOCUMENT_MANAGER_KEY);
    }

    private String getDocTypeCodeByDocTypeID(String docTypeID) {
        IDocTypeBS docTypeBs = (IDocTypeBS) Helper.getBean(DOCUMENT_TYPE_BS);
        if (docTypeID != null && !"".equals(docTypeID)) {
            return docTypeBs.getDocTypeCodeById(docTypeID);
        }
        return "";
    }

    /**
     * 根据条件列表显示文档
     *
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward listDocuments(DefaultForm from, IRequest request,
                                  IResponse response) throws Exception {
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        List result = new ArrayList();

        DocumentVo docVo = new DocumentVo();
        if (!Helper.populate(docVo, request)) {
            return MessageAgent.sendErrorMessage(request, POPULATE_ERROR_MSG_STR, MessageStyle.ALERT_AND_BACK);
        }

        //获得PAGEVO
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = docmentBs.queryDocRecordCountByCondition(docVo);
            pageVo = Helper.createPageVo(request, recordCount);
        }

        String orderStr = Helper.findOrderStr(request);
        result = docmentBs.queryAllDocByCondition(pageVo.getCurrentPage(), pageVo.getPageSize(), orderStr, docVo);
        //保存排序字段
        Helper.saveOrderStr(orderStr, request);
        //保存分页信息
        Helper.savePageVo(pageVo, request);
        //获得数据集
        request.setAttribute("documents", result);
        //vo带入下个页面
        request.setAttribute("docVo", docVo);
        //为分页提供cmd参数
        request.setAttribute("cmd", "listDocuments");
        //回写栏目编码一遍栏目预览时使用。
        request.setAttribute("docTypeCode", this.getDocTypeCodeByDocTypeID(docVo.getDocTypeID()));
        //回写表单
        request.setAttribute(WRITE_BACK_FORM_VALUES_KEY, VoHelper.getMapFromRequest(request));
        return request.findForward(DOCUMENT_LIST_KEY);
    }

    /**
     * 跳转到编辑页面
     *
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward preEditorDocument(DefaultForm from, IRequest request,
                                      IResponse response) throws Exception {
        String documentID = request.getParameter("documentID");
        String docOperateType = request.getParameter("operationType");
        String docTypeId = request.getParameter("docTypeID");
        String siteId = request.getParameter("siteId");
        if (documentID == null || "".equals(documentID)) {
            return MessageAgent.sendErrorMessage(request, POPULATE_ERROR_MSG_STR, MessageStyle.ALERT_AND_BACK);
        }
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        IEwpDocTimeReleaseBs ewpDocTimeReleaseBs = (IEwpDocTimeReleaseBs) Helper.getBean(EWP_DOC_TIME_RELEASE_BS);
        Document document = docmentBs.findDocById(documentID);

        if (document == null) {
            return MessageAgent.sendErrorMessage(request, DATA_ERROR_MSG_STR, MessageStyle.ALERT_AND_BACK);
        }
        List<String> orginalDocTypeIds = docmentBs.getDocTypeIdsByDocId(documentID, DOCTYPE_DOC_ACTIVE);
        IDocTypeBS docTypeBs = (IDocTypeBS) Helper.getBean("docTypeBS");
        StringBuffer tempDocTypeIds = new StringBuffer();
        StringBuffer tempDocTypeNames = new StringBuffer();
        for (int i = 0; i < orginalDocTypeIds.size(); i++) {
            String tempId = orginalDocTypeIds.get(i);
            DocType docType = docTypeBs.findDocTypeById(tempId);
            tempDocTypeIds.append(tempId);
            tempDocTypeNames.append(docType.getName());
            if (i < orginalDocTypeIds.size() - 1) {
                tempDocTypeIds.append(",");
                tempDocTypeNames.append(",");
            }
        }

        Timestamp ts = ewpDocTimeReleaseBs.getReleaseTimeByDocumentId(documentID);

        request.setAttribute("orginalDocTypeIds", tempDocTypeIds.toString());
        request.setAttribute("orginalDocTypeNames", tempDocTypeNames.toString());
        request.setAttribute("operationType", docOperateType);
        request.setAttribute("preReleaseDate", ts);
        request.setAttribute("document", document);
        request.setAttribute("docTypeID", docTypeId);
        request.setAttribute("siteId", siteId);
        return request.findForward(PRE_EDITOR_KEY);
    }

    /**
     * 编辑文档
     *
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward editorDocument(DefaultForm from, IRequest request,
                                   IResponse response) throws Exception {
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        String siteId = request.getParameter("siteId");
        IEwpDocTimeReleaseBs ewpDocTimeReleaseBs = (IEwpDocTimeReleaseBs) Helper.getBean(EWP_DOC_TIME_RELEASE_BS);
        Document doc = new Document();
        if (!Helper.populate(doc, request)) {
            return MessageAgent.sendErrorMessage(request, POPULATE_ERROR_MSG_STR, MessageStyle.ALERT_AND_BACK);
        }
        Document document = docmentBs.findDocById(doc.getId());
        //是否允许评论
        if (doc.getIsComment() == null || !LOGIC_TRUE.equals(doc.getIsComment())) {
            document.setIsComment(LOGIC_FALSE);
        } else {
            document.setIsComment(LOGIC_TRUE);
        }
        //是否推荐文章
        if (doc.getRecommend() == null || !LOGIC_TRUE.equals(doc.getRecommend())) {
            document.setRecommend(LOGIC_FALSE);
        } else {
            document.setRecommend(LOGIC_TRUE);
        }
        //是否显示热词
        if (doc.getIsShowHotWords() == null || !LOGIC_TRUE.equals(doc.getIsShowHotWords())) {
            document.setIsShowHotWords(LOGIC_FALSE);
        } else {
            document.setIsShowHotWords(LOGIC_TRUE);
        }
        String status = document.getStatus();
        if (!DOC_STATUS_PUBLISHED.equals(status)) {//已发布状态文章不允许编辑
            document.setTitle(doc.getTitle());//标题
            document.setSource(doc.getSource());
            document.setTitelAbstract(doc.getTitelAbstract());//摘要
            document.setCreateBy(doc.getCreateBy());//作者
            document.setSeoKeyWord(doc.getSeoKeyWord());//关键字
            document.setShortTitle(doc.getShortTitle());//短标题
            document.setContent(doc.getContent());//内容
            document.setPicture(doc.getPicture());
            document.setSortNum(doc.getSortNum());
            Timestamp editTime = new Timestamp((new Date()).getTime());
            document.setEditTime(editTime);
            String destDocTypeIds = request.getParameter("destDocTypeIds"); //获得共享栏目ID
            List<String> tempDocTypeIds = this.getListString(destDocTypeIds);

            //  docmentBs.update(doc);
            List<String> originalDocTypeIds = docmentBs.getDocTypeIdsByDocId(doc.getId(), DOCTYPE_DOC_ACTIVE);
            docmentBs.update(document, originalDocTypeIds, tempDocTypeIds);

        }
        //放入request中，方便首页判断文档类型
        ewpDocTimeReleaseBs.documentTimeReleaseHandle(request.getServletRequest(), document.getId());
        request.setAttribute("docTypeID", doc.getDocTypeID());
        String docScope = request.getParameter("docScope");
        request.setAttribute("docScope", docScope);
        request.setAttribute("siteId", siteId);
        return request.findForward(DOCUMENT_MANAGER_KEY);
    }

    /**
     * 删除文档(假删除，更改状态为无效) - 可批量删除
     *
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward deleteDocuments(DefaultForm from, IRequest request,
                                    IResponse response) throws Exception {
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        String docIds = request.getParameter("docIds");
        docmentBs.batchDelete(docIds);
        String docTypeID = request.getParameter("docTypeID");
        //重新查询一次
        request.setAttribute("docTypeID", docTypeID);
        //listDocumentsByType(from,request,response);
        return request.findForward(DOCUMENT_QUERY_LIST);

    }

    /**
     * 根据文章所属文档类型（栏目），获取文章数据
     *
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward listDocumentsByType(DefaultForm from, IRequest request, IResponse response) throws Exception {
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        List result;

        DocumentVo docVo = new DocumentVo();
        if (!Helper.populate(docVo, request)) {
            return MessageAgent.sendErrorMessage(request, POPULATE_ERROR_MSG_STR, MessageStyle.ALERT_AND_BACK);
        }
        if (StringUtils.isBlank(docVo.getDocTypeID())) {
            docVo.setDocTypeID(request.getParameter("typeid"));
        }
        //获得PAGEVO
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            int recordCount = docmentBs.getDocCountByDocConditionDocTypeActiveAndNotPublished(docVo);
            pageVo = Helper.createPageVo(request, recordCount);
        }

        String orderStr = Helper.findOrderStr(request);
        result = docmentBs.queryAllDocByDocConditionDocTypeActiveAndNotPublished(pageVo.getCurrentPage(), pageVo.getPageSize(), orderStr, docVo);
        //保存排序字段
        Helper.saveOrderStr(orderStr, request);
        //保存分页信息
        Helper.savePageVo(pageVo, request);
        //获得数据集
        request.setAttribute("documents", result);
        //vo带入下个页面
        request.setAttribute("docVo", docVo);
        //为分页提供cmd参数
        request.setAttribute("cmd", "listDocumentsByType");
        //回写栏目编码一遍栏目预览时使用。
        request.setAttribute("docTypeCode", this.getDocTypeCodeByDocTypeID(docVo.getDocTypeID()));
        //回写表单
        request.setAttribute(WRITE_BACK_FORM_VALUES_KEY, VoHelper.getMapFromRequest(request));
        return request.findForward(DOCUMENT_LIST_KEY);
    }

    private List<String> getListString(String srcString) {
        List<String> result = new ArrayList<String>();
        String values = "";
        if (srcString != null && !"".equals(srcString) && srcString.endsWith(",")) {
            values = StringUtil.trimString(srcString);
            values = values.substring(0, values.length() - 1);
        } else if (srcString != null && !"".equals(srcString)) {
            values = StringUtil.trimString(srcString);
        }
        String[] temps = (values).split(",");

        for (String temp : temps) {
            if (temp != null && !"".equals(temp)) {
                result.add(temp);
            }
        }
        return result;
    }

    /**
     * 文章共享，可以将一栏目下的文章，共享至其他多栏目下
     *
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward shareDocument(DefaultForm from, IRequest request, IResponse response) throws Exception {
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        String docIds = request.getParameter("docIds");
        String destDocTypeIds = request.getParameter("destDocTypeIds");
        List<String> tempDocIds = getListString(docIds);
        List<String> tempDestTypeIds = getListString(destDocTypeIds);
        docmentBs.shareDocument(tempDocIds, tempDestTypeIds);
        String docTypeID = request.getParameter("docTypeID");
        request.setAttribute("docTypeID", docTypeID);
        return request.findForward(DOCUMENT_QUERY_LIST);
        //  return listDocumentsByType(from,request,response);
    }

    /**
     * 文章移动操作，将文章从一栏目移动到另外一个栏目下
     *
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward moveDocument(DefaultForm from, IRequest request,
                                 IResponse response) throws Exception {
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        String docIds = request.getParameter("docIds");
        String destDocTypeId = request.getParameter("destDocTypeId");
        String srcDocTypeID = request.getParameter("docTypeID");
        List<String> tempDocIds = getListString(docIds);
        docmentBs.transferDocument(tempDocIds, srcDocTypeID, destDocTypeId);
        String docTypeID = request.getParameter("docTypeID");
        request.setAttribute("docTypeID", docTypeID);
        return request.findForward(DOCUMENT_QUERY_LIST);
        //return listDocumentsByType(from,request,response);
    }

    /**
     * 拷贝文章。可以将文章从一个父栏目拷贝到其他栏目
     *
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward copyDocument(DefaultForm from, IRequest request,
                                 IResponse response) throws Exception {
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        String docIds = request.getParameter("docIds");//需要拷贝的文档ID
        String destDocTypeIds = request.getParameter("destDocTypeIds");//复制到其他栏目的ID
        List<String> tempDocIds = getListString(docIds);
        List<String> tempDestTypeIds = getListString(destDocTypeIds);
        docmentBs.copyDocuments(tempDocIds, tempDestTypeIds);
        String docTypeID = request.getParameter("docTypeID");
        request.setAttribute("docTypeID", docTypeID);
        return request.findForward(DOCUMENT_QUERY_LIST);
        // return listDocumentsByType(from,request,response);
    }

    /**
     * 根据条件查询数据
     *
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward queryDocuments(DefaultForm from, IRequest request,
                                   IResponse response) throws Exception {
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        List result;
        String docScope = request.getParameter("docScope");
        String webSiteId = request.getParameter("siteId");

        DocumentVo docVo = new DocumentVo();
        if (!Helper.populate(docVo, request)) {
            return MessageAgent.sendErrorMessage(request, POPULATE_ERROR_MSG_STR, MessageStyle.ALERT_AND_BACK);
        }
        //如果选择按栏目查询，但未有栏目ID仍按本站点查询。
        if (docScope.equals(DOCSCOPE_DOC_TYPE)) {
            if (docVo.getDocTypeID() == null || docVo.getDocTypeID().equals("")) {
                docScope = DOCSCOPE_WEB_SITE;
            }
        }

        //获得PAGEVO
        PageVo pageVo = Helper.findPageVo(request);
        if (pageVo != null) {
            pageVo = Helper.updatePageVo(pageVo, request);
        } else {
            if (docScope.equals(DOCSCOPE_DOC_TYPE)) {
                int recordCount = docmentBs.getDocCountByDocConditionDocTypeActiveAndNotPublished(docVo);
                pageVo = Helper.createPageVo(request, recordCount);
            } else {
                int recordCount = docmentBs.queryAllDocRecordCountByWebSiteId(docVo, webSiteId);
                pageVo = Helper.createPageVo(request, recordCount);
            }
        }

        String orderStr = Helper.findOrderStr(request);

        if (docScope.equals(DOCSCOPE_DOC_TYPE)) {
            result = docmentBs.queryAllDocByDocConditionDocTypeActiveAndNotPublished(pageVo.getCurrentPage(), pageVo.getPageSize(), orderStr, docVo);
        } else {
            result = docmentBs.queryAllDocByWebSiteId(pageVo.getCurrentPage(), pageVo.getPageSize(), orderStr, docVo, webSiteId);
        }
        //保存排序字段
        Helper.saveOrderStr(orderStr, request);
        //保存分页信息
        Helper.savePageVo(pageVo, request);
        //获得数据集
        request.setAttribute("documents", result);
        //vo带入下个页面
        request.setAttribute("docVo", docVo);
        request.setAttribute("docScope", docScope);
        //为分页提供cmd参数
        request.setAttribute("cmd", "queryDocuments");
        //回写栏目编码一遍栏目预览时使用。
        request.setAttribute("docTypeCode", this.getDocTypeCodeByDocTypeID(docVo.getDocTypeID()));
        //回写表单
        request.setAttribute(WRITE_BACK_FORM_VALUES_KEY, VoHelper.getMapFromRequest(request));
        return request.findForward(DOCUMENT_LIST_KEY);
    }

    /**
     * 更新文章状态为发布状态。发布后的文章前台页面才可以查看到
     *
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward publishDocuments(DefaultForm from, IRequest request,
                                     IResponse response) throws Exception {
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        String docIds = request.getParameter("docIds");
        String siteId = request.getParameter("siteId");
        List<String> tempDocIds = getListString(docIds);
        for (String docId : tempDocIds) {
            Document doc = docmentBs.findDocById(docId);
            String status = doc.getStatus();
            if (DOC_STATUS_UNPUBLISHED.equals(status)) {
                Timestamp ts = new Timestamp((new Date()).getTime());
                doc.setPublishTime(ts);
                doc.setStatus(DOC_STATUS_PUBLISHED); //设置为发布状态
                docmentBs.updatePublish(doc, siteId);
            }

        }
        String docTypeID = request.getParameter("docTypeID");
        request.setAttribute("docTypeID", docTypeID);
        return request.findForward(DOCUMENT_QUERY_LIST);
        //return listDocumentsByType(from,request,response);
    }


    /**
     * 撤销文章发布状态状态修改为待发布状态。发布后的文章前台页面才可以查看到
     *
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward canclePublishDocuments(DefaultForm from, IRequest request,
                                           IResponse response) throws Exception {
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        String docIds = request.getParameter("docIds");
        String siteId = request.getParameter("siteId");
        List<String> tempDocIds = getListString(docIds);
        for (String docId : tempDocIds) {
            Document doc = docmentBs.findDocById(docId);
            String status = doc.getStatus();
            if (DOC_STATUS_PUBLISHED.equals(status)) {
                Timestamp ts = new Timestamp((new Date()).getTime());
                doc.setEditTime(ts); //更新编辑时间
                doc.setPublishTime(null);
                doc.setStatus(DOC_STATUS_UNPUBLISHED); //设置为待发布状态
                docmentBs.canclePublish(doc, siteId);
            }

        }
        String docTypeID = request.getParameter("docTypeID");
        request.setAttribute("docTypeID", docTypeID);
        return request.findForward(DOCUMENT_QUERY_LIST);
        //return listDocumentsByType(from,request,response);
    }

    /**
     * 更新文章状态为待发布状态。由上级管理人员确认发布
     *
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward submitDocuments(DefaultForm from, IRequest request,
                                    IResponse response) throws Exception {
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        String docIds = request.getParameter("docIds");
        List<String> tempDocIds = getListString(docIds);
        for (String docId : tempDocIds) {
            Document doc = docmentBs.findDocById(docId);
            String status = doc.getStatus();
            if (DOC_STATUS_DRAFT.equals(status)) {
                Timestamp ts = new Timestamp((new Date()).getTime());
                doc.setEditTime(ts); //更新编辑时间
                doc.setPublishTime(null);
                doc.setStatus(DOC_STATUS_UNPUBLISHED); //设置为待发布状态
                docmentBs.updateSubmit(doc);
            }

        }
        String docTypeID = request.getParameter("docTypeID");
        request.setAttribute("docTypeID", docTypeID);
        return request.findForward(DOCUMENT_QUERY_LIST);
        // return listDocumentsByType(from,request,response);
    }

    /**
     * 根据栏目ID获取前台页面所需的文章数据
     *
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward getFrontDocuments(DefaultForm from, IRequest request,
                                      IResponse response) throws Exception {
        String docTypeId = request.getParameter("docTypeId");
        String orderStr = request.getParameter("orderStr");
        String tempCurrentPage = request.getParameter("currentPage");
        int currentPage = Integer.parseInt(tempCurrentPage);
        String tempPageSize = request.getParameter("pageSize");
        ;
        int pageSize = Integer.parseInt(tempPageSize);
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        PageResults<Document> results = docmentBs.queryAllDocByDocTypeId(docTypeId, currentPage, pageSize, orderStr);
        request.setAttribute("pageresult", results);
        request.setAttribute("pageNo", currentPage);
        return request.findForward("toFront");
    }

    public IForward getSharedIds(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String docId = request.getParameter("docId");
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        List<String> docTypeIds = docmentBs.getDocTypeIdsByDocId(docId, DOCTYPE_DOC_ACTIVE);//获取此文档的父栏目ID，包括共享栏目，
        StringBuffer tempIds = new StringBuffer();
        for (int i = 0; i < docTypeIds.size(); i++) {
            String temp = docTypeIds.get(i);
            tempIds.append(temp);
            if (i < docTypeIds.size() - 1) {
                tempIds.append(",");
            }
        }
        response.getServletResponse().setContentType("text/plain; charset=UTF-8");
        response.getServletResponse().getWriter().print(tempIds.toString());
        response.getServletResponse().getWriter().flush();
        return null;
    }

    /**
     * 取消某一文章共享，也就是更新文章与栏目的关系
     *
     * @param from
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public IForward cancelShareDocument(DefaultForm from, IRequest request,
                                        IResponse response) throws Exception {
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        String docId = request.getParameter("docId");
        String destDocTypeIds = request.getParameter("destDocTypeIds");
        List<String> tempDestTypeIds = getListString(destDocTypeIds);
        List<String> originalDocTypeIds = docmentBs.getDocTypeIdsByDocId(docId, DOCTYPE_DOC_ACTIVE);
        Document doc = docmentBs.findDocById(docId);
        docmentBs.update(doc, originalDocTypeIds, tempDestTypeIds);
        String docTypeID = request.getParameter("docTypeID");
        request.setAttribute("docTypeID", docTypeID);
        return request.findForward(DOCUMENT_QUERY_LIST);
        //  return listDocumentsByType(from,request,response);
    }

    /**
     * 根据文章ID 以及当前进行的文章状态改变，来验证需要进行状态改变的文章是否都满足条件
     * 将结果通过jquery 返回到前台
     */
    public IForward checkDocStatus(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String docIds = request.getParameter("docIds");
        String status = request.getParameter("status");
        List<String> tempDocIds = getListString(docIds);
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        String result = "1"; //返回页面JS 的验证结果
        for (String docId : tempDocIds) {
            Document doc = docmentBs.findDocById(docId);
            String docStatus = doc.getStatus();
            if (!status.equals(docStatus)) {
                result = "0";
            }
        }
        response.getServletResponse().setContentType("text/plain; charset=UTF-8");
        response.getServletResponse().getWriter().print(result);
        response.getServletResponse().getWriter().flush();
        return null;
    }

    /**
     * 根据文章ID 以及当前进行的文章状态改变，来验证需要进行状态改变的文章是否都满足条件
     * 将结果通过jquery 返回到前台
     */
    public IForward getParentDocTypeIdByDocId(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String docIds = request.getParameter("docIds");
        List<String> tempDocIds = getListString(docIds);
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        String result = ""; //返回页面JS 的验证结果
        for (String docId : tempDocIds) {
            Document doc = docmentBs.findDocById(docId);
            String docTypeId = doc.getDocTypeID();
            result += "," + docTypeId;
        }
        if (!"".equals(result)) {
            result = result.substring(1);
        }
        response.getServletResponse().setContentType("text/plain; charset=UTF-8");
        response.getServletResponse().getWriter().print(result);
        response.getServletResponse().getWriter().flush();
        return null;
    }

    public IForward insertDocIndexAllByWebSiteIds(DefaultForm formBean, IRequest request, IResponse response) throws Exception {
        String websiteIds = request.getParameter("websiteIds");
        IDocumentBS docmentBs = (IDocumentBS) Helper.getBean(DOCUMENT_BS);
        docmentBs.insertDocIndexAllByWebSiteIds(websiteIds);

        String result = "{result:\"Y\"}"; //返回页面JS 的验证结果
        response.getServletResponse().setContentType("text/plain; charset=UTF-8");
        response.getServletResponse().getWriter().print(result);
        response.getServletResponse().getWriter().flush();
        return null;
    }

}
