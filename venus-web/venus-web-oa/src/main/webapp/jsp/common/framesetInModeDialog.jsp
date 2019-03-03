<%@ page contentType="text/html; charset=UTF-8" %>

<%
String title = request.getParameter("title");
String url = request.getParameter("url");
if(title == null)
	title = "";
if(url == null)
	url = "";
	
if(url.indexOf('^')!=-1)
	url=url.replace('^','&');
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=title%></title>
<base target="_base">
</head>

<frameset name="modeDialogMainFrameId" rows="0,*" cols="*" name="modeDialogMainFrame" frameborder="NO" border="0" framespacing="0">
	<frame src="about:blank" name="blank">
	<frame src="<%=url%>" name="modeDialogFrame" id="modeDialogFrameId">
</frameset>

<noframes><body>您的浏览器不支持框架</body></noframes>
</html>