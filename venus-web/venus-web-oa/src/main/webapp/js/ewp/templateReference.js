/**
 * 获取模板
 *
 * @param basePath 站点的根路径
 * @param siteId 模板所属站点ID
 * @param templateDialog 显示模板用的dialog
 * @param templateType 模板类型
 * @param inputId <input>元素的ID，用于存放所选模板的id
 * @param inputName <input>元素的ID，用于存放所选模板的name
 */
function getTemplateReference(basePath, siteId, templateDialog, templateType, inputId, inputName) {
    var refPath = basePath + '/jsp/ewp/template/templateframe.jsp?siteId=' + siteId + '&templateType=' + templateType + '&inputId=' + inputId + '&inputName=' + inputName;
    jQuery.get(refPath, {Action: "get", async: false}, function (data) {
        jQuery("#referenceTemplate").html(data);
        jQuery('.ui-dialog-titlebar-close').hide();
        templateDialog.dialog("open");
    });
}