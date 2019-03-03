<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/jsp/include/global.jsp" %>
<jsp:include page="include/globalDeepTree.jsp?nodeRelationType=noRelation" />
<script language="javascript">		
	//定义全局路径
	var treeImagePath = "image";  //树图标路径
	var xslPath = "deeptree_xsl.jsp?inputType=" + inputType + "&treeImagePath=" + treeImagePath;  //xslt文件相对路径
	var defaultXmlUrlPath = "deeptree_data.jsp?rootnode=";  //xmlSourceType为nodeCode时有效, 默认xml主路径, 转义影射：&-->%26, =-->%3D
	var defaultNodeCodeGetCurrent = "&getcurrent=1";  //xmlSourceType为nodeCode时有效, 默认取节点本身数据参数标识：&-->%26, =-->%3D
	
	var nameStrList = "";//选中的节点名称列表
	var codeStrList = "";//选中的节点id、party_id或code列表
	var typeStrList = "";//选中的节点团体类型列表
	
	//根据需要可以重写的方法
	function toDoMouseClick(thisEvent) {}
	function toDoMouseDbClick(thisEvent) {
		//var thisHiddenValue = getHiddenValueByEvent(thisEvent.srcElement.id);
		//alert("事件对象的id是" + thisEvent.srcElement.id + "\n隐藏值的代码是\n" + thisHiddenValue.outerHTML);
	}
	function toDoMouseOver(thisEvent) {}
	function toDoMouseOut(thisEvent) {}
		
	function returnValueName() {  //获得所有选择的checkbox
		nameStrList = "";//清空名称列表
		codeStrList = "";//清空编号列表
		typeStrList = "";//清空类型列表
		
		currentId = "";
		var checkedArray = new Array(0);		
		var submitStringArray = new Array(0);
		var submitObjectArray = new Array(0);
				
		var obj = null;
		if(inputType == "checkbox") {
			obj = window.document.getElementsByName(prefixCheckbox);
			for(i=0;i<obj.length;i++){
				if(obj[i].checked){
					checkedArray.push(obj[i].id.substring(prefixCheckbox.length));
				}
			}
			if(submitType == "parentPriority") {
				submitStringArray = clearChild(checkedArray);  //全面扫描checkedArray,把其中冗余的字节点信息去掉,Id列表放入submitStringArray
			} else {
				submitStringArray = filterHidden(checkedArray);
			}
		} else if(inputType == "radio") {
			obj = window.document.getElementsByName(prefixRadio);
			for(i=0;i<obj.length;i++){
				if(obj[i].checked){
					checkedArray.push(obj[i].id.substring(prefixRadio.length));
				}
			}
			submitStringArray = checkedArray;
		}
		for(var i=0; i<submitStringArray.length; i++) {  //把要提交的checkbox属性填满放入submitObjectArray
			var tempObj = new Object();
			var thisObj = getObjectById(prefixDiv + submitStringArray[i]);
			var parentObj = getParentObject(thisObj);
            var thisJQueryObj=jQuery(thisObj);
            
			tempObj["childName"] = thisJQueryObj.attr("text");
			tempObj["childId"] = thisJQueryObj.attr("returnValue");
			tempObj["partyType"] =thisJQueryObj.attr("detailedType");
			tempObj["parentName"] = parentObj["parentName"];
			tempObj["parentId"] = parentObj["parentId"];
	
			submitObjectArray[submitObjectArray.length] = tempObj;
			
			if(i==submitStringArray.length-1) {
				nameStrList += tempObj["childName"];
				codeStrList += tempObj["childId"];
				typeStrList += tempObj["partyType"];
			}else {
				nameStrList += tempObj["childName"] + ",";
				codeStrList += tempObj["childId"] + ",";
				typeStrList += tempObj["partyType"] + ",";
			}
		}
	}	
	

</script>
<script type="text/javascript" src="<venus:base/>/jsp/authority/tree/crossbrowser/deeptree.js">
</script>
</head>
<body onLoad="doOnLoad()" topmargin=0 leftmargin=0 ><!-- background="image/txl_tree_bg.jpg"  -->
<form name="form" method="post">
<table border="0" width="100%" cellspacing="0" cellpadding="0">

  <tr> 
     <td width="*" valign="top" align="left"> 
		<!--树开始-->    
		<div name="deeptree" id="deeptree" class="deeptree"></div>
		<!--树结束-->
    </td>
  </tr>
</table>

</form>
</body>
</html>

