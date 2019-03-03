<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="udp.searchengine.test.vo.TestVo"%>
	
<fmt:bundle basename="udp.searchengine.searchengine_resource" prefix="udp.searchengine.">	
<html>
<head>
<title><fmt:message key='query_template'/></title>
<script language="javascript">
function addIndex(){
		if(checkTest1AndTest2()==false){
			return;
		}
		frm.action="<venus:base/>/TestAction.do?cmd=onAddIndex";
		frm.submit();
}
function updateIndex(){
		if(checkTest1AndTest2()==false){
			return;
		}
		frm.action="<venus:base/>/TestAction.do?cmd=onUpdateIndex";
		frm.submit();
}

function checkTest1AndTest2(){
	if(jQuery.trim(jQuery("[name='test1']").val())==""){
		alert("<fmt:message key='please_input_test1'/>");
		return false;
	}
	if(jQuery.trim(jQuery("[name='test2']").val())==""){
		alert("<fmt:message key='please_input_test2'/>");
		return false;
	}
	return true;
}
</script>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='test_list'/>",'<venus:base/>/themes/<venus:theme/>//');
</script>
	<%
		TestVo vo=(TestVo)request.getAttribute("result");
		
	
	
	%>
<form action="<venus:base/>/TestAction.do?cmd=onAddIndex" method="post" name="frm">
test1: <textarea name="test1" cols="80" rows="10"><%if(vo.getNeirong1()!=null){out.println(vo.getNeirong1());}%></textarea>
<br><br>

test2: <textarea name="test2" cols="80" rows="10"><%if(vo.getNeirong2()!=null){out.println(vo.getNeirong2());}%></textarea>
<br><br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

<%if(vo.getId()==null){
	
	
	
	%>
<input name="tijiao" type="button"  class="button_ellipse" value="<fmt:message bundle='${applicationResources}' key='save' />" onClick="addIndex()">
<%}else{%>

<input name="quxiao" type="button"  class="button_ellipse" value="<fmt:message bundle='${applicationResources}' key='save' />" onClick="updateIndex()">
<%}%>
<input name="cancelButton" type="button" class="button_ellipse" value="<fmt:message bundle='${applicationResources}' key='cancel' />" onClick="javascript:history.go(-1)" />
<input name="oid" type="hidden" value="<%=vo.getId()%>" >
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>//');
</script>
</body>
</fmt:bundle>
