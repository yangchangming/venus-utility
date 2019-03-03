<#--ITARI平台导航菜单-->
<#list  result as type>
<li class="">
    <a href="${rc.getContextPath()}${siteCode}/articles/${type.docTypeCode}">${type.name!""}</a>
</li>
</#list>
