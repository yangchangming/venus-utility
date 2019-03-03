<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<style type="text/css">

</style>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.3.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/plugin/jquery.xslt.js"></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/dwrewpxmlproxy/interface/XmlAnswerProxy.js'></script>  
<script type='text/javascript' src='<%=request.getContextPath()%>/dwrewpxmlproxy/engine.js'></script> 
<script type='text/javascript' src='<%=request.getContextPath()%>/dwrewpxmlproxy/util.js'></script>
<script>
jQuery(document).ready(function(){
    XmlAnswerProxy.XMLAnswer('','<%=gap.ewp.template.dummy.DummyXmlAnswerProxy.QUESTIONTOOL%>',function(data){
            jQuery('#questionTool').xslt(data,'./xslt/dummyquestion.xsl',function(){
                alert(jQuery('#questionTool').html());
            });   
        });    
});
</script>
<title>Question Template</title>
</head>
<body>
    <div id = "questionTool"></div>    
</body>
</html>