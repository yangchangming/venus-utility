<#-- 文章分页显示模板，主要提供给需要多页显示文章时的模板调用-->
<#macro pageTR formId pageResults colSpan>
<script lanuage="Javascript">
    function changePage(event, page) {
        if (page != 0) {
            var pageNoObj = document.getElementById('pageNo').value;
            pageNoObj = parseInt(pageNoObj) + page;
            document.getElementById('pageNo').value = pageNoObj;
        }
        var form = $(event).parents("form");
        ajaxNextPage(form);
        //document.getElementById('${formId}').submit();
    }

    function ajaxNextPage(frm) {
        var pageNo = document.getElementById('pageNo').value;
        var form = frm;  //jQuery(${formId});
        var action = form.prop("action");
        var docId = form.prop("id").value;
        var view = form.prop("view").value;
        var pageSize = form.prop("pageSize").value;
        var pageNo = form.prop("pageNo").value;
        var url = action + "/" + docId + view + "?pageSize=" + pageSize + "&pageNo=" + pageNo + " #content>*";
        form.find('#content').load(url);
    }
</script>
<div class="row">
    <div class="col-md-6 pull-left">
        共${pageResults.totalCount}条记录 / 共${pageResults.pageCount}页 / 每页${pageResults.pageSize}条记录
    </div>
    <div class="pull-right">
        <input class="btn-xs" type="button" value="上一页" onclick="changePage(this, -1)"
               <#if pageResults.currentPage==1>disabled</#if> >
        <input class="btn-xs" type="button" value="下一页" onclick="changePage(this, +1)"
               <#if pageResults.currentPage==pageResults.pageCount>disabled</#if> >
        第<SELECT class="col-md-1 input-mini" id="pageNo" name="pageNo" onchange="changePage(this,0)">
        <#list 1..pageResults.pageCount as page>
            <OPTION value="${page}" <#if pageResults.currentPage==page> selected</#if>>${page}</OPTION>
        </#list>
    </SELECT>页
    </div>
</div>
</#macro>