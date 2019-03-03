<#-- ITARI网站平台 框架模板-->
<#macro rayoo_itari currentLocation="itari" itariClass="home">
<!DOCTYPE html>
<html lang="en" ng-app="postsApp">
<head>
    <#include ".${siteCode}/include/head.ftl">
</head>
<body class="${itariClass}" style="background-color:#f0f0f0;" ng-controller="PostListCtrl">
    <#include ".${siteCode}/include/header.ftl">
<article class="container">
    <#nested />
</article>
<footer>
    <#include  ".${siteCode}/include/footer.ftl">
</footer>
    <@layout.gap_ewp_document_posts_init_itari websiteCode = "${siteCode}" postDocID = "${(result.id)!''}"/>
</body>
</html>
</#macro>

<#--ITARI网站首页框架模板-->
<#macro rayoo_itari_index currentLocation="瑞友科技IT应用研究院" itariClass="home">
    <@layout.rayoo_itari currentLocation itariClass>
        <#nested />
    </@layout.rayoo_itari>
</#macro>

<#--ITARI网站二级页面框架模板-->
<#macro rayoo_itari_news currentLocation="itari" itariClass="page-content">
    <@layout.rayoo_itari currentLocation itariClass>
        <#nested />
    </@layout.rayoo_itari>
</#macro>

<#--ITARI网站二级页面框架模板-->
<#macro rayoo_itari_aboutus currentLocation="itari" itariClass="page-content">
    <@layout.rayoo_itari currentLocation itariClass>
        <#nested />
    </@layout.rayoo_itari>
</#macro>

<#--ITARI网站三级页面框架模板-->
<#macro rayoo_itari_detail currentLocation="itari" itariClass="page-content">
    <@layout.rayoo_itari  currentLocation itariClass>
        <#nested />
    </@layout.rayoo_itari>
</#macro>

<#--ITARI网站技术方案二级页面框架模板-->
<#macro rayoo_itari_program currentLocation="itari" itariClass="page-content">
    <@layout.rayoo_itari  currentLocation itariClass>
        <#nested />
    </@layout.rayoo_itari>
</#macro>

<#--ITARI-GAP平台二级页面框架模板-->
<#macro rayoo_itari_gap currentLocation="gap" itariClass="page-content">
    <@layout.rayoo_itari  currentLocation itariClass>
        <#nested />
    </@layout.rayoo_itari>
</#macro>

<#--ITARI-找不到模板的公共处理模板-->
<#macro rayoo_itari_noexist currentLocation="noexist" itariClass="">
    <@layout.rayoo_itari  currentLocation itariClass>
        <#nested />
    </@layout.rayoo_itari>
</#macro>