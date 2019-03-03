<%@ include file="/jsp/include/global.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%
	String typeId = (String)request.getAttribute("partyrelationtype_id");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.New_relationship_as_root' bundle='${applicationAuResources}' /></title>
<script> 
	function toAdd_onClick() {  //到增加记录页面
	
		var refPath = "<venus:base/>/jsp/authority/org/auparty/mainFrame.jsp?pageFlag=ref&partyrelationtypeId=<%=typeId%>";
		var rtObj = window.showModalDialog(refPath,new Object(),'dialogHeight=600px;dialogWidth=800px;resizable:yes;status:no;scroll:auto;');
		if(rtObj != undefined){
			var partyId = rtObj[0];
			form.action="<venus:base/>/auPartyRelation/initRoot?partyId=" + partyId + "&partyRelationTypeId=<%=typeId%>";
    		form.submit();
		}
	}
</script>
</head>
<body>
<form name="form" method="post" action="">
<div id="auDivParent1">
<table class="table_div_content" height="100%">
	<tr>
		<td valign="top">
			<br><img src="<%=request.getContextPath()%>/images/au/yq_bt<au:i18next/>.jpg" width="73" height="14">
			<br>
			<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<fmt:message key='venus.authority.Type_of_relationship_the_group_has_not_root' bundle='${applicationAuResources}' /><br><br>
		</td>
		<td valign="top" width="300">
		<br><br><br>
			<table align="left">
				<tr> 
					<td class="button_ellipse" onClick="javascript:toAdd_onClick();"><img src="<venus:base/>/images/icon/add.gif" class="div_control_image"><fmt:message key='venus.authority.New_root' bundle='${applicationAuResources}' /></td>	
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>

