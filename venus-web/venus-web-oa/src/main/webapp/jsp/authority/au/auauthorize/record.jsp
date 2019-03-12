<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="venus.oa.util.GlobalConstants"%>
<%@ page import="venus.oa.authority.auresource.vo.AuResourceVo"%>
<%@ page import="venus.oa.authority.auauthorize.vo.AuAuthorizeVo"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Map,java.util.HashMap"%>
<%@ page import="java.util.Set"%>
<%
try {
	//获取该用户自身拥有权限的节点
    Map selMap = (Map)request.getAttribute("SEL_AU_MAP");
    Set selSet = selMap.keySet();
    //获取该用户继承权限的节点
    Map extMap = (Map)request.getAttribute("EXT_AU_MAP");
    Set extSet = extMap.keySet();
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
	<form name="form" method="post" action="">
	<table class="table_noFrame" width="96%" align="center"> 
        <tr> 
			<td align="left">
<!--菜单树开始-->   
		<script language="javascript">
			var basePath="<venus:base/>"; 
			var checkboxType = "";
			var selTxtArray = new Array("<fmt:message key='venus.authority.Refusal' bundle='${applicationAuResources}' />","<fmt:message key='venus.authority.Allow' bundle='${applicationAuResources}' />","<fmt:message key='venus.authority.May_authorize' bundle='${applicationAuResources}' />");
			var selValArray = new Array('0','1','2');
		</script>
		<script language="javascript" src="<venus:base/>/js/au/tree4au.js"></script>
		<script language="javascript">
			if (document.getElementById){
				//根节点
				var t0=new WebFXTree('<b><fmt:message key='venus.authority.Record_Resources' bundle='${applicationAuResources}' /></b>','',selTxtArray,selValArray);
				t0.setBehavior('classic');
<%			
		Map tabMap = new HashMap();
		Iterator itLTree = list.iterator(); 
		while(itLTree.hasNext()) {
			AuResourceVo vo = (AuResourceVo) itLTree.next();
			if( ! tabMap.keySet().contains(vo.getTable_name())) {
				tabMap.put(vo.getTable_name(),"");
%>
						var node<%=vo.getTable_name()%> = new WebFXTreeItem("<%=vo.getTable_chinesename()%>","<%="<fmt:message key='venus.authority.Table_name' bundle='${applicationAuResources}' />:"+vo.getTable_name()+";<fmt:message key='venus.authority.Table_Chinese_name' bundle='${applicationAuResources}' />:"+vo.getTable_chinesename()%>","<%=vo.getTable_name()%>","",new Array(0),new Array(0),"","","");
						t0.add(node<%=vo.getTable_name()%>);
<%	
			}
			String isDisable = "";
			String selfType = "";
			String selfAccs = "";
			String extType = "";
			String extAccs = "";
			//勾中已授权的节点
			if(selSet.contains(vo.getId())) {
				AuAuthorizeVo auVo = (AuAuthorizeVo)selMap.get(vo.getId());
				selfType = auVo.getAuthorize_status();
				selfAccs = auVo.getAccess_type();
			}
			if(extSet.contains(vo.getId())) {
				AuAuthorizeVo auVo = (AuAuthorizeVo)extMap.get(vo.getId());
				extType = auVo.getAuthorize_status();
				extAccs = auVo.getAccess_type();
			}
			//将公开节点置为不可用
			if(resSet.contains(vo.getId())) {
				isDisable = "disabled";
			}
%>
				//参数如下:WebFXTreeItem(sText,sTitle,treeID,selCode,selTxt,selVal,selWhoEx,selWho,isDisable)
				//sText:节点名称, sTitle:鼠标悬停提示, treeID:节点checkbox的name, selCode:节点编码
				//selTxt:选项名称数组, selVal:选项值数组, selWhoEx 继承权限数组, selWho:已有权限数组, isDisable:是否禁用
				var node<%=vo.getId()%> = new WebFXTreeItem("<%=vo.getName()%><%=resSet.contains(vo.getId())?"[<fmt:message key='venus.authority.Open' bundle='${applicationAuResources}' />]":""%>",
				"<%="<fmt:message key='venus.authority.Field_name' bundle='${applicationAuResources}' />:"+vo.getField_name()+";<fmt:message key='venus.authority.Chinese_name_field' bundle='${applicationAuResources}' />:"+vo.getField_chinesename()+";<fmt:message key='venus.authority.Filter_Type' bundle='${applicationAuResources}' />:"+vo.getFilter_type()+";<fmt:message key='venus.authority.Filter_value' bundle='${applicationAuResources}' />:"+vo.getValue()+";<fmt:message key='venus.authority.Notes' bundle='${applicationAuResources}' />:"+vo.getHelp()%>",
				"<%=vo.getId()%>","<%=vo.getField_name()%>",selTxtArray,selValArray,
				new Array('<%=extType%>','<%=extAccs%>'), new Array('<%=selfType%>','<%=selfAccs%>'),"<%=isDisable%>");
				node<%=vo.getTable_name()%>.add(node<%=vo.getId()%>);
<%					
		}
%>				
				document.write(t0);t0.expand();
			}
		</script>
<!--菜单树结束-->
			<input type="hidden" name="cmd" value="saveAuByVisitor">
			<input type="hidden" name="ids" value="">
			<input type="hidden" name="codes" value="">
			<input type="hidden" name="status" value="">
			<input type="hidden" name="types" value="">
			<input type="hidden" name="access" value="">
			<input type="hidden" name="vId" value="<%=(String)request.getAttribute("vId")%>">
			<input type="hidden" name="vCode" value="<%=(String)request.getAttribute("vCode")%>">
			<input type="hidden" name="vType" value="<%=(String)request.getAttribute("vType")%>">
			<input type="hidden" name="auType" value="<%=GlobalConstants.getResType_recd()%>">
			</td> 
		</tr> 
    </table>
<script language="javascript">
	function saveOnClick() {
		var ids = "";
		var codes = "";
		var types = "";
		var status = "";
		var access = "";
		
		for(var i=0; i<inputArray.length; i++) {
			var cbx = inputArray[i];	//全部的checkbox
			var selCbx = new Array(0);	//选中的checkbox
			for(var j=0; j<cbx.length; j++) {
				if(cbx[j].checked && !cbx[j].disabled) {
					selCbx[selCbx.length] = cbx[j];
				}
			}
			if(selCbx.length>0) {
				var selStatus = "2";//授权状态（拒绝0，允许1）
				var selAccess = 1; 	//权限种类（用素数表示，例如:可授权2，只读3，可写5...）
				for(var k=0; k<selCbx.length; k++) {
					if(selCbx[k].value=="0" || selCbx[k].value=="1") {
						selStatus = selCbx[k].value;
					}else {
						selAccess = selAccess * selCbx[k].value;//用素数的乘积表示多种权限种类的组合
					}
				}
				status += selStatus + ",";
				access += selAccess + ",";
				ids += selCbx[0].name + ",";
				codes += selCbx[0].code + ",";
				types += "<%=GlobalConstants.getResType_recd()%>" + ",";
			}
		}
		
		if(ids!="") {
			ids = ids.substring(0,ids.length-1);
			document.form.ids.value=ids;
		}
		if(codes!="") {
			codes = codes.substring(0,codes.length-1);
			document.form.codes.value=codes;
		}
		if(types!="") {
			types = types.substring(0,types.length-1);
			document.form.types.value=types;
		}
		if(status!="") {
			status = status.substring(0,status.length-1);
			document.form.status.value=status;
		}
		if(access!="") {
			access = access.substring(0,access.length-1);
			document.form.access.value=access;
		}
		
		form.action="<venus:base/>/auAuthorize/saveAuByVisitor";
		form.submit();
	}
</script>
</form>
</body>
</html>
<%
} catch(Exception e) {
	e.printStackTrace();
}
%>

