<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import = "java.util.List,java.util.Iterator,java.util.ArrayList"%>
<%@ page import="venus.frames.web.page.PageVo" %>
<%@ page import="venus.authority.service.history.vo.HistoryLogVo"%>
<%@ page import="venus.authority.util.VoHelperTools" %>
<%@ page import="venus.authority.util.StringHelperTools" %>
<%
	List historyList = (List)request.getAttribute("beans");
%>
<head>
<title><fmt:message key='venus.authority.Query_Template' bundle='${applicationAuResources}' /></title>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Agencies_operating_history' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');
</script>
<table class="table_noFrame">
	<tr>
		<td>&nbsp;&nbsp;
			<input name="button_cancel" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Determine' bundle='${applicationAuResources}' />"  onclick="javascript:window.close();" >
		</td>
	</tr>
</table>
<table width=90% align= "center">
<%
for(int i=0;i<historyList.size();i++){
	HistoryLogVo vo = (HistoryLogVo)historyList.get(i);	
%>
<tr>
<td>
<div id="auDivParent<%=i%>"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild<%=i%>',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')" >&nbsp;&nbsp;<fmt:message key='venus.authority.Operating_time_' bundle='${applicationAuResources}' /><%=vo.getOperate_date()%>&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key='venus.authority.Operator_' bundle='${applicationAuResources}' /><%=vo.getOperate_name()%>
		</td>
	</tr>
</table>
</div>
<div id="auDivChild<%=i%>"> 
<table class="table_div_content">
<tr><td>
	<table class="table_noFrame" width="100%">
		<tr>
			<td>
			<%
			String strTree = vo.getSource_orgtree();
			String trees[] = strTree.split(venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority._0"));
			for(int j=0;j<trees.length;j++){
			%>
				    <ul><img src="<%=request.getContextPath()%>/jsp/authority/tree/image/folderopen.gif"><%=trees[j]%>
				    <br/><br/>
			<%}%>
			<ul><img src="<%=request.getContextPath()%>/jsp/authority/tree/image/folderopen.gif"><%=vo.getSource_name()%>
			<br/><br/>
			<%
			for(int j=0;j<trees.length;j++){
			%>
				    </ul>
			<%}%>
			</td>
		</tr>		
	</table>
</td></tr>
</table>
</div>
</td>
</tr>
<%}%>		
</table>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>
</body>
</html>

