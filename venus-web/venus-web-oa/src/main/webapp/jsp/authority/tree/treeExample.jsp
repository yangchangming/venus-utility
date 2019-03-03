<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>tree example</title>
<script language="javascript">
<!--
	function getTree(outputArray, basePath, inputType, rootCode, isSubmitAll, returnValueType, returnNodeType){
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

		//返回值的类型，分id、code和party_id三种，默认为id
		if(returnValueType==null || returnValueType=="")
			returnValueType = "id";
			
		//返回节点的类型，分is_leaf（是否叶子节点）、party_type（团体类型ID）
		//和relation_type（团体关系类型ID）三种，默认为party_type
		if(returnNodeType==null || returnNodeType=="")
			returnNodeType = "party_type";

		var treePath ="";
		//根节点编号，如果为空则显示全部团体关系类型
		if(rootCode==null || rootCode=="") {
			treePath = basePath+"/jsp/authority/tree/treeRef.jsp?inputType="+inputType+"&nodeRelationType=noRelation&rootXmlSource="
				+basePath+"/jsp/authority/tree/treeRoot.jsp?submit_all%3D"+isSubmitAll+"%26return_type%3D"+returnValueType+"%26node_type%3D"+returnNodeType;
		}else {
			treePath = basePath+"/jsp/authority/tree/treeRef.jsp?inputType="+inputType+"&nodeRelationType=noRelation&rootXmlSource="
				+basePath+"/jsp/authority/tree/treeData.jsp?parent_code%3D"+rootCode+"%26submit_all%3D"+isSubmitAll+"%26return_type%3D"+returnValueType+"%26node_type%3D"+returnNodeType;
		}
		var rtObj = window.showModalDialog(treePath, new Object(),'dialogHeight=600px;dialogWidth=350px;resizable:yes;status:no;scroll:auto;');
		if(rtObj != undefined && rtObj.length > 0){
			var allTextValue = "";
			var allTextName = "";
			var allParentName = "";
			var detailedType = "";
			for(var i=0; i<rtObj.length-1; i++) {
				allTextValue += rtObj[i]['returnValue'] + ",";
				allTextName += rtObj[i]['childName'] + ",";
				allParentName += rtObj[i]["parentName"] + ",";
				detailedType += rtObj[i]["detailedType"] + ",";
			}
			allTextValue += rtObj[rtObj.length-1]['returnValue'];
			allTextName += rtObj[rtObj.length-1]['childName'];
			allParentName += rtObj[rtObj.length-1]['parentName'];
			detailedType += rtObj[rtObj.length-1]['detailedType'];
			
			var textValue = outputArray[0];
			var textName = outputArray[1];
			
			textValue.value = allTextValue;
			textName.value = allTextName;
			if(outputArray.length>=3) {
				var textParentName = outputArray[2];
				textParentName.value = allParentName;
			}
			if(outputArray.length==4) {
				var nodeType = outputArray[3];
				nodeType.value = detailedType;
			}
		} 
	}
//-->
</script>
</head>
<body> 
<table class="table_div_content" height="100%"> 
    <tr> 
        <td valign="top"> 
            <table class="table_noFrame" width="100%"> 
             	<!--tr bgcolor="#FFFFFF">
                    <td colspan="4" align="left">
                        根据登陆人所拥有的数据权限过滤后的树的例子(多选)
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" name="name1" inputName="组织机构树" 
						maxlength="50"/><input type="hidden" name="id1"/><img class="refButtonClass" src="<venus:base/>/images/au/09.gif" 
						onClick="javascript:getLimitTree(new Array(id1, name1),'','','','');"/> 
					</td> 
                </tr--> 
                <tr align="left" bgcolor="#FFFFFF"> 
                    <td colspan="4" align="left"><fmt:message key='venus.authority.Parent_node_name_' bundle='${applicationAuResources}' /><input type="text"  name="parentName" size="100"/></td> 
                </tr>
                <tr align="left" bgcolor="#FFFFFF"> 
                    <td colspan="4" align="left"><fmt:message key='venus.authority.Node_type' bundle='${applicationAuResources}' />ID<fmt:message key='venus.authority._' bundle='${applicationAuResources}' /><input type="text"  name="textType" size="100"/></td>
                </tr>
                <tr>
                    <td width="20%" align="right"><fmt:message key='venus.authority._All_organizations_radio_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" name="tree_name_radio" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
						maxlength="50" hiddenInputId="tree_id_radio"/><input type="hidden" name="tree_id_radio"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getTree(new Array(tree_id_radio, tree_name_radio, parentName, textType),'<venus:base/>','radio','','yes','');"/> </td>
                    <td width="20%" align="right"><fmt:message key='venus.authority._All_organizations_many_election_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" name="tree_name_checkbox" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
						maxlength="50" hiddenInputId="tree_id_checkbox"/><input type="hidden" name="tree_id_checkbox"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getTree(new Array(tree_id_checkbox, tree_name_checkbox, parentName, textType),'<venus:base/>','checkbox','','yes','');"/> </td>
                </tr> 
                <tr> 
                    <td colspan="4">iframe<fmt:message key='venus.authority.Example_' bundle='${applicationAuResources}' />
	                     <table class="table_noFrame" width="100%"> 
			                <tr>
			                    <td width="20%" align="right">&nbsp;</td>
			                    <td width="30%" align="left"><iframe name="myTree" width="200" height="300" src="deeptree_iframe.jsp?inputType=radio&rootXmlSource=<venus:base/>/jsp/authority/tree/treeRoot.jsp?submit_all%3Dno%26return_type%3Dparty_id"></iframe>
			                        </td> 
			                    <td width="20%">
			                        <input type="button" name="Submit1" value="<fmt:message key='venus.authority.Submit' bundle='${applicationAuResources}' />" onClick="myTree.returnValueName();alert(myTree.nameStrList);alert(myTree.codeStrList);" class="button_ellipse">
			                    </td>
			                    <td width="30%" align="left">&nbsp;</td> 
			                </tr> 
			            </table> 
					</td> 
                </tr> 
                <tr align="left"> 
                    <td colspan="4" align="left">
                    <fmt:message key='venus.authority.Method' bundle='${applicationAuResources}' />getTree(outputArray,basePath,inputType,rootCode,isSubmitAll,returnValueType,returnNodeType)<fmt:message key='venus.authority.In_the_overall' bundle='${applicationAuResources}' />js<fmt:message key='venus.authority.There_can_be_directly_invoked' bundle='${applicationAuResources}' /><br>
                    <fmt:message key='venus.authority.Specific_parameters_as_follows_' bundle='${applicationAuResources}' /><br>
                    &nbsp;&nbsp;&nbsp;&nbsp;outputArray<fmt:message key='venus.authority._Filling_the_table_single_array_can_be' bundle='${applicationAuResources}' />id<fmt:message key='venus.authority._1' bundle='${applicationAuResources}' />name<fmt:message key='venus.authority.Or' bundle='${applicationAuResources}' />id<fmt:message key='venus.authority._1' bundle='${applicationAuResources}' />name<fmt:message key='venus.authority._1' bundle='${applicationAuResources}' />parentName<br>
                    &nbsp;&nbsp;&nbsp;&nbsp;basePath<fmt:message key='venus.authority._Project_of_the_path_that_is_' bundle='${applicationAuResources}' />request.getContextPath()<br>
                    &nbsp;&nbsp;&nbsp;&nbsp;inputType<fmt:message key='venus.authority._The_node_type_sub_' bundle='${applicationAuResources}' />checkbox<fmt:message key='venus.authority.And' bundle='${applicationAuResources}' />radio<br>
                    &nbsp;&nbsp;&nbsp;&nbsp;rootCode<fmt:message key='venus.authority._Root_number_if_empty_all_groups_showed_the_type_of_relationship' bundle='${applicationAuResources}' /><br>
					&nbsp;&nbsp;&nbsp;&nbsp;isSubmitAll<fmt:message key='venus.authority._Control_whether_all_nodes_in_the_tree_belt' bundle='${applicationAuResources}' />checkbox<fmt:message key='venus.authority._2' bundle='${applicationAuResources}' />radio<fmt:message key='venus.authority._3' bundle='${applicationAuResources}' />yes<fmt:message key='venus.authority.Belts_' bundle='${applicationAuResources}' />no<fmt:message key='venus.authority.Only_the_most_at_the_end_of_the_tree_layer_of_tape_by_default' bundle='${applicationAuResources}' />no<br>
					&nbsp;&nbsp;&nbsp;&nbsp;returnType<fmt:message key='venus.authority._Return_value_of_type_sub_' bundle='${applicationAuResources}' />id<fmt:message key='venus.authority._4' bundle='${applicationAuResources}' />code<fmt:message key='venus.authority.And' bundle='${applicationAuResources}' />party_id<fmt:message key='venus.authority.Three_by_default' bundle='${applicationAuResources}' />id<br>
					&nbsp;&nbsp;&nbsp;&nbsp;returnNodeType<fmt:message key='venus.authority._Return_the_node_type_sub_' bundle='${applicationAuResources}' />is_leaf<fmt:message key='venus.authority._Whether_the_leaf_node_' bundle='${applicationAuResources}' />party_type<fmt:message key='venus.authority._Group_type' bundle='${applicationAuResources}' />ID<fmt:message key='venus.authority._And' bundle='${applicationAuResources}' />relation_type<fmt:message key='venus.authority._Group_relationship_type' bundle='${applicationAuResources}' />ID<fmt:message key='venus.authority._Three_by_default' bundle='${applicationAuResources}' />party_type<br>
					<br>
					</td> 
                </tr> 
            </table> 
        </td> 
    </tr> 
</table> 
</body>
</html>

