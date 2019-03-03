<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/jsp/include/global.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="venus.frames.i18n.util.LocaleHolder"%>
<%@ page import="udp.ewp.tools.helper.EwpVoHelper"%>
<%@ page import="udp.ewp.tools.helper.EwpStringHelper"%>
<%@ page import="udp.ewp.requests.model.Requests"%>
<%@ page import="udp.ewp.requests.util.IRequestsConstants"%>
<%@ page import="udp.ewp.util.EnumTools"%>
<%
    List lResult = null;
    if (request.getAttribute(IRequestsConstants.REQUEST_BEANS) != null) {
        lResult = (List) request.getAttribute(IRequestsConstants.REQUEST_BEANS);
    }
    Iterator itLResult = null;
    if (lResult != null) {
        itLResult = lResult.iterator();
    }
    Requests resultVo = null;
    request.setAttribute("wheretoknowMap",EnumTools.getSortedEnumMap(EnumTools.WHERETOKNOW));
%>
<html>
<fmt:bundle basename="udp.ewp.ewp_resource">
	<head>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title></title>
	<script language="javascript">
    function findSelections() {
        var ids = null; 
         jQuery("input[name='checkbox_template']:checkbox").each(function(){
            if(jQuery(this).is(":checked")){
                if(ids == null){
                    ids = new Array(0);
                }
                ids.push(jQuery(this).attr("value"));
            }
         });
           return ids;
    }
	function toMofidy_onClick() {  //从多选框到修改页面
		var ids = findSelections(); 
		if(ids == null) {  //如果ids为空
	  		alert('<fmt:message key="udp.ewp.select_one_record"/>');
	  		return;
		}
		if(ids.length > 1) {  //如果ids有2条以上的纪录
	  		alert('<fmt:message key="udp.ewp.only_can_a_record"/>');
	  		return;
		}
	      jQuery("form").attr("action","<venus:base/>/RequestsAction.do?id=" + ids);
            jQuery("input[name='cmd']").val("find");
            jQuery("form").submit();
	}  
	
	  function resetForm(){  
	        jQuery("input[name='template_name']").val('');
	        jQuery("input[name='template_content']").val('');
	  }
    
	function deleteMulti_onClick(){ 
 		var ids = findSelections(); 
		if(ids == null)	{ 
			 alert('<fmt:message key="udp.ewp.select_records" />');
			return;
		}
		if(confirm('<fmt:message key="whether_to_delete_the_data_completely" bundle="${applicationResources}"/>')) { 
            jQuery("form").attr("action","<venus:base/>/RequestsAction.do?ids=" + ids);
	        jQuery("input[name='cmd']").val("deleteMulti");
	        jQuery("form").submit();
            }
	}
	
	function simpleQuery_onClick(){
	        jQuery("form").attr("action","<venus:base/>/RequestsAction.do?backFlag=true");
	        jQuery("input[name='cmd']").val("simpleQuery");
	        jQuery("form").submit();
  	}
  	
	function toAdd_onClick() {
		 jQuery("form").attr("action","<%=request.getContextPath()%>/jsp/ewp/requests/insertRequests.jsp");
	      jQuery("form").submit();
	}
	
	function detail_onClick(){
	  var ids = findSelections(); 
        if(ids == null) { 
            alert('<fmt:message key="udp.ewp.select_one_record"/>');
            return;
        }
        if(ids.length > 1) {
            alert('<fmt:message key="udp.ewp.only_can_a_record"/>');
            return;
        }
        jQuery("form").attr("action","<venus:base/>/RequestsAction.do?id=" + ids);
        jQuery("input[name='cmd']").val("detail");
        jQuery("form").submit();
	}

</script>
	<script language="javascript">
    jQuery(function(){
        jQuery("td table tr").bind("dblclick",function(){
            var id =  jQuery(this).children("td").children("input[name='checkbox_template'][type='checkbox']").val();
            if(id != null && id != "undefined"){
                form.action="<venus:base/>/RequestsAction.do?id=" + id;
		        form.cmd.value = "detail";
		        form.submit();
		        return false;
            }
            return false;
        })
    });
</script>

	</head>
	<body>
	<div style="width:100%;">
	<script language="javascript">
	 writeTableTop('<fmt:message key="udp.ewp.requests_manage" />','<venus:base/>/themes/<venus:theme/>/');
</script>
	<form name="form" method="post" action="<%=request.getContextPath()%>/RequestsAction.do?backFlag=true"><input type="hidden" name="cmd" value="simpleQuery">
	<div id="ccParent0">
	<table class="table_div_control">
		<tr>
			<td><img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild0',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message
				key="query_with_condition" bundle="${applicationResources}" /></td>
		</tr>
	</table>
	</div>

	<div id="ccChild0">
	<table class="table_div_content">
		<tr>
			<td align="right"><fmt:message key="udp.ewp.requests.first_name" /></td>
			<td><input type="text" class="text_field" name="first_name" inputName="<fmt:message key="udp.ewp.requests.first_name"/>" maxLength="10" validate="isSearch"/></td>
			<td align="right"><fmt:message key="udp.ewp.requests.last_name" /></td>
			<td><input type="text" class="text_field" name="last_name" inputName='<fmt:message key="udp.ewp.requests.last_name"/>' maxLength="10" validate="isSearch"/></td>
			<td align="right"><fmt:message key="udp.ewp.requests.requestTitle" /></td>
			<td><input type="text" class="text_field" name="title" inputName='<fmt:message key="udp.ewp.requests.requestTitle"/>' maxLength="10" validate="isSearch"/></td>
		</tr>
		<tr>
			<td align="right"><fmt:message key="udp.ewp.requests.phone" /></td>
			<td><input type="text" class="text_field" name="phone" inputName='<fmt:message key="udp.ewp.requests.phone"/>' maxLength="10" validate="isSearch"/></td>
			 <td align="right"><fmt:message key="udp.ewp.requests.country" /></td>
            <td><input type="text" class="text_field" name="country" inputName='<fmt:message key="udp.ewp.requests.country"/>' maxLength="25" validate="isSearch"/></td>
            <td align="right"><fmt:message key="udp.ewp.requests.website" /></td>
            <td><input type="text" class="text_field" name="website" inputName='<fmt:message key="udp.ewp.requests.website"/>' maxLength="50" validate="isSearch"/></td>
		</tr>
		<tr>
		    <td align="right"><fmt:message key="udp.ewp.requests.email" /></td>
            <td><input type="text" class="text_field" name="email" inputName='<fmt:message key="udp.ewp.requests.email"/>' maxLength="100" validate="isSearch"/></td>
			<td align="right"><fmt:message key="udp.ewp.requests.referer" /></td>
			<td>
			 <select name="referer" id="referer" inputName='<fmt:message key="udp.ewp.requests.referer"/>'>
				<logic:iterate id="tempType" name="wheretoknowMap">
					<option value="<bean:write name="tempType" property="key"/>"><bean:write name="tempType" property="value" /></option>
				</logic:iterate>
			</select>
			</td>
			 <td align="center" colspan="2">
			 <input name="button_ok" class="button_ellipse" type="button" value='<fmt:message key="query" bundle="${applicationResources}"/>' onClickTo="javascript:simpleQuery_onClick()">
            <input name="button_reset" class="button_ellipse" type="button" value='<fmt:message key="clear" bundle="${applicationResources}"/>' onClick="javascript:this.form.reset()">
            </td>
		</tr>
	</table>
	</div>

	<div id="ccParent1">
	<table class="table_div_control">
		<tr>
			<td><img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message
				key="list_page" bundle="${applicationResources}" /></td>
			<td>
			<table align="right">
				<tr>
					<td><input name="button_view" class="button_ellipse" type="button" value='<fmt:message key="view" bundle="${applicationResources}"/>' onClick="javascript:detail_onClick();"></td>
					<td><input name="button_add" class="button_ellipse" type="button" value='<fmt:message key="add" bundle="${applicationResources}"/>' onClick="javascript:toAdd_onClick();"></td>
					<td><input name="button_delete" class="button_ellipse" type="button" value='<fmt:message key="delete" bundle="${applicationResources}"/>' onClick="javascript:deleteMulti_onClick();"></td>
					<td><input name="button_modify" class="button_ellipse" type="button" value='<fmt:message key="modify" bundle="${applicationResources}"/>' onClick="javascript:toMofidy_onClick();"></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	</div>

	<div id="ccChild1">
	<table class="table_div_content2">
		<tr>
			<td><layout:collection name="beans" id="rmBean" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0">

				<layout:collectionItem width="1%" title="<input type='checkbox' pdType='control' control='checkbox_template'/>" style="text-align:center;">
					<bean:define id="rmValue" name="rmBean" property="id" />
					<bean:define id="rmDisplayName" name="rmBean" property="id" />
					<input type="checkbox" name="checkbox_template" value="<%=rmValue%>" displayName="<%=rmDisplayName%>" />
				</layout:collectionItem>
				<layout:collectionItem width="3%" title='<%=LocaleHolder.getMessage("sequence") %>' style="text-align:center;">
					<venus:sequence />
					<bean:define id="rmValue" name="rmBean" property="id" />
					<input type="hidden" signName="hiddenId" value="<%=rmValue%>" />
				</layout:collectionItem>
				<layout:collectionItem width="8%" title='<%=LocaleHolder.getMessage("udp.ewp.requests.first_name") %>' property="first_name" sortable="true" />
				<layout:collectionItem width="8%" title='<%=LocaleHolder.getMessage("udp.ewp.requests.last_name") %>' property="last_name" sortable="true" />
				<layout:collectionItem width="8%" title='<%=LocaleHolder.getMessage("udp.ewp.requests.requestTitle") %>' property="title" sortable="true" />
				<layout:collectionItem width="8%" title='<%=LocaleHolder.getMessage("udp.ewp.requests.company") %>' property="company" sortable="true" />
				<layout:collectionItem width="8%" title='<%=LocaleHolder.getMessage("udp.ewp.requests.email") %>' property="email" sortable="true" />
				<layout:collectionItem width="8%" title='<%=LocaleHolder.getMessage("udp.ewp.requests.phone") %>' property="phone" sortable="true" />
				<layout:collectionItem width="8%" title='<%=LocaleHolder.getMessage("udp.ewp.requests.country") %>' property="country" sortable="true" />
				<layout:collectionItem width="8%" title='<%=LocaleHolder.getMessage("udp.ewp.requests.referer") %>' property="referer" sortable="true" />
			</layout:collection> <jsp:include page="/jsp/include/page.jsp" /></td>
		</tr>
	</table>
	</div>

	</form>
</fmt:bundle>
</div>
</body>
</html>
<script language="javascript">
<%
    if(request.getAttribute(IRequestsConstants.REQUEST_WRITE_BACK_FORM_VALUES) != null) {  
        out.print(EwpVoHelper.writeBackMapToForm((java.util.Map)request.getAttribute(IRequestsConstants.REQUEST_WRITE_BACK_FORM_VALUES)));  
    }
%>
</script>
