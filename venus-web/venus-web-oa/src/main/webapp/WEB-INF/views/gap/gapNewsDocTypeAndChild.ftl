<#--栏目及其子栏目展示模板-->
  <h4>${result.name!""}</h4><br/>
            <ul class="list-unstyled">
             <#if   result.validChildren ?has_content>
                  <#list   result.validChildren as child>
                     <li><a href="${rc.getContextPath()}${siteCode}/articles/${child.docTypeCode}"><i class="glyphicon-play"></i> ${child.name!""}</a>
                        <ul>
                           <#if child.validChildren ?has_content>
                               <#list child.validChildren as type>
                                  <li><a href="${rc.getContextPath()}${siteCode}/articles/${type.docTypeCode}">${type.name!""}</a></li>
                        
                               </#list>
                            </#if>
                        </ul>
                      </li><br/>
                  </#list>
             </#if>    
        </ul>