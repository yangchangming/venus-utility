  <#--媒体新闻首页展示模板，主要显示当前栏目名称以及此栏目下的文章标题，此模板嵌套在welcome.ftl-->
  <h2 ><a href="${rc.getContextPath()}${siteCode}/api/${result.docTypeCode}/news" class="more">&gt;更多</a>${result.name}</h2>
  <ul>
      <@include_page path="${siteCode}/api/articles/${result.id}/medianewstitles"/>
  </ul>