<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/gap-html.tld" prefix="venus" %>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=request.getContextPath()%>/themes/<venus:theme/>/css/gap31_mainstyle.css" type="text/css" rel="stylesheet" charset='UTF-8'>
</head>
<body> 
    <table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td height="45" align="center">
          <table width="100%" border="0" cellpadding="0" cellspacing="0" background="../../images/templatestyle/main0_04.jpg">
            <tr>
              <td width="420" valign="top"  class="indextitlebg">&nbsp;</td>
              <td background="../../images/templatestyle/main0_04.jpg"><img src="../../images/templatestyle/main0_04.jpg" width="2" height="45" /></td>
              <td width="403" align="right" class="indextitlebg_right">&nbsp;</td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td align="center" valign="top"><br>
            <br>
              <table width="580" height="283" border="0" align="center" cellpadding="0" cellspacing="0" background="<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/di1_6.gif">
          <tr>
            <td></td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td align="center" valign="bottom"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td>&nbsp;</td>
            <td align="center" class="leftfont_Content"><fmt:message key='venus.authority.RayooTech' bundle='${applicationAuResources}' />&nbsp;&nbsp; RayooTech All rights reserved. &nbsp;&nbsp;<fmt:message key='venus.authority.resolution' bundle='${applicationAuResources}' /></td>
            <td align="right">&nbsp;</td>
          </tr>
          <tr>
            <td width="11" valign="bottom"><img src="../../images/templatestyle/main0_15.gif" width="11" height="11"></td>
            <td>&nbsp;</td>
            <td width="11" align="right" valign="bottom"><img src="../../images/templatestyle/main0_18.gif" width="11" height="11"></td>
          </tr>
        </table></td>
      </tr>
    </table>
</body>
</html>
