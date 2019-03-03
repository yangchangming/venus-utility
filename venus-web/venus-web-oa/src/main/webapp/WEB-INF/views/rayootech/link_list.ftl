<h2 class="menuTitle2">${result.name!""}</h2>
<ul>
    <#list result.documents as document>
        <li><a href="${rc.getContextPath()}${siteCode}/article/${document.id}/about_us_page" class="cur">${document.title}</a></li>
    </#list>
</ul>
