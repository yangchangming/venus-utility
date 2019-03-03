<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ include file="/jsp/include/global.jsp" %>
<%@ page import="venus.frames.i18n.util.LocaleHolder" %>
    <head>
        <script type="text/javascript">
            function changeUrl(url, bodyUrl) {
                window.parent.frames["left"].frames["leftFrameSet"].location.href = url;
                window.parent.frames["left"].frames["bodyFrame"].location.href = bodyUrl;
            }
        </script>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <title>Header</title>
        <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/themes/<venus:theme/>/css/index/style.css"/>
    </head>
    <body>
    <div class="header">
        <div class="logo"><img src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/venusPage/logo.png"/></div>
        <div class="menu">
            <div class="nav">
                <ul class="nav_content">
                    <li><a href="javascript:void(0)"
                           onclick="changeUrl('<%=request.getContextPath()%>/jsp/venusPage/index/simpleTemplate.jsp','<%=request.getContextPath()%>/TemplateAction.do?cmd=queryAll');">单表列表控件</a>
                    </li>
                    <li><a href="javascript:void(0)"
                           onclick="changeUrl('<%=request.getContextPath()%>/jsp/venusPage/index/bookstore.jsp','<%=request.getContextPath()%>/BookStore.do?cmd=queryAllBooks');">网上书店</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    </body>
</html>
