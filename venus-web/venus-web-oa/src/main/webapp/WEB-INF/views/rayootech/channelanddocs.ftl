 <#-- 成功案例首页展示模板，嵌套在welcome.ftl-->
  <h2 class="blueLinkT"><a href="${rc.getContextPath()}${siteCode}/api/${result.docTypeCode}/casestudy">${result.name}</a></h2>
            <ul>
               <@include_page path="${siteCode}/api/articles/${result.id}/doctypedoctitles"/>
            </ul>