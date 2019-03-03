<#--GAP平台导航菜单-->
<#list  result as type>
    <li class="dropdown">
        <#if type.validChildren?has_content>
            <a href="${rc.getContextPath()}${siteCode}/articles/${type.docTypeCode}" class="dropdown-toggle" data-toggle="dropdown">${type.name!""}<b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <#list  type.validChildren as child>
                    <li><a href="${rc.getContextPath()}${siteCode}/articles/${child.docTypeCode}">${child.name}</a></li>
                    </#list>
                </ul>
        <#else>
            <a href="${rc.getContextPath()}${siteCode}/articles/${type.docTypeCode}">${type.name!""}<b class="caret"></b></a>
        </#if>
    </li>
</#list>
