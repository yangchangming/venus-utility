/*
*存储返回赋值的控件
*/
var global_outputArray;

//返回赋值
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
            
		jQuery("#"+textValue).size()!=0?jQuery("#"+textValue).val(allTextValue):jQuery("input[name='"+textValue+"']").val(allTextValue);
        jQuery("#"+textValue).size()!=0?jQuery("#"+textValue).trigger("change"):jQuery("input[name='"+textValue+"']").trigger("change");
        jQuery("#"+textName).size()!=0?jQuery("#"+textName).val(allTextName):jQuery("input[name='"+textName+"']").val(allTextName);
        jQuery("#"+textName).size()!=0?jQuery("#"+textName).trigger("change"):jQuery("input[name='"+textName+"']").trigger("change");
        
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

/**
 * 组织机构树的调用
*/
function getTree(outputArray, basePath, inputType, rootCode, isSubmitAll, returnValueType, treeLevel, dataLimit){
	//回填的表单项名称
	if(outputArray==null) {
		alert(venus.authority.Set_the_single_name_of_the_table_filling);
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
       showIframeDialog("iframeDialog",venus.authority.Reference_page, treePath, 400, 600);
}
/**
 * 组织机构树的调用，可切换关系类型
*/
function getRTTree(outputArray, basePath, inputType, rootCode, isSubmitAll, returnValueType, treeLevel, dataLimit){
    //回填的表单项名称
    if(outputArray==null) {
        alert(venus.authority.Set_the_single_name_of_the_table_filling);
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

    var treePath = basePath+"/jsp/authority/tree/treeRTRef.jsp?inputType="+inputType+"&nodeRelationType=noRelation&partyType="+rootCode+"&rootXmlSource="
            +basePath+"/jsp/authority/tree/orgRTTree.jsp?submit_all%3D"+isSubmitAll+"%26return_type%3D"+returnValueType+"%26tree_level%3D"+treeLevel+"%26data_limit%3D"+dataLimit;
    
        global_outputArray=outputArray;//设置返回输出控件集合
       showIframeDialog("iframeDialog",venus.authority.Reference_page, treePath, 400, 600);
}
/**
 * 根据团体类型判断是否可操作（单选框、复选框可操作）
*/
function getTypeChooseTree(outputArray, basePath, inputType, rootCode, isSubmitAll, returnValueType, treeLevel, dataLimit, typeLevel){
    //回填的表单项名称
    if(outputArray==null) {
        alert(venus.authority.Set_the_single_name_of_the_table_filling);
        return false;
    }
    //树结点的类型，分radio和checkbox两种，默认radio
    if(inputType==null || inputType=="")
        inputType = "radio";

    //控制树的节点是否全带checkbox（radio），yes全带，no只有树的最末尾一层带
    isSubmitAll = "yes";

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
    
    //参数：那一级可以显示单选或复选框，company公司级，department部门级，position岗位级，employee员工级。可多选，逗号“,”隔开
    if(typeLevel == null || typeLevel=="") {
        typeLevel = "0";
    }

    var treePath = basePath+"/jsp/authority/tree/treeRef.jsp?inputType="+inputType+"&nodeRelationType=noRelation&rootXmlSource="
            +basePath+"/jsp/authority/tree/orgTypeChooseTree.jsp?parent_code%3D"+rootCode+"%26submit_all%3D"+isSubmitAll+"%26return_type%3D"+returnValueType+"%26tree_level%3D"+treeLevel+"%26data_limit%3D"+dataLimit+"%26typeLevel%3D"+typeLevel;
    
        global_outputArray=outputArray;//设置返回输出控件集合
       showIframeDialog("iframeDialog",venus.authority.Reference_page, treePath, 400, 600);
}
/**
 * 组织机构树调级调用
*/
	function getChangeTree(outputArray, basePath, inputType, rootCode, isSubmitAll, returnValueType, treeLevel, dataLimit){
		//回填的表单项名称
		if(outputArray==null) {
			alert(venus.authority.Set_the_single_name_of_the_table_filling);
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

		var treePath = basePath+"/jsp/authority/tree/treeRef4Change.jsp?srcrelationid="+document.form.relationId.value+"&inputType="+inputType+"&nodeRelationType=noRelation&rootXmlSource="
				+basePath+"/jsp/authority/tree/orgTree.jsp?parent_code%3D"+rootCode+"%26submit_all%3D"+isSubmitAll+"%26return_type%3D"+returnValueType+"%26tree_level%3D"+treeLevel+"%26data_limit%3D"+dataLimit;
		
		showIframeDialog("iframeDialog",venus.authority.Reference_page, treePath, 400, 600);
	}
/**
 *带根节点的组织机构树的调用
*/
function getTreeByRoot(outputArray, basePath, inputType, rootCode, isSubmitAll, returnValueType, treeLevel, dataLimit){
	//回填的表单项名称
	if(outputArray==null) {
		alert(venus.authority.Set_the_single_name_of_the_table_filling);
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
			+basePath+"/jsp/authority/tree/orgTreeRoot.jsp?root_code%3D"+rootCode+"%26submit_all%3D"+isSubmitAll+"%26return_type%3D"+returnValueType+"%26tree_level%3D"+treeLevel+"%26data_limit%3D"+dataLimit;
	
        global_outputArray=outputArray;//设置返回输出控件集合
       showIframeDialog("iframeDialog",venus.authority.Reference_page, treePath, 400, 600);
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

