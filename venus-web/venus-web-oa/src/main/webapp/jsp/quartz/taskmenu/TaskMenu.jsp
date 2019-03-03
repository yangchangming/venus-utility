<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.net.URLEncoder" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=request.getContextPath()%>/js/common/TaskMenu.js"></script>
<link href="<venus:base/>/css/alex-css.jsp" type="text/css" rel="stylesheet" charset='UTF-8'>
<script>
var taskMenu1;
var taskMenu2;

var item1;
var item2;
var item3;
var item4;
var item5;
var item6;
var item7;
var item8;
TaskMenu.setStyle("<%=request.getContextPath()%>/css/Blue/blueStyle.css"); 

window.onload = function()
{
	TaskMenu.setHeadMenuSpecial(true);
	//TaskMenu.setScrollbarEnabled(true);
	//TaskMenu.setAutoBehavior(false);
	
	////////////////////////////////////////////////
//	item1 = new TaskMenuItem("菜单设置","<%=request.getContextPath()%>/images/quartz/button_demo.gif","parent.window.frames[1].location.href='TaskMenu_Demo.jsp'");
//	item2 = new TaskMenuItem("API","<%=request.getContextPath()%>/images/icons/button_demo.gif","parent.window.frames[1].location.href='TaskMenu_API.html'");
	////////////////////////////////////////////////
	
//	taskMenu1 = new TaskMenu("Control Panel");
//	item11 = new TaskMenuItem("任务调度日志","<%=request.getContextPath()%>/images/quartz/button_fc1.gif",	"parent.window.frames[1].location.href='<%=request.getContextPath()%>/quartzLog.do'");
//	taskMenu1.add(item1);
//	taskMenu1.add(item11);
//	taskMenu1.setBackground("<%=request.getContextPath()%>/images/alexmainframe/bg.gif");
//	taskMenu1.init();
//	taskMenu1._close();
	
	///////////////////////////////////////////
	taskMenu2 = new TaskMenu("调度中心");
	//sample: xxxx.do?pstate=add&WF_SECTION=<%=URLEncoder.encode("行政类流程","GBK")%>&WF_PACKAGE=YourName
	item21 = new TaskMenuItem("调度控制台","<%=request.getContextPath()%>/images/quartz/button_fc1.gif",
		"parent.window.frames[1].location.href='<%=request.getContextPath()%>/scheduleInfoForm.do'");
	item22 = new TaskMenuItem("调度计划表","<%=request.getContextPath()%>/images/quartz/button_fc1.gif",
		"parent.window.frames[1].location.href='<%=request.getContextPath()%>/queryJobSchedules.do'");		
	taskMenu2.add(item21);
	taskMenu2.add(item22);
	taskMenu2.init();
		
 	///////////////////////////////////////////
	taskMenu3 = new TaskMenu("作业管理");	
	item31 = new TaskMenuItem("查看作业","<%=request.getContextPath()%>/images/quartz/button_fc1.gif",
		"parent.window.frames[1].location.href='<%=request.getContextPath()%>/queryJobDefines.do'");
	item32 = new TaskMenuItem("创建作业","<%=request.getContextPath()%>/images/quartz/button_fc1.gif",
		"parent.window.frames[1].location.href='<%=request.getContextPath()%>/createJobForm.do?cmd=doCreateJobForm'");		
	
	taskMenu3.add(item31);
	taskMenu3.add(item32);
	taskMenu3.init();
	//Menu set over//
}
</script>
</head>
</html>