<@layout.rayoo_itari_gap  "GAP平台">
<header class="respage-Program"></header>
<section class="row features">
    <div class="col-md-9">
        <div class="page2">
            <a href="${rc.getContextPath()}${siteCode}/article/1099104000000000053">
                <header class="gap"></header>
            </a>
        </div>
    <#--GAP平台介绍，包括GAP-Tools,INTERNET,APPS-->
        <div class="page">
            <@include_page  path="${siteCode}/api/gap/rayoo_itari_gap_docTypes"/>
        </div>
    </div>
    <div class="col-md-3">
    <#--右侧的新闻动态-->
        <div class="page">
            <@include_page  path="${siteCode}/api/news/rayoo_itari_docTypes"/>
        </div>
    <#--右侧的最新专题-->
        <div class="page">
            <@include_page  path="${siteCode}/api/zxzt/rayoo_itari_gap_docTypeAndTitles"/>
        </div>
    <#--右侧的成功案例-->
        <div class="page">
            <@include_page  path="${siteCode}/api/casestudy/rayoo_itari_gap_docTypeAndTitles"/>
        </div>
    </div>
</section>


</@layout.rayoo_itari_gap>
