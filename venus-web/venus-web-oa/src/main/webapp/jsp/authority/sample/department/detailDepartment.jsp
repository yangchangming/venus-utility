<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.authority.util.VoHelperTools" %>
<%@ page import="venus.authority.util.StringHelperTools" %>
<%@ page import="venus.authority.sample.department.vo.DepartmentVo" %>
<%@ page import="venus.authority.sample.department.util.IDepartmentConstants" %>
<%  //取出本条记录
	DepartmentVo resultVo = null;  //定义一个临时的vo变量
	resultVo = (DepartmentVo)request.getAttribute(IDepartmentConstants.REQUEST_BEAN_VALUE);  //从request中取出vo, 赋值给resultVo
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
<title>ranmin-based architecture project</title>
<script language="javascript">
	function toAdd_onClick() {  //到增加记录页面
		form.action="<%=request.getContextPath()%>/relation/go2Add";
		form.cmd.value = "go2Add";
		form.submit();
	}
	function toSort_onClick() {  //到排序页面
		var parentRelId = document.form.relationId.value;
		window.location="<%=request.getContextPath()%>/jsp/authority/sample/relation/sort.jsp?parentRelId="+parentRelId;
	}
	function find_onClick(){  //直接点到修改页面
		form.action="<%=request.getContextPath()%>/department/find";
		form.cmd.value = "find";
		form.submit();
	}
	function delete_onClick(){  //直接点删除单条记录
		//if(!confirm("本操作将删除该部门的组织关系信息\n\n"
		//+ "该部门的基本信息不会被删除，如果要删除基本信息请到部门档案管理页面进行删除\n\n"
		if(!confirm("<fmt:message key='venus.authority.This_operation_will_completely_remove_the_sector' bundle='${applicationAuResources}' />\n"
		+ "<fmt:message key='venus.authority.Are_you_sure_you_do_this_' bundle='${applicationAuResources}' />")) {
			return false;
		}
		form.action="<%=request.getContextPath()%>/department/delete";
		form.cmd.value = "delete";
		form.target="_parent";
		form.submit();
	}
	function goBack() {
		form.action="<%=request.getContextPath()%>/department/queryAll";
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
				<input name="button_add" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Add' bundle='${applicationAuResources}' />"  onclick="javascript:toAdd_onClick();" >
				<input name="button_update" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Modify' bundle='${applicationAuResources}' />" onClick="javascript:find_onClick();">
				<input name="button_delete" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Delete' bundle='${applicationAuResources}' />" onClick="javascript:delete_onClick();">
				<input name="button_sort" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Sort' bundle='${applicationAuResources}' />" onClick="javascript:toSort_onClick();">
				<input name="button_change" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Tone_level' bundle='${applicationAuResources}' />" onClick="javascript:getChangeTree(new Array(),'<venus:base/>','radio','','2','','2','1');">
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
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild0',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')"><%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Department0")%><fmt:message key='venus.authority.Details0' bundle='${applicationAuResources}' />
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
					<td width="30%" align="right"><fmt:message key='venus.authority.Department_Name_' bundle='${applicationAuResources}' /></td>
					<td width="70%" align="left"><%=StringHelperTools.prt(resultVo.getDept_name())%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Code_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getDept_no())%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Department_ID_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getDept_flag())%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Department_Type_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getDept_type())%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Sectoral_level_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getDept_level())%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Responsible_Person_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getDept_leader())%></td>
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
	

