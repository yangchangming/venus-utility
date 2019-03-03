<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.authority.util.VoHelperTools" %>
<%@ page import="venus.authority.au.aufunctree.vo.AuFunctreeVo" %>
<%@ page import="venus.authority.au.aufunctree.util.IAuFunctreeConstants" %>
<%  //判断是否为修改页面
	boolean isModify = false;  //定义变量,标识本页面是否修改(或者新增)
	if(request.getParameter("isModify") != null) {  //如果从request获得参数"isModify"不为空
		isModify = true;  //赋值isModify为true
	}
	
	String parent_code = request.getParameter("total_code");
	if(parent_code == null) {  //如果从request获得参数"total_code"为空
		return;
	}
	String parent_type = request.getParameter("parent_type");
%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ranmin-based architecture project</title>
<script language="javascript">
	function insert_onClick(){  //插入单条数据
		if(form.type[0].checked && form.type[1].value=="<%=parent_type%>") {//在按钮下添加菜单
			alert("<fmt:message key='venus.authority.Can_not_be_added_under_the_menu_button_' bundle='${applicationAuResources}' />");
			return false;
		}
    	form.action="<%=request.getContextPath()%>/auFunctree/insert";
    	form.target="_parent";
	    form.submit();
	}
  	function update_onClick(id){  //保存修改后的单条数据
    	if(!getConfirm()) {  //如果用户在确认对话框中点"取消"
  			return false;
		}
	    form.action="<%=request.getContextPath()%>/auFunctree/update";
	    form.target="_parent";
	    form.type[0].disabled = false;
	    form.type[1].disabled = false;
    	form.submit();
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

<div id="auDivParent1"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild1',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')"><%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Modify"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Added")%><%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Function_menu")%>
		</td>
	</tr>
</table>
</div>

<div id="auDivChild1"> 
<table class="table_div_content">
<tr><td> 
	<table class="table_div_content_inner">
		<tr>
			<td width="70" align="right">&nbsp;</td>
			<td align="left">&nbsp;</td>
		</tr>
		<%if(!isModify){%> <!--如果是新增页面-->
		<tr>
			<td align="right"><fmt:message key='venus.authority.Parent_node' bundle='${applicationAuResources}' /></td>
			<td align="left">
				<input type="text" class="text_field_readonly" name="parent_name" inputName="<fmt:message key='venus.authority.Parent_node' bundle='${applicationAuResources}' />" value="<%=request.getParameter("parent_name")%>" maxlength="30" />
			</td>
		</tr>
		<tr>
			<td align="right"><span class="style_required_red">* </span><fmt:message key='venus.authority.Node_type' bundle='${applicationAuResources}' /></td>
			<td align="left">
				<input type="radio" name="type" value="0" checked ><fmt:message key='venus.authority.Function_menu' bundle='${applicationAuResources}' />
			    <input type="radio" name="type" value="2"><fmt:message key='venus.authority.Page_button' bundle='${applicationAuResources}' />
			</td>
		</tr>
		<%}else {%>
		<tr>
			<td align="right"><fmt:message key='venus.authority.Node_type' bundle='${applicationAuResources}' /></td>
			<td align="left">
				<input type="radio" name="type" value="0" disabled ><fmt:message key='venus.authority.Function_menu' bundle='${applicationAuResources}' />
			    <input type="radio" name="type" value="2" disabled ><fmt:message key='venus.authority.Page_button' bundle='${applicationAuResources}' />
			</td>
		</tr>
		<%}%>
		<tr>
			<td align="right"><span class="style_required_red">* </span><fmt:message key='venus.authority.Node_name' bundle='${applicationAuResources}' /></td>
			<td align="left">
				<input type="text" class="text_field" name="name" inputName="<fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' />" value="" maxLength="20" validate="notNull;isSearch"/>
			</td>
		</tr>
		<tr>
			<td align="right"><fmt:message key='venus.authority.Node_ID' bundle='${applicationAuResources}' /></td>
			<td align="left">
				<input type="text" class="text_field" name="keyword" inputName="<fmt:message key='venus.authority.Node_ID' bundle='${applicationAuResources}' />" value="" maxLength="20" validate="isSearch"/>
			</td>
		</tr>
		<tr>
			<td align="right"><fmt:message key='venus.authority.Actual_link' bundle='${applicationAuResources}' /></td>
			<td align="left">
				<input type="text" class="text_field" name="url" inputName="<fmt:message key='venus.authority.Actual_link' bundle='${applicationAuResources}' />" value="" maxLength="500"/>
				<input type="checkbox" id="is_ssl" name="is_ssl" value="1" >SSL
				<input type="checkbox" id="is_public" name="is_public" value="1" ><fmt:message key='venus.authority.Open' bundle='${applicationAuResources}' />
			</td>
		</tr>
		<tr>
			<td align="right"><fmt:message key='venus.authority.Help' bundle='${applicationAuResources}' /></td>
			<td align="left">
				<textarea class="textarea_limit_words" cols="60" rows="5" name="help" maxLength="150" id="helpId" validate="isSearch"></textarea>
			</td>
		</tr>
		</table>
</td></tr>
</table>
</div>
<input type="hidden" name="code" value="">			<!--自编码--> 
<input type="hidden" name="parent_code" value="<%=parent_code%>"><!--父编码--> 
<input type="hidden" name="total_code" value="">	<!--全编码-->     
<input type="hidden" name="hot_key" value="">   	<!--快捷键-->  
<input type="hidden" name="is_leaf" value="1">      <!--是否叶子-->  
<input type="hidden" name="type_is_leaf" value="1"> <!--类型内是否叶子-->  
<input type="hidden" name="order_code" value=""> 	<!--排序编码-->  
<input type="hidden" name="create_date" value=""> 	<!--添加时间-->  
<input type="hidden" name="modify_date" value=""> 	<!--修改时间-->  
<input type="hidden" name="id" value="">			<!--主键--> 
</form>			
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>
<%  //取出要修改的那条记录，并且回写表单
	if(isModify) {  //如果本页面是修改页面
  		out.print("<script language=\"javascript\">\n");  //输出script的声明开始
  		AuFunctreeVo resultVo = null;  //定义一个临时的vo变量
  		if(request.getAttribute(IAuFunctreeConstants.REQUEST_BEAN_VALUE) != null) {  //如果request中取出的bean不为空
  			resultVo = (AuFunctreeVo)request.getAttribute(IAuFunctreeConstants.REQUEST_BEAN_VALUE);  //从request中取出vo, 赋值给resultVo
  		}
  		if(resultVo != null) {  //如果vo不为空
			out.print(VoHelperTools.writeBackMapToForm(VoHelperTools.getMapFromVo(resultVo)));  //输出表单回写方法的脚本
			out.print("writeBackMapToForm();\n");  //输出执行回写方法
			if("1".equals(resultVo.getIs_ssl())){
			    out.print("document.getElementById('is_ssl').checked=true;\n");
			}
			out.print("document.getElementById('is_ssl').value=1;\n");
			if("1".equals(resultVo.getIs_public())){
                out.print("document.getElementById('is_public').checked=true;\n");
            }
            out.print("document.getElementById('is_public').value=1;\n");
		}
		out.print("</script>");  //输出script的声明结束
  	}
%>

