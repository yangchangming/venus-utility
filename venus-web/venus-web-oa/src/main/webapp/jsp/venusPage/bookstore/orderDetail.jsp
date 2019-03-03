<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/jsp/include/global.jsp" %>

<%@ page import="udp.template.bookstore.common.Constant" %>
<fmt:bundle basename="udp.template.bookstore.resource" prefix="udp.template.bookstore.">
<title><fmt:message key="querytemplate"/></title>

</head>
<body>
<script language="javascript">
	writeTableTop('<fmt:message key="vieworderdetail"/>','<venus:base/>/');
</script>

<form name="form" method="post" action="<venus:base/>/BookStore.do?cmd=disposeOrder">
<div id="ccChild0"> 
	
		<table class="table_noframe">
			<tr>
				<td valign="middle">
					<logic:equal name="isDisposeOrder" value="true">
							<input name="button_save" type="button" class="button_ellipse" value="<fmt:message key="confirm"/>" onclick='javascript:form.submit();' >
					</logic:equal>
					<input name="button_cancel" type="button" class="button_ellipse" value='<fmt:message key="back"/>'  onClick="javascript:history.go(-1);">
				</td>
			</tr>
		</table>
<table class="table_div_content">
	<tr> 
		<td align="right" width="20%" nowrap><fmt:message key="orderno"/>:</td>
		<td align="left">
			<input type="hidden" name="orderId" value="<layout:write name='order' property='id'/>">
			<layout:write name='order' property='id'/>
		</td>
	</tr>
	<tr> 
		<td align="right" width="20%" nowrap><fmt:message key="consignee"/>:</td>
		<td align="left">
			<layout:write name='order' property='name'/>
		</td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="address"/>:</td>
		<td align="left">
			<layout:write name='order' property='address'/>	
		</td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="tel"/>:</td>
		<td align="left">
			<layout:write name='order' property='tel'/>
		</td>
	</tr>
	
	<tr>
		<td align="right"><fmt:message key="postcode"/>:</td>
		<td align="left">
			<layout:write name='order' property='postcode'/>
		</td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="orderamount"/>:</td>
		<td align="left">
			￥<font color="red"><layout:write name='order' property='money'/></font>
		</td>
	</tr>
	<tr>
		<td align="right"><fmt:message key="orderstatus"/>:</td>
		<td align="left">
			<bean:define id="orderStatus" name="order" property="orderStatus"/>
			<%=Constant.ORDER_STATUS.get(orderStatus)%>
		</td>
	</tr>
	<logic:equal name="isDisposeOrder" value="true">
		<tr>
			<td align="right"><fmt:message key="processresult"/>:</td>
			<td align="left">
				<select name="orderStatus">
					<option value="2">处理成功</option>
					<option value="3">处理不成功</option>
				</select>
			</td>
		</tr>
		<tr>
			<td align="right"><fmt:message key="remark"/>:</td>
			<td align="left">
				<textArea name="result" maxlength="60" class="textarea_limit_words" style="width:200px">
				</textArea>
			</td>
		</tr>
	</logic:equal>
</table>
</div>
<div id="ccChild1"> 
<table class="table_div_content">
	<tr>
		<td>
		<layout:collection name="order" property="items" id="item"  styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >
			<layout:collectionItem width="5%"  title="序" style="text-align:center;">
				<venus:sequence/>
				<input type="hidden" name="checkbox_template" />
			</layout:collectionItem>
			<layout:collectionItem width="25%" title="书名" property="book.bookName"  style="text-align:center;"/>
			<layout:collectionItem width="15%" title="作者" property="book.author"  style="text-align:center;"/>
			<layout:collectionItem width="15%" title="定价" property="book.price"  style="text-align:center;"/>
			<layout:collectionItem width="10%" title="数量" property="quantity"  style="text-align:center;" />
			<layout:collectionItem width="15%" title="总价" property="totalPrice"  style="text-align:center;"/>
			<logic:notEmpty name="accountInSession">
					<layout:collectionItem width="15%" title="会员价"  property="vipPrice"  style="text-align:center;color:red"/>
			</logic:notEmpty>
		</layout:collection>
		</td>
	</tr>
	<tr>	<td>&nbsp;</td></tr>
</table>
</div>
</fmt:bundle>
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/');
</script>
</body>
</html>
