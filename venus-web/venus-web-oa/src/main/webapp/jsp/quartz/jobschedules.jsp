<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@page import="venus.frames.i18n.util.LocaleHolder"%>
<%@ page import="udp.quartz.extend.ConditionBackTool" %>

<fmt:bundle basename="udp.quartz.quartz_resource" prefix="udp.quartz.">

<link href="<venus:base/>/css/alex-css.jsp" type="text/css" rel="stylesheet" charset='UTF-8'>
<script language="javascript">

var checkboxName="checkbox_template";

var alertMSG='<fmt:message key="Select_One_Record"/>';

function findSelections(checkboxName, idName) {  //从列表中找出选中的id值列表
    var elementCheckbox = document.getElementsByName(checkboxName);  //通过name取出所有的checkbox
        
    var number = 0;  //定义游标
    var ids = null;  //定义id值的数组
    for(var i=0;i<elementCheckbox.length;i++){  //循环checkbox组
        if(elementCheckbox[i].checked) {  //如果被选中
            number += 1;  //游标加1
            if(ids == null) {
                ids = new Array(0);
            }
            var array11 = elementCheckbox[i].value;
            var arrayAll = array11.split(",")
            var array1 = arrayAll[0];
            ids.push(array1);  //加入选中的checkbox
        }
    }
    return ids;
}
//find taskUid
function findSelName(){
    var elementCheckbox = document.getElementsByName(checkboxName);
    for(var i=0;i<elementCheckbox.length;i++)
        if(elementCheckbox[i].checked)return elementCheckbox[i].value.split(",")[0];
    alert(alertMSG);
    return null;
}   
//find ProcDefID
function findSelGroup(){
    var elementCheckbox = document.getElementsByName(checkboxName);
    for(var i=0;i<elementCheckbox.length;i++)
        if(elementCheckbox[i].checked)return elementCheckbox[i].value.split(",")[1];
    alert(alertMSG);
    return null;
}

function searchJob(){
    form.action = "<venus:base/>/queryJobSchedules.do?condition=true";
    form.submit();
}

function jobManager(obj){
    var ids = findSelections("checkbox_template","WF_ID");
        if(ids == null) {
            alert(alertMSG);
            return;
        }
        if(ids.length > 1) {
            alert('<fmt:message key="Only_Can_A_Record"/>');
            return;
        }
    var name=findSelName();
    if(name==null)return;
    var group=findSelGroup();
    if(group==null)return;
    if(!window.confirm('<fmt:message key="Really_Perform_Operation"/>'))return;
    var form = document.all["form"];
    form.action = "<venus:base/>/scheduleJobManager.do?command="+obj.name+"&triggerName="+name+"&triggerGroup="+group;
    form.submit();
}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<body>

<script language="javascript">
    writeTableTop('<fmt:message key="Scheduling_Schedules"/>','<venus:base/>/themes/<venus:theme/>/');
</script>

<form name="form" id="form" action = "<venus:base/>/queryJobSchedules.do?condition=true" method="post" >
<div id="ccParentq"> 
<table class="table_div_control">
    <tr>
        <td>
            <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChildq',this,'<venus:base/>/themes/<venus:theme/>/')">
            <fmt:message key="Query_With_Paras"/>
        </td>
    </tr>
</table>
</div>


<div id="ccChildq">
<table class="table_div_content">
    <tr>
        <td width="100"><fmt:message key="Job_Name"/></td>
        <td><input type="text" name="jobName" class="text_field"></td>
    </tr>
    <tr>
        <td><fmt:message key="Job_Group"/></td>
        <td><input type="text" name="groupName" class="text_field"></td>
    </tr>
    <tr>
        <td><fmt:message key="Trigger_Date"/></td>
        <td><input type="text" class="text_field_reference" name="startDate" id="startDate" />
          <img class="img_1" src="<venus:base/>/themes/<venus:theme/>/images/icon/reference.gif" onClick="javascript:getYearMonthDay('startDate','<venus:base/>/');"/>
        </td>
        
    </tr>
    <!--
    <tr>
        <td>作业状态</td>
        <td><input type="text" name="state"></td>
    </tr>
    -->
    <tr>        
        </td><td>
        <td>
        <input type="button"  class="button_ellipse" value='<fmt:message key="Query"/>' onclick="searchJob();">
        <input type="reset" name="button_clear" class="button_ellipse" value='<fmt:message key="clear" bundle="${applicationResources}" />' >
        </td>
    </tr>
</table>
</div>


<div id="ccParent1"> 
<table class="table_div_control">
    <tr>
        <td>
            <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">
            <fmt:message key="Job_List"/>
        </td>
        <!--
        <td nowrap class="td_hand" onClick = "javascript:watchJob();">
        <img src="<venus:base/>/images/quartz/search.gif">查看</td>
        -->
        <td>
            <table align="right">
                <tr>
        <td nowrap class="button_ellipse" onClick = "javascript:jobManager(start);">
        <img name="start" src="<venus:base/>/themes/<venus:theme/>/images/icon/send.gif" title='<fmt:message key="immediate_run_the_task"/>' class="div_control_image"><fmt:message key="Run"/></td>
        <td nowrap class="button_ellipse" onClick = "javascript:jobManager(resume);">
        <img name="resume" src="<venus:base/>/themes/<venus:theme/>/images/icon/return.gif"  title='<fmt:message key="recover_the_suspended_task"/>' class="div_control_image"><fmt:message key="Recover"/></td>
        <td nowrap class="button_ellipse" onClick = "javascript:jobManager(pause);">
        <img name="pause" src="<venus:base/>/themes/<venus:theme/>/images/icon/pause.gif"  title='<fmt:message key="suspended_task_will_not_be_triggered"/>' class="div_control_image"><fmt:message key="Suspend"/></td>
        <!--        
        <td nowrap class="td_hand" onClick = "javascript:jobManager(stop);">
        <img name="stop" src="<venus:base/>/images/quartz/stop.gif"><fmt:message key="Stop"/></td>      
        -->
        <td nowrap class="button_ellipse" onClick = "javascript:jobManager(deleteJob);">
        <img name="deleteJob" src="<venus:base/>/themes/<venus:theme/>/images/icon/delete.gif"  title='<fmt:message key="remove_the_task_from_the_schedule"/>' class="div_control_image"><fmt:message key="Delete"/></td>
        </tr>
        </table>
        </td>
    </tr>
</table>
</div>

<!-- 列表开始 -->
<div id="ccChild1">
<table class="table_div_content2">
    <tr>
        <td>
        
        <layout:collection name="triggerList" id="triggerBo" styleClass="listCss" width="99%" indexId="orderNumber" align="center" sortAction="0">
            
            <layout:collectionItem width="3%" title="" style="text-align:center;">
                <bean:define id="wy3" name="triggerBo" property="name"/>                
                <bean:define id="wy4" name="triggerBo" property="group"/>               
                <input title="<%=LocaleHolder.getMessage("udp.quartz.trigger")+wy3%>" type="radio" name="checkbox_template" value="<%=wy3+","+wy4%>"/>
            </layout:collectionItem>
            
            <layout:collectionItem width="3%" title='<%=LocaleHolder.getMessage("udp.quartz.Index")%>' style="text-align:center;">
            <venus:sequence/>
                <bean:define id="wy3" name="triggerBo" property="name"/>
                <input type="hidden" signName="hiddenId" value="<%=wy3%>"/>
            </layout:collectionItem>

            <layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.quartz.Trigger_Name")%>' property="name" sortable="true"/>
            <layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.quartz.Trigger_Group")%>' property="group" sortable="true"/>
            <layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.quartz.Job_Name")%>' property="jobName" sortable="true"/>
            <layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.quartz.Job_Group")%>' property="jobGroup" sortable="true"/>
            <layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.quartz.Start_Time")%>' property="startTime" sortable="true"/>
            <layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.quartz.End_Time")%>' property="endTime" sortable="true"/>
            <layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.quartz.State")%>' property="state" sortable="true"/>
            <layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.quartz.Last_Trigger_Time")%>' property="previousFireTime" sortable="true"/>
            <layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.quartz.Next_Trigger_Time")%>' property="nextFireTime" sortable="true"/>
            <!--
            layout:collectionItem width="10%" title="优先级" property="priority" sortable="false"
            layout:collectionItem width="10%" title="触发器描述" property="description" sortable="false"
            -->
        </layout:collection>        
        <jsp:include page="/jsp/include/page.jsp" />
        
        </td>
    </tr>
</table>
</div>
<!-- 列表结束 -->
  
</form>
</fmt:bundle>

<script language="javascript">
    writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>

<!-- 参照显示层 -->
<div id="iframeDialog" style="display:none"></div>

</body>
</html>
<%  
    //表单回写
    if(request.getAttribute("writeBackFormValues") != null) {  //如果request中取出的bean不为空
        out.print("<script language=\"javascript\">\n");  //输出script的声明开始
        out.print(ConditionBackTool.writeBackMapToForm((java.util.Map)request.getAttribute("writeBackFormValues")));  //输出表单回写方法的脚本
        out.print("writeBackMapToForm();\n");  //输出执行回写方法
        out.print("</script>");  //输出script的声明结束
    }
%>
