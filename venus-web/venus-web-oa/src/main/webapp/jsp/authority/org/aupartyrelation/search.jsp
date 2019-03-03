<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>

<title><fmt:message key='venus.authority.Query_Template' bundle='${applicationAuResources}' /></title>
<script>    
	function simpleQuery_onClick(){  //简单的模糊查询
		parent.document.all.detail.src="<venus:base/>/auPartyRelation/simpleQuery&name="+document.all.name.value;
  	}
	function initFocus(){ 
		var ctrl=document.getElementById("name"); 
		ctrl.focus(); 
	}  
</script>

</head>
<body onload="initFocus()">

<!-- 查询开始 -->
<table class="table_noFrame">
<tr></tr>
<tr valign="middle">
	<td align="left" width="20%" nowrap><fmt:message key='venus.authority.The_name_of_the_fuzzy_query' bundle='${applicationAuResources}' /></td>
	<td width="30%"><input name="name" id="name" type="text" class="text_field" validate="isSearch"></td>
	<td align="left"><input type="button" name="Submit" class="button_ellipse" value="<fmt:message key='venus.authority.Inquiry' bundle='${applicationAuResources}' />" onclick="javascript:simpleQuery_onClick();">
	</td>
</tr>
</table>
</body>
</html>

