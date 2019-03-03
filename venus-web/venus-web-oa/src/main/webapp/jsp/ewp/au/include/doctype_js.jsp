<%@ page contentType="text/html; charset=UTF-8" %>

<script language="javascript">
    var hangDoctypeDialog = null;
    jQuery(document).ready(function () {
        hangDoctypeDialog = jQuery("#referenceHangDoctype").dialog({draggable: false,bgiframe: true, modal: true, height: 500, autoOpen: false, resizable: false, width: 368, position: ["center", "top"], overlay: { opacity: 0.3, background: "#c8cdd3" }});
    });
    function toDoctypeTreePage() {//ewp建站平台栏目数据权限
        var elementCheckbox = document.getElementsByName("checkbox_template");
        var number = 0;
        var relId = null;
        for (var i = 0; i < elementCheckbox.length; i++) {
            if (elementCheckbox[i].checked) {
                number += 1;
                relId = elementCheckbox[i].value;
            }
        }
        if (number == 0) {
            alert("<fmt:message key='venus.authority.Please_select_a_record_of_their_relationship' bundle='${applicationAuResources}' />!")
            return;
        }
        if (number > 1) {
            alert("<fmt:message key='venus.authority.Can_only_select_a_record' bundle='${applicationAuResources}' />!")
            return;
        }
        var thisHidden = getLayoutHiddenObjectById(relId);
        var code = jQuery(thisHidden).attr("code");
        var pType = jQuery(thisHidden).attr("party_type");
        var partyId = form.partyId.value;

        var doctypeTreePath = "<venus:base/>/jsp/ewp/docType/docTypeRef_hang.jsp?" + "hasSelectWebsite=Y&pType="+pType+"&partyId="+partyId+"&checktype=CHECK&tree_href=none&methodCallbackOk=callbackOK_AUDoctypeTree&methodCallbackCancel=callbackCancel_AUDoctypeTree";
        hangDoctypeDialog.dialog('moveToTop')
        jQuery.get(doctypeTreePath, {Action: "get", async: false}, function (data, textStatus) {
            jQuery("#referenceHangDoctype").html(data);
            jQuery('.ui-dialog-titlebar-close').hide();

            hangDoctypeDialog.dialog("open");
        });

    }
    function callbackOK_AUDoctypeTree(idnames, method) {
        var elementCheckbox = document.getElementsByName("checkbox_template");
        var number = 0;
        var relId = null;
        for (var i = 0; i < elementCheckbox.length; i++) {
            if (elementCheckbox[i].checked) {
                number += 1;
                relId = elementCheckbox[i].value;
            }
        }
        if (number == 0) {
            alert("<fmt:message key='venus.authority.Please_select_a_record_of_their_relationship' bundle='${applicationAuResources}' />!")
            return;
        }
        if (number > 1) {
            alert("<fmt:message key='venus.authority.Can_only_select_a_record' bundle='${applicationAuResources}' />!")
            return;
        }

        var thisHidden = getLayoutHiddenObjectById(relId);
        var code = jQuery(thisHidden).attr("code");
        var pType = jQuery(thisHidden).attr("party_type");
        var partyId = form.partyId.value;
        var doctypes = eval("(" + idnames + ")");
        var docTypeIds = doctypes.docTypeIds;
        var websitId = $("#reference_site_id").val();

        var doctype_au_url = "<venus:base/>/auRelationAction.do?cmd=saveOrUpdateDoctypeAu";
        $.post(doctype_au_url
                , { "pType": pType, "partyId": partyId, "docTypeIds": docTypeIds, "websitId": websitId }
                , function (data) {
                    if (data.isPass == "Y") {
                        alert("<fmt:message key='venus.authority.Save_successful_' bundle='${applicationAuResources}' />")
                    } else {
                        alert("<fmt:message key='venus.authority.Failed_' bundle='${applicationAuResources}' />")
                    }
                }
                , "json");

        callbackCancel_AUDoctypeTree();
    }

    function callbackCancel_AUDoctypeTree() {
        jQuery("#referenceHangDoctype").html("");
        hangDoctypeDialog.dialog("close");
    }

</script>