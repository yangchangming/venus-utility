 <#--成功案例 文章标题列表展示模板，此模板由casestudy.ftl进行嵌套调用-->
 <#list result.pageResults.results as doc>
     <li>
         <a href="${rc.getContextPath()}${siteCode}/article/${doc.id}">
            <#if  doc.title?length   lt   25>
                    ${doc.title}
              <#else>
                    ${ doc.title[0..24]}...
              </#if>
              ${doc.createTime?string('yyyy-MM-dd')}
           </a>
     </li>
</#list>