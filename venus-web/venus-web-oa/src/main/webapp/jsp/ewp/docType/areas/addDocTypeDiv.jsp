<!--
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
-->
<%@ page contentType="text/html; charset=UTF-8" %>
<div id="addDocTypeDiv" style="height: 30px; width: 100%; display: none;">
    <table border="0" style="width: 100%;" height="100%">
        <tbody>
        <tr>
            <td colspan="2">
                <input name="button_save" type="button" class="button_ellipse"
                       value='<fmt:message key="save" bundle="${applicationResources}"/>'
                       onClick="javascript:addDocType();">
                <input name="button_cancel" type="button" class="button_ellipse"
                       value='<fmt:message key="return" bundle="${applicationResources}"/>'
                       onClick="javascript:doBack();">
            </td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.parentID"/></td>
            <td align="left"><input name="add_parentID" id="add_parentID" type="hidden" value=""/> <input
                    name="add_parentName" id="add_parentName" validate="notNull" type="text"
                    class="text_field" hiddenInputId="add_parentID" readonly
                    inputName="<fmt:message key="udp.ewp.doctype.parentID" />"
                    /></td>
        </tr>
        <tr>
            <td align="right"><fmt:message key="udp.ewp.doctype.sharedIds"/>&nbsp;&nbsp;</td>
            <td align="left">
                <input name="add_sharedIds" id="add_sharedIds" type="hidden" value=""/>
                <input name="add_sharedNames" id="add_sharedNames" validate="notNull" type="text" class="text_field"
                       hiddenInputId="add_sharedIds" readonly
                       inputName="<fmt:message key="udp.ewp.doctype.sharedIds" />"/> <img class="refButtonClass"
                                                                                          src="<%=request.getContextPath() %>/themes/<venus:theme/>/images/icon/reference.gif"
                                                                                          onClick="getShareParentDoctype();"/>
            </td>
        </tr>
        <tr>
            <td align="right">*<fmt:message key="udp.ewp.doctype.name"/>&nbsp;&nbsp;</td>
            <td align="left"><input name="add_name" id="add_name" type="text" class="text_field" maxlength="25"
                                    inputName='<fmt:message key="udp.ewp.doctype.name"/>'
                                    onBlur="javascript:nameValidateWhenAdd()" validate="notNull" value=""><span
                    id="nameErrorMsg"></span></td>
            </td>
        </tr>
        <tr>
            <td align="right">*<fmt:message key="udp.ewp.doctype.docTypeCode"/>&nbsp;&nbsp;</td>
            <td align="left"><input name="add_docTypeCode" id="add_docTypeCode"
                                    onBlur="javascript:codeValidateWhenAdd()" maxlength="50" type="text"
                                    class="text_field" inputName='<fmt:message key="udp.ewp.doctype.docTypeCode"/>'
                                    validate="notNull" value=""> <span id="errorMsg"> </span></td>
        </tr>
        <tr>
            <td align="right"><fmt:message key="udp.ewp.doctype.template_id"/>&nbsp;&nbsp;</td>
            <td align="left">
                <input name="add_template_id" id="add_template_id" type="hidden" value=""/>
                <input name="add_template_name" id="add_template_name" validate="notNull" type="text" class="text_field"
                       hiddenInputId="add_template_id" readonly
                       inputName='<fmt:message key="udp.ewp.doctype.template_id" />'/>
                <img class="refButtonClass"
                     src="<%=request.getContextPath() %>/themes/<venus:theme/>/images/icon/reference.gif"
                     onClick="getTemplateReference(basedir,getSiteId(),templateDialog,'hang','add_template_id','add_template_name');"/>
            </td>
        </tr>
        <tr>
            <td align="right"><fmt:message key="udp.ewp.doctype.document_template_id"/>&nbsp;&nbsp;</td>
            <td align="left">
                <input name="add_document_template_id" id="add_document_template_id" type="hidden" value=""/>
                <input name="add_document_template_name" id="add_document_template_name" validate="notNull" type="text"
                       class="text_field" hiddenInputId="add_document_template_id" readonly
                       inputName='<fmt:message key="udp.ewp.doctype.document_template_id" />'/>
                <img class="refButtonClass"
                     src="<%=request.getContextPath() %>/themes/<venus:theme/>/images/icon/reference.gif"
                     onClick="getTemplateReference(basedir,getSiteId(),templateDialog,'doc','add_document_template_id','add_document_template_name');"/>
            </td>
        </tr>
        <tr>
            <td align="right"><fmt:message key="udp.ewp.keyword"/>&nbsp;&nbsp;</td>
            <td align="left"><input name="add_keywords" id="add_keywords" maxlength="255" type="text" class="text_field"
                                    value=""></td>
        </tr>
        <tr>
            <td align="right"><fmt:message key="udp.ewp.doctype.description"/>&nbsp;&nbsp;</td>
            <td align="left">
                <textarea style="width:204px" rows="3" cols="33" maxlength="500" name="add_description"
                          id="add_description" inputName='<fmt:message key="udp.ewp.doctype.description" />'
                          validate="notNull"></textarea>
            </td>
        </tr>
        <tr>
            <td align="right"><fmt:message key="udp.ewp.doctype.valid"/>&nbsp;&nbsp;</td>
            <td align="left"><select name="add_isValid" id="add_isValid">
                <logic:iterate id="valid" name="isValidMap">
                    <option value="<bean:write name="valid" property="key"/>"><bean:write name="valid"
                                                                                          property="value"/></option>
                </logic:iterate>
            </select></td>
        </tr>
        <tr>
            <td align="right"><fmt:message key="udp.ewp.doctype.isnavigate"/>&nbsp;&nbsp;</td>
            <td align="left"><select name="add_isNavigate" id="add_isNavigate">
                <logic:iterate id="navigate" name="navigateMap">
                    <option value="<bean:write name="navigate" property="key"/>"><bean:write name="navigate"
                                                                                             property="value"/></option>
                </logic:iterate>
            </select></td>
        </tr>
        <tr>
            <td align="right"><fmt:message key="udp.ewp.doctype.sort"/>&nbsp;&nbsp;</td>
            <td align="left"><select name="add_sortNum" id="add_sortNum">
                <logic:iterate id="sort" name="sortMap">
                    <option value="<bean:write name="sort" property="key"/>"><bean:write name="sort"
                                                                                         property="value"/></option>
                </logic:iterate>
            </select></td>
        </tr>
        <tr>
            <td align="right">定位到父节点</td>
            <td align="left"><input name="whetherLocateToParentNode" id="whetherLocateToParentNode" type="checkbox"
                                    checked value="Y"></td>
        </tr>
        <tr>
            <td align="right"><fmt:message key="udp.ewp.doctype.click.upload"/>
                <div id="addImageValues" style="display:none">
                    <input type="checkbox" name="add_imagePath" id="add_imagePath1" checked/>
                </div>
            </td>
            <td align="left">
                <div id="addOuterContainer"
                     style="width=400px;height=180px;white-space:nowrap;overflow:auto;display:block;">
                    <div id="addImageContainer" style="width:200px;height=150px">
                        <img id="add_showLogo1" alt=""
                             src="<%=request.getContextPath()%>/servlet/OutPutImageServlet?filename=doctypelogo.jpg"
                             width="150px" height="150px">
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        <tr>
            <td></td>
            <td></td>
        </tr>
        </tbody>
    </table>
</div>