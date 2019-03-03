 <#--解决方案文章标题列表展示模板，此模板由solutions.ftl进行嵌套调用-->
 <#list result.pageResults.results as doc>
          <p>   
          <!--
          <img alt="${doc.title}" src="${rc.getContextPath()}/servlet/OutPutImageServlet?filename=${doc.picture!""}">
          -->
             <a href="${rc.getContextPath()}${siteCode}/article/${doc.id}" target="_blank">
                <#if  doc.title?length   lt   25>
                        ${doc.title}
                  <#else> 
                        ${ doc.title[0..24]}...
                  </#if>  
                  ${doc.createTime?string('yyyy-MM-dd')}
               </a>
     </p>
</#list>