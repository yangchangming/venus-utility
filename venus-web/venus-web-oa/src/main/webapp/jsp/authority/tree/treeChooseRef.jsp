<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="venus.oa.organization.aupartyrelationtype.bs.IAuPartyRelationTypeBS" %>
<%@page import="venus.oa.organization.aupartyrelationtype.util.IConstants"%>
<%@page import="venus.oa.organization.aupartyrelationtype.vo.AuPartyRelationTypeVo"%>
<%@ page import="venus.frames.mainframe.util.Helper" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="venus.oa.util.GlobalConstants"%>
<%@ page import="java.util.List" %>
<%@ page import="venus.springsupport.BeanFactoryHelper" %>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Organization' bundle='${applicationAuResources}' /></title>
</head>
<%
String basePath=request.getParameter("basePath");
String submit_all=request.getParameter("submit_all");
String return_type=request.getParameter("return_type");
String tree_level=request.getParameter("tree_level");
String data_limit=request.getParameter("data_limit");
String hierarchy=request.getParameter("hierarchy");
String attributesFilter=request.getParameter("attributesFilter");
//规则中和团体关系有关的关系类型
java.util.Map relationTypeMap = new HashMap();
IAuPartyRelationTypeBS bs = (IAuPartyRelationTypeBS) BeanFactoryHelper.getBean("auPartyRelationTypeBS");
AuPartyRelationTypeVo searchVo = new AuPartyRelationTypeVo();
searchVo.setKeyword("4");
List al = bs.simpleQuery(1,Short.MAX_VALUE,null,searchVo);
for(int i=0;i<al.size();i++){
    AuPartyRelationTypeVo reTypeVo = (AuPartyRelationTypeVo)al.get(i);
    if(!GlobalConstants.getRelaType_proxy().equals(reTypeVo.getId())){//非代理关系
        relationTypeMap.put(reTypeVo.getId(),reTypeVo.getName());
    }
}
String rootXmlSource=basePath+"/jsp/authority/tree/orgChooseTree.jsp?submit_all="+submit_all+"&return_type="+return_type+"&tree_level="+tree_level+"&data_limit="+data_limit+"&hierarchy="+hierarchy+"&attributesFilter="+attributesFilter+"&parent_code=";
%>
<body topmargin=0 leftmargin=0 >
<script language="javascript">
	writeTableTop("<fmt:message key='venus.authority.Reference_page' bundle='${applicationAuResources}' />",'<venus:base/>/themes/<venus:theme/>/');  //显示本页的页眉
	function changeRT(v){
		var locationUrl = "deeptree.jsp?inputType=<%=request.getParameter("inputType")%>&submitType=<%=request.getParameter("submitType")==null?"submitAll":request.getParameter("submitType")%>&nodeRelationType=<%=request.getParameter("nodeRelationType")==null?"hasRelation":request.getParameter("nodeRelationType")%>&rootXmlSource=<%=venus.oa.util.StringHelperTools.encodeUrl(rootXmlSource)%>";
		document.myTree.location.href=locationUrl+v;
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
	</tr>
</table>
<table class="table_noFrame">
    <tr> 
     <td width="100%"> 
        <select style="width:100%" onchange="changeRT(this.value)">
        <%java.util.Iterator it = relationTypeMap.entrySet().iterator();
        String firstParentCode = null;
        while (it.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
            if(null==firstParentCode)
                firstParentCode = String.valueOf(entry.getKey());
        %>
            <option value="<%= String.valueOf(entry.getKey())%>"><%= String.valueOf(entry.getValue())%></option>
        <%} %>
        </select>
    </td>
  </tr>
  <tr> 
     <td width="100%" valign="top"> 
		<!--树开始-->    
		<iframe name="myTree" width="100%" height="450" src="deeptree.jsp
			?inputType=<%=request.getParameter("inputType")%>
			&submitType=<%=request.getParameter("submitType")==null?"submitAll":request.getParameter("submitType")%>
			&nodeRelationType=<%=request.getParameter("nodeRelationType")==null?"hasRelation":request.getParameter("nodeRelationType")%>
			&rootXmlSource=<%=venus.oa.util.StringHelperTools.encodeUrl(rootXmlSource)+firstParentCode%>">
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

