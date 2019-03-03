<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/WEB-INF/gap-html.tld" prefix="venus" %>

<script type="text/javascript">
    var hiddenHandler = new HiddenHandler(60,15,216);
   var thisParentFrame = parent.document.getElementById("subMainFrameSet");
   
    function hiddenFuncTree(event) {
        var thisObj = event.srcElement?event.srcElement:event.target;
        var thisFuncTree = parent;
        if(thisObj.getAttribute("status1") == "show") {
            //thisFuncTree.subMainFrameSet.cols='0,25,*';
            hiddenHandler.hidden();
            thisObj.setAttribute("status1","hidden") ;
            thisObj.src = thisObj.getAttribute("hiddImage1");
            thisObj.title = "显示菜单";
        } else if(thisObj.getAttribute("status1") == "hidden") {
            //thisFuncTree.subMainFrameSet.cols='200,25,*';
            hiddenHandler.show();
            thisObj.setAttribute("status1","show") ;
            thisObj.src = thisObj.getAttribute("showImage1");
            thisObj.title = "隐藏菜单";
        }
    } 
    


            function doMouseOverImg(event) {
            var thisObj = event.srcElement?event.srcElement:event.target;
                if(thisObj.getAttribute("status1") == "show") {
                     thisObj.src = thisObj.getAttribute("showImage2");
                } else if(thisObj.getAttribute("status1") == "hidden") {
                        thisObj.src = thisObj.getAttribute("hiddImage2");
                }
        }
        function doMouseOutImg(event) {
            var thisObj = event.srcElement?event.srcElement:event.target;
            if(thisObj.getAttribute("status1") == "show") {
                thisObj.src = thisObj.getAttribute("showImage1");
            } else if(thisObj.getAttribute("status1") == "hidden") {
                thisObj.src = thisObj.getAttribute("hiddImage1");
            }
        }
        function HiddenHandler(totalTime, minTime, totalWidth) {
                this.totalTime = totalTime;
                this.totalWidth = totalWidth;
                this.minTime = minTime;
                this.currentWidth = 0;
                this.currentTime = 0;
                this.isInit = false;
                this.getWidth = function() {
                var difWidth = this.totalWidth * (this.minTime / this.totalTime);
                return difWidth;
        }
        this.hidden = function() {
        if(this.currentWidth <= 0 && !this.isInit) {
        this.currentWidth = this.totalWidth;
        this.isInit = true;
        }
        if(this.currentWidth > 0) {
        setTimeout("hiddenHandler.hidden()", this.minTime);
        }
        var thisWidth = this.currentWidth - this.getWidth();
        if(thisWidth <= 0) {
        thisWidth = 0;
        }
        doHiddenInstance(thisWidth);
        this.currentWidth = thisWidth;
        }
        this.show = function() {
        if(this.currentWidth >= totalWidth && !this.isInit) {
        this.currentWidth = 0;
        this.isInit = true;
        }
        if(this.currentWidth < this.totalWidth) {
        setTimeout("hiddenHandler.show()", this.minTime);
        }
        var thisWidth = this.currentWidth + this.getWidth();
        if(thisWidth >= this.totalWidth) {
        thisWidth = this.totalWidth;
        }
        doHiddenInstance(thisWidth);
        this.currentWidth = thisWidth;
        }
        }
        function doHiddenInstance(width) {
        thisParentFrame.cols = width + ",24,*";
        }


</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Toolsbar</title>
<link type="text/css" rel="stylesheet" id="leftcss"   href="<%=request.getContextPath()%>/themes/<venus:theme/>/css/index/style.css">
</head>
<body style="background-color:#ededed;">
<div class="control">
<img  onclick="hiddenFuncTree(event);" 
    style="cursor:pointer" status1="show"   hiddimage2="<%=request.getContextPath()%>/themes/<venus:theme/>/images/index/Arrow.png"
     hiddimage1="<%=request.getContextPath()%>/themes/<venus:theme/>/images/index/Arrow_right.png" 
     showimage2="<%=request.getContextPath()%>/themes/<venus:theme/>/images/index/Arrow_right.png" 
     showimage1="<%=request.getContextPath()%>/themes/<venus:theme/>/images/index/Arrow.png" 
     title="隐藏菜单"
     src="<%=request.getContextPath()%>/themes/<venus:theme/>/images/index/Arrow.png" />
     </div>
</body>
</html>