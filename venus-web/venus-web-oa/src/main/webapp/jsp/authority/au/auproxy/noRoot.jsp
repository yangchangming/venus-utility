<%@ include file="/jsp/include/global.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Add_proxy' bundle='${applicationAuResources}' /></title>
<script> 
	function toAdd_onClick() {  //到增加记录页面
		window.location.href = "<venus:base/>/jsp/authority/au/auproxy/insertAuProxy.jsp";
	}
</script>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Add_proxy' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<form name="form" method="post">
<div id="ccParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild0',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')"><fmt:message key='venus.authority.Add_proxy' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>
<div id="ccChild0"> 
<table class="table_div_content">
	<tr>
		<td>
			<table class="table_div_content_inner">
				<tr>
					<td>
						<img src="<%=request.getContextPath()%>/images/au/yq_bt<au:i18next/>.jpg" width="73" height="14">
						<br>
						<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key='venus.authority.Agent_has_not_created' bundle='${applicationAuResources}' /><br><br>
					</td>
				</tr>
				<br><br><br>
				<tr> 
					<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input name="btnAdd1" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Creating_proxy' bundle='${applicationAuResources}' />" onClick="javascript:toAdd_onClick()">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>

