<%@page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Organization' bundle='${applicationAuResources}' /></title>
</head>
<%
String basePath=request.getParameter("basePath");
String parent_code=request.getParameter("parent_code");
String submit_all=request.getParameter("submit_all");
String return_type=request.getParameter("return_type");
String tree_level=request.getParameter("tree_level");
String data_limit=request.getParameter("data_limit");
String rootXmlSource=basePath+"/jsp/authority/tree/orgQueryTreeRapid.jsp?parent_code="+parent_code+"&submit_all="+submit_all+"&return_type="+return_type+"&tree_level="+tree_level+"&data_limit="+data_limit;
%>
<body topmargin=0 leftmargin=0 >
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Reference_page' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
	function post(URL, PARAMS) {
	    var temp=window.frames["myTree"].document.createElement("form");
	    temp.action=URL;
	    temp.method="POST";
	    temp.style.display="none";
	    for(var x in PARAMS) {
	        var opt=window.frames["myTree"].document.createElement("textarea");
	        opt.name=x;
	        opt.value=PARAMS[x];
	        temp.appendChild(opt);
	    }	    
	    window.frames["myTree"].document.body.appendChild(temp);
	    temp.submit();
	    return temp;
	}

	function query_onClick(){
		//document.frames["myTree"].location.href = "deeptree.jsp?inputType=<%=request.getParameter("inputType")%>&submitType=<%=request.getParameter("submitType")==null?"submitAll":request.getParameter("submitType")%>&nodeRelationType=<%=request.getParameter("nodeRelationType")==null?"hasRelation":request.getParameter("nodeRelationType")%>&rootXmlSource=<%=venus.authority.util.StringHelperTools.encodeUrl(rootXmlSource)%>"+"%26query_organize_name%3D"+document.all.organize_name.value;
		var httpPost = "deeptree.jsp?inputType=<%=request.getParameter("inputType")%>&submitType=<%=request.getParameter("submitType")==null?"submitAll":request.getParameter("submitType")%>&nodeRelationType=<%=request.getParameter("nodeRelationType")==null?"hasRelation":request.getParameter("nodeRelationType")%>&rootXmlSource=<%=venus.authority.util.StringHelperTools.encodeUrl(rootXmlSource)%>";
		post(httpPost, {
		    query_organize_name:document.all.organize_name.value
		});
	}

    function cancel_onClick() {
        window.parent.jQuery("#iframeDialog").dialog("close");
    }
</script>
<table class="table_noFrame">
	<tr>
		<td>&nbsp;&nbsp;
			<input name="button_ok" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Determine' bundle='${applicationAuResources}' />" onClick="javascript:myTree.returnValueName();">
			<input name="button_cancel" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Cancel' bundle='${applicationAuResources}' />"  onclick="javascript:cancel_onClick();" >
		</td>
		<tr>
			<td>&nbsp;&nbsp;<fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' />
			<input type="text" class="text_field" validate="isSearch" name="organize_name" inputName="<fmt:message key='venus.authority.Name' bundle='${applicationAuResources}' />" maxLength="50"/>
			<input name="button_ok" class="button_ellipse" type="button" value="<fmt:message key='venus.authority.Inquiry' bundle='${applicationAuResources}' />" onClick="javascript:query_onClick()"></td>
		</tr>
	</tr>
</table>
<table class="table_noFrame">
  <tr> 
     <td width="100%" valign="top"> 
		<!--树开始-->    
		<iframe name="myTree" width="100%" height="450" src="deeptree.jsp
			?inputType=<%=request.getParameter("inputType")%>
			&submitType=<%=request.getParameter("submitType")==null?"submitAll":request.getParameter("submitType")%>
			&nodeRelationType=<%=request.getParameter("nodeRelationType")==null?"hasRelation":request.getParameter("nodeRelationType")%>
			&rootXmlSource=<%=venus.authority.util.StringHelperTools.encodeUrl(rootXmlSource)%>">
		</iframe>
		<!--树结束-->
    </td>
  </tr>
</table>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</body>
</html>

