<#--通用文章列表展示模板，提供给新闻中心栏目调用-->
<#import  "paging.ftl" as ahxuPage>
<form action="${rc.getContextPath()}${siteCode}/api/articles" id="infoForm" name="infoForm" method="post">
    <input type="hidden" id="pageSize" name="pageSize" value="${result.pageResults.pageSize}"/>
    <input type="hidden" id="id" name="id" value="${result.docTypeId}"/>
    <input type="hidden" id="view" name="view" value="/commonNewsList"/>

    <div id="content">
    <#list result.pageResults.results as doc>
        <div class="row">
            <div class="col-md-6">
                <a href="${rc.getContextPath()}${siteCode}/article/${doc.id}" target="_blank">
                    <i class="glyphicon-arrow-right"></i>
                    <#if   doc.title?length   lt   25>
                    ${doc.title}
                    <#else>
                    ${doc.title[0..24]}...
                    </#if>  </a>
            </div>
            <div class="pull-right">${doc.createTime?string('yyyy-MM-dd')}</div>
        </div>
        <br/>
    </#list>
        <hr/>
        <div class="row">
            <div class="col-md-9">
            <#if result.pageResults.results?has_content>
                                     <@ahxuPage.pageTR formId="infoForm" pageResults=result.pageResults colSpan=1/>
                                  </#if>
            </div>
        </div>
    </div>
</form>        