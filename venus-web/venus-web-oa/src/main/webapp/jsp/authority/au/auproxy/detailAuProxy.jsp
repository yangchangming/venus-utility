<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.oa.util.VoHelperTools" %>
<%@ page import="venus.oa.util.StringHelperTools" %>
<%@ page import="venus.oa.organization.auparty.vo.PartyVo" %>
<%@ page import="venus.oa.organization.auparty.util.IConstants" %>
<%@ page import="venus.oa.util.GlobalConstants" %>
<%@ page import="venus.oa.helper.OrgHelper" %>
<%@ page import="java.util.List" %>
<%  //取出本条记录
	PartyVo resultVo = null;  //定义一个临时的vo变量
	resultVo = (PartyVo)request.getAttribute(IConstants.REQUEST_BEAN_VALUE);  //从request中取出vo, 赋值给resultVo
	VoHelperTools.replaceToHtml(resultVo);  //把vo中的每个值过滤
  
  	//判断是否为只读页面
	boolean isReadOnly = false;  //定义变量,标识本页面是否修改(或者新增)
	if(request.getParameter("isReadOnly") != null) {  //如果从request获得参数"isReadOnly"不为空
		isReadOnly = true;  //赋值isReadOnly为true
	}
	
	//判断是否已经关联用户
	boolean isConnectUser = false;
	List al = (List)request.getAttribute("linkedUserList");
	isConnectUser = 0==al.size();
	
	//获取关系id
	String relationId = request.getParameter("relationId");
	String vCode = OrgHelper.getRelationCodeByRelationId(relationId);
%>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>VENUS<fmt:message key='venus.authority.Organizational_competence_system' bundle='${applicationAuResources}' /></title>
<script language="javascript">
	function toAddProxy_onClick() {  //到增加记录页面
		var relationId = document.form.relationId.value;
		window.location="<%=request.getContextPath()%>/jsp/authority/au/auproxy/insertAuProxy.jsp?parentRelId="+relationId;
	}
	function toAddUser_onClick() {  //到增加记录页面
		var id = document.form.id.value;
		var refPath = "<venus:base/>/jsp/authority/au/auproxy/userRefFrame.jsp?proxyId="+id;

        showIframeDialog("iframeDialog","", refPath, 650, 600);
		
		//var rtObj = window.showModalDialog(refPath, new Object(),'dialogHeight=600px;dialogWidth=1000px;resizable:yes;status:no;scroll:auto;');
	}
	
	function toAddUser_returnValue(rtObj){
		if(rtObj != undefined) {
			var partyIds = rtObj;
			var relationId = document.form.relationId.value;
			form.action="<%=request.getContextPath()%>/auProxy/addRelation?partyId="+partyIds+"&parentRelId="+relationId;
			form.submit();
		}
	}
	
	function toDelUser_onClick() {
		var ids = findSelections("checkbox_template","id");  //取得多选框的选择项
		if(ids == null)	{  //如果ids为空
			alert("<fmt:message key='venus.authority.Please_Select_Records' bundle='${applicationAuResources}' />!")
			return;
		}
		if(confirm("<fmt:message key='venus.authority.It_completely_remove_the_data_' bundle='${applicationAuResources}' />")) {  //如果用户在确认对话框按"确定"
	    	form.action="<%=request.getContextPath()%>/auProxy/deleteMulti?ids=" + ids;
	    	form.submit();
		}
	}
	function toSort_onClick() {  //到排序页面
		var parentRelId = document.form.AuProxyId.value;
		window.location="<%=request.getContextPath()%>/jsp/authority/sample/AuProxy/sort.jsp?parentRelId="+parentRelId;
	}
	function find_onClick(){  //直接点到修改页面
		form.action="<%=request.getContextPath()%>/auProxy/find";
		form.submit();
	}
	function delete_onClick(){  //直接点删除单条记录
		if(!confirm("<fmt:message key='venus.authority.It_completely_remove_the_agent_' bundle='${applicationAuResources}' />")) {
			return false;
		}
		form.action="<%=request.getContextPath()%>/auProxy/delete";
		form.target="_parent";
		form.submit();
	}
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
	function auth_onButton(){
	   <%if(!isConnectUser){%>
	   //alert("已经关联用户，无法修改授权，请先解除代理与用户的关联！");
        showIframeObjDialog(window.parent.jQuery("#iframeDialog"),"", "<%=request.getContextPath()%>/jsp/authority/au/auauthorize/viewAllAu.jsp?vCode=<%=vCode%>&dialogFlag=dialog", 1000, 600);
	   <%}else{%>

        showIframeObjDialog(window.parent.jQuery("#iframeDialog"),"", "<%=request.getContextPath()%>/auParty/detailList?pageFlag=4&id=<%=resultVo.getId()%>&dialogFlag=dialog", 1000, 600);
	   <%}%>
    }
</script>
</head>
<body>
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Detailed_Page' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
</script>
<form name="form" method="post">
<table class="table_noFrame">
	<tr>
		<td>
			<%
			if(isReadOnly) {
			%>
				<input name="button_back" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Return' bundle='${applicationAuResources}' />"  onclick="javascript:returnBack()" >
			<%
			}else {
			%>
				<%if(GlobalConstants.getRelaID_proxy().equals(request.getParameter("relationId"))){ %>
				<input name="button_add" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Add_proxy' bundle='${applicationAuResources}' />" onclick="javascript:toAddProxy_onClick();" title="<fmt:message key='venus.authority.Add' bundle='${applicationAuResources}' /><%=resultVo.getName()%><fmt:message key='venus.authority.Agent' bundle='${applicationAuResources}' />">
				<% }else{%>
				<input name="button_update" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Modification_agent' bundle='${applicationAuResources}' />" onClick="javascript:find_onClick();" title="<fmt:message key='venus.authority.Modify' bundle='${applicationAuResources}' /><%=resultVo.getName()%><fmt:message key='venus.authority.Basic_information' bundle='${applicationAuResources}' />">
				<input name="button_delete" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Remove_agent' bundle='${applicationAuResources}' />" onClick="javascript:delete_onClick();" title="<fmt:message key='venus.authority.Delete' bundle='${applicationAuResources}' /><%=resultVo.getName()%>">
				<%if(isConnectUser){ %>
				<input name="button_authority" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Agent_authorization' bundle='${applicationAuResources}' />" onClick="javascript:auth_onButton();" title="<%=resultVo.getName()%><fmt:message key='venus.authority.Agent_authorization' bundle='${applicationAuResources}' />">
				<input name="button_add" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Associated_user' bundle='${applicationAuResources}' />"  onclick="javascript:toAddUser_onClick();" title="<fmt:message key='venus.authority.To' bundle='${applicationAuResources}' /><%=resultVo.getName()%><fmt:message key='venus.authority.Associated_user' bundle='${applicationAuResources}' />">
				<%}else{ %>
				<input name="button_authority" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.See_license' bundle='${applicationAuResources}' />" onClick="javascript:auth_onButton();" title="<%=resultVo.getName()%><fmt:message key='venus.authority.See_license' bundle='${applicationAuResources}' />">
				<input name="button_add" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Remove_user' bundle='${applicationAuResources}' />"  onclick="javascript:toDelUser_onClick();" title="<fmt:message key='venus.authority.Delete' bundle='${applicationAuResources}' /><%=resultVo.getName()%><fmt:message key='venus.authority.Has_been_associated_with_the_user' bundle='${applicationAuResources}' />">
				<%}%>
				<%}%>
				<!--input name="button_sort" class="button_ellipse" type="button" value="排序" onClick="javascript:toSort_onClick();"-->
			<%
			}
			%>
		</td>
	</tr>
</table>

<div id="auParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auChild0',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')"><fmt:message key='venus.authority.Agent_Details' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>

<div id="auChild0"> 
<table class="table_div_content">
	<tr>
		<td>
			<table class="table_div_content_inner">
				<tr>
					<td align="right"><fmt:message key='venus.authority.Agent_Name_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getName())%></td>
				</tr>
				<%if(!GlobalConstants.getRelaID_proxy().equals(request.getParameter("relationId"))){ %>
				<tr>
                    <td align="right"><fmt:message key='venus.authority.Create_by_' bundle='${applicationAuResources}' /></td>
                    <td align="left"><%=StringHelperTools.prt(OrgHelper.getPartyVoByID(resultVo.getOwner_org()).getName())%></td>
                </tr>
                <tr>
                    <td align="right"><fmt:message key='venus.authority.Remarks_' bundle='${applicationAuResources}' /></td>
                    <td colspan="3" align="left"><%=StringHelperTools.prt(resultVo.getRemark())%></td>
                </tr>
                <%}%>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Created_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getCreate_date(),19)%></td>
				</tr>
				<%if(!GlobalConstants.getRelaID_proxy().equals(request.getParameter("relationId"))){ %>
                <tr>
					<td align="right"><fmt:message key='venus.authority.Modified_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getModify_date(),19)%></td>
				</tr>
				<%}%>
			</table>
		</td>
	</tr>
</table>
</div>
<%if(!GlobalConstants.getRelaID_proxy().equals(request.getParameter("relationId"))){ %>                
<div id="auParent1"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auChild1',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')"><fmt:message key='venus.authority.Has_been_associated_with_the_user_list' bundle='${applicationAuResources}' />
		</td>
	</tr>
</table>
</div>

<div id="auChild1"> 
<table align=center class="table_div_content">
	<tr>
		<td>
			<layout:collection name="linkedUserList" id="linkedUserList" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >
				<layout:collectionItem width="30" title="" style="text-align:center;">
					<bean:define id="partyRelationId" name="linkedUserList" property="id"/>
					<input type="radio" name="checkbox_template" value="<%=partyRelationId%>"/>
				</layout:collectionItem>
				<layout:collectionItem width="5%" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sequence") %>' style="text-align:center;">
					<venus:sequence/>
				</layout:collectionItem>	
				<layout:collectionItem width="25%" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Number") %>' property="partyid" />
				<layout:collectionItem width="20%" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Name") %>' property="name"/>			
				<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Mailbox") %>' property="email" />
			</layout:collection>
		</td>
	</tr>
</table>
</div>
<%}%>
<!--
enable_status
enable_date
partytype_id
partytype_keyword
is_real
is_inherit
email
owner_org
-->
<input type="hidden" name="id" value="<%=resultVo.getId()%>">
<input type="hidden" name="relationId" value="<%=request.getParameter("relationId")%>">
<input type="hidden" name="name" value="<%=resultVo.getName()%>">
</form>
<!-- 参照显示层 -->
<div id="iframeDialog" style="display:none"></div>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
<%@page import="venus.authority.helper.OrgHelper;"%>
</html>
	

