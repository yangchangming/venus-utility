<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.authority.util.GlobalConstants"%>
<%@ page import="venus.authority.au.aufunctree.vo.AuFunctreeVo"%>
<%@ page import="venus.authority.au.auauthorize.vo.AuAuthorizeVo"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Map,java.util.HashMap"%>
<%@ page import="java.util.Set"%>
<%
try {
	//获取该用户自身拥有权限的节点
    Map allMap = (Map)request.getAttribute("ALL_AU_MAP");
    Set auSet = allMap.keySet();
	//获取全部功能节点
	List lFunctree = (List)request.getAttribute("FUNC_LIST");
	
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
			var selType="single";
			var selTxtArray = new Array("<fmt:message key='venus.authority.Refusal' bundle='${applicationAuResources}' />","<fmt:message key='venus.authority.Allow' bundle='${applicationAuResources}' />","<fmt:message key='venus.authority.May_authorize' bundle='${applicationAuResources}' />");
			var selValArray = new Array('0','1','2');
		</script>
		<script language="javascript" src="<venus:base/>/js/au/tree4auView.js"></script>
		<script language="javascript">
			if (document.getElementById){
<%
		String rootCode = "";
		Iterator itLTree = lFunctree.iterator(); 
		//根节点
		if(itLTree.hasNext()) {
			AuFunctreeVo vo = (AuFunctreeVo) itLTree.next();
			rootCode = vo.getTotal_code();
%>
				var t0=new WebFXTree('<b><%=vo.getName()%></b>','');
				t0.setBehavior('classic');
<%				
		}
		while(itLTree.hasNext()) {
			AuFunctreeVo vo = (AuFunctreeVo) itLTree.next();
			String isDisable = "";
			String selfType = "";
			String selfAccs = "";
			//勾中已授权的节点
			if(auSet.contains(vo.getId())) {
				AuAuthorizeVo auVo = (AuAuthorizeVo)allMap.get(vo.getId());
				selfType = auVo.getAuthorize_status();
				selfAccs = auVo.getAccess_type();
			}
			//将公开节点置为允许
			if(resSet.contains(vo.getId())) {
				selfType = "1";
				selfAccs = "";
			}
			if("1".equals(vo.getIs_leaf()) || "1".equals(vo.getType_is_leaf())) {
			
%>
				//参数如下：WebFXTreeItem(sText,sTitle,treeID,selCode,selTxt,selVal,selWhoEx,selWho,isDisable)
				//sText：节点名称, sTitle：鼠标悬停提示, treeID：节点checkbox的name, selCode:节点编码
				//selTxt：选项名称数组, selVal：选项值数组, selWhoEx 继承权限数组, selWho：已有权限数组, isDisable：是否禁用
				var node<%=vo.getTotal_code()%> = new WebFXTreeItem("<%=vo.getName()%><%=resSet.contains(vo.getId())?"[<fmt:message key='venus.authority.Open' bundle='${applicationAuResources}' />]":""%>","<%=vo.getName()%>","<%=vo.getId()%>","<%=vo.getTotal_code()%>",selTxtArray,selValArray,new Array(0),new Array('<%=selfType%>','<%=selfAccs%>'),"<%=isDisable%>");
<%
			}else {
%>
				var node<%=vo.getTotal_code()%> = new WebFXTreeItem("<%=vo.getName()%>","<%=vo.getName()%>","<%=vo.getId()%>","<%=vo.getTotal_code()%>",new Array(0),new Array(0),new Array(0),new Array(0),"");
<%
			}
			if(!rootCode.equals(vo.getParent_code())){//有父节点存在
%>
				node<%=vo.getParent_code()%>.add(node<%=vo.getTotal_code()%>);
<%					
			}else{
%>
				t0.add(node<%=vo.getTotal_code()%>);
<%				
			}
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

