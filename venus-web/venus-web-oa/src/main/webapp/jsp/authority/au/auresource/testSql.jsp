<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="venus.oa.authority.auresource.util.IAuResourceConstants" %>
<%
String isRight=(String)request.getAttribute(IAuResourceConstants.REQUEST_BEAN_VALUE);
%>
<title><fmt:message key='venus.authority.Record_level_data_permissions_test' bundle='${applicationAuResources}' /></title>
</head>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Test' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');
</script>
<br>
<%if("true".equals(isRight)){ %>
<center><font color='009900'><fmt:message key='venus.authority.Tested' bundle='${applicationAuResources}' /></font></center>
<%}else{%>
<center><font color='FF0000'><fmt:message key='venus.authority.Test_fails' bundle='${applicationAuResources}' /></font></center>
<%}%>
<br>
<center>
	<input name="button_close" type="button" class="button_ellipse" value="<fmt:message key='venus.authority.Close' bundle='${applicationAuResources}' />" onClick="javascript:window.close();">
</center>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>
</body>
</html>

