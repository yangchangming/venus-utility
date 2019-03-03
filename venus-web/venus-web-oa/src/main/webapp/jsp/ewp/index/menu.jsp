<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/WEB-INF/gap-html.tld" prefix="venus" %>
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
  <div id="tc" align="left"><img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/index/icon.png" />&nbsp;库存</div>
  <div class="side">
    <div id="firstpane" class="menu_list">

	    <p class="menu_head">流程运行</p>
	    <div class="menu_body">
	        <a href="#" target="mainFrame">仓库档案</a>
	        <a href="#"target="mainFrame">物料编码方案</a>
	        <a href="#" target="mainFrame">物料分类</a>
	        <a href="#" target="mainFrame">物料基础档案</a>
	    </div>

	    <p class="menu_head">流程设计</p>
	    <div class="menu_body">
	        <a href="#" target="mainFrame">入库单浏览</a>
	        <a href="#" target="mainFrame">入库单录入</a>
	        <a href="#" target="mainFrame">入库单质检</a>
	        <a href="#" target="mainFrame">入库单点验</a>
	        <a href="#" target="mainFrame">入库单记账</a>
	    </div>

    </div>
  </div>
  <div id="bc"></div>
</div>
</body>
</html>
