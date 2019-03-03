<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="venus.authority.util.VoHelperTools" %>
<%@ page import="venus.authority.util.StringHelperTools" %>
<%@ page import = "venus.commons.xmlenum.EnumRepository" %>
<%@ page import = "venus.commons.xmlenum.EnumValueMap" %>
<%@ page import="java.util.List" %>
<%
	EnumRepository er = EnumRepository.getInstance();
	er.loadFromDir();
	EnumValueMap typeMap = er.getEnumValueMap("AccreditType");
%>
<html>
<head>
<title><fmt:message key='venus.authority.Authorized_to_log_list' bundle='${applicationAuResources}' /></title>
<script>
	function simpleQuery_onClick(){
		form.action="<%=request.getContextPath()%>/auAuthorizeLog/queryAll";
		form.submit();
	}
	function clearParameter(){
		document.form.operator_name.value="";
		document.form.visitor_name.value="";
		document.form.resource_name.value="";
		document.form.start_time.value="";
		document.form.end_time.value="";
		document.form.accredit_type.value="";
	}	
</script>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Authorized_to_log_list' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');
</script>
<form name="form" method="post">
<div id="auDivParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild0',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')" >&nbsp;<fmt:message key='venus.authority.Conditional_Query' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>
<div id="auDivChild0"> 
<table class="table_div_content">
<tr><td>
	<table class="table_noFrame" width="100%">
		<tr>
			<td width="15%" align="right"><fmt:message key='venus.authority.Authorized' bundle='${applicationAuResources}' /></td>
			<td width="35%" align="left">
				<input type="text" class="text_field" validate="isSearch" name="operator_name" inputName="<fmt:message key='venus.authority.Authorized' bundle='${applicationAuResources}' />" maxLength="300" value=""/>
			</td>
			<td width="15%" align="right"><fmt:message key='venus.authority.Authorized_to_object' bundle='${applicationAuResources}' /></td>
			<td width="35%" align="left">
				<input type="text" class="text_field" validate="isSearch" name="visitor_name" inputName="<fmt:message key='venus.authority.Authorized_to_object' bundle='${applicationAuResources}' />" maxLength="300" value=""/>
			</td>
		</tr>
		<tr>
			<td width="15%" align="right"><fmt:message key='venus.authority.Resource_Name' bundle='${applicationAuResources}' /></td>
			<td width="35%" align="left">
				<input type="text" class="text_field" validate="isSearch" name="resource_name" inputName="<fmt:message key='venus.authority.Resource_Name' bundle='${applicationAuResources}' />" maxLength="300" value=""/>
			</td>		
			<td  align="right"><fmt:message key='venus.authority.License_type' bundle='${applicationAuResources}' /></td>
			<td  align="left">
				<select name="accredit_type" style="width:60px">
					<%
						List accreditTypeList = typeMap.getEnumList();
						for(int i=0;i<accreditTypeList.size();i++){%>
					<option value="<%=typeMap.getValue(accreditTypeList.get(i).toString())%>"><%=accreditTypeList.get(i)%></option>
	     			 <%}%>
				</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;			
				<input type="button" name="Submit" class="button_ellipse" value="<fmt:message key='venus.authority.Inquiry' bundle='${applicationAuResources}' />" onClickTo="javascript:simpleQuery_onClick();">&nbsp;&nbsp;
				<input type="button" name="del" class="button_ellipse"  value="<fmt:message key='venus.authority.Empty' bundle='${applicationAuResources}' />"  onClick="javascript:clearParameter();">
			</td>
		</tr>		
		<tr>
			<td width="15%" align='right'><fmt:message key='venus.authority.Authorized_time' bundle='${applicationAuResources}' /></td>
			<td width="30%" align="left">
      			<input id='start_time' type="text" class="text_field_half_reference_readonly" name='start_time' /><img src='<venus:base/>/themes/<venus:theme/>/images/icon/reference.gif' onClick="javascript:getYearMonthDay('start_time','<venus:base/>/');"  title="Calendar" class="img_1" style="cursor:hand;"/>&nbsp;<fmt:message key='venus.authority.To_' bundle='${applicationAuResources}' />&nbsp;<input id='end_time' type="text" class="text_field_half_reference_readonly"  name='end_time' /><img src='<venus:base/>/themes/<venus:theme/>/images/icon/reference.gif' onClick="javascript:getYearMonthDay('end_time','<venus:base/>/');"  title="Calendar" class="img_1" style="cursor:hand;"/>
      		</td>
		</tr>		
	</table>

</td></tr>
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
<table class="table_div_content">
	<tr>
		<td>
		<layout:collection name="beans" id="logList" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >
			<layout:collectionItem width="40" title="" style="text-align:center;">
				<bean:define id="wy3" name="logList" property="id"/>
				<input type="radio" name="checkbox_template" value="<%=null==wy3?"":wy3%>"/>
			</layout:collectionItem>
			<layout:collectionItem width="30"  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sequence") %>' style="text-align:center;">
				<venus:sequence/>
				<bean:define id="id" name="logList" property="id"/>
			</layout:collectionItem>
			<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Resource_Name") %>' property="resource_name" />
			<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Authorized") %>' property="operate_name" />
			<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Authorized_to_object") %>' property="visitor_name" />
			<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Authorized_time") %>'>
				<bean:define id="create_date" name="logList" property="create_date"/>
		    	<%=StringHelperTools.prt(create_date,19)%>			
		    </layout:collectionItem>
			<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.License_type") %>'>
				<bean:define id="wy5" name="logList" property="accredit_type"/>
			    <%=typeMap.getLabel(wy5.toString())%>			
			</layout:collectionItem>
			<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Authorized_version") %>' property="authorize_tag" />
		</layout:collection>
		<jsp:include page="/jsp/include/page.jsp" />
		</td>
	</tr>
</table>
</div>
<!-- 参照显示层 -->
<div id="iframeDialog" style="display:none"></div>

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

