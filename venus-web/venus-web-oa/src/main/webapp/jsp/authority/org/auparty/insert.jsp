<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import = "java.util.List,java.util.Iterator,java.util.ArrayList,java.util.HashMap"%>
<%@ page import="venus.oa.organization.auparty.vo.PartyVo"%>
<%@ page import="venus.oa.util.GlobalConstants"%>
<%@ page import="venus.oa.util.VoHelperTools"%>
	<%  //判断是否为修改页面
		boolean isModify = false;
		String strModify = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Added");
		if(request.getParameter("isModify") != null) {
			isModify = true;
			strModify = venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Modify");
		}
		String typeId = (String) request.getAttribute("typeId");		
		String my_title = strModify + venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Group");	
		boolean isPerson=GlobalConstants.isPerson(typeId);	
	%>
<title><%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Edit_Template"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.New_Template")%></title>
<script>

	function insert_onClick(){  //增加记录
		if(checkAllForms()){
    		form.action = "<venus:base/>/auParty/insert";
    		form.submit();
    	}
  	}
  	function update_onClick(){
  		if(checkAllForms()){
  			form.action = "<venus:base/>/auParty/update";
  			form.submit();
  		}
  	}  	
	function goBack_onClick(typeId) {//添加团体
	    window.location="<venus:base/>/auParty/queryAll?typeId="+typeId;
	}	
	<%if(!isPerson){%>
	function checkEmail(value, thisInput) {
		var reg  = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
		if(value==""){
			return true;
		}
		if (!reg.test(value)) {
			writeValidateInfo ("<fmt:message key='venus.authority.Please_enter_the_correct' bundle='${applicationAuResources}' />Email<fmt:message key='venus.authority.Format_' bundle='${applicationAuResources}' />", thisInput);
			return false;
		}
	
		return true;
	}
	<%}%>
</script>

</head>
<body>
<form name="form" method="post">
<input type="hidden" name="typeId" value="<%=typeId%>">
<table class="table_noframe">
	<tr>
		<td valign="middle">
			<input name="button_save" type="button" class="button_ellipse" value="<fmt:message key='venus.authority.Save' bundle='${applicationAuResources}' />" onClick="javascript:<%=isModify?"update_onClick()":"insert_onClick()"%>;">
			<input name="button_cancel" type="button" class="button_ellipse" value="<fmt:message key='venus.authority.Cancel' bundle='${applicationAuResources}' />"  onClick="goBack_onClick('<%=typeId%>');">
		</td>
	</tr>
</table>

<div id="auDivParent1"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild1',this,'<venus:base/>/themes/<venus:theme/>/')"><%=my_title%>
		</td>
	</tr>
</table>
</div>

<div id="auDivChild1"> 
<table class="table_div_content">
	<tr> 
		<td align="right" width="10%" nowrap><span class="style_required_red">*</span><fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' /></td>
		<td align="left">
			<input name="id" type="hidden" value="">
			<input name="name" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' />" maxlength=300 validate="notNull;isSearch" value="">
		</td>		
	</tr>
	<%if(isPerson){%>	
	<tr>
	    <td align="right" width="10%" nowrap><span class="style_required_red">*</span>Email</td>
	    <td align="left">
			<input name="email" type="text" class="text_field" inputName="Email" maxlength=50 validate="checkEmail" value="">
		</td>		
	</tr>	
	<%}%>
	<tr>
	    <td align="right" width="10%" nowrap><fmt:message key='venus.authority.Notes' bundle='${applicationAuResources}' /></td>
	    <td align="left">
			<textarea class="textarea_limit_words" cols="60" rows="5" name="remark" maxLength="500"  id="remarkId"></textarea>
		</td>		
	</tr>	
</table>
</div>
</form>
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

