<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>default</title>
<script language="javascript">
	function saveOnClick() {
		alert("<fmt:message key='venus.authority.Did_not_manipulate_the_data' bundle='${applicationAuResources}' />!");
	}
</script>
</head>
<body>
	<br>
	<br>
	<table class="table_noFrame" width="96%" align="center"> 
        <tr> 
			<td align="left"><img src="<%=request.getContextPath()%>/images/au/yq_bt<au:i18next/>.jpg" width="73" height="14"> <br> <br>
				<fmt:message key='venus.authority.Please_click_on_the_left_of_the_function_of_the_node_tree_of_the_function_select_the_node_you_want_to_operate_' bundle='${applicationAuResources}' /> <br> <br>
				<fmt:message key='venus.authority.Visitors_can_then_grant_under_the_function_node_the_data_rights_organization_' bundle='${applicationAuResources}' /> 
			</td> 
		</tr> 
    </table>
</body>
</html>
	

