<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="udp.quartz.scheduledata.bo.SchedulerBo"%>
<fmt:bundle basename="udp.quartz.quartz_resource" prefix="udp.quartz.">
<%
    SchedulerBo schedulerBo = (SchedulerBo)request.getAttribute("schedulerBo");
%>
<script language="javascript">

function setScheduler(){
    form1.action = "<venus:base/>/scheduleInfoForm.do";
    form1.submit();
    
}

function changScheduler(obj){
    var command=obj;
    var src1 = document.getElementById("schedulerTable").rows[1].cells[1];
    if(src1.innerHTML=='<fmt:message key="Run"/>'&&(command=="start"||command=="recover"))
    {
        alert('<fmt:message key="Is_Running"/>');
    }else if(src1.innerHTML=='<fmt:message key="Stop"/>'&&command!="start")
    {
        alert('<fmt:message key="Start_First"/>');
    }else if(src1.innerHTML=='<fmt:message key="Suspend"/>'&&command!="recover")
    {
        alert('<fmt:message key="Please_Recover"/>');
    }else
    {
        //alert("action ---------");
        form1.action = "<venus:base/>/scheduleInfoForm.do";
        form1.operation.value=command;
        form1.submit();
    }
}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<body>

<script language="javascript">
    writeTableTop('<fmt:message key="Scheduling_Console"/>','<venus:base/>/themes/<venus:theme/>/');
</script>

<form name="form1" action="" method="post">
<input name="operation" type="hidden" value="">
<div id="ccParentq"> 
<table class="table_div_control">
    <tr>
        <td>
            <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" onClick="javascript:hideshow('ccChildq',this,'<venus:base/>/themes/<venus:theme/>/')">
            <fmt:message key="Console_Information"/>
        </td>
    </tr>
</table>
</div>
<div id="ccChildq">
<table class="table_div_content">
    <!--
    <tr>
        <td width="200">调度计划</td>
        <td>
        <select id="choosenSchedulerName" name="choosenSchedulerName" onchange="submit()">
            
                <option id="schedulerName" value="schedulerName">调度器一</option>
        </select>
        </td>
    </tr>
    -->
    <tr>
    <td>
    <table id="schedulerTable" >
    <tr>
        <td width="130"><fmt:message key="Scheduling_Name"/></td><td><%=schedulerBo.getSchedulerName()%></td>
    </tr>
    <tr>
        <td><fmt:message key="Console_State"/></td><td><%=schedulerBo.getState()%></td>
    </tr>
    <tr>
        <td><fmt:message key="Start_Time"/></td><td><%=schedulerBo.getRunningSince()%></td>
    </tr>
    <tr>
        <td><fmt:message key="Run_Job_Number"/></td><td><%=schedulerBo.getNumJobsExecuted()%></td>
    </tr>
    <tr>
        <td><fmt:message key="Storage_Type"/></td><td><%=schedulerBo.getPersistenceType()%></td>
    </tr>
    <tr>
        <td><fmt:message key="ThreadPool_Size"/></td><td><%=schedulerBo.getThreadPoolSize()%></td>
    </tr>
    <tr>
        <td><fmt:message key="Scheduler_Version"/></td><td><%=schedulerBo.getVersion()%></td>
    </tr>
    <tr/>
    </table>
    </td>
    </tr>
    <tr>
        <td>
<table>
    <tr>
        <td width="30">
            <input type="button" class="button_ellipse" name="command"  value='<fmt:message key="Start"/>' title='<fmt:message key="start_plan"/>' onclick="changScheduler('start');"/>
        </td>
        <td width="30">
            <input type="button" class="button_ellipse" name="command"  value='<fmt:message key="Suspend"/>'  title='<fmt:message key="suspend_plan"/>' onclick="changScheduler('pause');"/>
        <td width="30">
            <input type="button" class="button_ellipse" name="command"  value='<fmt:message key="Recover"/>' title='<fmt:message key="recover_plan"/>' onclick="changScheduler('recover');"/> 
        </td>
        <td width="30">
            <input type="button" class="button_ellipse" name="command"  value='<fmt:message key="Stop"/>'  title='<fmt:message key="stop_plan"/>' onclick="changScheduler('waitAndStop');"/>  
        </td>
    </tr>
</table>
</td>
</tr>
</table>
</div>
<!--
设为当前调度计划 <input type="button"  value="设置" onclick="setScheduler()"/>
-->
</form>
</fmt:bundle>

<script language="javascript">
    writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>

</body>
</html>
