<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="/jsp/include/global.jsp" %>
<html>
    <head>
        <title></title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    </head>
    <style type="text/css" media="screen"> 
        html, body  { height:100%; }
        body { margin:0; padding:0; overflow:auto; text-align:center; 
               background-color: #ffffff; }   
    </style>    
    <script language="javascript" src="<venus:base/>/js/au/swfobject.js"></script>
    <script type="text/javascript">
            var swfVersionStr = "10.0.0";
            var xiSwfUrlStr = "<venus:base/>/flex/expressInstall.swf";
            /**
        向Flex应用中传入的参数，示例：
                var flashvars = {}; 
                flashvars.parent_code = "109910040000000000100001";
                flashvars.return_type = "code";
                ====================================================================
        参数说明：
                query_organize_name:机构名称
                parent_code:机构树父节点code
                submit_all:节点是否全带checkbox（radio），1全带，0只有树的最末尾一层带，默认为0
                return_type:机构树返回值，可以选择aupartyrelation表的id、party_id和code三者之一，默认为code
                tree_level:控制树展示的层次，0 全部，1 公司，2 部门，3 岗位，-3 去除岗位，默认为0
                data_limit:是否控制数据权限，0 否，1 是，默认为0
                =====================================================================
            **/
            var flashvars = {}; 
            flashvars.query_organize_name = "";
            flashvars.parent_code = "<%=StringUtils.trimToEmpty(request.getParameter("parent_code"))%>";
            flashvars.submit_all = "1";
            flashvars.return_type = "code";
            flashvars.tree_level = "<%=StringUtils.trimToEmpty(request.getParameter("tree_level"))%>";
            flashvars.data_limit = "<%=StringUtils.trimToEmpty(request.getParameter("data_limit"))%>";
            var params = {};
            params.quality = "high";
            params.bgcolor = "#FFFFFF";
            params.allowscriptaccess = "sameDomain";
            params.allowfullscreen = "true";
            var attributes = {};
            attributes.id = "movies";
            attributes.name = "movies";
            attributes.align = "middle";
            swfobject.embedSWF(
                "<venus:base/>/flex/Richer.swf", "flashContent", 
                "100%", "100%", 
                swfVersionStr, xiSwfUrlStr, 
                flashvars, params, attributes);    
    </script>
    <body>
        <div id="flashContent">
            <p>
                To view this page ensure that Adobe Flash Player version 
                10.0.0 or greater is installed. 
            </p>
            <script type="text/javascript"> 
                var pageHost = ((document.location.protocol == "https:") ? "https://" : "http://"); 
                document.write("<a href='http://www.adobe.com/go/getflashplayer'><img src='" 
                                + pageHost + "www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /></a>" ); 
            </script> 
        </div>
    </body>
</html>