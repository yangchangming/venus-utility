<!--
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
-->
<%@ page contentType="text/html; charset=UTF-8" %>
<div id="docTypeUpdateDiv" style="display:none">
    <table width="100%" height="100%" border="0">
        <tbody>
        <tr>
            <td colspan="2">
                <input name="button_save" type="button" class="button_ellipse"
                       value='<fmt:message key="udp.ewp.doctype.update"/>'
                       onClick="javascript:updateDocType_onClick();">
                <input name="button_cancel" type="button" class="button_ellipse"
                       value='<fmt:message key="return" bundle="${applicationResources}"/>'
                       onClick="javascript:doBack();">
            </td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.doctypeid"/>&nbsp;&nbsp;</td>
            <td align="left"><input type="text" class="text_field" id="update_id" name="update_id" value="" readonly/>
            </td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.parentID"/>&nbsp;&nbsp;</td>
            <td align="left"><input name="update_parentID" id="update_parentID" type="hidden" value=""/> <input
                    name="update_parentName" id="update_parentName" validate="notNull" type="text"
                    class="text_field" hiddenInputId="parentID" readonly
                    inputName="<fmt:message key="udp.ewp.doctype.parentID" />"
                    /></td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.sharedIds"/>&nbsp;&nbsp;</td>
            <td align="left"><input name="update_sharedIds" id="update_sharedIds" type="hidden" value=""/> <input
                    name="update_sharedNames" id="update_sharedNames" validate="notNull" type="text"
                    class="text_field" hiddenInputId="sharedIds" readonly
                    inputName="<fmt:message key="udp.ewp.doctype.sharedIds" />"
                    /><img class="refButtonClass"
                           src="<%=request.getContextPath() %>/themes/<venus:theme/>/images/icon/reference.gif"
                           onClick="getShareParentDoctypeForModify();"/></td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.template_id"/>&nbsp;&nbsp;</td>
            <td align="left"><input name="update_template_id" id="update_template_id" type="hidden" value=""/> <input
                    name="update_template_name" id="update_template_name" validate="notNull" type="text"
                    class="text_field" hiddenInputId="template_id" readonly
                    inputName="<fmt:message key="udp.ewp.doctype.template_id" />"
                    /><img class="refButtonClass"
                           src="<%=request.getContextPath() %>/themes/<venus:theme/>/images/icon/reference.gif"
                           onClick="getTemplateReference(basedir,getSiteId(),templateDialog,'hang','update_template_id','update_template_name');"/>
            </td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.document_template_id"/>&nbsp;&nbsp;</td>
            <td align="left"><input name="update_document_template_id" id="update_document_template_id" type="hidden"
                                    value=""/> <input name="update_document_template_name"
                                                      id="update_document_template_name" validate="notNull" type="text"
                                                      class="text_field" hiddenInputId="document_template_id" readonly
                                                      inputName="<fmt:message key="udp.ewp.doctype.document_template_id" />"
                    /><img class="refButtonClass"
                           src="<%=request.getContextPath() %>/themes/<venus:theme/>/images/icon/reference.gif"
                           onClick="getTemplateReference(basedir,getSiteId(),templateDialog,'doc','update_document_template_id','update_document_template_name');"/>
            </td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.name"/>&nbsp;&nbsp;</td>
            <td align="left"><input type="hidden" id="update_docTypeName_backup"/><input type="text" class="text_field"
                                                                                         maxlength="25"
                                                                                         onBlur="javascript:nameValidateWhenUpdate()"
                                                                                         id="update_name"
                                                                                         name="update_name"
                                                                                         onBlur="javascript:nameValidateWhenUpdate()"
                                                                                         value=""/><span
                    id="name_update_message"> </span></td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.docTypeCode"/>&nbsp;&nbsp;</td>
            <td align="left"><input type="hidden" id="update_docTypeCode_backup"/><input type="text" maxlength="25"
                                                                                         class="text_field"
                                                                                         onBlur="javascript:codeValidateWhenUpdate()"
                                                                                         id="update_docTypeCode"
                                                                                         name="update_docTypeCode"
                                                                                         value=""
                    /><span id="update_message"> </span></td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.keyword"/>&nbsp;&nbsp;</td>
            <td align="left"><input type="text" class="text_field" maxlength="255" id="update_keywords"
                                    name="update_keywords" value=""/></td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.description"/>&nbsp;&nbsp;</td>
            <td align="left">
                <textarea rows="3" cols="33" maxlength="500" style="width:204px" name="update_description"
                          id="update_description" inputName='<fmt:message key="udp.ewp.doctype.description" />'
                          validate="notNull"></textarea>
            </td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.valid"/>&nbsp;&nbsp;</td>
            <td align="left"><select name="update_isValid" id="update_isValid">
                <logic:iterate id="valid" name="isValidMap">
                    <option value="<bean:write name="valid" property="key"/>"><bean:write name="valid"
                                                                                          property="value"/></option>
                </logic:iterate>
            </select></td>
        </tr>
        <tr>
            <td align="right"><fmt:message key="udp.ewp.doctype.isnavigate"/>&nbsp;&nbsp;</td>
            <td align="left"><select name="update_isNavigate" id="update_isNavigate">
                <logic:iterate id="navigate" name="navigateMap">
                    <option value="<bean:write name="navigate" property="key"/>"><bean:write name="navigate"
                                                                                             property="value"/></option>
                </logic:iterate>
            </select></td>
        </tr>

        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.sort"/>&nbsp;&nbsp;</td>
            <td align="left"><select name="update_sortNum" id="update_sortNum">
                <logic:iterate id="sort" name="sortMap">
                    <option value="<bean:write name="sort" property="key"/>"><bean:write name="sort"
                                                                                         property="value"/></option>
                </logic:iterate>
            </select></td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.image"/>
                <div id="updateImageValues" style="display:none"></div>
            </td>
            <td align="left">
                <div id="upateOuterContainer"
                     style="width=400px;height=170px;white-space:nowrap;overflow:auto;display:block;">
                    <div id="update_imageContainer" style="width:200px;height:150px"></div>
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