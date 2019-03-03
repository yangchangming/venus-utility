<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<style type="text/css">
body{
    font-family: arial;
    margin: 0px; /* for IE6 / IE7 */
}

/* make drag container visible */
#drag{
    border: 2px dashed LightBlue;
    display: table;
}

/* table */
div#drag table {
    background-color: #eee;
    border-collapse: collapse;
    margin: 7px;    
}

/* table cells */
div#drag td{
    border: 1px solid navy;
    height: 50px;
    text-align: center;
    font-size: 10pt;
    padding: 2px;
}

/* drag object (DIV inside table cell) */
.drag, .southdrag, .eastdrag, .alldrag{
    margin: auto;
    text-align: center;
    width: 87px;
    height: 35px;
    line-height: 35px;
    border: 2px solid SteelBlue;
    background-color: white;
}
</style>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.3.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/ewp/ewp-drag.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/plugin/jquery.xslt.js"></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/dwrewpxmlproxy/interface/XmlAnswerProxy.js'></script>  
<script type='text/javascript' src='<%=request.getContextPath()%>/dwrewpxmlproxy/engine.js'></script> 
<script type='text/javascript' src='<%=request.getContextPath()%>/dwrewpxmlproxy/util.js'></script>
<script>
function operateSql(){
    XmlAnswerProxy.XMLAnswer(sqlTextArea.value,'<%=gap.ewp.template.dummy.DummyXmlAnswerProxy.SQLANSWER%>',function(data){
            jQuery('#sqlAnswer').xslt(data,'./xslt/dummypainting.xsl',function(){
                //alert(jQuery('#sqlAnswer').html());   
                EWP.drag.init();                
            });   
        });    
}
</script>
<title>Paint Template</title>
</head>
<body>
    <div id = "sqlOperate"><textarea id = "sqlTextArea"></textarea><input type = "button" value = "operate" onclick = "javascript:operateSql();"></div>
    <div id = "sqlAnswer"></div>
    <div id = "templateGenerate"></div>
</body>
</html>