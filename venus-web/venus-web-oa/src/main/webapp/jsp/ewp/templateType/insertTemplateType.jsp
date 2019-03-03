<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="udp.ewp.tools.helper.EwpVoHelper" %>
<%@ page import="udp.ewp.templatetype.model.TemplateType" %>
<%@ page import="udp.ewp.templatetype.util.ITemplateTypeConstants" %>
<%@ include file="/jsp/include/global.jsp" %>
<% 
//判断是否为修改页面
TemplateType resultVo = null;  //定义一个临时的vo变量
	boolean isModify = false;  //定义变量,标识本页面是否修改(或者新增)
	if(request.getParameter("isModify") != null) {  //如果从request获得参数"isModify"不为空
		isModify = true;  //赋值isModify为true
		if(request.getAttribute(ITemplateTypeConstants.REQUEST_BEAN) != null) {  //如果request中取出的bean不为空
  		    resultVo = (TemplateType)request.getAttribute(ITemplateTypeConstants.REQUEST_BEAN);  //从request中取出vo, 赋值给resultVo
  		}
	}
%>
<html>
<fmt:bundle basename="udp.ewp.ewp_resource" prefix="udp.ewp.">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>
<script language="javascript">
    if(<%=isModify%>)
        out.print('<fmt:message key="modify_page"  bundle="${applicationResources}" />');
    else
        out.print('<fmt:message key="new_page"  bundle="${applicationResources}"/>');
</script>
</title>
<script language="javascript">
	function insert_onClick(){  //插入单条数据
		form.action="<%=request.getContextPath()%>/TemplateTypeAction.do?cmd=insert";
		form.submit();
	}

  	function update_onClick(id){  //保存修改后的单条数据
    	if(!getConfirm()) {  //如果用户在确认对话框中点"取消"
  			return false;
		}
	    form.action="<%=request.getContextPath()%>/TemplateTypeAction.do?cmd=update";
    	form.submit();
	}

    function cancel_onClick(){  //取消后返回列表页面
        form.action="<%=request.getContextPath()%>/TemplateTypeAction.do?cmd=queryAll";
        form.submit();
    }
</script>
</head>
<body>
<script language="javascript">
    if(<%=isModify%>)
        writeTableTop('<fmt:message key="modify_page"  bundle="${applicationResources}"/>','<venus:base/>/themes/<venus:theme/>/');
    else
        writeTableTop('<fmt:message key="new_page"  bundle="${applicationResources}"/>','<venus:base/>/themes/<venus:theme/>/');
</script>
<form name="form" method="post">

<table class="table_noFrame">
	<tr>
		<td><br>&nbsp;&nbsp;&nbsp;&nbsp;<input name="button_save" class="button_ellipse" type="button" value='<fmt:message key="save"  bundle="${applicationResources}"/>' onClickTo="javascript:<%=isModify?"update_onClick()":"insert_onClick()"%>">
			<input name="button_cancel" class="button_ellipse" type="button" value='<fmt:message key="cancel"  bundle="${applicationResources}"/>'  onClick="javascript:cancel_onClick()" >
		</td>
	</tr>
</table>

<div id="ccParent1"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">
            <c:if test="<%=isModify%>">
                <fmt:message key="modify_page"  bundle="${applicationResources}"/>
            </c:if>
            <c:if test="<%=(isModify==false)%>">
                <fmt:message key="new_page"  bundle="${applicationResources}"/>
            </c:if>
		</td>
	</tr>
</table>
</div>

<div id="ccChild1"> 
<table class="table_div_content">
<tr><td> 
	<table class="table_div_content_inner">
		<tr>
			<td align="right"><fmt:message key="template_type_name"/></td>
			<td align="left">
				<input type="text" class="text_field" name="typeName" inputName="typeName" value="<%=(resultVo==null?"":resultVo.getTypeName() )%>" maxLength="50" />
			</td>
			<td align="right"></td>
			<td align="left"></td>
		</tr>
		<tr>
            <td align="right"><fmt:message key="template_type_description"/></td>
            <td align="left">
                <input type="text" class="text_field" name="description" inputName="description" value="<%=(resultVo==null?"":resultVo.getDescription()) %>" maxLength="50" />
            </td>
            <td align="right"></td>
            <td align="left"></td>
        </tr>
		
		<tr>
			<td align="right"></td>
			<td align="left"></td>
			<td align="right"></td>
			<td align="left"></td>
		</tr>
	</table>
</td></tr>
</table>
</div>
            
<input type="hidden" name="id" value="<%=(resultVo==null?"":resultVo.getId()) %>">
<input type="hidden" name="createTime" />

</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
  </fmt:bundle>
</body>
</html>
<script language="javascript">
<%  //取出要修改的那条记录，并且回写表单
	if(isModify) {  //如果本页面是修改页面
	//	out.print(EwpVoHelper.writeBackMapToForm(EwpVoHelper.getMapFromVo(resultVo)));  //输出表单回写方法的脚本
  	}
%>
</script>
