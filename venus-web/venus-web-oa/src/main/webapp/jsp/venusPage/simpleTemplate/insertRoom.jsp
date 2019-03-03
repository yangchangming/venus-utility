<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="java.util.*"%>
<%@ page import="venus.commons.xmlenum.EnumRepository" %>
<%@ page import="venus.commons.xmlenum.EnumValueMap" %>
<%@ page import="venus.frames.mainframe.util.VoHelper" %>

<%  //判断是否为修改页面
		boolean isModify = false;
		if(request.getParameter("isModify") != null) {
			isModify = true;
		}
		pageContext.setAttribute("isModify",isModify);
		EnumRepository er = EnumRepository.getInstance();
        er.loadFromDir();
        EnumValueMap roomTypeMap = er.getEnumValueMap("RoomType");
        EnumValueMap RoomIsEmptyMap = er.getEnumValueMap("RoomIsEmpty");
	%>
<fmt:bundle basename="udp.template.simple.resource" prefix="udp.template.simple.">
<title><%=isModify?"修改模板":"新增模板"%></title>
<script>

	function insert_onClick(){  //增加记录
		if(checkAllForms()){
    		form.action = "<venus:base/>/TemplateAction.do?cmd=insert";
    		form.submit();
    	}
  	}
  	function update_onClick(){
	  	if(checkAllForms()){
  			form.action = "<venus:base/>/TemplateAction.do?cmd=update";
  			form.submit();
  		}
  	}
  	function getBuildList() {
		showIframeDialog("iframeDialog","<fmt:message key="BuildingName"/>", "<venus:base/>/TemplateAction.do?cmd=queryBuild", 450, 260);
	}

    //保存时判断同一楼栋下房间是否已存在
    function isRoomExist(value, obj) {
        var flag;
        jQuery.ajax({
            type: "get", // 数据发送方式
            url:'<venus:base/>/TemplateAction.do?cmd=isRoomExist', // 后台处理程序
            dataType : "json", // 接受数据格式
            cache: false, //禁用缓存
            async:false, //同步方式
            data:{ build_Id:jQuery("input[name='build_Id']").val(),name:value, id:jQuery("input[name='id']").val()}, // 要传递的数据
            success:function(data) { //回传函数
                if(data) {
                    writeValidateInfo(i18n.room_exist(value,jQuery("input[name='build_name']").val()), obj);
                    flag = false;
                } else{
                    flag =  true;
                }
            }
        });
        return flag;
    }
</script>

</head>
<body>
<script language="javascript">

	if(<%=isModify%>)
	   	   writeTableTop('<fmt:message key="UpdatePage"/>','<venus:base/>/themes/<venus:theme/>/');
	 else
	   writeTableTop('<fmt:message key="InsertPage"/>','<venus:base/>/themes/<venus:theme/>/');
</script>

<form name="form" method="post">
<input type="hidden" name="id" value="">
<table class="table_noframe">
	<tr>
		<td valign="middle">
			<input name="button_save" type="button" class="button_ellipse" value='<fmt:message key="save" bundle="${applicationResources}"/>' onClick="javascript:<%=isModify?"update_onClick()":"insert_onClick()"%>;">
			<input name="button_cancel" type="button" class="button_ellipse" value='<fmt:message key="return" bundle="${applicationResources}"/>'  onClick="javascript:history.go(-1);">
		</td>
	</tr>
</table>

<div id="ccParent1"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">
			  <c:if test="${isModify}"><fmt:message key="UpdateRoom"/></c:if>
		      <c:if test="${isModify==false}"><fmt:message key="InsertRoom"/></c:if>
		          
		</td>
	</tr>
</table>
</div>

<div id="ccChild1"> 
<table class="table_div_content">
	<tr> 
		<td align="right" width="10%" nowrap><font color="red">*</font><fmt:message key="BuildingName"/></td>
		<td align="left">
			<input name="build_Id" type="hidden" value="">
			<input name="build_name" type="text" value="<layout:write name='buildvo' property='name'/>" class="text_field_reference" inputName='<fmt:message key="BuildingName"/>' validate="notNull" readonly="true"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/reference.gif" onClick="javascript:getBuildList();" class="img_1">
		</td>
	</tr>
	<tr>
		<td align="right"><font color="red">*</font><fmt:message key="RoomName"/></td>
		<td align="left">
            <input name="name" type="text" class="text_field" maxlength="25" inputName='<fmt:message key="RoomName"/>' validate="notNull;isRoomExist" value="">
        </td>
		</td>
	</tr>
	<tr>
		<td align="right"><font color="red">*</font><fmt:message key="BuildingArea"/></td>
		<td align="left">
            <input name="area" type="text" class="text_field" maxValue="9999999" inputName='<fmt:message key="BuildingArea"/>' validate="notNull;isNumeric<%--numberMax--%>" value="">
		</td>
	</tr>
		<tr>
		<td align="right"><fmt:message key="RoomType"/></td>
		<td align="left">
			<select name="type" >
				<%
					List al = roomTypeMap.getEnumList();
					for(int i=0;i<al.size();i++){%>
				<option value="<%=roomTypeMap.getValue(al.get(i).toString())%>"><%=al.get(i)%></option>
     			 <%}%>
			</select>
		</td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="RoomRate"/></td>
		<td align="left">
			<input name="price" type="text" class="text_field" inputName='<fmt:message key="RoomRate"/>' validate="isInteger" value="">
		</td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="IsVacant"/></td>
		<td align="left">
				<%
					List al1 = RoomIsEmptyMap.getEnumList();
					for(int i=0;i<al1.size();i++){%>
				<input name="is_Empty" type="radio" <%=(al1.get(i).toString().equals("否")||al1.get(i).toString().equals("No"))?"checked":""%> value="<%=RoomIsEmptyMap.getValue(al1.get(i).toString())%>"><%=al1.get(i)%>
     			 <%}%>
		</td>
	</tr>
	<tr>
		<td align="right"><font color="red">*</font><fmt:message key="CheckinDate"/></td>
		<td align="left">
			<input name="residing_Date" id="residing_Date" type="text" class="text_field_reference" inputName='<fmt:message key="CheckinDate"/>' validate="notNull" readonly="true" value=""><img src="<venus:base/>/themes/<venus:theme/>/images/icon/reference.gif" onclick="javacript:getYearMonthDay('residing_Date','<venus:base/>/');" class="img_1">
		
		</td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="Remark"/></td>
		<td align="left">
				<!-- textarea class="textarea_fckEditor" cols="30" skin="silver" rows="5" name="brief" inputName="name" maxLength="25" ></textarea-->
				<input name="brief" type="text" class="text_field" value="">
		</td>
	</tr>
</table>
</div>
</form>
<!-- 参照显示层 -->
<div id="iframeDialog" style="display:none">
</div>

</fmt:bundle>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>
</body>
</html>
<%  //表单回写
	if(request.getAttribute("writeBackFormValues") != null) {
		out.print("<script language=\"javascript\">\n");
		out.print(VoHelper.writeBackMapToForm((java.util.Map)request.getAttribute("writeBackFormValues")));
		out.print("writeBackMapToForm();\n");
		out.print("</script>");
	}
%>