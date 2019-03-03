<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="venus.frames.i18n.util.LocaleHolder"%>
<%@ page import="udp.ewp.tools.helper.EwpVoHelper" %>
<%@ page import="udp.ewp.requests.util.IRequestsConstants"%>

<html>
<fmt:bundle basename="udp.ewp.ewp_resource" >
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
          jQuery("form").attr("action","<venus:base/>/HotWordsAction.do?id=" + ids);
            jQuery("input[name='cmd']").val("find");
            jQuery("form").submit();
    }

      //重置查询条件
      function resetForm(){
            jQuery("input[name='template_name']").val('');
            jQuery("input[name='template_content']").val('');
      }

    function deleteMulti_onClick(){
        var ids = findSelections();
        if(ids == null) {  //如果ids为空
             alert('<fmt:message key="udp.ewp.select_records" />');
            return;
        }
        if(confirm('<fmt:message key="whether_to_delete_the_data_completely" bundle="${applicationResources}"/>')) {
            jQuery("form").attr("action","<venus:base/>/HotWordsAction.do?ids=" + ids);
            jQuery("input[name='cmd']").val("deleteMulti");
            jQuery("form").submit();
            }
    }

    function simpleQuery_onClick(){  //简单的模糊查询
            jQuery("form").attr("action","<venus:base/>/HotWordsAction.do?backFlag=true");
            jQuery("input[name='cmd']").val("simpleQuery");
            jQuery("form").submit();
    }

    function toAdd_onClick() {  //到增加记录页面
         jQuery("form").attr("action","<venus:base/>/jsp/ewp/hotWords/insertHotWords.jsp");
          jQuery("form").submit();
    }

    function detail_onClick(){  //实现转到详细页面
      var ids = findSelections();
        if(ids == null) {
            alert('<fmt:message key="udp.ewp.select_one_record"/>');
            return;
        }
        if(ids.length > 1) {
            alert('<fmt:message key="udp.ewp.only_can_a_record"/>');
            return;
        }
        jQuery("form").attr("action","<venus:base/>/HotWordsAction.do?id=" + ids);
        jQuery("input[name='cmd']").val("detail");
        jQuery("form").submit();
    }

</script>
<script language="javascript">
    jQuery(function(){
        jQuery("td table tr").bind("dblclick",function(){
            var id =  jQuery(this).children("td").children("input[name='checkbox_template'][type='checkbox']").val();
            if(id != null && id != "undefined"){
                form.action="<venus:base/>/HotWordsAction.do?id=" + id;
                form.cmd.value = "detail";
                form.submit();
            }
            return false;
        })
    });
</script>

</head>
<body>
<div style="width:100%;">
<script language="javascript">
     writeTableTop('<fmt:message key="udp.ewp.hotwords_manage" />','<venus:base/>/themes/<venus:theme/>/');
</script>
<form name="form" method="post" action="<venus:base/>/HotWordsAction.do?backFlag=true">
<input type="hidden" name="cmd" value="simpleQuery">
<div id="ccParent0">
<table class="table_div_control">
    <tr>
        <td>
            <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild0',this,'<venus:base/>/themes/<venus:theme/>/')" >&nbsp;<fmt:message key="query_with_condition" bundle="${applicationResources}"/>
        </td>
    </tr>
</table>
</div>

<div id="ccChild0">
<table class="table_div_content" border="0">
        <tr>
            <td align="center">
                <fmt:message key="udp.ewp.hotwords.name"/>
                <input type="text" class="text_field" name="name" validate="isSearch" inputName='<fmt:message key="udp.ewp.hotwords.name"/>' maxLength="50"/>
                <input name="button_ok" class="button_ellipse" type="button" value='<fmt:message key="query" bundle="${applicationResources}"/>' onClickTo="javascript:simpleQuery_onClick()">
                <input name="button_reset" class="button_ellipse" type="button" value='<fmt:message key="clear" bundle="${applicationResources}"/>' onClick="javascript:this.form.reset()"></td>
        </tr>
</table>
</div>

<div id="ccParent1">
<table class="table_div_control">
    <tr>
        <td>
            <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key="list_page" bundle="${applicationResources}"/>
        </td>
        <td>
            <table align="right">
                <tr>
                    <td><input name="button_view" class="button_ellipse" type="button" value='<fmt:message key="view" bundle="${applicationResources}"/>' onClick="javascript:detail_onClick();"></td>
                    <td><input name="button_add" class="button_ellipse" type="button" value='<fmt:message key="add" bundle="${applicationResources}"/>' onClick="javascript:toAdd_onClick();"></td>
                    <td><input name="button_delete" class="button_ellipse" type="button" value='<fmt:message key="delete" bundle="${applicationResources}"/>'  onClick="javascript:deleteMulti_onClick();"></td>
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
        <td>
        <layout:collection name="beans" id="rmBean" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0">

            <layout:collectionItem width="1%" title="<input type='checkbox' pdType='control' control='checkbox_template'/>" style="text-align:center;">
                <bean:define id="rmValue" name="rmBean" property="id"/>
                <bean:define id="rmDisplayName" name="rmBean" property="id"/>
                <input type="checkbox" name="checkbox_template" value="<%=rmValue%>" displayName="<%=rmDisplayName%>"/>
            </layout:collectionItem>

            <layout:collectionItem width="3%"  title='<%=LocaleHolder.getMessage("sequence") %>' style="text-align:center;">
                <venus:sequence/>
                <bean:define id="rmValue" name="rmBean" property="id"/>
                <input type="hidden" signName="hiddenId" value="<%=rmValue%>"/>
            </layout:collectionItem>

            <layout:collectionItem width="8%" title='<%=LocaleHolder.getMessage("udp.ewp.hotwords.name") %>' property="name" sortable="true"/>
            <layout:collectionItem width="8%" title='<%=LocaleHolder.getMessage("udp.ewp.hotwords.link") %>' property="link" sortable="true"/>
            <layout:collectionItem width="8%" title='<%=LocaleHolder.getMessage("udp.ewp.hotwords.enable_status") %>' property="enableStatus" sortable="true">
                <bean:define id="status" name="rmBean" property="enableStatus" />
                <%if(status.equals("1")){%>
                    <fmt:message key="enable" bundle="${applicationResources}"/>
                <%}else{%>
                    <fmt:message key="disable" bundle="${applicationResources}"/>
                <%}%>
            </layout:collectionItem>

            </layout:collection>
        <jsp:include page="/jsp/include/page.jsp" />
        </td>
    </tr>
</table>
</div>
</form>
</div>
</body>
</fmt:bundle>
</html>
<script language="javascript">
<%
    if(request.getAttribute(IRequestsConstants.REQUEST_WRITE_BACK_FORM_VALUES) != null) {  
        out.print(EwpVoHelper.writeBackMapToForm((java.util.Map)request.getAttribute(IRequestsConstants.REQUEST_WRITE_BACK_FORM_VALUES)));  
    }
%>
</script>
