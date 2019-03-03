 <#-- 媒体新闻文章标题列表模板，主要调用doctitles.ftl来进行文章标题的展示，此模板嵌套在medianewsanddocs.ftl-->
<#import  "doctitles.ftl" as doctitle>
 <@doctitle.docTitles  result.pageResults  3 25 1/>
