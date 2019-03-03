<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Reference_page' bundle='${applicationAuResources}' /></title>
</head>
<body topmargin=0 leftmargin=0 >
	<script language="javascript">
		writeTableTop("<fmt:message key='venus.authority.Reference_page' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
	</script>
	<iframe id="myTree" name="myTree" width="100%" height="500" src="<%=request.getContextPath()%>/auProxy/queryUserList?proxyId=<%=request.getParameter("proxyId")%>">
	</iframe>
	<script language="javascript">
		writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
	</script>
</body>
</html>

