<#--显示栏目信息以及子栏目的模板，如果没有子栏目则取父栏目-->
<#if result.validChildren ? has_content>

<h3>${result.name!""}<span></span></h3>
<hr / style="width:200px;">
    <#list result.validChildren as docType>
    <p><i class="glyphicon"></i><a
            href="${rc.getContextPath()}${siteCode}/articles/${docType.docTypeCode}">${docType.name!""}</a></p>
    <hr/ style="width:200px;">
    </#list>

<#elseif result.parent ?has_content>
    <#list result.parent as type>
        <#assign parent = type>
    </#list>

    <#if parent.docTypeCode = "root">
    <h3>${parent.name!""}<span></span></h3>
    <hr / style="width:200px;">
        <#list parent.validChildren as docType>
        <p><i class="glyphicon"></i><a
                href="${rc.getContextPath()}${siteCode}/articles/${docType.docTypeCode}">${docType.name!""}</a></p>
        <hr/ style="width:200px;">
        </#list>

    <#else>

    <h3>上级栏目<span></span></h3>
    <hr / style="width:200px;">
        <#list result.parent as type>
            <#if type.docTypeCode!="root">
            <p><i class="glyphicon"></i><a
                    href="${rc.getContextPath()}${siteCode}/articles/${type.docTypeCode}">${type.name!""}</a></p>
            <hr/ style="width:200px;">
            </#if>
        </#list>

    </#if>
<#else>
</#if>
