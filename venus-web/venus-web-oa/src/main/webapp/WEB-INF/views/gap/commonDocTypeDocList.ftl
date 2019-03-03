<#--通用的文章列表页-->
<@layout.gap  currentLocation='${result.name!""}'>
<img src="${rc.getContextPath()}/images/ewp/website/gap/research.jpg"/>
<hr/>

<div class="row">
    <div class="col-md-3">
        <#if result.validChildren ? has_content>
                 <@include_page path="${siteCode}/api/${result.docTypeCode}/gapNewsDocTypeAndChild"/>
             <#elseif result.parent ?has_content>
            <#list result.parent as type>
                <@include_page  path="${siteCode}/api/${type.docTypeCode}/gapNewsDocTypeAndChild"/>
            </#list>
        <#else>
        </#if>
    </div>
    <div class="col-md-9">
        <div class="row">
            <div class="col-md-6">
                <h4>${result.name!""}</h4>
            </div>
            <div class="pull-right">
                <h5>当前位置：首页 >> ${result.name!""}</h5>
            </div>
        </div>
    </div>

    <div class="col-md-9">
        <hr/>
    </div>

    <div class="col-md-9">
        <@layout.pagingTable docTypeId=result.id pageSize=2/>
    </div>
</div>
</@layout.gap>