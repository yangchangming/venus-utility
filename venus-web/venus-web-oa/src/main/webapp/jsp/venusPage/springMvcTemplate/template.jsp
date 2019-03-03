<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@page import="udp.template.simple.util.SimpleReferenceFilter"%>
<%@ page import="venus.frames.i18n.util.LocaleHolder" %>
<%@ page import="venus.frames.mainframe.util.VoHelper" %>
<fmt:bundle basename="udp.template.simple.resource" prefix="udp.template.simple.">

<title><fmt:message key="QueryTemplate"/> </title>

<script language="javascript" src="<venus:base/>/js/jquery/plugin/PrintArea-2.1.js"></script>
<script language="javascript" src="<venus:base/>/js/jquery/plugin/jquery.form.js"></script>

<script>
        
	function simpleQuery_onClick(){  //简单的模糊查询
        form.action = "<venus:base/>/template/index";
    	form.submit();
  	}

    function findCheckbox_onClick() {  //从多选框到修改页面
		var elementCheckbox = document.getElementsByName("checkbox_template");
		var number = 0;
		var ids = null;
		for(var i=0;i<elementCheckbox.length;i++){
			if(elementCheckbox[i].checked) {
				number += 1;
				ids = elementCheckbox[i].value;
			}
		}
		if(number == 0) {
	  		alert('<fmt:message key="SelectRecord"/>')
	  		return;
		}
		if(number > 1) {
	  		alert('<fmt:message key="OnlyCanARecord"/>')
	  		return;
		}
		form.action = "<venus:base/>/template/" + ids;
        <%--form.action = "<venus:base/>/page/"--%>
		form.submit();
	}

	function detail_onClick(){
        var elementCheckbox = document.getElementsByName("checkbox_template");
        var number = 0;
        var ids = null;
        for(var i=0;i<elementCheckbox.length;i++){
            if(elementCheckbox[i].checked) {
                number += 1;
                ids = elementCheckbox[i].value;
            }
        }
        if(number == 0) {
            alert('<fmt:message key="SelectRecord"/>')
            return;
        }
        if(number > 1) {
            alert('<fmt:message key="OnlyCanARecord"/>')
            return;
        }
        form.action = "<venus:base/>/template/detail/" + ids;
        form.submit();
	}

	function deleteMulti_onClick(){  //从多选框物理删除多条记录
 		var elementCheckbox = document.getElementsByName("checkbox_template");
		var number = 0;
		var ids = "";
		for(var i=0;i<elementCheckbox.length;i++){
			if(elementCheckbox[i].checked) {
				number += 1;
				ids += "'" + elementCheckbox[i].value + "',";
			}
		}
		if(ids.length>0) {
			ids = ids.substr(0,ids.length-1);	
		}
		if(number == 0) {
	  		alert('<fmt:message key="SelectRecord"/>')
	  		return;
		}
		if(confirm('<fmt:message key="SureToDelete"/>')) {
	    	form.action="<venus:base/>/template/delete?id=" + ids;
    		form.submit();
		}
	}

    function toAdd_onClick() {  //到增加记录页面
		window.location="<venus:base/>/jsp/venusPage/springMvcTemplate/insertRoom.jsp";
	}

	function refresh_onClick(){  //刷新本页
		form.submit();
	}
    function clearForm(){
        jQuery('#form').clearForm();
        jQuery("#rubber_id").val("");
    }
	/*
	function buildQueryCondition() {
		var queryCondition = "";
		var qca = new Array();
		pushCondition(qca, "name");
		pushCondition(qca, "build_Id");
		
		if(qca.length >= 1) {  //组装查询条件
			queryCondition += " " + qca[0] + " ";
		}
		for(var i=1; i<qca.length; i++) {
			queryCondition += " and " + qca[i] + " ";
		}
		return queryCondition;
	}
	*/

    function getBuildList(event) {
        showIframeDialog("iframeDialog","<fmt:message key="BuildingName"/>", "<venus:base/>/template/index/build", 450, 260);
    }

	function printTable(){
	   jQuery("#ccChild1").printArea({mode:"popup",popClose:true});
	}
</script>

</head>
<body>
<script language="javascript">
	writeTableTop('<fmt:message key="query_with_condition" bundle="${applicationResources}"/>','<venus:base/>/themes/<venus:theme/>/');
</script>

<form id="form" name="form" method="post" action="<venus:base/>/template/index">
<%--<input type="hidden" name="cmd" value="simpleQuery">--%>

<!-- 查询开始 -->
<div id="ccParent0"> 
	<table class="table_div_control">
		<tr> 
			<td>
				<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" onClick="javascript:hideshow('ccChild0',this,'<venus:base/>/themes/<venus:theme/>/')">
				<fmt:message key="query_with_condition" bundle="${applicationResources}"/>
			</td>
		</tr>
	</table>
</div>

<div id="ccChild0"> 
<table class="table_div_content">
	<tr>
		<td align="right" width="10%" nowrap><fmt:message key="BuildingName"/></td>
		<td>
		<input  name="build_name" type="text" class="text_field_reference" inputName='<fmt:message key="BuildingName"/>' validate="isSearch" readOnly="true" rubber_id="build_Id">
		<img src="<venus:base/>/themes/<venus:theme/>/images/icon/reference.gif" onClick="javascript:getBuildList(event);" class="img_1">
			<input type="hidden" name="build_Id" id="rubber_id" value="">
		</td>
	</tr>
	<tr>
		<td align="right" width="10%" nowrap><fmt:message key="RoomName"/></td>
		<td><input name="name" type="text" class="text_field" inputName='<fmt:message key="RoomName"/>' validate="isSearch">
		       <input type="button" name="Submit" class="button_ellipse" value='<fmt:message key="query" bundle="${applicationResources}"/>' onClickTo="javascript:simpleQuery_onClick();">
		       <input type="button" name="clear" class="button_ellipse" value='<fmt:message key="clear" bundle="${applicationResources}" />' onclick="clearForm()">
		</td>
	</tr>
</table>

</div>

<div id="ccParent1"> 
	<table class="table_div_control">
		<tr > 
			<td>
				<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key="DetailTable"/>
			</td>
			<td> 
				<table align="right">
					<tr> 
						<td class="button_ellipse" onClick="javascript:toAdd_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/add.gif" class="div_control_image"><fmt:message key="add" bundle="${applicationResources}"/></td>
						<td class="button_ellipse" onClick="javascript:deleteMulti_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/delete.gif" class="div_control_image"><fmt:message key="delete" bundle="${applicationResources}"/></td>
						<td class="button_ellipse" onClick="javascript:findCheckbox_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/modify.gif" class="div_control_image"><fmt:message key="modify" bundle="${applicationResources}"/></td>
						<td class="button_ellipse" onClick="javascript:detail_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/search.gif" class="div_control_image"><fmt:message key="view" bundle="${applicationResources}"/></td>
				    </tr>
				</table>
			</td>
		</tr>
	</table>
</div>


	<table class="table_div_content">
		<tr>
			<td>
			<div id="ccChild1"> 
			<layout:collection name="wy" id="wy1" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >
				<layout:collectionItem  width="5%" title="<input type='checkbox' pdType='control' control='checkbox_template'/>" style="text-align:center;" >
					<bean:define id="wy3" name="wy1" property="id"/>
						<input type="checkbox" name="checkbox_template" value="<%=wy3%>"/>
				</layout:collectionItem>
				<layout:collectionItem width="5%"  title='<%=LocaleHolder.getMessage("sequence")%>' style="text-align:center;">
					<venus:sequence/>
				</layout:collectionItem>
				<layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.template.simple.BuildingName")%>' property="build_Id" sortable="true" >
					<bean:define id="wy6" name="wy1" property="build_Id"/>
						<%=SimpleReferenceFilter.get(String.valueOf(wy6))%>
				</layout:collectionItem>
				<layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.template.simple.RoomName")%>' property="name" sortable="true"/>
				<layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.template.simple.BuildingArea")%>'  property="area" sortable="true"/>
				<layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.template.simple.RoomType")%>' property="type" sortable="true">
					<bean:define id="wy2" name="wy1" property="type"/>
						<%=SimpleReferenceFilter.get("RoomType",String.valueOf(wy2))%>
				</layout:collectionItem>
				<layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.template.simple.RoomRate")%>' property="price" sortable="true"/>
				<layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.template.simple.IsVacant")%>' property="is_Empty" sortable="true">
					<bean:define id="wy5" name="wy1" property="is_Empty"/>
						<%=SimpleReferenceFilter.get("RoomIsEmpty",String.valueOf(wy5))%>
				</layout:collectionItem>
				<layout:collectionItem width="12%" title='<%=LocaleHolder.getMessage("udp.template.simple.CheckinDate")%>' property="residing_Date" sortable="true">
					<logic:notEmpty name="wy1" property="residing_Date">
						<bean:define id="wy7" name="wy1" property="residing_Date"/>
								<%
									java.text.SimpleDateFormat  format = new java.text.SimpleDateFormat("yyyy-MM-dd");
									out.print(format.format(format.parse(String.valueOf(wy7))));
								%>
						</logic:notEmpty>
				</layout:collectionItem>
				<layout:collectionItem width="48%" title='<%=LocaleHolder.getMessage("udp.template.simple.Remark")%>' property="brief"/>
			</layout:collection>
			</div>
			<jsp:include page="/jsp/include/page.jsp" />
			</td>
		</tr>
	</table>


</form>
<!-- 参照显示层 -->
<div id="iframeDialog" style="display:none">

</div>

</fmt:bundle>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>
</body>
</html>
<%  //表单回写
	if(request.getAttribute("writeBackFormValues") != null) {
		out.print("<script language=\"javascript\">\n");
		out.print(VoHelper.writeBackMapToForm((java.util.Map)request.getAttribute("writeBackFormValues")));
		out.print("writeBackMapToForm();\n");
		out.print("</script>");
	}
%>