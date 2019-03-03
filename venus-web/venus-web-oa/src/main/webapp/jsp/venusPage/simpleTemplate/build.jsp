<%@ page contentType="text/html; charset=UTF-8" %>

<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="venus.frames.mainframe.util.VoHelper" %>

<fmt:bundle basename="udp.template.simple.resource" prefix="udp.template.simple.">
<title><fmt:message key="RefTemplate"/></title>

<script>
	function returnvalue() {  //从多选框到修改页面
		var elementCheckbox = jQuery(":checked[name='checkbox_template']");
		var number = elementCheckbox.size();

		if(number == 0) {
	  		alert('<fmt:message key="SelectRecord"/>')
	  		return;
		}
		if(number > 1) {
	  		alert('<fmt:message key="OnlyCanARecord"/>')
	  		return;
		}
		var returnArray = new Array();
		var ids=elementCheckbox.val();
		returnArray[0] = ids.substring(0,ids.indexOf(":"));
		returnArray[1] = ids.substring(ids.indexOf(":")+1);

        window.parent.jQuery("input[name='build_Id']").val(returnArray[0]);
        window.parent.jQuery("input[name='build_name']").val(returnArray[1]);
        window.parent.jQuery("#iframeDialog").dialog("close");
	}  
	
    function cancel_onClick() {
        window.parent.jQuery("input[name='build_Id']").val("");
        window.parent.jQuery("input[name='build_name']").val("");

        window.parent.jQuery("#iframeDialog").dialog("close");
        
        //window.parent.jQuery("#iframeDialog iframe").attr("src", "");
        //window.parent.jQuery("#iframeDialog").dialog("destroy");
    }
	
	
	function checkAllList_onClick(thisObj){  //全选，全不选
		var elementCheckbox = document.getElementsByName("checkbox_template");
		for(var i=0;i<elementCheckbox.length;i++){
			elementCheckbox[i].checked = thisObj.checked;
		}
	}
	
</script>
<base target="_self">
</head>
<body>
<script language="javascript">
	writeTableTop('<fmt:message key="RefList"/>','<venus:base/>/');
</script>

<form name="form" method="post" action="<venus:base/>/TemplateAction.do">
<input type="hidden" name="cmd" value="">
<table>
	<tr>
		<td>
			<input type="button" name="Submit" value='<fmt:message key="Confirm"/>' class="button_ellipse" onClick="javascript:returnvalue();">
			<input type="button" name="cancel" value='<fmt:message key="Cancle"/>' class="button_ellipse" onClick="javascript:cancel_onClick();">
		</td>
	</tr>
</table>
<table>
	<tr>
		<td>
		<layout:collection name="buildList" id="wy1" styleClass="listCss" width="98%" indexId="orderNumber" align="center" sortAction="0">
			<layout:collectionItem width="20" title=" " style="text-align:center;">
				<bean:define id="wy3" name="wy1" property="id"/>
				<bean:define id="wy6" name="wy1" property="name"/>
					<input type="radio" name="checkbox_template" value="<%=wy3.toString() + ":" + wy6.toString()%>"/>
			</layout:collectionItem>
			<layout:collectionItem width="20"  title='<%=LocaleHolder.getMessage("udp.template.simple.Index") %>' style="text-align:center;">
				<venus:sequence/>
			</layout:collectionItem>
			<layout:collectionItem width="180" title='<%=LocaleHolder.getMessage("udp.template.simple.BulidingID") %>' property="id" sortable="true"/>
			<layout:collectionItem width="180" title='<%=LocaleHolder.getMessage("udp.template.simple.BuildingName") %>' property="name" sortable="true"/>
		</layout:collection>
		
		<jsp:include page="/jsp/include/page.jsp" />
		</td>
	</tr>
</table>

</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/');
</script>
</body>
</fmt:bundle>
<%@page import="venus.frames.i18n.util.LocaleHolder"%>
</html>
<%  //表单回写
	if(request.getAttribute("writeBackFormValues") != null) {
		out.print("<script language=\"javascript\">\n");
		out.print(VoHelper.writeBackMapToForm((java.util.Map)request.getAttribute("writeBackFormValues")));
		out.print("writeBackMapToForm();\n");
		out.print("</script>");
	}
%>