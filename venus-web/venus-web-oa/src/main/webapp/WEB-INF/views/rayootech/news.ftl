<#--新闻中心展示页面模板，此模板采用layout.ftl中得usenews布局模板，对其数据进行填充-->
<@layout.usenews "${result.name}">
<h2 class="textTitle11">${result.name!""}</h2>
<div class="subBox6">
    <@layout.pagingTable docTypeId=result.id pageSize=10/>
</div>
</@layout.usenews>