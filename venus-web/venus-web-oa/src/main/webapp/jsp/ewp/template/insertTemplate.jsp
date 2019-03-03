<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="udp.ewp.tools.helper.EwpVoHelper" %>
<%@ page import="udp.ewp.template.model.EwpTemplate" %>
<%@ page import="udp.ewp.template.util.ITemplateConstants" %>
<%@ page import="udp.ewp.util.EnumTools" %>
<%@ page import="java.util.*" %>
<%@ include file="/jsp/include/global.jsp" %>
<%
    //是否为修改页面
    EwpTemplate resultVo = new EwpTemplate();
    boolean isModify = false;
    if (request.getParameter("isModify") != null) {
        isModify = true;
        if (request.getAttribute(ITemplateConstants.REQUEST_BEAN) != null) {
            resultVo = (EwpTemplate) request.getAttribute(ITemplateConstants.REQUEST_BEAN);
        }
    }
    request.setAttribute("ewpTemplateVo", resultVo);
    request.setAttribute("template_typeMap", EnumTools.getSortedEnumMap(EnumTools.TEMPLATETYPE));
%>
<html>
<fmt:bundle basename="udp.ewp.ewp_resource">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><!-- 标题 新增模板 || 修改模板 -->
        <script language="javascript">
            if (<%=isModify%>)
                out.print('<fmt:message key="modify_page" bundle="${applicationResources}"/>');
            else
                out.print('<fmt:message key="new_page" bundle="${applicationResources}"/>');
        </script>
    </title>
</head>
<body>
<script language="javascript">
    if (<%=isModify%>)
        writeTableTop('<fmt:message key="modify_page" bundle="${applicationResources}" />', '<venus:base/>/themes/<venus:theme/>/');
    else
        writeTableTop('<fmt:message key="new_page" bundle="${applicationResources}" />', '<venus:base/>/themes/<venus:theme/>/');
</script>
<form name="form" method="post">
    <div id="ccParent1">
        <table class="table_div_control">
            <tr>
                <td>
                    <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image"
                         onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">
                    <input name="button_save" class="button_ellipse" type="button"
                           value='<fmt:message key="save" bundle="${applicationResources}" />'
                           onClickTo="javascript:<%=isModify?"update_onClick()":"insert_onClick()"%>">
                    <input name="button_cancel" class="button_ellipse" type="button"
                           value='<fmt:message key="cancel" bundle="${applicationResources}"/>'
                           onClick="javascript:cancel_onClick()">
                </td>
            </tr>
        </table>
    </div>
    <div id="ccChild1">
        <table class="table_div_content">
            <tr>
                <td>
                    <table class="table_div_content_inner">
                        <tr>
                            <td align="right" width="100px"><fmt:message key="udp.ewp.website.name"/></td>
                            <td align="left">
                                <select id="webSites" name="webSiteId" onchange="webSite_onChange()">
                                    <logic:iterate id="web" name="webSites">
                                        <c:if test="${web.id eq requestScope.ewpTemplateVo.webSiteId}">
                                            <option value="${web.id}" selected>${web.websiteName}</option>
                                        </c:if>
                                        <c:if test="${web.id ne requestScope.ewpTemplateVo.webSiteId}">
                                            <option value="${web.id}">${web.websiteName}</option>
                                        </c:if>
                                    </logic:iterate>
                                </select>
                            </td>
                            <td align="right"></td>
                            <td align="left"></td>
                        </tr>
                        <tr>
                            <td align="right" width="100px"><span class="style_required_red">* </span><fmt:message
                                    key="udp.ewp.template_name"/></td>
                            <td align="left"><input type="text" class="text_field" name="template_name"
                                                    validate="notNull"
                                                    inputName="<fmt:message key="udp.ewp.template_name" />"
                                                    value="${requestScope.ewpTemplateVo.template_name}" maxLength="50"/>
                            </td>
                            <td align="right"></td>
                            <td align="left"></td>
                        </tr>
                        <tr>
                            <td align="right" width="100px"><span class="style_required_red">* </span><fmt:message
                                    key="udp.ewp.template_viewname"/></td>
                            <td align="left">
                                <input type="hidden" id="template_viewname_backup" name="template_viewname_backup"
                                       value="${requestScope.ewpTemplateVo.template_viewname}"/><input
                                    validate="notNull" type="text" class="text_field" name="template_viewname"
                                    onchange="javascript:checkViewCodeIsUnique()" id="template_viewname"
                                    inputName="<fmt:message key="udp.ewp.template_viewname" />"
                                    value="${requestScope.ewpTemplateVo.template_viewname}" maxLength="50"/> <span
                                    id="errorMsg">  <fmt:message key="udp.ewp.template_viewcode_unique"/></span>
                            </td>
                            <td align="right"></td>
                            <td align="left"></td>
                        </tr>
                        <tr>
                            <td align="right" width="100px"><span class="style_required_red">* </span><fmt:message
                                    key="udp.ewp.template_type"/>&nbsp;&nbsp;</td>
                            <td align="left">
                                <select name="template_type" id="template_type">
                                    <logic:iterate id="tempType" name="template_typeMap">
                                        <c:if test="${tempType.key eq requestScope.ewpTemplateVo.template_type}">
                                            <option value="<bean:write name="tempType" property="key"/>" selected>
                                                <bean:write name="tempType" property="value"/></option>
                                        </c:if>
                                        <c:if test="${tempType.key ne requestScope.ewpTemplateVo.template_type}">
                                            <option value="<bean:write name="tempType" property="key"/>"><bean:write
                                                    name="tempType" property="value"/></option>
                                        </c:if>
                                    </logic:iterate>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td align="right" width="100px"><span class="style_required_red">* </span><fmt:message
                                    key="udp.ewp.template_content"/></td>
                            <td colspan="3" align="left"><textarea cols="100" rows="20" name="template_content"
                                                                   inputName="<fmt:message key="udp.ewp.template_content" />"
                                                                   validate="notNull"
                                                                   maxLength="32767.5">${requestScope.ewpTemplateVo.template_content_of_filter}</textarea>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
    <input type="hidden" name="id" value="${requestScope.ewpTemplateVo.id}"> <input type="hidden"
                                                                                    name="create_date"/><input
        id="isPass" type="hidden" value="Y"/></form>
<script language="javascript">

    var templateForm = jQuery("form");

    //校验模板编码是否唯一并且是英文字符
    function checkViewCodeIsUnique() {
        var template_viewname = jQuery("#template_viewname").val();
        var regu = /^[a-z,A-Z,0-9,_]+$/;
        if (!regu.exec(template_viewname)) {
            jQuery("#errorMsg").html('<fmt:message key="udp.ewp.template_viewcode_only_char_or_number_or_underline" />');
            jQuery("#isPass").val('N');
            return true;
        }
        var template_viewname_backup = jQuery("#template_viewname_backup").val();
        if (equals(template_viewname, template_viewname_backup)) {
            jQuery("#errorMsg").html("<fmt:message key="udp.ewp.template_viewcode_doesnot_exist" />");
            jQuery("#isPass").val('Y');
            return true;
        }
        var selSiteId = jQuery("#webSites").val();
        jQuery.ajax({url: "<venus:base/>/EwpTemplateAction.do?cmd=checkViewCodeIsUnique&Action=get&template_viewname=" + template_viewname + "&webSiteId=" + selSiteId, async: false, cache: false, dataType: "text", success: function (data, textStatus) {
            var jsonResult = eval('(' + data + ')');
            var isPass = jsonResult.isPass;
            var returnMessage = jsonResult.returnMessage;
            jQuery("#isPass").val(isPass);
            jQuery("#errorMsg").html(returnMessage);
        }});
        return true;
    }

    function webSite_onChange() {
        var template_viewname = jQuery("#template_viewname").val();
        if (template_viewname != "") {
            checkViewCodeIsUnique();
        }
    }

    //保存模板
    function insert_onClick() {
        if (checkViewCodeIsUnique()) {
            var checkResult = checkIntegrityAndValid();
            if (checkResult != null && checkResult.length > 0) {
                alert(checkResult);
                return false;
            } else {
                templateForm.attr("action", "<venus:base/>/EwpTemplateAction.do?cmd=insert");
                templateForm.submit();
            }
        }
    }

    //保存前检查输入的完整性和合法性
    function checkIntegrityAndValid() {
        var isPass = jQuery("#isPass").val();
        if (isPass != null && isPass == 'N')
            return "<fmt:message key='udp.ewp.template_viewcode_not_unique' />";
        else
            return "";
    }

    //修改模板
    function update_onClick(id) {
        if (checkViewCodeIsUnique()) {
            var checkResult = checkIntegrityAndValid();
            if (checkResult != null && checkResult.length > 0) {
                alert(checkResult);
                return false;
            }
            if (!getConfirm()) {
                return false;
            }
            templateForm.attr("action", "<venus:base/>/EwpTemplateAction.do?cmd=update");
            templateForm.submit();
        }
    }

    //取消操作
    function cancel_onClick() {
        templateForm.attr("action", "<venus:base/>/EwpTemplateAction.do?cmd=queryAll&clearCondition=true");
        templateForm.submit();
    }

    //判断两个字符串是否相等
    function equals(first, second)/*first 第一个参数 second 第二个参数*/ {
        if (first == null && second == null) {
            return true;
        }
        if (first == null && second != null) {
            return false;
        }
        if (first != null && second == null) {
            return false;
        }
        if (first == second) {
            return true;
        }
    }

</script>
<script language="javascript">
    writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
</fmt:bundle>
</body>
</html>