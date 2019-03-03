<#--ITARI部门网站首页技术方案模板-->
<h3>
    <div style="float:left;"><img src="${rc.getContextPath()}/images/itari/itari/icon_01.jpg">  ${result.name ! ""}
           <span>Technical
            solutions</span></div>
    <div class="pull-right" style="float:right;"><a
            href="${rc.getContextPath()}${siteCode}/articles/${result.docTypeCode}">更多</a></div>
</h3>
<div style="float:left;">
    <hr style=" width:500px;"/>
</div>
<@include_page  path="${siteCode}/api/articles/${result.id}/rayoo_itari_index_tech_list"/>
   
