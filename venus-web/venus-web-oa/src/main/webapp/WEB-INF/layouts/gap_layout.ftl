<#-- gap平台 框架模板-->
<#macro gap currentLocation="gap">
<!DOCTYPE html>
<html lang="en" ng-app="postsApp">
    <#include ".${siteCode}/include/head.ftl">
<body>
    <#include ".${siteCode}/include/header.ftl">
<div class="container">
    <#nested />
</div>
<!-- /container -->
<hr/>
    <#include  ".${siteCode}/include/footer.ftl">
</body>
</html>
</#macro>

<#--新闻中心布局模板-->
<#macro gaplayout currentLocation="GAP">
    <@layout.gap  currentLocation>
    <img src="${rc.getContextPath()}/images/ewp/website/gap/banner.jpg"/>
    <hr/>
    <div class="row">
        <div class="col-md-10">
            <#nested />
        </div>
    </div>

    <hr/>

    <div class="row">
        <div class="col-md-9">
            <@include_page path="${siteCode}/api/wsmxzGAPpt/indexWhySelectGap"/>
        </div>
        <div class="col-md-3">
            <div class="row">
                <div class="col-md-3"><img src="${rc.getContextPath()}/images/ewp/website/gap/help.png"/></div>
            </div>
            <div class="row">
                <div class="col-md-3">
                    <p>如果你有任何意见，请给我发邮件给我们，邮箱地址：<a href="#">gap@ufida.com.cn</a></p>
                </div>
            </div>
        </div>
    </div>
    </@layout.gap>
</#macro>

<#--gap默认首页布局模板-->
<#macro gap_index currentLocation="GAP WebSite">
    <@layout.gap  currentLocation>
    <img src="${rc.getContextPath()}/images/ewp/website/gap/banner.jpg"/>
    <hr/>

    <div class="row">
        <div class="col-md-9">
            <@include_page path="${siteCode}/api/GAPptjjfa/indexDocTypeAndChild" />
        </div>

        <div class="col-md-3">
            <@include_page path="${siteCode}/api/yfdt/indexDocList"/>
            <hr/>
            <br/>
            <@include_page path="${siteCode}/api/yfdt/indexDocList"/>
        </div>
    </div>

    <hr/>

    <div class="row">
        <div class="col-md-9">
            <@include_page path="${siteCode}/api/wsmxzGAPpt/indexWhySelectGap"/>
        </div>
        <div class="col-md-3">
            <div class="row">
                <div class="col-md-3"><img src="${rc.getContextPath()}/images/ewp/website/gap/help.png"/></div>
            </div>
            <div class="row">
                <div class="col-md-3">
                    <p>如果你有任何意见，请给我发邮件给我们，邮箱地址：<a href="#">gap@ufida.com.cn</a></p>
                </div>
            </div>
        </div>
    </div>
    </@layout.gap>
</#macro>
<#--gap默认新闻列表布局模板(对应)-->
<#macro gap_news currentLocation="GAP WebSite">
    <@layout.gap  currentLocation>
    <img src="${rc.getContextPath()}/images/ewp/website/gap/research.jpg"/>
    <hr/>
    <div class="row">
        <div class="col-md-3">
            <@include_page path="${siteCode}/api/gappt/gapNewsDocTypeAndChild"/>
        </div>

        <div class="col-md-9">
            <div class="row">
                <div class="col-md-5">
                    <@include_page path="${siteCode}/api/GAP-Tools/gapNewsDocList"/>
                </div>
                <div class="col-md-4">
                    <@include_page path="${siteCode}/api/GAP-Tools/gapNewsDocList"/>
                </div>
            </div>
            <hr/>
            <div class="row">
                <div class="col-md-5">
                    <@include_page path="${siteCode}/api/GAP-Apps/gapNewsDocList"/>
                </div>
                <div class="col-md-4">
                    <@include_page path="${siteCode}/api/GAP-Internet/gapNewsDocList"/>
                </div>
            </div>
        </div>
    </@layout.gap>
</#macro>
<#--gap默认新闻文章布局模板(对应布局1)-->
<#macro gap_news_document currentLocation="GAP WebSite">
    <@layout.gap  currentLocation>

        <br/>

        <div class="row">
            <div class="col-md-12">
                <h4>你现在的位置：首页 >> ${currentLocation}</h4>
            </div>
        </div>

        <div class="row">
            <hr class="col-md-12"/>
        </div>

        <div class="row">
            <div class="col-md-9">
                <div class="pagination-centered">
                    <h4>${(result.title)!""}</h4><br/>
                </div>
                <div class="well">
                ${(result.content )!""}
                </div>
                <@layout.gap_ewp_document_posts websiteCode = "${siteCode}" postDocID = "${(result.id)!''}"/>
            </div>

            <div class="col-md-3">
                <@include_page path="${siteCode}/api/kslj/gapNewsDocTypeAndChild"/>
                <hr/>
                <@include_page path="${siteCode}/api/jqdt/gapNewsDocTypeAndChild"/>
            </div>
        </div>

    </@layout.gap>
</#macro>

<#--gap默认研发动态布局模板(对应布局2)-->
<#macro gap_tabs currentLocation="GAP WebSite">
    <@layout.gap  currentLocation>

        <div class="row">
            <div class="col-md-9">
                <img src="${rc.getContextPath()}/images/ewp/website/gap/gap04.png"/>
            </div>
            <div class="col-md-3">
                <p class="well">${result.description}</p>
            </div>
        </div>

        <div class="row">
            <div class="col-md-9">
                <@include_page path="${siteCode}/api/${result.docTypeCode}/tabsDocType"/>
            </div>
            <div class="col-md-3">
                <@include_page path="${siteCode}/api/yfdt/gapNewsDocTypeAndChild"/>
            </div>
        </div>
    </@layout.gap>
</#macro>
