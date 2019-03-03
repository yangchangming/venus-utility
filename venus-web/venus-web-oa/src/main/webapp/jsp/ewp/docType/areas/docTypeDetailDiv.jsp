<!--
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
-->
<%@ page contentType="text/html; charset=UTF-8" %>
<div id="docTypeDetailDiv" style="display:none">
    <!-- 属性修改区域 -->
    <table width="100%" height="100%" border="0">
        <tbody>
        <tr>
            <td colspan="2">
                <input name="button_save" type="button" class="button_ellipse"
                       value='<fmt:message key="modify"  bundle="${applicationResources}"/>'
                       onClick="javascript:showTreeNodeDetailforUpdate();">
                <input name="button_save" type="button" class="button_ellipse"
                       value='<fmt:message key="udp.ewp.doctype.add"  />' onClick="javascript:addDocType_onClick();">
                <input name="button_save" type="button" class="button_ellipse"
                       value='<fmt:message key="udp.ewp.delete" />' onClick="javascript:deleteDocType_onClick();">
                <input name="button_save" type="button" class="button_ellipse"
                       value='<fmt:message key="udp.ewp.locate" />' onClick="javascript:locateDocType_onClick();">
            </td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.doctypeid"/>&nbsp;&nbsp;</td>
            <td align="left"><span id="id" name="iid"></span></td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.parentID"/>&nbsp;&nbsp;</td>
            <td align="left"><span id="parentName" name="iparentName"></span></td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.sharedIds"/>&nbsp;&nbsp;</td>
            <td align="left"><span id="sharedNames" name="sharedNames"></span></td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.template_id"/>&nbsp;&nbsp;</td>
            <td align="left"><span id="template_name" name="template_name"></span></td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.document_template_id"/>&nbsp;&nbsp;</td>
            <td align="left"><span id="document_template_name" name="document_template_name"></span></td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.name"/>&nbsp;&nbsp;</td>
            <td align="left"><span id="name" name="name"></span></td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.docTypeCode"/>&nbsp;&nbsp;</td>
            <td align="left"><span id="docTypeCode" name="docTypeCode"></span></td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.keyword"/>&nbsp;&nbsp;</td>
            <td align="left"><span id="keywords" name="keywords"></span></td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.description"/>&nbsp;&nbsp;</td>
            <td align="left">
                <div id="description" name="description" style="overflow:auto;height:60px"></div>
            </td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.valid"/>&nbsp;&nbsp;</td>
            <td align="left"><span id="isValid" name="isValid"></span></td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.isnavigate"/>&nbsp;&nbsp;</td>
            <td align="left"><span id="navigateMenu" name="navigateMenu"></span></td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.sort"/>&nbsp;&nbsp;</td>
            <td align="left"><span id="sortNum" name="sortNum"></span></td>
        </tr>
        <tr>
            <td style="width: 100px;" align="right"><fmt:message key="udp.ewp.doctype.image"/></td>
            <td align="left">
                <div style="width=400px;height=170px;white-space:nowrap;overflow:auto;display:block;">
                    <div id="imageContainer" style="width:200px;height:150px"></div>
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