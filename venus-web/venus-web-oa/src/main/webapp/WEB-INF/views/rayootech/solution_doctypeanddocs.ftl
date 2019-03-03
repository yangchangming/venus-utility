 <#--解决方案栏目以及子栏目展示进行展示模板，比如此模板嵌套在layout.ftl的usecasestudy中-->
 <@layout.usecasestudy "${result.name}">
  <div class="subBox3">
   <h3 class="menuTitle">${result.name!""}</h3>
      <ul>
         <@include_page  path="${siteCode}/api/articles/${result.id}solution_doclist"/>
    </ul>
     </div>
 </@layout.usecasestudy>