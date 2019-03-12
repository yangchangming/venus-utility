<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="venus.oa.helper.LoginHelper"%>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>VENUS<fmt:message key='venus.authority.Organizational_competence_system' bundle='${applicationAuResources}' /></title>
</head>
<%
	String loginName = LoginHelper.getLoginName(request);
	if( loginName == null ) {
%>
	<script language="javascript">
		self.parent.parent.location="<%=request.getContextPath() %>/jsp/login/login.jsp?errorType=1";
	</script>
<%
	}
%>
<body>
<table width="100%" height="34" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td bgcolor="#589aa9">&nbsp;</td>
    <td bgcolor="#589aa9"><table width="100%"  border="0" cellpadding="0" cellspacing="0">
      <tr>
		<td height="25"><img src="<%=request.getContextPath() %>/themes/<venus:theme/>/images/au/user_m.png" width="15" height="15"><span class="wel" style="color:#ffffff">&nbsp;&nbsp;<%=loginName%>&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key='venus.authority.Hello_' bundle='${applicationAuResources}' /></span></td>
      </tr>
    </table></td>
    <td height="3" valign="top" align="right" bgcolor="#e3e3e3"><!-- <img src="<%=request.getContextPath() %>/images/index/user_l_r.jpg" width="19" height="23"> --></td>
  </tr>
</table>
<table height="27" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td><!-- <img src="<%=request.getContextPath() %>/images/index/user_dif.jpg" width="175"> --></td>
  </tr>
</table>
</body>
</html>

