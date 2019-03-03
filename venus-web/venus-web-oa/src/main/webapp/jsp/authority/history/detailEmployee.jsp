<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.authority.service.history.util.IContants" %>
<%@ page import="venus.authority.sample.employee.vo.EmployeeVo" %>
<%@ page import="venus.authority.util.VoHelperTools" %>
<%@ page import="venus.authority.util.StringHelperTools" %>
<%@ page import="java.util.Map" %>
<%@ include file="/jsp/include/global.jsp" %>
<%
	Map map = (Map)request.getAttribute(IContants.REQUEST_BEANS_VALUE);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>VENUS<fmt:message key='venus.authority.Organizational_competence_system' bundle='${applicationAuResources}' /></title>
</head>
<body class="table_div_content">
<div id="ccChild0"> 
<table>
	<tr>
		<td>
			<table class="table_div_content_inner">
				<tr>
					<td width="20%" align="right" style="white-space: nowrap"><fmt:message key='venus.authority.Name_0' bundle='${applicationAuResources}' /></td>
					<td width="80%" align="left"><%=StringHelperTools.prt(map.get("person_name"))%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Code_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(map.get("person_no"))%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.English_name_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(map.get("english_name"))%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Staff_category_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(map.get("person_type"))%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Sex_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%="1".equals(map.get("sex"))?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Male"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Female")%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Mobile_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(map.get("mobile"))%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Telephone_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(map.get("tel"))%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.E_mail_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(map.get("email"))%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Address_1' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(map.get("address"))%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Postal_Code_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(map.get("postalcode"))%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Remarks_' bundle='${applicationAuResources}' /></td>
					<td colspan="3" align="left"><%=StringHelperTools.prt(map.get("remark"))%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Created_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(map.get("create_date"),19)%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Modified_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(map.get("modify_date"),19)%></td>
				</tr>
				</table>
			</td>
		</tr>
	</table>
</div>
</body>

