<%@ page isErrorPage="true" %>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>error</title>
</head>
<body style="background-color:#f5f5f5;">

<div style="width:560px; height:200px; background-color:#f5f5f5; margin:100px auto;">

	<div style="width:320px; height:160px; float:left; padding:20px;">
		<h4 style=" font-family:'微软雅黑', '宋体', Arial; font-size:18px;">出问题了:<</h4>
		<p style="font-size:12px; color:#7f7f7f;">请您检查确认输入的地址是否正确<br /><br />
			点击以下链接继续浏览网页<br /><br />
			>>&nbsp;&nbsp;<a href="javascript:history.go(-1);" onclick="returnBack();">返回上一页</a>
			<br />
		</p>
	</div>
	<div style="width:200px; height:200px; float:right;">
		<img id="error" src="<%=request.getContextPath() %>/images/itari/Error.png"/>
	</div>

	<div id="error-stack" style="font-size:12px; color:#7f7f7f;">
		请求URL: &nbsp;<%=request.getRequestURL()%><br/>
		异常类型: &nbsp;<%= exception.getClass() %><br>
		异常描述: &nbsp;<%= exception.getMessage() %><br>
		异常堆栈: &nbsp;<br>
		<pre>
		<%
//			java.io.PrintWriter pw = response.getWriter();
//			exception.printStackTrace(pw);
		%>
		</pre>

	</div>
</div>

</body>
</html>