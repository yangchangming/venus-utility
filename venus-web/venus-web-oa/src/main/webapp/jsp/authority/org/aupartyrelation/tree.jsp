<%@ include file="/jsp/include/global.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%
	String parentCode = (String)request.getAttribute("parent_code");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Group_Tree' bundle='${applicationAuResources}' /></title>
<script>
<!--
	function delete_onClick(){  //删除记录
		myTree.returnValueName();//获取树选中的值，记录到codeStrList里
		var ids = myTree.codeStrList;
		var typeId = myTree.typeStrList;
		if(ids=="") {
	  		alert("<fmt:message key='venus.authority.Please_select_the_records_you_want_to_delete' bundle='${applicationAuResources}' />!")
	  		return;
		}
		if(typeId=="0") {
			alert("<fmt:message key='venus.authority.Not_a_leaf_node_the_node_is_not_allowed_to_delete_' bundle='${applicationAuResources}' />")
	  		return;
		}
		if(confirm("<fmt:message key='venus.authority.Configuration_information_will_be_synchronized_to_delete_permissions_whether_completely_remove_the_data_' bundle='${applicationAuResources}' />")) {
	    	form.action="<venus:base/>/auPartyRelation/delete?relId="+ids+"&partyrelationtype_id=<%=parentCode%>";
    		form.submit();
		}
	}
	function toAdd_onClick() {  //到增加记录页面
		myTree.returnValueName();//获取树选中的值，记录到codeStrList里
		var ids = myTree.codeStrList;
		if(ids=="") {
	  		alert("<fmt:message key='venus.authority.Please_choose_which_node_s_parent_node' bundle='${applicationAuResources}' />!")
	  		return;
		}
		var refPath = "<venus:base/>/jsp/authority/org/auparty/mainFrame.jsp?pageFlag=ref";
		var rtObj = window.showModalDialog(refPath,new Object(),'dialogHeight=600px;dialogWidth=800px;resizable:yes;status:no;scroll:auto;');
		if(rtObj != undefined){	
			var partyIds = "";
			for(var i=0; i<rtObj.length-1; i++) {
				partyIds += rtObj[i] + ",";
			}
			partyIds += rtObj[rtObj.length-1];
			form.action="<venus:base/>/auPartyRelation/insertMulti?ids=" + partyIds + "&parentRelId=" + ids + "&relTypeId=<%=parentCode%>";
    		form.submit();
		}
	}
	function refresh_onClick(){  //刷新本页
		form.action="<venus:base/>/auPartyRelation/hasRoot?partyrelationtype_id=<%=parentCode%>";
		form.submit();
	}
		function getChangeTree4All(outputArray, basePath, inputType, rootCode, isSubmitAll, returnValueType, treeLevel, dataLimit){
		//回填的表单项名称
		if(outputArray==null) {
			alert("<fmt:message key='venus.authority.Set_the_single_name_of_the_table_filling' bundle='${applicationAuResources}' />");
			return false;
		}
		//树结点的类型，分radio和checkbox两种，默认radio
		if(inputType==null || inputType=="")
			inputType = "radio";

		//控制树的节点是否全带checkbox（radio），yes全带，no只有树的最末尾一层带
		if(isSubmitAll==null || isSubmitAll=="")
			isSubmitAll = "no";

		//返回值是哪个字段，可以选择partyrelation表的id、party_id和code三者之一，默认为code
		if(returnValueType==null || returnValueType=="")
			returnValueType = "code";
			
		//参数：控制树能展示到的层次，0 全部，1 公司，2 部门，3 岗位，-3 去除岗位，默认为0
		if(treeLevel == null || treeLevel=="") {
			treeLevel = "0";
		}
		
		//参数：是否控制数据权限,0 否，1 是
		if(dataLimit == null || dataLimit=="") {
			dataLimit = "0";
		}
		myTree.returnValueName();
		var treePath = basePath+"/jsp/authority/tree/treeRef4Change.jsp?srcrelationid="+myTree.codeStrList+"&inputType="+inputType+"&nodeRelationType=noRelation&rootXmlSource="
				+basePath+"/jsp/authority/tree/orgTree.jsp?parent_code%3D"+rootCode+"%26submit_all%3D"+isSubmitAll+"%26return_type%3D"+returnValueType+"%26tree_level%3D"+treeLevel+"%26data_limit%3D"+dataLimit;
		
		var returnVal = window.showModalDialog(treePath, new Object(),'dialogHeight=600px;dialogWidth=350px;resizable:yes;status:no;scroll:auto;');
		if (returnVal) {
			parent.location.reload();	
		}
	}
//-->
</script>
</head>
<body topmargin="0">
<form name="form" method="post" action="">
<div id="auDivParent1"> 
<table class="table_div_control">
	<tr > 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='venus.authority.Group_Tree' bundle='${applicationAuResources}' />
		</td>
		<td> 
			<table align="right">
				<tr> 
					<td class="button_ellipse" onClick="javascript:toAdd_onClick();"><img src="<venus:base/>/images/icon/add.gif" class="div_control_image"><fmt:message key='venus.authority.Added' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" onClick="javascript:delete_onClick();"><img src="<venus:base/>/images/icon/delete.gif" class="div_control_image"><fmt:message key='venus.authority.Delete' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" onClick="javascript:refresh_onClick();"><img src="<venus:base/>/images/icon/refresh.gif" class="div_control_image"><fmt:message key='venus.authority.Refresh' bundle='${applicationAuResources}' /></td>
					<%if("1099100400000000001".equals(parentCode)){%>
					<td class="button_ellipse" onClick="javascript:getChangeTree4All(new Array(),'<venus:base/>','radio','','1','','2','1');"><img src="<venus:base/>/images/icon/mix.gif" class="div_control_image"><fmt:message key='venus.authority.Tone_level' bundle='${applicationAuResources}' /></td>
					<%}%>
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
			<iframe name="myTree" width="100%" height="320" 
				src="<venus:base/>/jsp/authority/tree/deeptree_iframe.jsp?inputType=radio&rootXmlSource=
				<venus:base/>/jsp/authority/tree/treeLimitData.jsp?parent_code=<%=parentCode%>%26submit_all%3Dyes%26return_type%3Did%26node_type%3Dis_leaf%26data_limit%3D1">
			</iframe>
		</td>
	</tr>
</table>
</div>
</form>
</body>
</html>

