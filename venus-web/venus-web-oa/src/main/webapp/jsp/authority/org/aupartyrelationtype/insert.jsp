<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import = "java.util.List,java.util.Iterator,java.util.ArrayList,java.util.HashMap"%>
<%@ page import="venus.oa.org.aupartyrelationtype.vo.AuPartyRelationTypeVo"%>
<%@ page import="venus.oa.util.VoHelperTools"%>
<%@ page import = "venus.commons.xmlenum.EnumRepository" %>
<%@ page import = "venus.commons.xmlenum.EnumValueMap" %>
	<%  //判断是否为修改页面
		boolean isModify = false;
		if(request.getParameter("isModify") != null) {
			isModify = true;
		}
		EnumRepository er = EnumRepository.getInstance();
        er.loadFromDir();
        EnumValueMap typeMap = er.getEnumValueMap("RelationTypeStatus");
        List al = typeMap.getEnumList();
	%>
<title><%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Edit_Template"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.New_Template")%></title>
<script>

	function insert_onClick(){  //增加记录
		if(checkAllForms()){
		    if((document.getElementById("root_partytype_id").value==""&&confirm("根类型不需要设置？"))||document.getElementById("root_partytype_id").value!=""){
		        <%--form.action = "<venus:base/>/AuPartyRelationTypeAction.do?cmd=insert";--%>
				form.action = "<venus:base/>/auPartyRelationType/insert";
                form.submit();
            }
    	}
  	}
  	function update_onClick(){
  		<%--form.action = "<venus:base/>/AuPartyRelationTypeAction.do?cmd=update";--%>
		form.action = "<venus:base/>/auPartyRelationType/update";
  		form.submit();
  	}
  	function getBuildList() {
		<%--var obj = window.showModalDialog('<venus:base/>/AuPartyRelationTypeAction.do?cmd=queryBuild','','dialogHeight:350px;dialogWidth:450px;center:yes;help:no;resizable:yes;scroll:yes;status:no;');--%>
		var obj = window.showModalDialog('<venus:base/>/auPartyRelationType/queryBuild','','dialogHeight:350px;dialogWidth:450px;center:yes;help:no;resizable:yes;scroll:yes;status:no;');
		if(obj) {
			document.form.build_name.value = obj[1];
			document.form.build_Id.value = obj[0];
		}
	}
	
	function toSel_onClick(name,id) { 
//        var refPath = "refFrame.jsp?actionStr=AuPartyTypeAction&keyword="+document.form.keyword.value;
		var refPath = "refFrame.jsp?actionStr=auPartyType&keyword="+document.form.keyword.value;
        var rtObj = window.showModalDialog(refPath, new Object(),'dialogHeight=600px;dialogWidth=850px;resizable:yes;status:no;scroll:auto;');
        if(rtObj != undefined){ 
            var partyTypes = "";
            if(rtObj.length==1){                
                jQuery('#'+name).val(rtObj[0].name);
                jQuery('#'+id).val(rtObj[0].id);                
            }
        }
    }
	
</script>

</head>
<body>
<script language="javascript">
	writeTableTop('<%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Modify_page"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.New_page")%>','<venus:base/>/themes/<venus:theme/>/');
</script>

<form name="form" method="post">
<input type="hidden" name="id" value="">
<table class="table_noframe">
	<tr>
		<td valign="middle">
			<input name="button_save" type="button" class="button_ellipse" value="<fmt:message key='venus.authority.Save' bundle='${applicationAuResources}' />" onClick="javascript:<%=isModify?"update_onClick()":"insert_onClick()"%>;">
			<input name="button_cancel" type="button" class="button_ellipse" value="<fmt:message key='venus.authority.Cancel' bundle='${applicationAuResources}' />"  onClick="javascript:returnBack();">
		</td>
	</tr>
</table>

<div id="auDivParent1"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild1',this,'<venus:base/>/themes/<venus:theme/>/')"><%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Modify_the_relationship_between_body_type"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.New_types_of_group_relations")%>
		</td>
	</tr>
</table>
</div>

<div id="auDivChild1"> 
<table class="table_div_content">
	<tr> 
		<td align="right" width="10%" nowrap></td>
		<td align="left"><input name="Id" type="hidden" value=""></td>
	</tr>	
	<tr> 
		<td align="right" width="10%" nowrap><span class="style_required_red">* </span><fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' /></td>
		<td align="left">
			<input name="name" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' />" validate="notNull;isSearch" maxlength=50 value="">
		</td>
	</tr>	
	<%if(isModify) {%>
	<tr>
	    <td align="right" width="10%" nowrap></td>
	    <td align="left">
			<input name="keyword" type="hidden">
		</td>
	</tr>
	<%} else {%>
	<tr>
	    <td align="right" width="10%" nowrap><span class="style_required_red">* </span><fmt:message key='venus.authority.Classification' bundle='${applicationAuResources}' /></td>
	    <td align="left">
			<select name="keyword">
				<%for(int i=0;i<al.size();i++){%>
				<option value="<%=typeMap.getValue(al.get(i).toString())%>"><%=al.get(i)%></option>
     			<%}%>
			</select>
		</td>
	</tr>
	<%}%>
	<%if(isModify) {%>
	<tr>
        <td align="right" width="10%" nowrap></td>
        <td align="left">
            <input name="root_partytype_id" type="hidden">
        </td>
    </tr>
    <%} else {%>
	<tr>
        <td align="right" width="10%" nowrap><fmt:message key='venus.authority.RootPartyType' bundle='${applicationAuResources}' /></td>
        <td align="left">
            <input name="rootpartytypename" id="rootpartytypename" type="text" class="text_field_reference_readonly" maxlength=50 value=""><input name="root_partytype_id" id="root_partytype_id" type="hidden"><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:toSel_onClick('rootpartytypename','root_partytype_id')"/>
        </td>       
    </tr>
    <%} %>
	<tr>
	    <td align="right" width="10%" nowrap><fmt:message key='venus.authority.Notes' bundle='${applicationAuResources}' /></td>
	    <td align="left">
			<textarea name="remark" cols="60" rows="5" class="textarea_limit_words"></textarea>
		</td>		
	</tr>
</table>
</div>
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

