<%@ page contentType="text/html; charset=UTF-8" %>

<%
response.sendRedirect(
	(String)request.getAttribute(venus.frames.mainframe.action.GoToPathAction.GO_TO_PATH_KEY)
);
%>