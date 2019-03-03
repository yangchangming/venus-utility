  <#-- 公司新闻首页展示模板，嵌套在welcome.ftl，获得公司新闻栏目下的文章，调用newstitles.ftl进行文章标题展示-->
  <h2 ><a href="${rc.getContextPath()}${siteCode}/api/${result.docTypeCode}/news" class="more">&gt;更多</a>${result.name}</h2>
  <ul>
      <@include_page path="${siteCode}/api/articles/${result.id}/newstitles"/>
  </ul>