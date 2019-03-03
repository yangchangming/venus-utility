<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<fmt:bundle basename="udp.quartz.quartz_resource" prefix="udp.quartz.">

<%String errorInfo = (String)request.getAttribute("errorInfo");
%>
<html>
	<head>
		<title><fmt:message key="Error_Page"/></title>
	</head>
<script language="javascript">
	writeTableTop('<fmt:message key="Error_Page"/>','<venus:base/>/themes/<venus:theme/>/');
</script>
	<body>
		<font face="Comic Sans MS" size=4>
		<blockquote>
		<center>
		<fmt:message key="Error_Info"/>
		</center>
		<span id="errorInfo"><font color=red><%=errorInfo%></font>
		</span>
		</blockquote>
		</font>
	</body>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>
</fmt:bundle>	
</html>
