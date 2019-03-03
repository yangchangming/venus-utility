<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/jsp/include/global.jsp" %>
<%
 	String relId = request.getParameter("relId");
 	String pType = request.getParameter("pType");
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

	var del_array = new Array(0);//记录勾掉的id
	function addRemoveIds(id) {
		var i=0
		for( ; i<del_array.length; i++) {
			if(del_array[i]==id) {
				break;
			}
		}
		if(i==del_array.length) {
			del_array[i] = id;
		}
	}
	
	function saveOnClick() {  //获得所有选择的checkbox和取消的checkbox
		currentId = "";
		var checkedArray = new Array(0);		
		var submitStringArray = new Array(0);
		var codes = "";
		var types = "";
		var names = "";
		var del_codes = "";
		for(var i=0; i<del_array.length-1; i++) {
			del_codes += del_array[i]+",";
		}
		if(del_array.length>0) {
			del_codes += del_array[del_array.length-1];
		}
		document.form.del_codes.value=del_codes;
		
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
		for(var i=0; i<submitStringArray.length; i++) {
			var thisObj = getObjectById(prefixDiv + submitStringArray[i]);
			var jQueryObj = jQuery(thisObj);
			codes += jQueryObj.attr("returnValue") + ",";
			names += jQueryObj.attr("text") + ",";
			types += jQueryObj.attr("detailedType") + ",";
		}
		if(codes != "") {
			codes = codes.substring(0,codes.length-1);
			document.form.add_codes.value=codes;
			
			names = names.substring(0,names.length-1);
			document.form.names.value=names;
			
			types = types.substring(0,types.length-1);
			document.form.types.value=types;
		}
		form.action="<venus:base/>/auAuthorize/saveOrgAuByRelId";
		form.submit();
	}

/**********************　结束表单提交相关方法 **********************/
</script>
<script type="text/javascript" src="<venus:base/>/jsp/authority/tree/crossbrowser/deeptree.js">
</script>
</head>
<body onLoad="doOnLoad()" topmargin=0 leftmargin=0 >
<form name="form" method="post">
<input type="hidden" name="relId" value="<%=relId%>">
<input type="hidden" name="pType" value="<%=pType%>">

<input type="hidden" name="cmd" value="saveOrgAuByRelId">
<input type="hidden" name="add_codes" value="">
<input type="hidden" name="del_codes" value="">
<input type="hidden" name="names" value="">
<input type="hidden" name="types" value="">

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

