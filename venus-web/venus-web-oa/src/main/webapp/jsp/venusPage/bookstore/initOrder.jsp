<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>

<fmt:bundle basename="udp.template.bookstore.resource" prefix="udp.template.bookstore.">
<title>查询模板</title>

</head>
<body>
<script language="javascript">
	writeTableTop('<fmt:message key="checkorder"/>','<venus:base/>/');
</script>

<form name="form" method="post" action="<venus:base/>/BookStore.do?cmd=submitOrder">
<div id="ccChild0"> 
<table class="table_div_content">
	<tr> 
		<td align="right" width="20%" nowrap><font color="red">*</font><fmt:message key="consignee"/></td>
		<td align="left">
			<input type="hidden" name="accountId" value="<layout:write name='order' property='accountId'/>">
			<input name="name" type="text" maxlength="30" value="<layout:write name='order' property='name'/>" class="text_field" inputName='<fmt:message key="consignee"/>' validate="notNull" >
		</td>
	</tr>
	<tr>
		<td align="right"><font color="red">*</font><fmt:message key="address"/></td>
		<td align="left">
			<input name="address" type="text" class="text_field" maxlength="50" inputName='<fmt:message key="address"/>' validate="notNull" value="<layout:write name='order' property='address'/>">
		</td>
	</tr>
	<tr>
		<td align="right"><font color="red">*</font><fmt:message key="tel"/></td>
		<td align="left">
			<input name="tel" type="text" class="text_field" maxlength="15" inputName='<fmt:message key="tel"/>' validate="notNull;isTel" value="<layout:write name='order' property='tel'/>">
		</td>
	</tr>
	
	<tr>
		<td align="right"><font color="red">*</font><fmt:message key="postcode"/></td>
		<td align="left">
			<input name="postcode" type="text" class="text_field" maxlength="10" inputName='<fmt:message key="postcode"/>' validate="notNull" value="<layout:write name='order' property='postcode'/>">
		</td>
	</tr>
</table>
</div>
<div id="ccChild1"> 
<table class="table_div_content">
	<tr>
		<td>
		<layout:collection name="order" property="items" id="item"  styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >
			<layout:collectionItem width="5%"  title='<%=LocaleHolder.getMessage("udp.template.bookstore.index") %>' style="text-align:center;">
				<venus:sequence/>
				<input type="hidden" name="checkbox_template" />
			</layout:collectionItem>
			<layout:collectionItem width="25%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.bookname") %>' property="book.bookName"  style="text-align:center;"/>
			<layout:collectionItem width="15%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.author") %>' property="book.author"  style="text-align:center;"/>
			<layout:collectionItem width="15%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.price") %>' property="book.price"  style="text-align:center;"/>
			<layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.quantity") %>' property="quantity"  style="text-align:center;" />
			<layout:collectionItem width="15%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.totalPrice") %>'  property="totalPrice"  style="text-align:center;"/>
			<logic:notEmpty name="accountInSession">
					<layout:collectionItem width="15%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.vipPrice") %>'  property="vipPrice"  style="text-align:center;color:red"/>
			</logic:notEmpty>
		</layout:collection>
		</td>
	</tr>
	<tr>	<td>&nbsp;</td></tr>
</table>
</div>
<div id="ccChild2"> 
<table class="table_div_content">
	<tr> 
		<td align="right" width="20%" nowrap>&nbsp;</td>
		<td align="right" width="60%">
			<fmt:message key="payquantity">
			     <fmt:param > <font color="red"><layout:write name="order" property="money"/></font></fmt:param>
			</fmt:message>
		</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align="center" colspan="3">
			<input  type="button" class="button_ellipse"  value='<fmt:message key="submitorder"/>' onclick="javascript:save_onClick()">
			<input  type="button" class="button_ellipse"  value='<fmt:message key="back"/>' onclick="javascript:history.go(-1)">		
		</td>
	</tr>
	<tr><td>&nbsp;<td></tr>
	</table>
</div>
</form>
</fmt:bundle>
<script language="javascript">
	writeTableBottom('<venus:base/>/');
</script>
</body>
<script type="text/javascript">
    function save_onClick(){
        if(checkAllForms()){
            form.submit();
        }
    }
</script>
<%@page import="venus.frames.i18n.util.LocaleHolder"%>
</html>
