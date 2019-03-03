<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<% //引用此页面的外围ｈｔｍｌ容器高度需要超过420,但不要设置太高，要不然背景留白太多。 %>
<fmt:bundle basename="udp.ewp.ewp_resource">
<html>
<head>
<%
    String checktype = request.getParameter("checktype");
    String selectedValues = request.getParameter("selectedValues");
    String tree_href = request.getParameter("tree_href");
    String notIncludeValues = request.getParameter("notIncludeValues") == null ? "" : request.getParameter("notIncludeValues");
    String method = request.getParameter("method");
    String methodCallbackOk = request.getParameter("methodCallbackOk");
    String methodCallbackCancel = request.getParameter("methodCallbackCancel");
    String docIds = request.getParameter("docIds");
    String disableItemID = (request.getParameter("disableItemID") == null ? "" : request.getParameter("disableItemID"));
    String hasSelectWebsite = (request.getParameter("hasSelectWebsite") == null ? "N" : request.getParameter("hasSelectWebsite"));

    String pType = (request.getParameter("pType") == null) ? "" : request.getParameter("pType");
    String partyId = (request.getParameter("partyId") == null) ? "" : request.getParameter("partyId");
    String siteId = (request.getParameter("siteId") == null) ? "" : request.getParameter("siteId");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择栏目</title>
<%
    response.setHeader("Cache-Control", "no-cache");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    String context = request.getContextPath();
%>
<script src="<%=request.getContextPath()%>/dwrewp/interface/EwpTreeControl.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/dwr/engine.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/dwr/util.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/ewp/gap-ewp-tree-ext.js" type="text/javascript"></script>
<script language="javascript">

    function callbackOkOperation() {
        var idnames = getSelectedDocType();
        <%=methodCallbackOk%>(idnames, "<%=method%>");
    }

    function callbackCancelOperation() {
        <%=methodCallbackCancel%>();
    }

    function getSelectedDocType() {
        var docTypeIds = new Array();
        var chinesenames = new Array();
        var allDocType = document.getElementsByName("selectDocType");
        for (var i = 0; i < allDocType.length; i++) {
            if (allDocType[i].checked) {
                docTypeIds.push(allDocType[i].value);
                chinesenames.push(allDocType[i].getAttribute("chinesename"));
            }
        }
        var docIds = "<%=docIds%>";
        var idnames = '{"docIds":"' + docIds + '","docTypeIds":"' + docTypeIds + '","chinesenames":"' + chinesenames + '"}';
        return idnames;
    }

    //树的根节点
    var rootID = "root";
    //用户调用的beanID
    var beanId = "docTypeBS";
    var checktype = "<%=checktype%>";
    var notIncludeValues = "<%=notIncludeValues%>";
    var disableItemID = "<%=disableItemID%>";
    var selectedValues = "<%=selectedValues%>";
    var site_id = "<%=siteId%>";
    //用户自定义初始化树
    function onLoadPopUpTree() {
        webModel = form_treebasic.webModel.value;
        var tree_onclick = "doClick(this)";  //单击事件方法名
        var tree_param = "docTypeID";           //链接参数名
        if (site_id == null || site_id == "") {
            return;
        }
        var paramJson = '{tree_href:"none",tree_onclick:"' + tree_onclick + '",tree_param:"",site_id:"' + site_id + '",webModel:"' + webModel + '",tree_target:"",checkType:"' + checktype + '",notIncludeValues:"' + notIncludeValues + '",disableItemID:"' + disableItemID + '",selectedValues:"' + selectedValues + '",treeid:"popUpTree"}';
        initTree(rootID, 'popUpTree', paramJson);
    }

    //单击展开/收缩 事件
    function expandAndCollapse(triggeredEvent) {
        webModel = form_treebasic.webModel.value;
        if (jQuery.browser.mozilla) {
            if (triggeredEvent.target.id == "foldheader" || triggeredEvent.target.id == "deptheader") {
                var $current = jQuery(triggeredEvent.target.nextSibling.nextSibling);
                var $next = $current.parent().next();
                var imagesrc = triggeredEvent.target.src;
                if ($next.css("display") == null || $next.css("display") == "none") {
                    $next.css("display", "block");
                    if (imagesrc.indexOf("root.png") == -1) {
                        triggeredEvent.target.src = webModel + "/images/tree/parentopen.gif";
                    }
                } else {
                    $next.css("display", "none");
                    if (imagesrc.indexOf("root.png") == -1) {
                        triggeredEvent.target.src = webModel + "/images/tree/parent.gif";
                    }
                }
            }
        } else {
            if (window.event.srcElement.id == "foldheader" || window.event.srcElement.id == "deptheader") {
                var $current = jQuery(window.event.srcElement.nextSibling.nextSibling);
                var $next = $current.parent().next();
                var imagesrc = window.event.srcElement.src;
                if ($next.css("display") == null || $next.css("display") == "none") {
                    $next.css("display", "block");
                    if (imagesrc.indexOf("root.png") == -1) {
                        window.event.srcElement.src = webModel + "/images/tree/parentopen.gif";
                    }
                } else {
                    $next.css("display", "none");
                    if (imagesrc.indexOf("root.png") == -1) {
                        window.event.srcElement.src = webModel + "/images/tree/parent.gif";
                    }
                }
            }
        }
    }

    //全部取消
    function unSelectAll_onClick() {
        if ("<%=checktype%>" == "RADIO") {
            jQuery("#popUpTree input[type='radio']").attr("checked", false);
        } else if ("<%=checktype%>" == "CHECK") {
            jQuery("#popUpTree input[type='checkbox']").attr("checked", false);
        }
    }

    //全部展开树节点
    function expandAllNode_onClick() {
        jQuery("#popUpTree div[id*='container_']").each(function () {
            webModel = form_treebasic.webModel.value;
            jQuery(this).css("display", "block");
            jQuery(this).prev().children("img").attr("src", webModel + "/images/tree/parentopen.gif");//更改图标为打开状态
        });
        jQuery("#popUpTree .baseNode img:first").attr("src", webModel + "/images/tree/root.png");
    }

    //全部收缩树节点
    function collapseAllNode_onClick() {
        webModel = form_treebasic.webModel.value;
        jQuery("#popUpTree div[id*='container_']").each(function () {
            jQuery(this).css("display", "none");
            jQuery(this).prev().children("img").attr("src", webModel + "/images/tree/parent.gif");//更改图标为打开状态
        });
        jQuery("#popUpTree .baseNode img:first").attr("src", webModel + "/images/tree/root.png");
    }

    //选择ID相同的其它复选框
    function selectAllSimilar(obj) {
        jQuery("#popUpTree input[type='checkbox'][id='" + (jQuery(obj).attr("id")) + "']").prop("checked", jQuery(obj).is(":checked"));
    }

    //初始权限栏目
    function init_ewp_au_doctype(website_id) {
        if (website_id == null || website_id == "") {
            return;
        }
        jQuery("#site_id").val(website_id);
        jQuery("#reference_site_id").val(website_id);
        var query_doctype_au_url = webModel + "/auRelationAction.do?cmd=queryDoctypeAuByPartyAndWebSite";
        jQuery.post(query_doctype_au_url
                , { "pType": "<%=pType%>", "partyId": "<%=partyId%>", "websitId": website_id }
                , function (data) {
                    if (data.isPass == "Y") {
                        selectedValues = data.docTypeIds;
                        site_id = website_id;
                        onLoadPopUpTree();
                    } else {
                        alert("<fmt:message key='udp.ewp.website.request_website_error'/>")
                    }
                }
                , "json");
    }

    jQuery(document).ready(function () {
        webModel = form_treebasic.webModel.value;
        if (jQuery("#allWebsite_ewp").length > 0) {
            jQuery.ajax({url: webModel + "/WebsiteAction.do?cmd=getSiteAll", async: false, cache: false, dataType: "json", success: function (data, textStatus) {
                jQuery("#allWebsite_ewp").append(jQuery('<option>', { value: "" })
                        .text("--<fmt:message key='udp.ewp.website.choosewebsite'/>--"));
                jQuery.each(data, function (key, value) {
                    jQuery("#allWebsite_ewp").append(jQuery('<option>', { value: key })
                            .text(value));
                });
                jQuery("#allWebsite_ewp").val(jQuery("#reference_site_id").val());
                jQuery("#allWebsite_ewp").change(function () {
                    init_ewp_au_doctype(jQuery(this).val());
                });
            }});
        }
    });

</script>

<script language="javascript">
    jQuery(document).ready(function () {
        <%if("Y".equals(hasSelectWebsite)){%>
        init_ewp_au_doctype(jQuery("#reference_site_id").val());
        <%}else{%>
        onLoadPopUpTree();
        <%}%>
    });
</script>
</head>
<body>
<table width="100%" class="table_noFrame" bgcolor="#C8CDD3" cellpadding='0' cellspacing='0'>
    <tr>
        <td bgcolor="#C8CDD3" style="height: 20px;">
            <%if ("Y".equals(hasSelectWebsite)) {%>
            <select id="allWebsite_ewp"></select>
            <%}%>
        </td>
    </tr>
    <tr>
        <td align="left" bgcolor="#C8CDD3">
            <div id="popUpTree" align="left"
                 style="width:100%; height: 400px; background-color: #c8cdd3; border: 0px solid #90b3cf;overflow:auto"></div>
        </td>
    </tr>
    <tr>
        <td bgcolor="#C8CDD3">
            <input type="button" name="referenceCancel" value='<fmt:message key="udp.ewp.doctype.expandAllNode"/>'
                   class="button_ellipse" onClick="javascript:expandAllNode_onClick();">
            <input type="button" name="referenceCancel" value='<fmt:message key="udp.ewp.doctype.collapseAllNode"/>'
                   class="button_ellipse" onClick="javascript:collapseAllNode_onClick();">
            <input type="button" name="referenceCancel" value='<fmt:message key="udp.ewp.doctype.unSelectAll" />'
                   class="button_ellipse" onClick="javascript:unSelectAll_onClick();">
            &nbsp;&nbsp;&nbsp;
            <input name="button_ok" class="button_ellipse" type="button"
                   value='<fmt:message key="confirm" bundle="${applicationResources}"/>'
                   onClick="javascript:callbackOkOperation();">
            <input name="button_cancel" class="button_ellipse" type="button"
                   value='<fmt:message key="cancel" bundle="${applicationResources}"/>'
                   onclick="javascript:callbackCancelOperation();">
        </td>
    </tr>
</table>
</body>
</html>
</fmt:bundle>
