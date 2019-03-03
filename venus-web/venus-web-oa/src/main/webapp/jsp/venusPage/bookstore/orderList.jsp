<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>

<%@ page import="java.util.*"%>
<%@ page import="venus.frames.mainframe.util.VoHelper" %>
<%@ page import="udp.template.bookstore.common.Constant" %>
<%@ page import="udp.template.bookstore.model.*" %>
<fmt:bundle basename="udp.template.bookstore.resource" prefix="udp.template.bookstore.">
<title>查询模板</title>
<script>
	function simpleQuery_onClick(){  //简单的模糊查询
//		form.queryCondition.value = buildQueryCondition();
    	form.cmd.value = "simpleQuery";
    	form.submit();
  	}
	function exportExcel_onClick(){  //导出Excel
		oldCmd = form.cmd.value;
    	form.cmd.value = "exportExcel";
    	form.submit();
    	form.cmd.value = oldCmd;
  	}
	function exportPdf_onClick(){  //导出Pdf
		oldCmd = form.cmd.value;
    	form.cmd.value = "exportPdf";
    	form.submit();
    	form.cmd.value = oldCmd;
  	}
	function cancleOrder_onClick(orderId){ 
 		
		if(confirm('<fmt:message key="cancleorderconfirm"/>')) {
	    		window.location="<venus:base/>/BookStore.do?cmd=cancleOrder&orderId=" + orderId;
		}
	
	}
	function deleteOrder_onClick(orderId,orderStatus){ 
 		
		if(confirm('<fmt:message key="deleteorderconfirm"/>')) {
	    		window.location="<venus:base/>/BookStore.do?cmd=deleteOrder&orderId=" + orderId+"&orderStatus="+orderStatus;
		}
	
	}
	function disposeOrder_onClick(orderId){
		
	    window.location="<venus:base/>/BookStore.do?cmd=initDisposeOrder&orderId=" + orderId;
	}
	function orderDetail_onClick(orderId){
		
		window.location="<venus:base/>/BookStore.do?cmd=viewOrder&orderId=" +orderId;
	}
	function resubmitOrder_onClick(orderId){
		if(confirm('<fmt:message key="resubmitorderconfirm"/>')) {
			window.location="<venus:base/>/BookStore.do?cmd=reSubmitOrder&orderId=" +orderId;
		}
	}
	function refresh_onClick(){  //刷新本页
		form.submit();
	}
	jQuery(document).ready(function(){
		jQuery(':checkbox').hide();
	});
</script>

</head>
<body>
<script language="javascript">
	writeTableTop('<fmt:message key="orderlist"/>','<venus:base/>/themes/<venus:theme/>/');
</script>

<form name="form" method="post" action="<venus:base/>/BookStore.do?cmd=listOrder">
<!-- 查询开始 -->
<div id="ccParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" onClick="javascript:hideshow('ccChild0',this,'<venus:base/>/themes/<venus:theme/>/')">
			<fmt:message key="querywithparas"/>
		</td>
	</tr>
</table>
</div>

<div id="ccChild0"> 
<table class="table_div_content">
<tr>
	<td align="right" width="10%" nowrap><fmt:message key="orderno"/></td>
	<td>
		<input name="id" type="text" class="text_field" inputName='<fmt:message key="orderno"/>' validate="isSearch"  >
	</td>
</tr>
<tr>
	<td align="right" width="10%" nowrap><fmt:message key="consignee"/></td>
	<td><input name="name" type="text" class="text_field" inputName='<fmt:message key="consignee"/>' validate="isSearch"><input type="button" name="Submit" class="button_ellipse" value='<fmt:message key="query"/>' onclick="javascript:form.submit()">
	</td>
</tr>
</table>

</div>
<div id="ccParent1"> 
<table class="table_div_control">
	<tr > 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key="detailtable"/>
		</td>
		<td> 
			<table align="right">
				<tr> 
					
					<td class="button_ellipse" onClick="javascript:refresh_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/refresh.gif" class="div_control_image"><fmt:message key="refresh"/></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</div>
<div id="ccChild1"> 
<table class="table_div_content">
	<tr>
		<td>
		<layout:collection name="orders" id="order" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0"  >
			<layout:collectionItem width="1px" title="<input type='checkbox' pdType='control' control='checkbox_template'/>" style="text-align:center;">
				<bean:define id="bookId" name="order" property="id"/>
				<input type="checkbox" name="checkbox_template" value="<%=bookId%>"/>
			</layout:collectionItem>
			
			<layout:collectionItem width="5%"  title='<%=LocaleHolder.getMessage("sequence") %>' style="text-align:center;">
				<venus:sequence/>
				
				
			</layout:collectionItem>
			<layout:collectionItem width="15%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.orderno") %>' property="id" sortable="true" style="text-align:center;"/>
			<layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.consignee") %>' property="name" sortable="true" style="text-align:center;"/>
			<layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.tel") %>' property="tel"  style="text-align:center;"/>
			<layout:collectionItem width="6%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.postcode") %>' property="postcode"  style="text-align:center;"/>
			<layout:collectionItem width="8%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.orderamount") %>' property="money" sortable="true" style="text-align:center;"/>
			<layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.ordertime") %>' property="orderTime" sortable="true" style="text-align:center;"/>
			<layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.orderstatus") %>' property="orderStatus" sortable="true" style="text-align:center;">
				<bean:define id="status" name="order" property="orderStatus"/>
				<%=Constant.ORDER_STATUS.get(status)%>
			</layout:collectionItem>
			<layout:collectionItem width="12%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.remark") %>' property="result"  style="text-align:center;"/>
			<layout:collectionItem title='<%=LocaleHolder.getMessage("udp.template.bookstore.operation") %>' width="25%" style="text-align:center;">
				<bean:define id="orderId" name="order" property="id"/>
				<bean:define id="status" name="order" property="orderStatus"/>
				<bean:define id="accountId" type="java.lang.String" name="order" property="accountId"/>
				<a href="#" onclick="orderDetail_onClick('<%=orderId%>')"><fmt:message key="detail"/></a>&nbsp;
				<%--只能删除或重新提交自己的订单--%>
				<%
					Account account = (Account)session.getAttribute("accountInSession");
					if(account.getId().equals(accountId)){
				%>
					<logic:notEqual name="status"  value="2"><a href="#" onclick="deleteOrder_onClick('<%=orderId%>','<%=status%>')"><fmt:message key="delete"/></a></logic:notEqual>&nbsp;
					<logic:equal name="status"  value="3"><a href="#" onclick="resubmitOrder_onClick('<%=orderId%>')"><fmt:message key="resubmit"/></a></logic:equal>&nbsp;
				<%}%>
				<%--
				</logic:equal>
				--%>
				<%--只有管理员用户可以处理订单--%>
				<logic:equal name="accountInSession" property="userType" value="2">
					<logic:equal name="status"  value="1"><a href="#" onclick="disposeOrder_onClick('<%=orderId%>')"><fmt:message key="dispose"/></a></logic:equal>
				</logic:equal>
			</layout:collectionItem>
		</layout:collection>
		<jsp:include page="/jsp/include/page.jsp" />
		</td>
	</tr>
</table>
</div>
</form>
</fmt:bundle>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');
</script>
</body>
<%@page import="venus.frames.i18n.util.LocaleHolder"%>
<%@ page import="udp.template.bookstore.model.Account" %>
</html>
<%  //表单回写
	if(request.getAttribute("writeBackFormValues") != null) {
		out.print("<script language=\"javascript\">\n");
		out.print(VoHelper.writeBackMapToForm((java.util.Map)request.getAttribute("writeBackFormValues")));
		out.print("writeBackMapToForm();\n");
		out.print("</script>");
	}
%>