<#-- 文章分页显示模板，主要提供给需要多页显示文章时的模板调用-->
<#macro pageTR formId pageResults colSpan>
<script language="Javascript">
    function changePage(event, page) {
        if (page != 0) {
            var pageNoObj = document.getElementById('pageNo').value;
            pageNoObj = parseInt(pageNoObj) + page;
            document.getElementById('pageNo').value = pageNoObj;
        }
        var $form = $(event).parents("form");
        ajaxNextPage($form);
    }

    function changeNumPage(event,page){
        document.getElementById('pageNo').value = page;
        var $form = $(event).parents("form");
        ajaxNextPage($form);
    }

    function ajaxNextPage(frm) {
        var action = frm.prop("action");
        var view = frm.prop("view").value;
        var docId = frm.prop("docTypeId").value;
        var pageSize = frm.prop("pageSize").value;
        var pageNo = frm.prop("pageNo").value;
        var url = action + "/" + docId + "/" + view + "?pageSize=" + pageSize + "&pageNo=" + pageNo + " #content" + docId +">*";
        frm.find('#content' + docId).load(url);
    }
</script>

    <#assign currentPage=pageResults.currentPage>
    <#assign pageCount=pageResults.pageCount>
    <#assign isPreDisable=currentPage==1>
    <#assign isNextDisable=currentPage==pageCount>
<input type="hidden"  id="pageNo" name="pageNo" value="${currentPage}"/>
<div class="row">

    <div class="pull-right">
        <ul class="pagination">

            <#if isPreDisable>
                <li><a href="javascript:void(0);">&laquo;</a></li>
            <#else>
                <li><a href="javascript:void(0);" onclick="changePage(this, -1)">&laquo;</a></li>
            </#if>

            <#if pageCount <= 10>
                <#list 1..pageCount as page>
                    <#if page == currentPage>
                        <li><a href="javascript:void(0);">${page}</a></li>
                    <#else>
                        <li><a href="javascript:void(0);" onclick="changeNumPage(this, ${page})">${page}</a></li>
                    </#if>
                </#list>
            <#elseif pageCount - currentPage < 10>
                <#list pageCount-9..pageCount as page>
                    <#if page == currentPage>
                        <li><a href="javascript:void(0);">${page}</a></li>
                    <#else>
                        <li><a href="javascript:void(0);" onclick="changeNumPage(this, ${page})">${page}</a></li>
                    </#if>
                </#list>
            <#else>
                <#list currentPage - 4..currentPage + 2 as page>
                    <#if page == currentPage>
                        <li><a href="javascript:void(0);">${page}</a></li>
                    <#else>
                        <li><a href="javascript:void(0);" onclick="changeNumPage(this, ${page})">${page}</a></li>
                    </#if>
                </#list>
                ...
                <li><a href="javascript:void(0);" onclick="changeNumPage(this, ${pageCount - 1})">${pageCount - 1}</a></li>
                <li><a href="javascript:void(0);" onclick="changeNumPage(this, ${pageCount})">${pageCount}</a></li>
            </#if>

            <#if isNextDisable>
                <li><a href="javascript:void(0);">&raquo;</a></li>
            <#else>
                <li><a href="javascript:void(0);" onclick="changePage(this, +1)">&raquo;</a></li>
            </#if>
        </ul>
    </div>
</div>
</#macro>