<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import = "venus.authority.util.VoHelperTools" %>
<%@ page import = "venus.authority.util.StringHelperTools" %>
<%
	String parentCode = (String)request.getAttribute("parent_code");
%>
<title><fmt:message key='venus.authority.Query_Template' bundle='${applicationAuResources}' /></title>
<script>
	var staticRelationTypeId='';
	function view_onClick(id,relaid){
		document.form2.id.value=id;
		staticRelationTypeId=relaid;
		document.frames[0].location.href="<venus:base/>/jsp/authority/tree/deeptree_iframe.jsp?inputType=radio&rootXmlSource=<venus:base/>/jsp/authority/tree/partyDetailTreeRoot.jsp?submit_all%3Dno%26return_type%3Did%26id%3D"+id;
	}
	function view_onButton(){
	    var elementCheckbox = document.getElementsByName("checkbox_template");
		var number = 0;
		var id = null;
		var relaid=null;
		for(var i=0;i<elementCheckbox.length;i++){
			if(elementCheckbox[i].checked) {
				number += 1;
				id = elementCheckbox[i].value;
				relaid=document.getElementsByName('relaid')[i].value;
			}
		}
		if(number == 0) {
	  		alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		if(number > 1) {
	  		alert("<fmt:message key='venus.authority.Can_only_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		view_onClick(id,relaid);
	}


<!--
	function delete_onClick(){  //删除记录
		myTree.returnValueName();//获取树选中的值，记录到codeStrList里
		var ids = myTree.codeStrList;
		var typeId = myTree.typeStrList;
		if(ids=="") {
	  		alert("<fmt:message key='venus.authority.Please_select_the_records_you_want_to_delete' bundle='${applicationAuResources}' />!")
	  		return;
		}
		if(typeId=="0") {
			alert("<fmt:message key='venus.authority.Not_a_leaf_node_the_node_is_not_allowed_to_delete_' bundle='${applicationAuResources}' />")
	  		return;
		}
		if(confirm("<fmt:message key='venus.authority.It_completely_remove_the_data_' bundle='${applicationAuResources}' />")) {
	    	form.action="<venus:base/>/auPartyRelation/delete?relId="+ids+"&partyrelationtype_id="+staticRelationTypeId;
    		form.submit();
		}
	}
	function toAdd_onClick() {  //到增加记录页面
		myTree.returnValueName();//获取树选中的值，记录到codeStrList里
		var ids = myTree.codeStrList;
		if(ids=="") {
	  		alert("<fmt:message key='venus.authority.Please_choose_which_node_s_parent_node' bundle='${applicationAuResources}' />!")
	  		return;
		}
		var refPath = "<venus:base/>/jsp/authority/org/auparty/mainFrame.jsp?pageFlag=ref";
		var rtObj = window.showModalDialog(refPath,new Object(),'dialogHeight=600px;dialogWidth=800px;resizable:yes;status:no;scroll:auto;');
		if(rtObj != undefined){
    		var partyIds = "";
			for(var i=0; i<rtObj.length-1; i++) {
				partyIds += rtObj[i] + ",";
			}
			partyIds += rtObj[rtObj.length-1];
			form.action="<venus:base/>/auPartyRelation/insertMulti?ids=" + partyIds + "&parentRelId=" + ids + "&relTypeId="+staticRelationTypeId;
    		form.submit();
		}
	}
	function refresh_onClick(){  //刷新本页
		document.frames[0].location.href="<venus:base/>/jsp/authority/tree/deeptree_iframe.jsp?inputType=radio&rootXmlSource=<venus:base/>/jsp/authority/tree/partyDetailTreeRoot.jsp?submit_all%3Dno%26return_type%3Did%26id%3D"+document.form2.id.value;
	}
	
  function getRowHiddenRelaId() {  //从事件取得本行的id
	   var thisA = getEventObj(event);  //定义事件来源对象
	   var thisTr = jQuery(thisA).parent();  //定义本行的tr对象
	   var thisHidden = thisTr.find("input[signName='hiddenRelaId']");  //从thisTr递归的取出name是hiddenId的对象
	   
	   if(thisHidden != undefined && thisHidden != null) {  //如果thisHidden不为空
	       return thisHidden.val();
	   } else {
	       return null;
	   }
  }
//-->
</script>

</head>
<body>
<%--<form name="form" method="post" action="/authority/AuPartyRelationAction.do">--%>
	<form name="form" method="post" action="/auPartyRelation">
	<input type="hidden" name="cmd" value="">
	<input type="hidden" name="name" value="">
<div id="auDivParent1"> 
<table class="table_div_control">
	<tr > 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='venus.authority.Query_Results' bundle='${applicationAuResources}' />
		</td>
		<td> 
			<table align="right">
				<tr> 
					<td class="button_ellipse" onClick="javascript:view_onButton();"><img src="<venus:base/>/images/icon/modify.gif" class="div_control_image"><fmt:message key='venus.authority.Tree_View' bundle='${applicationAuResources}' /></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>

<div id="auDivChild1"> 
<table class="table_div_content">
	<tr>
		<td>
		<layout:collection onRowDblClick="detail_onClick(getRowHiddenId();getRowHiddenRelaId())" name="wy" id="wy1" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >
			<layout:collectionItem width="40" title="" style="text-align:center;">
				<bean:define id="wy3" name="wy1" property="id"/>
				<bean:define id="wy4" name="wy1" property="relationtype_id"/>
					<input type="radio"  name="checkbox_template" value="<%=wy3%>"/>
					<input type="hidden" signName="hiddenId" value="<%=wy3%>"/>
					<input type="hidden" name="relaid" signName="hiddenRelaId" value="<%=wy4%>"/>
			</layout:collectionItem>
			<layout:collectionItem width="20" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sequence") %>' style="text-align:center;">
				<venus:sequence/>
			</layout:collectionItem>
			<layout:collectionItem width="130" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Name") %>' property="name" />	
			<layout:collectionItem width="130" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Group_type_of_relationship") %>' property="relationtype_name" />	
			<layout:collectionItem title="Email"  property = "email"/>
		</layout:collection>
		
		<jsp:include page="/jsp/include/page.jsp" />
		</td>
	</tr>
</table>
</div>
</form>
</div>   
<form name="form2" method="post" action="">
<input type="hidden" name="id" value="">
<div id="auDivParent2"> 
<table class="table_div_control">
	<tr > 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild2',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='venus.authority.Group_Tree' bundle='${applicationAuResources}' />
		</td>
		<td> 
			<table align="right">
				<tr> 
					<td class="button_ellipse" onClick="javascript:toAdd_onClick();"><img src="<venus:base/>/images/icon/add.gif" class="div_control_image"><fmt:message key='venus.authority.Added' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" onClick="javascript:delete_onClick();"><img src="<venus:base/>/images/icon/delete.gif" class="div_control_image"><fmt:message key='venus.authority.Delete' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" onClick="javascript:refresh_onClick();"><img src="<venus:base/>/images/icon/refresh.gif" class="div_control_image"><fmt:message key='venus.authority.Refresh' bundle='${applicationAuResources}' /></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>
<div id="auDivChild2"> 
<table class="table_div_content">
	<tr>
		<td>
			<iframe name="myTree" width="100%" height="200" src="">
			</iframe>
		</td>
	</tr>
</table>
</div>
</form>
</body>
</html>
<%  //表单回写
	if(request.getAttribute("writeBackFormValues") != null) {
		out.print("<script language=\"javascript\">\n");
		out.print(VoHelperTools.writeBackMapToForm((java.util.Map)request.getAttribute("writeBackFormValues")));
		out.print("writeBackMapToForm();\n");
		out.print("</script>");
	}
%>

