<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.authority.util.StringHelperTools" %>
<%@ page import = "venus.authority.util.VoHelperTools" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ include file="/jsp/authority/org/aupartyrelation/organizeTooltip.jsp" %>
<title><fmt:message key='venus.authority.Reference_page' bundle='${applicationAuResources}' /></title>
<script>
	function simpleQuery_onClick(){  //简单的模糊查询
		if(checkAllForms()){
    		form.cmd.value = "queryUserList";
    		form.submit();
    	}
  	}
  	function findSelections(checkboxName, idName) {  //从列表中找出选中的id值列表
		var elementCheckbox = document.getElementsByName(checkboxName);  //通过name取出所有的checkbox
		var number = 0;  //定义游标
		var ids = null;  //定义id值的数组
		for(var i=0;i<elementCheckbox.length;i++){  //循环checkbox组
			if(elementCheckbox[i].checked) {  //如果被选中
				ids=elementCheckbox[i].value;  //加入选中的checkbox
                break;
			}
		}
		return ids;
	}
	function save_onClick() {//传回数组
		var ids = findSelections("checkbox_template","id");  //取得多选框的选择项
		if(ids == null)	{  //如果ids为空
			alert("<fmt:message key='venus.authority.Please_Select_Records' bundle='${applicationAuResources}' />!")
			return;
		}
		//parent.window.returnValue = ids;
		//parent.window.close();
		window.parent.parent.toAddUser_returnValue(ids);
		window.parent.parent.jQuery("#iframeDialog").dialog("close");
		
	}
	function clear_onClick() { //清空查询条件
		form.name.value = "";
		form.department.value = "";
		form.hid_department.value = "";
	}	
	function search_onKeyDown() {//回车后执行页面查询功能
		if (event.keyCode == 13) { 
			simpleQuery_onClick();
		}
	}	
	
	function cancle_Onclick(){
		window.parent.parent.jQuery("#iframeDialog").dialog("close");
	}
</script>
</head>
<body>
<form name="form" method="post" action="<venus:base/>/auProxy/queryUserList">
<input type="hidden" name="cmd" value="">
<input type="hidden" name="proxyId" value="<%=(String) request.getAttribute("proxyId")%>">
<table class="table_noFrame">
	<tr>
		<td>
			<input name="button_save" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Save' bundle='${applicationAuResources}' />" onClick="javascript:save_onClick();">
			<input name="button_cancel" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Cancel' bundle='${applicationAuResources}' />"  onClick="javascript:cancle_Onclick();" >
		</td>
	</tr>
</table>
<!-- 查询开始 -->
<div id="auDivParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild0',this,'<venus:base/>/themes/<venus:theme/>/')">
			<fmt:message key='venus.authority.Conditional_Query' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>
<div id="auDivChild0"> 
<table class="table_div_content">
<tr>
	<td width="5%" align="right"  nowrap><fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' /></td>
	<td width="25%" align="left"><input name="name" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' />" validate="isSearch" onKeyDown="javascript:search_onKeyDown();"/></td>
	<td width="15%" align="right" nowrap><fmt:message key='venus.authority.Respective_organizations' bundle='${applicationAuResources}' /></td>
	<td width="25%" align="left" nowrap>
        <input type="text" class="text_field_reference_readonly" validate="isSearch" name="department" inputName="<fmt:message key='venus.authority.Respective_organizations' bundle='${applicationAuResources}' />" maxLength="50" hiddenInputId="hid_department" value=""/>
        <input type="hidden" name="hid_department"/>
        <img class="refButtonClass" alt="<fmt:message key='venus.authority.Select_department' bundle='${applicationAuResources}' />" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onclick="javascript:getTree(new Array('hid_department', 'department'),'<venus:base/>','radio','','1','','2','1');">
    </td>
	<td  width="25%" align="left" nowrap>
		<input type="button" name="Submit" class="button_ellipse" value="<fmt:message key='venus.authority.Inquiry' bundle='${applicationAuResources}' />" onClick="javascript:simpleQuery_onClick();">
		<input name="button_clear" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Empty' bundle='${applicationAuResources}' />" onClick="javascript:clear_onClick()">		
	</td>
</tr>
</table>
</div>
<!-- 查询结束 -->
<div id="auDivParent1"> 
<table class="table_div_control">
	<tr > 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='venus.authority.Details_Form' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>

<div id="auDivChild1"> 
<table align=center class="table_div_content">
	<tr>
		<td>
			<layout:collection name="wy" id="wy1" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" onRowClick="javascript:showMessageBox(getRowHiddenId());" onRowMouseOver="javascript:closeMessageBox();">
				<layout:collectionItem width="8%" title="" style="text-align:center;">
					<logic:equal name="wy1" property="enable_status" value="1">
						<bean:define id="wy3" name="wy1" property="id"/>
							<input type="radio" name="checkbox_template" value="<%=wy3%>"/>
					</logic:equal>
					<logic:equal name="wy1" property="enable_status" value="0">
						<bean:define id="wy3" name="wy1" property="id"/>
							<input type="hidden" name="checkbox_template2" value="<%=wy3%>"/>
							<fmt:message key='venus.authority.Has_been_associated' bundle='${applicationAuResources}' />
					</logic:equal>
				</layout:collectionItem>
				<layout:collectionItem width="5%" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sequence") %>' style="text-align:center;">
					<venus:sequence/>
					<bean:define id="party_id" name="wy1" property="id"/>
					<input type="hidden" signName="hiddenId" value="<%=party_id%>" party_id="<%=party_id%>"/>					
				</layout:collectionItem>	
				<layout:collectionItem width="12%" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Name") %>' property="name" />	
				<%if (organizeTooltip == null) { %>
				<layout:collectionItem width="50%" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Organization") %>' sortable="false">
					<logic:notEmpty name="wy1" property="owner_org">
						<bean:define id="owner_org" name="wy1"  property="owner_org"/>
						 <%=StringHelperTools.replaceStringToHtml(owner_org)%>
					 </logic:notEmpty>
				</layout:collectionItem>			
				<%} %>	
				<layout:collectionItem width="25%" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Notes") %>' property="remark"/>	
			</layout:collection>
			<jsp:include page="/jsp/include/page.jsp" />
		</td>
	</tr>
</table>
</div>

<!-- 参照显示层 -->
<div id="iframeDialog" style="display:none"></div>
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

