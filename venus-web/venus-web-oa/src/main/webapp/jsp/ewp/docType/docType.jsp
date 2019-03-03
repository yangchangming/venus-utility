<!--
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="udp.ewp.util.EnumTools" %>
<%@ page import="venus.frames.mainframe.util.Helper" %>
<%@ page import="udp.ewp.website.bs.IWebsiteBs" %>
<%@ page import="udp.ewp.website.model.Website" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.util.List" %>
<%
    request.setAttribute("isValidMap", EnumTools.getSortedEnumMap(EnumTools.LOGICTRUEFALSE));
    request.setAttribute("sortMap", EnumTools.getSortedEnumMap(EnumTools.SORTNUM, EnumTools.numberComparator));
    request.setAttribute("navigateMap", EnumTools.getSortedEnumMap(EnumTools.NAVIGATE_MENU));
%>
<%
    //取出当前站点
    IWebsiteBs websiteBs = (IWebsiteBs) Helper.getBean("IWebsiteBs");
    String site_id = (String) session.getAttribute("site_id");
    Website currentWebsite = null;
    List websites = websiteBs.queryAll();
    for (int i = 0; i < websites.size(); i++) {
        Website website = (Website) websites.get(i);
        if (StringUtils.equals(site_id, website.getId())) {
            currentWebsite = website;
            break;
        }
    }
    if (currentWebsite == null) {
        for (int i = 0; i < websites.size(); i++) {
            if (currentWebsite == null) {
                Website website = (Website) websites.get(i);
                if (StringUtils.equals("Y", website.getIsDefault())) {
                    currentWebsite = website;
                    break;
                }
            }
        }
    }

    if (currentWebsite == null) {
        currentWebsite = (Website) websites.get(0);
    }
%>
<link rel="stylesheet" href="<venus:base/>/css/ewp/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="<venus:base/>/css/ewp/ewptree.css" type="text/css">
<script src="<venus:base/>/dwrewp/interface/EwpTreeControl.js" type="text/javascript"></script>
<script src="<venus:base/>/dwr/engine.js" type="text/javascript"></script>
<script src="<venus:base/>/dwr/util.js" type="text/javascript"></script>
<script type="text/javascript" src="<venus:base/>/js/jquery/ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="<venus:base/>/js/ewp/gap-ewp-tree-ext.js"></script>
<script type="text/javascript" src="<venus:base/>/js/ewp/gap-ewp-tree-main.js"></script>
<script type="text/javascript" src="<venus:base/>/js/ewp/doctype/doctype.js"></script>
<script type="text/javascript" src="<venus:base/>/js/ewp/templateReference.js"></script>
<script type="text/javascript" src="<venus:base/>/js/jquery/plugin/jquery.ajaxupload.js"></script>
<fmt:bundle basename="udp.ewp.ewp_resource">
<script type="text/javascript">
    var setting = {
        async: {
            enable: true,
            url: "<%=request.getContextPath()%>/doctype/tree",
            autoParam: ["id=pId", "siteId"]
        },
        edit: {
            drag: {
                autoExpandTrigger: true,
                isCopy: false
            },
            enable: true,
            showRemoveBtn: false,
            showRenameBtn: false
        },
        callback: {
            beforeDrag: beforeDragTreeNode,
            beforeDrop: beforeDropTreeNode,
            onClick: onClickTreeNode,
            onDrop: onDropTreeNode,
            onAsyncSuccess:onAsyncTreeNode
        },
        view: {
            showLine: false
        }
    };
</script>
<script type="text/javascript">

    var confirm_save_doctype = '<fmt:message key="udp.ewp.doctype.confirm_save_doctype"/>';
    var save_doctype_error = '<fmt:message key="udp.ewp.doctype.save_doctype_error"/>';
    var codeunique = '<fmt:message key="udp.ewp.doctype.code.unique"/>';
    var viewcode_has_exist_or_notvalid = '<fmt:message key="udp.ewp.doctype_check_code"/>';
    var nameunique = '<fmt:message key="udp.ewp.doctype.name.unique"/>';
    var name_has_exist_or_notvalid = '<fmt:message key="udp.ewp.doctype_check_name"/>';
    var confirm_update_doctype = '<fmt:message key="udp.ewp.doctype.confirm_update_doctype"/>';
    var confirm_delete_this_doctype = '<fmt:message key="udp.ewp.doctype.confirm_delete_this_doctype"/>';
    var confirm_move_this_doctype = ' <fmt:message key="udp.ewp.doctype.confirm_move_this_doctype"/>';
    var not_image = ' <fmt:message key="udp.ewp.doctype.not_image"/>';
    var root_not_allowedit = ' <fmt:message key="udp.ewp.doctype.root.not.allowedit"/>';
    var root_not_allowdelete = ' <fmt:message key="udp.ewp.doctype.root.not.allowdelete"/>';
    var root_not_allowmove = ' <fmt:message key="udp.ewp.doctype.root.not.allowmove"/>';
    var root_not_allowhang = ' <fmt:message key="udp.ewp.doctype.root.not.allowhang"/>';
    var pleaseinputdoctypename = ' <fmt:message key="udp.ewp.doctype.pleaseinputdoctypename"/>';
    var expandAll = '<fmt:message key="udp.ewp.doctype.expandAllNode"/>';
    var collapseAll = '<fmt:message key="udp.ewp.doctype.collapseAllNode"/>';
    var needFocusNodeId = null;
    //根目录
    var basedir = "<venus:base/>";
    // 新增保存时较验栏目编码是否唯一
    function codeValidateWhenAdd() {
        var waitingForCheckCode = jQuery("#add_docTypeCode").val();
        if (waitingForCheckCode != null && waitingForCheckCode != '') {
            var regu = /^[a-z,A-Z,0-9,_]+$/;
            if (!regu.exec(waitingForCheckCode)) {
                jQuery("#uniqueCodePass").val('N');
                jQuery("#errorMsg").html('<fmt:message key="udp.ewp.template_viewcode_only_char_or_number_or_underline" />');
                return true;
            }
        } else {
            jQuery("#uniqueCodePass").val('Y');
            jQuery("#errorMsg").html('<fmt:message key="udp.ewp.doctype.code.unique" />');
            return true;
        }
        if (waitingForCheckCode != null && waitingForCheckCode != '') {
            jQuery.ajax({url: "<venus:base/>/docTypeAction.do?cmd=checkViewCodeIsUnique&Action=get&waitingForCheckCode=" + waitingForCheckCode + "&website_id=" + getSiteId(), async: false, cache: false, dataType: "text", success: function (data, textStatus) {
                var checkResult = eval('(' + data + ')');
                var isUnique = checkResult.isUnique;
                var checkMessage = checkResult.checkMessage;
                jQuery("#uniqueCodePass").val(isUnique);
                jQuery("#errorMsg").html(checkMessage);
                return true;
            }});
        } else {
            jQuery("#errorMsg").html(codeunique);
            jQuery("#uniqueCodePass").val('Y');
            return true;
        }
        return true;
    }

    // 修改保存时较验栏目编码是否唯一
    function codeValidateWhenUpdate() {
        var waitingForCheckCode = jQuery("#update_docTypeCode").val();
        if (waitingForCheckCode != null && waitingForCheckCode != '') {
            var regu = /^[a-z,A-Z,0-9,_]+$/;
            if (!regu.exec(waitingForCheckCode)) {
                jQuery("#uniqueCodePass").val('N');
                jQuery("#uniqueCodePass").html('<fmt:message key="udp.ewp.template_viewcode_only_char_or_number_or_underline" />');
                return true;
            }
        } else {
            jQuery("#errorMsg").html(codeunique);
            jQuery("#uniqueCodePass").val('Y');
            return true;
        }
        var update_docTypeCode_backup = jQuery("#update_docTypeCode_backup").val();
        if (equals(waitingForCheckCode, update_docTypeCode_backup)) {
            jQuery("#update_message").html(codeunique);
            jQuery("#uniqueCodePass").val('Y');
            return true;
        }
        if (waitingForCheckCode != null && waitingForCheckCode != '') {
            jQuery.ajax({url: "<venus:base/>/docTypeAction.do?cmd=checkViewCodeIsUnique&Action=get&waitingForCheckCode=" + waitingForCheckCode + "&website_id=" + getSiteId(), async: false, cache: false, dataType: "text", success: function (data, textStatus) {
                var jsonDoctype = eval('(' + data + ')');
                var isUnique = jsonDoctype.isUnique;
                var checkMessage = jsonDoctype.checkMessage;
                jQuery("#uniqueCodePass").val(isUnique);
                jQuery("#update_message").html(checkMessage);
                return true;
            }});
        } else {
            jQuery("#update_message").html(codeunique);
            jQuery("#uniqueCodePass").val('Y');
            return true;
        }
        return true;
    }

    // 新增保存时较验栏目名称是否唯一
    function nameValidateWhenAdd() {
        var nameIsUnique = jQuery("#nameIsUnique").val();
        if (nameIsUnique == "Y") {
            var waitingForCheckName = jQuery("#add_name").val();
            if (waitingForCheckName != null && waitingForCheckName != '') {
                jQuery.ajax({url: "<venus:base/>/docTypeAction.do?cmd=checkNameIsUnique&Action=get&waitingForCheckName=" + waitingForCheckName, async: false, cache: false, dataType: "text", success: function (data, textStatus) {
                    var checkResult = eval('(' + data + ')');
                    var isUnique = checkResult.isUnique;
                    var checkMessage = checkResult.checkMessage;
                    jQuery("#uniqueNamePass").val(isUnique);
                    jQuery("#nameErrorMsg").html(checkMessage);
                    return true;
                }});
            } else {
                jQuery("#nameErrorMsg").html(codeunique);
                jQuery("#uniqueNamePass").val('Y');
                return true;
            }
        } else {
            jQuery("#nameErrorMsg").html("");
            jQuery("#uniqueNamePass").val('Y');
        }
        return true;
    }

    // 修改保存时较验栏目名称是否唯一
    function nameValidateWhenUpdate() {
        var nameIsUnique = jQuery("#nameIsUnique").val();
        if (nameIsUnique == "Y") {
            var waitingForCheckName = jQuery("#update_name").val();
            var update_docTypeName_backup = jQuery("#update_docTypeName_backup").val();
            if (equals(waitingForCheckName, update_docTypeName_backup)) {
                jQuery("#name_update_message").html(nameunique);
                jQuery("#uniqueNamePass").val('Y');
                return true;
            }
            if (waitingForCheckName != null && waitingForCheckName != '') {
                jQuery.ajax({url: "<venus:base/>/docTypeAction.do?cmd=checkNameIsUnique&Action=get&waitingForCheckName=" + waitingForCheckName, async: false, cache: false, dataType: "text", success: function (data, textStatus) {
                    var jsonDoctype = eval('(' + data + ')');
                    var isUnique = jsonDoctype.isUnique;
                    var checkMessage = jsonDoctype.checkMessage;
                    jQuery("#uniqueNamePass").val(isUnique);
                    jQuery("#name_update_message").html(checkMessage);
                    return true;
                }});
            } else {
                jQuery("#name_update_message").html(codeunique);
                jQuery("#uniqueNamePass").val('Y');
                return true;
            }
        } else {
            jQuery("#name_update_message").html("");
            jQuery("#uniqueNamePass").val('Y');
        }
        return true;
    }

    //删除图片
    function deleteImage(id) {
        jQuery("#add_showLogo" + id).remove();
        jQuery("#deleteImage" + id).remove();
        jQuery("#add_imagePath" + id).remove();
    }

    //删除图片
    function deleteImageforUpdate(id) {
        jQuery("#update_imagePath" + id).remove();         //删除图片
        jQuery("#updateDeleteImage" + id).remove();       //删除删除按钮
        jQuery("#update_imagePathValue" + id).remove(); //删除复选框
    }
</script>
<script type="text/javascript">
    var templateDialog = null;
    var hangDoctypeDialog = null;
    var currentCount = 1;
    var maxCount = 1;
    var currentUpdateCount = 1;
    var maxUpdateCount = 1;
    jQuery(document).ready(function () {
        //增加栏目时上传栏目图片
        var buttonAdd_ShowLogo = jQuery('#add_showLogo' + currentCount), interval;
        new AjaxUpload(buttonAdd_ShowLogo, {
            action: '<venus:base/>/docTypeAction.do?cmd=upLoadDocTypePic',
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
                var clickedId = buttonAdd_ShowLogo.attr("id");
                clickedId = clickedId.substr(12);
                var flag = response.flag;
                if (flag == 'success') {
                    createNewUpload(response.message, clickedId);
                } else {
                    alert(response.message);
                }
            }
        });

        //创建新的上传框
        function createNewUpload(message, clickedId) {
            var currentId = "add_showLogo" + clickedId;
            jQuery("#" + currentId).attr("src", "<venus:base/>/servlet/OutPutImageServlet?ts=" + new Date() + "&filename=" + message);
            //更新原复选框
            jQuery("input[name='add_imagePath'][id='add_imagePath" + clickedId + "']").val(message);
            if (clickedId == maxCount && maxCount < 20) {
                currentCount = currentCount + 1;
                maxCount = currentCount;
                currentId = "add_showLogo" + maxCount;
                jQuery("#addImageContainer").append("<img id='deleteImage" + clickedId + "' src='<%=request.getContextPath()%>/images/ewp/delete.png' width='20px' height='20px' onclick='deleteImage(" + clickedId + ");'/><img id='" + currentId + "' alt='' src='<%=request.getContextPath()%>/servlet/OutPutImageServlet?filename=doctypelogo.jpg' width='150' height='150'/>");
                var buttonAdd_ShowLogo = jQuery("#" + currentId), interval;
                new AjaxUpload(buttonAdd_ShowLogo, {
                    action: '<venus:base/>/docTypeAction.do?cmd=upLoadDocTypePic',
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
                        var clickedId = buttonAdd_ShowLogo.attr("id");
                        clickedId = clickedId.substr(12);
                        var flag = response.flag;
                        if (flag == 'success') {
                            //再增加一个图片输入框
                            createNewUpload(response.message, clickedId);
                        } else {
                            alert(response.message);
                        }
                    }
                });
                jQuery("#addOuterContainer").scrollLeft(jQuery("#addOuterContainer").attr("scrollLeft") + 150);
                //添加复选框
                jQuery("#addImageValues").append("<input type='checkbox' name='add_imagePath' id='add_imagePath" + maxCount + "' checked/>");
            }
        }

        jQuery("#treeTable tr").eq(1).find("td").eq(0);
        //初始化模板对话框
        templateDialog = jQuery("#referenceTemplate").dialog({ modal: true, height: 490, autoOpen: false, resizable: false, width: 450, overlay: { opacity: 0.3, background: "black" }});
        //初始化挂接模板对话框
        hangDoctypeDialog = jQuery("#referenceHangDoctype").dialog({ modal: true, height: 500, autoOpen: false, resizable: false, width: 450, overlay: { opacity: 0.3, background: "black" }});
    });

</script>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><fmt:message key="udp.ewp.doctype_manage"/></title>
</head>
<body onload="init(setting)">

<form name="form_treebasic" action="<venus:base/>/docTypeAction.do" method="post">
    <input name="cmd" type="hidden" value=""/>
    <input type="hidden" name="backFlag" value="true">
    <input id="webModel" name="webModel" type="hidden" class="text_field" inputName="发布目录" value="<venus:base/>"
           readonly="true">
    <input id="tree_href" name="tree_href" type="hidden" class="text_field" value="" readonly="true">
    <input id="tree_onclick" name="tree_onclick" type="hidden" class="text_field" value="doClick(this)" readonly="true">
    <input id="tree_target" name="tree_target" type="hidden" class="text_field" value="" readonly="true">
    <input type="hidden" name="currentDocTypeId" id="currentDocTypeId" value=""/>
    <input type="hidden" name="currentDocTypeCode" id="currentDocTypeCode" value=""/>
    <input type="hidden" name="currentDocTypeName" id="currentDocTypeName" value=""/>
    <input type="hidden" name="currentParentDocTypeId" id="currentParentDocTypeId" value=""/>
    <input type="hidden" name="currentParentDocTypeName" id="currentParentDocTypeName" value=""/>
    <input type="hidden" name="uniqueCodePass" id="uniqueCodePass" value="Y"/>
    <input type="hidden" name="uniqueNamePass" id="uniqueNamePass" value="Y"/>

    <input type="hidden" value="<%=currentWebsite.getNameIsUnique()%>" id="nameIsUnique">

    <table width="100%" border='0' cellpadding='0' cellspacing='0'>
        <tr>
            <td colspan='3'>
                <script type="text/javascript">writeTableTop('<fmt:message key="udp.ewp.doctype_manage"/>', '<venus:base/>/themes/<venus:theme/>/');</script>
            </td>
        </tr>
        <tr style="height: 5px">
            <td style="height: 5px"></td>
        </tr>
        <tr>
            <td align="left" valign="top" width="220px" height="550px" bgcolor="#e1e1e1">
                <table id="treeTable" width="220px" height="550px" border="0" cellpadding="0" cellspacing="0"
                       bgcolor="#7EBAFF">
                    <tr>
                        <td bgcolor="#e1e1e1" style="height: 20px" align="right">

                        </td>
                    </tr>
                    <tr>
                        <td valign="top" bgcolor="#e1e1e1"><!-- 左侧树 -->
                            <ul id="tree" class="ztree" align="left"
                                style="width: 220px; height: 100%; border: 0px solid #90b3cf; overflow: auto;"></ul>
                        </td>
                    </tr>
                </table>
            </td>
            <td>&nbsp;</td>
            <td align="left" valign="top" width="100%" height="100%">
                <table syle="border: solid 1px #666" cellpadding="0" cellspacing="0" bgcolor="#7EBAFF" width="100%"
                       height="100%">
                    <tr>
                        <td bgcolor="#e1e1e1" align="center" valign="top"><!--查看栏目详细信息 -->
                            <%@   include file="/jsp/ewp/docType/areas/docTypeDetailDiv.jsp" %>
                            <!--修改栏目开始 -->
                            <%@   include file="/jsp/ewp/docType/areas/docTypeUpdateDiv.jsp" %>
                            <!--添加栏目开始 -->
                            <%@   include file="/jsp/ewp/docType/areas/addDocTypeDiv.jsp" %>
                            <!--移动栏目开始 -->
                            <%@   include file="/jsp/ewp/docType/areas/defalutPageDiv.jsp" %>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <div id="msg"></div>
</form>
<script type="text/javascript">writeTableBottom('<venus:base/>/themes/<venus:theme/>/');</script>
<div id="referenceTemplate" title="请选择模板" style="background-color: #c8cdd3; cell-padding: 5px;"></div>
<div id="referenceHangDoctype" title="请选择要挂接的栏目"></div>
</body>
</html>
</fmt:bundle>
