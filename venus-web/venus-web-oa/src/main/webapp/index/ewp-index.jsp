<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>建站平台</title>
        <logic:notEmpty  name="local_in_sesson_key" scope="session">
            <fmt:setLocale value="${local_in_sesson_key}" scope="session"/>
        </logic:notEmpty>
        <fmt:setBundle basename="ApplicationResources" scope="session" var="applicationResources"/>
    </head>

  <frameset rows="39,*" border="0">
    <frame name="topFrame" src="<%=request.getContextPath()%>/jsp/ewp/index/header.jsp" scrolling="no" noresize>
    <frame name="left" src="<%=request.getContextPath()%>/jsp/ewp/index/main.jsp" />
  </frameset>
    <noframes>
        <body>
            对不起,您的浏览器不支持框架
        </body>
    </noframes>
</html>