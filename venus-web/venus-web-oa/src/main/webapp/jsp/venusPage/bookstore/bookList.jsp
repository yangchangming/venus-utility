<!--
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="venus.frames.mainframe.util.VoHelper" %>
<%@ page import="venus.frames.i18n.util.LocaleHolder"%>
<!-- fmt:setLocale value="zh" scope="session"/-->

<fmt:bundle basename="udp.template.bookstore.resource" prefix="udp.template.bookstore.">
<title><fmt:message key="querytemplate"/></title>
<script>
	function simpleQuery_onClick(){  //简单的模糊查询
    	form.submit();
  	}
	function detail_onClick(thisId){
		form.action = "<venus:base/>/TemplateAction.do?cmd=find&ids=" + thisId;
		form.submit();
	}
	function addToCart_onClick(){  
 		var elementCheckbox = document.getElementsByName("checkbox_template");
		var number = 0;
		var ids = "";
		for(var i=0;i<elementCheckbox.length;i++){
			if(elementCheckbox[i].checked) { 
				number += 1;
				ids +=  elementCheckbox[i].value;
			}
		}
		
		if(number == 0) {
	  		alert("<fmt:message key='selectbook'/>");  
	  		return;
		}
		if(number>1) {
			alert("<fmt:message key='onlyselectabook'/>");
			return;
		}
	   jQuery.get('<venus:base/>/BookStore.do?cmd=addItem2Cart&id='+ids,function(){
	   		alert("<fmt:message key='addsuccess'/>");
	   		jQuery(":checkbox").attr("checked", false); 
	   });
	}
	function listOrder_onClick(){
		window.location="<venus:base/>/BookStore.do?cmd=listOrder";
	}
	function refresh_onClick(){  //刷新本页
		form.submit();
	}
	function initOrder_onClick(){
		window.location="<venus:base/>/BookStore.do?cmd=initOrder";
	}
	function deleteItem(bookId){
		jQuery.get('<venus:base/>/BookStore.do?cmd=removeItemFromCart',{bookId:bookId},function(){
			alert("<fmt:message key='deletesuccess'/>");
			jQuery('#cartDetail').load('<venus:base/>/BookStore.do',{cmd:'listCart'},function(){
		                  getTbodyColor("#cartItemList");
		        });
		});
	}
	function updateCart(bookId,qurantity){
		jQuery.get('<venus:base/>/BookStore.do?cmd=updateCart&'+bookId+'='+qurantity,function(){
			jQuery('#cartDetail').load('<venus:base/>/BookStore.do',{cmd:'listCart'},function(){
	               getTbodyColor("#cartItemList");
	        });
		});
	}

  function showCart() {
		 jQuery("#cartDetail").dialog("open");
		jQuery('#cartDetail').load('<venus:base/>/BookStore.do',{cmd:'listCart'},function(){
		          getTbodyColor("#cartItemList");
		});
		
		
  }
  jQuery(document).ready(function(){
  		 jQuery("#cartDetail").dialog({ modal: true, autoOpen:false,resizable:false,width:800,height:150,overlay: { opacity: 0.1, background: "black" }});
		
		jQuery("#image1").toggle(
		  function () {
		    jQuery('#ccChild1').slideUp('slow');
		  },
		  function () {
		   jQuery('#ccChild1').slideDown("slow"); 
		  }
		);
  		
 });
</script>

</head>
<body align="center">
<script language="javascript">
	writeTableTop('<fmt:message key="booklist"/>','<venus:base/>/themes/<venus:theme/>/');
</script>
<div id="cartDetail" title="<fmt:message key='cartdetail'/>"></div>
<form name="form" method="post" action="<venus:base/>/BookStore.do?cmd=queryAllBooks">

<!-- 查询开始 -->
<div id="ccParent0"> 
<table class="table_div_control">
	<tr> 
		<td>
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" onClick="javascript:hideshow('ccChild0',this,'<venus:base/>/themes/<venus:theme/>/')">
			<fmt:message key="querywithparas"/>
		</td>
		<td  align="right">
    		<logic:empty name="accountInSession">游客</logic:empty>
			<logic:notEmpty name="accountInSession"><bean:define id="name" name="accountInSession" property="name"></bean:define><%=name%></logic:notEmpty>，您好
		</td>
	</tr>
</table>
</div>
<div id="ccChild0"> 
<table class="table_div_content">
<tr>
	<td align="right" width="10%" nowrap><fmt:message key="bookname"/></td>
	<td>
		<input name="bookName" type="text" class="text_field" inputName='<fmt:message key="bookname"/>' validate="isSearch" rubber_id="build_Id">
	</td>
</tr>
<tr>
	<td align="right" width="10%" nowrap><fmt:message key="author"/></td>
	<td><input name="author" type="text" class="text_field" inputName='<fmt:message key="author"/>' validate="isSearch"><input type="button" name="Submit" class="button_ellipse" value='<fmt:message key="query" bundle="${applicationResources}"/>' onClickTo="javascript:simpleQuery_onClick();">
	</td>
</tr>
</table>

</div>
<!--
<div id="container" class="cartDetail" style="border-style:solid;border-width:2pt; border-color:palegreen">
		<div style="height:28px;background-color:gold" onclick="javascript:$('#container').css('display','none')"></div>
		<div id="cartDetail" ></div>
</div>
-->

<div id="ccParent1"> 
<table class="table_div_control">
	<tr > 
		<td>
		<!--	<img src="<venus:base/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/')">&nbsp;明细表格-->
		
			<img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" id="image1" class="div_control_image" >&nbsp;<fmt:message key="detailtable"/>
		</td>
		<td> 
			<table align="right">
				<tr> 
					<td class="button_ellipse" onClick="javascript:addToCart_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/add.gif" class="div_control_image"><fmt:message key="addcart"/></td>
					<td class="button_ellipse" id="viewCartButton" onclick="showCart()"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/view.gif" class="div_control_image"><fmt:message key="viewcart"/></td>
					<td class="button_ellipse" onClick="javascript:initOrder_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/view.gif" class="div_control_image"><fmt:message key="settlement"/></td>
					<td class="button_ellipse" onClick="javascript:listOrder_onClick();"><img src="<venus:base/>/themes/<venus:theme/>/images/icon/view.gif" class="div_control_image"><fmt:message key="vieworders"/></td>
					<!--
					<td class="button_ellipse" onClick="javascript:deleteMulti_onClick();"><img src="<venus:base/>/images/icon/delete.gif" class="div_control_image">删除</td>
					<td class="button_ellipse" onClick="javascript:findCheckbox_onClick();"><img src="<venus:base/>/images/icon/modify.gif" class="div_control_image">修改</td>
					-->
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
		<layout:collection name="books" id="book" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >
			<layout:collectionItem width="5%" title="" style="text-align:center;">
				<bean:define id="bookId" name="book" property="id"/>
				<input type="radio" name="checkbox_template" value="<%=bookId%>"/>
			</layout:collectionItem>
			<layout:collectionItem width="5%"  title='<%=LocaleHolder.getMessage("sequence") %>' style="text-align:center;">
				<venus:sequence/>
			</layout:collectionItem>
			<layout:collectionItem width="15%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.bookname") %>' property="bookName" sortable="true" style="text-align:center;"/>
			<layout:collectionItem width="15%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.author") %>' property="author" sortable="true" style="text-align:center;"/>
			<layout:collectionItem width="15%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.price") %>' property="price" sortable="true" style="text-align:center;"/>
			<layout:collectionItem width="15%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.coAuthor") %>' property="coAuthor" sortable="true" style="text-align:center;"/>
			<layout:collectionItem width="15%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.isbn") %>' property="isbn" sortable="true" style="text-align:center;"/>
			<layout:collectionItem width="15%" title='<%=LocaleHolder.getMessage("udp.template.bookstore.consern") %>' property="bookConsern" sortable="true" style="text-align:center;"/>
		</layout:collection>
		<jsp:include page="/jsp/include/page.jsp" />
		</td>
	</tr>
</table>
</div>
</form>
<script language="javascript">
	writeTableBottom('<venus:base/>/themes/<venus:theme/>/');

	/*
	.subMainFrameSet.frames['leftFrameSet'];
	<!--["leftFrameSet"].frames["userFrame"]-->
	*/
</script>

</body>
</html>
</fmt:bundle>
<%  //表单回写
	if(request.getAttribute("writeBackFormValues") != null) {
		out.print("<script language=\"javascript\">\n");
		out.print(VoHelper.writeBackMapToForm((java.util.Map)request.getAttribute("writeBackFormValues")));
		out.print("writeBackMapToForm();\n");
		out.print("</script>");
	}
%>