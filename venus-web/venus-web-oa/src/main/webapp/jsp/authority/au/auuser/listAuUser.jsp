<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="venus.authority.util.StringHelperTools" %>
<%@ page import="venus.authority.util.VoHelperTools" %>
<%@ page import="venus.authority.au.auuser.util.IAuUserConstants" %>
<%@ page import="venus.commons.xmlenum.EnumRepository" %>
<%@ page import="venus.commons.xmlenum.EnumValueMap" %>
<%@ page import="venus.authority.helper.LoginHelper" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ include file="/jsp/authority/org/aupartyrelation/organizeTooltip.jsp" %>
<%
	String system_id = (String)request.getAttribute("system_id");
	if(system_id==null || system_id.length()==0) {
		system_id = request.getParameter("system_id");
		if(system_id==null) {
			system_id = "";
		}
	}
	String func_code = (String)request.getAttribute("func_code");
	if(func_code==null || func_code.length()==0) {
		func_code = request.getParameter("func_code");
		if(func_code==null) {
			func_code = "";
		}
	}
	boolean isAdmin = LoginHelper.getIsAdmin(request);
	EnumRepository er = EnumRepository.getInstance();
    er.loadFromDir();
	EnumValueMap enableStatusMap = er.getEnumValueMap("EnableStatus");	
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.User_Management' bundle='${applicationAuResources}' /></title>
<script language="javascript">
    var insertCount = '<%=session.getAttribute("insertCount")%>';
    <%session.removeAttribute("insertCount");%>
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
				ids.push(elementCheckbox[i].value);  //加入选中的checkbox
			}
		}
		return ids;
	}
	function findCheckbox_onClick() {  //从多选框到修改页面
		var ids = findSelections("checkbox_template","id");  //取得多选框的选择项
		if(ids == null) {  //如果ids为空
	  		alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		if(ids.length > 1) {  //如果ids有2条以上的纪录
	  		alert("<fmt:message key='venus.authority.Can_only_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		<%if(!isAdmin) {%>
			var thisHidden = getLayoutHiddenObjectById(ids[0]);
			var is_admin = jQuery(thisHidden).attr("is_admin");
			if(is_admin=="1") {
				alert("<fmt:message key='venus.authority.You_do_not_have_permission_to_modify_the_super_administrator' bundle='${applicationAuResources}' />!")
				return;
			}
		<%}%>
		form.action="<%=request.getContextPath()%>/auUser/find?id=" + ids;
		form.submit();
	}  
	function view_onClick(){//查看详细页面
		var ids = findSelections("checkbox_template","id");  //取得多选框的选择项
		if(ids == null) {  //如果ids为空
	  		alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		if(ids.length > 1) {  //如果ids有2条以上的纪录
	  		alert("<fmt:message key='venus.authority.Can_only_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		detail_onClick(ids);
	}
	function getCompanyTree() {
		
		var deeptreePath = '<venus:base/>/jsp/comm/tree/deeptree/deeptree_window.jsp?inputType=radio&rootXmlSource=<venus:base/>/jsp/comm/tree/rootXmlData_limit.jsp?root_code=101%26submit_type%3D0%26is_submit_all%3Dyes%26return_type%3Doriginal_id';
		
		var rtObj = window.showModalDialog(deeptreePath, new Object(),'dialogHeight=600px;dialogWidth=350px;');
		if(rtObj != undefined && rtObj.length > 0){
			form.child_name.value = rtObj[0]['childName'];
			form.child_code.value = rtObj[0]['returnValue']; 
		}
	}
	function resetPassword_onClick() {  //从多选框到修改页面
		var ids = findSelections("checkbox_template","id");  //取得多选框的选择项
		if(ids == null) {  //如果ids为空
	  		alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		if(ids.length > 1) {  //如果ids有2条以上的纪录
	  		alert("<fmt:message key='venus.authority.Can_only_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		form.action="<%=request.getContextPath()%>/auUser/resetPassword?id=" + ids;
		form.submit();
	} 
	function enable_onClick() {  //从多选框到修改页面
		var ids = findSelections("checkbox_template","id");  //取得多选框的选择项
		if(ids == null) {  //如果ids为空
	  		alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		if(ids.length > 1) {  //如果ids有2条以上的纪录
	  		alert("<fmt:message key='venus.authority.Can_only_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		var thisHidden = getLayoutHiddenObjectById(ids[0]);
		var enable_status = jQuery(thisHidden).attr("enable_status");
		if(enable_status=="1") {
			alert("<fmt:message key='venus.authority.Is_already_enabled' bundle='${applicationAuResources}' />!")
			return;
		}
		form.action="<%=request.getContextPath()%>/auUser/enable?id=" + ids;
		form.submit();
	}   
	function disable_onClick() {  //从多选框到修改页面
		var ids = findSelections("checkbox_template","id");  //取得多选框的选择项
		if(ids == null) {  //如果ids为空
	  		alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		if(ids.length > 1) {  //如果ids有2条以上的纪录
	  		alert("<fmt:message key='venus.authority.Can_only_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		
		var thisHidden = getLayoutHiddenObjectById(ids[0]);
		var is_admin = jQuery(thisHidden).attr("is_admin");
		var enable_status = jQuery(thisHidden).attr("enable_status");
		if(is_admin=="1") {
			alert("<fmt:message key='venus.authority.Super_Administrators_can_not_disable' bundle='${applicationAuResources}' />!")
			return;
		}
		if(enable_status=="0") {
			alert("<fmt:message key='venus.authority.Already_disabled' bundle='${applicationAuResources}' />!")
			return;
		}
		
		form.action="<%=request.getContextPath()%>/auUser/disable?id=" + ids;
		form.submit();
	}  
	function deleteMulti_onClick(){  //从多选框物理删除多条记录
 		var ids = findSelections("checkbox_template","id");  //取得多选框的选择项
		if(ids == null)	{  //如果ids为空
			alert("<fmt:message key='venus.authority.Please_Select_Records' bundle='${applicationAuResources}' />!")
			return;
		}
		for(var i=0; i<ids.length; i++) {
			var thisHidden = getLayoutHiddenObjectById(ids[i]);
			var is_admin = jQuery(thisHidden).attr("is_admin");
			if(is_admin=="1") {
				alert("<fmt:message key='venus.authority.Super_Administrators_can_not_remove' bundle='${applicationAuResources}' />!")
				return;
			}
		}
		if(confirm("<fmt:message key='venus.authority.It_completely_remove_the_data_' bundle='${applicationAuResources}' />")) {  //如果用户在确认对话框按"确定"
	    	form.action="<%=request.getContextPath()%>/auUser/deleteMulti?ids=" + ids;
	    	form.submit();
		}
	}
	function simpleQuery_onClick(){  //简单的模糊查询
		if(checkAllForms()){
			form.VENUS_PAGE_NO_KEY.value = 1; //切换状态后,从第一页开始显示
    		form.action="<%=request.getContextPath()%>/auUser/simpleQuery";
    		form.submit();
   		}
  	}
	function queryAll_onClick(){  //查询全部数据列表
		form.action="<%=request.getContextPath()%>/auUser/queryAll";
		form.submit();
	}
	function toAdd_onClick() {  //到增加记录页面
		window.location="<%=request.getContextPath()%>/jsp/authority/au/auuser/insertAuUser.jsp?system_id=<%=system_id%>&func_code=<%=func_code%>";
	}
	
	
function returnValueName(rtObj){//组织机构参照返回赋值
        if(rtObj != undefined && rtObj.length > 0){
        	
            if("code"== global_outputArray[0]){//批量增加走该分支
				addMulti_filter(rtObj);
			}
			
            var allTextValue = "";
            var allTextName = ""; 
            var allParentName = "";
            var detailedType = "";
            for(var i=0; i<rtObj.length-1; i++) {
                allTextValue += rtObj[i]['returnValue'] + ",";
                allTextName += rtObj[i]['childName'] + ",";
                allParentName += rtObj[i]["parentName"] + ",";
                detailedType += rtObj[i]["detailedType"] + ",";
            }
            allTextValue += rtObj[rtObj.length-1]['returnValue'];
            allTextName += rtObj[rtObj.length-1]['childName'];
            allParentName += rtObj[rtObj.length-1]['parentName'];
            detailedType += rtObj[rtObj.length-1]['detailedType'];

        var textValue = global_outputArray[0];
		jQuery("#"+textValue).size()!=0?jQuery("#"+textValue).val(allTextValue):jQuery("input[name='"+textValue+"']").val(allTextValue);
        jQuery("#"+textValue).size()!=0?jQuery("#"+textValue).trigger("change"):jQuery("input[name='"+textValue+"']").trigger("change");
            
       if(global_outputArray.length>=2) {
            var textName = global_outputArray[1];
	        jQuery("#"+textName).size()!=0?jQuery("#"+textName).val(allTextName):jQuery("input[name='"+textName+"']").val(allTextName);
	        jQuery("#"+textName).size()!=0?jQuery("#"+textName).trigger("change"):jQuery("input[name='"+textName+"']").trigger("change");
       }
        
        if(global_outputArray.length>=3) {
			var textParentName = global_outputArray[2];
			jQuery("#"+textParentName).val(allParentName);
			jQuery("#"+textParentName).trigger("change");
		}
		if(global_outputArray.length==4) {
			var nodeType = global_outputArray[3];
			jQuery("#"+nodeType).val(detailedType);
			jQuery("#"+nodeType).trigger("change");
		}
    }
    
}

	function addMulti_filter(rtObj){
			form.code.value = ""; 
			var code = form.code.value;
			for(var i=0; i<rtObj.length-1; i++) {
				if (code.indexOf(rtObj[i]['returnValue']) != -1) //过滤重复的参数
					continue;
				form.code.value += rtObj[i]['returnValue'] + ",";
			}
			if (code.indexOf(rtObj[rtObj.length-1]['returnValue']) != -1) { //过滤重复的参数
				form.code.value = code.substring(0,code.length-1);
			} else {
				form.code.value += rtObj[rtObj.length-1]['returnValue'];
			}
	    	form.action = "<%=request.getContextPath()%>/auUser/insertMulti";
	    	form.submit();
	}
	
	
	function toAddMulti_onClick() { //创建多个账号
		var treePath = "<venus:base/>/jsp/authority/tree/treeRef.jsp?inputType=checkbox&nodeRelationType=noRelation&submitType=parentPriority&rootXmlSource=<venus:base/>/jsp/authority/au/auuser/xmlData.jsp?submit_all%3D1%26parent_code%3D%26return_type%3Dcode";
		 global_outputArray=new Array('code');//设置返回输出控件集合
		 showIframeDialog("iframeDialog",venus.authority.Reference_page, treePath, 400, 600);
	}	
	function detail_onClick(thisId){  //实现转到详细页面
		var thisHidden = getLayoutHiddenObjectById(thisId);
		var is_admin = jQuery(thisHidden).attr("is_admin");
		if(is_admin=="1") {
			alert("<fmt:message key='venus.authority.Super_Administrator_can_not_see' bundle='${applicationAuResources}' />!")
			return;
		}
		form.id.value = thisId;  //赋值thisId给隐藏值id
		form.action="<%=request.getContextPath()%>/auUser/detail";
		form.submit();
	}
	function checkAllList_onClick(thisObj){  //全选，全不选
		if(thisObj.checked) {  //如果选中
			selectAllRows('template');  //全选checkbox
		} else {
			unSelectAllRows('template');  //全不选checkbox
		}
	}
	function initFocus(){ 
		var ctrl=document.getElementById("login_id"); 
		ctrl.focus(); 
	}  
	function clear_onClick() { //清空查询条件
		form.login_id.value = "";
		form.name.value = "";
		form.department.value = "";
		form.hid_department.value = "";
		form.enableStatus.options[0].selected = true;
	}	
	function search_onKeyDown() {//回车后执行页面查询功能
		if (event.keyCode == 13) { 
			simpleQuery_onClick();
		}
	}	
</script>

</head>
<body onload="initFocus()">
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.User_Management' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<form name="form" method="post">
<input type="hidden" name="code" value=""/>
<table class="table_noFrame">
	<tr>
		<td>
			<!--input name="button_advancedQuery" class="button_ellipse" type="button" value="高级查询" onClick="javascript:toAdvancedQuery_onClick()">
			<input name="button_queryAll" class="button_ellipse" type="button" value="查询全部"  onClick="javascript:queryAll_onClick()"-->		
		</td>
	</tr>
</table>   

<div id="auDivParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" class="div_control_image" onClick="javascript:hideshow('auDivChild0',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')" >&nbsp;<fmt:message key='venus.authority.Conditional_Query' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>

<div id="auDivChild0"> 
<table class="table_div_content">
<tr><td>
	<table class="table_noFrame" width="100%">
		<tr>
			<td width="15%" align="right" nowrap="nowrap"><fmt:message key='venus.authority.Login_Account' bundle='${applicationAuResources}' /></td>
			<td width="35%" align="left">
				<input type="text" class="text_field" validate="isSearch" name="login_id" id="login_id"  inputName="<fmt:message key='venus.authority.Login_Account' bundle='${applicationAuResources}' />" maxLength="300" value="" onKeyDown="javascript:search_onKeyDown();"/>
			</td>
			<td width="15%" align="right" nowrap="nowrap"><fmt:message key='venus.authority.Real_Name' bundle='${applicationAuResources}' /></td>
			<td width="35%" align="left">
				<input type="text" class="text_field" validate="isSearch" name="name" inputName="<fmt:message key='venus.authority.Real_Name' bundle='${applicationAuResources}' />" maxLength="300" value="" onKeyDown="javascript:search_onKeyDown();"/>
			</td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap"><fmt:message key='venus.authority.Respective_organizations' bundle='${applicationAuResources}' /></td>
			<td  align="left">
				<input type="text" class="text_field_reference_readonly" validate="isSearch" id="department" name="department" inputName="<fmt:message key='venus.authority.Department' bundle='${applicationAuResources}' />" maxLength="50" hiddenInputId="hid_department" value=""/><input type="hidden" id="hid_department"  name="hid_department"/><img class="refButtonClass" alt="<fmt:message key='venus.authority.Select_department' bundle='${applicationAuResources}' />" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif" onClick="javascript:getTree(new Array('hid_department', 'department'),'<venus:base/>','radio','','1','','2','1');"/>
			</td>			
			<td  align="right" nowrap="nowrap"><fmt:message key='venus.authority.User_Status' bundle='${applicationAuResources}' /></td>
			<td  align="left">
				<select name="enableStatus" style="width:60px">
					<%
						List enableStatusList = enableStatusMap.getEnumList();
						for(int i=0;i<enableStatusList.size();i++){%>
					<option value="<%=enableStatusMap.getValue(enableStatusList.get(i).toString())%>"><%=enableStatusList.get(i)%></option>
	     			 <%}%>
				</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input name="button_ok" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Inquiry' bundle='${applicationAuResources}' />" onClickTo="javascript:simpleQuery_onClick()">&nbsp;&nbsp;
				<input name="button_clear" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Empty' bundle='${applicationAuResources}' />" onClick="javascript:clear_onClick()">					
			</td>		
		</tr>	
		<!--tr>
			<td align="right">所属单位</td>
			<td align="left">
				<input type="text" class="text_field_reference_readonly" validate="isSearch" name="child_name" inputName="所属单位" 
				maxlength="50" style={width:185} hiddenInputId="child_code" /><input type="hidden" name="child_code"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/09.gif" onClick="javascript:getCompanyTree();"/>
			</td>
			<td align="right"></td>
			<td align="left">
				<input name="button_ok" class="button_ellipse" type="button" value="确定" onClickTo="javascript:simpleQuery_onClick()">
			</td>
		</tr-->
	</table>

</td></tr>
</table>
</div>
					
<div id="auDivParent1"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild1',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='venus.authority.User_list' bundle='${applicationAuResources}' />
		</td>
		<td> 
			<table align="right">
				<tr> 
					<td class="button_ellipse" onClick="javascript:toAdd_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/add.gif" class="div_control_image"><fmt:message key='venus.authority.Added' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" onClick="javascript:toAddMulti_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/add.gif" class="div_control_image"><fmt:message key='venus.authority.Volume_increase' bundle='${applicationAuResources}' /></td>
					<!-- td class="button_ellipse" onClick="javascript:deleteMulti_onClick();"><img src="<venus:base/>/images/icon/delete.gif" class="div_control_image">删除</td-->
					<td class="button_ellipse" onClick="javascript:findCheckbox_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/modify.gif" class="div_control_image"><fmt:message key='venus.authority.Modify' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" onClick="javascript:enable_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/enable.gif" class="div_control_image"><fmt:message key='venus.authority.Enabled' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" onClick="javascript:disable_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/disable.gif" class="div_control_image"><fmt:message key='venus.authority.Disable' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" onClick="javascript:view_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/view.gif" class="div_control_image"><fmt:message key='venus.authority.View' bundle='${applicationAuResources}' /></td>
					<!--td style={width:80} class="td_hand" onClick="javascript:resetPassword_onClick();"><img src="<%=request.getContextPath()%>/images/icon/refresh.gif" class="div_control_image">初始化密码</td-->
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>

<div id="auDivChild1"> 
<table class="table_div_content2" width="100%">
	<tr>
		<td>
		<layout:collection onRowDblClick="detail_onClick(getRowHiddenId())"  name="beans" id="rmBean" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >
			
			<layout:collectionItem width="30" title="" style="text-align:center;">
				<bean:define id="rmValue" name="rmBean" property="id"/>
				<bean:define id="rmDisplayName" name="rmBean" property="id"/>
				<input type="radio" name="checkbox_template" value="<%=rmValue%>" displayName="<%=rmDisplayName%>"/>
			</layout:collectionItem>
 
			<layout:collectionItem width="30"  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sequence") %>' style="text-align:center;">
				<venus:sequence/>
				<bean:define id="rmValue" name="rmBean" property="id"/>
				<bean:define id="login_id" name="rmBean" property="login_id"/>
				<bean:define id="real_name" name="rmBean" property="name"/>
				<bean:define id="party_id" name="rmBean" property="party_id"/>
				<bean:define id="is_admin" name="rmBean" property="is_admin"/>
				<bean:define id="enable_status" name="rmBean" property="enable_status"/>
				<input type="hidden" signName="hiddenId" value="<%=rmValue%>" login_id="<%=login_id%>" real_name="<%=real_name%>" party_id="<%=party_id%>" is_admin="<%=is_admin%>" enable_status="<%=enable_status%>"/>
			</layout:collectionItem>
			
		<layout:collectionItem width="15%" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Number") %>' property="party_id" sortable="false"/>
		<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Login_Account") %>' property="login_id" sortable="false"/>
		<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Real_Name") %>' property="name" sortable="false"/>
		<%if (organizeTooltip == null) { %>
		<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Organization") %>' sortable="false">
			<logic:notEmpty name="rmBean" property="owner_org">
				<bean:define id="owner_org" name="rmBean"  property="owner_org"/>
				 <%=StringHelperTools.replaceStringToHtml(owner_org)%>
			 </logic:notEmpty>
		</layout:collectionItem>		
		<%} %>
		<layout:collectionItem width="60" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Status") %>' sortable="false" style="text-align:center;">
			<bean:define id="enable_status" name="rmBean" property="enable_status"/>
		    <%="1".equals(enable_status)?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Enabled"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Disable")%>
		</layout:collectionItem>
		<layout:collectionItem width="150" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Created") %>' sortable="false">
			<bean:define id="create_date" name="rmBean" property="create_date"/>
		    <%=StringHelperTools.prt(create_date,19)%>
		</layout:collectionItem>
		<layout:collectionItem width="150" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Password_expiration") %>'  sortable="false">
			<logic:notEmpty name="rmBean" property="retire_date">
				<bean:define id="retire_date" name="rmBean" property="retire_date"/>
		    	<%=StringHelperTools.prt(retire_date,19)%>
			</logic:notEmpty>
		</layout:collectionItem>

		</layout:collection>
		
		<jsp:include page="/jsp/include/page.jsp" />
		</td>
	</tr>
</table>
</div>

<input type="hidden" name="id" value="">
<input type="hidden" name="queryCondition" value="">

<input type="hidden" name="system_id" value="<%=system_id%>">
<input type="hidden" name="func_code" value="<%=func_code%>">

<!-- 参照显示层 -->
<div id="iframeDialog" style="display:none"></div>

</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
	if (insertCount ==0 ) //当批量分配帐号时，没有新增加帐号后给予提示
	   alert("<fmt:message key='venus.authority.Employees_within_the_organization_accounts_have_been_allocated_' bundle='${applicationAuResources}' />");
</script>
</body>
</html>
<%  //表单回写
	if(request.getAttribute(IAuUserConstants.REQUEST_WRITE_BACK_FORM_VALUES) != null) {  //如果request中取出的bean不为空
		out.print("<script language=\"javascript\">\n");  //输出script的声明开始
		out.print(VoHelperTools.writeBackMapToForm((java.util.Map)request.getAttribute(IAuUserConstants.REQUEST_WRITE_BACK_FORM_VALUES)));  //输出表单回写方法的脚本
		out.print("writeBackMapToForm();\n");  //输出执行回写方法
		out.print("</script>");  //输出script的声明结束
	}
%>

