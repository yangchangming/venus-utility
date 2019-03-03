<%@ page contentType="text/html; charset=UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="venus.frames.i18n.util.LocaleHolder" %>
<%@ taglib uri="/WEB-INF/gap-html.tld" prefix="venus" %>
<%@ include file="/jsp/include/global.jsp" %>

<fmt:bundle basename="udp.quartz.quartz_resource" prefix="udp.quartz.">
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
  <div id="tc" align="left"><img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/index/icon.png" />&nbsp;调度中心</div>
  <div class="side">
    <div id="firstpane" class="menu_list">
        <div class="menu_body">
         <a href="<%=request.getContextPath()%>/scheduleInfoForm.do"  target="bodyFrame">调度控制台</a>  
        </div>
        
        <div class="menu_body">
         <a href="<%=request.getContextPath()%>/queryJobSchedules.do"  target="bodyFrame">调度计划表</a>  
        </div>
    </div>
  </div>
  
  <div id="bc"></div>
</div>
</fmt:bundle>
</body>
</html>
