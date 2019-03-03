<#--ITARI部门网站首页最新动态文章列表页模板-->
<#assign  index=0>
<#assign  size=6>
<#assign  titlelength=25>
<#assign  abstractlength=125>
<#assign docSize= result.pageResults.totalCount>
<#list  result.pageResults.results as doc>
    <#if index<size>
        <#if doc_index==0>
        <div class="list-unstyled ">
            <i class="bookmarking"></i>
            <h5><a href="${rc.getContextPath()}${siteCode}/article/${doc.id}">
                <#if doc.title?length   lt   titlelength>
                ${doc.title}
                <#else>
                ${doc.title[0..titlelength-1]}...
                </#if>  </a></h5>

            <p class="small">
                <#if doc.titelAbstract??>
                    <#if doc.titelAbstract?length lt abstractlength>
                        ${doc.titelAbstract}
                        <#else>
                        ${doc.titelAbstract[0..abstractlength-1]}......
                    </#if>
                </#if>
            </p>

            <div class="more"><a href="${rc.getContextPath()}${siteCode}/article/${doc.id}">详细内容</a></div>
        </div>
        <hr / style="width:500px;">
        <ul class="list-unstyled col-md-12">
        <#else>
            <li>
                <div style="float:left;"><a href="${rc.getContextPath()}${siteCode}/article/${doc.id}">
                    <#if  doc.title?length  lt   titlelength>
                    ${doc.title}
                    <#else>
                    ${doc.title[0..titlelength-1]}...
                    </#if>  </a></div>
                <div class="pull-right">  ${doc.publishTime?string('yyyy-MM-dd')}</div>
            </li>
            <br/>
        </#if>
        <#assign  index=index+1>
    </#if>
</#list>
<#-- 如果文章列表数量比较少，则输出空行进行填充-->
<#if  docSize <size>
    <#list  1..(size-docSize) as a>
        <li> 
            <div class="pull-right">  </div>
        </li>
        <br/>
    </#list>
</#if>
</ul>
  
