<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Function_Menu_Management' bundle='${applicationAuResources}' /></title>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Function_Menu_Management' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<table width="100%"  border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td>&nbsp;&nbsp;</td>
  	<td width="270" valign="top">
      <table width="100%" height="460"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#7EBAFF">
        <tr> 
          <td bgcolor="#FFFFFF"> 
             <iframe id="funcTree" name="funcTree" width="100%" height="100%" frameborder="0" 
             src="<%=request.getContextPath()%>/jsp/authority/tree/deeptree.jsp?rootXmlSource=<%=request.getContextPath()%>/jsp/authority/au/aufunctree/rootXmlData.jsp?root_code=101" ></iframe> 
          </td>
        </tr>
      </table>
    </td>
    <td valign="top">
      <table width="95%"  height="460"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#7EBAFF" >
        <tr> 
          <td bgcolor="#FFFFFF" align="center">
				<iframe id="detailAuFunctree" name="detailAuFunctree"	style="HEIGHT:100%; VISIBILITY: inherit; WIDTH: 100%; Z-INDEX: 2" scrolling=auto frameborder=0 src="<%=request.getContextPath()%>/jsp/authority/au/aufunctree/default.jsp" ></iframe> 
			</iframe>
          </td>
        </tr>
      </table>
    </td>    
  </tr>
</table>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>

	

