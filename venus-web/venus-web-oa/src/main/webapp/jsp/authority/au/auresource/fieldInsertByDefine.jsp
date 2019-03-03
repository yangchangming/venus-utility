<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import = "java.util.List,java.util.Iterator,java.util.ArrayList,java.util.HashMap"%>
<%@ page import="venus.authority.au.auresource.vo.AuResourceVo"%>
<%@ page import = "venus.authority.util.VoHelperTools"%>
<%  //判断是否为修改页面
	boolean isModify = false;
	if(request.getParameter("isModify") != null) {
		isModify = true;
	}
%>
<%@ include file="/jsp/include/global.jsp" %>
<title><%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Modify_the_field_of_resources"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Additional_field_resources0")%></title>
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
	    <td align="right" width="10%" nowrap><span class="style_required_red">* </span><fmt:message key='venus.authority.Chinese_name_field' bundle='${applicationAuResources}' /></td>
	    <td align="left">
			<input name="field_chinesename" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Chinese_name_field' bundle='${applicationAuResources}' />" maxlength="150" validate="notNull" value="">
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
		<td align="right"><span class="style_required_red">* </span><fmt:message key='venus.authority.Whether_the_public' bundle='${applicationAuResources}' /></td>
		<td align="left">
			<input type="radio" name="is_public" inputName="<fmt:message key='venus.authority.Whether_the_public' bundle='${applicationAuResources}' />" value="1"  /><fmt:message key='venus.authority.Be' bundle='${applicationAuResources}' />
			<input type="radio" name="is_public" inputName="<fmt:message key='venus.authority.Whether_the_public' bundle='${applicationAuResources}' />" value="0" checked /><fmt:message key='venus.authority.No0' bundle='${applicationAuResources}' />
		</td>
	</tr>
	<tr>
	    <td align="right" width="10%" nowrap><fmt:message key='venus.authority.Help' bundle='${applicationAuResources}' /></td>
	    <td align="left">
			<textarea class="textarea_limit_words" cols="60" rows="5" name="help" maxLength="500" id="helpId"></textarea>
		</td>		
	</tr>	
</table>
</div>

<input name="id" type="hidden" value="">
<input name="access_type" type="hidden" value="1">
<input name="enable_status" type="hidden" value="1">
<input name="resource_type" type="hidden" value="3">
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>
</body>
</html>
<%  //表单回写
	if(request.getAttribute("writeBackFormValues") != null) {
		out.print("<script language=\"javascript\">\n");
		out.print(VoHelperTools.writeBackMapToForm((java.util.Map)request.getAttribute("writeBackFormValues")));
		out.print("writeBackMapToForm();\n");
		out.print("</script>");
	}
%>

