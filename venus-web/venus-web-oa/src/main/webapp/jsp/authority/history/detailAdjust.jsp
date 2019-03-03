<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.authority.service.history.util.IContants" %>
<%@ page import="venus.authority.util.StringHelperTools" %>
<%@ page import="venus.authority.service.history.vo.HistoryLogVo" %>
<%@ include file="/jsp/include/global.jsp" %>
<%
	HistoryLogVo historyVo = (HistoryLogVo)request.getAttribute(IContants.REQUEST_BEANS_VALUE);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>VENUS<fmt:message key='venus.authority.Organizational_competence_system' bundle='${applicationAuResources}' /></title>
</head>
<body class="table_div_content">
<div id="ccChild0"> 
<table  width="100%">
	<tr>
		<td>
			<table class="table_div_content_inner">
				<tr>
					<td width="30%" align="right" style="white-space: nowrap"><fmt:message key='venus.authority.Resource_Name_' bundle='${applicationAuResources}' /></td>
					<td width="70%" align="left"><%=historyVo.getSource_name()%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.The_original_organization_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=historyVo.getSource_orgtree()%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.The_new_organization_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=(historyVo.getDest_orgtree() == null || "".equals(historyVo.getDest_orgtree())) ? venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.No") : historyVo.getDest_orgtree()%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Operator_Name_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=historyVo.getOperate_name()%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Operating_time_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(historyVo.getOperate_date(),19)%></td>
				</tr>				
				</table>
</td></tr>
</table>
</div>
</body>

