<%@ page import="venus.oa.util.StringHelperTools" %>
<%@page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<%
String rootXmlSource=request.getContextPath()+"/jsp/authority/au/auauthorize/viewFuncOrg.jsp?parent_code="+request.getParameter("rootCode")+"&vCode="+request.getParameter("vCode")+"&partyId="+request.getParameter("partyId")+"&isUser="+request.getParameter("isUser");
%>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<%
	String retMessage = (String)request.getAttribute("retMessage");
	if(retMessage!=null) {
		out.print("<script language='javascript'>alert('"+retMessage+"'); window.close();</script>");
	} 
%>
<body topmargin=0 leftmargin=0 >
<table  class="table_noFrame" width="100%">
<tr><td width="55%">
<table class="table_noFrame" width="100%">
  <tr> 
     <td valign="top" width="100%"> 
     	<!--树开始--> 
		<iframe id="myTree" name="myTree" width="100%" height="600" src="<%=request.getContextPath()%>/auAppend/<%=request.getParameter("cmd")%>?vCode=<%=request.getParameter("vCode")%>&partyId=<%=request.getParameter("partyId")%>&isUser=<%=request.getParameter("isUser")%>"></iframe>
		<!--树结束-->
    </td>
  </tr>
</table>
</td><td width="45%">
<table class="table_noFrame" width="100%">
  <tr> 
     <td valign="top" width="100%"> 
     	<input type="hidden" name="orgTreeSrc" id="orgTreeSrc" value="<%=request.getContextPath()%>/jsp/authority/tree/deeptree4AuOrg.jsp?inputType=checkbox&nodeRelationType=noRelation&vCode=<%=request.getParameter("vCode")%>	&rootXmlSource=<%=StringHelperTools.encodeUrl(rootXmlSource)%>">
     	<!--树开始--> 
		<iframe id="orgTree" name="orgTree" width="100%" height="600" src="<%=request.getContextPath()%>/jsp/authority/au/auauthorize/defaultView.jsp"></iframe>
		<!--树结束-->
    </td>
  </tr>
</table>
</td></tr></table>
</body>
</html>

