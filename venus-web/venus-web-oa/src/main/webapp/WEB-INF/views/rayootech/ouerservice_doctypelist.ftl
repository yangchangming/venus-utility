<#--我们的服务栏目以及子栏目展示模板，此模板嵌套在layout.ftl中得 useouerservice中，作为我们的服务左侧栏目导航-->
   <h2 class="menuTitle">${result.name!""}</h2>
      <ul>
       <#if   result.validChildren ?has_content>
            <#list  result.validChildren as type>
               <li><a href="${rc.getContextPath()}${siteCode}/api/${type.docTypeCode?default("")}/ouerservice_doctype" target="new">${type.name}</a></li>
             </#list>
        </#if>     
    </ul>