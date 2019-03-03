<%@page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Authorization_page' bundle='${applicationAuResources}' /></title>
</head>
<%
    String retMessage = (String)request.getAttribute("retMessage");
    if(null==retMessage)
        retMessage="<fmt:message key='venus.authority.Save_successfully' bundle='${applicationAuResources}' />!";
%>
<script>
window.parent.showRetMessage("<%=retMessage%>");
</script>
</html>

