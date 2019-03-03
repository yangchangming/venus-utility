<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="venus.commons.xmlenum.EnumRepository" %>
<%@ page import="venus.commons.xmlenum.EnumValueMap"%>
<%@ page import="java.util.*" %>
<%@ page import="udp.ewp.tree.util.ITreeConstants" %>
<%
    String docTypeID = "";
    if (request.getParameter("docTypeID") != null) {
        docTypeID = request.getParameter("docTypeID");
    } else if (request.getAttribute("docTypeID") != null) {
        docTypeID = (String) request.getAttribute("docTypeID");
    }

    String siteID = "";
    if (request.getParameter("siteId") != null) {
        siteID = request.getParameter("siteId");
    } else if (request.getAttribute("siteId") != null) {
        siteID = (String)request.getAttribute("siteId");
    }

    EnumRepository er = EnumRepository.getInstance();
    er.loadFromDir();
    EnumValueMap documentStatusMap = er.getEnumValueMap("DocumentStatus");
    EnumValueMap docScopeMap = er.getEnumValueMap("DocumentQueryScope");
%>
<fmt:bundle basename="udp.ewp.ewp_resource" prefix="udp.ewp.">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Gap-EWP</title>
    <link rel="stylesheet" href="<venus:base/>/css/ewp/zTreeStyle.css" type="text/css">
    <link rel="stylesheet" href="<venus:base/>/css/ewp/ewptree.css" type="text/css">
    <script type="text/javascript" src="<venus:base/>/js/jquery/ztree/jquery.ztree.all-3.5.min.js"></script>
    <script type="text/javascript">
        var setting = {
            async: {
                enable: true,
                url: "<%=request.getContextPath()%>/doctype/tree",
                autoParam: ["id=pId", "siteId"],
                otherParam: {
                    "<%=ITreeConstants.TREE_LINK_HREF%>": "/document.do?cmd=listDocumentsByType",
                    "<%=ITreeConstants.TREE_LINK_TARGET%>": "documentList"
                }
            },
            callback: {
                onClick: setDocTypeForSearch
            },
            view: {
                showLine: false
            }
        };
        var hangDoctypeDialog = null;

        $(document).ready(function () {
            $.fn.zTree.init($("#tree"), setting);
            hangDoctypeDialog = jQuery("#referenceHangDoctype").dialog({bgiframe: true, modal: true, height: 475, autoOpen: false, resizable: false, width: 368, position: ["center", "top"], overlay: { opacity: 0.3, background: "black" }});
        });

        function getSiteId() {
            var ztree = jQuery.fn.zTree.getZTreeObj("tree");
            var selNode = ztree.getSelectedNodes();
            if (selNode[0]) {
                return selNode[0].siteId;
            } else {
                return "<%=siteID%>";
            }
        }

        //查询
        function simpleQuery_onClick() {
            with (document.documentForm) {
                document.documentForm.elements['cmd'].value = 'queryDocuments';
                document.documentForm.elements['siteId'].value = getSiteId();
                document.forms[1].target = "documentList";
                var var_docScope = document.documentForm.elements['docScope'].value;
                var var_docTypeID = document.documentForm.elements['docTypeID'].value;
                if (var_docScope == "1" && (var_docTypeID == "" || var_docTypeID == null)) {
                    alert('<fmt:message key="choose_doc_type"/>');
                    return;
                }
            }
            document.forms[1].submit();
        }

        function getNewParentDoctype(checkType, sharedIds, method, docIds) {
            getNewParentDoctype(checkType, sharedIds, method, docIds, null);
        }

        function getNewParentDoctype(checkType, sharedIds, method, docIds, disableItemID) {
            var refPath = "<venus:base/>/jsp/ewp/docType/docTypeRef_hang.jsp?checktype=" + checkType + "&docIds=" + docIds + "&method=" + method + "&tree_href=none&selectedValues=" + sharedIds + "&siteId=" + getSiteId() + "&methodCallbackOk=callbackOK&methodCallbackCancel=callbackCancel";
            if (disableItemID != null && disableItemID != "") {
                refPath += "&disableItemID=" + disableItemID;
            }
            hangDoctypeDialog.dialog('moveToTop')
            jQuery.get(refPath, {Action: "get", async: false}, function (data, textStatus) {
                jQuery("#referenceHangDoctype").html(data);
                jQuery('.ui-dialog-titlebar-close').hide();

                hangDoctypeDialog.dialog("open");
            });
        }

        function callbackOK(idnames, method) {
            var iframe = document.getElementById("documentList");
            iframe.contentWindow[method](idnames);
        }

        function callbackCancel() {
            jQuery("#referenceHangDoctype").html("");
            hangDoctypeDialog.dialog("close");
        }
        function setDocTypeForSearch(event, treeId, treeNode) {
            jQuery("input[name='docTypeID']").attr("value", treeNode.id);
        }

        //清空查询条件
        function clearForm_onClick(){
            with(document.documentForm){
                document.documentForm.elements['title'].value = "";
                document.documentForm.elements['createBy'].value = "";
                document.documentForm.elements['status'].value = "";
                document.documentForm.elements['seoKeyWord'].value = "";
                document.documentForm.elements['docScope'].value = "1";
            }
        }
    </script>
</head>
<body>
<div style="width:100%;">
    <script language="javascript">
        writeTableTop('<fmt:message key="doclist" />', '<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
    </script>
    <div>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td valign="top" align="left" width="80px">
                    <table id="leftTable" height="550px" width="220px" border="0" cellpadding="0" cellspacing="0"
                           bgcolor="#7EBAFF" style="margin-top:5px">
                        <tr>
                            <td bgcolor="#e1e1e1" style="height:20px" align="right"></td>
                        </tr>
                        <tr>
                            <td bgcolor="#f0efef" valign="top">
                                <form name="form_treebasic" method="post">
                                    <input id="webModel" name="webModel" type="hidden" class="text_field"
                                           inputName="发布目录" value="<%=request.getContextPath()%>" readonly="true">
                                </form>
                                <!-- 左侧树 -->
                                <ul id="tree" align="left" class="ztree"
                                    style="width: 220px; height: 100%; background-color: #e1e1e1; border: 0px solid #90b3cf;overflow:auto; MARGIN-LEFT: 0px;"></ul>
                            </td>
                        </tr>
                    </table>
                </td>
                <td style="width:1px">&nbsp;</td>
                <td valign="top" align="left">
                    <form name="documentForm" action="<venus:base/>/document.do" method="post">
                        <div id="ccParent0">
                            <table class="table_div_control">
                                <tr>
                                    <td>
                                        <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild0',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;
                                        <fmt:message key="query_with_condition" bundle="${applicationResources}"/></td>
                                </tr>
                            </table>
                        </div>
                        <input type="hidden" name="cmd" value="">
                        <input type="hidden" name="siteId" value="">
                        <input type="hidden" id="docTypeID" name="docTypeID"
                               value="<%=(docTypeID==null?"":docTypeID) %>">
                        <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                            <tr id="ccChild0" style="height: 60px">
                                <td align="center">
                                    <table class="table_div_content" height="100%" border="0">
                                        <tr>
                                            <td align="right" width="10%" nowrap><fmt:message key="title"
                                                                                              bundle="${applicationResources}"/></td>
                                            <td align="left" width="25%"><input name="title" type="text"
                                                                                class="text_field" inputName=''
                                                                                validate="isSearch" rubber_id="build_Id"
                                                                                value="${requestScope.title}"></td>
                                            <td align="right" width="10%" nowrap><fmt:message key="status"
                                                                                              bundle="${applicationResources}"/></td>
                                            <td align="left" width="25%"><select name="status" class="text_field"
                                                                                 validate="isSearch">
                                                <%
                                                    List docStatusList = documentStatusMap.getEnumList();
                                                    for (int i = 0; i < docStatusList.size(); i++) {
                                                %>
                                                <option value="<%=documentStatusMap.getValue(docStatusList.get(i).toString())%>"><%=docStatusList.get(i)%>
                                                </option>
                                                <%
                                                    }
                                                %>
                                            </select>
                                            </td>
                                            <td align="right" width="10%" nowrap><fmt:message key="scope"/></td>
                                            <td align="left" width="25%"><select name="docScope" class="text_field"
                                                                                 validate="isSearch">
                                                <%
                                                    List docScopeList = docScopeMap.getEnumList();
                                                    for (int i = 0; i < docScopeList.size(); i++) {
                                                %>
                                                <option value="<%=docScopeMap.getValue(docScopeList.get(i).toString())%>"><%=docScopeList.get(i)%>
                                                </option>
                                                <%
                                                    }
                                                %>
                                            </select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="right" width="10%" nowrap><fmt:message key="author"/></td>
                                            <td align="left" width="25%"><input name="createBy" type="text"
                                                                                class="text_field" inputName=''
                                                                                validate="isSearch"></td>
                                            <td align="right" width="10%" nowrap><fmt:message key="keyword"/></td>
                                            <td align="left" width="25%"><input name="seoKeyWord" type="text"
                                                                                class="text_field" inputName=''
                                                                                validate="isSearch"></td>
                                            <td colspan="2" align="center">
                                                <input type="button" name="Submit" class="button_ellipse"
                                                       value='<fmt:message key="query" bundle="${applicationResources}"/>'
                                                       onClick="javascript:simpleQuery_onClick();">
                                                <input type="button" name="clear" class="button_ellipse"
                                                       value='<fmt:message key="clear" bundle="${applicationResources}" />'
                                                       onclick="javascript:clearForm_onClick();"
                                                        ></td>
                                        </tr>
                                    </table>

                                </td>
                            </tr>
                            <tr>
                                <td align="center">
                                    <iframe id="documentList" name="documentList" width="100%" frameborder="0"
                                            src="<%=request.getContextPath()%>/document.do?cmd=listDocumentsByType&docTypeID=<%=docTypeID %>"></iframe>
                                </td>
                            </tr>
                        </table>
                    </form>
                </td>
            </tr>
        </table>
    </div>
    <div id="referenceHangDoctype" title="请选择要挂接至的栏目"></div>
    <script language="javascript">
        writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
        jQuery(document).ready(function () {
            document.getElementById("documentList").height = 0;
            changeHeight();
        });

        jQuery("#documentList").load(function () {
            changeHeight();
        });
        function changeHeight() {
            var iframe = document.getElementById("documentList");
            try {
                var bHeight = iframe.contentWindow.document.body.scrollHeight;
                var dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
                var height = Math.max(bHeight, dHeight);
                iframe.height = height;
            } catch (ex) {
            }
        }

        function getUrlParam(url, name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表
            var r = url.search.substr(1).match(reg);  //匹配目标参数
            if (r != null) return unescape(r[2]);
            return ""; //返回参数值
        }

        function setDocTypeForSearch(obj) {
            jQuery("input[name='docTypeID']").attr("value", getUrlParam(obj, "docTypeID"));
        }
    </script>
</div>
</body>
</html>
</fmt:bundle>
