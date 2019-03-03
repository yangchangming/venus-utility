<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@page import="venus.frames.i18n.util.LocaleHolder"%>

<fmt:bundle basename="udp.searchengine.searchengine_resource" prefix="udp.searchengine.">
<script language="javaScript1.2">

	//跳转到新增页面
	function onAdd() {
		location.href="<venus:base/>/TestAction.do?cmd=onAdd";
	}
	//跳转到修改页面
	function onEditById() {
	 		var elementCheckbox = document.getElementsByName("checkbox_template");
		var number = 0;
		var ids = "";
		for(var i=0;i<elementCheckbox.length;i++){
			if(elementCheckbox[i].checked) {
				number += 1;
				ids += elementCheckbox[i].value + ",";
			}
		}
		if(number == 0) {
	  		alert("<fmt:message key='please_select_a_record'/>")
	  		return;
		}
		if(number > 1) {
	  		alert("<fmt:message key='can_only_select_one_record'/>")
	  		return;
		}
			
			form.action="<venus:base/>/TestAction.do?cmd=onEditById";
			form.submit();
	}
function onDel(){
	 		var elementCheckbox = document.getElementsByName("checkbox_template");
		var number = 0;
		var ids = "";
		for(var i=0;i<elementCheckbox.length;i++){
			if(elementCheckbox[i].checked) {
				number += 1;
				ids += elementCheckbox[i].value + ",";
			}
		}
		//if(ids.length>0) {
		//	ids = ids.substr(0,ids.length-1);	
		//	alert(ids);
		//}
		if(number == 0) {
	  		alert("<fmt:message key='please_select_a_record'/>")
	  		return;
		}
		
			//if(checkOidRadio()==false){
			//	alert("请选择一条记录");
			//	return;
			//}
			if(confirm("<fmt:message key='is_completely_remove_the_data'/>")) {
				form.action="<venus:base/>/TestAction.do?cmd=onDeleteIndex&oid="+ids;
				form.submit();
		}

}

function checkOidRadio(){
	var checkoidnum=0;
	for(i=0;i<document.all.checkbox_template.length;i++)//有一组单选钮的情况
	{
		if(document.all.checkbox_template(i).checked)
		{
			checkoidnum=checkoidnum+1; 
		}
	}
	if(checkoidnum==0){
		alert("<fmt:message key='please_select_a_record'/>");
		return false;
	}else if(checkoidnum>1){
		alert("<fmt:message key='please_select_a_record'/>");
		return false;
	}


	
}
</script>
<title><fmt:message key='list_page'/></title>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='test_list'/>",'');
</script>
	<form name="form" action = "<venus:base/>/TestAction.do?cmd=onQuery" method="post">
		<input type="hidden" name="cmd" value="">
		<div id="ccParent1">
			<table class="table_div_control">
				<tr>
					<td>
						<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='user_profile'/>
					</td>
					<td>
						<table align="right">
							<tr>	
								<td class="button_ellipse"  nowrap onClick="javascript:onAdd();" style="cursor:hand"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/add.gif" class="div_control_image"><fmt:message bundle='${applicationResources}' key='add' /></td>
								<td class="button_ellipse"  nowrap onClick="javascript:onEditById();" style="cursor:hand"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/modify.gif" class="div_control_image"><fmt:message bundle='${applicationResources}' key='modify' /></td>
								<td class="button_ellipse"  nowrap onClick="javascript:onDel();" style="cursor:hand"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/delete.gif" class="div_control_image"><fmt:message bundle='${applicationResources}' key='delete' /></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<div id="ccChild1"> 
<table class="table_div_content">
	<tr>
		<td>
			<layout:collection name="result" id="test1" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >
			<layout:collectionItem width="5%" style="text-align:center;" title="<input type='checkbox' pdType='control' control='checkbox_template'/>" >
			<bean:define id="test3" name="test1" property="id"/>
				<input type="checkbox" name="checkbox_template" value="<%=test3%>"/>
			</layout:collectionItem>

			<layout:collectionItem width="5%"  title='<%=LocaleHolder.getMessage("sequence") %>' style="text-align:center;">
				<venus:sequence/>
			</layout:collectionItem>
			<layout:collectionItem width="40%" title='<%=LocaleHolder.getMessage("udp.searchengine.item1") %>' property="neirong1"/>
			<layout:collectionItem width="40%" title='<%=LocaleHolder.getMessage("udp.searchengine.item2") %>' property="neirong2"/>
		</layout:collection>
				
		<jsp:include page="/jsp/include/page.jsp" />
		</td>
	</tr>
</table>
			</div>
		</div>
		
	</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>//');
</script>

</body>
</html>
</fmt:bundle>
