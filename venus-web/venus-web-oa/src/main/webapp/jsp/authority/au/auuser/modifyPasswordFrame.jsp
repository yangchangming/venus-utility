<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
	String successFlag = (String)request.getAttribute("success_flag");
	if(successFlag != null) {
		if(successFlag.equals("-1")) {
			out.print("<script language='javascript'>alert(\""+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Username_is_incorrect")+"\");</script>");
		}else if(successFlag.equals("0")) {
			out.print("<script language='javascript'>alert(\""+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Old_password_is_not_correct_the_password_must_use_the_correct_case_letters_")+"\");</script>");
		}else if(successFlag.equals("1")) {
			out.print("<script language='javascript'>alert(\""+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Your_password_has_been_changed")+"\");parent.window.close();</script>");
		}
	}
%>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Change_Password' bundle='${applicationAuResources}' /></title>
</head>
<body topmargin="0" leftmargin="0" rightmargin="0" bottommargin="0">
	<iframe name="modifyPassword" width="100%" height="100%" frameborder="0" scrolling="no" 
		src="<%=request.getContextPath()%>/jsp/authority/au/auuser/modifyPassword.jsp">
	</iframe>
</body>
</html>

