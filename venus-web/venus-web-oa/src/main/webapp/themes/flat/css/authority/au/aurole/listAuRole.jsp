<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
	String parentCode = (String)request.getAttribute("parent_code");
%>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Role_management' bundle='${applicationAuResources}' /></title>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Role_management' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<table width="100%"  border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td>&nbsp;&nbsp;</td>
  	<td width="270" valign="top">
      <table width="100%" height="460"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#cccccc">
        <tr> 
          <td bgcolor="#FFFFFF"> 
             <iframe frameborder="0" name="tree" width="100%" height="100%" 
             	src="<%=request.getContextPath()%>/jsp/authority/tree/deeptree_iframe.jsp?
				rootXmlSource=<venus:base/>/jsp/authority/au/aurole/xmlData.jsp?parent_code=<%=parentCode%>%26data_limit%3D1">
			</iframe>
          </td>
        </tr>
      </table>
    </td>
    <td valign="top">
      <table width="95%"  height="460"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#cccccc" >
        <tr> 
          <td bgcolor="#FFFFFF" align="center">
				<iframe id="detail" name="detail"	style="HEIGHT:100%; VISIBILITY: inherit; WIDTH: 100%; Z-INDEX: 2" scrolling=auto frameborder=0 src="<%=request.getContextPath()%>/jsp/authority/au/aurole/default.jsp" ></iframe> 
			</iframe>
          </td>
        </tr>
      </table>
    </td>    
  </tr>
</table>
<!-- 参照显示层 -->
<div id="iframeDialog" style="display:none"></div>

<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>

	

