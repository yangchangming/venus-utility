<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="venus.oa.helper.LoginHelper" %>
<%
	String menuCode = request.getParameter("menuCode");
	if (menuCode != null && !"".equals(menuCode)) {
	    PrintWriter writer = response.getWriter();
	    writer.print(LoginHelper.getMenuPath(request,menuCode));
	    writer.close();
	}
%>