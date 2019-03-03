<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<link rel='stylesheet' type='text/css' href='<%=request.getContextPath()%>/css/jquery/panel/dock.css' />
<link rel='stylesheet' type='text/css' href='<%=request.getContextPath()%>/css/template/style.css' />
<style type="text/css">
    .grid {
            font:40px Helvetica;
            color: #0099CC;
            text-align: center;
            margin: 0px;
            padding: 0px;
        }
</style>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/au/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/au/jquery/ui/jquery-ui-1.8.10.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/au/jquery/panel/jquery.dock.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/au/jquery/resize/jquery.ae.image.resize.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/au/template/style.js"></script>
<script type="text/javascript">
var json = '[{"x":18,"y":0,"w":108,"h":108,"fill":"rgba(153,204,255,0.7)"},{"x":144,"y":0,"w":1098,"h":108,"fill":"rgba(153,204,255,0.7)"},{"x":18,"y":126,"w":684,"h":108,"fill":"rgba(153,204,255,0.7)"},{"x":720,"y":126,"w":522,"h":342,"fill":"rgba(153,204,255,0.7)"},{"x":18,"y":792,"w":1224,"h":36,"fill":"rgba(153,204,255,0.7)"},{"x":18,"y":252,"w":288,"h":216,"fill":"rgba(153,204,255,0.7)"},{"x":324,"y":252,"w":378,"h":216,"fill":"rgba(153,204,255,0.7)"},{"x":18,"y":486,"w":1224,"h":180,"fill":"rgba(153,204,255,0.7)"},{"x":18,"y":684,"w":576,"h":90,"fill":"rgba(153,204,255,0.7)"},{"x":612,"y":684,"w":630,"h":90,"fill":"rgba(153,204,255,0.7)"}]';
var layouts;
jQuery(document).ready(function(){
      layouts = jQuery.parseJSON(json.replace(/(^\s*)|(\s*$)/g," "));
      for(var i in layouts){
	    var l = layouts[i];
	    var cssObj = {position:'absolute',left:l.x,top:l.y,width:l.w,height:l.h,border:'3px dashed #99CCFF'};
	    var divObj = '<div id="grid_'+i+'" class="grid">'+i+'</div>';
	    jQuery(divObj).appendTo('#layout').css(cssObj);
	    
	    jQuery("[id^=grid_]").mouseover(function(){
	        jQuery(this).css('backgroundColor','#FFFFCC');
	        jQuery('[id^=poition_]').css('zIndex',999);
	    }).mouseout(function(){
	        jQuery(this).css('backgroundColor','#fff');
	        jQuery('[id^=poition_]').css('zIndex',999);
	    });
	  }
	  
	  jQuery("div.poition_top, div.poition_right, div.poition_bottom, div.poition_left").dock({expansion: 200});

});
</script>
<title>Style Generator</title>
</head>
<body>
<div id="poition_top" class="poition_top sliderGallery" style="position:absolute;background-color:#cad8f3;width:560px;height:25px;"></div>
<div id="poition_right" class="poition_right sliderGalleryV" style="position:absolute;background-color:#cad8f3;height:400px;width:25px"></div>
<div id="poition_bottom" class="poition_bottom sliderGallery" style="position:absolute;background-color:#cad8f3;width:560px;height:25px;"></div>
<div id="poition_left" class="poition_left sliderGalleryV" style="position:absolute;background-color:#cad8f3;height:400px;width:25px;"></div>
<p id="lblMessage"></p>
<div id="layout" style="position:absolute"></div>
<div id="debug"></div>
</body>
</html>