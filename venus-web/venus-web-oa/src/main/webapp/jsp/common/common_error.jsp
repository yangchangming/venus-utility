<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ include file="/jsp/include/global.jsp" %>

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
          <table width="100%" border="0" cellpadding="0" cellspacing="0" background="<venus:base/>/images/templatestyle/main0_04.jpg">
            <tr>
              <td valign="top" ><img src="<venus:base/>/images/templatestyle/main0_03.jpg" width="80" height="45" /></td>
              <td width="99%" background="<venus:base/>/images/templatestyle/main0_04.jpg">
                  <img src="<venus:base/>/images/templatestyle/main0_04.jpg" width="2" height="45" />
              </td>
              <td align="right">
                  <img src="<venus:base/>/images/templatestyle/main0_05.jpg" width="80" height="45" />
              </td>
            </tr>
          </table>
        </td>
      </tr>
      <tr>
        <td align="center" valign="top"><br>
            <br>
            <table width="90%" border="0" align="center" cellspacing="3" class="tablebg">
              <tr>
                <td height="35" class="main_shm">
                    <img src="<venus:base/>/images/index/yq_bt<au:i18next/>.jpg" width="73" height="14"> 错误信息如下，请参考</td>
              </tr>
              <tr>
                <td class="main_shm1">◎<%=(request.getAttribute("Message")==null)?"无":request.getAttribute("Message")%>
                </td>
              </tr>
              <tr>
                <td class="main_shm">如有问题请与系统开发商联系。</td>
              </tr>
              <tr><td class="main_shm"><br><a href="#" onclick="javascript:window.close();">关闭窗口</a></td></tr>
              </table></td>
      </tr>
    </table>
</body>
</html>