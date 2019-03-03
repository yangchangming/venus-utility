<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="udp.ewp.tools.helper.EwpVoHelper" %>
<%@ page import="udp.ewp.tools.helper.EwpStringHelper" %>
<%@ page import="udp.ewp.templatetype.model.TemplateType" %>
<%@ page import="udp.ewp.template.util.ITemplateConstants" %>
<%  //取出本条记录
    TemplateType resultVo = null;  //定义一个临时的vo变量
	resultVo = (TemplateType)request.getAttribute(ITemplateConstants.REQUEST_BEAN);  //从request中取出vo, 赋值给resultVo
	EwpVoHelper.replaceToHtml(resultVo);  //把vo中的每个值过滤
%>
<html>
<fmt:bundle basename="udp.ewp.ewp_resource" prefix="udp.ewp.">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key="detail_template"  bundle="${applicationResources}"/></title>
<script language="javascript">
    function back_onClick(){  //返回列表页面
        form.action="<%=request.getContextPath()%>/TemplateTypeAction.do?cmd=queryAll";
        form.submit();
    }
</script>
</head>
<body>
<script language="javascript">
	writeTableTop('<fmt:message key="view_page"  bundle="${applicationResources}"/>','<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<form name="form" method="post">
<table class="table_noFrame">
	<tr>
		<td><br>&nbsp;&nbsp;&nbsp;&nbsp;<input name="button_back" class="button_ellipse" type="button" value='<fmt:message key="return"  bundle="${applicationResources}"/>'  onclick="javascript:back_onClick();" >
		</td>
	</tr>
</table>

<div id="ccParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild0',this,'<venus:base/>/themes/<venus:theme/>/')"><fmt:message key="view_page"  bundle="${applicationResources}"/>
		</td>
	</tr>
</table>
</div>

<div id="ccChild0"> 
	<table class="viewlistCss" style="width:100%">
		<tr>
			<td align="right" width="10%" nowrap><fmt:message key='template_type_name'/>：</td>
			<td align="left"><%=EwpStringHelper.prt(resultVo.getTypeName())%></td>
		</tr>
		<tr>
            <td align="right" width="10%" nowrap><fmt:message key='template_type_description'/>：</td>
            <td align="left"><%=EwpStringHelper.prt(resultVo.getDescription())%></td>
        </tr>
		<tr>
			<td align="right" width="10%" nowrap><fmt:message key='created_time'  bundle="${applicationResources}"/>：</td>
			<td align="left"><%=EwpStringHelper.prt(resultVo.getCreateTime())%></td>
		</tr>
		<tr>
			<td align="right"></td>
			<td align="left"></td>
		</tr>
	</table>
</div>

<input type="hidden" name="id" value="<%=EwpStringHelper.prt(resultVo.getId())%>">

</form>
</fmt:bundle>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>
