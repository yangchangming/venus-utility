<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import = "java.util.List,java.util.Iterator,java.util.ArrayList"%>
<%@ page import="venus.frames.web.page.PageVo" %>
<%@ page import="venus.oa.service.history.vo.HistoryLogVo"%>
<%@ page import="venus.oa.util.VoHelperTools" %>
<%@ page import="venus.oa.util.StringHelperTools" %>
<%@ page import = "venus.commons.xmlenum.EnumRepository" %>
<%@ page import = "venus.commons.xmlenum.EnumValueMap" %>
<%@ page import="java.util.Set"%>
<%
	EnumRepository er = EnumRepository.getInstance();
	er.loadFromDir();
	EnumValueMap typeMap = er.getEnumValueMap("AuthorityType");
	Set keySet = (Set)request.getAttribute("keySet");
%>
<title><fmt:message key='venus.authority.Query_Template' bundle='${applicationAuResources}' /></title>
<script>
	var inputType = "";
	function simpleQuery_onClick(){
		form.action="<%=request.getContextPath()%>/historyLog/simpleQueryForLogFrame";
		form.submit();
	}
	function clearParameter(){
		document.form.operator_name.value="";
		document.form.source_name.value="";
		document.form.log_start_time.value="";
		document.form.log_end_time.value="";
		document.form.optType.value="";
	}
	function checkSelectItem() {
		var elementCheckbox = document.getElementsByName("checkbox_template");
		var number = 0;
		var ids = null;
		for(var i=0;i<elementCheckbox.length;i++){
			if(elementCheckbox[i].checked) {
				number += 1;
				ids = elementCheckbox[i].value;
			}
		}
		if(number == 0) {
	  		alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}			
		return true;
	}
	function RemoveArray(array,attachId)
	{
		var i=0,n=0
	    for(;i<array.length;i++)
	    {
	        if(array[i]!=attachId)
	        {
	            array[n++]=array[i]
	        }
	    }	
	    if(i!=n)   
		   array.length -= 1;
	}
	Array.prototype.remove = function (obj) {
    	return RemoveArray(this,obj);
	};
	function historyLogAuthorize() {
		var checkedArray = new Array(0);		
<%
	for(Iterator it=keySet.iterator(); it.hasNext(); ) {
	    	%>
	    	checkedArray.push("<%=(String)it.next()%>");
	    	<%
	    } 	
%>		

		//获取打勾的选项
		var elementCheckbox = document.getElementsByName("checkbox_template");  //通过name取出所有的checkbox
		var number = 0;  //定义游标
		var codes = "";
		var names = "";
		var typenames = "";
		for(var i=0;i<elementCheckbox.length;i++){  //循环checkbox组
			if(elementCheckbox[i].checked) {  //如果被选中
				checkedArray.remove(elementCheckbox[i].value);
				codes += elementCheckbox[i].value + ",";
				var thisHidden = getLayoutHiddenObjectById(elementCheckbox[i].value);
				var thisJquery = jQuery(thisHidden);
				var orgname = thisJquery.attr("orgname");
				var typename = thisJquery.attr("typename");
				names += orgname + ",";
				typenames += typename + ",";
			}
		}
		var del_codes = "";	
		var add_codes = "";
		if(codes != "") {
			codes = codes.substring(0,codes.length-1);
			document.form.add_codes.value=codes;
		}
		for(var i=0;i<checkedArray.length;i++){
			del_codes += checkedArray[i] + ",";
		}
		if(del_codes != "") {
			del_codes = del_codes.substring(0,del_codes.length-1);
			document.form.del_codes.value=del_codes;
		}
		if(names != "") {
			names = names.substring(0,names.length-1);
			document.form.names.value=names;
		}
		if(typenames != "") {
			typenames = typenames.substring(0,typenames.length-1);
			document.form.types.value=typenames;;
		}
		form.action="<venus:base/>/auAuthorize/saveOrgAuByRelId";
		form.submit();	
	}

</script>

</head>
<body>
<form name="form" method="post">
<input type="hidden" name="relId" value="">
<input type="hidden" name="pType" value="">
<input type="hidden" name="add_codes" value="">
<input type="hidden" name="del_codes" value="">
<input type="hidden" name="names" value="">
<input type="hidden" name="types" value="">
<input type="hidden" name="visiCode" value="">
<div id="auDivParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild0',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')" >&nbsp;<fmt:message key='venus.authority.Conditional_query_history_log' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>
<div id="auDivChild0"> 
<table class="table_div_content">
<tr><td>
	<table class="table_noFrame" width="100%">
		<tr>
			<td width="15%" align="right"><fmt:message key='venus.authority.Operation_Name' bundle='${applicationAuResources}' /></td>
			<td width="35%" align="left">
				<input type="text" class="text_field" validate="isSearch" name="operator_name" inputName="<fmt:message key='venus.authority.Operation_Name' bundle='${applicationAuResources}' />" maxLength="300" value=""/>
			</td>
			<td width="15%" align="right"><fmt:message key='venus.authority.Operation_Resource_Name' bundle='${applicationAuResources}' /></td>
			<td width="35%" align="left">
				<input type="text" class="text_field" validate="isSearch" name="source_name" inputName="<fmt:message key='venus.authority.Operation_Resource_Name' bundle='${applicationAuResources}' />" maxLength="300" value=""/>
			</td>
		</tr>
		<tr>
			<td width="15%" align='right'><fmt:message key='venus.authority.Operating_time' bundle='${applicationAuResources}' /></td>
			<td width="30%" align="left">
      			<input id='log_start_time' type="text" class="text_field_half_reference_readonly" name='log_start_time' /><img src='<venus:base/>/themes/<venus:theme/>/images/icon/reference.gif' onClick="javascript:getYearMonthDay('log_start_time','<venus:base/>/');"  title="Calendar" class="img_1" style="cursor:hand;"/>&nbsp;<fmt:message key='venus.authority.To_' bundle='${applicationAuResources}' />&nbsp;<input id='log_end_time' type="text" class="text_field_half_reference_readonly"  name='log_end_time' /><img src='<venus:base/>/themes/<venus:theme/>/images/icon/reference.gif' onClick="javascript:getYearMonthDay('log_end_time','<venus:base/>/');"  title="Calendar" class="img_1" style="cursor:hand;"/>
      		</td>
			<td  align="right"><fmt:message key='venus.authority.Action_Type' bundle='${applicationAuResources}' /></td>
			<td  align="left">
				<select name="optType" style="width:60px">
					<%
						List operateTypeList = typeMap.getEnumList();
						for(int i=0;i<operateTypeList.size();i++){%>
					<option value="<%=typeMap.getValue(operateTypeList.get(i).toString())%>"><%=operateTypeList.get(i)%></option>
	     			 <%}%>				
				</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" name="Submit" class="button_ellipse" value="<fmt:message key='venus.authority.Inquiry' bundle='${applicationAuResources}' />" onClickTo="javascript:simpleQuery_onClick();">
				<input name="del" type="button" id="del" value="<fmt:message key='venus.authority.Empty' bundle='${applicationAuResources}' />" class="button_ellipse" onClick="javascript:clearParameter();">
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
		<layout:collection name="beans" id="wy1" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >
			<layout:collectionItem width="40" title="" style="text-align:center;">
				<bean:define id="wy3" name="wy1" property="source_code"/>
					<input type="checkbox" name="checkbox_template" value="<%=wy3%>" <%if(keySet.contains(wy3)) {out.write("checked");}%>/>
			</layout:collectionItem>
			<layout:collectionItem width="20"  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sequence") %>' style="text-align:center;">
				<venus:sequence/>
				<bean:define id="wy3" name="wy1" property="source_code"/>
				<bean:define id="name" name="wy1" property="source_name"/>
				<bean:define id="typename" name="wy1" property="id"/>
				<input type="hidden" signName="hiddenId" value="<%=wy3%>" orgname="<%=name%>" typename="<%=typename%>"/>
			</layout:collectionItem>
			<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Resource_Name") %>' property="source_name" />
			<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Original_organization") %>' property="source_orgtree" />
			<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Operation_Name") %>' property="operate_name" />
			<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Operating_time") %>' property="operate_date" />
			<layout:collectionItem width="60" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Action_Type") %>' style="text-align:center;">
				<bean:define id="wy5" name="wy1" property="operate_type"/>
			    <%=typeMap.getLabel(wy5.toString())%>
			 </layout:collectionItem>
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

