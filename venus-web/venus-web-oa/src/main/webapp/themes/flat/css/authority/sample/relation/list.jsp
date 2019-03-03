<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.authority.org.aupartyrelationtype.bs.IAuPartyRelationTypeBS" %>
<%
	String parentCode = (String)request.getAttribute("parent_code");
    IAuPartyRelationTypeBS bs = (IAuPartyRelationTypeBS)venus.frames.mainframe.util.Helper.getBean(venus.authority.org.aupartyrelationtype.util.IConstants.BS_KEY);
    String relationTypeName = ((venus.authority.org.aupartyrelationtype.vo.AuPartyRelationTypeVo)bs.find(parentCode.substring(0,19))).getName();
    String relid = request.getParameter("parentRelId");
    if(null==relid||"".equals(relid)){
        relid = null==request.getParameter("relationId")?"":request.getParameter("relationId");
    }
%>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Organization_Management' bundle='${applicationAuResources}' /></title>
</head>
<body>
<script language="javascript">
	writeTableTop("<%=relationTypeName%><fmt:message key='venus.authority.Management' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
  <tr> 
    <td>&nbsp;&nbsp;</td>
  	<td width="270" height="600" valign="top">
      <table width="100%" height="100%"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#cccccc">
        <tr> 
          <td bgcolor="#FFFFFF"> 
             <iframe name="tree" width="100%" height="100%"  frameborder=0  
				src="<%=request.getContextPath()%>/jsp/authority/tree/deeptree_iframe.jsp?
				rootXmlSource=<venus:base/>/jsp/authority/tree/treeLimitData.jsp?
				parent_code=<%=parentCode%>%26currentRelid%3D<%=relid%>%26data_limit%3D1%26return_type%3Dparty_id%26target%3Ddetail<%=parentCode%>%26url%3D<%=request.getContextPath()%>/relation/detail">
			</iframe>
          </td>
        </tr>
      </table>
    </td>
    <td valign="top">
      <table width="95%"  height="100%"  border="0" align="center" cellpadding="0" cellspacing="1" bgcolor="#cccccc" >
        <tr> 
          <td bgcolor="#FFFFFF" align="center">
				<iframe id="detail<%=parentCode%>" name="detail<%=parentCode%>"	style="HEIGHT:100%; VISIBILITY: inherit; WIDTH: 100%; Z-INDEX: 2" scrolling=auto frameborder=0 src="<%=request.getContextPath()%>/jsp/authority/sample/relation/default.jsp" ></iframe> 
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
