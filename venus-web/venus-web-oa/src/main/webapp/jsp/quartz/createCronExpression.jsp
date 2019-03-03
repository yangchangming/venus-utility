<%@ page contentType="text/html; charset=UTF-8" %>

<%@ include file="/jsp/include/global.jsp" %>
<fmt:bundle basename="udp.quartz.quartz_resource" prefix="udp.quartz.">
<title><fmt:message key="Create_Advanced_Triggering_Rules"/></title>

<script>

	function returnvalue() {  //从多选框到修改页面
		var ids = null;
		if(document.form1.day.value!=""&&document.form1.week.value!=""){
			alert('<fmt:message key="Weeks_And_Days_Can_Not_Be_Together"/>');
			return "* * * * * ?";
		}
		if(document.form1.minute.value==""){
			document.form1.minute.value ="*";
		}
		if(document.form1.hour.value==""){
			document.form1.hour.value ="*";
		}
		if(document.form1.day.value==""&&document.form1.week.value==""){
			document.form1.day.value ="?";
			document.form1.week.value ="*";
		}else if(document.form1.day.value==""){
			document.form1.day.value ="?";
		}else if(document.form1.week.value==""){
			document.form1.week.value ="?";
		}
		if(document.form1.month.value==""){
			document.form1.month.value ="*";
		}
		
		//alert(" 月="+document.form1.month.value+" 日="+document.form1.day.value+" 周="+document.form1.week.value+" 时="+document.form1.hour.value+" 分="+document.form1.minute.value);

		var returnArray = "0 "+document.form1.minute.value+" "+document.form1.hour.value+" "+document.form1.day.value+" "+document.form1.month.value+" "+document.form1.week.value;
		//var returnArray = "* * * * * *";
		
        window.parent.document.form1.cronExpression.value = returnArray;
        window.parent.jQuery("#iframeDialog").dialog("close");
	}
	
	function checkAllList_onClick(thisObj){  //全选，全不选
		var elementCheckbox = document.getElementsByName("checkbox_template");
		for(var i=0;i<elementCheckbox.length;i++){
			elementCheckbox[i].checked = thisObj.checked;
		}
	}
	
    //关闭层
    function cancel_onClick() {
        window.parent.jQuery("#iframeDialog").dialog("close");
    }
	
	 function isDigit(patrn,s)  
	 {  
		// var patrn=/^[1-5]?[0-9]$/;  
		 if (s=="") {
		 	return ""  
		 }
		 if (!patrn.exec(s)) {
		 	alert('<fmt:message key="EnterValidCharacter"/>');
		 	return ""  
		 }
		 return s  
	 } 
</script>
<base target="_self">
</head>
<body>
<script language="javascript">
	writeTableTop('<fmt:message key="Reference_List"/>','<venus:base/>/themes/<venus:theme/>/');
</script>

<form name="form1" method="post">
<input type="hidden" name="queryCondition" value="">
<div id="ccParentq">
<table class="table_div_control">
	<tr>
		<td>
			<input type="button" name="Submit" value="<fmt:message key="Confirm"/>" class="button_ellipse" onClick="javascript:returnvalue();">
			<input type="button" name="cancel" value="<fmt:message key="Cancel"/>" class="button_ellipse" onClick="javascript:cancel_onClick();">
		</td>
	</tr>
</table>
</div>

<div id="ccChildq">
<table class="table_div_content">
	<tr>
		<td  width="50"><fmt:message key="Minute"/></td>
		<td  width="210"><input type="text" class="text_field" name="minute" onchange="this.value=isDigit(/^[0-5]?[0-9](\/[0-5]?[0-9])?$/,this.value)">
		<td >(<fmt:message key="Range"/>0~59)</td>
	</tr>	
	<tr>
		<td ><fmt:message key="Hour"/></td>
		<td ><input type="text" class="text_field" name="hour" onchange="this.value=isDigit(/^(([0-1]?[0-9])|(2[0-3]))(\/(([0-1]?[0-9])|(2[0-3])))?$/,this.value)">
		<td >(<fmt:message key="Range"/>0~23)</td>
	</tr>
	<tr>
		<td ><fmt:message key="Day"/></td>
		<td ><input type="text" class="text_field" name="day" onchange="this.value=isDigit(/^(([1-2]?[0-9])|(3[0-1]))(\/(([1-2]?[0-9])|(3[0-1])))?$/,this.value)">
		<td >(<fmt:message key="Range"/>1~31)</td>
	</tr>
	<tr>
		<td ><fmt:message key="Month"/></td>
		<td ><input type="text" class="text_field" name="month" onchange="this.value=isDigit(/^([1-9]|(1[0-2]))(\/([1-9]|(1[0-2])))?$/,this.value)">
		<td >(<fmt:message key="Range"/>1~12 )</td>
	</tr>
	<tr>
		<td ><fmt:message key="Week"/></td>
		<td ><input type="text" class="text_field" name="week" onchange="this.value=isDigit(/^[1-7](\/[1-7])?$/,this.value)">
		<td >(<fmt:message key="Range"/>1~7 )</td>
	</tr>
	<!--
	<tr>
		<td >年：</td>
		<td ><input type="text" name="year">
		<td >(范围：)</td>
	</tr>
	-->

</table>
<table >
	<tr><td><fmt:message key="Help"/></td><td></td><td></td></tr>
	<tr><td></td><td><fmt:message key="No_Condition"/></td><td></td></tr>
	<tr><td></td><td><fmt:message key="Increasing_Condition"/></td><td></td></tr>
	<tr><td></td><td><fmt:message key="Increasing_Condition_Example"/></td><td></td></tr>
	<td></td><td><fmt:message key="Not_Together_Condition"/></td><td></td></tr>

</table>
</div>
</form>
</fmt:bundle>

<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>
</body>
</html>