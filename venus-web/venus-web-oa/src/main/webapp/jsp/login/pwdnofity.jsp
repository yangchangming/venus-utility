<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
		<script>
			alert("<%=(String)request.getAttribute("Message")%>");
			window.location.href="<%=request.getContextPath()%>/jsp/index.jsp";
		</script>
	</head>
	<body>
	</body>
</html>

