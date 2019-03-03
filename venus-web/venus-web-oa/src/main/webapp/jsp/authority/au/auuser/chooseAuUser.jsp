<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import = "venus.authority.util.VoHelperTools" %>
<%@ include file="/jsp/include/global.jsp" %>
<title><fmt:message key='venus.authority.Query_Template' bundle='${applicationAuResources}' /></title>
<script>
	function simpleQuery_onClick(){  //简单的模糊查询
    	form.cmd.value = "queryByOrgType";
    	form.submit();
  	}
function insert_onClick() {//传回数组
		var elementCheckbox = document.getElementsByName("checkbox_template");
		var number = 0;
		var relId = null;
		for(var i=0;i<elementCheckbox.length;i++){
			if(elementCheckbox[i].checked) {
				number += 1;
				relId = elementCheckbox[i].value;
			}
		}
		if(number == 0) {
	  		alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!")
	  		return ;
		}
		if(number > 1) {
	  		alert("<fmt:message key='venus.authority.Can_only_select_a_record' bundle='${applicationAuResources}' />!")
	  		return ;
		}
		
		var userName = "";
		try {
			var thisHidden = getLayoutHiddenObjectById(relId);
			userName = thisHidden.username;
		} catch(e) {
			
		}
	var retObj=new Array();
	retObj[0]=new Array();
	retObj[0]['returnValue']=relId;
	retObj[0]['childName']=userName;
	
	window.returnValue = retObj;
	window.close();
	}
	function getLayoutHiddenObjectById(id) {
		if(id == null) {
			return null;
		}
		var allInput = document.getElementsByTagName("input");
		for(var i=0; i<allInput.length; i++) {
			if(allInput[i].type == "hidden" && allInput[i].signName == "hiddenId" && allInput[i].value == id) {
				return allInput[i];
			}
		}
		return null;
	}
</script>

</head>
<body>
<form name="form" method="post" action="<venus:base/>/auParty/queryByOrgType">
<input type="hidden" name="cmd" value="">
<input type="hidden" name="typeId" value="<%=(String) request.getAttribute("typeId")%>">
<table class="table_noFrame">
	<tr>
		<td>
			<input name="button_save" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Save' bundle='${applicationAuResources}' />" onClick="javascript:insert_onClick();">
			<input name="button_cancel" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Cancel' bundle='${applicationAuResources}' />"  onClick="javascript:window.close();" >
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
	<td align="right" width="10%" nowrap><fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' /></td>
	<td><input name="name" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' />" validate="isSearch">
	<input type="button" name="Submit" class="button_ellipse" value="<fmt:message key='venus.authority.Inquiry' bundle='${applicationAuResources}' />" onClick="javascript:simpleQuery_onClick();">
	</td>
</tr>
</table>

</div>

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
		<layout:collection name="wy" id="wy1" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >
			
 			<layout:collectionItem width="30" title="" style="text-align:center;">
				<logic:equal name="wy1" property="enable_status" value="1">
					<bean:define id="wy3" name="wy1" property="id"/>
						<input type="radio"  name="checkbox_template" value="<%=wy3%>"/>
				</logic:equal>
				<logic:equal name="wy1" property="enable_status" value="0">
				&nbsp;<fmt:message key='venus.authority.Disable' bundle='${applicationAuResources}' />
				</logic:equal>
			</layout:collectionItem>
			<layout:collectionItem width="30" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sequence") %>' style="text-align:center;">
				<venus:sequence/>
				<bean:define id="pId" name="wy1" property="id"/>
				<bean:define id="wy4" name="wy1" property="name"/>
				<input type="hidden" signName="hiddenId" value="<%=pId%>"  username="<%=wy4%>"/>
			</layout:collectionItem>	
			<layout:collectionItem width="160" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Number") %>' property="id" />
			<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Name") %>' property="name" />	
			<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Notes") %>' property="remark"/>
		</layout:collection>
		<jsp:include page="/jsp/include/page.jsp" />
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

