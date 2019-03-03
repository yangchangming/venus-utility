<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="venus.authority.util.StringHelperTools" %>
<%@ page import="venus.authority.helper.LoginHelper" %>
<%@ page import = "venus.commons.xmlenum.EnumRepository" %>
<%@ page import = "venus.commons.xmlenum.EnumValueMap" %>

<%
	boolean isAdmin = LoginHelper.getIsAdmin(request);
	EnumRepository er = EnumRepository.getInstance();
	er.loadFromDir();
	EnumValueMap enableStatusMap = er.getEnumValueMap("EnableStatus");
	EnumValueMap paramTypeMap = er.getEnumValueMap("ParamType"); 
%>
<title><fmt:message key='venus.authority.Query_Template' bundle='${applicationAuResources}' /></title>
<script>
    var isAdmin = "<%=LoginHelper.getIsAdmin(request)%>";
    function initButton() {
	    var deleteBtn = document.getElementById("deleteBtn");
	    var updateBtn = document.getElementById("updateBtn");    
	    if (isAdmin == "false") { 
	        deleteBtn.disabled = true;  
	        updateBtn.disabled = true;      
	    } else {
	        deleteBtn.disabled = true;  
	    }
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
	  		alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		if(number > 1) {
	  		alert("<fmt:message key='venus.authority.Can_only_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		form.action = "<venus:base/>/sysParams/find?id=" + ids;
		form.submit();
	}
	function deleteMulti_onClick(){  //从多选框物理删除多条记录
			
 		var elementCheckbox = document.getElementsByName("checkbox_template");
		var number = 0;
		var ids = "";
		for(var i=0;i<elementCheckbox.length;i++){
			if(elementCheckbox[i].checked) {
				number += 1;
				ids += elementCheckbox[i].value + ",";
			}	
		}
		if(ids.length>0) {
			ids = ids.substr(0,ids.length-1);	
		}
		if(number == 0) {
	  		alert("<fmt:message key='venus.authority.Please_delete' bundle='${applicationAuResources}' />!")
	  		return;
		}
		if(confirm("<fmt:message key='venus.authority.It_completely_remove_the_data_' bundle='${applicationAuResources}' />")) {
	    	form.action="<venus:base/>/sysParams/delete?id=" + ids;
    		form.submit();
		}
	}
	function toAdd_onClick() {  //到增加记录页面
	    form.action="<venus:base/>/jsp/authority/sys/insert.jsp";
    	form.submit();
	}
	function refresh_onClick(){  //刷新本页
		window.location.reload();
	}
	function enable_onClick() {  //从多选框到修改页面
		var elementCheckbox = document.getElementsByName("checkbox_template");
		var number = 0;
		var ids = "";
		for(var i=0;i<elementCheckbox.length;i++){
			if(elementCheckbox[i].checked) {
				number += 1;
				ids += elementCheckbox[i].value + ",";
			}
		}
		if(ids.length>0) {
			ids = ids.substr(0,ids.length-1);	
		}
		if(number == 0) {
	  		alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		var thisHidden = getLayoutHiddenObjectById(ids);
		var enable = jQuery(thisHidden).attr("enable");
		if(enable=="1") {
			alert("<fmt:message key='venus.authority.Is_already_enabled' bundle='${applicationAuResources}' />!")
			return;
		}
		form.action="<%=request.getContextPath()%>/sysParams/enable?enable=1&id=" + ids;
		form.submit();
		alert("<fmt:message key='venus.authority.Change_the_system_configuration_items_in_the_system_log_back_on_after_the_entry_into_force_of_organizational_competence_' bundle='${applicationAuResources}' />");
	}   
	function disable_onClick() {  //从多选框到修改页面
		var elementCheckbox = document.getElementsByName("checkbox_template");
		var number = 0;
		var ids = "";
		for(var i=0;i<elementCheckbox.length;i++){
			if(elementCheckbox[i].checked) {
				number += 1;
				ids += elementCheckbox[i].value + ",";
			}
		}
		if(ids.length>0) {
			ids = ids.substr(0,ids.length-1);	
		}

		if(number == 0) {
	  		alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		var thisHidden = getLayoutHiddenObjectById(ids);
		var enable = jQuery(thisHidden).attr("enable");
		if(enable=="0") {
			alert("<fmt:message key='venus.authority.Already_disabled' bundle='${applicationAuResources}' />!")
			return;
		}
		form.action="<%=request.getContextPath()%>/sysParams/enable?enable=0&id=" + ids;
		form.submit();
		alert("<fmt:message key='venus.authority.After_changing_the_configuration_items_need_to_log_in_to_take_effect_' bundle='${applicationAuResources}' />");
	}  
	function buttonControl_onClick(thisId) {
		var thisHidden = getLayoutHiddenObjectById(thisId);
	    var deleteBtn = document.getElementById("deleteBtn");
	    var updateBtn = document.getElementById("updateBtn");    		
		if (isAdmin == "false") { // 用户只能新增配置项，可以删除自己新增的配置项
			if(thisHidden.propertytype == "0") { // 0为系统默认配置项，1为用户自定义项
				deleteBtn.disabled = true;	
				updateBtn.disabled = true;	
			} else {
				deleteBtn.disabled = false;	
				updateBtn.disabled = false;	
			}
		} else { //超级管理员不能删除系统默认配置项，只能新增或修改
            if(thisHidden.propertytype == "0") {
                deleteBtn.disabled = true;  
            } else {
                deleteBtn.disabled = false; 
            }		
		}
	}
</script>
</head>
<body onload = "initButton();">
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.The_system_configuration_items' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');
</script>

<form name="form" method="post" action="<venus:base/>/sysParams">
<input type="hidden" name="cmd" value="">

<div id="auDivParent1"> 
<table class="table_div_control">
	<tr > 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='venus.authority.Details_Form' bundle='${applicationAuResources}' />
		</td>
		<td> 
			<table align="right">
				<tr> 
					<td id="addBtn" class="button_ellipse" onClick="javascript:toAdd_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/add.gif" class="div_control_image"><fmt:message key='venus.authority.Added' bundle='${applicationAuResources}' /></td>
					<td id="deleteBtn" class="button_ellipse" onClick="javascript:deleteMulti_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/delete.gif" class="div_control_image"><fmt:message key='venus.authority.Delete' bundle='${applicationAuResources}' /></td>
					<td id="updateBtn" class="button_ellipse" onClick="javascript:findCheckbox_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/modify.gif" class="div_control_image"><fmt:message key='venus.authority.Modify' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" onClick="javascript:enable_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/enable.gif" class="div_control_image"><fmt:message key='venus.authority.Enabled' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" onClick="javascript:disable_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/disable.gif" class="div_control_image"><fmt:message key='venus.authority.Disable' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" onClick="javascript:refresh_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/refresh.gif" class="div_control_image"><fmt:message key='venus.authority.Refresh' bundle='${applicationAuResources}' /></td>
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
		<layout:collection name="beans" id="wy1" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" onRowClick="javascript:buttonControl_onClick(getRowHiddenId());">
			<layout:collectionItem width="40" title="" style="text-align:center;">
				<bean:define id="wy3" name="wy1" property="id"/>
				<bean:define id="enable" name="wy1" property="enable"/>
				<bean:define id="propertytype" name="wy1" property="propertytype"/>
					<input type="radio" name="checkbox_template" value="<%=wy3%>"/>
					<input type="hidden" signName="hiddenId" value="<%=wy3%>" enable="<%= enable %>" propertytype="<%= propertytype %>"/>
			</layout:collectionItem>
			<layout:collectionItem width="30"  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sequence") %>' style="text-align:center;">
				<venus:sequence/>
			</layout:collectionItem>
			<layout:collectionItem width="200"  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Parameter_name") %>' property="propertykey" />
			<layout:collectionItem width="120"  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Parameter_values") %>' property="value" />
			<layout:collectionItem width="120"  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Parameter_type") %>'>
				<bean:define id="propertytype" name="wy1" property="propertytype"/>
				<%=paramTypeMap.getLabel(propertytype.toString()) %>
			</layout:collectionItem>
			<layout:collectionItem width="180" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Set_the_time") %>' property="updateTime">
				<bean:define id="updateTime" name="wy1" property="updateTime"/>
				<%=StringHelperTools.prt(updateTime, 19)%>
			</layout:collectionItem>
			<layout:collectionItem width="100" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Set_of_people") %>' property="creatorName" />
			<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Description") %>' property="description" />
			<layout:collectionItem width="60" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Status") %>' style="text-align:center;">
				<bean:define id="enable" name="wy1" property="enable"/>
				<%=enableStatusMap.getLabel(enable.toString()) %>
			 </layout:collectionItem>
		 </layout:collection>
		</td>
	</tr>
	<tr>
		<td><br/></td>
	</tr>
</table>
</div>
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>
</body>
</html>

