<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ranmin-based architecture project</title>
</head>


<frameset name="subMainFrameSet" id="subMainFrameSet" rows="*" cols="216,24,*" framespacing="0" frameborder="0" border="0">
    <frame src="simpleTemplate.jsp" name="leftFrameSet" frameborder="0" scrolling="no" noresize id="controlFrame">
    <frame src="toolbar.jsp" name="controlFrame" frameborder="0" scrolling="no" noresize id="controlFrame">
    <frame name="bodyFrame" src="<%=request.getContextPath()%>/TemplateAction.do?cmd=queryAll"/>
</frameset>

<noframes><body>
</body></noframes>
</html>