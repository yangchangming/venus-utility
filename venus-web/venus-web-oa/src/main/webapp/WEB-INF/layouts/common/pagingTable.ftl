<#macro pagingTable docTypeId pageSize=15>
    <@include_page path="/${siteCode}/api/articles/${docTypeId}?pageSize=${pageSize}"/>
</#macro>