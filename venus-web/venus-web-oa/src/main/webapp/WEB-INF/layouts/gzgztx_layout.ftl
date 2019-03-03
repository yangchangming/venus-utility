<#--  贵州省团校官网框架模板  -->
<#macro gzgztx currentLocation="欢迎访问贵州省团校网站" gzgztxClass="home">
<!DOCTYPE html>
<html lang="en" ng-app="postsApp">
    <#include ".${siteCode}/include/head.ftl">
<body leftmargin="0" topmargin="0" bgcolor="#c2d6f4">
    <#include ".${siteCode}/include/header.ftl">
<article class="container">
    <#nested />
</article>
    <#include  ".${siteCode}/include/footer.ftl">
</body>
</html>
</#macro>


<#--  默认首页布局模板  -->
<#macro gzgztx_index currentLocation="欢迎访问贵州省团校网站" gzgztxClass="home">
    <@layout.gzgztx currentLocation gzgztxClass>
        <#nested />
    </@layout.gzgztx>
</#macro>

<#--  学校概况布局模板  -->
<#macro gzgztx_school_overview currentLocation="欢迎访问贵州省团校网站" gzgztxClass="home">
    <@layout.gzgztx currentLocation gzgztxClass>
        <#nested />
    </@layout.gzgztx>
</#macro>

<#--  培训动态布局模板  -->
<#macro gzgztx_trainning currentLocation="欢迎访问贵州省团校网站" gzgztxClass="home">
    <@layout.gzgztx currentLocation gzgztxClass>
        <#nested />
    </@layout.gzgztx>
</#macro>

<#--  管理机构布局模板  -->
<#macro gzgztx_organization currentLocation="欢迎访问贵州省团校网站" gzgztxClass="home">
    <@layout.gzgztx currentLocation gzgztxClass>
        <#nested />
    </@layout.gzgztx>
</#macro>

<#--  党建工作布局模板  -->
<#macro gzgztx_party_building currentLocation="欢迎访问贵州省团校网站" gzgztxClass="home">
    <@layout.gzgztx currentLocation gzgztxClass>
        <#nested />
    </@layout.gzgztx>
</#macro>

<#--  校园文化布局模板  -->
<#macro gzgztx_school_cluture currentLocation="欢迎访问贵州省团校网站" gzgztxClass="home">
    <@layout.gzgztx currentLocation gzgztxClass>
        <#nested />
    </@layout.gzgztx>
</#macro>

<#--  教学科研布局模板  -->
<#macro gzgztx_scientific currentLocation="欢迎访问贵州省团校网站" gzgztxClass="home">
    <@layout.gzgztx currentLocation gzgztxClass>
        <#nested />
    </@layout.gzgztx>
</#macro>

<#--  文章详情布局模板  -->
<#macro gzgztx_detail currentLocation="欢迎访问贵州省团校网站" gzgztxClass="home">
    <@layout.gzgztx currentLocation gzgztxClass>
        <#nested />
    </@layout.gzgztx>
</#macro>

