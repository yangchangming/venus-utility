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
  <div id="tc" align="left"><img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/index/icon.png" />&nbsp;<%=LocaleHolder.getMessage("udp.gbox.msg.resourcemgt") %></div>
  <div class="side">
    <div id="firstpane" class="menu_list">

	    <p class="menu_head"><%=LocaleHolder.getMessage("udp.gbox.msg.resourcemgt") %></p>
	    <div class="menu_body">
	       <a href="<%=request.getContextPath()%>/ResourceTypeAction.do?cmd=queryAll"  target="bodyFrame"><%=LocaleHolder.getMessage("udp.gbox.menu.typemgt") %></a>
	       <a href="<%=request.getContextPath()%>/ClassificationAction.do?cmd=queryAll" target="bodyFrame"><%=LocaleHolder.getMessage("udp.gbox.importresource") %></a>
	       <a href="<%=request.getContextPath()%>/ResourceAction.do?cmd=queryAll" target="bodyFrame"><%=LocaleHolder.getMessage("udp.gbox.resourceview") %></a>
	        <a href="<%=request.getContextPath()%>/OptionAction.do?cmd=queryAll" target="bodyFrame"><%=LocaleHolder.getMessage("udp.gbox.systemoption") %></a>
	        <a href="<%=request.getContextPath()%>/jsp/gbox/reference/referenceIndex.jsp" target="bodyFrame"><%=LocaleHolder.getMessage("udp.gbox.msg.userguide") %></a>
	    </div>

	  

    </div>
  </div>
  <div id="bc"></div>
</div>
</body>
</html>
