<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>default</title>
<link rel='stylesheet' type='text/css' href='<venus:base/>/css/jquery/dialog/jquery-dialog.css' />
<script type='text/javascript' src='<venus:base/>/js/au/jquery/jquery.js'></script>
<script type='text/javascript' src='<venus:base/>/js/au/jquery/dialog/jquery-impromptu.3.1.min.js'></script>
<script>
function initializationI18nMenu(){
    var treePath = "<venus:base/>/jsp/authority/i18n/chooseLanguageCountry.jsp";                
    window.showModalDialog(treePath, new Object(),'dialogHeight=350px;dialogWidth=640px;resizable:yes;status:no;scroll:auto;');
        
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
				<fmt:message key='venus.authority.If_you_add_modify_or_delete_function_node_the_function_of_the_node_tree_is_not_up_to_date_please' bundle='${applicationAuResources}' />IE<fmt:message key='venus.authority.Menu' bundle='${applicationAuResources}' />"<fmt:message key='venus.authority.Tool' bundle='${applicationAuResources}' />"->"Internet<fmt:message key='venus.authority.Options' bundle='${applicationAuResources}' />"->"<fmt:message key='venus.authority.Conventional' bundle='${applicationAuResources}' />"->"<fmt:message key='venus.authority.Set_up' bundle='${applicationAuResources}' />"<fmt:message key='venus.authority.Select' bundle='${applicationAuResources}' />"<fmt:message key='venus.authority.Every_visit_to_the_page' bundle='${applicationAuResources}' />"<fmt:message key='venus.authority.Can_' bundle='${applicationAuResources}' /> 
			</td> 
		</tr> 
    </table>
    <br>
    <br>
    <br>
    <table class="table_noFrame" width="96%" align="center"> 
        <tr> 
            <td align="center">
            <%--<input type="button" name="i18nchange" class="button_ellipse" value="<fmt:message key='venus.authority.functreelang' bundle='${applicationAuResources}' />" onclick="javascript:initializationI18nMenu()"> --%>
            </td> 
        </tr> 
    </table>
</body>
</html>
	

