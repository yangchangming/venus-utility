<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/include/global.jsp" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>template-menu</title>
</head>
<script language="javascript">
	function simpleTemplate_onClick() {  
			form.action="<%=request.getContextPath()%>/TemplateAction.do";
			form.cmd.value="queryAll";
			form.target="body";
			form.submit();
	}
	function complexTemplate_onClick() {  
			form.action="<%=request.getContextPath()%>/MasterTableAction.do";
			form.cmd.value="queryAll";
			form.target="body";
			form.submit();
	}
	function bookstoreTemplate_onClick(){
			form.action="<%=request.getContextPath()%>/BookStore.do?cmd=queryAllBooks";
			form.cmd.value="queryAllBooks";
			form.target="body";
			form.submit();
	}
	function logon_onClick(){
			form.action="<%=request.getContextPath()%>/jsp/bookstore/logon.jsp";
			form.target="body";
			form.submit();
			
	}
	function logout_onClick(){
			form.action="<%=request.getContextPath()%>/BookStore.do?cmd=logout";
			form.target="body";
			form.submit();
	}
</script>
<style type="text/css">
<!--
body {
	background-color: #004d9f;
}
-->
</style>
<body>
<form name="form" method="post" action="<%=request.getContextPath()%>/TemplateAction.do">
<input type="hidden" name="cmd" value="">

    <table cellpadding="0" cellspacing="0" class="table_div_content_frame" height="30" width="100%">
      <tr>
        <td width="11" valign="bottom" align="left"><img src="<%=request.getContextPath()%>/images/templatestyle/main0_15.gif" width="11" height="11"></td>
        <td >
		<table align="left">
				<tr> 
					<td class="button_ellipse" onClick="javascript:simpleTemplate_onClick();"><img src="<venus:base/>/images/icon/arrow.gif" class="div_control_image">单表列表控件</td>
					<td class="button_ellipse" onClick="javascript:complexTemplate_onClick();" ><img src="<venus:base/>/images/icon/arrow.gif" class="div_control_image">主子表列表控件(表头固定)</td>				
				</tr>
			</table>
		</td>
		<td>
				<table align="right">
				    <tr>
						<td>
						        <logic:empty name="accountInSession">游客</logic:empty>
								<logic:notEmpty name="accountInSession"><bean:define id="name" name="accountInSession" property="name"></bean:define><%=name%></logic:notEmpty>，您好！
						</td>
						<td class="button_ellipse" onClick="javascript:bookstoreTemplate_onClick();" ><img src="<venus:base/>/images/icon/arrow.gif" class="div_control_image">网上书店</td>
						<td class="button_ellipse" onClick="javascript:logon_onClick();" width="40" align="center"><span  id="logon">登录</span></td>
						<td class="button_ellipse" onClick="javascript:logout_onClick();"  width="40" align="center"><span id="logout">注销</span></td>
					</tr>
				</table>
		</td>
        <td width="11" align="right" valign="bottom"><img src="<%=request.getContextPath()%>/images/templatestyle/main0_18.gif" width="11" height="11"></td>
      </tr>
    </table>
</form>

</body>
</html>