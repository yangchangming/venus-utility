<%@ page contentType="text/html; charset=UTF-8" %>

<%@ include file="/jsp/include/global.jsp" %>

<fmt:bundle basename="udp.ewp.ewp_resource" prefix="udp.ewp.">
<title><fmt:message key="RefTemplate" bundle="${applicationResources}"/></title>

<script>
    function returnvalue() {  
      var id = jQuery('input[name=checkbox_template]:checked').val();
       var chinesename = jQuery('input[name=checkbox_template]:checked').attr("chinesename");
        if(id == null) {
            alert('<fmt:message key="select_one_record"/>')
            return;
        }
       var idnames = '{id:"'+id+'",chinesename:"'+chinesename+'"}';
       parent.window.returnValue = idnames;
       parent.window.close();
    }  
</script>
<base target="_self">
</head>
<body>
<script language="javascript">
    writeTableTop('<fmt:message key="reference_page" bundle="${applicationResources}"/>','<venus:base/>/themes/<venus:theme/>/');
</script>

<form name="form" method="post" action="<venus:base/>/TemplateTypeAction.do">
<input type="hidden" name="cmd" value="">
<table>
    <tr>
        <td>
            <input type="button" name="Submit" value='<fmt:message key="confirm"  bundle="${applicationResources}"/>' class="button_ellipse" onClick="javascript:returnvalue();">
            <input type="button" name="cancel" value='<fmt:message key="cancel"  bundle="${applicationResources}"/>' class="button_ellipse" onClick="javascript:parent.window.close();">
        </td>
    </tr>
</table>
<table>
    <tr>
        <td>
        <layout:collection name="beans" id="template" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0">
            <layout:collectionItem width="2%" title="" style="text-align:center;">
                <input type="radio" name="checkbox_template" value="${pageScope.template.id}" chinesename="${pageScope.template.typeName}" />
            </layout:collectionItem>
            <layout:collectionItem width="12%" title='<%=LocaleHolder.getMessage("sequence") %>' style="text-align:center;">
                <venus:sequence />
            </layout:collectionItem>
            <layout:collectionItem width="40%" title='<%=LocaleHolder.getMessage("udp.ewp.template_type_name") %>'  property="typeName" sortable="false" />
            <layout:collectionItem width="40%" title='<%=LocaleHolder.getMessage("udp.ewp.template_type_description") %>'  property="description" sortable="false" />
       </layout:collection>
        </td>
    </tr>
</table>

</form>
<script language="javascript">
    writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>
</fmt:bundle>
</body>

<%@page import="venus.frames.i18n.util.LocaleHolder"%>
</html>