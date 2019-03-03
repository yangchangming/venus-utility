<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="udp.ewp.tools.helper.EwpVoHelper" %>
<%@ page import="udp.ewp.tools.helper.EwpStringHelper" %>
<%@ page import="udp.ewp.util.EnumTools" %>
<%@ page import="udp.ewp.link.model.Link" %>
<%@ page import="udp.ewp.link.util.ILinkConstants" %>
<%@ include file="/jsp/include/global.jsp" %>
<%  //判断是否为修改页面
	Link resultVo = null;
	boolean isModify = false;
	if(request.getParameter("isModify") != null) {
		isModify = true;
		if(request.getAttribute(ILinkConstants.REQUEST_BEAN) != null) {
  		    resultVo = (Link)request.getAttribute(ILinkConstants.REQUEST_BEAN);
  		}
	}

			  request.setAttribute("linkCategoryMap",EnumTools.getSortedEnumMap(EnumTools.LINKCATEGORY));

%>
<html>
<fmt:bundle basename="udp.ewp.ewp_resource" >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script language="javascript">
	function insert_onClick(){
		form.action="<%=request.getContextPath()%>/LinkAction.do?cmd=insert";
		form.submit();
	}

  	function update_onClick(id){
        if(checkAllForms()){
            if(!getConfirm()) {
                return false;
            }
            form.action="<%=request.getContextPath()%>/LinkAction.do?cmd=update";
            form.submit();
        }

	}

    function cancel_onClick(){
        form.action="<%=request.getContextPath()%>/LinkAction.do?cmd=queryAll";
        form.submit();
    }
</script>
</head>
<body>
<script language="javascript">
    if(<%=isModify%>)
        writeTableTop('<fmt:message key="udp.ewp.link_modify" />','<venus:base/>/themes/<venus:theme/>/');
    else
        writeTableTop('<fmt:message key="udp.ewp.link_add" />','<venus:base/>/themes/<venus:theme/>/');
</script>
<form name="form" method="post">

<div id="ccParent1">
<table class="table_div_control">
	<tr>
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<%=request.getContextPath()%>/')">
             <input name="button_save" class="button_ellipse" type="button" value='<fmt:message key="save" bundle="${applicationResources}"/>' onClickTo="javascript:<%=isModify?"update_onClick()":"insert_onClick()"%>">
            <input name="button_cancel" class="button_ellipse" type="button" value='<fmt:message key="cancel" bundle="${applicationResources}"/>'  onClick="javascript:cancel_onClick()" >
		</td>
	</tr>
</table>
</div>

<div id="ccChild1">
<table class="table_div_content">
<tr><td>
	<table class="table_div_content_inner">
		<tr>
			<td align="right"><span class="style_required_red">* </span><fmt:message key="udp.ewp.link.category"/></td>
			<td align="left">
					<select name="category" id="category"  validate="notNull;"  inputName='<fmt:message key="udp.ewp.link.category"/>' >
	                            <logic:iterate id="tempType" name="linkCategoryMap">
	                                    <option value="<bean:write name="tempType" property="key"/>" ><bean:write name="tempType" property="value" /></option>
	                            </logic:iterate>
	                        </select>
			</td>
			<td align="right"></td>
			<td align="left"></td>
		</tr>
		<tr>
			<td align="right"><span class="style_required_red">* </span><fmt:message key="udp.ewp.link.linkTitle"/></td>
			<td align="left">
				<input type="text" class="text_field" name="title" inputName='<fmt:message key="udp.ewp.link.linkTitle"/>' value="" maxLength="33"  validate="notNull;"/>
			</td>
			<td align="right"></td>
			<td align="left"></td>
		</tr>
		<tr>
			<td align="right"><span class="style_required_red">* </span><fmt:message key="udp.ewp.link.content"/></td>
			<td colspan="3" align="left">
				<textarea class="textarea_limit_words" cols="60" rows="5" name="content" inputName='<fmt:message key="udp.ewp.link.content"/>' maxLength="1000"  validate="notNull;"></textarea>
				<span id="charaterLimitSpan"></span>
			</td>
		</tr>
		<tr>
			<td align="right"><span class="style_required_red">* </span><fmt:message key="udp.ewp.link.website"/></td>
			<td align="left">
				<input type="text" class="text_field" name="website" inputName='<fmt:message key="udp.ewp.link.website"/>' value="" maxLength="66"  validate="notNull;" />
			</td>
			<td align="right"></td>
			<td align="left"></td>
		</tr>
		<tr>
			<td align="right"></td>
			<td align="left"></td>
			<td align="right"></td>
			<td align="left"></td>
		</tr>
	</table>
</td></tr>
</table>
</div>

<input type="hidden" name="id" value="">
<input type="hidden" name="create_date" />

</form>
</fmt:bundle>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>
<script language="javascript">
<%
    if(isModify) {
        out.print(EwpVoHelper.writeBackMapToForm(EwpVoHelper.getMapFromVo(resultVo)));
    }
%>
</script>
