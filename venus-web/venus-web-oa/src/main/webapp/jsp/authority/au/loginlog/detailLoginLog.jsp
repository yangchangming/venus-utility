<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.oa.util.VoHelperTools" %>
<%@ page import="venus.oa.util.StringHelperTools" %>
<%@ page import="venus.oa.login.loginlog.vo.LoginLogVo" %>
<%@ page import="venus.oa.login.loginlog.util.ILoginLogConstants" %>
<%  //取出本条记录
	LoginLogVo resultVo = null;  //定义一个临时的vo变量
	resultVo = (LoginLogVo)request.getAttribute(ILoginLogConstants.REQUEST_BEAN);  //从request中取出vo, 赋值给resultVo
	VoHelperTools.replaceToHtml(resultVo);  //把vo中的每个值过滤
%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>gap.rm-based architecture project</title>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Detailed_Page' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<form name="form" method="post">
<table class="table_noFrame">
	<tr>
		<td>
			<input name="button_back" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Return' bundle='${applicationAuResources}' />"  onclick="javascript:returnBack();" >
		</td>
	</tr>
</table>
<div id="auDivParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild0',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')"><%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Log_log")%><fmt:message key='venus.authority.Details0' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>

<div id="auDivChild0"> 
<table class="table_div_content">
<tr><td>
	<table class="table_div_content_inner">
		<tr>
			<td width="60">&nbsp;</td>
			<td width="75">&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="right"><fmt:message key='venus.authority.Login_ID_' bundle='${applicationAuResources}' /></td>
			<td align="left"><%=StringHelperTools.prt(resultVo.getLogin_id())%></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="right"><fmt:message key='venus.authority.User_Name_' bundle='${applicationAuResources}' /></td>
			<td align="left"><%=StringHelperTools.prt(resultVo.getName())%></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="right">IP&nbsp;&nbsp;<fmt:message key='venus.authority.Address_' bundle='${applicationAuResources}' /></td>
			<td align="left"><%=StringHelperTools.prt(resultVo.getLogin_ip())%></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="right">IE&nbsp;&nbsp;<fmt:message key='venus.authority.Version_' bundle='${applicationAuResources}' /></td>
			<td align="left"><%=StringHelperTools.prt(resultVo.getIe())%></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="right"><fmt:message key='venus.authority.Operating_system_' bundle='${applicationAuResources}' /></td>
			<td align="left"><%=StringHelperTools.prt(resultVo.getOs())%></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="right"><fmt:message key='venus.authority.Main_machine_name_' bundle='${applicationAuResources}' /></td>
			<td align="left"><%=StringHelperTools.prt(resultVo.getHost())%></td>
		</tr>
		<!--tr>
			<td>&nbsp;</td>
			<td align="right">退出类型：</td>
			<td align="left"><%=StringHelperTools.prt(resultVo.getLogout_type())%></td>
		</tr-->
		<tr>
			<td>&nbsp;</td>
			<td align="right"><fmt:message key='venus.authority.Login_time_' bundle='${applicationAuResources}' /></td>
			<td align="left"><%=StringHelperTools.prt(resultVo.getLogin_time(),19)%></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="right"><fmt:message key='venus.authority.Exit_Time_' bundle='${applicationAuResources}' /></td>
			<td align="left"><%=StringHelperTools.prt(resultVo.getLogout_time(),19)%></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td align="right"><fmt:message key='venus.authority.Login_Status_' bundle='${applicationAuResources}' /></td>
			<td align="left"><%=StringHelperTools.prt(resultVo.getLogin_state())%></td>
		</tr>
	</table>
</td></tr>
</table>
</div>

<input type="hidden" name="id" value="<%=StringHelperTools.prt(resultVo.getId())%>">

</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>

