<#--成功案例页面展示模板，嵌套在layout.ftl的usecasestudy布局模板中-->
<@layout.usecasestudy "${result.name}">
      <h2 class="textTitle">${result.name!""}</h2>
                <ul class="rightLi">
                 <@include_page  path="${siteCode}/api/articles/${result.id}/casestudy_doclist"/>
                </ul>
</@layout.usecasestudy>