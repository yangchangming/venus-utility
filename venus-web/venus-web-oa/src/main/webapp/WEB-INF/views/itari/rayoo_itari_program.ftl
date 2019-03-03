<@layout.rayoo_itari_program  "${result.name!''}">
<header class="respage-gap"></header>
<section class="row features">
    <div class="col-md-3">
        <div class="page">
            <#include  "rayoo_itari_docTypes.ftl">
        </div>
        <div class="page">
            <#include  "rayoo_itari_contect_us.ftl">
        </div>
    </div>
    <div class="col-md-9">
        <div class="page">
            <h3>${result.name!""}
                <!--暂时不提供面包屑导航功能-->
                <#--<div class="pull-right">当前位置: 首页 > 技术方案 </div>-->
            </h3>
            <@include_page path="${siteCode}/api/articles/${result.id}/rayoo_itari_program_list?pageSize=10"/>
        </div>
    </div>
</section>

</@layout.rayoo_itari_program>
