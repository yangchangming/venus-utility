<#--ITARI网站的文章块，不包括其他内容-->
<@layout.rayoo_itari_aboutus>
<header class="about"></header>
<section class="row features">
    <div class="col-md-3">
        <div class="page">
            <@include_page path="${siteCode}/api/root/rayoo_itari_docTypes"/>
        </div>
        <div class="page">
            <#include "rayoo_itari_contect_us.ftl">
        </div>
    </div>
    <div class="col-md-9">
        <div class="page">
            <div class="pagination-centered">
                <h2>${result.title!""}</h2>
            </div>
            <hr style="width:780px;"/>
            <h6> ${result.publishTime!""}      作者：${result.createBy!""}      
                <#if result.source?has_content>
                    <a href='${result.source!""}' target=_blank>来源</a>
                </#if>
            </h6>
            <br/>

            <p>
            ${result.content!""}
            </p>
        </div>
        <@layout.gap_ewp_document_posts_body_itari websiteCode = "${siteCode}" postDocID = "${(result.id)!''}"/>
    </div>
</section>
</@layout.rayoo_itari_aboutus>
