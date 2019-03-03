<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/jsp/include/global.jsp" %>
<%
	String adjustStatus = (String)request.getAttribute("adjustStatus");
	String failCause = (String)request.getAttribute("failCause");
%>
<jsp:include page="include/globalDeepTree.jsp?returnTextType=plugParent" />
<title>deeptree</title>
<script language="javascript">	
	//定义全局路径
	var treeImagePath = "image";  //树图标路径
	var xslPath = "deeptree_xsl.jsp?inputType=" + inputType + "&treeImagePath=" + treeImagePath;  //xslt文件相对路径
	var defaultXmlUrlPath = "deeptree_data.jsp?rootnode=";  //xmlSourceType为nodeCode时有效, 默认xml主路径, 转义影射：&-->%26, =-->%3D
	var defaultNodeCodeGetCurrent = "&getcurrent=1";  //xmlSourceType为nodeCode时有效, 默认取节点本身数据参数标识：&-->%26, =-->%3D

	//根据需要可以重写的方法
	function toDoMouseClick(thisEvent) {
	}
	function toDoMouseDbClick(thisEvent) {
		//var thisHiddenValue = getHiddenValueByEvent(thisEvent.srcElement.id);
		//alert("事件对象的id是" + thisEvent.srcElement.id + "\n隐藏值的代码是\n" + thisHiddenValue.outerHTML);
	}
	function toDoMouseOver(thisEvent) {
	}
	function toDoMouseOut(thisEvent) {
	}
	
/**********************　开始表单提交相关方法 **********************/

	function changeRelation(srcrelationid) {  //获得所有选择的checkbox
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
			var jQueryObj = jQuery(thisObj);
	
			tempObj["childName"] = jQueryObj.attr("text");
			tempObj["childId"] = jQueryObj.attr("returnValue");
			tempObj["returnValue"] = jQueryObj.attr("returnValue");
			tempObj["parentName"] = parentObj["parentName"];
			tempObj["parentId"] = parentObj["parentId"];
			tempObj["detailedType"] = jQueryObj.attr("detailedType"); 
			
			submitObjectArray[submitObjectArray.length] = tempObj;
		}
		
		if(submitObjectArray.length == 0) {
			alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!");
			return false;
		}
		var newNodeCode = submitObjectArray[0]["returnValue"];
		document.form.action = "<%=request.getContextPath()%>/adjustOrganize/updateRelation?srcrelationid="+srcrelationid+"&newNodeCode="+newNodeCode;
		document.form.submit();
		
	}
	
	function transactionMessage() { //返回事务操作的类型
  		parent.hideBlockPage();
  		var adjustStatus = "<%=adjustStatus%>";
  		if (adjustStatus == "1") {
  			alert("<fmt:message key='venus.authority.Transfer_level_success' bundle='${applicationAuResources}' />!");
  		window.parent.parent.parent.location.href = "relation/showTree";
		window.parent.parent.jQuery("#iframeDialog").dialog("close");
  			
  		} 
  		if (adjustStatus == "0") {
  			var failCause = "<%=failCause%>";
  			alert("<fmt:message key='venus.authority.Transfer_level_failure' bundle='${applicationAuResources}' />!<fmt:message key='venus.authority.The_reason_is' bundle='${applicationAuResources}' />:" + failCause);
  			window.parent.parent.jQuery("#iframeDialog").dialog("close");
  		}
  		if (adjustStatus == "-1") {
  			alert("<fmt:message key='venus.authority.Transfer_level_failure' bundle='${applicationAuResources}' />!");	
  			window.parent.parent.jQuery("#iframeDialog").dialog("close");	
  		}  		
  	}

/**********************　结束表单提交相关方法 **********************/
</script>
<script type="text/javascript" src="<venus:base/>/jsp/authority/tree/crossbrowser/deeptree.js">
</script>
</head>
<body onLoad="doOnLoad()" topmargin=0 leftmargin=0 >
<form name="form" method="post">
<table class="table_noFrame">
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
<script language="javascript">
<!--
transactionMessage();
//-->
</script>

