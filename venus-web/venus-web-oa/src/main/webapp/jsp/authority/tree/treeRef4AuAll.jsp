<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="venus.oa.util.GlobalConstants"%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Organization' bundle='${applicationAuResources}' /></title>
</head>
<script>
var doorKeeper = 0;
function showRetMessage(retMessage){
    doorKeeper++;
    if(doorKeeper==1){
        //alert("行政关系保存成功！");
        roleTree.saveOnClick();
    }
    if(doorKeeper==2){
        //alert("角色关系保存成功！");
        proxyTree.saveOnClick();
    }
    if(doorKeeper==3){
        //alert("代理关系保存成功！");
        alert(retMessage);
        window.close();
    }
}
</script>
<%
	String retMessage = (String)request.getAttribute("retMessage");
	if(retMessage!=null) {
		out.print("<script language='javascript'>alert('"+retMessage+"'); window.close();</script>");
	} 
%>
<body topmargin=0 leftmargin=0 >
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Authorization_page' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<table class="table_noFrame">
	<tr>
		<td>&nbsp;&nbsp;
			<input name="button_ok" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Determine' bundle='${applicationAuResources}' />" onClick="javascript:myTree.saveOnClick();">
			<input name="button_cancel" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Cancel' bundle='${applicationAuResources}' />"  onclick="javascript:window.close();" >
		</td>
	</tr>
</table>
<table class="table_noFrame">
  <tr>
    <td width="30%" ><fmt:message key='venus.authority.Administrative_relations' bundle='${applicationAuResources}' /></td> 
    <td width="30%" ><fmt:message key='venus.authority.Role_relations' bundle='${applicationAuResources}' /></td> 
    <td width="30%" ><fmt:message key='venus.authority.Agent1' bundle='${applicationAuResources}' /></td> 
  </tr>
  <tr> 
     <td width="30%" valign="top"> 
		<!--树开始-->    
		<iframe name="myTree" width="100%" height="450" src="deeptree4Au.jsp
			?inputType=<%=request.getParameter("inputType")%>
			&submitType=<%=request.getParameter("submitType")==null?"submitAll":request.getParameter("submitType")%>
			&nodeRelationType=<%=request.getParameter("nodeRelationType")==null?"hasRelation":request.getParameter("nodeRelationType")%>
			&relId=<%=request.getParameter("relId")%>
			&pType=<%=request.getParameter("pType")%>
			&rootXmlSource=<%=venus.authority.util.StringHelperTools.encodeUrl(request.getParameter("rootXmlSource"))%>">
		</iframe>
		<!--树结束-->
    </td>    
     <td width="30%" valign="top"> 
        <!--树开始-->    
        <iframe name="roleTree" width="100%" height="450" src="deeptree4Au.jsp?inputType=<%=request.getParameter("inputType")%>&submitType=<%=request.getParameter("submitType")==null?"submitAll":request.getParameter("submitType")%>&nodeRelationType=<%=request.getParameter("nodeRelationType")==null?"hasRelation":request.getParameter("nodeRelationType")%>&relId=<%=request.getParameter("relId")%>&pType=<%=request.getParameter("pType")%>&rootXmlSource=<venus:base/>/jsp/authority/au/auauthorize/org.jsp?parent_code%3D<%=GlobalConstants.getRelaType_role()%>%26relId%3D<%=request.getParameter("relId") %>%26pType%3D<%=request.getParameter("pType") %>"></iframe>
        <!--树结束-->
    </td>    
    <td width="30%" valign="top"> 
        <!--树开始-->    
        <iframe name="proxyTree" width="100%" height="450" src="deeptree4Au.jsp?inputType=<%=request.getParameter("inputType")%>&submitType=<%=request.getParameter("submitType")==null?"submitAll":request.getParameter("submitType")%>&nodeRelationType=<%=request.getParameter("nodeRelationType")==null?"hasRelation":request.getParameter("nodeRelationType")%>&relId=<%=request.getParameter("relId")%>&pType=<%=request.getParameter("pType")%>&rootXmlSource=<venus:base/>/jsp/authority/au/auauthorize/org.jsp?parent_code%3D<%=GlobalConstants.getRelaType_proxy()%>%26relId%3D<%=request.getParameter("relId") %>%26pType%3D<%=request.getParameter("pType") %>"></iframe>
        <!--树结束-->
    </td>  
  </tr>
</table>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>

