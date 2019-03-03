<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.authority.au.aufunctree.bs.IAuFunctreeBs"%>
<%@ page import="venus.authority.au.aufunctree.vo.AuFunctreeVo" %>
<%@ page import="venus.authority.au.aufunctree.util.IAuFunctreeConstants" %>
<%@ page import="venus.frames.mainframe.util.Helper"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>

<%
try {
	String parent_code = request.getParameter("total_code");
	
	IAuFunctreeBs bs = (IAuFunctreeBs) Helper.getBean(IAuFunctreeConstants.BS_KEY);
	List lChild = bs.queryByCondition("parent_code='"+parent_code+"'","ORDER_CODE");//子节点列表
	String htmStr = "";
	for(Iterator itlChild = lChild.iterator(); itlChild.hasNext(); ) {
		AuFunctreeVo vo = (AuFunctreeVo) itlChild.next();
		htmStr += "<option value='"+vo.getId()+"'>"+vo.getName()+"</option>\n";				
	} 
%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Function_menu_sort' bundle='${applicationAuResources}' /></title>
<script language="javascript">
<!--
function movebox(box,value,text,pos0,direction)
{
	if(pos0 == box.options.length-1){
		SetButtonEnabledState('bt_move_down',false);	
	}else{
		SetButtonEnabledState('bt_move_down',true);
	}
	if(pos0 == 0){
		SetButtonEnabledState('bt_move_up',false);	
	}else{
		SetButtonEnabledState('bt_move_up',true);	
	}
	for(var i=0; i<box.options.length; i++){
		if(i==pos0){
			value0 = box.options[i].value;
			text0 = box.options[i].text;
			box.options[i].value=value;
	        box.options[i].text=text;
			if(direction == "up"){
		        box.options[i+1].value=value0;
		        box.options[i+1].text=text0;
		    }
			if(direction == "down"){
				box.options[i-1].value=value0;
		        box.options[i-1].text=text0;
		    }
		}else{
			box.options[i].value=box.options[i].value;
        	box.options[i].text=box.options[i].text;
        }
	}
}
function move_up(box){
	var pos = 0;
	var direction = "up";
	for(var i=0; i<box.options.length; i++){
		if(box.options[i].selected && box.options[i].value!=""){
			if(i-1>=0){
				pos = i-1;
				movebox(box,box.options[i].value,box.options[i].text,pos,direction);
				break;
			}
		}
	}
	for(var i=0; i<box.options.length; i++){
		if(i==pos){
			box.options[i].selected = true;
		}else{
			box.options[i].selected = false;
		}
	}

}
function move_down(box){
	var direction = "down";
	var pos = box.options.length-1;
	for(var i=0; i<box.options.length; i++){
		if(box.options[i].selected && box.options[i].value!=""){
			if(i+1<box.options.length){
				pos = i+1;
				movebox(box,box.options[i].value,box.options[i].text,pos,direction);
				break;
			}
		}
	}
	for(var i=0; i<box.options.length; i++){
		if(i==pos){
			box.options[i].selected = true;
		}else{
			box.options[i].selected = false;
		}
	}

}

function SetButtonEnabledState(sButtonID, bEnabledState){
	var e = new Error();
	try {
		var oBtn = eval("document.all." + sButtonID);
		oBtn.disabled = !bEnabledState;
		oBtn.style.cursor = ((bEnabledState) ? "hand" : "default");
	}catch (e) {
		return;
	}
	
	try {
		var oBtnImage = eval("document.all." + sButtonID + "_Image");
		oBtnImage.filters.Gray.enabled   = !bEnabledState;
		oBtnImage.filters.alpha.enabled  = !bEnabledState;
	}catch (e) {
	}
}

function sort_onClick() {
    
	var box = document.form1.sortList;
	if(box.length<2) {
        alert("<fmt:message key='venus.authority.No_items_can_be_sorted_' bundle='${applicationAuResources}' />");
        return false;
    }
	
	for (var i=box.options.length-1; i>=0; i--) {
		box.options[i].selected = true;
	}
   	document.form1.action= "<%=request.getContextPath()%>/auFunctree/sort";
   	document.form1.target="_parent";
   	document.form1.submit();
}

function setEnabledState_onClick(){
	SetButtonEnabledState('bt_move_down',true);
	SetButtonEnabledState('bt_move_up',true);
}
//-->
</script>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Sort_page' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>

<form name="form1" method="post" target="_parent">

<table class="table_noFrame">
	<tr>
		<td>
			<input name="button_save" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Save' bundle='${applicationAuResources}' />" onclickto="javascript:sort_onClick()">
			<input name="button_cancel" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Cancel' bundle='${applicationAuResources}' />"  onClick="javascript:returnBack()" >
		</td>
	</tr>
</table>

<div id="auDivParent1"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild1',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')"><%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Function_menu")%><fmt:message key='venus.authority.Sort' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>

<div id="auDivChild1"> 
	<table class="table_div_content">
		<tr>
			<td> 
				<table class="table_div_content_inner">
					<tr>
						<td width="20%" align="right">&nbsp;</td>
						<td width="60%" align="left">&nbsp;</td>
						<td width="20%" align="left">&nbsp;</td>
					</tr>
					<tr>
						<td align="right"><fmt:message key='venus.authority.Parent_node' bundle='${applicationAuResources}' /></td>
						<td align="left" colspan="2">
							<input type="text" class="text_field_readonly" name="parent_name" inputname="<fmt:message key='venus.authority.Parent_organization' bundle='${applicationAuResources}' />" value="<%=request.getParameter("parent_name")%>" maxlength="30" />
							<input type="hidden" name="parent_code" value="<%=parent_code%>"/>
						</td>
					</tr>
					<tr> 
						<td align="right"><fmt:message key='venus.authority.Node_list' bundle='${applicationAuResources}' /></td>
						<td align="left">			
							<select name="sortList" size="20" multiple style="width:204px;" onClick="setEnabledState_onClick();">
							<%
								out.print(htmStr);
							%>
						    </select>
						</td>
					    <td>
							<p>
					            <input name="bt_move_up" type="button" id="bt_move_up" class="button_ellipse" value="<fmt:message key='venus.authority.Move' bundle='${applicationAuResources}' />" onClick="javascript:move_up(document.form1.sortList)">
					        </p>
					        <p>
					            <input name="bt_move_down" type="button" id="bt_move_down" class="button_ellipse" value="<fmt:message key='venus.authority.Down' bundle='${applicationAuResources}' />" onClick="javascript:move_down(document.form1.sortList)">
				            </p>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</div>
</form>			
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>
<%
}catch(Exception e) {
	e.printStackTrace();
}
%>

