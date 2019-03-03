<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.authority.util.StringHelperTools" %>
<%@ page import="venus.authority.util.VoHelperTools" %>
<%@ page import="venus.authority.sample.department.util.IDepartmentConstants" %>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Departments' bundle='${applicationAuResources}' /></title>

<script language="javascript">

	/**
	 * 从列表中找出选中的id值列表
	 *
	 * @param checkboxName
	 * @param idName
	 * @returns {*}
     */
	function findSelections(checkboxName, idName) {
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

	/**
	 * 从多选框到修改页面
	 */
	function findCheckbox_onClick() {
		var ids = findSelections("checkbox_template","id");  //取得多选框的选择项
		if(ids == null) {  //如果ids为空
	  		alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		if(ids.length > 1) {  //如果ids有2条以上的纪录
	  		alert("<fmt:message key='venus.authority.Can_only_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		form.action="<%=request.getContextPath()%>/department/find?id=" + ids;
		form.submit();
	}

	/**
	 * 从多选框物理删除多条记录
	 */
	function deleteMulti_onClick(){
 		var ids = findSelections("checkbox_template","id");  //取得多选框的选择项
		if(ids == null)	{  //如果ids为空
			alert("<fmt:message key='venus.authority.Please_Select_Records' bundle='${applicationAuResources}' />!")
			return;
		}
		if(confirm("<fmt:message key='venus.authority.It_completely_remove_the_data_' bundle='${applicationAuResources}' />")) {  //如果用户在确认对话框按"确定"
	    	form.action="<%=request.getContextPath()%>/department/deleteMulti?ids=" + ids;
	    	form.submit();
		}
	}

	/**
	 * 简单的模糊查询
	 */
	function simpleQuery_onClick(){
		form.action="<%=request.getContextPath()%>/department/simpleQuery";
    	form.submit();
  	}

	/**
	 * 查询全部数据列表
	 */
	function queryAll_onClick(){
		form.action="<%=request.getContextPath()%>/department/queryAll";
		form.submit();
	}

	/**
	 * 到增加记录页面
	 */
	function toAdd_onClick() {
		window.location="<%=request.getContextPath()%>/jsp/authority/sample/department/insertDepartment.jsp";
	}

	/**
	 * 刷新本页
	 */
	function refresh_onClick(){
       window.location.href=window.location.href; 
        window.location.reload;
	}

	/**
	 * 实现转到详细页面
	 * @param thisId
     */
	function detail_onClick(thisId){
		form.id.value = thisId;  //赋值thisId给隐藏值id
		form.action="<%=request.getContextPath()%>/department/detail?isReadOnly=1";
		form.submit();
	}

	/**
	 * 全选，全不选
	 * @param thisObj
     */
	function checkAllList_onClick(thisObj){
		if(thisObj.checked) {  //如果选中
			selectAllRows('template');  //全选checkbox
		} else {
			unSelectAllRows('template');  //全不选checkbox
		}
	}

	/**
	 * 清空查询条件
	 */
	function clean_onClick() {
		form.dept_no.value = "";
		form.dept_name.value = "";
		form.dept_flag.value = "";
		form.dept_leader.value = "";
	}

	/**
	 * 查看详细页面
	 */
	function view_onClick(){
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
</script>

</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.List_pages' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>

<form name="form" method="post">

<div id="ccParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild0',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')" >&nbsp;<fmt:message key='venus.authority.Conditional_Query' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>

<div id="ccChild0"> 
<table class="table_div_content">
	<tr>
		<td>
			<table class="table_noFrame" width="100%">
				<tr>
					<td width="15%" align="right"><fmt:message key='venus.authority.Department_Code' bundle='${applicationAuResources}' /></td>
					<td width="35%" align="left">
						<input type="text" class="text_field" validate="isSearch" name="dept_no" inputName="<fmt:message key='venus.authority.Department_Code' bundle='${applicationAuResources}' />" maxLength="50"/>
					</td>
					<td width="15%" align="right"><fmt:message key='venus.authority.Department_name' bundle='${applicationAuResources}' /></td>
					<td width="35%" align="left">
						<input type="text" class="text_field" validate="isSearch" name="dept_name" inputName="<fmt:message key='venus.authority.Department_name' bundle='${applicationAuResources}' />" maxLength="300"/>
					</td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Department_logo' bundle='${applicationAuResources}' /></td>
					<td align="left">
						<input type="text" class="text_field" validate="isSearch" name="dept_flag" inputName="<fmt:message key='venus.authority.Department_logo' bundle='${applicationAuResources}' />" maxLength="50"/>
					</td>
					<td align="right"><fmt:message key='venus.authority.Responsible_person' bundle='${applicationAuResources}' /></td>
					<td align="left">
						<input type="text" class="text_field" validate="isSearch" name="dept_leader" inputName="<fmt:message key='venus.authority.Person_in_charge' bundle='${applicationAuResources}' />" maxLength="50"/>
					</td>
				</tr>

				<%--<tr>
					<td align="right">部门类型</td>
					<td align="left">
						<input type="text" class="text_field" validate="isSearch" name="dept_type" inputName="部门类型" maxLength="50"/>
					</td>
					<td align="right">部门级别</td>
					<td align="left">
						<input type="text" class="text_field" validate="isSearch" name="dept_level" inputName="部门级别" maxLength="50"/>
					</td>
				</tr>
				<tr>
					<td align="right">可用状态</td>
					<td align="left">
						<input type="radio" name="enable_status" inputName="启用状态" value="1">启用
						<input type="radio" name="enable_status" inputName="禁用状态" value="0">禁用
					</td>
					<td align="right">启用/禁用日期</td>
					<td align="left">
						<input type="text" class="text_field_half_reference_readonly" name="enable_date_from"/><img class="refButtonClass" src="<venus:base/>/images/09.gif" onClick="javascript:getYearMonthDay(enable_date_from,'<venus:base/>/');"/>&nbsp;到&nbsp;<input type="text" class="text_field_half_reference_readonly" name="enable_date_to"/><img class="refButtonClass" src="<venus:base/>/images/09.gif" onClick="javascript:getYearMonthDay(enable_date_to,'<venus:base/>/');"/>
					</td>
				</tr>
				<tr>
					<td align="right">创建日期</td><td align="left">
						<input type="text" class="text_field_half_reference_readonly" name="create_date_from"/><img class="refButtonClass" src="<venus:base/>/images/au/09.gif" onClick="javascript:getYearMonthDay(create_date_from,'<venus:base/>/');"/>&nbsp;到&nbsp;<input type="text" class="text_field_half_reference_readonly" name="create_date_to"/><img class="refButtonClass" src="<venus:base/>/images/au/09.gif" onClick="javascript:getYearMonthDay(create_date_to,'<venus:base/>/');"/>
					</td>
					<td align="right">修改日期</td>
					<td align="left">
						<input type="text" class="text_field_half_reference_readonly" name="modify_date_from"/><img class="refButtonClass" src="<venus:base/>/images/au/09.gif" onClick="javascript:getYearMonthDay(modify_date_from,'<venus:base/>/');"/>&nbsp;到&nbsp;<input type="text" class="text_field_half_reference_readonly" name="modify_date_to"/><img class="refButtonClass" src="<venus:base/>/images/au/09.gif" onClick="javascript:getYearMonthDay(modify_date_to,'<venus:base/>/');"/>
					</td>
				</tr>
				<tr>
					<td align="right">备注</td>
					<td align="left">
						<input type="text" class="text_field" validate="isSearch" name="remark" inputName="备注" maxLength="50"/>
					</td>
					<td align="right">备用字段1</td>
					<td align="left">
						<input type="text" class="text_field" validate="isSearch" name="column1" inputName="备用字段1" maxLength="300"/>
					</td>
				</tr>
				<tr>
					<td align="right">备用字段2</td>
					<td align="left">
						<input type="text" class="text_field" validate="isSearch" name="column2" inputName="备用字段2" maxLength="300"/>
					</td>
					<td align="right">备用字段3</td>
					<td align="left">
						<input type="text" class="text_field" validate="isSearch" name="column3" inputName="备用字段3" maxLength="300"/>
					</td>
				</tr> --%>


				<tr>
					<td align="right"></td>
					<td align="right"></td>
					<td align="right"></td>
					<td>
						<input name="button_ok" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Inquiry' bundle='${applicationAuResources}' />" onClick="javascript:simpleQuery_onClick()">
						<input name="button_ok" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Empty' bundle='${applicationAuResources}' />" onClick="javascript:clean_onClick()">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>
					
<div id="ccParent1"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='venus.authority.Details_Form' bundle='${applicationAuResources}' />
		</td>
		<td> 
			<table align="right">
				<tr> 
					<td class="button_ellipse" onClick="javascript:toAdd_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/add.gif" class="div_control_image"><fmt:message key='venus.authority.Added' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" onClick="javascript:deleteMulti_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/delete.gif" class="div_control_image"><fmt:message key='venus.authority.Delete' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" onClick="javascript:findCheckbox_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/modify.gif" class="div_control_image"><fmt:message key='venus.authority.Modify' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" onClick="javascript:view_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/view.gif" class="div_control_image"><fmt:message key='venus.authority.View' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" onClick="javascript:refresh_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/refresh.gif" class="div_control_image"><fmt:message key='venus.authority.Refresh' bundle='${applicationAuResources}' /></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>

<div id="ccChild1"> 
<table class="table_div_content2" width="100%">
	<tr>
		<td>
		<layout:collection onRowDblClick="detail_onClick(getRowHiddenId())" name="beans" id="rmBean" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0">
			
			<layout:collectionItem width="30" title="<input type='checkbox' pdType='control' control='checkbox_template'/>" style="text-align:center;">
				<bean:define id="rmValue" name="rmBean" property="id"/>
				<bean:define id="rmDisplayName" name="rmBean" property="id"/>
				<input type="checkbox" name="checkbox_template" value="<%=rmValue%>" displayName="<%=rmDisplayName%>"/>
			</layout:collectionItem>
			
			<layout:collectionItem width="30"  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sequence") %>' style="text-align:center;">
				<venus:sequence/>
				<bean:define id="rmValue" name="rmBean" property="id"/>
				<input type="hidden" signName="hiddenId" value="<%=rmValue%>"/>
			</layout:collectionItem>

			<layout:collectionItem width="160" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Department_Code") %>' property="dept_no"/>
			<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Department_name") %>' property="dept_name"/>
			<layout:collectionItem width="60" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Department_logo") %>' property="dept_flag"/>
			<layout:collectionItem width="100" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Person_in_charge") %>' property="dept_leader"/>
			<layout:collectionItem width="140" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Created") %>'>
				<bean:define id="create_date" name="rmBean" property="create_date"/><%=StringHelperTools.prt(create_date,19)%>
			</layout:collectionItem>
		</layout:collection>
		
		<jsp:include page="/jsp/include/page.jsp" />
		</td>
	</tr>
</table>
</div>

<input type="hidden" name="id" value="">
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>
<%  //表单回写
	if(request.getAttribute(IDepartmentConstants.REQUEST_WRITE_BACK_FORM_VALUES) != null) {  //如果request中取出的bean不为空
		out.print("<script language=\"javascript\">\n");  //输出script的声明开始
		out.print(VoHelperTools.writeBackMapToForm((java.util.Map)request.getAttribute(IDepartmentConstants.REQUEST_WRITE_BACK_FORM_VALUES)));  //输出表单回写方法的脚本
		out.print("writeBackMapToForm();\n");  //输出执行回写方法
		out.print("</script>");  //输出script的声明结束
	}
%>
	

