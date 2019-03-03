<#-- 文章标题展示宏定义，doctitles是宏的名字，参数datas是文章标题数据，size是需要展示多少条
    titlelength 表示标题的长度，hastime表示是否需要创建时间的展示 1表示需要，其他表示不需要
 -->
<#macro docTitles datas size titlelength hastime>
    <#assign index=0>
    <#list datas.results as doc>
        <#if index<size>
        <p>
            · &nbsp;
            <a href="${rc.getContextPath()}${siteCode}/article/${doc.id}">
                <#if  doc.title?length   lt   titlelength>
                ${doc.title}
                <#else>
                ${doc.title[0..titlelength-1]}...
                </#if>
                <#if hastime=1>
                ${doc.createTime?string('yyyy-MM-dd')}
                </#if>
            </a>
        </p>
        </#if>
        <#assign index=index+1>
    </#list>
</#macro>