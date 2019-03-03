<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/jsp/include/global.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>no power</title>
</head>
<body style="background-color:#f5f5f5;">
<div style="width:560px; height:200px; background-color:#f5f5f5; margin:100px auto;">
    <div style="width:320px; height:160px; float:left; padding:20px;">
        <fmt:bundle basename="udp.ewp.ewp_resource">
            <h4 style=" font-family:'微软雅黑', '宋体', Arial; font-size:18px;">
                <fmt:message key="udp.ewp.doctype.au.no_power"/>
            </h4>
            <p style="font-size:12px; color:#7f7f7f;">
                <fmt:message key="udp.ewp.doctype.au.no_power_description"/>
            </p>
        </fmt:bundle>
    </div>
</div>
</body>
</html>