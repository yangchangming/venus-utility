<#--新闻中心右侧成功案例栏目名称以及文档标题列表模板，此模板嵌套在layout.ftl的usenews布局中-->
   <h2 class="titie4">
   <span class="readMore2 blueLink2"></span><a href="${rc.getContextPath()}${siteCode}/api/${result.docTypeCode}/casestudy">${result.name}</a></h2>
                <ul class="rightNews2">
                     <@include_page path="${siteCode}/api/articles/${result.id}/doctypedoctitles"/>
                </ul>