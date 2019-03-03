<#--Gap平台栏目以及子栏目展示进行展示模板-->
<h2><a href="${rc.getContextPath()}${siteCode}/articles/gappt">${result.name!""}</a></h2>
<#if   result.validChildren ?has_content>
    <#list  result.validChildren as type>
    <hr/>
    <div class="row">
        <div class="col-md-2">
            <img src="${rc.getContextPath()}/images/ewp/website/gap/gap0${type_index + 1}.png"/>
        </div>
        <div class="col-md-7">
            <div class="well">
                <h4>${type.name!""}</h4><br/>
                <p>${type.description!""}</p>
            </div>
        </div>
    </div>
    </#list>
</#if>
        