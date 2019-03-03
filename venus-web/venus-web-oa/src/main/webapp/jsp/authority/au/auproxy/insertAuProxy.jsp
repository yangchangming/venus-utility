<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.authority.util.VoHelperTools" %>
<%@ page import="venus.authority.org.auparty.vo.PartyVo" %>
<%@ page import="venus.authority.org.auparty.util.IConstants" %>
<%  //判断是否为修改页面
	boolean isModify = false;  //定义变量,标识本页面是否修改(或者新增)
	if(request.getParameter("isModify") != null) {  //如果从request获得参数"isModify"不为空
		isModify = true;  //赋值isModify为true
	}

	//获取上级节点的关系表主键
	String parentRelId = request.getParameter("parentRelId");
	if(parentRelId==null) {
		if(isModify) {
			parentRelId = request.getParameter("relationId");
		}else {
			parentRelId = "";
		}
	}
%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>VENUS<fmt:message key='venus.authority.Organizational_competence_system' bundle='${applicationAuResources}' /></title>
<script language="javascript">
	function insert_onClick(){  //插入单条数据
    	form.action="<%=request.getContextPath()%>/auProxy/insert";
    	var parentRelId = form.parentRelId.value;
    	if(parentRelId!="")
    		form.target="_parent";
	    form.submit();
	}
  	function update_onClick(id){  //保存修改后的单条数据
	    form.action="<%=request.getContextPath()%>/auProxy/update";
	    var parentRelId = form.parentRelId.value;
    	if(parentRelId!="")
    		form.target="_parent";
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

<div id="ccParent1">
<table class="table_div_control">
	<tr>
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')"><%=isModify?venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Modify"):venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Added")%><fmt:message key='venus.authority.Proxy' bundle='${applicationAuResources}' />
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
					<td width="30%" align="right"><span class="style_required_red">* </span><fmt:message key='venus.authority.Agent_Name' bundle='${applicationAuResources}' /></td>
					<td width="70%" align="left">
						<input type="text" class="text_field" name="name" inputName="<fmt:message key='venus.authority.Agent_Name' bundle='${applicationAuResources}' />" validate="notNull;isSearch" value="" maxLength="300" />
						&nbsp;&nbsp;<fmt:message key='venus.authority.To_facilitate_the_maintenance_use_with_agents_on_the_name_' bundle='${applicationAuResources}' />  &nbsp;&nbsp;
					</td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Remarks' bundle='${applicationAuResources}' /></td>
					<td align="left">
						<textarea class="textarea_limit_words" cols="60" rows="5" name="remark" maxLength="500" maxLength="500"  id="remarkId"></textarea>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>
<input type="hidden" name="enable_status" value="1">
<input type="hidden" name="enable_date">
<input type="hidden" name="create_date">
<input type="hidden" name="modify_date">
<input type="hidden" name="partytype_id">
<input type="hidden" name="partytype_keyword">
<input type="hidden" name="is_real">
<input type="hidden" name="is_inherit">
<input type="hidden" name="email">
<input type="hidden" name="owner_org">

<input type="hidden" name="id" value="">
<input type="hidden" name="parentRelId" value="<%=parentRelId%>">
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>
<%  //取出要修改的那条记录，并且回写表单
	if(isModify) {  //如果本页面是修改页面
  		out.print("<script language=\"javascript\">\n");  //输出script的声明开始
  		PartyVo resultVo = null;  //定义一个临时的vo变量
  		if(request.getAttribute(IConstants.REQUEST_BEAN_VALUE) != null) {  //如果request中取出的bean不为空
  			resultVo = (PartyVo)request.getAttribute(IConstants.REQUEST_BEAN_VALUE);  //从request中取出vo, 赋值给resultVo
  		}
  		if(resultVo != null) {  //如果vo不为空
			out.print(VoHelperTools.writeBackMapToForm(VoHelperTools.getMapFromVo(resultVo)));  //输出表单回写方法的脚本
			out.print("writeBackMapToForm();\n");  //输出执行回写方法
		}
		out.print("</script>");  //输出script的声明结束
  	}
%>

