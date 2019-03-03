<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.authority.util.VoHelperTools" %>
<%@ page import="venus.authority.sample.employee.vo.EmployeeVo" %>
<%@ page import="venus.authority.sample.employee.util.IEmployeeConstants" %>
<%  //判断是否为修改页面
	boolean isModify = false;  //定义变量,标识本页面是否修改(或者新增)
	if(request.getParameter("isModify") != null) {
		isModify = true;
	}
	
	//获取上级节点的关系表主键，该主键不仅仅用来新增节点，而且还用来区分要跳转的页面
	String parentRelId = request.getParameter("parentRelId");//如果是组织机构管理页面跳转过来的且该页面为新增页面，该值不为空
	if(parentRelId==null) {
		if(isModify) {
			parentRelId = request.getParameter("relationId");//如果是组织机构管理页面跳转过来的且该页面为修改页面，该值不为空
			if(parentRelId==null) {
				parentRelId = "";//如果为空说明不是从组织机构管理页面跳转过来的，该值用来判断下一步要跳转的页面
			}		
		}else {
			parentRelId = "";//如果为空说明不是从组织机构管理页面跳转过来的，该值用来判断下一步要跳转的页面
		}
	}
%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script language="javascript">
	function insert_onClick(){  //插入单条数据
    	form.action="<%=request.getContextPath()%>/employee/insert";
 		var parentRelId = form.parentRelId.value;
    	if(parentRelId!="")
    		form.target="_parent";
	    form.submit();
	}
  	function update_onClick(id){  //保存修改后的单条数据
    	if(!getConfirm()) {  //如果用户在确认对话框中点"取消"
  			return false;
		}
	    form.action="<%=request.getContextPath()%>/employee/update";
	    var parentRelId = form.parentRelId.value;
    	if(parentRelId!="")
    		form.target="_parent";
    	form.submit();
	}
	function queryAll_onClick(){  //查询全部数据列表
		if (typeof(parent.frames["myList"]) == "undefined") {
			returnBack();
		} else {
			form.action="<%=request.getContextPath()%>/employee/queryAll";
			form.submit();			
		}
	}	
</script>
</head>
<body>
<script language="javascript">
	writeTableTop('<%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Modify_page"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.New_page")%>','<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<form name="form" method="post">

<table class="table_noFrame">
	<tr>
		<td>
			<input name="button_save" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Save' bundle='${applicationAuResources}' />" onClickTo="javascript:<%=isModify?"update_onClick()":"insert_onClick()"%>">
			<input name="button_cancel" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Cancel' bundle='${applicationAuResources}' />"  onClick="javascript:returnBack()" >
		</td>
	</tr>
</table>

<div id="ccParent1"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')"><%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Modify"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Added")%><%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Staff0")%>
		</td>
	</tr>
</table>
</div>

<div id="ccChild1"> 
<table class="table_div_content">
	<tr>
		<td> 
			<table class="table_div_content_inner">
				<tr>
					<td width="30%" align="right"><span class="style_required_red">* </span><fmt:message key='venus.authority.Name1' bundle='${applicationAuResources}' /></td>
					<td width="70%" align="left">
						<input type="text" class="text_field" name="person_name" inputName="<fmt:message key='venus.authority.Name0' bundle='${applicationAuResources}' />" validate="notNull;isSearch;" value="" maxLength="18" />
					</td>
				</tr>
				<%
				if( ! isModify && ( parentRelId==null || "".equals(parentRelId) ) ) {
				%>
				<tr>
					<td align="right"><span class="style_required_red">* </span><fmt:message key='venus.authority.Their_position_or_department_' bundle='${applicationAuResources}' /></td>
					<td align="left">
						<input type="text" class="text_field_reference_readonly" id="owner_party_name" name="owner_party_name" inputName="<fmt:message key='venus.authority.Their_position_or_department_' bundle='${applicationAuResources}' />" maxlength="50" validate="notNull;"  
						hiddenInputId="owner_party_id"/><input type="hidden" id="owner_party_id" name="owner_party_id"/><img class="refButtonClass" src="<venus:base/>/themes/<venus:theme/>/images/au/09.gif"
						onClick="javascript: try {getTypeChooseTree(new Array('owner_party_id', 'owner_party_name'),'<venus:base/>','radio','','1','id','3','1','department,position'); }catch(e){alert('<fmt:message key='venus.authority.Insufficient_privileges' bundle='${applicationAuResources}' />')}"/>
					</td>
				</tr>
				<%
				}
				%>
				<!--tr>
					<td align="right">员工编号</td>
					<td align="left">
						<input type="text" class="text_field" name="person_no" inputName="员工编号" value="" maxLength="50" />
					</td>
				</tr-->
				<tr>
					<td align="right"><fmt:message key='venus.authority.English_name' bundle='${applicationAuResources}' /></td>
					<td align="left">
						<input type="text" class="text_field" name="english_name" inputName="<fmt:message key='venus.authority.English_name0' bundle='${applicationAuResources}' />" validate="notChinese;isSearch;" value="" maxLength="50" />
					</td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Staff_category' bundle='${applicationAuResources}' /></td>
					<td align="left">
						<input type="text" class="text_field" name="person_type" inputName="<fmt:message key='venus.authority.Staff_category' bundle='${applicationAuResources}' />" validate="isSearch;" value="" maxLength="18" />
					</td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Sex' bundle='${applicationAuResources}' /></td>
					<td align="left">
						<input type="radio" name="sex" inputName="<fmt:message key='venus.authority.Sex0' bundle='${applicationAuResources}' />" value="1" checked ><fmt:message key='venus.authority.Male' bundle='${applicationAuResources}' />
						<input type="radio" name="sex" inputName="<fmt:message key='venus.authority.Sex0' bundle='${applicationAuResources}' />" value="0"><fmt:message key='venus.authority.Female' bundle='${applicationAuResources}' />
					</td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Mobile_telephone' bundle='${applicationAuResources}' /></td>
					<td align="left">
						<input type="text" class="text_field" name="mobile" inputName="<fmt:message key='venus.authority.Mobile_telephone' bundle='${applicationAuResources}' />" validate="isMobile;" value="" maxLength="50"/>
					</td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Fixed_telephone' bundle='${applicationAuResources}' /></td>
					<td align="left">
						<input type="text" class="text_field" name="tel" inputName="<fmt:message key='venus.authority.Fixed_telephone' bundle='${applicationAuResources}' />" validate="isTel;" value="" maxLength="50" />
					</td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.E_mail' bundle='${applicationAuResources}' /></td>
					<td align="left">
						<input type="text" class="text_field" name="email" inputName="<fmt:message key='venus.authority.E_mail' bundle='${applicationAuResources}' />" validate="isEmail;" value="" maxLength="300" />
					</td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Contact_Address' bundle='${applicationAuResources}' /></td>
					<td align="left">
						<input type="text" class="text_field" name="address" inputName="<fmt:message key='venus.authority.Contact_Address' bundle='${applicationAuResources}' />" validate="isSearch;" value="" maxLength="300" />
					</td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Post_Code' bundle='${applicationAuResources}' /></td>
					<td align="left">
						<input type="text" class="text_field" name="postalcode" inputName="<fmt:message key='venus.authority.Zip_Code' bundle='${applicationAuResources}' />" validate="isPostalCode;" value="" maxLength="6" />
					</td>
				</tr>
				<!--tr>
					<td align="right">备用字段</td>
					<td align="left">
						<input type="text" class="text_field" name="column1" inputName="备用字段" value="" maxLength="300" />
					</td>
				</tr>
				<tr>
					<td align="right">备用字段</td>
					<td align="left">
						<input type="text" class="text_field" name="column2" inputName="备用字段" value="" maxLength="300" />
					</td>
				</tr>
				<tr>
					<td align="right">备用字段</td>
					<td align="left">
						<input type="text" class="text_field" name="column3" inputName="备用字段" value="" maxLength="300" />
					</td>
				</tr-->
				<tr>
					<td align="right"><fmt:message key='venus.authority.Remarks' bundle='${applicationAuResources}' /></td>
					<td align="left">
						<textarea class="textarea_limit_words" cols="60" rows="5" name="remark" maxLength="500" id="remarkId"></textarea>
					</td>
				</tr>
				<!--tr>
					<td align="right" nowrap>可用状态</td>
					<td align="left">
						<input type="radio" name="enable_status" inputName="启用状态" value="1" checked >启用
						<input type="radio" name="enable_status" inputName="禁用状态" value="0">禁用
					</td>
				</tr-->
			</table>
		</td>
	</tr>
</table>
</div>
<input type="hidden" name="enable_status" value="1">
<input type="hidden" name="enable_date">
<input type="hidden" name="create_date">
<input type="hidden" name="modify_date">
            
<input type="hidden" name="id" value="">
<input type="hidden" name="parentRelId" value="<%=parentRelId%>">
<input type="hidden" name="person_no">
<!-- 参照显示层 -->
<div id="iframeDialog" style="display:none"></div>
</form>			
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>
<%  //取出要修改的那条记录，并且回写表单
	if(isModify) {  //如果本页面是修改页面
  		out.print("<script language=\"javascript\">\n");  //输出script的声明开始
  		EmployeeVo resultVo = null;  //定义一个临时的vo变量
  		if(request.getAttribute(IEmployeeConstants.REQUEST_BEAN_VALUE) != null) {  //如果request中取出的bean不为空
  			resultVo = (EmployeeVo)request.getAttribute(IEmployeeConstants.REQUEST_BEAN_VALUE);  //从request中取出vo, 赋值给resultVo
  		}
  		if(resultVo != null) {  //如果vo不为空
			out.print(VoHelperTools.writeBackMapToForm(VoHelperTools.getMapFromVo(resultVo)));  //输出表单回写方法的脚本
			out.print("writeBackMapToForm();\n");  //输出执行回写方法
		}
		out.print("</script>");  //输出script的声明结束
  	}
%>

