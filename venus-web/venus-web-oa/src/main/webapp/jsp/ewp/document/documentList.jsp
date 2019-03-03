<!--
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="venus.frames.mainframe.util.VoHelper"%>
<%@ page import="venus.frames.i18n.util.LocaleHolder" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="udp.ewp.util.ServletContextHelper" %>
<script src="<%=request.getContextPath()%>/dwrewp/interface/DocumentUtilAjax.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/dwr/engine.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/dwr/util.js" type="text/javascript"></script>
<fmt:bundle basename="udp.ewp.ewp_resource" prefix="udp.ewp.">
<html>
<title>documentList</title>
<%
    String docTypeID = "";
    String docTypeCode = "";
    if (request.getParameter("docTypeID") != null) {
        docTypeID = request.getParameter("docTypeID");
    } else if (request.getAttribute("docTypeID") != null) {
        docTypeID = (String) request.getAttribute("docTypeID");
    }
    if (request.getParameter("docTypeCode") != null) {
        docTypeCode = request.getParameter("docTypeCode");
    } else if (request.getAttribute("docTypeCode") != null) {
        docTypeCode = (String) request.getAttribute("docTypeCode");
    }
    String site_code = (String) session.getAttribute("site_code");
    if (site_code == null || site_code.isEmpty()) {
        site_code = ServletContextHelper.getDefaultWebsiteCode();
    }

    String cmd = (String) request.getAttribute("cmd");
    String context = request.getContextPath();
%>
<script>
function launchScrollCenter(url, name, width, height) {
    var str = "height=" + height + ",innerHeight=" + height;
    str += ",width=" + width + ",innerWidth=" + width;
    if (window.screen) {
        var ah = screen.availHeight - 30;
        var aw = screen.availWidth - 10;
        var xc = (aw - width) / 2;
        var yc = (ah - height) / 2;
        str += ",left=" + xc + ",screenX=" + xc;
        str += ",top=" + yc + ",screenY=" + yc;
    }
    str += ',resizable=1,scrollbars=yes,menubar=no,status=no';
    window.open(url, name, str);
}

//---------检查复选框选项，获取一条记录id------------------------------------------------
function selectItem_checkBox() {
    var elementCheckbox = document.getElementsByName("checkbox_document");
    var number = 0;
    var ids = null;
    for (var i = 0; i < elementCheckbox.length; i++) {
        if (elementCheckbox[i].checked) {
            number += 1;
            ids = elementCheckbox[i].value;
        }
    }
    if (number == 0) {
        alert('<fmt:message key="select_one_record"/>');
        return false;
    }
    if (number > 1) {
        alert('<fmt:message key="only_can_a_record"/>');
        return false;
    }
    return ids;
}

//---------通过页面id检查记录是否发布------------------------------------------------
function checkDocStatusIsPublished(docId) {
    var status = document.getElementById(docId).value;
    if (status == 2) {
        return true;
    } else {
        return false;
        ;
    }
}

//---------新建文档页面---------------------------------------------------------------------
function pre_addDocument_onClick() {
    //访问父窗口的form对象
    var documentForm = parent.document.forms[0];
    //重新还原form的目标窗口，默认是本窗口
    documentForm.target = "";
    var docTypeIDValue = document.getElementById("docTypeID").value;
    if (docTypeIDValue == null || docTypeIDValue == "") {
        alert('<fmt:message key="select_one_doctype"/>');
        return false;
    }
    documentForm.action = "<venus:base/>/jsp/ewp/document/addDocumentNew.jsp?docTypeID=" +
            docTypeIDValue + "&siteId=" + parent.getSiteId() + "&operationType=addDocument";
    documentForm.submit();

}

//---------编辑文档---------------------------------------------------------------------
function editorDocument_onClick() {
    //访问父窗口的form对象
    var documentForm = parent.document.forms[0];
    documentForm.target = "";
    var documentID = selectItem_checkBox();
    if (checkDocStatusIsPublished(documentID)) {
        alert('<fmt:message key="doc_status_not_permit_edit" />');
        return;
    }

    var docTypeIDValue = document.getElementById("docTypeID").value;
    if (documentID) {
        documentForm.action = "<venus:base/>/document.do?cmd=preEditorDocument&documentID=" +
                documentID + "&siteId=" + parent.getSiteId() + "&operationType=editorDocument&docTypeID=" + docTypeIDValue;
        documentForm.submit();
    }
}

//---------查看文档---------------------------------------------------------------------
function viewDocument_onClick() {
    //访问父窗口的form对象
    var documentForm = parent.document.forms[0];
    documentForm.target = "";
    var documentID = selectItem_checkBox();
    var docTypeIDValue = document.getElementById("docTypeID").value;
    if (documentID) {
        documentForm.action = "<venus:base/>/document.do?cmd=preEditorDocument&documentID=" +
                documentID + "&siteId=" + parent.getSiteId() + "&operationType=viewDocument&docTypeID=" + docTypeIDValue;
        documentForm.submit();
    }
}

//---------转移文档---------------------------------------------------------------------
function moveDocument_onClick() {

    var elementCheckbox = document.getElementsByName("checkbox_document");
    var number = 0;
    var ids = "";
    for (var i = 0; i < elementCheckbox.length; i++) {
        if (elementCheckbox[i].checked) {
            number += 1;
            ids = elementCheckbox[i].value + "," + ids;
        }
    }
    if (number == 0) {
        alert('<fmt:message key="select_one_record"/>');
        return;
    }
    getNewParentDoctype("RADIO", null, "moveDocument", ids);
}
//---------共享文档---------------------------------------------------------------------
function shareDocument_onClick() {
    var elementCheckbox = document.getElementsByName("checkbox_document");
    var number = 0;
    var ids = "";
    for (var i = 0; i < elementCheckbox.length; i++) {
        if (elementCheckbox[i].checked) {
            number += 1;
            ids = elementCheckbox[i].value + "," + ids;
        }
    }
    if (number == 0) {
        alert('<fmt:message key="select_one_record"/>');
        return;
    }
    getNewParentDoctype("CHECK", null, "shareDocument", ids);
}

//---------取消共享文档---------------------------------------------------------------------
function unShareDocument_onClick() {
    var documentID = selectItem_checkBox();
    if (!documentID) {
        return;
    }
    jQuery.get("<venus:base/>/document.do?cmd=getParentDocTypeIdByDocId", {Action: "get", docIds: documentID, async: false}, function (data, textStatus) {
        var docTypeIDValue = data;
        jQuery.get("<venus:base/>/document.do?cmd=getSharedIds", {Action: "get", docId: documentID, async: false}, function (data, textStatus) {
            getNewParentDoctype("CHECK", data, "unShareDocument", documentID, docTypeIDValue);
        });
    });

}
//---------拷贝文档---------------------------------------------------------------------
function copyDocument_onClick() {
    var elementCheckbox = document.getElementsByName("checkbox_document");
    var number = 0;
    var ids = "";
    for (var i = 0; i < elementCheckbox.length; i++) {
        if (elementCheckbox[i].checked) {
            number += 1;
            ids = elementCheckbox[i].value + "," + ids;
        }
    }
    if (number == 0) {
        alert('<fmt:message key="select_one_record"/>');
        return;
    }
    getNewParentDoctype("CHECK", null, "copyDocument", ids);
}
//---------审核管理人员发布文档---------------------------------------------------------------------
function publishDocument_onClick() {
    var elementCheckbox = document.getElementsByName("checkbox_document");
    var number = 0;
    var ids = "";
    for (var i = 0; i < elementCheckbox.length; i++) {
        if (elementCheckbox[i].checked) {
            number += 1;
            ids = elementCheckbox[i].value + "," + ids;
        }
    }
    if (number == 0) {
        alert('<fmt:message key="select_one_record"/>');
        return;
    }

    jQuery.get("<venus:base/>/document.do?cmd=checkDocStatus", {Action: "get", docIds: ids, status: 1, async: false}, function (data, textStatus) {
        if (data == "1") {
            var documentForm = document.forms[0];
            var docTypeIDValue = jQuery("#docTypeID").val();
            documentForm.action = "<venus:base/>/document.do?cmd=publishDocuments&docIds=" + ids + "&operationType=publishDocuments&docTypeID=" + docTypeIDValue + "&siteId=" + parent.getSiteId();
            documentForm.submit();
        } else if (data == "0") {
            alert('<fmt:message key="doc_publish_error"/>');
        } else {
            alert('<fmt:message key="other_error"/>');
        }
    });
}

function unpublishDocument_onClick() {
    var elementCheckbox = document.getElementsByName("checkbox_document");
    var number = 0;
    var ids = "";
    for (var i = 0; i < elementCheckbox.length; i++) {
        if (elementCheckbox[i].checked) {
            number += 1;
            ids = elementCheckbox[i].value + "," + ids;
        }
    }
    if (number == 0) {
        alert('<fmt:message key="select_one_record"/>');
        return;
    }
    if (confirm('<fmt:message key="determine_unpublish_the_document"/>')) {
        jQuery.get("<venus:base/>/document.do?cmd=checkDocStatus", {Action: "get", docIds: ids, status: 2, async: false}, function (data, textStatus) {
            if (data == "1") {
                var documentForm = document.forms[0];
                var docTypeIDValue = jQuery("#docTypeID").val();
                documentForm.action = "<venus:base/>/document.do?cmd=canclePublishDocuments&docIds=" + ids + "&docTypeID=" + docTypeIDValue + "&siteId" + parent.getSiteId();
                documentForm.submit();
            } else if (data == "0") {
                alert('<fmt:message key="doc_unpublish_error"/>');
            } else {
                alert('<fmt:message key="other_error"/>');
            }
        });
    }
}

//---------编辑人员提交文档---------------------------------------------------------------------
function submitDocument_onClick() {
    var elementCheckbox = document.getElementsByName("checkbox_document");
    var number = 0;
    var ids = "";
    for (var i = 0; i < elementCheckbox.length; i++) {
        if (elementCheckbox[i].checked) {
            number += 1;
            ids = elementCheckbox[i].value + "," + ids;
        }
    }
    if (number == 0) {
        alert('<fmt:message key="select_one_record"/>');
        return;
    }
    jQuery.get("<venus:base/>/document.do?cmd=checkDocStatus", {Action: "get", docIds: ids, status: 0, async: false}, function (data, textStatus) {
        if (data == "1") {
            var documentForm = document.forms[0];
            var docTypeIDValue = jQuery("#docTypeID").val();
            documentForm.action = "<venus:base/>/document.do?cmd=submitDocuments&docIds=" + ids + "&operationType=submitDocuments&docTypeID=" + docTypeIDValue;
            documentForm.submit();
        } else if (data == "0") {
            alert('<fmt:message key="doc_submit_error"/>');
        } else {
            alert('<fmt:message key="other_error"/>');
        }
    });
}

//---------删除文档---------------------------------------------------------------------
function deleteDocument_onClick(bookId) {
    var elementCheckbox = document.getElementsByName("checkbox_document");
    var number = 0;
    var ids = "";
    for (var i = 0; i < elementCheckbox.length; i++) {
        if (elementCheckbox[i].checked) {
            number += 1;
            var docId = elementCheckbox[i].value;
            if (checkDocStatusIsPublished(docId)) {
                alert('<fmt:message key="doc_status_not_permit_del" />');
                return;
            } else {
                ids = elementCheckbox[i].value + "," + ids;
            }
        }
    }

    if (number == 0) {
        alert('<fmt:message key="select_one_record"/>');
        return;
    }
    if (confirm('<fmt:message key="determine_delete_the_document"/>')) {
        var documentForm = document.forms[0];
        var docTypeIDValue = document.getElementById("docTypeID").value;
        documentForm.action = "<venus:base/>/document.do?cmd=deleteDocuments&docIds=" + ids + "&operationType=deleteDocuments&docTypeID=" + docTypeIDValue;
        documentForm.submit();
    }
}


//取得上级栏目
function getParentDoctype(checkType, sharedIds) {
    var refPath = "<venus:base/>/jsp/ewp/docType/docTypeRef.jsp?checktype=" + checkType + "&tree_href=none&selectedValues=" + sharedIds;
    var parentDoctype = window.showModalDialog(refPath, new Object(), 'dialogHeight=500px;dialogWidth=350px;center:yes;help:no;resizable:yes;scroll:yes;status:no;');
    var jsonParentDoctype = eval('(' + parentDoctype + ')');
    if (checkType == "CHECK") {
        jQuery("#destDocTypeIds").val(jsonParentDoctype.ids);
    } else {
        jQuery("#destDocTypeId").val(jsonParentDoctype.ids);
    }
    //  jQuery("#add_parent_name").val(jsonParentDoctype.chinesenames);
    return parentDoctype;
}


function getNewParentDoctype(checkType, sharedIds, method, docIds) {
    parent.getNewParentDoctype(checkType, sharedIds, method, docIds);
}

function getNewParentDoctype(checkType, sharedIds, method, docIds, disableItemID) {
    parent.getNewParentDoctype(checkType, sharedIds, method, docIds, disableItemID);
}

//弹出选择栏目回调操作。
//移动文档
function moveDocument(idnames) {
    var idnames = eval("(" + idnames + ")");
    var docIds = idnames.docIds;
    var docTypeIds = idnames.docTypeIds;
    var chinesenames = idnames.chinesenames;
    if (confirm('<fmt:message key="determine_transfer_the_document"/>')) {
        if (docTypeIds != '' && docTypeIds != undefined) {
            jQuery("#destDocTypeId").val(docTypeIds);
            var documentForm = document.forms[0];
            var docTypeIDValue = jQuery("#docTypeID").val(); //document.getElementById("docTypeID").value;
            documentForm.action = "<%=context%>/document.do?cmd=moveDocument&docIds=" + docIds + "&docTypeID=" + docTypeIDValue + "&operationType=moveDocument";
            documentForm.submit();
        } else {
            jQuery("#destDocTypeIds").val('');
        }
        parent.callbackCancel();
    }
}
//弹出选择栏目回调操作。
//共享文档
function shareDocument(idnames) {
    var idnames = eval("(" + idnames + ")");
    var docIds = idnames.docIds;
    var docTypeIds = idnames.docTypeIds;
    var chinesenames = idnames.chinesenames;
    if (confirm('<fmt:message key="determine_share_the_document"/>')) {
        if (docTypeIds != '' && docTypeIds != undefined) {
            jQuery("#destDocTypeIds").val(docTypeIds);
            var documentForm = document.forms[0];
            var docTypeIDValue = jQuery("#docTypeID").val();
            documentForm.action = "<%=context%>/document.do?cmd=shareDocument&docIds=" + docIds + "&operationType=shareDocument&docTypeID=" + docTypeIDValue;
            documentForm.submit();
        } else {
            jQuery("#destDocTypeIds").val('');
        }
        parent.callbackCancel();
    }
}
//弹出选择栏目回调操作。
//取消共享文档
function unShareDocument(idnames) {
    var idnames = eval("(" + idnames + ")");
    var docIds = idnames.docIds;
    var docTypeIds = idnames.docTypeIds;
    var chinesenames = idnames.chinesenames;
    if (confirm('<fmt:message key="determine_unshare_the_document"/>')) {
        if (docTypeIds != '' && docTypeIds != undefined) {
            jQuery("#destDocTypeIds").val(docTypeIds);
            var documentForm = document.forms[0];
            var docTypeIDValue = jQuery("#docTypeID").val();
            documentForm.action = "<%=context%>/document.do?cmd=cancelShareDocument&docId=" + docIds + "&docTypeID=" + docTypeIDValue;
            documentForm.submit();
        } else {
            jQuery("#destDocTypeIds").val('');
        }
        parent.callbackCancel();
    }
}
//弹出选择栏目回调操作。
//拷贝文档
function copyDocument(idnames) {
    var idnames = eval("(" + idnames + ")");
    var docIds = idnames.docIds;
    var docTypeIds = idnames.docTypeIds;
    var chinesenames = idnames.chinesenames;
    if (confirm('<fmt:message key="determine_copy_the_document"/>')) {
        if (docTypeIds != '' && docTypeIds != undefined) {
            jQuery("#destDocTypeIds").val(docTypeIds);
            var documentForm = document.forms[0];
            var docTypeIDValue = jQuery("#docTypeID").val();
            documentForm.action = "<%=context%>/document.do?cmd=copyDocument&docIds=" + docIds + "&operationType=copyDocument&docTypeID=" + docTypeIDValue;
            documentForm.submit();
        } else {
            jQuery("#destDocTypeIds").val('');
        }
        parent.callbackCancel();
    }
}

</script>

</head>

<body align="center">
<form id="docForm" name="form" method="post" action="<venus:base/>/document.do">
    <input type="hidden" name="cmd" value="<%=cmd!=null? cmd :""%>">
    <!-- 以下参数提供给翻页查询 -->
    <input type="hidden" name="docTypeID" id="docTypeID" value="<%=(docTypeID==null)?"":docTypeID%>"/>
    <input type="hidden" name="destDocTypeId" id="destDocTypeId"/>
    <input type="hidden" name="destDocTypeIds" id="destDocTypeIds"/>
    <input type="hidden" name="title" id="title"/>
    <input type="hidden" name="status" id="status"/>
    <input type="hidden" name="docScope" id="docScope"/>
    <input type="hidden" name="createBy" id="createBy"/>
    <input type="hidden" name="tag" id="tag"/>
    <input type="hidden" name="sharedIds" id="sharedIds"/>

    <div id="ccParent1">
        <table class="table_div_control">
            <tr>
                <td>
                    <!--	<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;明细表格-->

                    <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" id="image1"
                         class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key="detailtable"/>
                </td>
                <td>
                    <table align="right">
                        <tr>
                            <td><input type="button" class="button_ellipse" id="viewDoc_btn"
                                       onClick="javascript:viewDocument_onClick();"
                                       value='<fmt:message key="view"  bundle="${applicationResources}"/>'></td>
                            <td><input type="button" class="button_ellipse" id="addDoc_btn"
                                       onClick="javascript:pre_addDocument_onClick();"
                                       value='<fmt:message key="add" />'></td>
                            <td><input type="button" class="button_ellipse"
                                       onClick="javascript:editorDocument_onClick();"
                                       value='<fmt:message key="edit" />'></td>
                            <td><input type="button" class="button_ellipse" onClick="javascript:moveDocument_onClick();"
                                       value='<fmt:message key="move" />'></td>
                            <td><input type="button" class="button_ellipse"
                                       onClick="javascript:shareDocument_onClick();"
                                       value='<fmt:message key="share" />'></td>
                            <td><input type="button" class="button_ellipse"
                                       onClick="javascript:unShareDocument_onClick();"
                                       value='<fmt:message key="unshared" />'></td>
                            <td><input type="button" class="button_ellipse" onClick="javascript:copyDocument_onClick();"
                                       value='<fmt:message key="copy" />'></td>
                            <td><input type="button" class="button_ellipse"
                                       onClick="javascript:submitDocument_onClick();"
                                       value='<fmt:message key="submit" />'></td>
                            <td><input type="button" class="button_ellipse"
                                       onClick="javascript:publishDocument_onClick();"
                                       value='<fmt:message key="published" />'></td>
                            <td><input type="button" class="button_ellipse"
                                       onClick="javascript:unpublishDocument_onClick();"
                                       value='<fmt:message key="unpublished" />'></td>
                            <td><input type="button" class="button_ellipse"
                                       onClick="javascript:deleteDocument_onClick();"
                                       value='<fmt:message key="delete" />'></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>

    <div id="ccChild1">
        <table class="table_div_content">
            <tr>
                <td>

                    <layout:collection name="documents" id="document" styleClass="listCss" width="100%"
                                       indexId="orderNumber" align="center" sortAction="0">
                        <layout:collectionItem width="5%" title='<%=LocaleHolder.getMessage("sequence") %>'
                                               style="text-align:center;">
                            <venus:sequence/>
                        </layout:collectionItem>
                        <layout:collectionItem width="5%"
                                               title="<input type='checkbox' pdType='control' control='checkbox_document'/>"
                                               style="text-align:center;">
                            <bean:define id="documentId" name="document" property="id"/>
                            <input type="checkbox" name="checkbox_document" value="<%=documentId%>"/>
                        </layout:collectionItem>
                        <layout:collectionItem width="28%" title='<%=LocaleHolder.getMessage("title") %>'
                                               property="title" sortable="true" style="text-align:center;"/>
                        <layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.ewp.author") %>'
                                               property="createBy" sortable="true" style="text-align:center;"/>
                        <layout:collectionItem width="7%" title='<%=LocaleHolder.getMessage("status") %>'
                                               property="status" sortable="true" style="text-align:center;">
                            <bean:define id="documentId" name="document" property="id"/>
                            <bean:define id="status" name="document" property="status"/>
                            <input type="hidden" id="<%=documentId%>" name="<%=documentId%>" value="<%=status%>"/>
                            <logic:equal name="document" property="status" value="0">
                                <fmt:message key="draft"/>
                            </logic:equal>
                            <logic:equal name="document" property="status" value="1">
                                <fmt:message key="to_be_released"/>
                            </logic:equal>
                            <logic:equal name="document" property="status" value="2">
                                <fmt:message key="has_been_published"/>
                            </logic:equal>
                            <logic:equal name="document" property="status" value="3">
                                <fmt:message key="archive"/>
                            </logic:equal>
                        </layout:collectionItem>
                        <layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.ewp.doctype.sort") %>'
                                               property="sortNum" sortable="true" style="text-align:center;"/>
                        <layout:collectionItem width="15%" title='<%=LocaleHolder.getMessage("udp.ewp.releaseddate") %>'
                                               property="publishTime" sortable="true" style="text-align:center;"/>

                    </layout:collection>

                    <jsp:include page="/jsp/include/page.jsp"/>
                    <table>
                        <tr>
                            <td>
                                <a href="javascript:launchScrollCenter('<%=request.getContextPath()%>/<%=site_code%>/articles/<%=docTypeCode%>','chnPreviewWindow', 800, 600);"><%=LocaleHolder.getMessage("udp.ewp.doctype_preview") %>
                                </a></td>
                            <td></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
</form>
</body>
</html>
</fmt:bundle>
<% //表单回写
    if (request.getAttribute("writeBackFormValues") != null) {
        out.print("<script language=\"javascript\">\n");
        out.print(VoHelper.writeBackMapToForm((java.util.Map) request.getAttribute("writeBackFormValues")));
        out.print("writeBackMapToForm();\n");
        out.print("</script>");
    }
%>