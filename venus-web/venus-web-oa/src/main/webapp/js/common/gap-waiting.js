/* huqi modify version */

// Global variables
var cellwide = false;
var isNav4, isIE, isDom;
isDom = true;
var isShowWait = false;
var showWaitButtons = ["installAction","button.save","button.start","button.stop","button.install","button.uninstall","button.new","button.delete"];


//document.write("<div id='progress' style='z-index:1000;position:absolute;display:none;top:200;left:200;border: 1px black solid;padding:3px 3px 3px 3px;background-color:#FFFFFF;font-family: sans-serif;font-size: 90%;vertical-align:text-top'><table><tr><td><object><embed src='"+dir_base+"/images/waiting.swf' quality='high' type='application/x-shockwave-flash' width='40' height='40'></embed></object></td><td valign='top'>请稍候...</td></tr></table></div>");
document.write("<div id='progress' style=\"z-index:1000;position:absolute;display:none;top:200;left:200;border: 1px black solid;padding:3px 3px 3px 3px;background-color:#FFFFFF;font-family: sans-serif;font-size: 90%\"><img src='"+dir_base+"/images/waiting.gif' align='texttop'>请稍候...</div>");

/*for tester*/
//document.onclick = showWait;
//document.onkeypress = showWait;

function showWait(event) {
	srcEle = event.srcElement?event.srcElement:event.target;
	top.isloaded = 1;
	if (srcEle.type.toLowerCase() != "button") {
		return false;
	}
	document.all["progress"].style.top = event.clientY-10;
	document.all["progress"].style.left = event.clientX-20;
	appInstallWaitShow();           
}

function hideWait() {
	appInstallWaitHide();
	enableAllButton();
}





function appInstallWaitShow() {
	document.all["progress"].style.display = "block";
}

function appInstallWaitHide() {
	document.all["progress"].style.display = "none";
}

function enableAllButton() {	
	var inputArr = document.getElementsByTagName("input");
    for(var j=0;j<inputArr.length;j++)
	{
		var frm = inputArr[j];
		if(frm.type.toLowerCase() == "button"){
			if(frm.alexmustdisabled != true)
				frm.disabled = false;
		}
	}
}

function disableAllButton() {
	var inputArr = document.getElementsByTagName("input");
    for(var j=0;j<inputArr.length;j++)
	{
		var frm = inputArr[j];
		if(frm.type.toLowerCase() == "button"){
			if(frm.disabled = true)
				frm.alexmustdisabled = true;
			else
				frm.disabled = true;
		}
	}
}

function showBlockPage() {
  var cover = document.all("coverMsg");
  if (cover == null) {
	  jQuery(document.forms[0]).append("<IFRAME id='coverMsg' style='position:absolute; left:0px; top:0px;' frameborder='0' src='" + dir_base +"/jsp/common/blank.jsp' scrolling='no'></IFRAME>");
	  cover = document.all("coverMsg");
  }
	
	cover.style.top = 0;
	cover.style.left = 0;
	cover.style.width = document.body.scrollWidth;
	cover.style.height = document.body.scrollHeight;
	cover.style.visibility = "visible";
}

function hideBlockPage() {
  if (document.all("coverMsg") != null) {
	document.all("coverMsg").style.visibility = "hidden";
  }
}