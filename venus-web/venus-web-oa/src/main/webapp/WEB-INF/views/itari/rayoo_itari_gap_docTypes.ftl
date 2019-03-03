<#--GAP平台介绍，包括GAP-Tools,INTERNET,APPS-->
<ul class="nav nav-tabs" id="myTab">
<#if result.validChildren ? has_content>
    <#list result.validChildren as docType>
        <li <#if docType_index==0>class="active"</#if>>
            <a data-toggle="tab" href="#${docType.docTypeCode}">${docType.name}</a>
        </li>
    </#list>
</#if>
</ul>

<div id="myTabContent" class="tab-content">
<#if result.validChildren ? has_content>
    <#list result.validChildren as docType>
        <div id="${docType.docTypeCode}" <#if docType_index==0>class="tab-pane fade in active"<#else>
             class="tab-pane fade"</#if> >
            <p>${docType.description ! ""}
                <@include_page path="${siteCode}/api/articles/${docType.id}/rayoo_itari_gap_list?pageSize=10"/>
            </p>
        </div>
    </#list>
</#if>
</div>
       
        
     