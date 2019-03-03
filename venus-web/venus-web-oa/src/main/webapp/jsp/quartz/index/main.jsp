<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ taglib uri="/WEB-INF/gap-html.tld" prefix="venus" %>
<title>UDP-quartz</title>
</head>


<frameset name="subMainFrameSet" id="subMainFrameSet" rows="*" cols="216,24,*" framespacing="0" frameborder="0" border="0">
    <frame src="<%=request.getContextPath()%>/jsp/quartz/index/scheduleCenter.jsp" name="leftFrameSet" frameborder="0" scrolling="no" noresize id="controlFrame">
    <frame src="<%=request.getContextPath()%>/jsp/quartz/index/toolbar.jsp" name="controlFrame" frameborder="0" scrolling="no" noresize id="controlFrame">
    <frame name="bodyFrame" src="<%=request.getContextPath()%>/queryJobSchedules.do"/>
</frameset>

<noframes><body>
</body></noframes>
</html>