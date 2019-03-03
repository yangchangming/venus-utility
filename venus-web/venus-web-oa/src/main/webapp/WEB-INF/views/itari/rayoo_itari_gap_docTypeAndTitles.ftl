<#-- 公司新闻首页展示模板，嵌套在welcome.ftl，获得公司新闻栏目下的文章，调用newstitles.ftl进行文章标题展示-->
<h3><a href="${rc.getContextPath()}${siteCode}/articles/${result.docTypeCode}" class="more">${result.name}</a></h3>
<hr /  style="width:200px;">
<@include_page path="${siteCode}/api/articles/${result.id}/rayoo_itari_gap_newstitles"/>
   