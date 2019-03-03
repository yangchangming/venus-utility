<#--ITARI平台二级页面-->
<@layout.rayoo_itari_news  "${result.name!''}">
<header class="news"></header>
<section class="row features">
    <div class="col-md-3">
        <div class="page">
            <#include "rayoo_itari_docTypes.ftl">
        </div>
        <div class="page">
            <#include "rayoo_itari_contect_us.ftl">
        </div>
    </div>

    <div class="col-md-9">
        <div class="page">
            <!--暂时不提供面包屑导航功能-->
            <h3>${result.name!""}</h3>
            <hr
            / style="width:780px;">
            <@include_page path="${siteCode}/api/articles/${result.id}/rayoo_itari_news_list?pageSize=10"/>
        </div>
    </div>
</section>
</@layout.rayoo_itari_news>
