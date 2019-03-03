<!--
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/REC-html40/strict.dtd">
-->
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="udp.ewp.doctype.bs.IDocTypeBS" %>
<%@ page import="udp.ewp.doctype.model.DocType" %>
<%@ page import="udp.ewp.document.model.Document" %>
<%@ page import="udp.ewp.document.util.IConstants" %>
<%@ page import="udp.ewp.util.ServletContextHelper" %>
<%@ page import="venus.frames.mainframe.util.Helper" %>
<%@ page import="venus.frames.mainframe.util.VoHelper"%>
<%@ page import="java.sql.Timestamp" %>
<script src="<venus:base/>/fckeditor/fckeditor.js"></script>
<style>
    span.deleteable {
        position: relative;
    }

    span.deleteable input {
        padding-right: 16px;
    }

    span.deleteicon {
        position: absolute;
        display: block;
        top: 0px;
        *top: 2px;
        *left: 167px;
        right: 6px;
        width: 16px;
        height: 16px;
        background: url('<venus:base/>/images/ewp/clear.ico') no-repeat center;
        cursor: pointer;
    }
</style>

<%
    Document doc = new Document();
    String operationType = "";
    String docTypeID = "";
    String siteID = "";
    if (request.getParameter("docTypeID") != null) {
        docTypeID = request.getParameter("docTypeID");
    }
    if (request.getParameter("siteId") != null) {
        siteID = request.getParameter("siteId");
    }

    if (request.getAttribute("document") != null) {
        doc = (Document) request.getAttribute("document");
        // docTypeID = doc.getDocTypeID();
    }
    if (request.getParameter("operationType") != null) {
        operationType = request.getParameter("operationType");
    }
    if (request.getAttribute("operationType") != null) {
        operationType = (String) request.getAttribute("operationType");
    }
    String originalDocTypeIds = (String) request.getAttribute("orginalDocTypeIds");
    String originalDocTypeNames = (String) request.getAttribute("orginalDocTypeNames");
    IDocTypeBS docTypeBs = (IDocTypeBS) Helper.getBean("docTypeBS");
    DocType docType = null;
    if (docTypeID != null && !"".equals(docTypeID) && !"root".equals(docTypeID)) {
        docType = docTypeBs.findDocTypeById(docTypeID);
    }

    String site_code = ServletContextHelper.getWebsiteCodeBySiteId(siteID);

    Timestamp releaseTs = (Timestamp) request.getAttribute("preReleaseDate");
%>


<!-- fmt:setLocale value="zh" scope="session"/-->
<fmt:bundle basename="udp.ewp.ewp_resource" prefix="udp.ewp.">
<html>

<title><fmt:message key="add"/></title>

<head>

    <script type="text/javascript">
        var basePath = "<venus:base/>";

        //---------窗体加载完成后，根据自定义配置文件初始化FCKEditor编辑器----------------------------------------------
        jQuery(document).ready(function () {
            try {
                // oFckEditor.ToolbarSet = "EWPToolbar";
                //如果是查看状态，显示只读状态的菜单栏。
                <%
                if(operationType!=null&&operationType.equals(IConstants.VIEW_DOCUMENT_OPERATION_KEY)){
                    %>
                jQuery("#myFCKeditor_READONLY_ID").css("display", "block");
                jQuery("#myFCKeditor_ID").css("display", "none");
                //减少操作
                //oFckEditor.ToolbarSet = "Readonly";
                //oFckEditor.Width = "100%";
                //height不能写成100% ,在 firefox浏览器下会出现显示异常情况
                //oFckEditor.Height = "600";
                //oFckEditor.Config["CustomConfigurationsPath"] = basePath + "/fckeditor/EWP_FckConfig.js";
                //oFckEditor.ReplaceTextarea();

                //设置其它文档元素只读
                jQuery("#title").attr('readonly', true);
                jQuery("#shortTitle").attr('readonly', true);
                jQuery("#createBy").attr('readonly', true);
                jQuery("#source").attr('readonly', true);
                jQuery("#seoKeyWord").attr('readonly', true);
                jQuery("#titelAbstract").attr('readonly', true);
                jQuery("#isComment").attr('readonly', true);
                jQuery("#recommend").attr('readonly', true);
                jQuery("#add_parent_name").attr('readonly', true);
                jQuery("#add_parent_name_select").css("display", "none");
                jQuery("#picture").attr('readonly', true);
                jQuery("#sortNum").attr('readonly', true);

                <%
            }else{
                %>
                var oFckEditor = new FCKeditor("myTextArea");
                oFckEditor.BasePath = basePath + "/fckeditor/";
                oFckEditor.ToolbarSet = "Default";
                oFckEditor.Width = "100%";
                //height不能写成100% ,在 firefox浏览器下会出现显示异常情况
                oFckEditor.Height = "600";
                oFckEditor.Config["CustomConfigurationsPath"] = basePath + "/fckeditor/EWP_FckConfig.js";
                oFckEditor.ReplaceTextarea();
                <%
            }
            %>
            } catch (e) {
                alert(e.description);
            }

            $('span.deleteicon').click(function () {
                $(this).prev('input').val('');
            });
        });

        // 获取编辑器中HTML内容
        function getEditorHTMLContents(EditorName) {
            var oEditor = FCKeditorAPI.GetInstance(EditorName);
            return(oEditor.GetXHTML(true));
        }

        // 获取编辑器中文字内容
        function getEditorTextContents(EditorName) {
            var oEditor = FCKeditorAPI.GetInstance(EditorName);
            return(oEditor.EditorDocument.body.innerText);
        }

        // 设置编辑器中内容
        function SetEditorContents(EditorName, ContentStr) {
            var oEditor = FCKeditorAPI.GetInstance(EditorName);
            oEditor.SetHTML(ContentStr);
        }


    </script>

    <script>
        function checkContent() {
            var content = getEditorHTMLContents("myTextArea");
            if (content.length > 20000) {
                alert("文本长度过长.");
                return false;
            }
            return true;
        }

        //---------新建or编辑文档---------------------------------------------------------------------
        function saveDocument_onClick(operationTypeValue) {
            if (!checkContent()) {
                return;
            }

            var documentForm = document.forms[0];
            var docContent = document.getElementById('content');
            docContent.value = getEditorHTMLContents("myTextArea");

            if ("<%=IConstants.ADD_DOCUMENT_OPERATION_KEY %>" == operationTypeValue) {
                documentForm.action = "<venus:base/>/document.do?cmd=createDocument&siteId=<%=siteID%>";
            } else if (operationTypeValue == "<%=IConstants.EDITOR_DOCUMENT_OPERATION_KEY %>") {
                documentForm.action = "<venus:base/>/document.do?cmd=editorDocument&siteId=<%=siteID%>";
            }
            if (checkAllForms()) {
                documentForm.submit();
            }
        }


        //---------关闭新增文档页面,回到上一页----------------------------------------------------
        function close_onClick() {
            //window.history.go(-1);//回退到客户端历史记录上一条。（如果上一条历史记录是其它frame的话，会变更其它frame，调用此语句的frame不变，无法变成回退操作。）
            //returnBack();//此页面会回退到上一页面.（如果上一页面是保存提交，他会重新提交url，但不会提交内容，错误。）
            window.location.href = "<venus:base/>/jsp/ewp/document/document.jsp?siteId=<%=siteID%>&docTypeID=<%=docTypeID%>";//写死回退的页面。
        }

        //---------预览文档---------------------------------------------------------------------
        function previewDocument() {
            if (checkContent()) {
                var docContent = document.getElementById('content');
                docContent.value = getEditorHTMLContents("myTextArea");

                var documentForm = document.forms[0];
                documentForm.action = "<venus:base/>/<%=site_code%>/article/preview";
                documentForm.target = "_blank";
                documentForm.submit();
                documentForm.target = "_self";
            }

        }

        var hangDoctypeDialog = null;
        jQuery(document).ready(function () {
            hangDoctypeDialog = jQuery("#referenceHangDoctype").dialog({ modal: true, height: 475, autoOpen: false, resizable: false, width: 368, position: ["center", "top"], overlay: { opacity: 0.3, background: "black" }});
            // $('.ui-dialog-titlebar-close').hide();
        });

        ///取得上级栏目
        function getParentDoctype(checkType) {
            var selectValue = jQuery("#destDocTypeIds").val();
            var originalTypes = "<%=originalDocTypeIds%>";
            originalTypes = originalTypes + "," + selectValue;
            var checkType = "CHECK";
            var refPath = "<venus:base/>/jsp/ewp/docType/docTypeRef_hang.jsp?checktype=" + checkType + "&siteId=<%=siteID%>&method=add_save_onClick&selectedValues=" + originalTypes + "&methodCallbackOk=callbackOK&methodCallbackCancel=callbackCancel";
            jQuery.get(refPath, {Action: "get", async: false}, function (data, textStatus) {
                jQuery("#referenceHangDoctype").html(data);
                jQuery('.ui-dialog-titlebar-close').hide();
                hangDoctypeDialog.dialog("open");
            });
        }

        ///取得图片路径
        function getImgPath() {
            var url = "<venus:base/>/ResourceAction.do?cmd=queryResource&resourceType=1099200400000000001&txtUrl=picture";
            getInfoDialog(url);
        }

        function add_save_onClick(idnames) {
            var idnames = eval("(" + idnames + ")");
            var docIds = idnames.docIds;
            var docTypeIds = idnames.docTypeIds;
            var chinesenames = idnames.chinesenames;
            jQuery("#destDocTypeIds").val(docTypeIds);

            jQuery("#add_parent_name").val(chinesenames);
            hangDoctypeDialog.dialog("close");
        }

        function callbackOK(idnames, method) {
            window[method](idnames);
        }

        function callbackCancel() {
            jQuery("#referenceHangDoctype").html("");
            hangDoctypeDialog.dialog("close");
        }

    </script>
</head>
<form name="form" method="post" action="<venus:base/>/document.do">
<body align="center">
<script language="javascript">
    if ('<%=operationType %>' == '<%=IConstants.ADD_DOCUMENT_OPERATION_KEY %>') {
        writeTableTop('<fmt:message key="new_page" bundle="${applicationResources}" />', '<venus:base/>/themes/<venus:theme/>/');
    }
    if ('<%=operationType %>' == '<%=IConstants.EDITOR_DOCUMENT_OPERATION_KEY %>') {
        writeTableTop('<fmt:message key="modify_page" bundle="${applicationResources}" />', '<venus:base/>/themes/<venus:theme/>/');
    }
    if ('<%=operationType %>' == '<%=IConstants.VIEW_DOCUMENT_OPERATION_KEY %>') {
        writeTableTop('<fmt:message key="view_page" bundle="${applicationResources}" />', '<venus:base/>/themes/<venus:theme/>/');
    }
</script>

<input type="hidden" name="cmd" value="">
<input type="hidden" name="id" id="id" value="<%=doc.getId()==null?"":doc.getId() %>"/>
<input type="hidden" name="docTypeID" id="docTypeID" value="<%=docTypeID %>"/>
<input type="hidden" name="destDocTypeId" id="destDocTypeId"/>
<input type="hidden" name="destDocTypeIds" id="destDocTypeIds"/>
<input type="hidden" name="status" id="status" value="<%=doc.getStatus()==null?"":doc.getStatus() %>"/>
<input type="hidden" name="permissions" id="permissions"
       value="<%=doc.getPermissions()==null?"":doc.getPermissions() %>"/>
<input type="hidden" name="isValid" id="isValid" value="<%=doc.getIsValid()==null?"":doc.getIsValid() %>"/>
<input type="hidden" name="content" id="content" value=""/>


<div id="ccParent1">
    <table class="table_div_control" width="100%">
        <tr>

            <td>
                <table align="left">
                    <tr>
                        <%
                            if (!IConstants.VIEW_DOCUMENT_OPERATION_KEY.equals(operationType)) {
                        %>
                        <td><input type="button" class="button_ellipse"
                                   onClick="javascript:saveDocument_onClick('<%=operationType %>');"
                                   value='<fmt:message key="save" bundle="${applicationResources}"/>'></td>
                        <td><input type="button" class="button_ellipse" id="viewDocButton" onclick="previewDocument()"
                                   value='<fmt:message key="preview" bundle="${applicationResources}"/>'></td>
                        <%
                            }
                        %>
                        <td><input type="button" class="button_ellipse" onClick="javascript:close_onClick()"
                                   value='<fmt:message key="close" bundle="${applicationResources}"/>'></td>
                    </tr>
                </table>
            </td>

        </tr>
    </table>
</div>

<table width="100%" height="90%" border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td>
            <fieldset>
                <legend><fmt:message key="basicproperties"/></legend>
                <div id="ccChild1">
                    <table width="100%" height="260">
                        <tr>
                            <td align="right" width="20%" nowrap><fmt:message key="doctype"/>：</td>
                            <td align="left" width="80%">
                                <%=(docType == null ? docTypeID : docType.getName())%>
                            </td>

                        </tr>
                        <tr>
                            <td align="right" width="20%" nowrap><font color="red">*</font><fmt:message key="title"
                                                                                                        bundle="${applicationResources}"/>：
                            </td>
                            <td align="left" width="80%">
                                <input id="title" name="title" type="text" class="text_field" maxLength="100"
                                       value="<%=doc.getTitle()==null?"":doc.getTitle() %>" style="width: 50%"
                                       inputName='<fmt:message key="title" bundle="${applicationResources}"/>'
                                       validate="notNull"/>
                            </td>

                        </tr>


                        <tr>
                            <!--
                                <td align="right" width="10%" nowrap><fmt:message key="tag" />：</td>
                                <td align="left" width="40%">
                                    <input name="tag" type="text" class="text_field" value="<%=doc.getTag()==null?"":doc.getTag() %>" style="width: 50%" />
                                </td>  
                                -->
                            <td align="right" width="20%" nowrap><fmt:message key="shorttitle"/>：</td>
                            <td align="left" width="80%">
                                <input id="shortTitle" name="shortTitle" class="text_field" type="text" maxLength="64"
                                       value="<%=doc.getShortTitle()==null?"":doc.getShortTitle() %>"
                                       style="width: 50%"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="right" width="20%" nowrap><fmt:message key="author"/>：</td>
                            <td align="left" width="80%">
                                <input id="createBy" name="createBy" class="text_field" maxLength="32" type="text"
                                       value="<%=doc.getCreateBy()==null?"":doc.getCreateBy() %>" style="width: 50%"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="right" width="20%" nowrap><fmt:message key="source"/>：</td>
                            <td align="left" width="80%">
                                <input id="source" name="source" class="text_field" maxLength="64" type="text"
                                       value="<%=doc.getSource()==null?"":doc.getSource() %>" style="width: 50%"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="right" width="20%" nowrap><fmt:message key="keyword"/>：</td>
                            <td align="left" width="80%">
                                <input id="seoKeyWord" name="seoKeyWord" class="text_field" maxLength="64" type="text"
                                       value="<%=doc.getSeoKeyWord()==null?"":doc.getSeoKeyWord() %>"
                                       style="width: 50%"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="right" width="20%" nowrap><fmt:message key="summary"/>：</td>
                            <td align="left" width="80%">
                                <textarea id="titelAbstract" name="titelAbstract" rows="8" cols=""
                                          class="textarea_limit_words" style="width: 50%"
                                          maxLength="200"><%=doc.getTitelAbstract() == null ? "" : doc.getTitelAbstract() %></textarea>
                            </td>
                        </tr>
                    </table>
                </div>
            </fieldset>
            <p/>
            <fieldset>
                <legend><fmt:message key="extendedproperties"/></legend>

                <div id="ccChild1">

                    <table width="100%" height="60">
                        <tr>
                            <td align="right" width="25%" nowrap>
                                <input id="isComment" name="isComment" type="checkbox"
                                       value="1"  <%=(doc.getIsComment() == null || !doc.getIsComment().equals("1")) ? "" : "checked"%>
                                       style="display:none"/>
                                <!--<fmt:message key="comments" />-->
                            </td>
                            <td align="left" width="25%">
                                <input id="recommend" name="recommend" type="checkbox"
                                       value="1"  <%=(doc.getRecommend() == null || !doc.getRecommend().equals("1")) ? "" : "checked"%>/>
                                <fmt:message key="recommend"/>
                                <input id="isShowHotWords" name="isShowHotWords" type="checkbox"
                                       value="1"  <%=(doc.getIsShowHotWords() == null || !doc.getIsShowHotWords().equals("1")) ? "" : "checked"%>/>
                                <fmt:message key="showHotWords"/>
                            </td>
                            <td align="right" width="5%" nowrap><fmt:message
                                    key="share_document_to_other_channel"/></td>
                            <td align="left" width="45%">
                                <input name="add_parent_name" id="add_parent_name"
                                       value="<%=originalDocTypeNames==null ?"" :originalDocTypeNames%>"
                                       type="text" class="text_field_reference_readonly"
                                       hiddenInputId="destDocTypeIds" readonly inputName="上级栏目"/>
                                <img id="add_parent_name_select" class="refButtonClass"
                                     src="<%=request.getContextPath() %>/themes/<venus:theme/>/images/icon/reference.gif"
                                     onClick="getParentDoctype();"/>
                            </td>
                        </tr>
                        <tr>
                            <td align="right" width="5%" nowrap><fmt:message key="picture"/>：</td>
                            <td align="left" width="45%">
                                <input id="picture" name="picture" class="text_field" maxLength="128"
                                       type="text"
                                       value="<%=doc.getPicture()==null?"":doc.getPicture() %>"/>
                                <img id="add_picture_name_select" class="refButtonClass"
                                     src="<%=request.getContextPath() %>/themes/<venus:theme/>/images/icon/reference.gif"
                                     onClick="getImgPath();"/>
                            </td>
                            <td align="right" width="5%" nowrap><fmt:message key="doctype.sort"/>：</td>
                            <td align="left" width="45%">
                                <input id="sortNum" name="sortNum" class="text_field"
                                       maxLength="4294967296" type="text"
                                       value="<%=doc.getSortNum()==null?"0":doc.getSortNum() %>"/>
                            </td>
                        </tr>
                        <%if (doc.getStatus() != null && doc.getStatus().equals(IConstants.DOC_STATUS_UNPUBLISHED)) {%>
                        <tr>
                            <td align="right" width="5%" nowrap><fmt:message key="pre_release_date"/>：</td>
                            <td align="left" width="45%">
                                             <span class="deleteable">
                                                <input name="preReleaseDate" type="text" maxLength="128"
                                                       class="text_field_reference"
                                                       inputName='<fmt:message key="pre_release_date"/>' readonly="true"
                                                       value='<%=releaseTs != null ? releaseTs : ""%>'>
                                                 <span class="deleteicon"></span>
                                            </span>
                                <img src="<venus:base/>/themes/<venus:theme/>/images/icon/reference.gif"
                                     onclick="javacript:getYearMonthDayHourMinuteSecond('preReleaseDate','<venus:base/>/');"
                                     class="img_1">
                            </td>
                            <td align="right" width="5%" nowrap></td>
                            <td align="left" width="45%">
                            </td>
                        </tr>
                        <%}%>
                        <!--
                              <tr> 
                                <td align="right" width="10%" nowrap><fmt:message key="hits" />：</td>
                                <td align="left" width="90%">
                                  <font color="red"><%=doc.getVisitCount()==null?"":doc.getVisitCount() %></font>
                                </td>
                             </tr>   
                           <tr> 
                                <td align="right" width="10%" nowrap><fmt:message key="releaseddate" />：</td>
                                <td align="left" width="90%">
                                  <input  name="publishTime" type="text" class="text_field" readonly="readonly" value="<%=doc.getPublishTime() %>"/>
                                </td>
                             </tr>  
                             -->
                    </table>
                </div>
            </fieldset>
        </td>
    </tr>
    <tr style="width:100%" height="100%">
        <td valign="top">
            <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <!-- 编辑器区域 -->
                    <td width="70%" valign="top">
                        <DIV id="myFCKeditor_ID">
                            <textarea id="myTextArea"
                                      name="myTextArea"><%=doc.getContent() == null ? "" : doc.getContent() %>
                            </textarea>
                        </DIV>
                        <DIV id="myFCKeditor_READONLY_ID" style="display:none;">
                            <%=doc.getContent() == null ? "" : doc.getContent() %>
                        </DIV>
                    </td>
                </tr>
            </table>

        </td>

    </tr>

</table>
<script language="javascript">
    writeTableBottom('<venus:base/>/themes/<venus:theme/>/');  //显示本页的页脚

    //初始化页面元素 -- 复选框
    if (<%=doc.getIsComment() %> !=
    null && "1" != "<%=doc.getIsComment() %>"
    )
    {
        document.form.elements['isComment'].checked = false;
    }
    if (<%=doc.getRecommend() %> !=
    null && "1" == "<%=doc.getRecommend() %>"
    )
    {
        document.form.elements['recommend'].checked = true;
    }
    else
    if (<%=doc.getRecommend() %> !=
    null && "0" == "<%=doc.getRecommend() %>"
    )
    {
        document.form.elements['recommend'].checked = false;
    }


</script>
<div id="iframeDialog" title=""></div>
</body>
</form>
<div id="referenceHangDoctype" title="请选择要挂接至的栏目"></div>
<form name="form_treebasic" action="" method="post">
    <input id="webModel" name="webModel" type="hidden" class="text_field" inputName="发布目录"
           value="<%=request.getContextPath()%>" readonly="true">
</form>
</html>
</fmt:bundle>
<% //表单回写
    if (request.getAttribute("writeBackFormValues") != null) {
        out.print("<script language=\"javascript\">\n");
        out.print(VoHelper.writeBackMapToForm((java.util.Map) request.getAttribute("writeBackFormValues")));
        out.print("writeBackMapToForm();\n");
        out.print("</script>");
    }
%>