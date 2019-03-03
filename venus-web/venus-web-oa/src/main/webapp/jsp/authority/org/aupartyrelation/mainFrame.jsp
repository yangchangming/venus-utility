<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Group_relations_view' bundle='${applicationAuResources}' /></title>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Group_relations_view' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>&nbsp;&nbsp;</td>
				<td width="100%" valign="middle">
				<table width="98%" height="40" border="0" align="center"
					cellpadding="0" cellspacing="1" bgcolor="#7EBAFF">
					<tr valign="middle">
						<td bgcolor="#FFFFFF" valign="middle"><iframe id="leftTree" name="leftTree"
							width="100%" height="100%" frameborder="0"
							src="<%=request.getContextPath()%>/jsp/authority/org/aupartyrelation/search.jsp"></iframe>
						</td>
					</tr>
				</table>
				<td>&nbsp;&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>&nbsp;&nbsp;</td>
				<td width="180" valign="top">
				<table width="100%" height="380" border="0" align="center"
					cellpadding="0" cellspacing="1" bgcolor="#7EBAFF">
					<tr>
						<td bgcolor="#FFFFFF">
							<%--<iframe id="leftTree" name="leftTree" width="100%" height="100%" frameborder="0" src="<venus:base/>/AuPartyRelationTypeAction.do?cmd=queryLeftTree"></iframe>--%>
							<iframe id="leftTree" name="leftTree" width="100%" height="100%" frameborder="0" src="<venus:base/>/auPartyRelationType/queryLeftTree"></iframe>
						</td>
					</tr>
				</table>
				</td>
				<td valign="top">
				<table width="95%" height="380" border="0" align="center"
					cellpadding="0" cellspacing="1" bgcolor="#7EBAFF">
					<tr>
						<td bgcolor="#FFFFFF" align="center"><iframe id="detail"
							name="detail"
							style="HEIGHT:100%; VISIBILITY: inherit; WIDTH: 100%; Z-INDEX: 2"
							scrolling=auto frameborder=0
							src="<%=request.getContextPath()%>/jsp/authority/org/aupartyrelation/default.jsp"></iframe>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>

