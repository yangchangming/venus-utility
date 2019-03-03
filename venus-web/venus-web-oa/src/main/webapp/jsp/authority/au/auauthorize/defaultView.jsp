<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>default</title>
</head>
<body>
	<br>
	<br>
	<table class="table_noFrame" width="96%" align="center"> 
        <tr> 
			<td align="left"><img src="<%=request.getContextPath()%>/images/au/yq_bt<au:i18next/>.jpg" width="73" height="14"> <br> <br>
				<fmt:message key='venus.authority.Please_click_on_the_left_tree_select_the_node_functions_of_the_function_you_want_to_view_the_node_' bundle='${applicationAuResources}' /> <br> <br>
				<fmt:message key='venus.authority.Then_visitors_can_view_the_feature_node_in_the_organizational_structure_of_the_data_permission_' bundle='${applicationAuResources}' /> 
			</td> 
		</tr> 
    </table>
</body>
</html>
	

