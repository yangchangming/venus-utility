<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/WEB-INF/gap-html.tld" prefix="venus" %>
<%@ page import="venus.frames.i18n.util.LocaleHolder" %>
<head>
<script type="text/javascript">
 function changeUrl(url){
     window.parent.frames["left"].frames["leftFrameSet"].location.href=url;
     window.parent.frames["left"].frames["bodyFrame"].location.href="<%=request.getContextPath()%>/jsp/searchEngine/index/defaultContent.jsp";
 }
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Header</title>
<link type="text/css" rel="stylesheet"  href="<%=request.getContextPath()%>/themes/<venus:theme/>/css/index/style.css" />
</head>
<body>
<div class="header">
  <div class="logo"><img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/searchEngine/logo.png" /></div>
  <div class="menu">
    <div class="nav">
      <ul class="nav_content">
        <li><a href="#" onclick="changeUrl('<%=request.getContextPath()%>/jsp/searchEngine/index/searchEngine.jsp');" >全文检索引擎</a></li>
      </ul>
    </div>
  </div>
</div>
</body>
</html>
