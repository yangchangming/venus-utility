<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%
	String srcrelationid=request.getParameter("srcrelationid");
%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Organization' bundle='${applicationAuResources}' /></title>
<script type="text/javascript">
    function cancel_onClick() {
        window.parent.jQuery("#iframeDialog").dialog("close");
    }
</script>
</head>
<body topmargin=0 leftmargin=0 >
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Reference_page' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<form name="form" method="post">
<table class="table_noFrame">
	<tr>
		<td>&nbsp;&nbsp;
			<input name="button_ok" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Determine' bundle='${applicationAuResources}' />" onClick="javascript:myTree.changeRelation('<%=srcrelationid%>');">
			<input name="button_cancel" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Cancel' bundle='${applicationAuResources}' />"  onclick="javascript:cancel_onClick();" >
		</td>
	</tr>
</table>
<table class="table_noFrame">
  <tr> 
     <td width="100%" valign="top"> 
		<!--树开始-->    
		<iframe name="myTree" width="100%" height="450" src="deeptree4Change.jsp
			?inputType=<%=request.getParameter("inputType")%>
			&submitType=<%=request.getParameter("submitType")==null?"submitAll":request.getParameter("submitType")%>
			&nodeRelationType=<%=request.getParameter("nodeRelationType")==null?"hasRelation":request.getParameter("nodeRelationType")%>
			&rootXmlSource=<%=venus.authority.util.StringHelperTools.encodeUrl(request.getParameter("rootXmlSource"))%>">
		</iframe>
		<!--树结束-->
    </td>
  </tr>
</table>
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>

