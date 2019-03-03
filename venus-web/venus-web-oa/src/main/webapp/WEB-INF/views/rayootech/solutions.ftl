<#--成功案例页面展示模板，嵌套在layout.ftl的usecasestudy布局模板中-->
<@layout.usecasestudy "${result.name}">
             <h2 class="textTitle">${result.name!""}</h2>     
              <#if   result.validChildren ?has_content>
          <#list result.validChildren as type>
                    <div class="subBox3">
                    <!--
                    <img alt="${type.name}" src="${rc.getContextPath()}/servlet/OutPutImageServlet?filename=${type.imagePath!""}">
                    -->
                    <h3><a href="${rc.getContextPath()}${siteCode}/api/${type.docTypeCode?default("")}/solution_doctypeanddocs">${type.name}</a></h3>
                      <@include_page  path="${siteCode}/api/articles/${type.id}/solution_doclist"/>
                    </div>
     </#list>
    </#if>

</@layout.usecasestudy>