 <#--ITARI栏目主页的左上角的文章列表模板，供about_us.ftl调用-->
 <#list result.pageResults.results as doc>
         <li>
             <a href="${rc.getContextPath()}${siteCode}/article/${doc.id}" class="cur">
                <#if  doc.title?length   lt   25>
                        ${doc.title}
                  <#else> 
                        ${ doc.title[0..24]}...
                  </#if>  
               </a>
      </li>
</#list>