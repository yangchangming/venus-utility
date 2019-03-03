<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="udp.ewp.tools.helper.EwpVoHelper" %>
<%@ page import="venus.frames.i18n.util.LocaleHolder" %>
<%@ page import="udp.ewp.template.util.ITemplateConstants" %>
<html>
<fmt:bundle basename="udp.ewp.ewp_resource" prefix="udp.ewp.">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key="template_manage" bundle="${applicationResources}"/></title>
<script language="javascript">

    //获得选中的模板
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
    
    //转入修改模板界面
    function toMofidy_onClick() { 
        var ids = findSelections(); 
        if(ids == null) { 
            alert('<fmt:message key="select_one_record"/>');
            return;
        }
        if(ids.length > 1) {  
            alert('<fmt:message key="only_can_a_record"/>');
            return;
        }
        form.action="<%=request.getContextPath()%>/TemplateTypeAction.do?id=" + ids;
        form.cmd.value = "find";
        form.submit();
    }  
    
    //删除多个模板
    function deleteMulti_onClick(){  
        var ids = findSelections(); 
        if(ids == null) {
            alert('<fmt:message key="select_records"/>');
            return;
        }
        if(confirm('<fmt:message key="whether_to_delete_the_data_completely"  bundle="${applicationResources}"/>')) { 
            form.action="<%=request.getContextPath()%>/TemplateTypeAction.do?ids=" + ids;
            form.cmd.value = "deleteTemplateTypes";
            form.submit();
        }
    }
    
    //转入增加界面
    function toAdd_onClick() {  
        form.action="<%=request.getContextPath()%>/jsp/ewp/templateType/insertTemplateType.jsp";
        form.submit();
    }
    
    //查看详细信息
    function detail_onClick(){ 
        var ids = findSelections(); 
        if(ids == null) { 
            alert('<fmt:message key="select_one_record"/>');
            return;
        }
        if(ids.length > 1) {
            alert('<fmt:message key="only_can_a_record"/>');
            return;
        }
        form.action="<%=request.getContextPath()%>/TemplateTypeAction.do?id=" + ids;
        form.cmd.value = "detail";
        form.submit();
    }

</script>
</head>
<body>
<div style="width:100%;">
<script language="javascript">
    writeTableTop('<fmt:message key="list_page" bundle="${applicationResources}"/>','<venus:base/>/themes/<venus:theme/>/');  
</script>
<form name="form" method="post" action="<%=request.getContextPath()%>/EwpTemplateTypeAction.do?backFlag=true">
<input type="hidden" name="cmd" value="simpleQuery">                  
<div id="ccParent1"> 
<table class="table_div_control">
    <tr> 
        <td>
            <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image" onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message key="list_page" bundle="${applicationResources}"/>
        </td>
        <td> 
            <table align="right">
                <tr>
                    <td class="button_ellipse" onClick="javascript:detail_onClick();"><fmt:message key="view" bundle="${applicationResources}"/></td>
                    <td class="button_ellipse" onClick="javascript:toAdd_onClick();"><fmt:message key="add" bundle="${applicationResources}"/></td>
                    <td class="button_ellipse" onClick="javascript:deleteMulti_onClick();"><fmt:message key="delete" bundle="${applicationResources}"/></td>
                    <td class="button_ellipse" onClick="javascript:toMofidy_onClick();"><fmt:message key="modify" bundle="${applicationResources}"/></td>
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
        <layout:collection name="beans" id="template" styleClass="listCss" width="100%" indexId="orderNumber" align="center" sortAction="0" >
           <layout:collectionItem width="1%" title="<input type='checkbox' pdType='control' control='checkbox_template'/>" style="text-align:center;">
                <input type="checkbox" name="checkbox_template" value="${pageScope.template.id}"/>
            </layout:collectionItem>
            <layout:collectionItem width="5%"  title='<%=LocaleHolder.getMessage("sequence") %>' style="text-align:center;">
                <venus:sequence/>
            </layout:collectionItem>
            <layout:collectionItem width="14%" title='<%=LocaleHolder.getMessage("udp.ewp.template_type_name") %>'  property="typeName" sortable="false"/>
            <layout:collectionItem width="14%" title='<%=LocaleHolder.getMessage("udp.ewp.template_type_description") %>' property="description" sortable="false"/>
        </layout:collection>
     
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
<%  //表单回写
    if(request.getAttribute(ITemplateConstants.REQUEST_WRITE_BACK_FORM_VALUES) != null) {  //如果request中取出的表单回写bean不为空
        out.print(EwpVoHelper.writeBackMapToForm((java.util.Map)request.getAttribute(ITemplateConstants.REQUEST_WRITE_BACK_FORM_VALUES)));  //输出表单回写方法的脚本
    }
%>
</script>   
