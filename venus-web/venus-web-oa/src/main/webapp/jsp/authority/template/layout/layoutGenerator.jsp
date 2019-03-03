<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
<style type="text/css">
#popup {
    display: none;
    position: absolute;
    top: -100px;
    left: -100px;
    z-index: 100;
    background-color: #FFFFCC;
    border: none;
    padding: 0.5em;
}
</style>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/au/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/au/jquery/json/jquery.json-2.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/au/jquery/contextMenu/jquery.contextmenu.r2.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/au/template/layout.js"></script>
<script type="text/javascript">
function loaded(evt) {  
  var fileString = evt.target.result;
  jQuery.closeMenu();
  var jsons = jQuery.parseJSON(fileString.replace(/(^\s*)|(\s*$)/g," "));
  for(var i in jsons){
    var graphics = jsons[i];
    addRect(graphics.x, graphics.y, graphics.w, graphics.h, graphics.fill);
  }
}

function chooseLayout(f){
    var reader = new FileReader();
    reader.readAsText(f.files[0], "utf-8");
    reader.onload = loaded;
}

jQuery(document).ready(function(){
    init();
    jQuery('#layoutDesigner').contextMenu('layoutMenu', {
      bindings: {
        'save': function(t) {
          save();
        }
      }      
    });    
});
</script>
<title>Layout Generator</title>
</head>
<body>
<canvas id="layoutDesigner" style="border:1px solid #99CCFF;">
Fallback content, in case the browser does not support Canvas.
</canvas>
<div id="popup"></div>
<div id="debug"></div>
<div class="contextMenu" id="layoutMenu">
    <ul>
        <li id="open"><input id="filepicker" type="file" onchange="chooseLayout(this)" style="position:absolute; z-index:6; filter:alpha(opacity:0); opacity:0;"/><img src="<%=request.getContextPath()%>/images/au/folder.png" /> Open</li>
        <li id="save"><img src="<%=request.getContextPath()%>/images/au/disk.png" /> Save</li>
    </ul>
</div>
</body>
</html>