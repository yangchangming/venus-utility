<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.authority.util.VoHelperTools" %>
<%@ page import="venus.authority.util.StringHelperTools" %>
<%@ page import="venus.authority.au.aufunctree.vo.AuFunctreeVo" %>
<%@ page import="venus.authority.au.aufunctree.util.IAuFunctreeConstants" %>
<%@ page import="venus.authority.util.GlobalConstants"%>
<%  //取出本条记录
	AuFunctreeVo resultVo = null;  //定义一个临时的vo变量
	resultVo = (AuFunctreeVo)request.getAttribute(IAuFunctreeConstants.REQUEST_BEAN_VALUE);  //从request中取出vo, 赋值给resultVo
	VoHelperTools.replaceToHtml(resultVo);  //把vo中的每个值过滤
%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ranmin-based architecture project</title>
<script language="javascript">
	function find_onClick(){  //直接点到修改页面
		var parent_code = document.form.parent_code.value;
		form.action="<%=request.getContextPath()%>/auFunctree/find?parent_code="+parent_code;
		form.submit();
	}
	function delete_onClick(){  //直接点删除单条记录
		if(!getConfirm()) {  //如果用户在确认对话框中点"取消"
			return false;
		}
		form.action="<%=request.getContextPath()%>/auFunctree/delete";
		form.target="_parent";
		form.submit();
	}  
	function toAdd_onClick() {  //到增加记录页面
		form.action="<venus:base/>/jsp/authority/au/aufunctree/insertAuFunctree.jsp";
		form.submit();
	}
	function toSort_onClick() {  //到排序页面
		form.action="<venus:base/>/jsp/authority/au/aufunctree/sortAuFunctree.jsp";
		form.submit();
	}
</script>
</head>
<body>
<form name="form" method="post">
	<table class="table_noFrame">
		<tr>
			<td>
				<input name="button_back" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Add' bundle='${applicationAuResources}' />"  onclick="javascript:toAdd_onClick();" >
				<input name="button_update" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Modify' bundle='${applicationAuResources}' />" onClick="javascript:find_onClick();">
				<input name="button_delete" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Delete' bundle='${applicationAuResources}' />" onClick="javascript:delete_onClick();">
				<input name="button_sort" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Sort' bundle='${applicationAuResources}' />" onClick="javascript:toSort_onClick();">
			</td>
		</tr>
	</table>
	<br>
	<table border="1" bordercolordark="#FFFFFF" bordercolorlight="#7EBAFF" cellpadding="5" cellspacing="0" width="98%" align="center"> 
		<tr>
			<td width="5%" align="right" nowrap="nowrap"><fmt:message key='venus.authority.Node_Name_' bundle='${applicationAuResources}' /></td>
			<td align="left" bgcolor="#FFFFFF">&nbsp;<%=StringHelperTools.prt(resultVo.getName())%></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap"><fmt:message key='venus.authority.Node_types_' bundle='${applicationAuResources}' /></td>
			<td align="left" bgcolor="#FFFFFF">&nbsp;<%=GlobalConstants.getResType_menu().equals(resultVo.getType())?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Function_menu"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Page_button")%></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap"><fmt:message key='venus.authority.Node_ID_' bundle='${applicationAuResources}' /></td>
			<td align="left" bgcolor="#FFFFFF">&nbsp;<%=StringHelperTools.prt(resultVo.getKeyword())%></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap"><fmt:message key='venus.authority.Own_code_' bundle='${applicationAuResources}' /></td>
			<td align="left" bgcolor="#FFFFFF">&nbsp;<%=StringHelperTools.prt(resultVo.getCode())%></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap"><fmt:message key='venus.authority.Parent_code_' bundle='${applicationAuResources}' /></td>
			<td align="left" bgcolor="#FFFFFF">&nbsp;<%=StringHelperTools.prt(resultVo.getParent_code())%></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap"><fmt:message key='venus.authority.Nodes_' bundle='${applicationAuResources}' /></td>
			<td align="left" bgcolor="#FFFFFF">&nbsp;<%=StringHelperTools.prt(resultVo.getTotal_code())%></td>
		</tr>
		
		<!--tr>
			<td align="right">快捷键：</td>
			<td align="left" bgcolor="#FFFFFF">&nbsp;<%=StringHelperTools.prt(resultVo.getHot_key())%></td>
		</tr-->
		<tr>
			<td align="right" nowrap="nowrap"><fmt:message key='venus.authority.Help_information_' bundle='${applicationAuResources}' /></td>
			<td align="left" bgcolor="#FFFFFF">&nbsp;<%=StringHelperTools.prt(resultVo.getHelp())%></td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap"><fmt:message key='venus.authority.Actual_Link_' bundle='${applicationAuResources}' /></td>
			<td align="left" bgcolor="#FFFFFF" style="word-break:break-all">&nbsp;<%=StringHelperTools.prt(resultVo.getUrl())%></td>
		</tr>
		<tr>
            <td align="right" nowrap="nowrap"><fmt:message key='venus.authority.Support' bundle='${applicationAuResources}' />SSL<fmt:message key='venus.authority._' bundle='${applicationAuResources}' /></td>
            <td align="left" bgcolor="#FFFFFF" style="word-break:break-all">&nbsp;<%="1".equals(StringHelperTools.prt(resultVo.getIs_ssl()))?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Be"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.No0")%></td>
        </tr>
        <tr>
            <td align="right" nowrap="nowrap"><fmt:message key='venus.authority.Function_open_' bundle='${applicationAuResources}' /></td>
            <td align="left" bgcolor="#FFFFFF" style="word-break:break-all">&nbsp;<%="1".equals(StringHelperTools.prt(resultVo.getIs_public()))?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Be"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.No0")%></td>
        </tr>
		<tr>
			<td align="right" nowrap="nowrap"><fmt:message key='venus.authority.Sort_code_' bundle='${applicationAuResources}' /></td>
			<td align="left" bgcolor="#FFFFFF">&nbsp;<%=StringHelperTools.prt(resultVo.getOrder_code())%></td>
		</tr>
		<tr> 
			<td align="right" nowrap="nowrap"><fmt:message key='venus.authority.Added_' bundle='${applicationAuResources}' /></td> 
			<td align="left" bgcolor="#FFFFFF" bgcolor="#FFFFFF">&nbsp;<%=StringHelperTools.prt(resultVo.getCreate_date(),16)%></td> 
		</tr>
		<tr> 
			<td align="right" nowrap="nowrap"><fmt:message key='venus.authority.Modified_' bundle='${applicationAuResources}' /></td> 
			<td align="left" bgcolor="#FFFFFF" bgcolor="#FFFFFF">&nbsp;<%=StringHelperTools.prt(resultVo.getModify_date(),16)%></td> 
		</tr>
	</table> 
<input type="hidden" name="id" value="<%=StringHelperTools.prt(resultVo.getId())%>">
<input type="hidden" name="parent_type" value="<%=StringHelperTools.prt(resultVo.getType())%>">
<input type="hidden" name="total_code" value="<%=StringHelperTools.prt(resultVo.getTotal_code())%>">
<input type="hidden" name="parent_name" value="<%=StringHelperTools.prt(resultVo.getName())%>">
<input type="hidden" name="parent_code" value="<%=StringHelperTools.prt(resultVo.getParent_code())%>">
</form>
</body>
</html>
	

