<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="venus.oa.util.VoHelperTools" %>
<%@ page import="venus.oa.util.StringHelperTools" %>
<%@ page import = "venus.commons.xmlenum.EnumRepository" %>
<%@ page import = "venus.commons.xmlenum.EnumValueMap" %>
<%@ page import="venus.oa.util.GlobalConstants"%>
<%@ page import="java.util.List" %>
<%@ page import="venus.oa.history.vo.HistoryLogVo" %>
<%
	EnumRepository er = EnumRepository.getInstance();
	er.loadFromDir();
//	er.ReLoadFromDir();
	EnumValueMap typeMap = er.getEnumValueMap("OperateType");
%>
<title><fmt:message key='venus.authority.Query_Template' bundle='${applicationAuResources}' /></title>
<script>
	function simpleQuery_onClick(){
		form.action="<%=request.getContextPath()%>/historyLog/simpleQuery";
		form.submit();
	}
	function view_onClick() {  //从多选框到修改页面
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
		if(number > 1) {
	  		alert("<fmt:message key='venus.authority.Can_only_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		var actionurl = "<venus:base/>/historyLog/findByPartyId?source_id=" + ids;

		showIframeDialog("iframeDialog","<fmt:message key='venus.authority.Agencies_operating_history' bundle='${applicationAuResources}' />", actionurl, 400, 300);
	
	}
	function viewListDetail() {
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
		var thisHidden = getLayoutHiddenObjectById(ids);
		//var treePath = "<venus:base/>/jsp/authority/history/listDetailFrame.jsp?id=" + ids;
		var treePath="<venus:base/>/historyLog/findDetail?id="+ids;
		
		showIframeDialog("iframeDialog","<fmt:message key='venus.authority.Agencies_operating_history' bundle='${applicationAuResources}' />", treePath, 500, 500);
		/*
			机构的操作类型可能使用thisHidden.operate_type判断,如:
			thisHidden.operate_type == <%=GlobalConstants.HISTORY_LOG_DELETE%>
		*/
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
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.History_Log_Management' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');
</script>
<form name="form" method="post">
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
			<td width="15%" align="right"><fmt:message key='venus.authority.Resource_Name' bundle='${applicationAuResources}' /></td>
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
					<option value="<%=typeMap.getValue(operateTypeList.get(i).toString())%>" ><%=operateTypeList.get(i)%></option>
	     			 <%}%>
				</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="button" name="Submit" class="button_ellipse" value="<fmt:message key='venus.authority.Inquiry' bundle='${applicationAuResources}' />" onClickTo="javascript:simpleQuery_onClick();">&nbsp;&nbsp;
				<input name="del" type="reset" id="del" value="<fmt:message key='venus.authority.Empty' bundle='${applicationAuResources}' />" class="button_ellipse">
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
		<td> 
			<table align="right">
				<tr> 
					<!--<td class="button_ellipse" onClick="javascript:view_onClick();"><img src="<venus:base/>/images/icon/modify.gif" class="div_control_image">跟踪</td>-->
					<!--<td class="button_ellipse" onClick="javascript:undo_onClick();"><img src="<venus:base/>/images/icon/modify.gif" class="div_control_image">恢复</td>-->
					<td id="viewImgTd" class="button_ellipse" onClick="javascript:viewListDetail();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/view.gif" class="div_control_image"><fmt:message key='venus.authority.View' bundle='${applicationAuResources}' /></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>

<div id="auDivChild1"> 
<table class="table_div_content">

	<tr>
		<td>
			<div style="width=100%;overflow-x:visible;overflow-y:visible;">
				<table cellspacing="0" cellpadding="0" border="0" align="center" width="100%" class="listCss">
					<tr>
						<td valign="top">
							<table cellspacing="1" cellpadding="1" border="0" width="100%">
								<tr valign="top">
									<th class="listCss" ></th>
									<th class="listCss" width="30"><fmt:message key='venus.authority.Sequence' bundle='${applicationAuResources}' /></th>
									<th class="listCss"><fmt:message key='venus.authority.Resource_Name' bundle='${applicationAuResources}' /></th>
									<th class="listCss"><fmt:message key='venus.authority.Original_organization' bundle='${applicationAuResources}' /></th>
									<th class="listCss"><fmt:message key='venus.authority.Operation_Name' bundle='${applicationAuResources}' /></th>
									<th class="listCss"><fmt:message key='venus.authority.Operating_time' bundle='${applicationAuResources}' /></th>
									<th class="listCss"><fmt:message key='venus.authority.Action_Type' bundle='${applicationAuResources}' /></th>
								</tr>
								<%
									List beans = (List) request.getAttribute("beans");
									for(int i=0;  i<beans.size();) {
										HistoryLogVo vo= (HistoryLogVo) beans.get(i);
										i++;
								%>
								<tr>
									<td class="listCss" align="center">
										<input type="radio" name="checkbox_template" value="<%=vo.getId()%>"/>
									</td>
									<td class="listCss" align="center"><%=i%><input type="hidden" signName="hiddenId" value="<%=vo.getId()%>" /></td>
									<td class="listCss"><%=StringHelperTools.prt(vo.getSource_name())%></td>
									<td class="listCss"><%=StringHelperTools.prt(vo.getSource_orgtree())%></td>
									<td class="listCss"><%=StringHelperTools.prt(vo.getOperate_name())%></td>
									<td class="listCss"><%=StringHelperTools.prt(vo.getOperate_date(), 19)%></td>
									<td class="listCss"><%=StringHelperTools.prt(typeMap.getLabel(vo.getOperate_type()))%></td>
								</tr>
								<%
									}
								%>
							</table>
						</td>
					</tr>
				</table>
				<jsp:include page="/jsp/include/page.jsp" />
			</div>
		</td>
	</tr>




	<%--<tr>--%>
		<%--<td>--%>
		<%--<layout:collection name="beans" id="wy1" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >--%>
			<%--<layout:collectionItem width="40" title="" style="text-align:center;">--%>
				<%--<bean:define id="wy3" name="wy1" property="id"/>--%>
				<%--<input type="radio" name="checkbox_template" value="<%=null==wy3?"":wy3%>"/>--%>
			<%--</layout:collectionItem>--%>
			<%--<layout:collectionItem width="30"  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sequence") %>' style="text-align:center;">--%>
				<%--<venus:sequence/>--%>
				<%--<bean:define id="id" name="wy1" property="id"/>--%>
				<%--<bean:define id="operateType" name="wy1" property="operate_type"/>--%>
				<%--<input type="hidden" signName="hiddenId" value="<%=id%>" operate_type="<%=operateType%>"/>--%>
			<%--</layout:collectionItem>--%>
			<%--<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Resource_Name") %>' property="source_name" />--%>
			<%--<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Original_organization") %>' property="source_orgtree" />--%>
			<%--<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Operation_Name") %>' property="operate_name" />--%>
			<%--<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Operating_time") %>'>--%>
				<%--<bean:define id="operate_date" name="wy1" property="operate_date"/>--%>
		    	<%--<%=StringHelperTools.prt(operate_date,19)%>			--%>
			<%--</layout:collectionItem>--%>
			<%--<layout:collectionItem width="60" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Action_Type") %>' style="text-align:center;">--%>
				<%--<bean:define id="wy5" name="wy1" property="operate_type"/>--%>
			    <%--<%=typeMap.getLabel(wy5.toString())%>--%>
			 <%--</layout:collectionItem>--%>
		<%--</layout:collection>--%>
		<%--<jsp:include page="/jsp/include/page.jsp" />--%>
		<%--</td>--%>
	<%--</tr>--%>
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
<script language="javascript">
jQuery("select[name='optType']").val("<%=request.getAttribute("selectOptType")%>");
</script>


