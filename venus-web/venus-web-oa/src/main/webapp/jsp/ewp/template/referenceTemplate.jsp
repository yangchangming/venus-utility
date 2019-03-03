<%@ page contentType="text/html; charset=UTF-8" %>
<%@page import="venus.frames.i18n.util.LocaleHolder"%>
<%@ include file="/jsp/include/global.jsp" %>
<%
 String selectedValue = request.getParameter("selectedValue");
 request.setAttribute("selectedValue",selectedValue);
%>
<fmt:bundle basename="udp.ewp.ewp_resource" >
<title><fmt:message key="udp.ewp.template.reftemplate"/></title>

<script>
    function returnvalue() {  
      var id = jQuery('input[name=checkbox_template]:checked').val();
       var chinesename = jQuery('input[name=checkbox_template]:checked').attr("chinesename");
        if(id == null) {
            alert('<fmt:message key="udp.ewp.select_one_record"/>')
            return;
        }
       var idnames = '{id:"'+id+'",chinesename:"'+chinesename+'"}';
      return idnames;
    }  
</script>
<base target="_self">
</head>
<body style="background-color:#c8cdd3">
<input type="hidden" name="cmd" value="">
<table bgcolor="#c8cdd3" width="100%">
    <tr>
        <td>
        <layout:collection name="beans" id="template" styleClass="listCss" width="100%" height="100%" indexId="orderNumber" align="center" sortAction="0">
            <layout:collectionItem width="2%" title="" style="text-align:center;">
                <c:if test="${selectedValue eq pageScope.template.id}"><input type="radio" name="checkbox_template" value="${pageScope.template.id}" chinesename="${pageScope.template.template_name}" checked/></c:if>
                <c:if test="${selectedValue ne pageScope.template.id}"><input type="radio" name="checkbox_template" value="${pageScope.template.id}" chinesename="${pageScope.template.template_name}" /></c:if>
            </layout:collectionItem>
            <layout:collectionItem width="12%" title='<%=LocaleHolder.getMessage("sequence") %>' style="text-align:center;">
                <venus:sequence />
            </layout:collectionItem>
            <layout:collectionItem width="40%" title='<%=LocaleHolder.getMessage("udp.ewp.template_name") %>' property="template_name" sortable="false" />
            <layout:collectionItem width="40%" title='<%=LocaleHolder.getMessage("udp.ewp.template_viewname") %>' property="template_viewname" sortable="false" />
       </layout:collection>
        </td>
    </tr>
</table>
</fmt:bundle>
</body>
</html>