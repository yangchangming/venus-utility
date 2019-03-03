<#--通用文章列表展示模板，提供给新闻中心栏目调用-->
<#import  "/views/${siteCode}/paging.ftl" as ahxuPage>
<form action="${rc.getContextPath()}${siteCode}/api/articles" id="infoForm" name="infoForm" method="post">
    <input type="hidden" id="pageSize" name="pageSize" value="${result.pageResults.pageSize!pageSize}"/>
    <input type="hidden" id="docTypeId" name="docTypeId" value="${docTypeId!docTypeId}"/>
    <input type="hidden" id="view" name="view" value="rayoo_itari_program_list"/>

    <div id="content">

    <#list result.pageResults.results as doc>
        <hr
        / style="width:760px;">

        <div class="row">
            <h4><a href="${rc.getContextPath()}${siteCode}/article/${doc.id}">
                <#if   doc.title?length   lt   25>
                ${doc.title}
                <#else>
                ${doc.title[0..24]}...
                </#if></a></h4>

            <p class="small">${doc.titelAbstract!""}</p>

            <div style="float:right;">
                <dt>>><a href="${rc.getContextPath()}${siteCode}/article/${doc.id}">详细内容</a></dt>
            </div>
        </div>
    </#list>
    <#if  result.pageResults.results?size&gt;0>
        <hr
        / style="width:760px;">
    </#if>

        <div>
        <#if  result.pageResults.results?has_content>
                                     <@ahxuPage.pageTR  formId="infoForm" pageResults=result.pageResults colSpan=1/>
                                </#if>
        </div>
    </div>
</form>
