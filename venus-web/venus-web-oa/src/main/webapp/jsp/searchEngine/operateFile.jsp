<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import = "java.util.List,java.util.Iterator,java.util.ArrayList"%>
<%@ page import="udp.searchengine.vo.ResultSetVo" %>
<%@page import="venus.frames.i18n.util.LocaleHolder"%>

<fmt:bundle basename="udp.searchengine.searchengine_resource" prefix="udp.searchengine.">
<html>
<head>
<title><fmt:message key='upload_file'/></title>
<script>
	function save_onClick(){  //文件上传
		if(frm.filename.value==null || frm.filename.value==""){
			alert("<fmt:message key='upload_file_is_empty'/>");
			return;
		}
		frm.action="<venus:base/>/SearchEngineAction.do?cmd=onFileUpDownLoad";
    	//form.cmd.value = "onFileUpDownLoad";
    	frm.submit();
  	}

	function deleteMulti_onClick(){  //从多选框物理删除多条记录
 		var elementCheckbox = document.getElementsByName("checkbox_template");
		var number = 0;
		var ids = "";
		for(var i=0;i<elementCheckbox.length;i++){
			if(elementCheckbox[i].checked) {
				number += 1;
				ids += elementCheckbox[i].value + ",";
			}
		}
		//if(ids.length>0) {
		//	ids = ids.substr(0,ids.length-1);	
		//	alert(ids);
		//}
		if(number == 0) {
	  		alert("<fmt:message key='please_select_a_record'/>")
	  		return;
		}
		if(confirm("<fmt:message key='is_completely_remove_the_data'/>")) {
	    	form.action="<venus:base/>/SearchEngineAction.do?cmd=onDelFile&fileId=" + ids;
    		form.submit();
		}
	}


</script>

</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='index_operation'/>",'<venus:base/>/themes/<venus:theme/>//');
</script>

<form name="frm" enctype="multipart/form-data" method="post" action="<venus:base/>/SearchEngineAction.do?cmd=onQueryFileIndex">


<!-- 查询开始 -->
<div id="ccParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" onClick="javascript:hideshow('ccChild0',this,'<venus:base/>/themes/<venus:theme/>/')">
			<fmt:message key='upload_file_and_add_the_index'/>
		</td>
	</tr>
</table>
</div>

<div id="ccChild0"> 
<table class="table_div_content" width="700em">
<tr nowrap>
	<td align="right" nowrap width="100em"><fmt:message key='upload_file_path'/></td>
	<td width="200em" nowrap>
		<input name="filename" type="file" size="40" class="text_field_reference" inputName="<fmt:message key='upload_file_path'/>" validate="isSearch"  onkeydown="return false;"/>
	</td>
	<td align="left" width="400em" nowrap><input name="save"  class="button_ellipse" type="button" onClick="javascript:save_onClick();" value="<fmt:message key='upload_file_and_save_the_index'/>"/></td>
	
</tr>

</table>

</div>
</form>

<form name="form" method="post" action="<venus:base/>/SearchEngineAction.do?cmd=onQueryFileIndex">
<div id="ccParent1"> 
<table class="table_div_control">
	<tr > 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='details_form'/>
		</td>
		<td> 
			<table align="right">
				<tr> 
					<td class="button_ellipse" onClick="javascript:deleteMulti_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/delete.gif" class="div_control_image"><fmt:message bundle='${applicationResources}' key='delete'/></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>

<div id="ccChild1"> 
<table class="table_div_content">
	<tr>
		<td>
		<layout:collection name="fileUpLoad" id="fileUpLoad1" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >
			<layout:collectionItem width="5%" style="text-align:center;" title="<input type='checkbox' pdType='control' control='checkbox_template'/>" >
			<bean:define id="fileUpLoad3" name="fileUpLoad1" property="id"/>
				<input type="checkbox" name="checkbox_template" value="<%=fileUpLoad3%>"/>
			</layout:collectionItem>

			<layout:collectionItem width="5%"  title='<%=LocaleHolder.getMessage("sequence") %>' style="text-align:center;">
				<venus:sequence/>
			</layout:collectionItem>
			<layout:collectionItem width="90%" title='<%=LocaleHolder.getMessage("udp.searchengine.file_name") %>' property="path"/>
			
		</layout:collection>
		
		<jsp:include page="/jsp/include/page.jsp" />
		</td>
	</tr>
</table>
</div>
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>//');
</script>
</body>
</html>
</fmt:bundle>
