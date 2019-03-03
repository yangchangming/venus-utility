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
		out.print("<script language='javascript'>alert('"+retMessage+"'); window.close();</script>");
	} 
%>
<body topmargin=0 leftmargin=0 >
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Details0' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<table class="table_noFrame" width="100%">
  <tr> 
     <td width="100%" valign="top"> 
		<iframe name="historyLogFrame" width="100%" height="400" src="<venus:base/>/historyLog/findDetail?id=<%=request.getParameter("id")%>">
		</iframe>
    </td>
  </tr> 
</table>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>

