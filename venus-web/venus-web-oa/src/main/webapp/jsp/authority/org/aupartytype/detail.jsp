<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/include/global.jsp"%>
<%@ page import="venus.authority.util.VoHelperTools"%>
<%@ page import="venus.authority.util.StringHelperTools"%>
<%@ page import="venus.authority.org.aupartytype.vo.AuPartyTypeVo"%>
<%@ page import="venus.authority.org.aupartytype.util.IConstants"%>
<%@ page import="venus.commons.xmlenum.EnumRepository"%>
<%@ page import="venus.commons.xmlenum.EnumValueMap"%>
<%
        AuPartyTypeVo vo = (AuPartyTypeVo) request.getAttribute(IConstants.REQUEST_BEAN_VALUE);
        String my_create_date = "";
        String my_modify_date = "";
        String my_enable_date = "";
        if (vo.getEnable_date() != null) {
            my_enable_date = StringHelperTools.prt(StringHelperTools.prt(vo.getEnable_date()), 16);
        }
        if (vo.getCreate_date() != null) {
            my_create_date = StringHelperTools.prt(StringHelperTools.prt(vo.getCreate_date()), 16);
        }
        if (vo.getModify_date() != null) {
            my_modify_date = StringHelperTools.prt(StringHelperTools.prt(vo.getModify_date()), 16);
        }
        EnumRepository er = EnumRepository.getInstance();
        er.loadFromDir();
        EnumValueMap typeMap = er.getEnumValueMap("TypeStatus");
        String type_TypeName = typeMap.getLabel(vo.getKeyword());
		if(type_TypeName==null) type_TypeName=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Unknown");		
%>
<title><fmt:message key='venus.authority.View_Group_Type' bundle='${applicationAuResources}' /></title>
<script language="javascript">
    function queryAll_onClick(){  //查询全部数据列表
        if (typeof(parent.frames["myList"]) == "undefined") {
            returnBack();
        } else {
            <%--window.location="<venus:base/>/AuPartyTypeAction.do?cmd=simpleQuery&keyword=<%=request.getParameter("keywordconst")%>";--%>
			window.location="<venus:base/>/auPartyType/simpleQuery?keyword=<%=request.getParameter("keywordconst")%>";
        }
    }		
</script>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Detailed_list' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');
</script>
<table class="table_noframe" width="97%" align="center">
	<tr>
		<td valign="middle"><input name="button_cancel" type="button"
			class="button_ellipse" value="<fmt:message key='venus.authority.Return' bundle='${applicationAuResources}' />" onClick="queryAll_onClick();"></td>
	</tr>
</table>
<table border="1" bordercolordark="#FFFFFF" bordercolorlight="#7EBAFF"
	cellpadding="5" cellspacing="0" width="80%" align="center">
	<tr>
		<td colspan="2" align="center"><fmt:message key='venus.authority.Group_Type_Details' bundle='${applicationAuResources}' /></td>
	</tr>
	<tr>
		<td width="30%" align="right"><fmt:message key='venus.authority.Name_1' bundle='${applicationAuResources}' /></td>
		<td width="70%" align="left" bgcolor="#FFFFFF">&nbsp;<%=vo.getName()%></td>
	</tr>
	<tr>
		<td width="30%" align="right"><fmt:message key='venus.authority.Group_Type_Category_' bundle='${applicationAuResources}' /></td>
		<td width="70%" align="left" bgcolor="#FFFFFF">&nbsp;<%=type_TypeName%></td>
	</tr>
	<tr>
		<td width="30%" align="right"><fmt:message key='venus.authority.Which_mapping_tables_' bundle='${applicationAuResources}' /></td>
		<td width="70%" align="left" bgcolor="#FFFFFF">&nbsp;<%=vo.getTable_name()%></td>
	</tr>
	<tr>
		<td width="30%" align="right"><fmt:message key='venus.authority.Enabled' bundle='${applicationAuResources}' />/<fmt:message key='venus.authority.Disabled_' bundle='${applicationAuResources}' /></td>
		<td width="70%" align="left" bgcolor="#FFFFFF">&nbsp;<%="1".equals(vo.getEnable_status()) ? venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Enabled") : venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Disable")%></td>
	</tr>
	<tr>
		<td width="30%" align="right"><fmt:message key='venus.authority.Enabled' bundle='${applicationAuResources}' />/<fmt:message key='venus.authority.Disable_Time_' bundle='${applicationAuResources}' /></td>
		<td width="70%" align="left" bgcolor="#FFFFFF">&nbsp;<%=my_enable_date%></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key='venus.authority.Created_' bundle='${applicationAuResources}' /></td>
		<td align="left" bgcolor="#FFFFFF">&nbsp;<%=my_create_date%></td>
	</tr>
	<tr>
		<td align="right"><fmt:message key='venus.authority.Modified_' bundle='${applicationAuResources}' /></td>
		<td align="left" bgcolor="#FFFFFF">&nbsp;<%=my_modify_date%></td>
	</tr>
	<tr>
		<td width="30%" align="right"><fmt:message key='venus.authority.Notes_' bundle='${applicationAuResources}' /></td>
		<td width="70%" align="left" bgcolor="#FFFFFF">&nbsp;<%=vo.getRemark()%></td>
	</tr>
</table>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>
</body>
</html>

