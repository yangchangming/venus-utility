<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="/jsp/include/global.jsp" %>
<html>
<head>
<meta http-equiv=Content-Type content="text/html;  charset=UTF-8">
</head>
<body>
	<iframe id="leftMenu" name="leftMenu" style="display: block" border="0" marginWidth="0" frameSpacing="0" marginHeight="0" src="<%=request.getContextPath()%>/jsp/authority/menu/leftMenuData.jsp?totalCode=<%= request.getParameter("totalCode") %>" frameBorder="0" noResize width="100%" scrolling="yes" height="100%" vspale="0"/>
</body>
</html>