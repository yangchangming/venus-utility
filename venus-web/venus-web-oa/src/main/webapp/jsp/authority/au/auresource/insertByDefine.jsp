<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import = "java.util.List,java.util.Iterator,java.util.ArrayList,java.util.HashMap"%>
<%@ page import="venus.authority.au.auresource.vo.AuResourceVo"%>
<%@ page import="venus.authority.au.auresource.util.IAuResourceConstants"%>
<%@ page import = "venus.authority.util.VoHelperTools"%>
<%  //判断是否为修改页面
	boolean isModify = false;
	if(request.getParameter("isModify") != null) {
		isModify = true;
	}
%>
<%@ include file="/jsp/include/global.jsp" %>
<title><%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Modify_records_resources"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.New_Records_of_resources")%></title>
<script>
<!--
	function insert_onClick(){  //增加记录
		form.action = "<venus:base/>/auResource/insert";
    	form.submit();
  	}
  	
  	function update_onClick(){
  		form.action = "<venus:base/>/auResource/update";
  		form.submit();
  	}
  	
	function test_onClick(){
		if(document.form.value.value==''){
  			alert("<fmt:message key='venus.authority.Please_fill_in_the_filter_value_' bundle='${applicationAuResources}' />");
			document.form.value.focus();
			return false;
		}
		var height=window.screen.height/2-50;
		var width=window.screen.width/2-50;
		var urlTest="<venus:base/>/auResource/testSQL?tableName="
			+document.form.table_name.value
			+"&fieldName="+document.form.field_name.value
			+"&fieldType="+document.form.filter_type.value
			+"&fieldValue="+document.form.value.value;
		window.open (urlTest, 'testwindow', 'height=120, width=100, top='+height+', left='+width+', toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no');
  	}
    
	function cancel(){
		form.action = "<venus:base/>/auResource/queryAll";
		form.submit();
	}
//-->  	
</script>

</head>
<body>
<script language="javascript">
	writeTableTop('<%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Modify_page"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.New_page")%>','<venus:base/>/themes/<venus:theme/>/');
</script>

<form name="form" method="post">
<table class="table_noframe">
	<tr>
		<td valign="middle">
			<input name="button_save" type="button" class="button_ellipse" value="<fmt:message key='venus.authority.Save' bundle='${applicationAuResources}' />" onClickto="javascript:<%=isModify?"update_onClick()":"insert_onClick()"%>;">
			<input name="button_cancel" type="button" class="button_ellipse" value="<fmt:message key='venus.authority.Cancel' bundle='${applicationAuResources}' />"  onClick="javascript:returnBack();">
		</td>
	</tr>
</table>

<div id="ccParent1"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')"><%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Modify_the_field_of_resources"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Additional_field_resources0")%>
		</td>
	</tr>
</table>
</div>

<div id="ccChild1"> 
<table class="table_div_content">
	<tr> 
		<td align="right" width="10%" nowrap><span class="style_required_red">* </span><fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' /></td>
		<td align="left">
			<input name="name" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' />" maxlength="150" validate="notNull" value="">
		</td>		
	</tr>	
	<tr>
	    <td align="right" width="10%" nowrap><span class="style_required_red">* </span><fmt:message key='venus.authority.Field_name' bundle='${applicationAuResources}' /></td>
	    <td align="left">
			<input name="field_name" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Field_name' bundle='${applicationAuResources}' />" maxlength="150" validate="notNull" value="">
		</td>		
	</tr>
	<tr>
	    <td align="right" width="10%" nowrap><span class="style_required_red">* </span><fmt:message key='venus.authority.Table_Chinese_name' bundle='${applicationAuResources}' /></td>
	    <td align="left">
			<input name="table_chinesename" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Table_Chinese_name' bundle='${applicationAuResources}' />" maxlength="150" validate="notNull" value="">
		</td>		
	</tr>	
	<tr>
	    <td align="right" width="10%" nowrap><span class="style_required_red">* </span><fmt:message key='venus.authority.Table_name' bundle='${applicationAuResources}' /></td>
	    <td align="left">
			<input name="table_name" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Table_name' bundle='${applicationAuResources}' />" maxlength="150" validate="notNull" value="">
		</td>		
	</tr>	
	<tr> 
		<td align="right" width="10%" nowrap><span class="style_required_red">* </span><fmt:message key='venus.authority.Filters' bundle='${applicationAuResources}' /></td>
		<td align="left">
			<textarea name="value" class="textarea_limit_words" cols="55" rows="6" maxLength="300" id="valueId" inputName="<fmt:message key='venus.authority.Filters' bundle='${applicationAuResources}' />"></textarea>
		</td>		
	</tr>	
</table>
</div>

<input name="id" type="hidden" value="">
<input name="access_type" type="hidden" value="1">
<input name="enable_status" type="hidden" value="1">
<input name="resource_type" type="hidden" value="4">
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>
</body>
</html>
<%  //取出要修改的那条记录，并且回写表单
	if(isModify) {  //如果本页面是修改页面
  		out.print("<script language=\"javascript\">\n");  //输出script的声明开始
  		AuResourceVo resultVo = null;  //定义一个临时的vo变量
  		if(request.getAttribute(IAuResourceConstants.REQUEST_BEAN_VALUE) != null) {  //如果request中取出的bean不为空
  			resultVo = (AuResourceVo)request.getAttribute(IAuResourceConstants.REQUEST_BEAN_VALUE);  //从request中取出vo, 赋值给resultVo
  		}
  		if(resultVo != null) {  //如果vo不为空
			out.print(VoHelperTools.writeBackMapToForm(VoHelperTools.getMapFromVo(resultVo)));  //输出表单回写方法的脚本
			out.print("writeBackMapToForm();\n");  //输出执行回写方法
		}
		out.print("</script>");  //输出script的声明结束
  	}
%>

