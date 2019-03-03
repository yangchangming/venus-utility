<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import = "java.util.List,java.util.Iterator,java.util.ArrayList"%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Body_type_view' bundle='${applicationAuResources}' /></title>
</head>
<body>
<%
    //判断是否是引用页面
    String pageFlag = "0";
	if ("ref".equals(request.getParameter("pageFlag"))) {
		pageFlag = "1";
	}
	boolean isRoot=false;
	String partyrelationtypeId=request.getParameter("partyrelationtypeId");
	if(partyrelationtypeId!=null&&!"".equals(partyrelationtypeId)){
		isRoot=true;
	}
%>
<script>
	function reference_onClick()  {
		try   {   
			detail.reference_onClick();
		}	   
		catch(e)   {   
			window.close();
		}
	}
</script>
<input type="hidden" name="pageFlag" value="<%=pageFlag%>">
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Group_Management' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<table width="100%"  border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td>&nbsp;&nbsp;</td>
  	<td width="180" valign="top">
      <table width="100%" height="460"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#7EBAFF">
        <tr> 
          <td bgcolor="#FFFFFF"> 
             <iframe id="leftTree" name="leftTree" width="100%" height="100%" frameborder="0" src='<%if(isRoot) out.write(request.getContextPath()+"/auParty/initPage?partyrelationtypeId="+partyrelationtypeId);else out.write(request.getContextPath()+"/auParty/initPage");%>'></iframe>
          </td>
        </tr>
      </table>
    </td>
    <td valign="top">
      <table width="95%"  height="460"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#7EBAFF" >
        <tr> 
          <td bgcolor="#FFFFFF" align="center">
				<iframe id="detail" name="detail" style="HEIGHT:100%; VISIBILITY: inherit; WIDTH: 100%; Z-INDEX: 2" scrolling=auto frameborder=0 src="<%=request.getContextPath()%>/jsp/authority/org/auparty/default.jsp" ></iframe>
          </td>
        </tr>
      </table>
    </td>    
  </tr>
	<%if ("1".equals(pageFlag)) { %>
	<tr > 
		<td>&nbsp;&nbsp;</td>
		<td colspan="2" align="center">
			<br>
			<input type="button" name="btn_ok" class="button_ellipse" onClick="javascript:reference_onClick();" value="<fmt:message key='venus.authority.Determine' bundle='${applicationAuResources}' />">
			<input type="button" name="btn_close" class="button_ellipse" onClick="javascript:window.close();" value="<fmt:message key='venus.authority.Cancel' bundle='${applicationAuResources}' />">
		</td>
	</tr>
	<%}%>
</table>

<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>

</body>
</html>

