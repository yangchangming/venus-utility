<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.authority.util.VoHelperTools" %>
<%@ page import="venus.authority.util.StringHelperTools" %>
<%@ page import="venus.authority.au.auuser.vo.AuUserVo" %>
<%@ page import="venus.authority.au.auuser.util.IAuUserConstants" %>
<%@ page import="venus.frames.mainframe.util.Helper"%>
<%@ page import="venus.authority.util.GlobalConstants"%>
<%@ page import="venus.authority.org.aupartyrelationtype.bs.IAuPartyRelationTypeBS" %>
<%@page import="venus.authority.org.aupartyrelationtype.util.IConstants"%>
<%@page import="venus.authority.org.aupartyrelationtype.vo.AuPartyRelationTypeVo"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%  //取出本条记录
	AuUserVo resultVo = null;  //定义一个临时的vo变量
	resultVo = (AuUserVo)request.getAttribute(IAuUserConstants.REQUEST_BEAN_VALUE);  //从request中取出vo, 赋值给resultVo
	VoHelperTools.replaceToHtml(resultVo);  //把vo中的每个值过滤
	//关系类型
    java.util.Map relationTypeMap = new HashMap();
    IAuPartyRelationTypeBS bs = (IAuPartyRelationTypeBS)Helper.getBean(IConstants.BS_KEY);
    AuPartyRelationTypeVo searchVo = new AuPartyRelationTypeVo();
    searchVo.setKeyword("4");
    List al = bs.simpleQuery(1,Short.MAX_VALUE,null,searchVo);
    for(int i=0;i<al.size();i++){
        AuPartyRelationTypeVo reTypeVo = (AuPartyRelationTypeVo)al.get(i);
        if(!GlobalConstants.getRelaType_proxy().equals(reTypeVo.getId())){//非代理关系
            relationTypeMap.put(reTypeVo.getId(),reTypeVo.getName());
        }
    }
%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ranmin-based architecture project</title>
<script language="javascript">
	function goBack_onClick() {//返回
	    window.location="<venus:base/>/auUser/queryAll";
	}
	function find_onClick(){  //直接点到修改页面
		form.action="<%=request.getContextPath()%>/auUser/find";
		form.submit();
	}
	function delete_onClick(){  //直接点删除单条记录
		if(!getConfirm()) {  //如果用户在确认对话框中点"取消"
			return false;
		}
		form.action="<%=request.getContextPath()%>/auUser/delete";
		form.submit();
	}
</script>
</head>
<link href="<venus:base/>/themes/<venus:theme/>/css/tabs.css.jsp" rel="stylesheet" type="text/css">
<script language="javascript">
	var tabs  =  new Array(
		new Array ("<fmt:message key='venus.authority.Functional_competence' bundle='${applicationAuResources}' />","<venus:base/>/auAuthorize/viewFuncAu?isUser=1&partyId=<%=resultVo.getParty_id()%>"),
		new Array ("<fmt:message key='venus.authority.Agency_data_rights' bundle='${applicationAuResources}' />","<venus:base/>/jsp/authority/tree/deeptree4Au.jsp?inputType=checkbox&nodeRelationType=noRelation&rootXmlSource="
				+"<venus:base/>/jsp/authority/au/auauthorize/viewOrg.jsp?parent_code%3D<%=GlobalConstants.getRelaType_comp()%>%26partyId%3D<%=resultVo.getParty_id()%>%26isUser%3D1"),		
		<%java.util.Iterator it = relationTypeMap.entrySet().iterator();
        while (it.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
        %>
        new Array ("<%= String.valueOf(entry.getValue())%><fmt:message key='venus.authority.Data_rights' bundle='${applicationAuResources}' />","<venus:base/>/jsp/authority/tree/deeptree4Au.jsp?inputType=checkbox&nodeRelationType=noRelation&rootXmlSource="
                +"<venus:base/>/jsp/authority/au/auauthorize/viewOrg.jsp?parent_code%3D<%= String.valueOf(entry.getKey())%>%26partyId%3D<%=resultVo.getParty_id()%>%26isUser%3D1"),
        <%}%>
		new Array ("<fmt:message key='venus.authority.The_role_of_data_rights' bundle='${applicationAuResources}' />","<venus:base/>/jsp/authority/tree/deeptree4Au.jsp?inputType=checkbox&nodeRelationType=noRelation&rootXmlSource="
                +"<venus:base/>/jsp/authority/au/auauthorize/viewOrg.jsp?parent_code%3D<%=GlobalConstants.getRelaType_role()%>%26partyId%3D<%=resultVo.getParty_id()%>%26isUser%3D1"),      
        new Array ("<fmt:message key='venus.authority.Agency_data_rights0' bundle='${applicationAuResources}' />","<venus:base/>/jsp/authority/tree/deeptree4Au.jsp?inputType=checkbox&nodeRelationType=noRelation&rootXmlSource="
                +"<venus:base/>/jsp/authority/au/auauthorize/viewOrg.jsp?parent_code%3D<%=GlobalConstants.getRelaType_proxy()%>%26partyId%3D<%=resultVo.getParty_id()%>%26isUser%3D1"),      
        new Array ("<fmt:message key='venus.authority.Historical_data_permissions' bundle='${applicationAuResources}' />","<venus:base/>/historyLog/simpleQueryForView?partyId=<%=resultVo.getParty_id()%>&isUser=1&rType=<%=GlobalConstants.getResType_orga()%>"),
		new Array ("<fmt:message key='venus.authority.Functional_data_permissions' bundle='${applicationAuResources}' />","<venus:base/>/jsp/authority/au/auauthorize/viewFuncOrgFrame.jsp?cmd=viewFuncOrgAu&isUser=1&partyId=<%=resultVo.getParty_id()%>&rootCode=<%=GlobalConstants.getRelaType_comp()%>"),
		<%it = relationTypeMap.entrySet().iterator();
        while (it.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
        %>
        new Array ("<%= String.valueOf(entry.getValue())%><fmt:message key='venus.authority.Functional_data_permissions' bundle='${applicationAuResources}' />","<venus:base/>/jsp/authority/au/auauthorize/viewFuncOrgFrame.jsp?cmd=viewFuncOrgAu&isUser=1&partyId=<%=resultVo.getParty_id()%>&rootCode=<%=String.valueOf(entry.getKey())%>"),
        <%}%>
		new Array ("<fmt:message key='venus.authority.Field_level_data_permissions' bundle='${applicationAuResources}' />","<venus:base/>/auAuthorize/viewDataAu?isUser=1&partyId=<%=resultVo.getParty_id()%>&rType=<%=GlobalConstants.getResType_fild()%>"),
		new Array ("<fmt:message key='venus.authority.Record_level_data_permissions' bundle='${applicationAuResources}' />","<venus:base/>/auAuthorize/viewDataAu?isUser=1&partyId=<%=resultVo.getParty_id()%>&rType=<%=GlobalConstants.getResType_recd()%>")
	); 
	var myimages=new Array()
	function preloadimages(){
		for (i=0;i<preloadimages.arguments.length;i++){
			myimages[i]=new Image();
			myimages[i].src=preloadimages.arguments[i];
		}
	}
</script>
<script src="<venus:base/>/js/au/tabs.js"></script>
</head>
<body onload="preloadimages('<%=request.getContextPath() %>/images/au/ig_tab_winxp1.gif','<%=request.getContextPath() %>/images/au/ig_tab_winxp2.gif','<%=request.getContextPath() %>/images/au/ig_tab_winxp3.gif'); writeTabs(430);" >
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Detailed_Page' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<form name="form" method="post">
<table class="table_noFrame">
	<tr>
		<td>
			<!-- input name="button_update" class="button_ellipse" type="button" value="修改" onClick="javascript:find_onClick();">
			<input name="button_delete" class="button_ellipse" type="button" value="删除" onClick="javascript:delete_onClick();"-->
			<input name="button_back" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Return' bundle='${applicationAuResources}' />"  onclick="goBack_onClick();" >
		</td>
	</tr>
</table>

<div id="auDivParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" class="div_control_image" onClick="javascript:hideshow('auDivChild0',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')"><fmt:message key='venus.authority.View' bundle='${applicationAuResources}' /><%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Login")%><fmt:message key='venus.authority.Detailed' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>

<div id="auDivChild0"> 
<table class="table_div_content">
	<tr>
		<td>
			<table class="table_div_content_inner">
				<tr>
					<td width="60"><strong><fmt:message key='venus.authority.Basic_Information' bundle='${applicationAuResources}' /></strong><br></td>
					<td width="75">&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td align="right"><fmt:message key='venus.authority.Account_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getLogin_id())%></td>
				</tr>
				<!--tr>
					<td>&nbsp;</td>
					<td align="right">登录密码：</td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getPassword())%></td>
				</tr-->
				<tr>
					<td>&nbsp;</td>
					<td align="right"><fmt:message key='venus.authority.Real_Name_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getName())%></td>
				</tr>
				<!--tr>
					<td>&nbsp;</td>
					<td align="right">所属单位：</td>
					<td align="left"><%//=StringHelperTools.prt(venus.authority.au.helper.CommHelper.getOrgNameByParty_id(resultVo.getParty_id()))%></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td align="right">超级管理员：</td>
					<td align="left"><%//="1".equals(resultVo.getIs_admin())?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Be"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.No0")%></td>
				</tr-->
				<!--tr>
					<td>&nbsp;</td>
					<td align="right">代理状态：</td>
					<td align="left"><%="1".equals(resultVo.getAgent_status())?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Enabled"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Disable")%></td>
				</tr-->
				<tr>
					<td>&nbsp;</td>
					<td align="right"><fmt:message key='venus.authority.Created_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getCreate_date(),19)%></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td align="right"><fmt:message key='venus.authority.Modified_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getModify_date(),19)%></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>


<div id="auDivParent1"> 
<table class="table_div_control">
	<tr>
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='venus.authority.All_rights' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>

<div id="auDivChild1" align="center"> 
<table class="table_div_content">
	<tr>
		<td>
			<table width="100%">
			  <tr>
				<td>
				<div id="tabsDiv"></div>
				</td>
			</tr>
			</table>
		</td>
	</tr>
</table>
</div>	
<input type="hidden" name="id" value="<%=StringHelperTools.prt(resultVo.getId())%>">
<%
	String system_id = (String)request.getAttribute("system_id");
	if(system_id==null || system_id.length()==0) {
		system_id = request.getParameter("system_id");
		if(system_id==null) {
			system_id = "";
		}
	}
	String func_code = (String)request.getAttribute("func_code");
	if(func_code==null || func_code.length()==0) {
		func_code = request.getParameter("func_code");
		if(func_code==null) {
			func_code = "";
		}
	}
%>
<input type="hidden" name="system_id" value="<%=system_id%>">
<input type="hidden" name="func_code" value="<%=func_code%>">
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>
	

