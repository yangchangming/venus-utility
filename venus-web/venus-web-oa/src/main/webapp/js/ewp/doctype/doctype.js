function isEmpty(data) {
    return ((data == null) || (data.length() == 0));
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

function hiddenAllDivs() {
    jQuery("#docTypeDetailDiv").hide();
    jQuery("#docTypeUpdateDiv").hide();
    jQuery("#addDocTypeDiv").hide();
    jQuery("#defalutPageDiv").hide();
}

var $oldFocusObjs = null;
//保存后设置选中
function treeNodeOnfocusOfJquery(id) {
    if ($oldFocusObjs != null) {
        $oldFocusObjs.each(function () {
            jQuery(this).removeClass('tree_node_onfocus');
        });
    }
    $oldFocusObjs = jQuery("a[id='" + id + "']");
    $oldFocusObjs.each(function () {
        jQuery(this).addClass('tree_node_onfocus');
    });
}

//返回
function doBack() {
    hiddenAllDivs();
    jQuery("#docTypeDetailDiv").show();
}

//树栏目单击事件
function doClick(obj) {
    //取消树栏目的默认链接值
    obj.href = "#";
    //高亮显示所选栏目
    var id = obj.id;
    treeNodeOnfocusOfJquery(id);
    hiddenAllDivs();
    jQuery("#docTypeDetailDiv").show();
    showTreeNodeDetail(obj.id);
}

function getSiteId() {
    var ztree = jQuery.fn.zTree.getZTreeObj("tree");
    var selNode = ztree.getSelectedNodes();
    return selNode[0].siteId;
}

//显示树栏目对象详细信息
function showTreeNodeDetail(docTypeID) {
    DWREngine.setErrorHandler(eh);
    DWREngine.setAsync(false);
    EwpTreeControl.showTreeNodeDetail(docTypeID, beanId, function (data) {
        var jsonDoctype = eval('(' + data + ')');
        fillNodeDetail(jsonDoctype);
        DWREngine.setAsync(true);
    });
}

//点击更新时在更新界面显示详细信息
function showTreeNodeDetailforUpdate() {
    var docTypeID = jQuery("#currentDocTypeId").val();
    var currentDocTypeCode = jQuery("#currentDocTypeCode").val();

    if(currentDocTypeCode == 'root') {
        alert(root_not_allowedit);
        return;
    }
    hiddenAllDivs();
    jQuery("#docTypeUpdateDiv").show();
    DWREngine.setErrorHandler(eh);
    DWREngine.setAsync(false);
    EwpTreeControl.showTreeNodeDetail(docTypeID, beanId, function (data) {
        var jsonDoctype = eval('(' + data + ')');
        fillUpdateNodeDetail(jsonDoctype);
        if (currentDocTypeCode != null && currentDocTypeCode == "root") {
            jQuery("#update_docTypeCode").attr("readOnly", true);
        } else {
            jQuery("#update_docTypeCode").attr("readOnly", false);
        }
        DWREngine.setAsync(true);
    });
}

function onClickTreeNode(event, treeId, treeNode, clickFlag) {
    showTreeNodeDetail(treeNode.id);
    resetDivs();
}

function beforeDragTreeNode(treeId, treeNodes) {
    if (treeNodes[0].drag == "false") {
        return false;
    }
    return true;
}

function beforeDropTreeNode(treeId, treeNodes, targetNode, moveType) {
    if(treeNodes[0].siteId != targetNode.siteId) {
        alert("错误的操作，请勿跨站点移动");
        return false;
    }
    return confirm(confirm_move_this_doctype);
}

function onDropTreeNode(event, treeId, treeNodes, targetNode, moveType) {
    var move_parentId = "";
    if (moveType == "inner") {
        move_parentId = targetNode.id;
    } else if (moveType == "prev" || moveType == "next") {
        move_parentId = targetNode.getParentNode().id;
    } else if (moveType == null) {
        return;
    }
    var movedDoctType = '{id:"' + treeNodes[0].id + '",move_parentID:"' + move_parentId + '"}';

    DWREngine.setErrorHandler(eh);
    DWREngine.setAsync(false);
    EwpTreeControl.moveTreeNode(movedDoctType, beanId, function (data) {
        updateParentNode();
        showTreeNodeDetail(treeNodes[0].id);
        resetDivs();
    });
    DWREngine.setAsync(true);
}

function updateParentNode() {
    var ztree = jQuery.fn.zTree.getZTreeObj("tree");
    var selNodes = ztree.getSelectedNodes();
    var parentNode = selNodes[0].getParentNode();
    ztree.reAsyncChildNodes(parentNode, "refresh");
}

//开始新增栏目
function addDocType_onClick() {
    hiddenAllDivs();
    jQuery("#addDocTypeDiv").show();
    var parentId = jQuery("#currentDocTypeId").val();
    var parentName = jQuery("#currentDocTypeName").val();
    jQuery("#add_parentID").val(parentId);
    jQuery("#add_parentName").val(parentName);
    jQuery("#add_showLogo").attr("src", basedir + "/servlet/OutPutImageServlet?ts=" + new Date());
    //清理input框
    resetDoctype();
}

//新增栏目
function addDocType() {
    var name = jQuery("#add_name").val();
    if (name == null || name.length == 0) {
        alert(pleaseinputdoctypename);
        return;
    }
    var nameisunique = jQuery("#nameisunique").val();
    if (nameisunique == "Y") {
        var uniqueNamePass = jQuery("#uniqueNamePass").val();
        if (uniqueNamePass == "N") {
            alert(name_has_exist_or_notvalid);
            return;
        }
    }
    var uniqueCodePass = jQuery("#uniqueCodePass").val();
    if (uniqueCodePass == "N") {
        alert(viewcode_has_exist_or_notvalid);
        return;
    }
    var newDoctType = getNewDoctypeValue();
    var isLocToParent = jQuery("#whetherLocateToParentNode").is(":checked");

    if (confirm(confirm_save_doctype)) {
        DWREngine.setErrorHandler(eh);
        DWREngine.setAsync(false);
        EwpTreeControl.addTreeNode(newDoctType, beanId, function (call_docTypeID) {
            if (call_docTypeID != "") {
                var jsonDoctype = eval('(' + call_docTypeID + ')');
                addTreeNode(jsonDoctype, isLocToParent);
                if (isLocToParent) {
                    showTreeNodeDetail(jsonDoctype.pId);
                } else {
                    showTreeNodeDetail(jsonDoctype.id);
                }
                resetDivs();
            } else {
                alert(save_doctype_error);
            }
        });
        DWREngine.setAsync(true);
    }
}

function addTreeNode(jsonParam, isLocToParent) {
    var ztree = jQuery.fn.zTree.getZTreeObj("tree");
    var selNode = ztree.getSelectedNodes()[0];
    ztree.reAsyncChildNodes(selNode, 'refresh');

    needFocusNodeId = jsonParam.id;
}

function onAsyncTreeNode(event, treeId, treeNode, msg) {
    var ztree = jQuery.fn.zTree.getZTreeObj(treeId);
    if(needFocusNodeId != null) {
        var newNode = ztree.getNodesByParam('id', needFocusNodeId, treeNode);
        var isLocToParent = jQuery("#whetherLocateToParentNode").is(":checked");

        if (isLocToParent) {
            ztree.selectNode(treeNode, false);
        } else {
            ztree.selectNode(newNode[0], false);
        }
    }
    needFocusNodeId = null;
}

//更新栏目
function updateDocType_onClick() {
    var name = jQuery("#update_name").val();
    if (name == null || name.length == 0) {
        alert(pleaseinputdoctypename);
        return;
    }
    var uniqueCodePass = jQuery("#uniqueCodePass").val();
    if (uniqueCodePass == "N") {
        alert(viewcode_has_exist_or_notvalid);
        return;
    }
    var nameisunique = jQuery("#nameisunique").val();
    if (nameisunique == "Y") {
        var uniqueNamePass = jQuery("#uniqueNamePass").val();
        if (uniqueNamePass == "N") {
            alert(name_has_exist_or_notvalid);
            return;
        }
    }
    var upatedDoctType = getUpdateDoctypeValue();
    if (confirm(confirm_update_doctype)) {
        DWREngine.setErrorHandler(eh);
        DWREngine.setAsync(false);
        EwpTreeControl.updateTreeNode(upatedDoctType, beanId, function (data) {
            var jsonResult = eval('(' + data + ')');
            updateTreeNode();
            showTreeNodeDetail(jsonResult.id);
            resetDivs();
        });
        DWREngine.setAsync(true);
    }
}

function updateTreeNode() {
    var ztree = jQuery.fn.zTree.getZTreeObj("tree");
    var selNodes = ztree.getSelectedNodes();
    var node = ztree.getNodeByParam("rootId", selNodes[0].siteId, null);
    if (node != null) {
        ztree.reAsyncChildNodes(node, "refresh");
    }
}

//删除栏目
function deleteDocType_onClick() {
    var currentDocTypeCode = jQuery("#currentDocTypeCode").val();
    if (currentDocTypeCode != null && currentDocTypeCode == "root") {
        alert(root_not_allowdelete);
        return;
    }
    var id = jQuery("#currentDocTypeId").val();

    if (confirm(confirm_delete_this_doctype)) {
        DWREngine.setErrorHandler(eh);
        DWREngine.setAsync(false);
        EwpTreeControl.deleteTreeNode(id, beanId, function (callBackValue) {
            var jsonResult = eval('(' + callBackValue + ')');
            if (jsonResult.flag == false) {
                alert(jsonResult.message);
                return false;
            }
            deleteTreeNode();
            selectParentNode(jsonResult.id);
            showTreeNodeDetail(jsonResult.id);
        });
        DWREngine.setAsync(true);
    }
}

function selectParentNode(pid) {
    var ztree = jQuery.fn.zTree.getZTreeObj("tree");
    var parentNode = ztree.getNodeByParam('id', pid, null);
    ztree.selectNode(parentNode);
}

function deleteTreeNode() {
    var ztree = jQuery.fn.zTree.getZTreeObj("tree");
    var selNodes = ztree.getSelectedNodes();
    ztree.removeNode(selNodes[0]);
}

function resetDivs() {
    hiddenAllDivs();
    jQuery("#docTypeDetailDiv").show();
}

function locateDocType_onClick() {
    var ztree = jQuery.fn.zTree.getZTreeObj("tree");
    var selNodes = ztree.getSelectedNodes();
    ztree.selectNode(selNodes[0]);
}

//移动时取得上级栏目
function getMoveParentDoctype() {
    var site_id = jQuery(parent.document.getElementById("site_id")).val();
    if (site_id == null || site_id == "undefined") {
        site_id = "";
    }
    var refPath = basedir + '/jsp/ewp/docType/referenceDocType.jsp?site_id=' + site_id + '&tree_href=none&method=move_save_onClick&checktype=RADIO&selectedValues=' + jQuery("#move_parentID").val() + '&notIncludeValues=' + jQuery("#currentDocTypeId").val();
    jQuery.get(refPath, {Action: "get", async: false}, function (data, textStatus) {
        jQuery("#referenceHangDoctype").html(data);
        jQuery('.ui-dialog-titlebar-close').hide();
        hangDoctypeDialog.dialog("open");
    });

}

//添加时取得挂接的栏目
function getShareParentDoctype() {
    var site_id = getSiteId();
    if (site_id == null || site_id == "undefined") {
        site_id = "";
    }
    var refPath = basedir + '/jsp/ewp/docType/referenceDocType.jsp?site_id=' + site_id + '&tree_href=none&method=add_save_onClick&checktype=CHECK&selectedValues=' + jQuery("#add_sharedIds").val() + '&notIncludeValues=' + jQuery("#add_parentID").val();
    jQuery.get(refPath, {Action: "get", async: false}, function (data, textStatus) {
        jQuery("#referenceHangDoctype").html(data);
        jQuery('.ui-dialog-titlebar-close').hide();
        hangDoctypeDialog.dialog("open");
    });
}

//修改时取得挂接的栏目
function getShareParentDoctypeForModify() {
    var currentDocTypeCode = jQuery("#currentDocTypeCode").val();
    if (currentDocTypeCode != null && currentDocTypeCode == "root") {
        alert(root_not_allowhang);
        return;
    }
    var site_id = getSiteId();
    if (site_id == null || site_id == "undefined") {
        site_id = "";
    }
    var refPath = basedir + '/jsp/ewp/docType/referenceDocType.jsp?site_id=' + site_id + '&tree_href=none&method=update_save_onClick&checktype=CHECK&selectedValues=' + jQuery("#update_sharedIds").val() + '&notIncludeValues=' + jQuery("#update_parentID").val() + '&currentid=' + jQuery("#currentDocTypeId").val();
    jQuery.get(refPath, {Action: "get", async: false}, function (data, textStatus) {
        jQuery("#referenceHangDoctype").html(data);
        jQuery('.ui-dialog-titlebar-close').hide();
        hangDoctypeDialog.dialog("open");
    });
}

//用栏目VO填充详细列表值
function fillNodeDetail(jsonDoctype) {
    jQuery("#id").text(jsonDoctype.id);
    jQuery("#name").text(jsonDoctype.name);
    jQuery("#currentDocTypeId").val(jsonDoctype.id);
    jQuery("#currentDocTypeName").val(jsonDoctype.name);
    jQuery("#parentID").text(jsonDoctype.parentID);
    jQuery("#parentName").text(jsonDoctype.parentName);
    jQuery("#currentParentDocTypeId").val(jsonDoctype.parentID);
    jQuery("#currentParentDocTypeName").val(jsonDoctype.parentName);
    jQuery("#sharedIds").text(jsonDoctype.sharedIds);
    jQuery("#sharedNames").text(jsonDoctype.sharedNames);
    jQuery("#description").html(jsonDoctype.description);
    jQuery("#template_id").text(jsonDoctype.template_id);
    jQuery("#template_name").text(jsonDoctype.template_name);
    jQuery("#document_template_name").text(jsonDoctype.document_template_name);
    jQuery("#docTypeCode").text(jsonDoctype.docTypeCode);
    jQuery("#currentDocTypeCode").val(jsonDoctype.docTypeCode);
    jQuery("#sortNum").text(jsonDoctype.sortNum);
    jQuery("#isValid").text(jsonDoctype.isValidName);
    jQuery("#keywords").text(jsonDoctype.keywords);
    jQuery("#imageContainer").empty();
    var imagePath = jsonDoctype.imagePath;
    if (imagePath != null && imagePath != "" && imagePath != undefined) {
        var imagePaths = jsonDoctype.imagePath.split(",");
        for (var i = 0; i < imagePaths.length; i++) {
            jQuery("#imageContainer").append("<img src='" + basedir + "/servlet/OutPutImageServlet?ts=" + new Date() + "&filename=" + imagePaths[i] + "' width='200' height='150'/>");
        }
    } else {
        jQuery("#imageContainer").append("<img src='" + basedir + "/servlet/OutPutImageServlet?ts=" + new Date() + "&filename=doctypelogo.jpg' width='200' height='150'/>");
    }
    jQuery("#navigateMenu").text(jsonDoctype.isNavigateMenuName);
}

//更新栏目VO时填充详细列表值
function fillUpdateNodeDetail(jsonDoctype) {
    jQuery("#update_id").val(jsonDoctype.id);
    jQuery("#update_name").val(jsonDoctype.name);
    jQuery("#update_docTypeName_backup").val(jsonDoctype.name);
    jQuery("#update_parentID").val(jsonDoctype.parentID);
    jQuery("#update_parentName").val(jsonDoctype.parentName);
    jQuery("#update_move_parentID").val(jsonDoctype.parentID);
    jQuery("#update_move_parentName").val(jsonDoctype.parentName);
    jQuery("#update_sharedIds").val(jsonDoctype.sharedIds);
    jQuery("#update_sharedNames").val(jsonDoctype.sharedNames);
    var description = jsonDoctype.description;
    var reg = new RegExp("<br/>", "gi");
    description = description.replace(reg, "\n");
    jQuery("#update_keywords").val(jsonDoctype.keywords);
    jQuery("#update_description").val(description);
    jQuery("#update_template_id").val(jsonDoctype.template_id);
    jQuery("#update_template_name").val(jsonDoctype.template_name);
    jQuery("#update_document_template_id").val(jsonDoctype.document_template_id);
    jQuery("#update_document_template_name").val(jsonDoctype.document_template_name);
    jQuery("#update_docTypeCode").val(jsonDoctype.docTypeCode);
    jQuery("#update_docTypeCode_backup").val(jsonDoctype.docTypeCode);
    jQuery("#update_sortNum").val(jsonDoctype.sortNum);
    jQuery("#update_isValid").val(jsonDoctype.isValid);
    jQuery("#uniqueCodePass").val('Y');
    jQuery("#uniqueNamePass").val('Y');
    jQuery("#update_imagePath").val(jsonDoctype.imagePath);
    jQuery("#update_imageContainer").empty();
    jQuery("#updateImageValues").empty();
    var imagePath = jsonDoctype.imagePath;
    if (imagePath != null && imagePath != "" && imagePath != undefined) {
        var imagePaths = jsonDoctype.imagePath.split(",");
        maxUpdateCount = imagePaths.length + 1;
        for (var i = 1; i <= imagePaths.length; i++) {
            jQuery("#update_imageContainer").append("<img id='update_imagePath" + i + "' src='" + basedir + "/servlet/OutPutImageServlet?ts=" + new Date() + "&filename=" + imagePaths[i - 1] + "' width='200' height='150'/><img id='updateDeleteImage" + i + "' src='" + basedir + "/images/ewp/delete.png' width='20px' height='20px' onclick='deleteImageforUpdate(" + i + ");'/>");
            jQuery("#updateImageValues").append("<input type='checkbox' name='update_imagePath' id='update_imagePathValue" + i + "' value='" + imagePaths[i - 1] + "' checked/>");
            initUpdateImageListener(i);
        }
        currentUpdateCount = maxUpdateCount;
    }
    jQuery("#update_imageContainer").append("<img id='update_imagePath" + currentUpdateCount + "' src='" + basedir + "/servlet/OutPutImageServlet?ts=" + new Date() + "&filename=doctypelogo.jpg' width='200' height='150'/>");
    jQuery("#updateImageValues").append("<input type='checkbox' name='update_imagePath' id='update_imagePathValue" + currentUpdateCount + "' value='on' checked/>");
    initLastImageListener();
    jQuery("#update_message").html(codeunique + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    jQuery("#name_update_message").html(nameunique + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    jQuery("#update_showLogo").attr("src", basedir + "/servlet/OutPutImageServlet?ts=" + new Date() + "&filename=" + jsonDoctype.imagePath);
    jQuery("#update_isNavigate").val(jsonDoctype.isNavigateMenu);
}
//上传图片的监听器
function initUpdateImageListener(currentIndex) {
    //修改栏目时上传栏目图片
    var buttonUpdate_ShowLogo = jQuery('#update_imagePath' + currentIndex), interval;
    new AjaxUpload(buttonUpdate_ShowLogo, {
        action: basedir + '/docTypeAction.do?cmd=upLoadDocTypePic',
        name: 'uploadFileName',
        responseType: 'json',
        onSubmit: function (file, ext) {
            if (ext && /^(jpg|png|jpeg|gif)$/.test(ext)) {
            } else {
                alert(not_image);
                return false;
            }
        },
        onComplete: function (filepath, response) {
            var clickedId = buttonUpdate_ShowLogo.attr("id");
            clickedId = clickedId.substr(16);
            var flag = response.flag;
            if (flag == 'success') {
                jQuery("#update_imagePath" + clickedId).attr("src", basedir + "/servlet/OutPutImageServlet?ts=" + new Date() + "&filename=" + response.message);
                jQuery("input[name='update_imagePath'][id='update_imagePathValue" + clickedId + "']").val(response.message);
            } else {
                alert(response.message);
            }
        }
    });
}

function initLastImageListener() {
    var buttonUpdate_ShowLogo = jQuery('#update_imagePath' + currentUpdateCount), interval;
    new AjaxUpload(buttonUpdate_ShowLogo, {
        action: basedir + '/docTypeAction.do?cmd=upLoadDocTypePic',
        name: 'uploadFileName',
        responseType: 'json',
        onSubmit: function (file, ext) {
            if (ext && /^(jpg|png|jpeg|gif)$/.test(ext)) {
            } else {
                alert(not_image);
                return false;
            }
        },
        onComplete: function (filepath, response) {
            var clickedId = buttonUpdate_ShowLogo.attr("id");
            clickedId = clickedId.substr(16);
            var flag = response.flag;
            if (flag == 'success') {
                createNewUpdateUpload(response.message, clickedId);
            } else {
                alert(response.message);
            }
        }
    });
}

//修改新的上传框
function createNewUpdateUpload(message, clickedId) {
    var currentId = "update_imagePath" + clickedId;
    jQuery("#" + currentId).attr("src", basedir + "/servlet/OutPutImageServlet?ts=" + new Date() + "&filename=" + message);
    jQuery("input[name='update_imagePath'][id='update_imagePathValue" + clickedId + "']").val(message);
    if (clickedId == maxUpdateCount && maxUpdateCount < 20) {
        var newClickedId = parseInt(clickedId) + 1;
        maxUpdateCount = newClickedId;
        currentId = "update_imagePath" + newClickedId;
        jQuery("#update_imageContainer").append("<img id='updateDeleteImage" + clickedId + "' src='" + basedir + "/images/ewp/delete.png' width='20px' height='20px' onclick='deleteImageforUpdate(" + clickedId + ");'/><img id='" + currentId + "' alt='' src='" + basedir + "/servlet/OutPutImageServlet?filename=doctypelogo.jpg' width='150' height='150'/>");
        var buttonUpdate_ShowLogo = jQuery("#" + currentId), interval;
        new AjaxUpload(buttonUpdate_ShowLogo, {
            action: basedir + '/docTypeAction.do?cmd=upLoadDocTypePic',
            name: 'uploadFileName',
            responseType: 'json',
            onSubmit: function (file, ext) {
                if (ext && /^(jpg|png|jpeg|gif)$/.test(ext)) {
                } else {
                    alert(not_image);
                    return false;
                }
            },
            onComplete: function (filepath, response) {
                var clickedId = buttonUpdate_ShowLogo.attr("id");
                clickedId = clickedId.substr(16);
                var flag = response.flag;
                if (flag == 'success') {
                    //再增加一个图片输入框
                    createNewUpdateUpload(response.message, clickedId);
                } else {
                    alert(response.message);
                }
            }
        });
        jQuery("#upateOuterContainer").scrollLeft(jQuery("#upateOuterContainer").attr("scrollLeft") + 150);
        //添加复选框
        jQuery("#updateImageValues").append("<input type='checkbox' name='update_imagePath' id='update_imagePathValue" + maxUpdateCount + "' checked/>");
    }
}

//新增栏目时重置栏目输入框
function resetDoctype() {
    jQuery("#add_sharedIds").val('');
    jQuery("#add_template_id").val('');
    jQuery("#add_template_name").val('');
    jQuery("#add_document_template_id").val('');
    jQuery("#add_document_template_name").val('');
    jQuery("#add_sharedNames").val('');
    jQuery("#add_name").val('');
    jQuery("#add_keywords").val('');
    jQuery("#add_description").val('');
    jQuery("#add_permissions").val('');
    jQuery("#add_docTypeCode").val('');
    jQuery("#add_isValid").val('1');
    jQuery("#add_sortNum").val('0');
    jQuery("#add_imagePath").val('');
    jQuery("#uniqueCodePass").val('Y');
    jQuery("#uniqueNamePass").val('Y');
    jQuery("#errorMsg").html(codeunique + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    jQuery("#showLogo").attr("src", basedir + "/servlet/OutPutImageServlet?filename=doctypelogo.jpg" + "&ts=" + new Date());
}

//新增栏目输入完毕后返回的值串
function getNewDoctypeValue() {
    var add_parentID = jQuery("#add_parentID").val();
    var add_name = jQuery("#add_name").val();
    var add_keywords = jQuery("#add_keywords").val();
    var add_description = jQuery("#add_description").val();
    var reg = new RegExp("\n", "gi");
    add_description = add_description.replace(reg, "<br/>");
    var add_isValid = jQuery("#add_isValid").val();
    var add_sharedIds = jQuery("#add_sharedIds").val();
    var templateId = jQuery("#add_template_id").val();
    var docTemplateId = jQuery("#add_document_template_id").val();
    var site_id = getSiteId();
    var docTypeCode = jQuery("#add_docTypeCode").val();
    var add_sortNum = jQuery("#add_sortNum").val();

    var add_imagePath = "";
    jQuery("input[name='add_imagePath']").each(function () {
        var val = jQuery(this).val();
        if (val != null && val != "" && val != undefined && val != "on" && jQuery(this).is(":checked")) {
            add_imagePath = add_imagePath + jQuery(this).val() + ",";
        }
    });
    if (add_imagePath != null && add_imagePath != "" && add_imagePath != undefined) {
        add_imagePath = add_imagePath.substring(0, add_imagePath.length - 1);
    }
    var isnavigate = jQuery("#add_isNavigate").val();
    var whetherLocateToParentNode = jQuery("#whetherLocateToParentNode").is(":checked");
    return  '{parentID:"' + add_parentID + '",name:"' + add_name + '",description:"' + add_description
        + '",isValid:"' + add_isValid + '",imagePath:"' + add_imagePath + '",site_id:"' + site_id
        + '",sharedIds:"' + add_sharedIds + '",sortNum:"' + add_sortNum + '",whetherLocateToParentNode:"' + whetherLocateToParentNode
        + '",templateId:"' + templateId + '",docTemplateId:"' + docTemplateId + '",docTypeCode:"' + docTypeCode
        + '",isNavigateMenu:"' + isnavigate + '",keywords:"' + add_keywords + '"}';
}

//修改栏目输入完毕后返回的值串
function getUpdateDoctypeValue() {
    var parentID = jQuery("#update_parentID").val();
    var docTypeId = jQuery("#update_id").val();
    var docTypeName = jQuery("#update_name").val();
    var update_description = jQuery("#update_description").val();
    var reg = new RegExp("\n", "gi");
    update_description = update_description.replace(reg, "<br/>");
    var isValid = jQuery("#update_isValid").val();
    var site_id = getSiteId();
    var sortNum = jQuery("#update_sortNum").val();
    var sharedIds = jQuery("#update_sharedIds").val();
    var templateid = jQuery("#update_template_id").val();
    var docTemplateid = jQuery("#update_document_template_id").val();
    var docTypeCode = jQuery("#update_docTypeCode").val();
    var isnavigate = jQuery("#update_isNavigate").val();
    var keywords = jQuery('#update_keywords').val();
    var imagePath = "";
    jQuery("input[name='update_imagePath']").each(function () {
        var val = jQuery(this).val();
        if (val != null && val != "" && val != undefined && val != "on" && jQuery(this).is(":checked")) {
            imagePath = imagePath + jQuery(this).val() + ",";
        }
    });
    if (imagePath != null && imagePath != "" && imagePath != undefined) {
        imagePath = imagePath.substring(0, imagePath.length - 1);
    }
    return '{parentID:"' + parentID + '",name:"' + docTypeName + '",description:"' + update_description
        + '",isValid:"' + isValid + '",imagePath:"' + imagePath + '",id:"' + docTypeId + '",sortNum:"' + sortNum
        + '",site_id:"' + site_id + '",sharedIds:"' + sharedIds + '",templateId:"' + templateid
        + '",docTemplateId:"' + docTemplateid + '",docTypeCode:"' + docTypeCode + '",isNavigateMenu:"' + isnavigate
        + '",keywords:"' + keywords + '"}';
}