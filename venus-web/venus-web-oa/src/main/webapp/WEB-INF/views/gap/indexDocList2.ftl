<#-- gap网站首页栏目以及栏目下文章标题 布局2-->
<h3><img src="${rc.getContextPath()}/images/ewp/website/gap/gap06.png">${result.name ! ""}</h3>
<hr/>
<ul class="nav list-group">
<@include_page path="${siteCode}/api/articles${result.id}/indexDocTypeDocTitles"/>
</ul>
<p><a class="pull-right"
      href="${rc.getContextPath()}${siteCode}/api/${result.docTypeCode}/commonDocTypeDocList2"">更多</a>
</p>
