<%@ include file="/jsp/include/global.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.authority.util.VoHelperTools" %>
<%@ page import="venus.authority.au.auuser.vo.AuUserVo" %>
<%@ page import="venus.authority.au.auuser.util.IAuUserConstants" %>
<%@ page import="venus.authority.util.GlobalConstants" %>
<%  //判断是否为修改页面
	boolean isModify = false;  //定义变量,标识本页面是否修改(或者新增)
	if(request.getParameter("isModify") != null) {  //如果从request获得参数"isModify"不为空
		isModify = true;  //赋值isModify为true
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Modify_page"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.New_page")%></title>
<script language="javascript">
	var originalLoginId;
	var validLoginId = true;
	function insert_onClick(){  //插入单条数据
		if(!validLoginId) {
			alert("<fmt:message key='venus.authority.This_account_has_been_occupied' bundle='${applicationAuResources}' />");
			setVenusInputError(form.login_id);
			return false;		
		}
		if(form.password2.value != form.password.value) {
			alert("<fmt:message key='venus.authority.Confirm_Password_inconsistent' bundle='${applicationAuResources}' />");
			return false;
		}
    	form.action="<%=request.getContextPath()%>/auUser/insert";
	    form.submit();
	}
  	function update_onClick(id){  //保存修改后的单条数据
		if(!validLoginId) {
			alert("<fmt:message key='venus.authority.This_account_has_been_occupied' bundle='${applicationAuResources}' />");
			setVenusInputError(form.login_id);
			return false;		
		}  	
		if(form.password2.value != form.password.value) {
			alert("<fmt:message key='venus.authority.Confirm_Password_inconsistent' bundle='${applicationAuResources}' />");
			return false;
		}
    	if(!getConfirm()) {  //如果用户在确认对话框中点"取消"
  			return false;
		}
	    form.action="<%=request.getContextPath()%>/auUser/update";
    	form.submit();
	}
	function getEmployeeRef(rootCode,type) {  //获取人员参照列表
		refPath = "<venus:base/>/jsp/authority/au/auuser/chooseDiv.jsp?typeId="+type;
		var rtObj = window.showModalDialog(refPath, new Object(),'dialogHeight=500px;dialogWidth=600px;resizable:yes;status:no;scroll:auto;');
		if(rtObj != undefined && rtObj.length > 0){
			form.party_id.value = rtObj[0]['returnValue'];
			form.name.value = rtObj[0]['childName'];
		}
	}


	function getOrgTree() {
		var treePath = "<venus:base/>/jsp/authority/tree/treeRef.jsp?inputType=radio&submit_all=0&rootXmlSource=<venus:base/>/jsp/authority/au/auuser/xmlData.jsp?parent_code%3D%26return_type%3Dparty_id";
		global_outputArray=new Array('party_id','name');//设置返回输出控件集合
       showIframeDialog("iframeDialog",venus.authority.Reference_page, treePath, 400, 600);
	}
	
	function validateLoginId() {
		var loginId = form.login_id.value;
		if (loginId != null) { 
			if (loginId == originalLoginId) { //在修改页面，如果改变后的值与原始loginId相同，验证通过
				validLoginId = true;
				setVenusInputDefault(form.login_id);
				return true;
			}
			jQuery.ajax({
				type: "GET",
				url: "<%=request.getContextPath()%>/auUser/validateLoginId?loginId=" + loginId,
//				data: "cmd=validateLoginId&loginId=" + loginId,
				async: true,
				success: function(msg) {
					 	if (msg > 0) {
					 		validLoginId = false;
					 		alert("<fmt:message key='venus.authority.This_account_has_been_occupied' bundle='${applicationAuResources}' />");
					 		setVenusInputError(form.login_id);
					 	} else {
					 		validLoginId = true;
					 		setVenusInputDefault(form.login_id);
					 	}					
				},
				error: function(xmlhttp,msg) {
					alert("<fmt:message key='venus.authority.Network_link_fails_' bundle='${applicationAuResources}' />");
				}
			});			
		}
	}
	
	//调用getEmployeeRef 的代码：
	//onClick="javascript:getEmployeeRef('<%//=GlobalConstants.getRelaType_comp()%>','<%//=GlobalConstants.getPartyType_empl()%>');"/>
</script>
<script language="javascript" for=window event=onload>
	originalLoginId = form.login_id.value; //修改之前记录原始loginId
</script>
</head>
<body>
<script language="javascript">
	writeTableTop('<%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Modify_page"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.New_page")%>','<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<form name="form" method="post">

<table class="table_noFrame">
	<tr>
		<td>
			<input name="button_save" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Save' bundle='${applicationAuResources}' />" onClickTo="javascript:<%=isModify?"update_onClick()":"insert_onClick()"%>">
			<input name="button_cancel" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Cancel' bundle='${applicationAuResources}' />"  onClick="javascript:returnBack()" >
		</td>
	</tr>
</table>

<div id="auDivParent1"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild1',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')"><%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Modify"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Added")%><%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Login")%>
		</td>
	</tr>
</table>
</div>

<div id="auDivChild1"> 
<table class="table_div_content">
<tr><td> 
	<table class="table_div_content_inner">
		<tr>
			<td width="80" align="right" nowrap>&nbsp;</td>
			<td align="left">&nbsp;</td>
			<td align="left" rowspan="5"><br>
			<fmt:message key='venus.authority.Tips_' bundle='${applicationAuResources}' /><br><br>
			<fmt:message key='venus.authority.Sign_restricted_account_letters_numbers_underscores_can_not_use_Chinese_characters_in_length' bundle='${applicationAuResources}' />24<fmt:message key='venus.authority.Characters_or_less_' bundle='${applicationAuResources}' /><br><br>
			<fmt:message key='venus.authority.To_protect_the_security_of_your_information_passwords_can_not_be_too_simple_you_can_use_letters_and_Arabic_numerals' bundle='${applicationAuResources}' /><br>
			<fmt:message key='venus.authority.Combination_of_length' bundle='${applicationAuResources}' />24<fmt:message key='venus.authority.Characters_within_and_to_distinguish_the_case_of_the_English_alphabet_' bundle='${applicationAuResources}' /></td>
		</tr>
		<tr>
			<td align="right"><span class="style_required_red">* </span><fmt:message key='venus.authority.Real_Name' bundle='${applicationAuResources}' /></td>
			<td align="left">
				<input type="text" <%if(!isModify){%>class="text_field_reference_readonly"<%} else {%>class="text_field_readonly"<%}%>
				validate="notNull;" name="name" inputName="<fmt:message key='venus.authority.Real_Name' bundle='${applicationAuResources}' />" maxlength="50" 
				hiddenInputId="party_id"/><input type="hidden" name="party_id"/><%if(!isModify){%><img class="refButtonClass" 
				src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getOrgTree();"/>
				<%}%>
			</td>
		</tr>
		<tr>
			<td align="right"><span class="style_required_red">* </span><fmt:message key='venus.authority.Login_Account' bundle='${applicationAuResources}' /></td>
			<td align="left">
				<input type="text" class="text_field" name="login_id" inputName="<fmt:message key='venus.authority.Login_Account' bundle='${applicationAuResources}' />" value="" maxLength="24" validate="notChinese;notNull;" onchange="validateLoginId();"/>
			</td>
		</tr>
		<tr>
			<td align="right"><span class="style_required_red">* </span><fmt:message key='venus.authority.Password' bundle='${applicationAuResources}' /></td>
			<td align="left">
				<input type="password" class="text_field" name="password" inputName="<fmt:message key='venus.authority.Password' bundle='${applicationAuResources}' />" value="" maxLength="24" validate="notChinese;notNull;"/>
			</td>
		</tr>
		<tr>
			<td align="right"><span class="style_required_red">* </span><fmt:message key='venus.authority.Password_confirmation' bundle='${applicationAuResources}' /></td>
			<td align="left">
				<input type="password" class="text_field" name="password2" inputName="<fmt:message key='venus.authority.Password_confirmation' bundle='${applicationAuResources}' />" value="" maxLength="24" validate="notChinese;notNull;"/>
			</td>
		</tr>
		<%if(!isModify){%>
		<tr>
			<td align="right"><span class="style_required_red">* </span><fmt:message key='venus.authority.Enabled0' bundle='${applicationAuResources}' /></td>
			<td align="left">
				<input type="radio" name="enable_status" inputName="<fmt:message key='venus.authority.Enabled0' bundle='${applicationAuResources}' />" value="1" checked /><fmt:message key='venus.authority.Enabled' bundle='${applicationAuResources}' />
				<input type="radio" name="enable_status" inputName="<fmt:message key='venus.authority.Enabled0' bundle='${applicationAuResources}' />" value="0" /><fmt:message key='venus.authority.Disable' bundle='${applicationAuResources}' />
			</td>
		</tr>
		<%} else {%>
		<tr>
			<td align="right"></td>
			<td align="left"><input type="hidden" name="enable_status"/></td>
		</tr>
		<%}%>
		<!--tr>
			<td align="right">是否超级管理员</td>
			<td align="left">
				<input type="radio" name="is_admin" inputName="是否超级管理员" value="1"/>是
				<input type="radio" name="is_admin" inputName="是否超级管理员" value="0" checked />否
			</td>
		</tr-->
	</table>
</td></tr>
</table>


</div>
<input type="hidden" name="is_admin" value="0"/>
<input type="hidden" name="create_date" />
<input type="hidden" name="password1" />
<input type="hidden" name="modify_date" />     
<input type="hidden" name="agent_status" />       
<input type="hidden" name="id" value="" >
<%
	String system_id = (String)request.getAttribute("system_id");
	if(system_id==null || system_id.length()==0) {
		system_id = request.getParameter("system_id");
		if(system_id==null) {
			system_id = "";
		}
	}
	String func_code = (String)request.getAttribute("func_code");
	if(func_code==null || func_code.length()==0) {
		func_code = request.getParameter("func_code");
		if(func_code==null) {
			func_code = "";
		}
	}
%>
<input type="hidden" name="system_id" value="<%=system_id%>">
<input type="hidden" name="func_code" value="<%=func_code%>">
<!-- 参照显示层 -->
<div id="iframeDialog" style="display:none"></div>

</form>			
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>
<%  //取出要修改的那条记录，并且回写表单
	if(isModify) {  //如果本页面是修改页面
  		out.print("<script language=\"javascript\">\n");  //输出script的声明开始
  		AuUserVo resultVo = null;  //定义一个临时的vo变量
  		if(request.getAttribute(IAuUserConstants.REQUEST_BEAN_VALUE) != null) {  //如果request中取出的bean不为空
  			resultVo = (AuUserVo)request.getAttribute(IAuUserConstants.REQUEST_BEAN_VALUE);  //从request中取出vo, 赋值给resultVo
  		}
  		if(resultVo != null) {  //如果vo不为空
			out.print(VoHelperTools.writeBackMapToForm(VoHelperTools.getMapFromVo(resultVo)));  //输出表单回写方法的脚本
			out.print("writeBackMapToForm();\n");  //输出执行回写方法
			out.print("document.form.password1.value=document.form.password.value;\n");
			out.print("document.form.password2.value=document.form.password.value;\n");  //回写密码确认
		}
		out.print("</script>");  //输出script的声明结束
  	}
%>

