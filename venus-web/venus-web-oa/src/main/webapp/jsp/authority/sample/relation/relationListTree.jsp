<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%
    String parentCode = (String) request.getAttribute("parent_code");
    String relid = request.getParameter("parentRelId");
    if (null == relid || "".equals(relid)) {
        relid = null == request.getParameter("relationId") ? "" : request.getParameter("relationId");
    }
    String returnType =  StringUtils.trimToEmpty((String)request.getParameter("returnType"));
    String dataLimit =  StringUtils.trimToEmpty((String)request.getParameter("dataLimit"));
    String[] excWithYesAu =  request.getParameterValues("excWithYesAu");
    String[] excWithNoAu =  request.getParameterValues("excWithNoAu");
    String excYesStr = "";
    if(null!=excWithYesAu){
        for(int i = 0;i<excWithYesAu.length;i++){
            excYesStr += ("%26excWithYesAu%3D"+excWithYesAu[i]);
        }
    }    
    String excNoStr = "";
    if(null!=excWithNoAu){
        for(int i = 0;i<excWithNoAu.length;i++){
            excNoStr += ("%26excWithNoAu%3D"+excWithNoAu[i]);
        }
    }    
    String urlDirection =  java.net.URLEncoder.encode(StringUtils.trimToEmpty((String)request.getParameter("urlDirection")),"UTF-8");
    urlDirection =  java.net.URLEncoder.encode(urlDirection,"UTF-8");
%>
<%@ include file="/jsp/include/global.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Organization_Management'
    bundle='${applicationAuResources}' /></title>
</head>
<body>
<script language="javascript">
    writeTableTop("<fmt:message key='venus.authority.Organization_Management' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td>&nbsp;&nbsp;</td>
        <td width="270" valign="top">
        <table width="100%" height="100%" border="0" align="center"
            cellpadding="0" cellspacing="1" bgcolor="#7EBAFF">
            <tr>
                <td bgcolor="#FFFFFF"><iframe name="tree" width="100%"
                    height="100%" frameborder=0
                    src="<%=request.getContextPath()%>/jsp/authority/tree/deeptree_iframe.jsp?rootXmlSource=<venus:base/>/jsp/authority/tree/treeLimitData.jsp?parent_code=<%=parentCode%>%26currentRelid%3D<%=relid%>%26data_limit%3D<%=dataLimit%><%=excYesStr%><%=excNoStr%>%26return_type%3D<%=returnType%>%26target%3DdetailCustom%26url%3D<%=urlDirection%>">
                </iframe></td>
            </tr>
        </table>
        </td>
        <td valign="top">
        <table width="95%" height="100%" border="0" align="center"
            cellpadding="0" cellspacing="1" bgcolor="#7EBAFF">
            <tr>
                <td bgcolor="#FFFFFF" align="center"><iframe id="detailCustom"
                    name="detailCustom"
                    style="HEIGHT: 100%; VISIBILITY: inherit; WIDTH: 100%; Z-INDEX: 2"
                    scrolling=auto frameborder=0
                    src="<%=request.getContextPath()%>/jsp/authority/sample/relation/default.jsp"></iframe>
                </iframe></td>
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
