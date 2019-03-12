<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import = "java.util.List,java.util.Iterator,java.util.ArrayList"%>
<%@ page import="venus.frames.web.page.PageVo" %>
<%@ page import="venus.oa.util.VoHelperTools" %>
<%@ page import="venus.oa.util.StringHelperTools" %>
<%@ page import="venus.oa.organization.auparty.vo.PartyVo"%>
<%@ page import="venus.oa.organization.auparty.util.IConstants" %>
<%
    PartyVo vo = (PartyVo)request
				.getAttribute(IConstants.REQUEST_BEAN_VALUE);
	String my_create_date = "";
	String my_modify_date = "";
	if (vo.getCreate_date() != null) {
		my_create_date = StringHelperTools.prt(StringHelperTools.prt(vo
			.getCreate_date()), 16);
	}
	if (vo.getModify_date() != null) {
		my_modify_date = StringHelperTools.prt(StringHelperTools.prt(vo
			.getModify_date()), 16);
	}
%>
<title><fmt:message key='venus.authority.View_Company' bundle='${applicationAuResources}' /></title>
<script language="javascript">

	function toAdd_onClick(thisId) {//添加团体关系
		//打开组织机构树
		var treePath = "<venus:base/>/jsp/authority/tree/treeRef.jsp?inputType=radio&nodeRelationType=noRelation"
			+"&rootXmlSource=<venus:base/>/jsp/authority/tree/treeRoot.jsp?submit_all%3Dyes%26return_type%3Did%26node_type%3Drelation_type";
		var rtObj = window.showModalDialog(treePath,new Object(),'dialogHeight=600px;dialogWidth=350px;resizable:yes;status:no;scroll:auto;');
		if(rtObj != undefined && rtObj.length > 0){
			var relId = rtObj[0]['returnValue'];
			var relTypeId = rtObj[0]['detailedType'];
			var partyId = document.form.partyId.value;
			form.action="<venus:base/>/auPartyRelation/insert?returnPage=party_detail"
				+ "&id=" + partyId + "&parentRelId=" + relId + "&relTypeId=" + relTypeId;
    		form.submit();
		} 
	}	
	
	function delete_onClick(thisId, type) {//揪除团体关系
		var elementCheckbox = document.getElementsByName("checkbox_template");
		var number = 0;
		var relId = null;
		for(var i=0;i<elementCheckbox.length;i++){
			if(elementCheckbox[i].checked) {
				number += 1;
				relId = elementCheckbox[i].value;
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
		
		var isLeaf = "0";
		try {
			var thisHidden = getLayoutHiddenObjectById(relId);
			isLeaf = thisHidden.is_leaf;
		} catch(e) {
			
		}
		if(isLeaf=="0") {
	  		alert("<fmt:message key='venus.authority.Not_a_leaf_node_the_node_is_not_allowed_to_delete_' bundle='${applicationAuResources}' />");
	  		return;
		}
		
		if(confirm("<fmt:message key='venus.authority.It_completely_remove_the_data_' bundle='${applicationAuResources}' />")) {
			var partyId = document.form.partyId.value;
	    	form.action="<venus:base/>/auPartyRelation/delete?returnPage=party_detail&id="+partyId+"&relId="+relId;
    		form.submit();
		}
	}
	function goBack_onClick(typeId) {//添加团体
	    window.location="<venus:base/>/auParty/queryAll?typeId="+typeId;
	}	
	function refresh_onClick() {//刷新
		var partyId = document.form.partyId.value;
		form.action = "<venus:base/>/auParty/detailPage?id=" + partyId;
	    form.submit();
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
		
	function view_onClick(id){
		//document.frames[0].location.href="<venus:base/>/jsp/authority/tree/deeptree_iframe.jsp?rootXmlSource=<venus:base/>/jsp/authority/tree/partyDetailTreeRoot.jsp?submit_all%3Dno%26return_type%3Dparty_id%26id%3D"+id;
	}
</script>

</head>
<!--body onLoad="javascript:refresh_left();"-->
<body>
<form name="form" method="post"> 
<input type="hidden" name="partyId" value="<%=vo.getId()%>">
<table class="table_noframe" width="97%" align="center">
	<tr>
		<td valign="middle">			
			<input name="button_cancel" type="button" class="button_ellipse" value="<fmt:message key='venus.authority.Return' bundle='${applicationAuResources}' />"  onClick="goBack_onClick('<%=vo.getPartytype_id()%>');">
		</td>
	</tr>
</table>
	<table border="1" bordercolordark="#FFFFFF" bordercolorlight="#7EBAFF" cellpadding="5" cellspacing="0" width="96%" align="center"> 	
		<tr> 
			<td width="30%" align="right"><fmt:message key='venus.authority.Name_1' bundle='${applicationAuResources}' /></td> 
			<td width="70%" align="left" bgcolor="#FFFFFF">&nbsp;<%=vo.getName()%></td> 
		</tr> 		
		<tr> 
			<td width="30%" align="right"><fmt:message key='venus.authority.Type_1' bundle='${applicationAuResources}' /></td> 
			<td width="70%" align="left" bgcolor="#FFFFFF">&nbsp;<%=vo.getPartyname()%></td> 
		</tr> 		
		<tr> 
			<td align="right"><fmt:message key='venus.authority.Added_' bundle='${applicationAuResources}' /></td> 
			<td align="left" bgcolor="#FFFFFF">&nbsp;<%=my_create_date%></td> 
		</tr>
		<tr> 
			<td align="right"><fmt:message key='venus.authority.Modified_' bundle='${applicationAuResources}' /></td> 
			<td align="left" bgcolor="#FFFFFF">&nbsp;<%=my_modify_date%></td> 
		</tr>
	</table> 

<div id="auDivParent1"> 
<table class="table_div_control">
	<tr > 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='venus.authority.Their_relationship' bundle='${applicationAuResources}' />
		</td>
		<td> 
			<table align="right">
				<tr> 
					<td class="button_ellipse" onClick="javascript:toAdd_onClick();" title="<fmt:message key='venus.authority.Select_the_parent_group' bundle='${applicationAuResources}' />"><img src="<venus:base/>/images/icon/add.gif" class="div_control_image"><fmt:message key='venus.authority.New_Relationship' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" onClick="javascript:delete_onClick();"><img src="<venus:base/>/images/icon/delete.gif" class="div_control_image"><fmt:message key='venus.authority.Remove_Relations' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" onClick="javascript:refresh_onClick();"><img src="<venus:base/>/images/icon/refresh.gif" class="div_control_image"><fmt:message key='venus.authority.Refresh' bundle='${applicationAuResources}' /></td>
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
		<layout:collection onRowDblClick="detail_onClick(getRowHiddenId())" name="list" id="wy1" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >
			<layout:collectionItem width="40" title="" style="text-align:center;">
				<bean:define id="wy3" name="wy1" property="id"/>
				<input type="radio" name="checkbox_template" value="<%=wy3%>"/>
			</layout:collectionItem>
			<layout:collectionItem width="20"  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sequence") %>' style="text-align:center;">
				<venus:sequence/>
				<bean:define id="wy3" name="wy1" property="id"/>
				<bean:define id="isLeaf" name="wy1" property="is_leaf"/>
				<bean:define id="code" name="wy1" property="code"/>
				<input type="hidden" signName="hiddenId" value="<%=wy3%>" is_leaf="<%=isLeaf%>"/>
			</layout:collectionItem>
			<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Name") %>' property="name" />
			<layout:collectionItem width="100" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Group_type_of_relationship") %>' property="relationtype_name" />
            <layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Parent_groups") %>' property="parent_partyname" />
            <layout:collectionItem width="80" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Whether_the_leaf_node") %>'> 
            	<bean:define id="isLeaf" name="wy1" property="is_leaf"/>
					<%="1".equals(isLeaf)?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Be"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.No0")%>
			</layout:collectionItem> 
		</layout:collection>
		
		<jsp:include page="/jsp/include/page.jsp" />
		</td>
	</tr>
</table>
</div>   
<div id="auDivParent2"> 
<table class="table_div_control">
	<tr > 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild2',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='venus.authority.Tree' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>
<div id="auDivChild2"> 
<table class="table_div_content">
	<tr>
		<td>
	                     <table bgcolor="#FFFFFF" width="100%"> 
			                <tr>
			                    <td width="5%" align="right">&nbsp;</td>
			                    <td width="90%" align="left"><iframe name="myTree" width="100%" height="150" frameborder="0"></iframe>
			                    </td> 
			                    <td width="5%" align="left">&nbsp;</td> 
			                </tr> 
			            </table> 
					</td> 
	</tr>
</table>
</div>
</form> 
</body>
</html>

