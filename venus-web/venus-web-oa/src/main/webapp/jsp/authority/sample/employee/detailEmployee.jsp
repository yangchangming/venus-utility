<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.oa.util.VoHelperTools" %>
<%@ page import="venus.oa.util.StringHelperTools" %>
<%@ page import="venus.oa.organization.employee.vo.EmployeeVo" %>
<%@ page import="venus.oa.organization.employee.util.IEmployeeConstants" %>
<%  //取出本条记录
	EmployeeVo resultVo = null;  //定义一个临时的vo变量
	resultVo = (EmployeeVo)request.getAttribute(IEmployeeConstants.REQUEST_BEAN_VALUE);  //从request中取出vo, 赋值给resultVo
	VoHelperTools.replaceToHtml(resultVo);  //把vo中的每个值过滤

	boolean isReadOnly = false;  //定义变量,标识本页面是否为只读页面
	if(request.getParameter("isReadOnly") != null) {  //如果从request获得参数"isReadOnly"不为空
		isReadOnly = true;  //赋值isReadOnly为true
	}
%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Staff_Details' bundle='${applicationAuResources}' /></title>
<script language="javascript">
	function toAdd_onClick() {  //到增加记录页面
		form.action="<%=request.getContextPath()%>/relation/go2Add";
		form.cmd.value = "go2Add";
		form.submit();
	}
	function find_onClick(){  //直接点到修改页面
		form.action="<%=request.getContextPath()%>/employee/find";
		form.cmd.value = "find";
		form.submit();
	}
	function delete_onClick(){  //直接点删除单条记录
		if(!confirm("<fmt:message key='venus.authority.This_action_will_remove_the_employee_organization_relationship_information' bundle='${applicationAuResources}' />\n"
		+ "<fmt:message key='venus.authority.If_the_staff_does_not_schedule_any_current_positions_then_the_employee_s_basic_information_will_be_removed_together' bundle='${applicationAuResources}' />\n"
		+ "<fmt:message key='venus.authority.If_the_employee_currently_only_arranged_for_a_post_then_the_employees_will_be_placed_in_their_respective_departments_below' bundle='${applicationAuResources}' />\n"
		+ "<fmt:message key='venus.authority.Are_you_sure_you_do_this_' bundle='${applicationAuResources}' />")) {
			return false;
		}
		form.action="<%=request.getContextPath()%>/employee/delete";
		form.cmd.value = "delete";
		form.target="_parent";
		form.submit();
	}
	function goBack() {
		form.action="<%=request.getContextPath()%>/employee/queryAll";
		form.cmd.value = "queryAll";
		form.submit();
	}  
</script>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Detailed_Page' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<form name="form" method="get">
<table class="table_noFrame">
	<tr>
		<td>
			<%
			if(isReadOnly) {
			%>
				<input name="button_back" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Return' bundle='${applicationAuResources}' />"  onclick="javascript:goBack()" >
			<%
			}else {
			%>
				<input name="button_update" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Modify' bundle='${applicationAuResources}' />" onClick="javascript:find_onClick();">
				<input name="button_delete" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Delete' bundle='${applicationAuResources}' />" onClick="javascript:delete_onClick();">
				<input name="button_change" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Tone_level' bundle='${applicationAuResources}' />" onClick="javascript:getChangeTree(new Array(),'<venus:base/>','radio','','1','','3','1');">
			<%
			}
			%>
		</td>
	</tr>
</table>

<div id="ccParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild0',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')"><%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Staff0")%><fmt:message key='venus.authority.Details0' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>

<div id="ccChild0"> 
<table class="table_div_content">
	<tr>
		<td>
			<table class="table_div_content_inner">
				<tr>
					<td width="30%" align="right"><fmt:message key='venus.authority.Name_0' bundle='${applicationAuResources}' /></td>
					<td width="70%" align="left"><%=StringHelperTools.prt(resultVo.getPerson_name())%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Code_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getPerson_no())%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.English_name_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getEnglish_name())%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Staff_category_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getPerson_type())%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Sex_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%="1".equals(resultVo.getSex())?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Male"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Female")%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Mobile_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getMobile())%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Telephone_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getTel())%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.E_mail_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getEmail())%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Address_1' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getAddress())%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Postal_Code_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getPostalcode())%></td>
				</tr>
				<!--tr>
					<td align="right">备用字段：</td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getColumn1())%></td>
				</tr>
				<tr>
					<td align="right">备用字段：</td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getColumn2())%></td>
				</tr>
				<tr>
					<td align="right">备用字段：</td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getColumn3())%></td>
				</tr-->
				<tr>
					<td align="right"><fmt:message key='venus.authority.Remarks_' bundle='${applicationAuResources}' /></td>
					<td colspan="3" align="left"><%=StringHelperTools.prt(resultVo.getRemark())%></td>
				</tr>
				<!--tr>
					<td align="right">可用状态：</td>
					<td align="left"><%="1".equals(resultVo.getEnable_status())?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Enabled"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Disable")%></td>
				</tr>
				<tr>
					<td align="right">启用/禁用时间：</td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getEnable_date(),19)%></td>
				</tr-->
				<tr>
					<td align="right"><fmt:message key='venus.authority.Created_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getCreate_date(),19)%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Modified_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getModify_date(),19)%></td>
				</tr>
				</table>
			</td>
		</tr>
	</table>
</div>

<!-- 所属关系示意图的代码  开始-->
<%if(isReadOnly) {%>
<div id="auDivParent2"> 
<table class="table_div_control">
	<tr > 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild2',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='venus.authority.Schematic_diagram_of_their_relationship' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>
<div id="auDivChild2"> 
<table class="table_div_content">
	<tr>
		<td>
             <table class="table_div_content_inner" width="100%"> 
                <tr>
                    <td width="100%" align="left">
                    	<iframe name="myTree" width="100%" height="150" frameborder="0" 
                    		src="<venus:base/>/jsp/authority/tree/deeptree_iframe.jsp?rootXmlSource=<venus:base/>/jsp/authority/tree/partyDetailTreeRoot.jsp?party_id%3D<%=resultVo.getId()%>">
                    	</iframe>
                    </td> 
                </tr> 
            </table> 
		</td> 
	</tr>
</table>
</div>
<%}%>
<!-- 所属关系示意图的代码  结束-->
<input type="hidden" name="cmd" value="">
<input type="hidden" name="enable_status" value="1">
<input type="hidden" name="id" value="<%=StringHelperTools.prt(resultVo.getId())%>">
<input type="hidden" name="relationId" value="<%=request.getParameter("relationId")%>">
<input type="hidden" name="partyType" value="<%=request.getParameter("type")%>">
<!-- 参照显示层 -->
<div id="iframeDialog" style="display:none"></div>
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>
	

