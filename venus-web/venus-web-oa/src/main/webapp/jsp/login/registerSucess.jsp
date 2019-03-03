<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/include/global.jsp"%>
<%@ page import="venus.authority.au.auuser.vo.AuUserVo"%>
<%@ page import="venus.authority.au.auuser.util.IAuUserConstants" %>
<%
    AuUserVo vo = (AuUserVo)request
				.getAttribute(IAuUserConstants.REQUEST_BEANS_VALUE);
	String Name=vo.getName();
	String Login_id=vo.getLogin_id();
%>
<title><fmt:message key='venus.authority.Successful_registration_page' bundle='${applicationAuResources}' /></title>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Registration_page' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<table width="100%" height="100%" border="0">
	<tr>
		<td nowrap align=center><%=Name%><fmt:message key='venus.authority.Hi_Your_login_name_is_' bundle='${applicationAuResources}' /><%=Login_id%><fmt:message key='venus.authority._8' bundle='${applicationAuResources}' /></td>
	</tr>
	<tr>
		<td align=center><input type=button value=<fmt:message key='venus.authority.Close' bundle='${applicationAuResources}' /> class="button_ellipse" onClick=javascript:window.close();return false;></td>
	</tr>
</table>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>

