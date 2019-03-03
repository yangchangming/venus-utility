<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/include/global.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="venus.frames.i18n.util.LocaleHolder" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Menu</title>
<link type="text/css" rel="stylesheet" id="leftcss"   href="<%=request.getContextPath()%>/themes/<venus:theme/>/css/index/style.css">
<script type="text/javascript">$(document).ready(function()
{
    $("#firstpane p.menu_head").click(function()
    {
        
        $(this).next("div.menu_body").slideToggle(300).siblings("div.menu_body").slideUp("slow");
    $(this).siblings();
        $("#firstpane p.menu_head").css({height:"25px"});      
    });
});
</script>
</head>
<body>
<div class="main">
<br />
  <div id="tc" align="left"><img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/index/icon.png" />&nbsp;列表控件</div>
  <div class="side">
    <div id="firstpane" class="menu_list">
        <div class="menu_body">
         <a href="<%=request.getContextPath()%>/TemplateAction.do?cmd=queryAll"  target="bodyFrame">单表列表控件</a>  
        </div>
        <div class="menu_body">
            <a href="<%=request.getContextPath()%>/template/index"  target="bodyFrame">springMVC示例</a>
        </div>
    </div>
  </div>
  <div id="bc"></div>
</div>
</body>
</html>
