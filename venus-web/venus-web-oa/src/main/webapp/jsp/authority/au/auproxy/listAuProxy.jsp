<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
	String parentCode = (String)request.getAttribute("parent_code");
%>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Agent_Management' bundle='${applicationAuResources}' /></title>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Agent_Management' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<table width="100%"  border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>&nbsp;&nbsp;</td>
  	<td width="220" valign="top">
      <table width="100%" height="600"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#7EBAFF">
        <tr>
          <td bgcolor="#FFFFFF">
             <iframe name="tree" width="100%" height="100%"
             	src="<%=request.getContextPath()%>/jsp/authority/tree/deeptree_iframe.jsp?
				rootXmlSource=<venus:base/>/jsp/authority/au/auproxy/xmlData.jsp?parent_code=<%=parentCode%>">
			</iframe>
          </td>
        </tr>
      </table>
    </td>
    <td valign="top">
      <table width="95%"  height="600"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#7EBAFF" >
        <tr>
          <td bgcolor="#FFFFFF" align="center">
				<iframe id="detail" name="detail"	style="HEIGHT:100%; VISIBILITY: inherit; WIDTH: 100%; Z-INDEX: 2" scrolling=auto frameborder=0 src="<%=request.getContextPath()%>/jsp/authority/au/auproxy/default.jsp" ></iframe>
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



