     <#--强档推荐栏目以及子栏目显示 ，挂接共享至此栏目的栏目确保无子栏目，直接包含文档，此模板嵌套在welcom.ftl-->
    <h2 class="blueLinkT"><a href="${rc.getContextPath()}${siteCode}/api/${result.docTypeCode}/news">${result.name!""}</a></h2>
            <ul>
               <#if result.validChildren ?has_content>
            <#list  result.validChildren as type>
                <#if type.isValid == "1">
                   <li><a href="${rc.getContextPath()}${siteCode}/api/${type.docTypeCode?default("")}/news">${type.name}</a></li>
                </#if>
             </#list>
        </#if>     
            </ul>