<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>

<fmt:bundle basename="udp.searchengine.searchengine_resource" prefix="udp.searchengine.">
<html>
<head>
<title><fmt:message key='search_engine_test_page'/></title>
<SCRIPT LANGUAGE="JScript">
	function toInitIndex_onClick() {  //到增加记录页面
			form.action="<venus:base/>/SearchEngineAction.do";
			form.cmd.value="initIndex";
			form.submit();
	}
</SCRIPT>
</head>
<body>
<fmt:message key='initialize_the_index_library_success'/>
</body>
</html>
</fmt:bundle>