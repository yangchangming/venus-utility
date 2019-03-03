<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%
	String retMessage = (String)request.getAttribute("retMessage");
    String closeWindow = StringUtils.stripToEmpty(request.getParameter("closeWindow"));
	if(retMessage!=null) {
		out.print("<script language='javascript'>alert('"+retMessage+"');window.location='"+request.getContextPath()+"/jsp/authority/au/auauthorize/default.jsp';"+("true".equals(closeWindow)?"parent.window.close();":"")+"</script>");
	} 
%>

