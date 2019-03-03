     <#--我们的服务栏目以及子栏目显示 ，此模板嵌套在welcom.ftl-->
    <h2 class="blueLinkT"><a href="${rc.getContextPath()}${siteCode}/api/${result.docTypeCode}/ouerservices">${result.name!""}</a></h2>
            <ul>
               <#if    result.validChildren ?has_content>
            <#list  result.validChildren as type>
                <#if type.isValid == "1">
                    <li><a href="${rc.getContextPath()}${siteCode}/api/${type.docTypeCode?default("")}/ouerservice_doctype" target="new">${type.name}</a></li>
                </#if>
             </#list>
        </#if>     
            </ul>