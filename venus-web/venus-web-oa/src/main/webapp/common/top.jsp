<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/gap-html.tld" prefix="venus" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>无标题文档</title>
<link href="<%=request.getContextPath()%>/themes/<venus:theme/>/css/gap31_mainstyle.css" type="text/css" rel="stylesheet" charset='UTF-8'>
<script language="javascript" src="<venus:base/>/js/jquery/jquery-1.6.4.min.js"></script>
<script type="text/javascript">
    function changeLocal(local){
        if(!local||local=="") return;
        var url = "<venus:base/>/jsp/common/changeLocal.jsp?local="+local;
        jQuery.ajax({url:url,async:false,cache:false});
        top.window.location.reload();
    }
</script>
</head>
<body>
<center>
<table width="100%" height="99" border="0" cellpadding="0" cellspacing="0" background="<%=request.getContextPath()%>/themes/<venus:theme/>/images/au/di1_1.gif">
  <tr>
    <td valign="top">
	    <table width="100%" height="22" border="0" cellpadding="0" cellspacing="0">
	      <tr>
	        <td class="date">&nbsp;</td>
	        <td>&nbsp;</td>
	        <td width="504" align="right">
					<table width="504" border="0" cellpadding="0" cellspacing="0">
			          <tr>
					        <td width="250" class="toptext">Global Application Platform </td>
					        <td>&nbsp;</td>
					        <td align="right"><img src="../images/index/di1_2.gif" width="23" height="22" /></td>
					        <td nowrap="nowrap" background="../images/index/di1_4.gif">
					               <span class="topfont">　| VENUS4.1 版本  &nbsp;&nbsp;&nbsp;&nbsp;
					                           <a href="#" onclick="changeLocal('zh')">中文</a>&nbsp;&nbsp;&nbsp;
					                           <a href="#" onclick="changeLocal('en')">English</a>
					               </span>
					         </td>
			          </tr>
			        </table>
		    </td>
	        <td width="1%" background="../images/index/di1_4.gif">&nbsp;</td>
	      </tr>
	    </table>
	    <table width="100%" height="77" border="0" cellpadding="0" cellspacing="0">
	        <tr>
	          <td height="56"><img src="<venus:base/>/themes/<venus:theme/>/images/au/ufida_logo<au:i18next/>.gif" width="291" height="26" style="margin-left:10px;"/></td>
	          <td>&nbsp;</td>
	          <td width="504" align="right" valign="top"><img src="../images/index/di1_mainframe.gif" width="504" height="65" /></td>
	          <td width="1%">&nbsp;</td>
	        </tr>
	    </table>
      </td>
  </tr>
</table>
</center>
</body>	
