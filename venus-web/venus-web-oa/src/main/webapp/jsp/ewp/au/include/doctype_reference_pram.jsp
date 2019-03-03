<%@ page contentType="text/html; charset=UTF-8" %>
<form name="form_treebasic" method="post">
    <input id="divid" name="divid" type="hidden" class="text_field" inputName="节点层id" value="" readonly="true">
    <input id="rootFlag" name="rootFlag" type="hidden" class="text_field" inputName="是否有根节点" value="" readonly="true">
    <input id="webModel" name="webModel" type="hidden" class="text_field" inputName="发布目录" value="<%=request.getContextPath()%>" readonly="true">
    <input id="tree_href" name="tree_href" type="hidden" class="text_field" value="<%=request.getContextPath()%>/document.do?cmd=listDocumentsByType" readonly="true">
    <input id="tree_onclick" name="tree_onclick" type="hidden" class="text_field" value="doClick(this);changeHeight();setDocTypeForSearch(this);" readonly="true">
    <input id="tree_param" name="tree_param" type="hidden" class="text_field" value="docTypeID" readonly="true">
    <input id="tree_target" name="tree_target" type="hidden" class="text_field" value="documentList" readonly="true">
    <input id="site_id" name="site_id" type="hidden" class="text_field" value="${sessionScope.site_id}" readonly="true">
    <input id="reference_site_id" name="site_id" type="hidden" class="text_field" value="${sessionScope.site_id}" readonly="true">
</form>