<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="venus.frames.i18n.util.LocaleHolder" %>
<%@ page import="udp.ewp.util.EnumTools" %>
<%
    request.setAttribute("template_typeMap", EnumTools.getSortedEnumMap(EnumTools.TEMPLATETYPE));
    request.setAttribute("booleanMap", EnumTools.getSortedEnumMap(EnumTools.LOGICBOOLEAN));
    Integer addCount = (Integer) request.getAttribute("addCount");
%>
<html>
<fmt:bundle basename="udp.ewp.ewp_resource">
<head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><fmt:message key="udp.ewp.template_manage"/></title>
    <script language="javascript">
        //获得选中的模板
        function findSelections() {
            var ids = null;
            jQuery("input[name='checkbox_template']:checkbox").each(function () {
                if (jQuery(this).is(":checked")) {
                    if (ids == null) {
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
            if (ids == null) {
                alert('<fmt:message key="udp.ewp.select_one_record"/>');
                return;
            }
            if (ids.length > 1) {
                alert('<fmt:message key="udp.ewp.only_can_a_record"/>');
                return;
            }
            form.action = "<%=request.getContextPath()%>/EwpTemplateAction.do?id=" + ids;
            form.cmd.value = "findForUpdateByID";
            form.submit();
        }

        //删除多个模板
        function deleteMulti_onClick() {
            var ids = findSelections();
            if (ids == null) {
                alert('<fmt:message key="udp.ewp.select_records"/>');
                return;
            }
            if (confirm('<fmt:message key="whether_to_delete_the_data_completely" bundle='${applicationResources}'/>')) {
                form.action = "<%=request.getContextPath()%>/EwpTemplateAction.do?ids=" + ids;
                form.cmd.value = "deleteMulti";
                form.submit();
            }
        }

        //查询
        function simpleQuery_onClick() {
            if (checkAllForms()) {
                var site_id = jQuery(parent.window.frames[0].document).find("#site_id").val();
                form.cmd.value = "simpleQuery";
                jQuery("form").attr("action", "<venus:base/>/EwpTemplateAction.do?site_id=" + site_id + "&backFlag=true");
                form.submit();
            }
        }

        //重置查询条件
        function resetForm() {
            jQuery("input[name='template_name']").val('');
            jQuery("input[name='template_content']").val('');
            jQuery("#siteSel").val('');
            jQuery("#template_type").val('');
        }

        //转入增加界面
        function toAdd_onClick() {
            form.action = "<%=request.getContextPath()%>/EwpTemplateAction.do";
            form.cmd.value = "findForInsert";
            form.submit();
        }

        //导入模板页面
        function toImport_onClick() {
            var site_id = jQuery(parent.window.frames[0].document).find("#site_id").val();
            form.cmd.value = "importTemplates";
            jQuery("form").attr("action", "<venus:base/>/EwpTemplateAction.do?site_id=" + site_id);
            form.submit();
        }

        //查看详细信息
        function detail_onClick() {
            var ids = findSelections();
            if (ids == null) {
                alert('<fmt:message key="udp.ewp.select_one_record"/>');
                return;
            }
            if (ids.length > 1) {
                alert('<fmt:message key="udp.ewp.only_can_a_record"/>');
                return;
            }
            form.action = "<%=request.getContextPath()%>/EwpTemplateAction.do?id=" + ids;
            form.cmd.value = "detail";
            form.submit();
        }

        function setDefault_onClick() {
            var ids = findSelections();
            if (ids == null) {
                alert('<fmt:message key="udp.ewp.select_one_record"/>');
                return;
            }
            if (ids.length > 1) {
                alert('<fmt:message key="udp.ewp.only_can_a_record"/>');
                return;
            }

            jQuery.ajax({url: "<venus:base/>/EwpTemplateAction.do?cmd=isDefaultTemplate&Action=get&id=" + ids, async: false, cache: false, dataType: "text", success: function (data, textStatus) {
                var jsonResult = eval('(' + data + ')');
                var isPass = jsonResult.isPass;
                if (isPass == "Y") {
                    alert('<fmt:message key="udp.ewp.Is_the_default_template"/>');
                } else if (isPass == "wrong") {
                    alert('<fmt:message key="udp.ewp.wrong_template_type"/>');
                } else {
                    if (confirm('<fmt:message key="udp.ewp.template.whether_to_set_default_template"/>')) {
                        jQuery("form").attr("action", "<venus:base/>/EwpTemplateAction.do?id=" + ids);
                        jQuery("input[name='cmd']").val("updateDefaultTemplate");
                        jQuery("input[name='backFlag']").val("true");
                        jQuery("form").submit();
                    }
                }
            }});
        }

        $(document).ready(function () {
            <%if(addCount!=null){%>
            alert("新添加模板"+<%=addCount%>+
            "个。"
            )
            ;
            <%} %>
        })
    </script>
    <script language="javascript">
        jQuery(function () {
            jQuery(".listCss tr tr").bind("dblclick", function () {
                var id = jQuery(this).find("input[name='checkbox_template'][type='checkbox']").val();
                if (id != null && id != "undefined") {
                    form.action = "<%=request.getContextPath()%>/EwpTemplateAction.do?id=" + id;
                    form.cmd.value = "detail";
                    form.submit();
                }
            })
        });
    </script>
</head>
<body>
<div style="width:100%;">
    <script language="javascript">
        writeTableTop('<fmt:message key="udp.ewp.template_manage" />', '<venus:base/>/themes/<venus:theme/>/');
    </script>
    <form name="form" method="post"
          action="<%=request.getContextPath()%>/EwpTemplateAction.do?cmd=simpleQuery&backFlag=true"><input type="hidden"
                                                                                                           name="cmd"
                                                                                                           value="simpleQuery">

        <div id="ccParent0">
            <table class="table_div_control">
                <tr>
                    <td>
                        <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image"
                             onClick="javascript:hideshow('ccChild0',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;
                        <fmt:message key="query_with_condition" bundle="${applicationResources}"/></td>
                </tr>
            </table>
        </div>

        <div id="ccChild0">
            <table class="table_div_content" border="0">
                <tr>
                    <td>
                        <table class="table_noframe" width="100%">
                            <tr>
                                <td align="right" width="15%"><fmt:message key="udp.ewp.website.name"/></td>
                                <td align="left" width="35%">
                                    <select id="siteSel" name="website_id">
                                        <option value=""><fmt:message key="udp.ewp.select_one_website"/></option>
                                        <logic:iterate id="website" name="webSites">
                                            <c:if test="${ requestScope.website_id eq website.id }">
                                                <option value="<bean:write name="website" property="id"/>" selected><bean:write
                                                        name="website" property="websiteName"/></option>
                                            </c:if>
                                            <c:if test="${ requestScope.website_id ne website.id }">
                                                <option value="<bean:write name="website" property="id"/>"><bean:write
                                                        name="website" property="websiteName"/></option>
                                            </c:if>
                                        </logic:iterate>
                                    </select>
                                </td>
                                <td align="right" width="15%"><fmt:message key="udp.ewp.template_name"/></td>
                                <td align="left" width="35%">
                                    <input type="text" validate="isSearch" class="text_field" name="template_name"
                                           inputName="<fmt:message key="udp.ewp.template_name" />" maxLength="50"
                                           value="${requestScope.template_name}"/>
                                </td>
                            </tr>
                            <tr>
                                <td align="right" width="15%"><fmt:message key="udp.ewp.template_content"/></td>
                                <td align="left" width="35%">
                                    <input type="text" validate="isSearch" class="text_field" name="template_content"
                                           inputName="<fmt:message key="udp.ewp.template_content" />" maxLength="32767.5"
                                           value="${requestScope.template_content}"/>
                                </td>
                                <td align="right" width="15%"><fmt:message key="udp.ewp.template_type"/></td>
                                <td align="left" width="35%">
                                    <select name="template_type" id="template_type">
                                        <option value=""><fmt:message key="udp.ewp.select_template_type"/></option>
                                        <logic:iterate id="tempType" name="template_typeMap">
                                            <c:if test="${ requestScope.template_type eq tempType.key }">
                                                <option value="<bean:write name="tempType" property="key"/>" selected><bean:write
                                                        name="tempType" property="value"/></option>
                                            </c:if>
                                            <c:if test="${ requestScope.template_type ne tempType.key }">
                                                <option value="<bean:write name="tempType" property="key"/>"><bean:write
                                                        name="tempType" property="value"/></option>
                                            </c:if>
                                        </logic:iterate>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="3"></td>
                                <td align="left">
                                    <input name="button_ok" class="button_ellipse" type="button"
                                           value='<fmt:message key="query" bundle="${applicationResources}" />'
                                           onClick="javascript:simpleQuery_onClick()">
                                    <input name="button_reset" class="button_ellipse" type="button"
                                           value='<fmt:message key="clear" bundle="${applicationResources}" />'
                                           onClick="javascript:resetForm()">
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </div>

        <div id="ccParent1">
            <table class="table_div_control">
                <tr>
                    <td><img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image"
                             onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">&nbsp;<fmt:message
                            key="list_page" bundle="${applicationResources}"/></td>
                    <td>
                        <table align="right">
                            <tr>
                                <td><input name="button_default" class="button_ellipse" type="button"
                                           value='<fmt:message key="udp.ewp.template.set_default_template"/>'
                                           onClick="javascript:setDefault_onClick();"></td>
                                <td><input name="button_view" class="button_ellipse" type="button"
                                           value='<fmt:message key="view" bundle="${applicationResources}"/>'
                                           onClick="javascript:detail_onClick();"></td>
                                <td><input name="button_add" class="button_ellipse" type="button"
                                           value='<fmt:message key="add" bundle="${applicationResources}"/>'
                                           onClick="javascript:toAdd_onClick();"></td>
                                <td><input name="button_delete" class="button_ellipse" type="button"
                                           value='<fmt:message key="delete" bundle="${applicationResources}"/>'
                                           onClick="javascript:deleteMulti_onClick();"></td>
                                <td><input name="button_modify" class="button_ellipse" type="button"
                                           value='<fmt:message key="modify" bundle="${applicationResources}"/>'
                                           onClick="javascript:toMofidy_onClick();"></td>
                                <td><input name="button_import" class="button_ellipse" type="button"
                                           value='<fmt:message key="udp.ewp.template.import" />'
                                           onClick="javascript:toImport_onClick();"></td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </div>

        <div id="ccChild1">
            <table class="table_div_content2">
                <tr>
                    <td><layout:collection name="beans" id="template" styleClass="listCss" width="100%"
                                           indexId="orderNumber" align="center" sortAction="0">
                        <layout:collectionItem width="1%"
                                               title="<input type='checkbox' pdType='control' control='checkbox_template'/>"
                                               style="text-align:center;">
                            <input type="checkbox" name="checkbox_template" value="${pageScope.template.id}"/>
                        </layout:collectionItem>
                        <layout:collectionItem width="5%" title='<%=LocaleHolder.getMessage("sequence") %>'
                                               style="text-align:center;">
                            <venus:sequence/>
                        </layout:collectionItem>
                        <layout:collectionItem width="14%"
                                               title='<%=LocaleHolder.getMessage("udp.ewp.template.website") %>'
                                               property="webSite.websiteName" sortable="false"/>
                        <layout:collectionItem width="14%"
                                               title='<%=LocaleHolder.getMessage("udp.ewp.template.name") %>'
                                               property="template_name" sortable="false"/>
                        <layout:collectionItem width="14%"
                                               title='<%=LocaleHolder.getMessage("udp.ewp.template.viewname") %>'
                                               property="template_viewname" sortable="false"/>
                        <layout:collectionItem width="14%"
                                               title='<%=LocaleHolder.getMessage("udp.ewp.template.typename") %>'
                                               property="template_type" sortable="false"/>
                        <layout:collectionItem width="8%"
                                               title='<%=LocaleHolder.getMessage("udp.ewp.template.isdefault") %>'
                                               property="isDefault" sortable="false">
                            <bean:write name="booleanMap" property="${pageScope.template.isDefault}"/>
                        </layout:collectionItem>
                    </layout:collection>
                        <jsp:include page="/jsp/include/page.jsp"/>
                    </td>
                </tr>
            </table>
        </div>

    </form>
    <script language="javascript">
        writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
    </script>
</div>
</body>
</fmt:bundle>
</html>

<script language="javascript">
</script>
