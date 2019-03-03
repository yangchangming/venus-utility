<#--ITARI部门网站首页技术方案文章列表页模板-->
<#assign  index=0>
<#assign   size=10>
<#assign   titlelength=25>
<#assign  docSize= result.pageResults.totalCount>
<div class="row">
    <i class="search"></i>
<ul class="unstyled col-md-7">
<#list   result.pageResults.results as doc>
    <#if index<=size>
        <#if doc_index==5>
        </ul>
        </div>
        <hr/>
        <div class="row">
            <i class="collaborate"></i>
        <ul class="unstyled col-md-7 ">
        <#else>
            <li style="line-height:35px;">
                <a href="${rc.getContextPath()}${siteCode}/article/${doc.id}">
                    <#if  doc.title?length   lt   titlelength>
                    ${doc.title}
                    <#else>
                    ${doc.title[0..titlelength-1]}...
                    </#if>
                </a>
            </li>
        </#if>
        <#assign  index=index+1>
    </#if>
</#list>
<#--如果文章列表数量比较少，则输出空行进行填充-->
<#if  docSize <size>
    <#list  1..(size-docSize) as a>
        <li>
               
        </li>
        <br/>
    </#list>
</#if>
</ul>
</div>
