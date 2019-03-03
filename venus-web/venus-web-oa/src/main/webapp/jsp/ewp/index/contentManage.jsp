<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/WEB-INF/gap-html.tld" prefix="venus" %>
<%@ page import="venus.frames.i18n.util.LocaleHolder" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Menu</title>
<link type="text/css" rel="stylesheet" id="leftcss"   href="<%=request.getContextPath()%>/themes/<venus:theme/>/css/index/style.css">
<script type="text/javascript" language="javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.6.4.min.js"></script>
<script type="text/javascript">$(document).ready(function()
{
	$("#firstpane p.menu_head").click(function()
    {
		
		$(this).next("div.menu_body").slideToggle(300).siblings("div.menu_body").slideUp("slow");
		$("#firstpane p.menu_head").css({height:"25px"});		
	});
});
</script>
</head>
<body>
<div class="main">
<br />
  <div id="tc" align="left"><img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/index/icon.png" />&nbsp;<%=LocaleHolder.getMessage("udp.ewp.content_manage") %></div>
  <div class="side">
    <div id="firstpane" class="menu_list">

	    <p class="menu_head"><%=LocaleHolder.getMessage("udp.ewp.doctype_manage") %></p>
	    <div class="menu_body">
	     <a href="<%=request.getContextPath()%>/jsp/ewp/docType/docType.jsp"  target="bodyFrame"><%=LocaleHolder.getMessage("udp.ewp.doctype_manage") %></a>  
	    </div>

	    <p class="menu_head"><%=LocaleHolder.getMessage("udp.ewp.document_manage") %></p>
	    <div class="menu_body">
	       <a href="<%=request.getContextPath()%>/jsp/ewp/document/document.jsp"  target="bodyFrame"><%=LocaleHolder.getMessage("udp.ewp.document_manage") %></a>
	    </div>

    </div>
  </div>
  <div id="bc"></div>
</div>
</body>
</html>
