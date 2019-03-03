<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>tree example</title>
<script language="javascript">
function relationTree(rootCode,returnType,dataLimit,urlDirection){
    var treePath = "<venus:base/>/relation/showRelationTree?rootCode="+rootCode+"&returnType="+returnType+"&dataLimit="+dataLimit+"&excWithNoAu={ISPROCESSUNIT:'1'}&excWithNoAu={id:'1099100700000000004'}&excWithYesAu={ISPROCESSUNIT:'0'}&urlDirection="+urlDirection;
    window.showModalDialog(treePath, new Object(),'dialogHeight=540px;dialogWidth=900px;resizable:yes;status:no;scroll:auto;');
}
</script>
</head>
<body> 
<table class="table_div_content" height="100%"> 
    <tr> 
        <td valign="top"> 
            <table class="table_noFrame" width="100%"> 
                <tr align="left"> 
                    <td colspan="2" align="left">
                    <fmt:message key='venus.authority.Call_method' bundle='${applicationAuResources}' />relationTree(rootCode,returnType,dataLimit,urlDirection)<fmt:message key='venus.authority.Access_to_the_organization_tree' bundle='${applicationAuResources}' /><br>
                    <fmt:message key='venus.authority.Specific_parameters_as_follows_' bundle='${applicationAuResources}' /><br>
                    &nbsp;&nbsp;&nbsp;&nbsp;rootCode<fmt:message key='venus.authority._Root_number_if_it_is_empty_then_show_all_organizations' bundle='${applicationAuResources}' /><br>
                    &nbsp;&nbsp;&nbsp;&nbsp;returnType<fmt:message key='venus.authority._Return_value_is_what_the_field_you_can_choose' bundle='${applicationAuResources}' />aupartyrelation<fmt:message key='venus.authority.Table' bundle='${applicationAuResources}' />id<fmt:message key='venus.authority._4' bundle='${applicationAuResources}' />party_id<fmt:message key='venus.authority.And' bundle='${applicationAuResources}' />code<fmt:message key='venus.authority.One_of_the_three_the_default_is' bundle='${applicationAuResources}' />code<br>
                    &nbsp;&nbsp;&nbsp;&nbsp;dataLimit<fmt:message key='venus.authority._Whether_the_control_data_permissions' bundle='${applicationAuResources}' />0 <fmt:message key='venus.authority.No_' bundle='${applicationAuResources}' />1 <fmt:message key='venus.authority.Is_by_default' bundle='${applicationAuResources}' />0<br>
                    &nbsp;&nbsp;&nbsp;&nbsp;urlDirection<fmt:message key='venus.authority._relation_urldirection' bundle='${applicationAuResources}' /><br><br>
                    <fmt:message key='venus.authority.The_following_example_shows_how_to_call' bundle='${applicationAuResources}' />:
                    <br>
                    </td> 
                </tr>
                <tr>
                    <td colspan="2">
                        <br/>
                    </td> 
                </tr> 
                <tr>
                    <td colspan="2">
                        <br/>
                    </td> 
                </tr> 
                <tr>
                    <td colspan="2">
                        <button type="button" onclick="javascript:relationTree('1099100400000000004','party_id','1','<venus:base/>/relation/detail%26a%3Db')">
                        <fmt:message key='venus.authority.Relation_Custom' bundle='${applicationAuResources}' />
                        </button>
                    </td> 
                </tr> 
            </table> 
        </td> 
    </tr> 
</table> 
</body>
</html>

