<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page errorPage="/jsp/include/errorpage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-template.tld" prefix="template" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="/WEB-INF/gap-html.tld" prefix="venus" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
response.setHeader("Pragma","no-cache"); //HTTP 1.0 
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<%@page import="venus.frames.i18n.util.LocaleHolder"%>
<fmt:bundle basename="udp.template.bookstore.resource" prefix="udp.template.bookstore.">
<div style="overflow:auto;height:150px;">
<table class="table_div_content" id="cartItemList">
	<tr>
		<td>
		<layout:collection name="cartInSession" property="items" id="item"  styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >
			<layout:collectionItem width="5%"  title='<%=LocaleHolder.getMessage("sequence") %>' style="text-align:center;">
				<venus:sequence/>
				<input type="hidden" name="checkbox_template" />
			</layout:collectionItem>
			<layout:collectionItem width="20%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.bookname") %>'  property="book.bookName"  style="text-align:center;"/>
			<layout:collectionItem width="15%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.author") %>' property="book.author"  style="text-align:center;"/>
			<layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.price") %>' property="book.price"  style="text-align:center;"/>
			<layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.quantity") %>' property="quantity"  style="text-align:center;">
					<bean:define id="bookQuantity" name="item" property="quantity"/>
					<bean:define id="bookId" name="item" property="book.id"/>
					<input type="text" name="<%=bookId%>" value="<%=bookQuantity%>" onchange="updateCart(this.name,this.value)" style="text-align:center;width:40px" class="text_field" size="4"/>
			</layout:collectionItem>
			<layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.totalPrice") %>'  property="totalPrice"  style="text-align:center;"/>
			<logic:notEmpty name="accountInSession"> 
					<layout:collectionItem width="10%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.vipPrice") %>'  property="vipPrice"  style="text-align:center;color:red"/>
			</logic:notEmpty>
			<layout:collectionItem width="15%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.operation") %>'  url=""  style="text-align:center;">
				<bean:define id="bookId" name="item" property="book.id"/>
				<a href="#" onclick="deleteItem('<%=bookId%>')"><fmt:message key="delete"/></a>
			</layout:collectionItem>
		</layout:collection>
		</td>
	</tr>
	<tr>	<td>&nbsp;</td></tr>
</table>
<div>
</fmt:bundle>