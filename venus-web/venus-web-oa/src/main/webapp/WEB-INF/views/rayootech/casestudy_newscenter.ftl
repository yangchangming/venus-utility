 <#--成功案例页面展示模板中，右侧新闻中心栏目及文章列表展示嵌套模板，此模板嵌套在layout.ftl的usecasestudy-->
   <h2 class="titie3"><span>${result.name!""}</span></h2>
                <ul class="rightNews ">
                   <@include_page path="${siteCode}/api/articles/${result.id}/newstitles"/>
                </ul>
                <div class="readMore"><a href="${rc.getContextPath()}${siteCode}/api/${result.docTypeCode}/news" class="more">&gt;更多</a></div>