<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="udp.ewp.tools.helper.EwpVoHelper" %>
<%@ page import="udp.ewp.hotwords.model.HotWords" %>
<%@ page import="udp.ewp.hotwords.util.IHotWordsConstants" %>
<%@ include file="/jsp/include/global.jsp" %>
<%
HotWords resultVo = null;
	boolean isModify = false;
	if(request.getParameter("isModify") != null) {
		isModify = true;
		if(request.getAttribute(IHotWordsConstants.REQUEST_BEAN) != null) {
  		    resultVo = (HotWords)request.getAttribute(IHotWordsConstants.REQUEST_BEAN);
  		}
	}
%>
<html>
<fmt:bundle basename="udp.ewp.ewp_resource" >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script language="javascript">
	function insert_onClick(){  //插入单条数据
		form.action="<venus:base/>/HotWordsAction.do?cmd=insert";
		form.submit();
	}

  	function update_onClick(id){  //保存修改后的单条数据
    	if(!getConfirm()) {  //如果用户在确认对话框中点"取消"
  			return false;
		}
	    form.action="<venus:base/>/HotWordsAction.do?cmd=update";
    	form.submit();
	}

    function cancel_onClick(){  //取消后返回列表页面
        form.action="<venus:base/>/HotWordsAction.do?cmd=queryAll";
        form.submit();
    }
</script>
</head>
<body>
<script language="javascript">
    if(<%=isModify%>)
         writeTableTop('<fmt:message key="udp.ewp.hotwords_modify_hotwords" />','<venus:base/>/themes/<venus:theme/>/');
    else
        writeTableTop('<fmt:message key="udp.ewp.hotwords_add_hotwords" />','<venus:base/>/themes/<venus:theme/>/');
</script>
<form name="form" method="post">
<input type="hidden" name="id" value="">
<div id="ccParent1"> 
<table class="table_div_control">
	<tr> 
		<td>
		     <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')"> 
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
			<td align="right"><span class="style_required_red">* </span><fmt:message key="udp.ewp.hotwords.name"/></td>
			<td align="left">
				<input type="text" class="text_field" name="name" inputName="<fmt:message key="udp.ewp.hotwords.name"/>" value="" maxLength="50"  validate="notNull;" />
			</td>
			<td align="right"></td>
			<td align="left"></td>
		</tr>
        <tr>
			<td align="right"><span class="style_required_red">* </span><fmt:message key="udp.ewp.hotwords.link"/></td>
			<td align="left">
				<input type="text" class="text_field" name="link" inputName="<fmt:message key="udp.ewp.hotwords.link"/>" value="" maxLength="500"  validate="notNull;" />
			</td>
			<td align="right"></td>
			<td align="left"></td>
		</tr>
        <tr>
            <td align="right"><span class="style_required_red">* </span><fmt:message key="udp.ewp.hotwords.enable_status"/></td>
            <td align="left">
                <input type="radio" class="radio" name="enableStatus" value="1" checked="true">是
                <input type="radio" class="radio" name="enableStatus" value="0">否
            </td>
            <td align="right"></td>
            <td align="left"></td>
        </tr>
	</table>
</td></tr>
</table>
</div>
</form>
</fmt:bundle>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>
<script language="javascript">
<% 
    if(isModify) {  //如果本页面是修改页面
        out.print(EwpVoHelper.writeBackMapToForm(EwpVoHelper.getMapFromVo(resultVo)));  //输出表单回写方法的脚本
    }
%>
</script>
