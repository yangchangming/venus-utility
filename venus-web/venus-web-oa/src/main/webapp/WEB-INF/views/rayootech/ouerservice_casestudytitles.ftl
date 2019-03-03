<#-- 我们的服务模板中右侧成功案例嵌套展示模板，
   进行成功案例栏目名称以及此栏目下文章标题的展示，此模板嵌套至layout.ftl 中得useouerservice
-->
<h2 class="title1"><span>${result.name!""}</span></h2>
<ul>
        <@include_page path="${siteCode}/api/articles/${result.id}/doctypedoctitles"/>
</ul>
<div class="readMore"><a href="${rc.getContextPath()}${siteCode}/api/${result.docTypeCode}/casestudy">更多</a> </div>