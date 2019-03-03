<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="venus.authority.service.sys.vo.SysParamVo"%>
<%@ page import="venus.authority.util.GlobalConstants"%>
<%
	SysParamVo funcTreeMode = GlobalConstants.getSysParam(GlobalConstants.FUNCTREEMODE);
	 if (funcTreeMode != null && "1".equals(funcTreeMode.getValue())) { //功能树全加载
%>	     
<script>
	window.location.href="<%=request.getContextPath()%>/jsp/tree.jsp";
</script>
<% } else { //功能树懒加载 %>
<script>
	window.location.href="<%=request.getContextPath()%>/jsp/authority/lazytree/lazytree.jsp?rootXmlSource=<%=request.getContextPath()%>/jsp/authority/au/aufunctree/lazyTreeRootXmlData.jsp?root_code=101";
</script>		     
<% } %>

