<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<style type="text/css">

</style>
<link rel='stylesheet' type='text/css' href='<%=request.getContextPath()%>/css/ewp/tablemodel.css' />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/jquery-1.3.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/plugin/jquery.jeditable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery/plugin/jquery.xslt.js"></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/dwrewpxmlproxy/interface/XmlAnswerProxy.js'></script>  
<script type='text/javascript' src='<%=request.getContextPath()%>/dwrewpxmlproxy/engine.js'></script> 
<script type='text/javascript' src='<%=request.getContextPath()%>/dwrewpxmlproxy/util.js'></script>
<script>
jQuery(document).ready(function(){
    XmlAnswerProxy.XMLAnswer('','<%=gap.ewp.template.dummy.DummyXmlAnswerProxy.TABLEMODEL%>',function(data){
            jQuery('#tableModel').xslt(data,'./xslt/dummytablemodel.xsl',function(){
                //alert(jQuery('#tableModel').html());
                /*
                jQuery.each(jQuery('.modify'), function() {
			      jQuery(this).attr('contentEditable',true);
			    });
			    */
			    jQuery('.modifytext').editable(function(value, settings) {
			         alert(this);
                     alert(value);
                     alert(settings);
			    }, {
			         indicator : '<img src="images/ajaxList/ajax-loader2.gif">',
			         tooltip   : 'Click to edit...',
				     callback : function(value, settings) {
				         alert(this);
				         alert(value);
				         alert(settings);
				     }
			     });
			    jQuery('.modifytextarea').editable('', {
			         type:'textarea',
                     indicator : '<img src="images/ajaxList/ajax-loader2.gif">',
                     tooltip   : 'Click to edit...'
                 });
                jQuery('.modifyboolean').editable('', {
                     type:'select',
                     data   : " {'true':'True','false':'False','selected':'false'}",
                     indicator : '<img src="images/ajaxList/ajax-loader2.gif">',
                     tooltip   : 'Click to edit...'
                 });
            });   
        });
    
});
</script>
<title>Question Template</title>
</head>
<body>
    <div id = "tableModel"></div>    
</body>
</html>