<%@ page language="java" contentType="text/html; charset=UTF-8" %>
		<%@ include file="/jsp/include/global.jsp" %>
		<html>
		<head>
			<title><fmt:message key='venus.authority.Error_Rights' bundle='${applicationAuResources}' /></title>
		</head>
		<body>
		<div align="center">
			<p>&nbsp;</p>
			<p>&nbsp;</p>
	<p>&nbsp;</p>
	  <p><img src="<%=request.getContextPath()%>/images/au/au_error.jpg" width="329" height="211"></p>
	  <p><fmt:message key='venus.authority.Error_Cause_attempt_to_access_unauthorized_resources_has_been_denied_by_the_system_' bundle='${applicationAuResources}' /></p>
	  <p><fmt:message key='venus.authority._5' bundle='${applicationAuResources}' /><a href="javascript:void(0)" onclick="javascript:returnBack();"><fmt:message key='venus.authority.Back' bundle='${applicationAuResources}' /></a><fmt:message key='venus.authority._6' bundle='${applicationAuResources}' /></p>
	</div>
	</body>
</html>

