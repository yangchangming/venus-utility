<#--成功案例文章展示模板，采用layout.ftl布局模板 usecasestudy 进行数据填充-->
<@layout.usecasestudy "${result.title}">
    <h3>${result.title!""}</h3>
         <p>
             ${result.content!""}
         </p>
</@layout.usecasestudy>