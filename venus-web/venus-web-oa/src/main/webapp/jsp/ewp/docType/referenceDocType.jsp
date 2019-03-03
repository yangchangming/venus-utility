<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/gap-html.tld" prefix="venus" %>
<%
    String referenceChecktype = request.getParameter("checktype");
    String referenceSelectedValues = request.getParameter("selectedValues");
    String referenceTree_href = request.getParameter("tree_href");
    String referenceSite_id = request.getParameter("site_id");
    String referenceCurrentid = request.getParameter("currentid");
    String referenceNotIncludeValues = request.getParameter("notIncludeValues");
    String method = request.getParameter("method");
%>
<fmt:bundle basename="udp.ewp.ewp_resource">
    <html>
    <title>参照页面<%=referenceChecktype%>
    </title>
    <script language="javascript">
        //新增时保存
        function add_save_onClick() {
            var ids = new Array();
            var chinesenames = new Array();
            var allDocType = document.getElementsByName("selectDocType");
            for (var i = 0; i < allDocType.length; i++) {
                if (allDocType[i].checked) {
                    ids.push(allDocType[i].value);
                    chinesenames.push(allDocType[i].getAttribute("chinesename"));
                }
            }
            if (ids != '' && ids != undefined) {
                jQuery("#add_sharedIds").val(ids);
                jQuery("#add_sharedNames").val(chinesenames);
            } else {
                jQuery("#add_sharedIds").val('');
                jQuery("#add_sharedNames").val('');
            }
            hangDoctypeDialog.dialog("close");
        }

        //更新时保存
        function update_save_onClick() {
            var ids = new Array();
            var chinesenames = new Array();
            var allDocType = document.getElementsByName("selectDocType");
            for (var i = 0; i < allDocType.length; i++) {
                if (allDocType[i].checked) {
                    ids.push(allDocType[i].value);
                    chinesenames.push(allDocType[i].getAttribute("chinesename"));
                }
            }
            if (ids != '' && ids != undefined) {
                jQuery("#update_sharedIds").val(ids);
                jQuery("#update_sharedNames").val(chinesenames);
            } else {
                jQuery("#update_sharedIds").val('');
                jQuery("#update_sharedNames").val('');
            }
            hangDoctypeDialog.dialog("close");
        }

        //移动时保存
        function move_save_onClick() {
            var id = jQuery('input[name=selectDocType]:checked').val();
            var chinesename = jQuery('input[name=selectDocType]:checked').attr("chinesename");
            if (id == null) {
                alert('<fmt:message key="udp.ewp.select_one_record"/>')
                return;
            }
            if (id != '' && id != undefined) {
                jQuery("#move_parentID").val(id);
                jQuery("#move_parentName").val(chinesename);
            } else {
                jQuery("#move_parentID").val('');
                jQuery("#move_parentName").val('');
            }
            hangDoctypeDialog.dialog("close");
        }

        function return_onClick() {  //取消后返回列表页面
            jQuery("#referenceHangDoctype").html("");
            hangDoctypeDialog.dialog("close");
        }

        //选择ID相同的其它复选框
        function selectAllSimilar(obj) {
        　 jQuery("#referenceTree input[type='checkbox'][id='" + (jQuery(obj).attr("id")) + "']").prop("checked", jQuery(obj).is(":checked"));
        }

        //全部取消
        function unSelectAll_onClick() {
        　 jQuery("#referenceTree input").attr("checked", false);
        }

        //全部展开树节点
        function expandAllNode_onClick() {
            jQuery("#referenceTree div[id*='container_']").each(function () {
                jQuery(this).css("display", "block");
                jQuery(this).prev().children("img").attr("src", webModel + "/images/tree/parentopen.gif");//更改图标为打开状态
            });
            jQuery("#referenceTree .baseNode img:first").attr("src", webModel + "/images/tree/root.png");
        }

        //全部收缩树节点
        function collapseAllNode_onClick() {
            jQuery("#referenceTree div[id*='container_']").each(function () {
                jQuery(this).css("display", "none");
                jQuery(this).prev().children("img").attr("src", webModel + "/images/tree/parent.gif");//更改图标为打开状态
            });
            jQuery("#referenceTree .baseNode img:first").attr("src", webModel + "/images/tree/root.png");
        }


        //树的根节点
        var referenceRootID = "root";
        //用户调用的beanID
        var referenceBeanId = "docTypeBS";
        //用户自定义初始化树
        function onLoadReferenceTree() {
            var referenceWebModel = referenceForm_treebasic.referenceWebModel.value;
            var referenceTree_onclick = "doClick(this)";  //单击事件方法名
            var referenceTree_param = "docTypeID";       //链接参数名
            var referenceSite_id = "<%=referenceSite_id%>";
            var paramJson = '{tree_href:"<%=referenceTree_href%>",tree_onclick:"' + referenceTree_onclick + '",treeid:"referenceTree",site_id:"' + referenceSite_id + '",webModel:"' + referenceWebModel + '",tree_param:"",tree_target:"",checkType:"<%=referenceChecktype%>",notIncludeValues:"<%=referenceNotIncludeValues%>",currentid:"<%=referenceCurrentid%>",selectedValues:"<%=referenceSelectedValues%>"}';
            initTree(referenceRootID, 'referenceTree', paramJson);
        }

    </script>
    <script language="javascript">
        jQuery(document).ready(function () {
            onLoadReferenceTree();
        });
    </script>
    <base target="_self">
    </head>
    <body>

    <form name="referenceForm_treebasic" action="" method="post"><input type="hidden" name="referenceCmd" value="">
        <input type="hidden" id="reference_site_id" name="reference_site_id"
               value="<%=referenceSite_id%>"> <input id="referenceWebModel" name="referenceWebModel" type="hidden"
                                                     class="text_field" inputName="发布目录"
                                                     value="<%=request.getContextPath()%>" readonly="true">
        <table bgcolor="#c8cdd3" style="width: 100%;">
            <tr>
                <td>
                    <input type="button" name="referenceCancel"
                           value='<fmt:message key="udp.ewp.doctype.expandAllNode"/>' class="button_ellipse"
                           onClick="javascript:expandAllNode_onClick();">
                    <input type="button" name="referenceCancel"
                           value='<fmt:message key="udp.ewp.doctype.collapseAllNode"/>' class="button_ellipse"
                           onClick="javascript:collapseAllNode_onClick();">
                    <input type="button" name="referenceCancel"
                           value='<fmt:message key="udp.ewp.doctype.unSelectAll" />' class="button_ellipse"
                           onClick="javascript:unSelectAll_onClick();">
                    &nbsp;&nbsp;&nbsp;
                    <input type="button" name="referenceSubmit"
                           value='<fmt:message key="confirm" bundle="${applicationResources}"/>' class="button_ellipse"
                           onClick="javascript:<%=method%>();">
                    <input type="button" name="referenceCancel"
                           value='<fmt:message key="cancel" bundle="${applicationResources}"/>' class="button_ellipse"
                           onClick="javascript:return_onClick();">
                </td>
            </tr>
            <tr>
                <td>
                    <div id="referenceTree" align="left"
                         style="width: 410px; height: 400px; background-color: #c8cdd3; border: 0px solid #90b3cf; overflow: auto;"></div>
                </td>
            </tr>
        </table>

    </form>
    </body>
    </html>
</fmt:bundle>