<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import = "venus.authority.util.VoHelperTools" %>
<%@ page import = "venus.authority.util.StringHelperTools" %>
<%@ page import="venus.authority.org.auconnectrule.vo.AuConnectRuleVo"%>
<%@ page import="venus.authority.org.auconnectrule.util.IConstants" %>
<%
    AuConnectRuleVo vo = (AuConnectRuleVo)request
				.getAttribute(IConstants.REQUEST_BEAN_VALUE);
	String my_create_date = "";
	String my_modify_date = "";	
    if (vo.getCreate_date() != null) {
		my_create_date = StringHelperTools.prt(StringHelperTools.prt(vo
			.getCreate_date()), 16);
	}
	if (vo.getModify_date() != null) {
		my_modify_date = StringHelperTools.prt(StringHelperTools.prt(vo
			.getModify_date()), 16);
	}
%>
<title><fmt:message key='venus.authority.View_Group_Type' bundle='${applicationAuResources}' /></title>
<script language="javascript">

	function goBack_onClick() {//返回
	    <%--window.location="<venus:base/>/AuConnectRuleAction.do?cmd=queryAll";--%>
		window.location="<venus:base/>//auConnectRule/queryAll"
	}	
		
</script>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Detailed_list' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');
</script>
<table class="table_noframe" width="97%" align="center">
	<tr>
		<td valign="middle">			
			<input name="button_cancel" type="button" class="button_ellipse" value="<fmt:message key='venus.authority.Return' bundle='${applicationAuResources}' />"  onClick="goBack_onClick();">
		</td>
	</tr>
</table>
	<table border="1" bordercolordark="#FFFFFF" bordercolorlight="#7EBAFF" cellpadding="5" cellspacing="0" width="80%" align="center"> 	
		<tr> 
			<td colspan="2" align="center"> <fmt:message key='venus.authority.Detailed_rules_for_groups_to_connect' bundle='${applicationAuResources}' /></td> 
		</tr>
		<tr> 
			<td width="30%" align="right"><fmt:message key='venus.authority.Name_1' bundle='${applicationAuResources}' /></td> 
			<td width="70%" align="left" bgcolor="#FFFFFF">&nbsp;<%=vo.getName()%></td> 
		</tr> 		
		<tr> 
			<td width="30%" align="right"><fmt:message key='venus.authority.Parent_Group_type_' bundle='${applicationAuResources}' /></td> 
			<td width="70%" align="left" bgcolor="#FFFFFF">&nbsp;<%=vo.getParent_partytype()%></td> 
		</tr> 		
		<tr> 
			<td width="30%" align="right"><fmt:message key='venus.authority.Sub_group_type_' bundle='${applicationAuResources}' /></td> 
			<td width="70%" align="left" bgcolor="#FFFFFF">&nbsp;<%=vo.getChild_partytype()%></td> 
		</tr>
		<tr> 
			<td width="30%" align="right"><fmt:message key='venus.authority.Type_of_group_relations_' bundle='${applicationAuResources}' /></td> 
			<td width="70%" align="left" bgcolor="#FFFFFF">&nbsp;<%=vo.getRelation_type()%></td> 
		</tr>
		<tr> 
			<td align="right"><fmt:message key='venus.authority.Created_' bundle='${applicationAuResources}' /></td> 
			<td align="left" bgcolor="#FFFFFF">&nbsp;<%=my_create_date%></td> 
		</tr>
		<tr> 
			<td align="right"><fmt:message key='venus.authority.Modified_' bundle='${applicationAuResources}' /></td> 
			<td align="left" bgcolor="#FFFFFF">&nbsp;<%=my_modify_date%></td> 
		</tr>
		<tr> 
			<td width="30%" align="right"><fmt:message key='venus.authority.Notes_' bundle='${applicationAuResources}' /></td> 
			<td width="70%" align="left" bgcolor="#FFFFFF">&nbsp;<%=vo.getRemark()%></td> 
		</tr>
	</table> 
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>
</body>
</html>

