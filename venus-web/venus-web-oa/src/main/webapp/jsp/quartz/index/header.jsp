<%@ page contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/gap-html.tld" prefix="venus" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="venus.frames.i18n.util.LocaleHolder" %>
<%@ include file="/jsp/include/global.jsp" %>

<fmt:bundle basename="udp.quartz.quartz_resource" prefix="udp.quartz.">
<head>
<script type="text/javascript">
 function changeUrl(url,bodyUrl){
     window.parent.frames["left"].frames["leftFrameSet"].location.href=url;
     window.parent.frames["left"].frames["bodyFrame"].location.href=bodyUrl;
 }
</script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Header</title>
<link type="text/css" rel="stylesheet"  href="<%=request.getContextPath()%>/themes/<venus:theme/>/css/index/style.css" />
</head>
<body>
<div class="header">
  <div class="logo"><img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/quartz/logo.png" /></div>
  <div class="menu">
    <div class="nav">
      <ul class="nav_content">
        <li><a href="#" onclick="changeUrl('<%=request.getContextPath()%>/jsp/quartz/index/scheduleCenter.jsp','<%=request.getContextPath()%>/queryJobSchedules.do');" >调度中心</a></li>
        <li><a href="#" onclick="changeUrl('<%=request.getContextPath()%>/jsp/quartz/index/jobManage.jsp','<%=request.getContextPath()%>/queryJobDefines.do');" >作业管理</a></li>
      </ul>
    </div>
  </div>
</div>
</fmt:bundle>
</body>
</html>
