<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/gap-html.tld" prefix="venus" %>
<%@ page import="venus.frames.web.page.PageVo" %>

<%  
	//翻页
	PageVo pageVo = new PageVo();
	if(request.getAttribute("VENUS_PAGEVO_KEY") != null) {
		pageVo = (PageVo) request.getAttribute("VENUS_PAGEVO_KEY");
	}
%>
 <fmt:bundle basename="ApplicationResources">
<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
	<tr valign="middle">
		<td align="left" class="page_text">
		<!-- 当前<span class="page_text01"><%=pageVo.getCurrentPage()%></span>页/共<span class="page_text01"><%=pageVo.getPageCount()%></span>页&nbsp;&nbsp;每页<span class="page_text01"><%=pageVo.getPageSize()%></span>条/共<span class="page_text01"><%=pageVo.getRecordCount()%></span>条记录&nbsp;&nbsp;第
           * -->
       
            <fmt:message key="layout.page.Lable" >
                <fmt:param><span class="page_text01"><%=pageVo.getCurrentPage()%></span></fmt:param>
                <fmt:param><span class="page_text01"><%=pageVo.getPageCount()%></span></fmt:param>
                <fmt:param><span class="page_text01"><%=pageVo.getPageSize()%></span></fmt:param>
                <fmt:param><span class="page_text01"><%=pageVo.getRecordCount()%></span></fmt:param>
                <fmt:param><input name="VENUS_PAGE_NO_KEY_INPUT" type="text" size="2" maxlength="5" value="<%=pageVo.getCurrentPage()%>" style="border:1px solid #90b3cf; margin-right:4px;"></fmt:param>
            </fmt:message>
        
            
            
            <input name="goAppointedButton" type="button" class="button_ellipse" value="GO" onClick="javascript:goAppointedPage()" style="width:30px;">
	  </td>
		<td align="right">
		<table>
		<tr align="right">
		<td><%if(pageVo.getCurrentPage()>1){%><a href="JavaScript:firstPage();"><%}%><img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/<%=(pageVo.getCurrentPage()>1)?"icon_page_frist.gif":"icon_page_frist1.gif"%>" width="10" height="12" border='0' alt="<fmt:message key='home_page'/>"><%if(pageVo.getCurrentPage()>1){%></a><%}%></td>
		<td><%if(pageVo.getCurrentPage()>1){%><a href="JavaScript:upPage();"><%}%><img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/<%=(pageVo.getCurrentPage()>1)?"icon_page_prev.gif":"icon_page_prev1.gif"%>" width="7" height="12" border='0' alt="<fmt:message key='previous_page'/>"><%if(pageVo.getCurrentPage()>1){%></a><%}%></td>
		<td width="20"><%if(pageVo.getPageCount()>=pageVo.getCurrentPage()+1){%><a href="JavaScript:downPage();"><%}%><img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/<%=(pageVo.getPageCount()>=pageVo.getCurrentPage()+1)?"icon_page_next.gif":"icon_page_next1.gif"%>" width="7" height="12" border='0' alt="<fmt:message key='next_page'/>"><%if(pageVo.getPageCount()>=pageVo.getCurrentPage()+1){%></a><%}%></td>
		<td><%if(pageVo.getPageCount()>pageVo.getCurrentPage()){%><a href="JavaScript:lastPage();"><%}%><img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/<%=(pageVo.getPageCount()>=pageVo.getCurrentPage()+1)?"icon_page_last.gif":"icon_page_last1.gif"%>" width="10" height="12" border='0' alt="<fmt:message key='last_page'/>"><%if(pageVo.getPageCount()>pageVo.getCurrentPage()){%></a><%}%></td>
		<td width="1">
			<input type="hidden" name="VENUS_PAGE_NO_KEY" value="<%=pageVo.getCurrentPage()%>">
			<input type="hidden" name="VENUS_PAGE_COUNT_KEY" value="<%=pageVo.getPageCount()%>">
			<input type="hidden" name="VENUS_PAGE_SIZE_KEY" value="<%=pageVo.getPageSize()%>">
<%
	if ( null != pageVo.getOrderKey() ){
%>
			<input type="hidden" name="VENUS_ORDER_KEY" value="<%=pageVo.getOrderKey()%>">
<%	
	}
%>
		</td>
		</tr>
		</table>
	  </td>
	</tr>
</table>
</fmt:bundle>
<script language="javascript">

//翻页相关
function firstPage(){  //去首页
	form.VENUS_PAGE_NO_KEY.value=1;
	form.submit();
}
function upPage(){  //去上一页
	form.VENUS_PAGE_NO_KEY.value--;
	form.submit();
}
function downPage(){  //去下一页
	form.VENUS_PAGE_NO_KEY.value++;
	form.submit();
}
function lastPage(){  //去末页
	form.VENUS_PAGE_NO_KEY.value=<%=pageVo.getPageCount()%>;
	form.submit();
}
function goAppointedPage(){  //直接跳到某页
	checkPageNoKey();
	form.submit();
}
function checkPageNoKey() {
	form.VENUS_PAGE_NO_KEY.value = form.VENUS_PAGE_NO_KEY_INPUT.value;
	if(form.VENUS_PAGE_NO_KEY_INPUT.value <= 0) {
		form.VENUS_PAGE_NO_KEY.value = 1;
	}
}
</script>