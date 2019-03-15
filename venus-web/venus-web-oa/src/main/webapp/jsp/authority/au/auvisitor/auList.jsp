<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="venus.oa.util.StringHelperTools" %>
<%@ page import="venus.oa.util.VoHelperTools" %>
<%@ page import="java.util.List" %>
<%@ page import="venus.oa.authority.auvisitor.vo.AuVisitorVo" %>
<%@ include file="/jsp/include/global.jsp" %>
<%
    String partyTypes = request.getParameter("partyTypes");
    if( partyTypes==null || "".equals(partyTypes) ) {
    	partyTypes = (String)request.getAttribute("partyTypes");
    }
%>
<title><fmt:message key='venus.authority.Query_Template' bundle='${applicationAuResources}' /></title>
<script>
<!--
	function simpleQuery_onClick(){  //简单的模糊查询
		form.action = "<venus:base/>/auVisitor/simpleQueryByTypes";
    	form.submit();
  	}
    function returnValueName(rtObj) { 
		if(rtObj != undefined){// && rtObj.length > 0){//20100528人寿需求
            var submitObjectArray = rtObj["submitObjectArray"];
            var del_array = rtObj["del_array"];
            var parentRelIds = "";
            for(var i=0; i<submitObjectArray.length-1; i++) {
                parentRelIds += submitObjectArray[i]['returnValue'] + ",";
            }
            if(submitObjectArray.length > 0)
                parentRelIds += submitObjectArray[submitObjectArray.length-1]['returnValue'];
            
            var delRelIds = "";
            for(var i=0; i<del_array.length-1; i++) {
                delRelIds += del_array[i] + ",";
            }
            if(del_array.length > 0)
                delRelIds += del_array[del_array.length-1]; 
            
            jQuery("#name").val("");
                        
            var ids = findSelections("checkbox_template","id");  //取得多选框的选择项
            form.action="<venus:base/>/auVisitor/addMultiRole?partyId="+ids+"&parentRelIds="+parentRelIds+"&delRelIds="+delRelIds;
            form.submit();
        }
    
    }
  	
  	function role_onButton(){
  		var ids = findSelections("checkbox_template","id");  //取得多选框的选择项
		if(ids == null) {  //如果ids为空
	  		alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		if(ids.length > 1) {  //如果ids有2条以上的纪录
	  		alert("<fmt:message key='venus.authority.Can_only_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		
		var treePath = "<venus:base/>/jsp/authority/tree/treeRef4RoRel.jsp?inputType=checkbox&nodeRelationType=noRelation&rootXmlSource="
                +"<venus:base/>/jsp/authority/au/aurole/xmlData.jsp?has_checkbox%3Dyes%26partyId%3D"+ids;
                
                
      	showIframeDialog("iframeDialog","<fmt:message key='venus.authority.Reference_page' bundle='${applicationAuResources}' />", treePath, 400, 600);        
	}
	
	function auth_onClick(id){
		var types = form.partyTypes.value;
		window.location="<venus:base/>/auParty/detailList?pageFlag="+types+"&id="+id;
	}
	
	function auth_onButton(){
		var ids = findSelections("checkbox_template","id");  //取得多选框的选择项
		if(ids == null) {  //如果ids为空
	  		alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		if(ids.length > 1) {  //如果ids有2条以上的纪录
	  		alert("<fmt:message key='venus.authority.Can_only_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		auth_onClick(ids);
	}
	
	function findSelections(checkboxName, idName) {  //从列表中找出选中的id值列表
		var elementCheckbox = document.getElementsByName(checkboxName);  //通过name取出所有的checkbox
		var number = 0;  //定义游标
		var ids = null;  //定义id值的数组
		for(var i=0;i<elementCheckbox.length;i++){  //循环checkbox组
			if(elementCheckbox[i].checked) {  //如果被选中
				number += 1;  //游标加1
				if(ids == null) {
					ids = new Array(0);
				}
				var thisTr = jQuery(elementCheckbox[i]).parent().parent().get(0);  //定义本行的tr对象
				var thisHidden = getObjectByNameRecursive(thisTr, "hiddenId");  //从thisTr递归的取出name是hiddenId的对象
				if(thisHidden != undefined && thisHidden != null) {  //如果thisHidden不为空
					ids.push(thisHidden.value);  //加入选中的checkbox
				} 
				//ids.push(elementCheckbox[i].value);  //因为页面回写的缘故，从下一级页面返回时会覆盖该checkbox_template的值，故这个方法无效
			}
		}
		return ids;
	}
	

	function initFocus(){ 
		var ctrl=document.getElementById("name"); 
		ctrl.focus(); 
	}  
//-->
</script>
</head>
<body onload="initFocus()">
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Check_list' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');
</script>
<form name="form" method="post" action="<venus:base/>/auVisitor/simpleQueryByTypes">
<input type="hidden" name="partyTypes" value="<%=partyTypes%>">
<!-- 查询开始 -->
<div id="auDivParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild0',this,'<venus:base/>/themes/<venus:theme/>/')">
			<fmt:message key='venus.authority.Conditional_Query' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>

<div id="auDivChild0"> 
<table class="table_div_content">
<tr>

<td align="right" width="10%" nowrap><fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' /></td>
	<td><input name="name"  id="name" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' />" validate="isSearch">	
	<input type="button" name="Submit" class="button_ellipse" value="<fmt:message key='venus.authority.Inquiry' bundle='${applicationAuResources}' />" onClickto="javascript:simpleQuery_onClick();">
	</td>
</tr>
</table>

</div>
<div id="auDivParent1"> 
<table class="table_div_control">
	<tr > 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='venus.authority.Query_Results' bundle='${applicationAuResources}' />
		</td>
		<td> 
			<table align="right">
				<tr> 
				<%
				if("1".equals(partyTypes)) {%>
					<td class="button_ellipse" onClick="javascript:role_onButton();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/add.gif" class="div_control_image"><fmt:message key='venus.authority.Distribution_of_roles' bundle='${applicationAuResources}' /></td>
				<%}%>
					<td class="button_ellipse" onClick="javascript:auth_onButton();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/modify.gif" class="div_control_image"><fmt:message key='venus.authority.Authorization_Management' bundle='${applicationAuResources}' /></td>
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
			<div style="width=100%;overflow-x:visible;overflow-y:visible;">
				<table cellspacing="0" cellpadding="0" border="0" align="center" width="100%" class="listCss">
					<tr>
						<td valign="top">
							<table cellspacing="1" cellpadding="1" border="0" width="100%">
								<tr valign="top">
									<th class="listCss" width="5%"></th>
									<th class="listCss" width="5%"><fmt:message key='venus.authority.Sequence' bundle='${applicationAuResources}' /></th>
									<th class="listCss" width="20%"><fmt:message key='venus.authority.Number' bundle='${applicationAuResources}' /></th>
									<th class="listCss" width="20%"><fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' /></th>
									<th class="listCss" width="35%"><fmt:message key='venus.authority.Organization' bundle='${applicationAuResources}' /></th>
									<th class="listCss"><fmt:message key='venus.authority.Notes' bundle='${applicationAuResources}' /></th>
								</tr>
								<%
									List beans = (List) request.getAttribute("wy");
									for(int i=0;  i<beans.size();) {
										AuVisitorVo vo= (AuVisitorVo)beans.get(i);
										i++;
								%>
								<tr>
									<td class="listCss" align="center">
										<input type="radio" name="checkbox_template" value="<%=vo.getId()%>"/>
									</td>
									<td class="listCss" align="center"><%=i%><input type="hidden" signName="hiddenId" value="<%=vo.getId()%>" /></td>
									<td class="listCss"><%=StringHelperTools.prt(vo.getId())%></td>
									<td class="listCss"><%=StringHelperTools.prt(vo.getName())%></td>

									<%if (vo.getOwner_org()!=null) {%>
									<td class="listCss"><%=StringHelperTools.prt(StringHelperTools.replaceStringToHtml(vo.getOwner_org()))%></td>
									<%}%>
									<td class="listCss"><%=StringHelperTools.prt(vo.getRemark())%></td>
								</tr>
								<%
									}
								%>
							</table>
						</td>
					</tr>
				</table>
				<jsp:include page="/jsp/include/page.jsp" />
			</div>
		</td>
	</tr>


	<%--<tr>--%>
		<%--<td>--%>
		<%--<layout:collection onRowDblClick="auth_onClick(getRowHiddenId())" name="wy" id="wy1" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >--%>
			<%--<layout:collectionItem width="30" title="" style="text-align:center;">--%>
				<%--<bean:define id="wy3" name="wy1" property="id"/>--%>
					<%--<input type="radio" name="checkbox_template" value="<%=wy3%>"/>--%>
					<%--<input type="hidden" signName="hiddenId" value="<%=wy3%>"/>--%>
			<%--</layout:collectionItem>--%>
			<%--<layout:collectionItem width="30" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sequence") %>' style="text-align:center;">--%>
				<%--<venus:sequence/>--%>
			<%--</layout:collectionItem>--%>
			<%--<layout:collectionItem width="160" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Number") %>' property="id" />	--%>
			<%--<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Name") %>' property="name" />	--%>
				<%--<layout:collectionItem width="400" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Organization") %>' sortable="false">--%>
					<%--<logic:notEmpty name="wy1" property="owner_org">--%>
						<%--<bean:define id="owner_org" name="wy1"  property="owner_org"/>--%>
						 <%--<%=StringHelperTools.replaceStringToHtml(owner_org)%>--%>
					 <%--</logic:notEmpty>--%>
				<%--</layout:collectionItem>		--%>
			<%--<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Notes") %>' property="remark" />--%>
		<%--</layout:collection>--%>
		<%----%>
		<%--<jsp:include page="/jsp/include/page.jsp" />--%>
		<%--</td>--%>
	<%--</tr>--%>
</table>
</div>
<!-- 参照显示层 -->
<div id="iframeDialog" style="display:none"></div>
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>
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

