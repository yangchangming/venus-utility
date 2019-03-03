<%@ page contentType="text/html; charset=UTF-8" %>

<%@page import="venus.frames.i18n.util.LocaleHolder" %>
<%@ taglib uri="/WEB-INF/gap-html.tld" prefix="venus" %>
<%
    String templateType = request.getParameter("templateType");
    String siteId = request.getParameter("siteId");
    String inputId = request.getParameter("inputId");
    String inputName = request.getParameter("inputName");
%>
<title><fmt:message key="reference_page" bundle="${applicationResources}"/></title>
<script language="javascript">
    //新增时保存
    function save_onClick() {
        var idnames = document.getElementById('templateFrame').contentWindow.returnvalue();
        if (idnames != '' && idnames != undefined) {
            var template = eval('(' + idnames + ')');
            if (null != template && template != "") {
                jQuery("#<%=inputId%>").val(template.id);
                jQuery("#<%=inputName%>").val(template.chinesename);
            } else {
                jQuery("#<%=inputId%>").val('');
                jQuery("#<%=inputName%>").val('');
            }
            templateDialog.dialog("close");
        }
    }

    function return_onClick() {  //取消后返回列表页面
        jQuery("#referenceTemplate").html("");
        templateDialog.dialog("close");
    }
</script>
</head>
<body style="width:410px;height:450px">
<table class="table_noFrame" bgcolor="#c8cdd3">
    <tr>
        <td>
            <input name="button_ok" class="button_ellipse" type="button" value="<%=LocaleHolder.getMessage("confirm")%>"
                   onClick="javascript:save_onClick();">
            <input name="button_cancel" class="button_ellipse" type="button"
                   value="<%=LocaleHolder.getMessage("return")%>" onclick="javascript:return_onClick();">
        </td>
    </tr>
    <tr>
        <td>
            <iframe id="templateFrame"
                    src="<venus:base/>/EwpTemplateAction.do?cmd=queryReference&siteId=<%=siteId%>&templateType=<%=templateType%>"
                    frameBorder=0 style="width:410px;height:400px;overflow:auto;"></iframe>
        </td>
    </tr>
</table>
</body>
</html>