<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title></title>
</head>
<body>
<form name="loginForm" method="post" action="<%=request.getContextPath()%>/login">
<input name="login_id" type="text" /><br>
<input name="password" type="text" />
<input name="submit" type="submit" value="submit">
</form>
</body>
</html>

