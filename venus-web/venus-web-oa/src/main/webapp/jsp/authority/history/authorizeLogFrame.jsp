<%@page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Organization' bundle='${applicationAuResources}' /></title>
</head>
<%
	String retMessage = (String)request.getAttribute("retMessage");
	if(retMessage!=null) {
		out.print("<script language='javascript'>alert('"+retMessage+"'); window.parent.jQuery('#iframeDialog').dialog('close');</script>");
	} 
%>
<body topmargin=0 leftmargin=0 >
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Authorization_page' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
	function cancel_onClick() {
        window.parent.jQuery("#iframeDialog").dialog("close");
    }
    
</script>
<table class="table_noFrame">
	<tr>
		<td>&nbsp;&nbsp;
			<input name="button_ok" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Determine' bundle='${applicationAuResources}' />" onClick="javascript:historyLogFrame.historyLogAuthorize();">
			<input name="button_cancel" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Cancel' bundle='${applicationAuResources}' />"  onclick="javascript:cancel_onClick();" >
		</td>
	</tr>
</table>
<table class="table_noFrame" width="100%">
  <tr> 
     <td width="100%" valign="top"> 
		<!--树开始-->    
		<iframe id="historyLogFrame" name="historyLogFrame" width="100%" height="550" src="<venus:base/>/historyLog/simpleQueryForLogFrame?relId=<%=request.getParameter("relId")%>
			&pType=<%=request.getParameter("pType")%>
			&visiCode=<%=request.getParameter("visiCode")%>">
		</iframe>
		<!--树结束-->
    </td>
  </tr>
</table>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>

