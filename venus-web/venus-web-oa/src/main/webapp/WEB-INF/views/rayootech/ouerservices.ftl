 <#-- 我们的服务展示模板，通过布局模板layout.ftl 中定义的useouerservice来进行数据填充
      我们的服务没有文章列表，其数据展示为我们的服务下子栏目的描述数据
   -->
<@layout.useouerservice "${result.name}">
                <h2 class="textTitle3">${result.name!""}</h2>
                 <#if   result.validChildren ?has_content>
                     <#list result.validChildren as temp>
                        <div class="subBox4">
                            <h3><a href="${rc.getContextPath()}${siteCode}/api/${temp.docTypeCode?default("")}/ouerservice_doctype">${temp.name}</a></h3>
                               <#if  temp.validChildren ?has_content>
                                    <#list  temp.validChildren as type>
                                              <ul class="rightLi">
                                        <li>
                                       <a href="${rc.getContextPath()}${siteCode}/api/${type.docTypeCode?default("")}/ouerservice_doctype">${type.name}</a>
                                        <p>${type.description}</p>
                                        </li>
                                    </ul>
                                      </#list>   
                                       <#else>
                                            <p>${temp.description}</p>
                                 </#if>  
                         
                        </div>
                  </#list>
                  </#if>
</@layout.useouerservice>