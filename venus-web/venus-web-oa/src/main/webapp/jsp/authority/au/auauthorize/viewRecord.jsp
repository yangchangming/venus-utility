<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.authority.util.GlobalConstants"%>
<%@ page import="venus.authority.au.auresource.vo.AuResourceVo"%>
<%@ page import="venus.authority.au.auauthorize.vo.AuAuthorizeVo"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Map,java.util.HashMap"%>
<%@ page import="java.util.Set"%>
<%
try {
	//获取该用户自身拥有权限的节点
    Map auMap = (Map)request.getAttribute("ALL_AU_MAP");
    Set auSet = auMap.keySet();
	//获取全部功能节点
	List list = (List)request.getAttribute("RES_LIST");
	//获取公开访问的节点
	Map resMap = (Map)request.getAttribute("PUB_RES_MAP");
    Set resSet = resMap.keySet();
%>
<html>
<head>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>default</title>
</head>
<body>
	<br>
	<br>
	<table class="table_noFrame" width="96%" align="center"> 
        <tr> 
			<td align="left">
<!--菜单树开始-->   
		<script language="javascript">
			var basePath="<venus:base/>";
			var selTxtArray = new Array("<fmt:message key='venus.authority.Refusal' bundle='${applicationAuResources}' />","<fmt:message key='venus.authority.Allow' bundle='${applicationAuResources}' />","<fmt:message key='venus.authority.May_authorize' bundle='${applicationAuResources}' />");
			var selValArray = new Array('0','1','2');
		</script>
		<script language="javascript" src="<venus:base/>/js/au/tree4auView.js"></script>
		<script language="javascript">
			if (document.getElementById){
				//根节点
				var t0=new WebFXTree('<b><fmt:message key='venus.authority.Record_Resources' bundle='${applicationAuResources}' /></b>','');
				t0.setBehavior('classic');
<%			
		Map tabMap = new HashMap();
		Iterator itLTree = list.iterator(); 
		while(itLTree.hasNext()) {
			AuResourceVo vo = (AuResourceVo) itLTree.next();
			if( ! tabMap.keySet().contains(vo.getTable_name())) {
				tabMap.put(vo.getTable_name(),"");
%>
						var node<%=vo.getTable_name()%> = new WebFXTreeItem("<%=vo.getTable_chinesename()%>","<%="<fmt:message key='venus.authority.Table_name' bundle='${applicationAuResources}' />:"+vo.getTable_name()+";<fmt:message key='venus.authority.Table_Chinese_name' bundle='${applicationAuResources}' />:"+vo.getTable_chinesename()%>","<%=vo.getTable_name()%>","",new Array(0),new Array(0),new Array(0),new Array(0),"");
						t0.add(node<%=vo.getTable_name()%>);
<%	
			}
			String isDisable = "";
			String selfType = "";
			String selfAccs = "";
			//勾中已授权的节点
			if(auSet.contains(vo.getId())) {
				AuAuthorizeVo auVo = (AuAuthorizeVo)auMap.get(vo.getId());
				selfType = auVo.getAuthorize_status();
				selfAccs = auVo.getAccess_type();
			}
			//将公开节点置为允许
			if(resSet.contains(vo.getId())) {
				selfType = "1";
				selfAccs = "";
			}
%>
				//参数如下:WebFXTreeItem(sText,sTitle,treeID,selCode,selTxt,selVal,selWhoEx,selWho,isDisable)
				//sText:节点名称, sTitle:鼠标悬停提示, treeID:节点checkbox的name, selCode:节点编码
				//selTxt:选项名称数组, selVal:选项值数组, selWhoEx 继承权限数组, selWho:已有权限数组, isDisable:是否禁用
				var node<%=vo.getId()%> = new WebFXTreeItem("<%=vo.getName()%><%=resSet.contains(vo.getId())?"[<fmt:message key='venus.authority.Open' bundle='${applicationAuResources}' />]":""%>",
				"<%="<fmt:message key='venus.authority.Field_name' bundle='${applicationAuResources}' />:"+vo.getField_name()+";<fmt:message key='venus.authority.Chinese_name_field' bundle='${applicationAuResources}' />:"+vo.getField_chinesename()+";<fmt:message key='venus.authority.Filter_Type' bundle='${applicationAuResources}' />:"+vo.getFilter_type()+";<fmt:message key='venus.authority.Filter_value' bundle='${applicationAuResources}' />:"+vo.getValue()+";<fmt:message key='venus.authority.Notes' bundle='${applicationAuResources}' />:"+vo.getHelp()%>",
				"<%=vo.getId()%>","<%=vo.getValue()%>",selTxtArray,selValArray,new Array(0),new Array('<%=selfType%>','<%=selfAccs%>'),"<%=isDisable%>");
				
				node<%=vo.getTable_name()%>.add(node<%=vo.getId()%>);
<%					
					
		}
%>				
				document.write(t0);t0.expand();
			}
		</script>
<!--菜单树结束-->
			</td> 
		</tr> 
    </table>
</body>
</html>
<%
} catch(Exception e) {
	e.printStackTrace();
}
%>

