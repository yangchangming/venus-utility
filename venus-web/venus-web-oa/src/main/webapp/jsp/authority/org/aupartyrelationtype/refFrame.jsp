<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%
	String actionStr = request.getParameter("actionStr");
    String keyword = request.getParameter("keyword");
	if(actionStr==null){
		out.print("<script>alert('<fmt:message key='venus.authority.Parameter_error_' bundle='${applicationAuResources}' />');window.close();</script>");
	}
%>
<%@ include file="/jsp/include/global.jsp" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='venus.authority.Reference_page' bundle='${applicationAuResources}' /></title>
<script>
	function ok_onClick()  {
		var elementCheckbox = myList.document.getElementsByName("checkbox_template");  //通过name取出所有的checkbox
		var elementHiddenName = myList.document.getElementsByName("hidden_name");  //通过name取出所有的checkbox
		var number = 0;  //定义游标
		var ids = null;  //定义id值的数组
		for(var i=0;i<elementCheckbox.length;i++){  //循环checkbox组
			if(elementCheckbox[i].checked) {  //如果被选中
				number += 1;  //游标加1
				if(ids == null) {
					ids = new Array(0);
				}
				ids.push({id:elementCheckbox[i].value,name:elementHiddenName[i].value});  //加入选中的checkbox
			}
		}
		if(ids == null)	{  //如果ids为空
			alert("<fmt:message key='venus.authority.Please_Select_Records' bundle='${applicationAuResources}' />!")
			return;
		}
		//if(ids.length > 1) {  //如果ids有2条以上的纪录
	  	//	alert("只能选择一条记录!")
	  	//	return;
		//}
		window.returnValue = ids;
		window.close();
	}
</script>
</head>
<body topmargin=0 leftmargin=0 rightmargin=0>
<table width="100%"  border="0" cellspacing="0" cellpadding="0">
	<tr> 
		<td align="left" height="40">
			&nbsp;&nbsp;
			<input type="button" name="btn_ok" class="button_ellipse" onClick="javascript:ok_onClick();" value="<fmt:message key='venus.authority.Determine' bundle='${applicationAuResources}' />">
			<input type="button" name="btn_close" class="button_ellipse" onClick="javascript:window.close();" value="<fmt:message key='venus.authority.Cancel' bundle='${applicationAuResources}' />">
		</td>
	</tr>
  	<tr> 
    	<td height="510">
    		<iframe name="myList" width="100%" height="100%" 
    			<%--src="<%=request.getContextPath()%>/<%=actionStr%>.do?cmd=simpleQuery&keyword=<%=keyword%>">--%>
				src="<%=request.getContextPath()%>/<%=actionStr%>/simpleQuery?keyword=<%=keyword%>">
    		</iframe>
    	</td> 
  	</tr>
</table>
</body>
</html>

