<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/include/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="venus.oa.service.test.util.IConstants"%>
<%@ page import="venus.oa.service.test.vo.TestVo"%>
<%@ page import="venus.oa.util.VoHelperTools"%>
<%@ page import="java.beans.BeanInfo"%>
<%@ page import="java.beans.Introspector"%>
<%@ page import="java.beans.PropertyDescriptor"%>
<%@ page import="java.beans.Expression"%>
<%
        List list = (List) request.getAttribute(IConstants.PROJECT_VALUE);
        PropertyDescriptor[] pds = new PropertyDescriptor[0];
        if (list.size() > 0) {
            try {
                Object object = list.get(0);
                BeanInfo bi = Introspector.getBeanInfo(object.getClass());
                pds = bi.getPropertyDescriptors();
            } catch (java.beans.IntrospectionException e) {
            }
        }
		List tableList = (List) request.getAttribute(IConstants.TABLE_VALUE);
%>
<title><fmt:message key='venus.authority.Query_Template' bundle='${applicationAuResources}' /></title>
<script language="javascript">
function viewDetail(obj) {  //显示权限页面
	window.location="<venus:base/>/test/queryAll?tablename="+obj;
}
</script>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Check_list' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');
</script>

<form name="form" method="post"	action="<venus:base/>/test">
	<input type="hidden" name="cmd" value="">
	<input type="hidden" name="tablename" value="">
<div id="ccParent0">
<table class="table_div_control">
	<tr>
		<td><img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif"
			class="div_control_image"
			onClick="javascript:hideshow('ccChild0',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='venus.authority.Table_name' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>

<div id="ccChild0">
<table class="table_div_content">
<tr>
<td><fmt:message key='venus.authority.Select_the_table_name' bundle='${applicationAuResources}' />
<select name="table_name" onchange="javascript:viewDetail(this.value)">
	<%for(int i=0;i<tableList.size();i++){
		TestVo vo=(TestVo)tableList.get(i);
	%>
	<option value="<%=vo.getName()%>" <%if(vo.getName().equals((String)request.getAttribute("table"))) out.write("selected");%>><%=vo.getName()%></option>
	<%}%>
</select>
</td>
</tr>
</table>
<div id="ccParent1">
<table class="table_div_control">
	<tr>
		<td><img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif"
			class="div_control_image"
			onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='venus.authority.Field_name' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>

<div id="ccChild1">
<table class="table_div_content">
	<tr>
		<td>
		<table border="1">
			<tr>
				<%for (int i = 0; i < pds.length; i++) {
            // Get property name
            String propName = pds[i].getName();
            if ("class".equals(propName))
                    continue;
        %>
				<td><%=propName%></td>
				<%}%>
			</tr>
			<%for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            %>
			<tr>
				<%for (int j = 0; j < pds.length; j++) {
                // Get property name
                String propName = pds[j].getName();
                if ("class".equals(propName))
                    continue;
                propName = propName.substring(0, 1).toUpperCase() + propName.substring(1);
                Expression expr = new Expression(o, "get" + propName, new Object[0]);
                expr.execute();
                String s = "";
                if (expr.getValue() != null)
                    s = expr.getValue().toString();%>

				<td><%=s%></td>
				<%}%>
			</tr>
			<%}%>
		</table>
		</td>
	</tr>
	<tr>
		<td><jsp:include page="/jsp/include/page.jsp" /></td>
	</tr>



</table>
</div>
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>
</body>
</html>
<% //表单回写
        if (request.getAttribute("writeBackFormValues") != null) {
            out.print("<script language=\"javascript\">\n");
            out.print(VoHelperTools.writeBackMapToForm((java.util.Map) request.getAttribute("writeBackFormValues")));
            out.print("writeBackMapToForm();\n");
            out.print("</script>");
        }
%>

