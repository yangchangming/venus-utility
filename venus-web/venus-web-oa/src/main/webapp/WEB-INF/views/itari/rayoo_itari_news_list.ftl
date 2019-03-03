<#--通用文章列表展示模板，提供给新闻中心栏目调用-->
<#import  "/views/${siteCode}/paging.ftl" as ahxuPage>
<form action="${rc.getContextPath()}${siteCode}/api/articles" id="infoForm" name="infoForm" method="post">
    <input type="hidden" id="pageSize" name="pageSize" value="${result.pageResults.pageSize!pageSize}"/>
    <input type="hidden" id="docTypeId" name="docTypeId" value="${docTypeId!docTypeId}"/>
    <input type="hidden" id="view" name="view" value="rayoo_itari_news_list"/>

    <div id="content">
        <table class="table table-striped">
        <#list result.pageResults.results as doc>
            <tr>
                <td>
                    <div style="float:left;"><a href="${rc.getContextPath()}${siteCode}/article/${doc.id}">
                        <#if   doc.title?length   lt   25>
                        ${doc.title}
                        <#else>
                        ${doc.title[0..24]}...
                        </#if>
                    </a></div>
                    <div class="pull-right"> ${doc.publishTime?string('yyyy-MM-dd')}</div>
                </td>
            </tr>
        </#list>
        </table>
        <div>
        <#if result.pageResults.results?has_content>
                                     <@ahxuPage.pageTR formId="infoForm" pageResults=result.pageResults colSpan=1/>
                                  </#if>
        </div>
    </div>
</form>
