<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="venus.commons.xmlenum.EnumRepository" %>
<%@ page import="venus.commons.xmlenum.EnumValueMap"%>
<%@ page import="udp.ewp.util.EnumTools" %>
<%@ page import="udp.ewp.website.model.Website" %>
<%@ page import="udp.ewp.website.util.IWebsiteConstants" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.TreeMap" %>
<%@ include file="/jsp/include/global.jsp" %>
<%
    Website resultVo = null;
    boolean isModify = false;
    if (request.getParameter("isModify") != null) {
        isModify = true;
        if (request.getAttribute(IWebsiteConstants.REQUEST_BEAN) != null) {
            resultVo = (Website) request.getAttribute(IWebsiteConstants.REQUEST_BEAN);
            request.setAttribute("language", resultVo.getLanguage());
            request.setAttribute("default", resultVo.getIsDefault() == null ? "N" : resultVo.getIsDefault());
            request.setAttribute("nameIsUnique", resultVo.getNameIsUnique() == null ? "N" : resultVo.getNameIsUnique());
            request.setAttribute("linkTarget", resultVo.getLinkTarget());
            request.setAttribute("hotWordsSwitcher", resultVo.getHotWordsSwitcher());
        }
    }
    EnumRepository er = EnumRepository.getInstance();
    er.loadFromDir();

    TreeMap<String, String> linkTargetMap = EnumTools.getSortedEnumMap(EnumTools.LINKTARGET);
    request.setAttribute("linkTargetMap", linkTargetMap);

    TreeMap<String, String> logicTF = EnumTools.getSortedEnumMap(EnumTools.LOGICTRUEFALSE);
    request.setAttribute("logicTF", logicTF);

    EnumValueMap languageMap = er.getEnumValueMap("Language");
    List languageList = languageMap.getEnumList();
    TreeMap langMap = new TreeMap(new Comparator() {
        public int compare(Object languageOne, Object languageTwo) {
            return EnumTools.getOrderNum((String) languageOne) - EnumTools.getOrderNum((String) languageTwo);
        }
    });
    for (int i = 0; i < languageList.size(); i++) {
        langMap.put(languageMap.getValue(languageList.get(i).toString()), languageList.get(i));
    }
    request.setAttribute("langMap", langMap);

    EnumValueMap logicBooleanMap = er.getEnumValueMap("LogicBoolean");
    List logicBooleanList = logicBooleanMap.getEnumList();
    HashMap booleanMap = new HashMap();
    for (int i = 0; i < logicBooleanList.size(); i++) {
        booleanMap.put(logicBooleanMap.getValue(logicBooleanList.get(i).toString()), logicBooleanList.get(i));
    }
    request.setAttribute("booleanMap", booleanMap);
%>
<html>
<fmt:bundle basename="udp.ewp.ewp_resource">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<%=request.getContextPath()%>/js/gbox/ymprompt/ymPrompt.css" type="text/css" rel="stylesheet">
    <script language="javascript" src="<%=request.getContextPath()%>/js/gbox/ymprompt/ymPrompt.js"></script>
    <script language="javascript" src="<%=request.getContextPath()%>/js/ewp/templateReference.js"></script>
    <title>
        <script language="javascript">
            if (<%=isModify%>)
                out.print('<fmt:message key="udp.ewp.website_modify" />');
            else
                out.print('<fmt:message key="udp.ewp.website_add"/>');
        </script>
    </title>
    <script language="javascript">
        var templateDialog = null;

        $(function () {
            templateDialog = jQuery("#referenceTemplate").dialog({ modal: true, height: 490, autoOpen: false, resizable: false, width: 450, overlay: { opacity: 0.3, background: "black" }});
        });

        function checkOnlyDefaultWebsite() {
            var isDefault = jQuery("#isDefault").val();
            var id = jQuery("#id").val();
            if (isDefault != "Y") {
                jQuery("#errorMsg_isdefault").html('');
                jQuery("#isPass_isdefault").val('Y');
                return true;
            }
            jQuery.ajax({url: "<venus:base/>/WebsiteAction.do?cmd=checkOnlyDefaultWebsite&Action=get&id=" + id, async: false, cache: false, dataType: "text", success: function (data, textStatus) {
                var jsonResult = eval('(' + data + ')');
                var isPass = jsonResult.isPass;
                var returnMessage = jsonResult.returnMessage;
                jQuery("#isPass_isdefault").val(isPass);
                jQuery("#errorMsg_isdefault").html(returnMessage);
            }});
            return true;
        }

        function checkDocTypeNameIsUnique() {
            var nameIsUnique = jQuery("#nameIsUnique").val();
            var id = jQuery("#id").val();
            if (nameIsUnique != "Y") {
                jQuery("#errorMsg_nameisunique").html('');
                jQuery("#isPass_nameisunique").val('Y');
                return true;
            }
            jQuery.ajax({url: "<venus:base/>/WebsiteAction.do?cmd=checkDocTypeNameIsUniqueByCurrentWebSite&website_id=<%=resultVo==null?"":resultVo.getId()%>", async: false, cache: false, dataType: "text", success: function (data, textStatus) {
                var jsonResult = eval('(' + data + ')');
                var isPass = jsonResult.isPass;
                var returnMessage = jsonResult.returnMessage;
                jQuery("#isPass_nameisunique").val(isPass);
                jQuery("#errorMsg_nameisunique").html(returnMessage);
            }});
            return true;
        }

        // 新增保存时较验网站编码是否唯一
        function codeValidateWhenAdd() {
            var waitingForCheckCode = jQuery("#input_websiteCode").val();
            if (waitingForCheckCode != null && waitingForCheckCode != '') {
                var regu = /^[a-z,A-Z,0-9,_]+$/;
                if (!regu.exec(waitingForCheckCode)) {
                    jQuery("#isPass_codeisunique").val('N');
                    jQuery("#errorMsg").html('<fmt:message key="udp.ewp.template_viewcode_only_char_or_number_or_underline" />');
                    return true;
                }

                <%if(isModify) {%>
                var update_websiteCode_backup = <%=resultVo.getWebsiteCode()%>;
                if (waitingForCheckCode == update_websiteCode_backup) {
                    jQuery("#isPass_codeisunique").val('Y');
                    jQuery("#errorMsg").html(<fmt:message key="udp.ewp.website.code.unique" />);
                    return true;
                }
                <%}%>

                jQuery.ajax({url: "<venus:base/>/WebsiteAction.do?cmd=checkWebsiteCodeIsUnique&Action=get&waitingForCheckCode=" + waitingForCheckCode, async: false, cache: false, dataType: "text", success: function (data, textStatus) {
                    var checkResult = eval('(' + data + ')');
                    var isPass = checkResult.isPass;
                    var returnMessage = checkResult.returnMessage;
                    jQuery("#isPass_codeisunique").val(isPass);
                    jQuery("#errorMsg").html(returnMessage);
                    return true;
                }});
            } else {
                jQuery("#isPass_codeisunique").val('Y');
                jQuery("#errorMsg").html('<fmt:message key="udp.ewp.website.code.unique" />');
                return true;
            }
            return true;
        }


        function insert_onClick() {
            var isPass_isdefault = jQuery("#isPass_isdefault").val();
            if (isPass_isdefault == "N") {
                alert('<fmt:message key="udp.ewp.website.onlyonedefault"/>');
                return;
            }
            var websiteName = jQuery("input[name='websiteName']").val();
            if (websiteName == null || websiteName == "") {
                alert('<fmt:message key="udp.ewp.website.namecanotbenull"/>');
                return;
            }
            form.action = "<%=request.getContextPath()%>/WebsiteAction.do?cmd=insert";
            form.submit();
        }

        function update_onClick(id) {
            var isPass_isdefault = jQuery("#isPass_isdefault").val();
            if (isPass_isdefault == "N") {
                alert("<fmt:message key="udp.ewp.website.onlyonedefault"/>");
                return;
            }
            var isPass_nameisunique = jQuery("#isPass_nameisunique").val();
            if (isPass_nameisunique == "N") {
                alert("<fmt:message key="udp.ewp.website.nameisunique_no"/>");
                return;
            }
            var websiteName = jQuery("input[name='websiteName']").val();
            if (websiteName == null || websiteName == "") {
                alert('<fmt:message key="udp.ewp.website.namecanotbenull"/>');
                return;
            }
            ymPrompt.confirmInfo({title: '<fmt:message key="udp.ewp.website.question"/>', message: '<fmt:message key="udp.ewp.website.suretosave"/>', handler: updateCallBackFunc, btn: [
                ['<fmt:message key="udp.ewp.website.yes" />', 'yes'],
                ['<fmt:message key="udp.ewp.website.no" />', 'no']
            ]});
        }

        function updateCallBackFunc(value) {
            if (value == 'yes') {
                var isModifyCode = "0"
                if ($("#older_code").val() != $("#input_websiteCode").val()) {
                    isModifyCode = "<%=IWebsiteConstants.MODIFY_CODE%>";
                }
                jQuery.post("<%=request.getContextPath()%>/WebsiteAction.do?cmd=update&isModifyCode=" + isModifyCode, jQuery("#myform").serialize(), function () {
                    window.location.href = "<%=request.getContextPath()%>/WebsiteAction.do?cmd=queryAll";
                });
            }
        }


        function cancel_onClick() {  //取消后返回列表页面
            form.action = "<%=request.getContextPath()%>/WebsiteAction.do?cmd=queryAll&clearCondition=true";
            form.submit();
        }
    </script>
</head>
<body>
<script language="javascript">
    if (<%=isModify%>)
        writeTableTop('<fmt:message key="udp.ewp.website_modify" />', '<venus:base/>/themes/<venus:theme/>/');
    else
        writeTableTop('<fmt:message key="udp.ewp.website_add" />', '<venus:base/>/themes/<venus:theme/>/');
</script>
<form id="myform" name="form" method="post">

    <div id="ccParent1">
        <table class="table_div_control">
            <tr>
                <td>
                    <img src="<venus:base/>/themes/<venus:theme/>/images/icon/07-0.gif" class="div_control_image"
                         onClick="javascript:hideshow('ccChild1',this,'<venus:base/>/themes/<venus:theme/>/')">
                    &nbsp;&nbsp;<input name="button_save" class="button_ellipse" type="button"
                                       value='<fmt:message key="save" bundle="${applicationResources}"/>'
                                       onClick="javascript:<%=isModify?"update_onClick()":"insert_onClick()"%>">
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
                            <td align="right"><span class="style_required_red">* </span><fmt:message
                                    key="udp.ewp.website.name"/></td>
                            <td align="left">
                                <input type="text" class="text_field" name="websiteName"
                                       inputName="<fmt:message key="udp.ewp.website.name"/>" validate="notNull"
                                       value="<%=(resultVo==null?"":resultVo.getWebsiteName())%>" maxLength="50"/>
                            </td>
                            <td align="right"></td>
                            <td align="left"></td>
                        </tr>
                        <tr>
                            <td align="right"><span class="style_required_red">* </span><fmt:message
                                    key="udp.ewp.website.code"/></td>
                            <td align="left">
                                <input type="text" class="text_field" name="websiteCode" id="input_websiteCode"
                                       inputName="<fmt:message key="udp.ewp.website.code"/>" validate="notNull"
                                       value="<%=(resultVo==null?"":resultVo.getWebsiteCode())%>" maxLength="112"
                                       onBlur="javascript:codeValidateWhenAdd()"/>
                                <span id="errorMsg"></span>
                            </td>
                            <td align="right"></td>
                            <td align="left"></td>
                        </tr>
                        <tr>
                            <td align="right"><fmt:message key="udp.ewp.keyword"/></td>
                            <td align="left">
                                <input type="text" class="text_field" name="keywords" id="input_keywords"
                                       value="<%=(resultVo==null?"":resultVo.getKeywords())%>" maxLength="127"/>
                            </td>
                            <td align="right"></td>
                            <td align="left"></td>
                        </tr>
                        <tr>
                            <td align="right"><fmt:message key="udp.ewp.website.description"/></td>
                            <td align="left">
                                <input type="text" class="text_field" name="description"
                                       inputName="<fmt:message key="udp.ewp.website.description"/>" validate="notNull"
                                       value="<%=(resultVo==null?"":resultVo.getDescription()) %>" maxLength="50"/>
                            </td>
                            <td align="right"></td>
                            <td align="left"></td>
                        </tr>
                        <tr>
                            <td align="right"><span class="style_required_red">* </span><fmt:message
                                    key="udp.ewp.website.language"/></td>
                            <td align="left">
                                <select name="language" id="language">
                                    <logic:iterate id="lang" name="langMap">
                                        <c:if test="${lang.key eq requestScope.language}">
                                            <option value="<bean:write name="lang" property="key"/>" selected>
                                                <bean:write name="lang" property="value"/></option>
                                        </c:if>
                                        <c:if test="${lang.key ne language}">
                                            <option value="<bean:write name="lang" property="key"/>"><bean:write
                                                    name="lang" property="value"/></option>
                                        </c:if>
                                    </logic:iterate>
                                </select>
                            </td>
                            <td align="right"></td>
                            <td align="left"></td>
                        </tr>
                        <tr>
                            <td align="right"><span class="style_required_red">* </span><fmt:message
                                    key='udp.ewp.website.isdefault'/></td>
                            <td align="left">
                                <select name="isDefault" id="isDefault" onchange="javascript:checkOnlyDefaultWebsite()">
                                    <c:if test='${"N" eq requestScope["default"]}'>
                                        <option value="N" selected><fmt:message key='udp.ewp.website.no'/></option>
                                    </c:if>
                                    <c:if test='${"N" ne requestScope["default"]}'>
                                        <option value="N"><fmt:message key='udp.ewp.website.no'/></option>
                                    </c:if>
                                    <c:if test='${"Y" eq requestScope["default"]}'>
                                        <option value="Y" selected><fmt:message key='udp.ewp.website.yes'/></option>
                                    </c:if>
                                    <c:if test='${"Y" ne requestScope["default"]}'>
                                        <option value="Y"><fmt:message key='udp.ewp.website.yes'/></option>
                                    </c:if>
                                </select>
                                <span id="errorMsg_isdefault"> </span>
                            </td>
                            <td align="right"></td>
                            <td align="left"></td>
                        </tr>
                        <tr>
                            <td align="right"><span class="style_required_red">* </span><fmt:message
                                    key='udp.ewp.website.nameisunique'/></td>
                            <td align="left">
                                <select name="nameIsUnique" id="nameIsUnique"
                                        <%if (isModify){%>onchange="javascript:checkDocTypeNameIsUnique()"<%}%>>
                                    <c:if test='${"N" eq requestScope.nameIsUnique}'>
                                        <option value="N" selected><fmt:message key='udp.ewp.website.no'/></option>
                                    </c:if>
                                    <c:if test='${"N" ne requestScope.nameIsUnique}'>
                                        <option value="N"><fmt:message key='udp.ewp.website.no'/></option>
                                    </c:if>

                                    <c:if test='${"Y" eq requestScope.nameIsUnique}'>
                                        <option value="Y" selected><fmt:message key='udp.ewp.website.yes'/></option>
                                    </c:if>
                                    <c:if test='${"Y" ne requestScope.nameIsUnique}'>
                                        <option value="Y"><fmt:message key='udp.ewp.website.yes'/></option>
                                    </c:if>
                                </select>
                                <span id="errorMsg_nameisunique" />
                            </td>
                            <td align="right"></td>
                            <td align="left"></td>
                        </tr>
                        <tr>
                            <td align="right"><fmt:message key='udp.ewp.hotwords.switcher'/>
                            </td>
                            <td align="left">
                                <select name="hotWordsSwitcher" id="hotWordsSwitcher">
                                    <logic:iterate id="lgcTF" name="logicTF">
                                        <c:if test="${lgcTF.key eq requestScope.hotWordsSwitcher}">
                                            <option value="<bean:write name='lgcTF' property='key'/>" selected>
                                                <bean:write name="lgcTF" property="value"/></option>
                                        </c:if>
                                        <c:if test="${lgcTF.key ne requestScope.hotWordsSwitcher}">
                                            <option value="<bean:write name='lgcTF' property='key'/>">
                                                <bean:write name="lgcTF" property="value"/></option>
                                        </c:if>
                                    </logic:iterate>
                                </select>
                            </td>
                            <td align="left"></td>
                            <td align="left"></td>
                        </tr>
                        <tr>
                            <td align="right"><fmt:message key='udp.ewp.hotwords.linkTarget'/>
                            </td>
                            <td align="left">
                                <select name="linkTarget">
                                    <logic:iterate id="linkTgt" name="linkTargetMap">
                                        <c:if test="${linkTgt.key eq requestScope.linkTarget}">
                                            <option value="<bean:write name="linkTgt" property="key"/>"
                                                    selected><bean:write name="linkTgt"
                                                                         property="value"/></option>
                                        </c:if>
                                        <c:if test="${linkTgt.key ne requestScope.linkTarget}">
                                            <option value="<bean:write name="linkTgt" property="key"/>">
                                                <bean:write name="linkTgt" property="value"/></option>
                                        </c:if>
                                    </logic:iterate>
                                </select>
                            </td>
                            <td align="left"></td>
                            <td align="left"></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>

    <input type="hidden" id="id" name="id" value="<%=(resultVo==null?"":resultVo.getId()) %>">
    <input type="hidden" name="create_date"/>
    <input type="hidden" id="older_code" name="older_code" value="<%=(resultVo==null?"":resultVo.getWebsiteCode())%>"/>
    <input id="isPass_isdefault" type="hidden" value="Y"/>
    <input id="isPass_nameisunique" type="hidden" value="Y"/>
    <input id="isPass_codeisunique" type="hidden" value="Y"/>
</form>
<script language="javascript">
    writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚
</script>
<div id="referenceTemplate" title="请选择模板" style="background-color: #c8cdd3; cell-padding: 5px;"></div>
</fmt:bundle>
</body>
</html>
