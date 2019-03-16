<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.oa.organization.auparty.util.IConstants" %>
<%@ page import="venus.oa.organization.auparty.vo.PartyVo" %>
<%@ page import="venus.oa.util.StringHelperTools" %>
<%@ page import="venus.oa.util.VoHelperTools" %>
<%@ page import="java.util.List" %>
<%@ page import="venus.oa.organization.aupartyrelation.vo.AuPartyRelationVo" %>
<%--!
	List empList = new ArrayList();
--%>
<%--
	String ids = "";
    List list = (List) request.getAttribute("linkedUserList");
    for (int i = 0; i < list.size(); i++) {
		AuPartyRelationVo vo = (AuPartyRelationVo) list.get(i);
		ids += "'" + vo.getPartyid() + "',";
    }
    if (ids != null && !"".equals(ids)) {
		ids = ids.substring(0, ids.length() - 1);
		String strsql = "SELECT * FROM AU_EMPLOYEE  WHERE ID IN (" + ids + ")";
		empList = ProjTools.getCommonBsInstance().doQuery(strsql,new RowMapper() {
			    public Object mapRow(ResultSet rs, int no) throws SQLException {
				HashMap map = new HashMap();
				map.put("id", rs.getString("id"));
				map.put("sex", rs.getString("sex"));
				map.put("email", rs.getString("email"));
				return map;
			    }
			});
    }
--%>
<%--!
	//根据partyid找到人员vo
    public Map getEmployee(Object partyId) {
		Map empMap = new HashMap();
		for (Iterator i = empList.iterator(); i.hasNext();) {
		    //获取节点的数据
		    empMap = (HashMap) i.next();
		    if (((String)partyId).equals((String)empMap.get("id")))
				break;
		}
		return empMap;
    }
--%>
<%
    //取出本条记录
    PartyVo resultVo = null; //定义一个临时的vo变量
    resultVo = (PartyVo) request.getAttribute(IConstants.REQUEST_BEAN_VALUE); //从request中取出vo, 赋值给resultVo
    VoHelperTools.replaceToHtml(resultVo); //把vo中的每个值过滤

    //判断是否为只读页面
    boolean isReadOnly = false; //定义变量,标识本页面是否修改(或者新增)
    if (request.getParameter("isReadOnly") != null) { //如果从request获得参数"isReadOnly"不为空
		isReadOnly = true; //赋值isReadOnly为true
    }
%>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>VENUS<fmt:message key='venus.authority.Organizational_competence_system' bundle='${applicationAuResources}' /></title>
<script language="javascript">
	function toAddRole_onClick() {  //到增加记录页面
		var relationId = document.form.relationId.value;
		window.location="<%=request.getContextPath()%>/jsp/authority/au/aurole/insertAuRole.jsp?parentRelId="+relationId;
	}
	
	
function returnValueNameObj(rtObj){//组织机构参照返回赋值
			var partyIds = "";
			for(var i=0; i<rtObj.length-1; i++) {
				partyIds += rtObj[i] + ",";
			}
			partyIds += rtObj[rtObj.length-1];
			
			var relationId = document.form.relationId.value;
			form.action="<%=request.getContextPath()%>/auRole/addMultiRelation?partyIds="+partyIds+"&parentRelId="+relationId;
			form.submit();
}

	function toAddUser_onClick() {  //到增加记录页面
		var id = document.form.id.value;
		var refPath = "<venus:base/>/jsp/authority/au/aurole/userRefFrame.jsp?roleId="+id;
		var iframeObj=window.parent.jQuery("#iframeDialog");
		 showIframeObjDialog(iframeObj,venus.authority.Reference_page, refPath, 800, 600);

	}
	function toDelUser_onClick() {
		var ids = findSelections("checkbox_template","id");  //取得多选框的选择项
		if(ids == null)	{  //如果ids为空
			alert("<fmt:message key='venus.authority.Please_Select_Records' bundle='${applicationAuResources}' />!")
			return;
		}
		if(confirm("<fmt:message key='venus.authority.It_completely_remove_the_data_' bundle='${applicationAuResources}' />")) {  //如果用户在确认对话框按"确定"
	    	form.action="<%=request.getContextPath()%>/auRole/deleteMulti?ids=" + ids;
	    	form.submit();
		}
	}
	function toSort_onClick() {  //到排序页面
		var parentRelId = document.form.AuRoleId.value;
		window.location="<%=request.getContextPath()%>/jsp/authority/sample/AuRole/sort.jsp?parentRelId="+parentRelId;
	}
	function find_onClick(){  //直接点到修改页面
		form.action="<%=request.getContextPath()%>/auRole/find";
		form.submit();
	}
	function delete_onClick(){  //直接点删除单条记录
		if(!confirm("<fmt:message key='venus.authority.It_completely_remove_the_role_' bundle='${applicationAuResources}' />")) {
			return false;
		}
		form.action="<%=request.getContextPath()%>/auRole/delete";
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
	function simpleQuery_onClick(){  //简单的模糊查询
		if(checkAllForms()){
			form.action = "<%=request.getContextPath()%>/auRole/detail";
    		form.submit();
    	}
  	}	
	function clear_onClick() { //清空查询条件
		form.name.value = "";
	}  	
	function search_onKeyDown() {//回车后执行页面查询功能
		if (event.keyCode == 13) { 
			simpleQuery_onClick();
		}
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
			<% if (isReadOnly) {%>
				<input name="button_back" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Return' bundle='${applicationAuResources}' />"  onclick="javascript:returnBack()" >
			<%} else {%>
				<input name="button_add" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Adding_subordinate_role' bundle='${applicationAuResources}' />" onclick="javascript:toAddRole_onClick();" title="<fmt:message key='venus.authority.Add' bundle='${applicationAuResources}' /><%=resultVo.getName()%><fmt:message key='venus.authority.The_lower_role' bundle='${applicationAuResources}' />">
				<input name="button_update" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Modify_role' bundle='${applicationAuResources}' />" onClick="javascript:find_onClick();" title="<fmt:message key='venus.authority.Modify' bundle='${applicationAuResources}' /><%=resultVo.getName()%><fmt:message key='venus.authority.Basic_information' bundle='${applicationAuResources}' />">
				<input name="button_delete" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Delete_role' bundle='${applicationAuResources}' />" onClick="javascript:delete_onClick();" title="<fmt:message key='venus.authority.Delete' bundle='${applicationAuResources}' /><%=resultVo.getName()%>">
				<input name="button_add" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Associated_user' bundle='${applicationAuResources}' />"  onclick="javascript:toAddUser_onClick();" title="<fmt:message key='venus.authority.To' bundle='${applicationAuResources}' /><%=resultVo.getName()%><fmt:message key='venus.authority.Associated_user' bundle='${applicationAuResources}' />">
				<input name="button_add" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Remove_user' bundle='${applicationAuResources}' />"  onclick="javascript:toDelUser_onClick();" title="<fmt:message key='venus.authority.Delete' bundle='${applicationAuResources}' /><%=resultVo.getName()%><fmt:message key='venus.authority.Has_been_associated_with_the_user' bundle='${applicationAuResources}' />">
				<!--input name="button_sort" class="button_ellipse" type="button" value="排序" onClick="javascript:toSort_onClick();"-->
			<%}%>
		</td>
	</tr>
</table>

<div id="auParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auChild0',this,'<%=request.getContextPath()%>/themes/<venus:theme/>/')"><fmt:message key='venus.authority.For_more_information_on_the_role_of' bundle='${applicationAuResources}' />
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
					<td align="right"><fmt:message key='venus.authority.Role_name_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getName())%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Remarks_' bundle='${applicationAuResources}' /></td>
					<td colspan="3" align="left"><%=StringHelperTools.prt(resultVo.getRemark())%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Created_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getCreate_date(),
				    19)%></td>
				</tr>
				<tr>
					<td align="right"><fmt:message key='venus.authority.Modified_' bundle='${applicationAuResources}' /></td>
					<td align="left"><%=StringHelperTools.prt(resultVo.getModify_date(),
				    19)%></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>

<!-- 查询开始 -->
<!--  
<div id="auDivParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('auDivChild0',this,'<venus:base/>/themes/<venus:theme/>/')">
			按条件查询
		</td>
	</tr>
</table>
</div>
<div id="auDivChild0"> 
<table class="table_div_content">
<tr>
	<td width="5%" align="right"  nowrap>名称</td>
	<td width="20%" align="left"><input name="name" type="text" class="text_field" inputName="名称" validate="isSearch" onKeyDown="javascript:search_onKeyDown();"/></td>
	<td  width="20%" align="left">
		<input type="button" name="Submit" class="button_ellipse" value="查询" onClick="javascript:simpleQuery_onClick();">
		<input name="button_clear" class="button_ellipse" type="button" value="清空" onClick="javascript:clear_onClick()">		
	</td>
</tr>
</table>
</div>
-->

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
		<td width="1%" align="right"  nowrap><fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' /></td>
		<td width="10%" align="left">&nbsp;<input name="name" type="text" class="text_field" inputName="<fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' />" validate="isSearch" onKeyDown="javascript:search_onKeyDown();"/></td>
		<td  width="15%" align="left">
			<input type="button" name="Submit" class="button_ellipse" value="<fmt:message key='venus.authority.Inquiry' bundle='${applicationAuResources}' />" onClick="javascript:simpleQuery_onClick();">&nbsp;&nbsp;
			<input name="button_clear" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Empty' bundle='${applicationAuResources}' />" onClick="javascript:clear_onClick()">		
		</td>
	</tr>


	<tr>
		<td colspan="3">
			<div style="width=100%;overflow-x:visible;overflow-y:visible;">
				<table cellspacing="0" cellpadding="0" border="0" align="center" width="100%" class="listCss">
					<tr>
						<td valign="top">
							<table cellspacing="1" cellpadding="1" border="0" width="100%">
								<tr valign="top">
									<th class="listCss" ></th>
									<th class="listCss" width="30"><fmt:message key='venus.authority.Sequence' bundle='${applicationAuResources}' /></th>
									<th class="listCss"><fmt:message key='venus.authority.Number' bundle='${applicationAuResources}' /></th>
									<th class="listCss"><fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' /></th>
									<th class="listCss"><fmt:message key='venus.authority.Mailbox' bundle='${applicationAuResources}' /></th>
								</tr>
								<%
									List beans = (List) request.getAttribute("linkedUserList");
									for(int i=0;  i<beans.size();) {
										AuPartyRelationVo vo= (AuPartyRelationVo)beans.get(i);
										i++;
								%>
								<tr>
									<td class="listCss" align="center">
										<input type="radio" name="checkbox_template" value="<%=vo.getId()%>"/>
									</td>
									<td class="listCss" align="center"><%=i%><input type="hidden" signName="hiddenId" value="<%=vo.getId()%>" /></td>
									<td class="listCss"><%=StringHelperTools.prt(vo.getPartyid())%></td>
									<td class="listCss"><%=StringHelperTools.prt(vo.getName())%></td>
									<td class="listCss"><%=StringHelperTools.prt(vo.getEmail())%></td>
								</tr>
								<%
									}
								%>
							</table>
						</td>
					</tr>
				</table>
				<jsp:include page="/jsp/include/page.jsp" />
			</div>
		</td>
	</tr>





	<%--<tr>--%>
		<%--<td colspan="3">--%>
			<%--<layout:collection name="linkedUserList" id="linkedUserList" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >--%>
				<%--<layout:collectionItem width="5%" title="<input type='checkbox' pdType='control' control='checkbox_template'/>" style="text-align:center;">--%>
					<%--<bean:define id="partyRelationId" name="linkedUserList" property="id"/>--%>
					<%--<input type="checkbox" name="checkbox_template" value="<%=partyRelationId%>"/>--%>
				<%--</layout:collectionItem>--%>
				<%--<layout:collectionItem width="5%" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Sequence") %>' style="text-align:center;">--%>
					<%--<venus:sequence/>--%>
				<%--</layout:collectionItem>	--%>
				<%--<layout:collectionItem width="25%" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Number") %>' property="partyid" />--%>
				<%--<layout:collectionItem width="20%" title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Name") %>' property="name"/>			--%>
				<%--<layout:collectionItem  title='<%=venus.frames.i18n.util.LocaleHolder.getMessage("venus.authority.Mailbox") %>' property="email" />--%>
			<%--</layout:collection>--%>
		<%--</td>--%>
	<%--</tr>--%>
</table>
</div>
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

</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>
<%
    //表单回写
    if (request.getAttribute("writeBackFormValues") != null) {
		out.print("<script language=\"javascript\">\n");
		out.print(VoHelperTools.writeBackMapToForm((java.util.Map) request.getAttribute("writeBackFormValues")));
		out.print("writeBackMapToForm();\n");
		out.print("</script>");
    }
%>

