<#--关于我们栏目首页中间内容模板，供about_us.ftl调用-->
<#list result.pageResults.results as document>
<div class="navLink">
   <ul>
        <li class="navLinkFir"><a href="/">USE首页</a></li>
        <li><a href="/about_us/">关于我们</a></li>
        <li><a href="#">${document.title}</a></li>
    </ul>
    <div class="clear"></div>
    <div class="textContent">
             <h2 class="textTitle">${document.title}</h2>
              ${document.content}
     </div>
</div>
</#list>