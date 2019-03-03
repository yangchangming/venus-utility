<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List,java.util.Iterator" %>
<%@ page import="venus.authority.util.VoHelperTools" %>
<%@ page import="venus.authority.util.StringHelperTools" %>
<%@ page import="venus.authority.login.loginlog.vo.LoginLogVo" %>
<%@ page import="venus.authority.login.loginlog.util.ILoginLogConstants" %>
<%  //取出List
	List lResult = null;  //定义结果列表的List变量
	if(request.getAttribute(ILoginLogConstants.REQUEST_BEANS) != null) {  //如果request中的beans不为空
		lResult = (List)request.getAttribute(ILoginLogConstants.REQUEST_BEANS);  //赋值给resultList
	}
	Iterator itLResult = null;  //定义访问List变量的迭代器
	if(lResult != null) {  //如果List变量不为空
		itLResult = lResult.iterator();  //赋值迭代器
	}
	LoginLogVo resultVo = null;  //定义一个临时的vo变量
%>
<%  //是否跳往打印页面
	if("1".equals(request.getParameter("isPrint"))) {  //如果isPrint参数等于1
		while(itLResult != null && itLResult.hasNext()) {  //循环迭代器
			resultVo = (LoginLogVo) itLResult.next();  //赋值给临时vo变量
			VoHelperTools.replaceToScript(resultVo);  //过滤html关键字，并把null替换为""
		}
		session.setAttribute(ILoginLogConstants.REQUEST_BEANS, lResult);  //把List变量放到session中
		response.sendRedirect(request.getContextPath()+"/jsp/authority/au/loginlog/exportLoginLog.jsp");  //跳转到打印页面
		return;
	}
%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>gap.rm-based architecture project</title>
<script language="javascript">
	var rmActionName = "loginLog";
	var rmJspPath = "";
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
				ids.push(elementCheckbox[i].attributes[idName].nodeValue);  //加入选中的checkbox
			}
		}
		return ids;
	}
	function findCheckbox_onClick() {  //从多选框到修改页面
		var ids = findSelections("checkbox_template","value");  //取得多选框的选择项
		if(ids == null) {  //如果ids为空
	  		alert("<fmt:message key='venus.authority.Please_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		if(ids.length > 1) {  //如果ids有2条以上的纪录
	  		alert("<fmt:message key='venus.authority.Can_only_select_a_record' bundle='${applicationAuResources}' />!")
	  		return;
		}
		form.action="<%=request.getContextPath()%>/" + rmActionName + "/find?id=" + ids;
		form.submit();
	}  
	function deleteMulti_onClick(){  //从多选框物理删除多条记录
 		var ids = findSelections("checkbox_template","value");  //取得多选框的选择项
		if(ids == null)	{  //如果ids为空
			alert("<fmt:message key='venus.authority.Please_Select_Records' bundle='${applicationAuResources}' />!")
			return;
		}
		if(confirm("<fmt:message key='venus.authority.It_completely_remove_the_data_' bundle='${applicationAuResources}' />")) {  //如果用户在确认对话框按"确定"
	    	form.action="<%=request.getContextPath()%>/" + rmActionName + "/deleteMulti?ids=" + ids;
	    	form.submit();
		}
	}
	function deleteAll_onClick() { //删除全部记录
		if(confirm("<fmt:message key='venus.authority.It_completely_remove_all_data_' bundle='${applicationAuResources}' />")) {  //如果用户在确认对话框按"确定"
	    	form.action="<%=request.getContextPath()%>/" + rmActionName + "/deleteAll";
	    	form.submit();
		}
	}
	function simpleQuery_onClick(){  //简单的模糊查询
		form.action="<%=request.getContextPath()%>/" + rmActionName + "/simpleQuery";
    	form.submit();
  	}
	function queryAll_onClick(){  //查询全部数据列表
		form.action="<%=request.getContextPath()%>/" + rmActionName + "/queryAll";
		form.submit();
	}
	function exportExcel_onClick(){  //把当前页面的记录导出到Excel
    	form.isPrint.value="1";  //赋值隐藏值isPrint为1
    	form.target="_blank";  //target为新弹出页面
    	form.submit();
    	form.target="_self";  //清空target值
    	form.isPrint.value="";  //清空isPrint的值
	}
	function exportExcelTotal_onClick(){  //把所有的记录导出到Excel
    	form.isPrint.value="1";
    	form.VENUS_PAGE_SIZE_KEY.value = "99999999";  //定义很大的翻页值
    	form.target="_blank";
		form.submit();
 		form.target="_self";	
		form.VENUS_PAGE_SIZE_KEY.value = "";
    	form.isPrint.value="";
   	}
	function toAdd_onClick() {  //到增加记录页面
		form.action="<%=request.getContextPath()%>/jsp/authority/au/loginlog" + rmJspPath + "/insertLoginLog.jsp";
		form.submit();
	}
	function toAdvancedQuery_onClick() {  //到高级查询页面
		form.action="<%=request.getContextPath()%>/jsp/authority/au/loginlog" + rmJspPath + "/queryLoginLog.jsp";
		form.submit();
	}
	function refresh_onClick(){  //刷新本页
		form.submit();
	}
	function detail_onClick(thisId){  //实现转到详细页面
		form.id.value = thisId;  //赋值thisId给隐藏值id
		form.action="<%=request.getContextPath()%>/" + rmActionName + "/detail";
		form.submit();
	}
	function clear_onClick() {  //清空查询条件
		form.login_id.value = "";
		form.name.value = "";
		form.login_ip.value = "";
		form.login_time_from.value = "";
		form.login_time_to.value = "";
	}

</script>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.List_pages' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<form name="form" method="post">
<div id="auDivParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild0',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')" >&nbsp;<fmt:message key='venus.authority.Conditional_Query' bundle='${applicationAuResources}' /><%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Log_log")%>
		</td>
	</tr>
</table>
</div>

<div id="auDivChild0"> 
<table class="table_div_content">
<tr><td>
	<table class="table_noFrame" width="100%">
		<tr>
			<td width="15%" align="right"><fmt:message key='venus.authority.Login_Account0' bundle='${applicationAuResources}' /></td>
			<td width="35%" align="left">
				<input type="text" class="text_field" name="login_id" inputName="<fmt:message key='venus.authority.Login_Account0' bundle='${applicationAuResources}' />" maxLength="25"/>
			</td>
			<td width="15%" align="right"><fmt:message key='venus.authority.User_Name' bundle='${applicationAuResources}' /></td>
			<td width="35%" align="left"><input type="text" class="text_field" name="name" inputName="<fmt:message key='venus.authority.User_Name' bundle='${applicationAuResources}' />" maxLength="25"/></td>
		</tr>
		<tr>
			<td align="right">IP<fmt:message key='venus.authority.Address' bundle='${applicationAuResources}' /></td>
			<td align="left">
				<input type="text" class="text_field" name="login_ip" inputName="IP<fmt:message key='venus.authority.Address' bundle='${applicationAuResources}' />" maxLength="25"/>
			</td>
			<td align="right"><fmt:message key='venus.authority.Login_time' bundle='${applicationAuResources}' /></td>
			<td align="left">
				<input type="text" class="text_field_half_reference_readonly" id="login_time_from" name="login_time_from" inputName="<fmt:message key='venus.authority.Login_time' bundle='${applicationAuResources}' />"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/icon/reference.gif" onClick="javascript:getYearMonthDay('login_time_from','<venus:base/>/');"/>&nbsp;<fmt:message key='venus.authority.To0' bundle='${applicationAuResources}' />&nbsp;<input type="text" class="text_field_half_reference_readonly" id="login_time_to" name="login_time_to" inputName="<fmt:message key='venus.authority.Login_time' bundle='${applicationAuResources}' />"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/icon/reference.gif" onClick="javascript:getYearMonthDay('login_time_to','<venus:base/>/');"/>
			</td>
		</tr>
		<tr>
			<td/>
			<td/>
			<td/>
			<td><input name="button_ok" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Inquiry' bundle='${applicationAuResources}' />" onClickTo="javascript:simpleQuery_onClick()">
			<input name="button_clear" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Empty' bundle='${applicationAuResources}' />" onClick="javascript:clear_onClick()"></td>
		</tr>
	</table>

</td></tr>
</table>
</div>
					
<div id="auDivParent1"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild1',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')">&nbsp;<%=ILoginLogConstants.TABLE_NAME_CHINESE%><fmt:message key='venus.authority.List' bundle='${applicationAuResources}' />
		</td>
		<td> 
			<table align="right">
				<tr>
					<td class="button_ellipse" id="td_deleteMulti_onClick" onClick="javascript:deleteMulti_onClick();" title="<fmt:message key='venus.authority.Delete_the_selected_record' bundle='${applicationAuResources}' />"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/delete.gif" class="div_control_image"><fmt:message key='venus.authority.Delete' bundle='${applicationAuResources}' /></td>
					<!--td class="button_ellipse" id="td_findCheckbox_onClick" onClick="javascript:findCheckbox_onClick();" title="跳转到修改所选的某条记录"><img src="<%=request.getContextPath()%>/images/icon/modify.gif" class="div_control_image">修改</td-->
					<td class="button_ellipse" id="td_refresh_onClick" onClick="javascript:refresh_onClick();" title="<fmt:message key='venus.authority.Refresh_the_current_page' bundle='${applicationAuResources}' />"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/refresh.gif" class="div_control_image"><fmt:message key='venus.authority.Refresh' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" id="td_exportExcel_onClick" onClick="javascript:exportExcel_onClick();" title="<fmt:message key='venus.authority.To_check_out_the_record_of_the_current_flip_Export_to' bundle='${applicationAuResources}' />Excel"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/output.gif" class="div_control_image"><fmt:message key='venus.authority.Export_this_page' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" id="td_exportExcelTotal_onClick" onClick="javascript:exportExcelTotal_onClick();" title="<fmt:message key='venus.authority.Check_out_all_the_records_to_export_to' bundle='${applicationAuResources}' />Excel"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/output.gif" class="div_control_image"><fmt:message key='venus.authority.Export_All' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse" id="td_deleteAll_onClick" onClick="javascript:deleteAll_onClick();" title="<fmt:message key='venus.authority.Delete_all_records' bundle='${applicationAuResources}' />"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/delete.gif" class="div_control_image"><fmt:message key='venus.authority.Remove_All' bundle='${applicationAuResources}' /></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>

<div id="auDivChild1"> 
<table class="table_div_content2" width="100%" >
	<tr>
		<td>
		<layout:collection onRowDblClick="detail_onClick(getRowHiddenId())"  name="beans" id="rmBean" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0">
			
			<layout:collectionItem width="30" title="<input type='checkbox' pdType='control' control='checkbox_template'/>" style="text-align:center;">
				<bean:define id="rmValue" name="rmBean" property="id"/>
				<bean:define id="rmDisplayName" name="rmBean" property="id"/>
				<input type="checkbox" name="checkbox_template" value="<%=rmValue%>" displayName="<%=rmDisplayName%>" sequenceValue="<venus:sequence/>"/>
			</layout:collectionItem>
			
			<layout:collectionItem width="30"  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sequence") %>' style="text-align:center;">
				<venus:sequence/>
				<bean:define id="rmValue" name="rmBean" property="id"/>
				<input type="hidden" signName="hiddenId" value="<%=rmValue%>"/>
			</layout:collectionItem>
			

			<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Account") %>' property="login_id" sortable="false"/>
			
			<layout:collectionItem width="80" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Name0") %>' property="name" sortable="false"/>
			
			<layout:collectionItem title="IP<fmt:message key='venus.authority.Address' bundle='${applicationAuResources}' />" property="login_ip" sortable="false"/>
			
			<layout:collectionItem width="60" title="IE<fmt:message key='venus.authority.Version' bundle='${applicationAuResources}' />" property="ie" sortable="false"/>
			
			<layout:collectionItem width="110" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Operating_system") %>' property="os" sortable="false"/>
			
			<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Host_name") %>' property="host" sortable="false"/>
			
			<!--layout:collectionItem width="60" title="退出类型" property="logout_type" sortable="false"/-->
			
			<layout:collectionItem width="150" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Login_time") %>' property="login_time" sortable="false">
				<bean:define id="rmValue" name="rmBean" property="login_time"/>
				<%=StringHelperTools.prt(rmValue, 19)%>
			</layout:collectionItem>
			
	        <layout:collectionItem width="150" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Exit_time") %>'  sortable="false">
	            <logic:notEmpty name="rmBean" property="logout_time">
	                <bean:define id="logout_time" name="rmBean" property="logout_time"/>
	                <%=StringHelperTools.prt(logout_time,19)%>
	            </logic:notEmpty>
	        </layout:collectionItem>			
			
			<layout:collectionItem title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Login_Status") %>' property="login_state" sortable="false"/>
			

		</layout:collection>
		
		<!-- 下边这句是翻页, 如果去掉就不带翻页了,同时注意Action中也要调整方法 -->
		<jsp:include page="/jsp/include/page.jsp" />
		</td>
	</tr>
</table>
</div>

<input type="hidden" name="id" value="">
<input type="hidden" name="isPrint" value="">
<!-- 参照显示层 -->
<div id="iframeDialog" style="display:none"></div>

</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>
<%  //表单回写
	if(request.getAttribute(ILoginLogConstants.REQUEST_WRITE_BACK_FORM_VALUES) != null) {  //如果request中取出的bean不为空
		out.print("<script language=\"javascript\">\n");  //输出script的声明开始
		out.print(VoHelperTools.writeBackMapToForm((java.util.Map)request.getAttribute(ILoginLogConstants.REQUEST_WRITE_BACK_FORM_VALUES)));  //输出表单回写方法的脚本
		out.print("writeBackMapToForm();\n");  //输出执行回写方法
		out.print("</script>");  //输出script的声明结束
	}
%>

