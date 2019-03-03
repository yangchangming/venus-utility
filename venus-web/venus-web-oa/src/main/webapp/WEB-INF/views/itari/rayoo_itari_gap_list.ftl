<#--GAP平台组件展示列表-->
<#import  "/views/${siteCode}/rayoo_itari_gap_paging.ftl" as ahxuPage>
<form action="${rc.getContextPath()}${siteCode}/api/articles" id="${result.docTypeId}" name="${result.docTypeId}" method="post">
    <input type="hidden" id="pageSize" name="pageSize" value="${result.pageResults.pageSize}"/>
    <input type="hidden" id="docTypeId" name="docTypeId" value="${docTypeId!docTypeId}"/>
    <input type="hidden" id="view" name="view" value="rayoo_itari_gap_list"/>

    <div id="content${result.docTypeId}">
    <#list result.pageResults.results as doc>
        <hr/>
        <div class="row">
            <div class="col-md-3" style=" margin-right:30px;"><img src="${rc.getContextPath()}${doc.picture!""}"></div>
            <h5><a href="${rc.getContextPath()}${siteCode}/article/${doc.id}">
                <#if doc.title?length lt 25>
                ${doc.title}
                <#else>
                ${doc.title[0..24]}...
                </#if></a></h5>

            <p class="small">${doc.titelAbstract!""}</p>
            <dt>>><a href="${rc.getContextPath()}${siteCode}/article/${doc.id}">详细内容</a></dt>

        </div>
    </#list>
        <hr/>
        <br/>

        <div>
        <#if  result.pageResults.results?has_content>
            <@ahxuPage.pageTR  formId="${result.docTypeId}" pageResults=result.pageResults colSpan=1/>
        </#if>
        </div>
    </div>
</form> 
