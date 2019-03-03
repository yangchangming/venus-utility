<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
	String successFlag = (String)request.getAttribute("success_flag");
	if(successFlag != null) {
	    if(successFlag.equals("-1")) {
	        out.print("<script language='javascript'>alert(\""+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Username_is_incorrect")+"\");</script>");
	    }else if(successFlag.equals("2")) {
	        out.print("<script language='javascript'>alert(\""+venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.New_password_and_old_password_can_not_be_the_same_")+"\");</script>");
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
	<iframe name="modifyPassword" width="100%" height="100%" border="0"
		src="<%=request.getContextPath()%>/jsp/authority/au/auuser/forceModifyPassword.jsp?changePwd=<%=request.getParameter("changePwd") %>">
	</iframe>
</body>
</html>

