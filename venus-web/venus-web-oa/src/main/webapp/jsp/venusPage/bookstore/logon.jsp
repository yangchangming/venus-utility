<%@ page import="venus.frames.mainframe.util.VoHelper" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<fmt:bundle basename="udp.template.bookstore_resource" prefix="udp.template.bookstore.">
<title>用户登录</title>
<script>

	function submit_onClick(){  //增加记录
		if(checkAllForms()){
    		form.action = "<venus:base/>/BookStore.do?cmd=logon";
    		form.submit();
    	}
  	}
</script>

</head>
<body>
<script language="javascript">
	writeTableTop('<fmt:message key="selectaccount"/>','<venus:base/>/');
</script>

<form name="form" method="post">
<input type="hidden" name="id" value="">


<div id="ccChild1"> 
<table class="table_div_content" align="center">
	<tr><td width="20%" nowrap>&nbsp;</td><td>&nbsp;</td></tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td align="right"><font color="red"><fmt:message key="normaluser"/><font></td>
		<td align="left">
			<a href="<venus:base/>/BookStore.do?cmd=logon&accountId=account1&password=1">普通用户一</a><br>
			<a href="<venus:base/>/BookStore.do?cmd=logon&accountId=account2&password=1">Helen</a>
		</td>
	</tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr>
		<td align="right"><font color="red"><fmt:message key="admin"/></font></td>
		<td align="left">
			<a href="<venus:base/>/BookStore.do?cmd=logon&accountId=admin&password=1">管理员一</a>
		</td>
	</tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
	<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
</table>
</div>
</form>
</fmt:bundle>
<script language="javascript">
	writeTableBottom('<venus:base/>/');
</script>
</body>
</html>
<%  //表单回写
	if(request.getAttribute("writeBackFormValues") != null) {
		out.print("<script language=\"javascript\">\n");
		out.print(VoHelper.writeBackMapToForm((java.util.Map)request.getAttribute("writeBackFormValues")));
		out.print("writeBackMapToForm();\n");
		out.print("</script>");
	}
%>