<#--栏目二级页面　以多页签方式显示当然前栏目信息以及子栏目信息-->       
 <ul id="tab" class="nav nav-tabs">
 <#list result.parent as father>
        <#list  father.validChildren as child>
                     <#if result.docTypeCode == child.docTypeCode>
                          <li  class="active"><a href="#${child.docTypeCode!""}" data-toggle="tab">${child.name!""}</a></li>
                      <#else>
                          <li><a href="#${child.docTypeCode!""}" data-toggle="tab">${child.name!""}</a></li>
                      </#if>
       </#list>
  </#list>

 </ul>
 <div id="myTabContent" class="tab-content">
 <#list result.parent as father>
        <#list  father.validChildren as child>
    <#if   result.docTypeCode == child.docTypeCode>
    <div class="tab-pane fade in active"  id="${child.docTypeCode!""}">
        <p>${child.description!""}</p>
    </div>
    <#else>
    <div class="tab-pane fade" id="${child.docTypeCode!""}">
        <p>${child.description!""}</p>
    </div>
    </#if>
</#list>
</#list>
 </div>