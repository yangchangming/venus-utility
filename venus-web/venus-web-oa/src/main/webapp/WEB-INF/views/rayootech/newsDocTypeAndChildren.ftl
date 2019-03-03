 <#--新闻中心栏目以及子栏目展示进行展示模板-->
   <h2 class="menuTitle"><a href="${rc.getContextPath()}${siteCode}/api/${result.docTypeCode?default("")}/news">${result.name!""}</a></h2>
      <ul>
       <#if  result.validChildren ?has_content>
            <#list   result.validChildren as type>
               <li><a href="${rc.getContextPath()}${siteCode}/api/${type.docTypeCode?default("")}/news">${type.name}</a></li>
             </#list>
        </#if>     
    </ul>