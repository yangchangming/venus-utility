<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="venus.frames.i18n.util.LocaleHolder"%>
<%@ include file="/jsp/include/global.jsp" %>
<%
        String userLocal = request.getParameter("local");
        if(!StringUtils.isEmpty(userLocal)){
            pageContext.getSession().setAttribute(LocaleHolder.LOCAL_IN_SESSION_KEY,userLocal);
        }
    %>
<logic:notEmpty  name="local_in_sesson_key" scope="session">
    <fmt:setLocale value="${local_in_sesson_key}" scope="session"/>
</logic:notEmpty>
<fmt:setBundle basename="ApplicationResources" scope="session" var="applicationResources"/>
<fmt:setBundle basename="venus.authority.authority_resource" scope="session" var="applicationAuResources"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>
    do nothing,only set user local to session scope.
</body>
</html>