<#--ITARI栏目首页中间内容模板，供itari.ftl调用-->
<#list result.pageResults.results as document>
 <div class="navLink2 greenLink">
                <ul>
                    <li class="navLinkFir2"><a href="#">USE首页</a></li>
                   <li><a href="/about_us/">联系我们</a></li>
                    <li><a href="#">${document.title}</a></li>
                </ul>
            <div class="clear"></div>
</div>
<div class="textContent">
                <h2 class="textTitle5">${document.title}</h2>
                ${document.content}
</div>
</#list>