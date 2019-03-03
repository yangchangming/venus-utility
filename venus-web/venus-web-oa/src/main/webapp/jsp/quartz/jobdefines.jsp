<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@page import="venus.frames.i18n.util.LocaleHolder"%>
<%@ page import="udp.quartz.extend.ConditionBackTool" %>

<fmt:bundle basename="udp.quartz.quartz_resource" prefix="udp.quartz.">
<link href="<venus:base/>/css/alex-css.jsp" type="text/css" rel="stylesheet" charset='UTF-8'>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<body>

<script language="javascript">
    writeTableTop('<fmt:message key="View_Job"/>','<venus:base/>/themes/<venus:theme/>/');
</script>

<form name="form" id="form" method="post" action="<venus:base/>/queryJobDefines.do?condition=true">
<div id="ccParentq"> 
<table class="table_div_control">
    <tr>
        <td>
            <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" onClick="javascript:hideshow('ccChildq',this,'<venus:base/>/themes/<venus:theme/>/')">
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
        <td><fmt:message key="Job_Description"/></td>
        <td><input type="text" name="description" class="text_field"></td>
    </tr>
    <tr>        
        <td><input type="button" class="button_ellipse" value='<fmt:message key="Query"/>' onclick="javascript:form.submit();"></td>
    </tr>
</table>
</div>

<div id="ccParent1"> 
<table class="table_div_control">
    <tr>
        <td>
            <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">
            <fmt:message key="Job_List"/>
        </td>
        <!--
        <td nowrap class="td_hand" onClick = "javascript:watchJob();">
        <img src="<venus:base/>/images/quartz/search.gif">查看</td>
        <td nowrap class="td_hand" onClick = "javascript:stopJob();">
        <img src="<venus:base/>/images/icon/modify.gif">编辑</td>     
        <td nowrap class="td_hand" onClick = "javascript:deleteJob();">
        <img src="<venus:base/>/images/icon/delete.gif">删除</td>
        -->
    </tr>
</table>
</div>

<!-- 列表开始 -->
<div id="ccChild1">
<table class="table_div_content">
    <tr>
        <td>
        
        <layout:collection name="jobList" id="jobDetail" styleClass="listCss" width="99%" indexId="orderNumber" align="center" sortAction="0">
            
            <layout:collectionItem width="3%" title="<input type='checkbox' pdType='control' control='checkbox_template'/>" style="text-align:center;">
                <bean:define id="wy3" name="jobDetail" property="name"/>    
                <bean:define id="wy4" name="jobDetail" property="group"/>
                <bean:define id="wy5" name="jobDetail" property="jobClass"/>                
                <input title="<%=LocaleHolder.getMessage("udp.quartz.job")+wy3%>" type="checkbox" name="checkbox_template" value="<%=wy3+","+wy4%>"/>
            </layout:collectionItem>
            
            <layout:collectionItem width="3%" title='<%=LocaleHolder.getMessage("udp.quartz.Index")%>' style="text-align:center;">
                <venus:sequence/>
                <bean:define id="wy3" name="jobDetail" property="name"/>
                <input type="hidden" signName="hiddenId" value="<%=wy3%>"/>
            </layout:collectionItem>
            

            <layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.quartz.Job_Name")%>' property="name" sortable="true"/>
            <layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.quartz.Job_Group")%>' property="group" sortable="true"/>
            <layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.quartz.Job_Class")%>' property="jobClass" sortable="true"/>
            <layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.quartz.Job_Description")%>' property="description" sortable="false"/>
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
