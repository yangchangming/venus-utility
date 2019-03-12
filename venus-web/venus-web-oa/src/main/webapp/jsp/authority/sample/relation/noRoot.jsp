<%@ page import="venus.oa.organization.aupartyrelationtype.vo.AuPartyRelationTypeVo" %>
<%@ page import="venus.oa.organization.aupartyrelationtype.bs.IAuPartyRelationTypeBS" %>
<%@ page import="venus.oa.organization.aupartytype.vo.AuPartyTypeVo" %>
<%@ page import="venus.oa.organization.aupartytype.bs.IAuPartyTypeBS" %>
<%@ page import="venus.springsupport.BeanFactoryHelper" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%
String relationType_id = (String)request.getAttribute("parent_code");
String location = "/jsp/authority/sample/company/insertCompany.jsp?isAddRoot=1";
String action = "company";
boolean isCommon = false;
if(org.apache.commons.lang.StringUtils.isNotEmpty(relationType_id)){
    Object relationTypeVo = ((IAuPartyRelationTypeBS) BeanFactoryHelper.getBean("auPartyRelationTypeBS")).find(relationType_id);
    String partyType_id = ((AuPartyRelationTypeVo)relationTypeVo).getRoot_partytype_id();
    if(org.apache.commons.lang.StringUtils.isNotEmpty(partyType_id)){
        Object partyTypeVo = ((IAuPartyTypeBS) BeanFactoryHelper.getBean("auPartyTypeBS")).find(partyType_id);
        String tableName = ((AuPartyTypeVo)partyTypeVo).getTable_name();
        if(org.apache.commons.lang.StringUtils.isNotEmpty(tableName)){
            boolean isGenerateCode = tableName.contains("ORGANIZE_");
            String catalogue = isGenerateCode?tableName.substring("ORGANIZE_".length()):tableName.substring("COLLECTIVE_".length());
            catalogue = String.valueOf(catalogue.charAt(0))+catalogue.substring(1).toLowerCase();
            location = "/jsp/authority/generate/"+catalogue+(isGenerateCode?"Organize":"Collective")+"/insert"+catalogue+(isGenerateCode?"Organize":"Collective")+".jsp?isAddRoot=1&relType="+relationType_id;
            action = catalogue+(isGenerateCode?"Organize":"Collective")+"Action";
            isCommon = true;
        }
    }
}else{
    relationType_id = "";
}
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Add_the_root_node' bundle='${applicationAuResources}' /></title>
<script> 
	function toAdd_onClick() {  //到增加记录页面
		window.location.href = "<venus:base/><%=location%>";
	}
	function toSel_onClick() {  //到选择记录页面
		var refPath = "<%=request.getContextPath()%>/jsp/authority/sample/relation/refFrame.jsp?actionStr=<%=action%>";
		var rtObj = window.showModalDialog(refPath, new Object(),'dialogHeight=600px;dialogWidth=850px;resizable:yes;status:no;scroll:auto;');
		if(rtObj != undefined){
			var partyId = rtObj[0];
			form.action="<%=request.getContextPath()%>/relation/initRoot?partyId="+partyId+"&relTypeId=<%=relationType_id%>";
			form.submit();
		}
	}
</script>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Add_the_root_node' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<form name="form" method="post">
<div id="ccParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild0',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')"><fmt:message key='venus.authority.Add_the_root_node' bundle='${applicationAuResources}' />
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
					<td>
						<img src="<%=request.getContextPath()%>/images/au/yq_bt<au:i18next/>.jpg" width="73" height="14">
						<br>
						<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key='venus.authority.Organizations_do_not_tree_root_Note_The_root_is_usually_the_top_companies_' bundle='${applicationAuResources}' /><br><br>
					</td>
				</tr>
				<br><br><br>
				<tr> 
					<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input name="btnAdd1" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Create_root_node' bundle='${applicationAuResources}' />" onClick="javascript:toAdd_onClick()">
					</td>
				</tr>
				<%if(isCommon){ %>
				<tr><td></td></tr>
				<tr> 
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input name="btnAdd2" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Select_from_the_existing_org' bundle='${applicationAuResources}' />" onClick="javascript:toSel_onClick()">
                    </td>
                <tr>
				<%} %>
				<!--tr> 
					<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input name="btnAdd2" class="button_ellipse" type="button" value="从现有的公司中选择" onClick="javascript:toSel_onClick()">
					</td>
				</tr-->
			</table>
		</td>
	</tr>
</table>
</div>
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>

