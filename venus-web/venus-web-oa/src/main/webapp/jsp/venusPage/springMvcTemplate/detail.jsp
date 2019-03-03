<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="udp.template.simple.util.SimpleReferenceFilter"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<fmt:bundle basename="udp.template.simple.resource" prefix="udp.template.simple.">
<title><fmt:message key="QueryTemplate"/></title>
<script language="javascript">
    var rmActionName = "TemplateAction";
    function find_onClick(){  //直接点到修改页面
        form.action="<%=request.getContextPath()%>/" + rmActionName + ".do?cmd=find";
        form.submit();
    }
    function delete_onClick(){  //直接点删除单条记录
        if(!getConfirm()) {  //如果用户在确认对话框中点"取消"
            return false;
        }
        form.action="<%=request.getContextPath()%>/" + rmActionName + ".do?cmd=delete";
        form.submit();
    }  
</script>
</head>
<body>
<script language="javascript">
    writeTableTop('<fmt:message key="Detail" />','<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<form name="form" method="post">
<table class="table_noFrame">
    <tr>
        <td>
            <!-- 
            <input name="button_update" class="button_ellipse" type="button" value="修改" onClick="javascript:find_onClick();">
            <input name="button_delete" class="button_ellipse" type="button" value="删除" onClick="javascript:delete_onClick();">
            -->
            <input name="button_back" class="button_ellipse" type="button" value='<fmt:message key="return" bundle="${applicationResources}"/>'  onclick="javascript:history.go(-1);" >
        </td>
    </tr>
</table>

<div id="ccParent0"> 
<table class="table_div_control">
    <tr> 
        <td>
            <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild0',this,'<venus:base/>/themes/<venus:theme/>/')">
                        <fmt:message key="QueryTemplate"/>
        </td>
    </tr>
</table>
</div>

<div id="ccChild0"> 
<table class="viewlistCss" style="width:100%">
    <tr> 
        <th align="right" width="10%" nowrap><fmt:message key="BuildingName"/></th>
        <td align="left"><bean:write name="buildvo" property="name" /></td>
    </tr>
    <tr>
        <th align="right"><fmt:message key="RoomName"/></th>
        <td align="left"><bean:write name="bean" property="name" /></td>
    </tr>
    <tr>
        <th align="right"><fmt:message key="BuildingArea"/></th>
        <td align="left"><bean:write name="bean" property="area" /></td>
    </tr>
    <tr>
        <th align="right"><fmt:message key="RoomType"/></th>
        <td align="left"><bean:define id="roomType" name="bean" property="type"/><%=SimpleReferenceFilter.get("RoomType",String.valueOf(roomType))%></td>
    </tr>
    <tr>
        <th align="right"><fmt:message key="RoomRate"/></th>
        <td align="left"><bean:write name="bean" property="price" /></td>
    </tr>
    <tr>
        <th align="right"><fmt:message key="IsVacant"/></th>
        <td align="left"><bean:define id="isEmpty" name="bean" property="is_Empty"/><%=SimpleReferenceFilter.get("RoomIsEmpty",String.valueOf(isEmpty))%></td>
    </tr>
    <tr>
        <th align="right"><fmt:message key="CheckinDate"/></th>
        <td align="left"><bean:define id="residingDate" name="bean" property="residing_Date"/>
        <%
             java.text.SimpleDateFormat  format = new java.text.SimpleDateFormat("yyyy-MM-dd");
             out.print(format.format(format.parse(String.valueOf(residingDate))));
         %>
        </td>
    </tr>
    <tr>
        <th align="right"><fmt:message key="Remark"/></th>
        <td align="left"><bean:write name="bean" property="brief" /></td>
    </tr>
</table>
</div>

<input type="hidden" name="id" value="<bean:write name="bean" property="id" />" />

</form> 
</fmt:bundle>
<script language="javascript">
    writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>
