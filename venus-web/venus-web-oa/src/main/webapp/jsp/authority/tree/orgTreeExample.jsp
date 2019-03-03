<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>tree example</title>
<script language="javascript">
    var global_outputArray;

    function returnValueName(rtObj){
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
            

            var textValue = global_outputArray[0];
            var textName = global_outputArray[1];
            
            jQuery("#"+textValue).val(allTextValue);
            jQuery("#"+textValue).trigger("change");
            jQuery("#"+textName).val(allTextName);
            jQuery("#"+textName).trigger("change");
            
            if(global_outputArray.length>=3) {
                var textParentName = global_outputArray[2];
                jQuery("#"+textParentName).val(allParentName);
                jQuery("#"+textParentName).trigger("change");
            }
            if(global_outputArray.length==4) {
                var nodeType = global_outputArray[3];
                jQuery("#"+nodeType).val(detailedType);
                jQuery("#"+nodeType).trigger("change");
            }
        } 
    
}

//<!--
	function getTree(outputArray, basePath, inputType, rootCode, isSubmitAll, returnValueType, treeLevel, dataLimit){
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

		var treePath = basePath+"/jsp/authority/tree/treeRef.jsp?inputType="+inputType+"&nodeRelationType=noRelation&rootXmlSource="
				+basePath+"/jsp/authority/tree/orgTree.jsp?parent_code%3D"+rootCode+"%26submit_all%3D"+isSubmitAll+"%26return_type%3D"+returnValueType+"%26tree_level%3D"+treeLevel+"%26data_limit%3D"+dataLimit;

        global_outputArray=outputArray;//设置返回输出控件集合
	    showIframeDialog("iframeDialog","<fmt:message key='venus.authority.Reference_page' bundle='${applicationAuResources}' />", treePath, 400, 600);
	}
	
	function getQueryTree(outputArray, basePath, inputType, rootCode, isSubmitAll, returnValueType, treeLevel, dataLimit){
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

		var treePath = basePath+"/jsp/authority/tree/treeQueryRef.jsp?inputType="+inputType+"&nodeRelationType=noRelation&basePath="+basePath+"&parent_code="+rootCode+"&submit_all="+isSubmitAll+"&return_type="+returnValueType+"&tree_level="+treeLevel+"&data_limit="+dataLimit;

        global_outputArray=outputArray;//设置返回输出控件集合
       showIframeDialog("iframeDialog","<fmt:message key='venus.authority.Reference_page' bundle='${applicationAuResources}' />", treePath, 400, 600);
	}

    function getChooseTree(outputArray, basePath, inputType, rootCode, isSubmitAll, returnValueType, treeLevel, dataLimit, hierarchy, attributesFilter){
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
            
        //参数：控制树能展示到的层次，根据partytypeid来控制团体类型（所有需要显示的partytypeid用半角逗号分开），默认为0，即不过滤团体类型
        if(treeLevel == null || treeLevel=="") {
            treeLevel = "0";
        }
        
        //参数：是否控制数据权限，0 否，1 是
        if(dataLimit == null || dataLimit=="") {
            dataLimit = "0";
        }
        
        //参数：是否显示的层级数目，默认-1为全显示
        if(hierarchy == null || hierarchy=="") {
            hierarchy = "-1";
        }
        
        //参数：控制某些属性的数据可操作（具有单选或多选按钮），默认为不控制
        if(attributesFilter == null || attributesFilter=="") {
            attributesFilter = "{}";
        }

        var treePath = basePath+"/jsp/authority/tree/treeChooseRef.jsp?inputType="+inputType+"&nodeRelationType=noRelation&basePath="+basePath+"&parent_code="+rootCode+"&submit_all="+isSubmitAll+"&return_type="+returnValueType+"&tree_level="+treeLevel+"&data_limit="+dataLimit+"&hierarchy="+hierarchy+"&attributesFilter="+attributesFilter;

         global_outputArray=outputArray;//设置返回输出控件集合
       showIframeDialog("iframeDialog","<fmt:message key='venus.authority.Reference_page' bundle='${applicationAuResources}' />", treePath, 400, 600);
    }
//-->

function example_alert(){
	if(myTree.codeStrList==""){
		alert("请选择左侧树节点");
		return;
	}

    alert(myTree.nameStrList);

    alert(myTree.codeStrList);

}
</script>
</head>
<body> 
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Form_List' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>

<table class="table_div_content" height="100%"> 
    <tr> 
        <td valign="top"> 
            <table class="table_noFrame" width="100%"> 
                <tr align="left"> 
                    <td colspan="4" align="left">
                    <fmt:message key='venus.authority.Call_method' bundle='${applicationAuResources}' />getTree(outputArray,basePath,inputType,rootCode,isSubmitAll,returnValueType,treeLevel,dataLimit)<fmt:message key='venus.authority.Access_to_the_organization_tree' bundle='${applicationAuResources}' /><br>
                    <fmt:message key='venus.authority.The_method_in_the_overall' bundle='${applicationAuResources}' />js<fmt:message key='venus.authority.There_can_be_directly_invoked' bundle='${applicationAuResources}' />;<fmt:message key='venus.authority.This_method_can_also_refer_to' bundle='${applicationAuResources}' />jsp<fmt:message key='venus.authority.Of' bundle='${applicationAuResources}' />javascript<fmt:message key='venus.authority.Method' bundle='${applicationAuResources}' />:getTree<fmt:message key='venus.authority.Own_custom' bundle='${applicationAuResources}' /><br><br>
                    <fmt:message key='venus.authority.Specific_parameters_as_follows_' bundle='${applicationAuResources}' /><br>
                    &nbsp;&nbsp;&nbsp;&nbsp;outputArray<fmt:message key='venus.authority._Filling_the_table_single_array_can_be' bundle='${applicationAuResources}' />id<fmt:message key='venus.authority._1' bundle='${applicationAuResources}' />name<fmt:message key='venus.authority.Or' bundle='${applicationAuResources}' />id<fmt:message key='venus.authority._1' bundle='${applicationAuResources}' />name<fmt:message key='venus.authority._1' bundle='${applicationAuResources}' />parentName<fmt:message key='venus.authority._1' bundle='${applicationAuResources}' />partyType<br>
                    &nbsp;&nbsp;&nbsp;&nbsp;basePath<fmt:message key='venus.authority._Project_of_the_path_that_is_' bundle='${applicationAuResources}' />request.getContextPath()<br>
                    &nbsp;&nbsp;&nbsp;&nbsp;inputType<fmt:message key='venus.authority._The_node_type_sub_' bundle='${applicationAuResources}' />checkbox<fmt:message key='venus.authority.And' bundle='${applicationAuResources}' />radio<br>
                    &nbsp;&nbsp;&nbsp;&nbsp;rootCode<fmt:message key='venus.authority._Root_number_if_it_is_empty_then_show_all_organizations' bundle='${applicationAuResources}' /><br>
					&nbsp;&nbsp;&nbsp;&nbsp;isSubmitAll<fmt:message key='venus.authority._Control_whether_all_nodes_in_the_tree_belt' bundle='${applicationAuResources}' />checkbox<fmt:message key='venus.authority._2' bundle='${applicationAuResources}' />radio<fmt:message key='venus.authority._3' bundle='${applicationAuResources}' />1<fmt:message key='venus.authority.Belts_' bundle='${applicationAuResources}' />0<fmt:message key='venus.authority.Only_the_most_at_the_end_of_the_tree_layer_of_tape_by_default' bundle='${applicationAuResources}' />0<br>
					&nbsp;&nbsp;&nbsp;&nbsp;returnType<fmt:message key='venus.authority._Return_value_is_what_the_field_you_can_choose' bundle='${applicationAuResources}' />aupartyrelation<fmt:message key='venus.authority.Table' bundle='${applicationAuResources}' />id<fmt:message key='venus.authority._4' bundle='${applicationAuResources}' />party_id<fmt:message key='venus.authority.And' bundle='${applicationAuResources}' />code<fmt:message key='venus.authority.One_of_the_three_the_default_is' bundle='${applicationAuResources}' />code<br>
					&nbsp;&nbsp;&nbsp;&nbsp;treeLevel<fmt:message key='venus.authority._Control_tree_shows_the_level_of' bundle='${applicationAuResources}' />0 <fmt:message key='venus.authority.All_' bundle='${applicationAuResources}' />1 <fmt:message key='venus.authority.Company' bundle='${applicationAuResources}' />2 <fmt:message key='venus.authority.Departments0' bundle='${applicationAuResources}' />3 <fmt:message key='venus.authority.Post_' bundle='${applicationAuResources}' />-3 <fmt:message key='venus.authority.Removal_of_posts_by_default' bundle='${applicationAuResources}' />0<br>
					&nbsp;&nbsp;&nbsp;&nbsp;dataLimit<fmt:message key='venus.authority._Whether_the_control_data_permissions' bundle='${applicationAuResources}' />0 <fmt:message key='venus.authority.No_' bundle='${applicationAuResources}' />1 <fmt:message key='venus.authority.Is_by_default' bundle='${applicationAuResources}' />0<br><br>
					<fmt:message key='venus.authority.The_following_example_shows_how_to_call' bundle='${applicationAuResources}' />:
					<br>
					</td> 
                </tr>
                <tr align="left"> 
                    <td colspan="4" align="left"><fmt:message key='venus.authority.Back_to_the_full_name_of_the_node_' bundle='${applicationAuResources}' /><input type="text"  id="parentName" name="parentName" size="100" class="text_field" style="width:60%;"/></td> 
                </tr>
                <tr align="left"> 
                    <td colspan="4" align="left"><fmt:message key='venus.authority.Back_to_the_node_type' bundle='${applicationAuResources}' />ID<fmt:message key='venus.authority._' bundle='${applicationAuResources}' /><input type="text"  id="textType" name="textType" size="100" class="text_field" style="width:60%;"/></td>
                </tr>
                <tr>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Out_of_all_the_organizations_only_the_radio_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name1" name="name1" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
						maxlength="50" hiddenInputId="id1"/><input type="hidden" id="id1" name="id1"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getTree(new Array('id1', 'name1', 'parentName', 'textType'),'<venus:base/>','radio','','','','','');"/> </td>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Whole_organization_you_can_choose_more_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name2" name="name2" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
						maxlength="50" hiddenInputId="id2"/><input type="hidden" id="id2" name="id2"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getTree(new Array('id2', 'name2', 'parentName', 'textType'),'<venus:base/>','checkbox','','','','','');"/> </td>
                </tr> 
                 <tr>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Out_of_all_organizations_all_optional_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name3" name="name3" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
						maxlength="50" hiddenInputId="id3"/><input type="hidden" id="id3" name="id3"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getTree(new Array('id3', 'name3', 'parentName', 'textType'),'<venus:base/>','radio','','1','','','');"/> </td>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Entire_organization_control_permissions_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name4" name="name4" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
						maxlength="50" hiddenInputId="id4"/><input type="hidden" id="id4" name="id4"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getTree(new Array('id4', 'name4', 'parentName', 'textType'),'<venus:base/>','checkbox','','1','','','1');"/> </td>
                </tr> 
                <tr>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Out_of_all_the_organizations_you_can_check_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name13" name="name13" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
						maxlength="50" hiddenInputId="id13"/><input type="hidden" id="id13" name="id13"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getQueryTree(new Array('id13', 'name13', 'parentName', 'textType'),'<venus:base/>','radio','','1','','','');"/> </td>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Can_query_control_permissions_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name14" name="name14" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
						maxlength="50" hiddenInputId="id14"/><input type="hidden" id="id14" name="id14"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getQueryTree(new Array('id14', 'name14', 'parentName', 'textType'),'<venus:base/>','checkbox','','','','','1');"/> </td>
                </tr> 
                <tr>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Does_not_control_the_permissions_only_the_company_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name5" name="name5" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
						maxlength="50" hiddenInputId="id5"/><input type="hidden" id="id5" name="id5"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getTree(new Array('id5', 'name5', 'parentName', 'textType'),'<venus:base/>','radio','','','','1','');"/> </td>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Control_authority_only_the_company_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name6" name="name6" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
						maxlength="50" hiddenInputId="id6"/><input type="hidden" id="id6" name="id6"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getTree(new Array('id6', 'name6', 'parentName', 'textType'),'<venus:base/>','checkbox','','','','1','1');"/> </td>
                </tr> 
                <tr>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Does_not_control_permissions_from_the_department_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name7" name="name7" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
						maxlength="50" hiddenInputId="id7"/><input type="hidden" id="id7" name="id7"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getTree(new Array('id7', 'name7', 'parentName', 'textType'),'<venus:base/>','radio','','','','2','');"/> </td>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Control_permissions_from_the_department_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name8" name="name8" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
						maxlength="50" hiddenInputId="id8"/><input type="hidden" id="id8" name="id8"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getTree(new Array('id8', 'name8', 'parentName', 'textType'),'<venus:base/>','checkbox','','','','2','1');"/> </td>
                </tr>  
                <tr>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Does_not_control_the_permissions_to_post_a_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name9" name="name9" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
						maxlength="50" hiddenInputId="id9"/><input type="hidden" id="id9" name="id9"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getTree(new Array('id9', 'name9', 'parentName', 'textType'),'<venus:base/>','radio','','','','3','');"/> </td>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Control_authority_out_to_the_post_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name10" name="name10" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
						maxlength="50" hiddenInputId="id10"/><input type="hidden" id="id10" name="id10"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getTree(new Array('id10', 'name10', 'parentName', 'textType'),'<venus:base/>','checkbox','','','','3','1');"/> </td>
                </tr>
                <tr>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Does_not_control_the_rights_to_remove_posts_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name11" name="name11" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
						maxlength="50" hiddenInputId="id11"/><input type="hidden" id="id11" name="id11"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getTree(new Array('id11', 'name11', 'parentName', 'textType'),'<venus:base/>','radio','','','','-3','');"/> </td>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Control_permission_to_remove_posts_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name12" name="name12" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
						maxlength="50" hiddenInputId="id12"/><input type="hidden" id="id12" name="id12"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getTree(new Array('id12', 'name12', 'parentName', 'textType'),'<venus:base/>','checkbox','','','','-3','1');"/> </td>
                </tr>
                <tr>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Does_not_control_the_rights_to_relationtype_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name11" name="name11" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
                        maxlength="50" hiddenInputId="id11"/><input type="hidden" id="id11" name="id11"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getChooseTree(new Array('id11', 'name11', 'parentName', 'textType'),'<venus:base/>','radio','','','','','');"/> </td>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Control_permission_to_relationtype_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name12" name="name12" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
                        maxlength="50" hiddenInputId="id12"/><input type="hidden" id="id12" name="id12"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getChooseTree(new Array('id12', 'name12', 'parentName', 'textType'),'<venus:base/>','checkbox','','1','','','1');"/> </td>
                </tr>
                <tr>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Control_the_attribute_to_relationtype_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name11" name="name11" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
                        maxlength="50" hiddenInputId="id11"/><input type="hidden" id="id11" name="id11"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getChooseTree(new Array('id11', 'name11', 'parentName', 'textType'),'<venus:base/>','radio','','','','','','','{ISPROCESSUNIT:1}');"/> </td>
                    <td width="20%" align="right"><fmt:message key='venus.authority._Control_the_hierarchy_to_relationtype_' bundle='${applicationAuResources}' /></td>
                    <td width="30%" align="left">
                        <input type="text" class="text_field_reference_readonly" validators="isSearch" id="name12" name="name12" inputName="<fmt:message key='venus.authority.Organization_Tree' bundle='${applicationAuResources}' />" 
                        maxlength="50" hiddenInputId="id12"/><input type="hidden" id="id12" name="id12"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getChooseTree(new Array('id12', 'name12', 'parentName', 'textType'),'<venus:base/>','checkbox','','','','','1','2','');"/> </td>
                </tr>
                <br>
                <tr> 
                    <td colspan="4">iframe<fmt:message key='venus.authority.Example_' bundle='${applicationAuResources}' />
	                     <table class="table_noFrame" width="100%"> 
			                <tr>
			                    <td width="20%" align="right">&nbsp;</td>
			                    <td width="30%" align="left"><iframe name="myTree" width="200" height="150" src="deeptree_iframe.jsp?inputType=radio&rootXmlSource=<venus:base/>/jsp/authority/tree/orgTree.jsp?submit_all%3D1%26return_type%3Did%26tree_level%3D0%26data_limit%3D0"></iframe>
			                        </td> 
			                    <td width="20%">
			                        <input type="button" name="Submit1" value="<fmt:message key='venus.authority.Submit' bundle='${applicationAuResources}' />" onClick="myTree.returnValueName();example_alert();" class="button_ellipse">
			                    </td>
			                    <td width="30%" align="left">&nbsp;</td> 
			                </tr> 
			            </table> 
					</td> 
                </tr>  
            </table> 
        </td> 
    </tr> 
</table> 
<!-- 参照显示层 -->
<div id="iframeDialog" style="display:none"></div>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>

