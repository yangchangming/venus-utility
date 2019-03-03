<#--通用文章列表分页展示模板-->
<script lanuage="Javascript">
    function changePage(event, page) {
        if (page != 0) {
            var pageNoObj = document.getElementById('pageNo').value;
            pageNoObj = parseInt(pageNoObj) + page;
            document.getElementById('pageNo').value = pageNoObj;
        }
        var form = $(event).parents("form");
        ajaxNextPage(form);
    }

    function ajaxNextPage(frm) {
        var form = frm;
        var action = form.prop("action");
        var docId = form.prop("docTypeId").value;
        var pageSize = form.prop("pageSize").value;
        var pageNo = form.prop("pageNo").value;
        var url = action + "/" + docId + "?pageSize=" + pageSize + "&pageNo=" + pageNo + " #content>*";
        form.find('#content').load(url);
    }
</script>
<form action="${rc.getContextPath()}${siteCode}/api/articles/" id="infoForm" name="infoForm" method="post">
    <input type="hidden" id="pageSize" name="pageSize" value="${result.pageResults.pageSize!pageSize}"/>
    <input type="hidden" id="docTypeId" name="docTypeId" value="${docTypeId!docTypeId}"/>

    <div id="content" class="ewp_paging_content">
        <hr class="ewp_paging_hr_top"/>
    <#if result.pageResults.results?has_content>
        <#list result.pageResults.results as doc>
            <div class="ewp_paging_row">
                <div class="left_div">
                    <a href="${rc.getContextPath()}${siteCode}/article/${doc.id}" target="_blank">
                        <i class="glyphicon glyphicon-arrow-right"></i>
                        <#if   doc.title?length   lt   25>
                        ${doc.title}
                        <#else>
                        ${doc.title[0..24]}...
                        </#if>  </a>
                </div>
                <div class="right_div">${doc.createTime?string('yyyy-MM-dd')}</div>
            </div>
            <br/>
        </#list>
    <#else>
        <div class="ewp_paging_row">
            <div class="left_div">暂无内容!</div>
        </div>
        <br/>
    </#if>
        <hr class="ewp_paging_hr_bottom"/>
        <div class="ewp_paging_row">
        <#if result.pageResults.results?has_content>
            <div class="row">
                <div class="left_div">
                    共${result.pageResults.totalCount}条记录 / 共${result.pageResults.pageCount}页 /
                    每页${result.pageResults.pageSize}条记录
                </div>
                <div class="right_div">
                    <input class="btn-xs" type="button" value="上一页" onclick="changePage(this, -1)"
                           <#if result.pageResults.currentPage==1>disabled</#if> >
                    <input class="btn-xs" type="button" value="下一页" onclick="changePage(this, +1)"
                           <#if result.pageResults.currentPage==result.pageResults.pageCount>disabled</#if> >
                    第<SELECT class="col-md-1 input-mini" id="pageNo" name="pageNo" onchange="changePage(this, 0)">
                    <#list 1..result.pageResults.pageCount as page>
                        <OPTION value="${page}" <#if result.pageResults.currentPage==page>
                                selected</#if>>${page}</OPTION>
                    </#list>
                </SELECT>页
                </div>
            </div>
        </#if>
        </div>
    </div>
</form>