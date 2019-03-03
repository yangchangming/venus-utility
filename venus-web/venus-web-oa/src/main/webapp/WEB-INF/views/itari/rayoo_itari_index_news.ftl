<#--ITARI部门网站最新动态文章列表-->
<h3>
    <div style="float:left;"><img src="${rc.getContextPath()}/images/itari/itari/icon_01.jpg">  ${result.name ! ""}
           <span class="list-small">News</span></div>
    <div class="pull-right" style="float:right;">
        <a href="${rc.getContextPath()}${siteCode}/articles/${result.docTypeCode}">更多</a></div>
</h3>
<div style="float:left;">
    <hr style=" width:500px;"/>
</div>
<@include_page path="${siteCode}/api/articles/${result.id}/rayoo_itari_index_news_list"/>
                   
