<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户欢迎界面</title>
</head>
<body>
<table width="100%" height="50" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td bgcolor="#e3e3e3">&nbsp;</td>
    <td bgcolor="#e3e3e3"><table width="100%"  border="0" cellpadding="0" cellspacing="0">
      <tr>
		<td height="25" align="center">
				<img src="<%=request.getContextPath() %>/themes/<venus:theme/>/images/au/user_m.png" width="17" height="19"><span class="wel">欢迎使用VENUS4.1系统&nbsp;</span>
		</td>
      </tr>
      <tr>
        <td height="3"><img src="<%=request.getContextPath() %>/images/index/user_xx.jpg" width="146" height="2"></td>
      </tr>
    </table></td>
    <td valign="top" bgcolor="#e3e3e3"></td>
  </tr>
</table>
<table height="27" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td><!-- <img src="<%=request.getContextPath() %>/images/index/user_dif.jpg" width="175"> --></td>
  </tr>
</table>
</body>
</html>
