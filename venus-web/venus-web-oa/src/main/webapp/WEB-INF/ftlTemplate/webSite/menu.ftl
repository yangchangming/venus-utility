<#--导航栏目菜单显示模板-->
<ul>
    <li><a href="${rc.getContextPath()}${siteCode}">首页</a></li>
    <#list  result as type>
        <li><a href="${rc.getContextPath()}${siteCode}/articles/${type.docTypeCode}">${type.name}</a></li>
    </#list>
</ul>