<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.oa.login.tools.OnlineUserVo" %>
<%@ page import="venus.oa.util.StringHelperTools" %>
<%@ page import="venus.oa.helper.LoginHelper"%>
<%@ page import="java.util.List" %>
<jsp:useBean id="onlineuser" class="venus.authority.login.tools.OnLineUser" scope="application"/>
<%
	String ids = request.getParameter("ids");
	if(ids!=null && ids.length()>0) {
		String session_ids[] = ids.split(","); 
		for(int i=0; i<session_ids.length; i++) {
			onlineuser.forceOffline(session_ids[i]);//强制下线
		}
	}
	
	String login_id = request.getParameter("login_id");
	String name = request.getParameter("name");
	if(login_id==null) 
		login_id = "";
	if(name==null) 
		name = "";
	List onlineUserList = onlineuser.queryOnlineUserList(login_id,  name);
	
	String currentUser = LoginHelper.getLoginId(request);//获得当前登录账号
%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<script language="javascript">

	function findSelections(checkboxName, idName) {  //从列表中找出选中的id值列表
		var elementCheckbox = document.getElementsByName(checkboxName);  //通过name取出所有的checkbox
		var number = 0;  //定义游标
		var ids = null;  //定义id值的数组
		for(var i=0;i<elementCheckbox.length;i++){  //循环checkbox组
			if(elementCheckbox[i].checked) {  //如果被选中
				number += 1;  //游标加1
				if(ids == null) {
					ids = new Array(0);
				}
				ids.push(elementCheckbox[i].value);  //加入选中的checkbox
			}
		}
		return ids;
	}
	function refresh_onClick(){  //刷新本页
		form.action="<%=request.getContextPath()%>/jsp/login/onlineUserList.jsp";
		form.submit();
	}
	function simpleQuery_onClick(){  //模糊查询
		form.action="<%=request.getContextPath()%>/jsp/login/onlineUserList.jsp";
		form.submit();
  	}
  	function forceOffline(){  //强制下线
 		var ids = findSelections("checkbox_template","id");  //取得多选框的选择项
		if(ids == null)	{  //如果ids为空
			alert("<fmt:message key='venus.authority.Select_User' bundle='${applicationAuResources}' />!")
			return;
		}
		var currentUser = "<%=currentUser%>";
		for(var i=0; i<ids.length; i++) {
			var thisHidden = getLayoutHiddenObjectById(ids[i]);
			var is_admin = jQuery(thisHidden).attr("is_admin");
			var login_id = jQuery(thisHidden).attr("login_id");
			if(is_admin=="true") {
				alert("<fmt:message key='venus.authority.You_do_not_have_permission_to_force_the_Super_Administrator_offline' bundle='${applicationAuResources}' />!")
				return;
			}
			if(login_id==currentUser) {
				alert("<fmt:message key='venus.authority.Can_not_force_your_downline' bundle='${applicationAuResources}' />!")
				return;
			}
		}
		var msg = ids.length==1?"<fmt:message key='venus.authority.Whether_the_force_the_user_offline_' bundle='${applicationAuResources}' />":"<fmt:message key='venus.authority.Whether_these_users_offline_mandatory_' bundle='${applicationAuResources}' />";
		if(confirm(msg)) {  //如果用户在确认对话框按"确定"
	    	form.action="<%=request.getContextPath()%>/jsp/login/onlineUserList.jsp?ids="+ ids;
	    	form.submit();
		}
	}

</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><fmt:message key='venus.authority.Online_user_list' bundle='${applicationAuResources}' /></title>
</head>

<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Online_user_list' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<FORM name="form" method="post">
<div id="auDivParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild0',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')" >&nbsp;<fmt:message key='venus.authority.Conditional_queries_online' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>

<div id="auDivChild0"> 
<table class="table_div_content">
<tr><td>
	<table class="table_noFrame" width="100%">
		<tr>
			<td width="15%" align="right"><fmt:message key='venus.authority.Login_Account0' bundle='${applicationAuResources}' /></td>
			<td width="35%" align="left">
				<input type="text" class="text_field" name="login_id" inputName="<fmt:message key='venus.authority.Login_Account0' bundle='${applicationAuResources}' />" value="<%=login_id%>" maxLength="25"/>
			</td>
			<td width="15%" align="right"><fmt:message key='venus.authority.User_Name' bundle='${applicationAuResources}' /></td>
			<td width="35%" align="left"><input type="text" class="text_field" name="name" inputName="<fmt:message key='venus.authority.User_Name' bundle='${applicationAuResources}' />" value="<%=name%>" maxLength="25"/></td>
		</tr>
		<tr>
			<td/>
			<td/>
			<td/>
			<td><input name="button_ok" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Inquiry' bundle='${applicationAuResources}' />" onClickTo="javascript:simpleQuery_onClick()">
			<input type="reset" name="clear" class="button_ellipse" value='<fmt:message key="clear" bundle="${applicationResources}" />'>
			</td>
		</tr>
	</table>

</td></tr>
</table>
</div>
					
<div id="auDivParent1"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild1',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')">&nbsp;<fmt:message key='venus.authority.Online_user_list' bundle='${applicationAuResources}' />
		</td>
		<td> 
			<table align="right">
				<tr>
					<td class="button_ellipse"  id="td_forceOffline" onClick="javascript:forceOffline();" title="<fmt:message key='venus.authority.The_selected_user_forced_offline' bundle='${applicationAuResources}' />"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/delete.gif" class="div_control_image"><fmt:message key='venus.authority.Forced_offline' bundle='${applicationAuResources}' /></td>
					<td class="button_ellipse"  id="td_refresh_onClick" onClick="javascript:refresh_onClick();" title="<fmt:message key='venus.authority.Refresh_the_current_page' bundle='${applicationAuResources}' />"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/refresh.gif" class="div_control_image"><fmt:message key='venus.authority.Refresh' bundle='${applicationAuResources}' /></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>
<div id="auDivChild1"> 
<table class="table_div_content2" width="100%" >
	<tr>
		<td>
			<div style="width=100%;overflow-x:visible;overflow-y:visible;">
				<table cellspacing="0" cellpadding="0" border="0" align="center" width="100%" class="listCss">
					<tr>
						<td valign="top">
							<table cellspacing="1" cellpadding="1" border="0" width="100%">
								<tr valign="top">
									<th class="listCss" ></th>
									<th class="listCss" width="30"><fmt:message key='venus.authority.Sequence' bundle='${applicationAuResources}' /></th>
									<th class="listCss"  ><fmt:message key='venus.authority.Login_Account0' bundle='${applicationAuResources}' /></th>
									<th class="listCss"  ><fmt:message key='venus.authority.User_Name' bundle='${applicationAuResources}' /></th>
									<th class="listCss"  >IP<fmt:message key='venus.authority.Address' bundle='${applicationAuResources}' /></th>
									<th class="listCss"  >IE<fmt:message key='venus.authority.Version' bundle='${applicationAuResources}' /></th>
									<th class="listCss"  ><fmt:message key='venus.authority.Operating_system' bundle='${applicationAuResources}' /></th>
									<th class="listCss" ><fmt:message key='venus.authority.Host_name' bundle='${applicationAuResources}' /></th>
									<th class="listCss" width="140"><fmt:message key='venus.authority.Login_time' bundle='${applicationAuResources}' /></th>
								</tr>
				      			<%
								for(int i=0;  i<onlineUserList.size();  ) {
									OnlineUserVo vo= (OnlineUserVo)onlineUserList.get(i);
									i++;
								%>
								<tr>
									<td class="listCss" align="center"><input type="radio" name="checkbox_template" value="<%=vo.getSession_id()%>"/></td>
									<td class="listCss" align="center"><%=i%><input type="hidden" signName="hiddenId" value="<%=vo.getSession_id()%>" login_id="<%=vo.getLogin_id()%>" is_admin="<%=vo.getIsAdmin()%>"/></td>
									<td class="listCss" ><%=StringHelperTools.prt(vo.getLogin_id())%></td>
									<td class="listCss" ><%=StringHelperTools.prt(vo.getName())%></td>
									<td class="listCss" ><%=StringHelperTools.prt(vo.getLogin_ip())%></td>
									<td class="listCss" ><%=StringHelperTools.prt(vo.getIe())%> </td>
									<td class="listCss" ><%=StringHelperTools.prt(vo.getOs())%></td>
									<td class="listCss" ><%=StringHelperTools.prt(vo.getHost())%></td>
									<td class="listCss" ><%=StringHelperTools.prt(vo.getLogin_time(), 19)%></td>
								</tr>	
								<%			
								}
								%>
							</table>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
</table>
</div>
</FORM>
<input type="hidden" name="id" value="">
<input type="hidden" name="queryCondition" value="">
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>

