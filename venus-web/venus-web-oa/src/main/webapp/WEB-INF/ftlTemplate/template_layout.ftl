<#--默认首页布局模板-->
<#macro index currentLocation="myweb">
<!DOCTYPE html>
<html lang="en">
    <#include ".${siteCode}/include/head.ftl">
<body>
<div style="width: 1000px;margin: auto;">
    <#include ".${siteCode}/include/header.ftl">
    <#nested />
    <hr/>
    <#include  ".${siteCode}/include/footer.ftl">
</div>
<!-- /container -->
</body>
</html>
</#macro>